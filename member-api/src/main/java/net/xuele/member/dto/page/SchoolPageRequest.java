package net.xuele.member.dto.page;

import net.xuele.common.page.PageRequest;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2015/6/8 0008.
 */
public class SchoolPageRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = -7543066745707582065L;

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
     * 学校学制总和(小学学制+初中学制+高中学制)
     */
    private Integer lengthOfSchool;

    /**
     * 校徽
     */
    private String badge;

    /**
     * 学校封面
     */
    private String cover;

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
     * 注册时间
     */
    private Date addTime;

    /**
     * 地区编号
     */
    private String area;

    /**
     * 地区编号名称
     */
    private String areaName;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 学校简介
     */
    private String about;

    /**
     * 获取 [M_SCHOOL] 的属性 学校ID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置[M_SCHOOL]的属性学校ID
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 获取 [M_SCHOOL] 的属性 学校名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置[M_SCHOOL]的属性学校名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取 [M_SCHOOL] 的属性 学校英文名称
     */
    public String getEnglishName() {
        return englishName;
    }

    /**
     * 设置[M_SCHOOL]的属性学校英文名称
     */
    public void setEnglishName(String englishName) {
        this.englishName = englishName == null ? null : englishName.trim();
    }

    /**
     * 获取 [M_SCHOOL] 的属性 学校类别
     */
    public Integer getsType() {
        return sType;
    }

    /**
     * 设置[M_SCHOOL]的属性学校类别
     */
    public void setsType(Integer sType) {
        this.sType = sType;
    }

    /**
     * 获取 [M_SCHOOL] 的属性 学校地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置[M_SCHOOL]的属性学校地址
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    /**
     * 获取 [M_SCHOOL] 的属性 联系电话
     */
    public String getTel() {
        return tel;
    }

    /**
     * 设置[M_SCHOOL]的属性联系电话
     */
    public void setTel(String tel) {
        this.tel = tel == null ? null : tel.trim();
    }

    /**
     * 获取 [M_SCHOOL] 的属性 校训
     */
    public String getMotto() {
        return motto;
    }

    /**
     * 设置[M_SCHOOL]的属性校训
     */
    public void setMotto(String motto) {
        this.motto = motto == null ? null : motto.trim();
    }

    /**
     * 获取 [M_SCHOOL] 的属性 学校网址
     */
    public String getWeb() {
        return web;
    }

    /**
     * 设置[M_SCHOOL]的属性学校网址
     */
    public void setWeb(String web) {
        this.web = web == null ? null : web.trim();
    }

    /**
     * 获取 [M_SCHOOL] 的属性 学校学制总和(小学学制+初中学制+高中学制)
     */
    public Integer getLengthOfSchool() {
        return lengthOfSchool;
    }

    /**
     * 设置[M_SCHOOL]的属性学校学制总和(小学学制+初中学制+高中学制)
     */
    public void setLengthOfSchool(Integer lengthOfSchool) {
        this.lengthOfSchool = lengthOfSchool;
    }

    /**
     * 获取 [M_SCHOOL] 的属性 校徽
     */
    public String getBadge() {
        return badge;
    }

    /**
     * 设置[M_SCHOOL]的属性校徽
     */
    public void setBadge(String badge) {
        this.badge = badge == null ? null : badge.trim();
    }

    /**
     * 获取 [M_SCHOOL] 的属性 学校封面
     */
    public String getCover() {
        return cover;
    }

    /**
     * 设置[M_SCHOOL]的属性学校封面
     */
    public void setCover(String cover) {
        this.cover = cover == null ? null : cover.trim();
    }

    /**
     * 获取 [M_SCHOOL] 的属性 管理员名称
     */
    public String getManagerName() {
        return managerName;
    }

    /**
     * 设置[M_SCHOOL]的属性管理员名称
     */
    public void setManagerName(String managerName) {
        this.managerName = managerName == null ? null : managerName.trim();
    }

    /**
     * 获取 [M_SCHOOL] 的属性 管理员
     */
    public String getManager() {
        return manager;
    }

    /**
     * 设置[M_SCHOOL]的属性管理员
     */
    public void setManager(String manager) {
        this.manager = manager == null ? null : manager.trim();
    }

    /**
     * 获取 [M_SCHOOL] 的属性 建校时间
     */
    public Date getSchoolTime() {
        return schoolTime;
    }

    /**
     * 设置[M_SCHOOL]的属性建校时间
     */
    public void setSchoolTime(Date schoolTime) {
        this.schoolTime = schoolTime;
    }

    /**
     * 获取 [M_SCHOOL] 的属性 注册时间
     */
    public Date getAddTime() {
        return addTime;
    }

    /**
     * 设置[M_SCHOOL]的属性注册时间
     */
    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    /**
     * 获取 [M_SCHOOL] 的属性 地区编号
     */
    public String getArea() {
        return area;
    }

    /**
     * 设置[M_SCHOOL]的属性地区编号
     */
    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }

    /**
     * 获取 [M_SCHOOL] 的属性 地区编号名称
     */
    public String getAreaName() {
        return areaName;
    }

    /**
     * 设置[M_SCHOOL]的属性地区编号名称
     */
    public void setAreaName(String areaName) {
        this.areaName = areaName == null ? null : areaName.trim();
    }

    /**
     * 获取 [M_SCHOOL] 的属性 状态
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置[M_SCHOOL]的属性状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取 [M_SCHOOL] 的属性 学校简介
     */
    public String getAbout() {
        return about;
    }

    /**
     * 设置[M_SCHOOL]的属性学校简介
     */
    public void setAbout(String about) {
        this.about = about == null ? null : about.trim();
    }
}
