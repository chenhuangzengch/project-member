package net.xuele.member.persist;

import net.xuele.common.page.Page;
import net.xuele.member.domain.MClass;
import net.xuele.member.domain.MClassStudent;
import net.xuele.member.domain.MSchool;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MSchoolMapper {

    int delete(List<String> ids);

    int insert(MSchool school);

    /**
     * 根据学校ID查询学校信息
     */
    MSchool selectByPrimaryKey(@Param("schoolId") String schoolId);

    long selectCount(MSchool school);

    int update(MSchool school);

    List<MSchool> selectSchoolPage(@Param(value = "pageSize") int pageSize, @Param(value = "page") Page page,@Param(value = "school") MSchool school);

    /**
     * 班级信息
     */
    List<MClassStudent> selectAllClass(@Param(value = "record") MClass record, @Param(value = "minYears") int minYears);

    /**
     * APP接口：获取某地区的全部学校
     */
    List<MSchool> getByAreaId(String areaId);

    /**
     * APP接口：根据学校名称搜索该用户所在地区下的学校
     */
    List<MSchool> selectSchoolByName(@Param(value = "schoolName")String schoolName, @Param(value = "areaId")String areaId);

    /**
     * 根据街道获取该街道下的学校
     */
    List<MSchool> querySchoolByStreetArea(@Param(value = "streetArea")String streetArea);

    /**
     * APP接口：根据学校名和区域编码查看学校数量
     * @param schoolName
     * @param areaId
     * @return
     */
    int getSchoolNumByNameAndAreaId(@Param(value = "schoolName")String schoolName, @Param(value = "areaId")String areaId);

    /**
     * @user circle
     * 根据schoolIds获取学校名
     * getSchoolNamesByIds
     */
    List<String> getSchoolNamesByIds(@Param("schoolIds") List<String> schoolIds);
}