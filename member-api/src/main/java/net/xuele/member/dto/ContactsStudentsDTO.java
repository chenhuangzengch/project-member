package net.xuele.member.dto;

import net.xuele.member.constant.UserConstants;

import java.io.Serializable;
import java.util.List;

/**
 * Created by guochun.shen on 2015/9/21 0021.
 */
public class ContactsStudentsDTO implements Serializable{
    private static final long serialVersionUID = 3226997330960606707L;
    /**
     * 用户id
     */
    private String id;

    /**
     * 用户名称
     */
    private String name;

    /**
     * 头像地址
     */
    private String icon;


    /**
     * 对应家长或者老师名称
     */
    private List<ContactsStudentsLeafDTO> leaf;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon == null || icon.length() == 0 ? UserConstants.ICON_DEFAULT : icon.trim();
    }

    public void setIcon(String icon) {
        this.icon = icon == null ? null : icon.trim();
    }

    public List<ContactsStudentsLeafDTO> getLeaf() {
        return leaf;
    }

    public void setLeaf(List<ContactsStudentsLeafDTO> leaf) {
        this.leaf = leaf;
    }
}
