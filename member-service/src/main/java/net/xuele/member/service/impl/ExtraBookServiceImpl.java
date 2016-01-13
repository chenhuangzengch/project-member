package net.xuele.member.service.impl;

import net.xuele.member.domain.CtExtraBook;
import net.xuele.member.dto.ExtraBookDTO;
import net.xuele.member.persist.CtExtraBookMapper;
import net.xuele.member.persist.MSchoolBookMapper;
import net.xuele.member.service.ExtraBookService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhongjian.xu on 2015/8/24 0024.
 */
public class ExtraBookServiceImpl implements ExtraBookService {
    @Autowired
    private CtExtraBookMapper extraBookMapper;
    @Autowired
    private MSchoolBookMapper schoolBookMapper;

    /**
     * 设置m_school_book的教辅id,根据教辅id获取教辅信息
     */
    @Override
    public ExtraBookDTO setExtraBookId(String bookId, String extraBookId, String schoolId) {
        //1、设置m_school_book表的教辅id
        schoolBookMapper.setExtraBookId(bookId, extraBookId, schoolId);
        //2、根据教辅id获取教辅信息
        return getByPrimaryId(extraBookId);
    }

    /**
     * 根据教辅id获取教辅信息
     */
    @Override
    public ExtraBookDTO getByPrimaryId(String extraBookId) {
        CtExtraBook extraBook = extraBookMapper.getByPrimaryKey(extraBookId);
        ExtraBookDTO extraBookDTO = new ExtraBookDTO();
        BeanUtils.copyProperties(extraBook, extraBookDTO);
        return extraBookDTO;
    }

    /**
     * 根据课本id获取教辅信息
     */
    @Override
    public List<ExtraBookDTO> queryByBookId(String bookId) {
        List<CtExtraBook> extraBookList = extraBookMapper.queryByBookId(bookId);
        List<ExtraBookDTO> extraBookDTOList = new ArrayList<>();
        for (CtExtraBook book : extraBookList) {
            ExtraBookDTO extraBookDTO = new ExtraBookDTO();
            BeanUtils.copyProperties(book, extraBookDTO);
            extraBookDTOList.add(extraBookDTO);
        }
        return extraBookDTOList;
    }



}
