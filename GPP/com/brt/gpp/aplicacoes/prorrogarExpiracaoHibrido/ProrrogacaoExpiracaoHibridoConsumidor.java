package com.brt.gpp.aplicacoes.prorrogarExpiracaoHibrido;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.tecnomen.TecnomenAprovisionamento;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
 * @author Marcos C. Magalhaes 
 *
 */
public class ProrrogacaoExpiracaoHibridoConsumidor extends Aplicacoes implements ProcessoBatchConsumidor
{
    private SimpleDateFormat sdf;
    private TecnomenAprovisionamento TA;

	/**
	 * Metodo....:EnvioUsuariosShutdownProdutor
	 * Descricao.:Construtor da classe do processo batch
	 */
	public ProrrogacaoExpiracaoHibridoConsumidor()
	{
		super(GerentePoolLog.getInstancia(ProrrogacaoExpiracaoHibridoConsumidor.class).getIdProcesso(Definicoes.CL_PRORROGACAO_EXPIRACAO_HIBRIDO)
		     ,Definicoes.CL_PRORROGACAO_EXPIRACAO_HIBRIDO);
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#startup()
	 */
	public void startup() throws Exception
	{
		sdf = new SimpleDateFormat("dd/MM/yyyy");
		TA = new TecnomenAprovisionamento();
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#startup(com.brt.gpp.comum.produtorConsumidor.Produtor)
	 */
	public void startup(Produtor produtor) throws Exception
	{
		startup();
	}
	
	public void startup(ProcessoBatchProdutor produtor) throws Exception
	{
		startup();
	}

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#execute(java.lang.Object)
	 */
	public void execute(Object obj) throws Exception
	{
		// Faz o cast do objeto para o ValueObject desejado
		// apos isso realiza o insert na tabela de interface
		// armazenando o xml construido a partir dessas informacoes
		
		UsuariosHibridosAtivadosVO usrHibridoAtivados = (UsuariosHibridosAtivadosVO)obj;
		try
		{
			Calendar novaData = Calendar.getInstance();
			novaData.setTime(usrHibridoAtivados.getData());
			novaData.add(Calendar.DAY_OF_MONTH, usrHibridoAtivados.getNumDias());
			Date dataExp = novaData.getTime();
			SimpleDateFormat conversorDate = new SimpleDateFormat(Definicoes.MASCARA_DATE);
			TA.atualizarStatusAssinante(usrHibridoAtivados.getMsisdn(), 
										(short)-1, 
										null, 
										dataExp, 
										dataExp, 
										dataExp, 
										dataExp);
	
			super.log(Definicoes.DEBUG,"Consumidor.execute","Usuario "+usrHibridoAtivados.getMsisdn()+ "teve prazo prorrogado para " + conversorDate.format(dataExp));
		}
		catch(Exception e)
		{
		    super.log(Definicoes.WARN, "Consumidor.execute", "Erro no processamento do assinante " + usrHibridoAtivados.getMsisdn()+" Erro:"+e);
		}
	}
	
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#finish()
	 */
	public void finish()
	{
	}
}
