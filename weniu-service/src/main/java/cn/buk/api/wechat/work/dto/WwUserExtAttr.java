package cn.buk.api.wechat.work.dto;

import java.util.ArrayList;
import java.util.List;

public class WwUserExtAttr {

    private List<BaseAttr> attrs;

    public List<BaseAttr> getAttrs() {
        if (attrs == null) attrs = new ArrayList<>();
        return attrs;
    }

    public void setAttrs(List<BaseAttr> attrs) {
        this.attrs = attrs;
    }
}
