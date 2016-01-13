package net.xuele.member.dto;

import net.xuele.member.domain.MRole;

import java.util.List;

/**
 * zhihuan.cai 新建于 2015/7/13 0013.
 */
public class ResRolesDTO {


    /**
     * 资源ID
     */
    private String resourceId;

    /**
     * 资源名称
     */
    private String name;

    /**
     * 资源地址
     */
    private String url;


    /**
     * 资源匹配值
     */
    private String pattern;


    /**
     * 资源对应的角色列表
     */
    private List<MRole> roles;

    // get and sets

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<MRole> getRoles() {
        return roles;
    }

    public void setRoles(List<MRole> roles) {
        this.roles = roles;
    }


    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

}
