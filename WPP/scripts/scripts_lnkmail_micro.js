function fSubmeter(oForm)
{
	return (isNull(oForm.contrato,"Número do contrato")
			&& isNull(oForm.nome,"Nome do Titular")
			&& isNull(oForm.codigo,"CPF/CNPJ do titular")
			&& isNull(oForm.ddd,"DDD do Telefone para Habilitação") 
			&& isNull(oForm.telefone,"Telefone para Habilitação")
			&& isNull(oForm.contato,"Nome para contato")
			&& isNull(oForm.email,"E-mail")
			&& isEmail(oForm.email)
			&& vRadio(oForm.periodo,"Informe o período para ser contatado.")
			);
}