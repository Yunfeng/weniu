package cn.buk.api.wechat.work.message;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author yfdai
 */
public class AppChatVoiceMessage extends AppChatMessage {

  public AppChatVoiceMessage() {
    super("voice");
  }


  @JSONField(name = "voice")
  private MediaInfo mediaInfo = new MediaInfo();

  public MediaInfo getMediaInfo() {
    return mediaInfo;
  }

  public void setMediaInfo(MediaInfo mediaInfo) {
    this.mediaInfo = mediaInfo;
  }
}
