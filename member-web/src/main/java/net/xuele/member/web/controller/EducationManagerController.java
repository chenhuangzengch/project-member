package net.xuele.member.web.controller;

import net.xuele.common.ajax.AjaxResponse;
import net.xuele.common.exceptions.MemberException;
import net.xuele.common.security.SessionUtil;
import net.xuele.common.security.UserSession;
import net.xuele.member.dto.GroupDTO;
import net.xuele.member.dto.ProvincialAreaDTO;
import net.xuele.member.service.EducationOrganizationService;
import net.xuele.member.web.wrapper.GroupWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaike.du on 2015/8/12 0012.
 */
@Controller
@RequestMapping("/educationManager")
public class EducationManagerController {

    @Autowired
    private EducationOrganizationService educationOrganizationService;

    /**
     * 获取下一级教育机构地区
     *
     * @param areaId 地区编号
     *               为空，则根据用户所在的教育机构地区获取下一级教育机构地区
     *               不为空，则根据地区编号查询下一级教育机构地区
     * @return 下一级教育机构地区编号和名称
     */
    @ResponseBody
    @RequestMapping("queryArea")
    public AjaxResponse<GroupWrapper> queryArea(String areaId) {
        List<GroupDTO> groupDTOList = educationOrganizationService.queryOrgEducationNoSchool(gainUser().getUserId(), areaId);
        List<GroupWrapper> groupWrapperList = new ArrayList<>();
        for (GroupDTO group : groupDTOList) {
            GroupWrapper gw = new GroupWrapper();
            BeanUtils.copyProperties(group, gw);
            groupWrapperList.add(gw);
        }
        return new AjaxResponse(groupWrapperList);
    }

    /**
     * 根据省级地区编号查询所有的市区级
     */
    @ResponseBody
    @RequestMapping("provincialArea")
    public AjaxResponse<ProvincialAreaDTO> queryProvincialArea(String areaId) {
        if (StringUtils.isEmpty(areaId)) {
            throw new MemberException("没有获取地区编号");
        }
        if (areaId.length() != 2) {
            throw new MemberException("地区编号不是省级");
        }
        List<ProvincialAreaDTO> padtoList = educationOrganizationService.queryProvincialArea(areaId);
        return new AjaxResponse(padtoList);
    }

    //获取用户
    private UserSession gainUser() {
        return SessionUtil.getUserSession();
    }
}
