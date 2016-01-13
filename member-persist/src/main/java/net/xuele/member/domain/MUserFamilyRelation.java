package net.xuele.member.domain;

public class MUserFamilyRelation {
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
     * 成员编号
     */
    private String memberId;

    /**
     * 成员称谓
     */
    private String memberName;

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
}