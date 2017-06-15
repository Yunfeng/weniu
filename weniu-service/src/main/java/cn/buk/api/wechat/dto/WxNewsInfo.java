package cn.buk.api.wechat.dto;

import java.util.Date;
import java.util.List;

/**
 * Created by yfdai on 2017/6/8.
 */
public class WxNewsInfo {

    private List<WxNewsItem> news_item;

    private Date create_time;

    private Date update_time;

    public List<WxNewsItem> getNews_item() {
        return news_item;
    }

    public void setNews_item(List<WxNewsItem> news_item) {
        this.news_item = news_item;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }
}
