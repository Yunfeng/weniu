package cn.buk.api.wechat.dto;

public class JsonResult {

	private String attach;

	public static JsonResult createJsonResult(int retCode) {
		return createJsonResult(retCode, null);
	}
	public static JsonResult createJsonResult(int retCode, String okMsg) {
		JsonResult jsonResult = new JsonResult();
		if (retCode > 0 ) {
			jsonResult.setStatus("OK");
			jsonResult.setReturnCode(retCode);
			jsonResult.setDesc(okMsg);
		} else {
			jsonResult.setStatus("ER");
			jsonResult.setErrcode(retCode);
		}

		return jsonResult;
	}

	/**
	 * 状态
	 */
	private String status ;
	
	/**
	 * 说明
	 */
	private String desc;

    private int errcode;
    private String errmsg;

	/**
	 * 用于返回正确结果时要传递的信息
	 */
	private int returnCode;
	private String url;

	/**
	 * @return the status
	 */
	public String getStatus() {
		if (status == null) return "NA";
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		if (desc == null) desc = "";
		return desc.trim();
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
		if (errmsg == null) return "";
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

	public void setReturnCode(int returnCode) {
		this.returnCode = returnCode;
	}

	public int getReturnCode() {
		return returnCode;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getAttach() {
		return attach;
	}
}
