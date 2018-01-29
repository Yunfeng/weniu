package cn.buk.api.wechat.work.dto;

/**
 * 企业微信中的成员
 * Work-Weixin
 */
public class WwUser extends BaseResponse {

    private String userid; // * 成员UserID。对应管理端的帐号，企业内必须唯一。不区分大小写，长度为1~64个字节

    private String name; // * 成员名称。长度为1~64个字节

    private String mobile; // 手机号码。企业内必须唯一，mobile/email二者不能同时为空

    private String email; // 邮箱。长度为0~64个字节。企业内必须唯一，mobile/email二者不能同时为空

    private int[] department; // * 成员所属部门id列表,不超过20个

    private String english_name;

    private String position;

    private String gender;

    private int isleader;

    private int enable;

    private String telephone;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int[] getDepartment() {
        return department;
    }

    public void setDepartment(int[] department) {
        this.department = department;
    }

    public String getEnglish_name() {
        return english_name;
    }

    public void setEnglish_name(String english_name) {
        this.english_name = english_name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getIsleader() {
        return isleader;
    }

    public void setIsleader(int isleader) {
        this.isleader = isleader;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
