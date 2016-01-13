package net.xuele.member.domain;

public class MTeacherBook {

    private Long id;
    /**
     * 学乐号(老师id)
     */
    private String userId;

    /**
     * 课本id
     */
    private String bookId;

    /**
     * 是否为主授（1，是，0，否）
     */
    private Integer isMain;

    /**
     * 所属学校ID
     */
    private String schoolId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取 [M_TEACHER_BOOK] 的属性 学乐号(老师id)
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置[M_TEACHER_BOOK]的属性学乐号(老师id)
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * 获取 [M_TEACHER_BOOK] 的属性 课本id
     */
    public String getBookId() {
        return bookId;
    }

    /**
     * 设置[M_TEACHER_BOOK]的属性课本id
     */
    public void setBookId(String bookId) {
        this.bookId = bookId == null ? null : bookId.trim();
    }

    /**
     * 获取 [M_TEACHER_BOOK] 的属性 是否为主授（1，是，0，否）
     */
    public Integer getIsMain() {
        return isMain;
    }

    /**
     * 设置[M_TEACHER_BOOK]的属性是否为主授（1，是，0，否）
     */
    public void setIsMain(Integer isMain) {
        this.isMain = isMain;
    }

    /**
     * 获取 [M_TEACHER_BOOK] 的属性 所属学校ID
     */
    public String getSchoolId() {
        return schoolId;
    }

    /**
     * 设置[M_TEACHER_BOOK]的属性所属学校ID
     */
    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId == null ? null : schoolId.trim();
    }
}