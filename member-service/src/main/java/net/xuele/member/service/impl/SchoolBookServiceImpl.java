package net.xuele.member.service.impl;


import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.common.utils.StringUtils;
import net.xuele.common.dto.ClassInfoDTO;
import net.xuele.common.exceptions.MemberException;
import net.xuele.common.utils.MemberAssert;
import net.xuele.member.context.MemberLogicContext;
import net.xuele.member.domain.CtBook;
import net.xuele.member.domain.DBooks;
import net.xuele.member.domain.MSchoolBook;
import net.xuele.member.dto.CtBookDTO;
import net.xuele.member.dto.ExtraBookDTO;
import net.xuele.member.dto.SchoolBookDTO;
import net.xuele.member.dto.SchoolDTO;
import net.xuele.member.persist.CtAreaTextbookMapper;
import net.xuele.member.persist.CtBookMapper;
import net.xuele.member.persist.MSchoolBookMapper;
import net.xuele.member.service.*;
import net.xuele.member.util.ClassNameUtil;
import net.xuele.member.util.NumberUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by kaike.du on 2015/7/16 0016.
 */

public class SchoolBookServiceImpl implements SchoolBookService {
    @Autowired
    private MSchoolBookMapper mSchoolBookMapper;
    @Autowired
    private CtBookMapper ctBookMapper;
    @Autowired
    private CtAreaTextbookMapper ctAreaTextbookMapper;
    @Autowired
    private SeqGeneratorService seqGeneratorService;
    @Autowired
    private ExtraBookService extraBookService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private TeacherService teacherService;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public SchoolBookDTO saveBook(String bookId, String schoolId) {
        MSchoolBook mSchoolBook = mSchoolBookMapper.selectByPrimaryKey(schoolId, bookId);
        MemberAssert.isNull(mSchoolBook, "学校已有该教材", MemberLogicContext.ERROR_CODE_SCHOOL_BOOK_IS_EXIST);
        CtBook theCtBook = ctBookMapper.queryByPrimaryKey(bookId);
        List<CtBook> ctBooks = ctBookMapper.querySchoolBook(schoolId);
        for (CtBook ctBook : ctBooks) {
            if ((theCtBook.getGrade().equals(ctBook.getGrade())) && (theCtBook.getSubjectId().equals(ctBook.getSubjectId()))) {
                if (theCtBook.getEditionId().equals(ctBook.getEditionId())) {
                    break;
                } else {
                    MemberAssert.isTrue(theCtBook.getEditionId().equals(ctBook.getEditionId()),
                            "同一科目只能设置同一版本的两本教材", MemberLogicContext.ERROR_CODE_SCHOOL_BOOK_EDITION_IS_DIFFERENT);
                }
            }
        }
        SchoolBookDTO schoolBookDTO = new SchoolBookDTO();
        schoolBookDTO.setBookId(bookId);
        schoolBookDTO.setBookName(theCtBook.getBookName());
        schoolBookDTO.setSchoolId(schoolId);
        schoolBookDTO.setUpdateTime(new Date());
        mSchoolBookMapper.insert(parseToMSchoolBook(schoolBookDTO));
        //获取该课本有多少教辅
        List<ExtraBookDTO> ebList = extraBookService.queryByBookId(bookId);
        schoolBookDTO.setExtraBookCount(CollectionUtils.isNotEmpty(ebList) ? ebList.size() : 0);
        return schoolBookDTO;
    }

    @Override
    public SchoolBookDTO deleteBook(String bookId, String schoolId) {
        SchoolBookDTO schoolBookDTO = new SchoolBookDTO();
        schoolBookDTO.setBookId(bookId);
        schoolBookDTO.setSchoolId(schoolId);
        mSchoolBookMapper.deleteByPrimaryKey(schoolId, bookId);
        return schoolBookDTO;
    }

    /**
     * 保存该学校的同步课堂教材
     */
    @Override
    public int saveSyncBook(ArrayList<String> bookIds, String schoolId) {
        int in = 0;
        //1、去掉空内容
        List<String> strlist = new ArrayList<>();
        strlist.add("");
        bookIds.removeAll(strlist);
        //2、判断是否上下册对应
        judgeSynBook(bookIds);
        //3、保存
        Date now = new Date();
        List<MSchoolBook> schoolBookList = new ArrayList<>();
        for (String bookId : bookIds) {
            MSchoolBook sbook = new MSchoolBook();
            sbook.setBookId(bookId);
            sbook.setSchoolId(schoolId);
            sbook.setUpdateTime(now);
            sbook.setId(seqGeneratorService.generate("m_school_sync_class"));
            schoolBookList.add(sbook);
        }
        if (CollectionUtils.isNotEmpty(schoolBookList)) {
            try {
                mSchoolBookMapper.delSyncBookBySchoolId(schoolId);
                in = mSchoolBookMapper.saveSyncBook(schoolBookList);
            } catch (Exception e) {
                logger.error("保存同步课堂失败");
                throw new MemberException("保存同步课堂失败");
            }
        }
        return in;
    }

    //判断同步课堂教材是否上下册对应
    private boolean judgeSynBook(List<String> bookIds) {
        if (CollectionUtils.isEmpty(bookIds)) {
            return true;
        }
        List<DBooks> booksList = mSchoolBookMapper.queryDBooks(bookIds);
        if (CollectionUtils.isEmpty(booksList)) {
            return true;
        }
        Map<String, List<DBooks>> map = new HashMap<>();
        for (DBooks dBooks : booksList) {
            String key = dBooks.getSubjectId() + "_" + dBooks.getGradeNum();
            List<DBooks> pair = map.get(key);
            if (pair == null) {//不存在，则put，不会有错
                pair = new ArrayList<>(2);
                pair.add(dBooks);
                map.put(key, pair);
            } else {//存在则要判断
                if (pair.size() == 1) {
                    DBooks one = pair.get(0);
                    //必须同一版本，并是上下册
                    if (one.getEditionId().equals(dBooks.getEditionId()) && !one.getSemester().equals(dBooks.getSemester())) {
                        pair.add(dBooks);
                    } else {
                        throw new MemberException("同一科目只能设置同一版本的同步教材," +
                                NumberUtil.getSimpleNumber(dBooks.getGradeNum()) + "年级" + dBooks.getBookName() + "设置错误");
                    }
                } else {//一个年级只能有两本上下册
                    throw new MemberException("同一科目只能设置同一版本的同步教材," +
                            NumberUtil.getSimpleNumber(dBooks.getGradeNum()) + "年级" + dBooks.getBookName() + "设置错误");
                }
            }
        }
        return true;
    }

    @Override
    public SchoolBookDTO updateBook(String oldBookId, String newBookId, String schoolId) {
        MSchoolBook mSchoolBook = mSchoolBookMapper.selectByPrimaryKey(schoolId, newBookId);
        MemberAssert.isNull(mSchoolBook, "学校已有该教材", MemberLogicContext.ERROR_CODE_SCHOOL_BOOK_IS_EXIST);
        CtBook theCtBook = ctBookMapper.queryByPrimaryKey(newBookId);
        List<CtBook> ctBooks = ctBookMapper.querySchoolBook(schoolId);
        for (CtBook ctBook : ctBooks) {
            if ((theCtBook.getGrade().equals(ctBook.getGrade())) && (theCtBook.getSubjectId().equals(ctBook.getSubjectId()))) {
                if (theCtBook.getEditionId().equals(ctBook.getEditionId())) {
                    break;
                } else {
                    MemberAssert.isTrue(theCtBook.getEditionId().equals(ctBook.getEditionId()),
                            "同一科目只能设置同一版本的两本教材", MemberLogicContext.ERROR_CODE_SCHOOL_BOOK_EDITION_IS_DIFFERENT);
                }
            }
        }
        SchoolBookDTO schoolBookDTO = new SchoolBookDTO();
        schoolBookDTO.setBookId(newBookId);
        schoolBookDTO.setSchoolId(schoolId);
        schoolBookDTO.setUpdateTime(new Date());
        mSchoolBookMapper.updateBook(oldBookId, newBookId, schoolId);
        return schoolBookDTO;
    }

    /**
     * 获取所有同步课堂所有教材，不按学校分
     */
    @Override
    public List<CtBookDTO> queryAllSynBook() {
        List<DBooks> booksList = mSchoolBookMapper.queryAllSynBook();
        return parseToDTO(booksList);
    }


    /**
     * 根据学校ID获取该学校设置的同步课堂教材
     */
    @Override
    public List<CtBookDTO> querySchoolSynBook(String schoolId) {
        List<DBooks> booksList = mSchoolBookMapper.querySchoolSynBook(schoolId);
        return parseToDTO(booksList);
    }

    private List<CtBookDTO> parseToDTO(List<DBooks> booksList) {
        List<CtBookDTO> bookDTOList = new ArrayList<>();
        for (DBooks book : booksList) {
            CtBookDTO bookDTO = new CtBookDTO();
            BeanUtils.copyProperties(book, bookDTO);
            bookDTO.setGrade(book.getGradeNum());
            bookDTO.setSubjectId(String.valueOf(book.getSubjectId()));
            bookDTOList.add(bookDTO);
        }
        return bookDTOList;
    }

    @Override
    public List<CtBookDTO> queryDefaultDBooks() {
        List<DBooks> dBooksList = mSchoolBookMapper.queryDefaultDBooks();
        return parseToDTO(dBooksList);
    }

    @Override
    public List<CtBookDTO> saveSchoolBook(String schoolId) {
        List<CtBook> booksList = ctBookMapper.querySchoolBook(schoolId);
        if (booksList.size() != 0) {
            List<CtBookDTO> bookDTOList = new ArrayList<>();
            for (CtBook book : booksList) {
                CtBookDTO bookDTO = new CtBookDTO();
                BeanUtils.copyProperties(book, bookDTO);
                bookDTOList.add(bookDTO);
            }
            return bookDTOList;
        } else {
            SchoolDTO schoolDTO = schoolService.getBySchoolId(schoolId);
            if (schoolDTO == null) {
                throw new MemberException(schoolId + "学校不存在或已经被删除");
            }
            String areaId = schoolDTO.getArea();
            List<CtBook> ctBooks = ctAreaTextbookMapper.queryAreaBook(areaId);
            if (ctBooks == null || ctBooks.isEmpty()) {
                ctBooks = ctBookMapper.queryDefaultBook();
            }
            List<CtBookDTO> bookDTOList = new ArrayList<>();
            Date now = new Date();
            List<MSchoolBook> schoolBookList = new ArrayList<>();
            for (CtBook ctBook : ctBooks) {
                CtBookDTO bookDTO = new CtBookDTO();
                BeanUtils.copyProperties(ctBook, bookDTO);
                bookDTOList.add(bookDTO);
                MSchoolBook sbook = new MSchoolBook();
                sbook.setBookId(ctBook.getBookId());
                sbook.setSchoolId(schoolId);
                sbook.setUpdateTime(now);
                sbook.setId(seqGeneratorService.generate("m_school_book"));
                schoolBookList.add(sbook);
            }
            mSchoolBookMapper.saveSchoolBook(schoolBookList);
            return bookDTOList;
        }
    }

    /**
     * 根据学校ID获取该学校设置的教材
     */
    @Override
    public List<CtBookDTO> querySchoolBook(String schoolId) {
        List<CtBook> booksList = ctBookMapper.querySchoolBook(schoolId);
        List<CtBookDTO> bookDTOList = new ArrayList<>();
        for (CtBook book : booksList) {
            CtBookDTO bookDTO = new CtBookDTO();
            BeanUtils.copyProperties(book, bookDTO);
            bookDTOList.add(bookDTO);
        }
        return bookDTOList;
    }

    @Override
    public CtBookDTO getCtBook(Integer gradeCode, String subjectId, String schoolId) {
        if (gradeCode == null || gradeCode == 0 || StringUtils.isEmpty(subjectId)) {
            return null;
        }
        if (StringUtils.isEmpty(schoolId)) {
            schoolId = null;
        }
        Integer semester = ClassNameUtil.getSemester();
        CtBook ctBook = ctBookMapper.getCtBook(gradeCode, subjectId, schoolId, semester);
        if (ctBook == null) {
            return null;
        }
        CtBookDTO ctBookDTO = new CtBookDTO();
        BeanUtils.copyProperties(ctBook, ctBookDTO);
        return ctBookDTO;
    }

    /**
     * 查询老师的积分宝教材信息
     *
     * @param userId   用户id
     * @param schoolId 学校id
     */
    @Override
    public List<CtBookDTO> queryTeacherSyncBook(String userId, String schoolId) {
        //查询老师的授课科目
        List<CtBookDTO> teachBookList = teacherService.queryBookDTO(userId);
        ArrayList<String> subjectIdList = new ArrayList<>();
        for (CtBookDTO bookDTO : teachBookList) {
            if (!subjectIdList.contains(bookDTO.getSubjectId())) {
                subjectIdList.add(bookDTO.getSubjectId());
            }
        }
        //查询老师的授课年级
        List<ClassInfoDTO> teachClassList = teacherService.queryTeacherClass(userId);
        ArrayList<Integer> gradeCodeList = new ArrayList<>();
        for (ClassInfoDTO cidto : teachClassList) {
            if (!gradeCodeList.contains(cidto.getGradeNum())) {
                gradeCodeList.add(cidto.getGradeNum());
            }
        }
        //查询当前学期
        ArrayList<Integer> semesterList = new ArrayList<>();
        semesterList.add(ClassNameUtil.getSemester());
        List<CtBookDTO> bookList = queryCtBook(schoolId, gradeCodeList, subjectIdList, semesterList);
        return bookList;
    }

    /**
     * APP接口：查询某学校，某些年级，某些科目，某些学期的教材信息(组合结果)，没有设置则返回默认的教材信息
     *
     * @param schoolId  学校id
     * @param gradeCode 年级id列表
     * @param subjectId 科目id列表
     * @param sem       学期列表
     * @return 教材信息
     */
    @Override
    public List<CtBookDTO> queryCtBook(String schoolId, ArrayList<Integer> gradeCode, ArrayList<String> subjectId, ArrayList<Integer> sem) {
        if (StringUtils.isEmpty(schoolId) || CollectionUtils.isEmpty(gradeCode) || CollectionUtils.isEmpty(subjectId) || CollectionUtils.isEmpty(sem)) {
            return new ArrayList<>();
        }

        ArrayList<Integer> semester = new ArrayList<>();
        //如果是9年级则获取上下两册
        if (gradeCode.contains(9)) {
            semester.add(1);
            semester.add(2);
        } else {
            semester = (ArrayList<Integer>) sem.clone();
        }

        Map<String, CtBook> map = new HashMap<>();
        //查询获取默认教材
        List<CtBook> defaultBookList = ctBookMapper.queryDefaultBookList(gradeCode, subjectId, semester);
        for (CtBook book : defaultBookList) {
            map.put(book.getSubjectId() + "_" + book.getGrade() + "_" + book.getSemester(), book);
        }
        //区域教材
        SchoolDTO schoolDTO = schoolService.getBySchoolId(schoolId);
        if (StringUtils.isNotEmpty(schoolDTO.getArea())) {
            List<CtBook> areaBookList = ctAreaTextbookMapper.queryAreaBookList(schoolDTO.getArea(), gradeCode, subjectId, semester);
            for (CtBook book : areaBookList) {
                map.put(book.getSubjectId() + "_" + book.getGrade() + "_" + book.getSemester(), book);
            }
        }
        //查询学校已经设置的教材信息
        List<CtBook> ctBookList = ctBookMapper.queryCtBookByList(schoolId, gradeCode, subjectId, semester);
        for (CtBook book : ctBookList) {
            map.put(book.getSubjectId() + "_" + book.getGrade() + "_" + book.getSemester(), book);
        }
        List<CtBook> list = new ArrayList<>(map.values());

        //如果是9年级则获取上下两册，其他年级根据学期获取
        if (gradeCode.contains(9)) {
            if (sem.contains(1) && !sem.contains(2)) {//包含9年级，但是学期没有第二学期，则删除9年级外的其他年级的第二学期课本
                list = getRightBook(list, 2);
            } else if (!sem.contains(1) && sem.contains(2)) {//包含9年级，但是学期没有第一学期，则删除9年级外的其他年级的第一学期课本
                list = getRightBook(list, 1);
            }
        }
        if(CollectionUtils.isEmpty(list)){
            return new ArrayList<>();
        }

        //按subject_id,grade,semester排序
        Collections.sort(list, new Comparator<CtBook>() {
            @Override
            public int compare(CtBook book1, CtBook book2) {
                if (book1.getSubjectId().equals(book2.getSubjectId())) {
                    if (book1.getGrade().equals(book2.getGrade())) {
                        if (book1.getSemester().equals(book2.getSemester())) {
                            return 0;
                        } else if (book1.getSemester().compareTo(book2.getSemester()) > 0) {
                            return 1;
                        } else {
                            return -1;
                        }
                    } else if (book1.getGrade().compareTo(book2.getGrade()) > 0) {
                        return 1;
                    } else {
                        return -1;
                    }
                } else if (book1.getSubjectId().compareTo(book2.getSubjectId()) > 0) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        List<CtBookDTO> bookDTOList = new ArrayList<>();
        for (CtBook book : list) {
            CtBookDTO bookDTO = new CtBookDTO();
            BeanUtils.copyProperties(book, bookDTO);
            bookDTOList.add(bookDTO);
        }
        if (logger.isInfoEnabled()) {
            logger.info("schoolId:{},gradeCode:,subjectId:{},semester{}", schoolId, gradeCode, subjectId, semester);
            StringBuilder buf = new StringBuilder("queryCtBook()return:");
            for (CtBookDTO ctBookDTO : bookDTOList) {
                buf.append(ctBookDTO.getBookName()).append("|");
            }
            logger.info(buf.toString());
        }
        return bookDTOList;
    }

    private List<CtBook> getRightBook(List<CtBook> list, int period) {
        List<CtBook> bookList = new ArrayList<>();
        for (CtBook book : list) {
            if (book.getGrade() == 9 || book.getSemester() != period) {
                bookList.add(book);
            }
        }
        return bookList;
    }

    @Override
    public CtBookDTO getDBook(Integer gradeCode, Integer subjectId, String schoolId) {
        if (gradeCode == null || gradeCode == 0 || subjectId == null || subjectId == 0) {
            return null;
        }
        if (StringUtils.isEmpty(schoolId)) {
            schoolId = null;
        }
        Integer semester = ClassNameUtil.getSemester();
        DBooks dBook = mSchoolBookMapper.getDBook(gradeCode, subjectId, schoolId, semester);
        if (dBook == null) {
            dBook = getADefaultBook(gradeCode, subjectId);
        }
        if (dBook == null) {
            return null;
        }
        CtBookDTO ctBookDTO = new CtBookDTO();
        BeanUtils.copyProperties(dBook, ctBookDTO);
        //字段名字不一样
        ctBookDTO.setGrade(dBook.getGradeNum());
        return ctBookDTO;
    }

    /**
     * APP接口：查询某学校，某些年级，某些科目，某些学期的同步课堂教材信息
     */
    @Override
    public List<CtBookDTO> queryDBook(String schoolId, ArrayList<Integer> gradeCode, ArrayList<String> subjectId, ArrayList<Integer> semester) {
        if (StringUtils.isEmpty(schoolId) || CollectionUtils.isEmpty(gradeCode) || CollectionUtils.isEmpty(subjectId) || CollectionUtils.isEmpty(semester)) {
            return new ArrayList<>();
        }
        List<DBooks> booksList = mSchoolBookMapper.queryDBooksByList(schoolId, gradeCode, subjectId, semester);
        Map<String, DBooks> map = new HashMap<>();
        for (DBooks book : booksList) {
            map.put(book.getSubjectId() + "_" + book.getGradeNum() + "_" + book.getSemester(), book);
        }
        //查询学校没有设置的教材信息，获取默认教材
        List<DBooks> defaultBookList = mSchoolBookMapper.queryDefaultBookList(gradeCode, subjectId, semester);
        //学校设置的+默认教材
        for (DBooks book : defaultBookList) {
            if (map.get(book.getSubjectId() + "_" + book.getGradeNum() + "_" + book.getSemester()) == null) {
                booksList.add(book);
            }
        }
        if (CollectionUtils.isEmpty(booksList)) {
            return new ArrayList<>();
        }
        //按subjectId,gradeNum,semester排序
        Collections.sort(booksList, new Comparator<DBooks>() {
            @Override
            public int compare(DBooks book1, DBooks book2) {
                if (book1.getSubjectId().equals(book2.getSubjectId())) {
                    if (book1.getGradeNum().equals(book2.getGradeNum())) {
                        if (book1.getSemester().equals(book2.getSemester())) {
                            return 0;
                        } else if (book1.getSemester().compareTo(book2.getSemester()) > 0) {
                            return 1;
                        } else {
                            return -1;
                        }
                    } else if (book1.getGradeNum().compareTo(book2.getGradeNum()) > 0) {
                        return 1;
                    } else {
                        return -1;
                    }
                } else if (book1.getSubjectId().compareTo(book2.getSubjectId()) > 0) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        List<CtBookDTO> bookDTOList = new ArrayList<>();
        for (DBooks book : booksList) {
            CtBookDTO bookDTO = new CtBookDTO();
            BeanUtils.copyProperties(book, bookDTO);
            bookDTO.setGrade(book.getGradeNum());
            bookDTOList.add(bookDTO);
        }
        return bookDTOList;
    }

    @Override
    public void updateExtraBookId(String bookId, String extraBookId, String schoolId) {
        mSchoolBookMapper.setExtraBookId(bookId, extraBookId, schoolId);
    }

    /**
     * WEB平台、APP接口：根据bookId和schoolId获取学校课本关联表信息
     */
    @Override
    public SchoolBookDTO getByBookIdAndSchoolId(String bookId, String schoolId) {
        MSchoolBook schoolBook = mSchoolBookMapper.selectByPrimaryKey(schoolId, bookId);
        if (schoolBook == null) {
            return null;
        }
        SchoolBookDTO schoolBookDTO = new SchoolBookDTO();
        BeanUtils.copyProperties(schoolBook, schoolBookDTO);
        return schoolBookDTO;
    }

    /**
     * 取默认同步课堂课本
     * editionId:1
     */
    private DBooks getADefaultBook(Integer gradeCode, Integer subjectId) {
        return mSchoolBookMapper.getByGradeNumAndSubjectId(gradeCode, subjectId);
    }

    private MSchoolBook parseToMSchoolBook(SchoolBookDTO schoolBookDTO) {
        MSchoolBook mSchoolBook = new MSchoolBook();
        BeanUtils.copyProperties(schoolBookDTO, mSchoolBook);
        mSchoolBook.setId(seqGeneratorService.generate("m_school_book"));
        return mSchoolBook;
    }

}
