package com.bluecom.common.util;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

public class SmsUtils {

	  public static String nullcheck(String str,  String Defaultvalue ) throws Exception
	     {
	          String ReturnDefault = "" ;
	          if (str == null)
	          {
	              ReturnDefault =  Defaultvalue ;
	          }
	          else if (str == "" )
	         {
	              ReturnDefault =  Defaultvalue ;
	          }
	          else
	          {
	              ReturnDefault = str ;
	          }
	           return ReturnDefault ;
	     }
	     /**
	     * BASE64 Encoder
	     * @param str
	     * @return
	     */
	    public static String base64Encode(String str)  throws java.io.IOException {
	    	
	    	Encoder encoder = Base64.getEncoder();
	        byte[] strByte = str.getBytes();
	        String result = encoder.encodeToString(strByte);
	        return result ;
	    }

	    /**
	     * BASE64 Decoder
	     * @param str
	     * @return
	     */
	    public static String base64Decode(String str)  throws java.io.IOException {
	    	Decoder decoder = Base64.getDecoder();

	        byte[] strByte = decoder.decode(str);
	        String result = new String(strByte);
	        return result ;
	    }
	    
}
