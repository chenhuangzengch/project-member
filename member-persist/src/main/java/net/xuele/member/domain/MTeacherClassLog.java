package net.xuele.member.domain;

import java.util.Date;

/**
 * Created by guochun.shen on 2015/10/20 0020.
 */
public class MTeacherClassLog {
    private String id;
    /**
     * 教师id
     */
    private String userId;
    /**
     * 班级id
     */
    private String classId;
    /**
     * 操作人用户id
     */
    private String operatorUserId;
    /**
     * 内容摘要
     */
    private String changeInfo;
    /**
     * 学校id
     */
    private String schoolId;
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getOperatorUserId() {
        return operatorUserId;
    }

    public void setOperatorUserId(String operatorUserId) {
        this.operatorUserId = operatorUserId;
    }

    public String getChangeInfo() {
        return changeInfo;
    }

    public void setChangeInfo(String changeInfo) {
        this.changeInfo = changeInfo;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
