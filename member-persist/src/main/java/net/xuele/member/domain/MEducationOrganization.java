package net.xuele.member.domain;

public class MEducationOrganization {
    /**
     * 机构ID
     */
    private String orgId;

    /**
     * 名称
     */
    private String name;

    /**
     * 排序码
     */
    private String sort;

    /**
     * 层级
     */
    private Integer level;

    /**
     * 父机构ID
     */
    private String pId;

    /**
     * 父机构名称
     */
    private String pName;

    /**
     * 图标
     */
    private String image;

    /**
     * 封面
     */
    private String cover;

    /**
     * 介绍
     */
    private String explains;

    /**
     * 管理员ID
     */
    private String managerId;

    /**
     * 管理员名称
     */
    private String managerName;

    /**
     * 所属行政区划名称
     */
    private String areasName;

    /**
     * 所属行政区划
     */
    private String areas;

    /**
     * 类型
     */
    private Integer oType;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 获取 [M_EDUCATION_ORGANIZATION] 的属性 机构ID
     */
    public String getOrgId() {
        return orgId;
    }

    /**
     * 设置[M_EDUCATION_ORGANIZATION]的属性机构ID
     */
    public void setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
    }

    /**
     * 获取 [M_EDUCATION_ORGANIZATION] 的属性 名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置[M_EDUCATION_ORGANIZATION]的属性名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取 [M_EDUCATION_ORGANIZATION] 的属性 排序码
     */
    public String getSort() {
        return sort;
    }

    /**
     * 设置[M_EDUCATION_ORGANIZATION]的属性排序码
     */
    public void setSort(String sort) {
        this.sort = sort == null ? null : sort.trim();
    }

    /**
     * 获取 [M_EDUCATION_ORGANIZATION] 的属性 层级
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * 设置[M_EDUCATION_ORGANIZATION]的属性层级
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * 获取 [M_EDUCATION_ORGANIZATION] 的属性 父机构ID
     */
    public String getpId() {
        return pId;
    }

    /**
     * 设置[M_EDUCATION_ORGANIZATION]的属性父机构ID
     */
    public void setpId(String pId) {
        this.pId = pId == null ? null : pId.trim();
    }

    /**
     * 获取 [M_EDUCATION_ORGANIZATION] 的属性 父机构名称
     */
    public String getpName() {
        return pName;
    }

    /**
     * 设置[M_EDUCATION_ORGANIZATION]的属性父机构名称
     */
    public void setpName(String pName) {
        this.pName = pName == null ? null : pName.trim();
    }

    /**
     * 获取 [M_EDUCATION_ORGANIZATION] 的属性 图标
     */
    public String getImage() {
        return image;
    }

    /**
     * 设置[M_EDUCATION_ORGANIZATION]的属性图标
     */
    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    /**
     * 获取 [M_EDUCATION_ORGANIZATION] 的属性 封面
     */
    public String getCover() {
        return cover;
    }

    /**
     * 设置[M_EDUCATION_ORGANIZATION]的属性封面
     */
    public void setCover(String cover) {
        this.cover = cover == null ? null : cover.trim();
    }

    /**
     * 获取 [M_EDUCATION_ORGANIZATION] 的属性 介绍
     */
    public String getExplains() {
        return explains;
    }

    /**
     * 设置[M_EDUCATION_ORGANIZATION]的属性介绍
     */
    public void setExplains(String explains) {
        this.explains = explains == null ? null : explains.trim();
    }

    /**
     * 获取 [M_EDUCATION_ORGANIZATION] 的属性 管理员ID
     */
    public String getManagerId() {
        return managerId;
    }

    /**
     * 设置[M_EDUCATION_ORGANIZATION]的属性管理员ID
     */
    public void setManagerId(String managerId) {
        this.managerId = managerId == null ? null : managerId.trim();
    }

    /**
     * 获取 [M_EDUCATION_ORGANIZATION] 的属性 管理员名称
     */
    public String getManagerName() {
        return managerName;
    }

    /**
     * 设置[M_EDUCATION_ORGANIZATION]的属性管理员名称
     */
    public void setManagerName(String managerName) {
        this.managerName = managerName == null ? null : managerName.trim();
    }

    /**
     * 获取 [M_EDUCATION_ORGANIZATION] 的属性 所属行政区划名称
     */
    public String getAreasName() {
        return areasName;
    }

    /**
     * 设置[M_EDUCATION_ORGANIZATION]的属性所属行政区划名称
     */
    public void setAreasName(String areasName) {
        this.areasName = areasName == null ? null : areasName.trim();
    }

    /**
     * 获取 [M_EDUCATION_ORGANIZATION] 的属性 所属行政区划
     */
    public String getAreas() {
        return areas;
    }

    /**
     * 设置[M_EDUCATION_ORGANIZATION]的属性所属行政区划
     */
    public void setAreas(String areas) {
        this.areas = areas == null ? null : areas.trim();
    }

    /**
     * 获取 [M_EDUCATION_ORGANIZATION] 的属性 类型
     */
    public Integer getoType() {
        return oType;
    }

    /**
     * 设置[M_EDUCATION_ORGANIZATION]的属性类型
     */
    public void setoType(Integer oType) {
        this.oType = oType;
    }

    /**
     * 获取 [M_EDUCATION_ORGANIZATION] 的属性 状态
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置[M_EDUCATION_ORGANIZATION]的属性状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
}