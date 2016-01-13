package net.xuele.member.domain;

public class MUserIcon {
    /**
     * 自增长主键
     */
    private Long id;

    /**
     * 学乐号
     */
    private String userId;

    /**
     * 绑定学乐号
     */
    private String icon;

    /**
     * 1：在用；0：废弃
     */
    private Integer status;

    /**
     * 获取 [M_USER_ICON] 的属性 自增长主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置[M_USER_ICON]的属性自增长主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取 [M_USER_ICON] 的属性 学乐号
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置[M_USER_ICON]的属性学乐号
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * 获取 [M_USER_ICON] 的属性 绑定学乐号
     */
    public String getIcon() {
        return icon;
    }

    /**
     * 设置[M_USER_ICON]的属性绑定学乐号
     */
    public void setIcon(String icon) {
        this.icon = icon == null ? null : icon.trim();
    }

    /**
     * 获取 [M_USER_ICON] 的属性 1：在用；0：废弃
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置[M_USER_ICON]的属性1：在用；0：废弃
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
}