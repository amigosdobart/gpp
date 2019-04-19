/* Created on 29/11/2005
 *
 */
package br.com.brasiltelecom.ppp.interfacegpp;

import org.omg.CORBA.ORB;

import com.brt.gpp.aplicacoes.recarregar.DadosRecarga;
import com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamento;
import com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoHelper;
import com.brt.gpp.componentes.recarga.orb.recarga;
import com.brt.gpp.componentes.recarga.orb.recargaHelper;


/**
 * Efetua a conexão com o GPP a fim de executar operacoes de recarga a partir do portal. 
 * 
 * OBS: Os metodos serao implementados conforme a demanda. Atualmente (data de criacao), somente a operacao de troca
 * de plano e necessaria no portal devido a necessidade de troca de promocao de assinante.
 * 
 * @author Marcos C. Magalhães
 * @since 29/10/2005
 */
public class RecargaGPP 
{

	/**
	 * Troca o status do voucher
	 * 
	 * @param	String						numeroVoucher				Número do voucher
	 * @param	String						statusVoucher				Novo tatus do voucher
	 * @param	double						comentario				    Comentário
	 * @param	String						operador					Matricula do operador
	 * @param 	String						servidor 					Endereco do servidor
	 * @param 	String						porta 						Porta do servidor
	 * @return	short													Codigo de retorno
	 * @throws	Exception
	 */
	public static short alteraStatusVoucher(String numeroVoucher, double statusVoucher, String comentario, String operador, 
								   String servidor, String porta) 
		throws Exception
	{		
		short result = -1;
		
		ORB orb = GerenteORB.getORB(servidor, porta);

		byte[] managerId = "ComponenteNegociosRecarga".getBytes();
		
		recarga pPOA = recargaHelper.bind(orb, "/AtivaComponentesPOA", managerId);
		
		try
		{
			result = pPOA.alteraStatusVoucher(numeroVoucher, statusVoucher, comentario);
		}
		catch (Exception e) 
		{
			throw new Exception("Erro ao conectar no GPP");
		}
			
		return result;
	}
	
	/**
	 * Metodo....: cobraServico
	 * Descricao.: Realiza a cobranca por algum servico 
	 * 			   realizado por acessos Pre-pago
	 * @since....: 17/08/2006
	 * @author...: JOAO PAULO GALVAGNI
	 * 
	 * @param 	msisdn			- MSISDN do assinante
	 * @param 	codigoServico	- Codigo do servico
	 * @param 	operacao		- C: Consulta, D: Debito ou E: Estorno
	 * @param 	operador		- Solicitante da cobranca
	 * @param 	servidor		- Servidor para conexao com o GPP
	 * @param 	porta			- Porta para conexao com o GPP
	 * @return	result			- XML contendo os dados apos a cobranca
	 * @throws  Exception
	 */
	public static String cobraServico(String msisdn, String codigoServico, String operacao, String operador, String servidor, String porta) throws Exception
	{
	    String result = "";
	    
		ORB orb = GerenteORB.getORB(servidor, porta);
		byte[] managerId = "ComponenteNegociosAprovisionamento".getBytes();
		aprovisionamento pPOA = aprovisionamentoHelper.bind(orb, "/AtivaComponentesPOA", managerId);
		
		try
		{
			result = pPOA.cobrarServico(msisdn, codigoServico, operacao, operador);
		}
		catch (Exception e) 
		{
			throw new Exception("Erro ao conectar no GPP");
		}
		
		return result;
	}
    
    /**
     * Metodo....: ajusteCreditoBancario
     * Descricao.: Realiza o ajuste de crédito
     *              bancário no GPP
     * @since....: 20/02/2008
     * @author...: Anderson Jefferson Cerqueira
     * 
     * @param   dadosRecarga    - Objeto DadosRecarga que possui todos 
     *                          os parâmetros necessário para efetuar 
     *                          o ajuste de crédito bancário
     * @param   servidor        - Servidor para conexao com o GPP
     * @param   porta           - Porta para conexao com o GPP
     * @return  result          - short contendo o resultado obtido pelo GPP
     * @throws  Exception
     */
    public static short ajusteCreditoBancario(DadosRecarga dadosRecarga, 
            String servidor, String porta) 
    throws Exception
    {       
        short result = -1;
        
        ORB orb = GerenteORB.getORB(servidor, porta);
        
        byte[] managerId = "ComponenteNegociosRecarga".getBytes();
        
        recarga pPOA = recargaHelper.bind(orb, "/AtivaComponentesPOA", managerId);
        
        try
        {
            result = pPOA.executaRecargaBanco(dadosRecarga.getMSISDN(), dadosRecarga.getTipoTransacao(), 
                    dadosRecarga.getIdentificacaoRecarga(), dadosRecarga.getNsuInstituicao(), 
                    dadosRecarga.getCodLoja(), dadosRecarga.getTipoCredito(), 
                    dadosRecarga.getValorPrincipal(), dadosRecarga.getDataHora(), 
                    dadosRecarga.getDataHora(),dadosRecarga.getDataContabil(), 
                    dadosRecarga.getTerminal(), dadosRecarga.getTipoTerminal(), 
                    dadosRecarga.getSistemaOrigem(), dadosRecarga.getOperador());
        }
        catch (Exception e) 
        {
            throw new Exception("Erro ao conectar no GPP");
        }
        
        return result;
    }

}
