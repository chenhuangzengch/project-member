package net.xuele.member.web.controller;

import net.xuele.common.ajax.AjaxResponse;
import net.xuele.common.security.SessionUtil;
import net.xuele.common.security.UserSession;
import net.xuele.member.dto.ExtraBookDTO;
import net.xuele.member.service.ExtraBookService;
import net.xuele.member.web.wrapper.ExtraBookWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhongjian.xu on 2015/8/24 0024.
 */
@Controller
@RequestMapping("extraBook")
public class ExtraBookController {
    @Autowired
    private ExtraBookService extraBookService;

    /**
     * 设置m_school_book的教辅id,并根据教辅id获取教辅信息
     */
    @ResponseBody
    @RequestMapping("setExtraBookId")
    public AjaxResponse<ExtraBookWrapper> setExtraBookId(@RequestParam("bookId") String bookId, @RequestParam("extraBookId") String extraBookId) {
        ExtraBookDTO extraBookDTO = extraBookService.setExtraBookId(bookId, extraBookId, gainUser().getSchoolId());
        ExtraBookWrapper extraBookWrapper = new ExtraBookWrapper();
        BeanUtils.copyProperties(extraBookDTO, extraBookWrapper);
        return new AjaxResponse(extraBookWrapper);
    }

    /**
     * 根据课本id获取教辅信息
     */
    @ResponseBody
    @RequestMapping("queryByBookId")
    public AjaxResponse<ExtraBookWrapper> queryByBookId(@RequestParam("bookId") String bookId) {
        List<ExtraBookDTO> extraBookDTOList = extraBookService.queryByBookId(bookId);
        List<ExtraBookWrapper> extraBookWrapperList = new ArrayList<>();
        for (ExtraBookDTO dto : extraBookDTOList) {
            ExtraBookWrapper wrapper = new ExtraBookWrapper();
            BeanUtils.copyProperties(dto, wrapper);
            extraBookWrapperList.add(wrapper);
        }
        return new AjaxResponse(extraBookWrapperList);
    }

    //获取用户信息
    private UserSession gainUser() {
        return SessionUtil.getUserSession();
    }
}
