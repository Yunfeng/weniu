package cn.buk.api.wechat.dto;

public class Page {

    private int pageNo = 1;
    private int pageSize = 20;
    private int pageTotal;
    private int rowCount;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        if (pageNo <= 0)
            this.pageNo = 1;
        else
            this.pageNo = pageNo;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int recordCount) {
        this.rowCount = recordCount;

        int x = recordCount % pageSize;
        int y = recordCount / pageSize;
        this.pageTotal = y;
        if (x >= 1) this.pageTotal++;

        if (this.pageNo > this.pageTotal) this.pageNo = 1;

    }
}
