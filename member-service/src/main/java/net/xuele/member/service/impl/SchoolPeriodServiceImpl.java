package net.xuele.member.service.impl;


import com.alibaba.dubbo.common.utils.CollectionUtils;
import net.xuele.common.exceptions.MemberException;
import net.xuele.common.utils.MemberAssert;
import net.xuele.member.constant.IdentityIdConstants;
import net.xuele.member.constant.SectionConstants;
import net.xuele.member.context.MemberLogicContext;
import net.xuele.member.domain.MClass;
import net.xuele.member.domain.MSchoolPeriod;
import net.xuele.member.dto.PeriodDTO;
import net.xuele.member.dto.SchoolPeriodDTO;
import net.xuele.member.dto.UserDTO;
import net.xuele.member.persist.MClassMapper;
import net.xuele.member.persist.MSchoolPeriodMapper;
import net.xuele.member.service.SchoolPeriodService;
import net.xuele.member.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


public class SchoolPeriodServiceImpl implements SchoolPeriodService {
    @Autowired
    private MSchoolPeriodMapper schoolPeriodMapper;
    @Autowired
    private MClassMapper mClassMapper;
    @Autowired
    private UserService userService;
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 保存某学校的学段学制信息
     */
    @Override
    public void savePeriod(PeriodDTO periodDTO, String schoolId, Boolean forcible) {
        //没有班级任意修改学制学段
        if (mClassMapper.selectBySchoolId(schoolId) == 0) {
            saveNormally(periodDTO, schoolId);
            return;
        }
        //有班级，并且强制改学制学段
        if (forcible != null && forcible) {
            saveForcibly(periodDTO, schoolId);
            return;
        }
        //这里肯定有异常了
        throw new MemberException("该学校已有班级，不能修改");
    }

    private void saveForcibly(PeriodDTO periodDTO, String schoolId) {
        logger.warn("强制改学制学段，schoolId：" + schoolId + String.valueOf(periodDTO.getPrimary()) + String.valueOf(periodDTO.getJunior()) + String.valueOf(periodDTO.getSenior()));
        if (periodDTO.getPrimary() == null && periodDTO.getJunior() == null && periodDTO.getSenior() == null) {
            return;
        }
        Integer primarySchool = periodDTO.getPrimary();
        Integer juniorMiddleSchool = periodDTO.getJunior();
        Integer seniorHighSchool = periodDTO.getSenior();
        MemberAssert.isTrue(!((primarySchool != null) && (juniorMiddleSchool != null) && (primarySchool + juniorMiddleSchool != 9)),
                "学校小学和初中相加必须九年", MemberLogicContext.ERROR_CODE_SCHOOL_PERIOD_IS_ERROR_TWO);
        MemberAssert.isTrue(!((primarySchool != null) && (seniorHighSchool != null) && (juniorMiddleSchool == null)),
                "学校不能只有小学和高中", MemberLogicContext.ERROR_CODE_SCHOOL_PERIOD_IS_ERROR);
        List<MSchoolPeriod> periods = schoolPeriodMapper.selectBySchoolId(schoolId);
        MSchoolPeriod primary = null;
        MSchoolPeriod junior = null;
        MSchoolPeriod senior = null;
        for (MSchoolPeriod schoolPeriodDTO : periods) {
            switch (schoolPeriodDTO.getSection()) {
                case 0:
                    primary = schoolPeriodDTO;
                    break;
                case 1:
                    junior = schoolPeriodDTO;
                    break;
                case 2:
                    senior = schoolPeriodDTO;
                    break;
                default:
                    break;
            }
        }
        savePrimary(schoolId, primarySchool, primary);
        saveJunior(schoolId, juniorMiddleSchool, junior);
        saveSenior(schoolId, seniorHighSchool, senior);
    }

    private void saveSenior(String schoolId, Integer seniorHighSchool, MSchoolPeriod senior) {
        if (seniorHighSchool != null) {//要修改的有这个学段
            if (senior == null) {//原来这个学段没有，那新增没问题
                SchoolPeriodDTO schoolPeriodDTO = new SchoolPeriodDTO();
                String schoolPeriodId = schoolId + "-2-" + seniorHighSchool;
                schoolPeriodDTO.setId(schoolPeriodId);
                schoolPeriodDTO.setSection(2);
                schoolPeriodDTO.setSectionDisplay(SectionConstants.SENIOR_HIGH_SCHOOL);
                insert(schoolId, seniorHighSchool, schoolPeriodDTO);
            } else {//原来有这个学段，那需要判断是否需要修改学制
                if (!senior.getLength().equals(seniorHighSchool)) {
                    updateLength(senior, seniorHighSchool);
                }
                //不需要修改
            }
        } else {//要修改的没有高中
            if (senior != null) {
                deleteThisLevel(senior);
            }
        }
    }

    private void saveJunior(String schoolId, Integer juniorMiddleSchool, MSchoolPeriod junior) {
        if (juniorMiddleSchool != null) {//要修改的有这个学段
            if (junior == null) {//原来这个学段没有，那新增没问题
                SchoolPeriodDTO schoolPeriodDTO = new SchoolPeriodDTO();
                String schoolPeriodId = schoolId + "-1-" + juniorMiddleSchool;
                schoolPeriodDTO.setId(schoolPeriodId);
                schoolPeriodDTO.setSection(1);
                schoolPeriodDTO.setSectionDisplay(SectionConstants.JUNIOR_MIDDLE_SCHOOL);
                insert(schoolId, juniorMiddleSchool, schoolPeriodDTO);
            } else {//原来有这个学段，那需要判断是否需要修改学制
                if (!junior.getLength().equals(juniorMiddleSchool)) {
                    updateLength(junior, juniorMiddleSchool);
                }
                //不需要修改
            }
        } else {//要修改的没有初中
            if (junior != null) {
                deleteThisLevel(junior);
            }
        }
    }

    private void savePrimary(String schoolId, Integer primarySchool, MSchoolPeriod primary) {
        if (primarySchool != null) {//要修改的有这个学段
            if (primary == null) {//原来这个学段没有，那新增没问题
                SchoolPeriodDTO schoolPeriodDTO = new SchoolPeriodDTO();
                String schoolPeriodId = schoolId + "-0-" + primarySchool;
                schoolPeriodDTO.setId(schoolPeriodId);
                schoolPeriodDTO.setSection(0);
                schoolPeriodDTO.setSectionDisplay(SectionConstants.PRIMARY_SCHOOL);
                insert(schoolId, primarySchool, schoolPeriodDTO);
            } else {//原来有这个学段，那需要判断是否需要修改学制
                if (!primary.getLength().equals(primarySchool)) {
                    updateLength(primary, primarySchool);
                }
                //不需要修改
            }
        } else {//要修改的没有小学
            if (primary != null) {
                deleteThisLevel(primary);
            }
        }
    }

    private void deleteThisLevel(MSchoolPeriod schoolPeriod) {
        //获取需要删除的班级
        List<String> classIds = mClassMapper.selectIdsByPeriod(schoolPeriod.getSchoolId(), schoolPeriod.getId());
        if (classIds != null && !classIds.isEmpty()) {
            throw new MemberException("请删除【" + schoolPeriod.getSectionDisplay() + "】的班级");
            //根据学校id和班级ids修改对应的学生的班级id为null
//            studentMapper.updateByClassList(classIds, schoolPeriod.getSchoolId());
            //根据学校id和班级ids删除教师班级对应表
//            mClassMapper.deleteByClassList(classIds, schoolPeriod.getSchoolId());
            //最终删除班级
//            mClassMapper.deleteByPeriod(schoolPeriod.getSchoolId(), schoolPeriod.getId());

        }
        //最终删除学制学段
        schoolPeriodMapper.deleteByPrimaryId(schoolPeriod.getSchoolId(), schoolPeriod.getId());
    }

    private void updateLength(MSchoolPeriod schoolPeriod, Integer length) {
        schoolPeriod.setLength(length);
        schoolPeriodMapper.updateLengthById(schoolPeriod);
    }

    private void saveNormally(PeriodDTO periodDTO, String schoolId) {
        Integer primarySchool = periodDTO.getPrimary();
        Integer juniorMiddleSchool = periodDTO.getJunior();
        Integer seniorHighSchool = periodDTO.getSenior();
        MemberAssert.isTrue(!(primarySchool != null && juniorMiddleSchool != null && primarySchool + juniorMiddleSchool != 9),
                "小学和初中相加必须是九年", MemberLogicContext.ERROR_CODE_SCHOOL_PERIOD_IS_ERROR_TWO);
        MemberAssert.isTrue(!((primarySchool != null) && (seniorHighSchool != null) && (juniorMiddleSchool == null)),
                "学校不能只有小学和高中", MemberLogicContext.ERROR_CODE_SCHOOL_PERIOD_IS_ERROR);
        schoolPeriodMapper.deleteBySchoolId(schoolId);
        String ID;
        SchoolPeriodDTO schoolPeriodDTO = new SchoolPeriodDTO();
        if (primarySchool != null) {
            ID = schoolId + "-0-" + primarySchool;
            schoolPeriodDTO.setId(ID);
            schoolPeriodDTO.setSection(0);
            schoolPeriodDTO.setSectionDisplay(SectionConstants.PRIMARY_SCHOOL);
            insert(schoolId, primarySchool, schoolPeriodDTO);
        }
        if (juniorMiddleSchool != null) {
            ID = schoolId + "-1-" + juniorMiddleSchool;
            schoolPeriodDTO.setId(ID);
            schoolPeriodDTO.setSection(1);
            schoolPeriodDTO.setSectionDisplay(SectionConstants.JUNIOR_MIDDLE_SCHOOL);
            insert(schoolId, juniorMiddleSchool, schoolPeriodDTO);
        }
        if (seniorHighSchool != null) {
            ID = schoolId + "-2-" + seniorHighSchool;
            schoolPeriodDTO.setId(ID);
            schoolPeriodDTO.setSection(2);
            schoolPeriodDTO.setSectionDisplay(SectionConstants.SENIOR_HIGH_SCHOOL);
            insert(schoolId, seniorHighSchool, schoolPeriodDTO);
        }
    }

    /**
     * 根据学校ID获取学制学段信息
     */
    @Override
    public List<SchoolPeriodDTO> queryBySchoolId(String schoolId) {
        List<MSchoolPeriod> schoolPeriodList = schoolPeriodMapper.selectBySchoolId(schoolId);
        List<SchoolPeriodDTO> schoolPeriodDTOList = new ArrayList<>();
        for (MSchoolPeriod sp : schoolPeriodList) {
            SchoolPeriodDTO spdto = new SchoolPeriodDTO();
            BeanUtils.copyProperties(sp, spdto);
            schoolPeriodDTOList.add(spdto);
        }
        return schoolPeriodDTOList;
    }

    /**
     * 根据用户id返回该用户所对应的学段
     * 老师：学校的学段
     * 学生：所在班级对应的学段
     */
    @Override
    public List<Integer> queryPeriodByUserId(String userId, String schoolId) {
        UserDTO userDTO = userService.getByUserId(userId);
        List<Integer> integerList = new ArrayList<>();
        if (userDTO == null) {
            logger.debug(userId + "不存在");
            throw new MemberException("用户不存在");
        }
        if (IdentityIdConstants.STUDENT.equals(userDTO.getIdentityId())) {
            MClass cla = mClassMapper.getClassByUserId(userId, schoolId);
            if (cla == null) {
                return integerList;
            }
            MSchoolPeriod sp = schoolPeriodMapper.getByPrimaryIdAndSchoolId(cla.getSchoolPeriodId(), schoolId);
            if (sp == null) {
                throw new MemberException("学校没有学制学段信息");
            }
            integerList.add(sp.getSection());
            return integerList;
        } else if (IdentityIdConstants.TEACHER.equals(userDTO.getIdentityId())) {
            List<MSchoolPeriod> spList = schoolPeriodMapper.selectBySchoolId(schoolId);
            for (MSchoolPeriod sp : spList) {
                integerList.add(sp.getSection());
            }
            return integerList;
        }
        return integerList;
    }

    /**
     * 同步课堂年级获取(小学和初中)：通过学校id获取该学校需要设置同步课堂的年级列表
     */
    @Override
    public List<Integer> getSysGradeNum(String schoolId) {
        List<MSchoolPeriod> spdtoList = schoolPeriodMapper.selectBySchoolId(schoolId);
        if (CollectionUtils.isEmpty(spdtoList)) {
            logger.debug("{}没有学制学段信息", schoolId);
            throw new MemberException("没有学制学段信息");
        }
        List<Integer> gradNumList = new ArrayList<>();
        int pPeriod = 0;//小学学制
        int jPeriod = 0;//初中学制
        //同步课堂不考虑高中
        for (MSchoolPeriod spdto : spdtoList) {
            if (spdto.getSection() == 0) {
                pPeriod = spdto.getLength();
            } else if (spdto.getSection() == 1) {
                jPeriod = spdto.getLength();
            }
        }
        int count;
        if (pPeriod != 0) {
            if (jPeriod != 0) {//有小学和初中
                count = pPeriod + jPeriod;
            } else {//只有小学
                count = pPeriod;
            }
            for (int i = 1; i <= count; i++) {
                gradNumList.add(i);
            }
            return gradNumList;
        }
        if (jPeriod != 0) {//只有初中
            for (int i = 10 - jPeriod; i <= 9; i++) {
                gradNumList.add(i);
            }
            return gradNumList;
        }
        return gradNumList;
    }

    private void insert(String schoolId, Integer schoolSection, SchoolPeriodDTO schoolPeriodDTO) {
        schoolPeriodDTO.setLength(schoolSection);
        schoolPeriodDTO.setSchoolId(schoolId);
        MSchoolPeriod schoolPeriod = new MSchoolPeriod();
        BeanUtils.copyProperties(schoolPeriodDTO, schoolPeriod);
        schoolPeriodMapper.insert(schoolPeriod);
    }
}
