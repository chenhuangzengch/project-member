package net.xuele.member.domain;

/**
 * Created by zhongjian.xu on 2015/7/31 0031.
 */
public class KidInfo {
    /**
     * 用户ID
     */
    private String userId;
    /**
     *用户名称
     */
    private String realName;
    /**
     *年级 1,2
     */
    private Integer grade;
    /**
     * 学期 1，2
     */
    private Integer semester;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }
}
