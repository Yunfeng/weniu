package cn.buk.api.wechat.dto;

/**
 * Created by yfdai on 2017/5/1.
 */
public class WxTemplateDataParam {

    private String value;
    private String color;

    public static WxTemplateDataParam createByValue(String value) {
        WxTemplateDataParam param = new WxTemplateDataParam();
        param.setValue(value);
        return param;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
