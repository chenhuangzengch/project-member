package net.xuele.member.web.wrapper;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * Created by guochun.shen on 2015/7/31 0031.
 */
public class FamilyMembersWrapper {
    /**
     * 关系id
     */
    private Long id;
    /**
     * 家长id
     */
    private String userId;
    /**
     * 成员编号
     */
    private String memberId;
    /**
     * 邀请时间
     */
    private Date addTime;
    /**
     * 成员称谓
     */
    private String memberName;
    /**
     * 成员真实姓名
     */
    private String memberRealName;
    /**
     * 成员状态
     */
    private Integer memberStatus;
    /**
     * 成员头像
     */
    private String memberIcon;
    /**
     * 成员手机
     */
    private String memberMobile;
    /**
     * 初始化密码
     */
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberRealName() {
        return memberRealName;
    }

    public void setMemberRealName(String memberRealName) {
        this.memberRealName = memberRealName;
    }

    public Integer getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(Integer memberStatus) {
        this.memberStatus = memberStatus;
    }

    public String getMemberIcon() {
        return memberIcon;
    }

    public void setMemberIcon(String memberIcon) {
        this.memberIcon = memberIcon;
    }

    public String getMemberMobile() {
        return memberMobile;
    }

    public void setMemberMobile(String memberMobile) {
        this.memberMobile = memberMobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
