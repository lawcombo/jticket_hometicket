package com.bluecom.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateHelper {

	public static Date getDateStart(Date date) {
		
		return removeTime(date);
	}
	
	public static Date getNextDate(Date date) {
		return addDays(removeTime(date), 1);
	}
	
	public static Date removeTime(Date date) {    
		Calendar cal = Calendar.getInstance();  
        cal.setTime(date);  
        cal.set(Calendar.HOUR_OF_DAY, 0);  
        cal.set(Calendar.MINUTE, 0);  
        cal.set(Calendar.SECOND, 0);  
        cal.set(Calendar.MILLISECOND, 0);  
        return cal.getTime(); 
    }
	
	public static Date addDays(Date date, int days) {
		
		Date dateStart = removeTime(date);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateStart);
		calendar.add(Calendar.DATE, days);		
		return  calendar.getTime();
	}
		
	public static Date getFirstDayOfMonth(Date date) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, 1);          
        cal.set(Calendar.HOUR_OF_DAY, 0);  
        cal.set(Calendar.MINUTE, 0);  
        cal.set(Calendar.SECOND, 0);  
        cal.set(Calendar.MILLISECOND, 0);  
        return cal.getTime();
	}
	
	public static Date addMonths(Date date, int months) {
		
		Date monthStart = getFirstDayOfMonth(date);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(monthStart);
		calendar.add(Calendar.MONTH, months);		
		return  calendar.getTime();
	}
	
	
	
	public static Date getStartThisYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MONTH, 1); 
		cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);  
        cal.set(Calendar.MINUTE, 0);  
        cal.set(Calendar.SECOND, 0);  
        cal.set(Calendar.MILLISECOND, 0);  
        return cal.getTime();
	}
	public static Date getEndThisYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 1);
		cal.set(Calendar.MONTH, 1); 
		cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);  
        cal.set(Calendar.MINUTE, 0);  
        cal.set(Calendar.SECOND, 0);  
        cal.set(Calendar.MILLISECOND, 0);  
        return cal.getTime();
	}
	
	
	public static Date getStartMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) -11); // 11개월 전 1일
		cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);  
        cal.set(Calendar.MINUTE, 0);  
        cal.set(Calendar.SECOND, 0);  
        cal.set(Calendar.MILLISECOND, 0);  
        return cal.getTime();
	}
	public static Date getEndMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1); // 다음달 1일
		cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);  
        cal.set(Calendar.MINUTE, 0);  
        cal.set(Calendar.SECOND, 0);  
        cal.set(Calendar.MILLISECOND, 0);  
        return cal.getTime();
	}
	
	public static String toString(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String dateString = "";
		try {
			dateString = format.format(date);
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return dateString;
	}
	

	public static String getExpireDate(String playDate, int validDate) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(sdf.parse(playDate));
		c.add(Calendar.DATE, validDate);  // number of days to add
		return sdf.format(c.getTime());  // dt is now the new date
	}


	public static Date setEndTime(Date date) {
		
		Date dateStart = removeTime(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateStart);
		cal.set(Calendar.HOUR_OF_DAY, 23);  
        cal.set(Calendar.MINUTE, 59);  
        cal.set(Calendar.SECOND, 59);  
        cal.set(Calendar.MILLISECOND, 999);  
		return  cal.getTime();
	}
	

	public static Date getEndDayOfMonth(Date date) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

	    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));          
	    
	    cal.set(Calendar.HOUR_OF_DAY, 23);  
	    cal.set(Calendar.MINUTE, 59);  
	    cal.set(Calendar.SECOND, 59);  
	    cal.set(Calendar.MILLISECOND, 999);  
	    
	    return cal.getTime();
	}

	public static Date getFirstDayOfWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        cal.set(Calendar.HOUR_OF_DAY, 0);  
        cal.set(Calendar.MINUTE, 0);  
        cal.set(Calendar.SECOND, 0);  
        cal.set(Calendar.MILLISECOND, 0);  
        return cal.getTime();
	}
	
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
		else if (iMode == 13) sFormat = "HH";
		else if (iMode == 14) sFormat = "mm";
		
		else sFormat = "E MMM dd HH:mm:ss z yyyy";// Wed Feb 03 15:26:32 GMT+09:00 1999

		Locale locale = new Locale("en", "EN");
		// SimpleTimeZone timeZone = new SimpleTimeZone(32400000, "KST");
		SimpleDateFormat formatter = new SimpleDateFormat(sFormat, locale);
		// formatter.setTimeZone(timeZone);
		// SimpleDateFormat formatter = new SimpleDateFormat(sFormat);

		return formatter.format(new Date());
	}
}
