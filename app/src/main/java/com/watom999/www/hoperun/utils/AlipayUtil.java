package com.watom999.www.hoperun.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.net.URISyntaxException;

/**
 * Created by Administrator on 2018/6/22 0022.
 */

public class AlipayUtil {
    private static final String ALIPAY_PACKAGE_NAME = "com.eg.android.AlipayGphone";
    // 支付宝二维码通用 Intent Scheme Url 格式
     private static final String INTENT_URL_FORMAT = "alipayqr://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode={[urlCode]}%3F_s%3Dweb-other&_t=" + System.currentTimeMillis();
    /**
     * 打开转账窗口
     * 支付宝二维码方法，需要使用 https://fama.alipay.com/qrcode/index.htm 网站生成的二维码
     * @param urlCode 手动解析二维码获得地址中的参数，例如HTTPS://QR.ALIPAY.COM/FKX07636ZRCCQH2VENC2E7
     * @return 是否成功调用
     */
    public static boolean startAlipayClient(Activity activity, String urlCode) {
        return startIntentUrl(activity, INTENT_URL_FORMAT.replace("{[urlCode]}", urlCode));
    }
    /**
     * 打开 Intent Scheme Url
     * @return 是否成功调用
     */
    public static boolean startIntentUrl(Activity activity, String intentFullUrl) {
        try {
            Intent intent = Intent.parseUri(
                    intentFullUrl,
                    Intent.URI_INTENT_SCHEME
            );
            activity.startActivity(intent);
            return true;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return false;
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 判断支付宝客户端是否已安装，建议调用转账前检查
     * @return 支付宝客户端是否已安装
     */
    public static boolean hasInstalledAlipayClient(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(ALIPAY_PACKAGE_NAME, 0);
            return info != null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
