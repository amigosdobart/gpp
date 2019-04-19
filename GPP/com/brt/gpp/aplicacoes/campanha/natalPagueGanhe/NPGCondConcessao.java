package com.brt.gpp.aplicacoes.campanha.natalPagueGanhe;

import java.util.Calendar;
import java.util.Date;

import com.brt.gpp.aplicacoes.campanha.dao.AssinanteCampanhaDAO;
import com.brt.gpp.aplicacoes.campanha.dao.NatalPagueGanheDAO;
import com.brt.gpp.aplicacoes.campanha.entidade.AssinanteCampanha;
import com.brt.gpp.aplicacoes.campanha.entidade.Campanha;
import com.brt.gpp.aplicacoes.campanha.entidade.CondicaoConcessao;
import com.brt.gpp.aplicacoes.campanha.entidade.NPGInfosBonificacao;
import com.brt.gpp.aplicacoes.campanha.entidade.NPGStatusBonificacao;
import com.brt.gpp.aplicacoes.promocao.controle.ControlePulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.AssinantePulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.Promocao;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoStatusAssinante;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.mapeamentos.MapCodigosRetorno;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.mapeamentos.MapPromocao;

/**
 *	Implementacao da validacao das condicoes de concessao do bonus one-shot da promocao Natal Pague e Ganhe.
 * 
 *	@author		Daniel Ferreira
 *	@since		08/11/2006
 */
public class NPGCondConcessao implements CondicaoConcessao 
{
	
	/**
	 *	Campanha promocional cujas condicoes de concessao sao implementados por esta classe.
	 */
	private Campanha campanha;
	
	/**
	 *	Objeto que encapsula as informacoes referentes a bonificacao do assinante.
	 */
	private NPGInfosBonificacao infosBonificacao;
	
	//Construtores.
	
	/**
	 *	Construtor da classe.
	 */
	public NPGCondConcessao()
	{
		this.campanha			= null;
		this.infosBonificacao	= null;
	}
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		campanha				Campanha a qual as condicoes de concessao pertencem.
	 */
	public NPGCondConcessao(Campanha campanha)
	{
		super();
		
		this.campanha = campanha;
	}
	
	//Implementacao de CondicaoConcessao.
	
	/**
	 *	@see		com.brt.gpp.aplicacoes.campanha.entidade.CondicaoConcessao#deveSerBonificado(AssinanteCampanha, PREPConexao)
	 */
	public boolean deveSerBonificado(AssinanteCampanha assinante, PREPConexao conexaoPrep)
	{
		try
		{
			//Obtendo as informacoes de bonificacao a partir dos parametros.
			this.infosBonificacao = NPGInfosBonificacao.newInstance(assinante.getParametros());
			//Verificando se o assinante nao esta validado. Esta e a primeira acao uma vez que o processo de inscricao
			//cadastra assinantes na campanha, mesmo nao validados, para facilitar a consulta no WPP. Assinantes 
			//nao validados devem ser retirados da campanha pelo processo de concessao.
			if(this.infosBonificacao.getCodStatus() == NPGStatusBonificacao.NAO_VALIDADO)
			{
				AssinanteCampanhaDAO.retiraAssinante(assinante);
				return false;
			}
			//Verificando se o assinante ja recebeu o bonus. Em caso positivo o bonus nao pode ser concedido novamente.
			//Portanto e necessario retirar o assinante da campanha e executar a pos-bonificacao.
			this.validarRecebimento(assinante, conexaoPrep);
			if(this.infosBonificacao.getCodStatus() == NPGStatusBonificacao.BONUS_CONCEDIDO)
			{
				this.executarPosBonificacao(assinante, conexaoPrep);
				AssinanteCampanhaDAO.retiraAssinante(assinante);
				return false;
			}
			//Verificando se o assinante realizou o montante minimo em recargas.
			if(this.infosBonificacao.getCodStatus() == NPGStatusBonificacao.INSCRITO)
				this.validarRecargas(assinante, conexaoPrep);
			//Se o periodo de espera nao tiver terminado, o bonus nao deve ser concedido e o status deve manter-se inalterado.
			if(((this.infosBonificacao.getCodStatus() == NPGStatusBonificacao.INSCRITO) ||
				(this.infosBonificacao.getCodStatus() == NPGStatusBonificacao.RECARGA_EFETUADA)) && 
			   (!this.isFimEspera()))
				return false;
			//Verifcando se o assinante esta cadastrado em promocao Pula-Pula valida.
			if((this.infosBonificacao.getCodStatus() == NPGStatusBonificacao.RECARGA_EFETUADA) || 
			   (this.infosBonificacao.getCodStatus() == NPGStatusBonificacao.PENDENTE_PULA_PULA))
				this.validarPulaPula(assinante, conexaoPrep);
			//Verificando se o bonus do assinante esta liberado.
			if(this.infosBonificacao.getCodStatus() == NPGStatusBonificacao.BONUS_LIBERADO)
				return true;
		}
		catch(Exception e)
		{
			//Em caso de excecao alterar o codigo de retorno para erro tecnico e manter o status.
			try
			{
				MapCodigosRetorno mapRetorno = MapCodigosRetorno.getInstance();
				int codRetorno = Definicoes.RET_ERRO_TECNICO;
				this.infosBonificacao.setCodRetorno(codRetorno);
				this.infosBonificacao.setDescRetorno(mapRetorno.getRetorno(codRetorno).getDescRetorno());
				AssinanteCampanhaDAO.atualizarParamsAssinanteCampanha(assinante, conexaoPrep);
			}
			catch(Exception ignore){}
		}
		
		return false;
	}
	
	/**
	 *	@see		com.brt.gpp.aplicacoes.campanha.entidade.CondicaoConcessao#executarPosBonificacao(AssinanteCampanha, PREPConexao)
	 */
	public void executarPosBonificacao(AssinanteCampanha assinante, PREPConexao conexaoPrep)
	{
		MapCodigosRetorno mapRetorno = MapCodigosRetorno.getInstance();
		
		try
		{
			//Obtendo as informacoes de bonificacao a partir dos parametros e confirmando a bonificacao do assinante.
			this.infosBonificacao = NPGInfosBonificacao.newInstance(assinante.getParametros());
			this.validarRecebimento(assinante, conexaoPrep);
			
			//Alterando o status do Pula-Pula do assinante para que os saldos nao seja zerados na proxima concessao.
			ControlePulaPula controle = new ControlePulaPula(conexaoPrep.getIdProcesso());
			controle.trocarStatusPulaPula(assinante.getMsisdn(), PromocaoStatusAssinante.STATUS_ATIVO_RECARGA, conexaoPrep);
		}
		catch(Exception e)
		{
			//Em caso de excecao alterar o codigo de retorno para erro tecnico e manter o status.
			try
			{
				int codRetorno = Definicoes.RET_ERRO_TECNICO;
				this.infosBonificacao.setCodRetorno(codRetorno);
				this.infosBonificacao.setDescRetorno(mapRetorno.getRetorno(codRetorno).getDescRetorno());
				AssinanteCampanhaDAO.atualizarParamsAssinanteCampanha(assinante, conexaoPrep);
			}
			catch(Exception ignore){}
		}
	}
	
	/**
	 *	@see		com.brt.gpp.aplicacoes.campanha.entidade.CondicaoConcessao#getNomeCondicao()
	 */
	public String getNomeCondicao()
	{
		return "NPGCondConcessao";
	}
	
	/**
	 *	@see		com.brt.gpp.aplicacoes.campanha.entidade.CondicaoConcessao#getValorConcederBonus()
	 */
	public double getValorConcederBonus()
	{
		if(this.infosBonificacao != null)
			return this.infosBonificacao.getValorAparelho();
		
		return 0.0;
	}
	
	/**
	 *	@see		com.brt.gpp.aplicacoes.campanha.entidade.CondicaoConcessao#getValorConcederSM()
	 */
	public double getValorConcederSM()
	{
		return 0.0;
	}
	
	/**
	 *	@see		com.brt.gpp.aplicacoes.campanha.entidade.CondicaoConcessao#getValorConcederDados()
	 */
	public double getValorConcederDados()
	{
		return 0.0;
	}
	
	/**
	 *	@see		com.brt.gpp.aplicacoes.campanha.entidade.CondicaoConcessao#getDataSatisfacaoCondicao()
	 */
	public Date getDataSatisfacaoCondicao()
	{
		return Calendar.getInstance().getTime();
	}
	
	//Metodos de validacao e controle de status da bonificacao.

	/**
	 *	Valida se o assinante recebeu o bonus one-shot.
	 *
	 *	@param		assinante				Informacoes do cadastro do assinante na campanha.
	 *	@param		conexaoPrep				Conexao com o banco de dados.
	 *	@throws		Exception
	 */
	private void validarRecebimento(AssinanteCampanha assinante, PREPConexao conexaoPrep) throws Exception
	{
		String	tipoTransacao	= this.campanha.getTipoTransacao();
		Date	dataSubidaTSD	= this.infosBonificacao.getDataSubidaTSD();
		
		//Verificando se o assinante ja recebeu o bonus. Neste caso, e necessario atualizar o status do assinante
		//para bonus concedido e retira-lo da campanha.
		if((this.infosBonificacao.getCodStatus() != NPGStatusBonificacao.BONUS_CONCEDIDO) &&
           (NatalPagueGanheDAO.assinanteRecebeuBonus(this.infosBonificacao, tipoTransacao, dataSubidaTSD, conexaoPrep)))
		{
			int codStatus = NPGStatusBonificacao.BONUS_CONCEDIDO;
			this.infosBonificacao.setCodStatus(codStatus);
			this.infosBonificacao.setDescStatus(NPGStatusBonificacao.getDescricao(codStatus));
			MapCodigosRetorno mapRetorno = MapCodigosRetorno.getInstance();
			int codRetorno = Definicoes.RET_OPERACAO_OK;
			this.infosBonificacao.setCodRetorno(codRetorno);
			this.infosBonificacao.setDescRetorno(mapRetorno.getRetorno(codRetorno).getDescRetorno());
			assinante.setParametros(this.infosBonificacao.toMap());
			AssinanteCampanhaDAO.atualizarParamsAssinanteCampanha(assinante, conexaoPrep);
		}
	}
	
	/**
	 *	Valida se o assinante efetuou o montante minimo em recargas para recebimento do bonus one-shot.
	 *
	 *	@param		assinante				Informacoes do cadastro do assinante na campanha.
	 *	@param		conexaoPrep				Conexao com o banco de dados.
	 *	@throws		Exception
	 */
	public void validarRecargas(AssinanteCampanha assinante, PREPConexao conexaoPrep) throws Exception
	{
		MapCodigosRetorno mapRetorno = MapCodigosRetorno.getInstance();
		//Verificando se o assinante realizou o montante minimo em recargas. Esta verificacao depende do periodo
		//de espera para liberacao do bonus. Se, apos o termino do periodo, o assinante nao tiver realizado a
		//recarga, deve ser retirado da campanha sem receber o bonus. 
		//Para evitar a nao bonificacao no caso de o assinante realizar a recarga antes da subida do TSD, a 
		//verificacao deve truncar a data de subida.
		MapConfiguracaoGPP mapConfiguracao = MapConfiguracaoGPP.getInstance();
		double valorMinimo = Double.parseDouble(mapConfiguracao.getMapValorConfiguracaoGPP("CAMPANHA_NPG_MINIMO_RECARGAS"));
		java.sql.Date dataSubidaTSD = new java.sql.Date(this.infosBonificacao.getDataSubidaTSD().getTime());
		if(NatalPagueGanheDAO.assinanteRealizouRecarga(this.infosBonificacao, valorMinimo, dataSubidaTSD, conexaoPrep))
		{
			int codStatus = NPGStatusBonificacao.RECARGA_EFETUADA; 
			this.infosBonificacao.setCodStatus(codStatus);
			this.infosBonificacao.setDescStatus(NPGStatusBonificacao.getDescricao(codStatus));
			int codRetorno = Definicoes.RET_OPERACAO_OK;
			this.infosBonificacao.setCodRetorno(codRetorno);
			this.infosBonificacao.setDescRetorno(mapRetorno.getRetorno(codRetorno).getDescRetorno());
		}
		else if(this.isFimEspera())
		{
			int codStatus = NPGStatusBonificacao.NAO_VALIDADO;
			this.infosBonificacao.setCodStatus(codStatus);
			this.infosBonificacao.setDescStatus(NPGStatusBonificacao.getDescricao(codStatus));
			int codRetorno = Definicoes.RET_RECARGA_NAO_REALIZADA;
			this.infosBonificacao.setCodRetorno(codRetorno);
			this.infosBonificacao.setDescRetorno(mapRetorno.getRetorno(codRetorno).getDescRetorno());
		}
		
		//Atualizando os parametros do assinante na campanha e retirando o assinante da campanha, caso necessario.
		assinante.setParametros(this.infosBonificacao.toMap());
		AssinanteCampanhaDAO.atualizarParamsAssinanteCampanha(assinante, conexaoPrep);
		if(this.infosBonificacao.getCodStatus() == NPGStatusBonificacao.NAO_VALIDADO)
			AssinanteCampanhaDAO.retiraAssinante(assinante);
	}
	
	/**
	 *	Valida se o assinante esta cadastrado em uma promocao Pula-Pula valida para recebimento do bonus one-shot.
	 *
	 *	@param		assinante				Informacoes do cadastro do assinante na campanha.
	 *	@param		conexaoPrep				Conexao com o banco de dados.
	 *	@throws		Exception
	 */
	public void validarPulaPula(AssinanteCampanha assinante, PREPConexao conexaoPrep) throws Exception
	{
		MapCodigosRetorno mapRetorno = MapCodigosRetorno.getInstance();
		
		//Passado o periodo de espera, deve ser feita a verificacao se o assinante foi cadastrado em uma promocao 
		//Pula-Pula valida. Caso contrario, o status do assinante deve ser alterado para pendente. 
		ControlePulaPula controle = new ControlePulaPula(conexaoPrep.getIdProcesso());
		if(this.isCadastradoPulaPula(controle.consultaPromocaoPulaPula(assinante.getMsisdn(), 
																	   null, 
																	   null, 
																	   false,
																	   conexaoPrep)))
		{
			int codStatus = NPGStatusBonificacao.BONUS_LIBERADO; 
			this.infosBonificacao.setCodStatus(codStatus);
			this.infosBonificacao.setDescStatus(NPGStatusBonificacao.getDescricao(codStatus));
			int codRetorno = Definicoes.RET_OPERACAO_OK;
			this.infosBonificacao.setCodRetorno(codRetorno);
			this.infosBonificacao.setDescRetorno(mapRetorno.getRetorno(codRetorno).getDescRetorno());
			assinante.setParametros(this.infosBonificacao.toMap());
			AssinanteCampanhaDAO.atualizarParamsAssinanteCampanha(assinante, conexaoPrep);
		}
		else if(this.infosBonificacao.getCodStatus() != NPGStatusBonificacao.PENDENTE_PULA_PULA)
		{
			int codStatus = NPGStatusBonificacao.PENDENTE_PULA_PULA; 
			this.infosBonificacao.setCodStatus(codStatus);
			this.infosBonificacao.setDescStatus(NPGStatusBonificacao.getDescricao(codStatus));
			int codRetorno = Definicoes.RET_PROMOCAO_ASSINANTE_NAO_EXISTE;
			this.infosBonificacao.setDescRetorno(mapRetorno.getRetorno(codRetorno).getDescRetorno());
			assinante.setParametros(this.infosBonificacao.toMap());
			AssinanteCampanhaDAO.atualizarParamsAssinanteCampanha(assinante, conexaoPrep);
		}
	}
	
	//Outros metodos.
	
	/**
	 *	Retorna o numero de dias passados desde a subida de TSD do assinante que resultou em seu cadastro na campanha.
	 *	As datas de subida de TSD e atual sao truncadas.
	 *
	 *	@return		Numero de dias passados desde a subida de TSD. 
	 */
	private int getDias(Date dataSubidaTSD)
	{
		long ini = new java.sql.Date(dataSubidaTSD.getTime()).getTime();
		long fim = new java.sql.Date(Calendar.getInstance().getTimeInMillis()).getTime();
		
		return new Double((fim - ini)/86400000).intValue();
	}
	
	/**
	 *	Retorna se o periodo de espera para recebimento do bonus one-shot terminou.
	 *
	 *	@return		True se o periodo terminou e false caso contrario.
	 *	@throws		Exception 
	 */
	private boolean isFimEspera() throws Exception
	{
		MapConfiguracaoGPP mapConfiguracao = MapConfiguracaoGPP.getInstance();
		int diasEspera = Integer.parseInt(mapConfiguracao.getMapValorConfiguracaoGPP("CAMPANHA_NPG_DIAS_ESPERA"));
		return (this.getDias(this.infosBonificacao.getDataSubidaTSD()) > diasEspera);
	}
	
	/**
	 *	Retorna se o assinante esta cadastrado em um promocao Pula-Pula valida para recebimento do bonus one-shot.
	 *
	 *	@param		assinante				Informacoes referentes a promocao Pula-Pula do assinante.
	 *	@return		True se o assinante esta cadastrado e false caso contrario.
	 *	@throws		Exception 
	 */
	private boolean isCadastradoPulaPula(AssinantePulaPula assinante) throws Exception
	{
		MapConfiguracaoGPP	mapConfiguracao	= MapConfiguracaoGPP.getInstance();
		MapPromocao			mapPromocao		= MapPromocao.getInstancia();
		
		if(assinante == null)
			return false;
		
		String[] promocoes = mapConfiguracao.getMapValorConfiguracaoGPP("CAMPANHA_NPG_PROMOCOES_PERMITIDAS").split(";");
		for(int i = 0; i < promocoes.length; i++)
		{
			Promocao promocao = mapPromocao.getPromocao(Integer.parseInt(promocoes[i]));
			if((promocao != null) && (promocao.equals(assinante.getPromocao())))
				return true;
		}
		
		return false;
	}
	
}

