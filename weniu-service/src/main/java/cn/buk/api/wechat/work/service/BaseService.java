package cn.buk.api.wechat.work.service;

import cn.buk.api.wechat.dao.WeixinDao;
import cn.buk.api.wechat.entity.Token;
import cn.buk.api.wechat.entity.WeixinEntConfig;
import cn.buk.api.wechat.work.dto.TokenResponse;
import cn.buk.common.util.DateUtil;
import cn.buk.common.util.HttpUtil;
import com.alibaba.fastjson.JSON;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static cn.buk.api.wechat.entity.WeixinEntConfig.WORK_WX_DEFAULT;

/**
 * @author yfdai
 */
public class BaseService {

    private static final Logger logger = LogManager.getLogger(BaseService.class);

    /**
     * 是否在控制台输出
     */
    protected boolean outputJson;

    @Autowired
    protected WeixinDao weixinDao;


    protected Token getToken(int enterpriseId) {
        return getToken(enterpriseId, false);
    }

    protected Token getToken(int enterpriseId, boolean forced) {
        return getToken(enterpriseId, WORK_WX_DEFAULT, forced);
    }

    protected Token getToken(final int enterpriseId, final int msgType, boolean forced) {
        WeixinEntConfig entConfig = weixinDao.getWeixinEntConfig(enterpriseId, msgType);
        if (entConfig == null) {
            logger.error("No WeixinEntConfig: " + enterpriseId + ", " + msgType);
            return null;
        }

        Token token = weixinDao.retrieveWeixinToken(enterpriseId, Token.WORK_WEIXIN_TOKEN, msgType);
        long pastSeconds = 0;
        if (token != null) {
            pastSeconds = DateUtil.getPastSeconds(token.getCreateTime());
        }

        if (forced || token == null || pastSeconds >= token.getExpires_in()) {
            //去获取新token
            final String url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?";

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("corpid", entConfig.getCorpId()));
            params.add(new BasicNameValuePair("corpsecret", entConfig.getSecret()));

            final String jsonStr = HttpUtil.getUrl(url, params);
            if (this.outputJson) {
                logger.info(jsonStr);
            }

            //判断返回结果
            var rs = JSON.parseObject(jsonStr, TokenResponse.class);
            if (rs.getErrcode() == 0) {

                token = new Token();
                token.setAccess_token(rs.getAccessToken());
                token.setExpires_in(rs.getExpiresIn());
                token.setEnterpriseId(enterpriseId);
                token.setWeixinType(Token.WORK_WEIXIN_TOKEN);
                token.setMsgType(msgType);

                weixinDao.createWeixinToken(token);
            }
        }

        return token;
    }

}
