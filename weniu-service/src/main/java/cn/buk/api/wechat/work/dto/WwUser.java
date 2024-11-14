package cn.buk.api.wechat.work.dto;

import cn.buk.api.wechat.dto.BaseResponse;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * 企业微信中的成员
 * Work Weixin: ww
 *
 * @author yfdai
 */
public class WwUser extends BaseResponse {

  private String userid; // * 成员UserID。对应管理端的帐号，企业内必须唯一。不区分大小写，长度为1~64个字节

  private String name; // * 成员名称。长度为1~64个字节

  private String gender; //性别。0表示未定义，1表示男性，2表示女性

  private String alias; // 	别名；第三方仅通讯录应用可获取

  private String english_name;

  private String mobile; // 手机号码。企业内必须唯一，mobile/email二者不能同时为空

  private String email; // 邮箱。长度为0~64个字节。企业内必须唯一，mobile/email二者不能同时为空

  private String position;

  private int[] department; // * 成员所属部门id列表,不超过20个

  private int[] order; // * 部门内的排序值，默认为0。数量必须和department一致，数值越大排序越前面。值范围是[0, 2^32)

  @JSONField(name = "is_leader_in_dept")
  private int[] isLeader;


  @JSONField(name = "external_position")
  private String externalPosition; // 	对外职务，如果设置了该值，则以此作为对外展示的职务，否则以position来展示。

  private int enable; //成员启用状态。1表示启用的成员，0表示被禁用。注意，服务商调用接口不会返回此字段

  private int status; //激活状态: 1=已激活，2=已禁用，4=未激活。
//    已激活代表已激活企业微信或已关注微工作台（原企业号）。未激活代表既未激活企业微信又未关注微工作台（原企业号）。

  private String telephone;

  private String address; // 	地址。

  @JSONField(name = "qr_code")
  private String qrCode; //员工个人二维码，扫描可添加为外部联系人；第三方仅通讯录应用可获取

  private String avatar;  //头像url。注：如果要获取小图将url最后的”/0”改成”/100”即可。第三方仅通讯录应用可获取


  private WwUserExtAttr extattr;


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public int[] getDepartment() {
    return department;
  }

  public void setDepartment(int[] department) {
    this.department = department;
  }

  public String getEnglish_name() {
    return english_name;
  }

  public void setEnglish_name(String english_name) {
    this.english_name = english_name;
  }

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public int getEnable() {
    return enable;
  }

  public void setEnable(int enable) {
    this.enable = enable;
  }

  public String getTelephone() {
    return telephone;
  }

  public void setTelephone(String telephone) {
    this.telephone = telephone;
  }

  public String getUserid() {
    return userid;
  }

  public void setUserid(String userid) {
    this.userid = userid;
  }

  public WwUserExtAttr getExtattr() {
    if (extattr == null) extattr = new WwUserExtAttr();
    return extattr;
  }

  public void setExtattr(WwUserExtAttr extattr) {
    this.extattr = extattr;
  }

  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getExternalPosition() {
    return externalPosition;
  }

  public void setExternalPosition(String externalPosition) {
    this.externalPosition = externalPosition;
  }

  public String getQrCode() {
    return qrCode;
  }

  public void setQrCode(String qrCode) {
    this.qrCode = qrCode;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public int[] getIsLeader() {
    return isLeader;
  }

  public void setIsLeader(int[] isLeader) {
    this.isLeader = isLeader;
  }

  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  public int[] getOrder() {
    return order;
  }

  public void setOrder(int[] order) {
    this.order = order;
  }
}
