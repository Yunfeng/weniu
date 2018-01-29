package cn.buk.api.wechat.work.service;

import cn.buk.api.wechat.dao.WeixinDao;
import cn.buk.api.wechat.dto.JsSdkParam;
import cn.buk.api.wechat.entity.Token;
import cn.buk.api.wechat.entity.WeixinEntConfig;
import cn.buk.api.wechat.entity.WeixinMaterial;
import cn.buk.api.wechat.util.HttpUtil;
import cn.buk.api.wechat.util.SignUtil;
import cn.buk.api.wechat.work.dto.*;
import cn.buk.util.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkWeixinServiceImpl implements WorkWeixinService {

    private static Logger logger = Logger.getLogger(WorkWeixinServiceImpl.class);

    @Autowired
    private WeixinDao weixinDao;

    private Token getToken(int enterpriseId, boolean forced) {
        return getToken(enterpriseId, WeixinEntConfig.WORK_WX_DEFAULT, forced);
    }

    private Token getToken(final int enterpriseId, final int msgType, boolean forced) {
        WeixinEntConfig entConfig = weixinDao.getWeixinEntConfig(enterpriseId, msgType);

        Token token = weixinDao.retrieveWeixinToken(enterpriseId, Token.WORK_WEIXIN_TOKEN, msgType);
        long pastSeconds = 0;
        if (token != null) {
            pastSeconds = DateUtil.getPastSeconds(token.getCreateTime());
        }

        if (forced || token == null || pastSeconds >= token.getExpires_in()) {
            //去获取新token
            //https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
            String url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?";

            List<NameValuePair> params = new ArrayList<>();

            params.add(new BasicNameValuePair("corpid", entConfig.getCorpId()));
            params.add(new BasicNameValuePair("corpsecret", entConfig.getSecret()));

            String jsonStr = HttpUtil.getUrl(url, params);

            //判断返回结果
            JSONObject param = JSONObject.parseObject(jsonStr);

            token = new Token();
            token.setAccess_token((String) param.get("access_token"));
            token.setExpires_in((Integer) param.get("expires_in"));
            token.setWeixinId(enterpriseId);
            token.setWeixinType(Token.WORK_WEIXIN_TOKEN);
            token.setMsgType(msgType);

            weixinDao.createWeixinToken(token);
        }

        return token;
    }


    public UserInfoResponse getUserInfo(int enterpriseId, String code) {
        Token token = getToken(enterpriseId, false);
        String url = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?"; //?=" + token.getAccess_token() + "&code=" + code;

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("access_token", token.getAccess_token()));
        params.add(new BasicNameValuePair("code", code));

        String jsonStr = HttpUtil.getUrl(url, params);

        logger.debug(jsonStr);

        return JSON.parseObject(jsonStr, UserInfoResponse.class);
    }

    public UserDetailResponse getUserDetail(int enterpriseId, String userTicket) {
        Token token = getToken(enterpriseId, false);
        String url = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserdetail?access_token=" + token.getAccess_token();

        Map<String, Object> map = new HashMap<>();
        map.put("user_ticket", userTicket);

        String jsonBody = new JSONObject(map).toJSONString();

        String jsonStr = HttpUtil.postUrl(url, jsonBody);
        logger.debug(jsonStr);

        return JSON.parseObject(jsonStr, UserDetailResponse.class);
    }

    /**
     * 获取jsapi_ticket
     */
    public JsSdkParam getJsSdkConfig(final int enterpriseId, String jsapi_url) {
        JsSdkParam jsapiParam = new JsSdkParam();

        WeixinEntConfig entConfig = weixinDao.getWeixinEntConfig(enterpriseId, WeixinEntConfig.WORK_WX_DEFAULT);
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
    @Override
    public ListDepartmentResponse listDepartment(int enterpriseId) {
        Token token = getToken(enterpriseId, WeixinEntConfig.WORK_WX_CONTACTS, false);
        final String url = "https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token=" + token.getAccess_token();

        String jsonStr = HttpUtil.getUrl(url, null);

        return JSON.parseObject(jsonStr, ListDepartmentResponse.class);
    }

    @Override
    public CreateDepartmentResponse createDepartment(int enterpriseId, WwDepartment dept) {
        Token token = getToken(enterpriseId, WeixinEntConfig.WORK_WX_CONTACTS, false);

        final String url = "https://qyapi.weixin.qq.com/cgi-bin/department/create?access_token=" + token.getAccess_token();

        String jsonBody = JSON.toJSON(dept).toString();
        String jsonStr = HttpUtil.postUrl(url, jsonBody);

        return JSON.parseObject(jsonStr, CreateDepartmentResponse.class);
    }

    @Override
    public BaseResponse updateDepartment(int enterpriseId, WwDepartment dept) {
        Token token = getToken(enterpriseId, WeixinEntConfig.WORK_WX_CONTACTS, false);

        final String url = "https://qyapi.weixin.qq.com/cgi-bin/department/update?access_token=" + token.getAccess_token();

        String jsonBody = JSON.toJSON(dept).toString();
        String jsonStr = HttpUtil.postUrl(url, jsonBody);

        return JSON.parseObject(jsonStr, BaseResponse.class);
    }

    @Override
    public BaseResponse deleteDepartment(int enterpriseId, int id) {
        Token token = getToken(enterpriseId, WeixinEntConfig.WORK_WX_CONTACTS, false);

        final String url = "https://qyapi.weixin.qq.com/cgi-bin/department/delete?access_token=" + token.getAccess_token() + "&id=" + id;

        String jsonStr = HttpUtil.getUrl(url, null);

        return JSON.parseObject(jsonStr, BaseResponse.class);
    }

    @Override
    public BaseResponse createUser(int enterpriseId, WwUser user) {
        Token token = getToken(enterpriseId, WeixinEntConfig.WORK_WX_CONTACTS, false);

        final String url = "https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token=" + token.getAccess_token();

        String jsonBody = JSON.toJSON(user).toString();
        String jsonStr = HttpUtil.postUrl(url, jsonBody);

        return JSON.parseObject(jsonStr, BaseResponse.class);
    }

    @Override
    public BaseResponse updateUser(int enterpriseId, WwUser user) {
        Token token = getToken(enterpriseId, WeixinEntConfig.WORK_WX_CONTACTS, false);

        final String url = "https://qyapi.weixin.qq.com/cgi-bin/user/update?access_token=" + token.getAccess_token();

        String jsonBody = JSON.toJSON(user).toString();
        String jsonStr = HttpUtil.postUrl(url, jsonBody);

        return JSON.parseObject(jsonStr, BaseResponse.class);
    }

    @Override
    public WwUser getUser(int enterpriseId, String userId) {
        Token token = getToken(enterpriseId, WeixinEntConfig.WORK_WX_CONTACTS, false);

        final String url = "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=" + token.getAccess_token() + "&userid=" + userId;

        String jsonStr = HttpUtil.getUrl(url, null);

        return JSON.parseObject(jsonStr, WwUser.class);
    }

    @Override
    public BaseResponse deleteUser(int enterpriseId, String userId) {
        Token token = getToken(enterpriseId, WeixinEntConfig.WORK_WX_CONTACTS, false);

        final String url = "https://qyapi.weixin.qq.com/cgi-bin/user/delete?access_token=" + token.getAccess_token() + "&userid=" + userId;

        String jsonStr = HttpUtil.getUrl(url, null);

        return JSON.parseObject(jsonStr, BaseResponse.class);
    }

    @Override
    public ListUserResponse listUser(int enterpriseId, int deptId) {
        Token token = getToken(enterpriseId, WeixinEntConfig.WORK_WX_CONTACTS, false);

        final String url = "https://qyapi.weixin.qq.com/cgi-bin/user/list?access_token=" + token.getAccess_token() + "&department_id=" + deptId;

        String jsonStr = HttpUtil.getUrl(url, null);

        return JSON.parseObject(jsonStr, ListUserResponse.class);
    }

    @Override
    public BaseResponse authUserSuccessfully(int enterpriseId, String userId) {
        Token token = getToken(enterpriseId, WeixinEntConfig.WORK_WX_CONTACTS, false);
        final String url = "https://qyapi.weixin.qq.com/cgi-bin/user/authsucc?access_token=" + token.getAccess_token() + "&userId=" + userId;
        String jsonStr = HttpUtil.getUrl(url, null);

        return JSON.parseObject(jsonStr, BaseResponse.class);
    }

}
