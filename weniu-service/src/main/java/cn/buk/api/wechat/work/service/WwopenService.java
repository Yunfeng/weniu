package cn.buk.api.wechat.work.service;

import cn.buk.api.wechat.entity.WeixinEntConfig;
import cn.buk.api.wechat.entity.WwProviderAuthCorpInfo;
import cn.buk.api.wechat.work.dto.*;

import java.util.Date;

public interface WwopenService {


    /**
     * 校验消息签名，也就是校验来源
     * @param enterpriseId 企业ID
     * @param msgType 消息类型：默认应用，通讯录，外部联系人
     * @param corpId 实际使用的corpId
     * @param signature 签名
     * @param timestamp 时间戳
     * @param nonce 随机字符串
     * @param msg_encrypt 加密的消息
     * @return
     */
    String verifyWorkWeixinSource(int enterpriseId, int msgType, final String corpId, String signature, String timestamp, String nonce, String msg_encrypt) throws Exception;


    /**
     * 保存 suite_ticket
     */
    int saveSuiteTicket(int enterpriseId, String suiteId, String suiteTicket, Date timeStamp);


    /**
     *根据临时授权码，获取授权企业信息，以及永久授权码
     */
    WwProviderAuthCorpInfo createSuiteAuthInfo(int enterpriseId, String suiteId, String authCode);

    /**
     * 取消应用授权
     * @param enterpriseId
     * @param suiteId
     * @param authCorpId
     * @return
     */
    int cancelSuiteAuthInfo(int enterpriseId, String suiteId, String authCorpId);

    /**
     *获取企业授权信息
     */
    WwpPermanentCorpAuthInfo getSuiteAuthInfo(int enterpriseId, String suiteId, String authCorpId);


    /**
     * 获取授权企业的access_token
     * @param enterpriseId 企业ID（企业微信服务商)
     * @param authCorpId 授权企业的企业id
     * @return
     */
    WwpPermanentCorpAuthInfo getSuiteAuthCorpAccessToken(int enterpriseId, String authCorpId);

    /**
     * 获取授权企业的access_token
     * @param enterpriseId 企业ID（企业微信服务商)
     * @param suiteId 应用id
     * @param authCorpId 授权企业的企业id
     * @return
     */
    WwpPermanentCorpAuthInfo getSuiteAuthCorpAccessToken(int enterpriseId, String suiteId, String authCorpId);

    WwpLoginInfo getLoginInfo(int enterpriseId, String authCode);

    /**
     * 第三方根据code获取企业成员信息
     */
    UserInfoResponse getUserInfo3rd(int enterpriseId, String code);

    /**
     * 第三方使用user_ticket获取成员详情
     */
    UserDetailResponse getUserDetail3rd(int enterpriseId, String userTicket);

    WeixinEntConfig getWeixinEntConfig(int enterpriseId, int msgType);

    /**
     * 根据code获取成员信息(服务商获取授权企业成员的openid等信息）
     * @param code 成员授权获得的code，最大为512字节，只能用一次，5分钟有效期
     * @return
     */
    UserInfoResponse getUserInfo(int enterpriseId, String code);

    /**
     * 注册定制化: 获取应用的预授权码
     */
    WwpPreAuthCode getSuitePreAuthCode(int enterpriseId);

    /**
     * 注册定制化: 获取注册码
     * 该API用于根据注册模板生成注册码（register_code）。
     */
    WwpRegisterCode getSuiteRegisterCode(int enterpriseId, String templateId);

}
