package cn.buk.api.wechat.work.message;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author yfdai
 */
public class AppChatImageMessage extends AppChatMessage {

  public AppChatImageMessage() {
    super("image");
  }


  @JSONField(name = "image")
  private MediaInfo mediaInfo = new MediaInfo();

  public MediaInfo getMediaInfo() {
    return mediaInfo;
  }

  public void setMediaInfo(MediaInfo mediaInfo) {
    this.mediaInfo = mediaInfo;
  }
}
