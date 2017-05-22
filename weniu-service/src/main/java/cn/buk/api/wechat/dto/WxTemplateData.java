package cn.buk.api.wechat.dto;

/**
 * Created by yfdai on 2017/5/1.
 */
public class WxTemplateData {

    private WxTemplateDataParam first;
    private WxTemplateDataParam keyword1;
    private WxTemplateDataParam keyword2;
    private WxTemplateDataParam keyword3;
    private WxTemplateDataParam remark;

    public WxTemplateDataParam getFirst() {
        return first;
    }

    public void setFirst(WxTemplateDataParam first) {
        this.first = first;
    }

    public WxTemplateDataParam getKeyword1() {
        return keyword1;
    }

    public void setKeyword1(WxTemplateDataParam keyword1) {
        this.keyword1 = keyword1;
    }

    public WxTemplateDataParam getKeyword2() {
        return keyword2;
    }

    public void setKeyword2(WxTemplateDataParam keyword2) {
        this.keyword2 = keyword2;
    }

    public WxTemplateDataParam getKeyword3() {
        return keyword3;
    }

    public void setKeyword3(WxTemplateDataParam keyword3) {
        this.keyword3 = keyword3;
    }

    public WxTemplateDataParam getRemark() {
        return remark;
    }

    public void setRemark(WxTemplateDataParam remark) {
        this.remark = remark;
    }
}
