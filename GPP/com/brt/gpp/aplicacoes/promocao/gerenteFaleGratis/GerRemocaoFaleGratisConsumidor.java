package com.brt.gpp.aplicacoes.promocao.gerenteFaleGratis;

import java.util.Date;
import java.util.Iterator;
import java.util.TreeSet;
import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.aplicacoes.promocao.controle.ControlePromocao;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoLimiteSegundos;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.MapPromocaoLimiteSegundos;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
 * Classe responsavel por executar o processo de remocao do 
 * plano de preco do Fale Gratis, alterando o mesmo para a
 * promocao pula pula vigente
 * 
 * @author João Paulo Galvagni
 * @since  01/05/2007
 */
public class GerRemocaoFaleGratisConsumidor extends Aplicacoes
			 implements ProcessoBatchConsumidor
{
	private PREPConexao		  conexaoPrep		= null;
	private GerFaleGratisDAO  dao				= null;
	MapPromocaoLimiteSegundos mapLimiteSegundos = null;
	private ControlePromocao  controlePromocao 	= null;
	
	/**
	 * Construtor da Classe
	 *
	 */
	public GerRemocaoFaleGratisConsumidor()
	{
		super(GerentePoolLog.getInstancia(GerRemocaoFaleGratisConsumidor.class).getIdProcesso(Definicoes.CL_GER_REMOCAO_FALE_GRATIS_CONS),
				Definicoes.CL_GER_REMOCAO_FALE_GRATIS_CONS);
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
	 * Descricao.: Seta as instancias necessarias da classe
	 * 
	 */
	public void startup() throws Exception
	{
		dao = new GerFaleGratisDAO(getIdLog());
		mapLimiteSegundos = MapPromocaoLimiteSegundos.getInstance();
		// Cria uma nova instancia do ControlePromocao, classe responsavel por alterar
		// o Plano do Assinante que ultrapassou o limite maximo da promocao Fale Gratis
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
		GerFaleGratisVO vo = (GerFaleGratisVO)obj;
		
		// Validacoes a serem realizadas para a troca de promocao Fale Gratis para Pula-Pula
		// - Quantidade de segundos (minutos) eh igual ou superior ao valor maximo estipulado
		// ACAO: Trocar a promocao FaleGratis pela PulaPula vigente e enviar uma mensagem de texto
		//		 informando o assinante da troca
		// - Qtde de segundos eh igual ou superior a algum limite parcial
		// ACAO: Enviar uma mensagem de texto informando o assinante que o mesmo atingiu o
		// 		 limite parcial, informando inclusive o limite maximo de sua promocao
		PromocaoLimiteSegundos limite = getLimiteAlcancado(vo);
		conexaoPrep.setAutoCommit(false);
		short retorno = Definicoes.RET_OPERACAO_OK;
		
		log(Definicoes.DEBUG, "execute", "Validando o limite para o assinante " + vo.getMsisdn());
		if ( limite != null && limite.isLimiteMaximo() )
		{
			log(Definicoes.DEBUG, "execute", "Assinante " + vo.getMsisdn() + 
											 " ultrapassou o limide MAXIMO de " +
											 getLimiteEmMinutos(mapLimiteSegundos.
											 getLimiteMaximo(limite.getPromocao().getIdtPromocao())) +
											 " minutos");
			
			// Cria uma nova instancia de Assinante para o processo de troca
			// de plano Fale Gratis para o Pula-Pula vigente
			Assinante assinante = new Assinante();
			assinante.setMSISDN(vo.getMsisdn());
			assinante.setPlanoPreco((short)vo.getIdPlanoFaleGratis());
			
			String tipoEspelhamento = Definicoes.CTRL_PROMOCAO_TIPO_ESPELHAMENTO_FGN + vo.getIdPromocao();
			
			// Realiza uma chamada ao metodo de Troca de Plano, utilizando para tanto
			// o controle pelo Espelhamento, que eh o que define para qual plano o 
			// assinante deve ser 'migrado'. O processo de troca de plano espelho ja
			// realiza a atualizacao do objeto Assinante com o novo Plano de Preco
			retorno = controlePromocao.trocarPlanoEspelho(assinante,
														  tipoEspelhamento,
														  (short)-1,
														  new Date(),
														  conexaoPrep);
			
			// Seta o plano de preco Pula-Pula do objeto vo, para que o mesmo possa
			// ser utilizado na atualizacao da tabela TBL_PRO_TOTALIZACAO_FALEGRATIS
			if (Definicoes.RET_OPERACAO_OK == retorno)
			{
				// Apos a troca do plano do assinante, sera verificado o status atual
				// na tabela TBL_PRO_ASSINANTE e aplicada a seguinte regra:
				// Se status = 0 (Normal)
				// 				:Atualizar o status do assinante para 5 (Limite Alcancado)
				// Se status = 4 (Recarga Expirada)
				// 				:Atualizar o status do assinante para 6 (Limite Alcancado e Recarga Expirada)
				dao.atualizaStatusAssinanteRemocao(vo, conexaoPrep);
				
				vo.setIdPlanoPulaPula((int)assinante.getPlanoPreco());
				vo.setIdLimiteSMS(limite.getIdLimite());
				
				// Atualiza a tabela TBL_PRO_TOTALIZACAO_FALEGRATIS com as informacoes
				// da mensagem (SMS) e da troca do plano Fale Gratis para o Pula-Pula
				dao.atualizaTrocaPlanoFaleGratis(vo, conexaoPrep);
				
				// Solicita a gravacao da mensagem de SMS para envio ao assinante,
				// avisando-o de que o mesmo atingiu o limite maximo da promocao
				dao.gravaSMSLimiteUltrapassado(vo.getMsisdn(), getMsgLimiteMaxAlcancado(limite), Definicoes.LIMITE_FGN_MAXIMO_ALCANCADO);
			}
		}
		else
			if (limite != null)
			{
				vo.setIdLimiteSMS(limite.getIdLimite());
				
				log(Definicoes.DEBUG, "execute", "Assinante " + vo.getMsisdn() +
												 " ultrapassou algum limite Parcial (" + 
												 getLimiteEmMinutos(vo.getNumSegundosTotalizados()) + ")");
				
				// Solicita a gravacao da mensagem de SMS para envio ao assinante,
				// avisando-o de que o mesmo atingiu um limite parcial
				dao.gravaSMSLimiteUltrapassado(vo.getMsisdn(), getMsgLimiteParcialAlcancado(limite), Definicoes.LIMITE_FGN_PARCIAL_ALCANCADO);
				
				// Atualiza a tabela TBL_PRO_TOTALIZACAO_FALEGRATIS
				// com a informacao do ID da mensagem de SMS
				dao.atualizaIdLimiteSMS(vo, conexaoPrep);
			}
		
		if (retorno == Definicoes.RET_OPERACAO_OK)// Realiza o commit das acoes do Banco de Dados
			conexaoPrep.commit();
		else
			conexaoPrep.rollback();
	}
	
	/**
	 * Metodo....: getLimiteAlcancado
	 * Descricao.: Retorna o limite que foi alcancado
	 * 
	 * @param  vo			- Objeto com as informacoes do assinante
	 * @return retorno		- Objeto com as informacoes do Limite Alcancado
	 * @throws GPPInternalErrorException
	 */
	private PromocaoLimiteSegundos getLimiteAlcancado(GerFaleGratisVO vo) throws GPPInternalErrorException
	{
		TreeSet limitesPromocao = mapLimiteSegundos.getLimitesByIdPromocao(vo.getIdPromocao());
		PromocaoLimiteSegundos limiteAlcancado = null;
		
		// Realiza uma iteracao entre os limites pre-estabelecidos para a promocao
		// do assinante, validando se cada limite foi alcancado e, caso positivo,
		// seta as variaveis de ID e do Limite alcancado com os respectivos
		// valores para posterior envio do SMS de alerta
		for (Iterator i = limitesPromocao.iterator(); i.hasNext(); )
		{
			PromocaoLimiteSegundos promoLimite = (PromocaoLimiteSegundos)i.next();
			// Valida se o tempo atingido pelo assinante eh maior do que o limite 
			if (vo.getNumSegundosTotalizados() >= promoLimite.getNumSegundos() &&
				vo.getIdLimiteSMS() < promoLimite.getIdLimite() )
				limiteAlcancado = promoLimite;
		}
		
		// Retorna o objeto
		return limiteAlcancado;
	}
	
	/**
	 * Metodo....: getMsgLimiteParcialAlcancado
	 * Descricao.: Retorna a mensagem padrao para quando o assinante
	 * 			   ultrapassar algum limite parcial da promocao Fale Gratis
	 * 
	 * @param  promoLimite	 - Detalhes do limite do assinante
	 * @return mensagem		 - Mensagem a ser enviada ao assinante
	 */
	private String getMsgLimiteParcialAlcancado(PromocaoLimiteSegundos promoLimite)
	{
		StringBuffer mensagem = new StringBuffer("BrT Informa: Voce ultrapassou ");
		
		mensagem.append( getLimiteEmMinutos(promoLimite.getNumSegundos()) );
		mensagem.append(" minutos da Promocao Fale de Graca a noite. Seu limite e de ");
		mensagem.append( getLimiteEmMinutos(mapLimiteSegundos.getLimiteMaximo(promoLimite.getPromocao().getIdtPromocao())) ) ;
		mensagem.append(" minutos mensais.");
		
		return mensagem.toString();
	}
	
	/**
	 * Metodo....: getMsgLimiteMaxAtingido
	 * Descricao.: Retorna a mensagem padrao para quando o assinante
	 * 			   atingir o limite maximo de minutos (segundos) da
	 * 			   promocao Fale Gratis
	 * 
	 * @param  promoLimite - Objeto contendo as informacoes do SMS
	 * @return mensagem	   - Mensagem a ser enviada ao assinante
	 */
	private String getMsgLimiteMaxAlcancado(PromocaoLimiteSegundos promoLimite)
	{
		StringBuffer mensagem = new StringBuffer("BrT Informa: Voce atingiu o limite de ");
		
		// Adiciona a mensagem a quantidade de MINUTOS para a promocao
		// Fale Gratis informada.
		mensagem.append( getLimiteEmMinutos(mapLimiteSegundos.getLimiteMaximo(promoLimite.getPromocao().getIdtPromocao())) );
		mensagem.append(" minutos da Promocao Fale de Graca a Noite.");
		mensagem.append(" Faca recarga e aproveite a promocao a partir do proximo mes.");
		
		// Retorno da mensagem montada
		return mensagem.toString();
	}
	
	/**
	 * Metodo....: getLimiteEmMinutos
	 * Descricao.: Retorna o limite de tempo em Minutos
	 * 
	 * @param  limiteSegundos - Limite no formato de segundos
	 * @return int			  - Limite no formato de minutos
	 */
	public int getLimiteEmMinutos(long limiteSegundos)
	{
		// Transforma segundos em minutos para envio ao assinante
		// atraves de SMS
		return (int)limiteSegundos / 60;
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