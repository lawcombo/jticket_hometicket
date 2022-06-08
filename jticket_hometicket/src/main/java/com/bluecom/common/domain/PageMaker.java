package com.bluecom.common.domain;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public class PageMaker implements Serializable{
	
	private int totalCount;
	private int startPage;
	private int endPage;
	private boolean prev;
	private boolean next;
	
	private int displayPageNum = 5;
	
	private Criteria criteria;
	

	public Criteria getCriteria() {
		return criteria;
	}

	public void setCriteria(Criteria criteria) {
		this.criteria = criteria;
//		this.displayPageNum = criteria.getPerPageNum();
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
		
		calcData();
	}
	
	private void calcData(){
		
		endPage = (int) (Math.ceil(criteria.getPage() / (double)displayPageNum) * displayPageNum);
		
		startPage = (endPage - displayPageNum) + 1;
		
		int tempEndPage = (int)(Math.ceil(totalCount / (double)criteria.getPerPageNum()));
		
		if(endPage > tempEndPage){
			endPage = tempEndPage;
		}
		
		prev = startPage == 1 ? false : true;
		
		next = endPage * criteria.getPerPageNum() >= totalCount ? false : true;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public int getStartPage() {
		return startPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public int getFinalPage() {
		return (int) Math.ceil(totalCount * 1.0 / (double)criteria.getPerPageNum());
	}
	
	public boolean isPrev() {
		return prev;
	}

	public boolean isNext() {
		return next;
	}

	public int getDisplayPageNum() {
		return displayPageNum;
	}

	/**
	 * 검색 조건이 없는 상황에서 사용
	 * @param page
	 * @return
	 */
	public String makeQuery(int page){
		
		UriComponents uriComponents = 
				UriComponentsBuilder.newInstance()
				.queryParam("page", page)
				.queryParam("perPageNum", criteria.getPerPageNum())
				.build();
		
		return uriComponents.toUriString();
	}
	
	/**
	 * 검색 조건이 있는 상황에서 사용
	 * @param page
	 * @return
	 */
	public String makeSearch(int page){
		
		UriComponents uriComponents = 
				UriComponentsBuilder.newInstance()
				.queryParam("page", page)
				.queryParam("perPageNum", criteria.getPerPageNum())
				.queryParam("searchType", ((SearchCriteria) criteria).getSearchType())
				.queryParam("category", ((SearchCriteria) criteria).getCategory())
				.queryParam("keyword", encoding(((SearchCriteria) criteria).getKeyword()))
				.build();
		
		return uriComponents.toUriString();
	}
	/**
	 * 검색 조건이 있는 상황에서 사용
	 * @param page
	 * @return
	 */
	public String makeCategory(int page){
		
		UriComponents uriComponents = 
				UriComponentsBuilder.newInstance()
				.queryParam("page", 1)
				.queryParam("perPageNum", 10)
				.queryParam("searchType", "")
				.queryParam("category", ((SearchCriteria) criteria).getCategory())
				.queryParam("keyword", "")
				.build();
		
		return uriComponents.toUriString();
	}
	
	
	

	private String encoding(String keyword){
		
		if(keyword == null || keyword.trim().length() == 0){
			return "";
		}
		
		try{
			return URLEncoder.encode(keyword, "UTF-8"); 
		} catch(UnsupportedEncodingException e){
			return "";
		}
	}

	@Override
	public String toString() {
		return "PageMaker [totalCount=" + totalCount + ", startPage="
				+ startPage + ", endPage=" + endPage + ", prev=" + prev
				+ ", next=" + next + ", displayPageNum=" + displayPageNum
				+ ", cri=" + criteria + "]";
	}
}
