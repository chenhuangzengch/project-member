package net.xuele.member.web.wrapper;

import net.xuele.member.dto.SchoolBookDTO;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * Created by kaike.du on 2015/7/16 0016.
 */
public class SchoolBookWrapper {
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
     * 该课本有多少教辅
     */
    private Integer extraBookCount;

    public SchoolBookWrapper() {
    }

    public SchoolBookWrapper(SchoolBookDTO result) {
        BeanUtils.copyProperties(result, this);
    }

    public Integer getExtraBookCount() {
        return extraBookCount;
    }

    public void setExtraBookCount(Integer extraBookCount) {
        this.extraBookCount = extraBookCount;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
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
