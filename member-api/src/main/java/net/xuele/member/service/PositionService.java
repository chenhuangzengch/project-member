package net.xuele.member.service;

import net.xuele.member.dto.PositionDTO;
import net.xuele.member.dto.PositionMemberDTO;
import net.xuele.member.dto.TeacherDTO;

import java.util.List;

/**
 * Created by kaike.du on 2015/6/25 0025.
 */
public interface PositionService {
    /**
     * 添加职务.
     *
     * @param positionDTO 职务数据
     * @return 添加的职务的数据
     */
    PositionDTO savePosition(PositionDTO positionDTO);

    /**
     * 删除职务.
     *
     * @param positionDTO 职务数据
     * @return 删除的职务的数据
     */
    PositionDTO deletePosition(PositionDTO positionDTO);

    /**
     * 删除职务时编辑教师.
     *
     * @param positionDTO 职务数据
     * @return 教师编辑后的数据
     */
    TeacherDTO updateTeacher(PositionDTO positionDTO);

    /**
     * 编辑职务.
     *
     * @param positionDTO 职务数据
     * @return 编辑以后的职务数据
     */
    PositionDTO updatePosition(PositionDTO positionDTO);

    /**
     * 职务管理初始查询将查询结果显示在页面
     *
     * @param schoolId 学校id
     * @return 显示在页面上的职务数据列表
     */
    List<PositionMemberDTO> queryPositionMember(String schoolId);

    /**
     * 职务列表，不显示校长职务
     *
     * @return 职务信息列表
     */
    List<PositionDTO> queryPositionList(String schoolId);


}
