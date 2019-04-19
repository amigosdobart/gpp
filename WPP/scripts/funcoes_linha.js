function enviarForm1(frmDados) {
	if (frmDados.sltEstado.value == '') {
		alert("Favor selecionar um estado.");
		frmDados.sltEstado.focus();
		return;
	}
	if (frmDados.txtCnpfj.value == '') {
		alert("Favor preencher o campo CPF/CNPJ");
		frmDados.txtCnpfj.focus();
		return;	
	}
	if (!checkCPFCNPJ(frmDados.txtCnpfj)) {		
		frmDados.txtCnpfj.focus();
		return;			
	}	
	frmDados.submit();
	return;
}

function enviarForm2(frmDados, codStatus, tipoCadastro) {
	
	// Numero somente para codStatus = 7
	if ((codStatus != 7)
			&& (tipoCadastro != "cnpj")) {
		if (frmDados.txtNumFormulario.value == '') {
			alert("O número do formulário precisa ser preenchido.");
			frmDados.txtNumFormulario.focus();
			return;	
		}	
		if (!(frmDados.txtNumFormulario.value.length == 7)) {
			alert("O número do formulário precisa possuir 7 dígitos.");
			frmDados.txtNumFormulario.focus();
			return;			
		}
	}
	
	if (frmDados.txtNomeCompleto.value == '') {
		alert("Favor preencher o Nome completo.");
		frmDados.txtNomeCompleto.focus();
		return;
	}	
	if (frmDados.txtCnpfj.value == '') {
		alert("Favor preencher o campo CPF/CNPJ.");
		frmDados.txtCnpfj.focus();
		return;	
	}
	if (!checkCPFCNPJ(frmDados.txtCnpfj)) {		
		frmDados.txtCnpfj.focus();
		return;			
	}	
	if (frmDados.sltTitularCnpfj[frmDados.sltTitularCnpfj.selectedIndex].value == '') {
		alert("Selecione se é titular do CPF/CNPJ.");
		frmDados.sltTitularCnpfj.focus();
		return;	
	}
	if (frmDados.sltDocumento[frmDados.sltDocumento.selectedIndex].value == '') {
		alert("Favor selecionar um documento.");
		frmDados.sltDocumento.focus();
		return;	
	}
	if (frmDados.txtNumeroDocumento.value == '') {
		alert("Favor preencher o número do documento.");
		frmDados.txtNumeroDocumento.focus();
		return;			
	}
	if (frmDados.sltSexo[frmDados.sltSexo.selectedIndex].value == '') {
		alert("Selecione o sexo.");	
		frmDados.sltSexo.focus();
		return;
	}
	if (frmDados.txtNascimento.value == '' || frmDados.txtNascimento.value == 'ddmmaaaa') {
		alert("Favor preencher o campo Nascimento.");
		frmDados.txtNascimento.focus();
		return;	
	}
	if (frmDados.txtNacionalidade.value == '') {
		alert("Favor preencher o campo Nacionalidade.");
		frmDados.txtNacionalidade.focus();
		return;	
	}
	if (frmDados.sltEstadoCivil[frmDados.sltEstadoCivil.selectedIndex].value == '') {
		alert("Favor selecionar o estado civil.");
		frmDados.sltEstadoCivil.focus();
		return;
	}
	if (frmDados.sltProfissao[frmDados.sltProfissao.selectedIndex].value == '') {
		alert("Selecione a profissão.");
		frmDados.sltProfissao.focus();
		return;	
	}	
	if (frmDados.txtDddContato.value == '') {
		alert("Favor preencher o ddd para contato.");
		frmDados.txtDddContato.focus();
		return;	
	}
	if (frmDados.txtTelefoneContato.value == '') {
		alert("Favor preencher o telefone para contato.");
		frmDados.txtTelefoneContato.focus();
		return;	
	}
	if (frmDados.txtEmailContato.value == '') {
		alert("Favor preencher o campo e-mail.");
		frmDados.txtEmailContato.focus();
		return;	
	}
	if (!validaEMail(frmDados.txtEmailContato)) {
		return;
	}	
	
	
	frmDados.submit();
	return;	
}

function enviarForm2PJ(frmDados, codStatus, tipoCadastro) {
	
	// Numero somente para codStatus = 7
	if ((codStatus != 7)
			&& (tipoCadastro != "cnpj")) {
		if (frmDados.txtNumFormulario.value == '') {
			alert("O número do formulário precisa ser preenchido.");
			frmDados.txtNumFormulario.focus();
			return;	
		}
		if (!(frmDados.txtNumFormulario.value.length == 7)) {
			alert("O número do formulário precisa possuir 7 dígitos.");
			frmDados.txtNumFormulario.focus();
			return;			
		}
	}
	
	if (frmDados.txtRazaoSocial.value == '') {
		alert("Favor preencher o campo Razão Social.");
		frmDados.txtRazaoSocial.focus();
		return;
	}
	
	if (frmDados.txtCnpfj.value == '') {
		alert("Favor preencher o campo CPF/CNPJ.");
		frmDados.txtCnpfj.focus();
		return;	
	}
	if (!checkCPFCNPJ(frmDados.txtCnpfj)) {		
		frmDados.txtCnpfj.focus();
		return;			
	}	
	if (frmDados.txtNumeroInscricao.value == '') {
		alert("Favor preencher o campo número de inscrição.");
		frmDados.txtNumeroInscricao.focus();
		return;	
	}	
	if (frmDados.txtRamoAtividade.value == '') {
		alert("Favor preencher o Ramo de Atividade.");
		frmDados.txtRamoAtividade.focus();
		return;
	}
	if (frmDados.txtNomeCompleto.value == '') {
		alert("Favor preencher o Nome completo.");
		frmDados.txtNomeCompleto.focus();
		return;
	}
	if (frmDados.sltDocumento[frmDados.sltDocumento.selectedIndex].value == '') {
		alert("Favor selecionar um documento.");
		frmDados.sltDocumento.focus();
		return;
	}
	if (frmDados.txtNumeroDocumento.value == '') {
		alert("Favor preencher o campo número do documento.");
		frmDados.txtNumeroDocumento.focus();
		return;
	}
	if (frmDados.txtDddContato.value == '') {
		alert("Favor preencher o ddd para contato.");
		frmDados.txtDddContato.focus();
		return;
	}
	if (frmDados.txtTelefoneContato.value == '') {
		alert("Favor preencher o telefone para contato.");
		frmDados.txtTelefoneContato.focus();
		return;	
	}
	if (frmDados.txtEmailContato.value == '') {
		alert("Favor preencher o campo e-mail.");
		frmDados.txtEmailContato.focus();
		return;	
	}
	if (!validaEMail(frmDados.txtEmailContato)) {
		return;
	}	
	
	frmDados.submit();
	return;	
}

function enviarForm3(frmDados) {

	if (frmDados.txtEndInstalacaoEndereco.value == "") {
		alert("Favor preencher o campo endereço.");
		frmDados.txtEndInstalacaoEndereco.focus();
		return;	
	}
	if (frmDados.txtEndInstalacaoBairro.value == "") {
		alert("Favor preencher o campo bairro.");
		frmDados.txtEndInstalacaoBairro.focus();
		return;	
	}
	if (frmDados.txtEndInstalacaoCidade.value == "") {
		alert("Favor preencher o campo cidade.");
		frmDados.txtEndInstalacaoCidade.focus();
		return;	
	}
	if (frmDados.sltEndInstalacaoEstado[frmDados.sltEndInstalacaoEstado.selectedIndex].value == "") {
		alert("Favor selecionar um estado.");
		frmDados.sltEndInstalacaoEstado.focus();
		return;	
	}
	
	frmDados.submit();
	return;	
	
}

function enviarForm4(frmDados) {
	frmDados.submit();
	return;		
}

function enviarForm5(frmDados) {
	
	if (frmDados.sltUsoDoTelefone[frmDados.sltUsoDoTelefone.selectedIndex].value == "") {
		alert("Selecione o uso do telefone.");
		frmDados.sltUsoDoTelefone.focus();
		return;	
	}
	
	frmDados.submit();
	return;		
}

function enviarFormFinaliza(frmDados,tipoSolic,novaLinha) {
	
	if ((novaLinha)
			&& (tipoSolic == "cpf")) {
		if (frmDados.hdnEnviouArquivos.value != "S") {
			alert("Você precisa enviar os documentos antes da confirmação.");
			return;
		}
	}

	frmDados.submit();
	return;
	
}

function cancelaSolicitacao(frmDados) {

	frmDados.action = frmDados.stSessErrorURL.value;
	frmDados.submit();
	return;
	
}

function printPopup(frmDados, tipoCadastro){

	window.open("","wPrint","width=20,height=20,top=2000");
	// apenas para testes
	//window.open("","wPrint","width=600,height=600");
	frmDados.target = "wPrint" ;
	bkpAction = frmDados.action;
	bkpSuccess = frmDados.stSuccessURL.value;	
	frmDados.action = "imprimesolicitalinha";
	if (tipoCadastro == "cpf") {
		frmDados.stSuccessURL.value = "../../adsl_form/linha.form.imprime.cpf.jsp";
	} else {
		frmDados.stSuccessURL.value = "../../adsl_form/linha.form.imprime.cnpj.jsp";
	}	
	frmDados.submit();
	frmDados.action = bkpAction;
	frmDados.stSuccessURL.value = bkpSuccess;
	frmDados.target = "";	
	return;
}

function checkCPFCNPJ(cnpfj) {
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

function validaEMail(field) {
	var email, Valid, strDomain, strUser, i, intCount;
	var arrAux;
	email=field.value;
	if (email=="") {
		return true;
	} else {
		Valid=false;
		if (email.charAt(email.length-1)==".") {
			email=email.substr(0, email.length-1);
			field.value=email;
		}
		if (email.indexOf("@")>0) {
			strDomain=email.substr(email.indexOf("@")+1, email.length-(email.indexOf("@")+1));
			strUser=email.substr(0, email.indexOf("@"));
			arrAux=stringSplit(strUser, ".");
			intCount=-1;
			for (i=0; i<=arrAux.length-1; i++) {
				if (arrAux[i]>"" && arrAux[i].indexOf("@")==-1) {
					intCount++;
				}
			}
			if (intCount==arrAux.length-1) Valid=true;
			if (Valid) {
				Valid=false;
				arrAux=stringSplit(strDomain, ".");
				intCount=-1;
				for (i=0; i<=arrAux.length-1; i++) {
					if (arrAux[i]>"" && stringIsAlpha(arrAux[i].charAt(0)) && arrAux[i].indexOf("@")==-1) {
						intCount++;
					}
				}
				if (intCount==arrAux.length-1) Valid=true;
			}
		}
		if (!Valid) {
			field.focus();
			alert("Este e-Mail é inválido");
			return false;
		} else {
			return true;
		}
	}
}

function stringSplit(strString, charSep) {
	var arrAux=new Array(), i, j;
	if (charSep.length==1) {
		j=0;
		arrAux[j]="";
		for (i=0; i<strString.length; i++) {
			if (strString.charAt(i)!=charSep) {
				arrAux[j]=arrAux[j]+strString.charAt(i);
			} else {
				j++;
				arrAux[j]="";
			}
		}
		return arrAux;
	} else return NaN;
}

function stringIsAlpha(strChar) {
	var i, blnValid;
	blnValid=false;
	for (i=0; i<strChar.length; i++) {
		if ((strChar.charAt(i)>="a" && strChar.charAt(i)<="z") || (strChar.charAt(i)>="A" && strChar.charAt(i)<="Z")) blnValid=true;
	}
	return blnValid;
}

function carregaProfissao(frmDados, profissao) {
	for(i=0; i< frmDados.sltProfissao.length;i++){
		if (frmDados.sltProfissao.options[i].value == profissao){
			frmDados.sltProfissao.options[i].selected = true;
			frmDados.sltProfissao.options[i].blur() ;
			break;
		}
	}	
}

