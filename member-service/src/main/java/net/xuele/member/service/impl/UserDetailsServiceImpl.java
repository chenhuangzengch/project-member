package net.xuele.member.service.impl;


import net.xuele.common.dto.ClassInfoDTO;
import net.xuele.common.exceptions.MemberException;
import net.xuele.common.security.UserSession;
import net.xuele.common.security.XuleleGrantedAuthority;
import net.xuele.member.constant.IdentityIdConstants;
import net.xuele.member.constant.SectionConstants;
import net.xuele.member.constant.UserConstants;
import net.xuele.member.domain.*;
import net.xuele.member.persist.*;
import net.xuele.member.service.ResourceService;
import net.xuele.member.service.SchoolPeriodService;
import net.xuele.member.service.UserService;
import net.xuele.member.util.ClassNameUtil;
import net.xuele.teacheval.constants.AccessAction;
import net.xuele.teacheval.domain.UserAccessAction;
import net.xuele.teacheval.service.WriteAccessLogService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.*;

/**
 * zhihuan.cai 新建于 2015/6/9 0009.
 */

public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    @Autowired
    private UserService userService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private CasLoginMapper casLoginMapper;
    @Autowired
    private MUserDetailMapper userDetailMapper;
    @Autowired
    private MEducationMapper educationMapper;
    @Autowired
    private MSchoolManagerMapper schoolManagerMapper;
    @Autowired
    private MParentsMapper parentsMapper;
    @Autowired
    private MSchoolBookMapper mSchoolBookMapper;
    @Autowired
    private SchoolPeriodService schoolPeriodService;

    @Autowired(required = false)
    private WriteAccessLogService writeAccessLogService;

    /**
     * 登录时填充信息有：
     * 1、用户信息 m_users中获得
     * 2、身份信息
     */
    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        logger.info("开始获取登录信息" + loginId);
        long start = System.currentTimeMillis();
        String userId = null;
        String schoolId = null;
        if (loginId.length() == 11 || loginId.contains("@") || loginId.length() < 8) {
            userId = casLoginMapper.getUserIdByLoginId(loginId);
            if (StringUtils.isEmpty(userId)) {
                logger.error("该登录号没有相关信息");
                throw new MemberException("该登录号没有相关信息");
            }
        } else {
            userId = loginId;
        }

        userId = userId.trim();
        schoolId = userService.getSchoolId(userId);

        if (schoolId == null) {
            logger.error("数据库中user_id为'{}'的用户school_id为null", userId);
            throw new MemberException("您的账号异常，请联系客服人员！");
        }

        //学校信息，角色信息和user信息
        MUserLoginInfo userInfo = userDetailMapper.getLoginInfo(userId, schoolId);

        if (userInfo == null) {
            logger.error("数据库中user_id为'{}'的用户数据存在问题", userId);
            throw new MemberException("您的账号异常，请联系客服人员！");
        }

        if (userInfo.getSchool() != null
                && userInfo.getSchool().getStatus() != 1) {
            logger.error("user_id为'{}'的用户学校已被删除", userId);
            throw new MemberException("您所在的学校已被删除");
        }

        //if (userInfo.getStatus() == 0){
        //    logger.error("user_id为'{}'的用户已离校",userId);
        //    throw new MemberException("您已离校,如有问题请联系学校管理员");
        //}

        List<GrantedAuthority> authList = new ArrayList<>();
        List<String> roleIds = new ArrayList<>();
        List<MRole> roles = userInfo.getRoles();
        if (roles != null) {
            for (MRole role : roles) {
                roleIds.add(role.getRoleId());
                authList.add(new XuleleGrantedAuthority(role.getRoleId()));
            }
        }
        if (roles == null || roles.isEmpty()) {
            logger.error("账号数据有问题，查询不到user_id为{}用户的roleId", userId);
            throw new MemberException("您的账号异常，请联系管理员或联系客服人员！");
        }
        UserSession userSession = new UserSession(userId, userId, authList);
        //先设置
        setInfoByIdentityId(userId, schoolId, userInfo, userSession);
        userSession.setSchoolId(schoolId);
        userSession.setRealName(userInfo.getRealName());
        userSession.setUserId(userId);
        String icon = userInfo.getIcon();
        icon = (icon == null ? UserConstants.ICON_DEFAULT : icon);
        userSession.setIcon(icon);
        userSession.setStatus(userInfo.getStatus());
        userSession.setAccountId(userInfo.getAccountId());
        MSchool mSchool = userInfo.getSchool();
        if (mSchool != null) {
            userSession.setSchoolName(mSchool.getName());
            userSession.setArea(mSchool.getArea());
            userSession.setAreaName(mSchool.getAreaName());
            userSession.setSchoolWeb(mSchool.getWeb());
        }


        //获取一级和二级菜单
        userSession.setResource(resourceService.getMenuRoleResourceFisrtAndSecond(roleIds));
        //登录日志(SessionInfoInterceptor)
        //获取个人面板(SessionInfoInterceptor)
        long end = System.currentTimeMillis();
        long time = end - start;
        logger.info("-----------------------------------------");
        logger.info("填充用户数据所需毫秒数：{} ", time);
        if (writeAccessLogService != null) {
            try {
                writeAccessLogService.writeAccessLog(new UserAccessAction.Builder()
                        .accessAction(AccessAction.Login)
                        .userId(userId)
                        .occurredTime(new Date().getTime())
                        .build());
            } catch (Exception e) {
                logger.error("writeAccessLogService调用异常{}", e.getMessage());
            }
        }
        return userSession;
    }

    //根据身份向usersession中填充相应的信息
    private void setInfoByIdentityId(String userId, String schoolId, MUserLoginInfo userInfo, UserSession userSession) {
        Map<String, Object> paramMap = new HashMap<>();
        if (userInfo.getSchool() != null) {
            paramMap.put("customWeb", userInfo.getSchool().getCustomWeb());
        }
        String bookId = null;
        String identityId = userInfo.getIdentityId().toUpperCase();
        userSession.setIdentityId(identityId);
        userSession.setIdentityDescription(userInfo.getIdentityDescription());
        switch (identityId) {
            //机构管理员
            case IdentityIdConstants.EDUCATION_MANAGER:
                MEducationManagerLoginInfo mEducationManagerLoginInfo = userDetailMapper.getEducationManagerInfo(userId, schoolId);
                CtBook book = mEducationManagerLoginInfo.getCtBook();
                if (book != null) {
                    bookId = book.getBookId();
                    userSession.setBookName(book.getBookName());
                    userSession.setSubjectId(book.getSubjectId());
                    userSession.setSubjectName(book.getSubjectName());
                }
                if (StringUtils.isEmpty(bookId)) {
                    bookId = UserConstants.DEFAULT_BOOK_ID;
                }
                MSchoolBook schoolBook = mSchoolBookMapper.selectByPrimaryKey(schoolId, bookId);
                if (schoolBook != null) {
                    userSession.setExtraBookId(schoolBook.getExtraBookId());
                }
                userSession.setBookId(bookId);
                userSession.setPositionId(mEducationManagerLoginInfo.getPositionId());
                userSession.setPositionName(mEducationManagerLoginInfo.getPositionName());
                userSession.setEducationalId(mEducationManagerLoginInfo.getEducationId());
                userSession.setEducationalName(mEducationManagerLoginInfo.getEducationName());
                userSession.setArea(mEducationManagerLoginInfo.getArea());
                userSession.setAreaName(mEducationManagerLoginInfo.getAreaName());
                break;
            //机构人员
            case IdentityIdConstants.EDUCATION_STAFF:
                MEducation mEducation = educationMapper.selectByPrimaryKey(userId);
                BeanUtils.copyProperties(mEducation, userSession);
                break;
            //学校管理员
            case IdentityIdConstants.SCHOOL_MANAGER:
                MSchoolManager mSchoolManager = schoolManagerMapper.selectByPrimaryKey(userId);
                BeanUtils.copyProperties(mSchoolManager, userSession);
                //设置改学校是否有同步课堂
                List<Integer> sysGradeNumList = schoolPeriodService.getSysGradeNum(schoolId);
                logger.info("同步课堂的年级数：" + sysGradeNumList.size());
                paramMap.put("synClass", CollectionUtils.isNotEmpty(sysGradeNumList) ? true : false);
                break;
            //教师
            case IdentityIdConstants.TEACHER:
                MTeacherLoginInfo mTeacher = userDetailMapper.getTeacherInfo(userId, schoolId);
                //该老师是否是管理层老师(0不是，1是）
                paramMap.put("isManager", mTeacher.getIsManager());
                CtBook ctBook = mTeacher.getCtBook();
                if (ctBook != null) {
                    bookId = ctBook.getBookId();
                    userSession.setBookName(ctBook.getBookName());
                    userSession.setSubjectId(ctBook.getSubjectId());
                    userSession.setSubjectName(ctBook.getSubjectName());
                    userSession.setTeacherPeriodType(getPeriodType(userInfo, ctBook));
                }
                if (StringUtils.isEmpty(bookId)) {
                    bookId = UserConstants.DEFAULT_BOOK_ID;
                }
                MSchoolBook mSchoolBook = mSchoolBookMapper.selectByPrimaryKey(schoolId, bookId);
                if (mSchoolBook != null) {
                    userSession.setExtraBookId(mSchoolBook.getExtraBookId());
                }
                userSession.setBookId(bookId);
                userSession.setPositionId(mTeacher.getPositionId());
                userSession.setPositionName(mTeacher.getPositionName());

                List<ClassInfoDTO> teacherClass = new ArrayList<>();
                for (MClass mClass : mTeacher.getClasses()) {
                    ClassInfoDTO classInfoDTO = new ClassInfoDTO();
                    classInfoDTO.setClassId(mClass.getClassId());
                    classInfoDTO.setChargeId(mClass.getChargeId());
                    classInfoDTO.setChargeName(mClass.getChargeName());
                    int years = mClass.getYears();
                    classInfoDTO.setClassName(ClassNameUtil.getFullName(years, mClass.getName()));
                    classInfoDTO.setYear(years);
                    classInfoDTO.setGradeNum(ClassNameUtil.getGradeNum(years));
                    classInfoDTO.setSemester(ClassNameUtil.getSemester());
                    teacherClass.add(classInfoDTO);
                }
                userSession.setTeacherClass(teacherClass);
                break;
            //学生
            case IdentityIdConstants.STUDENT:
                MStudentLoginInfo mStudentLoginInfo = userDetailMapper.getStudentInfo(userId, schoolId);
                MClass mClass = mStudentLoginInfo.getmClass();
                if (mClass != null) {
                    ClassInfoDTO classInfoDTO = new ClassInfoDTO();
                    classInfoDTO.setClassId(mClass.getClassId());
                    classInfoDTO.setClassName(ClassNameUtil.getFullName(mClass.getYears(), mClass.getCodeSharing()));
                    classInfoDTO.setGradeNum(ClassNameUtil.getGradeNum(mClass.getYears()));
                    classInfoDTO.setGradeName(ClassNameUtil.getGradeName(mClass.getYears()));
                    userSession.setStudentClass(classInfoDTO);
                    userSession.setClassId(mClass.getClassId());
                    userSession.setGradeNum(ClassNameUtil.getGradeNum(mClass.getYears()));
                }
                userSession.setSemester(ClassNameUtil.getSemester());
                break;
            //家长
            case IdentityIdConstants.PARENT:
                MParents mParents = parentsMapper.selectByPrimaryKey(userId, schoolId);
                BeanUtils.copyProperties(mParents, userSession);
                break;
            default:
                break;
        }
        userSession.setParamMap(paramMap);
    }


    private Integer getPeriodType(MUserLoginInfo userLoginInfo, CtBook book) {
        List<MSchoolPeriod> mSchoolPeriods = userLoginInfo.getPeriods();
        Integer primary = null;
        for (MSchoolPeriod mSchoolPeriod : mSchoolPeriods) {
            if (mSchoolPeriod.getSection() == SectionConstants.PRIMARY_SCHOOL_NUM) {//小学
                primary = mSchoolPeriod.getLength();//小学学段
            }
            if (mSchoolPeriod.getSection() == SectionConstants.JUNIOR_MIDDLE_SCHOOL_NUM) {//初中
                primary = 9 - mSchoolPeriod.getLength();//小学学段
            }
        }
        if (primary != null) {
            if (book.getGrade() <= primary && book.getGrade() > 0
                    && (book.getSubjectId().endsWith("010") || book.getSubjectId().endsWith("020") ||
                    book.getSubjectId().endsWith("030") || book.getSubjectId().endsWith("040") || book.getSubjectId().endsWith("150"))) {
                return 1;
            } else if (book.getGrade() > primary && book.getGrade() <= 9
                    && (book.getSubjectId().endsWith("020") || book.getSubjectId().endsWith("050") || book.getSubjectId().endsWith("060"))) {
                return 2;
            }
        }
        return 0;
    }

}
