package cn.buk.api.wechat.entity;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 企业微信服务商 suite_ticket
 */
@Entity
@Table(name="ww_provider_ticket",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_ww_provider_ticket",
                columnNames = {"enterprise_id", "suite_id"}
                )
)
public class WwProviderTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "enterprise_id")
    private int enterpriseId;

    //第三方应用的SuiteId
    @Column(name = "suite_id")
    private String suiteId;

    //Ticket内容，最长为512字节
    @Column(name = "suite_ticket", length = 512)
    private String suiteTicket;

    //时间戳
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time_stamp")
    private Date timeStamp;

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

    public String getSuiteId() {
        return suiteId;
    }

    public void setSuiteId(String suiteId) {
        this.suiteId = suiteId;
    }

    public String getSuiteTicket() {
        return suiteTicket;
    }

    public void setSuiteTicket(String suiteTicket) {
        this.suiteTicket = suiteTicket;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
