package net.xuele.member.service;

import net.xuele.common.exceptions.MemberException;

/**
 * Created by ZhengTao on 2015/7/21 0021.
 */
public interface SendMessageService {

    enum Template {
        ResetPassword("openApi_resetPassword"), BindEmail("Activate_Registration");

        Template(String name) {
            this.name = name;
        }

        Template() {
        }

        private String name;

        public String getName() {
            return name;
        }
    }

    enum CodeType {
        /**
         * 绑定手机，找回密码,手机邀请站外用户
         */
        BindPhone("bindMobile"), FindPassword("findPassword"), InviteUser("inviteUser");
        private String name;

        CodeType() {
        }

        CodeType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

    }

    void sendMessage(String phoneNumber, String message);

    /**
     * 发送校验短信
     *
     * @param phoneNumber 手机号码
     * @param type        绑定手机，找回密码
     * @param args        可选参数，当CodeType 为InviteUser时，需要传入邀请者的真实姓名
     */
    void sendMessage(String phoneNumber, CodeType type, String... args) throws MemberException;

    /**
     * 校验手机校验码
     *
     * @param phoneNumber 电话号码
     * @param code        用户获取的校验码
     * @param type        校验类型
     * @return true:成功
     */
    boolean checkCode(String phoneNumber, String code, CodeType type) throws MemberException;

    /**
     * 校验邮箱验证码
     *
     * @param email    用户邮箱
     * @param code     用户获取的校验码
     * @param template 校验类型
     */
    boolean checkCode(String email, String code, Template template) throws MemberException;


    /**
     * 发送重置密码的邮件
     *
     * @param targetEmail 邮件地址
     */
    void sendResetPwdEmail(String targetEmail) throws MemberException;

    /**
     * 发送绑定邮箱的邮件
     *
     * @param targetEmail 目标邮件地址
     * @param userName 用户名
     */
    void sendActiveCodeEmail(String targetEmail, String userName) throws MemberException;


}
