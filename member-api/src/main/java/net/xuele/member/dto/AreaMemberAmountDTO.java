package net.xuele.member.dto;

import java.io.Serializable;

/**
 * Created by guochun.shen on 2015/8/5 0005.
 */
public class AreaMemberAmountDTO implements Serializable {
    private static final long serialVersionUID = 3378533735276552480L;

    //学校数量
    private long schoolAmount;
    //教师数量
    private long teacherAmount;
    //学生数量
    private long studentAmount;

    public long getSchoolAmount() {
        return schoolAmount;
    }

    public void setSchoolAmount(long schoolAmount) {
        this.schoolAmount = schoolAmount;
    }

    public long getTeacherAmount() {
        return teacherAmount;
    }

    public void setTeacherAmount(long teacherAmount) {
        this.teacherAmount = teacherAmount;
    }

    public long getStudentAmount() {
        return studentAmount;
    }

    public void setStudentAmount(long studentAmount) {
        this.studentAmount = studentAmount;
    }
}
