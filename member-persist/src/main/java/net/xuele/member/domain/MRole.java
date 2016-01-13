package net.xuele.member.domain;

public class MRole {
    private String roleId;

    private String roleName;

    /**
     * ״̬
     */
    private Integer status;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    /**
     * 获取[M_ROLE]的属性״̬
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置[M_ROLE]的属性״̬
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
}