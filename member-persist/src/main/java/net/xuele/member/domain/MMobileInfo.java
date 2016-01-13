package net.xuele.member.domain;

import java.util.Date;

public class MMobileInfo {
    /**
     * UUID
     */
    private String id;
    /**
     *用户ID
     */
    private String userId;
    /**
     *学校编号
     */
    private String schoolId;
    /**
     *设备类型  1: Andriod; 2: IOS
     */
    private Integer mobileType;
    /**
     *设备描述
     */
    private String description;
    /**
     *创建时间
     */
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId == null ? null : schoolId.trim();
    }

    public Integer getMobileType() {
        return mobileType;
    }

    public void setMobileType(Integer mobileType) {
        this.mobileType = mobileType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}