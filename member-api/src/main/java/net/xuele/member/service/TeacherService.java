package net.xuele.member.service;


import net.xuele.common.dto.ClassInfoDTO;
import net.xuele.common.page.PageResponse;
import net.xuele.member.dto.*;
import net.xuele.member.dto.page.TeacherPageRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mengzhiheng
 */
public interface TeacherService {
    /**
     * 根据老师ID获取老师表信息
     */
    TeacherDTO getTeacherByUserId(String userId, String schoolId);

    /**
     * APP接口：老师添加班级（班级和老师都只有一个）
     * WEB平台：班级管理添加老师（班级一个，老师多个）
     * 老师已存在，不是新增老师
     *
     * @param userIds 老师的学乐号，用","隔开
     * @param classId 班级ID
     */
    int saveClassTeachers(String userIds, String classId, String schoolId);

    /**
     * member、通知 用
     * 根据名称或科目ID查询老师（无全校、管理层）
     * 没有科目ID，只根据名称查询
     * 没有科目ID和名称,则查询全校老师
     * 根据科目ID和名称查询
     * 根据科目ID查询
     *
     * @param realName  老师名称
     * @param subjectId 科目ID
     * @param schoolId  学校ID
     * @return 老师信息
     */
    List<UserTeacherDTO> queryTeachers(String realName, String subjectId, String schoolId);

    /**
     * APP接口：根据科目ID查询老师信息（有全校、管理层）
     *
     * @param subjectId 科目ID
     * @param userId    用户ID
     * @return 老师信息
     */
    List<UserTeacherDTO> queryUsersBySubIdOrName(String subjectId, String userId);


    /**
     * 根据班级id查询该班级的所有老师
     *
     * @param classId 班级id
     * @return 老师信息
     */
    List<UserTeacherDTO> queryClassTeachers(String classId);

    /**
     * 修改老师资料
     *
     * @param userId     老师id
     * @param realName   老师名称
     * @param positionId 职务id
     * @return 修改后的老师信息
     */
    UserTeacherDTO updateTeacherInfo(String userId, String realName, String positionId);

    /**
     * APP接口、WEB平台：根据老师ID获取老师信息
     *
     * @param userId 老师ID
     * @return 老师信息
     */
    UserTeacherDTO getTeacherInfo(String userId);

    /**
     * 云教学：根据老师ID获取老师信息
     * 可能出现查询不同学校的老师
     *
     * @param userId 老师ID
     * @return 老师信息
     */
    UserTeacherDTO getTeacherInfo(String userId, String schoolId);

    /**
     * 根据多个老师ID获取老师信息
     *
     * @param userIds 多个老师ID
     * @return 老师信息
     */
    List<UserTeacherDTO> queryTeacherInfo(ArrayList<String> userIds);

    /**
     * 1.member 用
     * 2.通知接口调用
     * realName为空，根据老师名称搜索
     * realName不为空，查询所有管理层人员
     */
    List<UserTeacherDTO> queryAllManager(String schoolId, String realName);

    /**
     * @param pageRequest 老师信息
     * @return 登录信息
     */
    PageResponse<TeacherLoginDTO> queryPage(TeacherPageRequest pageRequest);

    ExcelInfo queryExcelInfo(TeacherPageRequest pageRequest);

    /**
     * 设置校长，返回原校长
     *
     * @param userId 学乐号
     */
    String savePrincipal(String userId, String schoolId);

    /**
     * 添加新老师
     *
     * @param name       名称
     * @param positionId 职务ID
     * @param schoolId   学校ID
     * @return 老师信息
     */
    UserTeacherDTO saveTeacher(String name, String positionId, String schoolId);

    /**
     * 管理层删除老师
     */
    void deleteManagers(String userIds);

    /**
     * 添加管理层人员
     */
    void saveManagers(String userIds, String schoolId);

    /**
     * APP接口、WEB平台：获取老师授课班级
     * 前端有用,删除或新增班级会调用这个接口，又因读写分离，因此标成readOnly=false
     *
     * @param userId 老师ID
     * @return 老师授课的班级
     */
    List<ClassInfoDTO> queryTeacherClass(String userId);

    /**
     * APP接口：根据老师ID获取老师所有的授课教材
     * 云教学在用
     *
     * @param userId 教师ID
     * @return List<CtBookDTO>
     */
    List<CtBookDTO> queryBookDTO(String userId);

    /**
     * 根据科目ID获取老师的授课教材
     *
     * @param userId    教师ID
     * @param subjectId 科目ID
     * @return List<CtBookDTO>
     */
    List<CtBookDTO> queryBookDTOBySubject(String userId, String subjectId);

    /**
     * APP接口：添加或修改教材（主授和非主授课本都直接替换）
     *
     * @param userId    用户ID
     * @param oldBookId 原来教材ID
     * @param newBookId 新教材ID
     * @param isMain    是否是主授教材
     */
    void updateBook(String userId, String oldBookId, String newBookId, Integer isMain);

    /**
     * 查询是否是主授教材
     *
     * @param userId   用户ID
     * @param bookId   教材ID
     * @param schoolId 学校ID
     * @return 是否是主授教材
     */
    Integer isMain(String userId, String bookId, String schoolId);

    /**
     * APP接口：修改主授教材(原主授课本保留变为非主授课本)
     *
     * @param userId    用户ID
     * @param newBookId 新教材ID
     */
    void updateTeacherBook(String userId, String newBookId);

    /**
     * APP接口：删除已授教材，不能删除主授教材
     *
     * @param userId 用户ID
     * @param bookId 教材ID
     */
    void deleteMaterialForUser(String userId, String bookId);

    /**
     * APP接口：根据老师名称查询老师
     *
     * @param realName 用户名称
     * @param userId   登录用户ID
     * @return 老师信息
     */
    List<UserTeacherDTO> queryTeacherByRealName(String realName, String userId);

    /**
     * APP接口：删除老师授课班级
     *
     * @param userId  用户ID
     * @param classId 班级ID
     */
    void delTeacherClass(String userId, String classId);

    /**
     * 根据老师ID获取老师信息
     *
     * @param userId 老师ID
     * @return 老师信息
     */
    MyMessageDTO getMyMessageTeacher(String userId, String schoolId);

    /**
     * APP接口：修改教师信息
     * 修改教师信息，包括主授课本bookId、学校职务和职务id、姓名、手机、邮箱
     * 所有任课班级id classIds 担任班主任班级ids
     *
     * @param teacherClassDTO 所有信息
     *                        return 教师信息
     */
    TeacherClassDTO saveEditTeacherForAPP(TeacherClassDTO teacherClassDTO);

    /**
     * APP接口：查询教师信息
     * 包括主授课本bookId、学校职务和职务id、姓名、手机、邮箱、任课班级
     *
     * @param userId   学乐号
     * @param schoolId 学校ID
     * @return {@link TeacherClassDTO 所有信息
     */
    TeacherClassDTO getTeacherForAPP(String userId, String schoolId);

    /**
     * 获取教师主授课本是否能布置提分宝作业与同步课堂作业
     *
     * @param schoolId 学校ID
     * @param bookId   主授课本ID
     * @return 1:小学语、数、英、科学(可以布置同步课堂) 2:初中数理化（可以布置提分宝） 0:其他
     * 信息技术不分学段返回1
     */
    int getTeacherPeriodType(String schoolId, String bookId);

    /**
     * 获取教师授课班级列表
     *
     * @param schoolId 学校ID
     * @param userId   老师ID
     * @return
     */
    List<GradeDTO> queryGradeByTeacher(String schoolId, String userId);

    /**
     * 获取教师授课某个年级的班级列表
     *
     * @param userId 老师ID
     * @param year   年级
     * @return
     */
    List<ClassInfoDTO> queryTeacherClassByGrade(String userId, int year);
}


