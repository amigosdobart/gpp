package com.brt.gpp.aplicacoes.gerenciamentoVouchers;

/**
 * Esta classe armazena as informacoes sobre a requisicao de ativacao
 * de cartoes fisicos vindos do sistema SAP. Os dados da requisicao
 * ficam neste objeto para serem processados pelo processo de ativacao
 * e controle de jobs da tecnomen  
 * 
 * @author Vanessa Andrade
 * 
 * Adaptacao de pacote por Joao Carlos
 * Data..: 30-Jan-2005
 * Modificacoes: Controle do Id da requisicao para uso no novo processo de gerenciamento de vouchers
 * 
 */
public class SolicitacaoAtivacao
{
	private long   idRequisicao;
	private String eventoNegocio;
	private String Interface;
	private String Remessa;
	private String Ano_Operacao;
	private String Cod_Atividade;
	private int Cod_Ativacao;
	private String Cod_Motivo;
	private String Numero_Caixa;
	private String Num_Inicial_Lote;
	private String Num_Final_Lote;

	 /**
	  * Metodo...: getInterface
	  * Descricao: Retorna o Interface para a solicitacao de Voucher
	  * @return String	Interface
	  */
	 public String getInterface()
	 {
		 return this.Interface;
	 }

	 /**
	  * Metodo...: getRemessa
	  * Descricao: Retorna a Remessa para a solicitacao de Voucher
	  * @return String	Remessa
	  */
	 public String getRemessa()
	 {
		 return this.Remessa;
	 }

	 /**
	  * Metodo...: getAno_Operacao
	  * Descricao: Retorna o Ano_Operacao (AAAA) para a solicitacao de Voucher
	  * @return String	Ano_Operacao
	  */
	 public String getAno_Operacao()
	 {
		 return this.Ano_Operacao;
	 }

	 /**
	  * Metodo...: getCod_Atividade
	  * Descricao: Retorna o Cod_Atividade para a solicitacao de Voucher
	  * @return String	Cod_Atividade
	  */
	 public String getCod_Atividade()
	 {
		 return this.Cod_Atividade;
	 }

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
	  * Metodo...: getCod_Motivo
	  * Descricao: Retorna o Cod_Motivo para a solicitacao de Voucher
	  * @return String	Cod_Motivo
	  */
	 public String getCod_Motivo()
	 {
		 return this.Cod_Motivo;
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

	 // Métodos Setter
	 /**
	  * Metodo...: setInterface
	  * Descricao: Atribui a Interface que esta solicitando o Voucher
	  * @param String	aInterface
	  */
	 public void setInterface(String aInterface)
	 {
		 this.Interface = aInterface;
	 }

	 /**
	  * Metodo...: setRemessa
	  * Descricao: Atribui o Remessa que esta solicitando o Voucher
	  * @param String	aRemessa
	  */
	 public void setRemessa(String aRemessa)
	 {
		 this.Remessa = aRemessa;
	 }

	 /**
	  * Metodo...: setAno_Operacao
	  * Descricao: Atribui o Ano_Operacao que esta solicitando o Voucher
	  * @param String	aAno_Operacao
	  */
	 public void setAno_Operacao(String aAno_Operacao)
	 {
		 this.Ano_Operacao = aAno_Operacao;
	 }

	 /**
	  * Metodo...: setCod_Atividade
	  * Descricao: Atribui o Cod_Atividade que esta solicitando o Voucher
	  * @param String	aCod_Atividade
	  */
	 public void setCod_Atividade(String aCod_Atividade)
	 {
		 this.Cod_Atividade = aCod_Atividade;
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
	  * Metodo...: setCod_Motivo
	  * Descricao: Atribui o Cod_Motivo que esta solicitando o Voucher
	  * @param String	aCod_Motivo
	  */
	 public void setCod_Motivo(String aCod_Motivo)
	 {
		 this.Cod_Motivo = aCod_Motivo;
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
	  * Metodo...: setNum_Inicial
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
	 * @return Returns the eventoNegocio.
	 */
	public String getEventoNegocio()
	{
		return eventoNegocio;
	}
	
	/**
	 * @param eventoNegocio The eventoNegocio to set.
	 */
	public void setEventoNegocio(String eventoNegocio)
	{
		this.eventoNegocio = eventoNegocio;
	}

	/**
	 * @return Returns the idRequisicao.
	 */
	public long getIdRequisicao()
	{
		return idRequisicao;
	}

	/**
	 * @param idRequisicao The idRequisicao to set.
	 */
	public void setIdRequisicao(long idRequisicao)
	{
		this.idRequisicao = idRequisicao;
	}
}
