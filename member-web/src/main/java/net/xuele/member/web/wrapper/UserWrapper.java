package net.xuele.member.web.wrapper;

/**
 * Created by ZhengTao on 2015/6/23 0023.
 */
public class UserWrapper {
    /**
     * 用户userId
     */
    private String userId;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 用户头像完整地址,页面src属性
     */
    private String icon;
    /**
     * 授课ID
     */
    private String subjectId;
    /**
     * 授课名称
     */
    private String subjectName;
    /**
     * 1表示是班主任
     * 0表示不是班主任
     */
    private int charge;
    /**
     * 职务ID
     */
    private String positionId;
    /**
     * 职务名称
     */
    private String positionName;
    /**
     * 是否为管理层(0不是，1是）
     */
    private Integer isManager;

    public Integer getIsManager() {
        return isManager;
    }

    public void setIsManager(Integer isManager) {
        this.isManager = isManager;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getCharge() {
        return charge;
    }

    public void setCharge(int charge) {
        this.charge = charge;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }
}
