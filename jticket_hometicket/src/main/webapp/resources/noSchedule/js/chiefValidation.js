/**
 * 
 */

/* 이름, 전화번호, 메일, ID, PW */
var regxName = new RegExp(/^[^\s]{0,20}$/);
var regxTell = new RegExp(/^\d{2,3}-\d{3,4}-\d{4}$/);
var regxMail = new RegExp(/^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/);
var regxId = new RegExp(/^[a-zA-Z][a-zA-Z\d]{3,15}$/);

function checkName(name){
	var a = regxName.exec(name);
	if(regxName.exec(name) == null){
		alert("이름은 20자 이하의 특수문자, 공백을 제외한 문자로 작성되어야 합니다.");
		return false;
	} else {
		return true;
	}
}
function checkTel(tel){
	if(regxTell.exec(tel) == null){
		alert("전화번호는 010-0000-0000 또는 (0)02-000-0000 규칙을 준수하여 작성해주세요.");
		return false;
	} else {
		return true;
	}
}
function checkMail(mail){
	if(regxMail.exec(mail) == null){
		alert("이메일의 형식에 맞추어 작성해주세요.\nex)abc@ticketdashboard.com");
		return false;
	} else {
		return true;
	}
}
function checkId(id){
	if(regxId.exec(id) == null){
		alert("아이디는 4~16자의 영문 대, 소문자와 숫자만 사용 가능합니다. ");
		return false;
	} else {
		return true;
	}
}
function checkPw(password){
	var num = password.search(/[0-9]/g);
	var eng = password.search(/[a-z]/g);
	var spe = password.search(/[\s]/g); // 공백을 포함한 특수문자와 매칭

	if(password.length < 4 || password.length > 20){
		alert("비밀번호는 4자리 ~ 20자리 이내로 입력해주세요.");
	    return false;
	}else if(spe > 0){
	    alert("비밀번호는 공백이나 특수문자 없이 입력해주세요.");
	    return false;
	}else if(num > 0 && eng > 0){
		alert("비밀번호는 특수문자를 제외한 영문,숫자를 혼합하여 입력해주세요.");
		return false;
	}else {
	    return true;
	}
}