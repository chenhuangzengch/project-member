package net.xuele.member.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import net.xuele.common.dto.ClassInfoDTO;
import net.xuele.common.exceptions.MemberException;
import net.xuele.common.page.PageResponse;
import net.xuele.common.utils.MemberAssert;
import net.xuele.common.utils.PageUtils;
import net.xuele.member.constant.*;
import net.xuele.member.context.MemberLogicContext;
import net.xuele.member.domain.*;
import net.xuele.member.dto.*;
import net.xuele.member.dto.page.TeacherPageRequest;
import net.xuele.member.persist.*;
import net.xuele.member.pojo.RequestTeacherLoginPage;
import net.xuele.member.service.*;
import net.xuele.member.util.ClassNameUtil;
import net.xuele.member.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.*;

/**
 * Created by mengzhiheng on 2015/6/26 0008.
 */

public class TeacherServiceImpl implements TeacherService {
    @Autowired
    private MTeacherMapper teacherMapper;
    @Autowired
    private MClassMapper classMapper;
    @Autowired
    private MPositionMapper mPositionMapper;
    @Autowired
    private MSchoolMapper mSchoolMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private MSchoolPeriodMapper schoolPeriodMapper;
    @Autowired
    private CtBookMapper bookMapper;
    @Autowired
    private MUsersMapper usersMapper;
    @Autowired
    private MSchoolMapper schoolMapper;
    @Autowired
    private MUserRoleMapper userRoleMapper;
    @Autowired
    private MClassMapper mClassMapper;
    @Autowired
    private ClassService classService;
    @Autowired
    private SeqGeneratorService seqGeneratorService;
    @Autowired
    private FamilyRelationService familyRelationService;
    @Autowired
    private CacheService cacheService;

    private static Logger logger = LoggerFactory.getLogger(TeacherServiceImpl.class);

    /**
     * 根据老师ID获取老师表信息
     */
    @Override
    public TeacherDTO getTeacherByUserId(String userId, String schoolId) {
        MTeacher mTeacher = teacherMapper.selectByPrimaryKey(userId, schoolId);
        TeacherDTO teacherDTO = new TeacherDTO();
        BeanUtils.copyProperties(mTeacher, teacherDTO);
        return teacherDTO;
    }

    /**
     * APP接口：老师添加班级（班级和老师都只有一个）
     * WEB平台：班级管理添加老师（班级一个，老师多个）
     *
     * @param userIds 老师的学乐号，用","隔开
     * @param classId 班级ID
     */
    @Override
    public int saveClassTeachers(String userIds, String classId, String schoolId) {
        String[] userId = userIds.split(",");
        //存放学乐号，并去重
        Set<String> set = new HashSet<>(Arrays.asList(userId));
        //删除用
        List<String> userIdList = new ArrayList<>();
        //插入用
        List<MTeacherClass> teacherClassList = new ArrayList<>();
        for (String id : set) {
            userIdList.add(id);
            MTeacherClass teacherClass = new MTeacherClass();
            Long teacherClassId = seqGeneratorService.generate("m_teacher_class");
            teacherClass.setId(teacherClassId);
            teacherClass.setTeacherId(id);
            teacherClass.setSchoolId(schoolId);
            teacherClass.setClassId(classId);
            teacherClassList.add(teacherClass);
        }
        //先删除，防止已经存在
        teacherMapper.deleteTeacherClass(userIdList, classId, schoolId);
        //再插入
        return teacherMapper.insertTeacherClass(teacherClassList);
    }

    /**
     * 根据名称或科目ID查询老师（无全校、管理层）
     * 没有科目ID，只根据名称查询
     * 没有科目ID和名称,则查询全校老师
     * 根据科目ID和名称查询
     * 根据科目ID查询
     */
    @Override
    public List<UserTeacherDTO> queryTeachers(String realName, String subjectId, String schoolId) {
        if (StringUtils.isEmpty(realName)) {
            realName = null;
        }
        List<MUserTeacher> userTeacherList;
        //没有科目ID，只根据老师名称查询
        //没有科目ID和名称,则查询全校老师
        if (StringUtils.isEmpty(subjectId)) {
            userTeacherList = teacherMapper.selectTeacherWithoutSubjectId(realName, schoolId);
        } else {
            //根据科目ID和名称查询
            //根据科目ID查询
            if (("MANAGE_NODE").equals(subjectId)) {
                userTeacherList = teacherMapper.getAllManager(schoolId, realName);
            }else if("UNLOAD_NODE".equals(subjectId)){
                userTeacherList =teacherMapper.selectTeacherBySubjectNull(schoolId);
            }else{
                userTeacherList = teacherMapper.selectTeacherWithSubjectId(realName, subjectId, schoolId);
            }
        }
        List<UserTeacherDTO> userTeacherDTOList = new ArrayList<>();
        for (MUserTeacher ut : userTeacherList) {
            UserTeacherDTO utDTO = new UserTeacherDTO();
            BeanUtils.copyProperties(ut, utDTO);
            userTeacherDTOList.add(utDTO);
        }
        return userTeacherDTOList;
    }

    /**
     * 查询班级老师
     *
     * @param classId 班级ID
     */
    @Override
    public List<UserTeacherDTO> queryClassTeachers(String classId) {
        String schoolId = classService.getSchoolId(classId);
        List<MUserTeacher> userTeacherList = teacherMapper.selectClassTeachers(classId, schoolId);
        MClass mClass = classMapper.selectByPrimaryKey(classId, schoolId);
        List<UserTeacherDTO> userTeacherDTOArrayList = new ArrayList<>();
        for (MUserTeacher ut : userTeacherList) {
            UserTeacherDTO userTeacherDTO = new UserTeacherDTO();
            BeanUtils.copyProperties(ut, userTeacherDTO);
            //判断是否为班主任，并放在第一位，1表示是班主任，0表示不是班主任
            if (ut.getUserId().equals(mClass.getChargeId())) {
                userTeacherDTO.setCharge(1);
                userTeacherDTOArrayList.add(0, userTeacherDTO);
            } else {
                userTeacherDTO.setCharge(0);
                userTeacherDTOArrayList.add(userTeacherDTO);
            }
        }
        return userTeacherDTOArrayList;
    }

    /**
     * 修改老师资料
     *
     * @param userId     学乐号
     * @param realName   名称
     * @param positionId 职务
     */
    @Override
    public UserTeacherDTO updateTeacherInfo(String userId, String realName, String positionId) {
        String schoolId = userService.getSchoolId(userId);
        //获取职务
        if (StringUtils.isNotEmpty(positionId)) {
            MPosition position = mPositionMapper.selectByPrimaryKey(positionId);
            if (position != null) {
                //修改老师表
                teacherMapper.updateTeacherMessage(userId, position.getName(), position.getPositionId(), schoolId);
            }

        }
        //修改用户表
        usersMapper.updateUserName(userId, realName, schoolId);
        //修改班级表
        List<MClass> classList = classMapper.queryChiefClass(userId,schoolId);
        if (CollectionUtils.isNotEmpty(classList)) {
            classMapper.updateClassTeacherList(classList, userId, realName);
        }
        //修改家庭关系表中用户名称
        familyRelationService.updateName(userId, realName);
        cacheService.delete(MessageFormat.format(CacheConstants.KEY_USER, userId));
        return getTeacherInfo(userId);
    }

    /**
     * APP接口、WEB平台：根据老师ID获取老师信息
     */
    @Override
    public UserTeacherDTO getTeacherInfo(String userId) {
        return getTeacherInfo(userId, userService.getSchoolId(userId));
    }

    /**
     * 云教学：根据老师ID获取老师信息
     */
    @Override
    public UserTeacherDTO getTeacherInfo(String userId, String schoolId) {
        MUserTeacher userTeacher = teacherMapper.selectChief(userId, schoolId);
        if (userTeacher == null) {
            return null;
        }
        UserTeacherDTO userTeacherDTO = new UserTeacherDTO();
        BeanUtils.copyProperties(userTeacher, userTeacherDTO);
        return userTeacherDTO;
    }

    /**
     * 根据多个老师ID获取老师信息
     */
    @Override
    public List<UserTeacherDTO> queryTeacherInfo(ArrayList<String> userIds) {
        List<MUserTeacher> userTeacherList = teacherMapper.queryTeacherByUserIds(userIds, userService.getSchoolId(userIds.get(0)));
        List<UserTeacherDTO> dtoList = new ArrayList<>();
        for (MUserTeacher teacher : userTeacherList) {
            UserTeacherDTO userTeacherDTO = new UserTeacherDTO();
            BeanUtils.copyProperties(teacher, userTeacherDTO);
            dtoList.add(userTeacherDTO);
        }
        return dtoList;
    }

    /**
     * realName为空，根据老师名称搜索
     * realName不为空，查询所有管理层人员
     */
    public List<UserTeacherDTO> queryAllManager(String schoolId, String realName) {
        if (StringUtils.isEmpty(realName)) {
            realName = null;
        }
        List<MUserTeacher> userTeacherList = teacherMapper.getAllManager(schoolId, realName);
        List<UserTeacherDTO> userTeacherDTOList = new ArrayList<>();
        for (MUserTeacher ut : userTeacherList) {
            UserTeacherDTO utdto = new UserTeacherDTO();
            BeanUtils.copyProperties(ut, utdto);
            if (PositionConstants.PRINCIPAL.equals(ut.getPositionId())) {
                userTeacherDTOList.add(0, utdto);
            } else {
                userTeacherDTOList.add(utdto);
            }
        }
        return userTeacherDTOList;
    }

    @Override
    public PageResponse<TeacherLoginDTO> queryPage(TeacherPageRequest pageRequest) {
        PageResponse<TeacherLoginDTO> pageResponse = new PageResponse<>();
        PageUtils.buldPageResponse(pageRequest, pageResponse);
        RequestTeacherLoginPage request = new RequestTeacherLoginPage();
        BeanUtils.copyProperties(pageRequest, request);
        pageResponse.setRecords(teacherMapper.countTeacherLogin(request));
        List<MTeacherLogin> teacherLogins = teacherMapper.selectTeacherLogin(request);
        List<TeacherLoginDTO> list = new ArrayList<>();
        for (MTeacherLogin teacherLogin : teacherLogins) {
            TeacherLoginDTO t = new TeacherLoginDTO();
            BeanUtils.copyProperties(teacherLogin, t);
            t.setDate(teacherLogin.getDate() == null ? "从未登录" : DateFormatUtils.format(teacherLogin.getDate(), "yyyy-MM-dd HH:mm"));
            list.add(t);
        }
        pageResponse.setRows(list);
        return pageResponse;
    }

    @Override
    public ExcelInfo queryExcelInfo(TeacherPageRequest pageRequest) {
        RequestTeacherLoginPage request = new RequestTeacherLoginPage();
        BeanUtils.copyProperties(pageRequest, request);
        List<MTeacherLogin> teacherLogins = teacherMapper.selectTeacherLoginNoLimit(request);
        List<List<Object>> data = new ArrayList<>();
        for (MTeacherLogin teacherLogin : teacherLogins) {
            List<Object> row = new ArrayList<>();
            row.add(teacherLogin.getRealName());
            row.add(teacherLogin.getSubjectName());
            row.add(teacherLogin.getPositionName());
            row.add(teacherLogin.getUserId());
            row.add(teacherLogin.getDate() == null ? null : DateFormatUtils.format(teacherLogin.getDate(), "yyyy-MM-dd HH:mm"));
            data.add(row);
        }
        ExcelInfo info = new ExcelInfo();
        info.setData(data);
        //数据的标题头,第一行,可以为不设置
        info.setHeaders(new String[]{"老师姓名", "主授科目", "职务", "登录账号", "上线情况"});
        info.setSheetName("Sheet1");//可以为不设置
        return info;
    }

    /**
     * 设置校长，返回原校长
     */
    @Override
    public String savePrincipal(String userId, String schoolId) {
        MTeacher teacher = new MTeacher();
        teacher.setPositionId(PositionConstants.TEACHER);
        teacher.setPositionName(mPositionMapper.selectByPrimaryKey(PositionConstants.TEACHER).getName());
        String originalPrincipalId = null;
        MTeacher mTeacher = teacherMapper.selectPrincipalBySchoolId(schoolId, PositionConstants.PRINCIPAL);
        if (mTeacher != null) {
            originalPrincipalId = mTeacher.getUserId();
            if (originalPrincipalId.equals(userId))
                originalPrincipalId = null;
            mPositionMapper.updateInTeacher(teacher, PositionConstants.PRINCIPAL, schoolId);
        }

        teacherMapper.setPrincipalByUserId(userId, PositionConstants.PRINCIPAL, schoolId, mPositionMapper.selectByPrimaryKey(PositionConstants.PRINCIPAL).getName());
        //插入用户角色关系表
        userRoleMapper.deleteUserRole(userId, RoleIdConstants.SCHOOL_TEACHER_MANAGER, schoolId);
        MUserRole userRole = new MUserRole();
        userRole.setId(seqGeneratorService.generate("m_user_role"));
        userRole.setUserId(userId);
        userRole.setRoleId(RoleIdConstants.SCHOOL_TEACHER_MANAGER);
        userRole.setSchoolId(schoolId);
        userRoleMapper.insert(userRole);
        return originalPrincipalId;
    }

    /**
     * 添加新老师
     */
    @Override
    public UserTeacherDTO saveTeacher(String name, String positionId, String schoolId) {
        //插入m_users、cas_login、m_user_role、m_login_user
        UserDTO addedUser = insertUser(name, schoolId);
        //插入m_teachers
        TeacherDTO teacherDTO = insertTeacher(positionId, schoolId, addedUser);
        return getUserTeacherDTO(addedUser, teacherDTO);
    }

    private UserTeacherDTO getUserTeacherDTO(UserDTO addedUser, TeacherDTO teacherDTO) {
        UserTeacherDTO teacherWrapper = new UserTeacherDTO();
        teacherWrapper.setRealName(addedUser.getRealName());
        teacherWrapper.setUserId(addedUser.getUserId());
        teacherWrapper.setPositionId(teacherDTO.getPositionId());
        teacherWrapper.setPositionName(teacherDTO.getPositionName());
        teacherWrapper.setIcon(addedUser.getIcon());
        return teacherWrapper;
    }

    //插入m_teachers
    private TeacherDTO insertTeacher(String positionId, String schoolId, UserDTO addedUser) {
        MSchool school = mSchoolMapper.selectByPrimaryKey(schoolId);
        if (school == null) {
            throw new MemberException("学校不存在");
        }
        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setSchoolId(schoolId);
        teacherDTO.setSchoolName(school.getName());
        MPosition mPosition = mPositionMapper.selectByPrimaryKey(positionId);
        if (mPosition == null) {
            throw new MemberException("该职务不存在");
        }
        teacherDTO.setPositionId(mPosition.getPositionId());
        teacherDTO.setPositionName(mPosition.getName());
        teacherDTO.setUserId(addedUser.getUserId());
        teacherDTO.setIsManager(0);
        teacherMapper.insert(parseToMTeacher(teacherDTO));
        return teacherDTO;
    }

    //插入m_users、cas_login、m_user_role、m_login_user
    private UserDTO insertUser(String name, String schoolId) {
        UserDTO userDTO = new UserDTO();
        userDTO.setIdentityId(IdentityIdConstants.TEACHER);
        userDTO.setIdentityDescription("教师");
        userDTO.setRealName(name);
        userDTO.setSchoolId(schoolId);
        userDTO.setArea(schoolMapper.selectByPrimaryKey(schoolId).getArea());
        return userService.saveUser(userDTO);
    }

    /**
     * 管理层删除老师
     */
    @Override
    public void deleteManagers(String userIds) {
        String[] userId = userIds.split(",");
        Set<String> set = new HashSet<>(Arrays.asList(userId));
        //修改老师表
        teacherMapper.deleteManagers(new ArrayList<>(set), userService.getSchoolId(userId[0]));
        //删除用户角色关系表
        userRoleMapper.deleteUserRoleList(new ArrayList<>(set), RoleIdConstants.SCHOOL_TEACHER_MANAGER, userService.getSchoolId(userId[0]));
    }

    /**
     * 添加管理层人员
     */
    @Override
    public void saveManagers(String userIds, String schoolId) {
        String[] userId = userIds.split(",");
        Set<String> set = new HashSet<>(Arrays.asList(userId));
        //修改老师表
        teacherMapper.setManagers(new ArrayList<>(set), schoolId);
        //插入用户角色关系表
        List<MUserRole> userRoleList = new ArrayList<>();
        for (int i = 0; i < userId.length; i++) {
            MUserRole userRole = new MUserRole();
            userRole.setId(seqGeneratorService.generate("m_user_role"));
            userRole.setUserId(userId[i]);
            userRole.setRoleId(RoleIdConstants.SCHOOL_TEACHER_MANAGER);
            userRole.setSchoolId(schoolId);
            userRoleList.add(userRole);
        }
        userRoleMapper.saveUserRoleList(userRoleList);
    }

    /**
     * APP接口、WEB平台：获取老师授课班级
     */
    @Override
    @Transactional(readOnly = false)
    public List<ClassInfoDTO> queryTeacherClass(String userId) {
        List<MClassInfo> teacherClassList = teacherMapper.getTeacherClass(userId, userService.getSchoolId(userId));
        List<ClassInfoDTO> classInfoDTOList = new ArrayList<>();
        int gradeNum;
        for (MClassInfo tc : teacherClassList) {
            ClassInfoDTO tcdto = new ClassInfoDTO();
            BeanUtils.copyProperties(tc, tcdto);
            gradeNum = getGradeNum(tc.getYear());
            //设置年级号
            tcdto.setGradeNum(gradeNum);
            //设置学期
            tcdto.setSemester(ClassNameUtil.getSemester());
            //设置班级名称 年级+班级:一年级（1）班
            tcdto.setClassName(ClassNameUtil.getFullName(tc.getYear(), tc.getClassName()));
            classInfoDTOList.add(tcdto);
        }
        return classInfoDTOList;
    }


    /**
     * APP接口：根据老师ID获取老师所有的授课教材
     */
    @Override
    public List<CtBookDTO> queryBookDTO(String userId) {
        List<CtBookInfo> bookList = bookMapper.getMaterialsByUserid(userId, userService.getSchoolId(userId), null);
        List<CtBookDTO> bookAppDTOList = new ArrayList<>();
        for (CtBookInfo book : bookList) {
            CtBookDTO bookDTO = new CtBookDTO();
            BeanUtils.copyProperties(book, bookDTO);
            bookAppDTOList.add(bookDTO);
        }
        return bookAppDTOList;
    }

    /**
     * 根据老师ID和科目ID查询老师对应的教材
     *
     * @param userId    教师ID
     * @param subjectId 科目ID
     */
    @Override
    @Transactional(readOnly = false)
    public List<CtBookDTO> queryBookDTOBySubject(String userId, String subjectId) {
        List<CtBookInfo> bookList = bookMapper.getMaterialsByUserid(userId, userService.getSchoolId(userId), subjectId);
        List<CtBookDTO> bookDTOList = new ArrayList<>();
        for (CtBookInfo book : bookList) {
            CtBookDTO bookDTO = new CtBookDTO();
            BeanUtils.copyProperties(book, bookDTO);
            bookDTOList.add(bookDTO);
        }
        return bookDTOList;
    }

    //根据年份获取年级号
    private int getGradeNum(int year) {
        Date now = new Date();
        int pastHalf = isPastHalf(now) ? 1 : 0;
        int currentYear = DateUtil.getYear(now);
        return currentYear + pastHalf - year;
    }

    /**
     * 这个时刻是不是录入学生后,现在以8月1日为准,超过此日期返回true
     */
    private boolean isPastHalf(Date now) {
        String monthDay = DateFormatUtils.format(now, "MMdd");
        return monthDay.compareTo(ClassConstants.YEAR_SEPARATOR) >= 0;
    }

    //MTeacherDTO转换到Teacher
    private MTeacher parseToMTeacher(TeacherDTO teacherDTO) {
        MTeacher mTeacher = new MTeacher();
        BeanUtils.copyProperties(teacherDTO, mTeacher);
        return mTeacher;
    }

    /**
     * APP接口：添加或修改教材（直接替换）
     */
    @Override
    public void updateBook(String userId, String oldBookId, String newBookId, Integer isMain) {
        String schoolId = userService.getSchoolId(userId);
        if (StringUtils.isNotEmpty(oldBookId)) {//修改
            if (isMain == SearchConstants.ISNOTMAIN) {//非主授教材
                teacherMapper.deleteBookIdByUserId(userId, oldBookId, schoolId);
                teacherMapper.deleteBookIdByUserId(userId, newBookId, schoolId);
                saveTeacherBook(userId, newBookId, SearchConstants.ISNOTMAIN);
            } else {
                teacherMapper.deleteBookIdByUserId(userId, oldBookId, schoolId);
                teacherMapper.deleteBookIdByUserId(userId, newBookId, schoolId);
                saveTeacherBook(userId, newBookId, SearchConstants.ISMAIN);
                teacherMapper.updateTeacherBookId(userId, newBookId, schoolId);
            }
        } else {//添加
            if (isMain == SearchConstants.ISNOTMAIN) {//非主授教材
                teacherMapper.deleteBookIdByUserId(userId, newBookId, schoolId);
                saveTeacherBook(userId, newBookId, SearchConstants.ISNOTMAIN);
            } else {//主授教材
                teacherMapper.deleteByUserIdAndisMain(userId, SearchConstants.ISMAIN, schoolId);
                teacherMapper.deleteBookIdByUserId(userId, newBookId, schoolId);
                saveTeacherBook(userId, newBookId, SearchConstants.ISMAIN);
                teacherMapper.updateTeacherBookId(userId, newBookId, schoolId);
            }
        }
    }

    /**
     * 查询是否是主授教材
     *
     * @param userId   用户ID
     * @param bookId   教材ID
     * @param schoolId 学校ID
     * @return isMain 是否是主授教材
     */
    @Override
    public Integer isMain(String userId, String bookId, String schoolId) {
        return teacherMapper.isMain(userId, bookId, schoolId);
    }

    /**
     * APP接口：修改主授教材(原主授课本保留变为非主授课本)
     */
    @Override
    public void updateTeacherBook(String userId, String newBookId) {
        String schoolId = userService.getSchoolId(userId);
        //修改老师课本关联表
        teacherMapper.updateTeacherBookIsMain(userId, SearchConstants.ISNOTMAIN, schoolId);
        teacherMapper.deleteBookIdByUserId(userId, newBookId, schoolId);
        saveTeacherBook(userId, newBookId, SearchConstants.ISMAIN);
        //修改老师表
        teacherMapper.updateTeacherBookId(userId, newBookId, schoolId);
    }

    //保存老师课本关联表
    private void saveTeacherBook(String userId, String BookId, Integer isMain) {
        MTeacherBook teacherBook = new MTeacherBook();
        Long id = seqGeneratorService.generate("m_teacher_book");
        teacherBook.setId(id);
        teacherBook.setUserId(userId);
        teacherBook.setBookId(BookId);
        teacherBook.setSchoolId(userService.getSchoolId(userId));
        teacherBook.setIsMain(isMain);
        teacherMapper.saveTeacherBook(teacherBook);
    }

    /**
     * APP接口：删除已授教材，不能删除主授教材
     */
    @Override
    public void deleteMaterialForUser(String userId, String bookId) {
        String schoolId = userService.getSchoolId(userId);
        MTeacher teacher = teacherMapper.getByUserIdAndBookId(userId, bookId, schoolId);
        MemberAssert.isTrue(teacher == null, "主授教材不能删除", MemberLogicContext.ERROR_CODE_BOOK_ID_IS_EXIST);
        teacherMapper.deleteBookIdByUserId(userId, bookId, schoolId);
    }

    /**
     * APP接口：根据科目ID查询老师信息（有全校、管理层）
     */
    @Override
    public List<UserTeacherDTO> queryUsersBySubIdOrName(String subjectId, String userId) {
        String schoolId = userService.getSchoolId(userId);
        List<MUserTeacher> userTeacherList;
        if (SearchConstants.SCHOOLID.equals(subjectId)) {//全校
            userTeacherList = teacherMapper.selectTeacherWithoutSubjectId(null, schoolId);
        } else if (SearchConstants.MANAGERID.equals(subjectId)) {//管理层
            userTeacherList = teacherMapper.getAllManager(schoolId, null);
        } else {//根据科目ID查询
            userTeacherList = teacherMapper.selectTeacherWithSubjectId(null, subjectId, schoolId);
        }
        List<UserTeacherDTO> userTeacherAppDTOList = new ArrayList<>();
        for (MUserTeacher teacher : userTeacherList) {
            UserTeacherDTO utdto = new UserTeacherDTO();
            BeanUtils.copyProperties(teacher, utdto);
            userTeacherAppDTOList.add(utdto);
        }
        return userTeacherAppDTOList;
    }

    /**
     * APP接口：根据老师名称查询老师
     */
    @Override
    public List<UserTeacherDTO> queryTeacherByRealName(String realName, String userId) {
        String schoolId = userService.getSchoolId(userId);
        List<MUserTeacher> userTeacherList = teacherMapper.selectTeacherWithoutSubjectId(realName, schoolId);
        List<UserTeacherDTO> teacherAppDTOList = new ArrayList<>();
        for (MUserTeacher teacher : userTeacherList) {
            UserTeacherDTO tadto = new UserTeacherDTO();
            BeanUtils.copyProperties(teacher, tadto);
            teacherAppDTOList.add(tadto);
        }
        return teacherAppDTOList;
    }

    /**
     * APP接口：删除老师授课班级
     */
    @Override
    public void delTeacherClass(String userId, String classId) {
        ClassDTO classDTO = classService.getByClassId(classId);
        if (userId.equals(classDTO.getChargeId())) {
            throw new MemberException("班主任不可删除任课班级");
        }
        String schoolId = classService.getSchoolId(classId);
        //删除老师班级关联表
        classMapper.delByClassIdAndUserIdAndSchoolId(classId, userId, schoolId);
    }

    /**
     * 根据老师ID获取老师信息
     */
    @Override
    public MyMessageDTO getMyMessageTeacher(String userId, String schoolId) {
        MyMessageDTO messageDTO = new MyMessageDTO();
        //1、获取用户信息
        UserDTO users = userService.getByUserId(userId);
        if (users == null) {
            throw new MemberException("该用户不存在");
        }
        messageDTO.setUserId(users.getUserId());
        messageDTO.setRealName(users.getRealName());
        messageDTO.setIcon(users.getIcon());
        messageDTO.setSignature(users.getSignature());
        MSchool school = schoolMapper.selectByPrimaryKey(schoolId);
        if (school == null) {
            throw new MemberException("该用户没有学校信息");
        }
        messageDTO.setSchoolName(school.getName());
        //2、获取老师所有授课科目
        List<CtBookInfo> bookList = bookMapper.queryTeacherSubjectByUserId(userId, schoolId);
        List<CtBookDTO> bookAppDTOList = new ArrayList<>();
        for (CtBookInfo bookInfo : bookList) {
            CtBookDTO bookDTO = new CtBookDTO();
            BeanUtils.copyProperties(bookInfo, bookDTO);
            bookAppDTOList.add(bookDTO);
        }
        messageDTO.setSubjectList(bookAppDTOList);
        //3、获取所有授课班级
        List<ClassInfoDTO> classInfoDTOList = queryTeacherClass(userId);
        messageDTO.setClassList(classInfoDTOList);
        return messageDTO;
    }

    /**
     * 修改教师信息，包括主授课本bookId、学校职务和职务id、姓名、手机、邮箱
     * 任课班级
     *
     * @param {@link TeacherClassDTO} 所有信息
     */
    @Override
    public TeacherClassDTO saveEditTeacherForAPP(TeacherClassDTO teacherClassDTO) {
        String userId = teacherClassDTO.getUserId();
        String schoolId = teacherClassDTO.getSchoolId();
        String realName = teacherClassDTO.getRealName();
        String mobile = teacherClassDTO.getMobile();
        String email = teacherClassDTO.getEmail();
        if (StringUtils.isEmpty(userId)) {
            logger.debug("请确认userId不为空");
            return null;
        }
        boolean isAllNull = true;
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userId);
        if (!StringUtils.isEmpty(schoolId)) {
            userDTO.setSchoolId(schoolId);
            isAllNull = false;
        } else {
            schoolId = userService.getSchoolId(userId);
        }
        if (!StringUtils.isEmpty(realName)) {
            userDTO.setRealName(realName);
            isAllNull = false;
        }
        if (!StringUtils.isEmpty(mobile)) {
            userDTO.setMobile(mobile);
            isAllNull = false;
        }
        if (!StringUtils.isEmpty(email)) {
            userDTO.setEmail(email);
            isAllNull = false;
        }
        if (!isAllNull) {
            userService.updateByUserId(userDTO);
        }
        String bookId = teacherClassDTO.getBookId();
        if (!StringUtils.isEmpty(bookId)) {
            updateTeacherBook(userId, bookId);
        }
        String positionId = teacherClassDTO.getPositionId();
        String positionName;
        if (!StringUtils.isEmpty(positionId)) {
            positionName = mPositionMapper.selectByPrimaryKey(positionId).getName();
            teacherMapper.updateTeacherMessage(userId, positionName, positionId, userService.getSchoolId(userId));
        }

        List<MTeacherClass> lists = new ArrayList<>();
        if (teacherClassDTO.getClassIds() != null) {
            for (String classId : teacherClassDTO.getClassIds()) {
                MTeacherClass mTeacherClass = new MTeacherClass();
                Long id = seqGeneratorService.generate("m_teacher_class");
                mTeacherClass.setId(id);
                mTeacherClass.setClassId(classId);
                mTeacherClass.setSchoolId(schoolId);
                mTeacherClass.setTeacherId(userId);
                mTeacherClass.setId(seqGeneratorService.generate("m_teacher_class"));
                lists.add(mTeacherClass);
            }
            teacherMapper.deleteByTeacherIdAndSchoolId(userId, schoolId);
            teacherMapper.insertTeacherClass(lists);
        }
        //班主任修改
        List<MClass> mClasses = new ArrayList<>();
        if (teacherClassDTO.getChargeClassIds() != null) {
            for (String str : teacherClassDTO.getChargeClassIds()) {
                MClass mClass = new MClass();
                mClass.setClassId(str);
                mClasses.add(mClass);
            }
            classMapper.updateClassTeacherList(mClasses, userId, realName);
        }
        return teacherClassDTO;
    }

    @Override
    public TeacherClassDTO getTeacherForAPP(String userId, String schoolId) {
        if (StringUtils.isEmpty(userId)) {
            logger.debug("userId不能为空");
            return null;
        }
        TeacherClassDTO teacherClassDTO = new TeacherClassDTO();
        //查询用户联系方式信息
        UserDTO muser = userService.getByUserId(userId);
        if (muser == null) {
            logger.debug("该用户不存在");
            throw new MemberException("该用户不存在");
        }
        teacherClassDTO.setRealName(muser.getRealName());
        teacherClassDTO.setEmail(muser.getEmail());
        teacherClassDTO.setMobile(muser.getMobile());
        if (StringUtils.isEmpty(schoolId)) {
            logger.debug("学校最好不为空");
            schoolId = muser.getSchoolId();
        }
        if (StringUtils.isEmpty(schoolId)) {
            logger.debug("该用户数据有误");
            throw new MemberException("该用户数据有误");
        }
        //区域信息
        MSchool mSchool = schoolMapper.selectByPrimaryKey(schoolId);
        if (mSchool == null) {
            logger.debug("用户下的schoolId错误！");
            throw new MemberException("数据有误！");
        }
        teacherClassDTO.setSchoolName(mSchool.getName());
        teacherClassDTO.setAreaCode(mSchool.getArea());
        teacherClassDTO.setAreaName(mSchool.getAreaName());
        teacherClassDTO.setSchoolId(schoolId);
        teacherClassDTO.setSchoolName(mSchool.getName());
        //查询所有的任课班级
        List<MClassInfo> lists = teacherMapper.getTeacherClass(userId, schoolId);
        if (CollectionUtils.isNotEmpty(lists)) {
            List<String> classIds = new ArrayList<>();
            List<ClassDTO> classes = new ArrayList<>();
            for (MClassInfo mClassInfo : lists) {
                String classId = mClassInfo.getClassId();
                classIds.add(classId);
                //根据classIds查询班级信息
                MClass mClass = mClassMapper.selectByPrimaryKey(classId, schoolId);
                if (mClass == null) {
                    logger.error("班级不存在");
                    continue;
                }
                ClassDTO classDTO = new ClassDTO();
                classDTO.setChargeId(mClass.getChargeId());
                classDTO.setChargeName(mClass.getChargeName());
                classDTO.setClassId(classId);
                classDTO.setCodeSharing(mClass.getCodeSharing());
                classDTO.setGrade(ClassNameUtil.getGradeNum(mClass.getYears()));
                classes.add(classDTO);
            }
            teacherClassDTO.setClasses(classes);
            teacherClassDTO.setClassIds(classIds);
        }
        //查询教师信息 职务、课本、科目
        MUserTeacher mUserTeacher = teacherMapper.selectChief(userId, schoolId);
        if (mUserTeacher != null) {
            teacherClassDTO.setPositionId(mUserTeacher.getPositionId());
            teacherClassDTO.setPositionName(mUserTeacher.getPositionName());
            teacherClassDTO.setBookId(mUserTeacher.getBookId());
            teacherClassDTO.setBookName(mUserTeacher.getBookName());
            teacherClassDTO.setSubjectId(mUserTeacher.getSubjectId());
            teacherClassDTO.setSubjectName(mUserTeacher.getSubjectName());
        }
        //根据班主任id查询classId
        List<String> chargeClassIds = classMapper.getClassIdsByChargeId(userId);
        teacherClassDTO.setChargeClassIds(chargeClassIds);
        teacherClassDTO.setUserId(userId);
        return teacherClassDTO;
    }

    /**
     * 获取教师主授课本是否能布置提分宝作业与同步课堂作业
     * 1小学语、数、英、科学(可以布置同步课堂) 2初中数理化（可以布置提分宝） 0其他
     * 信息技术不分学段返回1
     */
    @Override
    public int getTeacherPeriodType(String schoolId, String bookId) {
        //没有主授课本
        if (StringUtils.isEmpty(bookId)) {
            return 0;
        }
        //获取课本信息
        CtBook bookDTO = bookMapper.queryByPrimaryKey(bookId);
        if (bookDTO == null) {
            return 0;
        }
        if(bookDTO.getSubjectId().endsWith("150")){
            return 1;
        }
        //获取某学校的小学学段
        int primary = getFirstGradeMiddleSchool(schoolId);
        if (primary != 0) {
            if (bookDTO.getGrade() <= primary && bookDTO.getGrade() > 0
                    && (bookDTO.getSubjectId().endsWith("010") || bookDTO.getSubjectId().endsWith("020") || bookDTO.getSubjectId().endsWith("030")
                    || bookDTO.getSubjectId().endsWith("040"))) {
                return 1;
            } else if (bookDTO.getGrade() > primary && bookDTO.getGrade() <= 9
                    && (bookDTO.getSubjectId().endsWith("020") || bookDTO.getSubjectId().endsWith("050") || bookDTO.getSubjectId().endsWith("060"))) {
                return 2;
            }
        }
        return 0;
    }

    @Override
    public List<GradeDTO> queryGradeByTeacher(String schoolId, String userId) {
        ClassDTO classDTO = new ClassDTO();
        classDTO.setSchoolId(schoolId);
        List<GradeDTO> gradeDTOs = classService.queryGrades(classDTO);
        List<ClassInfoDTO> classInfoDTOList = queryTeacherClass(userId);
        List<GradeDTO> gradeDTOList = new ArrayList<>();
        Map classInfoMap = new HashMap();
        for (ClassInfoDTO classInfoDTO : classInfoDTOList) {
            classInfoMap.put(classInfoDTO.getYear(), classInfoDTO.getYear());
        }
        for (GradeDTO gradeDTO : gradeDTOs) {
            if (classInfoMap.get(gradeDTO.getId()) != null) {
                gradeDTOList.add(gradeDTO);
            }
        }
        return gradeDTOList;
    }

    @Override
    public List<ClassInfoDTO> queryTeacherClassByGrade(String userId, int year) {
        List<ClassInfoDTO> classInfoDTOList = queryTeacherClass(userId);
        List<ClassInfoDTO> classInfoDTOs = new ArrayList<>();
        for (ClassInfoDTO classInfoDTO : classInfoDTOList) {
            if (classInfoDTO.getYear() == year) {
                classInfoDTOs.add(classInfoDTO);
            }
        }
        return classInfoDTOs;
    }

    //根据学校ID获取学校的小学学段
    private int getFirstGradeMiddleSchool(String schoolId) {
        List<MSchoolPeriod> spDTOList = schoolPeriodMapper.selectBySchoolId(schoolId);
        if (CollectionUtils.isEmpty(spDTOList)) {
            throw new MemberException("学校未设置学制学段");
        }
        for (MSchoolPeriod spDTO : spDTOList) {
            if (spDTO.getSection() == SectionConstants.PRIMARY_SCHOOL_NUM) {//小学
                return spDTO.getLength();//小学学段
            }
            if (spDTO.getSection() == SectionConstants.JUNIOR_MIDDLE_SCHOOL_NUM) {//初中
                return 9 - spDTO.getLength();//小学学段
            }
        }
        return 0;
    }


}
