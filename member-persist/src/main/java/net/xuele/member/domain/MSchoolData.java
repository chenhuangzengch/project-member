package net.xuele.member.domain;

/**
 * Created by guochun.shen on 2015/8/5 0005.
 */
public class MSchoolData {

    /**
     * 区分教师学生
     */
    private String type;

    /**
     * 教师学生数量
     */
    private long amount;

    /**
     * 学生数量
     */
    private String schoolId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }
}
