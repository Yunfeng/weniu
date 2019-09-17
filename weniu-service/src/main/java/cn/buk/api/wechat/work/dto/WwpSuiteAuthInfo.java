package cn.buk.api.wechat.work.dto;

/**
 * 授权信息
 */
public class WwpSuiteAuthInfo {

    private String agentid;
    private String name;
    private String square_logo_url;
    private String round_logo_url;

    private WwpSuitePrivilegeInfo privilege;


    //    agentid 	授权方应用id
//    name 	授权方应用名字
//    square_logo_url 	授权方应用方形头像
//    round_logo_url 	授权方应用圆形头像
//    appid 	旧的多应用套件中的对应应用id，新开发者请忽略
//    privilege 	应用对应的权限
//    allow_party 	应用可见范围（部门）
//    allow_tag 	应用可见范围（标签）
//    allow_user 	应用可见范围（成员）
//    extra_party 	额外通讯录（部门）
//    extra_user 	额外通讯录（成员）
//    extra_tag 	额外通讯录（标签）
//    level 	权限等级。
//            1:通讯录基本信息只读
//2:通讯录全部信息只读
//3:通讯录全部信息读写
//4:单个基本信息只读
//5:通讯录全部信息只写

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

    public String getAgentid() {
        return agentid;
    }

    public void setAgentid(String agentid) {
        this.agentid = agentid;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSquare_logo_url() {
        return square_logo_url;
    }

    public void setSquare_logo_url(String square_logo_url) {
        this.square_logo_url = square_logo_url;
    }

    public String getRound_logo_url() {
        return round_logo_url;
    }

    public void setRound_logo_url(String round_logo_url) {
        this.round_logo_url = round_logo_url;
    }

    public WwpSuitePrivilegeInfo getPrivilege() {
        return privilege;
    }

    public void setPrivilege(WwpSuitePrivilegeInfo privilege) {
        this.privilege = privilege;
    }
}
