package cn.buk.api.wechat.work.dto;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UploadMediaResponseTest {

  @Test
  void convertMediaResponse() {
    final String jsonStr = "{\"errcode\":0,\"errmsg\":\"ok\",\"type\":\"file\",\"media_id\":\"3O-PWqP8uYdNmIYXcyi716E7WZ2MSuIluLa_ikn5WAX1l7irTlPD5I989u1wDZABj\",\"created_at\":\"1608301796\"}";

    UploadMediaResponse response =  JSON.parseObject(jsonStr, UploadMediaResponse.class);
    assertNotNull(response);
    assertEquals(0, response.getErrcode());
    assertEquals("ok", response.getErrmsg());
    assertEquals("file", response.getMediaType());
    assertEquals("3O-PWqP8uYdNmIYXcyi716E7WZ2MSuIluLa_ikn5WAX1l7irTlPD5I989u1wDZABj", response.getMediaId());
    assertEquals(1608301796, response.getCreatedAt());

  }
}