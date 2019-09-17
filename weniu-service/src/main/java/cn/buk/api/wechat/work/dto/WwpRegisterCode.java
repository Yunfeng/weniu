package cn.buk.api.wechat.work.dto;

/**
 * wwp - work weixin provider 企业微信服务商
 * 获取预授权码
 */
public class WwpRegisterCode extends BaseResponse {

    private String register_code; //注册码，只能消费一次。在访问注册链接时消费。最长为512个字节
    private int expires_in;  //register_code有效期，生成链接需要在有效期内点击跳转


    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getRegister_code() {
        return register_code;
    }

    public void setRegister_code(String register_code) {
        this.register_code = register_code;
    }
}
