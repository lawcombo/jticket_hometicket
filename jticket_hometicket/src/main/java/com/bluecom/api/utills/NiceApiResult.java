package com.bluecom.api.utills;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
public class NiceApiResult {

	private String retcode;
	private String retmsg;
	private String statuscode;
	
	private ArrayList<DataVO> datas;

	@JsonCreator
	@Builder
	public NiceApiResult(
			@JsonProperty("retcode") String retCode,
			@JsonProperty("retmsg") String retMsg,
			@JsonProperty("statuscode") String statusCode,
			@JsonProperty("datas") ArrayList<DataVO> datas
			) {
		super();
		this.retcode = retCode;
		this.retmsg = retMsg;
		this.statuscode = statusCode;
		this.datas = datas;
	}

	public NiceApiResult() {
		super();
	}
	
	
}
