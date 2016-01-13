package net.xuele.member.constant;

/**
 * Created by wuxh on 15/9/8.
 */
public enum MemberMessageKeyEnum {

    SEND_MESSAGE_KEY("member.db.redundance.modify");

    private final String name;

    MemberMessageKeyEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
