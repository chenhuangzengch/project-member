package net.xuele.member.service.impl;


import net.xuele.common.dto.MenuNode;
import net.xuele.common.exceptions.MemberException;
import net.xuele.member.domain.MResources;
import net.xuele.member.domain.MRole;
import net.xuele.member.domain.MenuRoleResource;
import net.xuele.member.dto.ResRolesDTO;
import net.xuele.member.dto.ResourceAndRoleDTO;
import net.xuele.member.dto.RoleDTO;
import net.xuele.member.persist.MResourcesMapper;
import net.xuele.member.service.ResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zhongjian.xu on 2015/5/29 0008.
 */

public class ResourceServiceImpl implements ResourceService {
    @Autowired
    private MResourcesMapper resourceMapper;

    private static Logger logger = LoggerFactory.getLogger(ResourceServiceImpl.class);

    /**
     * 获取一级和二级菜单资源
     */
    @Override
    public List<MenuNode> queryResourcesByUserId(String userId) {
        Long start = System.currentTimeMillis();
        List<MenuNode> resourceDtO = new ArrayList<>();
        //一级菜单
        List<MResources> firstLevel = resourceMapper.selectFirstLevel(userId);
        for (MResources resource : firstLevel) {
            //二级菜单
            List<MResources> secondLevel = resourceMapper.selectSecondLevel(resource.getResourceId());
            if (CollectionUtils.isEmpty(secondLevel)) {
                continue;
            }
            MenuNode firstLevelDTO = paseToDTO(resource);
            List<MenuNode> secondLevelDTO = paseToDTOList(secondLevel);
            firstLevelDTO.setNextLevel(secondLevelDTO);
            resourceDtO.add(firstLevelDTO);
        }
        Long end = System.currentTimeMillis();
        Long time = end - start;
        logger.info("-----------------------------------------------");
        logger.info("原方法获取一级菜单和二级菜单所需时间为：{}毫秒", time);
        return resourceDtO;
    }


    @Override
    public List<ResourceAndRoleDTO> queryResourcesAndRoles() {
        List<ResourceAndRoleDTO> resourceAndRoleDTOs = new ArrayList<>();
        List<ResRolesDTO> resRolesDTOs = resourceMapper.selectResourcesAndRoles();
        if (resRolesDTOs != null && resRolesDTOs.size() > 0) {
            for (Iterator<ResRolesDTO> resIt = resRolesDTOs.iterator(); resIt.hasNext(); ) {
                ResRolesDTO resDTO = resIt.next();
                ResourceAndRoleDTO resourceAndRoleDTO = new ResourceAndRoleDTO();
                resourceAndRoleDTO.setName(resDTO.getName());
                resourceAndRoleDTO.setResourceId(resDTO.getResourceId());
                resourceAndRoleDTO.setUrl(resDTO.getUrl());
                resourceAndRoleDTO.setPattern(resDTO.getPattern());
                List<RoleDTO> roleDTOs = new ArrayList<>();
                List<MRole> mRoles = resDTO.getRoles();
                for (Iterator<MRole> mRoleIt = mRoles.iterator(); mRoleIt.hasNext(); ) {
                    MRole mRole = mRoleIt.next();
                    RoleDTO roleDTO = new RoleDTO();
                    roleDTO.setRoleId(mRole.getRoleId());
                    roleDTO.setRoleName(mRole.getRoleName());
                    roleDTOs.add(roleDTO);
                }
                resourceAndRoleDTO.setRoles(roleDTOs);
                resourceAndRoleDTOs.add(resourceAndRoleDTO);
            }
        }
        return resourceAndRoleDTOs;
    }

    //MResources转换成ResourceDTO
    private MenuNode paseToDTO(MResources resource) {
        MenuNode resourceDTO = new MenuNode();
        BeanUtils.copyProperties(resource, resourceDTO);
        return resourceDTO;
    }

    //List<MResources>转换成List<ResourceDTO>
    private List<MenuNode> paseToDTOList(List<MResources> resourceList) {
        List<MenuNode> resourceDTO = new ArrayList<>();
        for (MResources res : resourceList) {
            MenuNode resdto = new MenuNode();
            BeanUtils.copyProperties(res, resdto);
            resourceDTO.add(resdto);
        }
        return resourceDTO;
    }


    @Override
    public List<MenuNode> getMenuRoleResourceFisrtAndSecond(List<String> roleIds) {
        Long start = System.currentTimeMillis();
        List<MenuNode> menuNodes = new ArrayList<>();
        List<MenuRoleResource> menuRoleResources = resourceMapper.selectResourcesByRoleIdsForAuth(roleIds);
        if (CollectionUtils.isEmpty(menuRoleResources)) {
            logger.error("该用户没有菜单");
            throw new MemberException("该用户没有菜单");
        }
        for (MenuRoleResource menuRoleResource : menuRoleResources) {
            MenuNode menuNode = new MenuNode();
            menuNode.setName(menuRoleResource.getName());
            menuNode.setUrl(menuRoleResource.getUrl());
            logger.info("menu name is :"+menuRoleResource.getName());
            logger.info("menu url is :"+menuRoleResource.getUrl());
            menuNode.setResourceId(menuRoleResource.getResourceId());
            List<MenuNode> menuNodeNext = new ArrayList<>();
            for (MenuRoleResource menuRoleResourceSon : menuRoleResource.getNextLevel()) {
                MenuNode menuNodeSon = new MenuNode();
                menuNodeSon.setName(menuRoleResourceSon.getName());
                menuNodeSon.setUrl(menuRoleResourceSon.getUrl());
                logger.info("menu name is :"+menuRoleResourceSon.getName());
                logger.info("menu url is :"+menuRoleResourceSon.getUrl());
                menuNodeSon.setResourceId(menuRoleResourceSon.getResourceId());
                menuNodeNext.add(menuNodeSon);
            }
            menuNode.setNextLevel(menuNodeNext);
            menuNodes.add(menuNode);
        }

        Long end = System.currentTimeMillis();
        Long time = end - start;
        logger.info("---------------------------------");
        logger.info("获取权限下的一级菜单和二级菜单所需时间为：{}毫秒", time);
        return menuNodes;
    }

}

