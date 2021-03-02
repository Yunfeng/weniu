package cn.buk.api.wechat.work.service;

import cn.buk.api.wechat.dto.BaseResponse;
import cn.buk.api.wechat.dto.JsSdkParam;
import cn.buk.api.wechat.entity.Token;
import cn.buk.api.wechat.work.dto.*;
import cn.buk.api.wechat.work.message.TaskCardMessage;

import javax.validation.constraints.NotNull;

/**
 * @author yfdai
 */
public interface WorkWeixinService {


  /**
   * 校验消息签名，也就是校验来源
   *
   * @param enterpriseId 企业ID
   * @param msgType 消息类型：默认应用，通讯录，外部联系人
   * @param corpId 实际使用的corpId
   * @param signature 签名
   * @param timestamp 时间戳
   * @param nonce 随机字符串
   * @param msg_encrypt 加密的消息
   */
  String verifyWorkWeixinSource(int enterpriseId, int msgType, final String corpId,
      String signature, String timestamp, String nonce, String msg_encrypt) throws Exception;

  /**
   * 根据code获取成员信息
   *
   * @param code 成员授权获得的code，最大为512字节，只能用一次，5分钟有效期
   */
  UserInfoResponse getUserInfo(int enterpriseId, String code);

  /**
   * 根据user_ticket获取成员详情
   */
  UserDetailResponse getUserDetail(int enterpriseId, String userTicket);

  /**
   * 获取自定义菜单
   * @return
   */
//    String getCustomMenu();

  /**
   * 获取jsapi_ticket
   */
  JsSdkParam getJsSdkConfig(int enterpriseId, String jsapi_url);

  /**
   * 上传临时素材
   * @param enterpriseId 企业id
   * @param mediaType 媒体文件类型
   * @param filename 媒体文件名
   * @param displayName 上传后的文件名
   * @return 接口返回内容
   */
  UploadMediaResponse uploadMedia(int enterpriseId, String mediaType, String filename, String displayName);

  /**
   * 获取临时素材 语音和图片素材返回下载到本地后的地址；视频文件返回URL
   */
  String getMedia(int enterpriseId, String mediaType, String mediaId);

  /**
   * 获取部门列表（通讯录）
   */
  ListDepartmentResponse listDepartment(int enterpriseId);

  /**
   * 获取部门列表（应用可见的）
   */
  ListDepartmentResponse listDepartment(int enterpriseId, int msgType);

  /**
   * 创建部门
   */
  CreateDepartmentResponse createDepartment(int enterpriseId, WwDepartment dept);

  /**
   * 修改部门
   */
  BaseResponse updateDepartment(int enterpriseId, WwDepartment dept);

  /**
   * 删除部门
   */
  BaseResponse deleteDepartment(int enterpriseId, int id);

  BaseResponse createUser(int enterpriseId, WwUser user);

  BaseResponse updateUser(int enterpriseId, WwUser user);

  WwUser getUser(int enterpriseId, String userId);

  BaseResponse deleteUser(int enterpriseId, String userId);

  ListUserResponse listUser(int enterpriseId, int deptId);

  /**
   * 用户验证成功（新用户加入企业微信时二次验证成功）
   */
  BaseResponse authUserSuccessfully(int enterpriseId, String userId);


  /**
   * 保存token
   */
  int saveToken(int enterpriseId, String accessToken, int expiresIn);

  Token getWorkWeixinToken(int enterpriseId, boolean forced);

  /**
   * 获取配置了客户联系功能的成员列表
   * @param accessToken 调用接口凭证
   * @return
   */
  ExternalContactFollowUsersResponse getExternalContactFollowUsers(String accessToken);


  /**
   * 获取外部联系人列表
   * @param accessToken 调用接口凭证
   * @param userId 企业成员的userid
   * @return
   */
  ExternalContactListResponse getExternalContactList(String accessToken, String userId);

  /**
   * 获取外部联系人详情
   * @param accessToken 调用接口凭证
   * @param externalUserId 外部联系人的userid，注意不是企业成员的帐号
   * @return
   */
  ExternalContactDetailResponse getExternalContactDetail(final String accessToken, final String externalUserId);

  /**
   * 发送应用消息：文本消息
   */
  void sendTextMsg(int enterpriseId, String msg, String weixinIds, String deptIds, String tagIds);

  /**
   * 发送文件消息
   */
  void sendFileMsg(int enterpriseId, String mediaId, String weixinIds, String deptIds, String tagIds);

  /**
   * 发送应用消息：任务卡片消息
   */
  void sendTaskCardMsg(int enterpriseId, @NotNull TaskCardMessage msg);

}
