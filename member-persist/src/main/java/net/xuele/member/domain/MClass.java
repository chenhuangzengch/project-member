package net.xuele.member.domain;

import java.util.Date;

public class MClass {
    /**
     * 班级ID
     */
    private String classId;

    /**
     * 所属学校ID
     */
    private String schoolId;

    /**
     * 所属学校名称
     */
    private String schoolName;

    /**
     * 班主任ID
     */
    private String chargeId;

    /**
     * 班主任名称
     */
    private String chargeName;

    /**
     * 学界
     */
    private Integer years;

    /**
     * 班级号,根据当前年级递增生成
     */
    private Integer codeSharing;

    /**
     * 图标
     */
    private String mImage;

    /**
     * 封面
     */
    private String cover;

    /**
     * 班级别名
     */
    private String aliasName;

    /**
     * 班级名称
     */
    private String name;

    /**
     * 班级说明
     */
    private String explains;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建者ID
     */
    private String creatorId;

    /**
     * 创建者名称
     */
    private String creatorName;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 学制学段ID
     */
    private String schoolPeriodId;

    /**
     * 获取 [M_CLASS] 的属性 班级ID
     */
    public String getClassId() {
        return classId;
    }

    /**
     * 设置[M_CLASS]的属性班级ID
     */
    public void setClassId(String classId) {
        this.classId = classId == null ? null : classId.trim();
    }

    /**
     * 获取 [M_CLASS] 的属性 所属学校ID
     */
    public String getSchoolId() {
        return schoolId;
    }

    /**
     * 设置[M_CLASS]的属性所属学校ID
     */
    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId == null ? null : schoolId.trim();
    }

    /**
     * 获取 [M_CLASS] 的属性 所属学校名称
     */
    public String getSchoolName() {
        return schoolName;
    }

    /**
     * 设置[M_CLASS]的属性所属学校名称
     */
    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName == null ? null : schoolName.trim();
    }

    /**
     * 获取 [M_CLASS] 的属性 班主任ID
     */
    public String getChargeId() {
        return chargeId;
    }

    /**
     * 设置[M_CLASS]的属性班主任ID
     */
    public void setChargeId(String chargeId) {
        this.chargeId = chargeId == null ? null : chargeId.trim();
    }

    /**
     * 获取 [M_CLASS] 的属性 班主任名称
     */
    public String getChargeName() {
        return chargeName;
    }

    /**
     * 设置[M_CLASS]的属性班主任名称
     */
    public void setChargeName(String chargeName) {
        this.chargeName = chargeName == null ? null : chargeName.trim();
    }

    /**
     * 获取 [M_CLASS] 的属性 学界
     */
    public Integer getYears() {
        return years;
    }

    /**
     * 设置[M_CLASS]的属性学界
     */
    public void setYears(Integer years) {
        this.years = years;
    }

    /**
     * 获取 [M_CLASS] 的属性 班级号,根据当前年级递增生成
     */
    public Integer getCodeSharing() {
        return codeSharing;
    }

    /**
     * 设置[M_CLASS]的属性班级号,根据当前年级递增生成
     */
    public void setCodeSharing(Integer codeSharing) {
        this.codeSharing = codeSharing;
    }

    /**
     * 获取 [M_CLASS] 的属性 图标
     */
    public String getmImage() {
        return mImage;
    }

    /**
     * 设置[M_CLASS]的属性图标
     */
    public void setmImage(String mImage) {
        this.mImage = mImage == null ? null : mImage.trim();
    }

    /**
     * 获取 [M_CLASS] 的属性 封面
     */
    public String getCover() {
        return cover;
    }

    /**
     * 设置[M_CLASS]的属性封面
     */
    public void setCover(String cover) {
        this.cover = cover == null ? null : cover.trim();
    }

    /**
     * 获取 [M_CLASS] 的属性 班级别名
     */
    public String getAliasName() {
        return aliasName;
    }

    /**
     * 设置[M_CLASS]的属性班级别名
     */
    public void setAliasName(String aliasName) {
        this.aliasName = aliasName == null ? null : aliasName.trim();
    }

    /**
     * 获取 [M_CLASS] 的属性 班级名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置[M_CLASS]的属性班级名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取 [M_CLASS] 的属性 班级说明
     */
    public String getExplains() {
        return explains;
    }

    /**
     * 设置[M_CLASS]的属性班级说明
     */
    public void setExplains(String explains) {
        this.explains = explains == null ? null : explains.trim();
    }

    /**
     * 获取 [M_CLASS] 的属性 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置[M_CLASS]的属性创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取 [M_CLASS] 的属性 创建者ID
     */
    public String getCreatorId() {
        return creatorId;
    }

    /**
     * 设置[M_CLASS]的属性创建者ID
     */
    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId == null ? null : creatorId.trim();
    }

    /**
     * 获取 [M_CLASS] 的属性 创建者名称
     */
    public String getCreatorName() {
        return creatorName;
    }

    /**
     * 设置[M_CLASS]的属性创建者名称
     */
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName == null ? null : creatorName.trim();
    }

    /**
     * 获取 [M_CLASS] 的属性 状态
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置[M_CLASS]的属性状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSchoolPeriodId() {
        return schoolPeriodId;
    }

    public void setSchoolPeriodId(String schoolPeriodId) {
        this.schoolPeriodId = schoolPeriodId;
    }
}