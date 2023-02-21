// form의 input들을 Object로 바꾸어줌
function getFormData($form){
   	var unindexed_array = $form.serializeArray();
    var indexed_array = {};	

   	$.map(unindexed_array, function(n, i){
       	indexed_array[n['name']] = n['value'];
   	});

   	return indexed_array;
}

// 팝업창에서 부모창으로 값을 돌려줌
function returnValuesToParentWindow(result) {
	if(!window.opener != null && !window.opener.closed) {
		window.opener.setReturnedValuesFromChildWindow(result);
	}
	window.close();
}

const popupCenter = function popCenter(obj) {
	// obj {url, title, w, h}
	// Fixes dual-screen position                             Most browsers      Firefox
    const dualScreenLeft = window.screenLeft !==  undefined ? window.screenLeft : window.screenX;
    const dualScreenTop = window.screenTop !==  undefined   ? window.screenTop  : window.screenY;

    const width = window.innerWidth ? window.innerWidth : document.documentElement.clientWidth ? document.documentElement.clientWidth : screen.width;
    const height = window.innerHeight ? window.innerHeight : document.documentElement.clientHeight ? document.documentElement.clientHeight : screen.height;

    const systemZoom = width / window.screen.availWidth;
    const left = (width - obj.w) / 2 / systemZoom + dualScreenLeft
    const top = (height - obj.h) / 2 / systemZoom + dualScreenTop
    
    window.open(obj.url, obj.title, 
      'scrollbars=yes, width=' + (obj.w / systemZoom) + ', height=' + (obj.h / systemZoom) + ', top=' + top + ', left=' + left
    )

    //if (window.focus) newWindow.focus();
}

// 전화번호 하이픈 찍기
function addHyphenToPhoneNumber(phoneNumber) {
	return phoneNumber.replace(/[^0-9]/g, "").replace(/(^02|^0505|^1[0-9]{3}|^0[0-9]{2})([0-9]+)?([0-9]{4})$/,"$1-$2-$3").replace("--", "-");

}

//숫자를 3자리마다 콤마 찍기
function numberWithCommas(x) {
    x = x.toString();
    var pattern = /(-?\d+)(\d{3})/;
    while (pattern.test(x))
        x = x.replace(pattern, "$1,$2");
    return x;
}
