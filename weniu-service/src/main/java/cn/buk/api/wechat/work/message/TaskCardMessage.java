package cn.buk.api.wechat.work.message;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 发送应用消息:文本消息
 */
public class TaskCardMessage extends AbstractMessage {

  public TaskCardMessage() {
    super("taskcard");
  }

  @JSONField(name = "taskcard")
  private TaskCard taskCard;


  public TaskCard getTaskCard() {
    return taskCard;
  }

  public void setTaskCard(TaskCard taskCard) {
    this.taskCard = taskCard;
  }
}
