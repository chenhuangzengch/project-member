package net.xuele.member.service;


import net.xuele.common.ajax.AjaxResponse;
import net.xuele.common.dto.RelativeDTO;
import net.xuele.common.page.PageResponse;
import net.xuele.member.dto.*;
import net.xuele.member.dto.page.ContactsByStudentUserIdPageRequest;
import net.xuele.member.dto.page.ContactsStudentPageRequest;
import net.xuele.member.dto.page.ContactsTeacherPageRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZhengTao
 */
public interface UserService {

    /**
     * 获取用户的学校id,先从缓存中取，取不到到数据库取,数据库取到后存到缓存，以确保第二次缓存中能取到
     *
     * @param userId 会员号
     * @return 获取学校Id，已
     */
    String getSchoolId(String userId);

    /**
     * 通过用户id查找用户数据.
     *
     * @param userId 用户id
     * @return {@link UserDTO} or {@code null}
     */
    UserDTO getByUserId(String userId);

    /**
     * 通过用户id查找用户数据.
     *
     * @param userId 用户id
     * @return {@link UserDTO} or {@code null}
     */
    UserProfileDTO getProfileByUserId(String userId);

    /**
     * 通过用户id查找用户所有角色.
     *
     * @param userId 用户id
     * @return {@link List <RoleDTO>}
     */
    List<RoleDTO> queryRoleByUserId(String userId);

    /**
     * 根据用户ID列表获取该用户列表信息
     *
     * @param userIds  用户ID列表，用","隔开
     * @param schoolId 学校id 如果用户不是同一个学校，值为""
     * @return 用户信息
     */
    List<UserDTO> queryUsersByUserIdList(String userIds, String schoolId);

    /**
     * 根据用户id列表获取该用户列表信息
     *
     * @param userIdList 用户id列表
     * @param schoolId   学校id 如果用户不是同一个学校，值为""
     * @return 用户信息
     */
    List<UserDTO> getByUserIds(ArrayList<String> userIdList, String schoolId);

    /**
     * APP接口：根据用户ID列表获取该用户列表信息
     *
     * @param userIds 用户ID列表
     * @return 用户信息
     */
    List<StudentManagerDTO> queryStudentsByUserIdList(ArrayList<String> userIds);

    void updateStatus(Integer status, String userIds);

    /**
     * APP接口、WEB平台：重置密码
     *
     * @param loginId  任意登录Id
     * @param password 改的密码
     */
    void updatePassword(String loginId, String password);

    /**
     * 修改多个登录号的密码
     *
     * @param loginId  多个登录号，用“,”隔开
     * @param password 修改后的密码
     */
    void updatePasswordList(String loginId, String password);

    /**
     * 重置密码，通过邮件或手机，并在重置密码后，删除缓存中的code
     *
     * @param loginId   loginId 登录id（有三种，学乐号、手机、邮箱）
     * @param password  密码
     * @param type      登录类型 （'mobile'、'email'）
     * @param typeValue 值（手机号码、邮箱地址）
     * @param checkCode 接收的校验码
     * @return true or false
     */
    boolean updateResetPasswordByMobileOrEmail(String loginId, String password, String type, String typeValue, String checkCode);

    /**
     * 初始化密码
     *
     * @param userId          用户Id
     * @param password        改的密码
     * @param statusAfterInit 初始化后的状态
     */
    void updatePasswordForInit(String userId, String password, Integer statusAfterInit);


    /**
     * 新增用户，并新增cas_login
     *
     * @param userDTO 填充好的数据
     * @return {@link UserDTO}
     */
    UserDTO saveUser(UserDTO userDTO);

    /**
     * APP接口：根据用户ID获取该用户相关角色信息
     *
     * @param loginId 用户登录id
     * @return 用户信息
     */
    List<RoleInfoDTO> queryRoleInfo(String loginId);

    /**
     * 通知接口用
     * 根据用户ID(老师或者学生)获取所在学校的学校ID
     *
     * @param userId 用户id
     * @return schoolId 学校id
     */
    String getSchoolIdByUserId(String userId);

    /**
     * 通知接口用
     * APP接口、WEB平台：根据学校ID获取学校的管理员和校长
     *
     * @param schoolId 学校id
     * @return List<SchoolManagerDTO> 获取学校的管理员和校长
     */
    List<SchoolManagerDTO> querySchoolManagerAndPrincipal(String schoolId);

    /**
     * APP接口：根据家长名称获取孩子信息
     * ∞
     *
     * @param userId 家长ID
     * @return 孩子信息 {@link List<FamilyRelationDTO>}
     */
    List<FamilyRelationDTO> queryChildsByParentId(String userId);


    /**
     * 导入老师和学生数据
     *
     * @param lists    数据
     * @param schoolId 学校ID
     * @param userId   用户ID
     */
    ExcelUploadInfoDTO saveExcelUser(ArrayList<ArrayList<Object>> lists, String type, String schoolId, String userId) throws IllegalAccessException;

    /**
     * APP接口：验证用户名和密码是否正确,正确则插入日志
     *
     * @param loginId  用户ID
     * @param password 密码
     * @param type     类型 0：PC端；1：IOS端；2：Android端
     * @return 用户信息
     */
    UserDTO saveAndCheckLogin(String loginId, String password, Integer type);

    /**
     * APP接口：验证用户名和密码是否正确
     *
     * @param loginId  用户ID
     * @param password 密码
     * @return 用户信息
     */
    UserDTO checkPassword(String loginId, String password);

    /**
     * APP接口：检查用户名是否存在
     *
     * @param loginId 用户ID
     * @return true：存在  false不存在
     */
    boolean checkUserIdExist(String loginId);

    /**
     * modify by wuxh 0817 用来保证事务
     * sendActiveCodeEmail --> updateSendActiveCodeEmail
     *
     * @param email    邮箱
     * @param userId   用户id
     * @param userName 用户名
     * @return {@link AjaxResponse}
     */
    void updateSendActiveCodeEmail(String email, String userId, String userName);

    /**
     * WEB接口:安全设置中，绑定个人手机号或重新绑定手机号
     *
     * @param userId 用户id
     * @param mobile 手机号
     * @param code   验证码
     */
    void updateBindMobile(String userId, String mobile, String code);

    /**
     * WEB接口:安全设置，绑定个人邮箱或重新绑定邮箱
     *
     * @param userId 用户id
     * @param email  邮箱
     */
    void updateEmail(String userId, String email);

    /**
     * 生成excel信息
     *
     * @param uselessList 需要生成的数据
     * @return 信息
     */
    ExcelInfo saveUselessData(ArrayList<ExcelStateDTO> uselessList);

    /**
     * APP接口：根据用户身份获取相关角色信息
     *
     * @param users 用户信息
     * @return {@link RoleInfoDTO} or {@code}相关角色信息
     */
    RoleInfoDTO getRoleInfo(UserDTO users);

    /**
     * 个人面板
     *
     * @param userId 用户ID
     * @return {@link List <RelativeDTO>} or {@code null} 个人面板信息
     */
    List<RelativeDTO> getPersonPanel(String userId);

    /**
     * 激活邮箱，激活后用户可以通过邮箱登录
     *
     * @param userId 用户id
     * @param email  邮箱
     * @param code   验证码
     */
    void saveActivateEmail(String userId, String email, String code);

    /**
     * 修改用户登录密码，相同userId下不同loginId的密码都要修改
     * changePassword --> updateChangePassword  by wuxh 0817 for transcation Management
     *
     * @param userId      用户id
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    void updateChangePassword(String userId, String oldPassword, String newPassword);

    /**
     * 修改密码记录日志
     *
     * @param logPasswordDTO 修改信息
     */
    void updatePasswordLog(LogPasswordDTO logPasswordDTO);

    /**
     * 更新用户数据（其它平台会用）
     *
     * @param userDTO {@link UserDTO} 必须有userId
     */
    void updateByUserId(UserDTO userDTO);

    /**
     * 解除绑定手机或邮箱
     *
     * @param userId    userId
     * @param loginType {@link net.xuele.member.constant.UserConstants#PHONE}，{@link net.xuele.member.constant.UserConstants#EMAIL}
     */
    void deleteMobileOrEmail(String userId, Integer loginType);

    /**
     * 用户登录修改或插入登录信息
     *
     * @param userId   用户id
     * @param schoolId 学校id
     * @param type     登录类型(0:PC端；1:IOS端；2:Android端)
     * @see #saveLoginStatistics(CasLoginLogDTO)
     */
    @Deprecated
    void saveLoginStatistics(String userId, String schoolId, Integer type);

    /**
     * 用户登录修改或插入登录信息
     *
     * @Param casLoginLogDTO 参数：
     * loginId  登录id
     * userId   用户id（*）
     * schoolId 学校id
     * type     登录类型(0:PC端；1:IOS端；2:Android端)（*）
     */
    void saveLoginStatistics(CasLoginLogDTO casLoginLogDTO);

    /**
     * 保存头像，并将之前使用的头像保存到已使用的头像库中
     *
     * @param userId 用户id
     * @param icon   头像
     */
    void saveIcon(String userId, String icon);

    /**
     * 用户绑定功能，一个用户绑定到另一个用户
     *
     * @param userId       学乐号
     * @param targetUserId 绑定学乐号，密码和绑定号已这个为准
     */
    void saveBindUser(String userId, String targetUserId);

    /**
     * 获取某学校的某身份的人数
     *
     * @param schoolId   学校id
     * @param identityId 身份id
     * @return 人数
     */
    Integer getCountOfUser(String schoolId, String identityId);

    /**
     * APP接口：查询是否绑定邮箱
     *
     * @param userId 用户学乐号
     * @return 0、userId为空，1、未绑定，2、已绑定，3、待验证
     */
    EmailStatusDTO emailStatusDTO(String userId);

    /**
     * 通讯录 根据userId查询自己的联系人(通讯录左侧菜单) web端用
     *
     * @param userId     用户ID
     * @param schoolId   学校ID
     * @param identityId 角色身份
     * @return
     */
    ContactsGroupDTO queryContactsLeftNode(String userId, String schoolId, String identityId);

    /**
     * 通讯录 分页查询学校管理层 web端
     * app端   不用分页
     *
     * @param contactsTeacherPageRequest 分页信息
     * @return 分页学校管理层
     */
    PageResponse<ContactsTeacherDTO> queryTeacherInfoBySubjectPage(ContactsTeacherPageRequest contactsTeacherPageRequest);

    /**
     * 通讯录 分页查询班级学生web端
     * app端   不用分页
     *
     * @param contactsStudentPageRequest 分页信息
     * @return 分页学生
     */
    PageResponse<ContactsStudentsDTO> queryStudentInfoByClassPage(ContactsStudentPageRequest contactsStudentPageRequest);

    /**
     * 学生查询 家人 、同学、老师信息 web端
     * app端   不用分页
     *
     * @param contactsByStudentUserIdPageRequest
     * @return ContactsStudentsLeafDTO
     */
    PageResponse<ContactsStudentsLeafDTO> queryContactsByStudentPage(ContactsByStudentUserIdPageRequest contactsByStudentUserIdPageRequest);

    /**
     * 家长查询 孩子、老师信息web端
     * app端   不用分页
     *
     * @param contactsByStudentUserIdPageRequest
     * @return ContactsStudentsLeafDTO
     */
    PageResponse<ContactsStudentsDTO> queryContactsByParentPage(ContactsByStudentUserIdPageRequest contactsByStudentUserIdPageRequest);

    /**
     * 通讯录 根据userId查询自己的联系人(通讯录左侧菜单) app端用
     *
     * @param userId     用户ID
     * @param schoolId   学校ID
     * @param identityId 角色身份
     * @return
     */
    ContactsGroupDTO queryContactsLeftNodeByApp(String userId, String schoolId, String identityId);

    /**
     * 从redis中获取前端css、js版本version值
     *
     * @return
     */
    String getCssJsVersion();

    /**
     * 学生所在的班级的其他学生最后一次登录的时间距离当前时间超过day天
     *
     * @param userId   学生id
     * @param schoolId 学校id
     * @param classId  班级id
     * @param day      天数
     * @return 学生信息
     */
    List<UserDTO> queryStudentTimeOut(String userId, String schoolId, String classId, Integer day);

    /**
     * 添加用户，学生或老师
     *
     * @param userAddDTO 必要的一些数据
     * @return 返回学乐号
     */
    String saveOneUser(UserAddDTO userAddDTO);

    /**
     * 保存用户设备信息
     * 手机端调用
     *
     * @param mobileInfoDTO 用户设备信息表的一些数据
     */
    void saveMobileDevice(MobileInfoDTO mobileInfoDTO);

    /**
     * 通过学校ID和身份 查询对应的users
     *
     * @param schoolId
     * @param identityId
     * @return
     */
    List<String> getUserIdsByIdentityId(String schoolId, String identityId);

    /**
     * 校验用户状态
     *
     * @param userId userId
     * @return 1, 正确, 其它错误码
     */
    Integer getUserValidateStatus(String userId);

}

