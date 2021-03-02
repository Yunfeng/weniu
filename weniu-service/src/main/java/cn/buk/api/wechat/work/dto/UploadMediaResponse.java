package cn.buk.api.wechat.work.dto;

import cn.buk.api.wechat.dto.BaseResponse;
import com.alibaba.fastjson.annotation.JSONField;

public class UploadMediaResponse extends BaseResponse  {
//  type 	媒体文件类型，分别有图片（image）、语音（voice）、视频（video），普通文件(file)
  @JSONField(name = "type")
  private String mediaType;

//  media_id 	媒体文件上传后获取的唯一标识，3天内有效
  @JSONField(name = "media_id")
  private String mediaId;

//  created_at 	媒体文件上传时间戳
  @JSONField(name = "created_at")
  private long createdAt;

  public String getMediaType() {
    return mediaType;
  }

  public void setMediaType(String mediaType) {
    this.mediaType = mediaType;
  }

  public String getMediaId() {
    return mediaId;
  }

  public void setMediaId(String mediaId) {
    this.mediaId = mediaId;
  }

  public long getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(long createdAt) {
    this.createdAt = createdAt;
  }
}
