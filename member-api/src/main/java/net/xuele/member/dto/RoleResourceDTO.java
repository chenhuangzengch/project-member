package net.xuele.member.dto;

import java.io.Serializable;

/**
 * Created by guochun.shen on 2015/5/29 0029.
 */
public class RoleResourceDTO implements Serializable{

    private static final long serialVersionUID = 4771680182241007916L;
    private String roleId;

    private String resourceId;

    public String getRoleId() {
        return roleId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }
}
