package net.xuele.member.persist;
import net.xuele.member.domain.MEducationManagerLoginInfo;
import net.xuele.member.domain.MStudentLoginInfo;
import net.xuele.member.domain.MTeacherLoginInfo;
import net.xuele.member.domain.MUserLoginInfo;
import org.apache.ibatis.annotations.Param;

public interface MUserDetailMapper {

    MUserLoginInfo getLoginInfo(@Param("userId") String userId,@Param("schoolId") String schoolId);
    MTeacherLoginInfo getTeacherInfo(@Param("userId") String userId,@Param("schoolId") String schoolId);
    MStudentLoginInfo getStudentInfo(@Param("userId") String userId,@Param("schoolId") String schoolId);
    MEducationManagerLoginInfo getEducationManagerInfo(@Param("userId") String userId,@Param("schoolId") String schoolId);
}