package cn.buk.api.wechat.work.service;

import cn.buk.api.wechat.entity.Token;
import cn.buk.api.wechat.entity.WeixinEntConfig;
import cn.buk.common.util.DateUtil;

import static cn.buk.api.wechat.entity.WeixinEntConfig.WORK_WX_DEFAULT;

public class BaseTest {
  protected WeixinEntConfig createConfig() {
    WeixinEntConfig config = new WeixinEntConfig();
    config.setEnterpriseId(1);
    config.setCorpId("wx92bd521d408c3cc2");
    //默认企业微信应用
    config.setMsgType(WORK_WX_DEFAULT);
    config.setAgentId(1);
    config.setSecret("Efw49qHFbqD4Ydah2OqxBw7PPsh-DZIy5CFDTQACb1I");

    return config;
  }

  protected Token createToken() {
    Token token = new Token();
    token.setAccess_token("F4N-FTGLX0zPhtDg4sIV59PPrBL_yStypnGm05qZhxZXehwMXRi_ADhuK85NPEmLE-WTnamOV-EVaZ288E4GqA40oJ708TVyoeCtFs71X92UnQqAwBo9hsBKgf-k-h4YilJknRqDI_s-s_f12APYD6INLD2bfTNvU_0vK4jpsQgr-NtV_3p6cubSuUpooyWyYK9i_ml7ZP6gZFBOzYjeyg");
    token.setExpires_in(7200);
    token.setCreateTime(DateUtil.getCurDateTime());
    return token;
  }
}
