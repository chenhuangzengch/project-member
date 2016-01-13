package net.xuele.member.dto.page;

import net.xuele.common.page.PageRequest;

/**
 * Created by ZhengTao on 2015/7/1 0001.
 */
public class TeacherPageRequest extends PageRequest implements java.io.Serializable {
    private static final long serialVersionUID = -2142628559964641876L;
    private String schoolId;
    private String realName;
    private String positionId;
    private String subjectId;
    private Integer status;

    public int getPageSize() {
        if (pageSize > 0) {
            return pageSize;
        }
        return 100;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }
}
