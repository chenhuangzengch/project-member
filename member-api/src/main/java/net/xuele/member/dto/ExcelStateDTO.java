package net.xuele.member.dto;

import java.io.Serializable;

/**
 * Created by zhongjian.xu on 2015/7/23 0023.
 */
public class ExcelStateDTO implements Serializable {
    private static final long serialVersionUID = -7160172810527629713L;
    /**
     * 用户名称
     */
    private String realName;
    /**
     * 身份 学生，老师...
     */
    private String identityDescription;
    /**
     * 年级1,2...
     */
    private String gradeNum;
    /**
     * 班级 1,2...
     */
    private String classNum;
    /**
     * 电话号码
     */
    private String mobile;
    /**
     * 性别
     */
    private String sex;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 错误信息
     */
    private String errorInfo;

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdentityDescription() {
        return identityDescription;
    }

    public void setIdentityDescription(String identityDescription) {
        this.identityDescription = identityDescription;
    }

    public String getGradeNum() {
        return gradeNum;
    }

    public void setGradeNum(String gradeNum) {
        this.gradeNum = gradeNum;
    }

    public String getClassNum() {
        return classNum;
    }

    public void setClassNum(String classNum) {
        this.classNum = classNum;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
