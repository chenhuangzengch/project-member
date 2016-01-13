package net.xuele.member.persist;

import net.xuele.member.domain.DSummary;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DSummaryMapper {
    //int deleteByPrimaryKey(String summaryCode);
    //
    //int insert(DSummary record);
    //
    //DSummary selectByPrimaryKey(String summaryCode);
    //
    //int updateByPrimaryKey(DSummary record);
    /**
     * APP接口、WEB平台：查询所有科目列表，不根据学校区分
     */
    List<DSummary> selectAll();

    /**
     * 查询年级对应的科目列表
     * @param grade 年级
     * @return 科目列表
     */
    List<DSummary> selectGradeSubject(@Param(value = "grade") Integer grade);
    /**
     * 根据学校ID查询该学校的科目列表
     */
    List<DSummary> querySchoolSubject(String schoolId);

    /**
     * 根据用户ID查询科目列表
     * @param userId
     * @param schoolId
     * @return
     */
    List<DSummary> queryTeacherSubject(@Param(value = "userId") String userId,@Param(value = "schoolId") String schoolId);
}