package cn.buk.api.wechat.entity;

import jakarta.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: william
 * Date: 14-10-11
 * Time: 下午11:53
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="weixin_group")
public class WeixinGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="enterprise_id")
    private int enterpriseId;

    @Column(name="group_id")
    private int groupId;

    @Column(name="group_name", length = 50)
    private String groupName;

    private int count;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(int weixinId) {
        this.enterpriseId = weixinId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
