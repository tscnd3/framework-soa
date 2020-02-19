package com.xinyue.framework.mogo.helper;

import java.io.Serializable;

public class MogCondition<T> implements Serializable{
	
	private static final long serialVersionUID = -1701825050780983174L;

	private T t;
	
	private int currPage=1;
	
	private int pageSize=10;
	
	private MogSort mogSort;
	
	public MogCondition(){
		super();
	}
	
	public MogCondition(T t){
		this.t=t;
	}
	
	public MogCondition(T t,MogSort mogSort){
		this.t=t;
		this.mogSort=mogSort;
	}

	public T getT() {
		return t;
	}

	public void setT(T t) {
		this.t = t;
	}


	public int getCurrPage() {
		return currPage<=1?0:(currPage-1);
	}

	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public MogSort getMogSort() {
		return mogSort;
	}

	public void setMogSort(MogSort mogSort) {
		this.mogSort = mogSort;
	}

	
	
	
	
}
