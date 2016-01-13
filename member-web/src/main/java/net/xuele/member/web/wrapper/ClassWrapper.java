package net.xuele.member.web.wrapper;

import net.xuele.member.dto.ClassDTO;
import org.springframework.beans.BeanUtils;

/**
 * Created by ZhengTao on 2015/8/14 0014.
 */
public class ClassWrapper {
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

    /**
     * 班主任ID
     */
    private String chargeId;

    /**
     * 班主任名称
     */
    private String chargeName;
    /**
     * 班级人数
     */
    private int studentNumber;

    public ClassWrapper() {
    }

    public ClassWrapper(ClassDTO classDTO) {
        BeanUtils.copyProperties(classDTO, this);
    }

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

    public String getChargeId() {
        return chargeId;
    }

    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
    }

    public String getChargeName() {
        return chargeName;
    }

    public void setChargeName(String chargeName) {
        this.chargeName = chargeName;
    }

    public int getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(int studentNumber) {
        this.studentNumber = studentNumber;
    }
}
