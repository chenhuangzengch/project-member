package net.xuele.member.web.wrapper;

/**
 * 资源wrapper
 * zhihuan.cai 新建于 2015/7/12 0012.
 */
public class ResourceWrapper {


    private String id;

    private String name;

    public ResourceWrapper() {

    }

    public ResourceWrapper(String id, String name) {
        this.id = id;
        this.name = name;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
