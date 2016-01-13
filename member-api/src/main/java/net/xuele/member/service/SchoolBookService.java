package net.xuele.member.service;

import net.xuele.member.dto.CtBookDTO;
import net.xuele.member.dto.SchoolBookDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaike.du on 2015/7/16 0016.
 */
public interface SchoolBookService {
    /**
     * 通过学校id和课本id设置教材
     *
     * @param bookId   课本id
     * @param schoolId 学校id
     * @return 设置的教材数据
     */
    SchoolBookDTO saveBook(String bookId, String schoolId);

    /**
     * 通过学校id和课本id删除教材
     *
     * @param bookId   课本id
     * @param schoolId 学校id
     * @return 删除的教材数据
     */
    SchoolBookDTO deleteBook(String bookId, String schoolId);

    /**
     * 保存某学校的同步课堂教材
     *
     * @param bookIds  课本id列表
     * @param schoolId 学校id
     */
    int saveSyncBook(ArrayList<String> bookIds, String schoolId);

    /**
     * 通过学校id原先的课本id和新的课本id编辑教材
     *
     * @param oldBookId 原先的课本id
     * @param newBookId 新的课本id
     * @param schoolId  学校id
     * @return 编辑以后的教材数据
     */
    SchoolBookDTO updateBook(String oldBookId, String newBookId, String schoolId);

    /**
     * 获取所有同步课堂所有教材，不按学校分
     *
     * @return 同步课堂教材
     */
    List<CtBookDTO> queryAllSynBook();

    /**
     * 根据学校ID获取该学校设置的同步课堂教材
     *
     * @param schoolId 学校ID
     * @return 同步课堂教材
     */
    List<CtBookDTO> querySchoolSynBook(String schoolId);

    /**
     * 获取默认同步课堂教材
     *
     * @return 同步课堂教材
     */
    List<CtBookDTO> queryDefaultDBooks();

    /**
     * 根据学校ID获取该学校设置的教材
     *
     * @param schoolId 学校ID
     * @return 学校设置的教材
     */
    List<CtBookDTO> querySchoolBook(String schoolId);

    /**
     * APP接口：根据年级和科目id获取某学校的某课本,根据当前时间判断上下学期
     *
     * @param gradeCode 年级：1,2,3
     * @param subjectId 科目id
     * @param schoolId  学校Id
     * @return {@link CtBookDTO}
     */
    CtBookDTO getCtBook(Integer gradeCode, String subjectId, String schoolId);

    /**
     * APP接口：查询某学校，某些年级，某些科目，某些学期的教材信息
     *
     * @param schoolId  学校id
     * @param gradeCode 年级id列表
     * @param subjectId 科目id列表
     * @param semester  学期列表
     * @return 教材信息
     */
    List<CtBookDTO> queryCtBook(String schoolId, ArrayList<Integer> gradeCode, ArrayList<String> subjectId, ArrayList<Integer> semester);

    /**
     * APP接口：根据年级和科目id获取某学校的某同步课堂课本,根据当前时间判断上下学期
     *
     * @param gradeCode 年级：1,2,3
     * @param subjectId 科目id
     * @param schoolId  学校Id
     * @return {@link CtBookDTO}
     */
    CtBookDTO getDBook(Integer gradeCode, Integer subjectId, String schoolId);

    /**
     * APP接口：查询某学校，某些年级，某些科目，某些学期的同步课堂教材信息
     *
     * @param schoolId  学校id
     * @param gradeCode 年级id列表
     * @param subjectId 科目id列表
     * @param semester  学期列表
     * @return 同步课堂教材信息
     */
    List<CtBookDTO> queryDBook(String schoolId, ArrayList<Integer> gradeCode, ArrayList<String> subjectId, ArrayList<Integer> semester);

    /**
     * 根据学校ID初始化设置学校的教材
     *
     * @param schoolId 学校ID
     * @return 学校设置的教材
     */
    List<CtBookDTO> saveSchoolBook(String schoolId);

    void updateExtraBookId(String bookId, String extraBookId, String schoolId);

    /**
     * WEB平台、APP接口：根据bookId和schoolId获取学校课本关联表信息
     *
     * @param bookId   课本id
     * @param schoolId 学校id
     * @return 学校课本关联表信息
     */
    SchoolBookDTO getByBookIdAndSchoolId(String bookId, String schoolId);

    /**
     * 查询老师的积分宝教材信息
     *
     * @param userId   用户id
     * @param schoolId 学校id
     */
    List<CtBookDTO> queryTeacherSyncBook(String userId,String schoolId);
}