package net.xuele.member.dto;

import java.io.Serializable;

/**
 * Created by guochun.shen on 2015/5/29 0029.
 */
public class RoleDTO implements Serializable {

    private static final long serialVersionUID = -8209845951106058720L;
    private String roleId;

    private String roleName;

    private Integer status;

    public String getRoleId() {
        return roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
