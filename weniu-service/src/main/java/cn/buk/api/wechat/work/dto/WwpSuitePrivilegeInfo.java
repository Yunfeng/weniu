package cn.buk.api.wechat.work.dto;

/**
 * 授权信息
 */
public class WwpSuitePrivilegeInfo {

    private int level;
    private int[] allow_party;
    private int[] allow_tag;
    private int[] extra_party;
    private int[] extra_tag;

    private String[] allow_user;
    private String[] extra_user;

    //    allow_party 	应用可见范围（部门）
//    allow_tag 	应用可见范围（标签）
//    allow_user 	应用可见范围（成员）
//    extra_party 	额外通讯录（部门）
//    extra_user 	额外通讯录（成员）
//    extra_tag 	额外通讯录（标签）
//    level 	权限等级。
//            1:通讯录基本信息只读
//2:通讯录全部信息只读
//3:通讯录全部信息读写
//4:单个基本信息只读
//5:通讯录全部信息只写

//            "level":1,
//                "allow_party":[1,2,3],
//            "allow_user":["zhansan","lisi"],
//            "allow_tag":[1,2,3],
//            "extra_party":[4,5,6],
//            "extra_user":["wangwu"],
//            "extra_tag":[4,5,6]

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int[] getAllow_party() {
        return allow_party;
    }

    public void setAllow_party(int[] allow_party) {
        this.allow_party = allow_party;
    }

    public int[] getAllow_tag() {
        return allow_tag;
    }

    public void setAllow_tag(int[] allow_tag) {
        this.allow_tag = allow_tag;
    }

    public int[] getExtra_party() {
        return extra_party;
    }

    public void setExtra_party(int[] extra_party) {
        this.extra_party = extra_party;
    }

    public String[] getAllow_user() {
        return allow_user;
    }

    public void setAllow_user(String[] allow_user) {
        this.allow_user = allow_user;
    }

    public String[] getExtra_user() {
        return extra_user;
    }

    public void setExtra_user(String[] extra_user) {
        this.extra_user = extra_user;
    }


    public int[] getExtra_tag() {
        return extra_tag;
    }

    public void setExtra_tag(int[] extra_tag) {
        this.extra_tag = extra_tag;
    }
}
