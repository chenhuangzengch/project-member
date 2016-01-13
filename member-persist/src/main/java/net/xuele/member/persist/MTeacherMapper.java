package net.xuele.member.persist;

import net.xuele.common.page.Page;
import net.xuele.member.domain.*;
import net.xuele.member.pojo.RequestTeacherLoginPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MTeacherMapper {
    //int deleteByPrimaryKey(String userId);
    //
    int insert(MTeacher record);
    //

    //
    //List<MTeacher> selectAll();
    //
    //int updateByPrimaryKey(MTeacher record);

    /**
     * 根据老师ID获取老师表信息
     */
    MTeacher selectByPrimaryKey(@Param("userId") String userId, @Param("schoolId") String schoolId);

    /**
     * 通过schoolId查询校长
     */
    MTeacher selectPrincipalBySchoolId(@Param("schoolId") String schoolId, @Param("principal") String principal);

    /**
     * 设置校长
     */
    int setPrincipalByUserId(@Param("userId") String userId, @Param("principal") String principal, @Param("schoolId") String schoolId, @Param("positionName") String positionName);

    /**
     * APP接口：老师添加班级（班级和老师都只有一个）
     * WEB平台：班级管理添加老师（班级一个，老师多个）
     */
//    int addClassTeachers(@Param("userIdList") List<String> userIdList, @Param("classId") String classId, @Param("schoolId") String schoolId);

    /**
     * APP接口、WEB平台：根据老师ID获取老师信息
     */
    MUserTeacher selectChief(@Param("userId") String userId, @Param("schoolId") String schoolId);

    /**
     * WEB平台、APP接口：查询全校老师、根据老师名称查询老师
     */
    List<MUserTeacher> selectTeacherWithoutSubjectId(@Param("realName") String realName,
                                                     @Param("schoolId") String schoolId);

    /**
     * WEB平台、APP平台：根据科目ID和名称查询、根据科目ID查询
     */
    List<MUserTeacher> selectTeacherWithSubjectId(@Param("realName") String realName,
                                                  @Param("subjectId") String subjectId,
                                                  @Param("schoolId") String schoolId);

    /**
     * 查询某班级所有老师
     */
    List<MUserTeacher> selectClassTeachers(@Param("classId") String classId,
                                           @Param("schoolId") String schoolId);

    /**
     * 修改老师资料
     */
    int updateTeacherMessage(@Param("userId") String userId, @Param("positionName") String positionName, @Param("positionId") String positionId, @Param("schoolId") String schoolId);

    /**
     * APP接口：查询所有管理层人员
     * WEB平台：查询所有管理层人员、根据名称查询
     */
    List<MUserTeacher> getAllManager(@Param("schoolId") String schoolId, @Param("realName") String realName);

    /**
     * 分页查询老师数据
     *
     * @param page 过滤条件和分页信息
     * @return 一页数据
     */
    List<MTeacherLogin> selectTeacherLogin(RequestTeacherLoginPage page);

    /**
     * 分页查询老师数据的个数
     *
     * @param page 过滤条件
     * @return 总共多少条数据
     */
    long countTeacherLogin(RequestTeacherLoginPage page);

    /**
     * 查询老师数据,不限制条数
     *
     * @param request 过滤条件
     * @return 数据
     */
    List<MTeacherLogin> selectTeacherLoginNoLimit(RequestTeacherLoginPage request);

    /**
     * 管理层删除老师
     */
    int deleteManagers(@Param("userIdList") List<String> userIdList, @Param("schoolId") String schoolId);

    /**
     * 添加管理层人员
     */
    int setManagers(@Param("userIdList") List<String> userIdList, @Param("schoolId") String schoolId);

    /**
     * 根据多个ID查询多个老师信息
     */
    List<MUserTeacher> selectByUserIds(@Param("userIdList") List<String> userIdList, @Param("schoolId") String schoolId);

    /**
     * APP接口、WEB平台：修改老师的主授教材
     */
    int updateTeacherBookId(@Param("userId") String userId, @Param("bookId") String bookId, @Param("schoolId") String schoolId);

    /**
     * APP接口、WEB平台：获取老师授课班级
     */
    List<MClassInfo> getTeacherClass(@Param("userId") String userId, @Param("schoolId") String schoolId);

    /**
     * APP接口：删除老师主授教材
     */
    int deleteTeacherBookId(@Param("userId") String userId, @Param("schoolId") String schoolId);

    /**
     * APP接口：删除老师授课教材
     */
    int deleteBookIdByUserId(@Param("userId") String userId, @Param("bookId") String bookId, @Param("schoolId") String schoolId);

    /**
     * APP接口：添加老师课本关联表信息
     */
    void saveTeacherBook(MTeacherBook teacherBook);

    /**
     * APP接口：根据用户ID和课本ID查询老师信息
     */
    MTeacher getByUserIdAndBookId(@Param("userId") String userId, @Param("bookId") String bookId, @Param("schoolId") String schoolId);

    /**
     * APP接口：删除某老师的主授科目（老师课本关联表）
     */
    void deleteByUserIdAndisMain(@Param("userId") String userId, @Param("isMain") int isMain, @Param("schoolId") String schoolId);

    /**
     * APP接口：修改老师课本关联表主授状态
     */
    void updateTeacherBookIsMain(@Param("userId") String userId,
                                 @Param("result") int result,
                                 @Param("schoolId") String schoolId);

    /**
     * 插入老师班级关联信息数据（多条数据插入）
     */
    int insertTeacherClass(@Param("classInfo") List<MTeacherClass> classInfo);

    /**
     * 删除老师与班级的所有关系，通过teacherId和schoolId
     */
    void deleteByTeacherIdAndSchoolId(@Param("teacherId") String teacherId,
                                      @Param("schoolId") String schoolId);

    /**
     * 根据多个老师ID获取老师信息
     */
    List<MUserTeacher> queryTeacherByUserIds(@Param("userIds") List<String> userIds, @Param("schoolId") String schoolId);

    void deleteTeacherClass(@Param("userIdList") List<String> userIdList,
                            @Param("classId") String classId,
                            @Param("schoolId") String schoolId);

    String getMainBookId(@Param("userId") String userId, @Param("schoolId") String schoolId);

    void updateManagerBookToNotMain(@Param("userId") String userId, @Param("schoolId") String schoolId);

    /**
     * 查询是否是主授教材
     *
     * @param userId   用户ID
     * @param bookId   教材ID
     * @param schoolId 学校ID
     * @return isMain 是否是主授教材
     */
    Integer isMain(@Param("userId") String userId, @Param("bookId") String bookId, @Param("schoolId") String schoolId);

    /**
     * App 接口，修改学校名称
     *
     * @param schoolId   学校id
     * @param schoolName 学校名称
     */
    void updateSchoolNameBySchoolId(@Param("schoolId") String schoolId, @Param("schoolName") String schoolName);

    /**
     * 通讯录   分页查询book_id为空的老师数据
     *
     * @param schoolId 学校ID
     * @return 一页数据
     */
    List<MUserTeacher> selectTeacherBySubjectNull(@Param("schoolId") String schoolId);

    /**
     * 通讯录 查询学校管理层数量
     *
     * @param schoolId 学校ID
     * @return 学校管理层数量
     */
    long isManagerCount(@Param("schoolId") String schoolId);

    /**
     * 通讯录 查询学校不是管理层、没有科目老师的数量
     *
     * @param schoolId 学校ID
     * @return 学校其他老师数量
     */
    long nullSubjectManager(@Param("schoolId") String schoolId);

    /**
     * 通讯录 查询学校科目对应的老师数量
     *
     * @param schoolId 学校ID
     * @return MTeacherBookCount
     */
    List<MTeacherBookCount> queryUserBookList(@Param("schoolId") String schoolId);

    /**
     * 通讯录 分页查询学校管理层
     *
     * @param page     分页信息
     * @param schoolId
     * @return MUserTeacher
     */
    List<MUserTeacher> queryAllManagerPage(@Param(value = "page")
                                           Page page, @Param("schoolId") String schoolId);

    /**
     * 通讯录 学生分页查询老师
     *
     * @param page
     * @return
     */
    List<MTeacherLogin> queryContactsTeacherPage(@Param(value = "page")
                                                 Page page, @Param("schoolId") String schoolId,
                                                 @Param("classId") String classId, @Param("limit") Integer limit);

    /**
     * 通讯录 家长查询学生对应老师
     *
     * @param classIds 班级ID
     * @return
     */
    List<MTeacherLogin> queryContactsTeacher(@Param("classIds") List<String> classIds);

    /**
     * 通讯录   分页查询book_id为空的老师数据
     *
     * @param page 过滤条件和分页信息
     * @return 一页数据
     */
    List<MTeacherLogin> selectTeacherContacts(RequestTeacherLoginPage page);


    int updateTeacherPosition(@Param("userIds") List<String> userIds, @Param("positionId") String positionId,
                              @Param("positionName") String positionName, @Param("schoolId") String schoolId);

}