package cn.buk.api.wechat.dto;

import java.util.List;

/**
 * Created by yfdai on 2017/2/23.
 */
public class WeixinMenuItem {

    private String name;

    private String type; // click view

    private String key; // type = click

    private String url; // type = view

    private List<WeixinMenuItem> sub_button;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<WeixinMenuItem> getSub_button() {
        return sub_button;
    }

    public void setSub_button(List<WeixinMenuItem> sub_button) {
        this.sub_button = sub_button;
    }
}



