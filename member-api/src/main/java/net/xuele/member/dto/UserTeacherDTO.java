package net.xuele.member.dto;

import net.xuele.member.constant.UserConstants;

import java.io.Serializable;

/**
 * Created by zhongjian.xu on 2015/6/26 0026.
 */
public class UserTeacherDTO implements Serializable {

    private static final long serialVersionUID = 2472687255344282576L;
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
     * 是否是班主任 1：是 0：不是
     */
    private int charge;
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
        this.icon = icon == null ? UserConstants.ICON_DEFAULT : icon.trim();
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

    public int getCharge() {
        return charge;
    }

    public void setCharge(int charge) {
        this.charge = charge;
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
