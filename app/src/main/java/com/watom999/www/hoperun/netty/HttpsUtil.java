package com.watom999.www.hoperun.netty;

import android.util.Log;

import com.watom999.www.hoperun.activity.MyApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Administrator on 2018/7/30 0030.
 */

public class HttpsUtil
{
    /**
     * post请求方法
     */
    private static final String METHOD_POST = "POST";

    /**
     * utf-8编码格式
     */
    private static final String DEFAULT_CHARSET = "utf-8";

    /**
     * doPost
     *
     * @param url
     *            请求地址
     * @param params
     *            请求参数
     * @param charset
     *            编码
     * @param ctype
     *            类型
     * @param connectTimeout
     *            连接超时时间
     * @param readTimeout
     *            读取超时时间
     * @return 结果
     * @throws Exception
     *             异常
     */
    public static String doPost(String url, String params, String charset, String ctype,
                                int connectTimeout, int readTimeout)
            throws Exception
    {
        charset = (charset == null || "".equals(charset)) ? DEFAULT_CHARSET : charset;
        byte[] content = {};
        if (params != null)
        {
            content = params.getBytes(charset);
        }
        return doPost(url, ctype, content, connectTimeout, readTimeout);
    }

    /**
     * doPost
     *
     * @param url
     *            请求地址
     * @param ctype
     *            类型
     * @param content
     *            内容
     * @param connectTimeout
     *            连接超时时间
     * @param readTimeout
     *            读取超时时间
     * @return 结果
     * @throws Exception
     *             异常
     */
    public static String doPost(String url, String ctype, byte[] content, int connectTimeout,
                                int readTimeout)
            throws Exception
    {
        HttpsURLConnection conn = null;
        OutputStream out = null;
        String rsp = null;
        try
        {
            try
            {
                SSLContext ctx = SSLContext.getInstance("TLS");
                ctx.init(new KeyManager[0], new TrustManager[] {new DefaultTrustManager()},
                        new SecureRandom());
                SSLContext.setDefault(ctx);

                conn = getConnection(new URL(url), METHOD_POST, ctype);
                conn.setHostnameVerifier(new HostnameVerifier()
                {
                    @Override
                    public boolean verify(String hostname, SSLSession session)
                    {
                        return true;
                    }
                });
                conn.setConnectTimeout(connectTimeout);
                conn.setReadTimeout(readTimeout);
            }
            catch (Exception e)
            {
                // log.error("GET_CONNECTOIN_ERROR, URL = " + url, e);
                throw e;
            }
            try
            {
                out = conn.getOutputStream();
                out.write(content);
                rsp = getResponseAsString(conn);
            }
            catch (IOException e)
            {
                // log.error("REQUEST_RESPONSE_ERROR, URL = " + url, e);
                throw e;
            }

        }
        finally
        {
            if (out != null)
            {
                out.close();
            }
            if (conn != null)
            {
                conn.disconnect();
            }
        }

        return rsp;
    }

    private static class DefaultTrustManager implements X509TrustManager
    {

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException
        {}

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException
        {}

        @Override
        public X509Certificate[] getAcceptedIssuers()
        {
            return null;
        }

    }

    /**
     * 获取连接
     *
     * @param url
     *            请求地址
     * @param method
     *            请求方法
     * @param ctype
     *            类型
     * @return HttpsURLConnection
     * @throws IOException
     *             异常
     */
    private static HttpsURLConnection getConnection(URL url, String method, String ctype)
            throws IOException
    {
        HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
        conn.setRequestMethod(method);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestProperty("Accept", "text/xml,text/javascript,text/html");
        conn.setRequestProperty("User-Agent", "stargate");
        conn.setRequestProperty("Content-Type", ctype);
        return conn;
    }

    /**
     * getResponseAsString
     *
     * @param conn
     *            conn连接
     * @return String
     * @throws IOException
     *             IOException
     */
    protected static String getResponseAsString(HttpURLConnection conn)
            throws IOException
    {
        String charset = getResponseCharset(conn.getContentType());
        InputStream es = conn.getErrorStream();
        if (es == null)
        {
            return getStreamAsString(conn.getInputStream(), charset);
        }
        else
        {
            String msg = getStreamAsString(es, charset);
            if (StringUtils.isEmpty(msg))
            {
                throw new IOException(conn.getResponseCode() + ":" + conn.getResponseMessage());
            }
            else
            {
                throw new IOException(msg);
            }
        }
    }

    /**
     * getStreamAsString
     *
     * @param stream
     *            stream
     * @param charset
     *            charset
     * @return String
     * @throws IOException
     *             IOException
     */
    private static String getStreamAsString(InputStream stream, String charset)
            throws IOException
    {
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, charset));
            StringWriter writer = new StringWriter();

            char[] chars = new char[256];
            int count = 0;
            while ((count = reader.read(chars)) > 0)
            {
                writer.write(chars, 0, count);
            }

            return writer.toString();
        }
        finally
        {
            if (stream != null)
            {
                stream.close();
            }
        }
    }

    /**
     * getResponseCharset
     *
     * @param ctype
     *            ctype
     * @return String
     */
    private static String getResponseCharset(String ctype)
    {
        String charset = DEFAULT_CHARSET;

        if (!StringUtils.isEmpty(ctype))
        {
            String[] params = ctype.split(";");
            for (String param : params)
            {
                param = param.trim();
                if (param.startsWith("charset"))
                {
                    String[] pair = param.split("=", 2);
                    if (pair.length == 2)
                    {
                        if (!StringUtils.isEmpty(pair[1]))
                        {
                            charset = pair[1].trim();
                        }
                    }
                    break;
                }
            }
        }
        return charset;
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier
    {
        @Override
        public boolean verify(String hostname, SSLSession session)
        {
            return true;
        }
    }

    /**
     * doGet
     *
     * @param url
     *            请求地址
     * @param keyValueParams
     *            参数
     * @param cypt
     *            cypt
     * @return String
     * @throws Exception
     *             Exception
     */
    public static String doGet(String url, Map<String, String> keyValueParams, String cypt)
            throws Exception
    {
        String result = "";
        BufferedReader in = null;
        try
        {

            String urlStr = url + "?" + getParamStr(keyValueParams);
            // System.out.println("GET请求的URL为："+urlStr);
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[] {new DefaultTrustManager()},
                    new java.security.SecureRandom());
            URL realUrl = new URL(urlStr);
            // 打开和URL之间的连接
            HttpsURLConnection connection = (HttpsURLConnection)realUrl.openConnection();
            // 设置https相关属性
            connection.setSSLSocketFactory(sc.getSocketFactory());
            connection.setHostnameVerifier(new TrustAnyHostnameVerifier());
            connection.setDoOutput(true);

            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("Content-type", cypt);
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();

            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null)
            {
                result += line;
            }
            // System.out.println("获取的结果为："+result);
        }
        catch (Exception e)
        {
            // System.out.println("发送GET请求出现异常！" + e);
            // e.printStackTrace();
            throw e;
        }
        // 使用finally块来关闭输入流
        finally
        {
            try
            {
                if (in != null)
                {
                    in.close();
                }
            }
            catch (Exception e2)
            {
                // e2.printStackTrace();
                throw e2;
            }
        }
        return result;
    }

    /**
     * 转化字符串参数
     *
     * @param params
     *            参数
     * @return String
     */
    public static String getParamStr(Map<String, String> params)
    {
        String paramStr = StringUtils.EMPTY;
        if (null == params || 0 == params.size())
        {
            return paramStr;
        }
        // 获取参数列表组成参数字符串
        for (String key : params.keySet())
        {
            paramStr += key + "=" + params.get(key) + "&";
        }
        // 去除最后一个"&"
        return paramStr.substring(0, paramStr.length() - 1);
    }

    /**
     * 解析出url参数中的键值对 如 "index.jsp?Action=del&id=123"，解析出Action:del,id:123存入map中
     *
     * @param url
     *            url地址
     * @return url请求参数部分
     * @author lzf
     */
    public static Map<String, String> getUrlParam(String url)
    {
        // 初始化返回
        Map<String, String> params = new HashMap<String, String>();
        if (StringUtils.isBlank(url))
        {
            return params;
        }
        //
        String strUrlParam = truncateUrl(url);
        if (StringUtils.isBlank(strUrlParam))
        {
            return params;
        }
        String[] arrSplit = strUrlParam.split("[&]");
        for (String strSplit : arrSplit)
        {
            String[] arrSplitEqual = strSplit.split("[=]");
            // 解析出键值
            if (arrSplitEqual.length > 1)
            {
                // 正确解析
                params.put(arrSplitEqual[0], arrSplitEqual[1]);
            }
            else
            {
                if (!"".equals(arrSplitEqual[0]))
                {
                    // 只有参数没有值，也加入
                    params.put(arrSplitEqual[0], "");
                }
            }
        }
        return params;
    }

    /**
     * 去掉url中的路径，留下请求参数部分
     *
     * @param url
     *            url地址
     * @return url
     * @author lzf
     */
    private static String truncateUrl(String url)
    {
        String strAllParam = null;
        String[] arrSplit = null;
        url = url.trim();
        arrSplit = url.split("[?]");
        if (url.length() > 1)
        {
            if (arrSplit.length > 1)
            {
                for (int i = 1; i < arrSplit.length; i++ )
                {
                    strAllParam = arrSplit[i];
                }
            }
        }
        return strAllParam;
    }

    //========================================================================================
    //========================================================================================
    //========================另一个人的工具============================
    //========================================================================================
    //========================================================================================
    /**
//     * @param inStream
     * @return SSLContext
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws CertificateException
     * @throws KeyStoreException
     * @throws UnrecoverableKeyException
     * @throws KeyManagementException
     */
    public static SSLContext getSSLContextWithCer() throws NoSuchAlgorithmException, IOException, CertificateException,
            KeyStoreException, UnrecoverableKeyException, KeyManagementException {
        // 实例化SSLContext
        SSLContext sslContext = null;

        // 从assets中加载证书，在HTTPS通讯中最常用的是cer/crt和pem

        InputStream inStream = MyApplication.getApplication().getAssets().open("mykey.cer");

		/**
         * X.509 标准规定了证书可以包含什么信息，并说明了记录信息的方法 常见的X.509证书格式包括：
         * cer/crt是用于存放证书，它是2进制形式存放的，不含私钥。
         * pem跟crt/cer的区别是它以Ascii来表示，可以用于存放证书或私钥。
         */
        // 证书工厂
        CertificateFactory cerFactory = CertificateFactory.getInstance("X.509");
        Certificate cer = cerFactory.generateCertificate(inStream);

        // 密钥库
        //Pkcs12也是证书格式 PKCS#12是“个人信息交换语法”。它可以用来将x.509的证书和证书对应的私钥打包，进行交换。
//        KeyStore keyStory = KeyStore.getInstance("PKCS12");
        KeyStore keyStory = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStory.load(inStream, null);
        // 加载证书到密钥库中
        keyStory.setCertificateEntry("cer", cer);

        // 信任管理器
        TrustManagerFactory tmFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmFactory.init(keyStory);

        sslContext = SSLContext.getInstance("SSL");

//        // 密钥管理器
//        KeyManagerFactory kMFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
//        kMFactory.init(keyStory, null);


        //初始化sslContext
//        sslContext.init(kMFactory.getKeyManagers(), tmFactory.getTrustManagers(), new SecureRandom());
        sslContext.init(null, tmFactory.getTrustManagers(), null);
//        inStream.close();
        return sslContext;
    }

    /**
     * @return SSLContext
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */

    public static SSLContext getSSLContextWithoutCer() throws NoSuchAlgorithmException, KeyManagementException {
        // 实例化SSLContext
        // 这里参数可以用TSL 也可以用SSL
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, new TrustManager[] { trustManagers }, new SecureRandom());
        return sslContext;
    }

    private static TrustManager trustManagers = new X509TrustManager() {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            Log.e("wanghaitao", "checkClientTrusted: "+authType);
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            Log.e("wanghaitao", "checkServerTrusted: "+authType);
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    };


    //验证主机名
    public static HostnameVerifier hostnameVerifier = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            // TODO Auto-generated method stub
            return true;
        }
    };

}