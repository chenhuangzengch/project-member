package net.xuele.member.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by guochun.shen on 2015/10/20 0020.
 */
public class TeacherClassLogDTO implements Serializable{
    private static final long serialVersionUID = -2752816497476883683L;
    /**
     * 用户ID
     */
    private List<String> userIdList;

    /**
     * 班级id
     */
    private String  classId;
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
