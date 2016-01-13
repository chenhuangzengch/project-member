package net.xuele.member.persist;

import net.xuele.member.domain.CtExtraBook;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CtExtraBookMapper {
//    int deleteByPrimaryKey(String extraBookId);
//    int insert(CtExtraBook record);
//    List<CtExtraBook> selectAll();
//    int updateByPrimaryKey(CtExtraBook record);

    /**
     * 根据课本id获取教辅信息
     */
    List<CtExtraBook> queryByBookId(@Param("bookId") String bookId);

    /**
     * 根据教辅id获取教辅信息
     */
    CtExtraBook getByPrimaryKey(@Param("extraBookId")String extraBookId);
}