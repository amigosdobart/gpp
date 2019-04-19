package br.com.brasiltelecom.ppp.dao;

import java.util.Calendar;

import org.hibernate.Session;

import br.com.brasiltelecom.ppp.portal.entity.RequisicaoExtrato;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

public class RequisicaoExtratoDAO
{
	public static void incluirRequisicaoExtrato(Session session, RequisicaoExtrato requisicao)
	{
		requisicao.setDatGeracao(null);
		requisicao.setDatRequisicao(Calendar.getInstance().getTime());
		requisicao.setIdtStatus_processamento(RequisicaoExtrato.STATUS_REQUISICAO_NAO_PROCESSADA);
		// O Codigo de retorno deve ser Zero na inclusao.
		requisicao.setIdtCodRetorno(Constantes.GPP_RET_OPERACAO_OK);
		session.save(requisicao);
	}
}
