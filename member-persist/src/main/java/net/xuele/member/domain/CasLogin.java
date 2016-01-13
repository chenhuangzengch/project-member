package net.xuele.member.domain;

public class CasLogin {
    /**
     * 登录号
     */
    private String loginId;
    /**
     * 登录类型（1：学乐号：2：手机；3：邮箱）
     */
    private int loginType;
    /**
     * 登录密码
     */
    private String password;
    /**
     * 默认学乐号
     */
    private String userId;

    /**
     * 登录状态，1：正常；0：禁用；
     */
    private Integer status;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}