package com.bluecom.common.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class NumUtil {

	 /**
	  * <pre>
	  * 설 명 : String 형의 숫자를 BigDecimal 객체로 변환합니다.
	  * </pre>
	  *
	  * @param d String형의 숫자.
	     * @return BigDecimal 객체
	  */
	    public static BigDecimal toBigDecimal(String d)
	    {
	        return new BigDecimal(d);
	    }

	    /**
	  * <pre>
	  * 설 명 : double,  float 형의 숫자를 BigDecimal 객체로 변환합니다.
	  * </pre>
	  *
	  * @param d double,  float 형의 숫자.
	     * @return BigDecimal 객체
	  */
	    public static BigDecimal toBigDecimal(double d)
	    {
	        return new BigDecimal(d);
	    }

	   
	    /**
	  * <pre>
	  * 설 명 : long, int 형의 숫자를 BigDecimal 객체로 변환합니다.
	  * </pre>
	  *
	  * @param d long, int 형의 숫자.
	     * @return BigDecimal 객체
	  */
	    public static BigDecimal toBigDecimal(long d)
	    {
	        return new BigDecimal(d);
	    }

	    /**
	  * <pre>
	  * 설 명 : Number 형 객체를 BigDecimal형의 객체로 변환합니다.
	  * </pre>
	  *
	  * @param d 모든 타입의 숫자.
	     * @return BigDecimal 객체
	  */
	    public static BigDecimal toBigDecimal(Number d)
	    {
	        if ( d instanceof BigDecimal ) {
	            return (BigDecimal)d;
	        } else if ( d instanceof Double ) {
	         return new BigDecimal(String.valueOf(d.doubleValue()));
	        } else if ( d instanceof Float ) {
	            return new BigDecimal(String.valueOf(d.floatValue()));
	        } else {
	         return new BigDecimal(String.valueOf(d.longValue()));
	        }
	    }

	   
	    /**
	  * <pre>
	  * 설 명 : 입력된 값을 해당 location에서 절사
	  * </pre>
	  *
	   * @param d   입력된 double 값
	  * @param loc   절사할 위치
	  * @return 절사된 double 값
	  */
	 public static double calTrunc(double d, int loc) throws Exception {
	 
	  BigDecimal bd = new BigDecimal( d );
	 
	  /////////// 2010. 7. 27. 추가 시작////////////////
	  //                      지수 형태가 아닌 경우 값이 바뀌어서 리턴됨.
	 
	  String str = "";
	  String tmpStr = String.valueOf(d);
	  // 값이 지수 형태인지 확인한다.
	  int tmpPos = tmpStr.lastIndexOf("E");
	  if( tmpPos >= 0 ) {
	   str = bd.abs().toString();
	  } else {
	   str = String.valueOf(Math.abs(d));
	  }
	  /////////// 2010. 7. 27. 추가 끝 ////////////////
	 
	        int dotPos = str.lastIndexOf(".");
	        String strPositive;
	        String strNegative;
	       
	        if (dotPos < 0) {
	            strPositive = str;
	            strNegative = "0";
	        } else {
	            strPositive = str.substring(0, dotPos);
	            strNegative = str.substring(dotPos + 1);
	        }
	        if (loc < 0 && strPositive.length() <= Math.abs(loc)) {
	            return 0.0D;
	        }
	        if (loc > 0 && strNegative.length() < loc) {
	            return d;
	        }
	        if (loc < 0) {
	            strNegative = "0";
	            String tmp = str.substring(0, strPositive.length() + loc);
	            strPositive = tmp + toZero(strPositive.length() - tmp.length());
	        } else {
	            strNegative = strNegative.substring(0, loc);
	        }
	        if (d < 0.0D) {
	            strPositive = "-" + strPositive;
	        }
	       
	        return Double.parseDouble(strPositive + "." + strNegative);
	 }


	 

	 /**
	  * <pre>
	  * 설 명 : 입력된 값을 해당 location에서 절상
	  * </pre>
	  *
	  * @param d   입력된 double 값
	  * @param loc   절상할 위치
	  * @return 절상된 double 값
	  */
	 public static double calCeil(double d, int loc) throws Exception {
	 
	  BigDecimal bd = new BigDecimal(d);
	        String str = bd.abs().toString();
	        int dotPos = str.lastIndexOf(".");
	        String strPositive;
	        String strNegative;
	       
	        if (dotPos < 0) {
	            strPositive = str;
	            strNegative = "0";
	        } else {
	            strPositive = str.substring(0, dotPos);
	            strNegative = str.substring(dotPos + 1);
	        }
	        if (loc < 0 && strPositive.length() < Math.abs(loc)) {
	            return 0.0D;
	        }
	        if (loc > 0 && strNegative.length() < loc + 1) {
	            return d;
	        }
	        if (loc < 0) {
	            strPositive = locCeilPositive(strPositive, loc);
	            strNegative = "0";
	        } else {
	            String tmp = locCeilNegative(strNegative, loc);
	            if (tmp.length() > strNegative.substring(0, loc + 1).length()) {
	                strPositive = plusStr(strPositive, 1);
	                strNegative = "0";
	            } else {
	                strNegative = tmp;
	            }
	        }
	        if (d < 0.0D) {
	            strPositive = "-" + strPositive;
	        }
	       
	        return Double.parseDouble(strPositive + "." + strNegative);
	 }
	 

	 /**
	  * <pre>
	  * 설 명 : 입력된 값을 해당 location에서 반올림
	  * </pre>
	  *
	  * @param d   입력된 double 값
	  * @param loc   반올림할 위치
	  * @return 반올림된 double 값
	  */
	    public static double calRound(double d, int loc) throws Exception {
	    
	     BigDecimal bd = new BigDecimal(d);
	     String str = bd.abs().toString();
	     int dotPos = str.lastIndexOf(".");
	     String strPositive;
	     String strNegative;
	     if (dotPos < 0) {
	         strPositive = str;
	         strNegative = "0";
	     } else {
	         strPositive = str.substring(0, dotPos);
	         strNegative = str.substring(dotPos + 1);
	     }
	     if (loc < 0 && strPositive.length() < Math.abs(loc)) {
	         return 0.0D;
	     }
	     if (loc > 0 && strNegative.length() < loc + 1) {
	         return d;
	     }
	     if (loc < 0) {
	         strPositive = locRoundPositive(strPositive, loc);
	         strNegative = "0";
	     } else {
	         String tmp = locRoundNegative(strNegative, loc);
	         if(tmp.length() > strNegative.substring(0, loc + 1).length()) {
	             strPositive = plusStr(strPositive, 1);
	             strNegative = "0";
	         } else {
	             strNegative = tmp;
	         }
	     }
	     if (d < 0.0D) {
	         strPositive = "-" + strPositive;
	     }
	    
	     return Double.parseDouble(strPositive + "." + strNegative);
	 }
	   
	    
	    /**
	  * <pre>
	  * 설 명 : 정수값을 문자열로 변환 후 공백문자로 left padding한다.
	  *    입력된 정수값보다 문자열이 길면 잘라버린다.
	  * </pre>
	  *
	  * @param i   입력된 정수값
	  * @param len   길이
	  * @return padding된 문자열
	  */
	 public static String lpad(int i, int len) {
	 
	        return lpad(String.valueOf(i), len);
	    }

	 /**
	  * <pre>
	  * 설 명 : 정수값을 문자열로 변환 후 입력문자로 left padding한다.
	  *   입력된 정수값보다 문자열이 길면 잘라버린다.
	  * </pre>
	  *
	  * @param i    입력된 정수값
	  * @param len    길이
	  * @param pad_string  padding할 문자열
	  * @return padding된 문자열
	  */
	    public static String lpad(int i, int len, String pad_string) {
	    
	        return lpad(String.valueOf(i), len, pad_string);
	    }

	    /**
	  * <pre>
	  * 설 명 : long값을 문자열로 변환 후 입력문자로 left padding한다.
	  *   입력된 정수값보다 문자열이 길면 잘라버린다.
	  * </pre>
	  *
	  * @param i    입력된 long값
	  * @param len    길이
	  * @param pad_string  padding할 문자열
	  * @return padding된 문자열
	  */   
	    public static String lpad(long i, int len, String pad_string) {
	    
	        return lpad(String.valueOf(i), len, pad_string);
	    }
	   
	   
	    /**
	  * <pre>
	  * 설 명 : 문자열을 공백문자로 left padding한다.
	  *   입력된 정수값보다 문자열이 길면 잘라버린다.
	  * </pre>
	  *
	 * @param s 입력된 문자열
	  * @param len 길이
	  * @return padding된 문자열
	  */
	    public static String lpad(String s, int len) {
	    
	        return lpad(s, len, "                                                       ");
	    }

	   
	    /**
	  * <pre>
	  * 설 명 : 문자열을 입력문자로 left padding한다.
	  *    입력된 정수값보다 문자열이 길면 잘라버린다.
	  * </pre>
	  *
	  * @param s    입력된 문자열
	  * @param len    길이
	  * @param pad_string padding할 입력문자
	  * @return padding된 문자열
	  */
	    public static String lpad(String str, int len, String pad_string) {
	    
	     String s = str;
	    
	        if (s.length() > len) {
	            return s.substring(s.length() - len);
	        }
	       
	        int sLength = s.length();
	        for (; sLength < len; ) {
	         s = pad_string + s;
	         sLength = s.length();
	        }
	       
	        return s.substring(s.length() - len);
	    }
	   
	 
	    /**
	  * <pre>
	  * 설 명 : 입력된 정수값을 문자열로 변환 후 공백문자로 right padding한다.
	  * </pre>
	  *
	  * @param i  입력된 정수값
	  * @param len 길이
	  * @return padding된 문자열
	  */
	    public static String rpad(int i, int len) {
	    
	        return rpadEn(String.valueOf(i), len);
	    }
	   
	   
	    /**
	  * <pre>
	  * 설 명 : 입력된 정수값을 문자열로 변환 후 입력문자로 right padding한다.
	  * </pre>
	  *
	  * @param i    입력된 정수값
	  * @param len   길이
	  * @param pad_string padding할 입력문자
	  * @return padding된 문자열
	  */
	    public static String rpad(int i, int len, String pad_string) {
	    
	        return rpadEn(String.valueOf(i), len, pad_string);
	    }
	   

	    /**
	  * <pre>
	  * 설 명 : 입력된 long값을 문자열로 변환 후 입력문자로 right padding한다.
	  * </pre>
	  *
	  * @param i    입력된 long값
	  * @param len   길이
	  * @param pad_string padding할 입력문자
	  * @return padding된 문자열
	  */
	    public static String rpad(long i, int len, String pad_string) {
	    
	        return rpadEn(String.valueOf(i), len, pad_string);
	    }
	   
	 
	    /**
	  * <pre>
	  * 설 명 : 입력된 문자열을 공백문자로 right padding한다.
	  * </pre>
	  *
	  * @param i    입력된 문자열
	  * @param len   길이
	  * @return padding된 문자열
	  */
	    public static String rpad(String s, int len) {
	    
	        return rpadKr(s, len);
	    }
	   

	    /**
	  * <pre>
	  * 설 명 : 입력된 문자열을 입력문자로 right padding한다.
	  * </pre>
	  *
	  * @param i    입력된 문자열
	  * @param len   길이
	  * @param pad_string padding할 입력문자
	  * @return padding된 문자열
	  */
	    public static String rpad(String s, int len, String pad_string) {
	    
	        return rpadKr(s, len, pad_string);
	    }
	   
	 
	    /**
	  * <pre>
	  * 설 명 : 입력된 문자열을 한글변환을 고려하여 right padding한다.
	  * </pre>
	  *
	  * @param i    입력된 문자열
	  * @param len   길이
	  */
	    private static String rpadKr(String s, int len) {
	    
	        return rpadKr(s, len, "                                                       ");
	    }

	   
	    /**
	  * <pre>
	  * 설 명 : 입력된 문자열을 right padding한다.
	  * </pre>
	  *
	  * @param i    입력된 문자열
	  * @param len   길이
	  */
	    private static String rpadEn(String s, int len) {
	       
	     return rpadEn(s, len, "                                                       ");
	    }

	   
	    /**
	  * <pre>
	  * 설 명 : 입력된 문자열을 한글변환을 고려하여 입력문자로 right padding한다.
	  * </pre>
	  *
	  * @param i    입력된 문자열
	  * @param len   길이
	  * @param pad_string padding할 입력문자
	  * @return padding된 문자열
	  */
	    private static String rpadKr(String s, int len, String pad_string) {
	    
	        String str = toUS8859CS(s);

	        if (str.length() > len) {
	            return toKSC5601CS(str.substring(0, len));
	        }
	        StringBuffer sb = new StringBuffer();
	        sb.append(str);
	       
	        int sbLength = sb.length();
	        for (; sbLength < len; ) {
	         sb.append(pad_string);
	         sbLength = sb.length();
	        }
	       
	        return toKSC5601CS(sb.toString().substring(0, len));
	    }

	       
	    /**
	  * <pre>
	  * 설 명 : 입력된 문자열을 입력문자로 right padding한다.
	  * </pre>
	  *
	  * @param i    입력된 문자열
	  * @param len   길이
	  * @param pad_string padding할 입력문자
	  * @return padding된 문자열
	  */
	    private static String rpadEn(String str, int len, String pad_string) {
	    
	        if (str.length() > len) {
	            return str.substring(0, len);
	        }
	        StringBuffer sb = new StringBuffer();
	        sb.append(str);
	       
	        int sbLength = sb.toString().length();
	        for (;sbLength < len; ) {
	         sb.append(pad_string);
	         sbLength = sb.toString().length();
	        }
	       
	        return sb.toString().substring(0, len);
	    }
	   

	    /**
	  * <pre>
	  * 설 명 : 입력된 문자열을 한글로 변환한다.
	  * </pre>
	  *
	   * @param str 입력된 문자열
	  * @return 변환된 문자열
	  */
	    public static String toKSC5601CS(String str) {
	    
	     String retStr = "";
	    
	        if (str == null || "".equals(str)) {
	            return "";
	        }
	        if (isKSC(str)) {
	            return str;
	        }
	       
	        try {
	         retStr = new String(str.getBytes("8859_1"), "KSC5601");
	        } catch (UnsupportedEncodingException ue) {
	         return null;
	        }
	       
	        return retStr;
	    }


	    /**
	  * <pre>
	  * 설 명 : 입력된 문자열을 한글로 변환한다.
	  * </pre>
	  *
	  * @param str 입력된 문자열
	  * @return 변환된 문자열
	  */
	    public static String toUS8859CS(String str) {
	    
	     String retStr = "";
	    
	        if (str == null || "".equals(str)) {
	            return retStr;
	        }
	       
	        try {
	         retStr = new String(str.getBytes("KSC5601"), "8859_1");
	        } catch (UnsupportedEncodingException ue) {
	         return null;
	        }
	       
	        return retStr;
	    }
	   

	    /**
	  * <pre>
	  * 설 명 : 입력된 문자열이 KSC5601코드 형식인지 검증한다.
	  * </pre>
	  *
	  * @param i    입력된 문자열
	  * @return 검증결과(true/false)
	  */
	    public static boolean isKSC(String str) {
	    
	        int j;
	        String tempStr = "";
	       
	        try {
	         tempStr = new String(str.getBytes("8859_1"), "KSC5601");
	        } catch (UnsupportedEncodingException ue) {
	         return false;
	        }
	       
	        byte tempByte[] = tempStr.getBytes();
	        j = 0;
	        for (int i = 0; i < tempByte.length; i++) {
	            if (tempByte[i] == 63) {
	                j++;
	            }
	        }

	        if (j > 1) {
	            return true;
	        }
	       
	        return false;
	    }
	   

	    /**
	  * <pre>
	  * 설 명 : 입력받은 수의 길이만큼 0으로된 문자열을 만들어 반환
	  * </pre>
	  *
	  * @param z 만들고자 하는 0 문자열의 길이
	  * @return 0으로 이루어진 문자열
	  */
	 private static String toZero(int z) {
	 
	        String returnStr = "";
	       
	        for (int i = 0; i < z; i++) {
	            returnStr = returnStr + "0";
	        }

	        return returnStr;
	    }
	 
	 
	 /**
	  * <pre>
	  * 설 명 : 입력받은 문자열의 길이만큼 0으로된 문자열을 만들어 반환
	  * </pre>
	  *
	  * @param str 만들고자 하는 0 문자열의 길이
	  * @return 0으로 이루어진 문자열
	  */
	 private static String toZero(String str) {
	 
	 
	        if (str == null || str.length() < 1) {
	            return str;
	        }
	       
	        int strLength = str.length();
	        String returnStr = "";
	        int returnStrLength = 0;
	        for (; returnStrLength < strLength; ) {
	         returnStr = returnStr + "0";
	         returnStrLength = returnStr.length();
	        }
	       
	        return returnStr;
	    }
	 
	 
	 /**
	  * <pre>
	  * 설 명 : 입력받은 문자열의 길이만큼 0으로된 문자열을 만들어 반환
	  * </pre>
	  *
	  * @param str 만들고자 하는 0 문자열의 길이
	  * @return 0으로 이루어진 문자열
	  */
	 private static String locCeilPositive(String str, int loc) {
	 
	        return cCeil(str.substring(0, str.length() + loc + 1)) +
	          toZero(str.substring(str.length() + loc + 1, str.length()));
	    }
	 

	 /**
	  * <pre>
	  * 설 명 : 문자열 절상처리
	  * </pre>
	  *
	  * @param str 처리할 문자
	  * @return 절상처리된 문자
	  */
	 private static String cCeil(String str) {
	 
	        String returnStr = str;
	        if (str == null || str.length() < 1 || str.endsWith("0")) {
	            return str;
	        }
	        returnStr = plusStr(str, 10);
	        if (returnStr.length() < str.length()) {
	            returnStr = lpad(returnStr, str.length(), "0");
	        }
	        if (returnStr.length() > str.length()) {
	            return returnStr.substring(0, returnStr.length() - 1) + "0";
	        } else {
	            return returnStr.substring(0, str.length() - 1) + "0";
	        }
	    }
	 
	 
	 /**
	  * <pre>
	  * 설 명 : 입력된 문자열과 수의 합을 계산
	  * </pre>
	  *
	  * @param str 입력된 문자
	  * @param val 입력된 숫자
	  * @return 합산 결과
	  */
	 private static String plusStr(String str, int val) {
	 
	        if (str == null || "".equals(str)) {
	            return str;
	        } else {
	            BigDecimal bd = new BigDecimal(str);
	           
	            return bd.add(new BigDecimal(Integer.toString(val))).toString();
	        }
	    }
	 

	 /**
	  * <pre>
	  * 설 명 : 특정위치에서의 문자열 절상처리
	  * </pre>
	  *
	  * @param str 처리할 문자
	  * @param loc 절상처리할 위치
	  * @return 절상처리된 문자
	  */
	 private static String locCeilNegative(String str, int loc) {
	 
	        return cCeil(str.substring(0, loc + 1));
	    }
	 

	 /**
	  * <pre>
	  * 설 명 : 특정위치에서의 문자열 반올림처리
	  * </pre>
	  *
	   * @param str 처리할 문자
	  * @param loc 반올림처리할 위치
	  * @return 반올림처리된 문자
	  */
	 private static String locRoundPositive(String str, int loc) {
	 
	        return cRound(str.substring(0, str.length() + loc + 1)) +
	          toZero(str.substring(str.length() + loc + 1, str.length()));
	    }
	 

	 /**
	  * <pre>
	  * 설 명 : 특정위치에서의 문자열 반올림처리
	  * </pre>
	  *
	  * @param str 처리할 문자
	  * @param loc 반올림처리할 위치
	  * @return 반올림처리된 문자
	  */
	 private static String locRoundNegative(String str, int loc) {
	 
	        if (str == null || str.length() < 1 || str.endsWith("0")) {
	            return str;
	        } else {
	            return cRound(str.substring(0, loc + 1));
	        }
	    }
	 
	 
	 /**
	  * <pre>
	  * 설 명 : 문자열 반올림처리
	  * </pre>
	  *
	  * @param str 처리할 문자
	  * @return 반올림처리된 문자
	  */
	 private static String cRound(String str) {
	 
	        String returnStr = str;
	        if (str == null || str.length() < 1 || str.endsWith("0")) {
	            return str;
	        }
	        returnStr = plusStr(str, 5);
	        if (returnStr.length() < str.length()) {
	            returnStr = lpad(returnStr, str.length(), "0");
	        }
	        if (Integer.toString(Integer.parseInt(returnStr)).length() >
	          Integer.toString(Integer.parseInt(str)).length()) {
	            return returnStr.substring(0, returnStr.length() - 1) + "0";
	        } else {
	            return returnStr.substring(0, str.length() - 1) + "0";
	        }
	    }  
	 
	 /**
	  * <pre>
	  * 설 명 : 입력된 object이 null이면 0으로 초기화된 object 리턴
	  * </pre>
	  *
	  * @param Integer
	  * @return 초기값이 0인 Integer
	  */
	 public static Integer nullToZero(Integer in) {
	 
	  return ( in == null ? new Integer(0) : in );
	    } 
	 
	 /**
	  * <pre>
	  * 설 명 : 입력된 object이 null이면 0으로 초기화된 object 리턴
	  * </pre>
	  *
	  * @param Integer
	  * @return 초기값이 0인 Integer
	  */
	 public static Long nullToZero(Long in) {
	 
	  return ( in == null ? new Long(0) : in );
	    }
	 
	 /**
	  * <pre>
	  * 설 명 : 입력된 object이 null이면 0으로 초기화된 object 리턴
	  * </pre>
	  *
	  * @param Integer
	  * @return 초기값이 0인 Integer
	  */
	 public static Float nullToZero(Float in) {
	 
	  return ( in == null ? new Float(0) : in );
	    }
	 
	 /**
	  * <pre>
	  * 설 명 : 입력된 object이 null이면 0으로 초기화된 object 리턴
	  * </pre>
	  *
	  * @param Integer
	  * @return 초기값이 0인 Integer
	  */
	 public static Double nullToZero(Double in) {
	 
	  return ( in == null ? new Double(0) : in );
	    }
	 
	 /**
	  * <pre>
	  * 설 명 : 입력된 object이 null이면 0으로 초기화된 object 리턴
	  * </pre>
	  *
	  * @param Integer
	  * @return 초기값이 0인 Integer
	  */
	 public static BigInteger nullToZero(BigInteger in) {
	 
	  return ( in == null ? new BigInteger("0") : in );
	    }
	 
	 /**
	  * <pre>
	  * 설 명 : 입력된 object이 null이면 0으로 초기화된 object 리턴
	  * </pre>
	  *
	  * @param Integer
	  * @return 초기값이 0인 Integer
	  */
	 public static BigDecimal nullToZero(BigDecimal in) {
	 
	  return ( in == null ? new BigDecimal("0") : in );
	    }
	 
	 
	 
	}