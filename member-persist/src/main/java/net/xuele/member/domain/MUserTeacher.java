package net.xuele.member.domain;

/**
 * Created by zhongjian.xu on 2015/6/26 0026.
 */
public class MUserTeacher {
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 用户名称
     */
    private String realName;
    /**
     * 用户头像
     */
    private String icon;
    /**
     * 科目ID
     */
    private String subjectId;
    /**
     * 科目名称
     */
    private String subjectName;
    /**
     * 职务ID
     */
    private String positionId;
    /**
     * 职务名称
     */
    private String positionName;
    /**
     * 教材ID
     */
    private String bookId;
    /**
     * 教材名称
     */
    private String bookName;
    /**
     * 是否为管理层(0不是，1是）
     */
    private Integer isManager;

    public Integer getIsManager() {
        return isManager;
    }

    public void setIsManager(Integer isManager) {
        this.isManager = isManager;
    }

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

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }
}
