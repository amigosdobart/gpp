package com.brt.gpp.comum.xmlVitria;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GerarXML;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

/**
 * Esta classe Gera um XML nos moldes do VITRIA.
 * Para montar este xml, precisa ser definido os dados padrao, sendo o mais importante a Versao do XML que este pertence.
 * Outra informacao importante eh o subXML que estarah dentro das tags CDATA
 * @author Magno Batista Correa
 * @version 1.0
 * @since 2007/04/16
 */
public class GerarXMLVitria
{
	private static final String sqlInserirVitriaOut =
		"INSERT INTO INTERFACE_VITRIA_OUT  " +
		"(   SEQUENCIAL,PROCESSO,          " +
		"    ID_SISTEMA,DATA_INCLUSAO,     " +
		"    DATA_LEITURA,NR_PRIORIDADE,   " +
		"    XML,CONTROLE_VITRIA           " +
		")                                 " +
		"VALUES                            " +
		"(   SEQ_INT_VITRIA_OUT.NEXTVAL,?, " +
		"    ?,SYSDATE,                    " +
		"    NULL,0,                       " +
		"    ?,?                           " +
		")                                 " ;
	
	private static final String sqlInserirTblIntPppOut =
		"INSERT INTO TBL_INT_PPP_OUT       " +
	   	"(    ID_PROCESSAMENTO,            " +
	   	"     DAT_CADASTRO,                " +
	   	"     IDT_EVENTO_NEGOCIO,          " +
	   	"     XML_DOCUMENT,                " +
	   	"     IDT_STATUS_PROCESSAMENTO     " +
	   	")                                 " +
	   	" VALUES                           " +
	   	"(    SEQ_ID_PROCESSAMENTO.NEXTVAL," +
	   	"     SYSDATE,                     " +
	   	"     ?, ?, ?                      " +
	   	")                                 ";

	private PREPConexao           conexaoPrep;
	protected long				  logId = 0;

	private GerarXML       geradorXML;
	private DadosXMLVitria dadosXMLVitria;
	
	private String xml;

	/**
	 * Monta o XML que sera armazenado na tabela de interface resultante do processamento consumidor
	 * @param subXML            -  XML que estarah dentro do CDATA
	 */
	public GerarXMLVitria(DadosXMLVitria dadosXMLVitria, PREPConexao conexaoPrep, long logId)
	{
		this.conexaoPrep = conexaoPrep;
		this.logId = logId;
		this.dadosXMLVitria = dadosXMLVitria;
		
		switch (dadosXMLVitria.getVersaoXMLVitria())
		{
		case DadosXMLVitria.VERSAO_XML_VITRIA_001:
		case DadosXMLVitria.VERSAO_XML_VITRIA_003:
			this.xml = dadosXMLVitria.getSubXML();
			break;
		case DadosXMLVitria.VERSAO_XML_VITRIA_002:
			geradorXML = new GerarXML("mensagem");
			geradorXML.abreNo("cabecalho");
			geradorXML.adicionaTag("empresa", "BRG");
			geradorXML.adicionaTag("sistema", dadosXMLVitria.getSistema());
			geradorXML.adicionaTag("processo", dadosXMLVitria.getProcesso());
			geradorXML.adicionaTag("data", dadosXMLVitria.getData());
			geradorXML.adicionaTag("identificador_requisicao", dadosXMLVitria.getIdentificadorRequisicao());
			geradorXML.adicionaTag("codigo_erro", dadosXMLVitria.getCodigoErro());
			geradorXML.adicionaTag("descricao_erro", dadosXMLVitria.getDescricaoErro());
			geradorXML.fechaNo();
			geradorXML.abreNo("conteudo");
			geradorXML.abreTagCDATA();
			geradorXML.concatenaString(dadosXMLVitria.getSubXML());
			geradorXML.fechaTagCDATA();
			geradorXML.fechaNo();
				
			this.xml = new String(GerarXML.PROLOG_XML_VERSAO_1_0_UTF8 + this.geradorXML.getXML());
			break;
		default:
			this.xml = "";
		break;
		}
	}
	
	/**
	 * 
	 * @return XML Completo, com cabecalho
	 */
	public String getXML()
	{
		//Retorno do XML completo
		return xml; 
	}
	
	/**
	 * Insere o XML criado na tabela de interface de saída com o VITRIA (INTERFACE_VITRIA_OUT)
	 * @return Numero de linhas processadas. 0 para falha e 1 para sucesso.
	 * @throws GPPInternalErrorException
	 */
	public int InserirXMLVitriaBanco() throws GPPInternalErrorException
	{
		int numLinhas = 0;
		switch (dadosXMLVitria.getVersaoXMLVitria())
		{
			case DadosXMLVitria.VERSAO_XML_VITRIA_001:
				Object[] parametros_001 = {dadosXMLVitria.getProcesso(),this.getXML(), Definicoes.IDT_PROCESSAMENTO_NOT_OK};
				numLinhas = conexaoPrep.executaPreparedUpdate(sqlInserirTblIntPppOut, parametros_001, this.getLogId());
				break;
			case DadosXMLVitria.VERSAO_XML_VITRIA_002:
				// Insere um xml na interface do VITRIA com os parâmetros dados
				Object[] parametros_002 = {dadosXMLVitria.getProcesso(), dadosXMLVitria.getSistema(), this.getXML(), Definicoes.IDT_PROCESSAMENTO_NOT_OK};
				numLinhas = conexaoPrep.executaPreparedUpdate(sqlInserirVitriaOut, parametros_002, this.getLogId());
				break;
			default:
				numLinhas = 0;
			break;
			case DadosXMLVitria.VERSAO_XML_VITRIA_003:
				Object[] parametros_003 = {dadosXMLVitria.getProcesso(),this.getXML(), Definicoes.IDT_PROCESSAMENTO_RETIDO_CONTINGENCIA};
				numLinhas = conexaoPrep.executaPreparedUpdate(sqlInserirTblIntPppOut, parametros_003, this.getLogId());
				break;
		}
		return numLinhas;
	}

	/**
	 * @return Retorna o logId.
	 */
	public long getLogId()
	{
		return logId;
	}

	/**
	 * @param logId O logId para alterar.
	 */
	public void setLogId(long logId)
	{
		this.logId = logId;
	}
}
