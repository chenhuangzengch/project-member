package net.xuele.member.persist;

import net.xuele.member.domain.DAreas;

import java.util.List;

public interface DAreasMapper {
    //int deleteByPrimaryKey(String code);
    //
    //int insert(DAreas record);


    //
    //List<DAreas> selectAll();
    //
    //int updateByPrimaryKey(DAreas record);
    /**
     * 根据区域id获取区域信息
     */
    DAreas selectByPrimaryKey(String areaId);

    /**
     * APP接口：根据当前地区编号获取下一级教育机构地区（确保areaId不为null）
     */
    List<DAreas> getOrgEducation(String areaId);


    /**
     * 根据区县ID获取街道信息
     */
    List<DAreas> getStreetArea(String areaId);
}