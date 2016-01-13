package net.xuele.member.service.impl;


import com.alibaba.dubbo.common.utils.CollectionUtils;
import net.xuele.common.dto.ClassInfoDTO;
import net.xuele.common.exceptions.MemberException;
import net.xuele.common.utils.MemberAssert;
import net.xuele.member.constant.ClassConstants;
import net.xuele.member.constant.PositionConstants;
import net.xuele.member.constant.SectionConstants;
import net.xuele.member.constant.UserConstants;
import net.xuele.member.context.MemberLogicContext;
import net.xuele.member.domain.*;
import net.xuele.member.dto.*;
import net.xuele.member.persist.*;
import net.xuele.member.service.CacheService;
import net.xuele.member.service.ClassService;
import net.xuele.member.service.SeqGeneratorService;
import net.xuele.member.service.UserService;
import net.xuele.member.util.ClassNameUtil;
import net.xuele.member.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by ZhengTao on 2015/6/10 0010.
 */

public class ClassServiceImpl implements ClassService {
    private static Logger logger = LoggerFactory.getLogger(ClassServiceImpl.class);
    @Autowired
    private MSchoolMapper mSchoolMapper;
    @Autowired
    private MClassMapper classMapper;
    @Autowired
    private MTeacherMapper teacherMapper;
    @Autowired
    private MSchoolPeriodMapper schoolPeriodMapper;
    @Autowired
    private CtBookMapper bookMapper;
    @Autowired
    private MStudentMapper studentMapper;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private UserService userService;
    @Autowired
    private SeqGeneratorService seqGeneratorService;
    @Autowired
    private MTeacherClassLogMapper mTeacherClassLogMapper;
    @Autowired
    private MPositionMapper mPositionMapper;

    @Override
    public String getSchoolId(String classId) {
        String schoolId = cacheService.get(MessageFormat.format(UserConstants.CLASS_SCHOOL_KEY, classId));
        if (StringUtils.isEmpty(schoolId)) {
            schoolId = classMapper.selectSchoolIdByClassId(classId);
            if (schoolId == null) {
                logger.error("班级所在学校Id不存在，classId:" + classId);
                return null;
            }
            cacheService.set(MessageFormat.format(UserConstants.CLASS_SCHOOL_KEY, classId), schoolId, 100, TimeUnit.DAYS);
        }
        return schoolId;
    }

    @Override
    public List<GradeDTO> queryGrades(ClassDTO classDTO) {
        Date now = new Date();
        int pastHalf = isPastHalf(now) ? 1 : 0;
        int currentYear = DateUtil.getYear(now);
        int lengthOfSchool = 0;
        List<MSchoolPeriod> periods = schoolPeriodMapper.selectBySchoolId(classDTO.getSchoolId());
        for (MSchoolPeriod period : periods) {
            lengthOfSchool += period.getLength();
        }
        MemberAssert.isTrue(periods.size() > 0 && periods.size() < 4, "学制设置错误", MemberLogicContext.ERROR_CODE_SCHOOL_PERIOD_SIZE);
        MSchoolPeriod first = periods.get(0);
        int limit = 0;
        if (first.getSection() == 1) {//小学不办,办初中
            limit = 9 - first.getLength();
        } else if (first.getSection() == 2) {//小学初中不办,办高中
            limit = 9;
        }
        lengthOfSchool += limit;
        //过滤毕业的年级
        int minYears = currentYear - lengthOfSchool + pastHalf;
        List<MGrade> mGrades = classMapper.selectGrade(classDTO.getSchoolId(), minYears, classDTO.getAliasName());
        List<GradeDTO> gradeDTOs = new ArrayList<>(lengthOfSchool - limit);
        for (int i = limit + 1, len = lengthOfSchool; i <= len; i++) {
            GradeDTO gradeDTO = new GradeDTO();
            gradeDTO.setLevel(i);
            gradeDTO.setId(currentYear - i + pastHalf);
            gradeDTOs.add(gradeDTO);
        }
        for (MGrade mGrade : mGrades) {
            int index = currentYear - mGrade.getYears() - 1 + pastHalf - limit;
            if (index < 0 || index > gradeDTOs.size() - 1) {
                logger.error("班级信息错误,学校id:" + classDTO.getSchoolId() +
                        ";年份:" + mGrade.getYears());
                continue;
            }
            GradeDTO gradeDTO = gradeDTOs.get(index);
            gradeDTO.setClassNumber(mGrade.getClassNumber());
        }
        return gradeDTOs;
    }

    /**
     * 新增班级
     *
     * @param classDTO 班级数据
     */
    @Override
    public ClassDTO saveClass(ClassDTO classDTO) {
        classDTO.setClassId(getClassId());
        classDTO.setCreatorName(getCreatorName(classDTO));
        classDTO.setCreateTime(new Date());
        classDTO.setStatus(ClassConstants.STATUS_OK);
        MSchool mSchool = getSchool(classDTO);
        classDTO.setSchoolId(mSchool.getId());
        classDTO.setSchoolName(mSchool.getName());
        int year = getYear(classDTO, mSchool);
        classDTO.setYears(year);
        //获取m_class表中缺少的最小班级号,根据年级获取
        int classCode = getClassCode(mSchool.getId(), year);
        if (classCode > 99) {
            throw new MemberException("一个年级限制99个班级，已达上限");
        }
        classDTO.setCodeSharing(classCode);
        classDTO.setName(String.format(ClassConstants.NAME_PATTERN, classCode));
        //班级别名不能重复
        int count = classMapper.selectByAliasName(parseToMClass(classDTO), getMinYear(classDTO));
        MemberAssert.isTrue(count == 0, "班级别名已存在", MemberLogicContext.ERROR_CODE_SELECT_CLASS_ALIAS_IS_EXIST);
        //获取学制学段ID
        classDTO.setSchoolPeriodId(getSchoolGrade(year, mSchool.getId()));
        classDTO.setmImage(UserConstants.ICON_DEFAULT);
        classMapper.insert(parseToMClass(classDTO));
        classDTO.setName(ClassNameUtil.getFullName(year, classDTO.getName()));
        return classDTO;
    }

    //根据年份判断学段
    private String getSchoolGrade(int year, String schoolId) {
        List<MSchoolPeriod> schoolPeriodList = schoolPeriodMapper.selectBySchoolId(schoolId);
        int pPeriod = 0;
        int jPeriod = 0;
        int sPeriod = 0;
        MSchoolPeriod perimary = new MSchoolPeriod();
        MSchoolPeriod junior = new MSchoolPeriod();
        MSchoolPeriod senior = new MSchoolPeriod();
        for (MSchoolPeriod sp : schoolPeriodList) {
            if (sp.getSection() == SectionConstants.PRIMARY_SCHOOL_NUM) {
                pPeriod = sp.getLength();
                perimary = sp;
            } else if (sp.getSection() == SectionConstants.JUNIOR_MIDDLE_SCHOOL_NUM) {
                jPeriod = sp.getLength();
                junior = sp;
            } else if (sp.getSection() == SectionConstants.SENIOR_HIGH_SCHOOL_NUM) {
                sPeriod = sp.getLength();
                senior = sp;
            }
        }
        //小学
        if (pPeriod != 0) {
            if (year > ClassNameUtil.getGraduateYear(pPeriod)) {
                return perimary.getId();
            }
        }
        //初中
        if (jPeriod != 0) {
            if (pPeriod != 0) {
                if (year > ClassNameUtil.getGraduateYear(jPeriod + pPeriod)) {
                    return junior.getId();
                }
            } else {
                if (year > ClassNameUtil.getGraduateYear(9) && year <= ClassNameUtil.getGraduateYear(9 - jPeriod)) {
                    return junior.getId();
                }
            }
        }
        //高中
        if (sPeriod != 0) {
            if (pPeriod != 0 && jPeriod != 0) {
                if (year > ClassNameUtil.getGraduateYear(jPeriod + pPeriod + sPeriod)) {
                    return senior.getId();
                }
            } else {
                if (year > ClassNameUtil.getGraduateYear(12) && year <= ClassNameUtil.getGraduateYear(12 - sPeriod)) {
                    return senior.getId();
                }
            }
        }
        return null;
    }

    @Override
    public List<ClassStudentDTO> queryClasses(ClassDTO classDTO) {
        int minYears = getMinYear(classDTO);
        List<ClassStudentDTO> classStudentDTOList = new ArrayList<>();
        List<MClassStudent> classList = mSchoolMapper.selectAllClass(parseToMClass(classDTO), minYears);
        for (MClassStudent cs : classList) {
            ClassStudentDTO classStudentDTO = new ClassStudentDTO();
            BeanUtils.copyProperties(cs, classStudentDTO);
            //班级名称转变为年级+班级
            classStudentDTO.setName(ClassNameUtil.getFullName(cs.getYear(), cs.getName()));
            classStudentDTOList.add(classStudentDTO);
        }
        return classStudentDTOList;
    }

    /**
     * 修改班级信息
     */
    @Override
    public ClassDTO updateClass(ClassDTO classDTO) {
        //班级别名不能重复
        int count = classMapper.selectByAliasName(parseToMClass(classDTO), getMinYear(classDTO));
        MemberAssert.isTrue(count == 0, "班级别名已存在", MemberLogicContext.ERROR_CODE_SELECT_CLASS_ALIAS_IS_EXIST);
        classMapper.updateByPrimaryKey(parseToMClass(classDTO));
        return classDTO;
    }

    /**
     * 根据班级ID删除班级
     */
    @Override
    public void deleteClassByClassId(String classId) {
        int count = studentMapper.selectStudentCount(classId, getSchoolId(classId));
        if (count != 0) {
            throw new MemberException("该班级还有" + count + "个学生信息，先转移学生才能删除");
        }
        classMapper.deleteClassByClassId(classId, getSchoolId(classId));
        classMapper.delClassIdAndSchoolId(classId, getSchoolId(classId));
    }


    /**
     * 获取m_class表中缺少的最小班级号
     */
    private Integer getClassCode(String schoolId, int year) {
        List<MClass> classCodeList = classMapper.getAllClassByYear(schoolId, year);
        Set<Integer> sets = new HashSet<>();
        //如果班级号第一个不是1，则返回1
        if (classCodeList.isEmpty() || (classCodeList.get(0).getCodeSharing() > 0 && classCodeList.get(0).getCodeSharing() != 1)) {
            return 1;
        }
        for (int i = 0; i < classCodeList.size(); i++) {
            if (!sets.contains(classCodeList.get(i).getCodeSharing())) {
                sets.add(classCodeList.get(i).getCodeSharing());
            }
        }
        for (int i = 1; ; i++) {
            if (!sets.contains(i)) {
                return i;
            }
        }
        //从班级号1开始，如果不连续，就返回缺少的数
/*        for (int i = 0; i < classCodeList.size() - 1; i++) {
            if (classCodeList.get(i).getCodeSharing() + 1 != classCodeList.get(i + 1).getCodeSharing()) {
                return classCodeList.get(i).getCodeSharing() + 1;
            }
        }
        //如果连续，则返回接下去的数
        return classCodeList.get(classCodeList.size() - 1).getCodeSharing() + 1;
       */
    }

    private int getYear(ClassDTO classDTO, MSchool mSchool) {
        //如果用户指定了学界,就直接设置
        if (classDTO.getYears() != null) {
            return classDTO.getYears();
        }
        Date now = new Date();
        int year = DateUtil.getYear(now);
        Assert.isTrue(classDTO.getGrade() > 0 && classDTO.getGrade() < mSchool.getLengthOfSchool());
        year = year - classDTO.getGrade();
        //比如现在是2015年7月份,1年级的入学年是2014(2005-1)年;
        // 如果是2015年9月份,则1年级的入学年是2015(2005-1+1)年.
        if (isPastHalf(now)) {
            year++;
        }
        return year;
    }

    /**
     * 这个时刻是不是录入学生后,现在以8月1日为准,超过此日期返回true
     */
    private boolean isPastHalf(Date now) {
        return ClassNameUtil.isPastHalf(now);
    }

    private MSchool getSchool(ClassDTO classDTO) {
        Assert.notNull(classDTO.getSchoolId(), "用户没有所属学校.");
        MSchool school = mSchoolMapper.selectByPrimaryKey(classDTO.getSchoolId());
        Assert.notNull(school, "数据库中不存在该学校:" + classDTO.getSchoolId());
        return school;
    }

    private String getCreatorName(ClassDTO classDTO) {
        UserDTO userDTO = userService.getByUserId(classDTO.getCreatorId());
        Assert.notNull(userDTO, "当前登录用户不存在");
        String creatorName = userDTO.getRealName();
        creatorName = StringUtils.isNotEmpty(creatorName) ? creatorName : userDTO.getUserId();
        return creatorName;
    }

    private String getClassId() {
        String classId = UUID.randomUUID().toString();
        classId = classId.replaceAll("\\-", "");
        logger.info("insertClass;id:" + classId);
        return classId;
    }

    private MClass parseToMClass(ClassDTO classDTO) {
        MClass mClass = new MClass();
        BeanUtils.copyProperties(classDTO, mClass);
        return mClass;
    }

    /**
     * 更换班主任、添加班主任
     *
     * @param userId  老师id
     * @param classId 班级id
     * @return 老师信息
     */
    @Override
    public UserTeacherDTO updateClassTeacher(String userId, String classId, String schoolId) {
        //判断是否是原班主任，原班主任则抛异常
        MClass mClass = classMapper.getClassByUserIdAndClassId(userId, classId, schoolId);
        if (mClass != null) {
            throw new MemberException("【" + mClass.getChargeName() + "】是原班主任，请选择其他老师");
        }
        MClass mUserClass = classMapper.selectByPrimaryKey(classId, schoolId);
        if (mUserClass == null) {
            logger.error("该班级不存在{}", classId);
            throw new MemberException("该班级不存在");
        }
        //更改班级表，该班级的班主任
        UserDTO userDTO = userService.getByUserId(userId);
        if (userDTO == null) {
            throw new MemberException("用户不存在");
        }
        classMapper.updateClassTeacher(userDTO.getUserId(), userDTO.getRealName(), classId, schoolId);
        Integer isThere = 0;
        //判断老师班级对应表是否有该老师信息，有则不再添加数据
        List<MUserTeacher> mUserTeachers = teacherMapper.selectClassTeachers(classId, schoolId);
        for (MUserTeacher mUserTeacher : mUserTeachers) {
            if (mUserTeacher.getUserId().equals(userId))
                isThere = 1;
        }
        if (isThere == 0) {
            //更改班级老师对应表
            List<MTeacherClass> teacherClassList = new ArrayList<>();
            MTeacherClass teacherClass = new MTeacherClass();
            Long id = seqGeneratorService.generate("m_teacher_class");
            teacherClass.setId(id);
            teacherClass.setSchoolId(schoolId);
            teacherClass.setClassId(classId);
            teacherClass.setTeacherId(userId);
            teacherClassList.add(teacherClass);
            teacherMapper.insertTeacherClass(teacherClassList);
        }
        //返回老师信息
        MUserTeacher userTeacher = teacherMapper.selectChief(userId, schoolId);
        if (userTeacher == null) {
            return null;
        }
        //如果要设置的班主任是老师，那她的职务要变成班主任
        if (PositionConstants.TEACHER.equals(userTeacher.getPositionId())) {
            teacherMapper.updateTeacherMessage(userTeacher.getUserId(),
                    mPositionMapper.selectByPrimaryKey(PositionConstants.HEADMASTER).getName(),
                    PositionConstants.HEADMASTER, schoolId);
        }
        //查询原班主任是否还任其他班级的班主任
        List<MClass> mClassList = new ArrayList<>();
        if (StringUtils.isNotEmpty(mUserClass.getChargeId())) {
            mClassList = classMapper.queryChiefClass(mUserClass.getChargeId(), schoolId);
        }
        //查询原班主伤的职务信息
        MUserTeacher mUserTeacher = teacherMapper.selectChief(mUserClass.getChargeId(), schoolId);
        if (mUserTeacher != null) {
            //查询出原班主任 是否还任其他班级的班主任，如果有 不做其他操作，如果没有且原来职务是班主任的则将职务变为教师
            if (CollectionUtils.isEmpty(mClassList) && PositionConstants.HEADMASTER.equals(mUserTeacher.getPositionId())) {
                teacherMapper.updateTeacherMessage(mUserTeacher.getUserId(), mPositionMapper.selectByPrimaryKey(PositionConstants.TEACHER).getName(), PositionConstants.TEACHER, schoolId);
            }
        }
        UserTeacherDTO userTeacherDTO = new UserTeacherDTO();
        BeanUtils.copyProperties(userTeacher, userTeacherDTO);
        return userTeacherDTO;
    }

    /**
     * 解除班主任
     */
    @Override
    public int deleteChief(String classId) {
        int ret = classMapper.removeChief(classId, getSchoolId(classId));
        //把修改班主任的信息保存到日志表m_teacher_class_log
        MTeacherClassLog mTeacherClassLog = new MTeacherClassLog();
        mTeacherClassLog.setChangeInfo("接口调用");
        mTeacherClassLog.setUserId("");
        mTeacherClassLog.setOperatorUserId("");
        mTeacherClassLog.setSchoolId(getSchoolId(classId));
        mTeacherClassLog.setClassId(classId);
        mTeacherClassLog.setId(getTeacherClassLogId());
        mTeacherClassLog.setCreateTime(new Date());
        mTeacherClassLogMapper.insert(mTeacherClassLog);
        return ret;
    }

    /**
     * 查询班级信息
     */
    @Override
    public ClassDTO getByClassId(String classId) {
        if (StringUtils.isEmpty(classId)) {
            return null;
        }
        MClass mClass = classMapper.selectByPrimaryKey(classId, getSchoolId(classId));
        if (mClass == null) {
            logger.warn("班级不存在：" + classId);
            return null;
        }
        ClassDTO classDTO = new ClassDTO();
        BeanUtils.copyProperties(mClass, classDTO);
        return classDTO;
    }

    //获取毕业的班级学界 返回2014，表示2013开始是毕业学界
    private int getMinYear(ClassDTO classDTO) {
        Date now = new Date();
        int pastHalf = isPastHalf(now) ? 1 : 0;
        int currentYear = DateUtil.getYear(now);
        int lengthOfSchool = 0;
        List<MSchoolPeriod> periods = schoolPeriodMapper.selectBySchoolId(classDTO.getSchoolId());
        for (MSchoolPeriod period : periods) {
            lengthOfSchool += period.getLength();
        }
        MemberAssert.isTrue(periods.size() > 0 && periods.size() < 4, "学制设置错误", MemberLogicContext.ERROR_CODE_SCHOOL_PERIOD_SIZE);
        MSchoolPeriod first = periods.get(0);
        int limit = 0;
        if (first.getSection() == 1) {//小学不办,办初中
            limit = 9 - first.getLength();
        } else if (first.getSection() == 2) {//小学初中不办,办高中
            limit = 9;
        }
        lengthOfSchool += limit;
        return currentYear - lengthOfSchool + pastHalf;
    }

    /**
     * APP接口：获取全部班级
     */
    @Override
    public List<ClassInfoDTO> queryAllClass(String userId) {
        List<MClass> classList = classMapper.getAllClass(userService.getSchoolId(userId));
        List<ClassInfoDTO> groupAppDTOList = new ArrayList<>();
        for (MClass cla : classList) {
            ClassInfoDTO gadto = new ClassInfoDTO();
            gadto.setClassId(cla.getClassId());
            gadto.setClassName(cla.getName());
            gadto.setAliasName(cla.getAliasName());
            gadto.setYear(cla.getYears());
            gadto.setChargeId(cla.getChargeId());
            gadto.setChargeName(cla.getChargeName());
            gadto.setmImage(cla.getmImage());
            gadto.setGradeNum(ClassNameUtil.getGradeNum(cla.getYears()));
            groupAppDTOList.add(gadto);
        }
        return groupAppDTOList;
    }

    /**
     * 根据学校ID获取毕业班级信息，有别名则根据别名搜索，没有则搜索全部
     */
    @Override
    public List<GraduateingClassDTO> queryALLGraduateClass(String schoolId, String aliasName) {
        if (StringUtils.isEmpty(aliasName)) {
            aliasName = null;
        }
        //所有学段的毕业班级
        List<MClassInfo> classInfoList = classMapper.querySchoolbySection(schoolId, aliasName);
        //所有毕业年份
        List<Integer> graduateYear = quertygraduateYear(classInfoList);
        //所有小学毕业班级
        List<MClassInfo> primarySchool = queryClassStage(classInfoList, SectionConstants.PRIMARY_SCHOOL_NUM);
        //所有的初中班级
        List<MClassInfo> middleSchool = queryClassStage(classInfoList, SectionConstants.JUNIOR_MIDDLE_SCHOOL_NUM);
        //所有的高中班级
        List<MClassInfo> highSchool = queryClassStage(classInfoList, SectionConstants.SENIOR_HIGH_SCHOOL_NUM);
        List<GraduateingClassDTO> gcDTOList = new ArrayList<>();
        for (Integer in : graduateYear) {
            GraduateingClassDTO gcdto = new GraduateingClassDTO();
            gcdto.setYear(in);
            gcdto.setPrimarySchool(getClass(primarySchool, in));
            gcdto.setMiddleSchool(getClass(middleSchool, in));
            gcdto.setHighSchool(getClass(highSchool, in));
            gcDTOList.add(gcdto);
        }
        return gcDTOList;
    }

    //获取某年份的毕业班级
    private List<ClassStudentDTO> getClass(List<MClassInfo> classInfoList, int year) {
        List<ClassStudentDTO> classStudentDTOList = new ArrayList<>();
        for (MClassInfo classInfo : classInfoList) {
            if (year == classInfo.getYear()) {
                ClassStudentDTO ctdto = new ClassStudentDTO();
                ctdto.setClassId(classInfo.getClassId());
                ctdto.setName(classInfo.getAliasName() + classInfo.getClassName());
                ctdto.setAliasName(classInfo.getAliasName());
                ctdto.setYear(classInfo.getYear());
                ctdto.setStudentNumber(classInfo.getStudentCount());
                ctdto.setmImage(classInfo.getmImage());
                classStudentDTOList.add(ctdto);
            }
        }
        return classStudentDTOList;
    }

    //获取毕业年份
    private List<Integer> quertygraduateYear(List<MClassInfo> ciList) {
        List<Integer> intList = new ArrayList<>();
        for (MClassInfo ci : ciList) {
            intList.add(ci.getYear());
        }
        return new ArrayList<>(new HashSet<>(intList));
    }

    //获取每个学段的毕业班级
    private List<MClassInfo> queryClassStage(List<MClassInfo> classInfoList, int section) {
        List<MClassInfo> ciList = new ArrayList<>();
        for (MClassInfo ci : classInfoList) {
            if (ci.getSection() == section) {
                ciList.add(ci);
            }
        }
        return ciList;
    }

    /**
     * APP接口：查询科目，年级，班级
     */
    @Override
    public SubjectGradeDTO getSubjectGrade(SubjectGradeDTO sgdto) {
        SubjectGradeDTO subjectGradeDTO = new SubjectGradeDTO();
        if (StringUtils.isNotEmpty(sgdto.getUserId())) {//1：userId存在，查老师选的课本的科目和年级，老师教的班级；
            //获取科目和年级
            List<CtBookInfo> bookList = bookMapper.queryTeacherSubject(sgdto.getUserId(), sgdto.getSchoolId());
            List<CtBookDTO> bookDTOList = new ArrayList<>();
            for (CtBookInfo book : bookList) {
                CtBookDTO bookDTO = new CtBookDTO();
                bookDTO.setGradeName(ClassNameUtil.getGradeName(book.getGrade()));
                BeanUtils.copyProperties(book, bookDTO);
                bookDTOList.add(bookDTO);
            }
            subjectGradeDTO.setSubjectList(bookDTOList);
            //获取班级
            List<MClass> classList = classMapper.queryTeacherClass(sgdto.getUserId(), sgdto.getSchoolId());
            List<ClassDTO> classDTOList = new ArrayList<>();
            for (MClass cla : classList) {
                ClassDTO classDTO = new ClassDTO();
                classDTO.setName(ClassNameUtil.getFullName(cla.getYears(), cla.getName()));
                BeanUtils.copyProperties(cla, classDTO);
                classDTOList.add(classDTO);
            }
            subjectGradeDTO.setClassList(classDTOList);
        } else if (StringUtils.isNotEmpty(sgdto.getSchoolId())) {//2：学校id存在，查学校对应的课本的科目和年级，和这个学校所有的班级；
            //获取科目和年级
            List<CtBookInfo> bookList = bookMapper.querySchoolSubject(sgdto.getSchoolId());
            List<CtBookDTO> bookDTOList = new ArrayList<>();
            for (CtBookInfo book : bookList) {
                CtBookDTO bookDTO = new CtBookDTO();
                bookDTO.setGradeName(ClassNameUtil.getGradeName(book.getGrade()));
                BeanUtils.copyProperties(book, bookDTO);
                bookDTOList.add(bookDTO);
            }
            subjectGradeDTO.setSubjectList(bookDTOList);
            //获取班级
            List<MClass> classList = classMapper.getAllClass(sgdto.getSchoolId());
            List<ClassDTO> classDTOList = new ArrayList<>();
            for (MClass cla : classList) {
                ClassDTO classDTO = new ClassDTO();
                classDTO.setName(ClassNameUtil.getFullName(cla.getYears(), cla.getName()));
                BeanUtils.copyProperties(cla, classDTO);
                classDTOList.add(classDTO);
            }
            subjectGradeDTO.setClassList(classDTOList);
        } else if (StringUtils.isNotEmpty(sgdto.getArea())) {//3：area存在，查这个区域下的所有学校对应的科目和年级，班级为null；
            List<CtBookInfo> bookList = bookMapper.queryAreaSchoolSubject(sgdto.getArea());
            List<CtBookDTO> bookDTOList = new ArrayList<>();
            for (CtBookInfo book : bookList) {
                CtBookDTO bookDTO = new CtBookDTO();
                bookDTO.setGradeName(ClassNameUtil.getGradeName(book.getGrade()));
                BeanUtils.copyProperties(book, bookDTO);
                bookDTOList.add(bookDTO);
            }
            subjectGradeDTO.setSubjectList(bookDTOList);
        } else if (StringUtils.isEmpty(sgdto.getArea())) {//4：area没有，查科目和年级的字典，班级为null
            List<CtBookInfo> bookList = bookMapper.queryGradeSubject();
            List<CtBookDTO> bookDTOList = new ArrayList<>();
            for (CtBookInfo book : bookList) {
                CtBookDTO bookDTO = new CtBookDTO();
                BeanUtils.copyProperties(book, bookDTO);
                bookDTOList.add(bookDTO);
            }
            subjectGradeDTO.setSubjectList(bookDTOList);
        }
        return subjectGradeDTO;
    }

    /**
     * 根据学校ID查询学校未毕业班级个数
     */
    @Override
    public int selectClassNumBySchoolId(String schoolId) {
        return classMapper.selectBySchoolId(schoolId);
    }

    /**
     * 根据学校id获取该学校的所有班级id
     */
    @Override
    public List<String> queryClassIdBySchoolId(String schoolId) {
        return classMapper.queryClassIdBySchoolId(schoolId);
    }

    @Override
    public List<ClassDTO> getAllClassByClassIds(String schoolId, List<String> classIds) {
        List<ClassDTO> classDTOs = new ArrayList<>();
        List<MClass> mClasses;
        if (classIds != null) {
            if (classIds.size() > 1) {
                mClasses = classMapper.getAllClassByClassIds(schoolId, classIds);
                for (MClass mClass : mClasses) {
                    ClassDTO classDTO = new ClassDTO();
                    BeanUtils.copyProperties(mClass, classDTO);
                    classDTOs.add(classDTO);
                }
            } else if (classIds.size() == 1) {
                MClass mClass = classMapper.selectByPrimaryKey(classIds.get(0), schoolId);
                ClassDTO classDTO = new ClassDTO();
                BeanUtils.copyProperties(mClass, classDTO);
                classDTOs.add(classDTO);
            }
        }

        return classDTOs;
    }

    @Override
    public List<String> getClassNamesByClassIds(String schoolId, List<String> classIds) {
        List<ClassDTO> classDTOs = getAllClassByClassIds(schoolId, classIds);
        List<String> strings = new ArrayList<>(classDTOs.size());
        for (ClassDTO spc : classDTOs) {
            if (spc.getYears() != null && spc.getYears() != 0) {
                strings.add(ClassNameUtil.getFullName(spc.getYears(), spc.getCodeSharing()));
            }
        }
        return strings;
    }

    @Override
    public void updateTeacherClassLog(TeacherClassLogDTO teacherClassLogDTO) {

        for (String eachUserId : teacherClassLogDTO.getUserIdList()) {
            //把修改班主任的信息保存到日志表m_teacher_class_log
            MTeacherClassLog mTeacherClassLog = new MTeacherClassLog();
            BeanUtils.copyProperties(teacherClassLogDTO, mTeacherClassLog);
            mTeacherClassLog.setUserId(eachUserId);
            mTeacherClassLog.setId(getTeacherClassLogId());
            mTeacherClassLog.setCreateTime(new Date());
            mTeacherClassLogMapper.insert(mTeacherClassLog);
        }
    }

    private String getTeacherClassLogId() {
        String id = UUID.randomUUID().toString();
        id = id.replaceAll("\\-", "");
        logger.info("insertTeacherClassLog;id:" + id);
        return id;
    }
}
