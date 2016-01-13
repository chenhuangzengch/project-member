package net.xuele.member.dto;

import java.io.Serializable;

/**
 * Created by kaike.du on 2015/6/26 0026.
 */
public class EducationManagerDTO implements Serializable {
    private static final long serialVersionUID = 5600194482236857862L;
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
     * 获取 [M_EDUCATION_MANAGER] 的属性 学乐号
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置[M_EDUCATION_MANAGER]的属性学乐号
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * 获取 [M_EDUCATION_MANAGER] 的属性 所属教育机构ID
     */
    public String getEducationalId() {
        return educationalId;
    }

    /**
     * 设置[M_EDUCATION_MANAGER]的属性所属教育机构ID
     */
    public void setEducationalId(String educationalId) {
        this.educationalId = educationalId == null ? null : educationalId.trim();
    }

    /**
     * 获取 [M_EDUCATION_MANAGER] 的属性 所属教育机构
     */
    public String getEducationalName() {
        return educationalName;
    }

    /**
     * 设置[M_EDUCATION_MANAGER]的属性所属教育机构
     */
    public void setEducationalName(String educationalName) {
        this.educationalName = educationalName == null ? null : educationalName.trim();
    }
}
