package cn.buk.api.wechat.work.message;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author yfdai
 */
public class AppChatMessage {

  protected AppChatMessage(String msgtype) {
    this.msgType = msgtype;
  }

  @JSONField(name = "chatid")
  private String chatId;

  @JSONField(name = "msgtype")
  private String msgType;

  /**
   * 表示是否是保密消息，0表示否，1表示是，默认0
   */
  private int safe;

  public String getChatId() {
    return chatId;
  }

  public void setChatId(String chatId) {
    this.chatId = chatId;
  }

  public String getMsgType() {
    return msgType;
  }

  public void setMsgType(String msgType) {
    this.msgType = msgType;
  }

  public int getSafe() {
    return safe;
  }

  public void setSafe(int safe) {
    this.safe = safe;
  }
}
