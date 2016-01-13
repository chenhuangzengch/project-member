package net.xuele.member.domain;

import java.util.Date;

/**
 * Created by wuxh on 15/8/28.
 */
public class MStudentLoginInfo {
    private String userId;

    private String studentNumber;

    private String familyName;

    private String familyCover;

    private Date joinClass;

    private MClass mClass;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getFamilyCover() {
        return familyCover;
    }

    public void setFamilyCover(String familyCover) {
        this.familyCover = familyCover;
    }

    public Date getJoinClass() {
        return joinClass;
    }

    public void setJoinClass(Date joinClass) {
        this.joinClass = joinClass;
    }

    public MClass getmClass() {
        return mClass;
    }

    public void setmClass(MClass mClass) {
        this.mClass = mClass;
    }
}
