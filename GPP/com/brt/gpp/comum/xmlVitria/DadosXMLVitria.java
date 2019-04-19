package com.brt.gpp.comum.xmlVitria;
/**
 * Esta classe Contem todos os dados indispensaveis para a criacao do XML para o VITRIA.
 * Os XMLs do VITRIA estao divididos em Versoes, sendo estas definidas entre o GPP e o VITRIA.
 * @author Magno Batista Correa
 * @version 1.0
 * @since 2007/04/16
 */

public class DadosXMLVitria
{
	public static final int VERSAO_XML_VITRIA_001 = 1; // TBL_INT_PPP_OUT com status N
	public static final int VERSAO_XML_VITRIA_002 = 2;
	public static final int VERSAO_XML_VITRIA_003 = 3; // TBL_INT_PPP_OUT com status de Contingencia

	// Sistemas cadastrados
	public static final String ID_SISTEMA_GPP         = "GPP";
	public static final String ID_SISTEMA_SIM_BROWSER = "SIB";

	// Processos cadastrados
	public static final String PROCESSO_ASSINANTE_RECARGA_EXPIRADA    = "STATUS_REC_EXP";

	public static final String CODIGO_ERRO_OPERACAO_OK    = "0000";
	public static final String DESCRICAO_ERRO_OPERACAO_OK = "Operacao OK";
	
	private String sistema;
	private String processo;
	private String data;
	private String identificadorRequisicao;
	private int versaoXMLVitria;
	private String subXML;
	private String codigoErro;
	private String descricaoErro;
	
	/**
	 * Cria um objeto de dados do XML para o VITRIA para o caso de Operacao OK (Sem erros)
	 * @param versaoXMLVitria          Versao de XML para o Vitria
	 * @param sistema                  Sistema que estah gerando o XML
	 * @param processo                 Nome do processo
	 * @param data                     Data do XML
	 * @param identificadorRequisicao  Identificador da requisicao
	 * @param subXML                   Sub XML a ser passado pelo Vitria para outro sistema
	 */
	public DadosXMLVitria(int versaoXMLVitria, String subXML,
			String sistema, String processo, String data,
			String identificadorRequisicao)
	{
		this.subXML = subXML;
		this.sistema = sistema;
		this.processo = processo;
		this.data = data;
		this.identificadorRequisicao = identificadorRequisicao;
		this.versaoXMLVitria = versaoXMLVitria;
		this.codigoErro = CODIGO_ERRO_OPERACAO_OK;
		this.descricaoErro = DESCRICAO_ERRO_OPERACAO_OK;
	}
	/**
	 * Cria um objeto de dados do XML para o VITRIA para o caso de Operacao OK (Sem erros)
	 * @param versaoXMLVitria          Versao de XML para o Vitria
	 * @param sistema                  Sistema que estah gerando o XML
	 * @param processo                 Nome do processo
	 * @param data                     Data do XML
	 * @param identificadorRequisicao  Identificador da requisicao
	 * @param subXML                   Sub XML a ser passado pelo Vitria para outro sistema
	 * @param codigoErro               O codigo de erro retornado pela operacao (vide tbl_ger_codigos_retorno)
	 * @param descricaoErro            Descricao do codigo de erro              (vide tbl_ger_codigos_retorno)
	 */
	public DadosXMLVitria(int versaoXMLVitria, String subXML,
			String sistema, String processo, String data,
			String identificadorRequisicao,
			String codigoErro,String descricaoErro )
	{
		this.subXML = subXML;
		this.sistema = sistema;
		this.processo = processo;
		this.data = data;
		this.identificadorRequisicao = identificadorRequisicao;
		this.versaoXMLVitria = versaoXMLVitria;
		this.codigoErro = codigoErro;
		this.descricaoErro = descricaoErro;
	}

	/**
	 * @return Retorna o data.
	 */
	public String getData()
	{
		return data;
	}
	/**
	 * @return Retorna o familiaXMLVitria.
	 */
	public int getVersaoXMLVitria()
	{
		return versaoXMLVitria;
	}
	/**
	 * @return Retorna o identificadorRequisicao.
	 */
	public String getIdentificadorRequisicao()
	{
		return identificadorRequisicao;
	}
	/**
	 * @return Retorna o processo.
	 */
	public String getProcesso()
	{
		return processo;
	}
	/**
	 * @return Retorna o sistema.
	 */
	public String getSistema()
	{
		return sistema;
	}

	/**
	 * @param data O data para alterar.
	 */
	public void setData(String data)
	{
		this.data = data;
	}
	/**
	 * @param versaoXMLVitria a versaoXMLVitria para alterar.
	 */
	public void setVersaoXMLVitria(int versaoXMLVitria)
	{
		this.versaoXMLVitria = versaoXMLVitria;
	}
	/**
	 * @param identificadorRequisicao O identificadorRequisicao para alterar.
	 */
	public void setIdentificadorRequisicao(String identificadorRequisicao)
	{
		this.identificadorRequisicao = identificadorRequisicao;
	}
	/**
	 * @param processo O processo para alterar.
	 */
	public void setProcesso(String processo)
	{
		this.processo = processo;
	}
	/**
	 * @param sistema O sistema para alterar.
	 */
	public void setSistema(String sistema)
	{
		this.sistema = sistema;
	}

	/**
	 * @return Retorna o subXML.
	 */
	public String getSubXML() {
		return subXML;
	}

	/**
	 * @param subXML O subXML para alterar.
	 */
	public void setSubXML(String subXML) {
		this.subXML = subXML;
	}

	/**
	 * @return Retorna o codigoErro.
	 */
	public String getCodigoErro() {
		return codigoErro;
	}

	/**
	 * @return Retorna o descricaoErro.
	 */
	public String getDescricaoErro() {
		return descricaoErro;
	}

	/**
	 * @param codigoErro O codigoErro para alterar.
	 */
	public void setCodigoErro(String codigoErro) {
		this.codigoErro = codigoErro;
	}

	/**
	 * @param descricaoErro O descricaoErro para alterar.
	 */
	public void setDescricaoErro(String descricaoErro) {
		this.descricaoErro = descricaoErro;
	}
}
