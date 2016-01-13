package net.xuele.member.web.wrapper;

/**
 * Created by ZhengTao on 2015/6/23 0023.
 */
public class ClassItemWrapper {
    /**
     * 班级ID
     */
    private String classId;
    /**
     * 班级别名
     */
    private String aliasName;

    /**
     * 班级名称
     */
    private String name;

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
