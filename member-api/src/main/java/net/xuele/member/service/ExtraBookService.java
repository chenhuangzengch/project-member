package net.xuele.member.service;

import net.xuele.member.dto.ExtraBookDTO;

import java.util.List;

/**
 * Created by zhongjian.xu on 2015/8/24 0024.
 */
public interface ExtraBookService {

    /**
     * 设置m_school_book的教辅id,并根据教辅id获取教辅信息
     *
     * @param bookId      课本id
     * @param extraBookId 教辅id
     * @param schoolId    学校id
     * @return 教辅信息
     */
    ExtraBookDTO setExtraBookId(String bookId, String extraBookId, String schoolId);

    /**
     * 根据课本id获取教辅信息
     *
     * @param bookId 课本id
     * @return 教辅信息
     */
    List<ExtraBookDTO> queryByBookId(String bookId);

    /**
     * 根据教辅id获取教辅信息
     *
     * @param extraBookId 教辅id
     * @return 教辅信息
     */
    ExtraBookDTO getByPrimaryId(String extraBookId);


}
