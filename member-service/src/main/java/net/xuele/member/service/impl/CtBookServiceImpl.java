package net.xuele.member.service.impl;


import net.xuele.common.exceptions.MemberException;
import net.xuele.member.domain.CtBook;
import net.xuele.member.dto.BookDTO;
import net.xuele.member.dto.CtBookDTO;
import net.xuele.member.persist.CtBookMapper;
import net.xuele.member.persist.MSchoolBookMapper;
import net.xuele.member.service.CtBookService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhongjian.xu on 2015/7/9 0009.
 */

public class CtBookServiceImpl implements CtBookService {

    @Autowired
    private CtBookMapper bookMapper;
    @Autowired
    private MSchoolBookMapper schoolBookMapper;

    @Override
    public List<CtBookDTO> queryBookByGradeCode(String gradeCode, String subjectId) {
        if (StringUtils.isEmpty(gradeCode) || StringUtils.isEmpty(subjectId)) {
            return null;
        }
        List<CtBook> books = bookMapper.selectBookByGradeCode(gradeCode, subjectId);
        if (books == null) {
            return null;
        }
        List<CtBookDTO> results = new ArrayList<>(books.size());
        for (CtBook book : books) {
            CtBookDTO bookDTO = new CtBookDTO();
            BeanUtils.copyProperties(book, bookDTO);
            results.add(bookDTO);
        }
        return results;
    }

    @Override
    public List<CtBookDTO> queryBookByGradeCode(String gradeCode, String subjectId, String schoolId) {
        if (StringUtils.isEmpty(gradeCode) || StringUtils.isEmpty(subjectId)) {
            return null;
        }
        List<CtBook> ctBooks = schoolBookMapper.getByGradeAndSubjectId(Integer.parseInt(gradeCode), subjectId, schoolId);
        if (ctBooks.size() == 2) {
            return new ArrayList<>(0);
        } else if (ctBooks.size() == 1) {
            List<CtBook> books = bookMapper.queryCtBook(ctBooks.get(0).getGrade(), subjectId, ctBooks.get(0).getEditionId());
            List<CtBookDTO> ctBookDTOs = new ArrayList<>();
            if (books.size() == 1) {
                return new ArrayList<>(0);
            } else if (books.size() == 2) {
                for (CtBook ctBook : books) {
                    if (!ctBook.getBookId().equals(ctBooks.get(0).getBookId())) {
                        CtBookDTO ctBookDTO = new CtBookDTO();
                        BeanUtils.copyProperties(ctBook, ctBookDTO);
                        ctBookDTOs.add(ctBookDTO);
                    }
                }
            }
            return ctBookDTOs;
        } else if (ctBooks.size() == 0) {
            List<CtBook> ctBookList = bookMapper.selectBookByGradeCode(gradeCode, subjectId);
            if (ctBookList == null) {
                return null;
            }
            List<CtBookDTO> results = new ArrayList<>(ctBooks.size());
            for (CtBook book : ctBookList) {
                CtBookDTO bookDTO = new CtBookDTO();
                BeanUtils.copyProperties(book, bookDTO);
                results.add(bookDTO);
            }
            return results;
        } else {
            throw new MemberException("某科目某年级最多只有两册书");
        }

    }

    ///**
    // * APP接口：查询每个年级有哪些科目
    // */
    //@Override
    //public List<BookDTO> queryGradeSubject() {
    //    List<CtBook> bookList = bookMapper.selectSubject();
    //    List<BookDTO> bookDTOList = new ArrayList<>();
    //    for (CtBook book : bookList) {
    //        BookDTO bookdto = new BookDTO();
    //        BeanUtils.copyProperties(book, bookdto);
    //        bookdto.setGradeName(NumberUtil.getSimpleNumber(book.getGrade()) + "年级");
    //        bookDTOList.add(bookdto);
    //    }
    //    return bookDTOList;
    //}

    /**
     * APP接口：获取某科目的教材
     */
    @Override
    public List<BookDTO> queryBookBySubjectId(String subjectId) {
        if (StringUtils.isEmpty(subjectId)) {
            return null;
        }
        List<CtBook> bookList = bookMapper.queryBookBySubjectId(subjectId);
        if (bookList == null) {
            return null;
        }
        List<BookDTO> bookAppDTOList = new ArrayList<>();
        for (CtBook book : bookList) {
            BookDTO bookdto = new BookDTO();
            BeanUtils.copyProperties(book, bookdto);
            bookAppDTOList.add(bookdto);
        }
        return bookAppDTOList;
    }

    @Override
    public CtBookDTO getByBookId(String bookId) {
        if (StringUtils.isEmpty(bookId)) {
            return null;
        }
        CtBook ctBook = bookMapper.queryByPrimaryKey(bookId);
        if (ctBook == null) {
            return null;
        }
        CtBookDTO ctBookDTO = new CtBookDTO();
        BeanUtils.copyProperties(ctBook, ctBookDTO);
        return ctBookDTO;
    }


}
