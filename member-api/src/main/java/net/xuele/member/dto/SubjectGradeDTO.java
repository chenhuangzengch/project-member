package net.xuele.member.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhongjian.xu on 2015/7/28 0028.
 */
public class SubjectGradeDTO implements Serializable {
    private static final long serialVersionUID = -2236558638310373431L;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 学校ID
     */
    private String schoolId;
    /**
     * 区域编号
     */
    private String area;
    /**
     * 科目列表
     */
    private List<CtBookDTO> subjectList;
    /**
     * 班级列表
     */
    private List<ClassDTO> classList;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public List<CtBookDTO> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<CtBookDTO> subjectList) {
        this.subjectList = subjectList;
    }

    public List<ClassDTO> getClassList() {
        return classList;
    }

    public void setClassList(List<ClassDTO> classList) {
        this.classList = classList;
    }
}
