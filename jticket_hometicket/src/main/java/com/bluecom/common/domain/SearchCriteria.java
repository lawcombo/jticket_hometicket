package com.bluecom.common.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class SearchCriteria extends Criteria implements Serializable{

	private String searchType;
	private String keyword;
	private String category;
	// 특정 상황에서 이용하기 위함
	private String companyCode;
	private String shopCode;

	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	@Override
	public String toString() {
		return "SearchCriteria [searchType=" + searchType + ", keyword=" + keyword + ", category=" + category + "]";
	}

}
