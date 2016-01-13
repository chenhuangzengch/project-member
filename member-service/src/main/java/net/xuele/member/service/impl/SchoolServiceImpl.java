package net.xuele.member.service.impl;


import net.xuele.common.exceptions.MemberException;
import net.xuele.common.page.Page;
import net.xuele.common.page.PageResponse;
import net.xuele.common.utils.MemberAssert;
import net.xuele.common.utils.PageUtils;
import net.xuele.member.constant.*;
import net.xuele.member.context.MemberLogicContext;
import net.xuele.member.domain.*;
import net.xuele.member.dto.*;
import net.xuele.member.dto.page.SchoolPageRequest;
import net.xuele.member.persist.DAreasMapper;
import net.xuele.member.persist.MSchoolManagerMapper;
import net.xuele.member.persist.MSchoolMapper;
import net.xuele.member.persist.MSchoolPeriodMapper;
import net.xuele.member.service.SchoolPeriodService;
import net.xuele.member.service.SchoolService;
import net.xuele.member.service.SeqGeneratorService;
import net.xuele.member.service.UserService;
import net.xuele.member.service.mq.MessageProducer;
import net.xuele.member.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhongjian.xu on 2015/6/8 0008.
 */

public class SchoolServiceImpl implements SchoolService {
    private static Logger logger = LoggerFactory.getLogger(SchoolServiceImpl.class);
    @Autowired
    private MSchoolMapper schoolMapper;
    @Autowired
    private MSchoolPeriodMapper schoolPeriodMapper;
    @Autowired
    private MSchoolManagerMapper schoolManagerMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private SchoolPeriodService schoolPeriodService;
    @Autowired
    private DAreasMapper dAreasMapper;
    @Autowired
    private SeqGeneratorService seqGeneratorService;
    @Autowired
    private MessageProducer messageProducer;

    /**
     * 添加学校信息
     */
    private int save(SchoolDTO schoolDTO) {
        MSchool school = new MSchool();
        BeanUtils.copyProperties(schoolDTO, school);
        String schoolId = school.getId();
        school.setAddTime(new Date());
        MSchool s = schoolMapper.selectByPrimaryKey(schoolId);
        if (s == null) {
            return schoolMapper.insert(school);
        } else {
            throw new MemberException("该学校id已存在");
        }
    }

    /**
     * 根据学校ID查询学校信息
     */
    @Override
    public SchoolDTO getBySchoolId(String id) {
        MSchool school = schoolMapper.selectByPrimaryKey(id);
        if (school == null) {
            return null;
        }
        SchoolDTO schoolDTO = new SchoolDTO();
        BeanUtils.copyProperties(school, schoolDTO);
        return schoolDTO;
    }

    /**
     * 根据ID更新学校信息
     */
    @Override
    public int update(SchoolDTO schoolDTO) {
        MemberAssert.notNull(schoolDTO.getId(), "信息ID为空，无法更新学校信息", MemberLogicContext.ERROR_CODE_UPDATE_SCHOOL_ID_IS_NULL);
        MSchool school = new MSchool();
        BeanUtils.copyProperties(schoolDTO, school);
        return schoolMapper.update(school);
    }

    /**
     * 学校信息分页查询
     */
    @Override
    public PageResponse<SchoolDTO> querySchoolPage(SchoolPageRequest schoolPageRequest) {
        //获取需要学校信息的总条数
        MSchool school = new MSchool();
        BeanUtils.copyProperties(schoolPageRequest, school);
        long count = schoolMapper.selectCount(school);
        //获取当前页的学校信息
        Page page = PageUtils.buildPage(schoolPageRequest);
        int pageSize = schoolPageRequest.getPageSize();
        List<MSchool> schoolList = schoolMapper.selectSchoolPage(pageSize, page, school);
        List<SchoolDTO> schoolDTOList = entityListToDtoList(schoolList);
        //返回对象
        PageResponse<SchoolDTO> pageResponse = new PageResponse<>();
        PageUtils.buldPageResponse(schoolPageRequest, pageResponse);
        pageResponse.setRows(schoolDTOList);
        pageResponse.setRecords(count);
        return pageResponse;
    }

    private List<SchoolDTO> entityListToDtoList(List<MSchool> schoolList) {
        List<SchoolDTO> schoolDTOList = new ArrayList<>();
        for (MSchool school : schoolList) {
            SchoolDTO schoolDTO = new SchoolDTO();
            BeanUtils.copyProperties(school, schoolDTO);
            schoolDTOList.add(schoolDTO);
        }
        return schoolDTOList;
    }

    /**
     * 根据学校id获取该学校的年级列表
     */
    @Override
    @Transactional(readOnly = false)
    public List<GradeDTO> queryGrade(String schoolId) {
        Date now = new Date();
        int pastHalf = isPastHalf(now) ? 1 : 0;
        int currentYear = DateUtil.getYear(now);
        //学校不存在，默认给它12个年级
        if (schoolId.equals("0")) {
            List<GradeDTO> gradeDTOList = new ArrayList<>(12);
            for (int i = 1; i <= 12; i++) {
                GradeDTO gradeDTO = new GradeDTO();
                gradeDTO.setLevel(i);
                gradeDTO.setId(currentYear - i + pastHalf);
                gradeDTOList.add(gradeDTO);
            }
            return gradeDTOList;
        }
        int lengthOfSchool = 0;
        List<MSchoolPeriod> periods = schoolPeriodMapper.selectBySchoolId(schoolId);
        for (MSchoolPeriod period : periods) {
            lengthOfSchool += period.getLength();
        }
        MemberAssert.isTrue(periods.size() > 0 && periods.size() < 4, "学制设置错误", MemberLogicContext.ERROR_CODE_SCHOOL_PERIOD_SIZE);
        MSchoolPeriod first = periods.get(0);
        int limit = 0;
        if (first.getSection() == 1) {//小学不办,办初中
            limit = 9 - first.getLength();
        } else if (first.getSection() == 2) {//小学初中不办,办高中
            limit = 9;
        }
        lengthOfSchool += limit;
        List<GradeDTO> gradeDTOList = new ArrayList<>(lengthOfSchool - limit);
        for (int i = limit + 1, len = lengthOfSchool; i <= len; i++) {
            GradeDTO gradeDTO = new GradeDTO();
            gradeDTO.setLevel(i);
            gradeDTO.setId(currentYear - i + pastHalf);
            gradeDTOList.add(gradeDTO);
        }
        return gradeDTOList;
    }

    /**
     * 根据年级查询出班级
     */
    @Override
    public List<ClassStudentDTO> queryAllClass(String schoolId, String year) {
        ClassDTO classDTO = new ClassDTO();
        classDTO.setSchoolId(schoolId);
        if (StringUtils.isEmpty(year)) {
            classDTO.setYears(null);
        } else {
            classDTO.setYears(Integer.parseInt(year));
        }
        List<MClassStudent> classStudentList = schoolMapper.selectAllClass(parseToMClass(classDTO), getMinYear(classDTO));
        List<ClassStudentDTO> classStudentDTOList = new ArrayList<>();
        for (MClassStudent ms : classStudentList) {
            ClassStudentDTO csDTO = new ClassStudentDTO();
            BeanUtils.copyProperties(ms, csDTO);
            classStudentDTOList.add(csDTO);
        }
        return classStudentDTOList;
    }

    //这个时刻是不是录入学生后,现在以8月1日为准,超过此日期返回true
    private boolean isPastHalf(Date now) {
        String monthDay = DateFormatUtils.format(now, "MMdd");
        return monthDay.compareTo(ClassConstants.YEAR_SEPARATOR) >= 0;
    }

    /**
     * 获取毕业的班级学界
     * 比如现在是2015-6,2014年入学,是一年级,索引是0;2012是三年级,索引是2;
     * 现在是2015-9,2014年入学,是二年级,索引是1;
     */
    private int getMinYear(ClassDTO classDTO) {
        Date now = new Date();
        int pastHalf = isPastHalf(now) ? 1 : 0;
        int currentYear = DateUtil.getYear(now);
        int lengthOfSchool = 0;
        List<MSchoolPeriod> periods = schoolPeriodMapper.selectBySchoolId(classDTO.getSchoolId());
        for (MSchoolPeriod period : periods) {
            lengthOfSchool += period.getLength();
        }
        MemberAssert.isTrue(periods.size() > 0 && periods.size() < 4, "学制设置错误", MemberLogicContext.ERROR_CODE_SCHOOL_PERIOD_SIZE);
        MSchoolPeriod first = periods.get(0);
        int limit = 0;
        if (first.getSection() == 1) {//小学不办,办初中
            limit = 9 - first.getLength();
        } else if (first.getSection() == 2) {//小学初中不办,办高中
            limit = 9;
        }
        lengthOfSchool += limit;
        return currentYear - lengthOfSchool + pastHalf;
    }

    private MClass parseToMClass(ClassDTO classDTO) {
        MClass mClass = new MClass();
        BeanUtils.copyProperties(classDTO, mClass);
        return mClass;
    }

    /**
     * 添加学校，并新建管理员账号
     */
    @Override
    public SchoolAndManageDTO saveCreateSchoolAndManager(SchoolAndManageDTO schoolAndManageDTO, ArrayList<SchoolPeriodDTO> schoolPeriodInfos) {
        schoolAndManageDTO.setName(schoolAndManageDTO.getName().replaceAll("\\s", ""));
        //涉及到的表
        // m_school、m_school_period、m_users、m_school_manager、
        // 新建表理员
        if (schoolAndManageDTO.getStreetArea() == null) {
            schoolAndManageDTO.setStreetArea("");
        }
        if (schoolAndManageDTO.getStreetAreaName() == null) {
            schoolAndManageDTO.setStreetAreaName("");
        }
        SchoolDTO schoolDTO = new SchoolDTO();
        UserDTO userDTO = new UserDTO();
        String schoolId = seqGeneratorService.generate("m_school").toString();
        userDTO.setSchoolId(schoolId);
        if (StringUtils.isEmpty(schoolAndManageDTO.getArea())) {
            logger.warn("地区不能为空！");
            throw new MemberException("地区不能为空！");
        }
        //判断该学校是否存在，通过区域和学校名来判定
        if (schoolAndManageDTO.getAreaName() == null) {
            String areaName = dAreasMapper.selectByPrimaryKey(schoolAndManageDTO.getArea()).getName();
            schoolAndManageDTO.setAreaName(areaName);
            schoolDTO.setAreaName(areaName);
        }

        if (schoolMapper.getSchoolNumByNameAndAreaId(schoolAndManageDTO.getName(), schoolAndManageDTO.getArea()) > 0) {
            throw new MemberException("该地区已包含名为" + schoolAndManageDTO.getName() + "的学校！");
        }
        userDTO.setIdentityId(IdentityIdConstants.SCHOOL_MANAGER);
        if (StringUtils.isNotEmpty(schoolAndManageDTO.getManagerMobile())) {
            userDTO.setMobile(schoolAndManageDTO.getManagerMobile());
        }
        if (StringUtils.isNotEmpty(schoolAndManageDTO.getManagerName())) {
            userDTO.setRealName(schoolAndManageDTO.getManagerName());
        } else {
            userDTO.setRealName("学校管理员");
        }
        if (StringUtils.isNotEmpty(schoolAndManageDTO.getArea())) {
            userDTO.setArea(schoolAndManageDTO.getArea());
        }

        userService.saveUser(userDTO);

        schoolAndManageDTO.setPassword(UserConstants.INIT_PASSWORD);
        schoolAndManageDTO.setManager(userDTO.getUserId());

        MSchoolManager mSchoolManager = new MSchoolManager();
        mSchoolManager.setUserId(userDTO.getUserId());

        schoolAndManageDTO.setId(schoolId);
        mSchoolManager.setSchoolId(schoolId);
        if (schoolAndManageDTO.getName() != null) {
            mSchoolManager.setSchoolName(schoolAndManageDTO.getName());
        } else {
            logger.info("学校名不能为空！");
            throw new MemberException("学校名称不能为空！");
        }
        //schoolManagerDTO需要学校名，学校id，和学乐号
        schoolManagerMapper.insert(mSchoolManager);

        // 向m_school_period表中插入学校学段信息
        int length;
        PeriodDTO periodDTO = new PeriodDTO();
        //用于标示学校的学段,1表示小学,2表示中学,4表示高中.
        int length_of_school = 0;
        for (SchoolPeriodDTO schoolPeriodDTO : schoolPeriodInfos) {
            switch (schoolPeriodDTO.getSection()) {
                case 0:
                    length = schoolPeriodDTO.getLength();
                    if (length == 0) {
                    } else if (length != 5 && length != 6) {
                        throw new MemberException("小学学段只能为5或6");
                    } else {
                        periodDTO.setPrimary(length);
                        length_of_school += 1;
                    }
                    break;
                case 1:
                    length = schoolPeriodDTO.getLength();
                    if (length == 0) {
                    } else if (length != 3 && length != 4) {
                        throw new MemberException("中学学段只能为3或4");
                    } else {
                        periodDTO.setJunior(length);
                        length_of_school += 2;
                    }
                    break;
                case 2:
                    length = schoolPeriodDTO.getLength();
                    if (length == 0) {
                    } else if (length != 3) {
                        throw new MemberException("高中学段只能为3");
                    } else {
                        periodDTO.setSenior(length);
                        length_of_school += 4;
                    }
                    break;
                default:
                    break;
            }
        }
        schoolPeriodService.savePeriod(periodDTO, schoolAndManageDTO.getId(), false);
        BeanUtils.copyProperties(schoolAndManageDTO, schoolDTO);

        schoolDTO.setLengthOfSchool(length_of_school);
        schoolDTO.setManagerName(userDTO.getRealName());
        //插入学校信息
        save(schoolDTO);
        return schoolAndManageDTO;
    }

    /**
     * 修改学校信息
     */
    @Override
    public SchoolAndManageDTO updateSchoolAndManager(SchoolAndManageDTO schoolAndManageDTO, ArrayList<SchoolPeriodDTO> schoolPeriodInfos) {
        long start = System.currentTimeMillis();
        if (StringUtils.isNoneBlank(schoolAndManageDTO.getName())) {
            schoolAndManageDTO.setName(schoolAndManageDTO.getName().replaceAll("\\s", ""));
        }
        SchoolDTO schoolDTO = new SchoolDTO();
        BeanUtils.copyProperties(schoolAndManageDTO, schoolDTO);
        String schoolId = schoolDTO.getId();
        logger.info("start updateSchoolAndManger info,schoolId is {}", schoolId);
        if (StringUtils.isEmpty(schoolId)) {
            logger.info("schoolId 不能不为空！");
            throw new MemberException("schoolId 不能不为空！");
        }
        MSchool mSchool = schoolMapper.selectByPrimaryKey(schoolId);
        if (StringUtils.isNotEmpty(schoolAndManageDTO.getName()) && !mSchool.getName().equals(schoolAndManageDTO.getName())) {
            if (schoolMapper.getSchoolNumByNameAndAreaId(schoolAndManageDTO.getName(), schoolAndManageDTO.getArea()) > 0) {
                logger.info("该地区已包含名为  {}  的学校！", schoolAndManageDTO.getName());
                throw new MemberException("该地区已包含名为" + schoolAndManageDTO.getName() + "的学校！");
            }
        }
        if (schoolAndManageDTO.getAreaName() == null) {
            String areaName = dAreasMapper.selectByPrimaryKey(schoolAndManageDTO.getArea()).getName();
            schoolAndManageDTO.setAreaName(areaName);
            schoolDTO.setAreaName(areaName);
        }


        // 向m_school_period表中插入学校学段信息
        int length;
        PeriodDTO periodDTO = new PeriodDTO();
        int length_of_school = 0;
        for (SchoolPeriodDTO schoolPeriodDTO : schoolPeriodInfos) {
            switch (schoolPeriodDTO.getSection()) {
                case 0:
                    length = schoolPeriodDTO.getLength();
                    if (length == 0) {
                    } else if (length != 5 && length != 6) {
                        throw new MemberException("小学学段只能为5或6");
                    } else {
                        periodDTO.setPrimary(length);
                        length_of_school += 1;
                    }
                    break;
                case 1:
                    length = schoolPeriodDTO.getLength();
                    if (length == 0) {
                    } else if (length != 3 && length != 4) {
                        throw new MemberException("中学学段只能为3或4");
                    } else {
                        periodDTO.setJunior(length);
                        length_of_school += 2;
                    }
                    break;
                case 2:
                    length = schoolPeriodDTO.getLength();
                    if (length == 0) {
                    } else if (length != 3) {
                        throw new MemberException("高中学段只能为3");
                    } else {
                        periodDTO.setSenior(length);
                        length_of_school += 4;
                    }
                    break;
                default:
                    break;
            }
        }
        logger.info("更新学校基本信息，学校id为{}", schoolId);
        schoolPeriodService.savePeriod(periodDTO, schoolAndManageDTO.getId(), true);
        logger.info("更新学校学制学段信息完成，schoolId is {}", schoolId);
        if (StringUtils.isNotEmpty(schoolAndManageDTO.getName()) && !mSchool.getName().equals(schoolAndManageDTO.getName())) {
            ModifyTableFieldDTO<SchoolDTO> schoolDTOModifyTableFieldDTO = new ModifyTableFieldDTO<>(new SchoolDTO());
            schoolDTOModifyTableFieldDTO.getTableDTO().setId(schoolId);
            schoolDTOModifyTableFieldDTO.getTableDTO().setName(schoolAndManageDTO.getName());
            schoolDTOModifyTableFieldDTO.setType(MemberMessageTypeEnum.MODIFY_SCHOOL_NAME);
            messageProducer.sendData(MemberMessageKeyEnum.SEND_MESSAGE_KEY, schoolDTOModifyTableFieldDTO);
            logger.info("异步更新学校名称发送mq消息成功，schoolId is {}", schoolId);
        }
        schoolDTO.setLengthOfSchool(length_of_school);
        update(schoolDTO);
        logger.info("更新学校信息一共消耗时间为：{}毫秒", System.currentTimeMillis() - start);
        return schoolAndManageDTO;
    }

    @Override
    public List<String> getNameByIds(List<String> schoolIds) {
        if (schoolIds != null && !schoolIds.isEmpty()) {
            return schoolMapper.getSchoolNamesByIds(schoolIds);
        } else {
            return new ArrayList<>(0);
        }
    }
}
