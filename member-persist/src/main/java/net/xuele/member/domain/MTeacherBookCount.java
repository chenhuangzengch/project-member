package net.xuele.member.domain;

/**
 * Created by guochun.shen on 2015/9/17 0017.
 */
public class MTeacherBookCount {
    private String  subjectId;
    private Long userNumber;

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public Long getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(Long userNumber) {
        this.userNumber = userNumber;
    }
}
