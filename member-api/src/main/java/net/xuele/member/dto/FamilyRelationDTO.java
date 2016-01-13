package net.xuele.member.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by guochun.shen on 2015/7/18 0018.
 */
public class FamilyRelationDTO implements Serializable {
    private static final long serialVersionUID = -8418602435003666939L;
    /**
     * 序列号
     */
    private Long id;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 目标用户ID
     */
    private String targetUserId;
    /**
     * 目标用户名称
     */
    private String targetUserName;

    /**
     * 用户名称
     */
    private String userName;
    /**
     * 成员编号
     */
    private String memberId;

    /**
     * 成员称谓
     */
    private String memberName;
    /**
     * 邀请方式（0：站内邀请；1：未绑定手机号邀请）
     */
    private Integer type;
    /**
     * 新增时间
     */
    private Date addTime;
    /**
     * 状态（0：已解除关系，1：成功；2：已拒绝；3：站内正在邀请；4：站外邀请手机验证码未填；5：站外邀请家长未初始化;）
     */
    private Integer status;
    /**
     * 学校ID
     */
    private String schoolId;

    /**
     * 邀请发起人userId
     */
    private String createUserId;
    /**
     * 获取 [M_FAMILLY_RELATION] 的属性 序列号
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置[M_FAMILLY_RELATION]的属性序列号
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取 [M_FAMILLY_RELATION] 的属性 用户ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置[M_FAMILLY_RELATION]的属性用户ID
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * 获取 [M_FAMILLY_RELATION] 的属性 目标用户ID
     */
    public String getTargetUserId() {
        return targetUserId;
    }

    /**
     * 设置[M_FAMILLY_RELATION]的属性目标用户ID
     */
    public void setTargetUserId(String targetUserId) {
        this.targetUserId = targetUserId == null ? null : targetUserId.trim();
    }

    /**
     * 获取 [M_FAMILLY_RELATION] 的属性 成员编号
     */
    public String getMemberId() {
        return memberId;
    }

    /**
     * 设置[M_FAMILLY_RELATION]的属性成员编号
     */
    public void setMemberId(String memberId) {
        this.memberId = memberId == null ? null : memberId.trim();
    }

    /**
     * 获取 [M_FAMILLY_RELATION] 的属性 成员称谓
     */
    public String getMemberName() {
        return memberName;
    }

    /**
     * 设置[M_FAMILLY_RELATION]的属性成员称谓
     */
    public void setMemberName(String memberName) {
        this.memberName = memberName == null ? null : memberName.trim();
    }

    public String getTargetUserName() {
        return targetUserName;
    }

    public void setTargetUserName(String targetUserName) {
        this.targetUserName = targetUserName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }
}
