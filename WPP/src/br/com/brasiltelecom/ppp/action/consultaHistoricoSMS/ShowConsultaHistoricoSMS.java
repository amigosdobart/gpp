package br.com.brasiltelecom.ppp.action.consultaHistoricoSMS;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.brt.gpp.aplicacoes.enviarSMS.entidade.DadosSMS;
import com.brt.gpp.aplicacoes.enviarSMS.entidade.TipoSMS;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.dao.EnvioSMSDAO;
import br.com.brasiltelecom.ppp.dao.TipoSMSDAO;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Consulta histórico de SMS.
 * 
 * @author Bernardo Vergne Dias
 * Data: 10-12-2007
 */
public class ShowConsultaHistoricoSMS extends ShowActionHibernate
{   
	private String codOperacao = Constantes.COD_CONSULTA_HISTORICO_SMS;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @return Nome da tela de apresentação (VM).
	 */
	public String getTela() 
	{
		return "consultaHistoricoSMS.vm";
	}

	/**
	 * Implementa a lógica de negócio dessa ação.
	 * 
	 * @param context 			Contexto do Velocity.
	 * @param request 			Requisição HTTP que originou a execução dessa ação.
	 * @param sessionFactory	Factory de sessões para acesso ao banco de dados (Hibernate).
	 */
	public void updateVelocityContext(VelocityContext context,
		   HttpServletRequest request, SessionFactory sessionFactory) throws Exception
	{
		Session session = null;
		
		try 
		{
            session = sessionFactory.getCurrentSession();
            session.beginTransaction();
            
			String msisdn = "55" + request.getParameter("msisdn");
            int meses = Integer.parseInt(request.getParameter("meses"));
            
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -meses);
            
            TipoSMS tipoSMS = TipoSMSDAO.findById(session, request.getParameter("tipo"));
            context.put("lista", EnvioSMSDAO.findByCriterios(session, msisdn, cal.getTime(), tipoSMS));
            context.put("sdf", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"));
            context.put("msisdn", request.getParameter("msisdn")); 
            context.put("action", new ShowConsultaHistoricoSMS());
            
	        session.getTransaction().commit();		
		}
		catch (Exception e)
		{
			context.put("erro", "Erro ao realizar a consulta. " + e.getMessage());
			logger.error("Erro ao realizar a consulta. " + e);	
			if (session != null && session.getTransaction() != null) 
				session.getTransaction().rollback();
		}				
	}
	
    /**
     * Wrapper para o RespostaSMSC.
     * Retorna uma descricao em portugues para o status do SMS
     */
    public String getDesStatus(DadosSMS dadosSMS)
    {
        if (dadosSMS == null)
            return "";
        
        int statusProc = dadosSMS.getIdtStatusProcessamento();
        String descricao = getDesStatusProcessamento(statusProc);
        
        if (statusProc == SMS_STATUS_ERRO)
        {
            Integer statusSMSC = dadosSMS.getIdtStatusSMSC();
            if (statusSMSC != null)
                descricao += " (" + getDesStatusSMSC(statusSMSC.intValue()) + ")";
        }
        return descricao;
    }         
    
    /**
     * Retorna uma descricao em portugues do status de processamento do SMS. <br>
     * OBSERVACAO: Método copiado da classe com.brt.gpp.comum.conexoes.smpp.RespostaSMSC, pois
     * nao eh possivel fazer referencia a esse classo no WPP devido a ausencia do smpp.jar
     */
    public String getDesStatusProcessamento(int status)
    {
        switch (status)
        {
            case SMS_STATUS_NAO_ENVIADO:     return "Pendente de processamento";
            case SMS_STATUS_ENVIANDO:        return "Enviando para o destinatário";
            case SMS_STATUS_ENVIADO:         return "Enviado para a plataforma de SMS";
            case SMS_STATUS_ENTREGE:         return "Entregue no destinatário";
            case SMS_STATUS_EXPIRADO:        return "Tempo de envio expirado";
            case SMS_STATUS_NAO_ENTREGAVEL:  return "Não entregável";
            case SMS_STATUS_REMOVIDO:        return "Descartado pela plataforma de SMS";
            case SMS_STATUS_ACEITO:          return "Mensagem lida pelo assinante";
            case SMS_STATUS_INVALIDO:        return "Mensagem inválida";
            case SMS_STATUS_REJEITADO:       return "Rejeitado pela plataforma de SMS";  
            case SMS_STATUS_ERRO:            return "Erro na plataforma de SMS";
        }
        return "Desconhecido";
    }
        
    /**
     * Retorna uma descricao em portugues do status de SMSC. <br>
     * Esse retorno somente faz sentido quando o status de processamento eh SMS_STATUS_ERRO. <br>
     * 
     * OBSERVACAO: Método copiado da classe com.brt.gpp.comum.conexoes.smpp.RespostaSMSC, pois
     * nao eh possivel fazer referencia a esse classo no WPP devido a ausencia do smpp.jar
     */
    public String getDesStatusSMSC(int status)
    {       
        switch (status)
        {
            case ESME_ROK:              return "OK";
            case ESME_RINVMSGLEN:       return "Tamanho do SM inválido";
            case ESME_RINVCMDLEN:       return "Comprimento do comando inválido";
            case ESME_RINVCMDID:        return "COMMAND ID inválido";
            case ESME_RINVBNDSTS:       return "BIND STATUS incorreto para o comando";
            case ESME_RALYBND:          return "ESME já está no estado BOUND";
            case ESME_RINVPRTFLG:       return "Flag PRIORITY inválida";
            case ESME_RINVREGDLVFLG:    return "Flag REGISTERED DELIVERY inválida";
            case ESME_RSYSERR:          return "Erro de sistema";
            case ESME_RINVSRCADR:       return "Endereço de origem inválido";  
            case ESME_RINVDSTADR:       return "Endereço de destino inválido";  
            case ESME_RINVMSGID:        return "MESSAGE ID inválido";  
            case ESME_RBINDFAIL:        return "Falha no BIND";  
            case ESME_RINVPASWD:        return "Senha inválida";  
            case ESME_RINVSYSID:        return "SYSTEM ID inválido";  
            case ESME_RCANCELFAIL:      return "Falha no cancelamento de SM";  
            case ESME_RREPLACEFAIL:     return "Falha na substituição de SM";  
        }        
        return "Desconhecido";
    }
    
    // STATUS DO SMS
    final int SMS_STATUS_NAO_ENVIADO    = 1;
    final int SMS_STATUS_ENVIANDO       = 2;
    final int SMS_STATUS_ENVIADO        = 3;
    final int SMS_STATUS_ENTREGE        = 4;
    final int SMS_STATUS_EXPIRADO       = 5;
    final int SMS_STATUS_REMOVIDO       = 6;
    final int SMS_STATUS_NAO_ENTREGAVEL = 7;
    final int SMS_STATUS_ACEITO         = 8;
    final int SMS_STATUS_INVALIDO       = 9;
    final int SMS_STATUS_REJEITADO      = 10;
    final int SMS_STATUS_ERRO           = 99;
    
    // CODIGOS DE RETORNO
    final short ESME_ROK            = 0x00;  //No Error
    final short ESME_RINVMSGLEN     = 0x01;  //Message Length is invalid
    final short ESME_RINVCMDLEN     = 0x02;  //Command Length is invalid
    final short ESME_RINVCMDID      = 0x03;  //Invalid Command ID
    final short ESME_RINVBNDSTS     = 0x04;  //Incorrect BIND Status for given command
    final short ESME_RALYBND        = 0x05;  //ESME Already in Bound State
    final short ESME_RINVPRTFLG     = 0x06;  //Invalid Priority Flag
    final short ESME_RINVREGDLVFLG  = 0x07;  //Invalid Registered Delivery Flag
    final short ESME_RSYSERR        = 0x08;  //System Error
    final short ESME_RINVSRCADR     = 0x0A;  //Invalid Source Address
    final short ESME_RINVDSTADR     = 0x0B;  //Invalid Dest Addr
    final short ESME_RINVMSGID      = 0x0C;  //Message ID is invalid
    final short ESME_RBINDFAIL      = 0x0D;  //Bind Failed
    final short ESME_RINVPASWD      = 0x0E;  //Invalid Password
    final short ESME_RINVSYSID      = 0x0F;  //Invalid System ID
    final short ESME_RCANCELFAIL    = 0x11;  //Cancel SM Failed
    final short ESME_RREPLACEFAIL   = 0x13;  //Replace SM Failed
    
	/**
	 * @return Nome da operação (permissão) associada a essa ação.
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
}
