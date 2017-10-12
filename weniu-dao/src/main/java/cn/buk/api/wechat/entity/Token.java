package cn.buk.api.wechat.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * User: yfdai
 */
@Entity
@Table(name="weixin_token")
public class Token {

    public static final int WEIXIN_SERVICE_TOKEN = 0; // 微信服务号 token
    public static final int WEIXIN_ENT_TOKEN = 8; // 微信企业号 token

    public static final int WEIXIN_JS_SDK_TICKET = 100; // 微信 js-sdk ticket

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int weixinId;

    /**
     * 0 - 服务号
     * 1 - 订阅号
     * 2 - 小程序
     * 8 - 企业号
     */
    @Column(name = "weixin_type")
    private int weixinType;

    @Column(length=512)
    private String access_token;

    private int expires_in;

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

    public int getWeixinType() {
        return weixinType;
    }

    public void setWeixinType(int weixinType) {
        this.weixinType = weixinType;
    }
}
