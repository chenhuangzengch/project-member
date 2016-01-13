package net.xuele.member.form;


import javax.validation.constraints.Size;

/**
 * zhihuan.cai 新建于 2015/7/31 0031.
 */
public class SaveClassForm {


    /**
     * 班级别名
     */
    @Size(min = 2,max = 10,message = "别名长度在2-10个中文字符")
    private String nick;

    /**
     * 班级id
     */
    private String id;

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
