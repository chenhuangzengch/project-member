package net.xuele.member.web.wrapper;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang.time.DateFormatUtils;

import java.util.Date;

/**
 * 老师返回对象封装 新建于 2015/6/22 0022.
 */
public class TeacherLoginWrapper {
    /**
     * 用户ID（学乐号）
     */
    private String userId;

    /**
     * 真实姓名
     */
    private String realName;


    /**
     * 主授科目
     */
    private String subjectName;

    /**
     * 职务名称
     */
    private String positionName;

    /**
     * 头像
     */
    private String icon;

    /**
     * 最后一次登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastLoginTime;

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

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
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

    public String getLastLoginTimeStr() {
        if (lastLoginTime == null) {
            return "";
        }
        return DateFormatUtils.format(lastLoginTime, "yyyy-MM-dd HH:mm");
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
