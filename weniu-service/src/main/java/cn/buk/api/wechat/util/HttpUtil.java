package cn.buk.api.wechat.util;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * User: yfdai
 */
public class HttpUtil extends BaseHttpClient {

    private static Logger logger = Logger.getLogger(HttpUtil.class);

    public static String getUrl(String url, List<NameValuePair> params) {
        //TODO 判断params有的话，还要判断url的结果字符是否为"?"
        String uri  = url;
        if (params != null) uri += URLEncodedUtils.format(params, "UTF-8");

        logger.debug(uri.toString());

        CloseableHttpClient httpClient = createHttpClient();
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SO_TIMEOUT).setConnectTimeout(CONNECTION_TIMEOUT).build();

        HttpGet httpGet = new HttpGet(uri);
        httpGet.setConfig(requestConfig);
        String rs="";

        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                rs = EntityUtils.toString(response.getEntity(), "UTF-8");

                logger.debug("response: " + rs);
            }

            response.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return rs;
    }

    public static String postUrl(String url, String body) {
        CloseableHttpClient httpClient = createHttpClient();
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SO_TIMEOUT).setConnectTimeout(CONNECTION_TIMEOUT).build();

        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);

        String rs = "";

        try {
            StringEntity entity = new StringEntity(body, "UTF-8");
            httpPost.setEntity(entity);

            CloseableHttpResponse response = httpClient.execute(httpPost);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                rs = EntityUtils.toString(response.getEntity(), "UTF-8");
            }

            response.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return rs;
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        logger.debug(ip);

        return ip;
    }

    public static void sendResponse(HttpServletResponse response, String content) {
        response.setContentType("text/plain;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        PrintWriter pw;
        try {
            pw = response.getWriter();
            pw.print(content);
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
