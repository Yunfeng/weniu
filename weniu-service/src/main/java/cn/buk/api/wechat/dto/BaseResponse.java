package cn.buk.api.wechat.dto;

/**
 *
 * @author yfdai
 * @date 2017/6/7
 */
public class BaseResponse {

    /**
     * 出错返回码，为0表示成功，非0表示调用失败
     */
    private int errcode;

    /**
     *  	返回码提示语
     */
    private String errmsg;

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}
