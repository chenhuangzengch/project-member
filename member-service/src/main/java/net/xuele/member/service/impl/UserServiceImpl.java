package net.xuele.member.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import net.xuele.common.dto.ClassInfoDTO;
import net.xuele.common.dto.RelativeDTO;
import net.xuele.common.exceptions.MemberException;
import net.xuele.common.page.Page;
import net.xuele.common.page.PageResponse;
import net.xuele.common.utils.MemberAssert;
import net.xuele.common.utils.PageUtils;
import net.xuele.member.constant.*;
import net.xuele.member.context.MemberLogicContext;
import net.xuele.member.domain.*;
import net.xuele.member.dto.*;
import net.xuele.member.dto.page.ContactsByStudentUserIdPageRequest;
import net.xuele.member.dto.page.ContactsStudentPageRequest;
import net.xuele.member.dto.page.ContactsTeacherPageRequest;
import net.xuele.member.persist.*;
import net.xuele.member.pojo.RequestTeacherLoginPage;
import net.xuele.member.service.*;
import net.xuele.member.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * @author ZhengTao
 */
public class UserServiceImpl implements UserService {
    @Autowired
    private MUsersMapper usersMapper;
    @Autowired
    private CasLoginMapper casLoginMapper;
    @Autowired
    private MUserRoleMapper userRoleMapper;
    @Autowired
    private MParentsMapper parentsMapper;
    @Autowired
    private MStudentMapper studentMapper;
    @Autowired
    private MEducationMapper educationMapper;
    @Autowired
    private MEducationManagerMapper educationManagerMapper;
    @Autowired
    private MTeacherMapper teacherMapper;
    @Autowired
    private MSchoolManagerMapper schoolManagerMapper;
    @Autowired
    private ClassService classService;
    @Autowired
    private MSchoolMapper schoolMapper;
    @Autowired
    private MFamilyRelationMapper familyRelationMapper;
    @Autowired
    private MClassMapper classMapper;
    @Autowired
    private MSchoolPeriodMapper schoolPeriodMapper;
    @Autowired
    private MLoginUserMapper loginUserMapper;
    @Autowired
    private MUserIconMapper mUserIconMapper;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private MLoginStatisticsMapper mLoginStatisticsMapper;
    @Autowired
    private CasLoginLogMapper casLoginLogMapper;
    @Autowired
    private SeqGeneratorService seqGeneratorService;
    @Autowired
    private SendMessageService sendMessageService;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private CasLoginService casLoginService;
    @Autowired
    private FamilyRelationService familyRelationService;
    @Autowired
    private CtBookMapper ctBookMapper;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private MPasswordLogMapper mPasswordLogMapper;
    @Autowired
    private MMobileInfoMapper mobileInfoMapper;
    @Autowired
    @Qualifier("signRedisTemplate")
    private RedisTemplate<String, String> signRedisTemplate;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    @Override
    public String getSchoolId(String userId) {
        if (StringUtils.isEmpty(userId) || userId.length() == 11 || userId.contains("@")) {
            logger.error("取学校id错误，user_id:" + String.valueOf(userId));
            return null;
        }
        String schoolId;
        if (UserConstants.GET_SCHOOLID_ONLY_FORM_DB) {
            schoolId = usersMapper.selectSchoolIdByUserId(userId);
        } else {
            String key = MessageFormat.format(UserConstants.USER_SCHOOL_KEY, userId);
            schoolId = cacheService.get(key);
            if (StringUtils.isEmpty(schoolId)) {
                schoolId = usersMapper.selectSchoolIdByUserId(userId);
                if (schoolId == null) {
                    logger.info("用户所在学校Id不存在:" + userId);
                    return null;
                }
                cacheService.set(key, schoolId, 100, TimeUnit.DAYS);
            }
        }
        return schoolId;
    }

    @Override
    public UserDTO getByUserId(String userId) {
        if (StringUtils.isEmpty(userId)) {
            return null;
        }
        String key = MessageFormat.format(CacheConstants.KEY_USER, userId);
        UserDTO dto = cacheService.get(key);
        if (dto != null) {
            return dto;
        }
        String schoolId = getSchoolId(userId);
        if (StringUtils.isEmpty(schoolId)) {
            return null;
        }
        Long middle = System.currentTimeMillis();
        MUsers dbUser = usersMapper.selectByPrimaryKey(userId, schoolId);
        if (dbUser == null) {
            return null;
        }
        //logger.info("从数据库获取用户{}信息花费时间{}", userId, System.currentTimeMillis() - middle);
        UserDTO to = new UserDTO();
        BeanUtils.copyProperties(dbUser, to);
        cacheService.set(key, to, CacheConstants.CACHE_SEVEN_DAY, TimeUnit.DAYS);
        return to;
    }

    @Override
    public UserProfileDTO getProfileByUserId(String userId) {
        UserDTO userDTO = getByUserId(userId);
        String schoolId = userDTO.getSchoolId();
        if (userDTO == null) {
            return null;
        }
        UserProfileDTO profile = new UserProfileDTO();
        profile.setUser(userDTO);
        //身份
        if (StringUtils.isEmpty(userDTO.getIdentityId())) {
            throw new MemberException("该用户没有身份");
        }
        String identityId = userDTO.getIdentityId().toUpperCase();
        if (identityId.equals(IdentityIdConstants.PARENT)) {
            MParents mParents = parentsMapper.selectByPrimaryKey(userId, schoolId);
            if (mParents != null) {
                ParentsDTO special = new ParentsDTO();
                BeanUtils.copyProperties(mParents, special);
                MSchool school = schoolMapper.selectByPrimaryKey(userDTO.getSchoolId());
                if (school != null) {
                    special.setSchoolName(school.getName());
                } else {
                    logger.error("家长学校不存在：" + mParents.getSchoolId());
                }
                profile.setProfile(special);
            } else {
                profile.setProfile(null);
            }
            return profile;
        } else if (identityId.equals(IdentityIdConstants.STUDENT)) {
            MStudent mEntity = studentMapper.selectByPrimaryKey(userId, schoolId);
            if (mEntity != null) {
                StudentDTO special = new StudentDTO();
                BeanUtils.copyProperties(mEntity, special);
                //获取年级和年级名称
                if (StringUtils.isNotEmpty(mEntity.getClassId())) {
                    ClassDTO classDTO = classService.getByClassId(mEntity.getClassId());
                    if (classDTO != null) {
                        special.setYear(classDTO.getYears());
                        special.setGradeName(ClassNameUtil.getGradeName(classDTO.getYears()));
                        special.setGradeNum(ClassNameUtil.getGradeNum(classDTO.getYears()));
                    }
                } else {
                    logger.warn("该学生无班级：" + userId);
                }
                special.setSemester(ClassNameUtil.getSemester());
                profile.setProfile(special);
            } else {
                profile.setProfile(null);
            }
            return profile;
        } else if (identityId.equals(IdentityIdConstants.EDUCATION_MANAGER)) {
            MEducationManager mEntity = educationManagerMapper.selectByPrimaryKey(userId);
            if (mEntity != null) {
                EducationManagerDTO special = new EducationManagerDTO();
                BeanUtils.copyProperties(mEntity, special);
                profile.setProfile(special);
            } else {
                profile.setProfile(null);
            }
            return profile;
        } else if (identityId.equals(IdentityIdConstants.EDUCATION_STAFF)) {
            MEducation mEntity = educationMapper.selectByPrimaryKey(userId);
            if (mEntity != null) {
                EducationDTO special = new EducationDTO();
                BeanUtils.copyProperties(mEntity, special);
                profile.setProfile(special);
            } else {
                profile.setProfile(null);
            }
            return profile;
        } else if (identityId.equals(IdentityIdConstants.SCHOOL_MANAGER)) {
            MSchoolManager mEntity = schoolManagerMapper.selectByPrimaryKey(userId);
            if (mEntity != null) {
                SchoolManagerDTO special = new SchoolManagerDTO();
                BeanUtils.copyProperties(mEntity, special);
                profile.setProfile(special);
            } else {
                profile.setProfile(null);
            }
            return profile;
        } else if (identityId.equals(IdentityIdConstants.TEACHER)) {
            MTeacher mEntity = teacherMapper.selectByPrimaryKey(userId, schoolId);
            if (mEntity != null) {
                TeacherDTO special = new TeacherDTO();
                BeanUtils.copyProperties(mEntity, special);
                profile.setProfile(special);
            } else {
                profile.setProfile(null);
            }
            return profile;
        } else
            return null;
    }

    /**
     * 这个时刻是不是录入学生后,现在以8月1日为准,超过此日期返回true
     */
    private boolean isPastHalf(Date now) {
        String monthDay = DateFormatUtils.format(now, "MMdd");
        return monthDay.compareTo(ClassConstants.YEAR_SEPARATOR) >= 0;
    }


    @Override
    public List<RoleDTO> queryRoleByUserId(String userId) {
        MemberAssert.notNull(userId, "用户id不能为空", MemberLogicContext.ERROR_CODE_SELECT_USER_ID_IS_NULL);
        List<MRole> roles = userRoleMapper.selectRoleByUserId(userId, getSchoolId(userId));
        return parseRoleList(roles);
    }

    /**
     * 根据用户ID列表获取该用户列表信息,用","隔开
     */
    @Override
    public List<UserDTO> queryUsersByUserIdList(String userIds, String schoolId) {
        String[] userId = userIds.split(",");
        //List->ArrayList
        return getByUserIds(new ArrayList(Arrays.asList(userId)), schoolId);
    }

    /**
     * 根据用户ID列表获取该用户列表信息
     */
    @Override
    public List<UserDTO> getByUserIds(ArrayList<String> userIdList, String schoolId) {
        if (CollectionUtils.isEmpty(userIdList)) {
            return new ArrayList<>();
        }
        if (StringUtils.isEmpty(schoolId)) {
            if (userIdList.size() == 1) {
                schoolId = getSchoolId(userIdList.get(0));
            } else {
                schoolId = null;
            }
        }
        List<MUsers> usersList = usersMapper.getByUserIds(userIdList, schoolId);
        List<UserDTO> userDTOList = new ArrayList<>();
        for (MUsers users : usersList) {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(users, userDTO);
            userDTOList.add(userDTO);
        }
        return userDTOList;
    }

    private List<RoleDTO> parseRoleList(List<MRole> users) {
        List<RoleDTO> roleDTOs = new ArrayList<>(users.size());
        for (MRole user : users) {
            roleDTOs.add(parseRole(user));
        }
        return roleDTOs;
    }

    private RoleDTO parseRole(MRole role) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRoleId(role.getRoleId());
        roleDTO.setRoleName(role.getRoleName());
        roleDTO.setStatus(role.getStatus());
        return roleDTO;
    }


    @Override
    public void updateStatus(Integer status, String userIds) {
        if (StringUtils.isEmpty(userIds)) {
            return;
        }
        String[] ids = userIds.split(",");
        if (ids.length == 0) {
            return;
        }
        String schoolId = getSchoolId(ids[0]);
        usersMapper.updateStatus(status, Arrays.asList(ids), schoolId);
        //离校操作
        if (status == UserConstants.STATUS_LEAVE) {
            //老师离校：班级表去掉班主任信息
            classMapper.chiefLeave(Arrays.asList(ids), schoolId);
            teacherMapper.updateTeacherPosition(Arrays.asList(ids),
                    PositionConstants.TEACHER, PositionConstants.TEACHER_NAME, schoolId);
        }
        //学生离校，返校，删除缓存数据
        deleteRedisOfParent(Arrays.asList(ids), schoolId);
        invalidCache(ids);
    }

    /*
     * 学生离校，根据多个孩子id删除家长在缓存中的数据
     * @param userIdList 孩子id列表
     * @param schoolId   孩子的学校id
     */
    private void deleteRedisOfParent(List<String> userIdList, String schoolId) {
        //获取孩子的家长信息
        List<MFamilyRelation> frList = familyRelationMapper.queryParentOfKidList(userIdList, schoolId);
        deleteRedis(frList);
    }

    /*
     * 修改用户图标，根据单个孩子id删除家长在缓存中的数据
     * @param userId   孩子id
     * @param schoolId 孩子的学校id
     */
    private void deleteRedisOfParent(String userId, String schoolId) {
        //获取孩子的家长信息
        List<MFamilyRelation> frList = familyRelationMapper.queryFamilyByStudent(userId, schoolId);
        deleteRedis(frList);
    }

    //学生离校,修改用户图标，删除该家长的缓存信息
    private void deleteRedis(List<MFamilyRelation> frList) {
        List<String> keyList = new ArrayList<>();
        for (MFamilyRelation fr : frList) {
            keyList.add(MessageFormat.format(CacheConstants.KEY_KID_OF_PARENT, fr.getUserId()));
            keyList.add(MessageFormat.format(CacheConstants.KEY_FAMILY_RELATION, fr.getUserId()));
        }
        if (CollectionUtils.isNotEmpty(keyList)) {
            cacheService.delete(keyList);
        }
    }

    /*
     * 自己修改学生名称，根据单个孩子id删除家长在缓存中的数据
     * @param userId   孩子id
     * @param schoolId 孩子的学校id
     */
    private void deleteRedisNameOfParent(String userId, String schoolId) {
        //获取孩子的家长信息
        List<MFamilyRelation> frList = familyRelationMapper.queryFamilyByStudent(userId, schoolId);
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

    /*
     * 家长名称修改，删除孩子的缓存
     * @param userId 家长id
     */
    private void deleteRedisOfKid(String userId) {
        List<MStudentManager> smList = familyRelationMapper.queryKidInfo(userId, 1);
        List<String> keyList = new ArrayList<>();
        for (MStudentManager sm : smList) {
            keyList.add(MessageFormat.format(CacheConstants.KEY_PARENT_OF_KID, sm.getUserId()));
        }
        if (CollectionUtils.isNotEmpty(keyList)) {
            cacheService.delete(keyList);
        }
    }

    /**
     * APP接口、WEB平台：重置密码
     */
    @Override
    public void updatePassword(String loginId, String password) {
        MemberAssert.isTrue(password.matches("[a-zA-Z0-9]{6,20}"), "密码格式不正确", MemberLogicContext.ERROR_CODE_PASSWORD_LENGTH);
        List<String> userIdsList = casLoginMapper.getUserIds(loginId);
        if (userIdsList.isEmpty()) {
            logger.warn("未找到用户。用户Id:" + loginId);
            return;
        }
        casLoginMapper.updatePassword(userIdsList, MemberEncryptUtil.encrypt(password));
    }

    /**
     * 修改多个登录号的密码
     */
    @Override
    public void updatePasswordList(String loginId, String password) {
        MemberAssert.isTrue(password.matches("[a-zA-Z0-9]{6,20}"), "密码格式不正确", MemberLogicContext.ERROR_CODE_PASSWORD_LENGTH);
        String[] loginIdList = loginId.split(",");
        Set<String> set = new HashSet<>(Arrays.asList(loginIdList));
        List<String> userIdsList = casLoginMapper.queryUserIds(new ArrayList<>(set));
        if (CollectionUtils.isEmpty(userIdsList)) {
            logger.warn("未找到用户");
            return;
        }
        casLoginMapper.updatePassword(userIdsList, MemberEncryptUtil.encrypt(password));
    }

    @Override
    public boolean updateResetPasswordByMobileOrEmail(String loginId, String password, String type, String typeValue, String checkCode) {
        validPasswordForChange(password);
        boolean res = false;
        String redisKey = null;
        if ("email".equals(type)) {
            res = sendMessageService.checkCode(typeValue, checkCode, SendMessageService.Template.ResetPassword);
            redisKey = MessageFormat.format(SecurityConstants.MEMBER_REDIS_EMAIL_KEY, SendMessageService.Template.ResetPassword.getName(), typeValue);
        } else if ("mobile".equals(type)) {
            res = sendMessageService.checkCode(typeValue, checkCode, SendMessageService.CodeType.FindPassword);
            redisKey = MessageFormat.format(SecurityConstants.MEMBER_REDIS_PHONE_KEY, SendMessageService.CodeType.FindPassword.getName(), typeValue);
        }

        if (res) {
            updatePassword(loginId, password);
            cacheService.delete(redisKey);
        }
        return res;
    }

    @Override
    public void updatePasswordForInit(String userId, String password, Integer statusAfterInit) {
        validPasswordForChange(password);
        updatePassword(userId, password);
        List<String> userIds = new ArrayList<>();
        userIds.add(userId);
        usersMapper.updateStatus(statusAfterInit, userIds, getSchoolId(userId));
        invalidCache(userId);
    }

    /**
     * 添加用户，保存m_users、cas_login、m_user_role、m_login_user
     *
     * @param userDTO 填充好的数据
     */
    @Override
    public UserDTO saveUser(UserDTO userDTO) {
        String userId = seqGeneratorService.generate("m_users").toString();
        userDTO.setUserId(userId);
        userDTO.setAddTime(new Date());
        userDTO.setUpdateTime(new Date());
        MUsers mUsers = new MUsers();
        userDTO.setStatus(UserConstants.STATUS_INIT);
        BeanUtils.copyProperties(userDTO, mUsers);
        mUsers.setIcon(UserConstants.ICON_DEFAULT);
        usersMapper.insert(mUsers);
        CasLogin casLogin = new CasLogin();
        casLogin.setUserId(mUsers.getUserId());
        casLogin.setLoginId(mUsers.getUserId());
        casLogin.setLoginType(UserConstants.MEMBER);
        casLogin.setPassword(UserConstants.INIT_PASSWORD);
        //保存cas_login
        casLoginMapper.insert(casLogin);
        MLoginUser loginUser = new MLoginUser();
        Long loginUserId = seqGeneratorService.generate("m_login_user");
        loginUser.setId(loginUserId);
        loginUser.setUserId(userId);
        loginUser.setBindUserId(userId);
        //保存m_login_user
        loginUserMapper.saveLoginUser(loginUser);
        MUserRole mUserRole = new MUserRole();
        mUserRole.setId(seqGeneratorService.generate("m_user_role"));
        mUserRole.setUserId(userId);
        mUserRole.setSchoolId(userDTO.getSchoolId());
        //保存m_user_role
        switch (userDTO.getIdentityId()) {
            case IdentityIdConstants.TEACHER:
                mUserRole.setRoleId(RoleIdConstants.TEACHER);
                userRoleMapper.insert(mUserRole);
                break;
            case IdentityIdConstants.STUDENT:
                mUserRole.setRoleId(RoleIdConstants.STUDENT);
                userRoleMapper.insert(mUserRole);
                break;
            case IdentityIdConstants.PARENT:
                mUserRole.setRoleId(RoleIdConstants.PARENT);
                userRoleMapper.insert(mUserRole);
                break;
            case IdentityIdConstants.SCHOOL_MANAGER:
                mUserRole.setRoleId(RoleIdConstants.SCHOOL_MANAGER);
                userRoleMapper.insert(mUserRole);
                break;
            case IdentityIdConstants.EDUCATION_MANAGER:
                mUserRole.setRoleId(RoleIdConstants.EDUCATION_MANAGER);
                userRoleMapper.insert(mUserRole);
                break;
            case IdentityIdConstants.EDUCATION_STAFF:
                mUserRole.setRoleId(RoleIdConstants.EDUCATION_STAFF);
                userRoleMapper.insert(mUserRole);
                break;
            default:
                break;
        }
        return userDTO;
    }

    /**
     * APP接口：根据用户ID获取该用户相关角色信息
     */
    @Override
    public List<RoleInfoDTO> queryRoleInfo(String loginId) {
        //获取该用户的相关角色
        List<String> userIds = casLoginMapper.getUserIds(loginId);
        //List<UserDTO> usersList = getByUserIds(userIds, null);
        List<UserDTO> usersList = getByUserIds(new ArrayList(userIds), null);
        List<RoleInfoDTO> roleInfoAppDTOList = new ArrayList<>();
        for (UserDTO userDTO : usersList) {
            roleInfoAppDTOList.add(getRoleInfo(userDTO));
        }
        return roleInfoAppDTOList;
    }

    /**
     * APP接口：根据用户身份获取相关角色信息
     */
    @Override
    public RoleInfoDTO getRoleInfo(UserDTO users) {
        String schoolId = getSchoolId(users.getUserId());
        StringBuffer stringBufferForId = new StringBuffer();
        StringBuffer stringBufferForName = new StringBuffer();
        //不同用户获取不同信息
        if (IdentityIdConstants.STUDENT.equals(users.getIdentityId())) {//学生
            MStudent student = studentMapper.selectByPrimaryKey(users.getUserId(), schoolId);
            RoleInfoDTO roleInfoDTO = new RoleInfoDTO();
            roleInfoDTO.setUserId(users.getUserId());
            roleInfoDTO.setRealName(users.getRealName());
            roleInfoDTO.setIcon(users.getIcon());
            roleInfoDTO.setPositionId(users.getIdentityId());
            roleInfoDTO.setPositionName(users.getIdentityDescription());
            roleInfoDTO.setIdentityId(users.getIdentityId());
            roleInfoDTO.setIdentityDescription(users.getIdentityDescription());
            roleInfoDTO.setRelativeid(student.getSchoolId());
            roleInfoDTO.setRelativename(student.getSchoolName());
            return roleInfoDTO;
        } else if (IdentityIdConstants.TEACHER.equals(users.getIdentityId())) {//老师
            MTeacher teacher = teacherMapper.selectByPrimaryKey(users.getUserId(), getSchoolId(users.getUserId()));
            RoleInfoDTO roleInfoDTO = new RoleInfoDTO();
            roleInfoDTO.setUserId(users.getUserId());
            roleInfoDTO.setRealName(users.getRealName());
            roleInfoDTO.setIcon(users.getIcon());
            roleInfoDTO.setPositionId(teacher.getPositionId());
            roleInfoDTO.setPositionName(teacher.getPositionName());
            roleInfoDTO.setIdentityId(users.getIdentityId());
            roleInfoDTO.setIdentityDescription(users.getIdentityDescription());
            roleInfoDTO.setRelativeid(teacher.getSchoolId());
            roleInfoDTO.setRelativename(teacher.getSchoolName());
            return roleInfoDTO;
        } else if (IdentityIdConstants.PARENT.equals(users.getIdentityId())) {//家长
            List<FamilyRelationDTO> famillyRelationList = queryChildsByParentId(users.getUserId());
            if (famillyRelationList.size() == 1) {
                FamilyRelationDTO frdto = famillyRelationList.get(0);
                RoleInfoDTO roleInfoDTO = new RoleInfoDTO();
                roleInfoDTO.setUserId(users.getUserId());
                roleInfoDTO.setRealName(users.getRealName());
                roleInfoDTO.setIcon(users.getIcon());
                roleInfoDTO.setPositionId(frdto.getMemberId());
                roleInfoDTO.setPositionName(frdto.getMemberName());
                roleInfoDTO.setIdentityId(users.getIdentityId());
                roleInfoDTO.setIdentityDescription(users.getIdentityDescription());
                roleInfoDTO.setRelativeid(frdto.getTargetUserId());
                roleInfoDTO.setRelativename(frdto.getTargetUserName());
                return roleInfoDTO;
            } else if (famillyRelationList.size() > 1) {
                for (FamilyRelationDTO fr : famillyRelationList) {
                    stringBufferForId.append(fr.getTargetUserId() + " ");
                    stringBufferForName.append((StringUtils.isNotEmpty(fr.getTargetUserName()) ? fr.getTargetUserName() : "") + " ");
                }
                RoleInfoDTO roleInfoDTO = new RoleInfoDTO();
                roleInfoDTO.setUserId(users.getUserId());
                roleInfoDTO.setRealName(users.getRealName());
                roleInfoDTO.setIcon(users.getIcon());
                roleInfoDTO.setPositionId("qinren");
                roleInfoDTO.setPositionName("亲人");
                roleInfoDTO.setIdentityId(users.getIdentityId());
                roleInfoDTO.setIdentityDescription(users.getIdentityDescription());
                roleInfoDTO.setRelativeid(stringBufferForId.toString().trim());
                roleInfoDTO.setRelativename(stringBufferForName.toString().trim());
                return roleInfoDTO;
            } else {
                RoleInfoDTO roleInfoDTO = new RoleInfoDTO();
                roleInfoDTO.setUserId(users.getUserId());
                roleInfoDTO.setRealName(users.getRealName());
                roleInfoDTO.setIcon(users.getIcon());
                roleInfoDTO.setIdentityId(users.getIdentityId());
                roleInfoDTO.setIdentityDescription(users.getIdentityDescription());
                return roleInfoDTO;
            }
        } else if (IdentityIdConstants.SCHOOL_MANAGER.equals(users.getIdentityId())) {//学校管理员
            MSchoolManager schoolManager = schoolManagerMapper.selectByPrimaryKey(users.getUserId());
            RoleInfoDTO roleInfoDTO = new RoleInfoDTO();
            roleInfoDTO.setUserId(users.getUserId());
            roleInfoDTO.setRealName(users.getRealName());
            roleInfoDTO.setIcon(users.getIcon());
            roleInfoDTO.setPositionId(users.getIdentityId());
            roleInfoDTO.setPositionName(users.getIdentityDescription());
            roleInfoDTO.setIdentityId(users.getIdentityId());
            roleInfoDTO.setIdentityDescription(users.getIdentityDescription());
            roleInfoDTO.setRelativeid(schoolManager.getSchoolId());
            roleInfoDTO.setRelativename(schoolManager.getSchoolName());
            return roleInfoDTO;
        } else if (IdentityIdConstants.EDUCATION_MANAGER.equals(users.getIdentityId())) {//教育机构管理员
            MEducationManager educationManager = educationManagerMapper.selectByPrimaryKey(users.getUserId());
            RoleInfoDTO roleInfoDTO = new RoleInfoDTO();
            roleInfoDTO.setUserId(users.getUserId());
            roleInfoDTO.setRealName(users.getRealName());
            roleInfoDTO.setIcon(users.getIcon());
            roleInfoDTO.setPositionId(users.getIdentityId());
            roleInfoDTO.setPositionName(users.getIdentityDescription());
            roleInfoDTO.setIdentityId(users.getIdentityId());
            roleInfoDTO.setIdentityDescription(users.getIdentityDescription());
            roleInfoDTO.setRelativeid(educationManager.getEducationalId());
            roleInfoDTO.setRelativename(educationManager.getEducationalName());
            return roleInfoDTO;
        } else if (IdentityIdConstants.EDUCATION_STAFF.equals(users.getIdentityId())) {//教育机构人员
            MEducation education = educationMapper.selectByPrimaryKey(users.getUserId());
            RoleInfoDTO roleInfoDTO = new RoleInfoDTO();
            roleInfoDTO.setUserId(users.getUserId());
            roleInfoDTO.setRealName(users.getRealName());
            roleInfoDTO.setIcon(users.getIcon());
            roleInfoDTO.setPositionId(education.getDutyId());
            roleInfoDTO.setPositionName(education.getDutyName());
            roleInfoDTO.setIdentityId(users.getIdentityId());
            roleInfoDTO.setIdentityDescription(users.getIdentityDescription());
            roleInfoDTO.setRelativeid(education.getEducationalId());
            roleInfoDTO.setRelativename(education.getEducationalName());
            return roleInfoDTO;
        }
        return null;
    }


    /**
     * APP接口：根据用户ID列表获取该用户列表信息
     */
    @Override
    public List<StudentManagerDTO> queryStudentsByUserIdList(ArrayList<String> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return new ArrayList<>();
        }
        List<UserDTO> usersList = getByUserIds(userIds, null);
        List<StudentManagerDTO> schoolAppDTOList = new ArrayList<>();
        for (UserDTO user : usersList) {
            StudentManagerDTO sadto = new StudentManagerDTO();
            BeanUtils.copyProperties(user, sadto);
            schoolAppDTOList.add(sadto);
        }
        return schoolAppDTOList;
    }

    /**
     * 根据用户ID(老师或者学生)获取所在学校的学校ID
     */
    @Override
    public String getSchoolIdByUserId(String userId) {
        return getSchoolId(userId);
    }

    /**
     * 通知接口用
     * APP接口、WEB平台：根据学校ID获取学校的管理员和校长
     *
     * @param schoolId
     * @return
     */
    @Override
    public List<SchoolManagerDTO> querySchoolManagerAndPrincipal(String schoolId) {

        List<MSchoolManagerPrincipal> mSchooManagerPrincipalList = schoolManagerMapper.querySchoolManagerAndPrincipal(schoolId);
        List<SchoolManagerDTO> schoolManagerDTOList = new ArrayList<>();
        for (MSchoolManagerPrincipal schoolManagerPrincipal : mSchooManagerPrincipalList) {
            SchoolManagerDTO schoolManagerDTO = new SchoolManagerDTO();
            BeanUtils.copyProperties(schoolManagerPrincipal, schoolManagerDTO);
            schoolManagerDTOList.add(schoolManagerDTO);
        }
        return schoolManagerDTOList;
    }

    /**
     * APP接口：根据家长名称获取孩子信息
     */
    @Override
    public List<FamilyRelationDTO> queryChildsByParentId(String userId) {
        String key = MessageFormat.format(CacheConstants.KEY_FAMILY_RELATION, userId);
        List<FamilyRelationDTO> list = cacheService.get(key);
        if (list != null) {
            return list;
        }
        List<MFamilyRelation> famillyRelationList = familyRelationMapper.getChildsByParentId(userId);
        List<FamilyRelationDTO> familyRelationDTOList = new ArrayList<>();
        for (MFamilyRelation ufr : famillyRelationList) {
            FamilyRelationDTO frdto = new FamilyRelationDTO();
            BeanUtils.copyProperties(ufr, frdto);
            familyRelationDTOList.add(frdto);
        }
        cacheService.set(key, familyRelationDTOList, CacheConstants.CACHE_SEVEN_DAY, TimeUnit.DAYS);
        return familyRelationDTOList;
    }

    /**
     * APP接口：验证用户名和密码是否正确,正确则插入日志
     */
    @Override
    public UserDTO saveAndCheckLogin(String loginId, String password, Integer type) {
        UserDTO userDTO = checkPassword(loginId, password);
        if (userDTO != null) {
            saveLoginStatistics(userDTO.getUserId(), userDTO.getSchoolId(), type);
        }
        return userDTO;
    }

    /**
     * APP接口：验证用户名和密码是否正确
     */
    @Override
    public UserDTO checkPassword(String loginId, String password) {
        CasLogin casLogin = casLoginMapper.selectByPrimaryKey(loginId);
        if (casLogin == null) {
//            return "用户名不存在";
            return null;
        }
        if (casLogin.getStatus() == null || casLogin.getStatus() != 1) {
//            return "用户被禁用";
            return null;
        }
        if (!MemberEncryptUtil.encrypt(password).equals(casLogin.getPassword())) {
//            return "用户密码错误";
            return null;
        }
        //插入登录日志表
        return getByUserId(casLogin.getUserId());
    }

    /**
     * APP接口：检查用户名是否存在
     */
    @Override
    public boolean checkUserIdExist(String loginId) {
        int in = casLoginMapper.checkUserIdExist(loginId);
        if (in > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 导入老师和学生数据
     */
    @Override
    public ExcelUploadInfoDTO saveExcelUser(ArrayList<ArrayList<Object>> lists, String type, String schoolId, String userId) {
        ExcelUploadInfoDTO result = new ExcelUploadInfoDTO();
        long s1 = System.currentTimeMillis();
        //转换excel中的数据
        List<ExcelState> esList = queryExcelInfo(lists);
        long e1 = System.currentTimeMillis();
        long t1 = e1 - s1;
        logger.info("-----------------------------------------");
        logger.info("转换excel中的数据所需时间为：{}毫秒", t1);
        //检测数据合理性
        String errorInfo = checkDataAndSave(esList, type, schoolId, userId);
        if (StringUtils.isNotEmpty(errorInfo)) {
            result.setErrorInfo(errorInfo);
            List<ExcelStateDTO> list = new ArrayList<>();
            result.setExcelStateDTOs(list);
            for (ExcelState es : esList) {
                ExcelStateDTO esdto = new ExcelStateDTO();
                BeanUtils.copyProperties(es, esdto);
                list.add(esdto);
            }
            return result;
        }
        return result;
    }


    /**
     * 生成excel信息
     */
    @Override
    public ExcelInfo saveUselessData(ArrayList<ExcelStateDTO> uselessList) {
        List<List<Object>> data = new ArrayList<>();
        for (ExcelStateDTO es : uselessList) {
            List<Object> row = new ArrayList<>();
            row.add(es.getRealName());
            row.add(es.getIdentityDescription());
            row.add(es.getGradeNum());
            row.add(es.getClassNum());
            row.add(es.getMobile());
            row.add(es.getSex());
            row.add(es.getErrorInfo());
            data.add(row);
        }
        ExcelInfo info = new ExcelInfo();
        info.setData(data);
        //数据的标题头,第一行,可以为不设置
        info.setHeaders(new String[]{ExcelConstants.FIRST_CELL, ExcelConstants.SECOND_CELL, ExcelConstants.THIRD_CELL, ExcelConstants.FOURTH_CELL,
                ExcelConstants.FIFTH_CELL, ExcelConstants.SIXTH_CELL, ExcelConstants.SEVENTH_CELL});
        return info;
    }

    //批量保存学生数据
    private void saveStudentList(List<ExcelState> userList, MSchool school, String userId) {
        for (ExcelState estate : userList) {
            estate.setUserId(seqGeneratorService.generate("m_users").toString());
        }
        //key规则：2001-3
        Map<String, MClass> classMap = new HashMap<>();
        //year:schoolPeriodId
        Map<Integer, String> yearPeriodIdMap = new HashMap<>();
        List<MStudent> studentList = new ArrayList<>();
        MClass cla;
        for (ExcelState es : userList) {
            //获取班级信息
            cla = getClassByNums(es, school, userId, classMap, yearPeriodIdMap);
            MStudent student = new MStudent();
            student.setUserId(es.getUserId());
            student.setClassId(cla.getClassId());
            student.setClassName(cla.getName());
            student.setClassAliasName(cla.getAliasName());
            student.setSchoolId(school.getId());
            student.setSchoolName(school.getName());
            student.setJoinClass(new Date());
            studentList.add(student);
        }
        long s3 = System.currentTimeMillis();
        //保存学生
        saveStudent(studentList);
        long e3 = System.currentTimeMillis();
        long t3 = e3 - s3;
        logger.info("-----------------------------------------");
        logger.info("保存学生所需时间为：{}毫秒", t3);
        //获取所有的学生ID
        List<String> userIdList = new ArrayList<>();
        for (MStudent student : studentList) {
            userIdList.add(student.getUserId());
        }
        long s4 = System.currentTimeMillis();
        //插入m_users
        saveUser(userList, IdentityIdConstants.STUDENT, IdentityIdConstants.STUDENT_NAME, school);
        long e4 = System.currentTimeMillis();
        long t4 = e4 - s4;
        logger.info("-----------------------------------------");
        logger.info("插入m_users所需时间为：{}毫秒", t4);
        long s5 = System.currentTimeMillis();
        //插入cas_login
        saveCasLogin(userIdList);
        long e5 = System.currentTimeMillis();
        long t5 = e5 - s5;
        logger.info("-----------------------------------------");
        logger.info("插入cas_login所需时间为：{}毫秒", t5);
        long s6 = System.currentTimeMillis();
        //插入m_user_role
        saveUserRole(userIdList, RoleIdConstants.STUDENT, school.getId());
        long e6 = System.currentTimeMillis();
        long t6 = e6 - s6;
        logger.info("-----------------------------------------");
        logger.info("插入m_user_role所需时间为：{}毫秒", t6);
        long s7 = System.currentTimeMillis();
        //插入m_login_user
        saveLoginUser(userIdList);
        long e7 = System.currentTimeMillis();
        long t7 = e7 - s7;
        logger.info("-----------------------------------------");
        logger.info("插入m_login_user所需时间为：{}毫秒", t7);
    }

    //批量保存老师数据
    private void saveTeacherList(List<ExcelState> userList, MSchool school) {
        for (ExcelState estate : userList) {
            estate.setUserId(seqGeneratorService.generate("m_users").toString());
        }
        //插入老师表
        List<MTeacher> teacherList = new ArrayList<>();
        for (ExcelState es : userList) {
            MTeacher teacher = new MTeacher();
            teacher.setUserId(es.getUserId());
            teacher.setSchoolId(school.getId());
            teacher.setSchoolName(school.getName());
            teacher.setPositionId(PositionConstants.TEACHER);
            teacher.setPositionName(PositionConstants.TEACHER_NAME);
            teacher.setIsManager(0);
            teacherList.add(teacher);
        }
        //teacherMapper.saveTeacherList(teacherList);
        saveTeacher(teacherList);
        //获取所有的教师ID
        List<String> userIdList = new ArrayList<>();
        for (MTeacher teacher : teacherList) {
            userIdList.add(teacher.getUserId());
        }
        //插入m_users
        saveUser(userList, IdentityIdConstants.TEACHER, IdentityIdConstants.TEACHER_NAME, school);
        //插入cas_login
        saveCasLogin(userIdList);
        //插入m_user_role
        saveUserRole(userIdList, RoleIdConstants.TEACHER, school.getId());
        //插入m_login_user
        saveLoginUser(userIdList);
    }

    //根据年级号和班级号获取班级ID
    private MClass getClassByNums(ExcelState es, MSchool school, String userId, Map<String, MClass> classMap,
                                  Map<Integer, String> yearPeriodIdMap) {
        //根据年级号获取年份
        int year = ClassNameUtil.getYear(Integer.parseInt(es.getGradeNum()));
        MClass mClass = classMap.get(year + "-" + es.getClassNum());
        //判断是否有该班级
        if (mClass == null) {
            mClass = classMapper.getClassByYearAndName(year, Integer.parseInt(es.getClassNum()), school.getId());
            if (mClass != null) {
                classMap.put(year + "-" + es.getClassNum(), mClass);
            }
        }
        if (mClass == null) {
            //创建班级
            mClass = saveClass(school, year, Integer.parseInt(es.getClassNum()), userId, yearPeriodIdMap);
            classMap.put(year + "-" + es.getClassNum(), mClass);
        }
        return mClass;
    }

    //创建班级
    private MClass saveClass(MSchool school, int year, int classNum, String userId, Map<Integer, String> yearPeriodIdMap) {
        MClass mClass = new MClass();
        mClass.setClassId(getClassId());
        mClass.setSchoolId(school.getId());
        mClass.setSchoolName(school.getName());
        mClass.setYears(year);
        mClass.setCodeSharing(classNum);
        mClass.setAliasName(ClassNameUtil.getFullName(year, classNum));
        mClass.setName(String.format(ClassConstants.NAME_PATTERN, classNum));
        mClass.setCreateTime(new Date());
        mClass.setCreatorId(userId);
        UserDTO userDTO = getByUserId(userId);
        mClass.setCreatorName(userDTO.getRealName());
        mClass.setStatus(ClassConstants.STATUS_OK);
        mClass.setmImage(UserConstants.ICON_DEFAULT);
        String periodId = yearPeriodIdMap.get(year);
        if (periodId == null) {
            periodId = getSchoolPeriodIdByYear(year, school.getId());
            yearPeriodIdMap.put(year, periodId);
        }
        mClass.setSchoolPeriodId(periodId);
        classMapper.insert(mClass);
        return mClass;
    }

    //根据年份判断学段
    private String getSchoolPeriodIdByYear(int year, String schoolId) {
        List<MSchoolPeriod> schoolPeriodList = schoolPeriodMapper.selectBySchoolId(schoolId);
        int pPeriod = 0;
        int jPeriod = 0;
        int sPeriod = 0;
        MSchoolPeriod primary = new MSchoolPeriod();
        MSchoolPeriod junior = new MSchoolPeriod();
        MSchoolPeriod senior = new MSchoolPeriod();
        for (MSchoolPeriod sp : schoolPeriodList) {
            if (sp.getSection() == SectionConstants.PRIMARY_SCHOOL_NUM) {
                pPeriod = sp.getLength();
                primary = sp;
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
                return primary.getId();
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
        //创建学制学段
        int gradeNum = ClassNameUtil.getGradeNum(year);
        if (pPeriod != 0) {//有小学
            if (gradeNum > pPeriod && gradeNum <= 9) {//插入的年级是初中
                MSchoolPeriod schoolPeriod = new MSchoolPeriod();
                schoolPeriod.setId(schoolId + "-1-" + (9 - pPeriod));
                schoolPeriod.setSection(1);
                schoolPeriod.setSectionDisplay("初中");
                schoolPeriod.setLength(9 - pPeriod);
                schoolPeriod.setSchoolId(schoolId);
                schoolPeriodMapper.insert(schoolPeriod);
                return schoolPeriod.getId();
            } else {//插入的年级是高中
                if (jPeriod != 0) {//初中有就插入高中
                    MSchoolPeriod schoolPeriod1 = insertSenior(schoolId);
                    return schoolPeriod1.getId();
                } else {//初中没有就插入初中高中
                    MSchoolPeriod schoolPeriod = new MSchoolPeriod();
                    schoolPeriod.setId(schoolId + "-1-" + (9 - pPeriod));
                    schoolPeriod.setSection(1);
                    schoolPeriod.setSectionDisplay("初中");
                    schoolPeriod.setLength(9 - pPeriod);
                    schoolPeriod.setSchoolId(schoolId);
                    schoolPeriodMapper.insert(schoolPeriod);
                    MSchoolPeriod schoolPeriod1 = insertSenior(schoolId);
                    return schoolPeriod1.getId();
                }
            }
        }
        if (jPeriod != 0) {//没有小学，有初中
            if (gradeNum <= 9 - jPeriod) {//当前插入小学
                MSchoolPeriod schoolPeriod = new MSchoolPeriod();
                schoolPeriod.setId(schoolId + "-0-" + (9 - jPeriod));
                schoolPeriod.setSection(0);
                schoolPeriod.setSectionDisplay("小学");
                schoolPeriod.setLength(9 - jPeriod);
                schoolPeriod.setSchoolId(schoolId);
                schoolPeriodMapper.insert(schoolPeriod);
                return schoolPeriod.getId();
            } else {//当前肯定高中
                MSchoolPeriod schoolPeriod = insertSenior(schoolId);
                return schoolPeriod.getId();
            }
        }
        if (sPeriod != 0) {//小学初中都没有，有高中
            if (gradeNum <= 6) {
                MSchoolPeriod schoolPeriod = new MSchoolPeriod();
                schoolPeriod.setId(schoolId + "-0-6");
                schoolPeriod.setSection(0);
                schoolPeriod.setSectionDisplay("小学");
                schoolPeriod.setLength(6);
                schoolPeriod.setSchoolId(schoolId);
                schoolPeriodMapper.insert(schoolPeriod);
                MSchoolPeriod schoolPeriod1 = new MSchoolPeriod();
                schoolPeriod1.setId(schoolId + "-1-3");
                schoolPeriod1.setSection(1);
                schoolPeriod1.setSectionDisplay("初中");
                schoolPeriod1.setLength(3);
                schoolPeriod1.setSchoolId(schoolId);
                schoolPeriodMapper.insert(schoolPeriod1);
                return schoolPeriod.getId();
            } else {//插入初中
                MSchoolPeriod schoolPeriod = new MSchoolPeriod();
                schoolPeriod.setId(schoolId + "-1-3");
                schoolPeriod.setSection(1);
                schoolPeriod.setSectionDisplay("初中");
                schoolPeriod.setLength(3);
                schoolPeriod.setSchoolId(schoolId);
                schoolPeriodMapper.insert(schoolPeriod);
                return schoolPeriod.getId();
            }
        }
        throw new MemberException("数据有异常，请联系管理员");
    }

    private MSchoolPeriod insertSenior(String schoolId) {
        MSchoolPeriod schoolPeriod = new MSchoolPeriod();
        schoolPeriod.setId(schoolId + "-2-3");
        schoolPeriod.setSection(2);
        schoolPeriod.setSectionDisplay("高中");
        schoolPeriod.setLength(3);
        schoolPeriod.setSchoolId(schoolId);
        schoolPeriodMapper.insert(schoolPeriod);
        return schoolPeriod;
    }


    private String getClassId() {
        String classId = UUID.randomUUID().toString();
        classId = classId.replaceAll("\\-", "");
        logger.info("insertClass;id:" + classId);
        return classId;
    }

    private String getPasswordLogId() {
        String id = UUID.randomUUID().toString();
        id = id.replaceAll("\\-", "");
        logger.info("insertPasswordLog;id:" + id);
        return id;
    }

    //转换excel中的数据
    private List<ExcelState> queryExcelInfo(ArrayList<ArrayList<Object>> lists) {
        //加上头标题，不能超过10001条
        MemberAssert.isTrue(lists.size() <= UserConstants.EXCEL_DATA_NUMBER + 1, "数据不能超过" + UserConstants.EXCEL_DATA_NUMBER + "条", MemberLogicContext.EXCEL_CODE_DATA_NUMBER_IS_MANY);
        List<ExcelState> esList = new ArrayList<>(lists.size() - 1);
        for (int i = 1, len = lists.size(); i < len; i++) {
            ExcelState es = new ExcelState();
            List<Object> row = lists.get(i);
            es.setRealName(row.size() > 0 ? ((String) row.get(0)).replaceAll(" +", "").trim() : null);
            es.setIdentityDescription(row.size() > 1 ? ((String) row.get(1)).replaceAll(" +", "").trim() : null);
            es.setGradeNum(row.size() > 2 ? ((String) row.get(2)).replaceAll(" +", "").trim() : null);
            es.setClassNum(row.size() > 3 ? ((String) row.get(3)).replaceAll(" +", "").trim() : null);
            es.setMobile(row.size() > 4 ? ((String) row.get(4)).replaceAll(" +", "").trim() : null);
            es.setSex(row.size() > 5 ? ((String) row.get(5)).replaceAll(" +", "").trim() : null);
            if (StringUtils.isNotEmpty(es.getRealName()) || StringUtils.isNotEmpty(es.getIdentityDescription()) || StringUtils.isNotEmpty(es.getGradeNum())
                    || StringUtils.isNotEmpty(es.getClassNum()) || StringUtils.isNotEmpty(es.getMobile()) || StringUtils.isNotEmpty(es.getSex())) {
                esList.add(es);
            }
        }
        return esList;
    }

    //检测数据数据合理性
    private String checkDataAndSave(List<ExcelState> excelList, String type, String schoolId, String userId) {
        List<ExcelState> teacherTrueList = new ArrayList<>();
        List<ExcelState> studentTrueList = new ArrayList<>();
        int errorRows = 0;
        for (ExcelState es : excelList) {
            if (StringUtils.isNotBlank(es.getMobile()) && es.getMobile().length() > 20) {
                es.setErrorInfo("联系电话错误");
                errorRows++;
                continue;
            }
            if (IdentityIdConstants.TEACHER.equals(type)) {
                if (StringUtils.isEmpty(es.getIdentityDescription()) || !es.getIdentityDescription().equals(IdentityIdConstants.TEACHER_NAME)) {
                    es.setErrorInfo("用户类型错误");
                    errorRows++;
                    continue;
                }
                if (checkTeacherData(es)) {
                    teacherTrueList.add(es);
                } else {
                    errorRows++;
                }
                continue;
            }
            if (IdentityIdConstants.STUDENT.equals(type)) {
                if (StringUtils.isEmpty(es.getIdentityDescription()) || !es.getIdentityDescription().equals(IdentityIdConstants.STUDENT_NAME)) {
                    es.setErrorInfo("用户类型错误");
                    errorRows++;
                    continue;
                }
                if (checkStudentData(es)) {
                    studentTrueList.add(es);
                } else {
                    errorRows++;
                }
                continue;
            }

            if (StringUtils.isNotEmpty(es.getIdentityDescription()) &&
                    es.getIdentityDescription().equals(IdentityIdConstants.TEACHER_NAME)) {
                if (checkTeacherData(es)) {
                    teacherTrueList.add(es);
                } else {
                    errorRows++;
                }
                continue;
            } else if (StringUtils.isNotEmpty(es.getIdentityDescription()) &&
                    es.getIdentityDescription().equals(IdentityIdConstants.STUDENT_NAME)) {
                if (checkStudentData(es)) {
                    studentTrueList.add(es);
                } else {
                    errorRows++;
                }
            } else {
                es.setErrorInfo("用户类型错误");
                errorRows++;
            }
        }
        if (errorRows > 0) {
            return "您有" + errorRows + "条错误数据";
        }
        MSchool school = schoolMapper.selectByPrimaryKey(schoolId);
        saveTeacherList(teacherTrueList, school);
        saveStudentList(studentTrueList, school, userId);
        return null;
    }

    //检测教师数据合理性
    private boolean checkTeacherData(ExcelState es) {
        if (StringUtils.isEmpty(es.getRealName())) {
            es.setErrorInfo("用户姓名不能为空");
            return false;
        } else if (es.getRealName().length() > 10) {
            es.setErrorInfo("用户姓名长度过长");
            return false;
        }
        if (!(StringUtils.isEmpty(es.getSex()) || (es.getSex().contains("男") || es.getSex().contains("女")))) {
            es.setErrorInfo("性别设置错误");
            return false;
        }
        return true;
    }

    //检测学生数据合理性
    private boolean checkStudentData(ExcelState es) {
        if (StringUtils.isEmpty(es.getRealName())) {
            es.setErrorInfo("用户姓名不能为空");
            return false;
        } else if (es.getRealName().length() > 10) {
            es.setErrorInfo("用户姓名长度过长");
            return false;
        }
        if (!(StringUtils.isEmpty(es.getSex()) || (es.getSex().contains("男") || es.getSex().contains("女")))) {
            es.setErrorInfo("性别设置错误");
            return false;
        }
        if (StringUtils.isEmpty(es.getGradeNum())) {
            es.setErrorInfo("年级不能为空");
            return false;
        } else if (!es.getGradeNum().matches("\\d{1,2}")) {
            es.setErrorInfo("年级设置错误");
            return false;
        } else {
            int grade = Integer.parseInt(es.getGradeNum());
            if (grade < 1 || grade > 12) {
                es.setErrorInfo("年级设置错误");
                return false;
            }
        }
        if (StringUtils.isEmpty(es.getClassNum())) {
            es.setErrorInfo("班级不能为空");
            return false;
        } else if (!es.getClassNum().matches("\\d{1,4}")) {
            es.setErrorInfo("班级设置错误");
            return false;
        } else {
            int classNum = Integer.parseInt(es.getClassNum());
            if (classNum < 1 || classNum > 9999) {
                es.setErrorInfo("班级设置错误");
                return false;
            }
        }
        return true;
    }

    @Override
    public void updateSendActiveCodeEmail(String email, String userId, String userName) {
        UserDTO userDTO = getByUserId(userId);
        String emailUser = userDTO.getMobile();
        if (email.equals(emailUser)) {
            throw new MemberException("该用户已绑定该邮箱");
        }
        if (casLoginMapper.selectByPrimaryKey(email) != null) {
            throw new MemberException("该email已与其他用户绑定");
        }
        updateEmail(userId, email);
        sendMessageService.sendActiveCodeEmail(email, userName);
    }

    @Override//by wuxh
    public void updateBindMobile(String userId, String mobile, String code) {
        //验证手机号码的合法性
        if (CheckRegexUtil.checkMobileIsLegal(mobile)) {
            if (sendMessageService.checkCode(mobile, code, SendMessageService.CodeType.BindPhone)) {
                String mobileLoginId = casLoginMapper.getLoginIdByUserIdAndLoginType(userId, UserConstants.PHONE);
                //解除已绑定的手机
                if (!mobile.equals(mobileLoginId)) {
                    deleteMobileOrEmail(userId, UserConstants.PHONE);
                }
                CasLogin casLogin = new CasLogin();
                casLogin.setUserId(userId);
                casLogin.setLoginType(UserConstants.PHONE);
                casLogin.setLoginId(mobile);
                CasLogin temp = casLoginMapper.selectByPrimaryKey(userId);
                casLogin.setPassword(temp.getPassword());
                usersMapper.updateMobile(userId, mobile, getSchoolId(userId));
                casLoginMapper.insert(casLogin);
                String redisKey = MessageFormat.format(SecurityConstants.MEMBER_REDIS_PHONE_KEY, SendMessageService.CodeType.BindPhone.getName(), mobile);
                cacheService.delete(redisKey);
            }
            invalidCache(userId);
        }
    }

    private void invalidCache(String... userIds) {
        if (userIds == null || userIds.length == 0) {
            return;
        }
        List<String> list = new ArrayList<>(userIds.length);
        for (String userId : userIds) {
            if (StringUtils.isEmpty(userId)) {
                continue;
            }
            list.add(MessageFormat.format(CacheConstants.KEY_USER, userId));
        }
        if (list.isEmpty()) {
            return;
        }
        cacheService.delete(list);
    }

    @Override //by wuxh
    public void updateEmail(String userId, String email) {
        //验证电子邮箱的合法性
        if (CheckRegexUtil.checkEmailIsLegal(email)) {
            usersMapper.updateEmail(userId, email, getSchoolId(userId));
            invalidCache(userId);
        }

    }

    @Override
    public void saveActivateEmail(String userId, String email, String code) {
        //验证电子邮箱的合法性
        if (CheckRegexUtil.checkEmailIsLegal(email)) {
            if (sendMessageService.checkCode(email, code, SendMessageService.Template.BindEmail)) {
                String emailLoginId = casLoginMapper.getLoginIdByUserIdAndLoginType(userId, UserConstants.EMAIL);
                //解除已绑定的邮箱
                if (!email.equals(emailLoginId)) {
                    deleteMobileOrEmail(userId, UserConstants.EMAIL);
                }
                deleteMobileOrEmail(userId, UserConstants.EMAIL);
                usersMapper.updateEmail(userId, email, getSchoolId(userId));
                invalidCache(userId);
                CasLogin casLogin = new CasLogin();
                casLogin.setUserId(userId);
                casLogin.setLoginType(UserConstants.EMAIL);
                casLogin.setLoginId(email);
                CasLogin temp = casLoginMapper.selectByPrimaryKey(userId);
                if (temp != null) {
                    casLogin.setPassword(temp.getPassword());
                    casLoginMapper.insert(casLogin);
                    String redisKey = MessageFormat.format(SecurityConstants.MEMBER_REDIS_EMAIL_KEY, SendMessageService.Template.BindEmail.getName(), email);
                    cacheService.delete(redisKey);
                } else {
                    logger.error("登录表中不存在改用户数据记录！");
                    throw new MemberException("账号错误，请联系管理员！");
                }
            }
        }
    }

    /**
     * 修改密码
     *
     * @param userId      用户id
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    @Override
    public void updateChangePassword(String userId, String oldPassword, String newPassword) {
        validPasswordForChange(newPassword);
        CasLogin casLogin = casLoginMapper.selectByPrimaryKey(userId);
        if (MemberEncryptUtil.aesEncrypt(oldPassword).equals(casLogin.getPassword())) {
            updatePassword(userId, newPassword);
        } else {
            throw new MemberException("原密码不正确");
        }
    }

    private void validPasswordForChange(String newPassword) {
        if ("123456".equals(newPassword)) {
            throw new MemberException("密码不能为123456，请重新输入。");
        }
    }

    /**
     * 修改密码记录日志
     *
     * @param logPasswordDTO 修改信息
     */
    @Override
    public void updatePasswordLog(LogPasswordDTO logPasswordDTO) {
        List<CasLogin> casLoginDTOList = casLoginMapper.queryUsersCasLogin(logPasswordDTO.getUserIdList());
        if (StringUtils.isEmpty(logPasswordDTO.getSchoolId())) {
            logPasswordDTO.setSchoolId(getSchoolId(logPasswordDTO.getOperatorUserId()));
        }
        for (CasLogin casLogin : casLoginDTOList) {
            //把修改的用户密码信息保存到日志表m_password_log
            MPasswordLog mPasswordLog = new MPasswordLog();
            BeanUtils.copyProperties(logPasswordDTO, mPasswordLog);
            mPasswordLog.setUserId(casLogin.getUserId());
            mPasswordLog.setId(getPasswordLogId());
            mPasswordLog.setNewPassword(MemberEncryptUtil.aesEncrypt(mPasswordLog.getNewPassword()));
            mPasswordLog.setOldPassword(casLogin.getPassword());
            mPasswordLog.setCreateTime(new Date());
            mPasswordLogMapper.insert(mPasswordLog);
        }
    }

    /**
     * 修改用户信息
     */
    @Override
    public void updateByUserId(UserDTO userDTO) {
        //修改m_users
        MUsers mUsers = new MUsers();
        if (StringUtils.isEmpty(userDTO.getSchoolId())) {
            userDTO.setSchoolId(getSchoolId(userDTO.getUserId()));
        }
        BeanUtils.copyProperties(userDTO, mUsers);
        usersMapper.updateByPrimaryKey(mUsers);
        if (StringUtils.isNotBlank(mUsers.getRealName())) {
            //修改m_family_relation的名称
            familyRelationService.updateName(mUsers.getUserId(), mUsers.getRealName());
            //删除缓存
            if (IdentityIdConstants.PARENT.equals(mUsers.getIdentityId())) {//修改家长的名称
                //家长名称修改，删除孩子的缓存
                deleteRedisOfKid(mUsers.getUserId());
            } else if (IdentityIdConstants.STUDENT.equals(mUsers.getIdentityId())) {//修改学生的名称
                //自己修改学生名称
                deleteRedisNameOfParent(mUsers.getUserId(), mUsers.getSchoolId());
            }
        }
        invalidCache(userDTO.getUserId());
    }

    /**
     * 个人面板
     */
    @Override
    public List<RelativeDTO> getPersonPanel(String userId) {
        //1、获取相关角色id
        List<String> userIds = casLoginMapper.getUserIds(userId);
        if (CollectionUtils.isEmpty(userIds)) {
            logger.error("该用户没有相关角色信息");
            throw new MemberException("该用户没有相关角色信息");
        }
        List<UserDTO> usersList = new ArrayList<>();
        for (String eachUserId : userIds) {
            UserDTO u = getByUserId(eachUserId);
            if (u == null) {
                logger.debug("{}用户不存在", eachUserId);
            } else {
                usersList.add(getByUserId(eachUserId));
            }
        }
        //2、获取相关角色信息
        List<RelativeDTO> roleList = new ArrayList<>();
        for (UserDTO user : usersList) {
            RelativeDTO role = new RelativeDTO();
            role.setUserId(user.getUserId());
            role.setRealName(user.getRealName());
            if (StringUtils.isNotEmpty((user.getIcon()))) {
                role.setIcon(user.getIcon());
            } else {
                role.setIcon(UserConstants.ICON_DEFAULT);
            }
            role.setIdentityId(user.getIdentityId());
            if (user.getIdentityId().equals(IdentityIdConstants.TEACHER)) {
                //学校名称
                MSchool school = schoolMapper.selectByPrimaryKey(user.getSchoolId());
                if (school == null) {
                    logger.error("该用户的相关角色中老师角色没有学校信息");
                    throw new MemberException("该用户的相关角色中老师角色没有学校信息");
                }
                String schoolId = school.getId();
                role.setOrgName(school.getName());
                //获取授课班级
                List<MClassInfo> classInfo = teacherMapper.getTeacherClass(user.getUserId(), schoolId);
                List<String> claList = queryUserMessage(classInfo);
                role.setClassList(claList);
                //获取职务信息
                MTeacher teacher = teacherMapper.selectByPrimaryKey(user.getUserId(), schoolId);
                if (teacher != null) {
                    role.setPositionId(teacher.getPositionId());
                    role.setPositionName(teacher.getPositionName());
                }
            } else if (user.getIdentityId().equals(IdentityIdConstants.STUDENT)) {
                //学校名称
                MSchool school = schoolMapper.selectByPrimaryKey(user.getSchoolId());
                if (school == null) {
                    logger.error("该用户的相关角色中学生角色没有学校信息");
                    throw new MemberException("该用户的相关角色中学生角色没有学校信息");
                }
                role.setOrgName(school.getName());
                role.setPositionName(user.getIdentityDescription());
            } else if (user.getIdentityId().equals(IdentityIdConstants.EDUCATION_MANAGER)) {
                //机构名称
                MEducationManager educationManager = educationManagerMapper.selectByPrimaryKey(user.getUserId());
                if (educationManager == null) {
                    logger.error("该用户的相关角色中机构管理员没有机构信息");
                    throw new MemberException("该用户的相关角色中机构管理员没有机构信息");
                }
                role.setOrgName(educationManager.getEducationalName());
                role.setPositionName(user.getIdentityDescription());
            } else if (user.getIdentityId().equals(IdentityIdConstants.EDUCATION_STAFF)) {
                //获取职务信息（根据身份）
                MEducation education = educationMapper.selectByPrimaryKey(user.getUserId());
                if (education == null) {
                    logger.error("该用户的相关角色中机构人员没有机构信息");
                    throw new MemberException("该用户的相关角色中机构人员没有机构信息");
                }
                //机构名称
                role.setOrgName(education.getEducationalName());
                role.setPositionName(education.getDutyName());
            } else if (user.getIdentityId().equals(IdentityIdConstants.SCHOOL_MANAGER)) {
                //学校名称
                MSchool school = schoolMapper.selectByPrimaryKey(user.getSchoolId());
                if (school == null) {
                    logger.error("该用户的相关角色中学校管理员角色没有学校信息");
                    throw new MemberException("该用户的相关角色中学校管理员角色没有学校信息");
                }
                role.setOrgName(school.getName());
                role.setPositionName(user.getIdentityDescription());
            } else if (user.getIdentityId().equals(IdentityIdConstants.PARENT)) {
                role.setOrgName("我的家庭");
                role.setPositionName("亲人");
                //获取孩子信息
                Map<String, String> kidList = new HashMap<>();
                List<FamilyRelationDTO> famillyRelationList = queryChildsByParentId(user.getUserId());
                for (FamilyRelationDTO fr : famillyRelationList) {
                    kidList.put(fr.getTargetUserName(), fr.getMemberName());
                    role.setKidList(kidList);
                }
            }
            roleList.add(role);
        }
        return roleList;
    }

    //获取班级信息
    private List<String> queryUserMessage(List<MClassInfo> cla) {
        List<String> claList = new ArrayList<>();
        for (MClassInfo ci : cla) {
            claList.add(ClassNameUtil.getGradeName(ci.getYear()) + ci.getClassName());
        }
        return claList;
    }

    //批量保存学生

    private void saveStudent(List<MStudent> studentList) {
        Connection conn = DataSourceUtils.getConnection(dataSource);
        String sql = "insert into m_student (user_id, class_id, class_name, class_alias_name," +
                "student_number, family_name,  family_cover, school_id, school_name,join_class)values (?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(sql);
            int i = 0;
            for (MStudent student : studentList) {
                i++;
                pst.setString(1, student.getUserId());
                pst.setString(2, student.getClassId());
                pst.setString(3, student.getClassName());
                pst.setString(4, student.getClassAliasName());
                pst.setString(5, student.getStudentNumber());
                pst.setString(6, student.getFamilyName());
                pst.setString(7, student.getFamilyCover());
                pst.setString(8, student.getSchoolId());
                pst.setString(9, student.getSchoolName());
                pst.setTimestamp(10, new java.sql.Timestamp(student.getJoinClass().getTime()));
                pst.addBatch();
                if (i % UserConstants.BATCH_SIZE == 0) {//可以设置不同的大小；如50，100，500，1000等等
                    pst.executeBatch();
                    pst.clearBatch();
                    i = 0;
                }
            }
            if (i != 0) {
                pst.executeBatch();
                pst.clearBatch();
            }
        } catch (SQLException e) {
            throw new MemberException("批量插入数据错误");
        }
    }

    //批量保存老师表
    private void saveTeacher(List<MTeacher> teacherList) {
        Connection conn = DataSourceUtils.getConnection(dataSource);
        String sql = "insert into m_teacher (user_id, school_id, school_name," +
                "book_id, position_id, position_name,  is_manager)" +
                "values(?,?,?,?,?,?,?)";
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(sql);
            int i = 0;
            for (MTeacher teacher : teacherList) {
                i++;
                pst.setString(1, teacher.getUserId());
                pst.setString(2, teacher.getSchoolId());
                pst.setString(3, teacher.getSchoolName());
                pst.setString(4, teacher.getBookId());
                pst.setString(5, teacher.getPositionId());
                pst.setString(6, teacher.getPositionName());
                pst.setInt(7, teacher.getIsManager());
                pst.addBatch();
                if (i % UserConstants.BATCH_SIZE == 0) {//可以设置不同的大小；如50，100，500，1000等等
                    pst.executeBatch();
                    pst.clearBatch();
                    i = 0;
                }
            }
            if (i != 0) {
                pst.executeBatch();
                pst.clearBatch();
            }
        } catch (SQLException e) {
            throw new MemberException("批量插入数据错误");
        }
    }

    //批量保存用户表
    private void saveUser(List<ExcelState> usersList, String identityId, String identityDescription, MSchool school) {
        Connection conn = DataSourceUtils.getConnection(dataSource);
        String sql = "insert into m_users (user_id, real_name, sex, icon, add_time,status, mobile," +
                "update_time, identity_id,identity_description, school_id,  area)" +
                "values(?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(sql);

            int i = 0;
            for (ExcelState es : usersList) {
                i++;
                pst.setString(1, es.getUserId());
                pst.setString(2, es.getRealName());
                if (StringUtils.isNotEmpty(es.getSex()) && es.getSex().contains("男")) {
                    pst.setInt(3, UserConstants.SEX_MAN);
                } else if (StringUtils.isNotEmpty(es.getSex()) && es.getSex().contains("女")) {
                    pst.setInt(3, UserConstants.SEX_WOMAN);
                } else {
                    pst.setNull(3, java.sql.Types.NULL);
                }
                pst.setString(4, UserConstants.ICON_DEFAULT);
                pst.setTimestamp(5, new java.sql.Timestamp(new Date().getTime()));
                pst.setInt(6, UserConstants.STATUS_INIT);
                pst.setString(7, es.getMobile());
                pst.setTimestamp(8, new java.sql.Timestamp(new Date().getTime()));
                pst.setString(9, identityId);
                pst.setString(10, identityDescription);
                pst.setString(11, school.getId());
                pst.setString(12, school.getArea());
                pst.addBatch();
                if (i % UserConstants.BATCH_SIZE == 0) {//可以设置不同的大小；如50，100，500，1000等等
                    pst.executeBatch();
                    pst.clearBatch();
                    i = 0;
                }
            }
            if (i != 0) {
                pst.executeBatch();
                pst.clearBatch();
            }
        } catch (SQLException e) {
            throw new MemberException("批量插入数据错误");
        }
    }


    //批量保存登录表
    private void saveCasLogin(List<String> userIdList) {
        Connection conn = DataSourceUtils.getConnection(dataSource);
        String sql = "insert into cas_login (login_id,login_type, password, user_id, status)" +
                "values(?,?,?,?,?)";
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(sql);
            int i = 0;
            for (String userId : userIdList) {
                i++;
                pst.setString(1, userId);
                pst.setInt(2, UserConstants.MEMBER);
                pst.setString(3, UserConstants.RESET_PASSWORD);
                pst.setString(4, userId);
                pst.setInt(5, 1);
                pst.addBatch();
                if (i % UserConstants.BATCH_SIZE == 0) {//可以设置不同的大小；如50，100，500，1000等等
                    pst.executeBatch();
                    pst.clearBatch();
                    i = 0;
                }
            }
            if (i != 0) {
                pst.executeBatch();
                pst.clearBatch();
            }
        } catch (SQLException e) {
            throw new MemberException("批量插入数据错误");
        }
    }

    //批量保存用户角色表
    private void saveUserRole(List<String> userIdList, String roleId, String schoolId) {
        Connection conn = DataSourceUtils.getConnection(dataSource);
        String sql = "insert into m_user_role (user_id, role_id, school_id,id)" +
                "values(?,?,?,?)";
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(sql);
            int i = 0;
            for (String userId : userIdList) {
                i++;
                pst.setString(1, userId);
                pst.setString(2, roleId);
                pst.setString(3, schoolId);
                pst.setLong(4, seqGeneratorService.generate("m_user_role"));
                pst.addBatch();
                if (i % UserConstants.BATCH_SIZE == 0) {//可以设置不同的大小；如50，100，500，1000等等
                    pst.executeBatch();
                    pst.clearBatch();
                    i = 0;
                }
            }
            if (i != 0) {
                pst.executeBatch();
                pst.clearBatch();
            }
        } catch (SQLException e) {
            throw new MemberException("批量插入数据错误");
        }
    }

    //批量保存登录认证表
    private void saveLoginUser(List<String> userIdList) {
        Connection conn = DataSourceUtils.getConnection(dataSource);
        String sql = "insert into m_login_user (id,user_id, bind_user_id)" +
                "values(?,?,?)";
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(sql);

            int i = 0;
            for (String userId : userIdList) {
                i++;
                Long id = seqGeneratorService.generate("m_login_user");
                pst.setLong(1, id);
                pst.setString(2, userId);
                pst.setString(3, userId);
                pst.addBatch();
                if (i % UserConstants.BATCH_SIZE == 0) {//可以设置不同的大小；如50，100，500，1000等等
                    pst.executeBatch();
                    pst.clearBatch();
                    i = 0;
                }
            }
            if (i != 0) {
                pst.executeBatch();
                pst.clearBatch();
            }
        } catch (SQLException e) {
            throw new MemberException("批量插入数据错误");
        }
    }

    /**
     * @param userId    用户ID
     * @param loginType 采用常量类中的方式（），不使用数字
     */
    @Override
    public void deleteMobileOrEmail(String userId, Integer loginType) {
        int type = loginType.intValue();
        if (type == UserConstants.MEMBER) {
            throw new MemberException("学乐号不可删除");
        } else if (type == UserConstants.PHONE) {
            usersMapper.updateMobile(userId, null, getSchoolId(userId));
        } else if (type == UserConstants.EMAIL) {
            usersMapper.updateEmail(userId, null, getSchoolId(userId));
        }
        casLoginMapper.deleteByUserIdAndLoginType(userId, loginType);
        invalidCache(userId);
    }

    @Override
    @Deprecated
    public void saveLoginStatistics(String userId, String schoolId, Integer type) {
        CasLoginLogDTO casLoginLogDTO = new CasLoginLogDTO();
        casLoginLogDTO.setLoginId(userId);
        casLoginLogDTO.setLoginType(type);
        casLoginLogDTO.setSchoolId(schoolId);
        casLoginLogDTO.setUserId(userId);
        saveLoginStatistics(casLoginLogDTO);
    }

    @Override
    public void saveLoginStatistics(CasLoginLogDTO casLoginLogDTO) {
        MLoginStatistics mLoginStatistics = new MLoginStatistics();
        mLoginStatistics.setUserId(casLoginLogDTO.getUserId());
        mLoginStatistics.setSchoolId(casLoginLogDTO.getSchoolId());
        Date date = new Date();
        mLoginStatistics.setLastLoginTime(date);
        mLoginStatistics.setLoginType(casLoginLogDTO.getLoginType());
        mLoginStatistics.setLoginIp(casLoginLogDTO.getLoginIp());
        if (mLoginStatisticsMapper.getCountByPrimaryKey(casLoginLogDTO.getUserId(), casLoginLogDTO.getSchoolId()) == 0) {
            mLoginStatistics.setFirstLoginTime(date);
            mLoginStatisticsMapper.insert(mLoginStatistics);
        } else {
            mLoginStatisticsMapper.updateByPrimaryKey(mLoginStatistics);
        }
        CasLoginLog casLoginLog = new CasLoginLog();
        Long id = seqGeneratorService.generate("cas_login_log");
        casLoginLog.setId(id);
        casLoginLog.setUserId(casLoginLogDTO.getUserId());
        casLoginLog.setLoginType(casLoginLogDTO.getLoginType());
        casLoginLog.setLoginId(casLoginLogDTO.getLoginId());
        casLoginLog.setLoginTimestamp(date);
        casLoginLog.setSchoolId(casLoginLogDTO.getSchoolId());
        casLoginLog.setLoginIp(casLoginLogDTO.getLoginIp());
        casLoginLogMapper.insert(casLoginLog);
    }


    @Override
    public void saveIcon(String userId, String icon) {
        if (StringUtils.isEmpty(userId)) {
            logger.debug("userId不能为空");
        }
        if (StringUtils.isEmpty(icon)) {
            logger.debug("icon不能为空");
            throw new MemberException("icon不能为空");
        }
        String schoolId = getSchoolId(userId);
        UserDTO mUser = getByUserId(userId);
        String oldIcon;
        if (mUser != null) {
            oldIcon = mUser.getIcon();
        } else {
            logger.error("userId不能为空:userId{};schoolId{}", userId, schoolId);
            throw new MemberException("找不到该用户！");
        }

        //插入一条新的数据到m_user_icon表中
        MUserIcon mUserIcon = new MUserIcon();
        Long userIconId = seqGeneratorService.generate("m_user_icon");
        mUserIcon.setId(userIconId);
        mUserIcon.setIcon(icon);
        mUserIcon.setUserId(userId);
        mUserIcon.setStatus(1);

        //查看该用户的该头像是否已有，没有就插入，若有的话，根据状态进行操作
        MUserIcon mUserIcon1 = mUserIconMapper.getByUserIdAndIcon(userId, icon);

        //八种状态
        if (oldIcon == null) {
            mUserIconMapper.insert(mUserIcon);
            usersMapper.updateIconByUserId(userId, icon, getSchoolId(userId));
        } else {
            if (mUserIcon1 != null) {
                //库中该用户已使用过该头像，且现在是废用状态
                if (mUserIcon1.getStatus() != 1) {
                    mUserIconMapper.updateStatusByUserIdAndIcon(userId, icon, 1);
                    if (!oldIcon.equals(icon)) {
                        mUserIconMapper.updateStatusByUserIdAndIcon(userId, oldIcon, 0);
                        usersMapper.updateIconByUserId(userId, icon, getSchoolId(userId));
                    }
                }
            } else {
                mUserIconMapper.insert(mUserIcon);
                if (!oldIcon.equals(icon)) {
                    mUserIconMapper.updateStatusByUserIdAndIcon(userId, oldIcon, 0);
                    usersMapper.updateIconByUserId(userId, icon, getSchoolId(userId));
                }
            }
        }
        //修改用户图标，删除缓存数据
        if (IdentityIdConstants.STUDENT.equals(mUser.getIdentityId())) {
            deleteRedisOfParent(userId, schoolId);
        }
        invalidCache(userId);
    }

    /**
     * 用户绑定功能，一个用户绑定到另一个用户
     */
    @Override
    public void saveBindUser(String userId, String targetUserId) {
        //修改m_login_user
        String bindUserId = loginUserMapper.getByUserId(userId).getBindUserId();
        loginUserMapper.updateBindUserId(bindUserId, targetUserId);
        //修改cas_login密码
        List<String> userIdList = loginUserMapper.queryUserIdsByBindUserId(targetUserId);
        String password = casLoginMapper.getByUserId(targetUserId).get(0).getPassword();
        casLoginMapper.updatePassword(userIdList, password);
    }

    /**
     * 获取某学校的某身份的人数
     *
     * @param schoolId   学校id
     * @param identityId 身份id
     * @return 人数
     */
    @Override
    public Integer getCountOfUser(String schoolId, String identityId) {
        return usersMapper.getCountOfUser(schoolId, identityId);
    }

    @Override
    public EmailStatusDTO emailStatusDTO(String userId) {
        //1、未绑定，2、已绑定，3、待验证
        EmailStatusDTO dto = new EmailStatusDTO();
        dto.setUserId(userId);
        if (StringUtils.isEmpty(userId)) {
            return dto;
        }
        String email = casLoginService.getLoginIdByUserIdAndLoginType(userId, UserConstants.EMAIL);
        if (StringUtils.isNotEmpty(email)) {
            dto.setEmail(email);
            dto.setStatus(2);
            return dto;
        }
        UserDTO user = getByUserId(userId);
        if (StringUtils.isEmpty(user.getEmail())) {
            dto.setStatus(1);
            return dto;
        } else {
            dto.setEmail(user.getEmail());
            dto.setStatus(3);
            return dto;
        }
    }

    /**
     * 通讯录 根据userId查询自己的联系人 web端
     *
     * @param userId     用户ID
     * @param schoolId   学校ID
     * @param identityId 角色身份
     * @return
     */
    @Override
    public ContactsGroupDTO queryContactsLeftNode(String userId, String schoolId, String identityId) {
        ContactsGroupDTO contactsGroupDTO = new ContactsGroupDTO();
        List<ContactsListGroupDTO> groupDTOLevel1 = new ArrayList<>();
        List<ContactsListGroupDTO> groupStudentLevel2 = new ArrayList<>();
        if (identityId.equals(IdentityIdConstants.SCHOOL_MANAGER)) {
            groupDTOLevel1.add(addManagerGroup(IdentityIdConstants.SCHOOL_MANAGER, schoolId));
            contactsGroupDTO.setLeftNode(groupDTOLevel1);
            //学校管理层获取通讯录

        } else if (identityId.equals(IdentityIdConstants.TEACHER)) {
            //学校教师获取通讯录

            groupStudentLevel2.add(addManagerGroup(IdentityIdConstants.TEACHER, schoolId));
            groupStudentLevel2.add(addClassGroup(userId, schoolId));
            contactsGroupDTO.setLeftNode(groupStudentLevel2);

        } else if (identityId.equals((IdentityIdConstants.STUDENT))) {
            //学校学生获取通讯录
            groupStudentLevel2.add(addParentGroup(IdentityIdConstants.STUDENT, userId, schoolId));
            groupStudentLevel2.add(addClassmateGroup(userId, schoolId));
            groupStudentLevel2.add(addTeacherGroup(IdentityIdConstants.STUDENT, userId, schoolId));
            contactsGroupDTO.setLeftNode(groupStudentLevel2);
        } else if (identityId.equals(IdentityIdConstants.PARENT)) {
            //学生家长获取通讯录
            groupStudentLevel2.add(addParentGroup(IdentityIdConstants.PARENT, userId, schoolId));
            groupStudentLevel2.add(addTeacherGroup(IdentityIdConstants.PARENT, userId, schoolId));
            contactsGroupDTO.setLeftNode(groupStudentLevel2);
        }
        return contactsGroupDTO;
    }

    @Override
    public PageResponse<ContactsTeacherDTO> queryTeacherInfoBySubjectPage(ContactsTeacherPageRequest contactsTeacherPageRequest) {
        String schoolId = contactsTeacherPageRequest.getSchoolId();
        List<MUserTeacher> mUserTeacherList = new ArrayList<>();

        //获取当前页的信息
        Page page = PageUtils.buildPage(contactsTeacherPageRequest);

        if (("MANAGE_NODE").equals(contactsTeacherPageRequest.getSubjectId())) {
            mUserTeacherList = teacherMapper.queryAllManagerPage(page, schoolId);
        } else {
            RequestTeacherLoginPage request = new RequestTeacherLoginPage();
            BeanUtils.copyProperties(contactsTeacherPageRequest, request);
            List<MTeacherLogin> teacherLoginList;
            if (("UNLOAD_NODE").equals(contactsTeacherPageRequest.getSubjectId())) {
                teacherLoginList = teacherMapper.selectTeacherContacts(request);
            } else {
                //limit为1时查询不分页信息，APP端用
                if (contactsTeacherPageRequest.getLimit() == 1) {
                    teacherLoginList = teacherMapper.selectTeacherLoginNoLimit(request);
                } else {
                    teacherLoginList = teacherMapper.selectTeacherLogin(request);
                }
            }
            for (MTeacherLogin mTeacherLogin : teacherLoginList) {
                MUserTeacher mUserTeacher = new MUserTeacher();
                mUserTeacher.setUserId(mTeacherLogin.getUserId());
                mUserTeacher.setRealName(mTeacherLogin.getRealName());
                mUserTeacher.setIcon(mTeacherLogin.getIcon());
                mUserTeacher.setSubjectName(mTeacherLogin.getSubjectName());
                mUserTeacher.setPositionName(mTeacherLogin.getPositionName());
                mUserTeacherList.add(mUserTeacher);
            }
        }
          /*
        给返回接口赋值
         */
        List<ContactsTeacherDTO> contactsTeacherDTOList = new ArrayList<>();
        for (MUserTeacher mUserTeacher : mUserTeacherList) {
            ContactsTeacherDTO contactsTeacherDTO = new ContactsTeacherDTO();
            contactsTeacherDTO.setId(mUserTeacher.getUserId());
            contactsTeacherDTO.setIcon(mUserTeacher.getIcon());
            contactsTeacherDTO.setName(mUserTeacher.getRealName());
            contactsTeacherDTO.setSubject(mUserTeacher.getSubjectName());
            contactsTeacherDTO.setDuty(mUserTeacher.getPositionName());
            contactsTeacherDTOList.add(contactsTeacherDTO);
        }

        //返回对象
        PageResponse<ContactsTeacherDTO> pageResponse = new PageResponse<>();
        PageUtils.buldPageResponse(contactsTeacherPageRequest, pageResponse);
        pageResponse.setRows(contactsTeacherDTOList);
        return pageResponse;
    }

    /**
     * 通讯录 分页查询班级学生web端
     * app端   不用分页   limit为1时查询
     *
     * @param contactsStudentPageRequest 分页信息
     * @return 分页学生
     */
    @Override
    public PageResponse<ContactsStudentsDTO> queryStudentInfoByClassPage(ContactsStudentPageRequest contactsStudentPageRequest) {
        //获取当前页的信息
        Page page = PageUtils.buildPage(contactsStudentPageRequest);
        List<MStudentContacts> mStudentContactsList = studentMapper.queryStudentInfoByClassPage(page, contactsStudentPageRequest.getClassId(), contactsStudentPageRequest.getSchoolId(), contactsStudentPageRequest.getLimit());
        List<String> userIdList = new ArrayList<>();
        List<String> userIdFamilyList = new ArrayList<>();
        for (int i = 0; i < mStudentContactsList.size(); i++) {
            userIdList.add(mStudentContactsList.get(i).getUserId());
        }
        List<MFamilyRelation> mFamilyRelationList = familyRelationMapper.queryFamilyList(userIdList);
        for (int i = 0; i < mFamilyRelationList.size(); i++) {
            userIdFamilyList.add(mFamilyRelationList.get(i).getUserId());
        }
        List<MStudentContacts> mStudentLeafList = studentMapper.queryStudentLeafInfo(userIdFamilyList, contactsStudentPageRequest.getSchoolId());
        List<ContactsStudentsDTO> contactsStudentsDTOs = new ArrayList<>();
        for (MStudentContacts mStudentContacts : mStudentContactsList) {//学生信息
            ContactsStudentsDTO contactsStudentsDTO = new ContactsStudentsDTO();
            contactsStudentsDTO.setId(mStudentContacts.getUserId());
            contactsStudentsDTO.setName(mStudentContacts.getRealName());
            contactsStudentsDTO.setIcon(mStudentContacts.getIcon());
            List<ContactsStudentsLeafDTO> contactsStudentsLeafDTOList = new ArrayList<>();
            for (MStudentContacts mStudentContact : mStudentLeafList) {//家长信息
                for (MFamilyRelation mFamilyRelation : mFamilyRelationList) {
                    if (mStudentContacts.getUserId().equals(mFamilyRelation.getTargetUserId()) && mStudentContact.getUserId().equals(mFamilyRelation.getUserId())) {
                        ContactsStudentsLeafDTO contactsStudentsLeafDTO = new ContactsStudentsLeafDTO();
                        contactsStudentsLeafDTO.setId(mStudentContact.getUserId());
                        contactsStudentsLeafDTO.setName(mStudentContact.getRealName());
                        contactsStudentsLeafDTO.setIcon(mStudentContact.getIcon());
                        contactsStudentsLeafDTO.setRelation(mStudentContact.getMemberName());
                        contactsStudentsLeafDTOList.add(contactsStudentsLeafDTO);
                    }
                }
            }
            contactsStudentsDTO.setLeaf(contactsStudentsLeafDTOList);
            contactsStudentsDTOs.add(contactsStudentsDTO);
        }
        //返回对象
        PageResponse<ContactsStudentsDTO> pageResponse = new PageResponse<ContactsStudentsDTO>();
        PageUtils.buldPageResponse(contactsStudentPageRequest, pageResponse);
        pageResponse.setRows(contactsStudentsDTOs);
        return pageResponse;
    }

    /**
     * 学生查询 家人 、同学、老师信息
     * app端   不用分页   limit为1时查询
     */
    @Override
    public PageResponse<ContactsStudentsLeafDTO> queryContactsByStudentPage(ContactsByStudentUserIdPageRequest request) {
        //获取当前页的信息
        Page page = PageUtils.buildPage(request);
        List<ContactsStudentsLeafDTO> contactsStudentsLeafDTOList = new ArrayList<>();
        if (("STUDENT_0").equals(request.getRequestFlag())) {
            //家人
            List<DFamilyMembers> dFamilyMembersList = familyRelationMapper.queryFamilyByStudentPage(page, request.getUserId(), request.getSchoolId(), request.getLimit());
            for (DFamilyMembers dFamilyMembers : dFamilyMembersList) {
                ContactsStudentsLeafDTO contactsStudentsLeafDTO = new ContactsStudentsLeafDTO();
                contactsStudentsLeafDTO.setId(dFamilyMembers.getMemberUserId());
                contactsStudentsLeafDTO.setIcon(dFamilyMembers.getMemberIcon());
                contactsStudentsLeafDTO.setName(dFamilyMembers.getMemberRealName());
                contactsStudentsLeafDTO.setRelation(dFamilyMembers.getMemberName());
                contactsStudentsLeafDTOList.add(contactsStudentsLeafDTO);
            }
        } else if (("STUDENT_1").equals(request.getRequestFlag())) {
            //同学
            MStudent mStudent = studentMapper.selectByPrimaryKey(request.getUserId(), request.getSchoolId());
            if (mStudent != null && mStudent.getClassId() != null && !"null".equals(mStudent.getClassId())) {
                List<MStudentContacts> mStudentContactsList = studentMapper.queryStudentInfoByClassmatePage(page, mStudent.getClassId(), mStudent.getSchoolId(), mStudent.getUserId(), request.getLimit());
                for (MStudentContacts mStudentContacts : mStudentContactsList) {
                    ContactsStudentsLeafDTO contactsStudentsLeafDTO = new ContactsStudentsLeafDTO();
                    contactsStudentsLeafDTO.setId(mStudentContacts.getUserId());
                    contactsStudentsLeafDTO.setName(mStudentContacts.getRealName());
                    contactsStudentsLeafDTO.setIcon(mStudentContacts.getIcon());
                    contactsStudentsLeafDTOList.add(contactsStudentsLeafDTO);
                }
            }

        } else if (("STUDENT_2").equals(request.getRequestFlag())) {
            //老师
            MStudent mStudent = studentMapper.selectByPrimaryKey(request.getUserId(), request.getSchoolId());
            if (mStudent != null && mStudent.getClassId() != null && !"null".equals(mStudent.getClassId())) {
                List<MTeacherLogin> mTeacherLoginList = teacherMapper.queryContactsTeacherPage(page, mStudent.getSchoolId(), mStudent.getClassId(), request.getLimit());
                for (MTeacherLogin mTeacherLogin : mTeacherLoginList) {
                    ContactsStudentsLeafDTO contactsStudentsLeafDTO = new ContactsStudentsLeafDTO();
                    contactsStudentsLeafDTO.setId(mTeacherLogin.getUserId());
                    contactsStudentsLeafDTO.setName(mTeacherLogin.getRealName());
                    contactsStudentsLeafDTO.setRelation(mTeacherLogin.getPositionName());
                    contactsStudentsLeafDTO.setIcon(mTeacherLogin.getIcon());
                    contactsStudentsLeafDTO.setSubject(mTeacherLogin.getSubjectName());
                    contactsStudentsLeafDTOList.add(contactsStudentsLeafDTO);
                }
            }
        } else {
            return null;
        }
        //返回对象
        PageResponse<ContactsStudentsLeafDTO> pageResponse = new PageResponse<ContactsStudentsLeafDTO>();
        PageUtils.buldPageResponse(request, pageResponse);
        pageResponse.setRows(contactsStudentsLeafDTOList);
        return pageResponse;
    }

    /**
     * 家长查询 孩子、老师信息
     * app端   不用分页   limit为1时查询
     *
     * @param request
     * @return
     */
    @Override
    public PageResponse<ContactsStudentsDTO> queryContactsByParentPage(ContactsByStudentUserIdPageRequest request) {
        //获取当前页的信息
        Page page = PageUtils.buildPage(request);
        List<ContactsStudentsDTO> contactsStudentsDTOList = new ArrayList<>();
        if (("PARENT_0").equals(request.getRequestFlag())) {
            //家人
            List<MStudentContacts> mStudentContactsList = studentMapper.queryStudentInfoByParent(page, request.getUserId(), request.getLimit());
            List<String> targetUserIdList = new ArrayList<>();
            List<String> userIdFamilyList = new ArrayList<>();
            for (int i = 0; i < mStudentContactsList.size(); i++) {
                targetUserIdList.add(mStudentContactsList.get(i).getUserId());
            }
            List<MFamilyRelation> mFamilyRelationList = familyRelationMapper.queryFamilyList(targetUserIdList);
            for (int i = 0; i < mFamilyRelationList.size(); i++) {
                userIdFamilyList.add(mFamilyRelationList.get(i).getUserId());
            }
            List<MStudentContacts> mStudentLeafList = studentMapper.queryStudentLeafInfo(userIdFamilyList, request.getSchoolId());
            for (MStudentContacts mStudentContacts : mStudentContactsList) {//学生信息
                ContactsStudentsDTO contactsStudentsDTO = new ContactsStudentsDTO();
                contactsStudentsDTO.setId(mStudentContacts.getUserId());
                contactsStudentsDTO.setName(mStudentContacts.getRealName());
                contactsStudentsDTO.setIcon(mStudentContacts.getIcon());
                List<ContactsStudentsLeafDTO> contactsStudentsLeafDTOList = new ArrayList<>();
                for (MStudentContacts mStudentContact : mStudentLeafList) {//家长信息
                    for (MFamilyRelation mFamilyRelation : mFamilyRelationList) {
                        if (mStudentContacts.getUserId().equals(mFamilyRelation.getTargetUserId()) && mStudentContact.getUserId().equals(mFamilyRelation.getUserId())) {
                            ContactsStudentsLeafDTO contactsStudentsLeafDTO = new ContactsStudentsLeafDTO();
                            contactsStudentsLeafDTO.setId(mStudentContact.getUserId());
                            contactsStudentsLeafDTO.setName(mStudentContact.getRealName());
                            contactsStudentsLeafDTO.setIcon(mStudentContact.getIcon());
                            contactsStudentsLeafDTO.setRelation(mStudentContact.getMemberName());
                            contactsStudentsLeafDTOList.add(contactsStudentsLeafDTO);
                        }
                    }
                }
                contactsStudentsDTO.setLeaf(contactsStudentsLeafDTOList);
                contactsStudentsDTOList.add(contactsStudentsDTO);
            }

        } else if (("PARENT_1").equals(request.getRequestFlag())) {
            //老师
            List<MStudentContacts> mStudentContactsList = studentMapper.queryStudentInfoByParent(page, request.getUserId(), request.getLimit());
            String classId = "";
            List<String> classIdList = new ArrayList<>();
            for (int i = 0; i < mStudentContactsList.size(); i++) {
                if (mStudentContactsList.get(i).getClassId() != null && !"null".equals(mStudentContactsList.get(i).getClassId())) {
                    classIdList.add(mStudentContactsList.get(i).getClassId());
                }
            }
            if (classIdList.size() == 0) {
                PageResponse<ContactsStudentsDTO> pageResponse = new PageResponse<ContactsStudentsDTO>();
                PageUtils.buldPageResponse(request, pageResponse);
                pageResponse.setRows(new ArrayList<ContactsStudentsDTO>(0));
                return pageResponse;
            }
            List<MTeacherLogin> mTeacherLoginList = teacherMapper.queryContactsTeacher(classIdList);

            for (MStudentContacts mStudentContacts : mStudentContactsList) {
                ContactsStudentsDTO contactsStudentsDTO = new ContactsStudentsDTO();
                contactsStudentsDTO.setId(mStudentContacts.getUserId());
                contactsStudentsDTO.setIcon(mStudentContacts.getIcon());
                contactsStudentsDTO.setName(mStudentContacts.getRealName());

                List<ContactsStudentsLeafDTO> contactsStudentsLeafDTOs = new ArrayList<>();
                for (MTeacherLogin mTeacherLogin : mTeacherLoginList) {
                    if (mStudentContacts.getClassId() != null) {
                        if (mStudentContacts.getClassId().equals(mTeacherLogin.getClassId())) {
                            ContactsStudentsLeafDTO contactsStudentsLeafDTO = new ContactsStudentsLeafDTO();
                            contactsStudentsLeafDTO.setId(mTeacherLogin.getUserId());
                            contactsStudentsLeafDTO.setName(mTeacherLogin.getRealName());
                            contactsStudentsLeafDTO.setIcon(mTeacherLogin.getIcon());
                            contactsStudentsLeafDTO.setRelation(mTeacherLogin.getPositionName());
                            contactsStudentsLeafDTO.setSubject(mTeacherLogin.getSubjectName());
                            contactsStudentsLeafDTOs.add(contactsStudentsLeafDTO);
                        }
                    }
                }
                contactsStudentsDTO.setLeaf(contactsStudentsLeafDTOs);
                contactsStudentsDTOList.add(contactsStudentsDTO);
            }

        } else {
            return null;
        }
        //返回对象
        PageResponse<ContactsStudentsDTO> pageResponse = new PageResponse<>();
        PageUtils.buldPageResponse(request, pageResponse);
        pageResponse.setRows(contactsStudentsDTOList);
        return pageResponse;
    }

    /**
     * 通讯录 增加管理层
     *
     * @param identityId 角色
     * @param schoolId   学校ID
     */
    private ContactsListGroupDTO addManagerGroup(String identityId, String schoolId) {
        //学校管理层获取通讯录
        String requestFlag = "";
        if (identityId.equals(IdentityIdConstants.SCHOOL_MANAGER)) {
            requestFlag = "SCHOOL_MANAGER_0";
        } else if (identityId.equals(IdentityIdConstants.TEACHER)) {
            requestFlag = "TEACHER_0";
        }
        ContactsListGroupDTO groupDTOLevel1 = new ContactsListGroupDTO();
        groupDTOLevel1.setGroupId("");
        groupDTOLevel1.setGroupName("同事");
        groupDTOLevel1.setMemberAmount(0);
        List<ContactsListGroupDTO> groupLevel2 = new ArrayList<>();
        ContactsListGroupDTO groupDTOLevel2 = new ContactsListGroupDTO();
        groupDTOLevel2.setMemberAmount(0);
        groupDTOLevel2.setGroupId("MANAGE_NODE");
        groupDTOLevel2.setRequestFlag(requestFlag);
        groupDTOLevel2.setGroupName("管理层");
        Long isManagerCount = teacherMapper.isManagerCount(schoolId);
        groupDTOLevel2.setMemberAmount(isManagerCount.intValue());
        groupLevel2.add(groupDTOLevel2);
        //学校对应的科目
        List<CtBook> ctBookList = ctBookMapper.querySubjectGroupByList(schoolId);
        //科目对应老师的数量
        List<MTeacherBookCount> mTeacherBookCountList = teacherMapper.queryUserBookList(schoolId);
        Map map = new HashMap();
        //Map存放科目对应老师的数量
        ContactsListGroupDTO groupDTOOtherLevel2 = new ContactsListGroupDTO();
        for (int i = 0; i < mTeacherBookCountList.size(); i++) {
            if (mTeacherBookCountList.get(i).getSubjectId() != null) {
                //有科目的放入Map
                map.put(mTeacherBookCountList.get(i).getSubjectId(), mTeacherBookCountList.get(i).getUserNumber());
            }
        }
        //groupDTOLevel2 传入科目对应的老师数量（map中的数据）
        for (CtBook ctBook : ctBookList) {
            if (map.get(ctBook.getSubjectId()) != null && !map.get(ctBook.getSubjectId()).equals(0)) {
                ContactsListGroupDTO groupDTOSubjectLevel2 = new ContactsListGroupDTO();
                groupDTOSubjectLevel2.setGroupName(ctBook.getSubjectName());
                groupDTOSubjectLevel2.setGroupId(ctBook.getSubjectId());
                groupDTOSubjectLevel2.setRequestFlag(requestFlag);
                groupDTOSubjectLevel2.setMemberAmount(Integer.parseInt(map.get(ctBook.getSubjectId()).toString()));
                groupLevel2.add(groupDTOSubjectLevel2);
            }
        }
        //把不是管理层和没有科目的放入其他
        Long nullSubjectManager = teacherMapper.nullSubjectManager(schoolId);
        if (nullSubjectManager != 0) {
            groupDTOOtherLevel2.setGroupName("其他");
            groupDTOOtherLevel2.setGroupId("UNLOAD_NODE");
            groupDTOOtherLevel2.setRequestFlag(requestFlag);
            groupDTOOtherLevel2.setMemberAmount(Integer.parseInt(nullSubjectManager.toString()));
            groupLevel2.add(groupDTOOtherLevel2);
        }
        groupDTOLevel1.setChildren(groupLevel2);
        return groupDTOLevel1;
    }

    /**
     * 通讯录 增加班级
     *
     * @param schoolId
     * @return
     */
    private ContactsListGroupDTO addClassGroup(String userId, String schoolId) {
        //学校管理层获取通讯录
        ContactsListGroupDTO groupDTOLevel1 = new ContactsListGroupDTO();
        groupDTOLevel1.setGroupId("");
        groupDTOLevel1.setGroupName("学生");
        groupDTOLevel1.setMemberAmount(0);
        List<ContactsListGroupDTO> groupLevel2 = new ArrayList<>();
        List<ClassInfoDTO> classInfoDTOList = teacherService.queryTeacherClass(userId);
        List<ClassInfoDTO> tempList = classInfoDTOList;
        Map map = new HashMap();
        for (int i = 0; i < classInfoDTOList.size(); i++) {
            map.put(classInfoDTOList.get(i).getYear(), classInfoDTOList.get(i).getYear());
        }
        for (int i = 0; i < map.size(); ) {

            for (int j = 0; j < tempList.size(); j++) {
                if (map.get(tempList.get(j).getYear()) != null) {
                    ContactsListGroupDTO groupDTOClassLevel2 = new ContactsListGroupDTO();
                    List<ContactsListGroupDTO> groupLevel3 = new ArrayList<>();
                    groupDTOClassLevel2.setGroupName(getGrade(tempList.get(j).getYear()));
                    for (int k = 0; k < classInfoDTOList.size(); k++) {
                        if (classInfoDTOList.get(k).getYear() == tempList.get(j).getYear() && map.get(tempList.get(j).getYear()) != null) {

                            ContactsListGroupDTO groupDTOClassLevel3 = new ContactsListGroupDTO();
                            groupDTOClassLevel3.setGroupName(classInfoDTOList.get(k).getClassName());
                            groupDTOClassLevel3.setRequestFlag("TEACHER_1");
                            groupDTOClassLevel3.setGroupId(classInfoDTOList.get(k).getClassId());
                            groupDTOClassLevel3.setMemberAmount(classInfoDTOList.get(k).getStudentCount());
                            groupLevel3.add(groupDTOClassLevel3);
                            groupDTOClassLevel2.setChildren(groupLevel3);
                        }
                    }
                    groupLevel2.add(groupDTOClassLevel2);

                }
                map.remove(tempList.get(j).getYear());
            }
        }

        groupDTOLevel1.setChildren(groupLevel2);
        return groupDTOLevel1;
    }

    /**
     * 通讯录 家长
     *
     * @param userId   用户ID
     * @param schoolId 学校ID
     */
    private ContactsListGroupDTO addParentGroup(String identityId, String userId, String schoolId) {
        //学校管理层获取通讯录
        String requestFlag = "";
        Integer memberAmount = 0;
        List<DFamilyMembers> dFamilyMembersList;
        if (identityId.equals(IdentityIdConstants.STUDENT)) {
            //学生获取家长数量
            requestFlag = "STUDENT_0";
            dFamilyMembersList = familyRelationMapper.getByTargetUserId(userId, schoolId);
            memberAmount = dFamilyMembersList.size();
        } else if (identityId.equals(IdentityIdConstants.PARENT)) {
            //家长获取学生数量
            requestFlag = "PARENT_0";
            memberAmount = queryChildsByParentId(userId).size();
        }


        ContactsListGroupDTO groupDTOLevel1 = new ContactsListGroupDTO();
        groupDTOLevel1.setGroupId(userId);
        groupDTOLevel1.setGroupName("家人");
        groupDTOLevel1.setMemberAmount(memberAmount);
        groupDTOLevel1.setRequestFlag(requestFlag);
        return groupDTOLevel1;
    }

    /**
     * 通讯录 同学
     *
     * @param userId   用户ID
     * @param schoolId 学校ID
     */
    private ContactsListGroupDTO addClassmateGroup(String userId, String schoolId) {
        //学校管理层获取通讯录
        String requestFlag = "STUDENT_1";
        MClassInfo mClassInfo = studentMapper.getStudentClassByContacts(userId, schoolId);
        ContactsListGroupDTO groupDTOLevel1 = new ContactsListGroupDTO();
        groupDTOLevel1.setGroupId(userId);
        groupDTOLevel1.setGroupName("同学");
        groupDTOLevel1.setMemberAmount(mClassInfo == null ? 0 : mClassInfo.getStudentCount());
        groupDTOLevel1.setRequestFlag(requestFlag);
        return groupDTOLevel1;
    }

    /**
     * 通讯录 老师
     *
     * @param userId   用户ID
     * @param schoolId 学校ID
     */
    private ContactsListGroupDTO addTeacherGroup(String identityId, String userId, String schoolId) {
        //学校管理层获取通讯录
        String requestFlag = "";
        Integer memberAmount = new Integer(0);
        if (identityId.equals(IdentityIdConstants.STUDENT)) {
            //学生获取老师数量
            requestFlag = "STUDENT_2";
            MStudentInfo studentInfo = studentMapper.getStudentInfo(userId, schoolId);
            if (studentInfo != null) {
                Page page = new Page();
                List<MTeacherLogin> mTeacherLoginList = teacherMapper.queryContactsTeacherPage(page, schoolId, studentInfo.getClassId(), 1);
                memberAmount = mTeacherLoginList.size();
            }
        } else if (identityId.equals(IdentityIdConstants.PARENT)) {
            //家长获取学生数量
            requestFlag = "PARENT_1";
            memberAmount = queryChildsByParentId(userId).size();
        }
        ContactsListGroupDTO groupDTOLevel1 = new ContactsListGroupDTO();
        groupDTOLevel1.setGroupId(userId);
        groupDTOLevel1.setGroupName("老师");
        groupDTOLevel1.setMemberAmount(memberAmount);
        groupDTOLevel1.setRequestFlag(requestFlag);
        return groupDTOLevel1;
    }

    /**
     * 通讯录 根据userId查询自己的联系人 app端
     *
     * @param userId     用户ID
     * @param schoolId   学校ID
     * @param identityId 角色身份
     */
    @Override
    public ContactsGroupDTO queryContactsLeftNodeByApp(String userId, String schoolId, String identityId) {
        ContactsGroupDTO contactsGroupDTO = new ContactsGroupDTO();
        List<ContactsListGroupDTO> groupDTOLevel1 = new ArrayList<>();
        List<ContactsListGroupDTO> groupStudentLevel2 = new ArrayList<>();
        if (identityId.equals(IdentityIdConstants.SCHOOL_MANAGER)) {
            ContactsListGroupDTO groupDTOLevel = new ContactsListGroupDTO();
            groupDTOLevel.setGroupId("");
            groupDTOLevel.setGroupName("同事");
            groupDTOLevel.setRequestFlag("SCHOOL_MANAGER_0");
            contactsGroupDTO.setLeftNode(groupDTOLevel1);
            //学校管理层获取通讯录

        } else if (identityId.equals(IdentityIdConstants.TEACHER)) {
            //学校教师获取通讯录
            ContactsListGroupDTO groupDTOLevel = new ContactsListGroupDTO();
            groupDTOLevel.setGroupId("");
            groupDTOLevel.setGroupName("同事");
            groupDTOLevel.setRequestFlag("TEACHER_0");
            groupStudentLevel2.add(groupDTOLevel);

            List<ClassInfoDTO> classInfoDTOList = teacherService.queryTeacherClass(userId);
            List<ClassInfoDTO> tempList = classInfoDTOList;
            Map map = new HashMap();
            for (int i = 0; i < classInfoDTOList.size(); i++) {
                map.put(classInfoDTOList.get(i).getYear(), classInfoDTOList.get(i).getYear());
            }
//            for (int i = 0; i < map.size(); ) {

            for (int j = 0; j < tempList.size(); j++) {
                if (tempList.get(j).getStudentCount() > 0) {
                    ContactsListGroupDTO groupDTOClassLevel2 = new ContactsListGroupDTO();
                    groupDTOClassLevel2.setGroupName(tempList.get(j).getClassName());
                    groupDTOClassLevel2.setRequestFlag("TEACHER_1");
                    groupDTOClassLevel2.setGroupId(tempList.get(j).getClassId());
                    groupStudentLevel2.add(groupDTOClassLevel2);

                }
//                    map.remove(tempList.get(j).getYear());
            }
//            }
            contactsGroupDTO.setLeftNode(groupStudentLevel2);

        } else if (identityId.equals((IdentityIdConstants.STUDENT))) {
            //学校学生获取通讯录
            groupStudentLevel2.add(addParentGroup(IdentityIdConstants.STUDENT, userId, schoolId));
            groupStudentLevel2.add(addClassmateGroup(userId, schoolId));
            groupStudentLevel2.add(addTeacherGroup(IdentityIdConstants.STUDENT, userId, schoolId));
            contactsGroupDTO.setLeftNode(groupStudentLevel2);
        } else if (identityId.equals(IdentityIdConstants.PARENT)) {
            //学生家长获取通讯录
            groupStudentLevel2.add(addParentGroup(IdentityIdConstants.PARENT, userId, schoolId));
            groupStudentLevel2.add(addTeacherGroup(IdentityIdConstants.PARENT, userId, schoolId));
            contactsGroupDTO.setLeftNode(groupStudentLevel2);
        }
        return contactsGroupDTO;
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
     * 从redis中获取前端css、js版本version值
     */
    @Override
    public String getCssJsVersion() {
        String version = signRedisTemplate.opsForValue().get(UserConstants.CSS_JS_VERSION_KEY);
        if (StringUtils.isEmpty(version)) {
            logger.debug("不能从redis库中获取到version值");
            version = "0";
        }
        return version;
    }

    @Override
    public List<UserDTO> queryStudentTimeOut(String userId, String schoolId, String classId, Integer day) {
        List<MUsers> userList = usersMapper.queryStudentTimeOut(userId, schoolId, classId, day);
        List<UserDTO> userDTOList = new ArrayList<>();
        for (MUsers users : userList) {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(users, userDTO);
            userDTOList.add(userDTO);
        }
        return userDTOList;
    }

    /**
     * 添加用户，学生或老师
     *
     * @param userAddDTO 必要的一些数据
     * @return 返回学乐号
     */
    @Override
    public String saveOneUser(UserAddDTO userAddDTO) {
        //检查数据是否合理
        checkData(userAddDTO);
        MUsers mUsers = new MUsers();
        mUsers.setSex(userAddDTO.getSex());
        mUsers.setSchoolId(userAddDTO.getSchoolId());
        mUsers.setRealName(userAddDTO.getRealName());
        String userId = null;
        if (userAddDTO.getType() == 1) {//老师
            //保存老师用户
            userId = saveUserOfTeacher(mUsers);
        } else if (userAddDTO.getType() == 2) {//学生
            //保存学生用户
            userId = saveUserOfStudent(mUsers, userAddDTO.getGradeNum(), userAddDTO.getClassNum());
        }
        return userId;
    }

    /**
     * 保存用户设备信息
     *
     * @param mobileInfoDTO 必要的一些数据
     */
    @Override
    public void saveMobileDevice(MobileInfoDTO mobileInfoDTO) {
        if (StringUtils.isEmpty(mobileInfoDTO.getUserId())) {
            throw new MemberException("用户ID不能为空");
        }
        if (StringUtils.isEmpty(mobileInfoDTO.getSchoolId())) {
            throw new MemberException("用户学校不能为空");
        }
        if (mobileInfoDTO.getMobileType() == null) {
            throw new MemberException("设备类型不能为空");
        }
        if (StringUtils.isNotEmpty(mobileInfoDTO.getDescription()) && mobileInfoDTO.getDescription().length() > 200) {
            throw new MemberException("设备描述不能超过200字符");
        }
        MMobileInfo mobileInfo = new MMobileInfo();
        mobileInfo.setId(UUID.randomUUID().toString().replaceAll("\\-", ""));
        mobileInfo.setUserId(mobileInfoDTO.getUserId());
        mobileInfo.setSchoolId(mobileInfoDTO.getSchoolId());
        mobileInfo.setMobileType(mobileInfoDTO.getMobileType());
        mobileInfo.setDescription(mobileInfoDTO.getDescription());
        mobileInfo.setCreateTime(new Date());
        mobileInfoMapper.insert(mobileInfo);
    }

    @Override
    public List<String> getUserIdsByIdentityId(String schoolId, String identityId) {
        if (StringUtils.isNotEmpty(schoolId)) {
            List<String> users = usersMapper.getUserIdsByIdentityId(schoolId, identityId);
            return users;
        } else {
            logger.debug("schoolId为空");
            throw new MemberException("schoolId为空");
        }
    }

    @Override
    public Integer getUserValidateStatus(String userId) {
        UserDTO userDTO = getByUserId(userId);
        if (userDTO == null) {
            return 2;
        }
        if (userDTO.getStatus() == 0) {
            return 3;
        }
        return 1;
    }

    /*
     * 添加老师用户
     * @param mUsers 用户表的数据
     * @return 返回学乐号
     */
    private String saveUserOfTeacher(MUsers mUsers) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(mUsers, userDTO);
        userDTO.setIdentityId(IdentityIdConstants.TEACHER);
        userDTO.setIdentityDescription(IdentityIdConstants.TEACHER_NAME);
        MSchool school = schoolMapper.selectByPrimaryKey(mUsers.getSchoolId());
        if (school == null) {
            logger.debug("{}学校不存在", mUsers.getSchoolId());
            throw new MemberException("学校不存在");
        }
        userDTO.setArea(school.getArea());
        //保存m_users、cas_login、m_user_role、m_login_user
        UserDTO users = saveUser(userDTO);
        MTeacher teacher = new MTeacher();
        teacher.setSchoolId(school.getId());
        teacher.setSchoolName(school.getName());
        teacher.setPositionId(PositionConstants.TEACHER);
        teacher.setPositionName(PositionConstants.TEACHER_NAME);
        teacher.setUserId(users.getUserId());
        teacher.setIsManager(0);
        //保存m_teacher
        teacherMapper.insert(teacher);
        return users.getUserId();
    }

    /*
     * 添加学生用户
     * @param mUsers用户表的数据
     * @param gradeNum     年级号
     * @param classNum     班级号
     * @return 学乐号
     */
    private String saveUserOfStudent(MUsers mUsers, int gradeNum, int classNum) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(mUsers, userDTO);
        userDTO.setIdentityId(IdentityIdConstants.STUDENT);
        userDTO.setIdentityDescription(IdentityIdConstants.STUDENT_NAME);
        MSchool school = schoolMapper.selectByPrimaryKey(mUsers.getSchoolId());
        if (school == null) {
            logger.debug("{}学校不存在", mUsers.getSchoolId());
            throw new MemberException("学校不存在");
        }
        userDTO.setArea(school.getArea());
        //保存m_users、cas_login、m_user_role、m_login_user
        UserDTO users = saveUser(userDTO);
        //判断是否有某班级，如果没有则创建班级
        MClass mclass = judgeClassIsExist(gradeNum, classNum, users.getUserId(), school);
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setUserId(users.getUserId());
        studentDTO.setSchoolId(school.getId());
        studentDTO.setSchoolName(school.getName());
        studentDTO.setClassName(mclass.getName());
        studentDTO.setClassId(mclass.getClassId());
        studentDTO.setClassAliasName(mclass.getAliasName());
        studentDTO.setJoinClass(new Date());
        MStudent mStudent = new MStudent();
        BeanUtils.copyProperties(studentDTO, mStudent);
        //保存m_student
        studentMapper.insert(mStudent);
        return users.getUserId();
    }

    /*
     * 根据年级号、班级号判断班级是否存在，不存在则创建班级
     * @param gradeNum 年级号
     * @param classNum 班级号
     * @param userId   创建班级的用户id
     * @param school   学校信息
     * @return 班级信息
     */
    private MClass judgeClassIsExist(int gradeNum, int classNum, String userId, MSchool school) {
        //根据年级号获取年份
        int year = ClassNameUtil.getYear(gradeNum);
        //根据年份和班级号查询班级信息
        MClass mClass = classMapper.getClassByYearAndName(year, classNum, school.getId());
        if (mClass == null) {//不存在
            //创建班级
            mClass = saveClass(school, year, classNum, userId, new HashMap<Integer, String>());
        }
        return mClass;
    }

    //检查数据是否合理
    private void checkData(UserAddDTO userAddDTO) {
        if (userAddDTO.getSex() != null) {
            if (userAddDTO.getSex() != 0 && userAddDTO.getSex() != 1) {
                throw new MemberException("性别设置错误");
            }
        }
        if (StringUtils.isEmpty(userAddDTO.getRealName())) {
            throw new MemberException("用户姓名不能为空");
        } else if (userAddDTO.getRealName().length() > 10) {
            throw new MemberException("用户姓名长度不能超过10个字符");
        }
        if (StringUtils.isEmpty(userAddDTO.getSchoolId())) {
            throw new MemberException("学校不能为空");
        }
        if (userAddDTO.getType() == 2) {//学生
            if (!Integer.toString(userAddDTO.getGradeNum()).matches("\\d{1,2}")) {
                throw new MemberException("年级设置错误");
            } else {
                int grade = userAddDTO.getGradeNum();
                if (grade < 1 || grade > 12) {
                    throw new MemberException("年级设置错误");
                }
            }
            if (!Integer.toString(userAddDTO.getClassNum()).matches("\\d{1,4}")) {
                throw new MemberException("班级设置错误");
            } else {
                int classNum = userAddDTO.getClassNum();
                if (classNum < 1 || classNum > 9999) {
                    throw new MemberException("班级设置错误");
                }
            }
        }
        if (userAddDTO.getType() == null || (userAddDTO.getType() != 1 && userAddDTO.getType() != 2)) {
            throw new MemberException("用户类型设置错误");
        }
    }
}
