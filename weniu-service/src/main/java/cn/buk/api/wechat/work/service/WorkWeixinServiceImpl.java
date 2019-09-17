package cn.buk.api.wechat.work.service;

import cn.buk.api.wechat.dto.JsSdkParam;
import cn.buk.api.wechat.entity.*;
import cn.buk.api.wechat.util.HttpUtil;
import cn.buk.api.wechat.util.SignUtil;
import cn.buk.api.wechat.work.dto.*;
import cn.buk.util.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.buk.api.wechat.entity.WeixinEntConfig.WORK_WX_DEFAULT;

public class WorkWeixinServiceImpl extends BaseService implements WorkWeixinService {

    private static Logger logger = Logger.getLogger(WorkWeixinServiceImpl.class);

    /**
     * 是否在控制台输出
     */
    private final boolean outputJson;

    public WorkWeixinServiceImpl() {
        this.outputJson = false;
    }

    public WorkWeixinServiceImpl(boolean outputJson) {
        this.outputJson = outputJson;
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

        String sEchoStr = wxcpt.VerifyURL(signature, timestamp, nonce, msg_encrypt);
        return sEchoStr;
    }

    public UserInfoResponse getUserInfo(int enterpriseId, String code) {
        Token token = getToken(enterpriseId, false);
        String url = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?"; //?=" + token.getAccess_token() + "&code=" + code;

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("access_token", token.getAccess_token()));
        params.add(new BasicNameValuePair("code", code));

        String jsonStr = HttpUtil.getUrl(url, params);

        if (this.outputJson) {
            System.out.println(jsonStr);
        }

        return JSON.parseObject(jsonStr, UserInfoResponse.class);
    }

    public UserDetailResponse getUserDetail(int enterpriseId, String userTicket) {
        Token token = getToken(enterpriseId, false);
        String url = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserdetail?access_token=" + token.getAccess_token();

        Map<String, Object> map = new HashMap<>();
        map.put("user_ticket", userTicket);

        String jsonBody = new JSONObject(map).toJSONString();

        String jsonStr = HttpUtil.postUrl(url, jsonBody);
        if (this.outputJson) {
            System.out.println(jsonStr);
        }

        return JSON.parseObject(jsonStr, UserDetailResponse.class);
    }

    /**
     * 获取jsapi_ticket
     */
    public JsSdkParam getJsSdkConfig(final int enterpriseId, String jsapi_url) {
        JsSdkParam jsapiParam = new JsSdkParam();

        WeixinEntConfig entConfig = weixinDao.getWeixinEntConfig(enterpriseId, WORK_WX_DEFAULT);
        jsapiParam.setAppId(entConfig.getCorpId());

        Token ticket = getJsSdkTicket(enterpriseId);

        // 3. 签名
        Map<String, String> ret = SignUtil.sign(ticket.getAccess_token(), jsapi_url);
        jsapiParam.setTimestamp(ret.get("timestamp"));
        jsapiParam.setNonceStr(ret.get("nonceStr"));
        jsapiParam.setSignature(ret.get("signature"));
        jsapiParam.setUrl(jsapi_url);

        return jsapiParam;
    }

    /**
     * 获取临时素材
     * @param mediaType
     * @param mediaId
     * @return 语音和图片素材返回下载到本地后的地址；视频文件返回URL
     */
    public String getMedia(int enterpriseId, String mediaType, String mediaId) {
        Token token = getToken(enterpriseId, false);
        final String url = "https://qyapi.weixin.qq.com/cgi-bin/media/get?" + "access_token=" + token.getAccess_token() + "&media_id=" + mediaId;



        if (mediaType.equalsIgnoreCase(WeixinMaterial.MATERIAL_VIDEO)) {
            return HttpUtil.getUrl(url, null);
        } else {
            return HttpUtil.download(url, null);
        }
    }


    /**
     * 获取js-sdk ticket, 可刷新
     */
    private synchronized Token getJsSdkTicket(int enterpriseId) {
        Token token = weixinDao.retrieveWeixinToken(enterpriseId, Token.WORK_WEIXIN_JSAPI_TICKET, 0);

        if (token == null || DateUtil.getPastSeconds(token.getCreateTime()) >= token.getExpires_in()) {
            token = refreshWeixinJsSdkTicket(enterpriseId);
        }

        return token;
    }

    /**
     * 获取js sdk需要的ticket
     */
    private Token refreshWeixinJsSdkTicket(int enterpriseId) {
        Token accessToken = getToken(enterpriseId, false);

        String url = "https://qyapi.weixin.qq.com/cgi-bin/get_jsapi_ticket?";

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("access_token", accessToken.getAccess_token()));

        String jsonStr = HttpUtil.getUrl(url, params);
        logger.info(jsonStr);

        //判断返回结果
        JSONObject param = (JSONObject) JSON.parse(jsonStr);

        Token ticket = new Token();
        ticket.setAccess_token((String) param.get("ticket"));
        ticket.setExpires_in((Integer) param.get("expires_in"));
        ticket.setWeixinType(Token.WORK_WEIXIN_JSAPI_TICKET);
        ticket.setWeixinId(enterpriseId);

        weixinDao.createWeixinToken(ticket);

        return ticket;
    }

    /**
     * 获取部门列表（通讯录权限）
     * @param enterpriseId
     * @return
     */
    public ListDepartmentResponse listDepartment(int enterpriseId) {
        return this.listDepartment(enterpriseId, WeixinEntConfig.WORK_WX_CONTACTS);
    }

    /**
     * 获取部门列表（应用可见的）
     * @param enterpriseId
     * @param msgType
     * @return
     */
    public ListDepartmentResponse listDepartment(final int enterpriseId, final int msgType) {
        Token token = getToken(enterpriseId, msgType, false);
        final String url = "https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token=" + token.getAccess_token();

        String jsonStr = HttpUtil.getUrl(url, null);
        if (this.outputJson) {
            System.out.println(jsonStr);
        }

        return JSON.parseObject(jsonStr, ListDepartmentResponse.class);
    }

    @Override
    public CreateDepartmentResponse createDepartment(int enterpriseId, WwDepartment dept) {
        Token token = getToken(enterpriseId, WeixinEntConfig.WORK_WX_CONTACTS, false);

        final String url = "https://qyapi.weixin.qq.com/cgi-bin/department/create?access_token=" + token.getAccess_token();

        String jsonBody = JSON.toJSON(dept).toString();
        String jsonStr = HttpUtil.postUrl(url, jsonBody);
        if (this.outputJson) {
            System.out.println(jsonStr);
        }

        return JSON.parseObject(jsonStr, CreateDepartmentResponse.class);
    }

    @Override
    public BaseResponse updateDepartment(int enterpriseId, WwDepartment dept) {
        Token token = getToken(enterpriseId, WeixinEntConfig.WORK_WX_CONTACTS, false);

        final String url = "https://qyapi.weixin.qq.com/cgi-bin/department/update?access_token=" + token.getAccess_token();

        String jsonBody = JSON.toJSON(dept).toString();
        String jsonStr = HttpUtil.postUrl(url, jsonBody);
        if (this.outputJson) {
            System.out.println(jsonStr);
        }

        return JSON.parseObject(jsonStr, BaseResponse.class);
    }

    @Override
    public BaseResponse deleteDepartment(int enterpriseId, int id) {
        Token token = getToken(enterpriseId, WeixinEntConfig.WORK_WX_CONTACTS, false);

        final String url = "https://qyapi.weixin.qq.com/cgi-bin/department/delete?access_token=" + token.getAccess_token() + "&id=" + id;

        String jsonStr = HttpUtil.getUrl(url, null);
        if (this.outputJson) {
            System.out.println(jsonStr);
        }

        return JSON.parseObject(jsonStr, BaseResponse.class);
    }

    @Override
    public BaseResponse createUser(int enterpriseId, WwUser user) {
        Token token = getToken(enterpriseId, WeixinEntConfig.WORK_WX_CONTACTS, false);

        final String url = "https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token=" + token.getAccess_token();

        String jsonBody = JSON.toJSON(user).toString();
        String jsonStr = HttpUtil.postUrl(url, jsonBody);
        if (this.outputJson) {
            System.out.println(jsonStr);
        }

        return JSON.parseObject(jsonStr, BaseResponse.class);
    }

    @Override
    public BaseResponse updateUser(int enterpriseId, WwUser user) {
        Token token = getToken(enterpriseId, WeixinEntConfig.WORK_WX_CONTACTS, false);

        final String url = "https://qyapi.weixin.qq.com/cgi-bin/user/update?access_token=" + token.getAccess_token();

        String jsonBody = JSON.toJSON(user).toString();
        String jsonStr = HttpUtil.postUrl(url, jsonBody);
        if (this.outputJson) {
            System.out.println(jsonStr);
        }

        return JSON.parseObject(jsonStr, BaseResponse.class);
    }

    @Override
    public WwUser getUser(int enterpriseId, String userId) {
        Token token = getToken(enterpriseId, WeixinEntConfig.WORK_WX_CONTACTS, false);

        final String url = "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=" + token.getAccess_token() + "&userid=" + userId;

        String jsonStr = HttpUtil.getUrl(url, null);
        if (this.outputJson) {
            System.out.println(jsonStr);
        }

        return JSON.parseObject(jsonStr, WwUser.class);
    }

    @Override
    public BaseResponse deleteUser(int enterpriseId, String userId) {
        Token token = getToken(enterpriseId, WeixinEntConfig.WORK_WX_CONTACTS, false);

        final String url = "https://qyapi.weixin.qq.com/cgi-bin/user/delete?access_token=" + token.getAccess_token() + "&userid=" + userId;

        String jsonStr = HttpUtil.getUrl(url, null);
        if (this.outputJson) {
            System.out.println(jsonStr);
        }

        return JSON.parseObject(jsonStr, BaseResponse.class);
    }

    @Override
    public ListUserResponse listUser(int enterpriseId, int deptId) {
        Token token = getToken(enterpriseId, WeixinEntConfig.WORK_WX_CONTACTS, false);

        final String url = "https://qyapi.weixin.qq.com/cgi-bin/user/list?access_token=" + token.getAccess_token() + "&department_id=" + deptId;

        String jsonStr = HttpUtil.getUrl(url, null);
        if (this.outputJson) {
            System.out.println(jsonStr);
        }

        return JSON.parseObject(jsonStr, ListUserResponse.class);
    }

    @Override
    public BaseResponse authUserSuccessfully(int enterpriseId, String userId) {
        Token token = getToken(enterpriseId, WeixinEntConfig.WORK_WX_CONTACTS, false);
        final String url = "https://qyapi.weixin.qq.com/cgi-bin/user/authsucc?access_token=" + token.getAccess_token() + "&userId=" + userId;
        String jsonStr = HttpUtil.getUrl(url, null);
        if (this.outputJson) {
            System.out.println(jsonStr);
        }

        return JSON.parseObject(jsonStr, BaseResponse.class);
    }

    @Override
    public String getExternalContact(final int enterpriseId, final String code) {
        Token token = getToken(enterpriseId, WeixinEntConfig.WORK_WX_EXTERNAL_CONTACTS, false);

        final String url = "https://qyapi.weixin.qq.com/cgi-bin/crm/get_external_contact?access_token=" + token.getAccess_token() + "&code=" + code;

        return HttpUtil.getUrl(url, null);
    }

    @Override
    public int saveToken(int enterpriseId, String accessToken, int expiresIn) {
        Token token = new Token();
        token.setAccess_token(accessToken);
        token.setExpires_in(expiresIn);
        token.setWeixinId(enterpriseId);
        token.setWeixinType(Token.WORK_WEIXIN_TOKEN);
        token.setMsgType(WORK_WX_DEFAULT);

        return weixinDao.createWeixinToken(token);
    }

}
