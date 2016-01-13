package net.xuele.member.web.wrapper;

import net.xuele.member.dto.PositionDTO;
import org.springframework.beans.BeanUtils;

/**
 * zhihuan.cai 新建于 2015/6/21 0021.
 */
public class PositionWrapper {


    /**
     * id
     */
    private String positionId;

    /**
     * 职务名称
     */
    private String name;

    /**
     * 职务描述
     */
    private String description;


    /**
     * 职务下的总人数
     */
    private int totals;


    /**
     * 职务类型，1：系统职务，不允许删除，前台直接禁用修改和删除方法。
     */
    private String positionType;

    /**
     * 所属学校ID
     */
    private String schoolId;

    public PositionWrapper(){}

    public PositionWrapper(PositionDTO result) {
        BeanUtils.copyProperties(result,this);
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTotals() {
        return totals;
    }

    public void setTotals(int totals) {
        this.totals = totals;
    }

    public String getPositionType() {
        return positionType;
    }

    public void setPositionType(String positionType) {
        this.positionType = positionType;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }


}
