package com.bluecom.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
	
	
}
