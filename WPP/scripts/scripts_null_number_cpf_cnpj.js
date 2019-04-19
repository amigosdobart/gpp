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


function validaCPF(oInput){

var s = oInput.value
while(s.lenght < 11)
	s = "0" + s;

var i;
var c = s.substr(0,9);
var dv = s.substr(9,2);
var d1 = 0;
	for (i = 0; i < 9; i++){
    		d1 += c.charAt(i)*(10-i);
   	}
	if (d1 == 0)
	{
	  alert("CPF Inválido. Verifique se há somente números.");
	  oInput.focus();
	  return false;
	}
    d1 = 11 - (d1 % 11);
    if (d1 > 9) d1 = 0;
    if (dv.charAt(0) != d1){
	  alert("CPF Inválido. Verifique se há somente números.");
	  oInput.focus();
	  return false;
    }
    
    d1 *= 2;
    for (i = 0; i < 9; i++){
    	d1 += c.charAt(i)*(11-i);
    }
    d1 = 11 - (d1 % 11);
    if (d1 > 9) d1 = 0;
    if (dv.charAt(1) != d1){
		alert("CPF Inválido. Verifique se há somente números.");
	  oInput.focus();
	  return false;
    }
    return true;
}



function validaCNPJ(oInput){
	
	var CNPJ = oInput.value
	
	while(CNPJ.length<14)
		CNPJ = "0" + CNPJ
	
	if (CNPJ.length<14)
	{
	  alert("CNPJ Inválido. Verifique se há somente números.");
	  oInput.focus();
	  return false;
	}
		
	CNPJ1 = CNPJ.substr(0, 12);
	CNPJ2 = CNPJ.substr(12, 2);
	Mult = "543298765432";
	Controle = "";
	Digito = 0;
		
	for (j = 1; j <= 2; j++)
	{
		Soma = 0;
		for (i = 0; i <= 11; i++)
		{
			Soma = Soma + (parseInt(CNPJ1.substr(i, 1)) * parseInt(Mult.substr(i, 1)));
		}
		if (j == 2)
		{
			Soma = Soma + (2 * Digito);
		}
		Digito = (parseInt(Soma) * 10) % 11;
		if (Digito == 10)
		{
			Digito = 0;
		}
		Controle = Controle + Digito.toString();
		Mult = "654329876543";			
	}

	if (Controle!=CNPJ2){		
		alert("CNPJ Inválido. Verifique se há somente números.")
		oInput.focus()
		return false;
	}
	else
	{
	   return true
	}	
}

function isNumber(oInput, campo)
{
  var sInput = oInput.value
  if ( !isNaN(sInput) ) {
    if ( String(new Number(sInput)) == String(sInput) )
    {
      if ( typeof(iMaxValue) != "undefined" )
      {
        if ( parseInt(oInput.value) > iMaxValue ) {
          alert("Este campo não deve ser maior que " + String(iMaxValue) + ".")
          return false
        }
      }
      return true;
    }
  }
  if (Trim(oInput.value) != "") {
  	return true
  }
  else {
	  oInput.focus()
	  alert("O campo " + campo + " não é válido.")
	  return false
 	}
}