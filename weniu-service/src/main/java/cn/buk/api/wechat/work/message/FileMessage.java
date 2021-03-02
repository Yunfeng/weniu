package cn.buk.api.wechat.work.message;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 发送应用消息:文本消息
 */
public class FileMessage extends AbstractMessage {

  public FileMessage() {
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
