package cn.buk.api.wechat.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * 关注微信公众号的用户
 */
@Entity
@Table(name="weixin_user", uniqueConstraints = @UniqueConstraint(name = "uk_wx_user", columnNames = {"weixin_id", "weixin_open_id"}))
public class WeixinUser {

    @Id
    @GeneratedValue
    private int id;

    @Column(name="weixin_id")
    private int ownerId;

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
    private String headimgurl;

    @Temporal(TemporalType.TIMESTAMP)
    private Date subscribe_time;

    @Column(name="weixin_union_id")
    private String unionid;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(insertable = false, updatable = false)
    private Date createTime;

    @Column(name="group_id")
    private Integer groupId;

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

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }
}
