package net.xuele.member.dto;

import java.io.Serializable;

/**
 * Created by ZhengTao on 2015/6/5 0005.
 */
public class PairDTO<L, R> implements Serializable {
    private static final long serialVersionUID = -2188703087826974906L;
    private L left;
    private R right;

    public PairDTO() {

    }

    public PairDTO(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public L getLeft() {
        return left;
    }

    public void setLeft(L left) {
        this.left = left;
    }

    public R getRight() {
        return right;
    }

    public void setRight(R right) {
        this.right = right;
    }
}
