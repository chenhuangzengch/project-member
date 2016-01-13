package net.xuele.member.persist;

import net.xuele.member.domain.CtBook;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

public interface CtAreaTextbookMapper {

    List<CtBook> queryAreaBook(String areaId);

    /**
     * 查询某区域下的某些科目，某些年级，某些学期的教材信息（组合）
     *
     * @param area      区域id
     * @param gradeCode 年级列表
     * @param subjectId 科目列表
     * @param semester  学期列表
     */
    List<CtBook> queryAreaBookList(@Param("area") String area,
                                   @Param("gradeCode") ArrayList<Integer> gradeCode,
                                   @Param("subjectId") ArrayList<String> subjectId,
                                   @Param("semester") ArrayList<Integer> semester);
}