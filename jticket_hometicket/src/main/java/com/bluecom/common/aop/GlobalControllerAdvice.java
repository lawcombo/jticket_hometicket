package com.bluecom.common.aop;

import java.util.Date;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import com.bluecom.common.util.StartEndDatePropertyEditor;

@ControllerAdvice
public class GlobalControllerAdvice {

	@InitBinder
	protected void initStartDateBinder(WebDataBinder binder){
		binder.registerCustomEditor(Date.class, "startDate", new StartEndDatePropertyEditor());
	}
	@InitBinder
	protected void initEndDateBinder(WebDataBinder binder){
		binder.registerCustomEditor(Date.class, "endDate", new StartEndDatePropertyEditor());
	}

	// 여기서 @ModelAttribute로 session 값을 검증하는 것은 어떨까? (여러 창을 띄운 후 한쪽에서 세션값을 변경할 때의 처리)
}
