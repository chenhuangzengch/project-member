package net.xuele.member.web.wrapper;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 学生对象ajax返回页面封装
 * zhihuan.cai 新建于 2015/6/22 0022.
 */
public class StudentWrapper {


    private String userId;


    private String realName;

    private String classAliasName;

    private String classId;
    private String icon;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastLoginDate;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getClassAliasName() {
        return classAliasName;
    }

    public void setClassAliasName(String classAliasName) {
        this.classAliasName = classAliasName;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
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
}
