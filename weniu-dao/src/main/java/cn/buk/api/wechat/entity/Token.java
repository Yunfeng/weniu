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
    public static final int WORK_WEIXIN_TOKEN = 8; // 企业微信 token
    public static final int WORK_WEIXIN_PROVIDER = 16; // 企业微信服务商

    public static final int WEIXIN_JS_SDK_TICKET = 100; // 微信 js-sdk ticket
    public static final int WORK_WEIXIN_JSAPI_TICKET = 108; // 企业微信 js-sdk ticket

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int weixinId;

    /**
     * 0 - 服务号
     * 1 - 订阅号
     * 2 - 小程序
     * 8 - 企业微信
     * 16 - 企业微信服务商
     */
    @Column(name = "weixin_type")
    private int weixinType;

    /**
     * 对应的消息类型：0-企业微信的默认应用，1-企业微信的通讯录，2-企业微信的外部联系人
     */
    @Column(name = "msg_type")
    private Integer msgType = 0;

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

    public Integer getMsgType() {
        return msgType == null ? 0 : msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }
}
