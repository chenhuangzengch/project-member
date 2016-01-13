package net.xuele.member.domain;

import java.util.Date;

public class MStudentManager {

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
     * 所属班级名称
     */
    private String className;
    /**
     * 所属班级别名
     */
    private String classAliasName;
    /**
     * 图标ID
     */
    private String icon;

    /**
     * 用户状态
     */
    private Integer status;

    /**
     * 学界
     */
    private Integer years;

    /**
     * 最后登录时间
     */
    private Date lastLoginDate;

    /**
     * 所属学校ID
     */
    private String schoolId;

    /**
     * 所属学校名称
     */
    private String schoolName;
    /**
     * 区域id
     */
    private String area;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
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
        this.icon = icon;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
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

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
