package net.xuele.member.domain;

/**
 * Created by zhongjian.xu on 2015/7/10 0010.
 */
public class MClassInfo {
    /**
     * 班级ID
     */
    private String classId;
    /**
     * 班级名称
     */
    private String className;
    /**
     * 班级别名
     */
    private String aliasName;
    /**
     * 学界
     */
    private int year;
    /**
     * 班级人数
     */
    private int studentCount;
    /**
     * 班级图标
     */
    private String mImage;
    /**
     * 学段说明(0,小学,1初中,2.高中)
     */
    private int section;
    /**
     * 班主任ID
     */
    private String chargeId;
    /**
     * 班主任名称
     */
    private String chargeName;
    /**
     *学校ID
     */
    private String schoolId;
    /**
     * 学校名称
     */
    private String schoolName;

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

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public String getChargeId() {
        return chargeId;
    }

    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
    }

    public String getChargeName() {
        return chargeName;
    }

    public void setChargeName(String chargeName) {
        this.chargeName = chargeName;
    }
}
