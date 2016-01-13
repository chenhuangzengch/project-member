package net.xuele.member.persist;

import net.xuele.member.domain.MUsers;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MUsersMapper {

    int insert(MUsers record);

    /**
     * WEB平台、APP接口：根据用户ID获取用户信息
     */
    MUsers selectByPrimaryKey(@Param(value = "userId") String userId, @Param(value = "schoolId") String schoolId);


    /**
     * 修改用户信息
     */
    int updateByPrimaryKey(MUsers record);


    /**
     * 修改离校状态
     */
    int updateStatus(@Param("status") int status, @Param("userIds") List<String> userIds, @Param("schoolId") String schoolId);


    /**
     * APP接口：根据用户ID列表获取该列表用户的信息
     */
    List<MUsers> getByUserIds(@Param("userIds") List<String> userIds,
                              @Param("schoolId") String schoolId);


    void updateUserName(@Param("userId") String userId, @Param("realName") String realName, @Param("schoolId") String schoolId);

    /**
     * 绑定手机号码或重新绑定
     *
     * @param userId 用户ID
     * @param mobile 手机号
     */
    void updateMobile(@Param("userId") String userId, @Param("mobile") String mobile, @Param("schoolId") String schoolId);

    /**
     * 绑定电子邮箱或重新绑定
     *
     * @param userId 用户Id
     * @param email  email
     */
    void updateEmail(@Param("userId") String userId, @Param("email") String email, @Param("schoolId") String schoolId);

    /**
     * 获取学校数量
     *
     * @param area 区域编号
     * @return 学校数量
     */
    long selectSchoolAmount(String area);

    /**
     * 获取教师数量
     *
     * @param area 区域编号
     * @return 教师数量
     */
    long selectTeacherAmount(String area);

    /**
     * 获取学生的数量
     *
     * @param area 区域编号
     * @return 学生的数量
     */
    long selectStudentAmount(String area);

    /**
     * 根据userId更新用户头像
     *
     * @param userId 用户ID
     * @param icon   头像
     */
    void updateIconByUserId(@Param("userId") String userId, @Param("icon") String icon, @Param("schoolId") String schoolId);

    /**
     * 获取用户所在学校id
     *
     * @param userId userId
     * @return 校id
     */
    String selectSchoolIdByUserId(String userId);

    Integer getCountOfUser(@Param("schoolId") String schoolId, @Param("identityId") String identityId);

    List<MUsers> queryStudentTimeOut(@Param("userId")String userId,
                                     @Param("schoolId")String schoolId,
                                     @Param("classId")String classId,
                                     @Param("day")Integer day);

    /**
     * 通过学校ID和身份 查询对应的users
     * @param schoolId
     * @param identityId
     * @return
     */
    List<String> getUserIdsByIdentityId(@Param("schoolId") String schoolId, @Param("identityId") String identityId);
}