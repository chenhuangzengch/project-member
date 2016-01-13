package net.xuele.member.persist;

import net.xuele.member.domain.MRole;
import net.xuele.member.domain.MUserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MUserRoleMapper {
    /**
     * 单个对象插入
     */
    int insert(MUserRole record);

    List<MRole> selectRoleByUserId(@Param("userId") String userId,@Param("schoolId") String schoolId);

    /**
     * 多个对象插入
     */
    void saveUserRoleList(@Param("list") List<MUserRole> list);

    /**
     * 多个对象删除
     */
    void deleteUserRoleList(@Param("list") List<String> list, @Param("roleId") String roleId,@Param("schoolId") String schoolId);
    /**
     * 单个对象删除
     */
    void deleteUserRole(@Param("userId") String userId, @Param("roleId") String roleId,@Param("schoolId") String schoolId);
}