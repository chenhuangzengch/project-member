package net.xuele.member.dto;

import java.io.Serializable;

/**
 * Created by zhongjian.xu on 2015/7/13 0013.
 */
public class BookDTO implements Serializable {
    private static final long serialVersionUID = 322127381374938061L;

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
