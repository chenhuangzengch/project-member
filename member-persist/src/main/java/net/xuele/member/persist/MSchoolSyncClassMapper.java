package net.xuele.member.persist;

import net.xuele.member.domain.MSchoolSyncClass;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MSchoolSyncClassMapper {
    int deleteByPrimaryKey(@Param("schoolId") String schoolId, @Param("bookId") String bookId);

    MSchoolSyncClass selectByPrimaryKey(@Param("schoolId") String schoolId, @Param("bookId") String bookId);

    List<MSchoolSyncClass> selectAll();

    int updateByPrimaryKey(MSchoolSyncClass record);

    int updateSyncClassBook(@Param("oldBookId") String oldBookId,@Param("newBookId") String newBookId,@Param("schoolId") String schoolId );
}