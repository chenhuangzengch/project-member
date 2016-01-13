package net.xuele.member.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by guochun.shen on 2015/7/31 0031.
 */
public class FamilyMembersDTO implements Serializable{
    private static final long serialVersionUID = 2969082993074773237L;
    /**
     * 关系id
     */
    private Long id;
    /**
     * 成员id
     */
    private String memberUserId;
    /**
     * 成员编号
     */
    private String memberId;
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
     * 新增时间
     */
    private Date addTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getMemberRealName() {
        return memberRealName;
    }

    public void setMemberRealName(String memberRealName) {
        this.memberRealName = memberRealName;
    }


    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberUserId() {
        return memberUserId;
    }

    public void setMemberUserId(String memberUserId) {
        this.memberUserId = memberUserId;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
}
