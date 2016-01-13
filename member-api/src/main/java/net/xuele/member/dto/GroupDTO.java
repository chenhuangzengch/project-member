package net.xuele.member.dto;

import java.io.Serializable;

/**
 * Created by zhongjian.xu on 2015/7/15 0015.
 */
public class GroupDTO implements Serializable {
    private static final long serialVersionUID = 7003300581893124104L;
    /**
     * 组ID
     */
    private String groupid;
    /**
     * 组名称
     */
    private String groupname;

    public GroupDTO() {
    }

    public GroupDTO(String groupid, String groupname) {
        this.groupid = groupid;
        this.groupname = groupname;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }
}
