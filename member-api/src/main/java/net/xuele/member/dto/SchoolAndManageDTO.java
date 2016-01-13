package net.xuele.member.dto;


import java.io.Serializable;
import java.util.Date;

/**
 * Created by wuxh on 15/8/1.
 */
public class SchoolAndManageDTO implements Serializable {

    private static final long serialVersionUID = -8562354441663090555L;
    /**
     * 学校ID
     */
    private String id;

    /**
     * 学校名称
     */
    private String name;

    /**
     * 学校英文名称
     */
    private String englishName;

    /**
     * 学校类别
     */
    private Integer sType;

    /**
     * 学校地址
     */
    private String address;

    /**
     * 联系电话
     */
    private String tel;

    /**
     * 校训
     */
    private String motto;

    /**
     * 学校网址
     */
    private String web;

    /**
     * 校徽
     */
    private String badge;

    /**
     * 管理员名称
     */
    private String managerName;

    /**
     * 管理员
     */
    private String manager;

    /**
     * 建校时间
     */
    private Date schoolTime;

    /**
     * 地区编号
     */
    private String area;

    /**
     * 地区编号名称
     */
    private String areaName;

    /**
     * 街道编号
     */
    private String streetArea;
    /**
     * 街道编号名称
     */
    private String streetAreaName;
    /**
     * 性别
     */
    private Integer managerSex;

    /**
     * 生日
     */
    private Date managerBirthday;

    /**
     * qq
     */
    private String managerQq;


    /**
     * 电话号码
     */
    private String managerTel;

    /**
     * 手机
     */
    private String managerMobile;

    /**
     * 邮箱
     */
    private String managerEmail;

    /**
     * 身份证
     */
    private String managerIdcard;

    /**
     * 密码
     */
    private String password;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public Integer getsType() {
        return sType;
    }

    public void setsType(Integer sType) {
        this.sType = sType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public Date getSchoolTime() {
        return schoolTime;
    }

    public void setSchoolTime(Date schoolTime) {
        this.schoolTime = schoolTime;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Integer getManagerSex() {
        return managerSex;
    }

    public void setManagerSex(Integer managerSex) {
        this.managerSex = managerSex;
    }

    public Date getManagerBirthday() {
        return managerBirthday;
    }

    public void setManagerBirthday(Date managerBirthday) {
        this.managerBirthday = managerBirthday;
    }

    public String getManagerQq() {
        return managerQq;
    }

    public void setManagerQq(String managerQq) {
        this.managerQq = managerQq;
    }

    public String getManagerTel() {
        return managerTel;
    }

    public void setManagerTel(String managerTel) {
        this.managerTel = managerTel;
    }

    public String getManagerMobile() {
        return managerMobile;
    }

    public void setManagerMobile(String managerMobile) {
        this.managerMobile = managerMobile;
    }

    public String getManagerEmail() {
        return managerEmail;
    }

    public void setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;
    }

    public String getManagerIdcard() {
        return managerIdcard;
    }

    public void setManagerIdcard(String managerIdcard) {
        this.managerIdcard = managerIdcard;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStreetArea() {
        return streetArea;
    }

    public void setStreetArea(String streetArea) {
        this.streetArea = streetArea;
    }

    public String getStreetAreaName() {
        return streetAreaName;
    }

    public void setStreetAreaName(String streetAreaName) {
        this.streetAreaName = streetAreaName;
    }
}
