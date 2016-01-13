package net.xuele.member.persist;

import net.xuele.member.domain.MSchoolManager;
import net.xuele.member.domain.MSchoolManagerPrincipal;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MSchoolManagerMapper {
    int deleteByPrimaryKey(String userId);

    int insert(MSchoolManager record);
    /**
     * APP接口：根据用户ID获取该用户所在的学校
     */
    MSchoolManager selectByPrimaryKey(String userId);

    List<MSchoolManager> selectAll();

    int updateByPrimaryKey(MSchoolManager record);

    /**通知接口用
     * APP接口、WEB平台：根据学校ID获取学校的管理员和校长
     * @param schoolId
     * @return
     */
    List<MSchoolManagerPrincipal> querySchoolManagerAndPrincipal(@Param("schoolId") String schoolId);


    /**
     * App 接口，修改学校名称
     * @param schoolId 学校id
     * @param schoolName 学校名称
     */
    void updateSchoolNameBySchoolId(@Param("schoolId")String schoolId, @Param("schoolName")String schoolName);
}