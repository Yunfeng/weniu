package cn.buk.api.wechat.work.service;

import cn.buk.api.wechat.dao.WeixinDao;
import cn.buk.api.wechat.entity.Token;
import cn.buk.api.wechat.entity.WeixinEntConfig;
import cn.buk.api.wechat.util.HttpUtil;
import cn.buk.util.DateUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static cn.buk.api.wechat.entity.WeixinEntConfig.WORK_WX_DEFAULT;

/**
 * @author yfdai
 */
public class BaseService {

    private static Logger logger = Logger.getLogger(BaseService.class);

    @Autowired
    protected WeixinDao weixinDao;


    protected Token getToken(int enterpriseId, boolean forced) {
        return getToken(enterpriseId, WORK_WX_DEFAULT, forced);
    }

    protected Token getToken(final int enterpriseId, final int msgType, boolean forced) {
        WeixinEntConfig entConfig = weixinDao.getWeixinEntConfig(enterpriseId, msgType);
        if (entConfig == null) {
            logger.error("No WeixinEntConfig: " + enterpriseId + ", " + msgType);
        }

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

            System.out.println(jsonStr);

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

}
