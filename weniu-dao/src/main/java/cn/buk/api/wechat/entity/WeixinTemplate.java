package cn.buk.api.wechat.entity;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 微信模板消息的模板
 */
@Entity
@Table(name="weixin_template", uniqueConstraints = @UniqueConstraint(name = "uk_wx_template", columnNames = {"enterprise_id", "template_id"}))
public class WeixinTemplate {

    public static final String BUSINESS_TPL_CHECK_IN = "001"; // 签到成功业务通知
    public static final String BUSINESS_TPL_POINTS_CHANGED = "002"; // 积分消费通知
    public static final String BUSINESS_TPL_ORDER_PAY = "101"; // 订单付款业务通知

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="enterprise_id")
    private int enterpriseId;

    private String template_id;

    private String title;

    private String content;

    /**
     * 业务ID
     */
    @Column(name = "business_id")
    private String businessId;

    /**
     * 尽量保持为空
     * 会将此值放到模板消息的first.data中, 比如"演示: "
     */
    private String remark;

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


    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public void setLastUpdate(Date lastupdate) {
        this.lastUpdate = lastupdate;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getRemark() {
        return remark == null ? "" : remark.trim();
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(int enterpriseId) {
        this.enterpriseId = enterpriseId;
    }
}
