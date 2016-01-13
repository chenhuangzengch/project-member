package net.xuele.member.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhongjian.xu on 2015/9/29 0029.
 */
public class ProvincialAreaDTO implements Serializable{
    private static final long serialVersionUID = -2716208689942133029L;
    /**
     * 区划编号
     */
    private String code;
    /**
     * 区划名称
     */
    private String name;
    /**
     * 区域
     */
    private List<AreaDTO> area;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AreaDTO> getArea() {
        return area;
    }

    public void setArea(List<AreaDTO> area) {
        this.area = area;
    }
}
