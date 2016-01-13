package net.xuele.member.domain;

/**
 * Created by guochun.shen on 2015/7/29 0029.
 */
public class MparentsCasLogin {
    /**
     * 登录号
     */
    private String loginId;
    /**
     * 登录类型
     */
    private int loginType;
    /**
     * 默认学乐号
     */
    private String userId;
    /**
     * 身份类型,引用d_identity
     */
    private String identityId;
    public int getLoginType() {
        return loginType;
    }

    public void setLoginType(int loginType) {
        this.loginType = loginType;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    /**
     * 获取 [M_USERS] 的属性 身份类型,引用d_identity
     */
    public String getIdentityId() {
        return identityId;
    }

    /**
     * 设置[M_USERS]的属性身份类型,引用d_identity
     */
    public void setIdentityId(String identityId) {
        this.identityId = identityId == null ? null : identityId.trim();
    }

}
