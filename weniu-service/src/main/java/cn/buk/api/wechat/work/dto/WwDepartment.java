package cn.buk.api.wechat.work.dto;

/**
 * 企业微信中的部门
 * Work-Weixin
 */
public class WwDepartment {

    private String name; // * 部门名称。长度限制为1~64个字节，字符不能包括\:?”<>｜

    private int parentid; // * 父部门id，32位整型

    private int order; // 在父部门中的次序值。order值大的排序靠前。有效的值范围是[0, 2^32)

    private int id; // 部门id，32位整型。指定时必须大于1，否则自动生成

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentid() {
        return parentid;
    }

    public void setParentid(int parentid) {
        this.parentid = parentid;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
