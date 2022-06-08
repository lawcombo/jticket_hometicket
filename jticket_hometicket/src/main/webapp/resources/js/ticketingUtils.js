$(function(){
	
	var path = window.location.href.toLowerCase(); // /ticketing/checkTicket?content_mst_cd=DMZGONDOLA_0_1
	var pathSplit = path.split("?"); // /ticketing/checkTicket
	
	// /ticketing/ 에서만 동작
	if(pathSplit[0].indexOf("ticketing/") != -1) {
		$(".sub-a").each(function() {
			var hrefPath = this.href.toLowerCase();
			var hrefPathSplit = hrefPath.split("?");
			
			if (pathSplit[0] === hrefPathSplit[0]) {
				
				$(this).addClass("active");
				return;
			}
		});
	}
	
})