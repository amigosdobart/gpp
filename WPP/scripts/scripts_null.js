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
  alert("Preencha o campo " + campo)
  return false
}
