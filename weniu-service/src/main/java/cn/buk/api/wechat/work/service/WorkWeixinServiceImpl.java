package cn.buk.api.wechat.work.service;

import cn.buk.api.wechat.dto.BaseResponse;
import cn.buk.api.wechat.dto.JsSdkParam;
import cn.buk.api.wechat.work.message.FileMessage;
import cn.buk.api.wechat.work.message.TaskCardMessage;
import cn.buk.api.wechat.work.message.TextMessage;
import cn.buk.api.wechat.entity.*;
import cn.buk.api.wechat.util.HttpUtil;
import cn.buk.api.wechat.util.SignUtil;
import cn.buk.api.wechat.work.dto.*;
import cn.buk.common.JsonResult;
import cn.buk.util.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import javax.validation.constraints.NotNull;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.buk.api.wechat.entity.WeixinEntConfig.WORK_WX_DEFAULT;

/**
 * @author yfdai
 */
public class WorkWeixinServiceImpl extends BaseService implements WorkWeixinService {

    private static final Logger logger = Logger.getLogger(WorkWeixinServiceImpl.class);

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

        return wxcpt.VerifyURL(signature, timestamp, nonce, msg_encrypt);
    }

    public UserInfoResponse getUserInfo(int enterpriseId, String code) {
        Token token = getToken(enterpriseId, false);
        String url = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?";

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

  @Override
  public UploadMediaResponse uploadMedia(int enterpriseId, String mediaType, String filename, String displayName) {
    final String API_URL = "https://qyapi.weixin.qq.com/cgi-bin/media/upload?";

    Token token = getToken(enterpriseId);
    String url = API_URL + "access_token=" + token.getAccess_token()+"&type=" + mediaType;

    String result = postFile(url, filename, displayName);
    logger.info(result);

    return JSON.parseObject(result, UploadMediaResponse.class);
  }

  public static String postFile(String url, String filePath, String displayName) {
    File file = new File(filePath);
    if (!file.exists())
      return null;

    String result = null;

    try {
      URL url1 = new URL(url);
      HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
      conn.setConnectTimeout(5000);
      conn.setReadTimeout(30000);
      conn.setDoOutput(true);
      conn.setDoInput(true);
      conn.setUseCaches(false);
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Connection", "Keep-Alive");
      conn.setRequestProperty("Cache-Control", "no-cache");
      String boundary = "-----------------------------" + System.currentTimeMillis();
      conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

      OutputStream output = conn.getOutputStream();
      output.write(("--" + boundary + "\r\n").getBytes());

      String uploadedFilename = displayName == null ? file.getName(): displayName;
      String contentDisposition = String.format("Content-Disposition: form-data; name=\"media\"; filename=\"%s\"\r\n", uploadedFilename);
      output.write(contentDisposition.getBytes(StandardCharsets.UTF_8));

      output.write("Content-Type: image/jpeg \r\n\r\n".getBytes());

      byte[] data = new byte[1024];
      int len = 0;
      FileInputStream input = new FileInputStream(file);
      while ((len = input.read(data)) > -1) {
        output.write(data, 0, len);
      }
      output.write(("\r\n--" + boundary + "\r\n\r\n").getBytes());
      output.flush();
      output.close();
      input.close();
      InputStream resp = conn.getInputStream();
      StringBuffer sb = new StringBuffer();
      while ((len = resp.read(data)) > -1)
        sb.append(new String(data, 0, len, StandardCharsets.UTF_8));
      resp.close();
      result = sb.toString();
      //System.out.println(result);
    } catch (ClientProtocolException e) {
      logger.error("postFile，不支持http协议", e);
    } catch (IOException e) {
      logger.error("postFile数据传输失败", e);
    }
//    System.out.println(result);
    return result;
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
        ticket.setEnterpriseId(enterpriseId);

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
    public ExternalContactDetailResponse getExternalContactDetail(final String accessToken, final String externalUserId) {
        final String url = "https://qyapi.weixin.qq.com/cgi-bin/externalcontact/get?access_token=" + accessToken + "&external_userid=" + externalUserId;

        String jsonStr = HttpUtil.getUrl(url, null);
        if (this.outputJson) {
            System.out.println(jsonStr);
        }

        return JSON.parseObject(jsonStr, ExternalContactDetailResponse.class);
    }

  @Override
  public ExternalContactFollowUsersResponse getExternalContactFollowUsers(String accessToken) {
    final String url = "https://qyapi.weixin.qq.com/cgi-bin/externalcontact/get_follow_user_list?access_token=" + accessToken;

    String jsonStr = HttpUtil.getUrl(url, null);
    if (this.outputJson) {
      System.out.println(jsonStr);
    }

    return JSON.parseObject(jsonStr, ExternalContactFollowUsersResponse.class);
  }

    @Override
    public ExternalContactListResponse getExternalContactList(String accessToken, String userId) {
        final String url = "https://qyapi.weixin.qq.com/cgi-bin/externalcontact/list?access_token=" + accessToken + "&userid=" + userId;

        String jsonStr = HttpUtil.getUrl(url, null);
        if (this.outputJson) {
            System.out.println(jsonStr);
        }

        return JSON.parseObject(jsonStr, ExternalContactListResponse.class);
    }

    @Override
    public int saveToken(int enterpriseId, String accessToken, int expiresIn) {
        Token token = new Token();
        token.setAccess_token(accessToken);
        token.setExpires_in(expiresIn);
        token.setEnterpriseId(enterpriseId);
        token.setWeixinType(Token.WORK_WEIXIN_TOKEN);
        token.setMsgType(WORK_WX_DEFAULT);

        return weixinDao.createWeixinToken(token);
    }

    @Override
    public Token getWorkWeixinToken(int enterpriseId, boolean forced) {
        return this.getToken(enterpriseId, forced);
    }

    @Override
    public void sendTextMsg(int enterpriseId, String msg, String weixinIds, String deptIds, String tagIds) {
        if (weixinIds != null && weixinIds.equalsIgnoreCase("NONE")) return;

        WeixinEntConfig cfg = weixinDao.getWeixinEntConfig(enterpriseId, WeixinEntConfig.WORK_WX_DEFAULT);
        if (cfg == null) {
            logger.warn("No weixin config.");
            return;
        }

        logger.info(cfg.getEnterpriseId() + ", " + enterpriseId + ": " + cfg.getId() + ", " + cfg.getCorpId() + ", " + cfg.getAgentId() + ", " + cfg.getSecret());

        TextMessage txtMsg = new TextMessage();
        txtMsg.setAgentid(cfg.getAgentId());

        if (weixinIds != null && weixinIds.trim().length() > 0) {
            txtMsg.setTouser( weixinIds.replaceAll(";", "|"));
        }

        if (deptIds != null && deptIds.trim().length() > 0) {
            txtMsg.setToparty(deptIds.replaceAll(";", "|"));
        }

        if (tagIds != null && tagIds.trim().length() > 0) {
            txtMsg.setTotag(tagIds.replaceAll(";", "|"));
        }


        txtMsg.setContent(msg  + ". " + DateUtil.formatDate(DateUtil.getCurDateTime(), "MM-dd HH:mm:ss"));

        String jsonStr = com.alibaba.fastjson.JSON.toJSONString(txtMsg);


        Token token = getToken(enterpriseId, WeixinEntConfig.WORK_WX_DEFAULT, false);
        logger.info("token: " + token.getId() + ", " + token.getEnterpriseId() + ", " + token.getMsgType() + ", " + token.getAccess_token());
        logger.info(jsonStr);

        JsonResult jsonResult = doSendAppMsg(jsonStr, token);

        if (jsonResult.getErrcode() == 0) {
            //发送成功
            //return jsonResult;
        } else if (jsonResult.getErrcode() == 40014) {
            //invalid access_token
            //try again
            System.out.println("try again ............................................................................");
            token = getToken(enterpriseId, WeixinEntConfig.WORK_WX_DEFAULT, true);
            doSendAppMsg(jsonStr, token);
        } else {
            logger.error(jsonResult.getErrcode() + " - " + jsonResult.getErrmsg());
        }
    }

  @Override
  public void sendFileMsg(int enterpriseId, String mediaId, String weixinIds, String deptIds, String tagIds) {
    if (weixinIds != null && weixinIds.equalsIgnoreCase("NONE")) return;

    WeixinEntConfig cfg = weixinDao.getWeixinEntConfig(enterpriseId, WeixinEntConfig.WORK_WX_DEFAULT);
    if (cfg == null) {
      logger.warn("No weixin config.");
      return;
    }

    logger.info(cfg.getEnterpriseId() + ", " + enterpriseId + ": " + cfg.getId() + ", " + cfg.getCorpId() + ", " + cfg.getAgentId() + ", " + cfg.getSecret());

    FileMessage fileMessage = new FileMessage();
    fileMessage.setAgentid(cfg.getAgentId());

    if (weixinIds != null && weixinIds.trim().length() > 0) {
      fileMessage.setTouser( weixinIds.replaceAll(";", "|"));
    }

    if (deptIds != null && deptIds.trim().length() > 0) {
      fileMessage.setToparty(deptIds.replaceAll(";", "|"));
    }

    if (tagIds != null && tagIds.trim().length() > 0) {
      fileMessage.setTotag(tagIds.replaceAll(";", "|"));
    }


    fileMessage.getMediaInfo().setMediaId(mediaId);

    String jsonStr = com.alibaba.fastjson.JSON.toJSONString(fileMessage);


    Token token = getToken(enterpriseId, WeixinEntConfig.WORK_WX_DEFAULT, false);
    logger.info("token: " + token.getId() + ", " + token.getEnterpriseId() + ", " + token.getMsgType() + ", " + token.getAccess_token());
    logger.info(jsonStr);

    JsonResult jsonResult = doSendAppMsg(jsonStr, token);

    if (jsonResult.getErrcode() == 0) {
      //发送成功
      //return jsonResult;
    } else if (jsonResult.getErrcode() == 40014) {
      //invalid access_token
      //try again
      System.out.println("try again ............................................................................");
      token = getToken(enterpriseId, WeixinEntConfig.WORK_WX_DEFAULT, true);
      doSendAppMsg(jsonStr, token);
    } else {
      logger.error(jsonResult.getErrcode() + " - " + jsonResult.getErrmsg());
    }
  }

  @Override
    public void sendTaskCardMsg(final int enterpriseId, @NotNull TaskCardMessage msg) {

        WeixinEntConfig cfg = weixinDao.getWeixinEntConfig(enterpriseId, WeixinEntConfig.WORK_WX_DEFAULT);
        if (cfg == null) {
            logger.warn("No work weixin config.");
            return;
        }

        msg.setAgentid(cfg.getAgentId());

        String jsonStr = JSON.toJSONString(msg);


        Token token = getToken(enterpriseId, WeixinEntConfig.WORK_WX_DEFAULT, false);
        logger.info("token: " + token.getId() + ", " + token.getEnterpriseId() + ", " + token.getMsgType() + ", " + token.getAccess_token());
        logger.info(jsonStr);

        JsonResult jsonResult = doSendAppMsg(jsonStr, token);

        if (jsonResult.getErrcode() == 0) {
            //发送成功
            //return jsonResult;
        } else if (jsonResult.getErrcode() == 40014) {
            //invalid access_token
            //try again
            System.out.println("try again ............................................................................");
            token = getToken(enterpriseId, WeixinEntConfig.WORK_WX_DEFAULT, true);
            doSendAppMsg(jsonStr, token);
        } else {
            logger.error(jsonResult.getErrcode() + " - " + jsonResult.getErrmsg());
        }
    }


    /**
     * 发送企业微信应用消息
     * @param jsonStr
     * @param token
     * @return
     */
    private JsonResult doSendAppMsg(final String jsonStr, final Token token) {
        String url = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + token.getAccess_token() ;

        String returnStr = HttpUtil.postUrl(url, jsonStr);

        logger.info("doSendAppMsg: " + returnStr);
        System.out.println(returnStr);

        return JSON.parseObject(returnStr, JsonResult.class);
    }
}
