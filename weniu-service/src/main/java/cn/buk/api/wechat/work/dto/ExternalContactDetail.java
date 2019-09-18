package cn.buk.api.wechat.work.dto;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 外部联系人详情
 * @author yfdai
 */
public class ExternalContactDetail {

  /**
   * 外部联系人的userid
   */
  @JSONField(name = "external_userid")
  private String externalUserId;

  /**
   * 外部联系人在微信开放平台的唯一身份标识（微信unionid），通过此字段企业可将外部联系人与公众号/小程序用户关联起来。
   * 仅当联系人类型是微信用户，且企业绑定了微信开发者ID有此字段。查看绑定方法
   */
  @JSONField(name = "unionid")
  private String unionId;

  /**
   * 外部联系人的姓名或别名
   */
  private String name;

  /**
   * 外部联系人头像，第三方不可获取
   */
  private String avatar;

  /**
   * 外部联系人的类型，1表示该外部联系人是微信用户，2表示该外部联系人是企业微信用户
   */
  private int type;

  /**
   * 外部联系人性别 0-未知 1-男性 2-女性
   */
  private int gender;

  /**
   * 外部联系人的职位，如果外部企业或用户选择隐藏职位，则不返回，仅当联系人类型是企业微信用户时有此字段
   */
  private String position;

  /**
   * 外部联系人所在企业的简称，仅当联系人类型是企业微信用户时有此字段
   */
  @JSONField(name = "corp_name")
  private String corpName;

  /**
   * 外部联系人所在企业的主体名称，仅当联系人类型是企业微信用户时有此字段
   */
  @JSONField(name = "corp_full_name")
  private String corpFullName;



  public String getExternalUserId() {
    return externalUserId;
  }

  public void setExternalUserId(String externalUserId) {
    this.externalUserId = externalUserId;
  }

  public String getUnionId() {
    return unionId;
  }

  public void setUnionId(String unionId) {
    this.unionId = unionId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public int getGender() {
    return gender;
  }

  public void setGender(int gender) {
    this.gender = gender;
  }

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public String getCorpName() {
    return corpName;
  }

  public void setCorpName(String corpName) {
    this.corpName = corpName;
  }

  public String getCorpFullName() {
    return corpFullName;
  }

  public void setCorpFullName(String corpFullName) {
    this.corpFullName = corpFullName;
  }

}
