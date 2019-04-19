// Definicao do Pacote
package com.brt.gpp.aplicacoes.gerenciamentoVouchers;

/**
  *
  * Este arquivo contem a definicao da classe de Detalhes do Voucher 
  * <P> Versao:        	1.0
  *
  * @Autor:            	Vanessa Andrade
  * Data:               07/05/2004
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */
public class DetalheVoucherXML {

	/***
	 * Metodo...: DetalheVoucher
	 * Descricao: Construtor Padrão
	 */
	public DetalheVoucherXML()
	{
		// Construtor padrão
	}
	
	/**
	 * Metodo...: DetalheVoucher
	 * Descricao: Construtor com parâmetros
	 * @param int			CodAtivacao		Código de Ativação
	 * @param String		NumeroCaixa		Número da Caixa
	 * @param String		NumInicialLote	Número do Lote Inicial
	 * @param NumFinalLote	NumFinalLote	Número do Lote Final
	 * @param msgRetorno	msgRetorno		Mensagem de Retorno 
	 */
	public DetalheVoucherXML (int CodAtivacao, String NumeroCaixa,
						String NumInicialLote, String NumFinalLote,
						String msgRetorno)
	{
		this.Cod_Ativacao=CodAtivacao;
		this.Numero_Caixa=NumeroCaixa;
		this.Num_Inicial_Lote=NumInicialLote;
		this.Num_Final_Lote=NumFinalLote;
		this.MsgRetorno=msgRetorno;
	}
	
	// Atributos da Classe
	private int Cod_Ativacao;         
	private String Numero_Caixa;       
	private String Num_Inicial_Lote;   
	private String Num_Final_Lote;     
	private String MsgRetorno;         
	
	/**
	 * Metodo...: getCod_Ativacao
	 * Descricao: Retorna o Cod_Ativacao para a solicitacao de Voucher
	 * @return int	Cod_Ativacao
	 */
	public int getCod_Ativacao() 
	{
		return this.Cod_Ativacao;
	}

	/**
	 * Metodo...: getNumero_Caixa
	 * Descricao: Retorna o Numero_Caixa para a solicitacao de Voucher
	 * @return String	Numero_Caixa
	 */
	public String getNumero_Caixa() 
	{
		return this.Numero_Caixa;
	}

	/**
	 * Metodo...: getNum_Inicial_Lote
	 * Descricao: Retorna o Num_Inicial_Lote para a solicitacao de Voucher
	 * @return String	Num_Inicial_Lote
	 */
	public String getNum_Inicial_Lote() 
	{
		return this.Num_Inicial_Lote;
	}

	/**
	 * Metodo...: getNum_Final_Lote
	 * Descricao: Retorna o Num_Final_Lote para a solicitacao de Voucher
	 * @return String	Num_Final_Lote
	 */
	public String getNum_Final_Lote() 
	{
		return this.Num_Final_Lote;
	}

	/**
	 * Metodo...: getMsgRetorno
	 * Descricao: Retorna o MsgRetorno para a solicitacao de Voucher
	 * @return String	MsgRetorno
	 */
	public String getMsgRetorno() 
	{
		return this.MsgRetorno;
	}

	/**
	 * Metodo...: setCod_Ativacao
	 * Descricao: Atribui o Cod_Ativacao que esta solicitando o Voucher
	 * @param int	aCod_Ativacao
	 */
	public void setCod_Ativacao(int aCod_Ativacao) 
	{
		this.Cod_Ativacao = aCod_Ativacao;
	}

	/**
	 * Metodo...: setNumero_Caixa
	 * Descricao: Atribui o Numero_Caixa que esta solicitando o Voucher
	 * @param String	aNumero_Caixa
	 */
	public void setNumero_Caixa(String aNumero_Caixa) 
	{
		this.Numero_Caixa = aNumero_Caixa;
	}

	/**
	 * Metodo...: setNum_Inicial_Lote
	 * Descricao: Atribui o Num_Inicial_Lote que esta solicitando o Voucher
	 * @param String	aNum_Inicial_Lote
	 */
	public void setNum_Inicial_Lote(String aNum_Inicial_Lote) 
	{
		this.Num_Inicial_Lote = aNum_Inicial_Lote;
	}

	/**
	 * Metodo...: setNum_Final_Lote
	 * Descricao: Atribui o Num_Final_Lote que esta solicitando o Voucher
	 * @param String	aNum_Final_Lote
	 */
	public void setNum_Final_Lote(String aNum_Final_Lote) 
	{
		this.Num_Final_Lote = aNum_Final_Lote;
	}

	/**
	 * Metodo...: setMsgRetorno
	 * Descricao: Atribui o MsgRetorno que esta solicitando o Voucher
	 * @param String	aMsgRetorno
	 */
	public void setMsgRetorno(String aMsgRetorno) 
	{
		this.MsgRetorno = aMsgRetorno;
	}
}
