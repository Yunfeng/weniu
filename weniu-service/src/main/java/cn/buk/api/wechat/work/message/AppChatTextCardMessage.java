package cn.buk.api.wechat.work.message;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author yfdai
 */
public class AppChatTextCardMessage extends AppChatMessage {

  public AppChatTextCardMessage() {
    super("textcard");
  }


  @JSONField(name = "textcard")
  private TextCard textCard = new TextCard();

  public TextCard getTextCard() {
    return textCard;
  }

  public void setTextCard(TextCard textCard) {
    this.textCard = textCard;
  }
}
