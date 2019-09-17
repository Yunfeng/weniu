package cn.buk.api.wechat.work.dto;

/**
 * 授权企业信息
 */
public class WwpAuthCorpInfo {
    private String corpid;
    private String corp_name;
    private String corp_type;
    private String corp_square_logo_url;
    private String corp_full_name;
    private String corp_wxqrcode;
    private String corp_scale;
    private String corp_industry;
    private String corp_sub_industry;
    private int corp_user_max;
    private int corp_agent_max;
    private long verified_end_time;
    private int subject_type;

    public String getCorpid() {
        return corpid;
    }

    public void setCorpid(String corpid) {
        this.corpid = corpid;
    }

    public String getCorp_name() {
        return corp_name;
    }

    public void setCorp_name(String corp_name) {
        this.corp_name = corp_name;
    }

    public String getCorp_type() {
        return corp_type;
    }

    public void setCorp_type(String corp_type) {
        this.corp_type = corp_type;
    }

    public String getCorp_square_logo_url() {
        return corp_square_logo_url;
    }

    public void setCorp_square_logo_url(String corp_square_logo_url) {
        this.corp_square_logo_url = corp_square_logo_url;
    }

    public String getCorp_full_name() {
        return corp_full_name;
    }

    public void setCorp_full_name(String corp_full_name) {
        this.corp_full_name = corp_full_name;
    }

    public String getCorp_wxqrcode() {
        return corp_wxqrcode;
    }

    public void setCorp_wxqrcode(String corp_wxqrcode) {
        this.corp_wxqrcode = corp_wxqrcode;
    }

    public String getCorp_scale() {
        return corp_scale;
    }

    public void setCorp_scale(String corp_scale) {
        this.corp_scale = corp_scale;
    }

    public String getCorp_industry() {
        return corp_industry;
    }

    public void setCorp_industry(String corp_industry) {
        this.corp_industry = corp_industry;
    }

    public String getCorp_sub_industry() {
        return corp_sub_industry;
    }

    public void setCorp_sub_industry(String corp_sub_industry) {
        this.corp_sub_industry = corp_sub_industry;
    }

    public int getCorp_user_max() {
        return corp_user_max;
    }

    public void setCorp_user_max(int corp_user_max) {
        this.corp_user_max = corp_user_max;
    }

    public int getCorp_agent_max() {
        return corp_agent_max;
    }

    public void setCorp_agent_max(int corp_agent_max) {
        this.corp_agent_max = corp_agent_max;
    }

    public long getVerified_end_time() {
        return verified_end_time;
    }

    public void setVerified_end_time(long verified_end_time) {
        this.verified_end_time = verified_end_time;
    }

    public int getSubject_type() {
        return subject_type;
    }

    public void setSubject_type(int subject_type) {
        this.subject_type = subject_type;
    }
//    "corpid": "xxxx",
//            "corp_name": "name",
//            "corp_type": "verified",
//            "corp_square_logo_url": "yyyyy",
//            "corp_user_max": 50,
//            "corp_agent_max": 30,
//            "corp_full_name":"full_name",
//            "corp_agent_max":1431775834,
//            "subject_type": 1，
//            "corp_wxqrcode": "zzzzz",
//            "corp_scale": "1-50人",
//            "corp_industry": "IT服务",
//            "corp_sub_industry": "计算机软件/硬件/信息服务"
//    corpid 	授权方企业微信id
//    corp_name 	授权方企业微信名称
//    corp_type 	授权方企业微信类型，认证号：verified, 注册号：unverified
//    corp_square_logo_url 	授权方企业微信方形头像
//    corp_user_max 	授权方企业微信用户规模
//    corp_full_name 	所绑定的企业微信主体名称
//    subject_type 	企业类型，1. 企业; 2. 政府以及事业单位; 3. 其他组织, 4.团队号
//    verified_end_time 	认证到期时间
//    corp_wxqrcode 	授权方企业微信二维码
//    corp_scale 	企业规模。当企业未设置该属性时，值为空
//    corp_industry 	企业所属行业。当企业未设置该属性时，值为空
//    corp_sub_industry 	企业所属子行业。当企业未设置该属性时，值为空
}
