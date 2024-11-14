package cn.buk.api.wechat.work.dto;

import cn.buk.api.wechat.dto.BaseResponse;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author yfdai
 */
public class TokenResponse extends BaseResponse {

  /**
   * 获取到的凭证，最长为512字节
   */
  @JSONField(name = "access_token")
  private String accessToken;

  /**
   * 凭证的有效时间（秒）
   */
  @JSONField(name = "expires_in")
  private int expiresIn;


  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public int getExpiresIn() {
    return expiresIn;
  }

  public void setExpiresIn(int expiresIn) {
    this.expiresIn = expiresIn;
  }
}
