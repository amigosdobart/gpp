
/*
*  Valida Email
*  Utilizar da seguinte forma: validaEmail(document.forms[#].elements[#])
*/
function validaEmail(email)
{
	if (email.value.indexOf('@') == -1)
	{
		alert('Email n�o est� no formato adequado.');
		email.focus();
		return false;
	}
	else return true;
}


/**
*	Fun��o que retira os espa�os em branco a direita e a esquerda, retornando uma nova string sem espa�os em branco.
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
*  Funcao para fazer formatacao de per�odo : mm/aaaa
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
*  Funcao para validar o per�odo, verificando mes e ano.
*  Utilizar da seguiinte forma: validaPeriodo(campo_date)
*/

function validarPeriodo(Wparam) {
	if ( trim( Wparam.value ) != "" ) {
		barra = Wparam.value.indexOf("/");
		mes = Wparam.value.substring(0,barra);
		ano = Wparam.value.substring(barra+1);
		data = mes + ano;
		if ( data.length < 6 ) {
			alert("O formato correto do per�odo � mm/aaaa");
			Wparam.focus();
			return false;
		}
		if ( mes > 12 || mes < 1 ){
			alert("M�s digitado � inv�lido");
			Wparam.focus();
			return false;
		}
   }
   return true;
}

/*
*  Funcao para fazer formatacao de datas : dd/mm/aaaa
*  Utilizar da seguinte forma: formata_data(this,event)
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
*  Funcao para fazer formatacao de datas : dd/mm/aaaa
*  Utilizar da seguinte forma: formata_data(this,event)
*/
function formataMsisdn( campo, e ) {
	car = (navigator.appName == "Netscape" ) ? e.which : e.keyCode;
	
	if ( ( car < 48 || car > 57 ) && ( car > 31 ) ) return false;
	if ( campo.value.length==1 )
	{
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

//Converte o c�digo ASCII para caracteres. V�lido para n�meros
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
*  Fun��o que formata o valor de entrada para moeda
*  Para utiliza��o em:
*    - frmFiltroAjusteCredito.vm
*    - frmFiltroAjusteDebito.vm
*    - frmFiltroAdministrativoTipoExtrato.vm
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

function formataMoeda2(moeda)
{
  intValue = "";
  result = "";
  
  for(counter = 0; counter < moeda.length; counter++)
    if(isNumChar(moeda.charAt(counter)))
      intValue += moeda.charAt(counter);
  
  while((intValue.length > 0) && (intValue.charAt(0) == "0"))
    intValue = intValue.substring(1, intValue.length);
  
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
			alert("O formato correto da data � dd/mm/aaaa");
			Wparam.focus();
			return false;
		}
		if (dia > 31 || dia < 1){
			alert("Dia digitado � inv�lido");
			Wparam.focus();
			return false;
		}
		if (mes > 12 || mes < 1){
			alert("M�s digitado � inv�lido");
			Wparam.focus();
			return false;
		}
		if (mes == 4 || mes == 6 || mes == 9 || mes == 11){
			if (dia > 30){
				alert("O m�s "+mes+" possui 30 dias");
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
					  t = "ter�";
					}
					else {
					  if(mesh > 2) {
					    t = "teve";
					  }
					  else {
					    if(mesh < 2) {
					      t = "ter�";
					    }
					    else {
					      t = "tem";
					    }
                 }
		         }
	         }
            alert("Fevereiro do ano "+ano+" n�o "+t+" "+dia+" dias");
            Wparam.focus();
            return false;
        }
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
			window.alert("CNPJ Inv�lido");
			campo.focus();
			return (false);
		}
		if (dig2 != Num.substr(13, 1)){
			window.alert("CNPJ Inv�lido");
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

function validacpf (campo) {
  var valor = campo.value;
  var result = "";
  var OK = false;
  var temp = limpa(valor);
  if (temp.length > 10) {
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
  else
  {
  	alert ('O CPF est� incorreto. Favor corrigir!');
  }
}

/*
* Fun��o que verifica se um valor � num�rico.
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
* Fun��o que formata Moeda.
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
* Fun��o que formata Moeda.
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
* Fun��o que formata Quantidade da NotaFiscal.
* Utilizar da seguinte forma: onBlur="formataCampoQtdeOnBlur(this);"
*/
function formataCampoQtdeOnBlur(campo)
{
	var tam = campo.value.length;

	if (tam <= 3)
	{
		if(!isNumber(campo.value))	{
			alert("Favor preencher campo apenas com n�meros.");
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
* Fun��o que formata Moeda.
* Utilizar da seguinte forma: onBlur="formataCampoMoedaOnBlur(this);"
*/
function formataCampoMoedaOnBlur(valor)
{
	var tam = valor.value.length;


	if (tam <= 2)
	{
		if(!isNumber(valor.value))
		{
			alert("Favor preencher campo apenas com n�meros.");
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
*  Func�o que verifica o preenchimento dos campos obrigatorios.
*  Parte do pressuposto que os campos obrigatorios ter�o nome iniciado com _obr.
*  Utilizar da seguinte forma: (document.forms[#])
*/
function valida(frm)
{
    for (i=0;i<frm.length;i++)
    {
    	if ((trim(frm.elements[i].value) == '') && (frm.elements[i].disabled == false))
        {
            alert('Por favor, preencha todos os campos.');
            frm.elements[i].focus();
            return false;
        }
    }
    return true;
}

/********************** FUN��ES DE VALIDA��O DE CAMPOS DE ENTRADA ********************/

/*
*  Fun��o que verifica se o Canal de Recarga foi selecionado
*  Para utiliza��o em:
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
*  Fun��o que verifica se o Motivo de Ajuste foi selecionado
*  Para utiliza��o em:
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
*  Fun��o que verifica se a Origem da Contesta��o foi selecionada
*  Para utiliza��o em:
*    - filtroConsultaContestacao.vm
*/
function isSelectedOrigem(origem)
{
  if(origem.value == 0)
  {
    alert('Favor selecionar a Origem da Contesta��o.');
    return false;
  }
  return true;
}

/*
*  Fun��o que verifica se a Origem da Recarga foi selecionado
*  Para utiliza��o em:
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
*  Fun��o que verifica se o per�odo foi selecionado
*  Para utiliza��o em:
*    - filtroConsultaComprovanteServico.vm
*    - filtroConsultaHistoricoExtrato.vm
*/
function isSelectedPeriodo(periodo)
{
  if(periodo.value == 0)
  {
    alert('Favor selecionar o Per�odo de Pesquisa.');
    return false;
  }
  return true;
}

/*
*  Fun��o que verifica se o Tipo de Comprovante foi selecionado
*  Para utiliza��o em:
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
*  Fun��o que valida o n�mero do Boletim de Sindic�ncia
*  Para utiliza��o em:
*    - filtroConsultaContestacao.vm
*/
function validaBS(bs)
{
  if(bs == "")
  {
    alert('Favor preencher o n�mero do Boletim de Sindic�ncia.');
    return false;
  }
  return true;
}

/*
*  Fun��o que valida a data final quando o tipo de pesquisa selecinado for por datas
*  Para utiliza��o em:
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
    alert('A Data Final de Pesquisa deve ser menor ou atual � Data Atual.');
  return dfValida;
}

/*
*  Fun��o que valida a data inicial quando o tipo de pesquisa selecinado for por datas
*  Para utiliza��o em:
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
    alert('A Data Inicial de Pesquisa deve ser menor ou atual � Data Atual.');
  return diValida;
}

/*
*  Fun��o que valida se a data final � posterior � data inicial
*  quando o tipo de pesquisa selecinado for por datas
*  Para utiliza��o em:
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
*  Fun��o que valida se o campo Dias de Expira��o foi preenchido
*  Para utiliza��o em:
*    - editAdministrativoAjuste.vm
*/
function validaDiasExpiracao(diasExpiracao)
{
  if(diasExpiracao.length == 0)
  {
    alert('Favor determinar os Dias de Expira��o dos cr�ditos do Ajuste.');
    return false;
  }
  return true;
}

/*
*  Fun��o que valida se o Motivo de Ajuste foi preenchido
*  Para utiliza��o em:
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
*  Fun��o que valida se o campo de MSISDN foi preenchido corretamente (10 n�meros)
*  Para utiliza��o em: 
*    - filtroAjusteCredito.vm
*    - filtroAjusteDebito.vm
*    - filtroConsultaComprovanteServico.vm
*    - filtroConsultaHistoricoExtrato.vm
*/
function validaMsisdn(msisdn)
{
  intMsisdn = "";
  for(counter = 0; counter < msisdn.length; counter++)
    if(isNumChar(msisdn.charAt(counter)))
      intMsisdn += msisdn.charAt(counter);
      
  if(intMsisdn.length < 10)
  {
    alert('O N�mero de Acesso est� incompleto. Favor Corrigir! (DDD + N�mero)');
    return false;
  }
  return true;
}

/*
*  Fun��o que valida se o Tipo de Extrato foi preenchido
*  Para utiliza��o em:
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
*  Fun��o que valida se o Valor de Ajuste foi preenchido
*  Para utiliza��o em:
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
*  Fun��o que valida se o Valor de Extrato foi preenchido
*  Para utiliza��o em:
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
*  Fun��o que valida se o campo de n�mero de voucher foi preenchido com os primeiros
*  8 n�meros
*  Para utiliza��o em: 
*    - consultaCartoesPrePagos.vm 
*/
function validaVoucher8Digitos(numVoucher)
{
  if(numVoucher.length < 8)
  {
    alert('Favor preencher com os 8 primeiros d�gitos do Cart�o Pr� Pago');
    return false;
  }
  return true;
}

/*************************************************************************************/

/*
*  Func�o que verifica o preenchimento dos campos obrigatorios.
*  @param frm formul�rio, qtdeObr quantidade de filtros obrigat�rios.
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
*	Fun��es usadas no formul�rio de usu�rio. Tem a fun��o de transferir dados de uma combo para outra.
*  Utiliza��o :   onclick="sel();" e onclick="rem();"
*  A fun��o onSubmit="posta()" serve para colocar os itens escolhidos separados por virgula para serem enviados.
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
*   Fun��o que verifica o intervalo das datas.
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
		alert("A data de inicio deve ser anterior � data fim.");
		dt_inicio.focus();
		return false;
	}

	if ((ano_inicio == ano_final) && (mes_inicio > mes_final))
	{
		alert("A data de inicio deve ser anterior � data fim.");
		dt_inicio.focus();
		return false;
	}

	if ((ano_inicio == ano_final) && (mes_inicio == mes_final) && (dia_inicio > dia_final))
	{
		alert("A data de inicio deve ser anterior � data fim.");
		dt_inicio.focus();
		return false;
	}

	return true;

}


/*
* Fun��o para desabilitar os campos quando o perfil do usu�rio for HelpDesk.
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


/******** FUN��ES EXCLUSIVAS DO FORMUL�RIO DE INCLUS�O / ALTERA��O DA NOTA FISCAL e FATURA *********/

/*
* Fun��o para desabilitar os campos chaves da nota fiscal, ou seja,
* os campos que tornam a nota fiscal �nica. Est� fun��o � chama s� na altera��o do documento.
* Ex.: desabilitaCamposChave(document.nomeDoForm)
*/
function desabilitaCamposChave(frm, tipo) {
	frm.obr_cnpjFornecedor.disabled = true;
	frm.obr_numeroDocumento.disabled = true;
	frm.obr_dataEmissao.disabled = true;
}

/*
* Fun��o para desabilitar os campos da nota fiscal no caso do perfil do usu�rio for colaborador.
* Este perfil s� poder� alterar o status da Nota Fiscal
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
* Fun��o para habilitar todos os campos da nota fiscal.
* � utilizada no envio do form, bem como na inclus�o de uma nova nota fiscal
* Ex.: habilitaTodos(document.nomeDoForm)
*/


function habilitaTodos(frm) {
	for (i = 0; i< frm.length; i++) {
	  frm.elements[i].disabled = false;
	}
}

/*
* Fun��o para desabilitar todos os campos da nota fiscal.
* � utilizada quando o status � cancelado, enviado ao colaborador ou concluido
* Ex.: desabilitaTodos(document.nomeDoForm)
*/


function desabilitaTodos(frm) {
	for (i = 0; i< frm.length; i++) {
	  frm.elements[i].disabled = true;
	}
}


/*
 *	Fun��o para realiza��o de c�lculo autom�tico do valor do ISS ap�s entrada do valor da aliquota e
 * Base de C�lculo.
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
	 	  	alert("Valor do ISS informado est� incorreto!");
	 	  	frm[nomeCampoValorISS].select();
	 	  	return false;
	 	 }
	}
	return true;
 }
 
 /*fun��o para verificar se � nota de servi�o. E caracterizada por verificar se os campos
  * com final ISS est�o preenchidos. Caso estejam retorna true; 
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
  
  /*  fun��o para verificar se pedido est� preenchido. Caso estejam retorna true;   */
  
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
 
 /** obriga o preenchimento dos campos se a nota for de servi�o.*/ 
 
  function validaCamposISS( frm, indice ) {
  	   
  	   var nomeCampoAliqISS = 'aliquota_' + indice;
  		var nomeCampoBaseISS = 'baseCalculoISS_' + indice;
  		var nomeValorTotalItem = 'obr_valorTotalItem_' + indice;
  		
		if ( trim( frm[nomeCampoAliqISS].value ) == "" ) {
			
	  		alert("Para Nota de Servi�o, o preenchimento da aliquota � obrigat�rio.\n Favor verificar o preenchimento dos campos do ISS. ");
	  		frm[nomeCampoAliqISS].focus();
	  		return false;
	  		
	  	} else if ( trim( frm[nomeCampoBaseISS].value ) == "" ) {
	  		
	  		alert("Para Nota de Servi�o, o preenchimento da Base de C�lculo � obrigat�rio.\n Favor verificar o preenchimento dos campos do ISS.");
	  		frm[nomeCampoBaseISS].focus();
	  		return false;
	  		
	  	} else if ( parseFloat( replaceToFloat( frm[nomeCampoBaseISS].value ) ) > parseFloat( replaceToFloat( frm[nomeValorTotalItem].value ) ) ) {
	  		
	  		alert("O Campo Base de C�lculo n�o pode ser maior do que o Valor Total com Imposto descrito no item.");
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
		   alert("Por favor, preencha o N�mero do Pedido.");
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
  
  
 /* Fun��o para retorno de valores sem ponto e v�rgula, para calculos de numeros de ponto flutuante
  * ex.: replaceToFloat(document.form.campo.value)
  */
	function replaceToFloat(valueField) {
		valueField = valueField.replace(".","");
	   valueField = valueField.replace(".","");
	   valueField = valueField.replace(".","");            
	   valueField = valueField.replace(",",".");
	   return valueField;
	}  
  /* Fun��o que obriga o preenchimento do CNPJ da filial antes de preencher o n�mero do pedido. 
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

/**Fun��o para verificar a igualdade do valor total informado no cabecalho da NF com o somatorio dos valores totais dos itens dos pedidos*/
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

  /** Fun��o respons�vel por verificar se existe duas ou mais linhas de itens da fatura com o mesmo numero da nota e data de emiss�o.*/
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
	
/*************** FIM DAS FUN��ES EXCLUSIVAS DA NOTA FISCAL *********/