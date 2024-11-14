package cn.buk.api.wechat.dto;

import com.alibaba.fastjson.annotation.JSONField;

/**
 *
 * @author yfdai
 *  2017/5/1
 */
public class WxTemplateData {

    private WxTemplateDataParam first;

    private WxTemplateDataParam keyword1;
    private WxTemplateDataParam keyword2;
    private WxTemplateDataParam keyword3;

    @JSONField(name = "OrderID")
    private WxTemplateDataParam orderId;

    @JSONField(name = "PersonName")
    private WxTemplateDataParam personName;

    @JSONField(name = "FlightInfor")
    private WxTemplateDataParam flightInfo;

    @JSONField(name = "Amount")
    private WxTemplateDataParam amount;

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

    public WxTemplateDataParam getOrderId() {
        return orderId;
    }

    public void setOrderId(WxTemplateDataParam orderId) {
        this.orderId = orderId;
    }

    public WxTemplateDataParam getPersonName() {
        return personName;
    }

    public void setPersonName(WxTemplateDataParam personName) {
        this.personName = personName;
    }

    public WxTemplateDataParam getFlightInfo() {
        return flightInfo;
    }

    public void setFlightInfo(WxTemplateDataParam flightInfo) {
        this.flightInfo = flightInfo;
    }

    public WxTemplateDataParam getAmount() {
        return amount;
    }

    public void setAmount(WxTemplateDataParam amount) {
        this.amount = amount;
    }
}
