package cn.buk.api.wechat.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 关注微信公众号的用户
 */
@Entity
@Table(name="weixin_user", uniqueConstraints = @UniqueConstraint(name = "uk_wx_user", columnNames = {"enterprise_id", "weixin_open_id"}))
public class WeixinUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="enterprise_id")
    private int enterpriseId;

    @Column(length=64, name="weixin_open_id")
    private String weixinOpenId;

    /**
     * subscribe 	用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。
     */
    private Integer subscribe;

    /**
     * nickname 	用户的昵称
     */
    private String nickname;

    /**
     * sex 	用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
     */
    private Integer sex;

    /**
     * language 	用户的语言，简体中文为zh_CN
     */
    private String language;


    private String city;
    private String province;
    private String country;
    @Column(name = "head_img_url")
    private String headimgurl;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    private Date subscribe_time;

    @Column(name="weixin_union_id")
    private String unionid;

    /**
     * 公众号运营者对粉丝的备注，公众号运营者可在微信公众平台用户管理界面对粉丝添加备注
     */
    private String remark;

    /**
     * 返回用户关注的渠道来源
     * ADD_SCENE_SEARCH 公众号搜索
     * ADD_SCENE_ACCOUNT_MIGRATION 公众号迁移
     * ADD_SCENE_PROFILE_CARD 名片分享，
     * ADD_SCENE_QR_CODE 扫描二维码，
     * ADD_SCENEPROFILE LINK 图文页内名称点击，
     * ADD_SCENE_PROFILE_ITEM 图文页右上角菜单，
     * ADD_SCENE_PAID 支付后关注，
     * ADD_SCENE_OTHERS 其他
     */
    @Column(name = "subscribe_scene")
    private String subscribe_scene;

    @Column(name="group_id")
    private Integer groupId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_update", insertable = false)
    private Date lastUpdate;

    public String getWeixinOpenId() {
        return weixinOpenId;
    }

    public void setWeixinOpenId(String weixinOpenId) {
        this.weixinOpenId = weixinOpenId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSubscribe() {
        if (subscribe == null)
            return 0;
        else
            return subscribe.intValue();
    }

    public void setSubscribe(Integer subscribe) {
        this.subscribe = subscribe;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSex() {
        if (sex == null)
            return 0;
        else
            return sex.intValue();
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public Date getSubscribe_time() {
        return subscribe_time;
    }

    public void setSubscribe_time(Date subscribe_time) {
        this.subscribe_time = subscribe_time;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public int getGroupId() {
        if (groupId == null)
            return -1;
        else
            return groupId.intValue();
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public int getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(int ownerId) {
        this.enterpriseId = ownerId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSubscribe_scene() {
        return subscribe_scene;
    }

    public void setSubscribe_scene(String subscribe_scene) {
        this.subscribe_scene = subscribe_scene;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
