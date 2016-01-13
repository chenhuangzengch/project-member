package net.xuele.member.domain;

public class MResources {
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
     * 资源类型
     */
    private Integer rType;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 父ID
     */
    private String pid;

    /**
     * 排序码
     */
    private String sort;

    /**
     * 层级
     */
    private Integer level;


    /**
     * 资源匹配值
     */
    private String pattern;


    /**
     * 获取[M_RESOURCES]的属性资源ID
     */
    public String getResourceId() {
        return resourceId;
    }

    /**
     * 设置[M_RESOURCES]的属性资源ID
     */
    public void setResourceId(String resourceId) {
        this.resourceId = resourceId == null ? null : resourceId.trim();
    }

    /**
     * 获取[M_RESOURCES]的属性资源名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置[M_RESOURCES]的属性资源名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取[M_RESOURCES]的属性资源地址
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置[M_RESOURCES]的属性资源地址
     */
    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    /**
     * 获取[M_RESOURCES]的属性资源类型
     */
    public Integer getrType() {
        return rType;
    }

    /**
     * 设置[M_RESOURCES]的属性资源类型
     */
    public void setrType(Integer rType) {
        this.rType = rType;
    }

    /**
     * 获取[M_RESOURCES]的属性状态
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置[M_RESOURCES]的属性状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取[M_RESOURCES]的属性父ID
     */
    public String getPid() {
        return pid;
    }

    /**
     * 设置[M_RESOURCES]的属性父ID
     */
    public void setPid(String pid) {
        this.pid = pid == null ? null : pid.trim();
    }

    /**
     * 获取[M_RESOURCES]的属性排序码
     */
    public String getSort() {
        return sort;
    }

    /**
     * 设置[M_RESOURCES]的属性排序码
     */
    public void setSort(String sort) {
        this.sort = sort == null ? null : sort.trim();
    }

    /**
     * 获取[M_RESOURCES]的属性层级
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * 设置[M_RESOURCES]的属性层级
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}