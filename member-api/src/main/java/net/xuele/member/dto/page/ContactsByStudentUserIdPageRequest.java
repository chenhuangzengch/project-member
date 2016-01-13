package net.xuele.member.dto.page;

import net.xuele.common.page.PageRequest;

import java.io.Serializable;

/**
 * Created by guochun.shen on 2015/9/22 0022.
 */
public class ContactsByStudentUserIdPageRequest  extends PageRequest implements Serializable {
    private static final long serialVersionUID = -7954300981640451679L;
    /**
     * 学校ID
     */
    private String schoolId;

    /**
     * 班级Id
     */
    private String userId;

    /**
     * 请求类型"requestFlag":学生角色：STUDENT_0 == > （家人） STUDENT_1 == >  （同学）STUDENT_2 == > （老师）
     *                      家长角色：PARENT_0 == > (家人) PARENT_1 老师）
     */
    private String requestFlag;

    /**
     * 是否分页
     */
    private int limit;

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRequestFlag() {
        return requestFlag;
    }

    public void setRequestFlag(String requestFlag) {
        this.requestFlag = requestFlag;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
