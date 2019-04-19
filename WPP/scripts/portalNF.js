/*
*  Valida Email
*  Utilizar da seguinte forma: validaEmail(document.forms[#].elements[#])
*/
function validaEmail(email)
{
	if (email.value.indexOf('@') == -1)
	{
		alert('Email não está no formato adequado.');
		email.focus();
		return false;
	}
	else return true;
}


/**
*	Função que retira os espaços em branco a direita e a esquerda, retornando uma nova string sem espaços em branco.
*/
function trim(s) {
	
	  while (s.substring(0,1) == ' ') {
	    s = s.substring(1,s.length);
	  }
	  while (s.substring(s.length-1,s.length) == ' ') {
	    s = s.substring(0,s.length-1);
	  }
	  return s;
}

/*
*  Funcao para fazer formatacao de período : mm/aaaa
*  Utilizar da seguinte forma: formataPeriodo(this)
*/
function formataPeriodo(campo, e)
{
	car = ( navigator.appName == "Netscape" ) ? e.which : e.keyCode;
	
	if ( ( car < 48 || car > 57 ) && ( car > 31 ) ) return false;
	if ( campo.value.length==2 )
	{
		campo.value+='/';
	}

	return true;
}

/*
*  Funcao para validar o período, verificando mes e ano.
*  Utilizar da seguiinte forma: validaPeriodo(campo_date)
*/

function validarPeriodo(Wparam) {
	if ( trim( Wparam.value ) != "" ) {
		barra = Wparam.value.indexOf("/");
		mes = Wparam.value.substring(0,barra);
		ano = Wparam.value.substring(barra+1);
		data = mes + ano;
		if ( data.length < 6 ) {
			alert("O formato correto do período é mm/aaaa");
			Wparam.focus();
			return false;
		}
		if ( mes > 12 || mes < 1 ){
			alert("Mês digitado é inválido");
			Wparam.focus();
			return false;
		}
   }
   return true;
}

/*
*  Funcao para fazer formatacao de datas : dd/mm/aaaa
*  Utilizar da seguinte forma: onKeypress="formataData(this,event);"
*/
function formataData( campo, e ) {

	car = (navigator.appName == "Netscape" ) ? e.which : e.keyCode;
	
	if ( ( car < 48 || car > 57 ) && ( car > 31 ) ) return false;
	if ( ( campo.value.length==2 ) || ( campo.value.length == 5 ) )
	{
		campo.value+='/';
	}

	return true;
}

/*
*  Funcao para fazer formatacao de mes/ano : mm/aaaa
*  Utilizar da seguinte forma: onKeypress="formataData(this,event);"
*/
function formataMesAno(campo, e) 
{	
	car = (navigator.appName == "Netscape" ) ? e.which : e.keyCode;
	
	if ((car < 48 || car > 57) && (car > 31)) 
	{
		return false;
	}
	
	if (campo.value.length==2)
	{
		campo.value+='/';
	}

	return true;
}

/*
*  Funcao para fazer formatacao de datas : dd/mm/aaaa
*  Utilizar da seguinte forma: formata_data(this,event)
*/
function formataMsisdn( campo, e ) {
	car = (navigator.appName == "Netscape" ) ? e.which : e.keyCode;
	
	if ( ( car < 48 || car > 57 ) && ( car > 31 ) ) return false;
	if ( campo.value.length==1 )
	{
		if (campo.value != '(')
		campo.value='(' + campo.value;
	}
	if ( campo.value.length == 3 )
	{
		campo.value+=')';
	}
	if ( campo.value.length==8 )
	{
		campo.value+='-';
	}

	return true;
}

function formataCep( campo, e ) {
	car = (navigator.appName == "Netscape" ) ? e.which : e.keyCode;
	
	if ( ( car < 48 || car > 57 ) && ( car > 31 ) ) return false;
	if ( campo.value.length==5 )
	{
		campo.value+='-';
	}
	return true;
}


/* 
*	Retorna o valor do msisdn sem os caracteres de apresentação ao usuário
*	Uso: document.frm.msisdn.value="valorPuro(document.frm.msisdn.value);";
*/
function valorPuro(msisdn)
{
  intMsisdn = "";
  for(counter = 0; counter < msisdn.length; counter++)
    if(isNumChar(msisdn.charAt(counter)))
      intMsisdn += msisdn.charAt(counter);
    return intMsisdn;
}

function isNumChar(char)
{
  if(char == "0") return true;
  if(char == "1") return true;
  if(char == "2") return true;
  if(char == "3") return true;
  if(char == "4") return true;
  if(char == "5") return true;
  if(char == "6") return true;
  if(char == "7") return true;
  if(char == "8") return true;
  if(char == "9") return true;
  return false;
}

function formataMsisdn2(msisdn)
{
  intValue = "";
  result = "";
  for(counter = 0; counter < msisdn.length; counter++)
    if(isNumChar(msisdn.charAt(counter)))
      intValue += msisdn.charAt(counter);
      
  if(intValue.length >= 2) result += "(";
  for(counter = 0; (counter < msisdn.length) && (counter < 2); counter++)
    result += intValue.charAt(counter);
  if(intValue.length >= 2) result += ")";
  
  if(intValue.length >= 10)
  {
    for(counter = 2; ((counter < msisdn.length) && (counter < 6)); counter++)
      result += intValue.charAt(counter);
    result += "-";
    for(counter = 6; (counter < msisdn.length) && (counter < 10); counter++)
      result += intValue.charAt(counter);
  }
  else
  {
    for(counter = 2; ((counter < msisdn.length) && (counter < 5)); counter++)
      result += intValue.charAt(counter);
    if(intValue.length > 5) result += "-";
    for(counter = 5; (counter < msisdn.length) && (counter < 9); counter++)
      result += intValue.charAt(counter);
  }
  return result;
}

function retiraMascaraMsisdn(msisdn)
{
	result = "";
	for(i = 0; i < msisdn.length; i++)
	{
	  if(isNumChar(msisdn.charAt(i)))
	  {
	    result += msisdn.charAt(i);
	  }
	}
	result = "55" + result;
	return result;   
}
  
//Converte o código ASCII para caracteres. Válido para números
function intAsciiToChar(intAscii)
{
  if(intAscii == 48) return "0";
  if(intAscii == 49) return "1";
  if(intAscii == 50) return "2";
  if(intAscii == 51) return "3";
  if(intAscii == 52) return "4";
  if(intAscii == 53) return "5";
  if(intAscii == 54) return "6";
  if(intAscii == 55) return "7";
  if(intAscii == 56) return "8";
  if(intAscii == 57) return "9";
  return "";
}

/*
*  NAO USAR ESSE METODO!! (vide formataDecimal())
*
*  Função que formata o valor de entrada para moeda
*/
function formataMoeda(moeda, event)
{
  intValue = "";
  result = "";
    
  char = (navigator.appName == "Netscape" ) ? event.which : event.keyCode;
  if ( ( char < 48 || char > 57 ) && ( char > 31 ) ) return moeda;
  
  for(counter = 0; counter < moeda.length; counter++)
    if(isNumChar(moeda.charAt(counter)))
      intValue += moeda.charAt(counter);
  
  while((intValue.length > 0) && (intValue.charAt(0) == "0"))
    intValue = intValue.substring(1, intValue.length);
      
  if(intValue.length == 0)
    return "0,0" + intValue;
  if(intValue.length == 1)
    return "0," + intValue;
  if(intValue.length >= 2)
  {
    result = ',' + intValue.substring(intValue.length - 1, intValue.length);
    for(counter = intValue.length - 2; counter >= 0; counter--)
    {
      result = intValue.charAt(counter) + result;
      if((counter > 0) && (((intValue.length-counter-1)%3) == 0))
        result = '.' + result;
    }
  }
  return result;
}

/**
* NAO USAR ESSE METODO!! (vide formataDecimal())
*/
function formataMoeda2(moeda, flag, numeroCasas)
{
  intValue = "";
  result = "";
  
  var offset = 0;
  if (numeroCasas) offset = numeroCasas - 2;
  
  for(counter = 0; counter < moeda.length; counter++)
    if(isNumChar(moeda.charAt(counter)))
      intValue += moeda.charAt(counter);
  if(flag!="ajusteCredito" && flag!="ajusteDebito")
  {
  while((intValue.length > 0) && (intValue.charAt(0) == "0"))
    intValue = intValue.substring(1, intValue.length);
  } 
  if(intValue.length == 0)    
    return ""
  if(intValue.length == 1)
    return "0,0" + intValue;
  if(intValue.length == 2)
    return "0," + intValue;
  if(intValue.length > 2)
  {
    result = ',' + intValue.substring(intValue.length - 2, intValue.length);
    for(counter = intValue.length - 3; counter >= 0; counter--)
    {
      result = intValue.charAt(counter) + result;
      if((counter > 0) && (((intValue.length-counter-2)%3) == 0))
        result = '.' + result;
    }
  }
  return result;
}


/*
*  So permite a entrada de numero num campo text de um formulario.
*  Deve ser chamado no evento KeyPress da seguinte forma: onKeyPress="soNumerosTarifa(event)"
*/
function soNumerosTarifa(e)
{
	car = ( navigator.appName == "Netscape" ) ? e.which : e.keyCode;

	if ( car > 31 && ( ( car < 48 && car != 46 ) || ( car > 57 ) ) )
		return false;
	else 
		return true;
}

/* 
*	Remove qualquer caractere nao numerico do campo informado
*/
function formataNumero2(obj)
{
	valor = obj.value;
  	intMsisdn = "";
  
  	for(counter = 0; counter < valor.length; counter++)
    	if(isNumChar(valor.charAt(counter)))
      		intMsisdn += valor.charAt(counter);
    obj.value = intMsisdn;
}

/*
*  Funcao para fazer formatacao de CPF : 008.225.764-73
					  
*  Utilizar da seguinte forma: formata_cpf(this)
*/
function formata_cpf(campo, e)
{
	car = ( navigator.appName == "Netscape" ) ? e.which : e.keyCode;
	
	if ( ( car < 48 || car > 57 ) && ( car > 31 ) ) return false;

	switch(campo.value.length) {
		case 3:
			campo.value+='.';
			break;
		case 7:
			campo.value+='.';
			break;
		case 11:
			campo.value+='-';
			break;
	}

	return true;
}

/* 
* FORMATA CPF 2 
*
OnBLur="this.value=formataCpf2(this.value)"
*/

function formataCpf2(cpf)
{
  intValue = "";
  result = "";
  for(counter = 0; counter < cpf.length; counter++)
    if(isNumChar(cpf.charAt(counter)))
      intValue += cpf.charAt(counter);
      
  for(counter = 0; (counter < cpf.length) && (counter < 3); counter++)
    result += intValue.charAt(counter);
  if(intValue.length >= 3) result += ".";
  
  if(intValue.length >= 11)
  {
    for(counter = 3; ((counter < cpf.length) && (counter < 6)); counter++)
      result += intValue.charAt(counter);
    result += ".";
    
    for(counter = 6; (counter < cpf.length) && (counter < 9); counter++)
      result += intValue.charAt(counter);
      result += "-";
    for(counter = 9; counter < cpf.length && (counter < 11); counter++)
      result += intValue.charAt(counter);
  }
  return result;
}

function formataCep2(cep)
{
  intValue = "";
  result = "";
  for(counter = 0; counter < cep.length; counter++)
    if(isNumChar(cep.charAt(counter)))
      intValue += cep.charAt(counter);
      
  for(counter = 0; (counter < cep.length) && (counter < 5); counter++)
    result += intValue.charAt(counter);
  if(intValue.length >= 5) result += "-";
  
    for(counter = 5; (counter < cep.length) && (counter < 8); counter++)
      result += intValue.charAt(counter);
  return result;
}

function formataCnpj2(cnpj)
{
  intValue = "";
  result = "";
  for(counter = 0; counter < cnpj.length; counter++)
    if(isNumChar(cnpj.charAt(counter)))
      intValue += cnpj.charAt(counter);
      
  for(counter = 0; (counter < cnpj.length) && (counter < 2); counter++)
    result += intValue.charAt(counter);
  if(intValue.length >= 3) result += ".";
  
  if(intValue.length >= 14)
  {
    for(counter = 2; ((counter < cnpj.length) && (counter < 5)); counter++)
      result += intValue.charAt(counter);
    result += ".";
    
    for(counter = 5; (counter < cnpj.length) && (counter < 8); counter++)
      result += intValue.charAt(counter);
      result += "/";
      
    for(counter = 8; (counter < cnpj.length) && (counter < 12); counter++)
      result += intValue.charAt(counter);
      result += "-";
      
    for(counter = 12; (counter < cnpj.length) && (counter < 14); counter++)
      result += intValue.charAt(counter);
  }
  return result;
}

/*
*  Funcao para fazer formatacao de Cnpj : 99.999.999/9999.99
					  99 999 999 999 99 		
*  Utilizar da seguinte forma: formata_cnpj(this)
*/
function formataCnpj(campo, e) {
	
	car = (navigator.appName == "Netscape" ) ? e.which : e.keyCode;
	
	if ( ( car < 48 || car > 57) && ( car > 31 ) )
	  return false;

	switch(campo.value.length){
		case 2:
			campo.value+='.';
			break;
		case 6:
			campo.value+='.';
			break;
		case 10:
			campo.value+='/';
			break;
		case 15:
			campo.value+='-';
			break;
	}

	return true;
}

/*
*  Funcao para validar a data, verificando mes dia e ano.
*  Utilizar da seguinte forma: validaData(campo_date)
*/
function validardata(Wparam) {
	if ( trim( Wparam.value ) != "" ){
		barra = Wparam.value.indexOf("/");
		dia = Wparam.value.substring(0,barra);
		string1 = Wparam.value.substring(barra+1);
		barra = string1.indexOf("/");
		mes = string1.substring(0,barra);
		ano = string1.substring(barra+1);
		data = dia + mes + ano;
		if (data.length < 8) {
			alert("O formato correto da data é dd/mm/aaaa");
			Wparam.focus();
			return false;
		}
		if (dia > 31 || dia < 1){
			alert("Dia digitado é inválido");
			Wparam.focus();
			return false;
		}
		if (mes > 12 || mes < 1){
			alert("Mês digitado é inválido");
			Wparam.focus();
			return false;
		}
		if (mes == 4 || mes == 6 || mes == 9 || mes == 11){
			if (dia > 30){
				alert("O mês "+mes+" possui 30 dias");
				Wparam.focus();
				return false;
			}
		}
		if (mes == 02){
			bis = (ano % 4 == 0 && (ano % 100 != 0 || ano % 400 == 0));
			if (dia>29 || (dia==29 && !bis)){
				hoje = new Date()
				anoh = hoje.getYear()
				mesh = hoje.getMonth()
				if (anoh > ano) {
				  t = "teve";
				}
				else {
					if(anoh < ano) {
					  t = "terá";
					}
					else {
					  if(mesh > 2) {
					    t = "teve";
					  }
					  else {
					    if(mesh < 2) {
					      t = "terá";
					    }
					    else {
					      t = "tem";
					    }
                 }
		         }
	         }
            alert("Fevereiro do ano "+ano+" não "+t+" "+dia+" dias");
            Wparam.focus();
            return false;
        }
     }
    }
}

/*
*  Funcao para validar a data, verificando mes e ano.
*  Utilizar da seguinte forma: validaMesAno(campo)
*/
function validaMesAno(pData) 
{
	if (trim(pData.value) != "")
	{
		barra = pData.value.indexOf("/");
		mes = pData.value.substring(0,barra);
		ano = pData.value.substring(barra + 1);
		data = mes + ano;
		if (data.length < 6) 
		{
			alert("O formato correto da data é mm/aaaa");
			pData.focus();
			return false;
		}
		if (mes < 1 || mes > 12)
		{
			alert("Mês digitado é inválido");
			pData.focus();
			return false;
		}
	}
}

/*
*  So permite a entrada de numero num campo text de um formulario.
*  Deve ser chamado no evento KeyPress da seguinte forma: onKeyPress="soNumeros(event)"
*/
function soNumeros(e)
{
	car = ( navigator.appName == "Netscape" ) ? e.which : e.keyCode;
	
	if ( car > 31 && ( car < 48 || car > 57 ) )
		return false;
	else 
	   return true;
}

/*
*  So permite a entrada de numero (incluindo o 'X') num campo text de um formulario.
*  Deve ser chamado no evento KeyPress da seguinte forma: onKeyPress="soNumerosEx(event)"
*/
function soNumerosEx(e)
{
	car = ( navigator.appName == "Netscape" ) ? e.which : e.keyCode;
	if (car == 120 || car == 88)
		return true;
	
	if ( car > 31 && ( car < 48 || car > 57 ) )
		return false;
	else 
	   return true;
}


function validacnpj(campo) {
 	if ( trim( campo.value ) != "" ) {
		var Num = limpa(campo.value);
		var Mult1 = '543298765432';
		var Mult2 = '6543298765432';
		var dig1 = 0;
		var dig2 = 0;
		var X = 0;
		for (var X = 0; X < 13; X++) {
			dig1 += (Num.substr(X, 1) * Mult1.substr(X, 1));
		}
		for (var X = 0; X < 14; X++) {
			dig2 += (Num.substr(X, 1) * Mult2.substr(X, 1));
		}
		dig1 = (dig1 * 10) % 11;
		dig2 = (dig2 * 10) % 11;
	
		if (dig1 == 10)	
			dig1 = 0;
		if (dig2 == 10)
			dig2 = 0;
		if (dig1 != Num.substr(12, 1)) {
			window.alert("CNPJ Inválido");
			campo.focus();
			return (false);
		}
		if (dig2 != Num.substr(13, 1)){
			window.alert("CNPJ Inválido");
			campo.focus();
			return (false);
		}
   }
	return (true);
}

function limpa(str) {
  var digitos = "0123456789";
  var temp = "";
  var digito = "";
  for (var i=0; i < str.length; i++) {
    digito = str.charAt(i);
    if (digitos.indexOf(digito)>=0) {
      temp=temp+digito;
    } 
  }
  return temp;
}

function inverte(str) {
		var temp="";
		var S=str;
		for (var i=0; i<S.length; i++) {
			temp=S.charAt(i)+temp
		}
		return temp	
}

function resto(str) {
		var invertido = inverte(limpa(str));
		var soma = 0;
		for (var i=0; i<invertido.length; i++){
			soma=soma+(i+2)*eval(invertido.charAt(i))
		}
		soma*=10;
		return ((soma % 11) % 10)
}

function validacpf(campo) {
  var valor = campo.value;
  var result = "";
  var OK = false;
  var temp = limpa(valor);
  if (temp.length>10) {
    var work=temp.substring(0,(temp.length)-2);
    var iresto = resto(work);
    OK = (iresto == eval(temp.charAt((temp.length)-2)));
    if (OK) {
      work = work+temp.charAt((temp.length)-2);
      iresto = resto(work);
      OK = (iresto == eval(temp.charAt((temp.length)-1)));
    }
  }
  if (OK) {
    return true;
  }
}

/*
* Função que verifica se um valor é numérico.
* Utilizar da seguinte forma: isNumber(document.forms[#].elements[#].value)
*/
function isNumber(inputVal)
{
	oneDecimal = false;
	inputStr = inputVal.toString();
	for(var i = 0;i < inputStr.length; i++)
	{
		var oneChar = inputStr.charAt(i);
		if(oneChar == "." && !oneDecimal)
		{
			oneDecimal = true;
			continue;
		}
		if(oneChar < "0" || oneChar > "9")
		{
			return false;
		}
	}
	return true;
}

/*
* NAO USAR ESSE METODO!! (vide formataDecimal())
*
* Função que formata Moeda.
* Utilizar da seguinte forma: onKeyUp="formataCampoMoeda(this);"
*/
function formataCampoMoeda(valor)
{
	var valorFormatado;
	var valorAux1;
	var valorAux=valor.value;

	if(valorAux.indexOf(",") != -1)
	{
		valorAux1 = valorAux.split(",");
		valorAux = valorAux1[0]+valorAux1[1];
	}

	while((pos=valorAux.indexOf(".")) != -1)
	{
		valorAux = valorAux.substring(0,pos)+valorAux.substr(pos+1);
	}

	var vl = parseInt(valorAux);
	valorAux = vl.toString();
	var tam = valorAux.length;

        if((!isNumber(valorAux)) && (tam > 0))
	{
		//alert("Favor preencher campo apenas com n\372meros.");
		//valor.value = valorAux.substring(0,tam-1);
		return;
	}


	if((tam > 2))
	{
		var valorAuxInt = valorAux.substring(0,tam-2);
		var tamInt = valorAuxInt.length;
		var dim = tamInt-3;
		while(dim > 0)
		{
			valorAuxInt = valorAuxInt.substring(0,dim)+"."+valorAuxInt.substr(dim);
			dim = dim-3;
		}
		valorFormatado = valorAuxInt+","+valorAux.substring(tam-2,tam);
		valor.value = valorFormatado;
	}
	else
	{
		valor.value = valorAux;
	}
}

/*
* Função que formata Moeda.
* Utilizar da seguinte forma: onKeyUp="formataCampoQtde(this);"
*/
function formataCampoQtde(campo)
{
	var valorFormatado;
	var valorAux1;
	var valorAux=campo.value;

	if(valorAux.indexOf(",") != -1)
	{
		valorAux1 = valorAux.split(",");
		valorAux = valorAux1[0]+valorAux1[1];
	}

	while((pos=valorAux.indexOf(".")) != -1)
	{
		valorAux = valorAux.substring(0,pos)+valorAux.substr(pos+1);
	}

	var vl = parseInt(valorAux);
	valorAux = vl.toString();
	var tam = valorAux.length;

        if((!isNumber(valorAux)) && (tam > 0))
	{
		//alert("Favor preencher campo apenas com n\372meros.");
		//valor.value = valorAux.substring(0,tam-1);
		return;
	}


	if((tam > 3))
	{
		var valorAuxInt = valorAux.substring(0,tam-3);
		var tamInt = valorAuxInt.length;
		var dim = tamInt-3;
		while(dim > 0)
		{
			valorAuxInt = valorAuxInt.substring(0,dim)+"."+valorAuxInt.substr(dim);
			dim = dim-3;
		}
		valorFormatado = valorAuxInt+","+valorAux.substring(tam-3,tam);
		campo.value = valorFormatado;
	}
	else
	{
		campo.value = valorAux;
	}
}

/*
* NAO USAR ESSE METODO!! (vide formataDecimal())
*
* Função que formata Quantidade da NotaFiscal.
* Utilizar da seguinte forma: onBlur="formataCampoQtdeOnBlur(this);"
*/
function formataCampoQtdeOnBlur(campo)
{
	var tam = campo.value.length;

	if (tam <= 3)
	{
		if(!isNumber(campo.value))	{
			alert("Favor preencher campo apenas com números.");
			campo.value = campo.value.substring(0,tam-1);
		} else if( tam == 1 ) {
			campo.value = "0,00"+campo.value;
		} else if ( tam == 2 ) {
			campo.value = "0,0"+campo.value;
		} else if ( tam == 3 ) {
			campo.value = "0,"+campo.value;
		}

	}
}


/*
* NAO USAR ESSE METODO!! (vide formataDecimal())
*
* Função que formata Moeda.
* Utilizar da seguinte forma: onBlur="formataCampoMoedaOnBlur(this);"
*/
function formataCampoMoedaOnBlur(valor)
{
	var tam = valor.value.length;


	if (tam <= 2)
	{
		if(!isNumber(valor.value))
		{
			alert("Favor preencher campo apenas com números.");
			valor.value = valor.value.substring(0,tam-1);
		}
		else if(tam == 1)
		{
			valor.value = "0,0"+valor.value;
		}
		else if(tam == 2)
		{
			valor.value = "0,"+valor.value;
		}
	}
}

/*
*  Funcão que verifica o preenchimento dos campos obrigatorios.
*  Parte do pressuposto que os campos obrigatorios terão nome iniciado com _obr.
*  Utilizar da seguinte forma: (document.forms[#])
*/
function valida(frm)
{
    for (i=0;i<frm.length;i++)
    {
        if (frm.elements[i].name.search('obr') != -1)
            if ((trim(frm.elements[i].value) == '') && (frm.elements[i].disabled == false))
            {
                alert('Por favor, preencha todos os campos obrigatórios.');
                frm.elements[i].focus();
                return 0;
            }
    }

    return 1;
}

/********************** FUNÇÕES DE VALIDAÇÃO DE CAMPOS DE ENTRADA ********************/

/*
*  Função que verifica se o Canal de Recarga foi selecionado
*  Para utilização em:
*    - filtroConsultaRecarga.vm
*/
function isSelectedCanalRecarga(canalRecarga)
{
  if((canalRecarga.value == "0") || (canalRecarga.value == ""))
  {
    alert('Favor selecionar o Canal da Recarga.');
    return false;
  }
  return true;
}

/*
*  Função que verifica se o Motivo de Ajuste foi selecionado
*  Para utilização em:
*    - filtroAjusteCredito.vm
*    - filtroAjusteDebito.vm
*/
function isSelectedMotivoAjuste(motivoAjuste)
{
  if(motivoAjuste.value == 0)
  {
    alert('Favor selecionar o Motivo do Ajuste.');
    return false;
  }
  return true;
}

/*
*  Função que verifica se a Origem da Contestação foi selecionada
*  Para utilização em:
*    - filtroConsultaContestacao.vm
*/
function isSelectedOrigem(origem)
{
  if(origem.value == 0)
  {
    alert('Favor selecionar a Origem da Contestação.');
    return false;
  }
  return true;
}

/*
*  Função que verifica se a Origem da Recarga foi selecionado
*  Para utilização em:
*    - filtroConsultaRecarga.vm
*/
function isSelectedOrigemRecarga(origemRecarga)
{
  if((origemRecarga.value == "0") || (origemRecarga.value == ""))
  {
    alert('Favor selecionar a Origem da Recarga.');
    return false;
  }
  return true;
}

/*
*  Função que verifica se o período foi selecionado
*  Para utilização em:
*    - filtroConsultaComprovanteServico.vm
*    - filtroConsultaHistoricoExtrato.vm
*/
function isSelectedPeriodo(periodo)
{
  if(periodo.value == 0)
  {
    alert('Favor selecionar o Período de Pesquisa.');
    return false;
  }
  return true;
}

/*
*  Função que verifica se o Tipo de Comprovante foi selecionado
*  Para utilização em:
*    - filtroConsultaHistoricoExtrato.vm
*/
function isSelectedTipoExtrato(tipoExtrato)
{
  if(tipoExtrato.value == 0)
  {
    alert('Favor selecionar o Tipo de Extrato.');
    return false;
  }
  return true;
}

/*
*  Função que valida o número do Boletim de Sindicância
*  Para utilização em:
*    - filtroConsultaContestacao.vm
*/
function validaBS(bs)
{
  if(bs == "")
  {
    alert('Favor preencher o número do Boletim de Sindicância.');
    return false;
  }
  return true;
}

/*
*  Função que valida a data final quando o tipo de pesquisa selecinado for por datas
*  Para utilização em:
*    - filtroConsultaComprovanteServico.vm
*    - filtroConsultaHistoricoExtrato.vm
*/
function validaDataFim(dataFim)
{
  if(dataFim == "")
  {
    alert('Favor preencher a Data Final de Pesquisa.');
    return false;
  }
  dfValida = true;
  sysdate = new Date();
  sysyear = sysdate.getYear();
  sysmonth = sysdate.getMonth() + 1;
  sysday = sysdate.getDate();
  dfyear = dataFim.substring(6, 10);
  dfmonth = dataFim.substring(3, 5);
  dfday = dataFim.substring(0, 2);
  if(sysyear < dfyear) dfValida = false;
  else if(sysyear == dfyear)
    if(sysmonth < dfmonth) dfValida = false;
    else if(sysmonth == dfmonth)
      if(sysday < dfday) dfValida = false;
  if(!dfValida)
    alert('A Data Final de Pesquisa deve ser menor ou atual à Data Atual.');
  return dfValida;
}

/*
*  Função que valida a data inicial quando o tipo de pesquisa selecinado for por datas
*  Para utilização em:
*    - filtroConsultaComprovanteServico.vm
*    - filtroConsultaHistoricoExtrato.vm
*/
function validaDataInicio(dataInicio)
{
  if(dataInicio == "")
  {
    alert('Favor preencher a Data Inicial de Pesquisa.');
    return false;
  }
  diValida = true;
  sysdate = new Date();
  sysyear = sysdate.getYear();
  sysmonth = sysdate.getMonth() + 1;
  sysday = sysdate.getDate();
  diyear = dataInicio.substring(6, 10);
  dimonth = dataInicio.substring(3, 5);
  diday = dataInicio.substring(0, 2);
  if(sysyear < diyear) diValida = false;
  else if(sysyear == diyear)
    if(sysmonth < dimonth) diValida = false;
    else if(sysmonth == dimonth)
      if(sysday < diday) diValida = false;
  if(!diValida)
    alert('A Data Inicial de Pesquisa deve ser menor ou atual à Data Atual.');
  return diValida;
}

/*
*  Função que valida se a data final é posterior à data inicial
*  quando o tipo de pesquisa selecinado for por datas
*  Para utilização em:
*    - filtroConsultaComprovanteServico.vm
*    - filtroConsultaHistoricoExtrato.vm
*/
function validaDatasInicioFim(dataInicio, dataFim)
{
  difValida = true;
  diyear = dataInicio.substring(6, 10);
  dimonth = dataInicio.substring(3, 5);
  diday = dataInicio.substring(0, 2);
  dfyear = dataFim.substring(6, 10);
  dfmonth = dataFim.substring(3, 5);
  dfday = dataFim.substring(0, 2);
  if(dfyear < diyear) difValida = false;
  else if(dfyear == diyear)
    if(dfmonth < dimonth) difValida = false;
    else if(dfmonth == dimonth)
      if(dfday < diday) difValida = false;
  if(!difValida)
    alert('A Data Final de Pesquisa deve ser maior que a Data Inicial.');
  return difValida;
}

/*
*  Função que valida se o campo Dias de Expiração foi preenchido
*  Para utilização em:
*    - editAdministrativoAjuste.vm
*/
function validaDiasExpiracao(diasExpiracao)
{
  if(diasExpiracao.length == 0)
  {
    alert('Favor determinar os Dias de Expiração dos créditos do Ajuste.');
    return false;
  }
  return true;
}

/*
*  Função que valida se o Motivo de Ajuste foi preenchido
*  Para utilização em:
*    - editAdministrativoAjuste.vm
*/
function validaMotivoAjuste(motivoAjuste)
{
  if(motivoAjuste.length == 0)
  {
    alert('Favor preencher o Motivo de Ajuste.');
    return false;
  }
  return true;
}

/*
*  Função que valida se o campo de 
 foi preenchido corretamente (10 números)
*  Para utilização em: 
*    - filtroAjusteCredito.vm
*    - filtroAjusteDebito.vm
*    - filtroConsultaComprovanteServico.vm
*    - filtroConsultaHistoricoExtrato.vm
*/
function validaMsisdn(msisdn,campo)
{
  intMsisdn = "";
  if (campo==null)
  		campo="Número de Acesso"
  for(counter = 0; counter < msisdn.length; counter++)
    if(isNumChar(msisdn.charAt(counter)))
      intMsisdn += msisdn.charAt(counter);
      
  if(intMsisdn.length < 9)
  {
    alert('O '+campo+' está incompleto. Favor Corrigir! (DDD + Número)');
    return false;
  }
  return true;
}


/* 
*	Retorna o valor do msisdn sem os caracteres de apresentação ao usuário
*	Uso: document.frm.msisdn.value="msisdnPuro(document.frm.msisdn.value);";
*/
function msisdnPuro(msisdn)
{
  intMsisdn = "";
  for(counter = 0; counter < msisdn.length; counter++)
    if(isNumChar(msisdn.charAt(counter)))
      intMsisdn += msisdn.charAt(counter);
    return intMsisdn;
}

/*
*  Função que valida se o Tipo de Extrato foi preenchido
*  Para utilização em:
*    - editTipoExtrato.vm
*/
function validaTipoExtrato(tipoExtrato)
{
  if(tipoExtrato.length == 0)
  {
    alert('Favor preencher o Nome do Tipo de Extrato');
    return false;
  }
  return true;
}

/*
*  Função que valida se o Valor de Ajuste foi preenchido
*  Para utilização em:
*    - filtroAjusteCredito.vm
*    - filtroAjusteDebito.vm
*/
function validaValorAjuste(valorAjuste)
{
  if(valorAjuste.length == 0)
  {
    alert('Favor preencher o Valor de Ajuste');
    return false;
  }
  return true;
}

/*
*  Função que valida se o Valor de Extrato foi preenchido
*  Para utilização em:
*    - editTipoExtrato.vm
*/
function validaValorExtrato(valorExtrato)
{
  if(valorExtrato.length == 0)
  {
    alert('Favor preencher o Valor de Extrato');
    return false;
  }
  return true;
}

/*
*  Função que valida se o campo de número de voucher foi preenchido com os primeiros
*  8 números
*  Para utilização em: 
*    - consultaCartoesPrePagos.vm 
*/
function validaVoucher8Digitos(numVoucher)
{
  if(numVoucher.length < 8)
  {
    alert('Favor preencher com os 8 primeiros dígitos do Cartão Pré Pago');
    return false;
  }
  return true;
}

/*
*  Função que valida se o campo de número PIN de voucher foi preenchido com os 6 números
*  Para utilização em: 
*    - consultaCartoesPrePagos.vm 
*/
function validaVoucher6Digitos(numVoucher)
{
  if(numVoucher.length < 6)
  {
    alert('Favor preencher os 6 dígitos do Código de Segurança do Cartão Pré Pago');
    return false;
  }
  return true;
}


/*************************************************************************************/

/*
*  Funcão que verifica o preenchimento dos campos obrigatorios.
*  @param frm formulário, qtdeObr quantidade de filtros obrigatórios.
*  Utilizar da seguinte forma: (document.forms[#])
*/
function validaFiltro(frm,qtdeObr)
{

	qtdePreenchido = 0;
	for (i=0;i<frm.length;i++)
    {
        if (frm.elements[i].type == 'text' || frm.elements[i].type == 'select-one') { 
            if ( trim( frm.elements[i].value ) != '' ) {
            	qtdePreenchido = qtdePreenchido +1;
            }
        } 
    }

	if(qtdePreenchido < qtdeObr){
                alert('Por favor, preencha pelo menos '+ qtdeObr +' filtro(s).');
                return 0;
	}

    return 1;
}


/*
*	Funções usadas no formulário de usuário. Tem a função de transferir dados de uma combo para outra.
*  Utilização :   onclick="sel();" e onclick="rem();"
*  A função onSubmit="posta()" serve para colocar os itens escolhidos separados por virgula para serem enviados.
*  
*/

    function setMultCombo(frm,strOr,strDest){
      var objO = frm[strOr];
      var objD = frm[strDest];
      var tam  = parseInt(objO.options.length)-1;
      for (var i=tam;i>=0;i--){
        if (objO.options[i].selected){
          objD.options[objD.options.length] = new Option(objO.options[i].text,objO.options[i].value);
          objO.options[i] = null;
        }
      }
    }



function posta() {
  var f = document.form1;
  var prim = 1;
  var xtxt = "";
  var valCampo = "";
  
  for (j=0;j < f.p_campos.options.length;j++) {
    if (prim == 0) 
    	xtxt = xtxt + ","; 
    valCampo = f.p_campos.options[j].text;
    tamCNPJ =  valCampo.indexOf("|");
    xtxt = xtxt + ("fornecedor_" + valCampo.substring(0,tamCNPJ - 1) ); 
    prim = 0; 
  }
  //alert(xtxt);
  document.form1.p_fornecedor.value   = xtxt;
  return true;
}

function postaInterno(frm) {
  var f = frm;
  var prim = 1;
  var xtxt = "";
  for (j=0;j < f.p_campos.options.length;j++) {
    if (prim == 0) xtxt = xtxt + ","; 
    xtxt = xtxt + f.p_campos.options[j].value; 
    prim = 0; 
  }
  document.form1.p_filial.value   = xtxt;
  return true;
}



/**
* NAO USAR ESSE METODO!! (vide formataDecimal())
*/
function formataReal(ValoraFormatar) {
    var i ;
    var decimalPointDelimiter = ",";
    var posDecPoint = parseInt("");
    var hasDecPoint = false;
    var s = new String(ValoraFormatar);
    var sAux = new String("");

    for (i = 0; i < s.length; i++)
    {   
    	var c = s.charAt(i);
    	if ( c == '.' ) c = decimalPointDelimiter;
	    sAux += c;
      if (c == decimalPointDelimiter){ 
        	  hasDecPoint = true;
        	  posDecPoint = i;
        	  break;}
    }
    for (var j = i+1; j < s.length; j++) sAux += s.charAt(j);
    
    if (!hasDecPoint) //(isNaN(posDecPoint)) 
      {	ValoraFormatar = s + ",00";}
    else
      {	
      	s = sAux + '00';
        ValoraFormatar= s.charAt(0);
        for (i = 1; i <= (posDecPoint+2); i++){ 
            ValoraFormatar += s.charAt(i);}
        if (posDecPoint == 0) ValoraFormatar = '0'+ValoraFormatar;
      }
    s = ValoraFormatar;
    ValoraFormatar = "";
    i=0;

    for (var j=s.length-4; j>=0; j--)
	{
		i++;
		ValoraFormatar = s.charAt(j) + ValoraFormatar;
		if (i == 3 && j != 0)
		{
			ValoraFormatar = "." + ValoraFormatar;
			i = 0;
		}
	}	    	
	ValoraFormatar +=  s.substring(s.length-3,s.length);
	
    return ValoraFormatar;
}

/*
*   Função que verifica o intervalo das datas.
*   Utilizar da seguinte forma: (document.forms[#].elementes[#],document.forms[#].elementes[#])
*/
function validaIntervaloData(dt_inicio,dt_fim)
{

	var dt = new Date();

	dia_inicio=dt_inicio.value.substr(0,2);
	mes_inicio=dt_inicio.value.substr(3,2);
	ano_inicio=dt_inicio.value.substr(6,4);

	dia_final=dt_fim.value.substr(0,2);
	mes_final=dt_fim.value.substr(3,2);
	ano_final=dt_fim.value.substr(6,4);

	if (ano_inicio > ano_final)
	{
		alert("A data de inicio deve ser anterior à data fim.");
		dt_inicio.focus();
		return false;
	}

	if ((ano_inicio == ano_final) && (mes_inicio > mes_final))
	{
		alert("A data de inicio deve ser anterior à data fim.");
		dt_inicio.focus();
		return false;
	}

	if ((ano_inicio == ano_final) && (mes_inicio == mes_final) && (dia_inicio > dia_final))
	{
		alert("A data de inicio deve ser anterior à data fim.");
		dt_inicio.focus();
		return false;
	}

	return true;

}


/*
* Função para desabilitar os campos quando o perfil do usuário for HelpDesk.
* Ex.: desabilitaCamposHelpDesk(document.nomeDoForm)
*/

function desabilitaCamposHelpDesk( frm ) {

	for (var i = 0; i< frm.length; i++) {
		if (frm.elements[i].type == 'submit'){
			 frm.elements[i].disabled = false;
		}else{
			frm.elements[i].disabled = true;
		}
	}
}



function habilitaTodos(frm) {
	for (i = 0; i< frm.length; i++) {
	  frm.elements[i].disabled = false;
	}
}


/******** FUNÇÕES EXCLUSIVAS DO FORMULÁRIO DE INCLUSÃO / ALTERAÇÃO DA NOTA FISCAL e FATURA *********/

/*
* Função para desabilitar os campos chaves da nota fiscal, ou seja,
* os campos que tornam a nota fiscal única. Está função é chama só na alteração do documento.
* Ex.: desabilitaCamposChave(document.nomeDoForm)
*/
function desabilitaCamposChave(frm, tipo) {
	frm.obr_cnpjFornecedor.disabled = true;
	frm.obr_numeroDocumento.disabled = true;
	frm.obr_dataEmissao.disabled = true;
}

/*
* Função para desabilitar os campos da nota fiscal no caso do perfil do usuário for colaborador.
* Este perfil só poderá alterar o status da Nota Fiscal
* Ex.: desabilitaCamposColaborador(document.nomeDoForm)
*/

function desabilitaCamposColaborador(frm) {
	var i;
	for (i = 0; i< frm.length; i++) {
		if ( !(frm.elements[i].name == 'statusWeb') ) {
		  if( !(frm.elements[i].name =='botao') ) 
			 frm.elements[i].disabled = true;
		} else {
			frm.elements[i].disabled = false;
		}
	}
}

/*
* Função para habilitar todos os campos da nota fiscal.
* É utilizada no envio do form, bem como na inclusão de uma nova nota fiscal
* Ex.: habilitaTodos(document.nomeDoForm)
*/


function habilitaTodos(frm) {
	for (i = 0; i< frm.length; i++) {
	  frm.elements[i].disabled = false;
	}
}

/*
* Função para desabilitar todos os campos da nota fiscal.
* É utilizada quando o status é cancelado, enviado ao colaborador ou concluido
* Ex.: desabilitaTodos(document.nomeDoForm)
*/


function desabilitaTodos(frm) {
	for (i = 0; i< frm.length; i++) {
	  frm.elements[i].disabled = true;
	}
}


/*
 *	Função para realização de cálculo automático do valor do ISS após entrada do valor da aliquota e
 * Base de Cálculo.
 * Ex.: onBlur="calculaValorISS(campo, indice)"
 */
 function calculaValorISSComTolerancia(frm, indice) {
   var nomeCampoAliqISS = 'aliquota_' + indice;
  	var nomeCampoBaseISS = 'baseCalculoISS_' + indice;
  	var nomeCampoValorISS = 'valorISS_' + indice; 
  	var valAliquota;
	var valBaseCalculo;
	var valorISSCalculado;
	var valorISSDigitado;
	if ((!trim(frm[nomeCampoAliqISS].value) == "") && (!trim(frm[nomeCampoBaseISS].value) == "")) {
		 valAliquota = replaceToFloat(frm[nomeCampoAliqISS].value);
		 valBaseCalculo = replaceToFloat(frm[nomeCampoBaseISS].value);
		 valorISSCalculado = parseFloat(valAliquota/100) * parseFloat(valBaseCalculo);
		 valorISSDigitado = parseFloat(replaceToFloat(frm[nomeCampoValorISS].value));
		 //verifica tolerancia
		 if ( !( ( valorISSDigitado >= (valorISSCalculado - 0.50)) && (valorISSDigitado <= (valorISSCalculado + 0.50)) ) ) {
	 	  	alert("Valor do ISS informado está incorreto!");
	 	  	frm[nomeCampoValorISS].select();
	 	  	return false;
	 	 }
	}
	return true;
 }
 
 /*função para verificar se é nota de serviço. E caracterizada por verificar se os campos
  * com final ISS estão preenchidos. Caso estejam retorna true; 
  */
  
  function verificaNFServico(frm, indice) {
     var ehNFServico = false;
  	  var campoAliq = 'aliquota_' + indice;
  	  var baseISS   = 'baseCalculoISS_' + indice;
  	  var valISS 	  = 'valorISS_' + indice;
  	
  	  if ( trim( frm[campoAliq].value ) != "" && trim( frm[campoAliq].value ) != "0,00" ) {
  	  	  ehNFServico = true;
  	  } else if ( trim( frm[baseISS].value ) != "" && trim( frm[baseISS].value ) != "0,00" ) {
  	  	  ehNFServico = true;
  	  } else if ( trim( frm[valISS].value ) != "" && trim( frm[valISS].value ) != "0,00" ) {
  		  ehNFServico = true;
  	  }
     return ehNFServico;
  }
  
  /*  função para verificar se pedido está preenchido. Caso estejam retorna true;   */
  
  function verificaPedido(frm, indice, chkPedido) {
  	
  	  var ehPedido = false;
  	  
  	  if (chkPedido == "S")
  	     ehPedido = true;
  	  else {
	  	  var campoPedido = 'numPedidoCompra_' + indice;
	  	  var campoItemPedido = 'numItemPedido_' + indice;
	  	  
	  	  if ( trim( frm[campoPedido].value ) != "" ) {
	  	     ehPedido = true;
	  	  } else if ( trim( frm[campoItemPedido].value ) != "" ) {
	  		  ehPedido = true;
	  	  } 
	  }
     return ehPedido;
  }
 
 /** obriga o preenchimento dos campos se a nota for de serviço.*/ 
 
  function validaCamposISS( frm, indice ) {
  	   
  	   var nomeCampoAliqISS = 'aliquota_' + indice;
  		var nomeCampoBaseISS = 'baseCalculoISS_' + indice;
  		var nomeValorTotalItem = 'obr_valorTotalItem_' + indice;
  		
		if ( trim( frm[nomeCampoAliqISS].value ) == "" ) {
			
	  		alert("Para Nota de Serviço, o preenchimento da aliquota é obrigatório.\n Favor verificar o preenchimento dos campos do ISS. ");
	  		frm[nomeCampoAliqISS].focus();
	  		return false;
	  		
	  	} else if ( trim( frm[nomeCampoBaseISS].value ) == "" ) {
	  		
	  		alert("Para Nota de Serviço, o preenchimento da Base de Cálculo é obrigatório.\n Favor verificar o preenchimento dos campos do ISS.");
	  		frm[nomeCampoBaseISS].focus();
	  		return false;
	  		
	  	} else if ( parseFloat( replaceToFloat( frm[nomeCampoBaseISS].value ) ) > parseFloat( replaceToFloat( frm[nomeValorTotalItem].value ) ) ) {
	  		
	  		alert("O Campo Base de Cálculo não pode ser maior do que o Valor Total com Imposto descrito no item.");
	  		frm[nomeCampoBaseISS].select();
	  		return false;
	  	}
	  	return true;
  }
 
 /** obriga o preenchimento dos campos do pedido, se for preenchido alguns dos campos do pedido.*/ 
  function validaPedido(frm, indice) {
  	
  	   var nomeCampoPedido = 'numPedidoCompra_' + indice;
  		var nomeCampoItemPedido = 'numItemPedido_' + indice;
  		
		if ( trim( frm[nomeCampoPedido].value ) == "" ) {
		   alert("Por favor, preencha o Número do Pedido.");
		   frm[nomeCampoPedido].focus();
		   return false;
	  	
		}
	  	if ( trim( frm[nomeCampoItemPedido].value ) == "" ) {
		  	alert("Por favor, preencha o Item do Pedido.");
		  	frm[nomeCampoItemPedido].focus();
		  	return false;
	  	}
	  	return true;
  }
 
  function calculaValorISSOnBlur(frm, campo, indice) {
  		var nomeCampoAliqISS = 'aliquota_' + indice;
	  	var nomeCampoBaseISS = campo + indice;
	  	if ( ( !trim( frm[nomeCampoAliqISS].value ) == "" ) && ( !trim( frm[nomeCampoBaseISS].value ) == "" ) ) {
	  		var valAliquota = replaceToFloat(frm[nomeCampoAliqISS].value);
	  		var valBaseCalculo = replaceToFloat(frm[nomeCampoBaseISS].value);
	  		var valorISS = (parseFloat(valAliquota / 100) * parseFloat(valBaseCalculo));
	  		valorISS = formataReal(valorISS);
	  		var nomeCampoValorISS = 'valorISS_' + indice; 
	  		frm[nomeCampoValorISS].value = valorISS;
	  	}
  }
  
  
 /* Função para retorno de valores sem ponto e vírgula, para calculos de numeros de ponto flutuante
  * ex.: replaceToFloat(document.form.campo.value)
  */
	function replaceToFloat(valueField) {
		valueField = valueField.replace(".","");
	   valueField = valueField.replace(".","");
	   valueField = valueField.replace(".","");            
	   valueField = valueField.replace(",",".");
	   return valueField;
	}  
  /* Função que obriga o preenchimento do CNPJ da filial antes de preencher o número do pedido. 
  *  onBlur="verificaCNPJFilial(frm, campo);"	
  */
  
  function verificaCNPJFilial(frm, campo) {
    	if ( trim( frm[campo].value ) != "" ) {
  			if ( trim( frm.obr_cnpjFilial.value ) == "" ) {
  				window.alert("Por favor, preencha o campo CNPJ Filial.");
  				frm.obr_cnpjFilial.focus();
  			}
  		}
  }

/**Função para verificar a igualdade do valor total informado no cabecalho da NF com o somatorio dos valores totais dos itens dos pedidos*/
  function verificaVTcomItens(frm, campoCabecalho, campoItem, contItens) {

	 	var valorTotalCabecalho = parseFloat(replaceToFloat(frm[campoCabecalho].value));
	 	var somaItem=0.00;
	 	var valor = 0.00;
	 	var somaDecimal = 0.00;
	 	var valorDecimal = 0.00;
		var valorInteiro = 0;
		var valorTotal = 0.00;

	 	for (var i = 1; i < contItens; i++) 
	 	{
	 		campoValItem = campoItem + i;
	 		valor = String(replaceToFloat(frm[campoValItem].value));

			if (valor.indexOf(".") != -1)
			{
			   valorInteiro = valor.substring(0,valor.indexOf("."));
			   valorDecimal = valor.substring(valor.indexOf(".")+1,valor.length);
			   valorDecimal = "0."+valorDecimal;
			}
			else
			{
				valorDecimal = 0.00;	
			}

	 		//somaItem += parseFloat( replaceToFloat( frm[campoValItem].value ) ); 

	 		valorInteiro = parseFloat(valorInteiro);
	 		valorDecimal = parseFloat(valorDecimal);

			//Valor total da parte inteira
	 		somaItem = parseFloat(somaItem + valorInteiro);

	 		//Valor total da parte decimal
	 		somaDecimal = parseFloat(somaDecimal + valorDecimal);
	 	}

		//Valor total
	 	valorTotal = parseFloat(somaItem + somaDecimal);

	 	if (valorTotalCabecalho == valorTotal)
	 		return true;
	 	else
	 		return false;
  }

  /** Função responsável por verificar se existe duas ou mais linhas de itens da fatura com o mesmo numero da nota e data de emissão.*/
  function verificaItensDuplicadosFT (frm, contItens) {
  		
  		var i, j;
  		var numeroNota, dataEmissao;
  		var ret = false;
  		
  		for (i = 1; i < contItens; i++) {
  			
  			numeroNota = frm['obr_nfReferencia_'+i].value;
  			dataEmissao = frm['obr_dataEmissao_'+i].value;
  			
  			for ( j = i+1; j < contItens; j++ ) {
  				
  				if ( ( numeroNota == frm['obr_nfReferencia_'+j].value ) &&
  					  ( dataEmissao == frm['obr_dataEmissao_'+j].value ) ) {
  					  	
  					//	alert (numeroNota +" == "+ frm['obr_nfReferencia_'+j].value+"\n"+ dataEmissao +" == "+ frm['obr_dataEmissao_'+j].value );	  	
  					  	ret = true;
  					  	break;
  				}
  					
  			}
  			if (ret == true) {
  				break;
  			}
  		}
  		
  		return ret;
  }
	
/*************** FIM DAS FUNÇÕES EXCLUSIVAS DA NOTA FISCAL *********/

	
	String.prototype.repeat = function(times) 
	{ 
    	return new Array(times+1).join(this) 
	}; 
	
	/*
	*  Formata o valor do campo para decimal (insere '.' e ',').
	*  Uso:
	*  <input type="text" maxlength="5" 
	*		onKeyPress="return formataDecimal2(this, event, 2);"
	*		onBlur="formataDecimal(this, 2);">
	*/
	function formataDecimal(obj, casas)
	{
		var valor = obj.value;
		
		var parteInteira = "0";
		var parteDecimal = "";
		
		var posicaoVirgula = valor.indexOf(',');
		
		// Se nao existe virgula, só temos valor inteiro
		if (posicaoVirgula == -1)
			parteInteira = valorPuro(valor);
		
		// Se existe virgula e ela nao esta no comeco, extraimos a parte inteira
		if (posicaoVirgula >= 1)
			parteInteira = valorPuro(valor.substr(0, posicaoVirgula));
		
		// Se existe virgula e ela nao esta no final, extraimos a parte decimal
		if (posicaoVirgula >= 0 && (posicaoVirgula + 1 < valor.length))
			parteDecimal = valorPuro(valor.substr(posicaoVirgula + 1));
	
		
		// Insere os pontos de milhar na parte inteira
		
		var result = "";
		for(var i = 1; i <= parteInteira.length; i++)
	    {
	    	var posicaoComplementar = parteInteira.length - i; // ler a string parteInteira do final para o comeco
	      	result = parteInteira.charAt(posicaoComplementar) + result;
	      	if((i % 3) == 0) result = '.' + result;
	    }
	    
	    if (result.length > 1 && result.substr(0,1) == ".")
	    	result = result.substr(1);
	    	
	    parteInteira = result;
		    
		    
		// Completa as casas da parte decimal
		
		while(parteDecimal.length < casas)
	  		parteDecimal += "0";
		
	  	obj.value = parteInteira + "," + parteDecimal;
	}

	/*
	*  Formata o valor do campo para decimal  (insere '.' e ',').
	*  Uso:
	*  <input type="text" maxlength="5" 
	*		onKeyPress="return formataDecimal2(this, event, 2);"
	*		onBlur="formataDecimal(this, 2);">
	*/
	function formataDecimal2(obj, event, casas)
	{
		if (!soNumeros(event))
			return false;
			
		if (obj.value.length >= obj.maxLength) 
			return true;
		
		casas--;
		
		var valor = obj.value;
	  	var intValue = "";
	  	var result = "";
	  	if (casas == null) casas = 2;
	  
	  	for(counter = 0; counter < valor.length; counter++)
	  	{
	    	if(isNumChar(valor.charAt(counter))) intValue += valor.charAt(counter);
	    }
	  
	  	while((intValue.length > 1) && (intValue.charAt(0) == "0"))
	  	{
	    	intValue = intValue.substring(1, intValue.length);
	    }
	    
	    if(intValue.length <= casas)
	    	result = "0," + "0".repeat(casas - intValue.length) + intValue;
	    	  	
	  	if(intValue.length > casas)
		{
		    result = ',' + intValue.substring(intValue.length - casas, intValue.length);
		    for(counter = intValue.length - (casas + 1); counter >= 0; counter--)
		    {
		      	result = intValue.charAt(counter) + result;
		      	if((counter > 0) && (((intValue.length - counter - casas) % 3) == 0))
		        	result = '.' + result;
		    }
		}
	  	
	  	obj.value = result;
		return true;
	}
	
	
	/*
	*  Formata o valor do campo para decimal  (insere ':' ).
	*  Uso:
	*  <input type="text" maxlength="8" 
	*		onKeyPress="return formataHora2(this, event);">
	*/
	function formataHora2(obj, event)
	{
		if (!soNumeros(event))
			return false;
			
		if (obj.value.length >= obj.maxLength) 
			return true;
		
		var keynum = ( navigator.appName == "Netscape" ) ? event.which : event.keyCode;
		var keychar = String.fromCharCode(keynum);
		var valor = valorPuro(obj.value) + keychar;
		
		// Insere os dois pontos para cada grupo de 2
		
		var result = "";
		for(var i = 1; i <= valor.length; i++)
	    {
	    	var posicaoComplementar = valor.length - i; // ler a string valor do final para o comeco
	      	result = valor.charAt(posicaoComplementar) + result;
	      	if((i % 2) == 0) result = ':' + result;
	    }
	    
	    if (result.length > 1 && result.substr(0,1) == ":")
	    	result = result.substr(1);

	  	obj.value = result;
		return false;
	}