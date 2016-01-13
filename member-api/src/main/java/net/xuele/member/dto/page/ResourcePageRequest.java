package net.xuele.member.dto.page;

import net.xuele.common.page.PageRequest;

import java.io.Serializable;

/**
 * Created by xuzj on 2015/6/2 0002.
 */
public class ResourcePageRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = -4329220882423685919L;

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

    public Integer getrType() {
        return rType;
    }

    public void setrType(Integer rType) {
        this.rType = rType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
