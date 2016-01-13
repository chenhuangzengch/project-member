package net.xuele.member.service.impl;


import net.xuele.common.utils.MemberAssert;
import net.xuele.member.constant.PositionConstants;
import net.xuele.member.context.MemberLogicContext;
import net.xuele.member.domain.MPosition;
import net.xuele.member.domain.MPositionMember;
import net.xuele.member.domain.MTeacher;
import net.xuele.member.dto.PositionDTO;
import net.xuele.member.dto.PositionMemberDTO;
import net.xuele.member.dto.TeacherDTO;
import net.xuele.member.persist.MPositionMapper;
import net.xuele.member.service.PositionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by kaike.du on 2015/6/25 0025.
 */

public class PositionServiceImpl implements PositionService {
    @Autowired
    private MPositionMapper positionMapper;


    /**
     * 职务管理添加职务
     *
     * @param positionDTO 职务数据
     * @return 职务信息
     */
    @Override
    public PositionDTO savePosition(PositionDTO positionDTO) {
        MemberAssert.isTrue(countBytes(positionDTO.getName())<=20,"职务名称限制10个中文字符",MemberLogicContext.ERROR_CODE_POSITION_NAME_IS_TOO_LONG);
        MPosition mPosition = positionMapper.selectName(positionDTO.getName(), positionDTO.getSchoolId());
        MemberAssert.isNull(mPosition, "学校已有该职务", MemberLogicContext.ERROR_CODE_SCHOOL_POSITION_NAME_IS_EXIST);
        positionDTO.setPositionId(generatePositionId());
        positionDTO.setDescription(PositionConstants.DESCRIPTION);
        positionDTO.setPositionType(PositionConstants.NEW_JOB_TYPE);
        positionMapper.insert(parseToMPosition(positionDTO));
        return positionDTO;
    }

    /**
     * 职务管理编辑职务
     *
     * @param positionDTO 职务数据
     * @return 职务信息
     */
    @Override
    public PositionDTO updatePosition(PositionDTO positionDTO) {
        MemberAssert.isTrue(countBytes(positionDTO.getName())<=20,"职务名称限制10个中文字符",MemberLogicContext.ERROR_CODE_POSITION_NAME_IS_TOO_LONG);
        MPosition mPosition = positionMapper.selectName(positionDTO.getName(), positionDTO.getSchoolId());
        if (mPosition != null) {
            if (!mPosition.getPositionId().equals(positionDTO.getPositionId()) )
                MemberAssert.isNull(mPosition, "学校已有该职务", MemberLogicContext.ERROR_CODE_SCHOOL_POSITION_NAME_IS_EXIST);
        }
        positionDTO.setDescription(PositionConstants.DESCRIPTION);
        positionDTO.setPositionType(PositionConstants.NEW_JOB_TYPE);
        positionMapper.updateByPrimaryKey(parseToMPosition(positionDTO));
        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setPositionId(positionDTO.getPositionId());
        teacherDTO.setPositionName(positionDTO.getName());
        positionMapper.updateInTeacher(parseToMTeacher(teacherDTO), positionDTO.getPositionId(), positionDTO.getSchoolId());
        return positionDTO;
    }



    /**
     * 职务列表，不显示校长职务
     *
     * @return 职务列表
     */
    @Override
    public List<PositionDTO> queryPositionList(String schoolId) {
        List<MPosition> positionList = positionMapper.selectPositionList(schoolId);
        List<PositionDTO> positionDTOList = new ArrayList<>();
        for (MPosition p : positionList) {
            PositionDTO positionDTO = new PositionDTO();
            BeanUtils.copyProperties(p, positionDTO);
            positionDTOList.add(positionDTO);
        }
        return positionDTOList;
    }

    /**
     * 职务管理删除职务
     *
     * @param positionDTO 职务数据
     * @return 职务信息
     */
    @Override
    public PositionDTO deletePosition(PositionDTO positionDTO) {
        updateTeacher(positionDTO);
        positionMapper.deleteByPrimaryKey(positionDTO.getPositionId());
        return positionDTO;
    }

    /**
     * 删除职务时将该学校该职务的人员的职务修改为教师
     */
    @Override
    public TeacherDTO updateTeacher(PositionDTO positionDTO) {
        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setPositionId(PositionConstants.TEACHER);
        teacherDTO.setPositionName(positionMapper.selectByPrimaryKey(PositionConstants.TEACHER).getName());
        positionMapper.updateInTeacher(parseToMTeacher(teacherDTO), positionDTO.getPositionId(), positionDTO.getSchoolId());
        return teacherDTO;
    }

    /**
     * 职务管理初始查询将查询结果显示在页面
     *
     * @param schoolId 学校id
     * @return 职务成员信息列表 {@link List<PositionMemberDTO>}
     */
    @Override
    public List<PositionMemberDTO> queryPositionMember(String schoolId) {
        List<MPositionMember> dbPositionList = positionMapper.selectTotal(schoolId);
        List<PositionMemberDTO> dbPositionDtoList = new ArrayList<>();
        for (MPositionMember ms : dbPositionList) {
            PositionMemberDTO csDTO = new PositionMemberDTO();
            BeanUtils.copyProperties(ms, csDTO);
            dbPositionDtoList.add(csDTO);
        }
        return dbPositionDtoList;
    }

    private String generatePositionId() {
        String positionId = UUID.randomUUID().toString();
        positionId = positionId.replaceAll("\\-", "");
        return positionId;
    }

    private MPosition parseToMPosition(PositionDTO positionDTO) {
        MPosition mPosition = new MPosition();
        BeanUtils.copyProperties(positionDTO, mPosition);
        return mPosition;
    }

    private MTeacher parseToMTeacher(TeacherDTO teacherDTO) {
        MTeacher mTeacher = new MTeacher();
        BeanUtils.copyProperties(teacherDTO, mTeacher);
        return mTeacher;
    }

    private Integer countBytes(String str){
        char[] c = str.toCharArray();
        int len = 0;
        for (int i = 0; i < c.length; i++) {
            len++;
            if (!isLetter(c[i])) {
                len++;
            }
        }
        return len;
    }
    private boolean isLetter(char c) {
        int k = 0x80;
        return c / k == 0 ? true : false;
    }




}

