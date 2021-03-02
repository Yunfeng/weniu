package cn.buk.api.wechat.work.dto;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserInfoResponseTest {

    @Test
    public void testCreate() {
        final String jsonStr = "    {\n" +
                "       \"errcode\": 0,\n" +
                "       \"errmsg\": \"ok\",\n" +
                "       \"CorpId\":\"CORPID\",\n" +
                "       \"UserId\":\"USERID\",\n" +
                "       \"DeviceId\":\"DEVICEID\",\n" +
                "       \"user_ticket\": \"USER_TICKET\",\n" +
                "       \"expires_in\":7200\n" +
                "    }";

        UserInfoResponse info = JSON.parseObject(jsonStr, UserInfoResponse.class);
        assertEquals(0, info.getErrcode());
        assertEquals("ok", info.getErrmsg());

        assertEquals("CORPID", info.getCorpId());
        assertEquals("USERID", info.getUserId());
        assertEquals("DEVICEID", info.getDeviceId());
        assertEquals("USER_TICKET", info.getUser_ticket());
        assertEquals(7200, info.getExpires_in());

    }

}