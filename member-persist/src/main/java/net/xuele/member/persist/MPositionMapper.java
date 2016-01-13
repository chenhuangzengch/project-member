package net.xuele.member.persist;

import net.xuele.member.domain.MPosition;
import net.xuele.member.domain.MPositionMember;
import net.xuele.member.domain.MTeacher;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MPositionMapper {
    int deleteByPrimaryKey(String positionId);

    int insert(MPosition record);

    MPosition selectByPrimaryKey(String positionId);

    MPosition selectName(@Param("name") String name,@Param("schoolId") String schoolId);

    List<MPositionMember> selectTotal(String schoolId);

    int updateByPrimaryKey(MPosition record);

    int updateInTeacher(@Param("record") MTeacher record,@Param("positionId") String positionId,@Param("schoolId") String schoolId);

    /**
     * 职务列表，不显示校长职务
     * @return
     */
    List<MPosition> selectPositionList(String schoolId);
}