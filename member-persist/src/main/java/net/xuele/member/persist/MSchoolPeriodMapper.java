package net.xuele.member.persist;

import net.xuele.member.domain.MSchoolPeriod;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MSchoolPeriodMapper {
    int insert(MSchoolPeriod schoolPeriod);

    /**
     * APP接口、WEB平台：根据学校ID获取学制学段信息
     *
     * @param schoolId schoolId
     * @return 小学, 初中, 高中
     */
    List<MSchoolPeriod> selectBySchoolId(String schoolId);

    /**
     * 根据学校id删除
     *
     * @param schoolId
     * @return
     */
    int deleteBySchoolId(String schoolId);

    /**
     * 获取指定学段的学校信息
     */
    MSchoolPeriod getByschoolIdAndSection(@Param("schoolId") String schoolId, @Param("section") int section);

    int updateLengthById(MSchoolPeriod schoolPeriod);

    /**
     * 根据学校id和班级id列表删除学制学段
     */
    int deleteByPrimaryId(@Param("schoolId") String schoolId, @Param("id") String id);

    /**
     * 根据学制学段id和学校id获取该学段的信息
     */
    MSchoolPeriod getByPrimaryIdAndSchoolId(@Param("schoolPeriodId") String schoolPeriodId, @Param("schoolId") String schoolId);
}