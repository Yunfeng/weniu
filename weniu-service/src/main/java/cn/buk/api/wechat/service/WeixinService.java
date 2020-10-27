package cn.buk.api.wechat.service;


import cn.buk.api.wechat.dto.*;
import cn.buk.api.wechat.entity.*;
import cn.buk.common.sc.CommonSearchCriteria;
import cn.buk.common.JsonResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 微信公众号服务类
 * Created by yfdai on 2017/2/6.
 */
public interface WeixinService {

  /**
   * 验证消息来源是否微信发送的
   *
   * @param signature 签名
   * @param timestamp 时间戳
   * @param nonce     随机字符
   * @return
   */
  boolean verifyWeixinSource(int enterpriseId, String signature, String timestamp, String nonce);

  /**
   * 处理微信主动发送过来的事件
   *
   * @param enterpriseId
   * @param response
   * @param rq
   */
  void processWeixinEvent(int enterpriseId, HttpServletResponse response, WxData rq);

  /**
   * 处理微信消息
   *
   * @param request
   * @param response
   */
  void processWeixinMessage(int enterpriseId, HttpServletRequest request, HttpServletResponse response);

  JsSdkParam getJsSdkConfig(int enterpriseId, String jsapi_url);

  /**
   * 微信网页授权
   *
   * @param weixinOauthCode 授权的code
   * @return
   */
  WeixinOauthToken getOauthToken(int enterpriseId, String weixinOauthCode);


  /**
   * 基础支持-获取access_token：从数据库中获取，必要时重新获取
   *
   * @param enterpriseId
   * @return
   */
  Token searchAccessToken(int enterpriseId);


  /**
   * 发送客服消息接口
   */
  String sendCustomMessage(final int enterpriseId, final String touser, final String msgType, final String content, List<Object> articles);

  /**
   * 获取模板消息的模板列表，并保存到本地
   */
  List<WeixinTemplate> syncTemplates(final int enterpriseId);


  /**
   * 发送模板消息
   */
  String sendTemplateMsg(final int enterpriseId, WxTemplateSend wxTplRq);

  /**
   * 获取微信用户列表
   *
   * @return
   */
  int syncUserList(final int enterpriseId);


  /**
   * 根据openid获取用户基本信息
   *
   * @param openid 微信用户的唯一标示
   * @return
   */
  WeixinUserInfo getUserInfo(final int enterpriseId, String openid);


  /**
   * 获取素材总数
   *
   * @return
   */
  WxMaterialSummary getMaterialSummary(final int enterpriseId);

  /**
   * 获取素材列表
   *
   * @return
   */
  WxMaterials getMaterials(final int enterpriseId, final String mediaType, final int offset, final int count);

  /**
   * 新增永久素材(非图文素材）
   *
   * @return
   */
  WxMediaResponse addMaterial(final int enterpriseId, String filePath, String mediaType);

  /**
   * 新增永久图文素材
   *
   * @return
   */
  WxMediaResponse addMaterialNews(final int enterpriseId, WxNewsRequest request);

  /**
   * 上传图文消息内的图片获取URL
   *
   * @param filePath
   * @return
   */
  WxMediaResponse uploadNewsImage(final int enterpriseId, String filePath);

  /**
   * 获取永久素材
   */
  String getMaterial(final int enterpriseId, String mediaType, String mediaId);

  /**
   * 删除永久素材
   */
  WxMediaResponse delMaterial(final int enterpriseId, String mediaId);

  /**
   * 获取临时素材
   * 语音和图片素材返回下载到本地后的地址；视频文件返回URL
   */
  String getMedia(final int enterpriseId, String mediaType, String mediaId);


  //database operation

  List<WeixinUser> searchSubscribers(int enterpriseId, CommonSearchCriteria sc);

  /**
   * 查找本地保存的永久素材列表
   *
   * @param enterpriseId
   * @param sc
   * @return
   */
  List<WeixinMaterial> searchMaterials(int enterpriseId, CommonSearchCriteria sc);

  List<WeixinMaterial> searchMaterials(int enterpriseId, String mediaId);

  /**
   * 保存永久素材信息在本地
   *
   * @param mediaType
   * @param mediaId
   * @param url
   * @return
   */
  int createWeixinMaterial(int enterpriseId, String mediaType, String mediaId, String url, String name);

  WeixinMaterial searchWeixinMaterial(final int enterpriseId, int id);


  WeixinTemplate searchWeixinTemplate(final int enterpriseId, String id);

  List<WeixinTemplate> searchTemplates(int enterpriseId);


  Token getToken(int enterpriseId);


  List<WeixinCustomMenu> searchCustomMenus(int enterpriseId);

  /**
   * 微信公众号自定义菜单创建接口
   */
  BaseResponse createCustomMenu(int enterpriseId);

  /**
   * 创建自定义菜单
   *
   * @param enterpriseId 企业id
   * @param jsonFilename 保存自定义菜单信息的文件名
   * @return
   */
  @Deprecated
  BaseResponse createCustomMenu(final int enterpriseId, String jsonFilename);

  int createCustomMenu(int enterpriseId, String name, String type, String url, String key, int level, int parentId);

  int createCustomMenu(int enterpriseId, String name, String type, String url, String key, int level, int parentId, int bindUrl);


  /**
   * 微信公众号自定义菜单查询接口
   */
  String getCustomMenu(int enterpriseId);


  String deleteCustomMenu(final int enterpriseId);

  int deleteCustomMenu(int enterpriseId, int id);


  /**
   * 创建临时二维码
   *
   * @return
   */
  String createWeixinTemporaryQr(final int enterpriseId);

  int createWeixinAccessTime(final int enterpriseId, String weixinOpenId);

  /**
   * 发送客服消息
   *
   * @param toUser
   * @param content
   * @return
   */
  String sendWeixinCustomMessage(final int enterpriseId, String toUser, String content);

  String apiGetUserList(final int enterpriseId);

  String apiGetUserInfo(final int enterpriseId);

  String apiGetUserInfo(final int enterpriseId, int userId);

  String apiGetGroupList(final int enterpriseId);

  String apiGetGroupId(final int enterpriseId, String weixinOpenId);

  JsonResult apiCreateGroup(final int enterpriseId, String groupName);

  /**
   * 修改分组名称
   *
   * @param groupId   要修改的分组id
   * @param groupName 新名字
   * @return
   */
  JsonResult apiUpdateGroup(final int enterpriseId, int groupId, String groupName);


  /**
   * weixin  user
   */
  List<WeixinUser> getUserList(final int enterpriseId);


  /**
   * weixin-group
   */
  List<WeixinGroup> getGroupList(final int enterpriseId);

  WeixinGroup getGroupInfo(final int enterpriseId, int groupId);



  /**
   * 生成 微信授权的 url
   *
   * @param url
   * @return
   */
  String buildUrlInWeixin(final int enterpriseId, String url);
}
