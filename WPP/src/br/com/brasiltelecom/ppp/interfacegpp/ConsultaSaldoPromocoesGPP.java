package br.com.brasiltelecom.ppp.interfacegpp;

import org.omg.CORBA.ORB;

import com.brt.gpp.componentes.consulta.orb.consulta;
import com.brt.gpp.componentes.consulta.orb.consultaHelper;
import com.brt.gpp.componentes.consulta.orb.consultaPackage.SaldoBoomerang;
import com.brt.gpp.componentes.consulta.orb.consultaPackage.StatusPulaPula;

/**
 * @author Joao Carlos
 * Data..: 25-Abr-2005
 *
 */
public class ConsultaSaldoPromocoesGPP 
{
	/**
	 * Metodo....:getValorConcedidoBoomerang
	 * Descricao.:Retorna o valor concedido para o assinante relativo a promocao boomerang 14
	 *            no mes desejado.
	 * @param msisdn 	- Msisdn a ser pesquisado
	 * @param mes		- Mes que devera ser utilizado na pesquisa
	 * @param servidor	- Servidor do GPP onde sera realizado a pesquisa
	 * @param porta		- Porta onde o servidor esta respondendo
	 * @return SaldoBoomerang - Objeto contendo o valor do bonus que o assinante recebeu ou recebera no mes desejado
	 *                          junto com um indicador se este fez a recarga no mesmo periodo
	 */
	public static SaldoBoomerang getValorConcedidoBoomerang(String msisdn,int mes, String servidor, String porta) throws Exception
	{
		// Realiza os procedimentos para inicializacao de um objeto CORBA para acesso ao GPP
		ORB orb = GerenteORB.getORB(servidor, porta);
		byte[] managerId = "ComponenteNegociosConsulta".getBytes();
		consulta pPOA = consultaHelper.bind(orb, "/AtivaComponentesPOA", managerId);
		
		// Define variavel que ira armazenar o valor concedido pela promocao
		SaldoBoomerang valorSaldo = null;
		try
		{
			valorSaldo = pPOA.consultaSaldoBoomerang(msisdn,mes);
		}
		catch (Exception e) 
		{
			throw new Exception("Erro ao conectar no GPP. Erro:"+e);
		}
		return valorSaldo;
	}
	
	/**
	 * Metodo....:getValorSaldoPulaPula
	 * Descricao.:Retorna o valor do saldo pula pula que o assinante recebeu em um determinado mes
	 * @param msisdn		- Msisdn do assinante
	 * @param mes			- Mes a ser utilizado para a pesquisa
	 * @param servidor		- Servidor do GPP onde sera consultado
	 * @param porta			- Porta onde o servidor GPP esta respondendo
	 * @return	double		- Valor do saldo pula pula do assinante para o mes escolhido
	 * @throws Exception
	 */
	public static double getValorSaldoPulaPula(String msisdn, int mes, String servidor, String porta) throws Exception
	{
		// Realiza os procedimentos para inicializacao de um objeto CORBA para acesso ao GPP
		ORB orb = GerenteORB.getORB(servidor, porta);
		byte[] managerId = "ComponenteNegociosConsulta".getBytes();
		consulta pPOA = consultaHelper.bind(orb, "/AtivaComponentesPOA", managerId);
		
		// Define variavel que ira armazenar o valor concedido pela promocao
		double valorSaldo = 0;
		try
		{
			// A API do GPP para a consulta de saldo pulapula multiplica o valor do saldo por 1000 
			// para repassar o valor decimal, portanto o valor retornado e convertido para double
			// e divido por 1000 para retornar o valor correto nesse formato
			valorSaldo = Double.parseDouble(pPOA.consultaSaldoPulaPulaNoMes(msisdn,mes))/1000;
		}
		catch (Exception e) 
		{
			throw new Exception("Erro ao conectar no GPP. Erro:"+e);
		}
		return valorSaldo;
	}
	
	/**
	 * Metodo....:getStatusPulaPula
	 * Descricao.:Retorna um objeto representando a consulta da Promoção Pula-Pula do WPP executada pelo GPP.
	 * @param 	String				msisdn		- Msisdn do assinante
	 * @param 	String				servidor	- Servidor do GPP onde sera consultado
	 * @param 	String				porta		- Porta onde o servidor GPP esta respondendo
	 * @return	StatusPulaPula					- Objeto representando a consulta da Promoção Pula-Pula.
	 * @throws Exception
	 *--@deprecated	Utilizar metodo do ConsultaPromocaoPulaPulaGPP.
	 */
	public static StatusPulaPula getStatusPulaPula(String msisdn, String servidor, String porta) throws Exception
	{
		//Realiza os procedimentos para inicializacao de um objeto CORBA para acesso ao GPP
		//ORB orb = GerenteORB.getORB(servidor, porta);
		//byte[] managerId = "ComponenteNegociosConsulta".getBytes();
		//consulta pPOA = consultaHelper.bind(orb, "/AtivaComponentesPOA", managerId);
		
		// Define variavel que ira armazenar o valor concedido pela promocao
		StatusPulaPula result = null;
		try
		{
			//result = pPOA.consultaStatusPulaPula(msisdn);
		}
		catch (Exception e) 
		{
			throw new Exception("Erro ao conectar no GPP. Erro:"+e);
		}
		
		return result;
	}
	
}
