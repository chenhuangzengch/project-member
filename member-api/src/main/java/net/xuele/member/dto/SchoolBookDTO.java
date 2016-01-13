package net.xuele.member.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by kaike.du on 2015/7/16 0016.
 */
public class SchoolBookDTO implements Serializable {
    private static final long serialVersionUID = 837486808442134223L;
    /**
     * 学校ID
     */
    private String schoolId;

    /**
     * 课本ID
     */
    private String bookId;

    /**
     * 课本名称
     */
    private String bookName;

    /**
     * 设置时间
     */
    private Date updateTime;

    /**
     * 教辅id
     */
    private String extraBookId;
    /**
     * 该课本有多少教辅
     */
    private Integer extraBookCount;

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Integer getExtraBookCount() {
        return extraBookCount;
    }

    public void setExtraBookCount(Integer extraBookCount) {
        this.extraBookCount = extraBookCount;
    }

    public String getExtraBookId() {
        return extraBookId;
    }

    public void setExtraBookId(String extraBookId) {
        this.extraBookId = extraBookId;
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
