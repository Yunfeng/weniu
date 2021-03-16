package cn.buk.api.wechat.entity;

import javax.persistence.*;
import java.util.Date;

/**
 *
 * @author yfdai
 * 2019-04-07
 *
 * 微信服务号应用的API参数
 */
@Entity
@Table(name = "weixin_service_config", uniqueConstraints = @UniqueConstraint(
        name = "uk_weixin_service_config", columnNames = {"enterprise_id", "msg_type"}))
public class WeixinServiceConfig {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "enterprise_id")
    private int enterpriseId;

    /**
     * 预留
     * 0 - 默认
     */
    @Column(name = "msg_type")
    private int msgType;

    /**
     * 开发者ID(AppID)
     */
    @Column(name = "app_id", nullable = false)
    private String appId;

    /**
     * 开发者密码(AppSecret)
     */
    @Column(name = "app_secret", nullable = false)
    private String appSecret;

    /**
     * 令牌(Token)
     * 3-32字符
     */
    @Column(nullable = false)
    private String token;

    /**
     * 消息加解密密钥
     * EncodingAESKey
     *消息加密密钥由43位字符组成，可随机修改，字符范围为A-Z，a-z，0-9。
     */
    @Column(name = "encoding_key")
    private String encodingKey;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_update", insertable = false)
    private Date lastUpdate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(int enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getEncodingKey() {
        return encodingKey;
    }

    public void setEncodingKey(String encodingKey) {
        this.encodingKey = encodingKey;
    }
}
