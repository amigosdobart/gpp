var ns4=(navigator.appName=="Netscape" && navigator.appVersion.substr(0,1)==4);
var ns6=(navigator.appName=="Netscape" && navigator.appVersion.substr(0,1)>4);

function getObj(objLayer,returnVar) {
	if (!returnVar) returnVar="obj";
	if (ns4) eval(returnVar + "=document." + objLayer);
	else if (ns6) eval(returnVar + "=document.getElementById('" + objLayer + "').style");
	else eval(returnVar + "=document.all." + objLayer + ".style");
}

function swapVisibility(objLayer,arrHidden,blnHideAll) {
	if (arrHidden) {
		for (i=0;i<eval(arrHidden + ".length");i++) {
			if (objLayer != eval(arrHidden + "[i]")) {
				getObj(eval(arrHidden + "[i]"));
				obj.visibility=(ns4)? "hide" : "hidden";
			}
		}
	}
	if (!objLayer=='') {
		getObj(objLayer);
		if (obj.visibility=='hidden' || obj.visibility=='hide') obj.visibility=(ns4)? "show" : "visible";
		else if (!arrHidden || blnHideAll) obj.visibility=(ns4)? "hide" : "hidden";
	}
}
