package net.xuele.member.domain;

/**
 * Created by zhongjian.xu on 2015/6/19 0019.
 */
public class MClassStudent{
    /**
     * 班级ID
     */
    private String classId;
    /**
     * 班级别名
     */
    private String aliasName;

    /**
     * 班级名称
     */
    private String name;

    /**
     * 班主任ID
     */
    private String chargeId;

    /**
     * 班主任名称
     */
    private String chargeName;
    /**
     * 班级人数
     */
    private int studentNumber;
    /**
     * 学界
     */
    private int year;
    /**
     * 班主任图标
     */
    private String icon;

    /**
     * 班级图标
     */
    private String mImage;
    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(int studentNumber) {
        this.studentNumber = studentNumber;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }
}
