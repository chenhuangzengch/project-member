package net.xuele.member.util;

import net.xuele.member.dto.ResourceAndRoleDTO;
import net.xuele.member.dto.RoleDTO;
import net.xuele.member.service.ResourceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.*;

/**
 * 资源及对应角色工厂bean
 * zhihuan.cai 新建于 2015/7/15 0015.
 */
public class RequestMapFactoryBean implements FactoryBean<Map<RequestMatcher, Collection<ConfigAttribute>>> {


    private ResourceService resourceService;

    @Override
    public Map<RequestMatcher, Collection<ConfigAttribute>> getObject() throws Exception {
        Map<RequestMatcher, Collection<ConfigAttribute>> requestMap = new LinkedHashMap<>();
        List<ResourceAndRoleDTO> resourceAndRoleDTOs = resourceService.queryResourcesAndRoles();
        for (Iterator<ResourceAndRoleDTO> resourceAndRoleDTOIterator = resourceAndRoleDTOs.iterator(); resourceAndRoleDTOIterator.hasNext(); ) {
            ResourceAndRoleDTO resourceAndRoleDTO = resourceAndRoleDTOIterator.next();
            String pattern = resourceAndRoleDTO.getPattern();
            if (StringUtils.isNotEmpty(pattern)) {
                //matcher 匹配防止用户输入后缀
                RequestMatcher requestMatcher = new AntPathRequestMatcher(pattern + "*", null, Boolean.FALSE);
                List<RoleDTO> roleDTOs = resourceAndRoleDTO.getRoles();
                Collection<ConfigAttribute> configAttributes = new ArrayList<>();
                for (Iterator<RoleDTO> roleDTOIterator = roleDTOs.iterator(); roleDTOIterator.hasNext(); ) {
                    RoleDTO roleDTO = roleDTOIterator.next();
                    configAttributes.add(new SecurityConfig(roleDTO.getRoleId()));
                }
                requestMap.put(requestMatcher, configAttributes);
            }
        }
        return requestMap;
    }


    @Override
    public Class<?> getObjectType() {
        return Map.class;
    }

    @Override
    public boolean isSingleton() {
        return Boolean.TRUE;
    }

    ////get and sets
    public ResourceService getResourceService() {
        return resourceService;
    }

    public void setResourceService(ResourceService resourceService) {
        this.resourceService = resourceService;
    }
}
