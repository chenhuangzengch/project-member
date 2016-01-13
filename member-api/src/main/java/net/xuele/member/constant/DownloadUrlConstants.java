package net.xuele.member.constant;

import net.xuele.member.util.PropertiesUtil;

/**
 * Created by wuxh on 2015/8/12 0012.
 */
public class DownloadUrlConstants {
    /**
     * android app下载链接
     */
    public static final String ANDROID_APP_URL = PropertiesUtil.getProperty("downloadUrlConstants.androidAppUrl");
    /**
     * ios app 下载链接
     */
    public static final String IOS_APP_URL = PropertiesUtil.getProperty("downloadUrlConstants.iosAppUrl");
}
