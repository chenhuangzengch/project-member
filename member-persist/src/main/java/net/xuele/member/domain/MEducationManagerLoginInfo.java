package net.xuele.member.domain;

/**
 * Created by wuxh on 15/8/29.
 */
public class MEducationManagerLoginInfo {

    private String userId;
    private String educationId;
    private String educationName;
    private Integer isManger;
    private String positionId;
    private String positionName;
    private CtBook ctBook;
    /**
     * 地区编号
     */
    private String area;

    /**
     * 地区编号名称
     */
    private String areaName;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEducationId() {
        return educationId;
    }

    public void setEducationId(String educationId) {
        this.educationId = educationId;
    }

    public String getEducationName() {
        return educationName;
    }

    public void setEducationName(String educationName) {
        this.educationName = educationName;
    }

    public Integer getIsManger() {
        return isManger;
    }

    public void setIsManger(Integer isManger) {
        this.isManger = isManger;
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

    public CtBook getCtBook() {
        return ctBook;
    }

    public void setCtBook(CtBook ctBook) {
        this.ctBook = ctBook;
    }
}
