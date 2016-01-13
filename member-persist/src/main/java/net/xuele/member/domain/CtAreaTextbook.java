package net.xuele.member.domain;

import java.util.Date;

public class CtAreaTextbook {
    private String tbId;

    private String areaCode;

    private String areaName;

    /**
     * 课本ID
     */
    private String bookId;

    /**
     * 课本名称
     */
    private String bookName;

    /**
     * 科目
     */
    private String subjectId;

    /**
     * 年级
     */
    private Integer grade;

    /**
     * 学期
     */
    private Integer semester;

    /**
     * 教材版本ID（引用ct_editions)
     */
    private String editionId;

    private String userId;

    private Date editTime;

    public String getTbId() {
        return tbId;
    }

    public void setTbId(String tbId) {
        this.tbId = tbId == null ? null : tbId.trim();
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode == null ? null : areaCode.trim();
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName == null ? null : areaName.trim();
    }

    /**
     * 获取 [CT_AREA_TEXTBOOK] 的属性 课本ID
     */
    public String getBookId() {
        return bookId;
    }

    /**
     * 设置[CT_AREA_TEXTBOOK]的属性课本ID
     */
    public void setBookId(String bookId) {
        this.bookId = bookId == null ? null : bookId.trim();
    }

    /**
     * 获取 [CT_AREA_TEXTBOOK] 的属性 课本名称
     */
    public String getBookName() {
        return bookName;
    }

    /**
     * 设置[CT_AREA_TEXTBOOK]的属性课本名称
     */
    public void setBookName(String bookName) {
        this.bookName = bookName == null ? null : bookName.trim();
    }

    /**
     * 获取 [CT_AREA_TEXTBOOK] 的属性 科目
     */
    public String getSubjectId() {
        return subjectId;
    }

    /**
     * 设置[CT_AREA_TEXTBOOK]的属性科目
     */
    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId == null ? null : subjectId.trim();
    }

    /**
     * 获取 [CT_AREA_TEXTBOOK] 的属性 年级
     */
    public Integer getGrade() {
        return grade;
    }

    /**
     * 设置[CT_AREA_TEXTBOOK]的属性年级
     */
    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    /**
     * 获取 [CT_AREA_TEXTBOOK] 的属性 学期
     */
    public Integer getSemester() {
        return semester;
    }

    /**
     * 设置[CT_AREA_TEXTBOOK]的属性学期
     */
    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    /**
     * 获取 [CT_AREA_TEXTBOOK] 的属性 教材版本ID（引用ct_editions)
     */
    public String getEditionId() {
        return editionId;
    }

    /**
     * 设置[CT_AREA_TEXTBOOK]的属性教材版本ID（引用ct_editions)
     */
    public void setEditionId(String editionId) {
        this.editionId = editionId == null ? null : editionId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }
}