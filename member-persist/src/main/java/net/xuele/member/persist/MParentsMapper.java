package net.xuele.member.persist;

import net.xuele.member.domain.MParents;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MParentsMapper {
    int deleteByPrimaryKey(String userId);

    int insert(MParents record);

    MParents selectByPrimaryKey(@Param("userId") String userId, @Param("schoolId") String schoolId);

    List<MParents> selectAll();

    int updateByPrimaryKey(MParents record);

}