package cn.buk.api.wechat.work.dto;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WwpPermanentCorpAuthInfoTest {

    @Test
    public void testCreate() {
        final String jsonStr = "{\n" +
                "        \"errcode\":0 ,\n" +
                "        \"errmsg\":\"ok\" ,\n" +
                "        \"access_token\": \"xxxxxx\", \n" +
                "        \"expires_in\": 7200, \n" +
                "        \"permanent_code\": \"xxxx\", \n" +
                "        \"auth_corp_info\": \n" +
                "        {\n" +
                "            \"corpid\": \"xxxx\",\n" +
                "            \"corp_name\": \"name\",\n" +
                "            \"corp_type\": \"verified\",\n" +
                "            \"corp_square_logo_url\": \"yyyyy\",\n" +
                "            \"corp_user_max\": 50,\n" +
                "            \"corp_agent_max\": 30,\n" +
                "            \"corp_full_name\":\"full_name\",\n" +
                "            \"verified_end_time\":1431775834,\n" +
                "            \"subject_type\": 1,\n" +
                "            \"corp_wxqrcode\": \"zzzzz\",\n" +
                "            \"corp_scale\": \"1-50人\",\n" +
                "            \"corp_industry\": \"IT服务\",\n" +
                "            \"corp_sub_industry\": \"计算机软件/硬件/信息服务\"\n" +
                "        },\n" +
                "        \"auth_info\":\n" +
                "        {\n" +
                "            \"agent\" :\n" +
                "            [\n" +
                "                {\n" +
                "                    \"agentid\":1,\n" +
                "                    \"name\":\"NAME\",\n" +
                "                    \"round_logo_url\":\"xxxxxx\",\n" +
                "                    \"square_logo_url\":\"yyyyyy\",\n" +
                "                    \"appid\":1,\n" +
                "                    \"privilege\":\n" +
                "                    {\n" +
                "                        \"level\":1,\n" +
                "                        \"allow_party\":[1,2,3],\n" +
                "                        \"allow_user\":[\"zhansan\",\"lisi\"],\n" +
                "                        \"allow_tag\":[1,2,3],\n" +
                "                        \"extra_party\":[4,5,6],\n" +
                "                        \"extra_user\":[\"wangwu\"],\n" +
                "                        \"extra_tag\":[4,5,6]\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"agentid\":2,\n" +
                "                    \"name\":\"NAME2\",\n" +
                "                    \"round_logo_url\":\"xxxxxx\",\n" +
                "                    \"square_logo_url\":\"yyyyyy\",\n" +
                "                    \"appid\":5\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        \"auth_user_info\":\n" +
                "        {\n" +
                "            \"userid\":\"aa\",\n" +
                "            \"name\":\"xxx\",\n" +
                "            \"avatar\":\"http://xxx\"\n" +
                "        }\n" +
                "}";

        WwpPermanentCorpAuthInfo info = JSON.parseObject(jsonStr, WwpPermanentCorpAuthInfo.class);
        assertEquals(0, info.getErrcode());
        assertEquals("name", info.getAuth_corp_info().getCorp_name());
        assertEquals(2, info.getAuth_info().getAgent().length);
        assertEquals("1", info.getAuth_info().getAgent()[0].getAgentid());
        assertEquals("2", info.getAuth_info().getAgent()[1].getAgentid());
        assertEquals(1, info.getAuth_info().getAgent()[0].getPrivilege().getLevel());

        assertEquals("aa", info.getAuth_user_info().getUserid());

    }

}