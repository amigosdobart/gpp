function fSubmeter(oForm)
{
	return (isNull(oForm.rz,"Raz�o Social")
			&& isNull(oForm.cnpj,"CNPJ")
			&& isNull(oForm.endereco,"Endere�o de Correspond�ncia")
			&& isNull(oForm.numero,"N�mero do Endere�o") 
			&& isNull(oForm.complemento,"Complemento do Endere�o")
			&& isNull(oForm.cep,"CEP")
			&& isNull(oForm.cidade,"Cidade")
			&& isNull(oForm.contato,"Nome do Respons�vel")
			&& isNull(oForm.cargo,"Cargo")
			&& isNull(oForm.email,"E-mail")
			&& isEmail(oForm.email)
			&& isNull(oForm.ddd,"DDD do Telefone para Contato") 
			&& isNull(oForm.telefone,"Telefone para Contato")
			&& vRadio(oForm.periodo,"Informe o per�odo para ser contatado.")
			);
}