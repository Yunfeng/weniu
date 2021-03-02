package cn.buk.api.wechat.work.message;

import com.alibaba.fastjson.annotation.JSONField;

public class CardButton {
  /**
   * 必需
   * 按钮key值，用户点击后，会产生任务卡片回调事件，回调事件会带上该key值，只能由数字、字母和“_-@”组成，最长支持128字节
   */
  private String key;

  /**
   * 必需
   * 按钮名称
   */
  private String name;

  /**
   * 点击按钮后显示的名称，默认为“已处理”
   */
  @JSONField(name = "replace_name")
  private String replaceName;

  /**
   * 按钮字体颜色，可选“red”或者“blue”,默认为“blue”
   */
  private String color;

  /**
   * 按钮字体是否加粗，默认false
   */
  @JSONField(name = "is_bold")
  private boolean isBold;


  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getReplaceName() {
    return replaceName;
  }

  public void setReplaceName(String replaceName) {
    this.replaceName = replaceName;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public boolean isBold() {
    return isBold;
  }

  public void setBold(boolean bold) {
    isBold = bold;
  }
}
