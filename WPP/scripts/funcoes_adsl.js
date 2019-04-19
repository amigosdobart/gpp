function Trim(sEntrada) {	
	sString = new String(sEntrada)
	for (var iCount2 = 0; iCount2 < sString.length; iCount2++)
		if ( sString.charAt(iCount2) != " ")
			break;
	for ( var iCount = sString.length - 1;  iCount > 0 ; iCount-- )
		if ( sString.charAt(iCount) != " ")
			break;
	if ( iCount2 > iCount ) 
		return ""
	return sString.substring(iCount2,iCount+1)
}

function isNull(objeto) {
  if (Trim(objeto) != "") {
  	return true;
  } else {
  	return false;
  }
}

function isEmail(objeto) {
  
  var difnulo = isNull(objeto)
  
  if (difnulo) {
      var sInput = objeto

        if ( sInput.indexOf("@") > -1 && sInput.indexOf(".") > -1) {
            return true
        } else {
          return false
        }

  } else {
    return difnulo;
  }
}

function checkCPFCNPJ(cnpfj,naoVerificaCpfCgc) {
	if (naoVerificaCpfCgc) {
		return true;
	} else {
		if (cnpfj.value.length > 11 ) {
			if ( validaCNPJ(cnpfj) ) {
				return true;
			}
		}
		else {
			if ( validaCPF(cnpfj) ) {
				return true;
			}
		}
		return false;
	}
}

function validaCPF(oInput){

var s = oInput.value

var i;
var c = s.substr(0,9);
var dv = s.substr(9,2);
var d1 = 0;
	for (i = 0; i < 9; i++){
    		d1 += c.charAt(i)*(10-i);
   	}
	if (d1 == 0)
	{
	  alert("CPF Invalido");
	  oInput.focus();
	  return false;
	}
    d1 = 11 - (d1 % 11);
    if (d1 > 9) d1 = 0;
    if (dv.charAt(0) != d1){
	  alert("CPF Invalido");
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
		alert("CPF Invalido");
	  oInput.focus();
	  return false;
    }
    return true;
}

function validaCNPJ(oInput){
	
	var CNPJ = oInput.value
	
	if (CNPJ.length<14)
	{
	  alert("CNPJ Invalido");
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
		alert("CNPJ Invalido")
		oInput.focus()
		return false;
	}
	else
	{
	   return true
	}	
}

function validaFormLogin(frmDados, tipoAdsl) {
	
	if (frmDados.txtDdd.value == "") {
		alert("Selecione um Ddd.");
		frmDados.txtDdd.focus();
		return;	
	}
	
	if (frmDados.txtTelefone.value == "") {
		alert("Preencha o campo Telefone.");
		frmDados.txtTelefone.focus();
		return;	
	}
	
	if (tipoAdsl == "") {
		if (frmDados.tipo_adsl[frmDados.tipo_adsl.selectedIndex].value == "") {
			alert("Selecione um tipo de ADSL.");
			frmDados.tipo_adsl.focus();
			return;
		}	
	}
	
	frmDados.submit();
	return;
}

function validaFormLoginUsuario(frmDados) {

	if (frmDados.txtUserName.value == "") {
		alert("Preencha o campo Login.");
		frmDados.txtUserName.focus();
		return;	
	}	
	
	if (frmDados.txtPassword.value == "") {
		alert("Preencha o campo Senha.");
		frmDados.txtPassword.focus();
		return;	
	}
	
	frmDados.submit();
	return;

}

function enviarSolicitacao(frmDados, naoValidaCnpfj, modemsDisponiveis, temModem) {

	if (!frmDados.chkContrato.checked) {
		alert("Para prosseguir é necessário aceitar o Contrato e Termos de Compromisso");
		frmDados.chkContrato.focus();
		return;
	}
	
	if (frmDados.txtNome.value == "") {
		alert("Informe o Nome do Solicitante");
		frmDados.txtNome.focus();
		return;	
	}

	if (frmDados.txtRg.value == "") {
		alert("Informe o Rg do Solicitante");
		frmDados.txtRg.focus();
		return;	
	}

	if (frmDados.txtCnpfj.value == "") {
		alert("Informe o CPF/CNPJ do Solicitante");
		frmDados.txtCnpfj.focus();
		return;	
	} else {
		if (!checkCPFCNPJ(frmDados.txtCnpfj, naoValidaCnpfj)) {
			return;
		}
	}

	if (frmDados.txtEmail.value == "") {
		alert("Informe o E-mail do Solicitante");
		frmDados.txtEmail.focus();
		return;	
	} else {
		if (!isEmail(frmDados.txtEmail.value)) {
			alert("E-mail inválido! Informe um E-mail válido!");
			frmDados.txtEmail.focus();
			return;
		}
	}
	
	if (temModem) {		
		if (modemsDisponiveis != "") {
			if (!vRadio(frmDados.rdoModem, "Selecione o tipo de Modem")) {
				return;
			}
			if (!checkAlugadoComprado(frmDados)) {
				return;
			}
		} else {
			vSohModemCliente(frmDados.rdoModem, "Selecione o Modem");
			if (!frmDados.sltModemProprio.selectedIndex > 0)  {
				alert("Selecione o tipo de Modem que deseja");	
				frmDados.sltModemProprio.focus();
				return false;	
			}			
		}		
	} else {
		vSohModemCliente(frmDados.rdoModem, "Selecione o Modem");
		if (!frmDados.sltModemProprio.selectedIndex > 0)  {
			alert("Selecione o tipo de Modem que deseja");	
			frmDados.sltModemProprio.focus();
			return false;	
		}		
	}

	if (total == 0) {
		if ( (frmDados.sltModemProprio.value == "MODNEC") || (frmDados.sltModemProprio.value == "MODSUM") ) {
		alert("Selecione a forma de pagamento do Modem");
		return;	
		}
	}

	if (!frmDados.sltProvedor.selectedIndex > 0) {
		alert("Selecione um provedor");
		frmDados.sltProvedor.focus();
		return;	
	}

	if (!frmDados.sltVelocidade.selectedIndex > 0) {
		alert("Selecione a velocidade");
		frmDados.sltVelocidade.focus();
		return;	
	}

	if (!frmDados.sltMidia.selectedIndex > 0) {
		alert("Selecione como ficou sabendo do Internet Turbo ADSL");
		frmDados.sltMidia.focus();
		return;	
	}

	frmDados.submit();
	return;
}

function vRadio(oEntrada, sCaption) {		
	if ( typeof(oEntrada[0]) == "undefined" ) {
	  if ( oEntrada.checked )
	    return true
	  else
	    return false
	} else {
  		for ( var iCount = 0 ;  ; iCount ++ ) {
  	  		if ( typeof( oEntrada[iCount] ) == "undefined" )
  	    			break;
  	  		if ( oEntrada[iCount].checked )
  	    			return true
  		}
  		oEntrada[0].focus()
    		if ( typeof(sCaption) == "undefined" )
    			alert(sCaption)
    		else
      			alert(sCaption)
  		return false
  	}
}

function vSohModemCliente(radioModemCliente, mensagem) {
	if (!radioModemCliente.checked) {
		alert(mensagem);		
		return false;
	}
	return true;
}

function checkVelo() {
	if (document.frmSolicitacao.sltVelocidade.value == "5251" ){
	    document.all.velo1.style.display = "block";
		document.all.velo2.style.display = "none";
		document.all.velo3.style.display = "none";
	}
	if (document.frmSolicitacao.sltVelocidade.value == "5351" ){
	    document.all.velo2.style.display = "block";
		document.all.velo1.style.display = "none";
		document.all.velo3.style.display = "none";
	}
	if (document.frmSolicitacao.sltVelocidade.value == "5150" ){
	    document.all.velo3.style.display = "block";
		document.all.velo2.style.display = "none";
		document.all.velo1.style.display = "none";
	}
	if (document.frmSolicitacao.sltVelocidade.value == "" ){
	    document.all.velo3.style.display = "none";
		document.all.velo2.style.display = "none";
		document.all.velo1.style.display = "none";
	}
}


function checkAlugadoComprado(frmDados) {
	// Verifica se alugado, entao prepara para seleção de modems
	if (frmDados.rdoModem[0].checked) {
		if (!frmDados.sltModem.selectedIndex > 0)  {
			alert("Selecione o tipo de Modem que deseja");	
			frmDados.sltModem.focus();
			return false;	
		}
	}
	
	// Verifica se comprado
	if (frmDados.rdoModem[1].checked) {
		if (!frmDados.sltModemProprio.selectedIndex > 0)  {
			alert("Selecione o tipo de Modem que deseja");	
			frmDados.sltModemProprio.focus();
			return false;	
		}
	}

	if (frmDados.rdoModem[0].checked && frmDados.chkDesconto.checked) {
		alert("O desconto de fidelização só é valido para compra de modem.");
		frmDados.chkDesconto.focus();
		return false;	
	}
	
	
	
	return true;
}

function enviarQuestionario(frmDados) {
	sName = null;
	for (i = 0; i < frmDados.elements.length; i++) {
		if (frmDados.elements[i].type == "radio") {
			if (sName != "frmDados.elements[i].name") {
				sName = frmDados.elements[i].name;
				if (!vRadio(eval("frmDados."+sName), "Favor responder todas as perguntas do formulário")) {
					return;
				}
			}
		}
	}	
	
	// Submit
	frmDados.submit();
	
}

function enviarAgendamento(frmDados) {
	
	if (!vRadio(frmDados.txtData_agendamento, "Favor selecionar uma data.")) {
		return;
	}

	frmDados.submit();
	return;	
	
}

function validaFormVendedor(frmDados) {

	if (frmDados.txtCodEmpresa.value == '') {
		alert('Favor preencher o código da empresa.');
		frmDados.txtCodEmpresa.focus();
		return;
	}
	
	if (frmDados.txtCodVendedor.value == '') {
		alert('Favor preencher o código do vendedor.');
		frmDados.txtCodVendedor.focus();
		return;		
	}
	
	frmDados.submit();
	return;
	
}

function validaFormVendedor2(frmDados) {
	frmDados.submit();
	return;
	
}

function editarAdsl(frmDados, acao) {
	
	frmDados.action = acao;
	frmDados.submit();
	return;
}

function confirmaSolicitacaoAdsl(frmDados, acao) {
	
	frmDados.action = acao;
	frmDados.submit();
	return;
}

function esqueceuSenha(frmDados) {

	if (frmDados.txtLogin.value == "") {
		alert("Favor preencher o campo Login.");
		frmDados.txtLogin.focus();
		return;	
	}
	
	frmDados.submit();
	return;
}

function adslSenhaRetornar(frmDados) {	
	frmDados.action = frmDados.stSuccessURL.value;	
	frmDados.submit();
	return;
}

