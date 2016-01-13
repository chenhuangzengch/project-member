package net.xuele.member.dto;

import net.xuele.member.util.NumberUtil;

/**
 * Created by ZhengTao on 2015/6/18 0018.
 */
public class GradeDTO implements java.io.Serializable {
    private static final long serialVersionUID = 1132262006500655396L;
    /**
     * 虚拟id,现在表示什么年份入学:2010,2011
     */
    private int id;
    /**
     * 年级:1:一年级,2:二年级
     */
    private int level;
    /**
     * 班级数量
     */
    private int classNumber;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getClassNumber() {
        return classNumber;
    }

    public void setClassNumber(int classNumber) {
        this.classNumber = classNumber;
    }

    public String getLevelName() {
        return NumberUtil.getSimpleNumber(getLevel()) + "年级";
    }

}
