package cn.buk.api.wechat.work.dto;

import cn.buk.api.wechat.dto.BaseResponse;

/**
 * wwp - work weixin provider 企业微信服务商
 * 获取预授权码
 */
public class WwpPreAuthCode extends BaseResponse {

    private String pre_auth_code; //预授权码,最长为512字节
    private int expires_in;  //有效期


    public String getPre_auth_code() {
        return pre_auth_code;
    }

    public void setPre_auth_code(String pre_auth_code) {
        this.pre_auth_code = pre_auth_code;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }
}
