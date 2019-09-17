package cn.buk.api.wechat.work.dto;

/**
 * 获取登录用户信息
 * https://qyapi.weixin.qq.com/cgi-bin/service/get_login_info?access_token=PROVIDER_ACCESS_TOKEN
 * 返回的结果
 */
public class WwpLoginInfo extends BaseResponse {

    private int usertype;

    private WwpAuthUserInfo user_info;

    private WwpAuthCorpInfo corp_info;

    public int getUsertype() {
        return usertype;
    }

    public void setUsertype(int usertype) {
        this.usertype = usertype;
    }

    public WwpAuthUserInfo getUser_info() {
        return user_info;
    }

    public void setUser_info(WwpAuthUserInfo user_info) {
        this.user_info = user_info;
    }

    public WwpAuthCorpInfo getCorp_info() {
        return corp_info;
    }

    public void setCorp_info(WwpAuthCorpInfo corp_info) {
        this.corp_info = corp_info;
    }
}
