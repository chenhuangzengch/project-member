package net.xuele.member.web.wrapper;

import com.fasterxml.jackson.annotation.JsonFormat;
import net.xuele.member.dto.UserTeacherDTO;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * 老师返回对象封装 新建于 2015/6/22 0022.
 */
public class TeacherWrapper {

    /**
     * 用户ID（学乐号）
     */
    private String userId;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 职务ID
     */
    private String positionId;

    /**
     * 职务名称
     */
    private String positionName;

    /**
     * 最后一次登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastLoginTime;

    /**
     * 用户头像
     */
    private String icon;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public TeacherWrapper(UserTeacherDTO teacherDTO) {
        BeanUtils.copyProperties(teacherDTO, this);
    }

    public TeacherWrapper() {
    }


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

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
}
