package net.xuele.member.persist;

import net.xuele.member.domain.CtBook;
import net.xuele.member.domain.DBooks;
import net.xuele.member.domain.MSchoolBook;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

public interface MSchoolBookMapper {
    int deleteByPrimaryKey(@Param("schoolId") String schoolId,
                           @Param("bookId") String bookId);

    int insert(MSchoolBook record);

    /**
     * WEB平台、APP接口：根据bookId和schoolId获取学校课本关联表信息
     */
    MSchoolBook selectByPrimaryKey(@Param("schoolId") String schoolId,
                                   @Param("bookId") String bookId);

    int updateBook(@Param("oldBookId") String oldBookId,
                   @Param("newBookId") String newBookId,
                   @Param("schoolId") String schoolId);

    /**
     * 获取同步课堂所有教材
     */
    List<DBooks> queryAllSynBook();

    /**
     * 保存该学校的同步课堂教材
     *
     * @param schoolBookList
     */
    int saveSyncBook(@Param("schoolBookList") List<MSchoolBook> schoolBookList);

    /**
     * 保存该学校的教材
     *
     * @param schoolBookList
     */
    int saveSchoolBook(@Param("schoolBookList") List<MSchoolBook> schoolBookList);

    /**
     * 删除该学校的同步课堂教材
     */
    void delSyncBookBySchoolId(String schoolId);

    /**
     * 根据学校ID获取该学校设置的同步课堂教材
     */
    List<DBooks> querySchoolSynBook(String schoolId);

    /**
     * APP接口：根据年级和科目id获取某学校的某同步课堂课本,根据当前时间判断上下学期
     */
    DBooks getDBook(@Param("gradeCode") Integer gradeCode,
                    @Param("subjectId") Integer subjectId,
                    @Param("schoolId") String schoolId,
                    @Param("semester") Integer semester);

    /**
     * 根据年级号和科目id获取同步课堂信息
     */
    DBooks getByGradeNumAndSubjectId(@Param("gradeCode") Integer gradeCode,
                                     @Param("subjectId") Integer subjectId);

    /**
     * 根据年级号,科目id和学校id获取科目信息
     */
    List<CtBook> getByGradeAndSubjectId(@Param("gradeCode") Integer gradeCode,
                                        @Param("subjectId") String subjectId, @Param("schoolId") String schoolId);

    /**
     * 设置m_school_book的教辅id
     */
    int setExtraBookId(@Param("bookId") String bookId,
                       @Param("extraBookId") String extraBookId,
                       @Param("schoolId") String schoolId);

    List<DBooks> queryDBooks(@Param("bookIds") List<String> bookIds);

    /**
     * APP接口：查询某学校，某些年级，某些科目，某些学期的同步课堂教材信息
     */
    List<DBooks> queryDBooksByList(@Param("schoolId") String schoolId,
                                   @Param("gradeCodeList") ArrayList<Integer> gradeCode,
                                   @Param("subjectIdList") ArrayList<String> subjectId,
                                   @Param("semesterList") ArrayList<Integer> semester);

    /**
     * 查询 默认指定同步课堂的课本
     *
     * @return 默认指定同步课堂的课本
     */
    List<DBooks> queryDefaultDBooks();

    /**
     * 查询某些年级，某些科目，某些学期的同步课堂教材信息
     *
     * @param gradeCodeList 年级列表
     * @param subjectIdList 科目列表
     * @param semesterList  学期列表
     * @return 教材信息
     */
    List<DBooks> queryDefaultBookList(@Param("gradeCodeList") ArrayList<Integer> gradeCodeList,
                                      @Param("subjectIdList") ArrayList<String> subjectIdList,
                                      @Param("semesterList") ArrayList<Integer> semesterList);
}