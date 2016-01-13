package net.xuele.member.persist;


import net.xuele.member.domain.MLoginStatistics;
import org.apache.ibatis.annotations.Param;

public interface MLoginStatisticsMapper {

    int insert(MLoginStatistics record);

    MLoginStatistics selectByPrimaryKey(@Param("userId") String userId, @Param("schoolId") String schoolId);

    int updateByPrimaryKey(MLoginStatistics record);

    int getCountByPrimaryKey(@Param("userId") String userId, @Param("schoolId") String schoolId);
}