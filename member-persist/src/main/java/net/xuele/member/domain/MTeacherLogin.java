package net.xuele.member.domain;

import java.util.Date;

/**
 * Created by ZhengTao on 2015/7/2 0002.
 */
public class MTeacherLogin {
    /**
     * 用户ID（学乐号）
     */
    private String userId;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 主授科目
     */
    private String subjectName;

    /**
     * 职务名称
     */
    private String positionName;

    /**
     * 头像
     */
    private String icon;

    /**
     * 时间
     */
    private Date date;

    private String classId;

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

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }
}
