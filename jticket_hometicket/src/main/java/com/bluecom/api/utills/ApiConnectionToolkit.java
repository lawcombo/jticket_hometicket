/**
* Comment  : ConnectToolkit
* @version : 1.0
* @date    : "2022-12-19"
* @author  : hoon
*/

package com.bluecom.api.utills;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ApiConnectionToolkit {
	
	private static final Logger logger = LoggerFactory.getLogger(ApiConnectionToolkit.class);
	
	//개발
	/*
	protected static String niceSaleApiUrl 		= "https://iticket-stg.nicetcm.co.kr/api/item/socialsale";
	protected static String niceRefundApiUrl 	= "https://iticket-stg.nicetcm.co.kr/api/item/socialcancel";
	*/
	
	//운영
	protected static String niceSaleApiUrl		= "https://iticket.nicetcm.co.kr/api/item/socialsale";
	protected static String niceRefundApiUrl	= "https://iticket.nicetcm.co.kr/api/item/socialcancel";
	
	protected static String niceTokenKey 	= "BC-Auth-Token";
	protected static String niceTokenValue 	= "{\"key\":\"4104A22CCDBC4159BE0373EA9CEFC42B\",\"token\":\"$2a$10$mC74L9bGjRgjtKh/Udx7zOJEn.TM9DSrVCAcUIvL04LZAEnoOC/Oa\",\"created\":\"20210226122046\",\"expired\":\"21210226122046\"}";
	
	public static String callByPost(String urlAddrFlag, String paramJson) throws Exception {
		try {

			/*
			HashMap<String, String> map = new HashMap<String, String>();
			
			map.put("key", "4104A22CCDBC4159BE0373EA9CEFC42B");
			map.put("token", "$2a$10$mC74L9bGjRgjtKh/Udx7zOJEn.TM9DSrVCAcUIvL04LZAEnoOC/Oa");
			map.put("created", "20210226122046");
			map.put("expired", "21210226122046");

			ObjectMapper mapper = new ObjectMapper();
	        String tokenJson = mapper.writeValueAsString(map);
			*/
	        
			String urlAddr 	= "http://211.253.8.154:21001/v1/hpub/ticket/hist";
			String httpType = "";
			
			/*
			if(urlAddrFlag.equals("sale"))
			{
				// 예매 판매 api
				urlAddr 	= niceSaleApiUrl;
				httpType 	= "POST";
			}
			else if(urlAddrFlag.equals("refund"))
			{
				// 예매 판매 취소 api
				urlAddr 	= niceRefundApiUrl;
				httpType 	= "PUT";
			}
			*/
			
			URL url = new URL(urlAddr);
			HttpURLConnection connectionUrl = (HttpURLConnection) url.openConnection();

			connectionUrl.setDoOutput(true);
			connectionUrl.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36");
			connectionUrl.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			//connectionUrl.setRequestMethod(httpType);
			connectionUrl.setRequestMethod("POST");
			connectionUrl.setRequestProperty("Content-Length", Integer.toString(paramJson.getBytes().length));
			connectionUrl.setRequestProperty("Content-Language", "UTF-8");
			
			//나이스 토큰값 셋팅.
			//connectionUrl.setRequestProperty(niceTokenKey, niceTokenValue);
			
			/*
			DataOutputStream dos = new DataOutputStream(connectionUrl.getOutputStream());
			dos.writeUTF(paramJson);
			dos.flush();
			dos.close();
			 */
			
			connectionUrl.setInstanceFollowRedirects(false); // very important line :)
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(connectionUrl.getOutputStream(), "UTF-8"), true);
			
			pw.print(paramJson);
			pw.flush();
			pw.close();

			
			// POST Response
			int responseCode = connectionUrl.getResponseCode();
			logger.info("Sending \'"+ httpType +"\' request to URL : " + url);
			logger.info("Response Code : " + responseCode);

			String statusCode = String.valueOf(responseCode);

			InputStream is = connectionUrl.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
//            System.out.println("result="+response.toString()); 

			return response.toString();
		} catch (Exception var3) {
			throw var3;
		}
	}
}

