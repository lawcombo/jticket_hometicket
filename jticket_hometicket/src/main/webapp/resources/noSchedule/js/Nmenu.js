$(function() {

	

	
	
	
	//common
	$("#gnb").one().clone().appendTo(".mobileNav > div").attr("id", "gnbM");
	$(".iconTop").one().clone().prependTo(".mobileNav .top");
	for(var i=0; i<10;i++) {
		$("#gnb .gnb"+i).one().clone().appendTo("#lnb.menu"+ i);
	}

	pcGnb();
	mGnb();

	
	var str = $(location).attr("href");
	var res = str.split("/");
	var indexArr2 = res[res.length-2];
	var indexArr = res[res.length-1];
	indexArr = indexArr.split(".asp");

	if(!$("#mainVisual").length) {
		$("#lnb li a, #gnbM li a").each(function(){
			menuUrl = $(this).attr("href");
			var idx = $(this).parent().index();
			if (menuUrl.match(indexArr[0])){
				$(this).parent().addClass("on");
			}
			if (menuUrl.match(indexArr2)){
				$(this).parent().parent().parent().addClass("on");
				$("#gnbM > li.on > ul").show();
			}
		});	

	}

	
	$(".lang").on({
		click : function(){
			$(this).children("p").slideToggle(300);
		},
		mouseleave : function(){
			$(this).children("p").slideUp(300);
		}
	});
	

	$(window).scroll(function(){
		var wTop = $(window).scrollTop();
		if(wTop > 500) {
			$(".scrollTop").fadeIn();
		} else {
			$(".scrollTop").fadeOut();
		}
	});
	$(".scrollTop").click(function(e){
		e.preventDefault();
		$("html, body").animate({ scrollTop : 0},'fast');
	});





	//�쒕툕硫붾돱
//	  var items = $('#countrySelection-items').width(); //�꾩껜�곸뿭
//	  var itemSelected = document.getElementsByClassName('countrySelection-item');
//
//	/*
//	  $("#countrySelection-items").scrollLeft(200).delay(200).animate({
//		scrollLeft: "-=200"
//	  }, 2000, "easeOutQuad");
//	 */
//		$('.countrySelection-paddle-right').click(function () {
//			$("#countrySelection-items").animate({
//				scrollLeft: '+='+items
//			});
//		});
//		$('.countrySelection-paddle-left').click(function () {
//			$( "#countrySelection-items" ).animate({
//				scrollLeft: "-="+items
//			});
//		});
//
//	  if(!/Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent) ) {
//		var scrolling = false;
//
//		$(".countrySelection-paddle-right").bind("mouseover", function(event) {
//			scrolling = true;
//			scrollContent("right");
//		}).bind("mouseout", function(event) {
//			scrolling = false;
//		});
//
//		$(".countrySelection-paddle-left").bind("mouseover", function(event) {
//			scrolling = true;
//			scrollContent("left");
//		}).bind("mouseout", function(event) {
//			scrolling = false;
//		});
//
//		function scrollContent(direction) {
//			var amount = (direction === "left" ? "-=3px" : "+=3px");
//			$("#countrySelection-items").animate({
//				scrollLeft: amount
//			}, 1, function() {
//				if (scrolling) {
//					scrollContent(direction);
//				}
//			});
//		}
//	  }
//  
//	$('.countrySelection-item').click(function(e) {
//	//	e.preventDefault();
//		$('.boardM').find('.active').removeClass('active');
//		$(this).addClass("active");
//	});
//
//	//�좏깮�� li 媛�닔 * 媛�濡쒓컪
//	target = $(".countrySelection-item.active").index() ;
//	target = target * 65
//
//
//	$("#countrySelection-items").scrollLeft(target);
	
	
	
	
});





function pcGnb(){
	$("#gnb, .gnb_bg").on({
		mouseover : function(){
			$("#gnb ul, .gnb_bg").stop().slideDown(300);
		},
		mouseleave : function(){
			$("#gnb ul, .gnb_bg").stop().slideUp(300);
		},
		mouseout : function(){
			$("#gnb ul, .gnb_bg").stop().slideUp(300);
		}
	});
		
}



function mGnb(){
	$(".cateM").click(function(e){
		e.preventDefault0;
		if($(this).hasClass("on")){
			$(this).removeClass("on");
		} else {
			$(this).addClass("on");
			setTimeout(function(){
				$(".mobileNav, .m_bg").addClass("on");
			}, 450);
		}
		/* EX9때문에 삽입 */
		$('.mobileNav').css({
			display:"block"
		});
	});

	$(".mobileNav .close").click(function(e){
		e.preventDefault0;
		$(".mobileNav, .m_bg, .cateM").removeClass("on");
		
		/* EX9때문에 삽입 */
		$('.mobileNav').css({
			display:"none"
		});
	});

	
	$("#gnbM > li > a").click(function(e){
		if(!$(this).hasClass("target")){
			e.preventDefault();
		}
		var thisList = $(this).parent();
		thisList.siblings().removeClass("on").find("ul").slideUp();
		thisList.addClass("on");
		thisList.find("ul").slideDown();
	});

}
