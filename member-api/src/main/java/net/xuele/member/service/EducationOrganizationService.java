package net.xuele.member.service;

import net.xuele.common.page.PageResponse;
import net.xuele.member.dto.*;
import net.xuele.member.dto.page.SchoolDistrictPageRequest;

import java.util.List;

/**
 * Created by zhongjian.xu on 2015/7/17 0017.
 */
public interface EducationOrganizationService {

    /**
     * APP接口：获取全部下属单位
     *
     * @param userId     用户ID
     * @param identityId 用户身份
     * @return 单位ID，单位名称
     */
    List<GroupDTO> queryOrgUnit(String userId, String identityId);

    /**
     * APP接口：
     * 没有地区编号则根据用户所在的教育机构地区获取下一级教育机构地区
     * 有则根据地区编号查询下一级教育机构地区
     * 没有下一级教育机构则显示下一级的学校
     *
     * @param userId 用户ID
     * @return 地区ID，地区名称
     */
    List<GroupDTO> queryOrgEducation(String userId, String areaId);

    /**
     * 没有地区编号则根据用户所在的教育机构地区获取下一级教育机构地区
     * 有则根据地区编号查询下一级教育机构地区
     * 没有下一级教育机构则显示当前机构
     *
     * @param userId 用户ID
     * @return 地区ID，地区名称
     */
    List<GroupDTO> queryOrgEducationNoSchool(String userId, String areaId);

    /**
     * 1.APP接口：根据学校名称搜索该用户所在地区下的学校
     * 2.通知接口调用
     *
     * @param userId     用户ID
     * @param schoolName 学校名称
     * @return 学校信息
     */
    List<SchoolDTO> querySchoolByName(String userId, String schoolName);

    /**
     * 根据地区获取该地区下的学校给前端页面
     *
     * @param area       地区id
     * @param schoolName 学校名称
     * @return 学校信息
     */
    List<SchoolDTO> querySchoolByNameAndArea(String area, String schoolName);

    /**
     * 根据街道获取该街道下的学校
     *
     * @param streetArea       街道id
     * @return 学校信息
     */
    List<SchoolDTO> querySchoolByStreetArea(String streetArea);

    /**
     * 通知接口调用
     * 根据地区编号获取该地区的所有学校
     *
     * @param areaId 地区编号
     * @return List<GroupDTO>所有学校
     */
    List<GroupDTO> getAreaSchool(String areaId);

    /**
     * APP接口：根据教育机构ID获取该机构管理员
     *
     * @param educationId 教育机构ID
     * @return 管理员信息
     */
    UserTeacherDTO getEducationManager(String educationId);


    /**
     * appCenter中学区单位，学区学校模块需要提供的接口
     * 根据区划ID获取下级区划
     *
     * @param areaId 区域编码
     * @return List<AreaDTO> 下级区划
     */
    List<AreaDTO> getUnderArea(String areaId);

    /**
     * 活动接口
     * 根据区县ID获取街道信息
     *
     * @param areaId 区域编码
     * @return List<AreaDTO> 下级街道
     */
    List<AreaDTO> getStreetArea(String areaId);
    /**
     * appCenter中学区单位，学区学校模块需要提供的接口
     * 根据区划ID获取管辖机构（分页请求）
     *
     * @param request 请求
     * @return PageResponse<OrganizationDTO>获取管辖机构
     */
    PageResponse<OrganizationDTO> getUnderOrganization(SchoolDistrictPageRequest request);

    /**
     * appCenter中学区单位，学区学校模块需要提供的接口
     * 获取区划下成员（学校，老师，学生）数量
     *
     * @param areaId 区域编码
     * @return AreaMemberAmountDTO 区划下成员（学校，老师，学生）数量
     */
    AreaMemberAmountDTO getAreaUnderMemberAmount(String areaId);

    /**
     * appCenter中学区单位，学区学校模块需要提供的接口
     * 获取区划下学校描述列表（分页）
     *
     * @param request 请求
     * @return PageResponse<SchoolDescriptionDTO> 学校描述
     */
    PageResponse<SchoolDescriptionDTO> getUnderSchoolDescription(SchoolDistrictPageRequest request);

    /**
     * APP接口：添加一个教育结构
     *
     * @param educationOrganAndManagerDTO 传递参数有（地区编码，机构名称，管理员联系方式（qq，mobile，email））
     * @return 机构信息（id，名称，状态）、管理员信息（userId，password）
     */
    EducationOrganAndManagerDTO saveEducationOrgAndManager(EducationOrganAndManagerDTO educationOrganAndManagerDTO);

    /**
     * APP接口：更新机构信息
     *
     * @param educationOrganAndManagerDTO {@link EducationOrganAndManagerDTO} 参数有，（机构id，地区编码，机构名称，管理员id、管理员联系方式（qq，mobile，email））
     *                                    不修改地区和机构名时请不要传递这两项值
     * @return 机构信息，管理员信息
     */
    EducationOrganAndManagerDTO updateEducationOrgAndManager(EducationOrganAndManagerDTO educationOrganAndManagerDTO);


    /**
     * APP接口：查询机构信息
     *
     * @param eduOrgId 传入机构id
     * @return {@link EducationOrganAndManagerDTO} 包含机构信息（id，区域信息），和管理员信息（id，联系方式）
     */
    EducationOrganAndManagerDTO getEduOrgAndManagerInfo(String eduOrgId);

    /**
     * 根据机构用户id获取机构信息
     * @user circle
     * @param userId 用户id
     * @return 机构id,机构名称
     */
    OrganizationDTO getOrganizationByUserId(String userId);

    void updateManagerBook(String userId, String newBookId);

    /**
     * 根据省级地区编号查询所有的市区级
     * @param areaId 省级地区编号
     * @return 市区级
     */
    List<ProvincialAreaDTO> queryProvincialArea(String areaId);
}
