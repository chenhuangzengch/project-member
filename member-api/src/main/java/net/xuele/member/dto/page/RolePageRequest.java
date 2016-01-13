package net.xuele.member.dto.page;

import net.xuele.common.page.PageRequest;

import java.io.Serializable;

/**
 * Created by guochun.shen on 2015/6/2 0002.
 */
public class RolePageRequest extends PageRequest implements Serializable{


    private static final long serialVersionUID = 3012341211326424972L;


    private String roleId;

    private String roleName;

    private Integer status;

    public String getRoleId() {
        return roleId;
    }

    public Integer getStatus() {
        return status;
    }

    public String getRoleName() {
        return roleName;
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
