package cn.buk.api.wechat.dto;

import org.dom4j.Element;

import java.util.Iterator;

public class WwExternalContact {

    public static WwExternalContact createByElement(Element root) {
        System.out.println(root.getName());

        if (!"Contact".equalsIgnoreCase(root.getName())) return null;

        WwExternalContact obj = new WwExternalContact();

        for (Iterator i = root.elementIterator(); i.hasNext(); ) {
            Element element = (Element) i.next();
            final String elementName = element.getName();
            final String temp = element.getStringValue();

            if (elementName.compareToIgnoreCase("OpenId") == 0) {
                obj.setOpenId(temp);
            } else if (elementName.compareToIgnoreCase("Name") == 0) {
                obj.setName(temp);
            } else if (elementName.compareToIgnoreCase("Remark") == 0) {
                obj.setRemark(temp);
            } else if (elementName.compareToIgnoreCase("Description") == 0) {
                obj.setDescription(temp);
            } else if (elementName.compareToIgnoreCase("Mobile") == 0) {
                obj.setMobile(temp);
            } else if (elementName.compareToIgnoreCase("email") == 0) {
                obj.setEmail(temp);
            } else if (elementName.compareToIgnoreCase("Position") == 0) {
                obj.setPosition(temp);
            } else if (elementName.compareToIgnoreCase("CorpName") == 0) {
                obj.setCorpName(temp);
            } else if (elementName.compareToIgnoreCase("CorpFullName") == 0) {
                obj.setCorpFullName(temp);
            } else if (elementName.compareToIgnoreCase("Type") == 0) {
                obj.setType(Integer.parseInt(temp));
            } else if (elementName.compareToIgnoreCase("Gender") == 0) {
                obj.setGender(Integer.parseInt(temp));
            } else if (elementName.compareToIgnoreCase("CreateTime") == 0) {
                obj.setCreateTime(Long.parseLong(temp));
            }
        }

        return obj;
    }

    private String openId;

    private Long createTime;

    private String name;

    private String remark;

    private String description;

    private String mobile;

    private String email;

    private String position;

    private String corpName;

    private String corpFullName;

    private int type; // 1- 微信用户, 2-企业微信用户

    private int gender; // 1- 男， 2- 女

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getCorpFullName() {
        return corpFullName;
    }

    public void setCorpFullName(String corpFullName) {
        this.corpFullName = corpFullName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

//                    "        <Contact>\n" +
//                            "            <OpenId><![CDATA[wmEAlECwAAHrbWYDOK5u3Af13xlYDDNQ]]></OpenId>\n" +
//                            "            <CreateTime>1519981072</CreateTime>\n" +
//                            "            <Name><![CDATA[张三]]></Name>\n" +
//                            "            <Remark><![CDATA[张经理]]></Remark>\n" +
//                            "            <Description><![CDATA[购车顾问]]></Description>\n" +
//                            "            <Mobile><![CDATA[138008000]]></Mobile>\n" +
//                            "            <Email><![CDATA[test@qq.com]]></Email>\n" +
//                            "            <Position><![CDATA[Mangaer]]></Position>\n" +
//                            "            <CorpName><![CDATA[腾讯]]></CorpName>\n" +
//                            "            <CorpFullName><![CDATA[腾讯科技有限公司]]></CorpFullName>\n" +
//                            "            <Type>1</Type>\n" +
//                            "            <Gender>1</Gender>\n" +
//                            "            <ExternalProfile>\n" +
//                            "                <ExternalAttr>\n" +
//                            "                    <Type>0</Type>\n" +
//                            "                    <Name><![CDATA[文本名称]]></Name>\n" +
//                            "                    <Text>\n" +
//                            "                        <Value><![CDATA[ 文本]]></Value>\n" +
//                            "                    </Text>\n" +
//                            "                </ExternalAttr>\n" +
//                            "                <ExternalAttr>\n" +
//                            "                    <Type>1</Type>\n" +
//                            "                    <Name><![CDATA[网页名称]]></Name>\n" +
//                            "                    <Web>\n" +
//                            "                        <Url><![CDATA[ http://www.test.com]]></Url>\n" +
//                            "                        <Title><![CDATA[ 文本]]></Title>\n" +
//                            "                    </Web>\n" +
//                            "                </ExternalAttr>\n" +
//                            "                <ExternalAttr>\n" +
//                            "                    <Type>2</Type>\n" +
//                            "                    <Name><![CDATA[测试app]]></Name>\n" +
//                            "                    <MiniProgram>\n" +
//                            "                        <AppId><![CDATA[ wxfdsafas]]></AppId>\n" +
//                            "                        <PagePath><![CDATA[/index]]></PagePath>\n" +
//                            "                        <Title><![CDATA[/index]]></Title>\n" +
//                            "                    </MiniProgram>\n" +
//                            "                </ExternalAttr>\n" +
//                            "            </ExternalProfile>\n" +
//                            "        </Contact>\n" +
}
