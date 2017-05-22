package cn.buk.api.wechat.dto;

import java.util.List;

/**
 * Created by yfdai on 15/9/21.
 */
public class CommonDto<T> {

    private List<T> dataList;

    private Page page;

    /**
     * 状态: 含义自定义
     */
    private int status;

    private int count;


    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
