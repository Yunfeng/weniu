package cn.buk.api.wechat.work.message;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

public class TaskCard {

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
   * 必需
   * 任务id，同一个应用发送的任务卡片消息的任务id不能重复，只能由数字、字母和“_-@”组成，最长支持128字节
   */
  @JSONField(name = "task_id")
  private String taskId;

  /**
   * 必需
   * 按钮列表，按钮个数为1~2个。
   */
  @JSONField(name = "btn")
  private List<CardButton> buttonList;

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

  public String getTaskId() {
    return taskId;
  }

  public void setTaskId(String taskId) {
    this.taskId = taskId;
  }

  public List<CardButton> getButtonList() {
    if (buttonList == null) {
      buttonList = new ArrayList<>();
    }
    return buttonList;
  }

  public void setButtonList(List<CardButton> buttonList) {
    this.buttonList = buttonList;
  }

}
