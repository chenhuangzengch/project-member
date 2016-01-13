package net.xuele.member.domain;

public class DAreas {
    /**
     * 区划编号
     */
    private String code;

    /**
     * 区划名称
     */
    private String name;

    /**
     * 获取 [D_AREAS] 的属性 区划编号
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置[D_AREAS]的属性区划编号
     */
    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    /**
     * 获取 [D_AREAS] 的属性 区划名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置[D_AREAS]的属性区划名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
}