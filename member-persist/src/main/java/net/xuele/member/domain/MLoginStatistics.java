package net.xuele.member.domain;

import java.util.Date;

public class MLoginStatistics {
    /**
     * 学乐号
     */
    private String userId;

    /**
     * 最后一次登录时间
     */
    private Date lastLoginTime;

    /**
     * 所属学校ID
     */
    private String schoolId;

    /**
     * 第一次登录时间（创建时间）
     */
    private Date firstLoginTime;

    /**
     * 登录类型（0：PC端；1：手机端）
     */
    private Integer loginType;
    /**
     * 登录的ip
     */
    private String loginIp;

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    /**
     * 获取 [M_LOGIN_STATISTICS] 的属性 学乐号
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置[M_LOGIN_STATISTICS]的属性学乐号
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * 获取 [M_LOGIN_STATISTICS] 的属性 最后一次登录时间
     */
    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    /**
     * 设置[M_LOGIN_STATISTICS]的属性最后一次登录时间
     */
    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    /**
     * 获取 [M_LOGIN_STATISTICS] 的属性 所属学校ID
     */
    public String getSchoolId() {
        return schoolId;
    }

    /**
     * 设置[M_LOGIN_STATISTICS]的属性所属学校ID
     */
    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId == null ? null : schoolId.trim();
    }

    /**
     * 获取 [M_LOGIN_STATISTICS] 的属性 第一次登录时间（创建时间）
     */
    public Date getFirstLoginTime() {
        return firstLoginTime;
    }

    /**
     * 设置[M_LOGIN_STATISTICS]的属性第一次登录时间（创建时间）
     */
    public void setFirstLoginTime(Date firstLoginTime) {
        this.firstLoginTime = firstLoginTime;
    }

    /**
     * 获取 [M_LOGIN_STATISTICS] 的属性 登录类型（0：PC端；1：手机端）
     */
    public Integer getLoginType() {
        return loginType;
    }

    /**
     * 设置[M_LOGIN_STATISTICS]的属性登录类型（0：PC端；1：手机端）
     */
    public void setLoginType(Integer loginType) {
        this.loginType = loginType;
    }
}