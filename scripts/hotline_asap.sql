SELECT * FROM TBL_INT_PPP_OUT

INSERT INTO TBL_INT_PPP_OUT
VALUES(
SEQ_ID_PROCESSAMENTO.NEXTVAL,SYSDATE,
'BLOQUEIO_ASAP',
'<root>	<idorigem>		<sistema>URA</sistema>	</idorigem>	<id_os>SB618401890688855885</id_os>	<msisdn_novo>556184018906</msisdn_novo>	<msisdn_antigo>556184018906</msisdn_antigo>	<case_type>Retirada de bloqueio Hotline</case_type>	<case_sub_type>Retirada de bloqueio Hotline</case_sub_type>	<fluxo>DesbloqueioURA</fluxo>	<order_priority>alta</order_priority>	<categoria>F2</categoria>	<categoria_anterior>F1</categoria_anterior>	<case_id>SBSB618401890688855885</case_id>	<nome_produto_smp>Produto Acesso Movel</nome_produto_smp>	<identificador_produto_smp>556184018906</identificador_produto_smp>	<provision>		<ELM_INFO_SIMCARD>			<macro_servico>ELM_INFO_SIMCARD</macro_servico>			<operacao>nao_alterado</operacao>			<x_tipo>SIMCARD</x_tipo>			<status>NAO_FEITO</status>			<parametros>				<simcard_msisdn>556184018906</simcard_msisdn>			</parametros>		</ELM_INFO_SIMCARD>		<ELM_BLOQ_HOT_LINE>			<macro_servico>ELM_BLOQ_HOT_LINE</macro_servico>			<operacao>Retirar</operacao>			<x_tipo>SERVICO DE BLOQUEIO</x_tipo>			<status>NAO_FEITO</status>		</ELM_BLOQ_HOT_LINE>	</provision></root>',
'N')

COMMIT


SELECT * FROM TBL_INT_PPP_IN
WHERE
IDT_EVENTO_NEGOCIO = 'BLOQUEIO_ASAP'
ORDER BY DAT_CADASTRO DESC
