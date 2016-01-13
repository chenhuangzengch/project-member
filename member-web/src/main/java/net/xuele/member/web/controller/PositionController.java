package net.xuele.member.web.controller;

import net.xuele.common.ajax.AjaxResponse;
import net.xuele.common.security.SessionUtil;
import net.xuele.common.security.UserSession;
import net.xuele.member.dto.PositionDTO;
import net.xuele.member.dto.PositionMemberDTO;
import net.xuele.member.service.PositionService;
import net.xuele.member.web.wrapper.PositionWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 职务控制类
 * zhihuan.cai 新建于 2015/6/21 0021.
 */
@Controller
@RequestMapping(value = "position")
public class PositionController {
    @Autowired
    private PositionService positionService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    private String getSchoolId() {
        UserSession userDetails = SessionUtil.getUserSession();
        return userDetails.getSchoolId();
    }

    /**
     * 新增职务
     *
     * @param positionName 职务名称
     * @return result(职务信息)
     */
    @RequestMapping(value = "add")
    @ResponseBody
    public AjaxResponse<PositionWrapper> add(@RequestParam("name") String positionName) {
        PositionDTO positionDTO = new PositionDTO();
        positionDTO.setName(positionName);
        positionDTO.setSchoolId(getSchoolId());
        PositionDTO result = positionService.savePosition(positionDTO);
        PositionWrapper positionWrapper = new PositionWrapper(result);
        return new AjaxResponse<>(positionWrapper);
    }

    /**
     * 编辑职务
     *
     * @param postName   职务名称
     * @param positionId 职务id
     * @return result(职务信息)
     */
    @RequestMapping(value = "update")
    @ResponseBody
    public AjaxResponse<PositionWrapper> update(@RequestParam("name") String postName, @RequestParam("positionId") String positionId) {
        PositionDTO positionDTO = new PositionDTO();
        positionDTO.setPositionId(positionId);
        positionDTO.setName(postName);
        positionDTO.setSchoolId(getSchoolId());
        PositionDTO result = positionService.updatePosition(positionDTO);
        PositionWrapper positionWrapper = new PositionWrapper(result);
        return new AjaxResponse<>(positionWrapper);
    }

    /**
     * 删除职务
     *
     * @param positionId 职务id
     * @return result(职务信息)
     */
    @RequestMapping(value = "delete")
    @ResponseBody
    public AjaxResponse<PositionWrapper> delete(@RequestParam(value = "positionId", required = true) String positionId) {
        PositionDTO positionDTO = new PositionDTO();
        positionDTO.setPositionId(positionId);
        positionDTO.setSchoolId(getSchoolId());
        //positionService.updateTeacher(positionDTO);
        PositionDTO result = positionService.deletePosition(positionDTO);
        PositionWrapper positionWrapper = new PositionWrapper(result);
        return new AjaxResponse<>(positionWrapper);
    }

    /**
     * 页面初始显示
     *
     * @param map map
     * @return 职务成员列表
     */
    @RequestMapping(value = "index")
    public String index(ModelMap map) {
        List<PositionMemberDTO> positionMemberDTOs = positionService.queryPositionMember(getSchoolId());
        map.put("positionMemberDTOs", positionMemberDTOs);
        logger.info("查询" + positionMemberDTOs);
        return "/position/index";
    }

}
