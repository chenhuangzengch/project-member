package net.xuele.member.dto;

import java.io.Serializable;

/**
 * Created by zhongjian.xu on 2015/11/20 0020.
 */
public class UserAddDTO implements Serializable {
    private static final long serialVersionUID = 7459463182163652437L;
    /**
     * 性别（0：男；1：女）
     */
    private Integer sex;
    /**
     * 姓名
     */
    private String realName;
    /**
     * 学校id
     */
    private String schoolId;
    /**
     * 年级号[1,12]
     */
    private int gradeNum;
    /**
     * 班级号[1-999]
     */
    private int classNum;
    /**
     * 类型（1：老师；2：学生）
     */
    private Integer type;

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public int getGradeNum() {
        return gradeNum;
    }

    public void setGradeNum(int gradeNum) {
        this.gradeNum = gradeNum;
    }

    public int getClassNum() {
        return classNum;
    }

    public void setClassNum(int classNum) {
        this.classNum = classNum;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
