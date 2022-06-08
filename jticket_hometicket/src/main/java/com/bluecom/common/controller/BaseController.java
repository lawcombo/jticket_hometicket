package com.bluecom.common.controller;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseController {
	
	protected void pageErrorLog(HttpServletRequest request){
		log.info("status_code ::: " + request.getAttribute("javax.servlet.error.status_code"));
		log.info("exception_type ::: " + request.getAttribute("javax.servlet.error.exception_type"));
		log.info("message ::: " + request.getAttribute("javax.servlet.error.message"));
		log.info("request_uri ::: " + request.getAttribute("javax.servlet.error.request_uri"));
		log.info("exception ::: " + request.getAttribute("javax.servlet.error.exception"));
		log.info("servlet_name ::: " + request.getAttribute("javax.servlet.error.servlet_name"));
	}
	
	
	protected void validationLog(Errors errors) {
		
		for(ObjectError error : errors.getAllErrors()) {
			
			log.info(error.toString());
		}
	}
	
//	protected static Map<String, String> getParamsFromUrl(String query) throws Exception {
//		String[] urls = query.split("?");
//		
//	      
//	    Map<String, String> map = new HashMap<String, String>();
//
//	    if(urls.length < 2) {
//	    	return map;
//	    }
//	    
//	    String[] params = urls[1].split("&");
//	    for (String param : params) {  
//	        String name = param.split("=")[0];  
//	        String value = param.split("=")[1];  
//	        map.put(name, value);  
//	    } 
//	   
//	    return map;
//	}
	
	protected String getParamValue(String link, String paramName) throws URISyntaxException {
        List<NameValuePair> queryParams = new URIBuilder(link).getQueryParams();
        return queryParams.stream()
                .filter(param -> param.getName().equalsIgnoreCase(paramName))
                .map(NameValuePair::getValue)
                .findFirst()
                .orElse("");
    }
}