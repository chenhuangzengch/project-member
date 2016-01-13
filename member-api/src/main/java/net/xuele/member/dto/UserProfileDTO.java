package net.xuele.member.dto;

/**
 * Created by ZhengTao on 2015/6/26 0026.
 */
public class UserProfileDTO implements java.io.Serializable {

    private static final long serialVersionUID = -3540224913059606477L;
    private UserDTO user;
    private Object profile;

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public Object getProfile() {
        return profile;
    }

    public void setProfile(Object profile) {
        this.profile = profile;
    }

}
