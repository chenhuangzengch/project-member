package net.xuele.member.dto;

import net.xuele.member.constant.UserConstants;

import java.io.Serializable;

/**
 * Created by zhongjian.xu on 2015/7/13 0013.
 */
public class RoleInfoDTO implements Serializable{
    private static final long serialVersionUID = 4907222555085226512L;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 用户名称
     */
    private String realName;
    /**
     * 身份ID
     */
    private String identityId;
    /**
     * 身份说明
     */
    private String identityDescription;
    /**
     * 相关名称
     */
    private String relativename;
    /**
     * 相关ID
     */
    private String relativeid;
    /**
     * 图标
     */
    private String icon;
    /**
     * 职务ID
     */
    private String positionId;
    /**
     * 职务名称
     */
    private String positionName;

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon == null ? UserConstants.ICON_DEFAULT : icon.trim();
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

    public String getIdentityId() {
        return identityId;
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

    public String getIdentityDescription() {
        return identityDescription;
    }

    public void setIdentityDescription(String identityDescription) {
        this.identityDescription = identityDescription;
    }

    public String getRelativename() {
        return relativename;
    }

    public void setRelativename(String relativename) {
        this.relativename = relativename;
    }

    public String getRelativeid() {
        return relativeid;
    }

    public void setRelativeid(String relativeid) {
        this.relativeid = relativeid;
    }
}
