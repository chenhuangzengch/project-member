package net.xuele.member.dto;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/7/8 0008.
 */
public class PeriodDTO implements Serializable {
    private static final long serialVersionUID = -7153096280517827427L;
    //小学年限
    private Integer primary;
    //中学年限
    private Integer junior;
    //高中年限
    private Integer senior;

    public Integer getPrimary() {
        return primary;
    }

    public void setPrimary(Integer primary) {
        this.primary = primary;
    }

    public Integer getJunior() {
        return junior;
    }

    public void setJunior(Integer junior) {
        this.junior = junior;
    }

    public Integer getSenior() {
        return senior;
    }

    public void setSenior(Integer senior) {
        this.senior = senior;
    }
}
