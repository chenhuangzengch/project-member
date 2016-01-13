package net.xuele.member.dto.page;

import net.xuele.common.page.PageRequest;
import net.xuele.member.constant.UserConstants;

import java.io.Serializable;

/**
 * Created by guochun.shen on 2015/7/1 0001.
 */
public class StudentPageRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = -8966345322091631394L;
    /**
     * 学乐号
     */
    private String userId;

    /**
     * 学生姓名
     */
    private String realName;

    /**
     * 所属班级id
     */
    private String classId;


    /**
     * 所属班级别名
     */
    private String classAliasName;

    private String icon;

    /**
     * 用户状态,[0,有效;1,未初始化;2,离校;]
     */
    private Integer status = 1;
    /**
     * 学界
     */
    private Integer years;
    /**
     * 最后登录时间
     */
    private String lastLoginDate;
    /**
     * 所属学校ID
     */
    private String schoolId;

    public int getPageSize() {
        if (pageSize > 0) {
            return pageSize;
        }
        return 100;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon == null ? UserConstants.ICON_DEFAULT : icon.trim();
    }

    public String getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(String lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    /**
     * 获取 [M_STUDENT] 的属性 学乐号
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置[M_STUDENT]的属性学乐号
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * 获取 [M_STUDENT] 的属性 所属班级id
     */
    public String getClassId() {
        return classId;
    }

    /**
     * 设置[M_STUDENT]的属性所属班级id
     */
    public void setClassId(String classId) {
        this.classId = classId == null ? null : classId.trim();
    }

    /**
     * 获取 [M_STUDENT] 的属性 所属班级别名
     */
    public String getClassAliasName() {
        return classAliasName;
    }

    /**
     * 设置[M_STUDENT]的属性所属班级别名
     */
    public void setClassAliasName(String classAliasName) {
        this.classAliasName = classAliasName == null ? null : classAliasName.trim();
    }

    /**
     * 获取 [M_USERS] 的属性 用户状态,[0,有效;1,未初始化;2,离校;]
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置[M_USERS]的属性用户状态,[0,有效;1,未初始化;2,离校;]
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getYears() {
        return years;
    }

    public void setYears(Integer years) {
        this.years = years;
    }
}
