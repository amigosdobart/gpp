package com.brt.gpp.aplicacoes.promocao.gerenteFaleGratis;

import java.util.Calendar;
import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.aplicacoes.promocao.controle.ControlePromocao;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoStatusAssinante;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
 * Classe responsavel por executar o processo de inclusao do 
 * plano de preco do Fale Gratis
 * 
 * @author João Paulo Galvagni
 * @since  01/05/2007
 */
public class GerInsercaoFaleGratisConsumidor extends Aplicacoes
			 implements ProcessoBatchConsumidor
{
	private PREPConexao 	 conexaoPrep 	  = null;
	private GerFaleGratisDAO dao		 	  = null;
	private ControlePromocao controlePromocao = null;
	/**
	 * Construtor da Classe
	 *
	 */
	public GerInsercaoFaleGratisConsumidor()
	{
		super(GerentePoolLog.getInstancia(GerInsercaoFaleGratisConsumidor.class).getIdProcesso(Definicoes.CL_GER_INSERCAO_FALE_GRATIS_CONS),
				Definicoes.CL_GER_INSERCAO_FALE_GRATIS_CONS);
	}
	
	/**
	 * Metodo....: startup
	 * Descricao.: Inicializa o objeto
	 * 
	 * @param produtor - Produtor
	 */
	public void startup(ProcessoBatchProdutor produtor) throws Exception
	{
		conexaoPrep = gerenteBancoDados.getConexaoPREP(getIdLog());
		startup();
	}
	
	/**
	 * Metodo....: startup
	 * Descricao.: Inicializa o objeto
	 * 
	 * @param produtor - Classe produtora
	 */
	public void startup(Produtor produtor) throws Exception
	{
		startup((ProcessoBatchProdutor) produtor);
	}
	
	/**
	 * Metodo....: startup
	 * Descricao.: Cria uma nova instancia do DAO
	 * 
	 */
	public void startup() throws Exception
	{
		dao = new GerFaleGratisDAO(getIdLog());
		controlePromocao = new ControlePromocao(getIdLog());
	}
	
	/**
	 * Metodo....: execute
	 * Descricao.: Metodo responsavel pela execucao de cada assinantes
	 * 			   que sera removido da Promocao Fale Gratis
	 * 
	 * @param obj	- Objeto contendo as informacoes para processar
	 */
	public void execute(Object obj) throws Exception
	{
		GerFaleGratisVO vo = (GerFaleGratisVO) obj;
		conexaoPrep.setAutoCommit(false);
		short retorno = Definicoes.RET_OPERACAO_OK;
		
		// Antes da insercao do assinante na promocao Fale Gratis, tem que se 
		// considerar o status da tabela TBL_PRO_ASSINANTE, seguindo a seguinte regra:
		// Status == Limite Alcancado (5):
		//		Atualizar o status do assinante para ATIVO (0)
		// 		Alterar o Plano de Preco do assinante para o FGN
		// Status == Limite Alcancado e Recarga Expirada (6):
		// 		Atualizar o status do assinante para Recarga Expirada (4)
		if (PromocaoStatusAssinante.STATUS_SUSPENSO_LIMITE_ALCANCADO == vo.getStatusPromocao())
		{
			// Cria uma nova instancia de Assinante para o processo de troca
			// de plano Pula-Pula para o Fale Gratis
			Assinante assinante = new Assinante();
			assinante.setMSISDN(vo.getMsisdn());
			assinante.setPlanoPreco((short)vo.getIdPlanoPulaPula());
			
			String tipoEspelhamento = Definicoes.CTRL_PROMOCAO_TIPO_ESPELHAMENTO_FGN_RETORNO + vo.getIdPromocao();
			
			log(Definicoes.DEBUG, "execute", "Migrando o assinante " + vo.getMsisdn() + 
											 " para o Plano Preco do FGN");
			
			// Nao havera validacoes a serem realizadas para o assinante, o 
			// mesmo deve apenas retornar a promocao Fale Gratis o qual pertencia
			// antes de ultrapassar o Limite Maximo da promocao
			retorno = controlePromocao.trocarPlanoEspelho(assinante,
														  tipoEspelhamento,
														  (short)-1,
														  Calendar.getInstance().getTime(),
														  conexaoPrep);
			
			// Seta o plano de preco Pula-Pula do objeto vo, para que o mesmo possa
			// ser utilizado na atualizacao da tabela TBL_PRO_TOTALIZACAO_FALEGRATIS
			if (Definicoes.RET_OPERACAO_OK == retorno)
			{
				// Atualiza a tabela TBL_PRO_TOTALIZACAO_FALEGRATIS, setando o campo
				// IDT_PLANO_FALEGRATIS para NULL, o que inviabiliza a selecao desse
				// assinante quando o processo executar novamente
				dao.atualizaRetornoPlanoFaleGratis(vo, conexaoPrep);
				
				// Atualiza o status do assinante na tabela de promocao de
				// acordo com o status atual e o retorno do assinante
				// para a promocao Fale de Graca a Noite
				dao.atualizaStatusAssinanteInsercao(vo, conexaoPrep);
			}
		}
		else
			if (PromocaoStatusAssinante.STATUS_SUSPENSO_LIMITE_ALCANCADO_E_RECARGA_EXPIRADA == vo.getStatusPromocao())
			{
				log(Definicoes.DEBUG, "execute", "Msisdn " + vo.getMsisdn() + " apenas teve seu status atualizado");
				dao.atualizaStatusAssinanteInsercao(vo, conexaoPrep);
			}
		
		// Valida se o retorno das acoes gerou algum erro, caso contrario,
		// o commit eh realizado
		if (retorno == Definicoes.RET_OPERACAO_OK)
			conexaoPrep.commit();
		else
			conexaoPrep.rollback();
	}
	
	/**
	 * Metodo....: finish
	 * Descricao.: Finaliza o necessario do objeto
	 * 
	 */
	public void finish()
	{
		gerenteBancoDados.liberaConexaoPREP(conexaoPrep, getIdLog());
	}
}