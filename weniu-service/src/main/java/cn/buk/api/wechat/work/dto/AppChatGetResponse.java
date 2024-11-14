package cn.buk.api.wechat.work.dto;


import cn.buk.api.wechat.dto.BaseResponse;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * 获取群聊会话
 *
 * @author yfdai
 */
public class AppChatGetResponse extends BaseResponse {

  @JSONField(name = "chat_info")
  private AppChatCreateRequest chatInfo;

  public AppChatCreateRequest getChatInfo() {
    return chatInfo;
  }

  public void setChatInfo(AppChatCreateRequest chatInfo) {
    this.chatInfo = chatInfo;
  }
}
