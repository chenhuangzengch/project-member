package net.xuele.member.dto;

/**
 * Created by Administrator on 2015/9/4 0004.
 */
public class EmailStatusDTO implements java.io.Serializable {
    private String userId;
    private String email;
    /**
     * 1、未绑定，2、已绑定，3、待验证
     */
    private Integer status;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
