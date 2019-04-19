/**

 - Funções genéricas JavaScript
 - Extraída do PL/SQL (pacoteBody.sql)
   Izaias Lisboa 06/08/2002 

*/

// Valida se InString contém apenas caracteres numéricos
function isNumberString (InString)  {
   if(InString.length==0)
      return (false);
   RefString="1234567890";
   for (Count=0; Count < InString.length; Count++)  {
   TempChar= InString.substring (Count, Count+1);
      if (RefString.indexOf (TempChar, 0)==-1)
         return (false);
   }
   return (true);
}

// --Retira todos os espaços em branco de um string
function stripSpaces (InString){
   OutString="";
   for (Count=0; Count < InString.length; Count++)  {
      TempChar=InString.substring (Count, Count+1);
      if (TempChar!=" ")
         OutString=OutString+TempChar;
   }
   return (OutString);
}

//--Valida se InString está dentro do intervalo informado
function isWithinRange (InString, RangeMin, RangeMax)  {
   if ((InString == null) || (InString == ""))
      return (false)
   if((InString>=RangeMin) && (InString<=RangeMax))
      return (true);
   else
      return (false);
}


// --Valida campo ddd do formulário
function validaDDD () {
   if (isNumberString(
      stripSpaces(document.forms[0].P_TELE_DDD.value)))
      return (true);
   else {
      alert("DDD inválido! DDD deve ser preenchido e conter apenas dígitos.");
      return (false);
   }
}


function JSLCheckRange(pctl, pval, pstyle, plowval, phival, pmsg) {
   var lval = "" + pval;
   if (lval != "") {
     var ctlval = parseInt(lval);
     if (pstyle == 1) { // full range
       if ( (ctlval < plowval) || (ctlval > phival)) { alert(pmsg); pctl.focus(); return false; }
     }
     if (pstyle == 2) { // check low value
       if (ctlval < plowval) { alert(pmsg); pctl.focus(); return false; }
     }
     if (pstyle == 3) { // check high value
       if (ctlval > phival) { alert(pmsg); pctl.focus(); return false; }
     }
   }
   return true;
}



function JSLChkMaxLength(pctl, plen, pmsg) {
   if (pctl.value.length > plen) {
     alert(pmsg);
     pctl.focus();
     return false;
   }
     return true;
}


function JSLChkNumScale (pctl, pval, pscale, pmsg) {
  if (pval != "") {
    var PointPos = pval.indexOf(".");
    if (PointPos != -1) {
      var Scale = pval.length - PointPos - 1;
      if (Scale > pscale) {
        alert(pmsg);
        pctl.focus();
        return false;
      }
    }
  }
  return true;
}


function JSLChkNumPrecision(pctl, pval, pprecision, pmsg) {
  if (pval != "") {
    var Prec = 0;
    var PointPos = pval.indexOf(".");
    // If a decimal point was not found
    // validate the number of digits in the whole string
    if (PointPos == -1) {
      Prec = pval.length;
    }
    else {  // Validate the number of digits before the decimal point
      Prec = PointPos;
    }
    if (Prec > pprecision) { alert(pmsg); pctl.focus(); return false; }
  }
   return true;
}


function JSLStripMask(p_val) {
  if (p_val == "") { return ""; }
  var str = p_val;
  str = JSLReplace(str, " ");

  str = JSLReplace(str, ",");
  str = JSLReplace(str, "$");
  if ((str.substring(0, 1) == "<") && (str.substring(str.length -1, str.length) == ">")) {
      str = "-" + str.substring(1, str.length - 1);
  }
  if (str.substring(str.length -1, str.length) == "-") {
    str = "-" + str.substring(0, str.length - 1);
  }
  if (str.substring(str.length -1, str.length) == "+") {
    str = str.substring(0, str.length - 1);
  }
  return str;
}


function JSLMakeUpper(pctl) {
   pctl.value = pctl.value.toUpperCase();
}



function JSLRadioValue(pctl) {
   var i;
   for (i=0;i<pctl.length;i++) {
      if (pctl[i].checked) {
         return pctl[i].value;
      }
   }
   return "";
}



function JSLChkConstraint(pconstraint, pmsg) {
   if (!(pconstraint)) { alert(pmsg); return false; }
     return true;
}

function JSLGetValue(pctl, ptype, pfalse) {
   var i = 0;
   if (ptype == null) { return pctl.value; }
   if (ptype == "CHECK") {
      if (pctl.checked) { return pctl.value; }
      else { return pfalse; }
   }
   if (ptype == "RADIO") {
      for (i=0;i<pctl.length;i++) {
         if (pctl[i].checked) { return pctl[i].value; }
      }
      return "";
   }
   if (ptype == "LIST") {
      if (pctl.selectedIndex >= 0) {
         if (pctl.options[pctl.selectedIndex].value != "") {
            return pctl.options[pctl.selectedIndex].value;
         }
         else { return pctl.options[pctl.selectedIndex].text; }
      }
      else { return ""; }
   }
}


function JSLReplace(pstr1, pstr2, pstr3) {
   if (pstr1 != "") {
     var rtnstr = "";
     var searchstr = pstr1;
     var addlen = pstr2.length;
     var index = pstr1.indexOf(pstr2);
     while ((index != -1) && (searchstr != "")) {
       rtnstr = rtnstr + searchstr.substring(0, index);
       if (pstr3 != null) {
         rtnstr = rtnstr + pstr3;
       }
       searchstr = searchstr.substring(index + addlen, searchstr.length);
       if (searchstr != "") {
          index = searchstr.indexOf(pstr2);
       }
       else { index = -1; }
     }
     return (rtnstr + searchstr);
   }
   else {
     return "";
   }
}

function JSLNotNull(pctl, pmsg){
   if (pctl.value == "") { alert(pmsg); pctl.focus(); return false; }
   return true;
}

function JSLToNumber(p_val) {
   var lval = JSLStripMask(p_val);
   if (lval == "") { return ""; }
   else { return parseFloat(lval); }
}
