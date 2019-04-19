package com.brt.gpp.aplicacoes.aprovisionar;

import java.util.*;
import java.io.*;
import com.brt.gpp.comum.Definicoes;
/**
  *
  * Este arquivo contem a definicao da classe de Dados de Aprovisionamento 
  * <P> Versao:        	1.0
  *
  * @Autor:            	Daniel Abib
  * Data:               18/05/2004
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */
public class DadosAprovisionamento 
{
	// Atributos da Classe
	private String acao;
	private String msisdn;
	private String imsi;
	private String planoPreco;
	private double creditoInicial;
	private int idioma;
	private String novoMsisdn;
	private String novoPlanoPreco;
	private String novoImsi;
	private String listaFF;
	private String senha;
	private double tarifa;
	private String motivoDesativacao;
	private int idMotivoTroca;
	private int idMotivoBloqueio;
	private double franquia;
	
	private short status;
	private String dataExpiracao;
	private String operador;



	/**
	 * Metodo...: DadosAprovisionamento
	 * Descricao: Construtor
	 * @param String	aLinha	Linha do arquivo contendo as informacoes de
	 * 					aprovisionamento separados pelo caracter ","
	 */	
	public DadosAprovisionamento(String aLinha)
	{
		String campos[] = aLinha.split(",");
			
		if (campos[0].equals(Definicoes.TIPO_APR_ATIVACAO))
		{
			setAcao(campos[0]);
			setMsisdn(campos[1]);
			setImsi(campos[2]);
			setPlanoPreco(campos[3]);
			setCreditoInicial(campos[4]);
			setIdioma(campos[5]);			
		}
		else if (campos[0].equals(Definicoes.TIPO_APR_DESATIVACAO))
		{
			setAcao(campos[0]);
			setMsisdn(campos[1]);
			setMotivoDesativacao(campos[12]);
		}
		else if (campos[0].equals(Definicoes.TIPO_APR_BLOQUEIO))
		{
			setAcao(campos[0]);
			setMsisdn(campos[1]);
			setIdMotivoBloqueio(campos[14]);	
		}
		else if (campos[0].equals(Definicoes.TIPO_APR_DESBLOQUEIO))
		{
			setAcao(campos[0]);
			setMsisdn(campos[1]);			
		}
		else if (campos[0].equals(Definicoes.TIPO_APR_TROCA_MSISDN))
		{
			setAcao(campos[0]);
			setMsisdn(campos[1]);
			setNovoMsisdn(campos[6]);
			setIdMotivoTroca(campos[13]);
		}
		else if (campos[0].equals(Definicoes.TIPO_APR_TROCA_PLANO))
		{
			setAcao(campos[0]);
			setMsisdn(campos[1]);
			setNovoPlanoPreco(campos[7]);
			setTarifa(campos[11]);
			setFranquia(campos[15]);
		}
		else if (campos[0].equals(Definicoes.TIPO_APR_TROCA_SIMCARD))
		{
			setAcao(campos[0]);
			setMsisdn(campos[1]);
			setNovoImsi(campos[8]);
			setTarifa(campos[11]);
		}
		else if (campos[0].equals(Definicoes.TIPO_APR_ATUALIZA_FF))
		{
			setAcao(campos[0]);
			setMsisdn(campos[1]);
			setListaFF(campos[9]);
		}
		else if (campos[0].equals(Definicoes.TIPO_APR_TROCA_SENHA))
		{
			setAcao(campos[0]);
			setMsisdn(campos[1]);
			setSenha(campos[10]);
		}
		else if (campos[0].equals(Definicoes.TIPO_APR_TROCA_STATUS_ASSINANTE))
		{
			setAcao(campos[0]);
			setMsisdn(campos[1]);
			setStatus(campos[16]);
			setDataExpiracao(campos[17]);
			setOperador(campos[18]);
			
		}
	}
	
	/**
	 * Metodo...: getAcao
	 * Descricao: Retorna Acao de Aprovisionamento
	 * @return	String
	 */
	public String getAcao() {
		return acao;
	}

	/**
	 * Metodo...: getCreditoInicial
	 * Descricao: Retorna o Crédito Inicial do Assinante
	 * @return	double
	 */
	public double getCreditoInicial() {
		return creditoInicial;
	}

	/**
	 * Metodo...: getIdioma
	 * Descricao: Retorna o código do Idioma da URA
	 * @return	int
	 */
	public int getIdioma() {
		return idioma;
	}

	/**
	 * Metodo...: getIdMotivoBloqueio
	 * Descricao: Retorna o Identificador do Motivo de bloqueio
	 * @return	int
	 */
	public int getIdMotivoBloqueio() {
		return idMotivoBloqueio;
	}

	/**
	 * Metodo...: getIdMotivoTroca
	 * Descricao: Retorna o Identificador do Motivo da Troca
	 * @return	int
	 */
	public int getIdMotivoTroca() {
		return idMotivoTroca;
	}

	/**
	 * Metodo...: getImsi
	 * Descricao: Retorna o IMSI
	 * @return	String
	 */
	public String getImsi() {
		return imsi;
	}

	/**
	 * Metodo...: getListaFF
	 * Descricao: Retorna Lista de Family and Friends
	 * @return	String
	 */
	public String getListaFF() {
		return listaFF;
	}

	/**
	 * Metodo...: getMotivoDesativacao
	 * Descricao: Retorna o Motivo da Desativacao
	 * @return	String
	 */
	public String getMotivoDesativacao() {
		return motivoDesativacao;
	}

	/**
	 * Metodo...: getMsisdn
	 * Descricao: Retorna o MSISDN
	 * @return	String
	 */
	public String getMsisdn() {
		return msisdn;
	}

	/**
	 * Metodo...: getNovoImsi
	 * Descricao: retorna o Novo IMSI (caso de troca)
	 * @return	String
	 */
	public String getNovoImsi() {
		return novoImsi;
	}

	/**
	 * Metodo...: getNovoMsidn
	 * Descricao: Retorna o novo Msisdn (em caso de troca)
	 * @return	String
	 */
	public String getNovoMsisdn() {
		return novoMsisdn;
	}

	/**
	 * Metodo...: getNovoPlanoPreco
	 * Descricao: Retorna o Novo Plano de Preço	 (caso de troca)
	 * @return	String
	 */
	public String getNovoPlanoPreco() {
		return novoPlanoPreco;
	}

	/**
	 * Metodo...: getPlanoPreco
	 * Descricao: Retorna o Plano de Preço
	 * @return	String
	 */
	public String getPlanoPreco() {
		return planoPreco;
	}

	/**
	 * Metodo...: getSenha
	 * Descricao: Retorna a Senha
	 * @return	String
	 */
	public String getSenha() {
		return senha;
	}

	/**
	 * Metodo...: getTarifa
	 * Descricao: Retorna Tarifa
	 * @return	double
	 */
	public double getTarifa() {
		return tarifa;
	}

	/**
	 * Metodo...: getFranquia
	 * Descricao: Retorna a franquia do plano híbrido
	 * @return	double
	 */
	public double getFranquia() {
		return franquia;
	}
	
	/**
	 * Metodo...: getStatus
	 * Descricao: Retorna o status do assinante
	 * @return	short
	 */	
	public short getStatus() {
		return status;
	}

	/**
	 * Metodo...: getDataExpiracao
	 * Descricao: Retorna a data de expiração do assinante
	 * @return	String
	 */
	public String getDataExpiracao() {
		return dataExpiracao;
	}

	/**
	 * Metodo...: getOperador
	 * Descricao: Retorna o operador
	 * @return	String
	 */
	public String getOperador() {
		return operador;
	}	

	/**
	 * Metodo...: setAcao
	 * Descricao: Seta a ação de aprovisionamento
	 * @param String aAcao 	- Acao desejada para o aprovisionamento
	 */
	public void setAcao(String aAcao) {
		String acoesDisponiveis[] = {Definicoes.TIPO_APR_ATIVACAO,
                                     Definicoes.TIPO_APR_DESATIVACAO,
                                     Definicoes.TIPO_APR_BLOQUEIO,
                                     Definicoes.TIPO_APR_DESBLOQUEIO,
                                     Definicoes.TIPO_APR_TROCA_MSISDN,
			                         Definicoes.TIPO_APR_TROCA_PLANO,
			                         Definicoes.TIPO_APR_TROCA_SIMCARD,
			                         Definicoes.TIPO_APR_ATUALIZA_FF,
			                         Definicoes.TIPO_APR_TROCA_SENHA,
			                         Definicoes.TIPO_APR_TROCA_STATUS_ASSINANTE};
        boolean acaoOk=false;
        for (int i=0; i < acoesDisponiveis.length; i++)                             
			if (aAcao.equals(acoesDisponiveis[i]))
				acaoOk=true;

		if (!acaoOk)
			throw new IllegalArgumentException("Acao desconhecida: " + aAcao);

		acao = aAcao;
	}

	/**
	 * Metodo...: setCreditoInicial
	 * Descricao: Seta o Crédito Inicial do Assinante
	 * @param double vlrCredIni - Valor do Credito Inicial quando acao=ativacao
	 */
	public void setCreditoInicial(double vlrCredIni) {
		creditoInicial = vlrCredIni;
	}

	/**
	 * Metodo...: vlrCredIni
	 * Descricao: Seta o Valor do Credito Inicial (caso ele seja passado como string)
	 * @param string vlrCredIni - Valor do Credito Inicial quando acao=ativacao
	 */
	public void setCreditoInicial(String vlrCredIni) {
		creditoInicial = Double.parseDouble(vlrCredIni);
	}

	/**
	 * Metodo...: setIdioma
	 * Descricao: Seta o idioma da URA
	 * @param int aIdioma - idioma utilizado para o assinante
	 */
	public void setIdioma(int aIdioma) {
		idioma = aIdioma;
	}

	/**
	 * Metodo...: setIdioma
	 * Descricao: Seta o idioma da URA (parametro String)
	 * @param String aIdioma - idioma utilizado para o assinante
	 */
	public void setIdioma(String aIdioma) {
		idioma = Integer.parseInt(aIdioma);
	}

	/**
	 * Metodo...: setIdMotivoBloqueio
	 * Descricao: Seta o ID do Motivo de Bloqueio
	 * @param int aIdMotBloq - Identificacao do motivo quando acao=bloqueio
	 */
	public void setIdMotivoBloqueio(int aIdMotBloq) {
		idMotivoBloqueio = aIdMotBloq;
	}

	/**
	 * Metodo...: setIdMotivoBloqueio
	 * Descricao: Seta o ID do Motivo do Bloqueio (motivo string)
	 * @param String aIdMotBloq - Identificacao do motivo quando acao=bloqueio
	 */
	public void setIdMotivoBloqueio(String aIdMotBloq) {
		idMotivoBloqueio = Integer.parseInt(aIdMotBloq);
	}

	/**
	 * Metodo...: setIdMotivoTroca
	 * Descricao: Seta o ID do Motivo da Troca
	 * @param int aIdMotTroca - Identificacao do motivo da troca de algum servico
	 */
	public void setIdMotivoTroca(int aIdMotTroca) {
		idMotivoTroca = aIdMotTroca;
	}

	/**
	 * Metodo...: setIdMotivoTroca
	 * Descricao: Seta o ID do Motivo da Troca
	 * @param String aIdMotTroca - Identificacao do motivo da troca de algum servico
	 */
	public void setIdMotivoTroca(String aIdMotTroca) {
		idMotivoTroca = Integer.parseInt(aIdMotTroca);
	}

	/**
	 * Metodo...: setImsi
	 * Descricao: Seta o IMSI do assinante
	 * @param String aImsi - identificacao do IMSI do assinante
	 */
	public void setImsi(String aImsi) {
		imsi = aImsi;
	}

	/**
	 * Metodo...: setListaFF
	 * Descricao: Seta Lista de Family & Friends
	 * @param String aListaFF - lista de atualizacao de Family and Friends
	 */
	public void setListaFF(String aListaFF) {
		listaFF = aListaFF;
	}

	/**
	 * Metodo...: setMotivoDesativacao
	 * Descricao: Seta Motivo de Desativação
	 * @param String aMotDesat - Descricao do motivo para acao=desativacao
	 */
	public void setMotivoDesativacao(String aMotDesat) {
		motivoDesativacao = aMotDesat;
	}

	/**
	 * Metodo...: setMsisdn
	 * Descricao: Seta Msisdn
	 * @param String aMsisdn - Identificacao do numero do assinante 
	 */
	public void setMsisdn(String aMsisdn) {
		if (aMsisdn == null || aMsisdn.equals(""))
			throw new IllegalArgumentException("Msisdn inválido.");

		msisdn = aMsisdn;
	}

	/**
	 * Metodo...: setNovoImsi
	 * Descricao: Seta novo IMSI
	 * @param String aNovoImsi - Novo IMSI quando a acao=troca de imsi
	 */
	public void setNovoImsi(String aNovoImsi) {
		novoImsi = aNovoImsi;
	}

	/**
	 * Metodo...: setNovoMsisdn
	 * Descricao: Seta novo MSISDN
	 * @param String aNovoMsisdn - Numero de troca do assinante quando acao=troca de msisdn
	 */
	public void setNovoMsisdn(String aNovoMsisdn) {
		novoMsisdn = aNovoMsisdn;
	}

	/**
	 * Metodo...: setNovoPlanoPreco
	 * Descricao: Seta o novo plano de preço
	 * @param String aNovoPlano - Novo plano do assinante quando acao=troca de plano
	 */
	public void setNovoPlanoPreco(String aNovoPlano) {
		novoPlanoPreco = aNovoPlano;
	}

	/**
	 * Metodo...: setPlanoPreco
	 * Descricao: Seta Plano de Preço
	 * @param String aPlanoPreco - Identificacao do plano utilizado para o assinante
	 */
	public void setPlanoPreco(String aPlanoPreco) {
		planoPreco = aPlanoPreco;
	}

	/**
	 * Metodo...: setSenha
	 * Descricao: Seta Senha
	 * @param String aSenha - senha codificada quando acao=troca de senha
	 */
	public void setSenha(String aSenha) {
		senha = aSenha;
	}

	/**
	 * Metodo...: setTarifa
	 * Descricao: Seta Tarifa
	 * @param double aTarifa - Tarifa utilizada se for necessario para a execucao de algum servico
	 */
	public void setTarifa(double aTarifa) {
		tarifa = aTarifa;
	}

	/**
	 * Metodo...: setTarifa
	 * Descricao: Seta Tarifa
	 * @param String aTarifa - Tarifa utilizada se for necessario para a execucao de algum servico
	 */
	public void setTarifa(String aTarifa) {
		tarifa = Double.parseDouble(aTarifa);
	}

	/**
	 * Metodo...: setFranquia
	 * Descricao: Seta Franquia de Plano Híbrido
	 * @param double aFranquia - Franquia utilizada se for necessario para a execucao de algum servico
	 */
	public void setFranquia(double aFranquia) {
		franquia = aFranquia;
	}

	/**
	 * Metodo...: setFranquia
	 * Descricao: Seta Franquia de Plano Híbrido
	 * @param String aFranquia - Franquia utilizada se for necessario para a execucao de algum servico
	 */
	public void setFranquia(String aFranquia) {
		franquia = Double.parseDouble(aFranquia);
	}
	
	/**
	 * Metodo...: setOperador
	 * Descricao: Seta o operador
	 * @param String operador - Identificação da pessoa que está realizando a alteração
	 */
	public void setOperador(String operador) {
		this.operador = operador;
	}
	
	/**
	 * Metodo...: setStatus
	 * Descricao: Seta o status do assinantes
	 * @param short status - Novo status do assinante
	 */
	public void setStatus(String status) {
		this.status = Short.parseShort(status);
	}
	
	/**
	 * Metodo...: setDataExpiracao
	 * Descricao: Seta a data de expiração do assinantes
	 * @param String dataExpiracao - Data de expiração do assinantes
	 */
	public void setDataExpiracao(String dataExpiracao) {
		this.dataExpiracao = dataExpiracao;
	}
	

	/***
	 * Metodo...: carregaDoArquivo
	 * Descricao: Importa arquivo de aprovisionamento, realizando
	 * 				o parse desse arquivo sendo que cada linha do mesmo se transforma em um objeto
	 * 				de aprovisionamento
	 * @param File	f	Arquivo a ser carregado	
	 * @return	Collection	Collection de Objetos
	 * @throws IOException
	 */
	public static Collection carregaDoArquivo(File f) throws Exception
	{
		Collection aprovisionamentos = new Vector();	
		FileReader 		fReader = new FileReader(f);
		BufferedReader 	buffer 	= new BufferedReader(fReader);
		
		String linha=null;
		while ( (linha=buffer.readLine()) != null)
		{
			try
			{
				aprovisionamentos.add(new DadosAprovisionamento(linha));
			}
			catch(Exception e)
			{
				System.out.println("Erro ao realizar o parse da linha:"+linha+ ".Erro:"+e);
			}
		}
		buffer.close();
		fReader.close();

		return aprovisionamentos;
	}
	
	/**
	 * Metodo...: toString
	 * Descricao: Sobreposicao do metodo toString para retornar a identificacao
	 * 				do aprovisionamento
	 * @return String - Identificacao do aprovisionamento 
	 */
	public String toString()
	{
		return "Acao: " + getAcao() + " Msisdn: " + getMsisdn();
	}
}
