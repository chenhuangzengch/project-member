package net.xuele.member.persist;

import net.xuele.member.domain.MClass;
import net.xuele.member.domain.MClassInfo;
import net.xuele.member.domain.MGrade;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MClassMapper {

    /**
     * 新增班级
     */
    int insert(MClass record);


    List<MClass> selectAll();

    /**
     * 修改班级信息
     */
    int updateByPrimaryKey(MClass record);

    int countBySchoolId(@Param(value = "schoolId") String schoolId, @Param(value = "years") int years);

    List<MGrade> selectGrade(@Param(value = "schoolId") String schoolId,
                             @Param(value = "minYears") int minYears,
                             @Param(value = "aliasName") String aliasName);

    /**
     * 设置班级的班主任
     */
    int updateClassTeacher(@Param(value = "userId") String userId,
                           @Param("realName") String realName,
                           @Param(value = "classId") String classId,
                           @Param("schoolId") String schoolId);

    void updateClassTeacherList(@Param(value = "classList") List<MClass> classList, @Param(value = "userId") String userId, @Param(value = "realName") String realName);

    /**
     * 查询班级信息
     */
    MClass selectByPrimaryKey(@Param("classId") String classId,
                              @Param("schoolId") String schoolId);

    /**
     * APP接口、WEB平台：修改班级的班主任信息
     */
    int removeChief(@Param("classId") String classId,
                    @Param("schoolId") String schoolId);

    /**
     * 查看班级别名是否已经存在
     */
    int selectByAliasName(@Param(value = "mClass") MClass mClass, @Param(value = "minYears") int minYears);

    /**
     * 根据班级id删除班级
     */
    int deleteClassByClassId(@Param("classId") String classId, @Param("schoolId") String schoolId);

    /**
     * APP接口、WEB平台：根据班级id、老师id、学校id删除老师班级关联表
     */
    int delByClassIdAndUserIdAndSchoolId(@Param("classId") String classId,
                                         @Param("teacherId") String teacherId,
                                         @Param("schoolId") String schoolId);

    /**
     * 根据班级id、学校id删除老师班级关联表
     */
    int delClassIdAndSchoolId(@Param("classId") String classId,
                              @Param("schoolId") String schoolId);

    /**
     * 根据学校ID查询学校未毕业班级个数
     */
    int selectBySchoolId(String schoolId);

    /**
     * APP接口：获取全部班级
     */
    List<MClass> getAllClass(@Param("schoolId") String schoolId);

    /**
     * APP接口：根据年级获取全部班级
     */
    List<MClass> getAllClassByYear(@Param("schoolId") String schoolId, @Param("year") int year);

    /**
     * 根据学校ID获取所有毕业班级信息
     */
    List<MClassInfo> queryALLGraduateClass(String schoolId);

    /**
     * 根据学校ID获取毕业班级信息，有别名则根据别名搜索，没有则搜索全部
     */
    List<MClassInfo> querySchoolbySection(@Param("schoolId") String schoolId, @Param("aliasName") String aliasName);

    /**
     * 班主任离校
     */
    int chiefLeave(@Param("userIds") List<String> userIds, @Param("schoolId") String schoolId);

    /**
     * 根据学界，班级号，学校获取班级信息
     */
    MClass getClassByYearAndName(@Param("year") int year, @Param("classNum") int classNum, @Param("schoolId") String schoolId);

    /**
     * 查询某老师在哪些班级当班主任
     *
     * @param userId   老师id
     * @param schoolId 学校id
     */
    List<MClass> queryChiefClass(@Param("userId") String userId, @Param("schoolId") String schoolId);

    /**
     * WEB平台、APP接口：根据老师ID获取所有授课班级
     */
    List<MClass> queryTeacherClass(@Param(value = "userId") String userId, @Param(value = "schoolId") String schoolId);

    /**
     * 通过班级ID和班主任ID获取班级信息
     */
    MClass getClassByUserIdAndClassId(@Param("userId") String userId,
                                      @Param("classId") String classId,
                                      @Param("schoolId") String schoolId);

    /**
     * 获取班主任id为chargeId的所有班级id
     *
     * @param chargeId
     * @return
     */
    List<String> getClassIdsByChargeId(@Param("chargeId") String chargeId);

    String selectSchoolIdByClassId(String classId);

    /**
     * App 接口，修改学校名称
     *
     * @param schoolId   学校id
     * @param schoolName 学校名称
     */
    void updateSchoolNameBySchoolId(@Param("schoolId") String schoolId, @Param("schoolName") String schoolName);

    /**
     * 根据学校id和学制学段id获取班级信息
     */
    List<String> selectIdsByPeriod(@Param("schoolId") String schoolId, @Param("id") String id);

    /**
     * 根据学校id和班级id列表删除教师班级对应表
     */
    int deleteByClassList(@Param("classIdList") List<String> classIds, @Param("schoolId") String schoolId);

    /**
     * 根据学校id和班级id列表删除班级
     */
    int deleteByPeriod(@Param("schoolId") String schoolId, @Param("id") String id);

    /**
     * 根据用户id和学校id获取该用户所在的班级表信息
     */
    MClass getClassByUserId(@Param("userId") String userId, @Param("schoolId") String schoolId);

    /**
     * 根据学校id获取该学校的所有班级id
     */
    List<String> queryClassIdBySchoolId(String schoolId);


    /**
     * 根据学校id和班级ids查询所有班级
     */
    List<MClass> getAllClassByClassIds(@Param("schoolId") String schoolId, @Param("classIds") List<String> classIds);
}