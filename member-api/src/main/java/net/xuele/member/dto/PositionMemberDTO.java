package net.xuele.member.dto;

import java.io.Serializable;

/**
 * Created by kaike.du on 2015/7/1 0001.
 */
public class PositionMemberDTO implements Serializable {
    private static final long serialVersionUID = 2770723706464424785L;
    /**
     * 职务ID
     */
    private String positionId;
    /**
     * 职务名称
     */
    private String name;

    /**
     * 职务说明
     */
    private String description;

    /**
     * 类型（类型0表示系统职务，1表示自定义职务）
     */
    private Integer positionType;

    /**
     * 职务人数
     */
    private Integer total;

    /**
     * 获取 [M_POSITION] 的属性 职务ID
     */
    public String getPositionId() {
        return positionId;
    }

    /**
     * 设置[M_POSITION]的属性职务ID
     */
    public void setPositionId(String positionId) {
        this.positionId = positionId == null ? null : positionId.trim();
    }

    /**
     * 获取 [M_POSITION] 的属性 职务名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置[M_POSITION]的属性职务名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取 [M_POSITION] 的属性 职务说明
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置[M_POSITION]的属性职务说明
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     * 获取 [M_POSITION] 的属性 类型（类型0表示系统职务，1表示自定义职务）
     */
    public Integer getPositionType() {
        return positionType;
    }

    /**
     * 设置[M_POSITION]的属性类型（类型0表示系统职务，1表示自定义职务）
     */
    public void setPositionType(Integer positionType) {
        this.positionType = positionType;
    }

    /**
     * 获取 [M_POSITION] 的属性 职务人数
     */
    public Integer getTotal() {
        return total;
    }

    /**
     * 设置[M_POSITION]的属性职务人数
     */
    public void setTotal(Integer total) {
        this.total = total;
    }
}