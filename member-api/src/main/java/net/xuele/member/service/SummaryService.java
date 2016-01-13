package net.xuele.member.service;


import net.xuele.member.dto.ContactsListGroupDTO;
import net.xuele.member.dto.GroupDTO;
import net.xuele.member.dto.SummaryDTO;

import java.util.List;

/**
 * Created by zhongjian.xu on 2015/6/24 0024.
 */
public interface SummaryService {
    /**
     * 查询所有科目列表，不根据学校区分
     *
     * @return 科目列表
     */
    List<SummaryDTO> queryAll();

    /**
     * 查询年级对应的科目列表
     * @param grade 年级
     * @return 科目列表
     */
    List<SummaryDTO> queryGradeSubject(Integer grade);
    /**
     * APP接口：根据用户ID查询该用户所在学校的科目列表
     *
     * @param userId 用户ID
     * @return 科目ID, 科目名称
     */
    List<GroupDTO> queryAllSubject(String userId);

    /**
     * 根据学校ID查询该学校的科目列表
     *
     * @param schoolId 学校ID
     * @return 科目列表
     */
    List<SummaryDTO> querySchoolSubject(String schoolId);

    /**
     * 根据用户ID查询科目列表
     * @param userId
     * @param schoolId
     * @return
     */
    List<SummaryDTO> queryTeacherSubject(String userId,String schoolId);

    /**通知调用
     * 根据学校ID查询该学校的科目列表，没有科目的教师显示在<其他> 中
     *
     * @param schoolId 学校ID
     * @return 科目列表
     */
    ContactsListGroupDTO querySubjectByNotify(String schoolId);
}
