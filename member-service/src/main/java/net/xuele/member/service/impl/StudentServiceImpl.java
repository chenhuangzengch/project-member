package net.xuele.member.service.impl;


import com.alibaba.dubbo.common.utils.CollectionUtils;
import net.xuele.common.dto.ClassInfoDTO;
import net.xuele.common.exceptions.MemberException;
import net.xuele.common.page.Page;
import net.xuele.common.page.PageResponse;
import net.xuele.common.utils.PageUtils;
import net.xuele.member.constant.CacheConstants;
import net.xuele.member.constant.ClassConstants;
import net.xuele.member.constant.IdentityIdConstants;
import net.xuele.member.domain.*;
import net.xuele.member.dto.*;
import net.xuele.member.dto.page.StudentPageRequest;
import net.xuele.member.persist.*;
import net.xuele.member.service.*;
import net.xuele.member.util.ClassNameUtil;
import net.xuele.member.util.DateUtil;
import net.xuele.member.util.NumberUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhongjian.xu on 2015/6/24 0024.
 */

public class StudentServiceImpl implements StudentService {
    @Autowired
    private MStudentMapper studentMapper;
    @Autowired
    private MClassMapper classMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private MFamilyRelationMapper famillyRelationMapper;
    @Autowired
    private MUsersMapper usersMapper;
    @Autowired
    private MSchoolMapper mSchoolMapper;
    @Autowired
    private ClassService classService;
    @Autowired
    private FamilyRelationService familyRelationService;
    @Autowired
    private CacheService cacheService;

    private static Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    /**
     * 班级添加学生
     *
     * @param userId  学乐号
     * @param classId 班级ID
     */
    @Override
    public int saveStudents(String userId, String classId) {
        String[] userIds = userId.split(",");
        String schoolId = classService.getSchoolId(classId);
        MClass mClass = classMapper.selectByPrimaryKey(classId, schoolId);
        int in = studentMapper.updateStudents(Arrays.asList(userIds), mClass);
        //学生换班，删除该家长的缓存信息
        deleteRedisOfParent(Arrays.asList(userIds), schoolId);
        return in;
    }

    /*
     * 学生换班，根据多个孩子id删除家长在缓存中的数据
     * @param userIdList 孩子id列表
     * @param schoolId   孩子的学校id
     */
    private void deleteRedisOfParent(List<String> userIdList, String schoolId) {
        //获取孩子的家长信息
        List<MFamilyRelation> frList = famillyRelationMapper.queryParentOfKidList(userIdList, schoolId);
        deleteRedis(frList);
    }

    /*
     * 学生换班，管理员修改名称，根据单个孩子id删除家长在缓存中的数据
     * @param userId   孩子id
     * @param schoolId 孩子的学校id
     */
    private void deleteRedisOfParent(String userId, String schoolId) {
        //获取孩子的家长信息
        List<MFamilyRelation> frList = famillyRelationMapper.queryFamilyByStudent(userId, schoolId);
        deleteRedis(frList);
    }

    //学生换班，管理员修改名称，删除该家长的缓存信息
    private void deleteRedis(List<MFamilyRelation> frList) {
        List<String> keyList = new ArrayList<>();
        for (MFamilyRelation fr : frList) {
            keyList.add(MessageFormat.format(CacheConstants.KEY_KID_OF_PARENT, fr.getUserId()));
            keyList.add(MessageFormat.format(CacheConstants.KEY_FAMILY_RELATION, fr.getUserId()));
            keyList.add(MessageFormat.format(CacheConstants.KEY_KID_NAME_OF_PARENT, fr.getUserId()));
        }
        if (CollectionUtils.isNotEmpty(keyList)) {
            cacheService.delete(keyList);
        }
    }

    @Override
    public StudentManagerDTO saveStudent(String name, String classId, String schoolId) {
        UserDTO userDTO = new UserDTO();
        userDTO.setRealName(name);
        userDTO.setIdentityId(IdentityIdConstants.STUDENT);
        userDTO.setIdentityDescription("学生");
        userDTO.setSchoolId(schoolId);
        userDTO.setArea(mSchoolMapper.selectByPrimaryKey(schoolId).getArea());
        UserDTO users = userService.saveUser(userDTO);
        MClass mclass = classMapper.selectByPrimaryKey(classId, schoolId);
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setUserId(users.getUserId());
        studentDTO.setSchoolId(mclass.getSchoolId());
        studentDTO.setSchoolName(mclass.getSchoolName());
        studentDTO.setClassName(mclass.getName());
        studentDTO.setClassId(classId);
        studentDTO.setClassAliasName(mclass.getAliasName());
        studentDTO.setJoinClass(new Date());
        MStudent mStudent = new MStudent();
        BeanUtils.copyProperties(studentDTO, mStudent);
        studentMapper.insert(mStudent);
        MStudentManager studentManager = studentMapper.selectStudentByUserId(mStudent.getUserId(), schoolId);
        StudentManagerDTO studentManagerDTO = new StudentManagerDTO();
        BeanUtils.copyProperties(studentManager, studentManagerDTO);
        studentManagerDTO.setClassAliasName(ClassNameUtil.getFullName(studentManager.getYears(), studentManager.getClassAliasName()));
        return studentManagerDTO;
    }

    /**
     * APP接口、WEB平台：根据学生名称查询学生信息
     */
    @Override
    public List<StudentManagerDTO> queryStudentByRealName(String realName, String userId) {
        if (StringUtils.isEmpty(userId)) {
            logger.debug("userId 不能为空");
            return null;
        }
        String schoolId = userService.getSchoolId(userId);
        List<MStudentManager> userList = studentMapper.selectStudentByRealName(realName, schoolId);
        List<StudentManagerDTO> userDTOList = new ArrayList<>();
        for (MStudentManager u : userList) {
            StudentManagerDTO userDTO = new StudentManagerDTO();
            BeanUtils.copyProperties(u, userDTO);
            userDTOList.add(userDTO);
        }
        return userDTOList;
    }

    /**
     * 查询无班级学生信息
     */
    @Override
    public List<StudentManagerDTO> queryStudentsWithoutClassId(String schoolId) {
        List<MStudentManager> usersList = studentMapper.selectStudentsWithoutClassId(null, schoolId);
        List<StudentManagerDTO> userDTOList = new ArrayList<>();
        for (MStudentManager user : usersList) {
            StudentManagerDTO userDTO = new StudentManagerDTO();
            BeanUtils.copyProperties(user, userDTO);
            userDTOList.add(userDTO);
        }
        return userDTOList;
    }

    /**
     * APP接口、WEB平台：根据班级ID查询该班级所有学生
     */
    @Override
    public List<StudentManagerDTO> queryClassStudents(String classId) {
        String schoolId = classService.getSchoolId(classId);
        List<MStudentManager> userList = studentMapper.selectClassStudents(classId, schoolId);
        List<StudentManagerDTO> userDTOList = new ArrayList<>();
        for (MStudentManager u : userList) {
            StudentManagerDTO userDTO = new StudentManagerDTO();
            BeanUtils.copyProperties(u, userDTO);
            userDTOList.add(userDTO);
        }
        return userDTOList;
    }

    /**
     * 查询学生信息分页
     */
    @Override
    public PageResponse<StudentManagerDTO> queryStudentPage(StudentPageRequest studentPageRequest) {
        String schoolId = studentPageRequest.getSchoolId();
        if (schoolId == null) {
            schoolId = userService.getSchoolId(studentPageRequest.getUserId());
            studentPageRequest.setSchoolId(schoolId);
        }
        //获取学生信息的总条数
        MStudentManager studentManager = new MStudentManager();
        BeanUtils.copyProperties(studentPageRequest, studentManager);
        long count = studentMapper.selectCount(studentManager);
        //获取当前页的信息
        Page page = PageUtils.buildPage(studentPageRequest);
        List<MStudentManager> mStudentList = studentMapper.selectMStudentManagerPage(page, studentManager);
          /*
        给返回接口赋值
         */
        List<StudentManagerDTO> studentManagerDTOs = new ArrayList<>();
        for (MStudentManager mstudents : mStudentList) {
            StudentManagerDTO studentManagerDTO = new StudentManagerDTO();
            BeanUtils.copyProperties(mstudents, studentManagerDTO);
            studentManagerDTO.setLastLoginDate(mstudents.getLastLoginDate() == null ? "从未登录" : DateFormatUtils.format(mstudents.getLastLoginDate(), "yyyy-MM-dd HH:mm"));
            if (mstudents.getYears() != null) {
                studentManagerDTO.setClassAliasName(getGrade(mstudents.getYears()) + studentManagerDTO.getClassAliasName());
            } else {
                studentManagerDTO.setClassAliasName("无班级");
            }
            studentManagerDTOs.add(studentManagerDTO);
        }

        //返回对象
        PageResponse<StudentManagerDTO> pageResponse = new PageResponse<>();
        PageUtils.buldPageResponse(studentPageRequest, pageResponse);
        pageResponse.setRows(studentManagerDTOs);
        pageResponse.setRecords(count);
        return pageResponse;

    }

    //根据学界获取年级名称
    private String getGrade(int y) {
        Date now = new Date();
        int pastHalf = isPastHalf(now) ? 1 : 0;
        int currentYear = DateUtil.getYear(now);
        int year = currentYear + pastHalf - y;
        return NumberUtil.getSimpleNumber(year) + "年级";
    }

    /**
     * 这个时刻是不是录入学生后,现在以8月1日为准,超过此日期返回true
     */
    private boolean isPastHalf(Date now) {
        String monthDay = DateFormatUtils.format(now, "MMdd");
        return monthDay.compareTo(ClassConstants.YEAR_SEPARATOR) >= 0;
    }

    /**
     * 导出学生信息
     */
    @Override
    public List<ExcelInfo> queryExcelInfo(StudentPageRequest studentPageRequest) {
        Map<Integer, ExcelInfo> map = new TreeMap<>();
        String schoolId = studentPageRequest.getSchoolId();
        if (schoolId == null) {
            schoolId = userService.getSchoolId(studentPageRequest.getUserId());
            studentPageRequest.setSchoolId(schoolId);
        }
        //获取学生信息的总条数
        MStudentManager studentManager = new MStudentManager();
        BeanUtils.copyProperties(studentPageRequest, studentManager);
        List<MStudentManager> mStudentList = studentMapper.selectStudents_export(studentManager);
        for (MStudentManager students : mStudentList) {
            List<List<Object>> data;
            List<Object> row = new ArrayList<>();
            row.add(students.getRealName());
            if (students.getYears() != null) {
                int grade = ClassNameUtil.getGradeNum(students.getYears());
                ExcelInfo excelInfo = map.get(grade);
                if (excelInfo == null) {
                    excelInfo = new ExcelInfo();
                    data = new ArrayList<>();
                    excelInfo.setData(data);
                    excelInfo.setSheetName(grade + "年级");
                    excelInfo.setHeaders(new String[]{"学生姓名", "所在班级", "登录账号", "上线情况"});
                    excelInfo.setColumnWidth(20);
                    map.put(grade, excelInfo);
                } else {
                    data = excelInfo.getData();
                }
                row.add(grade + "年级" + students.getClassAliasName());
            } else {
                ExcelInfo excelInfo = map.get(0);
                if (excelInfo == null) {
                    excelInfo = new ExcelInfo();
                    data = new ArrayList<>();
                    excelInfo.setData(data);
                    excelInfo.setSheetName("无班级");
                    excelInfo.setColumnWidth(20);
                    excelInfo.setHeaders(new String[]{"学生姓名", "所在班级", "登录账号", "上线情况"});
                    map.put(0, excelInfo);
                } else {
                    data = excelInfo.getData();
                }
                row.add("无班级");
            }
            row.add(students.getUserId());
            row.add(DateUtil.getLogin_date(students.getLastLoginDate()));
            data.add(row);
        }
        return new ArrayList<>(map.values());
    }

    /**
     * APP接口：根据学生ID获取该学生所在的班级信息
     */
    @Override
    public ClassInfoDTO queryUserClass(String userId) {
        String schoolId = userService.getSchoolId(userId);
        MClassInfo studentClass = studentMapper.getStudentClass(userId, schoolId);
        ClassInfoDTO tcdto = new ClassInfoDTO();
        if (studentClass != null) {
            BeanUtils.copyProperties(studentClass, tcdto);
            //设置年级号
            tcdto.setGradeNum(ClassNameUtil.getGradeNum(studentClass.getYear()));
            //设置学期
            tcdto.setSemester(ClassNameUtil.getSemester());
            //设置班级名称 年级+班级
            tcdto.setClassName(ClassNameUtil.getFullName(studentClass.getYear(), studentClass.getClassName()));
        }
        return tcdto;
    }

    /**
     * 通知用
     * 根据学生Id查询家庭成员
     */
    @Override
    public List<FamilyRelationDTO> queryFamilyByStudent(String userId) {
        String key = MessageFormat.format(CacheConstants.KEY_PARENT_OF_KID, userId);
        List<FamilyRelationDTO> list=cacheService.get(key);
        if(list!=null){
            return list;
        }
        List<FamilyRelationDTO> familyRelationDTOs = new ArrayList<>();
        List<MFamilyRelation> mFamilyRelations = famillyRelationMapper.queryFamilyByStudent(userId,
                userService.getSchoolId(userId));
        for (MFamilyRelation fr : mFamilyRelations) {
            FamilyRelationDTO familyRelationDTO = new FamilyRelationDTO();
            BeanUtils.copyProperties(fr, familyRelationDTO);
            familyRelationDTOs.add(familyRelationDTO);
        }
        cacheService.set(key, familyRelationDTOs, CacheConstants.CACHE_SEVEN_DAY, TimeUnit.DAYS);
        return familyRelationDTOs;
    }

    //根据年份获取年级号
    private int getGradeNum(int year) {
        Date now = new Date();
        int pastHalf = isPastHalf(now) ? 1 : 0;
        int currentYear = DateUtil.getYear(now);
        return currentYear + pastHalf - year;
    }

    /**
     * 修改学生信息
     */
    @Override
    public StudentManagerDTO updateStudentInfo(String userId, String realName, String classId) {
        String schoolId = userService.getSchoolId(userId);
        //更换用户名称
        usersMapper.updateUserName(userId, realName, schoolId);
        //更换班级
        MClass mClass = classMapper.selectByPrimaryKey(classId, schoolId);
        studentMapper.updateStudentMessage(userId, mClass);
        //修改家庭关系表中用户名称
        familyRelationService.updateName(userId, realName);
        MStudentManager studentManager = studentMapper.selectStudentByUserId(userId, schoolId);
        StudentManagerDTO studentManagerDTO = new StudentManagerDTO();
        BeanUtils.copyProperties(studentManager, studentManagerDTO);
        studentManagerDTO.setClassAliasName(getGrade(mClass.getYears()) + studentManagerDTO.getClassAliasName());
        //修改孩子名称，班级信息，删除缓存数据
        deleteRedisOfParent(userId, schoolId);
        cacheService.delete(MessageFormat.format(CacheConstants.KEY_USER, userId));
        return studentManagerDTO;
    }

    @Override
    public StudentDTO updateStudentForAPP(StudentDTO studentDTO) {
        //更新联系方式
        UserDTO userDTO = new UserDTO();
        boolean isAllNot = true;
        //学生姓名
        String name = studentDTO.getRealName();
        if (!StringUtils.isEmpty(name)) {
            userDTO.setRealName(name);
            isAllNot = false;
        }

        String school = studentDTO.getSchoolId();
        if (school != null) {
            userDTO.setSchoolId(school);
            isAllNot = false;
        } else {
            logger.warn("schoolId不能为空");
            return null;
        }

        String userId = studentDTO.getUserId();
        if (!StringUtils.isEmpty(userId)) {
            userDTO.setUserId(userId);
        } else {
            logger.warn("userId不能为空");
            return null;
        }

        String mobile = studentDTO.getMobile();
        if (!StringUtils.isEmpty(mobile)) {
            userDTO.setMobile(mobile);
            isAllNot = false;
        }

        String email = studentDTO.getEmail();
        if (!StringUtils.isEmpty(email)) {
            userDTO.setEmail(email);
            isAllNot = false;
        }

        if (!isAllNot) {
            userService.updateByUserId(userDTO);
        }

        //更新班级
        String schoolId = userService.getSchoolId(userId);
        if (StringUtils.isEmpty(schoolId)) {
            throw new MemberException("该学生没有学校");
        }

        MSchool mSchool = mSchoolMapper.selectByPrimaryKey(schoolId);

        String classId = studentDTO.getClassId();
        if (!StringUtils.isEmpty(classId)) {
            MClass mClass = classMapper.selectByPrimaryKey(classId, schoolId);
            if (mClass == null) {
                logger.warn("找不到班级号为" + classId + "的班级");
                return null;
            }
            MStudent mStudent = new MStudent();
            mStudent.setUserId(studentDTO.getUserId());
            mStudent.setClassName(mClass.getName());
            mStudent.setClassId(classId);
            mStudent.setSchoolId(schoolId);

            studentMapper.updateByPrimaryKey(mStudent);
        }

        studentDTO.setClassId(classId);
        studentDTO.setSchoolId(schoolId);
        studentDTO.setSchoolName(mSchool.getName());
        return studentDTO;
    }

    @Override
    public StudentDTO queryStudentForAPP(String userId, String schoolId) {
        UserDTO mUsers = userService.getByUserId(userId);
        if (mUsers == null) {
            logger.debug("未找到学乐号为" + userId + "的学生用户");
            return null;
        }

        MStudent mStudent = studentMapper.selectByPrimaryKey(userId, schoolId);
        if (mStudent == null) {
            logger.debug("未找到学生id为" + userId + "的学生");
            throw new MemberException("未找到学生id为" + userId + "的学生");
        }
        // 学生信息
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setUserId(userId);
        studentDTO.setRealName(mUsers.getRealName());
        studentDTO.setSchoolId(mStudent.getSchoolId());
        studentDTO.setSchoolName(mStudent.getSchoolName());
        studentDTO.setClassId(mStudent.getClassId());
        studentDTO.setClassName(mStudent.getClassName());
        studentDTO.setMobile(mUsers.getMobile());
        studentDTO.setEmail(mUsers.getEmail());

        //学校所在区域信息
        MSchool mSchool = mSchoolMapper.selectByPrimaryKey(mUsers.getSchoolId());
        if (mSchool == null) {
            logger.debug("未找到学乐号为" + userId + "的学生用户所在的学校");
            return null;
        }
        studentDTO.setAreaCode(mSchool.getArea());
        studentDTO.setAreaName(mSchool.getAreaName());

        //班级信息
        MClass mClass = classMapper.selectByPrimaryKey(mStudent.getClassId(), schoolId);
        if (mClass == null) {
            logger.debug("未找到学乐号为" + userId + "的学生用户所在的班级");
            return null;
        }
        studentDTO.setGradeNum(getGradeNum(mClass.getYears()));
        studentDTO.setYear(mClass.getYears());
        return studentDTO;
    }


    /**
     * 根据学生ID获取学生信息
     */
    @Override
    public MyMessageDTO getMyMessageStudent(String userId) {
        String schoolId = userService.getSchoolId(userId);
        MStudentInfo studentInfo = studentMapper.getStudentInfo(userId, schoolId);
        if (studentInfo == null) {
            throw new MemberException("该学生不存在");
        }
        MyMessageDTO studentInfoDTO = new MyMessageDTO();
        BeanUtils.copyProperties(studentInfo, studentInfoDTO);
        if (studentInfo.getClassId() != null) {
            studentInfoDTO.setClassName(ClassNameUtil.getFullName(studentInfo.getYear(), studentInfo.getClassName()));
        }
        return studentInfoDTO;
    }

}
