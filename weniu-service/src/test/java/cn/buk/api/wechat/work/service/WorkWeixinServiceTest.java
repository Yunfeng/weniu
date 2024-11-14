package cn.buk.api.wechat.work.service;

import cn.buk.api.wechat.dao.WeixinDao;
import cn.buk.api.wechat.entity.Token;
import cn.buk.api.wechat.entity.WeixinEntConfig;
import cn.buk.api.wechat.work.dto.ExternalContactDetailResponse;
import cn.buk.api.wechat.work.dto.ExternalContactFollowUsersResponse;
import cn.buk.api.wechat.work.dto.ExternalContactListResponse;
import cn.buk.api.wechat.work.dto.UploadMediaResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@Disabled
class WorkWeixinServiceTest extends BaseTest {

  @Mock
  private WeixinDao weixinDao;

  @InjectMocks
  private final WorkWeixinService service = new WorkWeixinServiceImpl(true);

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void test_getToken() {
    WeixinEntConfig config = createConfig();

    when(weixinDao.getWeixinEntConfig(anyInt(), anyInt())).thenReturn(config);
    when(weixinDao.retrieveWeixinToken(anyInt(), anyInt(), anyInt())).thenReturn(null);

    var token = service.getWorkWeixinToken(1, false);
    assertNotNull(token);
  }

  @Test
  void test_uploadMedia() {
    when(weixinDao.getWeixinEntConfig(anyInt(), anyInt())).thenReturn(createConfig());
    when(weixinDao.retrieveWeixinToken(anyInt(), anyInt(), anyInt())).thenReturn(createToken());

//    InputStream is = this.getClass().getResourceAsStream("tms-2.png");
//    System.out.println(this.getClass().getResource("/resources"));
//    String filePath = this.getClass().getClassLoader().getResource("tms-2.png").getFile();
//    System.out.println(filePath);
    File file = new File("../data/tms-2.png");
    try {
      System.out.println(file.getCanonicalPath());
    } catch (IOException e) {
      e.printStackTrace();
    }

    try {
      service.uploadMedia(1, "file", file.getCanonicalPath(), null);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void test_sendFileMsg() throws IOException {
    when(weixinDao.getWeixinEntConfig(anyInt(), anyInt())).thenReturn(createConfig());
    when(weixinDao.retrieveWeixinToken(anyInt(), anyInt(), anyInt())).thenReturn(createToken());

    File file = new File("../data/tms-2.png");
//    try {
//      System.out.println(file.getCanonicalPath());
//    } catch (IOException e) {
//      e.printStackTrace();
//    }

    UploadMediaResponse rs = service.uploadMedia(1, "file", file.getCanonicalPath(), "测试中文图片.png");

    final String mediaId = rs.getMediaId();


    service.sendFileMsg(1, mediaId, "william", null, null);

  }

  @Disabled
  @Test
  public void test_getExternalContactFollowUsers() {
    Token token = createToken();

    ExternalContactFollowUsersResponse response = service.getExternalContactFollowUsers(token.getAccess_token());

    assertNotNull(response);
    assertEquals(0, response.getErrcode());
    assertTrue(response.getUsers().length > 0);
  }

  @Disabled
  @Test
  public void test_getExternalContactList() {
    Token token = createToken();

    ExternalContactListResponse response = service.getExternalContactList(token.getAccess_token(), "DingXiaoYun");

    assertNotNull(response);
    assertEquals(0, response.getErrcode());
    assertTrue(response.getExternalUserIds().length > 0);
  }

  @Disabled
  @Test
  void test_getExternalContactDetail() {
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