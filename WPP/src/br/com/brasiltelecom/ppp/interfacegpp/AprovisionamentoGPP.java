/*
 * Created on 11/05/2005
 *
 */
package br.com.brasiltelecom.ppp.interfacegpp;

import org.omg.CORBA.ORB;

import com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamento;
import com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoHelper;

/**
 * Efetua a conexão com o GPP a fim de executar operacoes de aprovisionamento a partir do portal. 
 * 
 * OBS: Os metodos serao implementados conforme a demanda. Atualmente (data de criacao), somente a operacao de troca
 * de plano e necessaria no portal devido a necessidade de troca de promocao de assinante.
 * 
 * @author Daniel Ferreira
 * @since 11/05/2005
 */
public class AprovisionamentoGPP 
{

	/**
	 * Troca o plano do assinante
	 * 
	 * @param	String						msisdn						MSISDN do assinante
	 * @param	String						novoPlano					Novo plano do assinante
	 * @param	double						valorMudanca				Valor cobrado pela troca de plano
	 * @param	String						operador					Matricula do operador
	 * @param	double						valorFranquia				Valor da Franquia a ser cobrada para o novo plano
	 * @param 	String						servidor 					Endereco do servidor
	 * @param 	String						porta 						Porta do servidor
	 * @return	short													Codigo de retorno
	 * @throws	Exception
	 */
	public static short trocaPlano(String msisdn, String novoPlano, double valorMudanca, String operador, 
								   double valorFranquia, String servidor, String porta) 
		throws Exception
	{		
		short result = -1;
			
		ORB orb = GerenteORB.getORB(servidor, porta);
		byte[] managerId = "ComponenteNegociosAprovisionamento".getBytes();
		aprovisionamento pPOA = aprovisionamentoHelper.bind(orb, "/AtivaComponentesPOA", managerId);
		
		try
		{
			result = pPOA.trocaPlanoAssinante(msisdn, novoPlano, valorMudanca, operador, valorFranquia);
		}
		catch (Exception e) 
		{
			throw new Exception("Erro ao conectar no GPP");
		}
			
		return result;
	}
	
	/** alteraStatusAssinante
	 * Troca o status do assinante
	 * @param msisdn MSISDN do assinante
	 * @param status Novo Status
	 * @param dataExpiracao Data De Expiracao
	 * @param operador Matricula do operada
	 * @param servidor Endereco do servidor
	 * @param porta Porta do servidor
	 * @return Codigo de retorno
	 * @throws Exception
	 */
	public static short alteraStatusAssinante(String msisdn, short status, String dataExpiracao, String operador, String servidor, String porta)
	throws Exception
	{
		short result = -1;
		
		ORB orb = GerenteORB.getORB(servidor, porta);
		byte[] managerId = "ComponenteNegociosAprovisionamento".getBytes();
		aprovisionamento pPOA = aprovisionamentoHelper.bind(orb, "/AtivaComponentesPOA", managerId);

		try
		{
			result = pPOA.alterarStatusAssinante(msisdn, status, dataExpiracao, operador);
		}
		catch (Exception e) 
		{
			throw new Exception("Erro ao conectar no GPP");
		}
		
		return result;
	}
	
	/** alteraStatusPeriodico
	 * Troca o status do saldo periódico
	 * @param msisdn MSISDN do assinante
	 * @param status Novo Status
	 * @param dataExpiracao Data De Expiracao
	 * @param operador Matricula do operada
	 * @param servidor Endereco do servidor
	 * @param porta Porta do servidor
	 * @return Codigo de retorno
	 * @throws Exception
	 */
	public static short alteraStatusPeriodico(String msisdn, short status, String dataExpiracao, String operador, String servidor, String porta)
	throws Exception
	{
		short result = -1;
		
		ORB orb = GerenteORB.getORB(servidor, porta);
		byte[] managerId = "ComponenteNegociosAprovisionamento".getBytes();
		aprovisionamento pPOA = aprovisionamentoHelper.bind(orb, "/AtivaComponentesPOA", managerId);

		try
		{
			result = pPOA.alterarStatusPeriodico(msisdn, status, dataExpiracao, operador);
		}
		catch (Exception e) 
		{
			throw new Exception("Erro ao conectar no GPP");
		}
		
		return result;
	}
	
	/** modificaDadoAssinante
	 * Troca o status de servico do assinante
	 * 
	 * @param	String		msisdn			MSISDN do assinante
	 * @param	int			tipoDado		tipo de dado a ser alterado
	 * @param	String		novoDado		Valor novo
	 * @param	String		operador		Matricula do operador
	 * @param 	String		servidor 		Endereco do servidor
	 * @param 	String		porta 			Porta do servidor
	 * @return	short						Codigo de retorno
	 * @throws	Exception
	 */
	public static short modificaDadoAssinante(String msisdn, int tipoDado, String novoDado, String operador, String servidor, String porta) 
		throws Exception
	{		
		short result = -1;
			
		ORB orb = GerenteORB.getORB(servidor, porta);
		byte[] managerId = "ComponenteNegociosAprovisionamento".getBytes();
		aprovisionamento pPOA = aprovisionamentoHelper.bind(orb, "/AtivaComponentesPOA", managerId);

		try
		{
			// O método modificaDadosAssinante foi substituido no GPP 
			// pelos métodos: desbloqueiaAssinante e bloqueiaAssinante. 
			result = pPOA.desbloqueiaAssinante(msisdn, operador);
		}
		catch (Exception e) 
		{
			throw new Exception("Erro ao conectar no GPP");
		}
			
		return result;
	}
	
	/**
	 *	Troca a promocao Pula-Pula do assinante.
	 * 
	 *	@param	String						msisdn						MSISDN do assinante
	 *	@param	int							promocao					Identificador da nova promocao Pula-Pula do assinante.
	 *	@param	String						operador					Matricula do operador
	 *	@param	int							motivo						Motivo da operacao
	 *	@param	int							tipoDocumento				Identificador do tipo do documento
	 *	@param	String						numDocumento				Numero do documento
	 *	@param 	String						servidor 					Endereco do servidor
	 *	@param 	String						porta 						Porta do servidor
	 *	@return	short						result						Codigo de retorno
	 *	@throws	Exception
	 */
	public static short trocaPulaPulaPPP(String msisdn, int promocao, String operador, int motivo, int tipoDocumento, String numDocumento, String servidor, String porta) throws Exception
	{
	    short result = -1;
	    
		ORB orb = GerenteORB.getORB(servidor, porta);
		byte[] managerId = "ComponenteNegociosAprovisionamento".getBytes();
		aprovisionamento pPOA = aprovisionamentoHelper.bind(orb, "/AtivaComponentesPOA", managerId);
		
		try
		{
		    result = pPOA.trocaPulaPulaPPP(msisdn, promocao, operador, motivo, tipoDocumento, numDocumento);		
		}
		catch (Exception e) 
		{
			throw new Exception("Erro ao conectar no GPP");
		}
		
		return result;
	}
	
	/**
	 * Metodo....: atualizaAmigosTodaHora
	 * Descricao.: Realiza a atualizacao da lista de Amigos Toda Hora
	 * 			   do referido assinante no GPP - Apenas Pre-pago
	 * @since....: 21/08/2006
	 * @author...: JOAO PAULO GALVAGNI
	 * 
	 * @param msisdn		- MSISDN do assinante
	 * @param listaMsisdn	- Lista dos MSISDN no formato (6184011111;6133611111)
	 * @param codigoServico	- Codigo do servico
	 * @param operacao		- C: Consulta, D: Debito ou E: Estorno
	 * @param operador		- Solicitante da atualizacao
	 * @param servidor		- Servidor para conexao com o GPP
	 * @param porta			- Porta para conexao com o GPP
	 * @return result		- XML contendo os dados apos a atualizacao
	 * @throws Exception
	 */
	public static String atualizaAmigosTodaHora (String msisdn, String listaMsisdn, String codigoServico, String operacao, String operador, String servidor, String porta) throws Exception
	{
	    String result = "";
	    
		ORB orb = GerenteORB.getORB(servidor, porta);
		byte[] managerId = "ComponenteNegociosAprovisionamento".getBytes();
		aprovisionamento pPOA = aprovisionamentoHelper.bind(orb, "/AtivaComponentesPOA", managerId);
		
		try
		{
			result = pPOA.atualizarAmigosTodaHora(msisdn, listaMsisdn, codigoServico, operacao, operador);
		}
		catch (Exception e) 
		{
			throw new Exception("Erro ao conectar no GPP");
		}
		
		return result;
	}
	
	/**
	 * Metodo....: cadastraBumerangue
	 * Descricao.: Realiza o cadastramento da promocao 
	 * 			   Bumerangue14 para o referido assinante
	 * @since....: 21/08/2006
	 * @author...: JOAO PAULO GALVAGNI
	 * 
	 * @param msisdn		- MSISDN do assinante
	 * @param codigoServico	- Codigo do servico
	 * @param operacao		- C: Consulta, D: Debito ou E: Estorno
	 * @param operador		- Solicitante do cadastramento
	 * @param servidor		- Servidor para conexao com o GPP
	 * @param porta			- Porta para conexao com o GPP
	 * @return returno		- XML contendo os dados apos o cadastro
	 * @throws Exception
	 */
	public static String cadastraBumerangue(String msisdn, String codigoServico, String operacao, String operador, String servidor, String porta) throws Exception
	{
	    String result = "";
	    
		ORB orb = GerenteORB.getORB(servidor, porta);
		byte[] managerId = "ComponenteNegociosAprovisionamento".getBytes();
		aprovisionamento pPOA = aprovisionamentoHelper.bind(orb, "/AtivaComponentesPOA", managerId);
		
		try
		{
			result = pPOA.cadastrarBumerangue(msisdn, codigoServico, operacao, operador);
		}
		catch (Exception e) 
		{
			throw new Exception("Erro ao conectar no GPP");
		}
		
		return result;
	}
}
