package net.xuele.member.domain;

public class MSchoolPeriod {
    /**
     * 非业务序列主键
     */
    private String id;

    /**
     * 学段(0,1,2)
     */
    private Integer section;

    /**
     * 学段说明(0,小学,1初中,2.高中)
     */
    private String sectionDisplay;

    /**
     * 学制(3年制,4年制,5年制)
     */
    private Integer length;

    /**
     * 关联学校ID
     */
    private String schoolId;

    /**
     * 获取 [M_SCHOOL_PERIOD] 的属性 非业务序列主键
     */
    public String getId() {
        return id;
    }

    /**
     * 设置[M_SCHOOL_PERIOD]的属性非业务序列主键
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 获取 [M_SCHOOL_PERIOD] 的属性 学段(0,1,2)
     */
    public Integer getSection() {
        return section;
    }

    /**
     * 设置[M_SCHOOL_PERIOD]的属性学段(0,1,2)
     */
    public void setSection(Integer section) {
        this.section = section;
    }

    /**
     * 获取 [M_SCHOOL_PERIOD] 的属性 学段说明(0,小学,1初中,2.高中)
     */
    public String getSectionDisplay() {
        return sectionDisplay;
    }

    /**
     * 设置[M_SCHOOL_PERIOD]的属性学段说明(0,小学,1初中,2.高中)
     */
    public void setSectionDisplay(String sectionDisplay) {
        this.sectionDisplay = sectionDisplay == null ? null : sectionDisplay.trim();
    }

    /**
     * 获取 [M_SCHOOL_PERIOD] 的属性 学制(3年制,4年制,5年制)
     */
    public Integer getLength() {
        return length;
    }

    /**
     * 设置[M_SCHOOL_PERIOD]的属性学制(3年制,4年制,5年制)
     */
    public void setLength(Integer length) {
        this.length = length;
    }

    /**
     * 获取 [M_SCHOOL_PERIOD] 的属性 关联学校ID
     */
    public String getSchoolId() {
        return schoolId;
    }

    /**
     * 设置[M_SCHOOL_PERIOD]的属性关联学校ID
     */
    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId == null ? null : schoolId.trim();
    }
}