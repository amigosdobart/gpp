function Trim(sEntrada)
{
	sString = new String(sEntrada)
	for ( var iCount2 = 0;  iCount2 < sString.length ; iCount2++ )
		if ( sString.charAt(iCount2) != " ")
			break;
	for ( var iCount = sString.length - 1;  iCount > 0 ; iCount-- )
		if ( sString.charAt(iCount) != " ")
			break;
	if ( iCount2 > iCount ) 
		return ""
	return sString.substring(iCount2,iCount+1)
}

function isNull(oInput, campo)
{
  if ( Trim(oInput.value) != "" )
    return true
  oInput.focus()
  alert("Preencha o campo " + campo + ".")
  return false
}

function isNullCbo(oInput, campo)
{
  if ( Trim(oInput[oInput.selectedIndex].value) != "" )
    return true
  oInput.focus()
  alert("Selecione um item para " + campo + ".")
  return false
}

function isEmail(oEntrada) 
{
  var str = oEntrada.value
  var r1 = new RegExp("(@.*@)|(\\.\\.)|(@\\.)|(^\\.)");
  var r2 = new RegExp("^.+\\@(\\[?)[a-zA-Z0-9\\-\\.]+\\.([a-zA-Z]{2,3}|[0-9]{1,3})(\\]?)$");
  if ( (!r1.test(str) && r2.test(str)) )
    return true
  oEntrada.focus();
	alert("E-mail inválido")
	return false
}


function isSenha(oInput, campo, qtdMinCarac) {

     var s;
     s = oInput;
     var i; 
     var num = 0, carac = 0;
    if (!isNull(oInput, campo)) 
	{
		return false;
	}
	else
	{
	     for (i = 0; i < s.value.length; i++)
	     {
			var c = s.value.charAt(i);
		        // ha um numero
			if (((c >= "0") && (c <= "9")))
		 	{
				num++;
			}
			if (((c >= "a") && (c <= "z")) || ((c >= "A") && (c <= "Z")))
		 	{
				carac++;
			}
			if (c == "'" || c == "`" || c == "~" || c == '"' || c == '^')
			{
				alert("O campo " + campo + " contém caracteres inválidos.");
		          	oInput.focus();	
				return false;		
			}
	     }	
	     if (oInput.value.length < qtdMinCarac) {
		alert("A senha deve ter no mínimo " + qtdMinCarac + " caracteres.");
	        oInput.focus();
	        return false;
	     }
	     if(num < 2) {
					alert("A senha deve ter no mínimo 2 caracteres númericos.");
	        oInput.focus();
	        return false;
	     }
	
	     return true;
	 }
}