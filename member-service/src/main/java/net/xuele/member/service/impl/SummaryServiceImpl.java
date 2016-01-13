package net.xuele.member.service.impl;


import net.xuele.member.domain.CtBook;
import net.xuele.member.domain.DSummary;
import net.xuele.member.domain.MTeacherBookCount;
import net.xuele.member.dto.ContactsListGroupDTO;
import net.xuele.member.dto.GroupDTO;
import net.xuele.member.dto.SummaryDTO;
import net.xuele.member.persist.CtBookMapper;
import net.xuele.member.persist.DSummaryMapper;
import net.xuele.member.persist.MTeacherMapper;
import net.xuele.member.service.SummaryService;
import net.xuele.member.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mengzhiheng on 2015/6/26 0008.
 */

public class SummaryServiceImpl implements SummaryService {
    @Autowired
    private DSummaryMapper dSummaryMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private CtBookMapper ctBookMapper;
    @Autowired
    private MTeacherMapper teacherMapper;

    /**
     * 查询所有科目列表，不根据学校区分
     */
    @Override
    public List<SummaryDTO> queryAll() {
        List<DSummary> summaryList = dSummaryMapper.selectAll();
        List<SummaryDTO> summaryDTOList = new ArrayList<>();
        for (DSummary s : summaryList) {
            SummaryDTO summaryDTO = new SummaryDTO();
            BeanUtils.copyProperties(s, summaryDTO);
            summaryDTOList.add(summaryDTO);
        }
        return summaryDTOList;
    }

    @Override
    public List<SummaryDTO> queryGradeSubject(Integer grade) {
        List<DSummary> summaryList = dSummaryMapper.selectGradeSubject(grade);
        List<SummaryDTO> summaryDTOList = new ArrayList<>();
        for (DSummary s : summaryList) {
            SummaryDTO summaryDTO = new SummaryDTO();
            BeanUtils.copyProperties(s, summaryDTO);
            summaryDTOList.add(summaryDTO);
        }
        return summaryDTOList;
    }

    /**
     * APP接口：根据用户ID查询该用户所在学校的科目列表
     */
    @Override
    public List<GroupDTO> queryAllSubject(String userId) {
        List<DSummary> subjectList = dSummaryMapper.querySchoolSubject(userService.getSchoolId(userId));
        List<GroupDTO> groupAppDTOList = new ArrayList<>();
        for (DSummary summary : subjectList) {
            GroupDTO group = new GroupDTO();
            group.setGroupid(summary.getSummaryCode());
            group.setGroupname(summary.getSummaryName());
            groupAppDTOList.add(group);
        }
        //全校
        //groupAppDTOList.add(0, new GroupDTO(SearchConstants.SCHOOLID, SearchConstants.SCHOOLNAME));
        //管理层
        //groupAppDTOList.add(2, new GroupDTO(SearchConstants.MANAGERID, SearchConstants.MANAGERNAME));
        return groupAppDTOList;
    }

    /**
     * 根据学校ID查询该学校的科目列表
     */
    @Override
    public List<SummaryDTO> querySchoolSubject(String schoolId) {
        List<DSummary> summaryList = dSummaryMapper.querySchoolSubject(schoolId);
        List<SummaryDTO> summaryDTOList = new ArrayList<>();
        for (DSummary summary : summaryList) {
            SummaryDTO sdto = new SummaryDTO();
            BeanUtils.copyProperties(summary, sdto);
            summaryDTOList.add(sdto);
        }
        return summaryDTOList;
    }

    /**
     * 根据老师ID查询老师对应的科目列表
     * @param userId 老师ID
     * @param schoolId 学校ID
     */
    @Override
    public List<SummaryDTO> queryTeacherSubject(String userId, String schoolId) {
        List<DSummary> summaryList = dSummaryMapper.queryTeacherSubject(userId,schoolId);
        List<SummaryDTO> summaryDTOList = new ArrayList<>();
        for (DSummary summary : summaryList) {
            if(summary != null) {
                SummaryDTO sdto = new SummaryDTO();
                BeanUtils.copyProperties(summary, sdto);
                summaryDTOList.add(sdto);
            }
        }
        return summaryDTOList;
    }

    /**通知调用
     * 根据学校ID查询该学校的科目列表，没有科目的教师显示在<其他> 中
     *
     * @param schoolId 学校ID
     * @return 科目列表
     */
    @Override
    public ContactsListGroupDTO querySubjectByNotify(String schoolId) {

        ContactsListGroupDTO groupDTOLevel1 = new ContactsListGroupDTO();
        List<ContactsListGroupDTO> groupLevel2 = new ArrayList<>();
        ContactsListGroupDTO groupDTOLevel2 = new ContactsListGroupDTO();
        groupDTOLevel2.setMemberAmount(0);
        groupDTOLevel2.setGroupId("MANAGE_NODE");
        groupDTOLevel2.setGroupName("管理层");
        groupLevel2.add(groupDTOLevel2);
        //学校对应的科目
        List<CtBook> ctBookList = ctBookMapper.querySubjectGroupByList(schoolId);
        //科目对应老师的数量
        List<MTeacherBookCount> mTeacherBookCountList = teacherMapper.queryUserBookList(schoolId);
        Map map = new HashMap();
        //Map存放科目对应老师的数量
        ContactsListGroupDTO groupDTOOtherLevel2 = new ContactsListGroupDTO();
        for (int i = 0; i < mTeacherBookCountList.size(); i++) {
            if (mTeacherBookCountList.get(i).getSubjectId() != null) {
                //有科目的放入Map
                map.put(mTeacherBookCountList.get(i).getSubjectId(), mTeacherBookCountList.get(i).getUserNumber());
            }
        }
        //groupDTOLevel2 传入科目对应的老师数量（map中的数据） 没有教师的不显示
        for (CtBook ctBook : ctBookList) {
            if (map.get(ctBook.getSubjectId()) != null && !map.get(ctBook.getSubjectId()).equals(0)) {
                ContactsListGroupDTO groupDTOSubjectLevel2 = new ContactsListGroupDTO();
                groupDTOSubjectLevel2.setGroupName(ctBook.getSubjectName());
                groupDTOSubjectLevel2.setGroupId(ctBook.getSubjectId());
                groupDTOSubjectLevel2.setMemberAmount(Integer.parseInt(map.get(ctBook.getSubjectId()).toString()));
                groupLevel2.add(groupDTOSubjectLevel2);
            }
        }
        //把不是管理层和没有科目的放入其他
        Long nullSubjectManager = teacherMapper.nullSubjectManager(schoolId);
        if (nullSubjectManager != 0) {
            groupDTOOtherLevel2.setGroupName("其他");
            groupDTOOtherLevel2.setGroupId("UNLOAD_NODE");
            groupDTOOtherLevel2.setMemberAmount(Integer.parseInt(nullSubjectManager.toString()));
            groupLevel2.add(groupDTOOtherLevel2);
        }
        groupDTOLevel1.setChildren(groupLevel2);
        return groupDTOLevel1;
    }
}
