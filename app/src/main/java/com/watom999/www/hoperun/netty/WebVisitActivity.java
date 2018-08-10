package com.watom999.www.hoperun.netty;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.watom999.www.hoperun.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

/**
 * Created by Administrator on 2018/7/30 0030.
 */

public class WebVisitActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webvisit_layout);

        new Thread()
        {
            public void run()
            {
                try
                {
                    getConnection();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void getConnection() throws IOException, KeyManagementException, NoSuchAlgorithmException,
            UnrecoverableKeyException, CertificateException, KeyStoreException {

//        URL url = new URL("https://www.baidu.com/");
        URL url = new URL("https://10.50.30.244:443/SxgwPhoneServer/user/getCode.do");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(5 * 1000);
        connection.setReadTimeout(5 * 1000);
        connection.setRequestMethod("GET");

        Log.e("wanghaitao", "url==" + url);
        Log.e("wanghaitao", " 是否是https请求==" + (connection instanceof HttpsURLConnection));
        if (connection instanceof HttpsURLConnection) {

            //得到sslContext对象，有两种情况：1.需要安全证书，2.不需要安全证书
            SSLContext sslContext = HttpsUtil.getSSLContextWithCer();
            //SSLContext sslContext = HttpsUtil.getSSLContextWithoutCer();
            if (sslContext != null) {
                SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
                ((HttpsURLConnection) connection).setDefaultSSLSocketFactory(sslSocketFactory);
                //((HttpsURLConnection) connection).setHostnameVerifier(HttpsUtil.hostnameVerifier);
            }
        }
        int responseCode = connection.getResponseCode();
        Log.e("wanghaitao", "responseCode==" + responseCode);
        if (responseCode == 200) {
            InputStream is = connection.getInputStream();
            Log.e("wanghaitao", "is==" + is);
            is.close();
        }
        connection.disconnect();
    }
}
