package cn.buk.api.wechat.work.message;

import com.alibaba.fastjson.annotation.JSONField;

public abstract class AbstractMessage {

  protected AbstractMessage(String msgtype) {
    this.msgtype = msgtype;
  }

  /**
   * 成员ID列表（消息接收者，多个接收者用‘|’分隔，最多支持1000个）。特殊情况：指定为@all，则向关注该企业应用的全部成员发送
   */

  private String touser;

  /**
   * 部门ID列表，多个接收者用‘|’分隔，最多支持100个。当touser为@all时忽略本参数
   */
  private String toparty;

  /**
   * 标签ID列表，多个接收者用‘|’分隔。当touser为@all时忽略本参数
   */
  private String totag;

  /**
   * 必需,
   * 消息类型，
   * 不同的消息类型此值不同
   */
  private final String msgtype;

  /**
   * 企业应用的id，整型。企业内部开发，可在应用的设置页面查看；第三方服务商，可通过接口 获取企业授权信息 获取该参数值
   */
  private int agentid;

  /**
   * 表示是否开启id转译，0表示否，1表示是，默认0
   */
  @JSONField(name = "enable_id_trans")
  private int enableIdTrans;
  /**
   * 表示是否开启重复消息检查，0表示否，1表示是，默认0
   */
  @JSONField(name = "enable_duplicate_check")
  private int enableDuplicateCheck;
  /**
   * 表示是否重复消息检查的时间间隔，默认1800s，最大不超过4小时
   */
  @JSONField(name = "duplicate_check_interval")
  private int duplicateCheckInterval;

  public String getTouser() {
    return touser;
  }

  public void setTouser(String touser) {
    this.touser = touser;
  }

  public String getToparty() {
    return toparty;
  }

  public void setToparty(String toparty) {
    this.toparty = toparty;
  }

  public String getTotag() {
    return totag;
  }

  public void setTotag(String totag) {
    this.totag = totag;
  }

  public String getMsgtype() {
    return msgtype;
  }

  public int getAgentid() {
    return agentid;
  }

  public void setAgentid(int agentid) {
    this.agentid = agentid;
  }

  public int getEnableIdTrans() {
    return enableIdTrans;
  }

  public void setEnableIdTrans(int enableIdTrans) {
    this.enableIdTrans = enableIdTrans;
  }

  public int getEnableDuplicateCheck() {
    return enableDuplicateCheck;
  }

  public void setEnableDuplicateCheck(int enableDuplicateCheck) {
    this.enableDuplicateCheck = enableDuplicateCheck;
  }

  public int getDuplicateCheckInterval() {
    return duplicateCheckInterval;
  }

  public void setDuplicateCheckInterval(int duplicateCheckInterval) {
    this.duplicateCheckInterval = duplicateCheckInterval;
  }
}
