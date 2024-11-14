package cn.buk.api.wechat.util;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.util.Timeout;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static cn.buk.common.util.HttpUtil.createHttpClient;
import static java.net.SocketOptions.SO_TIMEOUT;

/**
 * User: yfdai
 * @author yfdai
 */
public class HttpUtil1  {

    private static final Logger logger = LogManager.getLogger(HttpUtil1.class);


    /**
     * 根据url下载文件，保存到filePath中
     * @param url
     * @param filePath
     * @return
     */
    public static String downloadFile(String url, String body, String filePath) {
        CloseableHttpClient httpClient = createHttpClient();
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(Timeout.ofMilliseconds(60000)).build();

        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);

        try {
            StringEntity entity = new StringEntity(body, StandardCharsets.UTF_8);
            httpPost.setEntity(entity);

            CloseableHttpResponse response = httpClient.execute(httpPost);

//            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                outHeaders(response);
//                rs = EntityUtils.toString(response.getEntity(), "UTF-8");
//            }

            InputStream is = response.getEntity().getContent();
            if (filePath == null)
                filePath = getFilePath(response);

            File file = new File(filePath);
            file.getParentFile().mkdirs();
            FileOutputStream fileout = new FileOutputStream(file);
            /**
             * 根据实际运行效果 设置缓冲区大小
             */
            byte[] buffer=new byte[10*1024];
            int ch = 0;
            while ((ch = is.read(buffer)) != -1) {
                fileout.write(buffer,0,ch);
            }
            is.close();
            fileout.flush();
            fileout.close();

            response.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }

        return filePath;
    }

    /**
     * 根据url下载文件，保存到filepath中
     * @param url
     * @param filepath
     * @return
     */
    public static String download(String url, String filepath) {
        String filename = null;
        try {
            CloseableHttpClient httpClient = createHttpClient();
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(Timeout.ofMilliseconds(600000)).build();

            HttpGet httpget = new HttpGet(url);
            var response = httpClient.execute(httpget);

            outHeaders(response);

            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            if (filepath == null)
                filepath = getFilePath(response);
            File file = new File(filepath);
            file.getParentFile().mkdirs();
            FileOutputStream fileout = new FileOutputStream(file);
            /**
             * 根据实际运行效果 设置缓冲区大小
             */
            byte[] buffer=new byte[10 * 1024];
            int ch = 0;
            while ((ch = is.read(buffer)) != -1) {
                fileout.write(buffer,0,ch);
            }
            is.close();
            fileout.flush();
            fileout.close();

            filename = filepath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filename;
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

    public static void outHeaders(HttpResponse response) {
        Header[] headers = response.getHeaders();
        for (int i = 0; i < headers.length; i++) {
            logger.debug(headers[i]);
        }
    }

    /**
     * 获取随机文件名
     * @return
     */
    public static String getRandomFileName() {
        return String.valueOf(System.currentTimeMillis());
    }

    /**
     * 获取response header中Content-Disposition中的filename值
     * @param response
     * @return
     */
    public static String getFileName(HttpResponse response) {
        var contentHeader = response.getFirstHeader("Content-Disposition");
        String filename = null;
        if (contentHeader != null) {
            //TODO ss
//            HeaderElement[] values = contentHeader.getElements();
//            if (values.length == 1) {
//                NameValuePair param = values[0].getParameterByName("filename");
//                if (param != null) {
//                    try {
//                        //filename = new String(param.getValue().toString().getBytes(), "utf-8");
//                        //filename=URLDecoder.decode(param.getValue(),"utf-8");
//                        filename = param.getValue();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
        }
        return filename;
    }

    /**
     * 获取response要下载的文件的默认路径
     * @param response
     * @return
     */
    public static String getFilePath(HttpResponse response) {
        //TODO: 临时路径或指定路径？
//        String filePath = "~/";//root + splash;
        String filePath = System.getProperty("java.io.tmpdir");
        String filename = getFileName(response);

        if (filename != null) {
            filePath += filename;
        } else {
            filePath += getRandomFileName();
        }

        logger.debug(filePath);

        return filePath;
    }

}
