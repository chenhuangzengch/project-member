package net.xuele.member.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhongjian.xu on 2015/11/25 0025.
 */
public class MobileInfoDTO implements Serializable {
    private static final long serialVersionUID = 5593086350046882502L;
    /**
     * UUID
     */
    private String id;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 学校编号
     */
    private String schoolId;
    /**
     * 设备类型  1: Andriod; 2: IOS
     */
    private Integer mobileType;
    /**
     * 设备描述
     */
    private String description;
    /**
     * 创建时间
     */
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
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
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
