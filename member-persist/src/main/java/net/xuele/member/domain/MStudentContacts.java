package net.xuele.member.domain;

public class MStudentContacts {

    /**
     * 学乐号
     */
    private String userId;

    /**
     * 学生姓名
     */
    private String realName;

    /**
     * 图标ID
     */
    private String icon;

    /**
     * 家长或者老师称谓
     */
    private String memberName;
    /**
     * 学生ID
     */
    private String targetUserId;

    /**
     * 班级Id
     */
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


    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(String targetUserId) {
        this.targetUserId = targetUserId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }
}
