package cn.buk.api.wechat.work.message;

import com.alibaba.fastjson.annotation.JSONField;

public class MediaInfo {

  @JSONField(name = "media_id")
  private String mediaId;

  public String getMediaId() {
    return mediaId;
  }

  public void setMediaId(String mediaId) {
    this.mediaId = mediaId;
  }
}
