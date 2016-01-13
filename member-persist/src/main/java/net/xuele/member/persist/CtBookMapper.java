package net.xuele.member.persist;

import net.xuele.member.domain.CtBook;
import net.xuele.member.domain.CtBookInfo;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

public interface CtBookMapper {
    /**
     * 获取每个年级有哪些科目
     */
    List<CtBookInfo> queryGradeSubject();

    List<CtBook> selectBookByGradeCode(@Param("grade") String grade, @Param("subjectId") String subjectId);

    /**
     * WEB平台、APP接口：根据老师id获取老师所有的授课教材
     */
    List<CtBookInfo> getMaterialsByUserid(@Param(value = "userId") String userId, @Param(value = "schoolId") String schoolId, @Param(value = "subjectId") String subjectId);

    /**
     * APP接口：获取某科目的教材
     */
    List<CtBook> queryBookBySubjectId(@Param("subjectId") String subjectId);

    /**
     * 根据学校ID获取该学校设置的教材
     */
    List<CtBook> querySchoolBook(String schoolId);

    /**
     * APP接口：根据老师id获取老师授课教材的科目和年级
     */
    List<CtBookInfo> queryTeacherSubject(@Param(value = "userId") String userId, @Param(value = "schoolId") String schoolId);

    /**
     * APP接口：根据学校ID查学校对应的课本的科目和年级
     */
    List<CtBookInfo> querySchoolSubject(String schoolId);

    /**
     * APP接口：根据区域编号查这个区域下的所有学校对应的科目和年级
     */
    List<CtBookInfo> queryAreaSchoolSubject(String area);

    /**
     * APP接口：根据年级和科目id获取某学校的某课本,根据当前时间判断上下学期
     */
    CtBook getCtBook(@Param("gradeCode") Integer gradeCode,
                     @Param("subjectId") String subjectId,
                     @Param("schoolId") String schoolId,
                     @Param("semester") Integer semester);

    List<CtBook> queryCtBook(@Param("gradeCode") Integer gradeCode,
                             @Param("subjectId") String subjectId,
                             @Param("educationId") String educationId);

    /**
     * 根据bookId查数据
     */
    CtBook queryByPrimaryKey(String bookId);

    /**
     * 根据老师id获取老师的所有授课的科目
     *
     * @return 科目id，科目名称
     */
    List<CtBookInfo> queryTeacherSubjectByUserId(@Param("userId") String userId,
                                                 @Param("schoolId") String schoolId);

    List<CtBook> queryDefaultBook();

    /**
     * APP接口：查询某学校，某些年级，某些科目，某些学期的教材信息
     */
    List<CtBook> queryCtBookByList(@Param("schoolId") String schoolId,
                                   @Param("gradeCodeList") ArrayList<Integer> gradeCodeList,
                                   @Param("subjectIdList") ArrayList<String> subjectIdList,
                                   @Param("semesterList") ArrayList<Integer> semesterList);

    /**
     * 通讯录  根据学校查询所有科目
     *
     * @param schoolId
     * @return
     */
    List<CtBook> querySubjectGroupByList(@Param("schoolId") String schoolId);

    /**
     * APP接口：查询某些年级，某些科目，某些学期的默认教材信息
     */
    List<CtBook> queryDefaultBookList(@Param("gradeCodeList") ArrayList<Integer> gradeCodeList,
                                      @Param("subjectIdList") ArrayList<String> subjectIdList,
                                      @Param("semesterList") ArrayList<Integer> semesterList);
}