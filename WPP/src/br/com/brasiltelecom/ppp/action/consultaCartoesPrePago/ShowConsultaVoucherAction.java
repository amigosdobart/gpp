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
 * Mostra a tela com o resultado da consulta do cartão pré-pago
 * 
 * Atualizado por Bernardo Vergne Dias (PIN)
 * 12/12/2006
 * 
 * @author André Gonçalves
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
     * @return Nome da tela de apresentação (VM).
     */
    public String getTela() 
    {
        return "consultaVoucher.vm";
    }
    
    /**
     * Implementa a lógica de negócio dessa ação.
     * 
     * @param context           Contexto do Velocity.
     * @param request           Requisição HTTP que originou a execução dessa ação.
     * @param sessionFactory    Factory de sessões para acesso ao banco de dados (Hibernate).
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
                context.put("erro", "Erro ao consultar os dados de cartão único. " +
                        "Favor tentar novamente ou notificar este erro ao Service Desk da TI.");
                request.setAttribute(Constantes.MENSAGEM, "Erro ao consultar os dados do cartão único '"+
                        request.getParameter("numeroCartao")+"'");
                        
                if(voucher.getCodRetorno() == Definicoes.RET_VOUCHER_NAO_EXISTE)
                {
                    context.put("aviso", "Informar ao assinante que o cartão não está ativo.");
                    context.put("erro", "Cartão inexistente.");
                    request.setAttribute(Constantes.MENSAGEM, "Erro ao consultar os dados do cartão único '"+
                            request.getParameter("numeroCartao")+"'. Cartão Inexistente");
                }
            }
            else
            {
                            
                /*
                 * VERIFICA PERMISSÃO PARA VER O PIN
                 * 
                 * O campo PIN no formulário de consulta, quando preenchido, deve ter
                 * necessariamente 6 digitos. É permitido ao usuario utilizar o caractere curinga 'X'
                 * nos dígitos em que ele tem dúvida (ou nao sabe o valor). Por regra, pode-se usar
                 * esse caractere no máximo 2 vezes. Se o número do PIN digitado pelo usuário estiver 
                 * correto (os caracteres curingas não sao validados), entao ele (o usuario) ganha 
                 * privilegio de ver o código de seguranca completo.
                 * 
                 * Usuários com a "permissao de ver PIN" definida no seu perfil sempre têm o privilégio de
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
                    
                    // Poder ver o PIN significa apenas poder ver a CÉLULA da tabela
                    // correspondente ao codigo de segurnaça. O número PIN só é mostrado
                    // dentro da célula se, e somente se, pinInvalido = false. 
                    // Caso pinInvalido = true então o usuário ver apenas uma mensagem
                    // de erro dentro da célula (ex: "Código incorreto.")
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
                
                // O IdMotivo não é levado em consideração no método findByFilter(), pois esse campo
                // não é preenchido na entidade Voucher.
                StatusMotivoVoucher smv = StatusMotivoVoucherDAO.findPrimeiroByIdStatusVoucher(session, voucher.getCodStatusVoucher());
                
                if (smv != null)
                    context.put("aviso", smv.getDesMensagem());
    
                /*
                 * AGRUPAMENTO DOS VALORESVOUCHER PARA FACILITAÇÃO NA VM
                 */
                
                // Formato: Map<CategoriaTEC, List<ValorVoucher> >
                // O list está ordenado por tipo de saldo: principal, bonus, sm, dados
                
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
                    
                request.setAttribute(Constantes.MENSAGEM, "Consulta do cartão único '" + 
                        request.getParameter("numeroCartao") + "' realizada com sucesso!");
            }
            
            session.getTransaction().commit(); 
        } 
        catch(Exception ex) 
        {
            logger.error("Erro ao consultar os dados de cartão único.",ex);
            context.put("erro", "Erro ao consultar os dados de cartão único. " +
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
     * @return Nome da operação (permissão) associada a essa ação.
     */
    public String getOperacao() 
    {
        return this.codOperacao;
    }
}