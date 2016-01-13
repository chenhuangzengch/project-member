package net.xuele.member.dto;

import java.io.Serializable;

/**
 * Created by kaike.du on 2015/6/26 0026.
 */
public class ParentsDTO implements Serializable {
    private static final long serialVersionUID = -5031370679458842871L;
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 家长称谓
     */
    private String appellation;

    /**
     * 学校ID
     */
    private String schoolId;
    /**
     * 学校名字
     */
    private String schoolName;

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    /**
     * 获取 [M_PARENTS] 的属性 用户ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置[M_PARENTS]的属性用户ID
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * 获取 [M_PARENTS] 的属性 家长称谓
     */
    public String getAppellation() {
        return appellation;
    }

    /**
     * 设置[M_PARENTS]的属性家长称谓
     */
    public void setAppellation(String appellation) {
        this.appellation = appellation == null ? null : appellation.trim();
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }
}
