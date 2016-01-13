package net.xuele.member.persist;

import net.xuele.member.domain.MEducation;

import java.util.List;

public interface MEducationMapper {
    int deleteByPrimaryKey(String userId);

    //int insert(MEducation record);
    /**
     * APP接口：根据用户ID获取该用户所在的教育机构
     */
    MEducation selectByPrimaryKey(String userId);

    List<MEducation> selectAll();

    int updateByPrimaryKey(MEducation record);
}