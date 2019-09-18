package cn.buk.api.wechat.work.dto;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取配置了客户联系功能的成员列表 反馈
 * @author yfdai
 */
public class ExternalContactDetailResponse extends BaseResponse {

  /**
   * 外部联系人详情
   */
  @JSONField(name = "external_contact")
  private ExternalContactDetail externalContactDetail;

  /**
   * 此外部联系人对应企业的员工，可能多个员工对应同一个外部联系人
   */
  @JSONField(name = "follow_user")
  private List<ExternalContactFollowUser> followUsers;

  public ExternalContactDetail getExternalContactDetail() {
    return externalContactDetail;
  }

  public void setExternalContactDetail(
      ExternalContactDetail externalContactDetail) {
    this.externalContactDetail = externalContactDetail;
  }

  public List<ExternalContactFollowUser> getFollowUsers() {
    if (followUsers == null) {
      followUsers = new ArrayList<>();
    }
    return followUsers;
  }

  public void setFollowUsers(
      List<ExternalContactFollowUser> followUsers) {
    this.followUsers = followUsers;
  }
}
