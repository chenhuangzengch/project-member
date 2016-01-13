package net.xuele.member.persist;

import net.xuele.common.page.Page;
import net.xuele.member.domain.MEducationOrganization;
import net.xuele.member.domain.MSchoolDescription;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MEducationOrganizationMapper {
    MEducationOrganization selectByPrimaryKey(String orgId);

    /**
     * APP接口：获取教育机构管理员全部下属单位
     */
    List<MEducationOrganization> getOrgUnitByManagerId(@Param(value = "userId") String userId, @Param(value = "schoolId") String schoolId);

    /**
     * APP接口：获取教育机构人员全部下属单位
     */
    List<MEducationOrganization> getOrgUnitByUserId(@Param(value = "userId") String userId, @Param(value = "schoolId") String schoolId);

    /**
     * 总记录数
     * appCenter中学区单位，学区学校模块需要提供的接口
     *
     * @param mEducationOrganization
     * @return
     */
    long selectOrgCount(MEducationOrganization mEducationOrganization);

    /**
     * appCenter中学区单位，学区学校模块需要提供的接口
     * 根据区划ID获取管辖机构（分页请求）
     * 分页查询
     */
    List<MEducationOrganization> selectMEducationOrganizationPage(@Param(value = "page") Page page,
                                                                  @Param(value = "mEducationOrganization") MEducationOrganization mEducationOrganization);

    /**
     * appCenter中学区单位，学区学校模块需要提供的接口
     * 总记录数
     */
    long selectSchoolCount(String areaId);

    /**
     * 插入一个机构，只提供基本信息如（org_id, name,
     * manager_id, areas_name,
     * areas, status）
     *
     * @param mEducationOrganization
     */
    void insertForAddEduOrg(MEducationOrganization mEducationOrganization);

    /**
     * 根据地区和机构名查询数量
     *
     * @param name
     * @param areaCode
     * @return
     */
    int getEduOrgNumByNameAndArea(@Param("name") String name, @Param("areaCode") String areaCode);

    /**
     * 根据机构id更新表数据
     */
    void updateByPrimaryKey(MEducationOrganization mEducationOrganization);

    /**
     * 获取某个区域下的 学校
     *
     * @param page   分页
     * @param areaId 区域id
     */
    List<MSchoolDescription> querySchoolDescription(@Param("page") Page page, @Param("areaId") String areaId);

    /**
     * 获取学校的校长，老师人数，学生人数等信息
     * @param schoolId
     * @return
     */
    MSchoolDescription getSchoolPrincipal(@Param("schoolId") String schoolId);
}