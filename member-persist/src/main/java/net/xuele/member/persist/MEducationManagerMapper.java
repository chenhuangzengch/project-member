package net.xuele.member.persist;

import net.xuele.member.domain.MEducationManager;
import net.xuele.member.domain.MUserTeacher;
import org.apache.ibatis.annotations.Param;

public interface MEducationManagerMapper {

    int insert(MEducationManager record);

    /**
     * APP接口：根据用户ID获取该用户所在的教育机构
     */
    MEducationManager selectByPrimaryKey(@Param(value = "userId") String userId);

    int updateByEducationalId(@Param(value = "educationalId") String educationalId,
                              @Param(value = "educationalName") String educationalName);

    /**
     * APP接口：根据教育机构ID获取该机构管理员
     */
    MUserTeacher getEducationManager(String educationalId);

}