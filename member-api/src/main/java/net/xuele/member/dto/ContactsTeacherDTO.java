package net.xuele.member.dto;

import net.xuele.member.constant.UserConstants;

import java.io.Serializable;

/**
 * Created by guochun.shen on 2015/9/17 0017.
 */
public class ContactsTeacherDTO implements Serializable {
    private static final long serialVersionUID = -2097994665440102609L;
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
     * 任教科目
     */
    private String subject;

    /**
     * 职务
     */
    private String duty;

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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }
}
