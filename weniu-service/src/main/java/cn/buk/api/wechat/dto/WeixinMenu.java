package cn.buk.api.wechat.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfdai on 2017/2/23.
 */
public class WeixinMenu {

    private List<WeixinMenuItem> button = new ArrayList<>();


    public List<WeixinMenuItem> getButton() {
        return button;
    }

    public void setButton(List<WeixinMenuItem> button) {
        this.button = button;
    }
}



