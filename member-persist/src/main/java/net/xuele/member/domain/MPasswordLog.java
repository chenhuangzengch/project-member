package net.xuele.member.domain;

import java.util.Date;

/**
 * Created by guochun.shen on 2015/10/9 0009.
 */
public class MPasswordLog {

    private String id;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 旧密码
     */
    private String oldPassword;
    /**
     * 新密码
     */
    private String newPassword;
    /**
     *密码变更人
     */
    private String operatorUserId;
    /**
     *学校Id
     */
    private String schoolId;
    /**
     *变更时间
     */
    private Date createTime;
    /**
     *变更信息
     */
    private String changeInfo;

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

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOperatorUserId() {
        return operatorUserId;
    }

    public void setOperatorUserId(String operatorUserId) {
        this.operatorUserId = operatorUserId;
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

    public String getChangeInfo() {
        return changeInfo;
    }

    public void setChangeInfo(String changeInfo) {
        this.changeInfo = changeInfo;
    }
}
