package cn.buk.api.wechat.work.message;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author yfdai
 */
public class MediaInfo {

  @JSONField(name = "media_id")
  private String mediaId;

  /**
   * 视频消息的描述，不超过512个字节，超过会自动截断
   */
  private String description;

  /**
   * 视频消息的标题，不超过128个字节，超过会自动截断
   */
  private String title;

  public String getMediaId() {
    return mediaId;
  }

  public void setMediaId(String mediaId) {
    this.mediaId = mediaId;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}
