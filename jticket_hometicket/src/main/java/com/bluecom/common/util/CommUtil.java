package com.bluecom.common.util;

import javax.servlet.http.HttpServletRequest;

public class CommUtil {

	public static String RequestParam(HttpServletRequest request, String param) {
		return request.getParameter(param)==null?"":request.getParameter(param);
	}
	
	
	
	public static String RequestParamType(HttpServletRequest request, String param) {
		/*type 값이 1(일반예매 모듈) 또는 2(회차 예매모듈) 만 허용 되도록 처리..*/
		String rtn ="";
		if(request.getParameter(param)==null) {
			rtn ="1";
		} else {
			rtn = request.getParameter(param);
		}
		
		if(!rtn.equals("1") && !rtn.equals("2")) {
			rtn = "";
		}
		
		return rtn;			
		
	}
	
	public static String Tel_Check(String orgTel) {
		/*type 값이 1(일반예매 모듈) 또는 2(회차 예매모듈) 만 허용 되도록 처리..*/
		String rtn ="";
		
		orgTel = orgTel.replace("-", "");
		
		if (orgTel.length() == 7){
			rtn = orgTel.substring(0, 3) + "-" + orgTel.substring(3, 7); 
        }
		else if (orgTel.length() == 8){
			rtn = orgTel.substring(0, 4) + "-" + orgTel.substring(4, 8);
	    }
		else if (orgTel.length() == 9){
			rtn = orgTel.substring(0, 2) + "-" + orgTel.substring(2, 5) + "-" + orgTel.substring(5, 9);
	    }
		else if (orgTel.length() == 10){
			 if (orgTel.substring(0, 2) == "02")//00-0000-0000
             {
				 rtn =  orgTel.substring(0, 2) + "-" + orgTel.substring(2, 6) + "-" + orgTel.substring(6,10);
             }
             else//000-000-0000
             {
            	 rtn = orgTel.substring(0, 3) + "-" + orgTel.substring(3, 6) + "-" + orgTel.substring(6,10);
             }
	    }
		else if (orgTel.length() == 11){
			rtn = orgTel.substring(0, 3) + "-" + orgTel.substring(3, 7) + "-" + orgTel.substring(7,11);
	    }
		else
		{
			rtn ="";
		}
		
		return rtn;			
		
	}
	
	public static String MaskingLast(String org, int length) {
		
		String rtn ="";
		
		rtn = org.replaceAll("(?<=.{"+length+"})." , "*");


		
		return rtn;			
		
	}
	
	
}
