package net.xuele.member.dto;

import net.xuele.member.constant.UserConstants;

/**
 * Created by ZhengTao on 2015/7/1 0001.
 */
public class TeacherLoginDTO implements java.io.Serializable {
    private static final long serialVersionUID = 5277863903892636214L;
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
     * 最后一次登录时间
     */
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon == null ? UserConstants.ICON_DEFAULT : icon.trim();
    }
}
