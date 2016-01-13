package net.xuele.member.dto.page;

import net.xuele.common.page.PageRequest;

import java.io.Serializable;

/**
 * Created by guochun.shen on 2015/9/18 0018.
 */
public class ContactsTeacherPageRequest extends PageRequest implements Serializable{
    private static final long serialVersionUID = -6815092507841174808L;
    /**
     * 学校ID
     */
    private String schoolId;

    private String subjectId;
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

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }
}
