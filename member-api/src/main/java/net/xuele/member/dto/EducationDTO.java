package net.xuele.member.dto;

import java.io.Serializable;

/**
 * Created by kaike.du on 2015/6/26 0026.
 */
public class EducationDTO implements Serializable {
    private static final long serialVersionUID = -8036424362210416168L;
    /**
     * 学乐号
     */
    private String userId;

    /**
     * 所属教育机构ID
     */
    private String educationalId;

    /**
     * 所属教育机构
     */
    private String educationalName;

    /**
     * 职务
     */
    private String dutyId;

    /**
     * 职务名称
     */
    private String dutyName;

    /**
     * 获取 [M_EDUCATION] 的属性 学乐号
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置[M_EDUCATION]的属性学乐号
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * 获取 [M_EDUCATION] 的属性 所属教育机构ID
     */
    public String getEducationalId() {
        return educationalId;
    }

    /**
     * 设置[M_EDUCATION]的属性所属教育机构ID
     */
    public void setEducationalId(String educationalId) {
        this.educationalId = educationalId == null ? null : educationalId.trim();
    }

    /**
     * 获取 [M_EDUCATION] 的属性 所属教育机构
     */
    public String getEducationalName() {
        return educationalName;
    }

    /**
     * 设置[M_EDUCATION]的属性所属教育机构
     */
    public void setEducationalName(String educationalName) {
        this.educationalName = educationalName == null ? null : educationalName.trim();
    }

    /**
     * 获取 [M_EDUCATION] 的属性 职务
     */
    public String getDutyId() {
        return dutyId;
    }

    /**
     * 设置[M_EDUCATION]的属性职务
     */
    public void setDutyId(String dutyId) {
        this.dutyId = dutyId == null ? null : dutyId.trim();
    }

    /**
     * 获取 [M_EDUCATION] 的属性 职务名称
     */
    public String getDutyName() {
        return dutyName;
    }

    /**
     * 设置[M_EDUCATION]的属性职务名称
     */
    public void setDutyName(String dutyName) {
        this.dutyName = dutyName == null ? null : dutyName.trim();
    }
}
