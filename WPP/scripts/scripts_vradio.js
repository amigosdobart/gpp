function vRadio(oEntrada,sCaption)
{
	//Somente um radio cadastrado
	if ( typeof(oEntrada[0]) == "undefined" )
	{
	  if ( oEntrada.checked )
	    return true
	  else
	    alert(sCaption)
	  return false
	}
	else
	{
	  	for ( var iCount = 0 ;  ; iCount ++ )
	  	{
	  	  if ( typeof( oEntrada[iCount] ) == "undefined" )
	  	    break;
	  	  if ( oEntrada[iCount].checked )
	  	    return true
	  	}
	  	oEntrada[0].focus()
	    if ( typeof(sCaption) == "undefined" )
	    	alert(oEntrada[0].name)
	    else
	      alert(sCaption)
	  	return false
  }
}