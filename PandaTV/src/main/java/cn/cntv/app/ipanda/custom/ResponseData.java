package cn.cntv.app.ipanda.custom;

import java.io.Serializable;

public class ResponseData<T> implements Serializable {

	private static final long serialVersionUID = -3628710808778329053L;

	private String status;
	private String msg;
	private int total;
	private int page;
	private int pagesize;
	private T data;
	private String collect_id;

	public String getStatus() {
		return status;
	}

	public ResponseData<T> setStatus(String status) {
		this.status = status;
		return this;
	}

	public String getCollect_id() {
		return collect_id;
	}

	public void setCollect_id(String collect_id) {
		this.collect_id = collect_id;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
