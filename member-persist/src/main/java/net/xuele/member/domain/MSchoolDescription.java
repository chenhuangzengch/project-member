package net.xuele.member.domain;

/**
 * Created by guochun.shen on 2015/8/5 0005.
 */
public class MSchoolDescription {

    /**
     * 学校Id
     */
    private String schoolId;
    /**
     * 学校名称
     */
    private String schoolName;

    /**
     * 校长
     */
    private String schoolMaster;

    /**
     * 校长userId
     */
    private String userId;
    /**
     * 教师数量
     */
    private long teacherAmount;

    /**
     * 学生数量
     */
    private long studentAmount;

    /**
     * 学校地区
     */
    private String schoolArea;
    /**
     * 地区名
     */
    private String areaName;

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolMaster() {
        return schoolMaster;
    }

    public void setSchoolMaster(String schoolMaster) {
        this.schoolMaster = schoolMaster;
    }

    public long getTeacherAmount() {
        return teacherAmount;
    }

    public void setTeacherAmount(long teacherAmount) {
        this.teacherAmount = teacherAmount;
    }

    public long getStudentAmount() {
        return studentAmount;
    }

    public void setStudentAmount(long studentAmount) {
        this.studentAmount = studentAmount;
    }

    public String getSchoolArea() {
        return schoolArea;
    }

    public void setSchoolArea(String schoolArea) {
        this.schoolArea = schoolArea;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}
