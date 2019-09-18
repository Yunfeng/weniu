package cn.buk.api.wechat.work.dto;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 获取配置了客户联系功能的成员列表 反馈
 * @author yfdai
 */
public class ExternalContactFollowUsersResponse extends BaseResponse {

  /**
   * 配置了客户联系功能的成员userid列表
   */
  @JSONField(name = "follow_user")
  private String[] users;

  public String[] getUsers() {
    return users;
  }

  public void setUsers(String[] users) {
    this.users = users;
  }

}
