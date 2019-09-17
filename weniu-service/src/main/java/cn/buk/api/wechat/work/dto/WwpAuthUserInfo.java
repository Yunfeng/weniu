package cn.buk.api.wechat.work.dto;

/**
 * 授权管理员的信息
 */
public class WwpAuthUserInfo {
    private String userid;
    private String name;
    private String email;
    private String avatar;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
//        "auth_user_info":
//    {
//        "userid":"aa",
//            "name":"xxx",
//            "avatar":"http://xxx"
//    }
//
//    auth_user_info 	授权管理员的信息
//    email 	授权管理员的邮箱（仅为外部管理员，该字段有返回值）
//    userid 	授权管理员的userid，可能为空（内部管理员一定有，不可更改）
//    name 	授权管理员的name，可能为空（内部管理员一定有，不可更改）
//    avatar 	授权管理员的头像url
}
