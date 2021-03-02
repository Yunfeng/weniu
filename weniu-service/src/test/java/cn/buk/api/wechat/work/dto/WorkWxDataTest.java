package cn.buk.api.wechat.work.dto;

import cn.buk.api.wechat.dto.WwExternalContact;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WorkWxDataTest {

  @Test
  void test_WorkWxData_textMsg() {
    final String xml = "    <xml>\n" +
            "       <ToUserName><![CDATA[toUser]]></ToUserName>\n" +
            "       <FromUserName><![CDATA[fromUser]]></FromUserName> \n" +
            "       <CreateTime>1348831860</CreateTime>\n" +
            "       <MsgType><![CDATA[text]]></MsgType>\n" +
            "       <Content><![CDATA[this is a test]]></Content>\n" +
            "       <MsgId>1234567890123456</MsgId>\n" +
            "       <AgentID>1</AgentID>\n" +
            "    </xml>\n" +
            "\n";
    WorkWxData data = WorkWxData.fromXml(xml);
    assertEquals("toUser", data.getToUserName());
    assertEquals("fromUser", data.getFromUserName());
    assertEquals((Long)1348831860l, data.getCreateTime());
    assertEquals("text", data.getMsgType());
    assertEquals("this is a test", data.getContent());
    assertEquals(1234567890123456l, data.getMsgId());
    assertEquals(1, data.getAgentId());
  }

  @Test
  void test_WorkWxData_voiceMsg() {
    final String xml = "    <xml>\n" +
            "       <ToUserName><![CDATA[toUser]]></ToUserName>\n" +
            "       <FromUserName><![CDATA[fromUser]]></FromUserName>\n" +
            "       <CreateTime>1357290913</CreateTime>\n" +
            "       <MsgType><![CDATA[voice]]></MsgType>\n" +
            "       <MediaId><![CDATA[media_id]]></MediaId>\n" +
            "       <Format><![CDATA[Format]]></Format>\n" +
            "       <MsgId>1234567890123456</MsgId>\n" +
            "       <AgentID>1</AgentID>\n" +
            "    </xml>\n" +
            "\n";
    WorkWxData data = WorkWxData.fromXml(xml);
    assertEquals("toUser", data.getToUserName());
    assertEquals("fromUser", data.getFromUserName());
    assertEquals((Long)1357290913l, data.getCreateTime());
    assertEquals("voice", data.getMsgType());
    assertEquals("media_id", data.getMediaId());
    assertEquals("Format", data.getMediaFormat());
    assertEquals(1234567890123456l, data.getMsgId());
    assertEquals(1, data.getAgentId());
  }

  @Test
  void test_WorkWxData_videoMsg() {
    final String xml = "    <xml>\n" +
            "       <ToUserName><![CDATA[toUser]]></ToUserName>\n" +
            "       <FromUserName><![CDATA[fromUser]]></FromUserName>\n" +
            "       <CreateTime>1357290913</CreateTime>\n" +
            "       <MsgType><![CDATA[video]]></MsgType>\n" +
            "       <MediaId><![CDATA[media_id]]></MediaId>\n" +
            "       <ThumbMediaId><![CDATA[thumb_media_id]]></ThumbMediaId>\n" +
            "       <MsgId>1234567890123456</MsgId>\n" +
            "       <AgentID>1</AgentID>\n" +
            "    </xml>\n" +
            "\n";
    WorkWxData data = WorkWxData.fromXml(xml);
    assertEquals("toUser", data.getToUserName());
    assertEquals("fromUser", data.getFromUserName());
    assertEquals((Long)1357290913l, data.getCreateTime());
    assertEquals("video", data.getMsgType());
    assertEquals("media_id", data.getMediaId());
    assertEquals("thumb_media_id", data.getThumbMediaId());
    assertEquals(1234567890123456l, data.getMsgId());
    assertEquals(1, data.getAgentId());
  }

  @Test
  void test_WorkWxData_locationMsg() {
    final String xml = "    <xml>\n" +
            "       <ToUserName><![CDATA[toUser]]></ToUserName>\n" +
            "       <FromUserName><![CDATA[fromUser]]></FromUserName>\n" +
            "       <CreateTime>1351776360</CreateTime>\n" +
            "       <MsgType><![CDATA[location]]></MsgType>\n" +
            "       <Location_X>23.134</Location_X>\n" +
            "       <Location_Y>113.358</Location_Y>\n" +
            "       <Scale>20</Scale>\n" +
            "       <Label><![CDATA[位置信息]]></Label>\n" +
            "       <MsgId>1234567890123456</MsgId>\n" +
            "       <AgentID>1</AgentID>\n" +
            "       <AppType><![CDATA[wxwork]]></AppType>\n" +
            "    </xml>\n" +
            "\n";
    WorkWxData data = WorkWxData.fromXml(xml);
    assertEquals("toUser", data.getToUserName());
    assertEquals("fromUser", data.getFromUserName());
    assertEquals((Long)1351776360l, data.getCreateTime());
    assertEquals("location", data.getMsgType());
    assertEquals(23.134, data.getLatitude());
    assertEquals(113.358, data.getLongitude());
    assertEquals(20, data.getScale());
    assertEquals("位置信息", data.getLocationInfo());
    assertEquals((Long)1234567890123456l, data.getMsgId());
    assertEquals(1, data.getAgentId());
    assertEquals("wxwork", data.getAppType());
  }

  @Test
  void test_WorkWxData_linkMsg() {
    final String xml = "    <xml>\n" +
            "       <ToUserName><![CDATA[toUser]]></ToUserName>\n" +
            "       <FromUserName><![CDATA[fromUser]]></FromUserName> \n" +
            "       <CreateTime>1348831860</CreateTime>\n" +
            "       <MsgType><![CDATA[link]]></MsgType>\n" +
            "       <Title><![CDATA[this is a title！]]></Title>\n" +
            "       <Description><![CDATA[this is a description！]]></Description>\n" +
            "       <Url><![CDATA[URL]]></Url>\n" +
            "       <PicUrl><![CDATA[this is a url]]></PicUrl>\n" +
            "       <MsgId>1234567890123456</MsgId>\n" +
            "       <AgentID>1</AgentID>\n" +
            "    </xml>\n" +
            "\n";
    WorkWxData data = WorkWxData.fromXml(xml);
    assertEquals("toUser", data.getToUserName());
    assertEquals("fromUser", data.getFromUserName());
    assertEquals((Long)1348831860l, data.getCreateTime());
    assertEquals("link", data.getMsgType());
    assertEquals("this is a title！", data.getTitle());
    assertEquals("this is a description！", data.getDescription());
    assertEquals("URL", data.getUrl());
    assertEquals("this is a url", data.getPicUrl());
    assertEquals((Long)1234567890123456l, data.getMsgId());
    assertEquals(1, data.getAgentId());
  }

  @Test
  void test_WorkWxData_subscribeEvent() {
    final String xml = "    <xml>\n" +
            "        <ToUserName><![CDATA[toUser]]></ToUserName>\n" +
            "        <FromUserName><![CDATA[UserID]]></FromUserName>\n" +
            "        <CreateTime>1348831860</CreateTime>\n" +
            "        <MsgType><![CDATA[event]]></MsgType>\n" +
            "        <Event><![CDATA[subscribe]]></Event>\n" +
            "        <AgentID>1</AgentID>\n" +
            "    </xml>\n" +
            "\n";
    WorkWxData data = WorkWxData.fromXml(xml);
    assertEquals("toUser", data.getToUserName());
    assertEquals("UserID", data.getFromUserName());
    assertEquals((Long)1348831860l, data.getCreateTime());
    assertEquals("event", data.getMsgType());
    assertEquals("subscribe", data.getEvent());
    assertEquals(1, data.getAgentId());
  }

  @Test
  void test_WorkWxData_enterAgentEvent() {
    final String xml = "    <xml><ToUserName><![CDATA[toUser]]></ToUserName>\n" +
            "    <FromUserName><![CDATA[FromUser]]></FromUserName>\n" +
            "    <CreateTime>1408091189</CreateTime>\n" +
            "    <MsgType><![CDATA[event]]></MsgType>\n" +
            "    <Event><![CDATA[enter_agent]]></Event>\n" +
            "    <EventKey><![CDATA[]]></EventKey>\n" +
            "    <AgentID>1</AgentID>\n" +
            "    </xml>\n" +
            "\n";
    WorkWxData data = WorkWxData.fromXml(xml);
    assertEquals("toUser", data.getToUserName());
    assertEquals("FromUser", data.getFromUserName());
    assertEquals((Long)1408091189l, data.getCreateTime());
    assertEquals("event", data.getMsgType());
    assertEquals("enter_agent", data.getEvent());
    assertEquals("", data.getEventKey());
    assertEquals(1, data.getAgentId());
  }

  @Test
  void test_WorkWxData_locationEvent() {
    final String xml = "    <xml>\n" +
            "       <ToUserName><![CDATA[toUser]]></ToUserName>\n" +
            "       <FromUserName><![CDATA[FromUser]]></FromUserName>\n" +
            "       <CreateTime>123456789</CreateTime>\n" +
            "       <MsgType><![CDATA[event]]></MsgType>\n" +
            "       <Event><![CDATA[LOCATION]]></Event>\n" +
            "       <Latitude>23.104</Latitude>\n" +
            "       <Longitude>113.320</Longitude>\n" +
            "       <Precision>65.000</Precision>\n" +
            "       <AgentID>1</AgentID>\n" +
            "       <AppType><![CDATA[wxwork]]></AppType>\n" +
            "    </xml>\n" +
            "\n";
    WorkWxData data = WorkWxData.fromXml(xml);
    assertEquals("toUser", data.getToUserName());
    assertEquals("FromUser", data.getFromUserName());
    assertEquals((Long)123456789l, data.getCreateTime());
    assertEquals("event", data.getMsgType());
    assertEquals("LOCATION", data.getEvent());
    assertEquals(23.104, data.getLatitude());
    assertEquals(113.320, data.getLongitude());
    assertEquals(65.000, data.getPrecision());
    assertEquals(1, data.getAgentId());
    assertEquals("wxwork", data.getAppType());
  }

  /**
   * 任务卡片事件推送
   */
  @Test
  void test_WorkWxData_taskCardClickEvent() {
    final String xml = "    <xml>\n" +
            "    <ToUserName><![CDATA[toUser]]></ToUserName>\n" +
            "    <FromUserName><![CDATA[FromUser]]></FromUserName>\n" +
            "    <CreateTime>123456789</CreateTime>\n" +
            "    <MsgType><![CDATA[event]]></MsgType>\n" +
            "    <Event><![CDATA[taskcard_click]]></Event>\n" +
            "    <EventKey><![CDATA[key111]]></EventKey>\n" +
            "    <TaskId><![CDATA[taskid111]]></TaskId >\n" +
            "    <AgentId>1</AgentId>\n" +
            "    </xml>\n" +
            "\n";
    WorkWxData data = WorkWxData.fromXml(xml);
    assertEquals("toUser", data.getToUserName());
    assertEquals("FromUser", data.getFromUserName());
    assertEquals((Long)123456789l, data.getCreateTime());
    assertEquals("event", data.getMsgType());
    assertEquals("taskcard_click", data.getEvent());
    assertEquals("key111", data.getEventKey());
    assertEquals("taskid111", data.getTaskId());
    assertEquals(1, data.getAgentId());
  }

  @Test
  void textWxDataFromXml() {
    String xml = "<xml>\n" +
            "        <ToUserName><![CDATA[ww4asffe99e54c0f4c]]></ToUserName>\n" +
            "        <FromUserName><![CDATA[lisi]]></FromUserName>\n" +
            "        <CreateTime>1403610513</CreateTime>\n" +
            "        <MsgType><![CDATA[event]]></MsgType>\n" +
            "        <Event><![CDATA[change_external_contact]]></Event>\n" +
            "        <ChangeType><![CDATA[mark_as_customer]]></ChangeType>\n" +
            "        <Contact>\n" +
            "            <OpenId><![CDATA[wmEAlECwAAHrbWYDOK5u3Af13xlYDDNQ]]></OpenId>\n" +
            "            <CreateTime>1519981072</CreateTime>\n" +
            "            <Name><![CDATA[张三]]></Name>\n" +
            "            <Remark><![CDATA[张经理]]></Remark>\n" +
            "            <Description><![CDATA[购车顾问]]></Description>\n" +
            "            <Mobile><![CDATA[138008000]]></Mobile>\n" +
            "            <Email><![CDATA[test@qq.com]]></Email>\n" +
            "            <Position><![CDATA[Mangaer]]></Position>\n" +
            "            <CorpName><![CDATA[腾讯]]></CorpName>\n" +
            "            <CorpFullName><![CDATA[腾讯科技有限公司]]></CorpFullName>\n" +
            "            <Type>1</Type>\n" +
            "            <Gender>1</Gender>\n" +
            "            <ExternalProfile>\n" +
            "                <ExternalAttr>\n" +
            "                    <Type>0</Type>\n" +
            "                    <Name><![CDATA[文本名称]]></Name>\n" +
            "                    <Text>\n" +
            "                        <Value><![CDATA[ 文本]]></Value>\n" +
            "                    </Text>\n" +
            "                </ExternalAttr>\n" +
            "                <ExternalAttr>\n" +
            "                    <Type>1</Type>\n" +
            "                    <Name><![CDATA[网页名称]]></Name>\n" +
            "                    <Web>\n" +
            "                        <Url><![CDATA[ http://www.test.com]]></Url>\n" +
            "                        <Title><![CDATA[ 文本]]></Title>\n" +
            "                    </Web>\n" +
            "                </ExternalAttr>\n" +
            "                <ExternalAttr>\n" +
            "                    <Type>2</Type>\n" +
            "                    <Name><![CDATA[测试app]]></Name>\n" +
            "                    <MiniProgram>\n" +
            "                        <AppId><![CDATA[ wxfdsafas]]></AppId>\n" +
            "                        <PagePath><![CDATA[/index]]></PagePath>\n" +
            "                        <Title><![CDATA[/index]]></Title>\n" +
            "                    </MiniProgram>\n" +
            "                </ExternalAttr>\n" +
            "            </ExternalProfile>\n" +
            "        </Contact>\n" +
            "    </xml>";

    WorkWxData wxData = WorkWxData.fromXml(xml);

    assertEquals("mark_as_customer", wxData.getChangeType());

    WwExternalContact c = wxData.getExternalContact();
    assertNotNull(c);
    assertEquals("test@qq.com", c.getEmail());
  }
}