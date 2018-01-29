package cn.buk.api.wechat.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by yfdai on 16/7/9.
 *
 * 微信企业号设置
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


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "enterprise_id")
    private int enterpriseId;

    /**
     * 消息类型
     * 0 - 默认的企业微信自定义应用
     * 1 - 企业微信的通讯录API
     */
    @Column(name = "msg_type")
    private int msgType;

    @Column(name = "corp_id")
    private String corpId;

    private String secret;

    @Column(name = "agent_id")
    private int agentId;

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
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getSecret() {
        return secret;
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
}
