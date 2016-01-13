package net.xuele.member.dto;

import java.io.Serializable;

/**
 * Created by guochun.shen on 2015/7/29 0029.
 */
public class CasLoginDTO implements Serializable{
    private static final long serialVersionUID = -439942137774591113L;
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
    /**
     * 学生学乐号
     */
    private String targetUserId;
    /**
     * 密码
     */
    private String password;

    /**
     * 登录状态，1：正常；0：禁用；
     */
    private Integer status;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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

    public String getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(String targetUserId) {
        this.targetUserId = targetUserId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
