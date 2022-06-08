package com.bluecom.common.util;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.util.StringUtils;

public class StartEndDatePropertyEditor extends PropertyEditorSupport {

	@Override
	public String getAsText() {
		return String.valueOf((Date) getValue() );
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if(StringUtils.hasText(text)) {
			try {
				Date date = new Date(text);
				setValue(date);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Calendar cal = Calendar.getInstance();
			setValue(cal.getTime());
		}

	}
	
	

}
