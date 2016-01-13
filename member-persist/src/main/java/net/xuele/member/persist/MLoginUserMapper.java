package net.xuele.member.persist;

import net.xuele.member.domain.MLoginUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MLoginUserMapper {
    int insert(MLoginUser record);

    /**
     * 插入m_login_user
     */
    void saveLoginUser(MLoginUser loginUser);

    /**
     * 根据用户ID获取信息
     */
    MLoginUser getByUserId(String userId);

    /**
     * 修改绑定学乐号的值
     *
     * @param bindUserId   原来值
     * @param targetUserId 新值
     */
    void updateBindUserId(@Param("bindUserId") String bindUserId, @Param("targetUserId") String targetUserId);

    /**
     * 根据绑定学乐号获取对应的相关学乐号
     */
    List<String> queryUserIdsByBindUserId(String bindUserId);
}