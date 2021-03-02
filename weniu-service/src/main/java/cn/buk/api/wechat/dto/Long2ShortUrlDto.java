package cn.buk.api.wechat.dto;

import com.alibaba.fastjson.annotation.JSONField;

public class Long2ShortUrlDto extends BaseResponse {

//  {"errcode":0,"errmsg":"ok","short_url":"http:\/\/w.url.cn\/s\/AvCo6Ih"}

  @JSONField(name = "short_url")
  private String shortUrl;

  public String getShortUrl() {
    return shortUrl;
  }

  public void setShortUrl(String shortUrl) {
    this.shortUrl = shortUrl;
  }
}
