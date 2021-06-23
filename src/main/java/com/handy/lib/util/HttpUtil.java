package com.handy.lib.util;

import org.apache.commons.lang.StringUtils;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Set;

/**
 * http请求工具类
 *
 * @author handy
 */
public class HttpUtil {
    private final static String REQUEST_TYPE_FORM = "application/x-www-form-urlencoded;charset=utf-8";
    private final static String REQUEST_TYPE_JSON = "application/json; charset=utf-8";
    private final static String CHARSET = "utf-8";
    private final static Integer CONNECT_TIMEOUT = 5000;
    private final static Integer READ_TIMEOUT = 5000;
    private final static String HTTPS = "https";

    /**
     * POST 以application/json; charset=utf-8方式传输
     *
     * @param url         路径
     * @param jsonContent json参数
     * @return 响应
     */
    public static String post(String url, String jsonContent) throws NoSuchAlgorithmException, IOException, KeyManagementException {
        return doRequest("POST", url, jsonContent, REQUEST_TYPE_JSON);
    }

    /**
     * POST以application/x-www-form-urlencoded;charset=utf-8方式传输
     *
     * @param url 路径
     * @return 响应
     */
    public static String post(String url) throws NoSuchAlgorithmException, IOException, KeyManagementException {
        return doRequest("POST", url, "", REQUEST_TYPE_FORM);
    }

    /**
     * POST 以application/x-www-form-urlencoded;charset=utf-8方式传输
     *
     * @param url
     * @param params
     * @return
     */
    public static String post(String url, Map<String, String> params) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        return doRequest("POST", url, buildQuery(params), REQUEST_TYPE_FORM);
    }

    /**
     * get 以application/x-www-form-urlencoded;charset=utf-8方式传输
     *
     * @param url 路径
     * @return
     */
    public static String get(String url) throws NoSuchAlgorithmException, IOException, KeyManagementException {
        return doRequest("GET", url, "", REQUEST_TYPE_FORM);
    }

    /**
     * get application/x-www-form-urlencoded;charset=utf-8方式传输
     *
     * @param url    路径
     * @param params 参数
     * @return
     */
    public static String get(String url, Map<String, String> params) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        return doRequest("GET", url + buildQuery(params), "", REQUEST_TYPE_FORM);
    }

    /**
     * 请求
     *
     * @param method         方式
     * @param url            url
     * @param requestContent 请求内容
     * @param requestType    请求类型
     * @return 响应
     */
    private static String doRequest(String method, String url, String requestContent, String requestType) throws IOException, KeyManagementException, NoSuchAlgorithmException {
        HttpURLConnection conn = null;
        OutputStream out = null;
        String rsp = null;
        try {
            conn = getConnection(new URL(url), method, requestType);
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);
            if (StringUtils.isNotBlank(requestContent)) {
                out = conn.getOutputStream();
                out.write(requestContent.getBytes(CHARSET));
            }
            rsp = getResponseAsString(conn);
        } finally {
            if (out != null) {
                out.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rsp;
    }

    /**
     * 获取请求
     *
     * @param url         url
     * @param method      方法
     * @param requestType 请求类型
     * @return
     */
    private static HttpURLConnection getConnection(URL url, String method, String requestType) throws NoSuchAlgorithmException, KeyManagementException, IOException {
        HttpURLConnection conn;
        if (HTTPS.equals(url.getProtocol())) {
            SSLContext ctx;
            ctx = SSLContext.getInstance("TLS");
            ctx.init(new KeyManager[0], new TrustManager[]{new DefaultTrustManager()}, new SecureRandom());
            HttpsURLConnection connHttps = (HttpsURLConnection) url.openConnection();
            connHttps.setSSLSocketFactory(ctx.getSocketFactory());
            connHttps.setHostnameVerifier((hostname, session) -> true);
            conn = connHttps;
        } else {
            conn = (HttpURLConnection) url.openConnection();
        }
        conn.setRequestMethod(method);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestProperty("Accept", "text/xml,text/javascript,text/html,application/json");
        conn.setRequestProperty("Content-Type", requestType);
        return conn;
    }

    private static String getResponseAsString(HttpURLConnection conn) throws IOException {
        InputStream es = conn.getErrorStream();
        if (es == null) {
            return getStreamAsString(conn.getInputStream());
        } else {
            return getStreamAsString(es);
        }
    }

    private static String getStreamAsString(InputStream stream) throws IOException {
        try {
            Reader reader = new InputStreamReader(stream, HttpUtil.CHARSET);
            StringBuilder response = new StringBuilder();
            final char[] buff = new char[1024];
            int read = 0;
            while ((read = reader.read(buff)) > 0) {
                response.append(buff, 0, read);
            }
            return response.toString();
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    private static String buildQuery(Map<String, String> params) throws UnsupportedEncodingException {
        if (params == null || params.isEmpty()) {
            return "";
        }
        StringBuilder query = new StringBuilder();
        query.append("?");
        Set<Map.Entry<String, String>> entries = params.entrySet();
        boolean hasParam = false;
        for (Map.Entry<String, String> entry : entries) {
            String name = entry.getKey();
            String value = entry.getValue();
            if (hasParam) {
                query.append("&");
            } else {
                hasParam = true;
            }
            query.append(name).append("=").append(URLEncoder.encode(value, CHARSET));
        }
        return query.toString();
    }

    private static class DefaultTrustManager implements X509TrustManager {
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }
    }

    /**
     * 从网络Url中下载文件
     *
     * @param urlStr   路径
     * @param saveDir  保存路径
     * @param fileName 文件名称
     */
    public static void downloadFile(String urlStr, File saveDir, String fileName) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(CONNECT_TIMEOUT);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取自己数组
        byte[] getData = readInputStream(inputStream);
        //文件保存位置
        if (!saveDir.exists()) {
            saveDir.mkdir();
        }
        File file = new File(saveDir + File.separator + fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        fos.close();
        inputStream.close();
    }

    /**
     * 从输入流中获取字节数组
     *
     * @param inputStream 输入流
     * @return
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream bos = null;
        try {
            byte[] buffer = new byte[1024];

            int len = 0;
            bos = new ByteArrayOutputStream();
            while ((len = inputStream.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }

}