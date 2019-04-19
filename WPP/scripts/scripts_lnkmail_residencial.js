function fSubmeter(oForm)
{
	return (isNull(oForm.contrato,"N�mero do contrato")
			&& isNull(oForm.nome,"Nome do Titular")
			&& isNull(oForm.codigo,"CPF/CNPJ do titular")
			&& isNull(oForm.ddd,"DDD do Telefone para Habilita��o") 
			&& isNull(oForm.telefone,"Telefone para Habilita��o")
			&& isNull(oForm.contato,"Nome para contato")
			&& isNull(oForm.email,"E-mail")
			&& isEmail(oForm.email)
			&& isNull(oForm.ddd_usuario,"DDD do Telefone para Contato") 
			&& isNull(oForm.telefone_usuario,"Telefone para Contato")
			&& vRadio(oForm.periodo,"Informe o per�odo para ser contatado.")
			);
}