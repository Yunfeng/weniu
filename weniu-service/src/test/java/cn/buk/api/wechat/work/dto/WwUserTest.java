package cn.buk.api.wechat.work.dto;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WwUserTest {

    @Test
    public void test_wwUser() {
        final String jsonStr = "    {\n" +
                "        \"errcode\": 0,\n" +
                "        \"errmsg\": \"ok\",\n" +
                "        \"userid\": \"zhangsan\",\n" +
                "        \"name\": \"李四\",\n" +
                "        \"department\": [1, 2],\n" +
                "        \"order\": [1, 2],\n" +
                "        \"position\": \"后台工程师\",\n" +
                "        \"mobile\": \"15913215421\",\n" +
                "        \"gender\": \"1\",\n" +
                "        \"email\": \"zhangsan@gzdev.com\",\n" +
                "        \"is_leader_in_dept\": [1, 0],\n" +
                "        \"avatar\": \"http://wx.qlogo.cn/mmopen/ajNVdqHZLLA3WJ6DSZUfiakYe37PKnQhBIeOQBO4czqrnZDS79FH5Wm5m4X69TBicnHFlhiafvDwklOpZeXYQQ2icg/0\",\n" +
                "        \"telephone\": \"020-123456\",\n" +
                "        \"enable\": 1,\n" +
                "        \"alias\": \"jackzhang\",\n" +
                "        \"address\": \"广州市海珠区新港中路\",\n" +
                "        \"extattr\": {\n" +
                "            \"attrs\": [\n" +
                "                {\n" +
                "                    \"type\": 0,\n" +
                "                    \"name\": \"文本名称\",\n" +
                "                    \"text\": {\n" +
                "                        \"value\": \"文本\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"type\": 1,\n" +
                "                    \"name\": \"网页名称\",\n" +
                "                    \"web\": {\n" +
                "                        \"url\": \"http://www.test.com\",\n" +
                "                        \"title\": \"标题\"\n" +
                "                    }\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        \"status\": 1,\n" +
                "        \"qr_code\": \"https://open.work.weixin.qq.com/wwopen/userQRCode?vcode=xxx\",\n" +
                "        \"external_position\": \"产品经理\",\n" +
                "        \"external_profile\": {\n" +
                "            \"external_corp_name\": \"企业简称\",\n" +
                "            \"external_attr\": [{\n" +
                "                    \"type\": 0,\n" +
                "                    \"name\": \"文本名称\",\n" +
                "                    \"text\": {\n" +
                "                        \"value\": \"文本\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"type\": 1,\n" +
                "                    \"name\": \"网页名称\",\n" +
                "                    \"web\": {\n" +
                "                        \"url\": \"http://www.test.com\",\n" +
                "                        \"title\": \"标题\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"type\": 2,\n" +
                "                    \"name\": \"测试app\",\n" +
                "                    \"miniprogram\": {\n" +
                "                        \"appid\": \"wx8bd80126147df384\",\n" +
                "                        \"pagepath\": \"/index\",\n" +
                "                        \"title\": \"my miniprogram\"\n" +
                "                    }\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    }\n" +
                "\n";

        WwUser user = JSON.parseObject(jsonStr, WwUser.class);

        assertNotNull(user);

        assertEquals(0, user.getErrcode());
        assertEquals("ok", user.getErrmsg());

        assertEquals("zhangsan", user.getUserid());
        assertEquals("李四", user.getName());
        assertEquals("后台工程师", user.getPosition());
        assertEquals("15913215421", user.getMobile());
        assertEquals("1", user.getGender());
        assertEquals("zhangsan@gzdev.com", user.getEmail());
        assertEquals("020-123456", user.getTelephone());
        assertEquals("jackzhang", user.getAlias());
        assertEquals("广州市海珠区新港中路", user.getAddress());
        assertEquals("产品经理", user.getExternalPosition());
        assertEquals("https://open.work.weixin.qq.com/wwopen/userQRCode?vcode=xxx", user.getQrCode());
        assertEquals(1, user.getEnable());
        assertEquals(1, user.getStatus());

        assertEquals(1, user.getDepartment()[0]);
        assertEquals(2, user.getDepartment()[1]);

        assertEquals(1, user.getOrder()[0]);
        assertEquals(2, user.getOrder()[1]);

        assertEquals(1, user.getIsLeader()[0]);
        assertEquals(0, user.getIsLeader()[1]);

        assertEquals("http://wx.qlogo.cn/mmopen/ajNVdqHZLLA3WJ6DSZUfiakYe37PKnQhBIeOQBO4czqrnZDS79FH5Wm5m4X69TBicnHFlhiafvDwklOpZeXYQQ2icg/0", user.getAvatar());


//                "extattr": {
//            "attrs": [
//            {
//                "type": 0,
//                    "name": "文本名称",
//                    "text": {
//                "value": "文本"
//            }
//            },
//            {
//                "type": 1,
//                    "name": "网页名称",
//                    "web": {
//                "url": "http://www.test.com",
//                        "title": "标题"
//            }
//            }
//            ]
//        },
//                "external_profile": {
//            "external_corp_name": "企业简称",
//                    "external_attr": [{
//                "type": 0,
//                        "name": "文本名称",
//                        "text": {
//                    "value": "文本"
//                }
//            },
//            {
//                "type": 1,
//                    "name": "网页名称",
//                    "web": {
//                "url": "http://www.test.com",
//                        "title": "标题"
//            }
//            },
//            {
//                "type": 2,
//                    "name": "测试app",
//                    "miniprogram": {
//                "appid": "wx8bd80126147df384",
//                        "pagepath": "/index",
//                        "title": "my miniprogram"
//            }
//            }
//            ]
//        }
//        }


    }
}