package cn.buk.api.wechat.work.service;

import cn.buk.api.wechat.dto.JsSdkParam;
import cn.buk.api.wechat.entity.WwProviderAuthCorpInfo;
import cn.buk.api.wechat.work.dto.*;

import java.util.Date;

public interface WorkWeixinService {


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
     * 根据code获取成员信息
     * @param code 成员授权获得的code，最大为512字节，只能用一次，5分钟有效期
     * @return
     */
    UserInfoResponse getUserInfo(int enterpriseId, String code);

    /**
     * 根据user_ticket获取成员详情
     * @param userTicket
     * @return
     */
    UserDetailResponse getUserDetail(int enterpriseId, String userTicket);

    /**
     * 获取自定义菜单
     * @return
     */
//    String getCustomMenu();

    /**
     * 获取jsapi_ticket
     * @param jsapi_url
     * @return
     */
    JsSdkParam getJsSdkConfig(int enterpriseId, String jsapi_url);

    /**
     * 获取临时素材
     * 语音和图片素材返回下载到本地后的地址；视频文件返回URL
     */
    String getMedia(int enterpriseId, String mediaType, String mediaId);

    /**
     * 获取部门列表（通讯录）
     * @return
     */
    ListDepartmentResponse listDepartment(int enterpriseId);

    /**
     * 获取部门列表（应用可见的）
     * @param enterpriseId
     * @param msgType
     * @return
     */
    ListDepartmentResponse listDepartment(int enterpriseId, int msgType);

    /**
     * 创建部门
     * @param enterpriseId
     * @return
     */
    CreateDepartmentResponse createDepartment(int enterpriseId, WwDepartment dept);

    /**
     * 修改部门
     * @param enterpriseId
     * @param dept
     * @return
     */
    BaseResponse updateDepartment(int enterpriseId, WwDepartment dept);

    /**
     * 删除部门
     * @param enterpriseId
     * @param id
     * @return
     */
    BaseResponse deleteDepartment(int enterpriseId, int id);

    BaseResponse createUser(int enterpriseId, WwUser user);

    BaseResponse updateUser(int enterpriseId, WwUser user);

    WwUser getUser(int enterpriseId, String userId);

    BaseResponse deleteUser(int enterpriseId, String userId);

    ListUserResponse listUser(int enterpriseId, int deptId);

    /**
     * 用户验证成功（新用户加入企业微信时二次验证成功）
     * @param enterpriseId
     * @param userId
     * @return
     */
    BaseResponse authUserSuccessfully(int enterpriseId, String userId);

    String getExternalContact(int enterpriseId, String code);

    /**
     * 保存token
     * @param enterpriseId
     * @param accessToken
     * @param expiresIn
     * @return
     */
    int saveToken(int enterpriseId, String accessToken, int expiresIn);
}
