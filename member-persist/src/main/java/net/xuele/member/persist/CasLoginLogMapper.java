package net.xuele.member.persist;

import net.xuele.member.domain.CasLoginLog;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface CasLoginLogMapper {

    int insert(CasLoginLog record);

    int getLoginTimes(@Param("userId") String userId,
                      @Param("schoolId") String schoolId,
                      @Param("startTime") Date startTime,
                      @Param("endTime") Date endTime,
                      @Param("loginType") List<Integer> loginType);

    /**
     * 获取某个学校在某段时间内登陆的某个身份的人数
     *
     * @param schoolId   学校id
     * @param identityId 登录人身份 老师、学生、全部
     * @param begTime    开始时间
     * @param endTime    截止时间
     * @return 人数
     */
    int getLoginPersonCount(@Param("schoolId") String schoolId,
                            @Param("begTime") Date begTime,
                            @Param("endTime") Date endTime,
                            @Param("identityId") String identityId);

    /**
     * 获取某人最后第二次登录时间
     *
     * @param userId   用户id
     * @param schoolId 学校id
     */
    CasLoginLog getLastSecondTimeByUserId(@Param("userId")String userId,
                                          @Param("schoolId")String schoolId);
}