package cn.buk.api.wechat.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * 微信自定义菜单
 * Created by yfdai on 2017/2/23.
 */
@Entity
@Table(name = "weixin_custom_menu")
public class WeixinCustomMenu {

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "enterprise_id")
    private int enterpriseId;

    /**
     * 自定义菜单最多包括3个一级菜单，每个一级菜单最多包含5个二级菜单。
     * 一级菜单最多4个汉字，二级菜单最多7个汉字，多出来的部分将会以“...”代替。
     * 二级菜单好像可以不只5个字
     */
    private int level; //1,2 1-表示一级菜单

    @Column(name = "parent_id")
    private int parentId;

    private String name;

    private String type; // click view

    @Column(name = "event_key")
    private String key; // type = click


    /**
     * 2000是指字符
     */
    @Column(length = 2000)
    private String url; // type = view

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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
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
}
