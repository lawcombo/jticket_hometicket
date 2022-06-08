package com.bluecom.common.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bluecom.common.util.ScriptUtils;

import egovframework.rte.fdl.property.EgovPropertyService;

@Controller
public class CommonController extends BaseController{
	
	@Autowired
	EgovPropertyService properties;

	@RequestMapping("/main")
	public String main() throws Exception {
		
		return "redirect:https://jejubeer.co.kr/brewery-program";
//		return "test";
	}
	
	@RequestMapping(value="/validator")
	public String validator() {
		return "common/validator";
	}
	
	@RequestMapping(value="/emailPolicy")
	public String email() {
		return "/common/emailPolicy";
	}
	
	@RequestMapping(value="/privatePolicy")
	public String privatePolicy() {
		return "/common/privatePolicy";
	}
	
	@GetMapping("/testTicketing")
	public String testTicketing() throws Exception {
		
		return "test";
	}
	
	@RequestMapping("/")
	public String error(HttpServletRequest request, Model model) throws Exception{
		
		pageErrorLog(request);

		model.addAttribute("msg", "예외가 발생하였습니다");
		return "common/error";
	}
	
	@RequestMapping("/sessionError")
	public void sessionError(HttpSession session, Model model, HttpServletRequest request
			, HttpServletResponse response, RedirectAttributes redirectAttributes) throws Exception {
		
		String jejuUrl = properties.getString("jejubeer");
		ScriptUtils.alertAndMovePage(response, "세션이 만료되었습니다.", jejuUrl);
    }
}
