package cn.buk.api.wechat.dto;

/**
 * Created by yfdai on 2017/5/1.
 */
public class WxTemplateSend {

    private String touser; //openid
    private String template_id;
    private String url;

    private WxTemplateData data;

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public WxTemplateData getData() {
        if (data == null) data = new WxTemplateData();
        return data;
    }

    public void setData(WxTemplateData data) {
        this.data = data;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
