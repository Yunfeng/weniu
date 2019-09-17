package cn.buk.api.wechat.service;

import cn.buk.api.wechat.dto.WxMaterials;
import cn.buk.api.wechat.util.EncoderHandler;
import cn.buk.api.wechat.work.dto.BaseAttr;
import cn.buk.api.wechat.work.dto.WwUser;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qq.weixin.mp.aes.AesException;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.qq.weixin.mp.aes.SHA1.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by yfdai on 2017/6/7.
 */
public class WeixinServiceTest {

    private WeixinService weixinService;

    @Before
    public void setup() {
        weixinService = new WeixinServiceImpl();
    }

    @Test
    public void testConvertErJson() throws Exception {
        String val = "{\"errcode\":40007,\"errmsg\":\"invalid media_id\"}";

        WxMaterials materials = JSON.parseObject(val, WxMaterials.class);

        assertEquals(40007, materials.getErrcode());
        assertEquals("invalid media_id", materials.getErrmsg());
    }

    @Test
    public void testConvertMaterialsJson() throws Exception {
        String val = " {\"item\":[" +
                "{" +
                "\"media_id\":\"D66eMY48x0SJaUhRU4GgitN49Vnj1wt-Ys7rNwA01sc\"," +
                "\"name\":\"???.jpg\",\"update_time\":1496889154," +
                "\"url\":\"?wx_fmt=jpeg\"}," +
                "{\"media_id\":\"D66eMY48x0SJaUhRU4Ggit8zfTV1_n-rkWxxeBeEqbM\",\"name\":\"???.jpg\",\"update_time\":1496847722,\"url\":\"http:\\/\\/mmbiz.qpic.cn\\/mmbiz_jpg\\/5JPNn1NvhAo0WsvfibibG7Qpib8NiaQlMmESSkK5cEGicjBs5ichmicH9jdAIv8aIyNekl7jwlGGm2IGvUr92MGw8Uq9g\\/0?wx_fmt=jpeg\"},{\"media_id\":\"D66eMY48x0SJaUhRU4Ggim_CU8juDucW3ItFZziM74c\",\"name\":\"???.jpg\",\"update_time\":1496847667,\"url\":\"http:\\/\\/mmbiz.qpic.cn\\/mmbiz_jpg\\/5JPNn1NvhAo0WsvfibibG7Qpib8NiaQlMmESAh2FbsAqgRicNibU1Xv0tp2xJ6wRX4EQ1YCiag4Jb4geZzibv4oDFiaJSEg\\/0?wx_fmt=jpeg\"},{\"media_id\":\"D66eMY48x0SJaUhRU4GgiiHiTPg7Li5VHcL1d6cz4Vs\",\"name\":\"???_????.jpg\",\"update_time\":1491813549,\"url\":\"http:\\/\\/mmbiz.qpic.cn\\/mmbiz_jpg\\/5JPNn1NvhArEK1hNiaiacHAZSGIiaKiayia40aWxQ0O0j19GodTDcpfA3mGUicjRRS4BBDhFTnibBYl6vzEY3PT2Hxccg\\/0?wx_fmt=jpeg\"},{\"media_id\":\"D66eMY48x0SJaUhRU4GgimLJer8hKY9GJ_Mri0azJvE\",\"name\":\"yh_qrcode\",\"update_time\":1491813540,\"url\":\"http:\\/\\/mmbiz.qpic.cn\\/mmbiz_jpg\\/5JPNn1NvhArEK1hNiaiacHAZSGIiaKiayia40xZlqzpktfqg8B7T7qYCoXmniavKK1XI1ekmBQBvJKgVVlkWtj2NKBMQ\\/0?wx_fmt=jpeg\"}" +
                "],\"total_count\":5,\"item_count\":5}";
        WxMaterials materials = JSON.parseObject(val, WxMaterials.class);

        assertEquals(5, materials.getTotal_count());
        assertEquals(5, materials.getItem_count());

        assertEquals("D66eMY48x0SJaUhRU4GgitN49Vnj1wt-Ys7rNwA01sc", materials.getItem().get(0).getMedia_id());
    }

    @Test
    public void testConvertNewsJson() throws Exception {
        String val = "{\"item\":[{\"media_id\":\"D66eMY48x0SJaUhRU4GgijkkycnV7l9ZZ6fbE88tb48\",\"content\":{\"news_item\":[{\"title\":\"?????????????1000??\",\"author\":\"??\",\"digest\":\"??????????????????????????????????????????????????????\",\"content\":\"<h1>???????????????????????????????????????????<br  \\/><\\/h1><h1><br  \\/><\\/h1><h1>???????????????????????????????????????????????????????<\\/h1><p><img data-s=\\\"300,640\\\" data-type=\\\"jpeg\\\" data-src=\\\"http:\\/\\/mmbiz.qpic.cn\\/mmbiz_jpg\\/5JPNn1NvhArEK1hNiaiacHAZSGIiaKiayia40aWxQ0O0j19GodTDcpfA3mGUicjRRS4BBDhFTnibBYl6vzEY3PT2Hxccg\\/0?wx_fmt=jpeg\\\" data-ratio=\\\"0.635\\\" data-w=\\\"600\\\"  \\/><\\/p><p style=\\\"max-width: 100%; min-height: 1em; color: rgb(62, 62, 62); font-size: 16px; line-height: 25.6px; white-space: normal; background-color: rgb(255, 255, 255); box-sizing: border-box !important; overflow-wrap: break-word !important; text-align: center;\\\"><span style=\\\"max-width: 100%; font-size: 14px; box-sizing: border-box !important; overflow-wrap: break-word !important;\\\">&nbsp;??? ?????????????<\\/span><\\/p><h1><br  \\/><\\/h1><h1>???????????????????????????????????????<\\/h1><h1><br  \\/><\\/h1><h1>???????????????????????......<\\/h1><p style=\\\"text-align:center\\\"><img data-s=\\\"300,640\\\" data-type=\\\"jpeg\\\" data-src=\\\"http:\\/\\/mmbiz.qpic.cn\\/mmbiz_jpg\\/5JPNn1NvhArEK1hNiaiacHAZSGIiaKiayia40xZlqzpktfqg8B7T7qYCoXmniavKK1XI1ekmBQBvJKgVVlkWtj2NKBMQ\\/0?wx_fmt=jpeg\\\" data-ratio=\\\"1\\\" data-w=\\\"258\\\"  \\/><\\/p><p style=\\\"text-align: center;\\\">?????????????????<\\/p><p><br  \\/><\\/p>\",\"content_source_url\":\"\",\"thumb_media_id\":\"KW5PWYiTMGbck-mPUPdFiLe69Q7jjE3KMovxzxxxaBg\",\"show_cover_pic\":0,\"url\":\"http:\\/\\/mp.weixin.qq.com\\/s?__biz=MzA4OTA5ODUyMg==&mid=100000005&idx=1&sn=0d5ff8c7b0987126ae8d15b549864704&chksm=102151702756d8669d0ab41b8ea7628b26b82556a080ec1abf6b8befd1b5c1adfb2d734f314a#rd\",\"thumb_url\":\"http:\\/\\/mmbiz.qpic.cn\\/mmbiz_jpg\\/5JPNn1NvhArEK1hNiaiacHAZSGIiaKiayia40aWxQ0O0j19GodTDcpfA3mGUicjRRS4BBDhFTnibBYl6vzEY3PT2Hxccg\\/0?wx_fmt=jpeg\",\"need_open_comment\":0,\"only_fans_can_comment\":0}],\"create_time\":1491813850,\"update_time\":1491813938},\"update_time\":1491813938}],\"total_count\":1,\"item_count\":1}";
        WxMaterials materials = JSON.parseObject(val, WxMaterials.class);

        assertEquals(1, materials.getTotal_count());
        assertEquals(1, materials.getItem_count());

        assertEquals("D66eMY48x0SJaUhRU4GgijkkycnV7l9ZZ6fbE88tb48", materials.getItem().get(0).getMedia_id());

        assertEquals(1, materials.getItem().get(0).getContent().getNews_item().size());
        assertEquals("KW5PWYiTMGbck-mPUPdFiLe69Q7jjE3KMovxzxxxaBg", materials.getItem().get(0).getContent().getNews_item().get(0).getThumb_media_id());
    }


    @Test
    public void testJson() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", "value");
        JSONObject obj = new JSONObject(map);

//        System.out.println(obj.toJSONString());
    }

    @Test
    public void testConvertJson2User() {
        final String jsonStr = "    {\n" +
                "       \"errcode\": 0,\n" +
                "       \"errmsg\": \"ok\",\n" +
                "       \"userid\": \"zhangsan\",\n" +
                "       \"name\": \"李四\",\n" +
                "       \"department\": [1, 2],\n" +
                "       \"order\": [1, 2],\n" +
                "       \"position\": \"后台工程师\",\n" +
                "       \"mobile\": \"15913215421\",\n" +
                "       \"gender\": \"1\",\n" +
                "       \"email\": \"zhangsan@gzdev.com\",\n" +
                "       \"isleader\": 1,\n" +
                "       \"avatar\": \"http://wx.qlogo.cn/mmopen/ajNVdqHZLLA3WJ6DSZUfiakYe37PKnQhBIeOQBO4czqrnZDS79FH5Wm5m4X69TBicnHFlhiafvDwklOpZeXYQQ2icg/0\",\n" +
                "       \"telephone\": \"020-123456\",\n" +
                "       \"english_name\": \"jackzhang\",\n" +
                "       \"extattr\": {\"attrs\":[{\"name\":\"爱好\",\"value\":\"旅游\"},{\"name\":\"卡号\",\"value\":\"1234567234\"}]},\n" +
                "       \"status\": 1\n" +
                "    }";

//        jsonStr = "{\"enable\":0,\"errcode\":0,\"extattr\":{\"attrs\":[{\"name\":\"eterm-id\",\"value\":\"eterm-username\"}]},\"isleader\":0,\"mobile\":\"15618206323\",\"userid\":\"123\"}";

//        System.out.println(jsonStr);

        WwUser user = JSON.parseObject(jsonStr, WwUser.class);

        assertEquals("zhangsan", user.getUserid());
        assertEquals("15913215421", user.getMobile());

        assertNotNull(user.getExtattr());

        assertEquals("爱好", user.getExtattr().getAttrs().get(0).getName());
        assertEquals("旅游", user.getExtattr().getAttrs().get(0).getValue());
    }

    @Test
    public void testConvertUser2Json() {
        WwUser user = new WwUser();
        user.setUserid("123");
        user.setMobile("15618206323");

        BaseAttr attr1 = new BaseAttr();
        attr1.setName("eterm-id");
        attr1.setValue("eterm-username");
        user.getExtattr().getAttrs().add(attr1);

//        System.out.println(JSON.toJSONString(user));
    }

    @Test
    public void testSha1Method() {
        String token = "fa";
        String timeStamp = "bfff";
        String nonce = "caaa";
        String echoStr = "dddd";

        try {
            String signature1 = getSHA1(token, timeStamp, nonce, echoStr);
            System.out.println(signature1);

            ArrayList<String> al = new ArrayList<>();
            al.add(token);
            al.add(timeStamp);
            al.add(nonce);
            al.add(echoStr);
            Collections.sort(al);

            String allString = "";
            for (String temp : al) {
                allString += temp;
            }

            System.out.println(allString);

            String signature2 = EncoderHandler.encode("SHA1", allString);
//            System.out.println(signature2);
        } catch (AesException e) {
            e.printStackTrace();
        }
    }



}