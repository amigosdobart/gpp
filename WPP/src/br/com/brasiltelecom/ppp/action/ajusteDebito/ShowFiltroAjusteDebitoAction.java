/*
 * Created on 24/03/2004
 *
 */
package br.com.brasiltelecom.ppp.action.ajusteDebito;

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
import com.brt.gpp.comum.mapeamentos.entidade.OrigemRecarga;
import com.brt.gpp.comum.mapeamentos.entidade.SaldoCategoria;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.action.grupo.SegurancaGrupo;
import br.com.brasiltelecom.ppp.dao.OrigemRecargaDAO;
import br.com.brasiltelecom.ppp.dao.SistemaOrigemDAO;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra a tela de ajuste de débito
 * 
 * @author André Gonçalves
 * @since 20/05/2004
 */
public class ShowFiltroAjusteDebitoAction extends ShowActionHibernate 
{

	private String codOperacao = Constantes.MENU_AJUSTE_DEBITO;
	Logger logger = Logger.getLogger(this.getClass());
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() 
	{
		return "filtroAjusteDebito.vm";
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}

	public void updateVelocityContext(VelocityContext context, HttpServletRequest request, SessionFactory sessionFactory) throws Exception
	{
		Session sessionHibernate = null;
		try 
		{
			//	Verifica se perfil do usuário permite acesso ao tipo de terminal requisitado (LigMix ou GSM)
			HttpSession session = request.getSession();
			Usuario login = (Usuario) session.getAttribute(Constantes.LOGIN);
			SegurancaGrupo.setPermissao(login, context);
			Usuario usuario = ((Usuario)request.getSession().getAttribute(Constantes.USUARIO));			
			
			// Inicia a sessão do hibernate
			sessionHibernate = sessionFactory.getCurrentSession();
			sessionHibernate.beginTransaction();
	        
			// Obtem as Origens que tem o sistema de origem PPP
			Collection origensSO 	 = SistemaOrigemDAO.findByIdSistemaOrigem(sessionHibernate, "PPP").getListaOrigens();
			// Obtem as Origens que são do tipo de lançamento Débito(D)
			Collection origensDebito = OrigemRecargaDAO.findByTipLancamento(sessionHibernate, "D");

			// Inicia as coleções
			Set origens  = new HashSet();
			Set saldos   = new HashSet();
			Set mascaras = new HashSet();
			
			// Obtem as categorias permitidas dos usuário logado no portal e para 
			// cada uma delas preenche as coleções de Tipos de Saldo, Origens e Máscara
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
			
			// Retira da coleção "origens" todas as origens
			// que não tem o sistema de origem "PPP" 
			origens.retainAll(origensSO);
			// Retira da coleção "origens" todas as origens
			// que não tem o tipo de lançamneto "D" 
			origens.retainAll(origensDebito);
			
			// Fecha a sessão do Hibernate
			sessionHibernate.close();
			
			// Atualiza o context com as coleções preenchidas
			context.put("origens",origens);
			context.put("saldos",saldos);
			context.put("mascaras",mascaras);
			
		}
		catch (Exception e)
		{
			context.put("erro", "Erro ao mostrar tela de ajuste de débito. <br><br>" + e);
			logger.error("Erro ao consultar o cadastro de Alíquotas de Imposto. " + e.getMessage());	
			if (sessionHibernate != null) sessionHibernate.getTransaction().rollback();
		}
	}
}
