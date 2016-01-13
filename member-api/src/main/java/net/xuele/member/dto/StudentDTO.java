package net.xuele.member.dto;

import net.xuele.member.constant.UserConstants;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhongjian.xu on 2015/6/24 0024.
 */
public class StudentDTO implements Serializable {
    private static final long serialVersionUID = -1541346377706089027L;
    /**
     * 学乐号
     */
    private String userId;

    /**
     * 用户名称
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
     * 学籍号
     */
    private String studentNumber;

    /**
     * 家庭名称
     */
    private String familyName;

    /**
     * 家庭封面
     */
    private String familyCover;

    /**
     * 所属学校ID
     */
    private String schoolId;

    /**
     * 所属学校名称
     */
    private String schoolName;
    /**
     * 学界
     */
    private Integer year;
    /**
     * 年级名称
     */
    private String GradeName;

    /**
     * 入班时间,加入班级的时间
     */
    private Date joinClass;
    /**
     * 年级 1,2
     */
    private Integer gradeNum;
    /**
     * 学期 1,2
     */
    private Integer semester;

    /**
     * 学生手机号码
     */
    private String mobile;
    /**
     * 学生邮箱
     */
    private String email;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 区域id
     */
    private String areaCode;
    /**
     * 区域名称
     */
    private String areaName;

    private String icon;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getGradeNum() {
        return gradeNum;
    }

    public void setGradeNum(Integer gradeNum) {
        this.gradeNum = gradeNum;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
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
     * 获取 [M_STUDENT] 的属性 所属班级名称
     */
    public String getClassName() {
        return className;
    }

    /**
     * 设置[M_STUDENT]的属性所属班级名称
     */
    public void setClassName(String className) {
        this.className = className == null ? null : className.trim();
    }

    /**
     * 获取 [M_STUDENT] 的属性 学籍号
     */
    public String getStudentNumber() {
        return studentNumber;
    }

    /**
     * 设置[M_STUDENT]的属性学籍号
     */
    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber == null ? null : studentNumber.trim();
    }

    /**
     * 获取 [M_STUDENT] 的属性 家庭名称
     */
    public String getFamilyName() {
        return familyName;
    }

    /**
     * 设置[M_STUDENT]的属性家庭名称
     */
    public void setFamilyName(String familyName) {
        this.familyName = familyName == null ? null : familyName.trim();
    }

    /**
     * 获取 [M_STUDENT] 的属性 家庭封面
     */
    public String getFamilyCover() {
        return familyCover;
    }

    /**
     * 设置[M_STUDENT]的属性家庭封面
     */
    public void setFamilyCover(String familyCover) {
        this.familyCover = familyCover == null ? null : familyCover.trim();
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
     * 获取 [M_STUDENT] 的属性 所属学校ID
     */
    public String getSchoolId() {
        return schoolId;
    }

    /**
     * 设置[M_STUDENT]的属性所属学校ID
     */
    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId == null ? null : schoolId.trim();
    }

    /**
     * 获取 [M_STUDENT] 的属性 所属学校名称
     */
    public String getSchoolName() {
        return schoolName;
    }

    /**
     * 设置[M_STUDENT]的属性所属学校名称
     */
    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName == null ? null : schoolName.trim();
    }

    /**
     * 获取 [M_STUDENT] 的属性 入班时间,加入班级的时间
     */
    public Date getJoinClass() {
        return joinClass;
    }

    /**
     * 设置[M_STUDENT]的属性入班时间,加入班级的时间
     */
    public void setJoinClass(Date joinClass) {
        this.joinClass = joinClass;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getGradeName() {
        return GradeName;
    }

    public void setGradeName(String gradeName) {
        GradeName = gradeName;
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

    /**
     * 获取 [M_USERS] 的属性 头像
     */
    public String getIcon() {
        return icon == null || icon.length() == 0 ? UserConstants.ICON_DEFAULT : icon.trim();
    }

    /**
     * 设置[M_USERS]的属性头像
     */
    public void setIcon(String icon) {
        this.icon = icon == null ? null : icon.trim();
    }

}
