package net.xuele.member.service.impl;

import net.xuele.common.exceptions.MemberException;
import net.xuele.common.page.Page;
import net.xuele.common.page.PageResponse;
import net.xuele.common.utils.PageUtils;
import net.xuele.member.constant.CacheConstants;
import net.xuele.member.constant.IdentityIdConstants;
import net.xuele.member.constant.UserConstants;
import net.xuele.member.domain.*;
import net.xuele.member.dto.*;
import net.xuele.member.dto.page.SchoolDistrictPageRequest;
import net.xuele.member.persist.*;
import net.xuele.member.service.CacheService;
import net.xuele.member.service.EducationOrganizationService;
import net.xuele.member.service.SeqGeneratorService;
import net.xuele.member.service.UserService;
import net.xuele.member.util.MemberEncryptUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2015/7/17 0017.
 */

public class EducationOrganizationServiceImpl implements EducationOrganizationService {
    @Autowired
    private MEducationOrganizationMapper educationOrganizationMapper;
    @Autowired
    private DAreasMapper areasMapper;
    @Autowired
    private MSchoolMapper schoolMapper;
    @Autowired
    private MEducationManagerMapper educationManagerMapper;
    @Autowired
    private MUsersMapper mUsersMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private SeqGeneratorService seqGeneratorService;
    @Autowired
    private MEducationMapper educationMapper;
    @Autowired
    private MTeacherMapper teacherMapper;
    @Autowired
    private CacheService cacheService;

    private static Logger logger = LoggerFactory.getLogger(EducationOrganizationServiceImpl.class);

    /**
     * APP接口：获取全部下属单位
     */
    @Override
    public List<GroupDTO> queryOrgUnit(String userId, String identityId) {
        List<MEducationOrganization> educationManagerList;
        if (IdentityIdConstants.EDUCATION_MANAGER.equals(identityId)) {//教育机构管理员身份
            educationManagerList = educationOrganizationMapper.getOrgUnitByManagerId(userId, userService.getSchoolId(userId));
        } else if (IdentityIdConstants.EDUCATION_STAFF.equals(identityId)) {//教育机构人员身份
            educationManagerList = educationOrganizationMapper.getOrgUnitByUserId(userId, userService.getSchoolId(userId));
        } else {
            return null;
        }
        List<GroupDTO> groupAppDTOList = new ArrayList<>();
        for (MEducationOrganization cla : educationManagerList) {
            GroupDTO gadto = new GroupDTO();
            gadto.setGroupid(cla.getOrgId());
            gadto.setGroupname(cla.getName());
            groupAppDTOList.add(gadto);
        }
        return groupAppDTOList;
    }

    /**
     * APP接口：没有地区编号则根据用户所在的教育机构地区获取下一级教育机构地区
     * 有则根据地区编号查询下一级教育机构地区
     * 没有下一级教育机构则显示下一级的学校
     */
    @Override
    public List<GroupDTO> queryOrgEducation(String userId, String areaId) {
        if (StringUtils.isEmpty(areaId)) {
            areaId = userService.getByUserId(userId).getArea();
        }
        if (StringUtils.isEmpty(areaId)) {
            return new ArrayList<>(0);
        }
        List<DAreas> areasList = areasMapper.getOrgEducation(areaId);
        List<GroupDTO> groupAppDTOList = new ArrayList<>();
        if (areasList.size() != 0) {
            for (DAreas cla : areasList) {
                GroupDTO gadto = new GroupDTO();
                gadto.setGroupid(cla.getCode());
                gadto.setGroupname(cla.getName());
                groupAppDTOList.add(gadto);
            }
        } else {
            //没有下一级教育机构则获取下一级的学校
            groupAppDTOList = getAreaSchool(areaId);
        }
        return groupAppDTOList;
    }

    //获取某地区的学校
    @Override
    public List<GroupDTO> getAreaSchool(String areaId) {
        List<MSchool> schoolList = schoolMapper.getByAreaId(areaId);
        List<GroupDTO> groupAppDTOList = new ArrayList<>();
        for (MSchool cla : schoolList) {
            GroupDTO gadto = new GroupDTO();
            gadto.setGroupid(cla.getId());
            gadto.setGroupname(cla.getName());
            groupAppDTOList.add(gadto);
        }
        return groupAppDTOList;
    }

    /**
     * 没有地区编号则根据用户所在的教育机构地区获取下一级教育机构地区
     * 有则根据地区编号查询下一级教育机构地区
     * 没有下一级教育机构则显示下一级的学校
     */
    @Override
    public List<GroupDTO> queryOrgEducationNoSchool(String userId, String areaId) {
        if (StringUtils.isEmpty(areaId)) {
            areaId = userService.getByUserId(userId).getArea();
        }
        if (StringUtils.isEmpty(areaId)) {
            return new ArrayList<>(0);
        }
        List<DAreas> areasList = areasMapper.getOrgEducation(areaId);
        List<GroupDTO> groupAppDTOList = new ArrayList<>();
        if (areasList.size() != 0) {
            for (DAreas cla : areasList) {
                GroupDTO gadto = new GroupDTO();
                gadto.setGroupid(cla.getCode());
                gadto.setGroupname(cla.getName());
                groupAppDTOList.add(gadto);
            }
        } else {
            //没有下一级机构则获取当前机构
            DAreas areas = areasMapper.selectByPrimaryKey(areaId);
            GroupDTO groupDTO = new GroupDTO();
            groupDTO.setGroupid(areas.getCode());
            groupDTO.setGroupname(areas.getName());
            groupAppDTOList.add(groupDTO);
        }
        return groupAppDTOList;
    }

    /**
     * APP接口：根据教育机构ID获取该机构管理员
     */
    @Override
    public UserTeacherDTO getEducationManager(String educationId) {
        MUserTeacher userTeacher = educationManagerMapper.getEducationManager(educationId);
        UserTeacherDTO utdto = new UserTeacherDTO();
        BeanUtils.copyProperties(userTeacher, utdto);
        return utdto;
    }

    /**
     * appCenter中学区单位，学区学校模块需要提供的接口
     * 根据区划ID获取下级区划
     *
     * @param areaId 地区
     * @return List<AreaDTO>
     */
    @Override
    public List<AreaDTO> getUnderArea(String areaId) {
        if (StringUtils.isEmpty(areaId)) {
            return new ArrayList<>();
        }
        List<DAreas> areasList = areasMapper.getOrgEducation(areaId);
        List<AreaDTO> groupAppDTOList = new ArrayList<>();
        for (DAreas cla : areasList) {
            AreaDTO areaDTO = new AreaDTO();
            BeanUtils.copyProperties(cla, areaDTO);
            groupAppDTOList.add(areaDTO);
        }
        return groupAppDTOList;
    }

    /**
     * 根据区县ID获取街道信息
     *
     * @param areaId 区域编码
     * @return AreaDTO
     */
    @Override
    public List<AreaDTO> getStreetArea(String areaId) {
        if (StringUtils.isEmpty(areaId)) {
            return new ArrayList<>();
        }
        List<DAreas> areasList = areasMapper.getStreetArea(areaId);
        List<AreaDTO> groupAppDTOList = new ArrayList<>();
        for (DAreas cla : areasList) {
            AreaDTO areaDTO = new AreaDTO();
            BeanUtils.copyProperties(cla, areaDTO);
            groupAppDTOList.add(areaDTO);
        }
        return groupAppDTOList;
    }

    /**
     * appCenter中学区单位，学区学校模块需要提供的接口
     * 根据区划ID获取管辖机构（分页请求）
     */
    @Override
    public PageResponse<OrganizationDTO> getUnderOrganization(SchoolDistrictPageRequest request) {
        //获取管辖机构的总条数
        MEducationOrganization mEducationOrganization = new MEducationOrganization();
        mEducationOrganization.setAreas(request.getAreaId());
        long count = educationOrganizationMapper.selectOrgCount(mEducationOrganization);
        //获取当前页的信息
        Page page = PageUtils.buildPage(request);
        List<MEducationOrganization> mEducationOrganizationList = educationOrganizationMapper.selectMEducationOrganizationPage(page, mEducationOrganization);
          /*
        给返回接口赋值
         */
        List<OrganizationDTO> organizationDTOs = new ArrayList<>();
        for (MEducationOrganization mEducationOrganizations : mEducationOrganizationList) {
            OrganizationDTO organizationDTO = new OrganizationDTO();
            organizationDTO.setOrganizationId(mEducationOrganizations.getOrgId());
            organizationDTO.setOrganizationName(mEducationOrganizations.getName());

            organizationDTOs.add(organizationDTO);
        }

        //返回对象
        PageResponse<OrganizationDTO> pageResponse = new PageResponse<>();
        PageUtils.buldPageResponse(request, pageResponse);
        pageResponse.setRows(organizationDTOs);
        pageResponse.setRecords(count);
        return pageResponse;
    }

    /**
     * appCenter中学区单位，学区学校模块需要提供的接口
     * 获取区划下成员（学校，老师，学生）数量
     */
    @Override
    public AreaMemberAmountDTO getAreaUnderMemberAmount(String areaId) {
        AreaMemberAmountDTO areaMemberAmountDTO = new AreaMemberAmountDTO();
        areaMemberAmountDTO.setSchoolAmount(mUsersMapper.selectSchoolAmount(areaId));
        areaMemberAmountDTO.setTeacherAmount(getTeacherAmount(areaId));
        areaMemberAmountDTO.setStudentAmount(getStudentAmount(areaId));
        return areaMemberAmountDTO;
    }

    /**
     * 缓存：某区域下学生总人数
     *
     * @param areaId 区域id
     * @return 学生总人数
     */
    private long getStudentAmount(String areaId) {
        String key = MessageFormat.format(CacheConstants.KEY_AREA_STUDENT_AMOUNT, areaId);
        Long lo=cacheService.get(key);
        if(lo!=null){
            return lo;
        }
        long sa = mUsersMapper.selectStudentAmount(areaId);
        cacheService.set(key, sa, CacheConstants.CACHE_ONE_DAY, TimeUnit.DAYS);
        return sa;
    }

    /**
     * 缓存：某区域下老师总人数
     *
     * @param areaId 区域id
     * @return 老师总人数
     */
    private long getTeacherAmount(String areaId) {
        String key = MessageFormat.format(CacheConstants.KEY_AREA_TEACHER_AMOUNT, areaId);
        Long lo=cacheService.get(key);
        if(lo!=null){
            return lo;
        }
        long ta = mUsersMapper.selectTeacherAmount(areaId);
        cacheService.set(key, ta, CacheConstants.CACHE_ONE_DAY, TimeUnit.DAYS);
        return ta;
    }

    /**
     * appCenter中学区单位，学区学校模块需要提供的接口
     * 获取区划下学校描述列表（分页）
     */
    @Override
    public PageResponse<SchoolDescriptionDTO> getUnderSchoolDescription(SchoolDistrictPageRequest request) {
        //获取当前页的信息
        Page page = PageUtils.buildPage(request);
        //获取MSchoolDescription对象数据
        List<MSchoolDescription> sdList = educationOrganizationMapper.querySchoolDescription(page, request.getAreaId());
        List<SchoolDescriptionDTO> sddtoList = new ArrayList<>();
        for (MSchoolDescription sd : sdList) {
            String key = MessageFormat.format(CacheConstants.KEY_SCHOOL_IN_AREA, sd.getSchoolId());
            SchoolDescriptionDTO dto=cacheService.get(key);
            if (dto != null) {
                sddtoList.add(dto);
            }else {
                SchoolDescriptionDTO sddto = new SchoolDescriptionDTO();
                BeanUtils.copyProperties(sd, sddto);
                MSchoolDescription mSchoolDescription = educationOrganizationMapper.getSchoolPrincipal(sd.getSchoolId());
                if (mSchoolDescription != null) {
                    sddto.setUserId(mSchoolDescription.getUserId());
                    sddto.setSchoolMaster(mSchoolDescription.getSchoolMaster());
                    sddto.setStudentAmount(mSchoolDescription.getStudentAmount());
                    sddto.setTeacherAmount(mSchoolDescription.getTeacherAmount());
                }
                sddtoList.add(sddto);
                cacheService.set(key, sddto, CacheConstants.CACHE_ONE_DAY, TimeUnit.DAYS);
            }
        }
        //返回对象
        PageResponse<SchoolDescriptionDTO> pageResponse = new PageResponse<>();
        PageUtils.buldPageResponse(request, pageResponse);
        pageResponse.setRows(sddtoList);
        //获取区划下学校描述的总条数
        long count = educationOrganizationMapper.selectSchoolCount(request.getAreaId());
        pageResponse.setRecords(count);
        return pageResponse;
    }

    /**
     * APP接口：根据学校名称搜索该用户所在地区下的学校
     */
    @Override
    public List<SchoolDTO> querySchoolByName(String userId, String schoolName) {
        String areaId = userService.getByUserId(userId).getArea();
        if (StringUtils.isEmpty(areaId)) {
            return new ArrayList<>(0);
        }
        return querySchoolByNameAndArea(areaId, schoolName);
    }

    @Override
    public List<SchoolDTO> querySchoolByNameAndArea(String areaId, String schoolName) {
        List<MSchool> schoolList = schoolMapper.selectSchoolByName(schoolName, areaId);
        List<SchoolDTO> schoolAppDTOList = new ArrayList<>();
        for (MSchool school : schoolList) {
            SchoolDTO sadto = new SchoolDTO();
            BeanUtils.copyProperties(school, sadto);
            schoolAppDTOList.add(sadto);
        }
        return schoolAppDTOList;
    }

    @Override
    public List<SchoolDTO> querySchoolByStreetArea(String streetArea) {
        List<MSchool> schoolList = schoolMapper.querySchoolByStreetArea(streetArea);
        List<SchoolDTO> schoolAppDTOList = new ArrayList<>();
        for (MSchool school : schoolList) {
            SchoolDTO sadto = new SchoolDTO();
            BeanUtils.copyProperties(school, sadto);
            schoolAppDTOList.add(sadto);
        }
        return schoolAppDTOList;
    }

    @Override
    public EducationOrganAndManagerDTO saveEducationOrgAndManager(EducationOrganAndManagerDTO educationOrganAndManagerDTO) {
        //添加机构时，添加管理员账号
        //涉及表有m_education_organization,m_users,m_education_manager

        MEducationOrganization mEducationOrganization = new MEducationOrganization();
        String areaCode = educationOrganAndManagerDTO.getAreaCode();
        String areaName;
        if (!StringUtils.isEmpty(areaCode)) {
            mEducationOrganization.setAreas(educationOrganAndManagerDTO.getAreaCode());
            areaName = areasMapper.selectByPrimaryKey(areaCode).getName();
            mEducationOrganization.setAreasName(areaName);
        } else {
            throw new MemberException("地区编码不能为空");
        }

        if (educationOrganizationMapper.getEduOrgNumByNameAndArea(educationOrganAndManagerDTO.getEduOrgName(), educationOrganAndManagerDTO.getAreaCode()) > 0) {
            logger.error("该地区已存在名为{}的教育机构", educationOrganAndManagerDTO.getEduOrgName());
            throw new MemberException("该地区已存在名为" + educationOrganAndManagerDTO.getEduOrgName() + "的教育机构");
        }

        UserDTO userDTO = new UserDTO();
        copyUserDTO(educationOrganAndManagerDTO, userDTO);
        if (StringUtils.isEmpty(educationOrganAndManagerDTO.getRealName())) {
            userDTO.setRealName("机构管理员");
        }
        userDTO.setArea(educationOrganAndManagerDTO.getAreaCode());
        userDTO.setIdentityId(IdentityIdConstants.EDUCATION_MANAGER);
        userDTO.setSchoolId("0");
        userDTO = userService.saveUser(userDTO);
        String eduOrgId = UUID.randomUUID().toString();
        eduOrgId = eduOrgId.replaceAll("\\-", "");
        mEducationOrganization.setOrgId(eduOrgId);
        String eduOrgName = educationOrganAndManagerDTO.getEduOrgName();
        if (!StringUtils.isEmpty(eduOrgName)) {
            mEducationOrganization.setName(eduOrgName);
        } else {
            logger.debug("机构名称不能为空！");
            throw new MemberException("机构名称不能为空！");
        }
        mEducationOrganization.setManagerId(userDTO.getUserId());
        mEducationOrganization.setStatus(1);
        educationOrganAndManagerDTO.setPassword(MemberEncryptUtil.aesDecrypt(UserConstants.INIT_PASSWORD));

        educationOrganizationMapper.insertForAddEduOrg(mEducationOrganization);

        MEducationManager mEducationManager = new MEducationManager();
        mEducationManager.setUserId(userDTO.getUserId());
        mEducationManager.setEducationalId(eduOrgId);
        mEducationManager.setEducationalName(eduOrgName);
        mEducationManager.setSchoolId("0");
        educationManagerMapper.insert(mEducationManager);

        educationOrganAndManagerDTO.setUserId(userDTO.getUserId());
        educationOrganAndManagerDTO.setEduOrgId(eduOrgId);
        educationOrganAndManagerDTO.setAreaName(areaName);
        return educationOrganAndManagerDTO;
    }


    @Override
    public EducationOrganAndManagerDTO updateEducationOrgAndManager(EducationOrganAndManagerDTO educationOrganAndManagerDTO) {
        MEducationOrganization mEducationOrganization = new MEducationOrganization();
        String eduOrgid = educationOrganAndManagerDTO.getEduOrgId();
        if (StringUtils.isEmpty(eduOrgid)) {
            throw new MemberException("机构id不能为空");
        }
        if (educationOrganizationMapper.selectByPrimaryKey(eduOrgid) == null) {
            throw new MemberException("库中没有该id的教育机构");
        }
        mEducationOrganization.setOrgId(eduOrgid);
        String areaCode = educationOrganAndManagerDTO.getAreaCode();
        String areaName;
        if (areaCode != null) {
            mEducationOrganization.setAreas(areaCode);
            areaName = areasMapper.selectByPrimaryKey(areaCode).getName();
            mEducationOrganization.setAreasName(areaName);
        }

        String orgName = educationOrganAndManagerDTO.getEduOrgName();
        if ((orgName != null) && (areaCode != null)) {
            if (educationOrganizationMapper.getEduOrgNumByNameAndArea(orgName, areaCode) > 0) {
                throw new MemberException("该地区已存在名为" + educationOrganAndManagerDTO.getEduOrgName() + "的教育机构");
            }
        }
        mEducationOrganization.setName(orgName);
        educationOrganizationMapper.updateByPrimaryKey(mEducationOrganization);
        if (StringUtils.isNotEmpty(orgName)) {
            educationManagerMapper.updateByEducationalId(eduOrgid, orgName);
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(educationOrganAndManagerDTO.getUserId());
        copyUserDTO(educationOrganAndManagerDTO, userDTO);
        String schoolId = userService.getSchoolId(userDTO.getUserId());
        String qq = educationOrganAndManagerDTO.getQq();
        if (!StringUtils.isEmpty(qq)) {
            userDTO.setQq(qq);
        }
        String mobile = educationOrganAndManagerDTO.getMobile();
        if (!StringUtils.isEmpty(mobile)) {
            userDTO.setMobile(mobile);
        }

        String email = educationOrganAndManagerDTO.getEmail();
        if (!StringUtils.isEmpty(email)) {
            userDTO.setEmail(email);
        }
        userDTO.setSchoolId(schoolId);
        userService.updateByUserId(userDTO);
        return educationOrganAndManagerDTO;
    }


    @Override
    public EducationOrganAndManagerDTO getEduOrgAndManagerInfo(String eduOrgId) {
        if (StringUtils.isEmpty(eduOrgId)) {
            logger.debug("不存在机构id为" + eduOrgId + "的数据");
            throw new MemberException("机构id不能为空");
        }
        MEducationOrganization mEducationOrganization = educationOrganizationMapper.selectByPrimaryKey(eduOrgId);
        if (mEducationOrganization == null) {
            logger.debug("不存在机构id为" + eduOrgId + "的数据");
            return null;
            //throw new MemberException("不存在机构id为" + eduOrgId + "的数据");
        }
        String userId = mEducationOrganization.getManagerId();
        if (StringUtils.isEmpty(userId)) {
            logger.debug("该机构暂时未设定管理员");
            throw new MemberException("该机构暂时未设定管理员");
        }
        EducationOrganAndManagerDTO educationOrganAndManagerDTO = new EducationOrganAndManagerDTO();
        UserDTO userDTO = userService.getByUserId(userId);
        educationOrganAndManagerDTO.setUserId(userId);
        educationOrganAndManagerDTO.setQq(userDTO.getQq());
        educationOrganAndManagerDTO.setMobile(userDTO.getMobile());
        educationOrganAndManagerDTO.setEmail(userDTO.getEmail());
        educationOrganAndManagerDTO.setEduOrgName(mEducationOrganization.getName());

        educationOrganAndManagerDTO.setEduOrgId(eduOrgId);
        educationOrganAndManagerDTO.setAreaCode(mEducationOrganization.getAreas());
        educationOrganAndManagerDTO.setAreaName(mEducationOrganization.getAreasName());

        return educationOrganAndManagerDTO;
    }

    /**
     * 根据机构用户id获取机构信息
     */
    @Override
    public OrganizationDTO getOrganizationByUserId(String userId) {
        UserDTO user = userService.getByUserId(userId);
        OrganizationDTO organizationDTO = new OrganizationDTO();
        if (IdentityIdConstants.EDUCATION_MANAGER.equals(user.getIdentityId())) {//教育机构管理员身份
            MEducationManager educationManager = educationManagerMapper.selectByPrimaryKey(userId);
            organizationDTO.setOrganizationId(educationManager.getEducationalId());
            organizationDTO.setOrganizationName(educationManager.getEducationalName());
        } else if (IdentityIdConstants.EDUCATION_STAFF.equals(user.getIdentityId())) {//教育机构人员身份
            MEducation education = educationMapper.selectByPrimaryKey(userId);
            organizationDTO.setOrganizationId(education.getEducationalId());
            organizationDTO.setOrganizationName(education.getEducationalName());
        }
        return organizationDTO;
    }


    private void copyUserDTO(EducationOrganAndManagerDTO educationOrganAndManagerDTO, UserDTO userDTO) {
        String qq = educationOrganAndManagerDTO.getQq();
        if (!StringUtils.isEmpty(qq)) {
            userDTO.setQq(qq);
        }
        String email = educationOrganAndManagerDTO.getEmail();
        if (!StringUtils.isEmpty(email)) {
            userDTO.setEmail(email);
        }
        String mobile = educationOrganAndManagerDTO.getMobile();
        if (!StringUtils.isEmpty(mobile)) {
            userDTO.setMobile(mobile);
        }
    }


    @Override
    public void updateManagerBook(String userId, String newBookId) {

        String schoolId = userService.getSchoolId(userId);
        teacherMapper.deleteBookIdByUserId(userId, newBookId, schoolId);
        teacherMapper.updateManagerBookToNotMain(userId, schoolId);
        MTeacherBook teacherBook = new MTeacherBook();
        Long id = seqGeneratorService.generate("m_teacher_book");
        teacherBook.setId(id);
        teacherBook.setUserId(userId);
        teacherBook.setBookId(newBookId);
        teacherBook.setSchoolId(schoolId);
        teacherBook.setIsMain(1);
        teacherMapper.saveTeacherBook(teacherBook);
    }

    /**
     * 根据省级地区编号查询所有的市区级
     */
    @Override
    public List<ProvincialAreaDTO> queryProvincialArea(String areaId) {
        List<ProvincialAreaDTO> padtoList = new ArrayList<>();
        List<DAreas> areaList = areasMapper.getOrgEducation(areaId);
        for (DAreas areas : areaList) {
            ProvincialAreaDTO padto = new ProvincialAreaDTO();
            padto.setCode(areas.getCode());
            padto.setName(areas.getName());

            List<DAreas> list = areasMapper.getOrgEducation(areas.getCode());
            List<AreaDTO> areaDTOList = new ArrayList<>();
            for (DAreas ar : list) {
                AreaDTO area = new AreaDTO();
                area.setCode(ar.getCode());
                area.setName(ar.getName());
                areaDTOList.add(area);
            }
            padto.setArea(areaDTOList);
            padtoList.add(padto);
        }
        return padtoList;
    }
}
