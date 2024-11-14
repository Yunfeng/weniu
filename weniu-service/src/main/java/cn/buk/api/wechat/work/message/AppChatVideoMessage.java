package cn.buk.api.wechat.work.message;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author yfdai
 */
public class AppChatVideoMessage extends AppChatMessage {

  public AppChatVideoMessage() {
    super("video");
  }


  @JSONField(name = "video")
  private MediaInfo mediaInfo = new MediaInfo();

  public MediaInfo getMediaInfo() {
    return mediaInfo;
  }

  public void setMediaInfo(MediaInfo mediaInfo) {
    this.mediaInfo = mediaInfo;
  }
}
