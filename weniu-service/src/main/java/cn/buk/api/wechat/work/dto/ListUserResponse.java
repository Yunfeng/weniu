package cn.buk.api.wechat.work.dto;

import cn.buk.api.wechat.dto.BaseResponse;

import java.util.List;

public class ListUserResponse extends BaseResponse {

    private List<WwUser> userlist;


    public List<WwUser> getUserlist() {
        return userlist;
    }

    public void setUserlist(List<WwUser> userlist) {
        this.userlist = userlist;
    }
}
