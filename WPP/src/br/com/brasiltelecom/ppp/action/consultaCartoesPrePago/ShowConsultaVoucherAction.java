package br.com.brasiltelecom.ppp.action.consultaCartoesPrePago;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.dao.CategoriaTECDAO;
import br.com.brasiltelecom.ppp.dao.StatusMotivoVoucherDAO;
import br.com.brasiltelecom.ppp.dao.VoucherDAO;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

import com.brt.gpp.aplicacoes.consultar.Voucher;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.mapeamentos.entidade.CategoriaTEC;
import com.brt.gpp.comum.mapeamentos.entidade.StatusMotivoVoucher;
import com.brt.gpp.comum.mapeamentos.entidade.ValorVoucher;

/**
 * Mostra a tela com o resultado da consulta do cart�o pr�-pago
 * 
 * Atualizado por Bernardo Vergne Dias (PIN)
 * 12/12/2006
 * 
 * @author Andr� Gon�alves
 * @since 21/05/2004
 * 
 * Atualizado por Bernardo Vergne Dias (controle total, hibernate)
 * 06/07/2007
 */
public class ShowConsultaVoucherAction extends ShowActionHibernate 
{
    private String codOperacao = Constantes.COD_CONSULTA_CARTAO;
    Logger logger = Logger.getLogger(this.getClass());
    
    /**
     * @return Nome da tela de apresenta��o (VM).
     */
    public String getTela() 
    {
        return "consultaVoucher.vm";
    }
    
    /**
     * Implementa a l�gica de neg�cio dessa a��o.
     * 
     * @param context           Contexto do Velocity.
     * @param request           Requisi��o HTTP que originou a execu��o dessa a��o.
     * @param sessionFactory    Factory de sess�es para acesso ao banco de dados (Hibernate).
     */
    public void updateVelocityContext(VelocityContext context,
           HttpServletRequest request, SessionFactory sessionFactory) throws Exception
    {
        Session session = null;     
        Usuario usuario = ((Usuario)request.getSession().getAttribute(Constantes.USUARIO)); 
        Voucher voucher = null;
        
        try 
        {
            session = sessionFactory.getCurrentSession(); 
            session.beginTransaction();
            
            /*
             * CONSULTA VOUCHER NO GPP
             */
            
            String servidor = (String)servlet.getServletContext().getAttribute(Constantes.GPP_NOME_SERVIDOR);
            String porta = (String)servlet.getServletContext().getAttribute(Constantes.GPP_PORTA_SERVIDOR);
            voucher = VoucherDAO.findById(session, servidor, porta, request.getParameter("numeroCartao"));
            context.put("voucher", voucher);
            
            // Possiveis retornos:
            
            // Definicoes.RET_OPERACAO_OK
            // Definicoes.RET_VOUCHER_NAO_EXISTE
            // Definicoes.RET_ERRO_GENERICO_TECNOMEN
            // Definicoes.RET_ERRO_GENERICO_GPP
            
            if (voucher.getCodRetorno() != Definicoes.RET_OPERACAO_OK)
            {
                context.put("erro", "Erro ao consultar os dados de cart�o �nico. " +
                        "Favor tentar novamente ou notificar este erro ao Service Desk da TI.");
                request.setAttribute(Constantes.MENSAGEM, "Erro ao consultar os dados do cart�o �nico '"+
                        request.getParameter("numeroCartao")+"'");
                        
                if(voucher.getCodRetorno() == Definicoes.RET_VOUCHER_NAO_EXISTE)
                {
                    context.put("aviso", "Informar ao assinante que o cart�o n�o est� ativo.");
                    context.put("erro", "Cart�o inexistente.");
                    request.setAttribute(Constantes.MENSAGEM, "Erro ao consultar os dados do cart�o �nico '"+
                            request.getParameter("numeroCartao")+"'. Cart�o Inexistente");
                }
            }
            else
            {
                            
                /*
                 * VERIFICA PERMISS�O PARA VER O PIN
                 * 
                 * O campo PIN no formul�rio de consulta, quando preenchido, deve ter
                 * necessariamente 6 digitos. � permitido ao usuario utilizar o caractere curinga 'X'
                 * nos d�gitos em que ele tem d�vida (ou nao sabe o valor). Por regra, pode-se usar
                 * esse caractere no m�ximo 2 vezes. Se o n�mero do PIN digitado pelo usu�rio estiver 
                 * correto (os caracteres curingas n�o sao validados), entao ele (o usuario) ganha 
                 * privilegio de ver o c�digo de seguranca completo.
                 * 
                 * Usu�rios com a "permissao de ver PIN" definida no seu perfil sempre t�m o privil�gio de
                 * ver o PIN completo no resultado da consulta.
                 */
                
                boolean podeVerPin = (usuario.getOperacoesPermitidas(Constantes.ACAO).get(Constantes.COD_CONSULTAR_VOUCHER_PIN) != null);
                boolean pinInvalido = false;
    
                if (request.getParameter("numeroPINCartao") != null && 
                    !request.getParameter("numeroPINCartao").equals(""))
                {
                    String PIN_usuario = request.getParameter("numeroPINCartao");
                    String PIN_correto = voucher.getCodigoSeguranca();
                    StringBuffer PIN_validacao = new StringBuffer();
                    
                    for (int i = 0; i < PIN_usuario.length(); i ++) 
                    {
                        if (PIN_usuario.toUpperCase().charAt(i) == 'X') 
                        {
                            PIN_validacao.append(PIN_correto.charAt(i));
                        }
                        else 
                        {
                            PIN_validacao.append(PIN_usuario.charAt(i));
                        }
                    }
    
                    if (!(PIN_validacao.toString().equals(PIN_correto))) 
                    {
                        pinInvalido = true;
                    }
                    
                    // Poder ver o PIN significa apenas poder ver a C�LULA da tabela
                    // correspondente ao codigo de segurna�a. O n�mero PIN s� � mostrado
                    // dentro da c�lula se, e somente se, pinInvalido = false. 
                    // Caso pinInvalido = true ent�o o usu�rio ver apenas uma mensagem
                    // de erro dentro da c�lula (ex: "C�digo incorreto.")
                    podeVerPin = true;
                }
                
                context.put("podeVerPin", new Boolean(podeVerPin)); 
                context.put("pinInvalido", new Boolean(pinInvalido));
                
                /*
                 * CANCELAMENTO DE VOUCHER
                 */
                
                boolean usuarioPodeCancelarVoucher = (usuario.getOperacoesPermitidas(Constantes.ACAO).get(Constantes.COD_CANCELAR_VOUCHER) != null);
                if(voucher.getCodStatusVoucher() == 1 && usuarioPodeCancelarVoucher)
                    context.put("podeCancelarVoucher", new Boolean(true));  
                 
                /*
                 * MENSAGEM STATUS
                 */
                
                // O IdMotivo n�o � levado em considera��o no m�todo findByFilter(), pois esse campo
                // n�o � preenchido na entidade Voucher.
                StatusMotivoVoucher smv = StatusMotivoVoucherDAO.findPrimeiroByIdStatusVoucher(session, voucher.getCodStatusVoucher());
                
                if (smv != null)
                    context.put("aviso", smv.getDesMensagem());
    
                /*
                 * AGRUPAMENTO DOS VALORESVOUCHER PARA FACILITA��O NA VM
                 */
                
                // Formato: Map<CategoriaTEC, List<ValorVoucher> >
                // O list est� ordenado por tipo de saldo: principal, bonus, sm, dados
                
                Map saldos = new HashMap();
                adicionaValoresVoucher(session, saldos, voucher.getValoresVoucherPrincipal());
                adicionaValoresVoucher(session, saldos, voucher.getValoresVoucherBonus());
                adicionaValoresVoucher(session, saldos, voucher.getValoresVoucherSm());
                adicionaValoresVoucher(session, saldos, voucher.getValoresVoucherDados());
                context.put("saldos", saldos);
                context.put("categoriasTEC", CategoriaTECDAO.findAll(session));
                
                DecimalFormat format = new DecimalFormat("#0.00");
                    format.setDecimalFormatSymbols(new DecimalFormatSymbols(new Locale("pt", "BR")));   
                    context.put("format", format);
                    
                request.setAttribute(Constantes.MENSAGEM, "Consulta do cart�o �nico '" + 
                        request.getParameter("numeroCartao") + "' realizada com sucesso!");
            }
            
            session.getTransaction().commit(); 
        } 
        catch(Exception ex) 
        {
            logger.error("Erro ao consultar os dados de cart�o �nico.",ex);
            context.put("erro", "Erro ao consultar os dados de cart�o �nico. " +
                    "Favor tentar novamente ou notificar este erro ao Service Desk da TI.");
            
            if (session != null && session.isOpen())
                session.getTransaction().rollback();
        }

    }

    /**
     * Organiza valoresVoucher por CategoriaTEC.
     * Formato: saldos = Map<CategoriaTEC, List<ValorVoucher> >
     */
    private void adicionaValoresVoucher(Session session, Map saldos, Map valoresVoucher)
    {
        for (Iterator it = valoresVoucher.values().iterator(); it.hasNext(); )
        {
            ValorVoucher vv = (ValorVoucher)it.next();
            CategoriaTEC categoriaTEC = vv.getCategoriaTEC();
            
            // Cria uma nova collection para essa categoria
            if (saldos.get(categoriaTEC) == null)
                saldos.put(categoriaTEC, new ArrayList());
    
            ((List)saldos.get(categoriaTEC)).add(vv);
        }
    }
    
    /**
     * @return Nome da opera��o (permiss�o) associada a essa a��o.
     */
    public String getOperacao() 
    {
        return this.codOperacao;
    }
}