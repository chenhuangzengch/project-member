package net.xuele.member.dto;

/**
 * Created by Administrator on 2015/7/14 0014.
 */
public class CtBookDTO implements java.io.Serializable {
    private static final long serialVersionUID = 7408130882054263170L;
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
     * 科目名称（冗余字段）
     */
    private String subjectName;

    /**
     * 年级
     */
    private Integer grade;
    /**
     * 年级名称
     */
    private String gradeName;

    /**
     * 学期
     */
    private Integer semester;

    /**
     * 学期描述
     */
    private String semesterDescribe;

    /**
     * 教材版本ID（引用ct_editions)
     */
    private String editionId;

    /**
     * 是否是主授教材  1：是  0：否
     */
    private int isMain;
    /**
     * 学校ID
     */
    private String schoolId;
    /**
     * 学校名称
     */
    private String schoolName;
    /**
     * 出版社名称
     */
    private String pressName;
    /**
     * 教材版本名称
     */
    private String editionName;

    /**
     * 教辅ID
     */
    private String extraBookId;

    /**
     * 教辅名称
     */
    private String extraBookName;
    /**
     * 教辅数量
     */
    private Integer extraCount;
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

    public Integer getExtraCount() {
        return extraCount;
    }

    public void setExtraCount(Integer extraCount) {
        this.extraCount = extraCount;
    }

    public String getPressName() {
        return pressName;
    }

    public void setPressName(String pressName) {
        this.pressName = pressName;
    }

    public String getEditionName() {
        return editionName;
    }

    public void setEditionName(String editionName) {
        this.editionName = editionName;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    /**
     * 获取 [CT_BOOK] 的属性 课本ID
     */
    public String getBookId() {
        return bookId;
    }

    /**
     * 设置[CT_BOOK]的属性课本ID
     */
    public void setBookId(String bookId) {
        this.bookId = bookId == null ? null : bookId.trim();
    }

    /**
     * 获取 [CT_BOOK] 的属性 课本名称
     */
    public String getBookName() {
        return bookName;
    }

    /**
     * 设置[CT_BOOK]的属性课本名称
     */
    public void setBookName(String bookName) {
        this.bookName = bookName == null ? null : bookName.trim();
    }

    /**
     * 获取 [CT_BOOK] 的属性 科目
     */
    public String getSubjectId() {
        return subjectId;
    }

    /**
     * 设置[CT_BOOK]的属性科目
     */
    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId == null ? null : subjectId.trim();
    }

    /**
     * 获取 [CT_BOOK] 的属性 科目名称（冗余字段）
     */
    public String getSubjectName() {
        return subjectName;
    }

    /**
     * 设置[CT_BOOK]的属性科目名称（冗余字段）
     */
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName == null ? null : subjectName.trim();
    }

    /**
     * 获取 [CT_BOOK] 的属性 年级
     */
    public Integer getGrade() {
        return grade;
    }

    /**
     * 设置[CT_BOOK]的属性年级
     */
    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    /**
     * 获取 [CT_BOOK] 的属性 学期
     */
    public Integer getSemester() {
        return semester;
    }

    /**
     * 设置[CT_BOOK]的属性学期
     */
    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    /**
     * 获取 [CT_BOOK] 的属性 学期描述
     */
    public String getSemesterDescribe() {
        return semesterDescribe;
    }

    /**
     * 设置[CT_BOOK]的属性学期描述
     */
    public void setSemesterDescribe(String semesterDescribe) {
        this.semesterDescribe = semesterDescribe == null ? null : semesterDescribe.trim();
    }

    /**
     * 获取 [CT_BOOK] 的属性 教材版本ID（引用ct_editions)
     */
    public String getEditionId() {
        return editionId;
    }

    /**
     * 设置[CT_BOOK]的属性教材版本ID（引用ct_editions)
     */
    public void setEditionId(String editionId) {
        this.editionId = editionId == null ? null : editionId.trim();
    }

    public int getIsMain() {
        return isMain;
    }

    public void setIsMain(int isMain) {
        this.isMain = isMain;
    }

    /**
     * 获取 [CT_EXTRA_BOOK] 的属性 教辅ID
     */
    public String getExtraBookId() {
        return extraBookId;
    }

    /**
     * 设置[CT_EXTRA_BOOK]的属性教辅ID
     */
    public void setExtraBookId(String extraBookId) {
        this.extraBookId = extraBookId == null ? null : extraBookId.trim();
    }

    /**
     * 获取 [CT_EXTRA_BOOK] 的属性 教辅名称
     */
    public String getExtraBookName() {
        return extraBookName;
    }

    /**
     * 设置[CT_EXTRA_BOOK]的属性教辅名称
     */
    public void setExtraBookName(String extraBookName) {
        this.extraBookName = extraBookName == null ? null : extraBookName.trim();
    }

    @Override
    public String toString() {
        return "CtBookDTO{" +
                "bookId='" + bookId + '\'' +
                ", bookName='" + bookName + '\'' +
                ", subjectId='" + subjectId + '\'' +
                ", subjectName='" + subjectName + '\'' +
                ", grade=" + grade +
                ", gradeName='" + gradeName + '\'' +
                ", semester=" + semester +
                ", semesterDescribe='" + semesterDescribe + '\'' +
                ", editionId='" + editionId + '\'' +
                ", isMain=" + isMain +
                ", schoolId='" + schoolId + '\'' +
                ", schoolName='" + schoolName + '\'' +
                ", pressName='" + pressName + '\'' +
                ", editionName='" + editionName + '\'' +
                '}';
    }
}
