ns6 = (navigator.appName=="Netscape" && navigator.appVersion.substr(0,1)>4);
ns4 = (document.layers)? true:false
ie4 = (document.all)? true:false

function init() {
	if (ns6) {
		obj = document.getElementById('objSobe').style
		obj2 = document.getElementById('objBanner').style
		iHeight = parseInt(innerHeight)-220;
	}
	if (ns4) {
		obj = document.objBanner.document.objSobe
		obj2 = document.objBanner
		iHeight = parseInt(innerHeight)-220;
	}
	if (ie4) {
		obj = objSobe.style
		obj2 = objBanner.style
		iHeight = parseInt(document.body.clientHeight)-220;
	}
	obj.xpos = parseInt(obj.top)
	obj2.top = iHeight
}
function swapVisib(){
	if (ns4) {
		document.objBanner.visibility = "hide";
	} else if (ns6) {
		document.getElementById('objBanner').style.visibility = "hidden";
	} else {
		objBanner.style.visibility = "hidden";
	}
}	
function slide() {
	if (ns6) {
		obj = document.getElementById('objSobe').style
	}
	if (ns4) {
		obj = document.objBanner.document.objSobe
	}
	if (ie4) {
		obj = objSobe.style
	}
	if (obj.xpos > 0) {
		obj.xpos -= 10
		obj.top = obj.xpos
		setTimeout("slide()",30)
	}
}
//start slide
//setTimeout("slide()",2000)