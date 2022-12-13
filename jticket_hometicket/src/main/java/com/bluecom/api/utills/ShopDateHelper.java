package com.bluecom.api.utills;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ShopDateHelper implements Serializable {
	
	
	public static String getTimeStamp(int iMode) {
		String sFormat;
		// if (iMode == 1) sFormat = "E MMM dd HH:mm:ss z yyyy";   // Wed Feb 03 15:26:32 GMT+09:00 1999
		if (iMode == 1) sFormat = "MMMM dd, yyyy HH:mm:ss z";   // Jun 03, 2001 15:26:32 GMT+09:00
		else if (iMode == 2) sFormat = "MM/dd/yyyy";// 02/15/1999
		else if (iMode == 3) sFormat = "yyyyMMdd";// 19990215
		else if (iMode == 4) sFormat = "HHmmss";// 121241
		else if (iMode == 5) sFormat = "dd MMM yyyy";// 15 Jan 1999
		else if (iMode == 6) sFormat = "yyyyMMddHHmm"; //200101011010
		else if (iMode == 7) sFormat = "yyyyMMddHHmmss"; //20010101101052
		else if (iMode == 8) sFormat = "HHmmss";
		else if (iMode == 9) sFormat = "yyyy";
		else if (iMode == 10) sFormat = "MM";
		else if (iMode == 11) sFormat = "MMddHHmmssSSS";
		else if (iMode == 12) sFormat = "yyyy-MM-dd";
		else if (iMode == 13) sFormat = "HH:mm:ss";
		
		else sFormat = "E MMM dd HH:mm:ss z yyyy";// Wed Feb 03 15:26:32 GMT+09:00 1999

		Locale locale = new Locale("en", "EN");
		// SimpleTimeZone timeZone = new SimpleTimeZone(32400000, "KST");
		SimpleDateFormat formatter = new SimpleDateFormat(sFormat, locale);
		// formatter.setTimeZone(timeZone);
		// SimpleDateFormat formatter = new SimpleDateFormat(sFormat);

		return formatter.format(new Date());
	}
	
	/**
     * 특정 Format의 String을 Date로 변환 
     * @param date 날짜 String형
     * @param fmt 날짜 String형의 Format 
     * @return 날짜 Date형. 날짜 String형의 오류가 있을 경우 null return
     */
    public  static java.util.Date stringToDate(String date, String fmt ) {
        
        if ( date != null && fmt != null ) {
            SimpleDateFormat sfmt = new SimpleDateFormat(fmt);                    
            try {
                return  sfmt.parse(date);                    
            } catch (ParseException pe) {                
                return null;
            }
        } else 
            return null;
    }
    
    /**
     * Date형을 원하는 포맷으로 변환하여 스트링으로 전환한다. 
     * @param date 날짜 Date형 
     * @return 날짜 String형. null일 경우 null return
     */     
    public static String dateToString(Date date, String fmt) {        
        if (date != null && fmt != null) {
            SimpleDateFormat sfmt = new SimpleDateFormat(fmt);        
            return  sfmt.format(date);        
        } else 
            return null;
    }
    
    /**
	 * 지정한 날짜에서 지정한 날수를 계산한 날짜 반환 <br>
	 * @param iType 앞/뒤로 계산할 단위 (1:일 단위, 2:월 단위, 3:년 단위)
	 * @param sDate 기준이 되는 날짜 - null일 경우, 오늘 날짜를 기준
	 * @param i 앞/뒤로 증가/감소 시킬 수
	 */
	public static String getDate(int iType, String sDate, int i) {

		if (sDate == null) sDate = getTimeStamp(3);

		if (i == 0) return sDate;
		else {
			Calendar cal = Calendar.getInstance();
			cal.set(Integer.parseInt(sDate.substring(0, 4))
				, Integer.parseInt(sDate.substring(4, 6)) - 1
				, Integer.parseInt(sDate.substring(6, 8)));

			if (iType == 2) cal.add(Calendar.MONTH, i);		// 월 단위
			else if (iType == 3) cal.add(Calendar.YEAR, i);	// 년 단위
			else cal.add(Calendar.DATE, i);					// 일 단위

			int iYear = cal.get(Calendar.YEAR);
			int iMonth = cal.get(Calendar.MONTH) + 1;
			int iDate = cal.get(Calendar.DATE);

			String sNewDate = "" + iYear;
			if (iMonth < 10) sNewDate += "0" + iMonth;
			else sNewDate += iMonth;
			if (iDate < 10) sNewDate += "0" + iDate;
			else sNewDate += iDate;

			return sNewDate;
		}
	}
	
	
	/**
    * 현재 날짜 시간
    */
   public static String todayDateTime() {
   	
   	long time = System.currentTimeMillis(); 
	SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
	String str = dayTime.format(new Date(time));
	//System.out.println(str);

	return str;
   }
   
   
   public static String getDateDayName(String date) throws Exception{
	   
	   String day = "" ;
	   
	   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd") ;
	   java.util.Date nDate = dateFormat.parse(date);         
	   Calendar cal = Calendar.getInstance();
	   cal.setTime(nDate);         
	   int dayNum = cal.get(Calendar.DAY_OF_WEEK);         
	    
	    
	   switch(dayNum){
	       case 1:
	           day = "일";  
	           break ;
	       case 2:
	           day = "월";
	           break ;
	       case 3:
	           day = "화";
	           break ;
	       case 4:
	           day = "수";
	           break ;
	       case 5:
	           day = "목";
	           break ;
	       case 6:
	           day = "금";
	           break ;
	       case 7:
	           day = "토";
	           break ;
	   }
	   
	   return day;
   }
   
   public static String getDateDayCode(String date) throws Exception {         
	   String day = "" ;         
	   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd") ;
	   java.util.Date nDate = dateFormat.parse(date);         
	   Calendar cal = Calendar.getInstance();
	   cal.setTime(nDate);         
	   int dayNum = cal.get(Calendar.DAY_OF_WEEK);                  
	    
	   switch(dayNum){
	       case 1:
	           day = "2"; //"일";  
	           break ;
	       case 2:
	           day = "1"; //"월";
	           break ;
	       case 3:
	           day = "1"; //"화";
	           break ;
	       case 4:
	           day = "1"; //"수";
	           break ;
	       case 5:
	           day = "1"; //"목";
	           break ;
	       case 6:
	           day = "1"; //"금";
	           break ;
	       case 7:
	           day = "2"; //"토";
	           break ;
	            
	   }
	    
	   return day ;
	 }

}
