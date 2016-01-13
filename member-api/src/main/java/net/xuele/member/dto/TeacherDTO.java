package net.xuele.member.dto;

import java.io.Serializable;

/**
 * Created by mengzhiheng  on 2015/6/26 0020.
 */
public class TeacherDTO implements Serializable {

    private static final long serialVersionUID = 3385066424570199754L;
    /**
     * 学乐号
     */
    private String userId;

    /**
     * 真实名称
     */
    private String realName;
    /**
     * 所属学校ID
     */
    private String schoolId;

    /**
     * 所属学校名称
     */
    private String schoolName;

    /**
     * 手机
     */
    private String mobile;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 主授科目
     */
    private String bookId;

    /**
     * 学校职务ID（引用m_position字典)
     */
    private String positionId;

    /**
     * 学校职务名称
     */
    private String positionName;

    /**
     * 是否为管理层(0不是，1是）
     */
    private Integer isManager;

    /**
     * 获取 [M_TEACHER] 的属性 学乐号
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置[M_TEACHER]的属性学乐号
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    /**
     * 获取 [M_TEACHER] 的属性 所属学校ID
     */
    public String getSchoolId() {
        return schoolId;
    }

    /**
     * 设置[M_TEACHER]的属性所属学校ID
     */
    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId == null ? null : schoolId.trim();
    }

    /**
     * 获取 [M_TEACHER] 的属性 所属学校名称
     */
    public String getSchoolName() {
        return schoolName;
    }

    /**
     * 设置[M_TEACHER]的属性所属学校名称
     */
    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName == null ? null : schoolName.trim();
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

    /**
     * 获取 [M_TEACHER] 的属性 主授科目
     */
    public String getBookId() {
        return bookId;
    }

    /**
     * 设置[M_TEACHER]的属性主授科目
     */
    public void setBookId(String bookId) {
        this.bookId = bookId == null ? null : bookId.trim();
    }

    /**
     * 获取 [M_TEACHER] 的属性 学校职务ID（引用m_position字典)
     */
    public String getPositionId() {
        return positionId;
    }

    /**
     * 设置[M_TEACHER]的属性学校职务ID（引用m_position字典)
     */
    public void setPositionId(String positionId) {
        this.positionId = positionId == null ? null : positionId.trim();
    }

    /**
     * 获取 [M_TEACHER] 的属性 学校职务名称
     */
    public String getPositionName() {
        return positionName;
    }

    /**
     * 设置[M_TEACHER]的属性学校职务名称
     */
    public void setPositionName(String positionName) {
        this.positionName = positionName == null ? null : positionName.trim();
    }

    /**
     * 获取 [M_TEACHER] 的属性 是否为管理层(0不是，1是）
     */
    public Integer getIsManager() {
        return isManager;
    }

    /**
     * 设置[M_TEACHER]的属性是否为管理层(0不是，1是）
     */
    public void setIsManager(Integer isManager) {
        this.isManager = isManager;
    }

}
