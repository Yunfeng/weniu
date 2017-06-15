package cn.buk.api.wechat.dto;

/**
 * 1.永久素材的总数，也会计算公众平台官网素材管理中的素材
 2.图片和图文消息素材（包括单图文和多图文）的总数上限为5000，其他素材的总数上限为1000
 3.调用该接口需https协议
 * Created by yfdai on 2017/6/8.
 */
public class WxMaterialSummary {

    private int voice_count;

    private int video_count;

    private int image_count;

    private int news_count;

    public int getVoice_count() {
        return voice_count;
    }

    public void setVoice_count(int voice_count) {
        this.voice_count = voice_count;
    }

    public int getVideo_count() {
        return video_count;
    }

    public void setVideo_count(int video_count) {
        this.video_count = video_count;
    }

    public int getImage_count() {
        return image_count;
    }

    public void setImage_count(int image_count) {
        this.image_count = image_count;
    }

    public int getNews_count() {
        return news_count;
    }

    public void setNews_count(int news_count) {
        this.news_count = news_count;
    }
}
