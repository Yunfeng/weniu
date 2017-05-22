package cn.buk.api.wechat.dto;

/**
 * Created by yfdai on 2017/5/1.
 */
public class WxTemplate {

    private String template_id;
    private String title;
    private String content;

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
