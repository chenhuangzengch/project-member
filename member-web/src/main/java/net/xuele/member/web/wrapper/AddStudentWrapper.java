package net.xuele.member.web.wrapper;

/**
 * Created by Administrator on 2015/6/26 0026.
 */
public class AddStudentWrapper {
    private String userIds;
    private String classId;

    public AddStudentWrapper(String userIds, String classId) {
        this.userIds = userIds;
        this.classId = classId;
    }

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }
}
