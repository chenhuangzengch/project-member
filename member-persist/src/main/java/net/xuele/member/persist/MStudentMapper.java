package net.xuele.member.persist;

import net.xuele.common.page.Page;
import net.xuele.member.domain.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MStudentMapper {
    /**
     * APP接口：根据学生ID获取学生信息
     */
    MStudent selectByPrimaryKey(@Param("userId") String userId,@Param("schoolId") String schoolId);

    /**
     * 1、班级添加学生
     * 2、学生离校，班级置为空
     */
    int updateStudents(@Param("userIdList") List<String> userIdList, @Param("mClass") MClass mClass);

    /**
     * 新增学生
     */
    int insert(MStudent mStudent);

    /**
     * APP接口、WEB平台：根据学生名称查询学生信息
     */
    List<MStudentManager> selectStudentByRealName(@Param("realName") String realName, @Param("schoolId") String schoolId);

    /**
     * 查询无班级学生信息
     */
    List<MStudentManager> selectStudentsWithoutClassId(@Param("classId") String classId, @Param("schoolId") String schoolId);

    /**
     * APP接口、WEB平台：根据班级ID查询该班级所有学生
     */
    List<MStudentManager> selectClassStudents(@Param("classId") String classId,@Param("schoolId") String schoolId);

    /**
     * 根据学校年级、班级、学生姓名获取学生信息
     * 分页查询
     */
    List<MStudentManager> selectMStudentManagerPage(@Param(value = "page")
                                                    Page page, @Param(value = "mStudentManager")
                                                    MStudentManager mStudentManager);

    /**
     * 根据年级班级用户名查询班级学生
     */
    List<MStudentManager> selectStudents_export(@Param(value = "mStudentManager")
                                                MStudentManager mStudentManager);

    /**
     * 总记录数
     *
     * @param mStudentManager
     * @return
     */
    long selectCount(MStudentManager mStudentManager);

    /**
     * 修改学生资料
     */
    int updateStudentMessage(@Param("userId") String userId, @Param("mClass") MClass mClass);

    int updateByPrimaryKey(MStudent record);

    /**
     * 根据userId查询学生信息
     */
    MStudentManager selectStudentByUserId(@Param("userId") String userId,@Param("schoolId") String schoolId);

    /**
     * APP接口：根据学生ID获取该学生所在的班级信息
     */
    MClassInfo getStudentClass(@Param("userId") String userId,@Param("schoolId") String schoolId);

    /**
     * 根据学生ID获取学生信息
     */
    MStudentInfo getStudentInfo(@Param("userId") String userId,@Param("schoolId") String schoolId);

    /**
     * 根据班级id获取该班的学生人数
     */
    int selectStudentCount(@Param("classId") String classId, @Param("schoolId") String schoolId);

    /**
     * App 接口，修改学校名称
     * @param schoolId 学校id
     * @param schoolName 学校名称
     */
    void updateSchoolNameBySchoolId(@Param("schoolId")String schoolId, @Param("schoolName")String schoolName);

    int updateByClassList(@Param("classIdList")List<String> classIds, @Param("schoolId")String schoolId);


    /**
     * 通讯录 老师  分页查询班级学生
     *
     */
    List<MStudentContacts> queryStudentInfoByClassPage(@Param(value = "page")
                                                    Page page,@Param("classId") String classId, @Param("schoolId") String schoolId, @Param("limit") Integer limit);
    /**
     * 通讯录 学生 分页查询班级同学
     * @param page
     * @param classId
     * @param schoolId
     * @param userId
     * @return
     */
    List<MStudentContacts> queryStudentInfoByClassmatePage(@Param(value = "page")
                                                       Page page,@Param("classId") String classId, @Param("schoolId") String schoolId,@Param("userId") String userId, @Param("limit") Integer limit);

    /**
     *通讯录 查询班级学生的家长信息
     * @param userIds
     * @param schoolId
     * @return
     */
    List<MStudentContacts> queryStudentLeafInfo(@Param("userIds")  List<String> userIds, @Param("schoolId") String schoolId);

    /**
     * 通讯录   家长查询孩子对应的家长信息
     * 查询出学生对应的班级及头像信息
     *
     * @param userId
     * @return
     */
    List<MStudentContacts> queryStudentInfoByParent(@Param(value = "page")
                                                    Page page,@Param("userId")  String userId, @Param("limit") Integer limit);
    /**
     * 通讯录   接口：根据学生ID获取该学生所在的班级信息(班级人数为正常用户且不包括自己)
     */
    MClassInfo getStudentClassByContacts(@Param("userId") String userId,@Param("schoolId") String schoolId);


}