package cn.buk.api.wechat.util;

import org.apache.http.HttpHost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;

/**
 * Created by yfdai on 2017/3/30.
 */
public class BaseHttpClient {

    // HTTP connection timeout
    protected static int CONNECTION_TIMEOUT = 100000;

    // HTTP scoket connection timeout
    protected static int SO_TIMEOUT = 120000;

    protected static CloseableHttpClient createHttpClient() {
        return createHttpClient(null, null);
    }

    protected static CloseableHttpClient createHttpClient(String proxyHost, String proxyPort) {
        int port = 0;
        try {
            if (proxyPort != null)
                port = Integer.parseInt(proxyPort);
        } catch (Exception ex) {
        }

        CloseableHttpClient httpClient;
        if (proxyHost == null || proxyHost.trim().length() == 0 || port <= 0) {
            httpClient = HttpClients.createDefault();
        } else {
            HttpHost proxy = new HttpHost(proxyHost, port);
            DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
            httpClient = HttpClients.custom()
                    .setRoutePlanner(routePlanner)
                    .build();
        }

        return httpClient;
    }
}
