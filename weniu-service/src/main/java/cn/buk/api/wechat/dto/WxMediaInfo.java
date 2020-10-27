package cn.buk.api.wechat.dto;

import java.util.Date;

/**
 * Created by yfdai on 2017/2/24.
 */
public class WxMediaInfo {

    private String media_id;

    private String name;

    private String url;

    private WxNewsInfo content;

    private Date update_time;

    public String getMedia_id() {
        return media_id;
    }

    public void setMedia_id(String media_id) {
        this.media_id = media_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public WxNewsInfo getContent() {
        return content;
    }

    public void setContent(WxNewsInfo content) {
        this.content = content;
    }
}
