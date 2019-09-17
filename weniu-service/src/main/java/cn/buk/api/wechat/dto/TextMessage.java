package cn.buk.api.wechat.dto;

/**
 * Created by william on 2015-10-23.
 */
public class TextMessage {

//    touser 	否 	成员ID列表（消息接收者，多个接收者用‘|’分隔，最多支持1000个）。特殊情况：指定为@all，则向关注该企业应用的全部成员发送
//    toparty 	否 	部门ID列表，多个接收者用‘|’分隔，最多支持100个。当touser为@all时忽略本参数
//    totag 	否 	标签ID列表，多个接收者用‘|’分隔。当touser为@all时忽略本参数
//    msgtype 	是 	消息类型，此时固定为：text
//    agentid 	是 	企业应用的id，整型。可在应用的设置页面查看
//    content 	是 	消息内容
//    safe 	否 	表示是否是保密消息，0表示否，1表示是，默认0
//    {
//        "touser": "UserID1|UserID2|UserID3",
//            "toparty": " PartyID1 | PartyID2 ",
//            "totag": " TagID1 | TagID2 ",
//            "msgtype": "text",
//            "agentid": "1",
//            "text": {
//        "content": "Holiday Request For Pony(http://xxxxx)"
//    },
//        "safe":"0"
//    }

    private String touser;
    private String toparty;
    private String totag;
    private String msgtype = "text";
    private int agentid;
    private int safe = 0;
    private Text text = new Text();

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getToparty() {
        return toparty;
    }

    public void setToparty(String toparty) {
        this.toparty = toparty;
    }

    public String getTotag() {
        return totag;
    }

    public void setTotag(String totag) {
        this.totag = totag;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }


    public int getSafe() {
        return safe;
    }

    public void setSafe(int safe) {
        this.safe = safe;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public void setContent(String content) {
        this.text.setContent(content);
    }

    public int getAgentid() {
        return agentid;
    }

    public void setAgentid(int agentid) {
        this.agentid = agentid;
    }
}
