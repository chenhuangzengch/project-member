package net.xuele.member.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

/**
 * @author tong 1 Nov 2013 09:53:52
 */
public class CaptchaUtil {

    private Random random = new Random();

    /**
     * 图片宽,根据验证码字符的长度确定
     */
    private int width;
    private int height = 26;// 图片高
    private int lineSize = 2;// 干扰线数量
    private char[] codes;

    private CaptchaUtil(char[] codes) {
        this.codes = codes;
        this.width = 15 * codes.length;

    }

    /**
     * 获得颜色
     */
    private Color getRandColor(int fc, int bc) {
        int r = fc + random.nextInt(bc - fc - 16);
        int g = fc + random.nextInt(bc - fc - 14);
        int b = fc + random.nextInt(bc - fc - 18);
        return new Color(r, g, b);
    }

    /**
     * 生成随机图片
     */
    private BufferedImage getImage() {
        // BufferedImage类是具有缓冲区的Image类,Image类是用于描述图像信息的类
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();// 产生Image对象的Graphics对象,改对象可以在图像上进行各种绘制操作
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 18));
        g.setColor(getRandColor(110, 133));
        // 绘制干扰线
        for (int i = 0; i < lineSize; i++) {
            drawLine(g);
        }
        // 绘制随机字符
        for (int i = 0; i < codes.length; i++) {
            drawString(g, codes[i], i);
        }
        g.dispose();
        return image;
    }

    /**
     * 绘制字符串
     */
    private void drawString(Graphics g, char c, int i) {
        g.setColor(new Color(random.nextInt(101), random.nextInt(111), random.nextInt(121)));
        // 原点平移到当前坐标系中的点
        g.translate(random.nextInt(3), random.nextInt(3));
        g.drawString(Character.toString(c), 13 * i + 3, 16);
    }

    /**
     * 绘制干扰线
     */
    private void drawLine(Graphics g) {
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        int xl = random.nextInt(13);
        int yl = random.nextInt(15);
        g.drawLine(x, y, x + xl, y + yl);
    }

    /**
     * 获取验证码
     *
     * @param code 验证码
     * @param out  图片流出的方向
     * @throws IOException 流异常
     */
    public static void writeImage(String code, OutputStream out) throws IOException {
        CaptchaUtil image = new CaptchaUtil(code.toCharArray());
        ImageIO.write(image.getImage(), "PNG", out);

        /*
        //tomcat 下没有temp文件夹时会出现图片验证码无法显示（解决方案一，手动创建temp文件夹），（这里提供另外一种解决方案）
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        encoder.encode(image.getImage());*/
    }

/*    public static void main(String[] args) throws IOException {
        writeImage("1234", new FileOutputStream("d:/a.png"));
    }*/

}