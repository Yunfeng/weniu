package cn.buk.api.wechat.dto;

/**
 * Created by yfdai on 2017/2/24.
 */
public class WeixinMediasRequest {

    private String type;

    private int offset;

    private int count;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
