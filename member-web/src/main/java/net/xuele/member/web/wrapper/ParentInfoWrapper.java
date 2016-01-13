package net.xuele.member.web.wrapper;

/**
 * Created by guochun.shen on 2015/8/10 0010.
 */
public class ParentInfoWrapper {

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
     * 学生学乐号
     */
    private String targetUserId;

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

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public int getLoginType() {
        return loginType;
    }

    public void setLoginType(int loginType) {
        this.loginType = loginType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(String targetUserId) {
        this.targetUserId = targetUserId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
}
