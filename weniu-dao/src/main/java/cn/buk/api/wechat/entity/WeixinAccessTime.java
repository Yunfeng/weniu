package cn.buk.api.wechat.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * 记录哪些用户主动与公众号互动了,
 * 开发者在一段时间内（目前修改为48小时）可以调用客服消息
 * User: yfdai
 */
@Entity
@Table(name="weixin_user_access")
public class WeixinAccessTime {

    @Id
    @GeneratedValue
    private int id;

    private int weixinId;

    @Column(length=64, name="weixin_open_id")
    private String weixinOpenId;

    @Column(name="access_time")
    private Date accessTime;

    @Column(insertable = false, updatable = false)
    private Date createTime;

    public String getWeixinOpenId() {
        return weixinOpenId;
    }

    public void setWeixinOpenId(String weixinOpenId) {
        this.weixinOpenId = weixinOpenId;
    }

    public Date getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(Date accessTime) {
        this.accessTime = accessTime;
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
}
