package net.xuele.member.dto;

import net.xuele.common.dto.ClassInfoDTO;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhongjian.xu on 2015/8/7 0007.
 */
public class MyMessageDTO implements Serializable {
    private static final long serialVersionUID = 6878106019768545427L;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 用户名称
     */
    private String realName;
    /**
     * 用户头像
     */
    private String icon;
    /**
     * 用户签名
     */
    private String signature;
    /**
     * 学校名称
     */
    private String schoolName;
    /**
     * 班级名称
     */
    private String className;
    /**
     * 授课科目
     */
    private List<CtBookDTO> subjectList;
    /**
     * 授课班级
     */
    private List<ClassInfoDTO> classList;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<CtBookDTO> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<CtBookDTO> subjectList) {
        this.subjectList = subjectList;
    }

    public List<ClassInfoDTO> getClassList() {
        return classList;
    }

    public void setClassList(List<ClassInfoDTO> classList) {
        this.classList = classList;
    }
}



