package net.xuele.member.dto;

import net.xuele.member.constant.UserConstants;

import java.io.Serializable;

/**
 * Created by guochun.shen on 2015/9/21 0021.
 */
public class ContactsStudentsLeafDTO implements Serializable{
    private static final long serialVersionUID = -1714987693255108282L;
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
    private String relation;

    private String subject;

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

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
