package net.xuele.member.service.impl;

import net.xuele.member.constant.IdentityIdConstants;
import net.xuele.member.domain.CasLogin;
import net.xuele.member.domain.CasLoginLog;
import net.xuele.member.dto.CasLoginDTO;
import net.xuele.member.persist.CasLoginLogMapper;
import net.xuele.member.persist.CasLoginMapper;
import net.xuele.member.service.CasLoginService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wuxh on 15/8/3.
 */

public class CasLoginServiceImpl implements CasLoginService {

    private static Logger logger = LoggerFactory.getLogger(CasLoginServiceImpl.class);

    @Autowired
    private CasLoginMapper casLoginMapper;
    @Autowired
    private CasLoginLogMapper casLoginLogMapper;

    @Override
    public String getUseIdByLoginId(String mobile) {
        if (StringUtils.isEmpty(mobile)) {
            logger.debug("mobile 为空");
            return null;
        }
        return casLoginMapper.getUserIdByLoginId(mobile);
    }

    /**
     * 根据登录号获取登录信息
     */
    @Override
    public CasLoginDTO getByLoginId(String userId) {
        if (StringUtils.isEmpty(userId)) {
            logger.debug("userId 为空");
            return null;
        }
        CasLogin casLogin = casLoginMapper.selectByPrimaryKey(userId);
        CasLoginDTO casLoginDTO = null;
        if (casLogin != null) {
            casLoginDTO = new CasLoginDTO();
            BeanUtils.copyProperties(casLogin, casLoginDTO);
        }
        return casLoginDTO;
    }

    @Override
    public void updateUserLoginStatus(String userId, Integer status) {
        if (!StringUtils.isEmpty(userId) && status != null) {
            casLoginMapper.updateStatusByUserId(userId, status);
        }
    }

    @Override
    public String getLoginIdByUserIdAndLoginType(String userId, Integer loginType) {
        if (StringUtils.isEmpty(userId) || loginType == null) {
            return null;
        }
        return casLoginMapper.getLoginIdByUserIdAndLoginType(userId, loginType);
    }

    @Override
    public int insert(CasLoginDTO casLoginDTO) {
        CasLogin casLogin = new CasLogin();
        BeanUtils.copyProperties(casLoginDTO, casLogin);
        return casLoginMapper.insert(casLogin);
    }

    @Override
    public List<CasLoginDTO> getByUserId(String userId) {

        List<CasLogin> list = casLoginMapper.getByUserId(userId);
        List<CasLoginDTO> casLoginDTOs = new ArrayList<>();
        for (CasLogin casLogin : list) {
            CasLoginDTO casLoginDTO = new CasLoginDTO();
            BeanUtils.copyProperties(casLogin, casLoginDTO);
            casLoginDTOs.add(casLoginDTO);
        }
        return casLoginDTOs;
    }

    @Override
    public int getLoginTimes(String userId, String schoolId, Date startTime, Date endTime, Integer loginType) {
        List<Integer> list = new ArrayList<>();
        if (loginType == 0 || loginType == 1 || loginType == 2) {
            list.add(loginType);
        } else if (loginType == 3) {
            list.add(1);
            list.add(2);
        } else if (loginType == 4) {
            list.add(0);
            list.add(1);
            list.add(2);
        }
        return casLoginLogMapper.getLoginTimes(userId, schoolId, startTime, endTime, list);
    }

    /**
     * 获取某个学校在某段时间内登陆的学生或教师人数
     *
     * @param schoolId      学校id
     * @param loginUserType 登录人类型 1：教师 2:学生 3：全部
     * @param begTime       开始时间
     * @param endTime       截止时间
     * @return 人数
     */
    @Override
    public int getLoginPersonCount(String schoolId, Integer loginUserType, Date begTime, Date endTime) {
        String identityId;
        if (loginUserType == 1) {
            identityId = IdentityIdConstants.TEACHER;
        } else if (loginUserType == 2) {
            identityId = IdentityIdConstants.STUDENT;
        } else {
            identityId = null;
        }
        return casLoginLogMapper.getLoginPersonCount(schoolId, begTime, endTime, identityId);
    }

    /**
     * 获取某人最后第二次登录时间
     *
     * @param userId   用户id
     * @param schoolId 学校id
     * @return 时间
     */
    @Override
    public Date getLastLoginTime(String userId, String schoolId) {
        CasLoginLog cl = casLoginLogMapper.getLastSecondTimeByUserId(userId, schoolId);
        return cl == null ? null : cl.getLoginTimestamp() != null ? cl.getLoginTimestamp() : null;
    }

}
