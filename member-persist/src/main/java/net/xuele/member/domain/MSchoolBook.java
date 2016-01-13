package net.xuele.member.domain;

import java.util.Date;

public class MSchoolBook {
    /**
     * 学校ID
     */
    private String schoolId;

    /**
     * 课本ID
     */
    private String bookId;
    /**
     * 课本ID
     */
    private String extraBookId;

    /**
     * 设置时间
     */
    private Date updateTime;

    /**
     * 自增长主键
     */
    private Long id;

    public String getExtraBookId() {
        return extraBookId;
    }

    public void setExtraBookId(String extraBookId) {
        this.extraBookId = extraBookId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取 [M_SCHOOL_BOOK] 的属性 学校ID
     */
    public String getSchoolId() {
        return schoolId;
    }

    /**
     * 设置[M_SCHOOL_BOOK]的属性学校ID
     */
    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId == null ? null : schoolId.trim();
    }

    /**
     * 获取 [M_SCHOOL_BOOK] 的属性 课本ID
     */
    public String getBookId() {
        return bookId;
    }

    /**
     * 设置[M_SCHOOL_BOOK]的属性课本ID
     */
    public void setBookId(String bookId) {
        this.bookId = bookId == null ? null : bookId.trim();
    }

    /**
     * 获取 [M_SCHOOL_BOOK] 的属性 设置时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置[M_SCHOOL_BOOK]的属性设置时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}