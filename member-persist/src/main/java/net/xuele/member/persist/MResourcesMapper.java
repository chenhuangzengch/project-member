package net.xuele.member.persist;

import net.xuele.common.page.Page;
import net.xuele.member.domain.MResources;
import net.xuele.member.domain.MenuRoleResource;
import net.xuele.member.dto.ResRolesDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MResourcesMapper {
    int delete(List<String> ids);

    int insert(MResources record);

    MResources selectByPrimaryKey(String resourceId);

    long selectCount(MResources resource);

    int update(MResources record);

    List<MResources> selectResourcePage(@Param(value = "pageSize") int pageSize, @Param(value = "page") Page page, @Param(value = "mResources") MResources mResources);

    /**
     * 获取一级菜单
     */
    List<MResources> selectFirstLevel(@Param("userId") String userId);

    /**
     * 获取二级菜单
     */
    List<MResources> selectSecondLevel(String resourceId);

    /**
     * 获取所有资源以及对应角色，用于权限验证
     */
    List<ResRolesDTO> selectResourcesAndRoles();

    List<MenuRoleResource> selectResourcesByUserIdAndSchoolIdForAuth(@Param("userId")String userId,@Param("schoolId")String schoolId);
    //根据roleIds查找菜单资源
    List<MenuRoleResource> selectResourcesByRoleIdsForAuth(@Param("roleIds") List<String> roleIds);
}