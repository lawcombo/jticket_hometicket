package com.bluecom.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("fileUtils")
public class FileUtils {

	String uploadPath;

	// 파일명의 중복을 피하기 위해 랜덤 파일명을 생성하는 메서드 getRandomSaveName() 을 구현
	private String getRandomSaveName() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	
	// 파일 삭제
	public void delFile(String filename, HttpSession session){
		
		uploadPath = session.getServletContext().getRealPath("/resources/corporate")+"/";
		
		String resultFile = uploadPath + filename;
		File file = new File(resultFile);
        
        if( file.exists() ){
            if(file.delete()){
            	System.out.println("::: FILE ::: DELETE ::: SUCCESS :::");
            }else{
            	System.out.println("::: FILE ::: DELETE ::: FAIL :::");
            }
        }else{
        	System.out.println("::: FILE ::: NOT_EXISTS :::");
        }
	}
	
	// 파일 업로드
	public String addFile(MultipartFile file, HttpSession session) throws Exception {
		// 저장경로, 객체 생성
		uploadPath = session.getServletContext().getRealPath("/resources/corporate")+"/";
		
		// 저장경로 폴더 생성
		File target = new File(uploadPath);
	    if(!target.exists()) target.mkdirs();
	    
	    String orgName = file.getOriginalFilename();
	    String fileExtension = orgName.substring(orgName.lastIndexOf("."));
	    String saveName = getRandomSaveName() + fileExtension;
	    
	    target = new File(uploadPath, saveName);
	    file.transferTo(target);
	    
	    return saveName;
	}
	
	
	// 파일 다운로드
	public void downLoad(Map<String, Object> file, HttpServletRequest request
			           , HttpSession session, HttpServletResponse response) throws Exception{
		// 0은 파일 없음, 1은 파일 있음
//		int result = 0;
		
		String path =  request.getSession().getServletContext().getRealPath("/resources/corporate");
		
        String filename = (String)file.get("corporate_origin"); // 다운로드할 원본 이름
        String downname = (String)file.get("corporate_save");  // 저장되어 다운로드 되는 저장용 이름
        
        String realPath = "";
        System.out.println("downname: "+downname);
         
        try {
            String browser = request.getHeader("User-Agent"); 
            //파일 인코딩 
            if (browser.contains("MSIE") || browser.contains("Trident") // 브라우저 별로 파일이름을 알맞게 인코딩 해준다.
                    || browser.contains("Chrome")) {
                filename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
            } else {
                filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
            }
        } catch (java.io.UnsupportedEncodingException ex) {
            System.out.println("UnsupportedEncodingException");
        }
        realPath = path + "/" + downname;
        System.out.println(realPath);
        File file1 = new File(realPath);
        if (!file1.exists()) { // 파일 여부 체크
        	return;
//            return result;
        }
         
        // 파일명 지정        
        response.setContentType("application/octer-stream");
        response.setHeader("Content-Transfer-Encoding", "binary;");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        try {
//        	response.getWriter().close();
            OutputStream out = response.getOutputStream();
            FileInputStream fis = new FileInputStream(realPath);
            
 
            int ncount = 0;
            byte[] bytes = new byte[512];
 
            while ((ncount = fis.read(bytes)) != -1 ) {
                out.write(bytes, 0, ncount);
            }
            fis.close();
            out.flush();
            out.close();
        } catch (FileNotFoundException ex) {
            System.out.println("FileNotFoundException");
        } catch (IOException ex) {
            System.out.println("IOException");
        }
//        result = 1;
        
//        return result;
	}
	
	
	
	public static String readText(String filePath)
    {		
		String str = "";
		try {		
			//파일 객체 생성
	        File file = new File(filePath);
	        
	        Scanner scan = new Scanner(file);
	        
	        while(scan.hasNextLine()) {
	        	str += scan.nextLine() + "\r\n";
	        }
		}
		catch(FileNotFoundException e)
		{

			log.error("Cannnot read file: " + filePath);
		}
		 return str;
    }
	
}
