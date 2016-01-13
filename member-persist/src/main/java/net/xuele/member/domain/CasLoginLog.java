package net.xuele.member.domain;

import java.util.Date;

public class CasLoginLog {
    /**
     * 登录id
     */
    private Long id;

    /**
     * 登录号
     */
    private String loginId;

    /**
     * 登录类型（0：PC端；1：IOS端；2：Android端）
     */
    private Integer loginType;

    /**
     * 学乐号
     */
    private String userId;

    /**
     * 登录时间
     */
    private Date loginTimestamp;

    /**
     * 学校ID
     */
    private String schoolId;
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
     * 获取 [CAS_LOGIN_LOG] 的属性 登录id
     */
    public Long getId() {
        return id;
    }


    /**
     * 设置[CAS_LOGIN_LOG]的属性登录id
     */

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取 [CAS_LOGIN_LOG] 的属性 登录号
     */
    public String getLoginId() {
        return loginId;
    }

    /**
     * 设置[CAS_LOGIN_LOG]的属性登录号
     */
    public void setLoginId(String loginId) {
        this.loginId = loginId == null ? null : loginId.trim();
    }

    /**
     * 获取 [CAS_LOGIN_LOG] 的属性 登录类型（0：PC端；1：IOS端；2：Android端）
     */
    public Integer getLoginType() {
        return loginType;
    }

    /**
     * 设置[CAS_LOGIN_LOG]的属性登录类型（0：PC端；1：IOS端；2：Android端）
     */
    public void setLoginType(Integer loginType) {
        this.loginType = loginType;
    }

    /**
     * 获取 [CAS_LOGIN_LOG] 的属性 学乐号
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置[CAS_LOGIN_LOG]的属性学乐号
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * 获取 [CAS_LOGIN_LOG] 的属性 登录时间
     */
    public Date getLoginTimestamp() {
        return loginTimestamp;
    }

    /**
     * 设置[CAS_LOGIN_LOG]的属性登录时间
     */
    public void setLoginTimestamp(Date loginTimestamp) {
        this.loginTimestamp = loginTimestamp;
    }

    /**
     * 获取 [CAS_LOGIN_LOG] 的属性 会话ID
     */
    public String getSchoolId() {
        return schoolId;
    }

    /**
     * 设置[CAS_LOGIN_LOG]的属性会话ID
     */
    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId == null ? null : schoolId.trim();
    }
}