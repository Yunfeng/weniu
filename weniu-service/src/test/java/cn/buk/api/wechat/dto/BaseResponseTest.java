package cn.buk.api.wechat.dto;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BaseResponseTest {

    @Test
    public void test_BaseResponse() {
        String json = "{\"errcode\":40018,\"errmsg\":\"invalid button name size\"}";

        BaseResponse baseResponse = JSON.parseObject(json, BaseResponse.class);

        assertNotNull(baseResponse);
        assertEquals(40018, baseResponse.getErrcode());
    }
}