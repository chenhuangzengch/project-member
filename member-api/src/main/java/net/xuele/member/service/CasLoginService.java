package net.xuele.member.service;

import net.xuele.member.dto.CasLoginDTO;

import java.util.Date;
import java.util.List;

/**
 * Created by wuxh on 15/8/3.
 */
public interface CasLoginService {

    /**
     * 根据登录号获取对应的用户id
     *
     * @param loginId 登录号
     * @return userId 用户id
     */
    String getUseIdByLoginId(String loginId);

    /**
     * 根据登录号获取登录信息
     *
     * @param loginId 登录号
     * @return 登录信息
     */
    CasLoginDTO getByLoginId(String loginId);

    /**
     * 更新用户的登录状态，1：正常；0：禁用；
     *
     * @param userId 用户id
     * @param status 用户状态
     */
    void updateUserLoginStatus(String userId, Integer status);

    /**
     * 根据用户id和登录类型获取登录号
     *
     * @param userId    用户id
     * @param loginType 登录类型
     * @return loginId 登录号
     */
    String getLoginIdByUserIdAndLoginType(String userId, Integer loginType);

    /**
     * 邀请家长时调用  手机号作为登陆号
     * 往cas_login插入数据
     *
     * @param casLoginDTO 登录信息
     * @return 插入数据库的条数
     */
    int insert(CasLoginDTO casLoginDTO);

    /**
     * 根据userId查询该用户下的所有可用来登录的login 信息
     *
     * @param userId 用户id
     * @return {@link CasLoginDTO}
     */
    List<CasLoginDTO> getByUserId(String userId);

    /**
     * 获取一段时间某用户登录的次数
     *
     * @param userId    用户id
     * @param schoolId  学校id
     * @param startTime 起始时间
     * @param endTime   结束时间
     * @param loginType 登录类型(0为pc端;1为IOS端;2为Android端;3为IOS端和Android端;4为所有类型.)
     * @return 登录次数
     */
    int getLoginTimes(String userId, String schoolId, Date startTime, Date endTime, Integer loginType);

    /**
     * 获取某个学校在某段时间内登陆的学生或教师人数
     *
     * @param schoolId      学校id
     * @param loginUserType 登录人类型 1：教师 2:学生 3：全部
     * @param begTime       开始时间
     * @param endTime       截止时间
     * @return 人数
     */
    int getLoginPersonCount(String schoolId, Integer loginUserType, Date begTime, Date endTime);

    /**
     * 获取某人最后一次登录时间
     * @param userId 用户id
     * @param schoolId 学校id
     * @return 时间
     */
    Date getLastLoginTime(String userId,String schoolId);

}
