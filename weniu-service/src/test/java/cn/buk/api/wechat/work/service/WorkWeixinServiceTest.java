package cn.buk.api.wechat.work.service;

import static cn.buk.api.wechat.entity.WeixinEntConfig.WORK_WX_EXTERNAL_CONTACTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import cn.buk.api.wechat.dao.WeixinDao;
import cn.buk.api.wechat.entity.Token;
import cn.buk.api.wechat.entity.WeixinEntConfig;
import cn.buk.api.wechat.work.dto.ExternalContactDetailResponse;
import cn.buk.api.wechat.work.dto.ExternalContactFollowUsersResponse;
import cn.buk.api.wechat.work.dto.ExternalContactListResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class WorkWeixinServiceTest {

  @Mock
  private WeixinDao weixinDao;

  @InjectMocks
  private final WorkWeixinService service = new WorkWeixinServiceImpl(true);

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Disabled
  @Test
  public void test_getToken() {

    WeixinEntConfig config = new WeixinEntConfig();
    config.setEnterpriseId(24);
    config.setCorpId("ww154883f946d16662");
    //外部联系人
    config.setMsgType(WORK_WX_EXTERNAL_CONTACTS);
    config.setSecret("7R-HQNLb1O-2MoYQPEaVhe_oXCuK_KN1BJr9gJ7TPMc");


    when(weixinDao.getWeixinEntConfig(anyInt(), anyInt())).thenReturn(config);
    when(weixinDao.retrieveWeixinToken(anyInt(), anyInt(), anyInt())).thenReturn(null);

    service.getWorkWeixinToken(24, true);

  }


  private Token createToken() {
    Token token = new Token();
    token.setAccess_token("ao2eYwuXA42a9KsmE3H6c4ruNhrML3vLb31AZLbbNbTZmh6ONAv2WZPQmiQMR-0xqYQIDzuD0dvdEyUSQkcdXPWq4ss1SQbgSLlvcLtJlBI4Ds31sdeTcLwvdb7vOsb_4wxIp96sALkL2DMm6B056QbAGUSpSgQC9g1T9Ua-Ahk3UaoMMw64KUmrBfNQUzllNn5D708X9Ys1Zt6kPLi47A");
    return token;
  }

  @Test
  public void test_getExternalContactFollowUsers() {
    Token token = createToken();

    ExternalContactFollowUsersResponse response = service.getExternalContactFollowUsers(token.getAccess_token());

    assertNotNull(response);
    assertEquals(0, response.getErrcode());
    assertTrue(response.getUsers().length > 0);
  }

  @Test
  public void test_getExternalContactList() {
    Token token = createToken();

    ExternalContactListResponse response = service.getExternalContactList(token.getAccess_token(), "DingXiaoYun");

    assertNotNull(response);
    assertEquals(0, response.getErrcode());
    assertTrue(response.getExternalUserIds().length > 0);
  }

  @Test
  public void test_getExternalContactDetail() {
    Token token = createToken();

    ExternalContactDetailResponse response = service.getExternalContactDetail(token.getAccess_token(), "wmbTm9DwAAoZ2CaasIOmrWf7cLhbx7Iw");

    assertNotNull(response.getExternalContactDetail());
//    assertEquals(0, response.getErrcode());
//    assertTrue(response.getExternalUserIds().length > 0);

    System.out.println(System.currentTimeMillis());

    long l = response.getFollowUsers().get(0).getCreateTime().getTime();
    System.out.println(l);

//    response.getFollowUsers().get(0).setCreateTime(new Date(l * 1000l));

    System.out.println(response.getFollowUsers().get(0).getCreateTime().getTime());
    System.out.println(response.getFollowUsers().get(0).getCreateTime());
  }
}