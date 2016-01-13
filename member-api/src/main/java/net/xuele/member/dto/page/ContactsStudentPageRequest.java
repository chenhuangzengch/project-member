package net.xuele.member.dto.page;

import net.xuele.common.page.PageRequest;

import java.io.Serializable;

/**
 * Created by guochun.shen on 2015/9/21 0021.
 */
public class ContactsStudentPageRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 606714543786810643L;

    /**
     * 学校ID
     */
    private String schoolId;

    /**
     * 班级Id
     */
    private String classId;
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

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
