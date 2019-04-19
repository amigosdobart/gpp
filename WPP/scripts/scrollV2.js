var upH = 12; // Height of up-arrow
var upW = 11; // Width of up-arrow
var downH = 12; // Height of down-arrow
var downW = 11; // Width of down-arrow
var dragH = 14; // Height of scrollbar
var dragW = 9; // Width of scrollbar
var scrollH = 162; // Height of scrollbar
var speed = 12; // Scroll speed

var navegador = new DetectaNavegador();

var clickUp, clickDown, clickDrag, clickAbove, clickBelow = false;
var timer = setTimeout("",500);
var upL, upT, downL, downT, dragL, dragT, rulerL, rulerT, contentT, contentH, contentClipH, scrollLength, startY;
var mouseY, mouseX;
var pageWidth, pageHeight = null;

function DetectaNavegador(){
	this.ver=navigator.appVersion;
	this.agent=navigator.userAgent;
	this.dom=document.getElementById?1:0;
	this.opera5=(navigator.userAgent.indexOf("Opera")>-1 && document.getElementById)?1:0;
	this.ie5=(this.ver.indexOf("MSIE 5")>-1 && this.dom && !this.opera5)?1:0;
	this.ie6=(this.ver.indexOf("MSIE 6")>-1 && this.dom && !this.opera5)?1:0;
	this.ie4=(document.all && !this.dom && !this.opera5)?1:0;
	this.ie=(document.all)?1:0;
	this.mac=this.agent.indexOf("Mac")>-1;
	this.ns6=(this.dom && parseInt(this.ver) >= 5) ?1:0;
	this.ns4=(document.layers && !this.dom)?1:0;

	this.bw=(this.ie6 || this.ie5 || this.ie4 || this.ns4 || this.ns6 || this.opera5);

	this.oculta = (this.ie || this.dom)? "hidden" : "hide";
	this.mostrar = (this.ie || this.dom)? "visible" : "show";
}

function modifica(id,t,l,v) {
	if(navegador.dom) gaveta_mapa = document.getElementById(id).style;
	if(navegador.ns4)	gaveta_mapa = document.layers[id];
	if(navegador.ie4)	gaveta_mapa = document.all[id].style;
		with(gaveta_mapa) {
			top  = t
			left = l
			visibility = v;
		}
}

function alinhaScroll() {

	pageWidth = (navegador.ie)? document.body.offsetWidth : innerWidth;
	if(pageWidth<800) { pageWidth = 800; }

	pageHeight = (navegador.ie)? document.body.scrollHeight : document.height;

	ajusteNs = (navegador.ns4)? 1 : 0;
	
//	var posLeft   = (pageWidth+1)/2+1;
	var posLeft   = (603); // posicao de todos os itens do scroll
//	var posTop1   = ((pageHeight/2) - (220/2) -31) + ajusteNs;
	var posTop1   = (472) + ajusteNs;	// posicao 'top' do bgscroll
//	var posTop2   = ((pageHeight/2) - (220/2) -31) + ajusteNs;
	var posTop2   = (463) + ajusteNs;	// posicao 'top' da seta de cima
//	var posTop3   = ((pageHeight/2) - (220/2) +100) + ajusteNs;
	var posTop3   = (635) + ajusteNs;	// posicao 'top' da seta de baixo
//	var posTop4   = ((pageHeight/2) - (220/2) -41) + ajusteNs;
	var posTop4   = (473) + ajusteNs;	// posicao 'top' do botao drag
//	var posTop5   = ((pageHeight/2) - (220/2) -32) + ajusteNs;
	var posTop5   = (470) + ajusteNs; // posicao 'top' do conteudo - contentClip
	

	modifica('bgscroll',posTop1,posLeft+1,navegador.mostrar);
	modifica('up',posTop2,posLeft+1,navegador.mostrar);
	modifica('down',posTop3,posLeft+1,navegador.mostrar);
	modifica('drag',posTop4,posLeft+3,navegador.mostrar);
	modifica('ruler',posTop4,posLeft,navegador.mostrar);
	modifica('contentClip',posTop5,posLeft-295,navegador.mostrar);
}

function down(e){
	if((navegador.ns4 && e.which!=1) || (navegador.ie && event.button!=1)) return true;
	getMouse(e);
	startY = (mouseY - dragT);
	if(mouseX >= upL && (mouseX <= (upL + upW)) && mouseY >= upT && (mouseY <= (upT + upH))){
		clickUp = true;
		return scrollUp();
	}	
	else if(mouseX >= downL && (mouseX <= (downL + downW)) && mouseY >= downT && (mouseY <= (downT + downH))){
		clickDown = true;
		return scrollDown();
	}
	else if(mouseX >= dragL && (mouseX <= (dragL + dragW)) && mouseY >= dragT && (mouseY <= (dragT + dragH))){
		clickDrag = true;
		return false;
	}
	else if(mouseX >= dragL && (mouseX <= (dragL + dragW)) && mouseY >= rulerT && (mouseY <= (rulerT + scrollH))){
		if(mouseY < dragT){
			clickAbove = true;
			clickUp = true;
			return scrollUp();
		}
		else{
			clickBelow = true;
			clickDown = true;
			return scrollDown();
		}
	}
	else{
		return true;
	}
}

function move(e){
	if(clickDrag && contentH > contentClipH){
		getMouse(e);
		dragT = (mouseY - startY);
		
		if(dragT < (rulerT))
			dragT = rulerT;		
		if(dragT > (rulerT + scrollH - dragH))
			dragT = (rulerT + scrollH - dragH);
		
		contentT = ((dragT - rulerT)*(1/scrollLength));
		contentT = eval('-' + contentT);

		moveTo();

		if(navegador.ie)
			return false;
	}
}

function up(){
	clearTimeout(timer);
	clickUp = false;
	clickDown = false;
	clickDrag = false;
	clickAbove = false;
	clickBelow = false;
	return true;
}

function getT(){
	if(navegador.ie)
		contentT = document.all.content.style.pixelTop;
	else if(navegador.ns4)
		contentT = document.contentClip.document.content.top;
	else if(navegador.dom)
		contentT = parseInt(document.getElementById("content").style.top);
}

function getMouse(e){
	if(navegador.ie){
		mouseY = event.clientY + document.body.scrollTop;
		mouseX = event.clientX + document.body.scrollLeft;
	}
	else if(navegador.ns4 || navegador.dom){
		mouseY = e.pageY;
		mouseX = e.pageX;
	}
}

function moveTo(){
	if(navegador.ie){
		document.all.content.style.top = contentT;
		document.all.ruler.style.top = dragT;
		document.all.drag.style.top = dragT;
	}
	else if(navegador.ns4){
		document.contentClip.document.content.top = contentT;
		document.ruler.top = dragT;
		document.drag.top = dragT;
	}
	else if(navegador.dom){
		document.getElementById("content").style.top = contentT + "px";
		document.getElementById("drag").style.top = dragT + "px";
		document.getElementById("ruler").style.top = dragT + "px";
	}
}

function scrollUp(){
	getT();
	
	if(clickAbove){
		if(dragT <= (mouseY-(dragH/2)))
			return up();
	}
	
	if(clickUp){
		if(contentT < 0){		
			dragT = dragT - (speed*scrollLength);
			
			if(dragT < (rulerT))
				dragT = rulerT;
				
			contentT = contentT + speed;
			if(contentT > 0)
				contentT = 0;
			
			moveTo();
			timer = setTimeout("scrollUp()",25);
		}
	}
	return false;
}

function scrollDown(){
	getT();
	
	if(clickBelow){
		if(dragT >= (mouseY-(dragH/2)))
			return up();
	}

	if(clickDown){
		if(contentT > -(contentH - contentClipH)){			
			dragT = dragT + (speed*scrollLength);
			if(dragT > (rulerT + scrollH - dragH))
				dragT = (rulerT + scrollH - dragH);
			
			contentT = contentT - speed;
			if(contentT < -(contentH - contentClipH))
				contentT = -(contentH - contentClipH);
			
			moveTo();
			timer = setTimeout("scrollDown()",25);
		}
	}
	return false;
}

function reloadPage(){
	location.reload();
	alinhaScroll();
}

function eventLoader(){

	if(navegador.ie){
		upL = document.all.up.style.pixelLeft;
		upT = document.all.up.style.pixelTop;		
		downL = document.all.down.style.pixelLeft;
		downT = document.all.down.style.pixelTop;
		dragL = document.all.drag.style.pixelLeft;
		dragT = document.all.drag.style.pixelTop;		
		rulerT = document.all.ruler.style.pixelTop;		
		contentH = parseInt(document.all.content.scrollHeight);
		contentClipH = parseInt(document.all.contentClip.style.height);
	}
	else if(navegador.ns4){
		upL = document.up.left;
		upT = document.up.top;		
		downL = document.down.left;
		downT = document.down.top;		
		dragL = document.drag.left;
		dragT = document.drag.top;		
		rulerT = document.ruler.top;
		contentH = document.contentClip.document.content.clip.bottom;
		contentClipH = document.contentClip.clip.bottom;
	}
	else if(navegador.dom){
		upL = parseInt(document.getElementById("up").style.left);
		upT = parseInt(document.getElementById("up").style.top);
		downL = parseInt(document.getElementById("down").style.left);
		downT = parseInt(document.getElementById("down").style.top);
		dragL = parseInt(document.getElementById("drag").style.left);
		dragT = parseInt(document.getElementById("drag").style.top);
		rulerT = parseInt(document.getElementById("ruler").style.top);
		contentH = parseInt(document.getElementById("content").offsetHeight);
		contentClipH = parseInt(document.getElementById("contentClip").offsetHeight);
		document.getElementById("content").style.top = 0 + "px";
	}
	
	if(contentClipH > contentH) {
		modifica('bgscroll',0,0,navegador.oculta);
		modifica('up',0,0,navegador.oculta);
		modifica('down',0,0,navegador.oculta);
		modifica('drag',0,0,navegador.oculta);
		modifica('ruler',0,0,navegador.oculta);
	}
	
	scrollLength = ((scrollH-dragH)/(contentH-contentClipH));
	if(navegador.ns4){
		document.captureEvents(Event.MOUSEDOWN | Event.MOUSEMOVE | Event.MOUSEUP);
	}
	window.onresize = ajusta;
	document.onmousedown = down;
	document.onmousemove = move;
	document.onmouseup = up;
}

function ajusta() {
	if(navegador.ns4){
		reloadPage();
	} else {
		eventLoader();
	}
}

window.onload = eventLoader;
