package cn.buk.api.wechat.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * 微信自定义菜单
 *
 * @author yfdai
 *  2017/2/23
 */
@Entity
@Table(name = "weixin_custom_menu")
public class WeixinCustomMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "enterprise_id")
    private int enterpriseId;

    /**
     * 自定义菜单最多包括3个一级菜单，每个一级菜单最多包含5个二级菜单。
     * 一级菜单最多4个汉字，二级菜单最多7个汉字，多出来的部分将会以“...”代替。
     * 二级菜单好像可以不只5个字
     * 1,2 1-表示一级菜单
     */
    private int level;

    @Column(name = "parent_id")
    private int parentId;

    private String name;

    /**
     * 生成菜单时的顺序值
     */
    @Column(name = "order_num")
    private Integer orderNum;

    /**
     * click view
     */
    private String type;

    /**
     * type = click
     */
    @Column(name = "event_key")
    private String key;

    /**
     * 0 - 不绑定 微信授权 url
     * 1 - (默认） 绑定微信授权rul
     */
    @Column(name = "bind_url")
    private Integer bindUrl;

    /**
     * 2000是指字符
     * type = view
     */
    @Column(length = 2000)
    private String url;

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

    public Integer getBindUrl() {
        return bindUrl == null ? 1 : bindUrl;
    }

    public void setBindUrl(Integer bindUrl) {
        this.bindUrl = bindUrl;
    }

    public int getOrderNum() {
        return orderNum == null ? 0 : orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }
}
