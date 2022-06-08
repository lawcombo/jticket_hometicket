package com.bluecom.common.validator;

import org.apache.commons.validator.Validator;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springmodules.validation.commons.DefaultValidatorFactory;

public class CustomDefaultValidatorFactory extends DefaultValidatorFactory {

	@Override
	public Validator getValidator(String beanName, Object bean, Errors errors) {
		
		if(bean instanceof CommonModel) {
			CommonModel commonModel = (CommonModel)bean;
			if(StringUtils.hasText(((CommonModel) bean).getFormName())) {
				return super.getValidator(commonModel.getFormName(), bean, errors);
			}
		}
		
		return super.getValidator(beanName, bean, errors);
	}	
}
