package net.xuele.member.domain;

/**
 * Created by zhongjian.xu on 2015/7/17 0017.
 */
public class CtBookInfo {
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
    private int grade;
    /**
     * 年级名称
     */
    private String gradeName;
    /**
     * 教材ID
     */
    private String bookId;
    /**
     * 教材名称
     */
    private String bookName;
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
     * 学期
     */
    private Integer semester;
    /**
     * 学期描述
     */
    private String semesterDescribe;
    /**
    * 出版社名称
    */
    private String pressName;
    /**
     * 教材版本名称
     */
    private String editionName;

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

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public String getSemesterDescribe() {
        return semesterDescribe;
    }

    public void setSemesterDescribe(String semesterDescribe) {
        this.semesterDescribe = semesterDescribe;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public int getIsMain() {
        return isMain;
    }

    public void setIsMain(int isMain) {
        this.isMain = isMain;
    }
}
