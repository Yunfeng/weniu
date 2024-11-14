package cn.buk.api.wechat.work.dto;


import cn.buk.api.wechat.dto.BaseResponse;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * 创建群聊
 *
 * @author yfdai
 */
public class AppChatCreateResponse extends BaseResponse {

  /**
   * 群聊的唯一标志
   */
  @JSONField(name = "chatid")
  private String chatId;

  public String getChatId() {
    return chatId;
  }

  public void setChatId(String chatId) {
    this.chatId = chatId;
  }
}
