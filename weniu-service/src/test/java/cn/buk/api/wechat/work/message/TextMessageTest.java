package cn.buk.api.wechat.work.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextMessageTest {

  @Test
  void test_TextMessage() {
    TextMessage txtMsg = new TextMessage();
    System.out.println(JSON.toJSONString(txtMsg));

    final String jsonStr = "    {\n" +
            "       \"touser\" : \"UserID1|UserID2|UserID3\",\n" +
            "       \"toparty\" : \"PartyID1|PartyID2\",\n" +
            "       \"totag\" : \"TagID1 | TagID2\",\n" +
            "       \"msgtype\" : \"text\",\n" +
            "       \"agentid\" : 1,\n" +
            "       \"text\" : {\n" +
            "           \"content\" : \"你的快递已到，请携带工卡前往邮件中心领取。\\n出发前可查看<a href=\\\"http://work.weixin.qq.com\\\">邮件中心视频实况</a>，聪明避开排队。\"\n" +
            "       },\n" +
            "       \"safe\":0,\n" +
            "       \"enable_id_trans\": 0,\n" +
            "       \"enable_duplicate_check\": 0,\n" +
            "       \"duplicate_check_interval\": 1800\n" +
            "    }\n" +
            "\n";
    TextMessage msg = JSON.parseObject(jsonStr, TextMessage.class);
    assertEquals("UserID1|UserID2|UserID3", msg.getTouser());
    assertEquals("PartyID1|PartyID2", msg.getToparty());
    assertEquals("TagID1 | TagID2", msg.getTotag());
    assertEquals("text", msg.getMsgtype());
    assertEquals(1, msg.getAgentid());
    assertEquals(0, msg.getSafe());
    assertEquals(0, msg.getEnableIdTrans());
    assertEquals(0, msg.getEnableDuplicateCheck());
    assertEquals(1800, msg.getDuplicateCheckInterval());

    System.out.println(JSON.toJSONString(msg));
  }
}