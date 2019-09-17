package cn.buk.api.wechat.work.dto;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import static org.junit.Assert.*;

public class WwpLoginInfoTest {
    @Test
    public void testCreate() {
        final String jsonStr = "    {\n" +
                "       \"errcode\":0,\n" +
                "       \"errmsg\":\"ok\",\n" +
                "       \"usertype\": 1,\n" +
                "       \"user_info\":{\n" +
                "           \"userid\":\"test\",\n" +
                "           \"name\":\"william\",\n" +
                "           \"avatar\":\"xxxx\"\n" +
                "       },\n" +
                "       \"corp_info\":{\n" +
                "           \"corpid\":\"wx6c698d13f7a409a4\",\n" +
                "        },\n" +
                "       \"agent\":[\n" +
                "           {\"agentid\":0,\"auth_type\":1},\n" +
                "           {\"agentid\":1,\"auth_type\":1},\n" +
                "           {\"agentid\":2,\"auth_type\":1}\n" +
                "       ],\n" +
                "       \"auth_info\":{\n" +
                "           \"department\":[\n" +
                "               {\n" +
                "                   \"id\":\"2\",\n" +
                "                   \"writable\":\"true\"\n" +
                "               }\n" +
                "           ]\n" +
                "       }\n" +
                "    }";

        WwpLoginInfo info = JSON.parseObject(jsonStr, WwpLoginInfo.class);
        assertEquals(0, info.getErrcode());
        assertEquals("ok", info.getErrmsg());
        assertEquals(1, info.getUsertype());

        assertNotNull(info.getUser_info());
        assertEquals("test", info.getUser_info().getUserid());
        assertEquals("william", info.getUser_info().getName());

        assertNotNull(info.getCorp_info());
        assertEquals("wx6c698d13f7a409a4", info.getCorp_info().getCorpid());
    }
}