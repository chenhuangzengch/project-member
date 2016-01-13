package net.xuele.member.dto;


import net.xuele.common.page.PageRequest;

import java.io.Serializable;

/**
 * @author ZhengTao on 2015/6/2 0002.
 */
public class UserPageRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = -2765242300170522287L;
    private String realName;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
