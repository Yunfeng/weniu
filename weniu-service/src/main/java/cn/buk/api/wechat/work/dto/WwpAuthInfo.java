package cn.buk.api.wechat.work.dto;

/**
 * 授权信息
 */
public class WwpAuthInfo {

    private WwpSuiteAuthInfo[] agent;

    public WwpSuiteAuthInfo[] getAgent() {
        return agent;
    }

    public void setAgent(WwpSuiteAuthInfo[] agent) {
        this.agent = agent;
    }

//    agent 	授权的应用信息，注意是一个数组，但仅旧的多应用套件授权时会返回多个agent，对新的单应用授权，永远只返回一个agent

//            "agent" :
//                    [
//    {
//        "agentid":1,
//            "name":"NAME",
//            "round_logo_url":"xxxxxx",
//            "square_logo_url":"yyyyyy",
//            "appid":1,
//            "privilege":
//        {
//            "level":1,
//                "allow_party":[1,2,3],
//            "allow_user":["zhansan","lisi"],
//            "allow_tag":[1,2,3],
//            "extra_party":[4,5,6],
//            "extra_user":["wangwu"],
//            "extra_tag":[4,5,6]
//        }
//    },
//    {
//        "agentid":2,
//            "name":"NAME2",
//            "round_logo_url":"xxxxxx",
//            "square_logo_url":"yyyyyy",
//            "appid":5
//    }
//        ]
}
