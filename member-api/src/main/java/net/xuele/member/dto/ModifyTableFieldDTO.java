package net.xuele.member.dto;

import net.xuele.member.constant.MemberMessageTypeEnum;

import java.io.Serializable;

/**
 * Created by wuxh on 15/9/8.
 */
public class ModifyTableFieldDTO<T> implements Serializable {

    private static final long serialVersionUID = 4466688810009570444L;

    private MemberMessageTypeEnum type;
    private T tableDTO;

    public ModifyTableFieldDTO() {
    }

    public ModifyTableFieldDTO(T tableDTO) {
        this.tableDTO = tableDTO;
    }

    public MemberMessageTypeEnum getType() {
        return type;
    }

    public void setType(MemberMessageTypeEnum type) {
        this.type = type;
    }

    public T getTableDTO() {
        return tableDTO;
    }

    public void setTableDTO(T tableDTO) {
        this.tableDTO = tableDTO;
    }
}
