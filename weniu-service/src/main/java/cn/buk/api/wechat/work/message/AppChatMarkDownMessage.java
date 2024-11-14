package cn.buk.api.wechat.work.message;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author yfdai
 */
public class AppChatMarkDownMessage extends AppChatMessage {

  public AppChatMarkDownMessage() {
    super("markdown");
  }

  @JSONField(name = "markdown")
  private Text text = new Text();

  public Text getText() {
    return text;
  }

  public void setText(Text text) {
    this.text = text;
  }
}
