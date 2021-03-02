package cn.buk.api.wechat.work.message;

/**
 * 发送应用消息:文本消息
 */
public class TextMessage extends AbstractMessage {

    public TextMessage() {
        super("text");
    }

    /**
     * 表示是否是保密消息，0表示否，1表示是，默认0
     */
    private int safe = 0;

    /**
     * 必需
     * 消息内容
     */
    private Text text = new Text();



    public int getSafe() {
        return safe;
    }

    public void setSafe(int safe) {
        this.safe = safe;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public void setContent(String content) {
        this.text.setContent(content);
    }

}
