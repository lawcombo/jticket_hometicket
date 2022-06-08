package com.bluecom.common.util;

import org.springframework.util.StringUtils;

public class SqlHelper {

	public static boolean contains(String text, String value) {
		
		if(StringUtils.hasText(text) && text.contains(value) ) {
			return true;
		}
		return false;
	}
}
