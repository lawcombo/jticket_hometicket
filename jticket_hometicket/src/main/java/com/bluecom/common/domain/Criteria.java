package com.bluecom.common.domain;

import java.io.Serializable;

public class Criteria implements Serializable {

	private String tableName;
	
	private int page;
	private int perPageNum;

	public Criteria(){
		this.page = 1;
		this.perPageNum = 10;
	}
	
	public String getTableName() {
		return tableName;
	}
	
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public int getPage() {
		return page;
	}
	
	public void setPage(int page) {
		
		if(page <= 0){
			this.page = 1;
			return;
		}
		
		this.page = page;
	}
	
	public int getPerPageNum() {
		return perPageNum;
	}
	
	public void setPerPageNum(int perPageNum) {
		
		if(perPageNum <= 0 || perPageNum > 100){
			this.perPageNum = 10;
			return;
		}
		
		this.perPageNum = perPageNum;
	}

	public int getPageStart(){
		return (this.page -1) * perPageNum;
	}
	
	@Override
	public String toString() {
		return "Criteria [tableName=" + tableName + ", page=" + page + ", perPageNum=" + perPageNum + "]";
	}
}
