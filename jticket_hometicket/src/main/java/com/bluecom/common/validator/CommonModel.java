package com.bluecom.common.validator;

import java.io.Serializable;

public class CommonModel  implements Serializable{

	// Validator용 formㅜ믇 (Null일시 Class이름으로 진행)
	private String formName;

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}
}
