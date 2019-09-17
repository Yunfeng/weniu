package cn.buk.api.wechat.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by yfdai on 16/7/9.
 *
 * 企业微信应用的API参数
 */
@Entity
@Table(name = "weixin_ent_config", uniqueConstraints = @UniqueConstraint(name = "uk_weixin_ent_config", columnNames = {"enterprise_id", "msg_type"}))
public class WeixinEntConfig {

    /**
     * 企业微信的默认自建应用
     */
    public static final int WORK_WX_DEFAULT = 0;

    /**
     * 企业微信的通讯录API
     */
    public static final int WORK_WX_CONTACTS = 1;

    /**
     * 企业微信的外部联系人
     */
    public static final int WORK_WX_EXTERNAL_CONTACTS = 2;


    /**
     * 企业微信的服务商(通用参数), 代表的是服务商的身份，与应用无关。
     * 请求单点登录、注册定制化等接口需要用到该凭证。
     */
    public static final int WORK_WX_PROVIDER_COMMON = 8;
    /**
     * 企业微信的服务商应用
     */
    public static final int WORK_WX_PROVIDER_APP = 9;
    /**
     * 企业微信的服务商应用(suiteId 代替 corpId)
     */
    public static final int WORK_WX_PROVIDER_APP_SUITE_ID = 10;


    /**
     * 企业微信的企业支付
     */
    public static final int WORK_WX_PAY = 100;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "enterprise_id")
    private int enterpriseId;

    /**
     * 消息类型
     * 0 - 默认的企业微信自定义应用
     * 1 - 企业微信的通讯录API
     * 2 - 企业微信的外部联系人api
     */
    @Column(name = "msg_type")
    private int msgType;

    @Column(name = "corp_id")
    private String corpId;

    private String secret;

    @Column(name = "agent_id")
    private int agentId;

    /**
     * 企业微信中企业应用API接受消息的token
     */
    private String token;

    /**
     * 企业微信中企业应用API接受消息的encodingAESKey
     */
    @Column(name = "encoding_aes_key")
    private String encodingAESKey;

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

    public String getCorpId() {
        return corpId.trim();
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getSecret() {
        return secret.trim();
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
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

    public String getEncodingAESKey() {
        return encodingAESKey;
    }

    public void setEncodingAESKey(String encodingAESKey) {
        this.encodingAESKey = encodingAESKey;
    }
}
