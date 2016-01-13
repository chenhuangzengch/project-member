package net.xuele.member.service;


import net.xuele.common.dto.ClassInfoDTO;
import net.xuele.common.page.PageResponse;
import net.xuele.member.dto.*;
import net.xuele.member.dto.page.StudentPageRequest;

import java.util.List;

/**
 * Created by zhongjian.xu on 2015/6/24 0024.
 */
public interface StudentService {
    /**
     * 班级添加学生
     * 学生原来已经存在，只是没有班级或者需要换班级，不是新增学生
     *
     * @param userId  学生学乐号，多个学生用","隔开
     * @param classId 班级ID
     */
    int saveStudents(String userId, String classId);

    /**
     * 新增加学生
     *
     * @param name    学生名称
     * @param classId 班级ID
     * @return 学生信息
     */
    StudentManagerDTO saveStudent(String name, String classId, String schoolId);

    /**
     * APP接口、WEB平台：根据学生名称查询学生信息
     *
     * @param realName 学生名称
     * @param userId   登陆用户ID
     * @return 该名称的所有学生信息
     */
    List<StudentManagerDTO> queryStudentByRealName(String realName, String userId);

    /**
     * 1.member 用
     * 2. APP接口、WEB平台：根据班级ID查询该班级所有学生
     * 3.通知接口调用
     *
     * @param classId 班级ID
     * @return 该班级的所有学生信息
     */
    List<StudentManagerDTO> queryClassStudents(String classId);

    /**
     * 查询无班级学生信息
     *
     * @return 无班级学生信息
     */
    List<StudentManagerDTO> queryStudentsWithoutClassId(String schoolId);

    /**
     * 修改学生资料
     *
     * @param userId   学生学乐号
     * @param realName 学生名称
     * @param classId  班级ID
     * @return 该学生信息
     */
    StudentManagerDTO updateStudentInfo(String userId, String realName, String classId);

    /**
     * 根据学校年级、班级、学生姓名获取学生信息
     */
    PageResponse<StudentManagerDTO> queryStudentPage(StudentPageRequest studentPageRequest);

    /**
     * 学生信息导出excel
     *
     * @param studentPageRequest 学生信息
     * @return 学生信息
     */
    List<ExcelInfo> queryExcelInfo(StudentPageRequest studentPageRequest);

    /**
     * APP接口：根据学生ID获取该学生所在的班级信息
     *
     * @param userId 学生ID
     * @return 班级信息
     */
    ClassInfoDTO queryUserClass(String userId);

    /**
     * 通知用
     * 根据学生Id查询家庭成员
     *
     * @param userId 学乐号
     * @return 家庭成员
     */
    List<FamilyRelationDTO> queryFamilyByStudent(String userId);

    /**
     * APP接口：编辑学生信息
     * 根据studentId编辑更新学生信息，主要包括班级信息和联系方式
     *
     * @param studentDTO {@link StudentDTO #userId #name #className #gradeNum #email #mobile} student信息
     *                   没有GradeName字段
     * @return 返回 传入的所有信息和classId、schoolId
     */
    StudentDTO updateStudentForAPP(StudentDTO studentDTO);

    /**
     *
     * APP接口：根据userId查询学生信息
     * @param userId 学乐号
     * @return {@link StudentDTO} 包含学生id，name，mobile，email。
     * 所在学校id，name；所在班级id，name，年级
     */
    StudentDTO queryStudentForAPP(String userId,String schoolId);

    /**
        @User openApi
     * 根据userId初始化化学生用户的密码，123456
     *
     * @param userId
     */
    /*
    void updateInitStudentPassword(String userId);
    */

    /**
     * 根据学生ID获取学生信息
     *
     * @param userId 学生ID
     * @return 学生信息
     */
    MyMessageDTO getMyMessageStudent(String userId);
}
