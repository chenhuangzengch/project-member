package net.xuele.member.dto;

/**
 * Created by ZhengTao on 2015/6/1 0001.
 */
public class UserRoleDTO implements java.io.Serializable{
    private static final long serialVersionUID = -6974024606050338670L;
    private String userId;

    private String roleId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
    }
}
