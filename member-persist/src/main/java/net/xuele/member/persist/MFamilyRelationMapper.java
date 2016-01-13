package net.xuele.member.persist;

import net.xuele.common.page.Page;
import net.xuele.member.domain.DFamilyMembers;
import net.xuele.member.domain.MFamilyRelation;
import net.xuele.member.domain.MStudentManager;
import net.xuele.member.domain.MUsers;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MFamilyRelationMapper {
    /**
     * 邀请家长，新增加关系
     *
     * @param record
     * @return
     */
    int insert(MFamilyRelation record);

    /**
     * 查询家庭成员
     *
     * @return
     */
    List<DFamilyMembers> queryFamilyMembers(@Param("userId") String userId, @Param("schoolId") String schoolId);

    //
    //MFamillyRelation selectByPrimaryKey(String id);
    //
    //List<MFamillyRelation> selectAll();
    //
    int updateByPrimaryKey(MFamilyRelation record);

    /**
     * APP接口：根据家长ID获取该家长的孩子信息
     */
    List<MFamilyRelation> getChildsByParentId(String userId);

    /**
     * 通知用
     * 根据学生Id查询家庭成员
     *
     * @param userId
     * @return
     */
    List<MFamilyRelation> queryFamilyByStudent(@Param("userId") String userId, @Param("schoolId") String schoolId);

    /**
     * 查询孩子的家长信息，不同学校的孩子
     *
     * @param userIds 学生id
     * @return 家庭成员列表
     */
    List<MFamilyRelation> queryFamilyList(@Param("userIds") List<String> userIds);

    /**
     * 查询孩子的家长id，同一学校的孩子
     *
     * @param userIds  孩子id列表
     * @param schoolId 学校id
     */
    List<MFamilyRelation> queryParentOfKidList(@Param("userIds") List<String> userIds, @Param("schoolId") String schoolId);

    int deleteByPrimaryKey(Long id);

    MUsers queryFamilyByPrimaryKey(Long id);

    /**
     * APP接口:根据家长ID获取孩子信息
     */
    List<MStudentManager> queryKidInfo(@Param("userId") String userId, @Param("status") int status);


    /**
     * 根据家长ID获取孩子名字和称谓
     */
    MFamilyRelation getMessageByParentId(String userId);


    /**
     * 根据关系ID获取关系信息
     */
    MFamilyRelation selectByPrimaryKey(Long id);

    /**
     * 站外邀请家长时 修改家庭关系表
     * 从 4：站外邀请手机验证码未填   变成 5：站外邀请家长未初始化
     *
     * @param mFamilyRelation
     */
    void updateFamilyRelation(MFamilyRelation mFamilyRelation);

    /**
     * 邀请家长，查询家庭关系表中是否有数据
     *
     * @param memberName
     * @param targetUserId
     * @return
     */
    MFamilyRelation getFamilyRelation(@Param("memberName") String memberName,
                                      @Param("targetUserId") String targetUserId,
                                      @Param("schoolId") String schoolId);

    /**
     * 邀请家长，查询家庭关系表   短信验证时需要通过查询家长称谓
     *
     * @param userId
     * @param targetUserId
     * @return
     */
    MFamilyRelation getFamilyRelationByUserId(@Param("userId") String userId,
                                              @Param("targetUserId") String targetUserId,
                                              @Param("schoolId") String schoolId);

    List<DFamilyMembers> getByTargetUserId(@Param("targetUserId") String targetUserId, @Param("schoolId") String schoolId);

    /**
     * 修改家庭关系表中家长名称
     */
    void updateUserName(@Param("userId") String userId,
                        @Param("realName") String realName);

    /**
     * 修改家庭关系表中孩子名称
     */
    void updateTargetUserName(@Param("userId") String userId,
                              @Param("realName") String realName,
                              @Param("schoolId") String schoolId);

    List<DFamilyMembers> queryFamilyByStudentPage(@Param(value = "page")
                                                  Page page, @Param("userId") String userId,
                                                  @Param("schoolId") String schoolId, @Param("limit") Integer limit);

    /**
     * 通讯录 家长查询学生对应家长
     *
     * @param targetUserIdList 班级ID
     * @return
     */
    List<DFamilyMembers> queryContactsParent(@Param("classIds") List<String> targetUserIdList);

    /**
     * 查询某个学生成功邀请到的家长数量
     *
     * @param targetUserId 学生ID
     * @param schoolId     学校ID
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @return 成功次数
     */
    long getInvitedToSucceedFamily(@Param("targetUserId") String targetUserId,
                                   @Param("schoolId") String schoolId,
                                   @Param("startTime") String startTime,
                                   @Param("endTime") String endTime);

    /**
     * 根据班级id获取这个班级所有学生的家长
     *
     * @param classId  班级id
     * @param schoolId 这个班级的学校id
     * @return 家长id列表
     */
    List<String> queryParentIdByClassId(@Param("schoolId") String classId,
                                        @Param("schoolId") String schoolId);
}