package cn.buk.api.wechat.work.message;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author yfdai
 */
public class AppChatFileMessage extends AppChatMessage {

  public AppChatFileMessage() {
    super("file");
  }


  @JSONField(name = "file")
  private MediaInfo mediaInfo = new MediaInfo();

  public MediaInfo getMediaInfo() {
    return mediaInfo;
  }

  public void setMediaInfo(MediaInfo mediaInfo) {
    this.mediaInfo = mediaInfo;
  }
}
