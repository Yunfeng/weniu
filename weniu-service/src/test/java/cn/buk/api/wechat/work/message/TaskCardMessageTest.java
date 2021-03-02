package cn.buk.api.wechat.work.message;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskCardMessageTest {

  @Test
  void test_TaskCardMessage() {
    final String jsonStr = "    {\n" +
            "       \"touser\" : \"UserID1|UserID2|UserID3\",\n" +
            "       \"toparty\" : \"PartyID1 | PartyID2\",\n" +
            "       \"totag\" : \"TagID1 | TagID2\",\n" +
            "       \"msgtype\" : \"taskcard\",\n" +
            "       \"agentid\" : 1,\n" +
            "       \"taskcard\" : {\n" +
            "                \"title\" : \"赵明登的礼物申请\",\n" +
            "                \"description\" : \"礼品：A31茶具套装<br>用途：赠与小黑科技张总经理\",\n" +
            "                \"url\" : \"URL\",\n" +
            "                \"task_id\" : \"taskid123\",\n" +
            "                \"btn\":[\n" +
            "                    {\n" +
            "                        \"key\": \"key111\",\n" +
            "                        \"name\": \"批准\",\n" +
            "                        \"replace_name\": \"已批准\",\n" +
            "                        \"color\":\"red\",\n" +
            "                        \"is_bold\": true\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"key\": \"key222\",\n" +
            "                        \"name\": \"驳回\",\n" +
            "                        \"replace_name\": \"已驳回\"\n" +
            "                    }\n" +
            "                ]\n" +
            "       },\n" +
            "       \"enable_id_trans\": 0,\n" +
            "       \"enable_duplicate_check\": 0,\n" +
            "       \"duplicate_check_interval\": 1800\n" +
            "    }\n" +
            "\n";

    TaskCardMessage msg = JSON.parseObject(jsonStr, TaskCardMessage.class);

    assertEquals("赵明登的礼物申请", msg.getTaskCard().getTitle());
    assertEquals("礼品：A31茶具套装<br>用途：赠与小黑科技张总经理", msg.getTaskCard().getDescription());
    assertEquals("URL", msg.getTaskCard().getUrl());
    assertEquals("taskid123", msg.getTaskCard().getTaskId());

    assertEquals(2, msg.getTaskCard().getButtonList().size());
    assertEquals("key111", msg.getTaskCard().getButtonList().get(0).getKey());
    assertEquals("批准", msg.getTaskCard().getButtonList().get(0).getName());
    assertEquals("已批准", msg.getTaskCard().getButtonList().get(0).getReplaceName());


    assertEquals("UserID1|UserID2|UserID3", msg.getTouser());
    assertEquals("PartyID1 | PartyID2", msg.getToparty());
    assertEquals("TagID1 | TagID2", msg.getTotag());
    assertEquals("taskcard", msg.getMsgtype());
    assertEquals(1, msg.getAgentid());
    assertEquals(0, msg.getEnableIdTrans());
    assertEquals(0, msg.getEnableDuplicateCheck());
    assertEquals(1800, msg.getDuplicateCheckInterval());

//    System.out.println(JSON.toJSONString(msg));
  }
}