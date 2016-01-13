package net.xuele.member.constant;

/**
 * Created by wuxh on 15/9/8.
 */
public enum MemberMessageTypeEnum {
    //修改学校名
    MODIFY_SCHOOL_NAME("modify_school_name");

    private final String name;

    MemberMessageTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
