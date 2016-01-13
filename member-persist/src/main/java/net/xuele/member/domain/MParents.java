package net.xuele.member.domain;

public class MParents {
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