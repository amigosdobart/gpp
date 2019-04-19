package br.com.brasiltelecom.ppp.interfacegpp;

import org.omg.CORBA.ORB;

import com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamento;
import com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoHelper;
import com.brt.gpp.comum.Definicoes;

/**
 * Efetua a conexão com o GPP para envio de SMS via Tangram
 * 
 * @author Jorge Abreu
 * @since 15/10/2007
 */
public class EnviaRequisicaoTangramGPP 
{
		
	/**
	 * Envia uma requisicao de envio de SMS via Tangram para o GPP
	 * 
	 * @param xml da entidade Requisicao do GPP
	 * @param endereço do servidor
	 * @param porta do servidor
	 * @return Código de retorno (0 = sucesso, 9998 = RET_ERRO_GENERICO_GPP, outros = codigo de retorno Tangram)
	 * @throws Exception
	 */
	public static short enviarRequisicaoTangram(String xml,String servidor,String porta) throws Exception
	{
		ORB orb = GerenteORB.getORB(servidor, porta);
		byte[] managerId = "ComponenteNegociosAprovisionamento".getBytes();
		aprovisionamento pPOA = aprovisionamentoHelper.bind(orb, "/AtivaComponentesPOA", managerId);
		short retorno = Definicoes.RET_ERRO_GENERICO_GPP;
		
		try
		{			
			retorno = pPOA.enviarRequisicaoTangram(xml);
		}
		catch (Exception e) 
		{
			throw new Exception(e.getMessage());
		}

		return retorno;
	}
	
}
