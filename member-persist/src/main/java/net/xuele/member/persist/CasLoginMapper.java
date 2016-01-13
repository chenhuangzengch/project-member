package net.xuele.member.persist;

import net.xuele.member.domain.CasLogin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CasLoginMapper {
    /**
     * 根据登录号获取登录信息
     */
    CasLogin selectByPrimaryKey(String loginId);
    /**
     * 根据邀请家庭成员时输入的登录号查询用户信息
     */
//    MparentsCasLogin getCasLoginByLoginId(String loginId);


    int insert(CasLogin record);

    /**
     * APP接口、WEB平台：修改密码、重置密码
     */
    int updatePassword(@Param("userIdsList") List<String> userIdsList, @Param("password") String password);

    /**
     * APP接口：根据登录号（学乐号，手机，邮箱）获取该用户的其他相关角色
     */
    List<String> getUserIds(String loginId);

    /**
     * APP接口：检查用户名是否存在
     */
    int checkUserIdExist(String loginId);

    /**
     * WEB平台：通过UserId和登录类型删除账号
     * @param userId
     * @param loginType
     */
    void deleteByUserIdAndLoginType(@Param("userId") String userId, @Param("loginType") int loginType);

    /**
     * 通过登录id来获取用户id
     * @param loginId
     * @return
     */
    String getUserIdByLoginId(String loginId);

    /**
     * 通过userId更新用户的登录状态
     * @param userId
     * @param status 0：禁用、1：正常
     */
    void updateStatusByUserId(@Param("userId")String userId,@Param("status")Integer status);

    /**
     * 根据用户名和登录类型查找loginId
     * @param userId
     * @param loginType
     * @return
     */
    String getLoginIdByUserIdAndLoginType(@Param("userId") String userId, @Param("loginType") Integer loginType);

    /**
     * 根据学乐号获取该用户下的所有登录账号记录
     */
    List<CasLogin> getByUserId(@Param("userId")String userId);

    /**
     * 获取多个登录号的相关用户id
     */
    List<String> queryUserIds(@Param("loginIds")List<String> loginIds);

    /**
     * 获取多个登录号的相关登录认证信息
     */
    List<CasLogin> queryUsersCasLogin(@Param("loginIds")List<String> loginIds);
}