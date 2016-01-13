package net.xuele.member.domain;

import java.util.List;

/**
 * Created by wuxh on 15/8/28.
 */
public class MTeacherLoginInfo {
    private String userId;
    private String positionId;
    private String positionName;

    private String isManager;
    private List<MClass> classes;
    private CtBook ctBook;
//    private String extraBookId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIsManager() {
        return isManager;
    }

    public void setIsManager(String isManager) {
        this.isManager = isManager;
    }

    public List<MClass> getClasses() {
        return classes;
    }

    public void setClasses(List<MClass> classes) {
        this.classes = classes;
    }

    public CtBook getCtBook() {
        return ctBook;
    }

    public void setCtBook(CtBook ctBook) {
        this.ctBook = ctBook;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }
}
