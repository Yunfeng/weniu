package cn.buk.api.wechat.work.dto;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.Date;

/**
 * @author yfdai
 */
public class ExternalContactFollowUser {

  @JSONField(name = "userid")
  private String userId;

  private String remark;

  private String description;

  @JSONField(name = "createtime")
  private Date createTime;

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Date getCreateTime() {
    if (this.createTime != null && Long.toString(this.createTime.getTime()).length() == 10) {
      // 企业微信接口中的时间戳是10位的，没有毫秒；而java中的时间戳是带毫秒的
      this.createTime = new Date(this.createTime.getTime() * 1000);
    }
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }
}
