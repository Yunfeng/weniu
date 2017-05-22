package cn.buk.api.wechat.dto;

public class CommonSearchCriteria extends AbstractSearchCriteria {

    @Override
    public int getPageNo() {
        return getPage().getPageNo();
    }

    @Override
    public void setPageNo(int pageNo) {
        getPage().setPageNo(pageNo);
    }

    @Override
    public int getPageSize() {
        return getPage().getPageSize();
    }

    @Override
    public void setPageSize(int pageSize) {
        getPage().setPageSize(pageSize);
    }

}
