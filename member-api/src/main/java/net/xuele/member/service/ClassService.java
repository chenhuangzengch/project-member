package net.xuele.member.service;


import net.xuele.common.dto.ClassInfoDTO;
import net.xuele.member.dto.*;

import java.util.List;

/**
 * @author ZhengTao
 */
public interface ClassService {

    /**
     * 获取班级的学校id,先从缓存中取，取不到到数据库取,数据库取到后存到缓存，以确保第二次缓存中能取到
     *
     * @param classId 班级号
     * @return 获取学校ID
     */
    String getSchoolId(String classId);

    /**
     * 获取某学校的年级列表
     *
     * @param classDTO 参数封装对象  需要参数：schoolId,aliasName
     * @return 年级列表
     */
    List<GradeDTO> queryGrades(ClassDTO classDTO);

    /**
     * 新增班级
     *
     * @param classDTO 新增的班级信息
     * @return 新增的班级信息
     */
    ClassDTO saveClass(ClassDTO classDTO);

    /**
     * 1、参数没有年份则获取某学校的全部未毕业班级信息
     * 2、参数有年份则获取某学校该年级的班级信息
     * 3、参数有别名则获取某学校的该别名班级信息
     *
     * @param classDTO 参数封装对象
     * @return 班级信息
     */
    List<ClassStudentDTO> queryClasses(ClassDTO classDTO);

    /**
     * 修改班级信息
     *
     * @param classDTO 修改的班级信息
     * @return 修改后的班级信息
     */
    ClassDTO updateClass(ClassDTO classDTO);

    /**
     * 根据班级ID删除班级
     *
     * @param classId 班级id
     */
    void deleteClassByClassId(String classId);

    /**
     * 更换班主任、添加班主任
     *
     * @param userId  老师id
     * @param classId 班级id
     * @return 老师信息
     */
    UserTeacherDTO updateClassTeacher(String userId, String classId, String schoolId);

    /**
     * 根据班级id解除该班的班主任
     *
     * @param classId 班级id
     */
    int deleteChief(String classId);

    /**
     * 根据班级id查询班级信息
     *
     * @param classId 班级id
     * @return 班级信息
     */
    ClassDTO getByClassId(String classId);

    /**
     * APP接口：根据用户id获取该用户所在学校的全部未毕业班级
     *
     * @param userId 用户id
     * @return 班级信息
     */
    List<ClassInfoDTO> queryAllClass(String userId);

    /**
     * 根据学校id获取毕业班级信息，有别名则根据别名搜索，没有则搜索全部
     *
     * @param schoolId  学校id
     * @param aliasName 班级别名
     * @return 毕业班级信息
     */
    List<GraduateingClassDTO> queryALLGraduateClass(String schoolId, String aliasName);

    /**
     * APP接口：查询科目，年级，班级
     *
     * @param subjectGradeDTO 存放区域id:area、学校id:schoolId、教师id:userId
     * @return 信息
     */
    SubjectGradeDTO getSubjectGrade(SubjectGradeDTO subjectGradeDTO);

    /**
     * 根据学校id查询学校未毕业班级个数
     *
     * @param schoolId 学校id
     * @return 班级个数
     */
    int selectClassNumBySchoolId(String schoolId);

    /**
     * 根据学校id获取该学校的所有班级id
     *
     * @param schoolId 学校id
     * @return 班级id列表
     */
    List<String> queryClassIdBySchoolId(String schoolId);

    /**
     * 根据班级ids查询班级信息
     * @use circle
     * @param schoolId
     * @param classIds
     * @return
     */
    List<ClassDTO> getAllClassByClassIds(String schoolId,List<String> classIds);

    /**
     * 根据班级ids查询班级信息
     * @use circle
     * @param schoolId
     * @param classIds
     * @return
     */
    List<String> getClassNamesByClassIds(String schoolId,List<String> classIds);

    /**
     * 修改班主任记录日志
     * @param teacherClassLogDTO 修改信息
     */
    void updateTeacherClassLog(TeacherClassLogDTO teacherClassLogDTO);
}

