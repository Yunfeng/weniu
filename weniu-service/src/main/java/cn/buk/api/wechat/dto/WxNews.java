package cn.buk.api.wechat.dto;

/**
 * 微信图文素材
 * Created by yfdai on 2017/6/7.
 */
public class WxNews {

     private String title; //标题

     private String thumb_media_id; //图文消息的封面图片素材id（必须是永久mediaID）

     private String author;//作者

     private String digest; //图文消息的摘要，仅有单图文消息才有摘要，多图文此处为空

     private int show_cover_pic; //是否显示封面，0为false，即不显示，1为true，即显示

     private String content; //  图文消息的具体内容，支持HTML标签，必须少于2万字符，小于1M，且此处会去除JS,涉及图片url必须来源"上传图文消息内的图片获取URL"接口获取。外部图片url将被过滤。

     private String content_source_url;       //"content_source_url": CONTENT_SOURCE_URL

     public String getTitle() {
          return title;
     }

     public void setTitle(String title) {
          this.title = title;
     }

     public String getThumb_media_id() {
          return thumb_media_id;
     }

     public void setThumb_media_id(String thumb_media_id) {
          this.thumb_media_id = thumb_media_id;
     }

     public String getAuthor() {
          return author;
     }

     public void setAuthor(String author) {
          this.author = author;
     }

     public String getDigest() {
          return digest;
     }

     public void setDigest(String digest) {
          this.digest = digest;
     }

     public int getShow_cover_pic() {
          return show_cover_pic;
     }

     public void setShow_cover_pic(int show_cover_pic) {
          this.show_cover_pic = show_cover_pic;
     }

     public String getContent() {
          return content;
     }

     public void setContent(String content) {
          this.content = content;
     }

     public String getContent_source_url() {
          return content_source_url;
     }

     public void setContent_source_url(String content_source_url) {
          this.content_source_url = content_source_url;
     }
}
