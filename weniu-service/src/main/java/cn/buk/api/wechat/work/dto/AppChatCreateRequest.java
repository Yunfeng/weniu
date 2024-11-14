package cn.buk.api.wechat.work.dto;


import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建群聊
 *
 * @author yfdai
 */
public class AppChatCreateRequest {

  @JSONField(name = "userlist")
  private List<String> userList;

  private String name;

  @JSONField(name = "chatid")
  private String chatId;


  private String owner;

  /**
   * 必须，群成员id列表。至少2人，至多2000人
   */
  public List<String> getUserList() {
    if (userList == null) {
      userList = new ArrayList<>();
    }
    return userList;
  }

  public void setUserList(List<String> userList) {
    this.userList = userList;
  }

  /**
   * 群聊名，最多50个utf8字符，超过将截断
   */
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * 群聊的唯一标志，不能与已有的群重复；字符串类型，最长32个字符。只允许字符0-9及字母a-zA-Z。如果不填，系统会随机生成群id
   */
  public String getChatId() {
    return chatId;
  }

  public void setChatId(String chatId) {
    this.chatId = chatId;
  }

  /**
   * 指定群主的id。如果不指定，系统会随机从userlist中选一人作为群主
   */
  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }
}
