package cn.buk.api.wechat.work.dto;

import cn.buk.api.wechat.dto.BaseResponse;

/**
 * wwp - work weixin provider 企业微信服务商
 * 获取企业永久授权码
 */
public class WwpPermanentCorpAuthInfo extends BaseResponse {

    private String access_token; //授权方（企业）access_token,最长为512字节
    private int expires_in;  //授权方（企业）access_token超时时间
    private String permanent_code; //企业微信永久授权码,最长为512字节

    private WwpAuthCorpInfo auth_corp_info;
    private WwpAuthInfo auth_info;
    private WwpAuthUserInfo auth_user_info;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getPermanent_code() {
        return permanent_code;
    }

    public void setPermanent_code(String permanent_code) {
        this.permanent_code = permanent_code;
    }

    public WwpAuthCorpInfo getAuth_corp_info() {
        return auth_corp_info;
    }

    public void setAuth_corp_info(WwpAuthCorpInfo auth_corp_info) {
        this.auth_corp_info = auth_corp_info;
    }

    public WwpAuthUserInfo getAuth_user_info() {
        return auth_user_info;
    }

    public void setAuth_user_info(WwpAuthUserInfo auth_user_info) {
        this.auth_user_info = auth_user_info;
    }

    public WwpAuthInfo getAuth_info() {
        return auth_info;
    }

    public void setAuth_info(WwpAuthInfo auth_info) {
        this.auth_info = auth_info;
    }
}
