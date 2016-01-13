package net.xuele.member.service;

import net.xuele.member.dto.BookDTO;
import net.xuele.member.dto.CtBookDTO;

import java.util.List;

/**
 * Created by ZhengTao on 2015/7/14 0014.
 */
public interface CtBookService {

    /**
     * 根据年级和科目获取云教学的课本
     *
     * @param gradeCode 年级code
     * @param subjectId 科目id
     * @return 云教学的课本
     */
    List<CtBookDTO> queryBookByGradeCode(String gradeCode, String subjectId);

    /**
     * 根据年级科目和学校获取云教学的课本(例如某学校一年级语文已经设置上学期教材，查询结果即为其相对应的下学期教材)
     *
     * @param gradeCode 年级code
     * @param subjectId 科目id
     * @param schoolId  学校id
     * @return 云教学的课本
     */
    List<CtBookDTO> queryBookByGradeCode(String gradeCode, String subjectId, String schoolId);


    /**
     * APP接口：获取某科目的教材
     *
     * @param subjectId 科目ID
     * @return 教材信息
     */
    List<BookDTO> queryBookBySubjectId(String subjectId);

    CtBookDTO getByBookId(String bookId);

}
