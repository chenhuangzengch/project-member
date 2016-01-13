package net.xuele.member.dto;

import java.io.Serializable;
import java.util.Date;

public class ExtraBookDTO implements Serializable{
    private static final long serialVersionUID = 5284528566908528943L;
    /**
     * 教辅ID
     */
    private String extraBookId;

    /**
     * 教辅名称
     */
    private String extraBookName;

    /**
     * 课本ID(ct_book)
     */
    private String bookId;

    /**
     * 状态(1、有效，0、无效)
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private String updateUser;

    /**
     * 获取 [CT_EXTRA_BOOK] 的属性 教辅ID
     */
    public String getExtraBookId() {
        return extraBookId;
    }

    /**
     * 设置[CT_EXTRA_BOOK]的属性教辅ID
     */
    public void setExtraBookId(String extraBookId) {
        this.extraBookId = extraBookId == null ? null : extraBookId.trim();
    }

    /**
     * 获取 [CT_EXTRA_BOOK] 的属性 教辅名称
     */
    public String getExtraBookName() {
        return extraBookName;
    }

    /**
     * 设置[CT_EXTRA_BOOK]的属性教辅名称
     */
    public void setExtraBookName(String extraBookName) {
        this.extraBookName = extraBookName == null ? null : extraBookName.trim();
    }

    /**
     * 获取 [CT_EXTRA_BOOK] 的属性 课本ID(ct_book)
     */
    public String getBookId() {
        return bookId;
    }

    /**
     * 设置[CT_EXTRA_BOOK]的属性课本ID(ct_book)
     */
    public void setBookId(String bookId) {
        this.bookId = bookId == null ? null : bookId.trim();
    }

    /**
     * 获取 [CT_EXTRA_BOOK] 的属性 状态(1、有效，0、无效)
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置[CT_EXTRA_BOOK]的属性状态(1、有效，0、无效)
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取 [CT_EXTRA_BOOK] 的属性 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置[CT_EXTRA_BOOK]的属性创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取 [CT_EXTRA_BOOK] 的属性 创建人
     */
    public String getCreateUser() {
        return createUser;
    }

    /**
     * 设置[CT_EXTRA_BOOK]的属性创建人
     */
    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    /**
     * 获取 [CT_EXTRA_BOOK] 的属性 修改时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置[CT_EXTRA_BOOK]的属性修改时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取 [CT_EXTRA_BOOK] 的属性 修改人
     */
    public String getUpdateUser() {
        return updateUser;
    }

    /**
     * 设置[CT_EXTRA_BOOK]的属性修改人
     */
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser == null ? null : updateUser.trim();
    }
}