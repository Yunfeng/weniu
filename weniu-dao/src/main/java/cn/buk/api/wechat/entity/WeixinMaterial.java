package cn.buk.api.wechat.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * 微信永久素材
 */
@Entity
@Table(name="weixin_material", uniqueConstraints = @UniqueConstraint(name = "uk_wx_material", columnNames = {"weixin_id", "media_id"}))
public class WeixinMaterial {

//    图片（image）: 2M，支持bmp/png/jpeg/jpg/gif格式
//
//    语音（voice）：2M，播放长度不超过60s，mp3/wma/wav/amr格式
//
//    视频（video）：10MB，支持MP4格式
//
//    缩略图（thumb）：64KB，支持JPG格式
    public static final String MATERIAL_IMAGE = "image";
    public static final String MATERIAL_VOICE = "voice";
    public static final String MATERIAL_VIDEO = "video";
    public static final String MATERIAL_THUMB = "thumb";
    public static final String MATERIAL_NEWS = "news";

//    public static final int MATERIAL_IMAGE_INT = 0;
//    public static final int MATERIAL_VOICE_INT = 1;
//    public static final int MATERIAL_VIDEO_INT = 2;
//    public static final int MATERIAL_THUMB_INT = 3;
//    public static final int MATERIAL_NEWS_INT = 4;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="enterprise_id")
    private int enterpriseId;

    /**
     * 素材类型
     */
    @Column(name = "m_type")
    private String materialType;

    /**
     * 图文素材仅有该项返回值。
     * 新增的永久素材的media_id
     */
    @Column(name = "media_id")
    private String mediaId;

    /**
     * 新增的图片素材的图片URL（仅新增图片素材时会返回该字段）
     */
    private String url;

    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(insertable = false, updatable = false)
    private Date createTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(insertable = false)
    private Date lastupdate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(Date lastupdate) {
        this.lastupdate = lastupdate;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(int enterpriseId) {
        this.enterpriseId = enterpriseId;
    }
}
