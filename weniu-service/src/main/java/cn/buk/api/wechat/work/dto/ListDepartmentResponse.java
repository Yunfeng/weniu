package cn.buk.api.wechat.work.dto;

import cn.buk.api.wechat.dto.BaseResponse;

import java.util.List;

public class ListDepartmentResponse extends BaseResponse {

    private List<WwDepartment> department;

    public List<WwDepartment> getDepartment() {
        return department;
    }

    public void setDepartment(List<WwDepartment> department) {
        this.department = department;
    }
}
