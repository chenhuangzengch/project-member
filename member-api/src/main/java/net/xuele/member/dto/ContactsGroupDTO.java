package net.xuele.member.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by guochun.shen on 2015/9/18 0018.
 */
public class ContactsGroupDTO implements Serializable{
    private static final long serialVersionUID = -642485948147573648L;
    /**
     * 返回通讯录左侧菜单
     */
    List<ContactsListGroupDTO> leftNode;

    public List<ContactsListGroupDTO> getLeftNode() {
        return leftNode;
    }

    public void setLeftNode(List<ContactsListGroupDTO> leftNode) {
        this.leftNode = leftNode;
    }
}
