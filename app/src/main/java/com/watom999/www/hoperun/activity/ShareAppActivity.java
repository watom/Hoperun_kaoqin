package com.watom999.www.hoperun.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.zxing.WriterException;
import com.mob.MobSDK;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.watom999.www.hoperun.R;
import com.watom999.www.hoperun.utils.AlipayUtil;
import com.watom999.www.hoperun.utils.MyToast;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by Administrator on 2018/6/21 0021.
 */

public class ShareAppActivity extends AppCompatActivity {
    String urlStr="https://fir.im/xlz5";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barcode_layout);

        ImageView barcode = findViewById(R.id.iv_barcode);
        Button daShangBtn = findViewById(R.id.btn_daShangBtn);
        generateBarCode(barcode,urlStr);
        MobSDK.init(this);
        showShare(urlStr);
        daShangBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AlipayUtil.hasInstalledAlipayClient(ShareAppActivity.this)){
                    AlipayUtil.startAlipayClient(ShareAppActivity.this,"HTTPS://QR.ALIPAY.COM/FKX07636ZRCCQH2VENC2E7");
                }else{
                    MyToast.showToast(ShareAppActivity.this,"未检测到支付宝，无法给伙计打赏，但是还是要谢谢您的支持");
                }
            }
        });
    }
    /**
     * 二维码生成
     */
    private void generateBarCode(ImageView view,String str) {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;
        Bitmap qrCodeBitmap = CodeUtils.createImage(str, width/2, width/2, null);
        view.setImageBitmap(qrCodeBitmap);
    }

    private void showShare(String url) {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle("润和考勤");
        // titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText("给你分享的是润和考勤App，点击链接"+url+"下载。");
        // url在微信、微博，Facebook等平台中使用
        oks.setUrl(url);
        // 启动分享GUI
        oks.show(this);
    }
}
