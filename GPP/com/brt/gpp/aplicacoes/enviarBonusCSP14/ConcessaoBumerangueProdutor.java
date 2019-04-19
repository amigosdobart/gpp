package com.brt.gpp.aplicacoes.enviarBonusCSP14;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.MapCodigoNacional;
import com.brt.gpp.comum.mapeamentos.entidade.CodigoNacional;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;

public class ConcessaoBumerangueProdutor extends Aplicacoes implements ProcessoBatchProdutor
{
	private String		statusProcesso  = Definicoes.TIPO_OPER_SUCESSO;
	private PREPConexao	conexaoPrep;
	private Iterator 	listaCodigosNacional;
	private long		numRegistros;
	private String 		dataMes;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	public ConcessaoBumerangueProdutor(long id)
	{
		super(id,Definicoes.CL_ENVIO_BONUS_CSP14);
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getIdProcessoBatch()
	 */
	public int getIdProcessoBatch()
	{
		return Definicoes.IND_BONUS_CSP14;
	}

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getDescricaoProcesso()
	 */
	public String getDescricaoProcesso()
	{
		return "Bonus agendado para "+numRegistros+" assinantes";
	}

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getStatusProcesso()
	 */
	public String getStatusProcesso()
	{
		return statusProcesso;
	}

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#setStatusProcesso(java.lang.String)
	 */
	public void setStatusProcesso(String status)
	{
		statusProcesso = status;
	}

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getDataProcessamento()
	 */
	public String getDataProcessamento()
	{
		return sdf.format(Calendar.getInstance().getTime());
	}

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getConexao()
	 */
	public PREPConexao getConexao()
	{
		return this.conexaoPrep;
	}

	/**
	 * Metodo....:incrementaRegistros
	 * Descricao.:Incrementa o contador para indicar registros processados com sucesso
	 *
	 */
	public void incrementaRegistros()
	{
		numRegistros++;
	}
	
	/**
	 * Metodo....:parseParametros
	 * Descricao.:Este metodo realiza a verificacao de parametros
	 * @param params - Array de valores contendo os parametros para a execucao do processo
	 * @throws GPPInternalErrorException
	 */
	private void parseParametros(String params[]) throws Exception
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		try
		{
			// Para o processo de concessao do bonus bumerangue eh esperado a data mes
			// para qual deve ser realizado a concessao. Esse parametro deve ser uma
			// string no formato YYYYMM onde YYYY = Ano e MM = Mes do periodo
			if (params == null || params.length == 0 ||params[0] == null)
			{
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.MONTH,-1);
				dataMes = sdf.format(cal.getTime());
			}
			else
			{
				sdf.parse(params[0]);
				dataMes = params[0];
			}
		}
		catch(ParseException pe)
		{
			throw new GPPInternalErrorException("Data mes invalida ou esta em formato invalido (yyyyMM). Valor: "+params[0]);
		}
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#startup(java.lang.String[])
	 */
	public void startup(String[] params) throws Exception
	{
		// Realiza o parse do parametro de data afim de identificar para qual mes a totalizacao
		// serah processada. Apos o parse realiza a pesquisa dos assinantes totalizados nessa
		// para para serem processados pela promocao
		parseParametros(params);
		
		// Pega a conexao no pool de conexoes e altera para nao realizar o auto-commit. Essa
		// funcionalidade vai permitir que as threads consumidoras nao realizem o commit sendo
		// entao que apesar de divido o processamento, somente apos o termino de todas eh que
		// o processamento serah efetivado
		conexaoPrep = GerentePoolBancoDados.getInstancia(super.getIdLog()).getConexaoPREP(super.getIdLog());
		conexaoPrep.setAutoCommit(false);
		
		MapCodigoNacional map = MapCodigoNacional.getInstance();
		listaCodigosNacional = map.buscarCodigosRegiaoBrt().iterator();
	}

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#next()
	 */
	public Object next() throws Exception
	{
		ConcessaoBumerangueVO vo = null;
		if (listaCodigosNacional.hasNext())
		{
			CodigoNacional codNacional = (CodigoNacional)listaCodigosNacional.next();
			vo = new ConcessaoBumerangueVO();
			vo.setCn	(codNacional);
			vo.setDatMes(this.dataMes);
		}
		
		return vo;
	}

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#finish()
	 */
	public void finish() throws Exception
	{
		// No finish do produtor, entao todas as threads jah realizaram
		// o processamento. Portanto nesse ponto os dados inseridos sao
		// efetivados
		conexaoPrep.commit();
		GerentePoolBancoDados.getInstancia(super.getIdLog()).liberaConexaoPREP(conexaoPrep,super.getIdLog());
	}

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#handleException()
	 */
	public void handleException()
	{
		// Caso algum erro aconteca entao o handleException realiza desfaz
		// os dados inseridos
		try
		{
			conexaoPrep.rollback();
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO,"handleException","Erro ao desfazer transacao de dados da concessao do bumerangue. Erro:"+e);
		}
	}

}
