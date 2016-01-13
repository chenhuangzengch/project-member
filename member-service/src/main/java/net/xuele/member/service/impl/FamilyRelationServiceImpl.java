package net.xuele.member.service.impl;


import com.alibaba.dubbo.common.utils.StringUtils;
import net.xuele.common.exceptions.MemberException;
import net.xuele.member.constant.CacheConstants;
import net.xuele.member.constant.IdentityIdConstants;
import net.xuele.member.constant.RoleIdConstants;
import net.xuele.member.constant.UserConstants;
import net.xuele.member.domain.*;
import net.xuele.member.dto.*;
import net.xuele.member.persist.CasLoginMapper;
import net.xuele.member.persist.MFamilyRelationMapper;
import net.xuele.member.persist.MLoginUserMapper;
import net.xuele.member.persist.MParentsMapper;
import net.xuele.member.service.*;
import net.xuele.member.util.ClassNameUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by guochun.shen on 2015/7/29 0029.
 */
public class FamilyRelationServiceImpl implements FamilyRelationService {

    @Autowired
    private CasLoginMapper casLoginMapper;
    @Autowired
    private MFamilyRelationMapper mFamilyRelationMapper;
    @Autowired
    private MParentsMapper mParentsMapper;
    @Autowired
    private MLoginUserMapper loginUserMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private SendMessageService sendMessageService;
    @Autowired
    private CasLoginService casLoginService;
    @Autowired
    private SeqGeneratorService seqGeneratorService;
    @Autowired
    private CacheService cacheService;

    /**
     * 根据输入的邀请号，获取家长信息
     *
     * @param loginId loginId
     * @return ParentInfoDTO
     */
    private ParentInfoDTO getCasLoginByLoginId(String loginId) {
        CasLogin casLogin = casLoginMapper.selectByPrimaryKey(loginId);
        ParentInfoDTO parentInfoDTO = new ParentInfoDTO();
        if (casLogin != null) {
            UserDTO userDTO = userService.getByUserId(casLogin.getUserId());
            parentInfoDTO.setUserId(userDTO.getUserId());
            parentInfoDTO.setLoginId(casLogin.getLoginId());
            parentInfoDTO.setLoginType(casLogin.getLoginType());
            parentInfoDTO.setIdentityId(userDTO.getIdentityId());
            parentInfoDTO.setIcon(userDTO.getIcon());
            parentInfoDTO.setUserName(userDTO.getRealName());
            return parentInfoDTO;
        } else {
            return null;
        }
    }

    /**
     * 邀请家长－－新增家长关系
     *
     * @param familyRelationDTO familyRelationDTO
     * @return FamilyRelationDTO
     */
    private FamilyRelationDTO saveFamilyRelation(FamilyRelationDTO familyRelationDTO) {
        Long id = seqGeneratorService.generate("m_family_relation");
        familyRelationDTO.setId(id);
        familyRelationDTO.setAddTime(new Date());
        MFamilyRelation mFamilyRelation = new MFamilyRelation();
        BeanUtils.copyProperties(familyRelationDTO, mFamilyRelation);
        if (StringUtils.isBlank(mFamilyRelation.getMemberName())) {
            throw new MemberException("称谓不能为空");
        }
        mFamilyRelationMapper.insert(mFamilyRelation);
        cacheService.delete(invalidCacheForAll(familyRelationDTO));
        return familyRelationDTO;
    }

    /**
     * APP接口:根据家长ID获取孩子信息
     */
    @Override
    @SuppressWarnings(value = "unchecked")
    public List<StudentDTO> queryKidInfo(String userId) {
        //加入缓存
        String key = MessageFormat.format(CacheConstants.KEY_KID_OF_PARENT, userId);
        List<StudentDTO> list = cacheService.get(key);
        if (list != null) {
            return list;
        }
        List<MStudentManager> smList = mFamilyRelationMapper.queryKidInfo(userId, UserConstants.FAMILY_RELATION_SUCCESS);
        List<StudentDTO> studentDTOList = new ArrayList<>();
        for (MStudentManager st : smList) {
            StudentDTO studentDTO = new StudentDTO();
            studentDTO.setClassId(st.getClassId());
            studentDTO.setUserId(st.getUserId());
            studentDTO.setRealName(st.getRealName());
            studentDTO.setSchoolId(st.getSchoolId());
            studentDTO.setGradeNum(ClassNameUtil.getGradeNum(st.getYears()));
            studentDTO.setSemester(ClassNameUtil.getSemester());
            studentDTO.setStatus(st.getStatus());
            studentDTO.setYear(st.getYears());
            studentDTO.setAreaCode(st.getArea());
            studentDTO.setIcon(st.getIcon());
            studentDTO.setClassName(st.getClassName());
            studentDTO.setSchoolName(st.getSchoolName());
            studentDTOList.add(studentDTO);
        }
        cacheService.set(key, studentDTOList, CacheConstants.CACHE_SEVEN_DAY, TimeUnit.DAYS);
        return studentDTOList;
    }

    /**
     * 查询家庭成员
     *
     * @return 家庭成员列表
     */
    @Override
    public List<FamilyMembersDTO> queryFamilyMembers(String userId) {
        List<DFamilyMembers> dFamilyMembersList = mFamilyRelationMapper.queryFamilyMembers(userId, userService.getSchoolId(userId));
        List<FamilyMembersDTO> familyMembersDTOList = new ArrayList<>();
        for (DFamilyMembers df : dFamilyMembersList) {
            FamilyMembersDTO familyMembersDTO = new FamilyMembersDTO();
            BeanUtils.copyProperties(df, familyMembersDTO);
            if (familyMembersDTO.getMemberStatus().equals(4)) {
                familyMembersDTO.setMemberIcon(UserConstants.ICON_DEFAULT);
            } else {
                UserDTO userDTO = userService.getByUserId(familyMembersDTO.getMemberUserId());
                if (userDTO == null) {
                    continue;
                }
                if (userDTO.getIcon() == null) {
                    userDTO.setIcon(UserConstants.ICON_DEFAULT);
                }
                familyMembersDTO.setMemberIcon(userDTO.getIcon());
                if (familyMembersDTO.getMemberStatus().equals(1)) {
                    familyMembersDTO.setMemberRealName(userDTO.getRealName());
                }
            }
            familyMembersDTOList.add(familyMembersDTO);
        }
        return familyMembersDTOList;
    }

    @Override
    public FamilyRelationDTO getMessageByParentId(String userId) {
        //加入缓存：根据家长ID获取孩子名字和称谓
        String key = MessageFormat.format(CacheConstants.KEY_KID_NAME_OF_PARENT, userId);
        FamilyRelationDTO dto = cacheService.get(key);
        if (dto != null) {
            return dto;
        }
        FamilyRelationDTO familyRelationDTO = new FamilyRelationDTO();
        MFamilyRelation mUserFamilyRelation = mFamilyRelationMapper.getMessageByParentId(userId);
        if (mUserFamilyRelation == null) {
            throw new MemberException("此邀请已被取消");
        }
        BeanUtils.copyProperties(mUserFamilyRelation, familyRelationDTO);
        cacheService.set(key, familyRelationDTO, CacheConstants.CACHE_SEVEN_DAY, TimeUnit.DAYS);
        return familyRelationDTO;
    }

    private void updateById(FamilyRelationDTO familyRelationDTO) {
        MFamilyRelation mFamilyRelation = new MFamilyRelation();
        BeanUtils.copyProperties(familyRelationDTO, mFamilyRelation);
        MFamilyRelation familyRelation = mFamilyRelationMapper.selectByPrimaryKey(mFamilyRelation.getId());
        if (familyRelation != null) {
            familyRelationDTO.setUserId(familyRelation.getUserId());
            familyRelationDTO.setTargetUserId(familyRelation.getTargetUserId());
            mFamilyRelationMapper.updateByPrimaryKey(mFamilyRelation);
            cacheService.delete(invalidCacheForAll(familyRelationDTO));
        }

    }

    @Override
    public FamilyRelationDTO getMessageById(Long id) {
        FamilyRelationDTO familyRelationDTO = new FamilyRelationDTO();
        MFamilyRelation mFamilyRelation = mFamilyRelationMapper.selectByPrimaryKey(id);
        BeanUtils.copyProperties(mFamilyRelation, familyRelationDTO);
        return familyRelationDTO;
    }

    /**
     * 站外邀请家长时 修改家庭关系表
     * 从 4：站外邀请手机验证码未填   变成 5：站外邀请家长未初始化
     *
     * @param familyRelationDTO 关系信息
     */
    public void updateFamilyRelation(FamilyRelationDTO familyRelationDTO) {
        MFamilyRelation mFamilyRelation = new MFamilyRelation();
        BeanUtils.copyProperties(familyRelationDTO, mFamilyRelation);
        mFamilyRelationMapper.updateFamilyRelation(mFamilyRelation);
        //删除缓存
        cacheService.delete(invalidCacheForAll(familyRelationDTO));
    }

    /**
     * 新增关系和解除关系，家长的孩子和孩子的家长都需要清楚缓存
     * 返回需要删除的key
     *
     * @param familyRelationDTO 需要userId,targetUserId
     * @return key值
     */
    private List<String> invalidCacheForAll(FamilyRelationDTO familyRelationDTO) {
        //新增家庭关系:孩子的家长和家长的孩子都需要
        if (familyRelationDTO.getStatus() == 1 || familyRelationDTO.getStatus() == 0) {
            List<String> list = new ArrayList<>();
            String userId = familyRelationDTO.getUserId();
            list.addAll(invalidCacheForAll(userId));
            String targetUserId = familyRelationDTO.getTargetUserId();
            list.add(MessageFormat.format(CacheConstants.KEY_PARENT_OF_KID, targetUserId));
            return list;
        }
        return Collections.emptyList();
    }


    /**
     * 家长的孩子信息
     * 返回需要删除的key
     *
     * @param userId 家长id
     */
    private List<String> invalidCacheForAll(String userId) {
        if (StringUtils.isNotEmpty(userId)) {
            List<String> keyList = new ArrayList<>();
            keyList.add(MessageFormat.format(CacheConstants.KEY_FAMILY_RELATION, userId));
            keyList.add(MessageFormat.format(CacheConstants.KEY_KID_OF_PARENT, userId));
            return keyList;
        }
        return Collections.emptyList();
    }

    /**
     * 查询家长关系表     邀请家长需要根据称谓和学生号 判断是否已经发起过邀请
     */
    public FamilyRelationDTO getFamilyRelation(String memberName, String targetUserId) {
        FamilyRelationDTO familyRelationDTO = new FamilyRelationDTO();
        MFamilyRelation mFamilyRelation = mFamilyRelationMapper.getFamilyRelation(memberName, targetUserId, userService.getSchoolId(targetUserId));
        if (mFamilyRelation != null) {
            BeanUtils.copyProperties(mFamilyRelation, familyRelationDTO);
        }
        return familyRelationDTO;
    }

    public FamilyRelationDTO getFamilyRelationByUserId(String userId, String targetUserId) {
        FamilyRelationDTO familyRelationDTO = new FamilyRelationDTO();
        MFamilyRelation mFamilyRelation = mFamilyRelationMapper.getFamilyRelationByUserId(userId, targetUserId, userService.getSchoolId(targetUserId));
        if (mFamilyRelation != null) {
            BeanUtils.copyProperties(mFamilyRelation, familyRelationDTO);
        }
        return familyRelationDTO;
    }

    public int insert(ParentsDTO parentsDTO) {
        MParents mParents = new MParents();
        BeanUtils.copyProperties(parentsDTO, mParents);
        return mParentsMapper.insert(mParents);
    }

    @Override
    public ParentInfoDTO getParentInfo(String loginId, String targetUserId, String memberName) {
        if (StringUtils.isEmpty(memberName)) {
            throw new MemberException("成员称谓不能为空，请重新输入！");
        }
        FamilyRelationDTO familyRelationDTO = getFamilyRelation(memberName, targetUserId);
        if (familyRelationDTO.getId() != null) {
            throw new MemberException("您输入的成员称谓已存在，请重新输入！");
        }
        ParentInfoDTO returnParentInfoDTO = new ParentInfoDTO();
        ParentInfoDTO parentInfoDTO = isParent(loginId, targetUserId);
        Integer type = 0;
        if (loginId.length() == 11 && !isMobileNO(loginId)) {
            throw new MemberException("您输入的信息不符合规则，请重新输入！");
        }
        if (parentInfoDTO == null) {
            /**
             * type0：站内邀请；1：未绑定手机号邀请
             */
            if (isMobileNO(loginId)) {
                type = 1;
            }
            if (type == 1) {
                returnParentInfoDTO.setLoginId(loginId);
                returnParentInfoDTO.setLoginType(1);
                returnParentInfoDTO.setTargetUserId(targetUserId);
                returnParentInfoDTO.setMemberName(memberName);
                returnParentInfoDTO.setLoginType(1);
            } else {
                throw new MemberException("您邀请的家长还不是会员，请通过其他方式进行邀请！");
            }
        } else {
            parentInfoDTO.setLoginType(type);
            if (parentInfoDTO.getIdentityId().equals(IdentityIdConstants.STUDENT) || parentInfoDTO.getIdentityId().equals(IdentityIdConstants.EDUCATION_MANAGER)
                    || parentInfoDTO.getIdentityId().equals(IdentityIdConstants.SCHOOL_MANAGER)
                    ) {
                throw new MemberException("您邀请的对象是管理员或学生,不能作为家长！");
            } else {
                parentInfoDTO.setLoginId(loginId);
                parentInfoDTO.setTargetUserId(targetUserId);
                parentInfoDTO.setUserId(parentInfoDTO.getUserId());
                parentInfoDTO.setUserName(parentInfoDTO.getUserName());
                parentInfoDTO.setIcon(parentInfoDTO.getIcon());
                parentInfoDTO.setMemberName(memberName);
            }
            returnParentInfoDTO = parentInfoDTO;
        }
        return returnParentInfoDTO;
    }

    /**
     * 判断是否是手机号
     *
     * @param mobiles 移动号段：134 135 136 137 138 139 147 150 151 152 157 158 159 178 182 183 184 187 188
     *                联通号段：130 131 132 145 155 156 176 185 186
     *                电信号段：133 153 177 180 181 189
     * @return boolean
     */
    public boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(14[57])|(15[^4,\\D])|(17[678])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 站内邀请  增加家庭关系
     *
     * @param familyRelationDTO familyRelationDTO
     */
    @Override
    public FamilyRelationDTO saveFamilyRelationForStudent(FamilyRelationDTO familyRelationDTO) {
        //确认邀请时，再次判断是否已经是家长
        isParent(familyRelationDTO.getUserId(), familyRelationDTO.getTargetUserId());
        UserDTO userParent = userService.getByUserId(familyRelationDTO.getUserId());
        UserDTO userTarget = userService.getByUserId(familyRelationDTO.getTargetUserId());
        familyRelationDTO.setSchoolId(userTarget.getSchoolId());
        familyRelationDTO.setType(0);
        familyRelationDTO.setUserName(userParent.getRealName());
        familyRelationDTO.setTargetUserName(userTarget.getRealName());
        familyRelationDTO.setStatus(3);
        FamilyRelationDTO familyRelation = saveFamilyRelation(familyRelationDTO);
        familyRelation = getFamilyRelation(familyRelation.getMemberName(), familyRelation.getTargetUserId());
        return familyRelation;
    }

    /**
     * 判断是否已经是家长
     *
     * @param userId       userId
     * @param targetUserId targetUserId
     */
    public ParentInfoDTO isParent(String userId, String targetUserId) {
        //查询绑定userId 是否是其家长
        List<String> userIdList = loginUserMapper.queryUserIdsByBindUserId(userId);
        //根据登陆表查询是否是其家长
        ParentInfoDTO parentInfoDTO = getCasLoginByLoginId(userId);
        List<FamilyMembersDTO> familyMembersDTOList = queryFamilyMembers(targetUserId);
        if (familyMembersDTOList != null) {
            for (FamilyMembersDTO familyMembersDTO : familyMembersDTOList) {
                if (familyMembersDTO.getMemberUserId().equals(userId) || familyMembersDTO.getMemberUserId().equals(parentInfoDTO == null ? "" : parentInfoDTO.getUserId())) {
                    throw new MemberException("您邀请的成员已经是家长，请重新输入！");
                }
                for (String s : userIdList) {
                    if (familyMembersDTO.getMemberUserId().equals(s)) {
                        throw new MemberException("您邀请的成员已经是家长，请重新输入！");
                    }
                }
            }
        }
        return parentInfoDTO;
    }

    /**
     * 站内邀请  同意或拒绝
     *
     * @param id   id
     * @param type type
     */
    @Override
    public void saveDoAction(Long id, Integer type) {
        FamilyRelationDTO familyRelationDTO = getMessageById(id);
        if (type == 1 && familyRelationDTO.getStatus() == 3) {
            //同意  状态变为 1：成功；
            familyRelationDTO.setStatus(1);
            updateById(familyRelationDTO);
            UserDTO userParent = userService.getByUserId(familyRelationDTO.getUserId());
            UserDTO userTarget = userService.getByUserId(familyRelationDTO.getTargetUserId());
            List<RoleDTO> roleDTOList = userService.queryRoleByUserId(userParent.getUserId());
            boolean isParent = false;
            //判断是否有家长角色，如果有 不新建用户
            if (roleDTOList != null) {
                for (RoleDTO roleDTO : roleDTOList) {
                    if (roleDTO.getRoleId().equals(RoleIdConstants.PARENT)) {
                        isParent = true;
                        break;
                    }
                }
            }

            if (!isParent) {
                String userId = "";
                //查询绑定userId 是否有家长角色，如果有 则不用新增加角色用户，直接取userId
                List<String> userIdList = loginUserMapper.queryUserIdsByBindUserId(familyRelationDTO.getUserId());
                for (String uId : userIdList) {
                    if (!familyRelationDTO.getUserId().equals(uId)) {
                        List<RoleDTO> roleDTOs = userService.queryRoleByUserId(uId);
                        if (roleDTOs != null) {
                            for (RoleDTO roleDTO : roleDTOs) {
                                if (roleDTO.getRoleId().equals(RoleIdConstants.PARENT)) {
                                    userId = uId;
                                }
                            }
                        }
                    }
                }

                if (userId.equals("")) {
                    //创建家长用户
                    UserDTO userDTO = new UserDTO();
                    userDTO.setIdentityId(RoleIdConstants.PARENT);
                    userDTO.setIdentityDescription("家长");
                    userDTO.setSchoolId(userTarget.getSchoolId());
                    userDTO.setArea(userTarget.getArea());
                    UserDTO userDTOs = userService.saveUser(userDTO);
                    //插入家长表
                    ParentsDTO parentsDTO = new ParentsDTO();
                    parentsDTO.setUserId(userDTOs.getUserId());
                    parentsDTO.setAppellation(familyRelationDTO.getMemberName());
                    parentsDTO.setSchoolId(userTarget.getSchoolId());
                    insert(parentsDTO);

                    //修改用户表，新建用户不进行初始化
                    userDTOs.setRealName(userParent.getRealName());
                    userService.updateByUserId(userDTOs);
                    userService.updateStatus(1, userDTOs.getUserId());
                    //绑定用户
                    userService.saveBindUser(userDTOs.getUserId(), userParent.getUserId());
                    userId = userDTOs.getUserId();
                }
                //修改家庭关系表
                familyRelationDTO.setUserId(userId);
                updateFamilyRelation(familyRelationDTO);


            }
        } else if (type == 0 && familyRelationDTO.getStatus() == 3) {
            //拒绝  状态变为 2：已拒绝
            familyRelationDTO.setStatus(2);
            updateById(familyRelationDTO);
        }
    }

    /**
     * 短信邀请，发送账号密码
     */
    private void saveSmsAddFamilyRelation(String loginId, String checkCode, String memberName, String targetUserId) {
        if (sendMessageService.checkCode(loginId, checkCode, SendMessageService.CodeType.InviteUser)) {

//            ParentInfoDTO parentInfoDTO = getCasLoginByLoginId(loginId);
//            if (parentInfoDTO == null) {

            //获取学生的姓名
            UserDTO userTarget = userService.getByUserId(targetUserId);

            //插入用户表
            UserDTO users = userService.getByUserId(targetUserId);
            UserDTO userDTO = new UserDTO();
            userDTO.setMobile(loginId);
            userDTO.setIdentityId(IdentityIdConstants.PARENT);
            userDTO.setIdentityDescription("家长");
            userDTO.setSchoolId(users.getSchoolId());
            userDTO.setArea(users.getArea());
            userDTO.setRealName("家长");
            UserDTO userDTOs = userService.saveUser(userDTO);

            //插入登录认证表（手机号登录）
            CasLoginDTO casLoginDTO = new CasLoginDTO();
            casLoginDTO.setLoginId(loginId);
            casLoginDTO.setUserId(userDTOs.getUserId());
            casLoginDTO.setLoginType(UserConstants.PHONE);
            casLoginDTO.setPassword(UserConstants.INIT_PASSWORD);
            casLoginService.insert(casLoginDTO);

            FamilyRelationDTO familyRelationDTO = new FamilyRelationDTO();
            familyRelationDTO.setSchoolId(userDTOs.getSchoolId());
            familyRelationDTO.setType(1);
            familyRelationDTO.setUserId(userDTOs.getUserId());
            if (StringUtils.isEmpty(memberName)) {
                FamilyRelationDTO familyRelations = getFamilyRelationByUserId(loginId, targetUserId);
                familyRelationDTO.setMemberName(familyRelations.getMemberName());
            } else {
                familyRelationDTO.setMemberName(memberName);
            }
            familyRelationDTO.setTargetUserId(targetUserId);
            familyRelationDTO.setStatus(5);
            familyRelationDTO.setTargetUserName(userTarget.getRealName());

            //修改家庭关系表
            updateFamilyRelation(familyRelationDTO);

            //插入家长表（没有就插入）
            ParentsDTO parentsDTO = new ParentsDTO();
            parentsDTO.setUserId(userDTOs.getUserId());
            parentsDTO.setAppellation(familyRelationDTO.getMemberName());
            parentsDTO.setSchoolId(userTarget.getSchoolId());
            insert(parentsDTO);
            String tem = "{0}的家长，使用学乐号 {1} 登录 http://www.xueleyun.com，密码 123456。";
            sendMessageService.sendMessage(loginId, MessageFormat.format(tem, userTarget.getRealName(), userDTOs.getUserId()));
        }
//        }
    }


    /**
     * 家庭信息根据status返回数据
     *
     * @param id 关系id
     * @return 家庭成员当前信息
     */
    @Override
    public FamilyWrapperDTO inviteByPlatform(Long id) {
        FamilyRelationDTO familyRelationDTO = getMessageById(id);
        FamilyWrapperDTO familyWrapperDTO = new FamilyWrapperDTO();
        BeanUtils.copyProperties(familyRelationDTO, familyWrapperDTO);
        if (familyRelationDTO.getStatus() == 4) {
            familyWrapperDTO.setMemberMobile(familyRelationDTO.getUserId());
            familyWrapperDTO.setMemberIcon(UserConstants.ICON_DEFAULT);
        } else {
            UserDTO userDTO = userService.getByUserId(familyRelationDTO.getUserId());
            if (userDTO.getIcon() == null) {
                familyWrapperDTO.setMemberIcon(UserConstants.ICON_DEFAULT);
            }
            if (userDTO.getRealName() == null) {
                userDTO.setRealName("");
            }
            if (userDTO.getMobile() == null) {
                userDTO.setMobile("未绑定");
            }
            if (familyRelationDTO.getStatus() == 1) {
                familyWrapperDTO.setMemberIcon(userDTO.getIcon());
                familyWrapperDTO.setMemberRealName(userDTO.getRealName());
                familyWrapperDTO.setMemberMobile(userDTO.getMobile());
            } else if (familyRelationDTO.getStatus() == 2) {
                familyWrapperDTO.setMemberIcon(userDTO.getIcon());
            } else if (familyRelationDTO.getStatus() == 3) {
                familyWrapperDTO.setMemberIcon(userDTO.getIcon());
                familyWrapperDTO.setMemberRealName(userDTO.getRealName());
            } else if (familyRelationDTO.getStatus() == 5) {
                UserDTO kidDTO = userService.getByUserId(familyRelationDTO.getTargetUserId());
                familyWrapperDTO.setMemberIcon(userDTO.getIcon());
                familyWrapperDTO.setMemberRealName(kidDTO.getRealName());
                familyWrapperDTO.setMemberMobile(userDTO.getMobile());
                familyWrapperDTO.setPassword("123456");
            } else {
                throw new MemberException("无此状态");
            }
        }
        return familyWrapperDTO;
    }

    /**
     * 根据关系id查找该学生的信息以及家长的信息
     *
     * @param id 关系id
     * @return 家长信息列表
     */
    @Override
    public List<ParentInfoDTO> getParentsMessageById(Long id) {
        FamilyRelationDTO familyRelationDTO = getMessageById(id);
        List<ParentInfoDTO> parentInfoDTOs = new ArrayList<>();
        UserDTO userDTO = userService.getByUserId(familyRelationDTO.getTargetUserId());
        ParentInfoDTO parentInfoDTO = new ParentInfoDTO();
        parentInfoDTO.setUserName(userDTO.getRealName());
        parentInfoDTO.setIcon(userDTO.getIcon());
        parentInfoDTOs.add(parentInfoDTO);
        List<DFamilyMembers> dFamilyMembersList = mFamilyRelationMapper.getByTargetUserId(
                familyRelationDTO.getTargetUserId(), userDTO.getSchoolId());
        for (DFamilyMembers dFamilyMembers : dFamilyMembersList) {
            userDTO = userService.getByUserId(dFamilyMembers.getMemberUserId());
            ParentInfoDTO parentInfoDto = new ParentInfoDTO();
            parentInfoDto.setUserName(userDTO.getRealName());
            parentInfoDto.setIcon(userDTO.getIcon());
            parentInfoDTOs.add(parentInfoDto);
        }
        return parentInfoDTOs;
    }


    @Override
    public FamilyRelationDTO updateParentInfo(String userId, String name) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userId);
        userDTO.setRealName(name);
        userService.updateByUserId(userDTO);
        userService.updateStatus(1, userId);
        FamilyRelationDTO familyRelationDTO = getMessageByParentId(userId);
        familyRelationDTO.setStatus(1);
        familyRelationDTO.setUserName(name);
        updateById(familyRelationDTO);
        return familyRelationDTO;
    }

    /**
     * 修改家庭关系表中用户名称
     */
    @Override
    public void updateName(String userId, String realName) {
        UserDTO users = userService.getByUserId(userId);
        List<String> keyList = new ArrayList<>();
        if (IdentityIdConstants.STUDENT.equals(users.getIdentityId())) {
            mFamilyRelationMapper.updateTargetUserName(userId, realName, users.getSchoolId());
            List<DFamilyMembers> members = mFamilyRelationMapper.getByTargetUserId(userId, users.getSchoolId());
            for (DFamilyMembers member : members) {
                keyList.addAll(invalidCacheForAll(member.getMemberUserId()));
            }
        } else {
            mFamilyRelationMapper.updateUserName(userId, realName);
            keyList.addAll(invalidCacheForAll(userId));
        }
        if (CollectionUtils.isNotEmpty(keyList)) {
            cacheService.delete(keyList);
        }
    }

    /**
     * 查询某个学生成功邀请到的家长数量
     *
     * @param targetUserId 学生ID
     * @param schoolId     学校ID
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @return 成功次数
     */
    @Override
    public long getInvitedToSucceedFamily(String targetUserId, String schoolId, Date startTime, Date endTime) {
        return mFamilyRelationMapper.getInvitedToSucceedFamily(targetUserId, schoolId, DateFormatUtils.format(startTime, "yyyy-MM-dd"), DateFormatUtils.format(endTime, "yyyy-MM-dd"));
    }

    /**
     * 家庭邀请
     * 给邀请用户发送手机验证码
     *
     * @param mobile       手机号
     * @param targetUserId 学生ID
     * @param memberName   称谓
     * @return long
     */
    @Override
    public long getCheckCodeForInviteUser(String mobile, String targetUserId, String memberName, String createUserId) {
        if (org.apache.commons.lang.StringUtils.isEmpty(mobile)) {
            throw new MemberException("手机号不能为空");
        }
        List<FamilyMembersDTO> familyMemberDTOList = queryFamilyMembers(targetUserId);
        int j = 0;
        for (FamilyMembersDTO familyMembersDTO : familyMemberDTOList) {
            if (familyMembersDTO.getMemberUserId().equals(mobile) && !familyMembersDTO.getMemberName().equals(memberName)) {
                j++;
            }
        }
        //允许修改手机号后，需要在发送验证码时判断是否已经邀请过
        if (j > 0) {
            throw new MemberException("您已邀请过此手机号用户，请重新输入！");
        }
        UserDTO userDTO = userService.getByUserId(targetUserId);
        sendMessageService.sendMessage(mobile, SendMessageService.CodeType.InviteUser, userDTO.getRealName());
        FamilyRelationDTO familyRelationDTO = new FamilyRelationDTO();
        familyRelationDTO.setMemberName(memberName);
        familyRelationDTO.setUserId(mobile);
        familyRelationDTO.setTargetUserId(targetUserId);
        familyRelationDTO.setTargetUserName(userDTO.getRealName());
        familyRelationDTO.setStatus(4);
        familyRelationDTO.setType(1);
        familyRelationDTO.setSchoolId(userDTO.getSchoolId());
        familyRelationDTO.setCreateUserId(createUserId);
        //判断是不是重新邀请，重新邀请做修改操作
        if (getFamilyRelation(memberName, targetUserId).getId() == null) {
            familyRelationDTO = saveFamilyRelation(familyRelationDTO);
        } else {
            updateFamilyRelation(familyRelationDTO);
            familyRelationDTO = getFamilyRelation(memberName, targetUserId);
        }
        if (familyRelationDTO.getId() == null) {
            throw new MemberException("邀请异常，请重试！");
        }
        return familyRelationDTO.getId();
    }

    @Override
    public FamilyRelationDTO smsAddFamilyRelation(String loginId, String checkCode, String memberName, String targetUserId) {
        //发送邀请时，判断此手机号是否已经邀请过，如果邀请过则不新增
        ParentInfoDTO parentInfoDTO = getCasLoginByLoginId(loginId);
        FamilyRelationDTO familyRelationDTO = new FamilyRelationDTO();
        if (parentInfoDTO == null) {
            saveSmsAddFamilyRelation(loginId, checkCode, memberName, targetUserId);
        } else {
            //邀请过的号码,修改家庭关系表userId,以及状态（站内邀请）和邀请方式（发送通知）
            UserDTO userTarget = userService.getByUserId(targetUserId);
            familyRelationDTO = getFamilyRelation(memberName, targetUserId);
            familyRelationDTO.setSchoolId(userService.getSchoolId(parentInfoDTO.getUserId()));
            familyRelationDTO.setType(0);
            familyRelationDTO.setUserId(parentInfoDTO.getUserId());
            if (StringUtils.isEmpty(memberName)) {
                FamilyRelationDTO familyRelations = getFamilyRelationByUserId(loginId, targetUserId);
                familyRelationDTO.setMemberName(familyRelations.getMemberName());
            } else {
                familyRelationDTO.setMemberName(memberName);
            }
            familyRelationDTO.setTargetUserId(targetUserId);
            familyRelationDTO.setStatus(3);
            familyRelationDTO.setTargetUserName(userTarget.getRealName());


            //修改家庭关系表
            updateFamilyRelation(familyRelationDTO);

        }
        return familyRelationDTO;
    }

    @Override
    public FamilyRelationDTO releaseRelationship(Long id, String identityId, String userId) {
        FamilyRelationDTO familyRelationDTO = getMessageById(id);

        if (identityId.equals(IdentityIdConstants.PARENT)) {
            if (!familyRelationDTO.getUserId().equals(userId)) {
                throw new MemberException("您只能解除自己与孩子的关系");
            }
        }
        familyRelationDTO.setStatus(0);
        updateById(familyRelationDTO);
        return familyRelationDTO;
    }

    @Override
    public FamilyRelationDTO reNotify(Long id) {
        if (id == null) {
            throw new MemberException("id为空，请传入正确的值");
        }
        FamilyRelationDTO familyRelationDTO = getMessageById(id);
        if (familyRelationDTO == null) {
            throw new MemberException("未能找到相应的用户");
        }
        UserDTO userParent = userService.getByUserId(familyRelationDTO.getUserId());
        UserDTO userTarget = userService.getByUserId(familyRelationDTO.getTargetUserId());

        if (userParent == null || userTarget == null) {
            throw new MemberException("未能找到用户");
        }
        if (familyRelationDTO.getStatus() == 2) {
            familyRelationDTO.setStatus(familyRelationDTO.getType() == 1 ? 5 : 3);
            updateById(familyRelationDTO);
        }
        //1手机号邀请
        if (familyRelationDTO.getType() == 1) {
            sendMessageService.sendMessage(userParent.getMobile(), userTarget.getRealName()
                    + "邀请您作为ta的家长，会员号：" + userParent.getUserId() + ",密码：123456。登录 http://www.xueleyun.com 成为学乐的一员吧！");
        }
        return familyRelationDTO;
    }

    /**
     * 根据班级id获取这个班级所有学生的家长
     *
     * @param classId  班级id
     * @param schoolId 这个班级的学校id
     * @return 家长id列表
     */
    @Override
    public List<String> queryParentIdByClassId(String classId, String schoolId) {
        return mFamilyRelationMapper.queryParentIdByClassId(classId, schoolId);
    }
}
