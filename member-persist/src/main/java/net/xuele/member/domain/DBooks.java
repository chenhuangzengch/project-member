package net.xuele.member.domain;

public class DBooks {
    /**
     * 课本ID
     */
    private String bookId;

    /**
     * 课本名称
     */
    private String bookName;

    /**
     * 年级
     */
    private Integer gradeNum;

    /**
     * 学期(1、上学期，2、下学期)
     */
    private Integer semester;

    /**
     * 科目id
     */
    private Integer subjectId;

    /**
     * 教材ID
     */
    private Integer editionId;
    /**
     * 是否默认(0:不是;1:是)
     */
    private Integer isDefault;

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    /**
     * 获取 [D_BOOKS] 的属性 课本ID
     */
    public String getBookId() {
        return bookId;
    }

    /**
     * 设置[D_BOOKS]的属性课本ID
     */
    public void setBookId(String bookId) {
        this.bookId = bookId == null ? null : bookId.trim();
    }

    /**
     * 获取 [D_BOOKS] 的属性 课本名称
     */
    public String getBookName() {
        return bookName;
    }

    /**
     * 设置[D_BOOKS]的属性课本名称
     */
    public void setBookName(String bookName) {
        this.bookName = bookName == null ? null : bookName.trim();
    }

    /**
     * 获取 [D_BOOKS] 的属性 年级
     */
    public Integer getGradeNum() {
        return gradeNum;
    }

    /**
     * 设置[D_BOOKS]的属性年级
     */
    public void setGradeNum(Integer gradeNum) {
        this.gradeNum = gradeNum;
    }

    /**
     * 获取 [D_BOOKS] 的属性 学期(1、上学期，2、下学期)
     */
    public Integer getSemester() {
        return semester;
    }

    /**
     * 设置[D_BOOKS]的属性学期(1、上学期，2、下学期)
     */
    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    /**
     * 获取 [D_BOOKS] 的属性 科目id
     */
    public Integer getSubjectId() {
        return subjectId;
    }

    /**
     * 设置[D_BOOKS]的属性科目id
     */
    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    /**
     * 获取 [D_BOOKS] 的属性 教材ID
     */
    public Integer getEditionId() {
        return editionId;
    }

    /**
     * 设置[D_BOOKS]的属性教材ID
     */
    public void setEditionId(Integer editionId) {
        this.editionId = editionId;
    }
}