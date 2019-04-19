package br.com.brasiltelecom.ppp.dao;

import org.hibernate.Session;

import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoTipoBonificacao;

public class PromocaoTipoBonificacaoDao
{
	public static PromocaoTipoBonificacao findById(Session session, short idTipoBonificacao)
	{
		return (PromocaoTipoBonificacao)session.get(PromocaoTipoBonificacao.class, new Short(idTipoBonificacao));
	}
}
