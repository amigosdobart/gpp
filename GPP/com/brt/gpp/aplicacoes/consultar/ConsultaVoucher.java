package com.brt.gpp.aplicacoes.consultar;

// Arquivo de Imports de Gerentes do GPP 
import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.gerenciamentoVouchers.OperacoesVoucher;
import com.brt.gpp.comum.conexoes.bancoDados.*;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.gppExceptions.*;

//Arquivos de Import Internos
import java.sql.*;
import java.text.*;

/**
  *
  * Este arquivo refere-se a classe ConsultaVoucher, responsavel pela implementacao da
  * logica de consulta de dados de voucher na plataforma Tecnomen
  *
  * <P> Versao:			1.0
  *
  * @Autor: 			Camile Cardoso Couto
  * Data: 				15/03/2004
  *
  * Atualizado por: Bernardo Vergne Dias (controle total)
  * 11/07/2007
  * 
  */
public final class ConsultaVoucher extends Aplicacoes
{
	     
	/**
	 * Metodo...: ConsultaVoucher
	 * Descricao: Construtor 
	 * @param	logId - Identificador do Processo para Log
	 * @return									
	 */
	 public ConsultaVoucher (long logId)
	 {
		super(logId, Definicoes.CL_CONSULTA_VOUCHER);
	 }

	 /**
	  * Consulta dados de voucher na plataforma Tecnomen. 
	  * Esse método sempre retorna o xml de um objeto Voucher nao nulo.
	  * Pelo menos o atributo CodigoRetorno estará preenchido (com valores constantes em Definicoes).
	  * 
	  * @param	voucherId 	Identificador do voucher	  
	  * @return	string 		Codigo de retorno + voucherId + codStatus + descStatus + valorFace + MSISDN + dtUltimaAtualizacao
	  * @throws GPPInternalErrorException
	  */
	 public String run (String avoucherId) throws GPPInternalErrorException
	 {
		 super.log(Definicoes.INFO, "run(Consulta Dados Voucher)", "Inicio Voucher "+avoucherId);
		 		
	 	 // Inicializa variaveis do metodo 
		 String retorno = null;
		 Voucher dadosVoucher = null;
		 
		 try
		 {
			//Efetua a consulta dos dados do voucher recebido como parametro
		 	OperacoesVoucher operacoes = new OperacoesVoucher(super.getIdLog());
			dadosVoucher = operacoes.getInformacoesVoucher(avoucherId);
			
			if(dadosVoucher.getCodRetorno() == Definicoes.RET_OPERACAO_OK)
			{
				// Verifica o codigo do status e classifica a descricao do status
				classificaDescricaoStatus(dadosVoucher);								
				
				// Se o voucher estiver em status cancelado, pega quem cancelou 
				// e a data de cancelamento na tbl_rec_voucher_cancelado do GPP.
				if(dadosVoucher.getCodStatusVoucher() == Definicoes.VOUCHER_INVALIDADO)
					consultaDadosCancelamento(dadosVoucher);
			}
			
			if(dadosVoucher.getCodRetorno() == Definicoes.RET_VOUCHER_NAO_EXISTE)
			{
				// O voucher que foi utilizado é removido da tabela de vouchers e armazenado na
				// tabela USED_VOUCHERS, sendo que a API utilizada para consulta retorna a mensagem
				// de erro TVM_VOUCHER_NO_DOES_NOT_EXIST. Por isso o metodo abaixo é executado para 
				// verificar se o GPP ja possui informacoes de utilizacao desse voucher para 
				// poder retornar a informacao correta da pesquisa.				
				buscaVoucherUtilizado(avoucherId,dadosVoucher);
			}
		}
		catch (Exception e1)
		{
			 super.log(Definicoes.ERRO, "run(Consulta Dados Voucher)", "Excecao:"+ e1);				
			 throw new GPPInternalErrorException ("Excecao ocorrida: " + e1);			 
		}
		finally
		{
			retorno = dadosVoucher.getVoucherXML() ;
			super.log(Definicoes.INFO, "run(Consulta Dados Voucher)", "Fim");
		}		 
		
		return retorno;
	}

	/**
	  * Metodo...: classificaDescricaoStatus
	  * Descricao: De acordo com o codigo do status do voucher, 
	  * 			seta a descricao desse status
	  * @param	dadosVoucher 	- Estrututra com dados do voucher	 
	  * @return 
	  * @throws 
	  */
	 public void classificaDescricaoStatus (Voucher dadosVoucher)
	 {
		switch(dadosVoucher.getCodStatusVoucher())
		{
			case Definicoes.VOUCHER_ATIVO:
			{
				dadosVoucher.setDescStatusVoucher(Definicoes.S_VOUCHER_ATIVO);
				break;				
			}
			case Definicoes.VOUCHER_EXPIRADO:
			{
				dadosVoucher.setDescStatusVoucher(Definicoes.S_VOUCHER_EXPIRADO);
				break;				
			}
			case Definicoes.VOUCHER_USADO:
			{
				dadosVoucher.setDescStatusVoucher(Definicoes.S_VOUCHER_USADO);
				break;
			}
			case Definicoes.VOUCHER_INVALIDADO:
			{
				dadosVoucher.setDescStatusVoucher(Definicoes.S_VOUCHER_INVALIDADO); 
				break;
			}
			default:
			{
				dadosVoucher.setDescStatusVoucher(Definicoes.S_VOUCHER_AINDA_NAO_ATIVO); 
				break;
			}
		}
	 }
	 	 
	 /**
	  * Metodo....:buscaVoucherUtilizado
	  * Descricao.:Este metodo identifica no GPP se o voucher foi utilizado, marcando tambem
	  *            as propriedades de quem utilizou e quando.
	  *            Observacao: Este metodo e chamado somente quando o voucher nao foi encontrado
	  *                        pela API da tecnomen
	  * @param numVoucher 	- Numero do voucher a ser pesquisado
	  * @param voucher		- Objeto voucher no qual os dados serao inseridos
	  * @return boolean		- Retorna indicador se o voucher foi encontrado ou nao
	  * @throws GPPInternalErrorException
	  */
	 private boolean buscaVoucherUtilizado(String aNumVoucher,Voucher voucher) throws GPPInternalErrorException
	 {
		super.log(Definicoes.DEBUG, "buscaVoucherUtilizado", "Inicio");
	 	boolean encontrou=false;
		PREPConexao conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.getIdLog());
		try
		{
			// Caso no numero do voucher venha o caracter "*" (curinga) entao
			// elimina-o da variavel, pois o numero do voucher no GPP e um campo
			// numerico entao deve ser pesquisado sem este caracter
			String numVoucher = aNumVoucher;
			if (numVoucher.indexOf("*") > -1)
				numVoucher = numVoucher.substring(0,numVoucher.indexOf("*"));

			String sql =    "SELECT NUM_VOUCHER " +
							     // ",VLR_FACE " +
							      ",DAT_ATUALIZACAO " +
							      ",IDT_MSISDN_UTILIZADOR " +
							  "FROM TBL_REC_VOUCHER " +
							 "WHERE ID_STATUS_VOUCHER = ? " +
							   "AND NUM_VOUCHER       = ? ";
			
			Object param[] = {new Integer(Definicoes.STATUS_VOUCHER_UTILIZADO),numVoucher};	 
			ResultSet rs = conexaoPrep.executaPreparedQuery(sql,param,super.getIdLog());
			// Consulta o voucher na tabela de vouchers do GPP com o status de UTILIZADO
			// sendo que caso este nao seja encontrado entao nada e executado no objeto VOUCHER 
			// passado como parametro sendo retornado os mesmos dados originais. Somente sera 
			// alterado as informacoes do voucher, caso ele exista no GPP com o status de UTILIZADO
			if (rs.next())
			{
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				voucher.setCodRetorno           (Definicoes.RET_S_OPERACAO_OK);
				voucher.setCodStatusVoucher     ((short)Definicoes.VOUCHER_USADO);
				voucher.setDescStatusVoucher    (Definicoes.S_VOUCHER_USADO);
				voucher.setDataUltimaAtualizacao(sdf.format(rs.getTimestamp("DAT_ATUALIZACAO")));
				voucher.setNumeroVoucher        (aNumVoucher);
				voucher.setUsadoPor      		(rs.getString("IDT_MSISDN_UTILIZADOR"));
				
				encontrou=true;
			}
		}
		catch(java.sql.SQLException e)
		{
			super.log(Definicoes.WARN, "buscaVoucherUtilizado", "Excecao SQL:"+e);
			throw new GPPInternalErrorException("Erro interno GPP:"+e);
		}
		finally
		{
			super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep,super.getIdLog());
		}
		super.log(Definicoes.DEBUG, "buscaVoucherUtilizado", "Fim");
		return encontrou;
	 }
	 
	 /**
	  * Metodo....:consultaDadosCancelamento
	  * Descricao.:Este metodo identifica no GPP o usuário que cancelou o voucher e a 
	  * 		   data do cancelamento.
	  * @param numVoucher 	- Numero do voucher a ser pesquisado
	  * @param voucher		- Objeto voucher no qual os dados serao inseridos
	  * @return void		
	 * @throws GPPInternalErrorException 
	  * @throws GPPInternalErrorException
	  */
	 private void consultaDadosCancelamento(Voucher voucher) throws GPPInternalErrorException 
	 {
		 super.log(Definicoes.DEBUG, "consultaDadosCancelamento", "Inicio");
		 PREPConexao conexaoPrep = null;
		 
		 try
		 {
			conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.getIdLog());
			// Consulta na tbl_rec_voucher_cancelado as informações referêntes
			// ao voucher cancelado
			String numVoucher = voucher.getNumeroVoucher();
		
			String sql =    "SELECT NOM_OPERADOR " +
							      ",DAT_CANCELAMENTO " +
							  "FROM TBL_REC_VOUCHER_CANCELADO " +
							 "WHERE NUM_CARTAO = ? " ;
			
			Object param[] = {numVoucher};	 
			ResultSet rs = conexaoPrep.executaPreparedQuery(sql,param,super.getIdLog());

			// Insere no objeto VOUCHER as infomações consultadas na tabela
			if (rs.next())
			{			
				voucher.setCanceladoPor         (rs.getString("NOM_OPERADOR"));
				voucher.setDataCancelamento     (rs.getTimestamp("DAT_CANCELAMENTO"));
			}
		 }
		 catch(java.sql.SQLException e)
		 {
			super.log(Definicoes.WARN, "consultaDadosCancelamento", "Excecao SQL:"+e);
		 }
		 finally
		 {
			super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep,super.getIdLog());
		 }
		 super.log(Definicoes.DEBUG, "consultaDadosCancelamento", 
				 					 "\n Cancelado por: "        + voucher.getCanceladoPor()+"" +
				 					 "\n Data de Cancelamento: " + voucher.getDataCancelamento());
			
	 }
}