package cn.buk.api.wechat.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * 发送图文消息（点击跳转到外链）
 * Created by yfdai on 2017/2/23.
 */
@Entity
@Table(name = "weixin_news")
public class WeixinNews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "enterprise_id")
    private int enterpriseId;

//    发送图文消息（点击跳转到图文消息页面）

    @Column(name = "media_id")
    private String mediaId;


//    发送图文消息（点击跳转到外链）

    private String title;

    private String description;

    /**
     * 外链的url
     */
    private String url;

    /**
     * 微信素材图片url
     */
    private String picurl;

    /**
     * 显示顺序
     */
    @Column(name = "display_order")
    private int displayOrder;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    private Date createTime;

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

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getUrl() {
        return url;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }
}


//{
//        "button": [
//        {
//        "name": "演示",
//        "sub_button": [
//        {
//        "type": "view",
//        "name": "测试",
//        "url": "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx9a613d6e422845b9&redirect_uri=http%3a%2f%2fmayer.90sky.com%2fwx-oauth.html&response_type=code&scope=snsapi_userinfo&state=SHANGHAIZHIYOUPARAMS#wechat_redirect"
//        },
//        {
//        "type": "view",
//        "name": "蔬菜",
//        "url": "http://mayer.90sky.com"
//        },
//        {
//        "type": "view",
//        "name": "机票",
//        "url": "http://b2c.90sky.com"
//        }
//        ]
//        },
//        {
//        "name": "机票服务",
//        "sub_button": [
//        {
//        "type": "click",
//        "name": "提醒消息",
//        "key": "V_CLICK_SEARCH_WARNING_MESSAGE"
//        },
//        {
//        "type": "click",
//        "name": "航班查询",
//        "key": "V_CLICK_SEARCH_FLIGHT"
//        }
//        ]
//        },
//        {
//        "name": "智游会员",
//        "sub_button": [
//        {
//        "type": "click",
//        "name": "签到",
//        "key": "V_CLICK_CHECKIN"
//        },
//        {
//        "type": "click",
//        "name": "入会/绑定",
//        "key": "V_CLICK_BIND"
//        }
//        ]
//        }
//        ]
//        }
