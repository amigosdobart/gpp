package com.brt.gpp.aplicacoes.promocao.pacoteDados;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.promocao.persistencia.Consulta;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.MapPromocaoLimiteSegundos;
import com.brt.gpp.comum.mapeamentos.MapTipoSaldo;
import com.brt.gpp.comum.mapeamentos.entidade.AssinanteOfertaPacoteDados;
import com.brt.gpp.comum.mapeamentos.entidade.OfertaPacoteDados;
import com.brt.gpp.comum.mapeamentos.entidade.PacoteDados;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;

/**
 * Classe responsavel por controlar o creditos do saldo
 * principal dos assinantes que estao utilizando uma 
 * Oferta de Pacote de Dados
 * 
 * @author Joao Paulo Galvagni
 * @since  11/09/2007
 */
public class GerentePacoteDadosProdutor extends Aplicacoes
			 							implements ProcessoBatchProdutor
{
	private String			  status 	 		 = null;
	private PREPConexao 	  conexaoPrep 		 = null;
	private ResultSet		  rs				 = null;
	private HashMap 		  mapOfertas 		 = null;
	private String 			  dataInicio	 	 = null;
	private int				  numRegistros		 = 0;
	MapPromocaoLimiteSegundos mapLimiteSegundos  = null;
	
	/**
	 * Construtor da Classe
	 * 
	 * @param aLogId - Id para controle de log
	 */
	public GerentePacoteDadosProdutor(long aLogId)
	{
		super(aLogId, Definicoes.CL_GER_PACOTE_DADOS_PROD);
	}
	
	/**
	 * Metodo....: startup
	 * Descricao.: Realiza a consulta dos assinantes que estao utilizando
	 * 			   de alguma Oferta de Pacote de Dados
	 * 
	 */
	public void startup(String[] params) throws Exception
	{
		super.log(Definicoes.DEBUG, "startup", "Inicio");
		dataInicio = getDataProcessamento();
		
		// Um HashMap eh criado para conter as informacoes das
		// Ofertas de Pacote de Dados, por conter varios registros
		// semelhantes
		mapOfertas = new HashMap();
		
		try
		{
            conexaoPrep = gerenteBancoDados.getConexaoPREP(getIdLog());
    		Consulta consulta = new Consulta(getIdLog());
            
            super.log(Definicoes.INFO, "startup", "Selecionando os assinantes que utilizam Pacote de Dados...");
            
            // Seta o ResultSet com o resultado da consulta
            rs = consulta.getAssinantesContratadosPacoteDados(conexaoPrep);
		}
		catch (GPPInternalErrorException e)
		{
			setStatusProcesso(Definicoes.PROCESSO_ERRO);
		    super.log(Definicoes.ERRO, "startup", "Erro GPP: " + e);
		}
		
		setStatusProcesso(Definicoes.PROCESSO_SUCESSO);
		super.log(Definicoes.DEBUG, "startup", "Fim");
	}
	
	/**
	 * Metodo....: next
	 * Descricao.: Retorna o proximo registro a ser processado
	 * 			   pelo Consumidor
	 * 
	 * @return Object - Objeto populado com as informacoes de Banco
	 */
	public Object next() throws Exception
	{
		AssinanteOfertaPacoteDados assinanteOferta =  null;
		
		if (rs.next())
		{
			assinanteOferta = new AssinanteOfertaPacoteDados();
			int idOferta = rs.getInt("IDT_OFERTA");
			OfertaPacoteDados ofertaPacoteDados = (OfertaPacoteDados) mapOfertas.get(new Integer(idOferta));
			
			// Valida se a Oferta de Pacote de Dados ainda nao existe no map
			if (ofertaPacoteDados == null)
			{
				PacoteDados pacoteDados = new PacoteDados();
				ofertaPacoteDados = new OfertaPacoteDados();
				pacoteDados.setIdtPacoteDados(rs.getInt("IDT_PACOTE_DADOS"));
				pacoteDados.setNumDias(rs.getInt("NUM_DIAS"));
				pacoteDados.setDesValorPacote(rs.getString("DES_VLR_PACOTE"));
				pacoteDados.setDesPacote(rs.getString("DES_PACOTE"));
				pacoteDados.setHabilitado(rs.getBoolean("IND_HABILITADO"));
				
				// Caso nao exista, uma nova instancia da Oferta sera criada
				// para ser inserida no map
				ofertaPacoteDados.setPacoteDados(pacoteDados);
				ofertaPacoteDados.setIdtOferta(idOferta);
				ofertaPacoteDados.setTipoSaldo(MapTipoSaldo.getInstance().getByIdTipoSaldoVoucher(rs.getShort("IDT_TIPO_SALDO")));
				ofertaPacoteDados.setDataInicioOferta(rs.getDate("DAT_INICIO_OFERTA"));
				ofertaPacoteDados.setDataFimOferta(rs.getDate("DAT_FIM_OFERTA"));
				mapOfertas.put(new Integer(idOferta), ofertaPacoteDados);
			}
			
			// Seta a OfertaPacoteDados
			assinanteOferta.setOfertaPacoteDados(ofertaPacoteDados);
			assinanteOferta.setMsisdn(rs.getString("IDT_MSISDN"));
			assinanteOferta.setValorSaldoTorpedo(rs.getBigDecimal("VLR_SALDO_TORPEDOS"));
			assinanteOferta.setValorSaldoDados(rs.getBigDecimal("VLR_SALDO_DADOS"));
			assinanteOferta.setAssinanteSuspenso(rs.getBoolean("IND_SUSPENSO"));
			assinanteOferta.setDataContratacao(rs.getDate("DAT_CONTRATACAO"));
			
			numRegistros++;
		}
		
		return assinanteOferta;
	}
	
	/**
	 * Metodo....: getConexao
	 * Descricao.: Retorna a conexao do Banco de Dados do Produtor
	 * 
	 * @return conexaoPrep - Conexao com o Banco de Dados
	 */
	public PREPConexao getConexao()
	{
		return conexaoPrep;
	}
	
	/**
	 * Metodo....: getDataProcessamento
	 * Descricao.: Retorna a data de processamento atual
	 * 
	 * @return String - Data atual de processamento
	 */
	public String getDataProcessamento()
	{
		 return new SimpleDateFormat(Definicoes.MASCARA_DATE).format(Calendar.getInstance().getTime());
	}
	
	/**
	 * Metodo....: getDescricaoProcesso
	 * Descricao.: Retorna a descricao do Processo
	 * 
	 * @return String - Detalhe do processo
	 */
	public String getDescricaoProcesso()
	{
		return "Gerenciamento do Cadastro de Pacote de Dados. " +
		   	   "Qtde registros: " + numRegistros;
	}
	
	/**
	 * Metodo....: getIdProcessoBatch
	 * Descricao.: Retorna o ID do processo batch
	 * 
	 * @return idProcessoBatch - ID do processo batch
	 */
	public int getIdProcessoBatch()
	{
		return Definicoes.IND_GER_PACOTE_DADOS;
	}
	
	/**
	 * Metodo....: getStatusProcesso
	 * Descricao.: Retorna o status do processo
	 * 
	 * @return status - Status atual do processo
	 */
	public String getStatusProcesso()
	{
		return status;
	}
	
	/**
	 * Metodo....: setStatusProcesso
	 * Descricao.: Seta o status do processo
	 * 
	 * @param novoStatus - Novo status do processo
	 */
	public void setStatusProcesso(String novoStatus)
	{
		status = novoStatus;
	}
	
	/**
	 * Metodo....: finish
	 * Descricao.: Realiza as acoes apos o termino do 
	 * 			   processamento do produtor
	 * 
	 */
	public void finish() throws Exception
	{
		// Chama a funcao para gravar no historico o Processo em questao
		super.gravaHistoricoProcessos(getIdProcessoBatch(), dataInicio,
									  getDataProcessamento(), getStatusProcesso(),
									  getDescricaoProcesso(), dataInicio);
		try
		{
			if (rs != null)
				rs.close();
		}
		catch(Exception e)
		{}
		
		gerenteBancoDados.liberaConexaoPREP(conexaoPrep, getIdLog());
		
		super.log(Definicoes.DEBUG, "finish", "Status de processamento: " + getStatusProcesso());
		super.log(Definicoes.DEBUG, "finish", "Descricao do processamento: " + getDescricaoProcesso());
	}
	
	/**
	 * Metodo....: handleException
	 * Descricao.: Trata as excecoes geradas pelo Produtor
	 * 
	 */
	public void handleException()
	{
	}
}