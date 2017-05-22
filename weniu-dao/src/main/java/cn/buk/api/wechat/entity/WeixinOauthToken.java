package cn.buk.api.wechat.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * 微信网页授权token Oauth
 */
@Entity
@Table(name="weixin_oauth_token")
public class WeixinOauthToken {

    @Id
    @GeneratedValue
    private int id;

    private int weixinId;

    @Column(length=512)
    private String access_token;

    @Column(length=512)
    private String refresh_token;

    private int expires_in;

    private String openid;

    private String scope;

    @Column(insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
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

    public int getWeixinId() {
        return weixinId;
    }

    public void setWeixinId(int weixinId) {
        this.weixinId = weixinId;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
