package cn.buk.api.wechat.dto;

public abstract class AbstractSearchCriteria {

	private Page page = new Page();

	private int enterpriseId;

	/**
	 * @return the page
	 */
	public Page getPage() {
		if (this.page == null) page = new Page();
		return page;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(Page page) {
		this.page = page;
	}

	/**
	 * @return the enterpriseId
	 */
	public int getEnterpriseId() {
		return enterpriseId;
	}

	/**
	 * @param enterpriseId the enterpriseId to set
	 */
	public void setEnterpriseId(int enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public int getPageNo() {
		return this.page.getPageNo();
	}

	public void setPageNo(int pageNo) {
		this.page.setPageNo( pageNo);
	}

	public int getPageSize() {
		return this.page.getPageSize();
	}

	public void setPageSize(int pageSize) {
		this.page.setPageSize(pageSize);
	}

}
