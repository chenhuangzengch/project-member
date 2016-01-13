package net.xuele.member.domain;

import java.util.List;

/**
 * @author wuxh
 * @date 2015/8/18 0018
 * @project member
 * @package net.xuele.member.domain.MenuRoleResource
 */
public class MenuRoleResource {
    private String resourceId;
    private String name;
    private String url;
    private String pattern;
    private Integer level;

    private List<MenuRoleResource> nextLevel;

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

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public List<MenuRoleResource> getNextLevel() {
        return nextLevel;
    }

    public void setNextLevel(List<MenuRoleResource> nextLevel) {
        this.nextLevel = nextLevel;
    }
}
