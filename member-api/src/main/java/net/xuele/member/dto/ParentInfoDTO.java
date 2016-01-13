package net.xuele.member.dto;

import net.xuele.member.constant.UserConstants;

import java.io.Serializable;

/**
 * Created by guochun.shen on 2015/8/6 0006.
 */
public class ParentInfoDTO implements Serializable{

    private static final long serialVersionUID = -4587012426501021058L;

    /**
     * 登录号
     */
    private String loginId;
    /**
     * 登录类型
     */
    private int loginType;
    /**
     * 默认学乐号
     */
    private String userId;
    /**
     * 身份类型,引用d_identity
     */
    private String identityId;
    /**
     * 学生学乐号
     */
    private String targetUserId;
    /**
     * 目标用户名称
     */
    private String targetUserName;
    /**
     * 用户名称
     */
    private String userName;

    /**
     * 头像
     */
    private String icon;

    /**
     * 称谓
     */
    private String memberName;

    public int getLoginType() {
        return loginType;
    }

    public void setLoginType(int loginType) {
        this.loginType = loginType;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    /**
     * 获取 [M_USERS] 的属性 身份类型,引用d_identity
     */
    public String getIdentityId() {
        return identityId;
    }

    /**
     * 设置[M_USERS]的属性身份类型,引用d_identity
     */
    public void setIdentityId(String identityId) {
        this.identityId = identityId == null ? null : identityId.trim();
    }

    public String getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(String targetUserId) {
        this.targetUserId = targetUserId;
    }

    public String getIcon() {
        return icon == null || icon.length() == 0 ? UserConstants.ICON_DEFAULT : icon.trim();
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

    public String getTargetUserName() {
        return targetUserName;
    }

    public void setTargetUserName(String targetUserName) {
        this.targetUserName = targetUserName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
