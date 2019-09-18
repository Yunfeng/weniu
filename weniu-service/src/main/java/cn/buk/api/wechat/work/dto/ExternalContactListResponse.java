package cn.buk.api.wechat.work.dto;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 获取配置了客户联系功能的成员列表 反馈
 * @author yfdai
 */
public class ExternalContactListResponse extends BaseResponse {

  /**
   * 外部联系人的userid列表
   */
  @JSONField(name = "external_userid")
  private String[] externalUserIds;

  public String[] getExternalUserIds() {
    return externalUserIds;
  }

  public void setExternalUserIds(String[] externalUserIds) {
    this.externalUserIds = externalUserIds;
  }


}
