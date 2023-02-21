

var english   = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ ";
var num       = "0123456789";
var comp      = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_";
var pw_comp   = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_";  
var ename     = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ 0123456789_~-()#.,";   
var pw_comp2  = "1234";
var pw_comp3  = "asdf";
var pw_comp4  = "qwer";
var pw_comp5  = "zxcv";
var pw_comp01 = "abcdefghijklmnopqrstuvwxyz";
var pw_comp02 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
var pw_comp03 = "123456789";
var pw_deny   = /[<,>,&,\",\']/;

//\n입력 방지
var newline = /.n/g;

function nl_ck(val){
  if( newline.test(val) == true ) {
    return true;
  }

  return false;
}


function PWCheck(id, value){

	var len = value.length;
	if(!(len >= 8 && len <= 15)){
		alert("비밀번호는 8자 이상 15자 이하여야 합니다.");
		return false;
	}

    chk1 = /[a-z]/;  //a-z와 0-9이외의 문자가 있는지 확인
    chk2 = /[A-Z]/;  //적어도 한개의 a-z 확인    
    chk3 = /\d/;  //적어도 한개의 0-9 확인
    chk4 = /[!,@,#,$,%,^,*,?,_,~]/;  //적어도 한개의 0-9 확인
    
    var tcnt = 0;
		
	if ( pw_deny.test(value) == true ) {
		alert('비밀번호는 < > & \" \' 기호를 사용하실 수 없습니다.');	
		return false;
	}
    
    if ( chk1.test(value) == true  || chk2.test(value) == true) {
    	tcnt = tcnt + 1;
    }
   	 
    if ( chk3.test(value) == true ) {
    	tcnt = tcnt + 1;
    }
    
    if ( chk4.test(value) == true ) {
    	tcnt = tcnt + 1;
    }      
    
    if ( tcnt < 3) {
    	alert('비밀번호는 영문자,숫자,특수기호의 조합으로 8~15자리를 사용해야 합니다.\n특수기호는 ! @ # $ % ^ * ? _ ~ 만 사용 가능합니다.');	
    	return false;
    }      

	for (var i=0; i < id.length-2; i++) {		 
				
		temp = id.substring(i, i+3);
		
		j = value.indexOf(temp);
		
		if ( j > -1 ) {
			alert("비밀번호에 아이디와 동일한 문자열을 3자이상 사용하실 수 없습니다.");	
			return false;
		} 
			
	}	    
    
 	if(/(\w)\1\1/.test(value))
    {
        alert('비밀번호에 같은 문자를 3번 이상 사용하실 수 없습니다.'); 
        return false;
    }
	
	var SamePass_0 = 0; //동일문자 카운트
	var SamePass_1 = 0; //연속성(+) 카운드
	var SamePass_2 = 0; //연속성(-) 카운드
	
//	var SamePass_3 = 0; //연속값2 체크카운트
	var SamePass_4 = 0; //연속값3 체크카운트
	var SamePass_5 = 0; //연속값4 체크카운트
	var SamePass_6 = 0; //연속값5 체크카운트
	
	for (var i=0; i<value.length-2; i++) {
		temp = value.substring(i, i+3);
		
		
		SamePass_0 = pw_comp01.indexOf(temp);
		SamePass_1 = pw_comp02.indexOf(temp);
		SamePass_2 = pw_comp03.indexOf(temp);
		
		if ( SamePass_0 > -1 ) {
			alert("연속된 문자열을 3자 이상 사용 할 수 없습니다.");	
			return false;
		}		
		if ( SamePass_1 > -1 ) {
			alert("연속된 문자열을 3자 이상 사용 할 수 없습니다.");	
			return false;
		}	
		if ( SamePass_2 > -1 ) {
			alert("연속된 문자열을 3자 이상 사용 할 수 없습니다.");	
			return false;
		}			
		
		SamePass_4 = pw_comp3.indexOf(temp);
		SamePass_5 = pw_comp4.indexOf(temp);
		SamePass_6 = pw_comp5.indexOf(temp);
		
		if ( SamePass_4 > -1 ) {
			alert("키보드상에서 연속한 위치의 문자열은 비밀번호로 사용하실 수 없습니다");	
			return false;
		}		
		if ( SamePass_5 > -1 ) {
			alert("키보드상에서 연속한 위치의 문자열은 비밀번호로 사용하실 수 없습니다");	
			return false;
		}	
		if ( SamePass_6 > -1 ) {
			alert("키보드상에서 연속한 위치의 문자열은 비밀번호로 사용하실 수 없습니다");	
			return false;
		}			
	}	

	return true;
}


function PWCheckRealtime(id, value){

	   var len = value.length;
	   if(!(len >= 8 && len <= 15)){
	      return "비밀번호는 8자 이상 15자 이하여야 합니다.";
	   }

	    chk1 = /[a-z]/;  //a-z와 0-9이외의 문자가 있는지 확인
	    chk2 = /[A-Z]/;  //적어도 한개의 a-z 확인    
	    chk3 = /\d/;  //적어도 한개의 0-9 확인
	    chk4 = /[!,@,#,$,%,^,*,?,_,~]/;  //적어도 한개의 0-9 확인
	    
	    var tcnt = 0;
	      
	   if ( pw_deny.test(value) == true ) {
	      return '비밀번호는 < > & \" \' 기호를 사용하실 수 없습니다.';
	   }
	    
	    if ( chk1.test(value) == true  || chk2.test(value) == true) {
	       tcnt = tcnt + 1;
	    }
	       
	    if ( chk3.test(value) == true ) {
	       tcnt = tcnt + 1;
	    }
	    
	    if ( chk4.test(value) == true ) {
	       tcnt = tcnt + 1;
	    }      
	    
	    if ( tcnt < 3) {
	       return '비밀번호는 영문자,숫자,특수기호의 조합으로 8~15자리를 사용해야 합니다.\n특수기호는 ! @ # $ % ^ * ? _ ~ 만 사용 가능합니다.';
	    }      

	   for (var i=0; i < id.length-2; i++) {       
	            
	      temp = id.substring(i, i+3);
	      
	      j = value.indexOf(temp);
	      
	      if ( j > -1 ) {   
	         return "비밀번호에 아이디와 동일한 문자열을 3자이상 사용하실 수 없습니다.";
	      } 
	         
	   }       
	    
	 if(/(\w)\1\1/.test(value))
	    {
	        return '비밀번호에 같은 문자를 3번 이상 사용하실 수 없습니다.';
	    }
	   
	   var SamePass_0 = 0; //동일문자 카운트
	   var SamePass_1 = 0; //연속성(+) 카운드
	   var SamePass_2 = 0; //연속성(-) 카운드
	   
	//   var SamePass_3 = 0; //연속값2 체크카운트
	   var SamePass_4 = 0; //연속값3 체크카운트
	   var SamePass_5 = 0; //연속값4 체크카운트
	   var SamePass_6 = 0; //연속값5 체크카운트
	   
	   for (var i=0; i<value.length-2; i++) {
	      temp = value.substring(i, i+3);
	      
	      
	      SamePass_0 = pw_comp01.indexOf(temp);
	      SamePass_1 = pw_comp02.indexOf(temp);
	      SamePass_2 = pw_comp03.indexOf(temp);
	      
	      if ( SamePass_0 > -1 ) {
	         return "연속된 문자열을 3자 이상 사용 할 수 없습니다.";
	      }      
	      if ( SamePass_1 > -1 ) {
	         return "연속된 문자열을 3자 이상 사용 할 수 없습니다.";
	      }   
	      if ( SamePass_2 > -1 ) {
	         return "연속된 문자열을 3자 이상 사용 할 수 없습니다.";
	      }         
	      
	      SamePass_4 = pw_comp3.indexOf(temp);
	      SamePass_5 = pw_comp4.indexOf(temp);
	      SamePass_6 = pw_comp5.indexOf(temp);
	      
	      if ( SamePass_4 > -1 ) {
	         return "키보드상에서 연속한 위치의 문자열은 비밀번호로 사용하실 수 없습니다";
	      }      
	      if ( SamePass_5 > -1 ) {
	         return "키보드상에서 연속한 위치의 문자열은 비밀번호로 사용하실 수 없습니다";
	      }   
	      if ( SamePass_6 > -1 ) {
	         return "키보드상에서 연속한 위치의 문자열은 비밀번호로 사용하실 수 없습니다";
	      }         
	   }   

	   return "";
	}



function PWCheck_f(id, value, lang){

	var len = value.length;
	
	if(!(len >= 8 && len <= 15)){
		if(lang == "ENG"){
			alert("Please enter in 8-15 characters including a english, number and a special characters. \n Special characters such as ! @ # $ % ^ * ? _ ~ can only be used.");
    		return false;
		}else if(lang == "CHN"){
			alert("请输入包含英文字母、数字、特殊文字的8~15个字符\n 特殊文字使用范围 : ! @ # $ % ^ * ? _ ~");
			return false;
		}else if(lang == "JPN"){
			alert("8~15字以内で英文字、数字、特殊文字を含む文字を入力してください。\n 特殊文字は!@#$%^*?_~のみ使用可能です。");
			return false;
		}
	}

    chk1 = /[a-z]/;  //a-z와 0-9이외의 문자가 있는지 확인
    chk2 = /[A-Z]/;  //적어도 한개의 a-z 확인    
    chk3 = /\d/;  //적어도 한개의 0-9 확인
    chk4 = /[!,@,#,$,%,^,*,?,_,~]/;  //적어도 한개의 0-9 확인
    
    var tcnt = 0;
    
    if ( pw_deny.test(value) == true ) {		
		if(lang == "ENG"){
			alert("Special characters such as < > & \" \' cannot be used in your password.");
    		return false;
		}else if(lang == "CHN"){
			alert("密码不得包含以下特殊符号： < > & \" \'");
			return false;
		}else if(lang == "JPN"){
			alert("パスワードに<>&\"\'の特殊文字は使用できません。");
			return false;
		}
	}
    
    if ( chk1.test(value) == true  || chk2.test(value) == true) {
    	tcnt = tcnt + 1;
    }
   	 
    if ( chk3.test(value) == true ) {
    	tcnt = tcnt + 1;
    }
    
    if ( chk4.test(value) == true ) {
    	tcnt = tcnt + 1;
    }      
    
    
    
    if ( tcnt < 3) {
		if(lang == "ENG"){
			alert("Please enter in 8-15 characters including a english, number and a special characters. \n Special characters such as ! @ # $ % ^ * ? _ ~ can only be used.");
    		return false;
		}else if(lang == "CHN"){
			alert("请输入包含英文字母、数字、特殊文字的8~15个字符\n 特殊文字使用范围 : ! @ # $ % ^ * ? _ ~");
			return false;
		}else if(lang == "JPN"){
			alert("8~15字以内で英文字、数字、特殊文字を含む文字を入力してください。\n 特殊文字は!@#$%^*?_~のみ使用可能です。");
			return false;
		}    	
    }

	for (var i=0; i < id.length-2; i++) {		 
				
		temp = id.substring(i, i+3);
		
		j = value.indexOf(temp);
		
		
		if ( j > -1 ) {
			if(lang == "ENG"){
	    		alert("While setting your password, you cannot use more than the three same characters you used for your ID.");
	    		return false;
			}else if(lang == "CHN"){
				alert("密码不可使用3位以上与ID相同的字符串。");
				return false;
			}else if(lang == "JPN"){
				alert("パスワードにIDと同一な文字列を3文字以上使用できません。");
				return false;
			}
		} 
			
	}	    

    
 	if(/(\w)\1\1/.test(value))
    {
 		if(lang == "ENG"){
    		alert("While setting your password, you cannot use a character more than three times.");
    		return false;
		}else if(lang == "CHN"){
			alert("密码不可使用3位以上相同的字符。");
			return false;
		}else if(lang == "JPN"){
			alert("パスワードに同じ文字を3回以上使用できません。");
			return false;
		}
    }

		
	
	var SamePass_0 = 0; //동일문자 카운트
	var SamePass_1 = 0; //연속성(+) 카운드
	var SamePass_2 = 0; //연속성(-) 카운드
	
	var SamePass_3 = 0; //연속값2 체크카운트
	var SamePass_4 = 0; //연속값3 체크카운트
	var SamePass_5 = 0; //연속값4 체크카운트
	var SamePass_6 = 0; //연속값5 체크카운트
	
	for (var i=0; i<value.length-2; i++) {
		temp = value.substring(i, i+3);
		
		
		SamePass_0 = pw_comp01.indexOf(temp);
		SamePass_1 = pw_comp02.indexOf(temp);
		SamePass_2 = pw_comp03.indexOf(temp);
		
		if ( SamePass_0 > -1 ) {
			if(lang == "ENG"){
	    		alert("consecutive characters cannot be used.");
	    		return false;
			}else if(lang == "CHN"){
				alert("密码不得包含3个以上的相同或连续字符.");
				return false;
			}else if(lang == "JPN"){
				alert("連続した文字列3字以上は使用できません。");
				return false;
			}
		}		
		if ( SamePass_1 > -1 ) {
			if(lang == "ENG"){
				alert("consecutive characters cannot be used.");
	    		return false;
			}else if(lang == "CHN"){
				alert("密码不得包含3个以上的相同或连续字符.");
				return false;
			}else if(lang == "JPN"){
				alert("連続した文字列3字以上は使用できません。");
				return false;
			}
		}	
		if ( SamePass_2 > -1 ) {
			if(lang == "ENG"){
				alert("consecutive characters cannot be used.");
	    		return false;
			}else if(lang == "CHN"){
				alert("密码不得包含3个以上的相同或连续字符.");
				return false;
			}else if(lang == "JPN"){
				alert("連続した文字列3字以上は使用できません。");
				return false;
			}
		}			
		
		SamePass_4 = pw_comp3.indexOf(temp);
		SamePass_5 = pw_comp4.indexOf(temp);
		SamePass_6 = pw_comp5.indexOf(temp);
		
		if ( SamePass_4 > -1 ) {
			if(lang == "ENG"){
	    		alert("Consecutive characters cannot be used for your password.");
	    		return false;
			}else if(lang == "CHN"){
				alert("密码不得包含来自键盘中的相同或连续字符.");
				return false;
			}else if(lang == "JPN"){
				alert("キーボード上で連続した位置の文字列は、パスワードに使用できません。");
				return false;
			}
		}
		if ( SamePass_5 > -1 ) {
			if(lang == "ENG"){
	    		alert("Consecutive characters cannot be used for your password.");
	    		return false;
			}else if(lang == "CHN"){
				alert("密码不得包含来自键盘中的相同或连续字符.");
				return false;
			}else if(lang == "JPN"){
				alert("キーボード上で連続した位置の文字列は、パスワードに使用できません。");
				return false;
			}
		}
		if ( SamePass_6 > -1 ) {
			if(lang == "ENG"){
	    		alert("Consecutive characters cannot be used for your password.");
	    		return false;
			}else if(lang == "CHN"){
				alert("密码不得包含来自键盘中的相同或连续字符.");
				return false;
			}else if(lang == "JPN"){
				alert("キーボード上で連続した位置の文字列は、パスワードに使用できません。");
				return false;
			}
		}		
	}	

	return true;
}


function PWCheck_val(chk_val, value, cd){
	if (cd == "01") {
		msg_text = "비밀번호는 전화번호와 동일하게 사용하실 수 없습니다.";
	} else if (cd == "02") {
		msg_text = "비밀번호는 휴대폰번호와 동일하게 사용하실 수 없습니다.";
	} else if (cd == "03") {
		msg_text = "비밀번호는 생년월일과 동일하게 사용하실 수 없습니다.";
	} else if (cd == "04") {
		msg_text = "비밀번호는 이름과 동일하게 사용하실 수 없습니다.";
	} else if (cd == "05") {
		msg_text = "비밀번호는 FAX번호와 동일하게 사용하실 수 없습니다.";
	} else if (cd == "06") {
		msg_text = "비밀번호는 사업자등록번호와 동일하게 사용하실 수 없습니다.";
	} else if (cd == "07") {
		msg_text = "비밀번호는 대표자명과 동일하게 사용하실 수 없습니다.";
	}

	j = value.indexOf(chk_val);

	if ( j > -1 ) {
		alert(msg_text);	
		return false;
	}		
	return true;
}

/*
 * PWCheck_new function 
 * param1: frm 폼이름 ex) document.form1
 * param2: checkid
 * param3: checkedLang default)KOR 
 * param4: pwdName pwd이름
 * param5: pwdConName pwd확인이름
 * param6: compareType 비교처리 1:입력값 검증 2:db검증
 * param7: compareVal pwd와 개인정보 비교
 * param8: backFun 처리완료 후 처리 함수
 */
function PWCheck_new(frm, checkId, checkLang, pwdName, pwdConName, compareType, compareVal, backFun ){
	var f = frm;
	var formLength = $(f).size();
	
	if (formLength != 1) {
		alert(" form이 존재하지 않습니다.");
		return false;
	}

	$(frm).append('<input type="hidden" name="checkId" id="checkId" value="'+checkId+'"/>');
	$(frm).append('<input type="hidden" name="pwdName" id="pwdName" value="'+pwdName+'"/>');
	$(frm).append('<input type="hidden" name="pwdConName" id="pwdConName" value="'+pwdConName+'"/>');
	$(frm).append('<input type="hidden" name="checkLang" id="checkLang" value="'+checkLang+'"/>');
	$(frm).append('<input type="hidden" name="compareType" id="compareType" value="'+compareType+'"/>');
	$(frm).append('<input type="hidden" name="compareVal" id="compareVal" value="'+compareVal+'"/>');
	$(frm).append('<input type="hidden" name="backFun" id="backFun" value="'+backFun+'"/>');
	$(frm).append('<input type="hidden" name="urlLocation" id="urlLocation" value="'+location.href+'"/>');
	$("#encYn").remove();
	$(frm).append('<input type="hidden" name="encYn" id="encYn" value="Y"/>');
	
	if($("#checkId").size() < 1){
		return;
	}
	var result = svcf_Ajax("/app/com/LSCO600101.do", f, {
		async : false,
		processClss : 'R'
	});
	svcf_SyncCallbackFn(result, fnChkCallBack);
}

function fnChkCallBack(status, data){
	var backFun = $("#backFun").val();
	
	$("#checkId").remove();
	$("#pwdName").remove();
	$("#pwdConName").remove();
	$("#checkLang").remove();
	$("#compareType").remove();
	$("#compareVal").remove();
	$("#backFun").remove();
	$("#urlLocation").remove();

	if(status.code == 0){
		if ( backFun != "") {
			eval(backFun).call(this, "");
		}
	} else {
		alert(status.message);
		if (data.focusNm != ""){
			eval("$('#"+data.focusNm+"').focus()");
		}
	}
}

function PWCheck_val_f(chk_val, value, cd, lang){
	
	var msg_text = "";
	
	if (cd == "01") {
		if(lang == "ENG"){
			msg_text = "The password must not be identical with your phone number.";
		}else if(lang == "CHN"){
			msg_text = "不可使用电话号码做密码。";
		}else if(lang == "JPN"){
			msg_text = "電話番号と同じ文字列はパスワードとして使用できません。";
		}
	} else if (cd == "02") {
		if(lang == "ENG"){
			msg_text = "The password must not be identical with your mobile phone number.";
		}else if(lang == "CHN"){
			msg_text = "不可使用手机号码做密码。";
		}else if(lang == "JPN"){
			msg_text = "携帯電話番号と同じ文字列はパスワードとして使用できません。";
		}
	} else if (cd == "03") {
		if(lang == "ENG"){
			msg_text = "The password must not be identical with your date of birth.";
		}else if(lang == "CHN"){
			msg_text = "不可使用生日做密码。";
		}else if(lang == "JPN"){
			msg_text = "生年月日と同じ文字列はパスワードとして使用できません。";
		}
	} else if (cd == "04") {
		if(lang == "ENG"){
			msg_text = "The password must not be identical with your name.";
		}else if(lang == "CHN"){
			msg_text = "不可使用姓名做密码。";
		}else if(lang == "JPN"){
			msg_text = "お名前と同じ文字列はパスワードとして使用できません。";
		}
	}

	j = value.indexOf(chk_val);

	if ( j > -1 ) {
		alert(msg_text);	
		return false;
	}		
	return true;
}
