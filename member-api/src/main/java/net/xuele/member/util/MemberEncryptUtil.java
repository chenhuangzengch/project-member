package net.xuele.member.util;

import net.xuele.common.exceptions.MemberException;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;

/**
 * Created by wuxh on 15/7/30.
 */
public class MemberEncryptUtil {
    private static final String PASSWORD_KEY = PropertiesUtil.getProperty("memberEncryptUtil.passwordKey");

    public static String aesEncrypt(String password) {
        String str;
        try {
            byte[] keyBytes = Arrays.copyOf(PASSWORD_KEY.getBytes("ASCII"), 16);

            SecretKey key = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] cleartext = password.getBytes("UTF-8");
            byte[] ciphertextBytes = cipher.doFinal(cleartext);

            str = new String(Hex.encodeHex(ciphertextBytes)).toUpperCase();

        } catch (Exception e) {
            throw new MemberException("aes加密失败，具体错误信息为：" + e.getMessage());
        }
        return str;
    }

    public static String aesDecrypt(String encryptResult) {
        String str;
        try {
            byte[] keyBytes = Arrays.copyOf(PASSWORD_KEY.getBytes("ASCII"), 16);

            SecretKey key = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] cleartext = Hex.decodeHex(encryptResult.toCharArray());
            byte[] ciphertextBytes = cipher.doFinal(cleartext);

            str = new String(ciphertextBytes, "UTF-8");

        } catch (Exception e) {
            throw new MemberException("aes解密失败，具体信息错误为：" + e.getMessage());
        }
        return str;
    }

    /**
     * 加密方法,到时候统一改
     *
     * @param password 明文密码
     * @return 加密后的密码
     */
    public static String encrypt(String password) {
        return aesEncrypt(password);
    }

    /**
     * 密码解密，到时候统一改
     *
     * @param password
     * @return
     */
    public static String decrypt(String password) {
        return aesDecrypt(password);
    }

    public static void main(String[] args) {
        //System.out.println(decrypt("2478C601CC820821E04453B1AB886038"));
        System.out.println(encrypt(null));
    }
/*
    public static void main(String[] args) {
        String content = "123456";
        String password = PASSWORD_KEY;
        // 加密
        System.out.println("加密前：" + content);
        String encryptResult = aesEncrypt(content);
        System.out.println("加密后：" + encryptResult);
        // 解密
        String decryptResult = aesDecrypt(encryptResult);
        System.out.println("解密后：" + decryptResult);
//        key:254B24719C80BAB4BAC1E2F70834E047219B2B5FA3876CB3783534288D91F5C70A7C176ED6B4806B9D523D0B3ED08D3E
        System.out.println(aesDecrypt("B284F322211E0F80C64656EF1F128A14E7D3F71CF95EE13A8939BB09ECF2E81795B57ED7AD23C8B8769577564AF2997C"));
        System.out.println(aesDecrypt("B284F322211E0F80C64656EF1F128A14E7D3F71CF95EE13A8939BB09ECF2E81795B57ED7AD23C8B8769577564AF2997C"));
        //FD03D7860328A39EA9EE3D98C0769CB123E7CBCFC3B430C96A219D6699B62FA6
        //SELECT HEX(aes_encrypt("123456", "xuelezhongguodev"));
        //SELECT aesDecrypt(UNHEX("6B761235F0B67CE0FE6FA8081E726799"), "xuelezhongguo258");
    }
*/

}
