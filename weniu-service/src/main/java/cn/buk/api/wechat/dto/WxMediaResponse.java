package cn.buk.api.wechat.dto;

/**
 * Created by yfdai on 2017/6/7.
 */
public class WxMediaResponse extends BaseResponse {

    private String media_id;

    private String url;

    public String getMedia_id() {
        return media_id;
    }

    public void setMedia_id(String media_id) {
        this.media_id = media_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
