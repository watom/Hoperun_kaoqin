package com.watom999.www.hoperun.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.webkit.WebView;

import com.watom999.www.hoperun.data.MySQLiteHelper;
import com.watom999.www.hoperun.entity.UserInfoEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/18 0018.
 */

public class DisposeData {
    UserInfoEntity userInfoEntity;
    WebView webView;
    Context context;
    MySQLiteHelper mySQLiteHelper;
    //线程并发时候，当我们的变量被被修改之后，其他的并发线程及时收到通知，其他的线程就可以访问数据
    //volatile--会屏蔽虚拟机优化过程
    private volatile static DisposeData instance;

    //iOS开发经常是这么搞的（基本上写单例都加索）
    private DisposeData() {
    }

    public static DisposeData getInstace() {
        if(instance == null){
            synchronized (DisposeData.class) {
                if(instance == null)
                    instance = new DisposeData();
            }
        }
        return instance;
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //保存数据
                    if (isURLChange(webView.getUrl(), String.valueOf(msg.arg1))) {
                        long insert = mySQLiteHelper.insert(userInfoEntity);
                        if (insert == -1) {
                            MyToast.showToast(context, "保存数据失败");
                        } else {
                            MyToast.showToast(context, "保存数据成功");
                        }
                    } else {
                        MyToast.showToast(context, "密码或账号不对，请重新登录");
                    }

                    break;
                case 2:
                    //直接登录
                    if (isURLChange(webView.getUrl(), String.valueOf(msg.arg1))) {
//                        fab.hide();
                    }
                    break;
            }
        }
    };

    public DisposeData(Context context) {
        this.context = context;
        mySQLiteHelper = new MySQLiteHelper(context);
    }

    /**
     * 从数据库中获取登录的账号密码
     */
    public Map<String, Object> getUserLoginInfo(String pageid) {
        HashMap<String, Object> userLoginInfo = mySQLiteHelper.getUserLoginInfo(pageid);
        return userLoginInfo;
    }

    /**
     * 将对话框中的输入数据保存到数据库中,保存时判断是否登录成功
     */
    public void saveLoginInfo(final String pageid, final String account, final String password) {
        userInfoEntity = new UserInfoEntity();
        userInfoEntity.setPage_id(pageid);
        userInfoEntity.setLogin_account(account);
        userInfoEntity.setLogin_password(password);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                msg.what = 1;
                msg.arg1 = Integer.parseInt(pageid);
                msg.obj = userInfoEntity;
                handler.sendMessage(msg);
            }
        }).start();
    }
    
    /**
     * 根据URL判断是否登录成功
     */
    public Boolean isURLChange(String url, String pageid) {
        Boolean flag = false;
        switch (pageid) {
            case "0":
                //是考勤主页面登录
                flag = isEqualStr(url, "http://elec.hoperun.com:8106/eoffice_pc/attendance/initPage");
                break;
            case "1":
                //是EHR系统的登录
                flag = isEqualStr(url, "http://ehr.hoperun.com:8018/HRIS/Portal/Home.do#nv_1");
                break;
            case "2":
                //是邮箱页面登录
                flag = isEqualStr(url, "http://mail.hoperun.com/wm2e/mail/login/show/loginShowAction_loginShow.do");
                break;
            case "3":
                //是云之家网页页面登录
                flag = isEqualStr(url, "https://www.yunzhijia.com/im/xiaoxi/");
                break;
        }
        return flag;
    }

    /**
     * 验证字符串是否相等
     */
    public Boolean isEqualStr(String url, String sample) {
        String subURL, subSample;
        StringBuilder stringBuilder1 = new StringBuilder(url);
        StringBuilder stringBuilder2 = new StringBuilder(sample);
        if (url.length() >= sample.length()) {
            subURL = stringBuilder1.substring(0, sample.length());
            subSample = stringBuilder2.substring(0, sample.length());
        } else {
            subURL = stringBuilder1.substring(0, url.length());
            subSample = stringBuilder2.substring(0, url.length());
        }
        boolean equals = subURL.equals(subSample);
        return equals;
    }
}
