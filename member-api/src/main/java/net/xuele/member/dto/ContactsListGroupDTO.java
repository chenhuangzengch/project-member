package net.xuele.member.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by guochun.shen on 2015/9/17 0017.
 */
public class ContactsListGroupDTO implements Serializable{

    private static final long serialVersionUID = 8099835817701176039L;
    /**
     * 组Id
     */
    private String groupId;
    /**
     * 组名称
     */
    private String groupName;
    /**
     * 组成员数量
     */
    private Integer memberAmount;

    /**
     * 对应的url约定

     */
    private String requestFlag;
    /**
     * 组下级信息
     */
    List<ContactsListGroupDTO> children;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getMemberAmount() {
        return memberAmount;
    }

    public void setMemberAmount(Integer memberAmount) {
        this.memberAmount = memberAmount;
    }

    public List<ContactsListGroupDTO> getChildren() {
        return children;
    }

    public void setChildren(List<ContactsListGroupDTO> children) {
        this.children = children;
    }
    public String getRequestFlag() {
        return requestFlag;
    }

    public void setRequestFlag(String requestFlag) {
        this.requestFlag = requestFlag;
    }
}
