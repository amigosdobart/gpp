/***********************************************************************************
*	(c) Ger Versluis 2000 version 5.41 24 December 2001	          *
*	For info write to menus@burmees.nl		          *
*	You may remove all comments for faster loading	          *		
***********************************************************************************/

	var NoOffFirstLineMenus=6;			// Number of first level items
	var LowBgColor=null;			// Background color when mouse is not over
	var LowSubBgColor='#FF6600';			// Background color when mouse is not over on subs
	var HighBgColor=null;			// Background color when mouse is over
	var HighSubBgColor='#CC3300';			// Background color when mouse is over on subs
	var FontLowColor=null;			// Font color when mouse is not over
	var FontSubLowColor='#FFFFCC';			// Font color subs when mouse is not over
	var FontHighColor=null;			// Font color when mouse is over
	var FontSubHighColor='#FFFFCC';			// Font color subs when mouse is over
	var BorderColor=null;			// Border color
	var BorderSubColor='#FFFFCC';			// Border color for subs
	var BorderWidth=1;				// Border width
	var BorderBtwnElmnts=1;			// Border between elements 1 or 0
	var FontFamily="verdana,arial,helvetica"	// Font family menu items
	var FontSize=8;				// Font size menu items
	var FontBold=0;				// Bold menu items 1 or 0
	var FontItalic=0;				// Italic menu items 1 or 0
	var MenuTextCentered='left';			// Item text position 'left', 'center' or 'right'
	var MenuCentered='left';			// Menu horizontal position 'left', 'center' or 'right'
	var MenuVerticalCentered='top';		// Menu vertical position 'top', 'middle','bottom' or static
	var ChildOverlap=.0;				// horizontal overlap child/ parent
	var ChildVerticalOverlap=.32;			// vertical overlap child/ parent
	var StartTop=114;				// Menu offset x coordinate
	var StartLeft=0;				// Menu offset y coordinate
	var VerCorrect=0;				// Multiple frames y correction
	var HorCorrect=0;				// Multiple frames x correction
	var LeftPaddng=3;				// Left padding
	var TopPaddng=2;				// Top padding
	var FirstLineHorizontal=0;			// SET TO 1 FOR HORIZONTAL MENU, 0 FOR VERTICAL
	var MenuFramesVertical=1;			// Frames in cols or rows 1 or 0
	var DissapearDelay=1000;			// delay before menu folds in
	var TakeOverBgColor=1;			// Menu frame takes over background color subitem frame
	var FirstLineFrame='navig';			// Frame where first level appears
	var SecLineFrame='space';			// Frame where sub levels appear
	var DocTargetFrame='space';			// Frame where target documents appear
	var TargetLoc='';				// span id for relative positioning
	var HideTop=0;				// Hide first level when loading new document 1 or 0
	var MenuWrap=1;				// enables/ disables menu wrap 1 or 0
	var RightToLeft=0;				// enables/ disables right to left unfold 1 or 0
	var UnfoldsOnClick=0;			// Level 1 unfolds onclick/ onmouseover
	var WebMasterCheck=0;			// menu tree checking on or off 1 or 0
	var ShowArrow=0;				// Uses arrow gifs when 1
	var KeepHilite=1;				// Keep selected path highligthed
	var Arrws=['tri.gif',5,10,'tridown.gif',10,5,'trileft.gif',5,10];	// Arrow source, width and height

function BeforeStart(){return}
function AfterBuild(){return}
function BeforeFirstOpen(){return}
function AfterCloseAll(){return}


// Menu tree
//	MenuX=new Array(Text to show, Link, background image (optional), number of sub elements, height, width);
//	For rollover images set "Text to show" to:  "rollover:Image1.jpg:Image2.jpg"

Menu1=new Array("rollover:img/lnk_mn_conheca_off.gif:img/lnk_mn_conheca_on.gif","/index.asp","",0,18,149);

Menu2=new Array("rollover:img/lnk_mn_atuacao_off.gif:img/lnk_mn_atuacao_on.gif","","",0,18,149);
//	Menu2_1=new Array("Care Brasil","/sobre/brasil.asp","",0,18,160);
//	Menu2_2=new Array("Visão, Missão e Valores","/sobre/missao.asp","",0);
//	Menu2_3=new Array("Conselho Deliberativo","/sobre/conselho.asp","",0);
//	Menu2_4=new Array("Equipe Executiva","/sobre/equipeexec.asp","",0);
//	Menu2_5=new Array("Plano Estratégico","/sobre/plano.asp","",0);
//	Menu2_6=new Array("Orçamento anual","/sobre/orcamento.asp","",0);
//	Menu2_7=new Array("Care International","/sobre/careint.asp","",0);
//	Menu2_8=new Array("Perguntas e respostas","/sobre/faq.asp","",0);
//	Menu2_9=new Array("Carreira na CARE","/sobre/carreira.asp","",0);

Menu3=new Array("rollover:img/lnk_mn_excelencia_off.gif:img/lnk_mn_excelencia_on.gif","","",0,18,149);
//	Menu3_1=new Array("Efeito catalisador","/projetos/catalisador.asp","",0,20,150);
//		Menu3_1_1=new Array("Projeto Sul da Bahia","/projetos/bahia.asp","",0,20,180);
//		Menu3_1_2=new Array("Projeto Nova Onda da Maré","/projetos/mare.asp","",0);

Menu4=new Array("rollover:img/lnk_mn_missao_off.gif:img/lnk_mn_missao_on.gif","","",0,18,149);
//	Menu4_1=new Array("Brasil: pobreza e desigualdade","/pobreza/brasil.asp","",0,20,190);
//	Menu4_2=new Array("Fatos sobre o Brasil","/pobreza/fatos.asp","",0);
//	Menu4_3=new Array("Artigos e links","/pobreza/artlinks.asp","",0);

Menu5=new Array("rollover:img/lnk_mn_cenario_off.gif:img/lnk_mn_cenario_on.gif","","",0,18,149);
//	Menu5_1=new Array("Parceiros da CARE Brasil","/parceiros/parceiros.asp","",0,20,190);
//	Menu5_2=new Array("Parceiros na Ação","/parceiros/acao.asp","",0);
//	Menu5_3=new Array("Código de Ética","/parceiros/etica.asp","",0);

Menu6=new Array("rollover:img/lnk_mn_metas_off.gif:img/lnk_mn_metas_on.gif","","",0,18,149);
//	Menu6_1=new Array("Informações para imprensa","/imprensa/imprensa.asp","",0,20,190);
//	Menu6_2=new Array("CARE Brasil na mídia","/imprensa/midia.asp","",0);

// javascript:top.location.href='blank.htm'
