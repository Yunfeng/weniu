package cn.buk.api.wechat.dto;

import java.util.List;

/**
 * 图文永久素材上传请求
 * Created by yfdai on 2017/5/1.
 */
public class WxNewsRequest {

    private List<WxNews> articles;


    public List<WxNews> getArticles() {
        return articles;
    }

    public void setArticles(List<WxNews> articles) {
        this.articles = articles;
    }
}
