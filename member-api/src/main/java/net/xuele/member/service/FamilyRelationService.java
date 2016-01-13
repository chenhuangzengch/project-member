package net.xuele.member.service;

import net.xuele.member.dto.*;

import java.util.Date;
import java.util.List;

/**
 * 家庭邀请
 * Created by guochun.shen on 2015/7/28 0028.
 */
public interface FamilyRelationService {

    /**
     * 邀请家长，根据loginId获取登录信息
     *
     * @param loginId loginId
     * @return {@link ParentInfoDTO}
     */
//    ParentInfoDTO getCasLoginByLoginId(String loginId);

    /**
     * 新增家庭关系表
     *
     * @param familyRelationDTO {@link FamilyRelationDTO}
     * @return {@link FamilyRelationDTO}
     */
//    FamilyRelationDTO saveFamilyRelation(FamilyRelationDTO familyRelationDTO);

    /**
     * 根据学生id查询家庭成员
     *
     * @param userId 学生id
     * @return 家庭成员列表
     */
    List<FamilyMembersDTO> queryFamilyMembers(String userId);

    /**
     * APP接口:根据家长ID获取孩子信息
     *
     * @param userId 家长ID
     * @return 孩子信息
     */
    List<StudentDTO> queryKidInfo(String userId);

    /**
     * 根据家长ID获取称谓以及孩子姓名(状态是5，站外邀请家长未初始化，状态是1，站内邀请家长未初始化)
     *
     * @param userId 家长id
     * @return {@link FamilyRelationDTO} 家庭关系信息
     */
    FamilyRelationDTO getMessageByParentId(String userId);


    /**
     * 更新家庭关系
     *
     * @param familyRelationDTO {@link FamilyRelationDTO} 必须有id
     */
//    void updateById(FamilyRelationDTO familyRelationDTO);

    /**
     * 根据关系ID获取关系信息
     *
     * @param id 关系id
     * @return 家庭关系信息
     */
    FamilyRelationDTO getMessageById(Long id);

    /**
     * 站外邀请家长时 修改家庭关系表
     * 从 4：站外邀请手机验证码未填   变成 5：站外邀请家长未初始化
     *
     * @param familyRelationDTO 家庭关系表信息
     */
//    void updateFamilyRelation(FamilyRelationDTO familyRelationDTO);

    /**
     * 查询家庭关系表     邀请家长需要根据称谓和学生号 判断是否已经发起过邀请
     *
     * @param memberName   称谓
     * @param targetUserId 学生ID
     * @return 家庭关系表
     */
//    FamilyRelationDTO getFamilyRelation(String memberName, String targetUserId);

    /**
     * 查询家庭关系表     短信验证时需要通过查询家长称谓
     *
     * @param userId       家长ID
     * @param targetUserId 学生ID
     * @return 家庭关系信息
     */
//    FamilyRelationDTO getFamilyRelationByUserId(String userId, String targetUserId);

    /**
     * 插入家长表
     *
     * @param parentsDTO 家长信息
     * @return int
     */
//    int insert(ParentsDTO parentsDTO);

    /**
     * 判断输入号码是否符合规则，符合查询是否存在，如果不存在且是手机号，则进行短信邀请
     *
     * @param loginId      登录号
     * @param targetUserId 学生ID
     * @param memberName   称谓
     * @return 家长信息
     */
    ParentInfoDTO getParentInfo(String loginId, String targetUserId, String memberName);

    /**
     * modify addFamilyRelationForStudent --> saveFamilyRelationForStudent by wuxh 0817 for transaction management
     * <p/>
     * 站内邀请  增加家庭关系
     *
     * @param familyRelationDTO {@link FamilyRelationDTO}
     */
    FamilyRelationDTO saveFamilyRelationForStudent(FamilyRelationDTO familyRelationDTO);

    /**
     * 站内邀请  同意或拒绝
     *
     * @param id   主键ID
     * @param type 1同意 0拒绝
     */
    void saveDoAction(Long id, Integer type);

    /**
     * smsAddFamilyRelation --> saveSmsAddFamilyRelation
     * 短信邀请，发送账号密码
     *
     * @param loginId      手机号
     * @param checkCode    验证码
     * @param memberName   称谓
     * @param targetUserId 学生ID
     */
//    void saveSmsAddFamilyRelation(String loginId, String checkCode, String memberName, String targetUserId);

    /**
     * 家庭信息根据status返回数据
     *
     * @param id 关系id
     * @return 家庭成员当前信息
     */
    FamilyWrapperDTO inviteByPlatform(Long id);

    /**
     * 根据关系id查找该学生的信息以及家长的信息
     *
     * @param id 关系id
     * @return 家长信息列表
     */
    List<ParentInfoDTO> getParentsMessageById(Long id);

    /**
     * 初始化家长信息
     *
     * @param userId 用户id
     * @param name   姓名
     * @return
     */
    FamilyRelationDTO updateParentInfo(String userId, String name);

    /**
     * 修改家庭关系表中用户名称
     *
     * @param userId   用户id
     * @param realName 用户名称
     */
    void updateName(String userId, String realName);

    /**
     * 查询某个学生成功邀请到的家长数量
     *
     * @param targetUserId 学生ID
     * @param schoolId     学校ID
     * @param startTime    开始时间 Date
     * @param endTime      结束时间 Date
     * @return 成功次数
     */
    long getInvitedToSucceedFamily(String targetUserId, String schoolId, Date startTime, Date endTime);

    /**
     * 家庭邀请 web、app
     * 给邀请用户发送手机验证码
     *
     * @param mobile       手机号
     * @param targetUserId 学生ID
     * @param memberName   称谓
     * @return
     */
    long getCheckCodeForInviteUser(String mobile, String targetUserId, String memberName, String createUserId);

    /**
     * 邀请家长web、app
     * 短信邀请，发送账号密码
     *
     * @param loginId      手机号
     * @param checkCode    验证码
     * @param memberName   称谓
     * @param targetUserId 学生ID
     * @return
     */
    FamilyRelationDTO smsAddFamilyRelation(String loginId, String checkCode, String memberName, String targetUserId);

    /**
     * 解除关系 web、app
     *
     * @param id         关系id
     * @param identityId 用户角色
     * @param userId     用户id
     * @return FamilyRelationDTO
     */
    FamilyRelationDTO releaseRelationship(Long id, String identityId, String userId);

    /**
     * 重新邀请 web、app
     *
     * @param id 关系id
     * @return FamilyRelationDTO
     */
    FamilyRelationDTO reNotify(Long id);

    /**
     * 根据班级id获取这个班级所有学生的家长
     *
     * @param classId  班级id
     * @param schoolId 这个班级的学校id
     * @return 家长id列表
     */
    List<String> queryParentIdByClassId(String classId, String schoolId);
}
