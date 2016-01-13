package net.xuele.member.dto;

import net.xuele.member.constant.UserConstants;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Zhihuan.cai on 2015/5/20 0020.
 */
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 3385066424570199754L;
    /**
     * 学乐号
     */
    private String userId;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 头像
     */
    private String icon;

    /**
     * qq
     */
    private String qq;

    /**
     * 资源类型
     */
    private Integer rType;

    /**
     * 注册时间
     */
    private Date addTime;

    /**
     * 注册来源
     */
    private Integer regType;

    /**
     * 注册IP
     */
    private String regIp;

    /**
     * 用户状态,[0,离校;1,有效;2,未初始化;]
     */
    private Integer status;

    /**
     * 电话号码
     */
    private String tel;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 个人签名
     */
    private String signature;

    /**
     * 封面,hdfs地址
     */
    private String cover;

    /**
     * 修改时间,修改的当前数据库时间
     */
    private Date updateTime;

    /**
     * 身份证
     */
    private String idcard;

    /**
     * 身份类型,引用d_identity
     */
    private String identityId;

    /**
     * 身份描述:用于切换视图.
     */
    private String identityDescription;

    /**
     * 资金账号ID
     */
    private String accountId;

    /**
     * 学校ID
     */
    private String schoolId;
    /**
     * 区域编号
     */
    private String area;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    /**
     * 获取 [M_USERS] 的属性 学乐号
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置[M_USERS]的属性学乐号
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * 获取 [M_USERS] 的属性 真实姓名
     */
    public String getRealName() {
        return realName;
    }

    /**
     * 设置[M_USERS]的属性真实姓名
     */
    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    /**
     * 获取 [M_USERS] 的属性 性别
     */
    public Integer getSex() {
        return sex;
    }

    /**
     * 设置[M_USERS]的属性性别
     */
    public void setSex(Integer sex) {
        this.sex = sex;
    }

    /**
     * 获取 [M_USERS] 的属性 生日
     */
    public Date getBirthday() {
        return birthday;
    }

    /**
     * 设置[M_USERS]的属性生日
     */
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    /**
     * 获取 [M_USERS] 的属性 头像
     */
    public String getIcon() {
        return icon == null || icon.length() == 0 ? UserConstants.ICON_DEFAULT : icon.trim();
    }

    /**
     * 设置[M_USERS]的属性头像
     */
    public void setIcon(String icon) {
        this.icon = icon == null ? null : icon.trim();
    }

    /**
     * 获取 [M_USERS] 的属性 qq
     */
    public String getQq() {
        return qq;
    }

    /**
     * 设置[M_USERS]的属性qq
     */
    public void setQq(String qq) {
        this.qq = qq == null ? null : qq.trim();
    }

    /**
     * 获取 [M_USERS] 的属性 资源类型
     */
    public Integer getrType() {
        return rType;
    }

    /**
     * 设置[M_USERS]的属性资源类型
     */
    public void setrType(Integer rType) {
        this.rType = rType;
    }

    /**
     * 获取 [M_USERS] 的属性 注册时间
     */
    public Date getAddTime() {
        return addTime;
    }

    /**
     * 设置[M_USERS]的属性注册时间
     */
    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    /**
     * 获取 [M_USERS] 的属性 注册来源
     */
    public Integer getRegType() {
        return regType;
    }

    /**
     * 设置[M_USERS]的属性注册来源
     */
    public void setRegType(Integer regType) {
        this.regType = regType;
    }

    /**
     * 获取 [M_USERS] 的属性 注册IP
     */
    public String getRegIp() {
        return regIp;
    }

    /**
     * 设置[M_USERS]的属性注册IP
     */
    public void setRegIp(String regIp) {
        this.regIp = regIp == null ? null : regIp.trim();
    }

    /**
     * 获取 [M_USERS] 的属性 用户状态,[0,有效;1,未初始化;2,离校;]
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置[M_USERS]的属性用户状态,[0,有效;1,未初始化;2,离校;]
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取 [M_USERS] 的属性 电话号码
     */
    public String getTel() {
        return tel;
    }

    /**
     * 设置[M_USERS]的属性电话号码
     */
    public void setTel(String tel) {
        this.tel = tel == null ? null : tel.trim();
    }

    /**
     * 获取 [M_USERS] 的属性 手机
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置[M_USERS]的属性手机
     */
    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    /**
     * 获取 [M_USERS] 的属性 邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置[M_USERS]的属性邮箱
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * 获取 [M_USERS] 的属性 个人签名
     */
    public String getSignature() {
        return signature;
    }

    /**
     * 设置[M_USERS]的属性个人签名
     */
    public void setSignature(String signature) {
        this.signature = signature == null ? null : signature.trim();
    }

    /**
     * 获取 [M_USERS] 的属性 封面,hdfs地址
     */
    public String getCover() {
        return cover;
    }

    /**
     * 设置[M_USERS]的属性封面,hdfs地址
     */
    public void setCover(String cover) {
        this.cover = cover == null ? null : cover.trim();
    }

    /**
     * 获取 [M_USERS] 的属性 修改时间,修改的当前数据库时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置[M_USERS]的属性修改时间,修改的当前数据库时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取 [M_USERS] 的属性 身份证
     */
    public String getIdcard() {
        return idcard;
    }

    /**
     * 设置[M_USERS]的属性身份证
     */
    public void setIdcard(String idcard) {
        this.idcard = idcard == null ? null : idcard.trim();
    }

    /**
     * 获取 [M_USERS] 的属性 身份类型,引用d_identity
     */
    public String getIdentityId() {
        return identityId;
    }

    /**
     * 设置[M_USERS]的属性身份类型,引用d_identity
     */
    public void setIdentityId(String identityId) {
        this.identityId = identityId == null ? null : identityId.trim();
    }

    /**
     * 获取 [M_USERS] 的属性 身份描述:用于切换视图.
     */
    public String getIdentityDescription() {
        return identityDescription;
    }

    /**
     * 设置[M_USERS]的属性身份描述:用于切换视图.
     */
    public void setIdentityDescription(String identityDescription) {
        this.identityDescription = identityDescription == null ? null : identityDescription.trim();
    }

    /**
     * 获取 [M_USERS] 的属性 资金账号ID
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * 设置[M_USERS]的属性资金账号ID
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId == null ? null : accountId.trim();
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }
}

