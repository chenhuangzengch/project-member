package net.xuele.member.service;

import net.xuele.common.page.PageResponse;
import net.xuele.member.dto.*;
import net.xuele.member.dto.page.SchoolPageRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhongjian.xu on 2015/6/8 0008.
 */
public interface SchoolService {
    /**
     * 添加学校信息
     *
     * @param schoolDTO {@link SchoolDTO}
     */
//    int save(SchoolDTO schoolDTO);

    /**
     * 删除一条或多条学校信息
     * 把信息状态status改为1，不显示
     *
     * @param id ids
     */
//    int delete(List<String> id);

    /**
     * 根据学校id查询学校信息
     *
     * @param schoolId 学校ID
     * @return 学校信息
     */
    SchoolDTO  getBySchoolId(String schoolId);

    /**
     * 根据ID更新学校信息
     *
     * @param schoolDTO {@link SchoolDTO}
     */
    int update(SchoolDTO schoolDTO);

    /**
     * 学校信息分页查询
     *
     * @param schoolPageRequest {@link SchoolPageRequest}
     * @return PageResponse<SchoolDTO> 学校信息
     */
    PageResponse<SchoolDTO> querySchoolPage(SchoolPageRequest schoolPageRequest);

    /**
     * member、通知 用
     * 根据学校id获取该学校的年级列表
     *
     * @param schoolId 学校id
     * @return List<GradeDTO> 年级列表
     */
    List<GradeDTO> queryGrade(String schoolId);

    /**
     * 1.member 用
     * 2.通知调用
     * 根据年级查询班级
     *
     * @param schoolId 学校id
     * @param year     入学年份
     * @return List<ClassStudentDTO> 班级
     */
    List<ClassStudentDTO> queryAllClass(String schoolId, String year);


    /**
     * APP接口:添加学校和管理员
     *
     * @param schoolAndManageDTO 学校信息和管理员，其中学校信息必须有schoolId、schoolName等学校主要信息；
     *                           manager管理员信息，主要包含managerId和managerName信息，
     * @param schoolPeriodInfos  学校学制学段信息
     */
    SchoolAndManageDTO saveCreateSchoolAndManager(SchoolAndManageDTO schoolAndManageDTO, ArrayList<SchoolPeriodDTO> schoolPeriodInfos);

    /**
     * APP接口：修改学校信息
     *
     * @param schoolAndManageDTO 学校信息
     * @param schoolPeriodInfos  学制学段
     */
    SchoolAndManageDTO updateSchoolAndManager(SchoolAndManageDTO schoolAndManageDTO, ArrayList<SchoolPeriodDTO> schoolPeriodInfos);

    /**
     * @user circle
     * 根据schoolIds获取所有的学校名称
     */
    List<String> getNameByIds(List<String> schoolIds);
}
