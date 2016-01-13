package net.xuele.member.dto;

import java.io.Serializable;
import java.util.List;

public class LogPasswordDTO implements Serializable{
    private static final long serialVersionUID = -3515963716038855046L;
    /**
     * 用户ID
     */
    private List<String> userIdList;

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
     *变更信息
     */
    private String changeInfo;

    public List<String> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<String> userIdList) {
        this.userIdList = userIdList;
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

    public String getChangeInfo() {
        return changeInfo;
    }

    public void setChangeInfo(String changeInfo) {
        this.changeInfo = changeInfo;
    }
}
