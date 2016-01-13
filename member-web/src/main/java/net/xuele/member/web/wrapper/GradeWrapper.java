package net.xuele.member.web.wrapper;

import net.xuele.member.util.NumberUtil;

/**
 * Created by zhengtao on 2015/6/23 0023.
 */
public class GradeWrapper {
    private int id;
    private int level;

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

    public String getName() {
        return NumberUtil.getSimpleNumber(getLevel()) + "年级";
    }
}
