package net.xuele.member.domain;

import java.util.List;

/**
 * Created by wuxh on 15/8/28.
 */
public class MUserLoginInfo {
    private String userId;
    private String realName;
    private String icon;
    private String identityId;
    private String identityDescription;
    private Integer status;
    private String accountId;
    private MSchool school;
    private List<MRole> roles;
    private List<MSchoolPeriod> periods;

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

    public MSchool getSchool() {
        return school;
    }

    public void setSchool(MSchool school) {
        this.school = school;
    }

    public List<MRole> getRoles() {
        return roles;
    }

    public void setRoles(List<MRole> roles) {
        this.roles = roles;
    }

    public String getIdentityId() {
        return identityId;
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

    public String getIdentityDescription() {
        return identityDescription;
    }

    public void setIdentityDescription(String identityDescription) {
        this.identityDescription = identityDescription;
    }

    public List<MSchoolPeriod> getPeriods() {
        return periods;
    }

    public void setPeriods(List<MSchoolPeriod> periods) {
        this.periods = periods;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}

