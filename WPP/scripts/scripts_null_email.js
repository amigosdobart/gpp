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