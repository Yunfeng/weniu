package cn.buk.api.wechat.dto;

import org.junit.Test;

import static org.junit.Assert.*;

public class WxDataTest {

    @Test
    public void textWxDataFromXml() {
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

        WxData wxData = WxData.fromXml(xml);

        assertEquals("mark_as_customer", wxData.getChangeType());

        WwExternalContact c = wxData.getExternalContact();
        assertNotNull(c);
        assertEquals("test@qq.com", c.getEmail());
    }
}