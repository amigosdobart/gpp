/*
 * Created on 24/03/2004
 *
 */
package br.com.brasiltelecom.ppp.action.ajusteCredito;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.brt.gpp.comum.mapeamentos.entidade.Categoria;
import com.brt.gpp.comum.mapeamentos.entidade.ConfiguracaoGPP;
import com.brt.gpp.comum.mapeamentos.entidade.OrigemRecarga;
import com.brt.gpp.comum.mapeamentos.entidade.SaldoCategoria;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.action.grupo.SegurancaGrupo;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

import br.com.brasiltelecom.ppp.dao.ConfiguracaoGPPDAO;
import br.com.brasiltelecom.ppp.dao.OrigemRecargaDAO;
import br.com.brasiltelecom.ppp.dao.SistemaOrigemDAO;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;

/**
 * Mostra a tela de ajuste de cr�dito
 * 
 * @author Andr� Gon�alves
 * @since 20/05/2004
 * Alterado por Marcos Magalh�es em 09/03/2006 para restringir acesso por perfil
 */
public class ShowFiltroAjusteCreditoAction extends ShowActionHibernate {

	private String codOperacao = Constantes.MENU_AJUSTE_CREDITO;
	Logger logger = Logger.getLogger(this.getClass());
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "filtroAjusteCredito.vm";
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return codOperacao;
	}

	public void updateVelocityContext(VelocityContext context, HttpServletRequest request, SessionFactory sessionFactory) throws Exception
	{
		Session sessionHibernate = null;
		try 
		{
			// Verifica se perfil do usu�rio permite acesso ao tipo de terminal requisitado (LigMix ou GSM)
			HttpSession session = request.getSession();
			Usuario login = (Usuario) session.getAttribute(Constantes.LOGIN);
			SegurancaGrupo.setPermissao(login, context);
			Usuario usuario = ((Usuario)request.getSession().getAttribute(Constantes.USUARIO));	
			
			// Inicia a sess�o do hibernate
			sessionHibernate = sessionFactory.getCurrentSession();
			sessionHibernate.beginTransaction();
			
			// Obtem as Origens que tem o sistema de origem PPP
			Collection origensSO 	 = SistemaOrigemDAO.findByIdSistemaOrigem(sessionHibernate, "PPP").getListaOrigens();
			// Obtem as Origens que s�o do tipo de lan�amento Cr�dito(C)
			Collection origensCredito = OrigemRecargaDAO.findByTipLancamento(sessionHibernate, "C");
			// Obtem o Objeto Configura��es GPP
			ConfiguracaoGPP configuracaoGPP = ConfiguracaoGPPDAO.findByIdConfiguracao(sessionHibernate, Constantes.PARAM_NUM_DIAS_EXPIRACAO) ;
			String diasExpiracao[] = configuracaoGPP.getVlrConfiguracao().split(",");
			
			// Inicia as cole��es
			Set origens  = new HashSet();
			Set saldos   = new HashSet();
			Set mascaras = new HashSet();
			
			// Obtem as categorias permitidas dos usu�rio logado no portal e para 
			// cada uma delas preenche as cole��es de Tipos de Saldo, Origens e M�scara
			for (Iterator i = usuario.getCategoriasPermitidas().iterator();i.hasNext();)
			{
				Categoria categoria = (Categoria) i.next();
				// Atualiza o objeto categoria pelo Hibernate
				sessionHibernate.refresh(categoria);
				
				mascaras.add(categoria.getDesMascaraMsisdn());
                for (Iterator i2 = categoria.getTiposSaldo().iterator(); i2.hasNext(); )
                {
                    SaldoCategoria saldoCat = (SaldoCategoria)i2.next();
                    saldos.add(saldoCat.getTipoSaldo());
                }
                
                // Considera apenas as origens que possuem a flag IndDisponivelPortal true
                for (Iterator it = categoria.getOrigensRecarga().iterator(); it.hasNext(); )
                {
                    OrigemRecarga origem = (OrigemRecarga)it.next();
                    if (origem.getIndDisponivelPortal() != null && origem.getIndDisponivelPortal().intValue() == 1)
                        origens.add(origem);
                }
			}
			
			// Retira da cole��o "origens" todas as origens
			// que n�o tem o sistema de origem "PPP" 
			origens.retainAll(origensSO);
            
			// Retira da cole��o "origens" todas as origens
			// que n�o tem o tipo de lan�amneto "C" 
			origens.retainAll(origensCredito);
			
			// Fecha a sess�o do Hibernate
			sessionHibernate.close();
			
			// Atualiza o context com as cole��es preenchidas
			context.put("origens",origens);
			context.put("saldos",saldos);
			context.put("mascaras",mascaras);
			context.put("diasExpiracao",diasExpiracao);
		}
		catch (Exception e)
		{
			context.put("erro", "Erro ao mostrar tela de ajuste de Cr�dito. <br><br>" + e);
			logger.error("Erro ao consultar o cadastro de Al�quotas de Imposto. " + e.getMessage());	
			if (sessionHibernate != null) sessionHibernate.getTransaction().rollback();
		}
	}
}
