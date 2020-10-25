package cn.buk.api.wechat.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * 授权企业信息
 */
@Entity
@Table(name="ww_provider_auth_corp_info",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_ww_provider_auth_corp_info",
                columnNames = {"enterprise_id", "suite_id", "corp_id"}
        )
)
public class WwProviderAuthCorpInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "enterprise_id")
    private int enterpriseId;

    //应用ID
    @Column(name = "suite_id")
    private String suiteId;

    //授权企业的corpId
    @Column(name = "corp_id")
    private String corpId;


    /**
     * 授权状态
     * 1 - 已授权
     * 0 - 未授权
     */
    private int status;

    @Column(name = "access_token", length=512)
    private String accessToken;

    @Column(name = "expires_in")
    private int expiresIn;

    /**
     * 永久授权码
     */
    @Column(name = "permanent_code", length = 512)
    private String permanentCode;

    @Column(name = "corp_name")
    private String corpName;

    @Column(name = "corp_full_name")
    private String corpFullName;

    /**
     * 授权人 user_id
     */
    @Column(name = "auth_user_id")
    private String authUserId;

    /**
     * 授权人 姓名
     */
    @Column(name = "auth_user_name")
    private String authUserName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_update")
    private Date lastUpdate;


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

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public int getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(int enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getPermanentCode() {
        return permanentCode;
    }

    public void setPermanentCode(String permanentCode) {
        this.permanentCode = permanentCode;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getCorpFullName() {
        return corpFullName;
    }

    public void setCorpFullName(String corpFullName) {
        this.corpFullName = corpFullName;
    }

    public String getAuthUserId() {
        return authUserId;
    }

    public void setAuthUserId(String authUserId) {
        this.authUserId = authUserId;
    }

    public String getAuthUserName() {
        return authUserName;
    }

    public void setAuthUserName(String authUserName) {
        this.authUserName = authUserName;
    }

    public String getSuiteId() {
        return suiteId;
    }

    public void setSuiteId(String suiteId) {
        this.suiteId = suiteId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
