package net.xuele.member.dto;

import net.xuele.member.constant.UserConstants;

import java.io.Serializable;

/**
 * Created by kaike.du on 2015/6/26 0026.
 */
public class SchoolManagerDTO implements Serializable {
    private static final long serialVersionUID = 4200105946206043460L;
    /**
     * 学乐号
     */
    private String userId;

    /**
     * 所属学校ID
     */
    private String schoolId;

    /**
     * 所属学校名称
     */
    private String schoolName;
    /**
     * 用户名称
     */
    private String realName;
    /**
     * 用户头像
     */
    private String icon;
    /**
     * 职务ID
     */
    private String positionId;
    /**
     * 职务名称
     */
    private String positionName;

    /**
     * 获取 [M_SCHOOL_MANAGER] 的属性 学乐号
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置[M_SCHOOL_MANAGER]的属性学乐号
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * 获取 [M_SCHOOL_MANAGER] 的属性 所属学校ID
     */
    public String getSchoolId() {
        return schoolId;
    }

    /**
     * 设置[M_SCHOOL_MANAGER]的属性所属学校ID
     */
    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId == null ? null : schoolId.trim();
    }

    /**
     * 获取 [M_SCHOOL_MANAGER] 的属性 所属学校名称
     */
    public String getSchoolName() {
        return schoolName;
    }

    /**
     * 设置[M_SCHOOL_MANAGER]的属性所属学校名称
     */
    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName == null ? null : schoolName.trim();
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon == null ? UserConstants.ICON_DEFAULT : icon.trim();
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }
}
