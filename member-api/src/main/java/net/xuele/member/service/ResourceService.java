package net.xuele.member.service;


import net.xuele.common.dto.MenuNode;
import net.xuele.member.dto.ResourceAndRoleDTO;

import java.util.List;

/**
 * Created by zhongjian.xu on 2015/5/19 0008.
 */


public interface ResourceService {

    /**
     * 获取系统中所有的资源以及资源对应的角色列表
     *
     */
    List<ResourceAndRoleDTO> queryResourcesAndRoles();

    /**
     * 获取一级和二级菜单资源
     *
     * @param userId 用户学乐号
     * @return 一级和二级菜单资源信息
     */
    List<MenuNode> queryResourcesByUserId(String userId);

    /**
     * 获取一级和二级菜单资源
     * @param roleIds roleIds
     * @return 一级和二级菜单资源信息
     */
    List<MenuNode> getMenuRoleResourceFisrtAndSecond(List<String> roleIds);
}

