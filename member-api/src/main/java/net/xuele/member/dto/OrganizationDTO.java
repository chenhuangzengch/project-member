package net.xuele.member.dto;

import java.io.Serializable;

/**
 * Created by guochun.shen on 2015/8/5 0005.
 */
public class OrganizationDTO implements Serializable{
    private static final long serialVersionUID = 3741330722044915679L;

    //机构ID
    private String organizationId;
    //机构名称
    private String organizationName;

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }
}
