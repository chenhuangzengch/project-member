package net.xuele.member.dto;

import java.io.Serializable;

/**
 * Created by wuxh on 2015/8/6 0006.
 */
public class EducationOrganAndManagerDTO implements Serializable {

    private static final long serialVersionUID = 9109281380407732613L;
    /**
     * 管理员学乐号
     */
    private String userId;
    /**
     * 管理员名称
     */
    private String realName;
    /**
     * 机构id
     */
    private String eduOrgId;

    /**
     * 机构名称
     */
    private String eduOrgName;

    /**
     * 机构所在地区编码
     */
    private String areaCode;

    /**
     * 机构所在你地区名
     */
    private String areaName;

    /**
     * 管理员qq号
     */
    private String qq;

    /**
     * 管理员电话
     */
    private String mobile;

    /**
     * 管理员邮箱
     */
    private String email;

    /**
     * 管理员密码
     */
    private String password;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getEduOrgId() {
        return eduOrgId;
    }

    public void setEduOrgId(String eduOrgId) {
        this.eduOrgId = eduOrgId;
    }

    public String getEduOrgName() {
        return eduOrgName;
    }

    public void setEduOrgName(String eduOrgName) {
        this.eduOrgName = eduOrgName;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}
