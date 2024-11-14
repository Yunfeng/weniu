package cn.buk.api.wechat.work.message;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yfdai
 */
public class TextCard {

  /**
   * 必需
   * 标题，不超过128个字节，超过会自动截断（支持id转译）
   */
  private String title;

  /**
   * 必需
   * 描述，不超过512个字节，超过会自动截断（支持id转译）
   */
  private String description;

  /**
   * 点击后跳转的链接。最长2048字节，请确保包含了协议头(http/https)
   */
  private String url;

  /**
   * 按钮文字。 默认为“详情”， 不超过4个文字，超过自动截断。
   */
  @JSONField(name = "btntxt")
  private String btnText;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getBtnText() {
    return btnText;
  }

  public void setBtnText(String btnText) {
    this.btnText = btnText;
  }
}
