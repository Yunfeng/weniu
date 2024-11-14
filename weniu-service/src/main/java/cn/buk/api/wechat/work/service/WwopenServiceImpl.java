package cn.buk.api.wechat.work.service;

import cn.buk.api.wechat.entity.*;
import cn.buk.api.wechat.work.dto.*;
import cn.buk.common.util.DateUtil;
import cn.buk.common.util.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WwopenServiceImpl extends BaseService implements WwopenService {

    private static final Logger logger = LogManager.getLogger(WwopenServiceImpl.class);

//    @Autowired
//    private WeixinDao weixinDao;

    /**
     * 获取服务商的token
     * @param enterpriseId
     * @return
     */
    private Token getProviderToken(final int enterpriseId) {
        return getProviderToken(enterpriseId, false);
    }

    /**
     * 服务商的token
     *
     * 以corpid、provider_secret（获取方法为：登录服务商管理后台->标准应用服务->通用开发参数，可以看到）换取provider_access_token，代表的是服务商的身份，而与应用无关。请求单点登录、注册定制化等接口需要用到该凭证。接口详情如下：
     *
     * 请求方式：POST（HTTPS）
     * 请求地址： https://qyapi.weixin.qq.com/cgi-bin/service/get_provider_token
     * @param enterpriseId
     * @param forced
     * @return
     */
    private Token getProviderToken(final int enterpriseId, boolean forced) {
        final int msgType = WeixinEntConfig.WORK_WX_PROVIDER_COMMON;
        final int weixinType = Token.WORK_WEIXIN_PROVIDER;

        WeixinEntConfig entConfig = weixinDao.getWeixinEntConfig(enterpriseId, msgType);

        Token token = weixinDao.retrieveWeixinToken(enterpriseId, weixinType, msgType);
        long pastSeconds = 0;
        if (token != null) {
            pastSeconds = DateUtil.getPastSeconds(token.getCreateTime());
        }

        if (forced || token == null || pastSeconds >= token.getExpires_in()) {
            //去获取新token
            final String url = "https://qyapi.weixin.qq.com/cgi-bin/service/get_provider_token";

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("corpid", entConfig.getCorpId());
            jsonObject.put("provider_secret", entConfig.getSecret());

            String jsonStr = jsonObject.toJSONString();
            logger.debug(jsonStr);

            jsonStr = HttpUtil.postUrl(url, jsonStr);
            logger.debug(jsonStr);

            //判断返回结果
            JSONObject param = JSONObject.parseObject(jsonStr);

            token = new Token();
            token.setAccess_token((String) param.get("provider_access_token"));
            token.setExpires_in((Integer) param.get("expires_in"));
            token.setEnterpriseId(enterpriseId);
            token.setWeixinType(weixinType);
            token.setMsgType(msgType);

            weixinDao.createWeixinToken(token);
        }

        return token;
    }

    /**
     * 获取企业微信第三方应用凭证
     * @param enterpriseId
     */
    private WwProviderToken getSuiteAccessToken(final int enterpriseId) {
        WeixinEntConfig cfg = weixinDao.getWeixinEntConfig(enterpriseId, WeixinEntConfig.WORK_WX_PROVIDER_APP_SUITE_ID);

        WwProviderToken token = weixinDao.retrieveWwProviderToken(enterpriseId, cfg.getCorpId());
        long pastSeconds = 0;
        if (token != null) {
            pastSeconds = DateUtil.getPastSeconds(token.getLastUpdate());
            if (pastSeconds < token.getExpiresIn()) return token;
        }


        WwProviderTicket ticket = weixinDao.getSuiteTicket(enterpriseId, cfg.getCorpId());

        final String suiteId = cfg.getCorpId();

        //去获取新token
        final String url = "https://qyapi.weixin.qq.com/cgi-bin/service/get_suite_token";

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("suite_id", suiteId);
        jsonObject.put("suite_secret", cfg.getSecret());
        jsonObject.put("suite_ticket", ticket.getSuiteTicket());

        String strJson = jsonObject.toJSONString();
        logger.info(strJson);

        String jsonStr = HttpUtil.postUrl(url, strJson);
        logger.info(jsonStr);

        //判断返回结果
        JSONObject param = JSONObject.parseObject(jsonStr);

//        {
//            "errcode":0 ,
//                "errmsg":"ok" ,
//                "suite_access_token":"61W3mEpU66027wgNZ_MhGHNQDHnFATkDa9-2llMBjUwxRSNPbVsMmyD-yq8wZETSoE5NQgecigDrSHkPtIYA",
//                "expires_in":7200
//        }

//        token = new Token();
        String suiteAccessToken = (String) param.get("suite_access_token");
        String errmsg = (String) param.get("errmsg");

        Object obj = param.get("expires_in");
        int expiresIn = obj == null ? 0 : (int)obj;

        obj = param.get("errcode");
        int errcode = obj == null ? 0 : (int)obj;

        if (errcode == 0) {
            weixinDao.saveWwProviderToken(enterpriseId, suiteId, suiteAccessToken, expiresIn);
        } else {
            logger.error(errmsg);
        }

        token = weixinDao.retrieveWwProviderToken(enterpriseId, cfg.getCorpId());
        return token;
    }


    @Override
    public String verifyWorkWeixinSource(final int enterpriseId, final int msgType, final String corpId, final String signature, final String timestamp,
                                         final String nonce, final String msg_encrypt) throws Exception {
        WeixinEntConfig entConfig = weixinDao.getWeixinEntConfig(enterpriseId, msgType);

        String corpId1 = corpId == null ? entConfig.getCorpId() : corpId;

        WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(entConfig.getToken(), entConfig.getEncodingAESKey(), corpId1);

        return wxcpt.VerifyURL(signature, timestamp, nonce, msg_encrypt);
    }



    @Override
    public int saveSuiteTicket(int enterpriseId, String suiteId, String suiteTicket, Date timeStamp) {
        return weixinDao.saveSuiteTicket(enterpriseId, suiteId, suiteTicket, timeStamp);
    }

    /**
     * 获取应用的预授权码
     * @param enterpriseId
     * @return
     */
    public WwpPreAuthCode getSuitePreAuthCode(int enterpriseId) {
        WwProviderToken token = getSuiteAccessToken(enterpriseId);

        final String url = "https://qyapi.weixin.qq.com/cgi-bin/service/get_pre_auth_code?suite_access_token=" + token.getAccessToken();
        String jsonStr = HttpUtil.getUrl(url, null);

        logger.info(jsonStr);

        return JSON.parseObject(jsonStr, WwpPreAuthCode.class);
    }

    /**
     * 注册定制化: 获取注册码
     * 该API用于根据注册模板生成注册码（register_code）。
     * @param enterpriseId
     * @param templateId 注册模板id，最长为128个字节
     * @return
     */
    public WwpRegisterCode getSuiteRegisterCode(final int enterpriseId, final String templateId) {
        Token token = getProviderToken(enterpriseId);

        final String requestUrl = "https://qyapi.weixin.qq.com/cgi-bin/service/get_register_code?provider_access_token=" + token.getAccess_token();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("template_id", templateId);

        String jsonStr = HttpUtil.postUrl(requestUrl, jsonObject.toJSONString());
        logger.info(jsonStr);

        return JSON.parseObject(jsonStr, WwpRegisterCode.class);
    }

    /**
     * 获取永久授权码
     * @param enterpriseId 企业
     * @param authCode 临时授权码，会在授权成功时附加在redirect_uri中跳转回第三方服务商网站，或通过回调推送给服务商。长度为64至512个字节
     * @return
     */
    public WwProviderAuthCorpInfo createSuiteAuthInfo(final int enterpriseId, final String suitedId, final String authCode) {
        WwProviderToken token = getSuiteAccessToken(enterpriseId);

        final String url = "https://qyapi.weixin.qq.com/cgi-bin/service/get_permanent_code?suite_access_token=" + token.getAccessToken();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("auth_code", authCode);

        String jsonStr = HttpUtil.postUrl(url, jsonObject.toJSONString());

        //判断返回结果
        WwpPermanentCorpAuthInfo info = JSON.parseObject(jsonStr, WwpPermanentCorpAuthInfo.class);

        WwProviderAuthCorpInfo corpInfo = new WwProviderAuthCorpInfo();
        corpInfo.setEnterpriseId(enterpriseId);
        corpInfo.setSuiteId(suitedId);
        corpInfo.setStatus(1);

        corpInfo.setAccessToken(info.getAccess_token());
        corpInfo.setExpiresIn(info.getExpires_in());
        corpInfo.setPermanentCode(info.getPermanent_code());

        corpInfo.setCorpId(info.getAuth_corp_info().getCorpid());
        corpInfo.setCorpName(info.getAuth_corp_info().getCorp_name());
        corpInfo.setCorpFullName(info.getAuth_corp_info().getCorp_full_name());

        corpInfo.setAuthUserId(info.getAuth_user_info().getUserid());
        corpInfo.setAuthUserName(info.getAuth_user_info().getName());

        int retCode = weixinDao.saveWwpAuthCorpInfo(corpInfo);
        return retCode > 0 ? corpInfo : null;
    }

    /**
     * 获取企业的授权信息（企业，永久授权码）
     * @param enterpriseId
     * @param authCorpId
     * @return
     */
    public WwpPermanentCorpAuthInfo getSuiteAuthInfo(int enterpriseId, String suiteId, String authCorpId) {
//        该API用于通过永久授权码换取企业微信的授权信息。 永久code的获取，是通过临时授权码使用get_permanent_code 接口获取到的permanent_code。
//        请求方式：POST（HTTPS）
//        请求地址： https://qyapi.weixin.qq.com/cgi-bin/service/get_auth_info?suite_access_token=SUITE_ACCESS_TOKEN
//        请求包体：
//        {
//            "auth_corpid": "auth_corpid_value", 授权方corpid
//                "permanent_code": "code_value" 永久授权码，通过get_permanent_code获取
//        }
        WwProviderAuthCorpInfo corpInfo = weixinDao.getWwpAuthCorpInfo(enterpriseId, suiteId, authCorpId);

        WwProviderToken token = getSuiteAccessToken(enterpriseId);

        final String url = "https://qyapi.weixin.qq.com/cgi-bin/service/get_auth_info?suite_access_token=" + token.getAccessToken();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("auth_corpid", authCorpId);
        jsonObject.put("permanent_code", corpInfo.getPermanentCode());

        String jsonStr = HttpUtil.postUrl(url, jsonObject.toJSONString());

        //判断返回结果
        return JSON.parseObject(jsonStr, WwpPermanentCorpAuthInfo.class);
    }


    @Override
    public int cancelSuiteAuthInfo(int enterpriseId, String suiteId, String authCorpId) {
        return weixinDao.cancelSuiteAuthInfo(enterpriseId, suiteId, authCorpId);
    }

    /**
     * 获取授权企业的access_token
     * @param enterpriseId 企业ID（企业微信服务商)
     * @param authCorpId 授权企业的企业id
     * @return
     */
    public WwpPermanentCorpAuthInfo getSuiteAuthCorpAccessToken(int enterpriseId, String authCorpId) {
        WeixinEntConfig config = weixinDao.getWeixinEntConfig(enterpriseId, WeixinEntConfig.WORK_WX_PROVIDER_APP_SUITE_ID);

        final String suitId = config.getCorpId();

        return getSuiteAuthCorpAccessToken(enterpriseId, suitId, authCorpId);
    }

    /**
     * 获取企业access_token
     */
    public WwpPermanentCorpAuthInfo getSuiteAuthCorpAccessToken(int enterpriseId, String suiteId, String authCorpId) {
//        第三方服务商在取得企业的永久授权码后，通过此接口可以获取到企业的access_token。
//        获取后可通过通讯录、应用、消息等企业接口来运营这些应用。
//        此处获得的企业access_token与企业获取access_token拿到的token，本质上是一样的，只不过获取方式不同。获取之后，就跟普通企业一样使用token调用API接口
//        调用企业接口所需的access_token获取方法如下。
//
//        请求方式：POST（HTTPS）
//        请求地址： https://qyapi.weixin.qq.com/cgi-bin/service/get_corp_token?suite_access_token=SUITE_ACCESS_TOKEN
//        请求包体：
//        {
//            "auth_corpid": "auth_corpid_value",
//                "permanent_code": "code_value"
//        }
//
//        参数说明：
//        参数 	是否必须 	说明
//        auth_corpid 	是 	授权方corpid
//        permanent_code 	是 	永久授权码，通过get_permanent_code获取
//
//        返回结果：
//        {
//            "errcode":0 ,
//                "errmsg":"ok" ,
//                "access_token": "xxxxxx",
//                "expires_in": 7200
//        }
//
//        参数说明：
//        参数 	说明
//        access_token 	授权方（企业）access_token,最长为512字节
//        expires_in 	授权方（企业）access_token超时时间
        WwProviderAuthCorpInfo corpInfo = weixinDao.getWwpAuthCorpInfo(enterpriseId, suiteId, authCorpId);

        WwProviderToken token = getSuiteAccessToken(enterpriseId);

        final String url = "https://qyapi.weixin.qq.com/cgi-bin/service/get_corp_token?suite_access_token=" + token.getAccessToken();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("auth_corpid", authCorpId);
        jsonObject.put("permanent_code", corpInfo.getPermanentCode());

        String jsonStr = HttpUtil.postUrl(url, jsonObject.toJSONString());


        //判断返回结果
        WwpPermanentCorpAuthInfo info = JSON.parseObject(jsonStr, WwpPermanentCorpAuthInfo.class);
        int retCode = weixinDao.updateWwpAuthCorpAccessToken(enterpriseId, authCorpId, info.getAccess_token(), info.getExpires_in());

        return info;
    }

    /**
     * 获取登录用户信息
     * @param enterpriseId 企业ID
     * @param authCode oauth2.0授权企业微信管理员登录产生的code,最长512字节。只能使用一次，5分钟未使用自动过期
     * @return
     */
    public WwpLoginInfo getLoginInfo(int enterpriseId, String authCode) {
//        https://qyapi.weixin.qq.com/cgi-bin/service/get_login_info?access_token=PROVIDER_ACCESS_TOKEN
//        {
//            "auth_code":"xxxxx"
//        }
        Token token = getProviderToken(enterpriseId);

        final String url = "https://qyapi.weixin.qq.com/cgi-bin/service/get_login_info?access_token=" + token.getAccess_token();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("auth_code", authCode);

        String jsonStr = HttpUtil.postUrl(url, jsonObject.toJSONString());

        logger.info(jsonStr);


        //判断返回结果

        return JSON.parseObject(jsonStr, WwpLoginInfo.class);
    }

    @Override
    public UserInfoResponse getUserInfo3rd(int enterpriseId, String code) {
//        第三方根据code获取企业成员信息
//
//        请求方式：GET（HTTPS）
//        请求地址：https://qyapi.weixin.qq.com/cgi-bin/service/getuserinfo3rd?access_token=SUITE_ACCESS_TOKEN&code=CODE
        WwProviderToken token = getSuiteAccessToken(enterpriseId);

        final String url = "https://qyapi.weixin.qq.com/cgi-bin/service/getuserinfo3rd?";

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("access_token", token.getAccessToken()));
        params.add(new BasicNameValuePair("code", code));

        String jsonStr = HttpUtil.getUrl(url, params);


        logger.info(jsonStr);


        //判断返回结果

        return JSON.parseObject(jsonStr, UserInfoResponse.class);
    }

    @Override
    public UserDetailResponse getUserDetail3rd(int enterpriseId, String userTicket) {
//        第三方使用user_ticket获取成员详情
//
//        请求方式：POST（HTTPS）
//        请求地址：https://qyapi.weixin.qq.com/cgi-bin/service/getuserdetail3rd?access_token=SUITE_ACCESS_TOKEN
        WwProviderToken token = getSuiteAccessToken(enterpriseId);

        final String url = "https://qyapi.weixin.qq.com/cgi-bin/service/getuserdetail3rd?access_token=" + token.getAccessToken();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user_ticket", userTicket);

        String jsonStr = HttpUtil.postUrl(url, jsonObject.toJSONString());
        logger.info(jsonStr);


        //判断返回结果

        return JSON.parseObject(jsonStr, UserDetailResponse.class);
    }

    @Override
    public WeixinEntConfig getWeixinEntConfig(int enterpriseId, int msgType) {
        return weixinDao.getWeixinEntConfig(enterpriseId, msgType);
    }

    @Override
    public UserInfoResponse getUserInfo(int enterpriseId, String code) {


        Token token = getToken(enterpriseId, false);
        String url = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?"; //?=" + token.getAccess_token() + "&code=" + code;

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("access_token", token.getAccess_token()));
        params.add(new BasicNameValuePair("code", code));

        String jsonStr = HttpUtil.getUrl(url, params);

        logger.info(jsonStr);

        return JSON.parseObject(jsonStr, UserInfoResponse.class);
    }

}
