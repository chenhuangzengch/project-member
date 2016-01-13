package net.xuele.member.domain;

public class MLoginUser {

    private Long id;
    /**
     * 默认学乐号
     */
    private String userId;

    /**
     * 绑定学乐号
     */
    private String bindUserId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取 [M_LOGIN_USER] 的属性 默认学乐号
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置[M_LOGIN_USER]的属性默认学乐号
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * 获取 [M_LOGIN_USER] 的属性 绑定学乐号
     */
    public String getBindUserId() {
        return bindUserId;
    }

    /**
     * 设置[M_LOGIN_USER]的属性绑定学乐号
     */
    public void setBindUserId(String bindUserId) {
        this.bindUserId = bindUserId == null ? null : bindUserId.trim();
    }
}