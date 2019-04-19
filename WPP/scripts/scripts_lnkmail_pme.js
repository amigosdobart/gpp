function fSubmeter(oForm)
{
	return (isNull(oForm.rz,"Razão Social")
			&& isNull(oForm.cnpj,"CNPJ")
			&& isNull(oForm.endereco,"Endereço de Correspondência")
			&& isNull(oForm.numero,"Número do Endereço") 
			&& isNull(oForm.complemento,"Complemento do Endereço")
			&& isNull(oForm.cep,"CEP")
			&& isNull(oForm.cidade,"Cidade")
			&& isNull(oForm.contato,"Nome do Responsável")
			&& isNull(oForm.cargo,"Cargo")
			&& isNull(oForm.email,"E-mail")
			&& isEmail(oForm.email)
			&& isNull(oForm.ddd,"DDD do Telefone para Contato") 
			&& isNull(oForm.telefone,"Telefone para Contato")
			&& vRadio(oForm.periodo,"Informe o período para ser contatado.")
			);
}