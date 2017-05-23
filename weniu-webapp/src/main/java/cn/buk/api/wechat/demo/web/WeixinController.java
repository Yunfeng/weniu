package cn.buk.api.wechat.demo.web;

import cn.buk.api.wechat.dto.CommonDto;
import cn.buk.api.wechat.dto.CommonSearchCriteria;
import cn.buk.api.wechat.entity.Token;
import cn.buk.api.wechat.entity.WeixinUser;
import cn.buk.api.wechat.service.WeixinService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

import static cn.buk.api.wechat.util.HttpUtil.sendResponse;

/**
 * 微信服务号控制
 * Created by yfdai on 2017/2/22
 */
@RestController
@RequestMapping("/weixin")
public class WeixinController {

    private static Logger logger = Logger.getLogger(WeixinController.class);

    @InitBinder("sc")
    public void initBinder1(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("sc.");
    }


    @Autowired
    private WeixinService weixinService;

    /**
     * 微信公众号消息转发的地址
     * 加密/校验流程如下：
     * 1. 将token、timestamp、nonce三个参数进行字典序排序
     * 2. 将三个参数字符串拼接成一个字符串进行sha1加密
     * 3. 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
     *
     * @return
     */
    @RequestMapping("/verify")
    public void weixinVerify(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "signature") final String signature,
                             @RequestParam(value = "timestamp") final String timestamp,
                             @RequestParam(value = "nonce") final String nonce,
                             @RequestParam(value = "echostr", required = false) String echostr) {

        final boolean msgFromWeixin = weixinService.verifyWeixinSource(signature, timestamp, nonce);
        logger.debug("Is this message from weixin? " + signature + ", " + timestamp + ", " + nonce + ", " + echostr + ": " + msgFromWeixin);

        if (msgFromWeixin == false) {
            logger.warn("Message source is invalid.");
            return;
        }

        // 验证消息
        if (echostr != null && echostr.length() > 0) {
            logger.debug(echostr);
            sendResponse(response, echostr);

            return;
        }

        weixinService.processWeixinMessage(request, response);
    }

    @RequestMapping("/subscribers")
    public CommonDto<WeixinUser> searchSubscribers(@ModelAttribute("sc") CommonSearchCriteria sc) {

        if (sc == null) sc = new CommonSearchCriteria();
        sc.setPageSize(2);

        List<WeixinUser> users = weixinService.searchSubscribers(0, sc);

        CommonDto<WeixinUser> dto = new CommonDto<>();
        dto.setDataList(users);
        dto.setPage(sc.getPage());

        return dto;
    }

    @RequestMapping("/access-token")
    public Token searchAccessToken() {
        Token token = weixinService.searchAccessToken(0);
        token.setAccess_token("demo");
        return token;
    }
}
