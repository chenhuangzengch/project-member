package net.xuele.member.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhongjian.xu on 2015/7/18 0018.
 */
public class GraduateingClassDTO implements Serializable {

    private static final long serialVersionUID = 577150086233325259L;
    /**
     * 年份
     */
    private int year;
    /**
     * 小学毕业班级
     */
    private List<ClassStudentDTO> primarySchool;
    /**
     * 初中毕业班级
     */
    private List<ClassStudentDTO> middleSchool;
    /**
     * 高中毕业班级
     */
    private List<ClassStudentDTO> highSchool;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<ClassStudentDTO> getPrimarySchool() {
        return primarySchool;
    }

    public void setPrimarySchool(List<ClassStudentDTO> primarySchool) {
        this.primarySchool = primarySchool;
    }

    public List<ClassStudentDTO> getMiddleSchool() {
        return middleSchool;
    }

    public void setMiddleSchool(List<ClassStudentDTO> middleSchool) {
        this.middleSchool = middleSchool;
    }

    public List<ClassStudentDTO> getHighSchool() {
        return highSchool;
    }

    public void setHighSchool(List<ClassStudentDTO> highSchool) {
        this.highSchool = highSchool;
    }
}
