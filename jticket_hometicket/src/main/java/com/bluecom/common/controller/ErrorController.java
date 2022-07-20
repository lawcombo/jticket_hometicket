package com.bluecom.common.controller;

import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bluecom.common.util.ScriptUtils;
import com.bluecom.ticketing.domain.ShopDetailVO;
import com.bluecom.ticketing.service.TicketingService;

import egovframework.rte.fdl.property.EgovPropertyService;

@Controller
@RequestMapping("/error")
public class ErrorController extends BaseController {
	
	@Autowired
	TicketingService ticketingService;
	@Autowired
	EgovPropertyService properties;
	
	@RequestMapping("/throwable")
	public void throwable(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception{
		
		pageErrorLog(request);
		String jejuUrl = properties.getString("jejubeer");
		ScriptUtils.alertAndMovePage(response, "예외가 발생하였습니다.", jejuUrl);
	}

	@RequestMapping("/exception")
	public void exception(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception{
		
		pageErrorLog(request);
		String jejuUrl = properties.getString("jejubeer");
		ScriptUtils.alertAndMovePage(response, "예외가 발생하였습니다.", jejuUrl);
	}

	@RequestMapping("/403")
	public void page403(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception{
		
		pageErrorLog(request);
		String jejuUrl = properties.getString("jejubeer");
		ScriptUtils.alertAndMovePage(response, "접근권한이 없습니다.", jejuUrl);
	}

	@RequestMapping("/404")
	public void page404(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception{

		pageErrorLog(request);
		String jejuUrl = properties.getString("jejubeer");
		ScriptUtils.alertAndMovePage(response, "페이지를 찾을 수 없습니다.", jejuUrl);
	}

	@RequestMapping("/405")
	public void page405(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception{

		pageErrorLog(request);
		String jejuUrl = properties.getString("jejubeer");
		ScriptUtils.alertAndMovePage(response, "잘못된 요청입니다.", jejuUrl);
	}

	@RequestMapping("/500")
	public void page500(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception{

		pageErrorLog(request);
		String jejuUrl = properties.getString("jejubeer");
		ScriptUtils.alertAndMovePage(response, "서버에 오류가 발생하였습니다.", jejuUrl);
	}

	@RequestMapping("/503")
	public void page503(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception{

		pageErrorLog(request);
		String jejuUrl = properties.getString("jejubeer");
		ScriptUtils.alertAndMovePage(response, "서비스를 사용할 수 없습니다.", jejuUrl);
	}
	
	
	//=====================================================DiamondBay======================================
	
	@RequestMapping("/diamondbay/throwable")
	public void throwableOfDiamondbay(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception{
		
		pageErrorLog(request);
		String jejuUrl = properties.getString("diamondbay");
		ScriptUtils.alertAndMovePage(response, "예외가 발생하였습니다.", jejuUrl);
	}

	@RequestMapping("/diamondbay/exception")
	public void exceptionOfDiamondbay(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception{
		
		pageErrorLog(request);
		String jejuUrl = properties.getString("diamondbay");
		ScriptUtils.alertAndMovePage(response, "예외가 발생하였습니다.", jejuUrl);
	}

	@RequestMapping("/diamondbay/403")
	public void page403OfDiamondbay(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception{
		
		pageErrorLog(request);
		String jejuUrl = properties.getString("diamondbay");
		ScriptUtils.alertAndMovePage(response, "접근권한이 없습니다.", jejuUrl);
	}

	@RequestMapping("/diamondbay/404")
	public void page404OfDiamondbay(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception{

		pageErrorLog(request);
		String jejuUrl = properties.getString("diamondbay");
		ScriptUtils.alertAndMovePage(response, "페이지를 찾을 수 없습니다.", jejuUrl);
	}

	@RequestMapping("/diamondbay/405")
	public void page405OfDiamondbay(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception{

		pageErrorLog(request);
		String jejuUrl = properties.getString("diamondbay");
		ScriptUtils.alertAndMovePage(response, "잘못된 요청입니다.", jejuUrl);
	}

	@RequestMapping("/diamondbay/500")
	public void page500OfDiamondbay(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception{

		pageErrorLog(request);
		String jejuUrl = properties.getString("diamondbay");
		ScriptUtils.alertAndMovePage(response, "서버에 오류가 발생하였습니다.", jejuUrl);
	}

	@RequestMapping("/diamondbay/503")
	public void page503OfDiamondbay(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception{

		pageErrorLog(request);
		String jejuUrl = properties.getString("diamondbay");
		ScriptUtils.alertAndMovePage(response, "서비스를 사용할 수 없습니다.", jejuUrl);
	}

	
	
	
	
	private int getHistoryBackCount(String path) {
		int historyBackCount = 1;
		if(path.equals("/ticketing/showTicketInfo") ||
				path.equals("/ticketing/insertReserver")) {
			historyBackCount  = 2;
		} else if(path.equals("/ticketing/cancelTicket")) {
			historyBackCount = 3;
		} else if(path.equals("/ticketing/payRequest"))
			historyBackCount = -1;
		return historyBackCount;
	}
}