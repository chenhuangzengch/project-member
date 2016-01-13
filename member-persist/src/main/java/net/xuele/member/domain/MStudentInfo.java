package net.xuele.member.domain;

/**
 * Created by zhongjian.xu on 2015/8/7 0007.
 */
public class MStudentInfo {
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 用户名称
     */
    private String realName;
    /**
     * 用户头像
     */
    private String icon;
    /**
     * 用户签名
     */
    private String signature;
    /**
     * 学校ID
     */
    private String schoolId;
    /**
     * 学校名称
     */
    private String schoolName;
    /**
     * 班级ID
     */
    private String classId;
    /**
     * 班级名称
     */
    private String className;
    /**
     * 学界
     */
    private Integer year;

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
