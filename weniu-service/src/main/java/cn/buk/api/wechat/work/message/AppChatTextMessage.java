package cn.buk.api.wechat.work.message;

/**
 * @author yfdai
 */
public class AppChatTextMessage extends AppChatMessage {

  public AppChatTextMessage() {
    super("text");
  }

  private Text text = new Text();

  public Text getText() {
    return text;
  }

  public void setText(Text text) {
    this.text = text;
  }
}
