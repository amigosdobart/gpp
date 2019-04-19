
package com.brt.gpp.aplicacoes.importacaoUsuarioPortalNDS;

import java.util.HashMap;
import java.util.Map;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.gerentesPool.GerentePoolLog;
import com.brt.gpp.comum.gppExceptions.*;
import com.brt.gpp.comum.mapeamentos.MapCodigosRetorno;
import com.brt.gpp.comum.mapeamentos.entidade.CodigoRetorno;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;

import com.brt.gpp.comum.conexoes.bancoDados.*;

import com.brt.gpp.comum.*;

/**
* Processo batch de importação de usuários do WPP. <br>
* 
* Esse processo trata as tabelas TBL_INT_PPP_IN e TBL_INT_PPP_OUT, realizando as
* operações de  de usuários.
* 
* @Autor: Vanessa Andrade
* Data: 17/05/2004
*
* Modificado por: Joao Carlos
* Data: 15/09/2004
* Razao: Comentando a linha que insere o grupo inicial (Perfil realizado pelo I-Chain)
*        e utilizacao do mapeamento de configuracao do GPP ao inves de select na tabela
*
* Modificado por: Danilo Alves Araujo
* Data: 10/04/2006
* Razão: Aplicação do Modelo Produtor-Consumido à classe Original.
* 
* Modificado por: Bernardo Vergne Dias
* Data: 18/12/2007
* Descrição: melhoramentos diversos:
*            - tratamento de exceções (catchs nao tratados, encapsulamento errado de excessoes, etc)
*            - tratamento do codigo de retorno na inclusão, atualização e exclusão (antes desprezado)
*            - correção da atualização do status de processamento na PPP_IN
*            - melhoramento de LOG
*            - correção de comentários de classe (antes referentes a Contestacao)
*/
public final class UsuarioPortalNDSConsumidor extends Aplicacoes implements ProcessoBatchConsumidor{

	private ProcessoBatchProdutor produtor;

	public UsuarioPortalNDSConsumidor()
	{
		super(GerentePoolLog.getInstancia(UsuarioPortalNDSConsumidor.class).getIdProcesso(Definicoes.CL_IMPORTACAO_USUARIOS)
			     ,Definicoes.CL_IMPORTACAO_USUARIOS);
	}

	public void startup(ProcessoBatchProdutor produtor) throws Exception
	{
	   this.produtor = produtor;
	}

	public void startup() throws Exception
	{
	}

	public void startup(Produtor produtor) throws Exception
	{
	}

	public void finish()
	{
	}

	public void execute(Object obj) throws Exception
	{
        PREPConexao conexaoPrep   = produtor.getConexao();
		DadosUsuario dadosUsuario = null;
		String status_xml         = null;
        String idt_evento_negocio = Definicoes.IDT_EVT_NEGOCIO_IMPORTACAO_USUARIO_IN;
        String xmlDocment         = null;
        int idProcessamento       = 0;
        int retorno               = Definicoes.RET_OPERACAO_OK;
        
        // Carrega os dados do VO
        Map map = (HashMap)obj;  
        xmlDocment = (String)map.get("XML_DOCUMENT");
        idProcessamento = ((Integer)map.get("ID_PROCESSAMENTO")).intValue();
        
        try
        {
            dadosUsuario = DadosUsuario.parseImportacaoUsuarioXML(idProcessamento, xmlDocment);

            // Obtem a acao que deve ser realizada no usuario (inclusao, exclusao, alteracao)
            String acao = dadosUsuario.getacao();
            
            if (acao.equals(Definicoes.IND_INCLUSAO_USUARIO))         retorno = inserirUsuario(conexaoPrep, dadosUsuario);
            else if (acao.equals(Definicoes.IND_ALTERACAO_USUARIO))   retorno = atualizarUsuario(conexaoPrep, dadosUsuario);
            else if (acao.equals(Definicoes.IND_EXCLUSAO_USUARIO))    retorno = excluirUsuario(conexaoPrep, dadosUsuario);
            
            // Analisa status do xml de saida
            if (retorno == Definicoes.RET_OPERACAO_OK)
                status_xml = Definicoes.RET_IMPORTACAO_USUARIO_OK;
            else
                status_xml = Definicoes.RET_IMPORTACAO_USUARIO_NOK;

            // Atualiza o registro processado
            atualizaIndProcessamento(dadosUsuario, idt_evento_negocio, retorno, conexaoPrep);
       
            // Troca o evento de negocio para o TAG de envio de dados
            idt_evento_negocio = Definicoes.IDT_EVT_NEGOCIO_IMPORTACAO_USUARIO_OUT;
            dadosUsuario.setevento(Definicoes.IDT_EVT_NEGOCIO_IMPORTACAO_USUARIO_OUT);

            //Map de Código de Retorno / Descricao
            MapCodigosRetorno mapRetorno = MapCodigosRetorno.getInstance();

            //Gera o XML de saída
            CodigoRetorno codRetorno = mapRetorno.getRetorno(retorno);
            String desRetorno = (codRetorno == null) ? "Erro na operacao" : codRetorno.getDescRetorno();
            String xmlRetorno = dadosUsuario.getRetornoImportacaoUsuarioXML(desRetorno, retorno, status_xml);

            // Grava os campos na tabela de saída
            gravaXmlSaida(conexaoPrep, dadosUsuario.getid_processamento(), GPPData.dataCompletaForamtada(), idt_evento_negocio, xmlRetorno);
        }
        catch (Exception e) 
        {
            produtor.setStatusProcesso(Definicoes.PROCESSO_ERRO);
            super.log(Definicoes.ERRO, "execute", "Excecao na importacao usuario NDS: " + e + "; XML: " + xmlDocment);
            retorno = Definicoes.RET_ERRO_TECNICO;
        }
	}

	/**
	 * Metodo...: atualizaIndProcessamento
	 * Descricao: Atualiza o indicador de processamento da tabela de entrada
     * @param   dadosUsuario        - Dados do usuario
	 * @param 	conexaoPrep			- Conexao com o banco de dados
     * @param   retorno             - Codigo de retorno da operacao (status de processamento)
	 * @param 	idt_evento_negocio	- Identificador do evento de negócio
	 */
	private void atualizaIndProcessamento(DadosUsuario dadosUsuario, String idt_evento_negocio, int retorno, PREPConexao conexaoPrep)
	{
		try
		{
			String idt_processamento = (retorno == Definicoes.RET_OPERACAO_OK) ? 
                    Definicoes.IDT_PROCESSAMENTO_OK : Definicoes.IDT_PROCESSAMENTO_ERRO;

			// Atualiza o indicador de processamento da tabela de entrada
			String sql_atualiza = "UPDATE TBL_INT_PPP_IN SET IDT_STATUS_PROCESSAMENTO = ? " +
								  "WHERE IDT_EVENTO_NEGOCIO = ? and ID_PROCESSAMENTO = ? ";

			Object paramMsg[] = {idt_processamento, idt_evento_negocio, new Integer(dadosUsuario.getid_processamento())};
			int ret = conexaoPrep.executaPreparedUpdate(sql_atualiza, paramMsg, super.logId);
            
			if (ret <= 0)
				super.log(Definicoes.ERRO, "atualizaIndProcessamento", "Erro na atualizacao de TBL_INT_PPP_IN. Retorno: " + ret);
		}
		catch (Exception e)
		{
            produtor.setStatusProcesso(Definicoes.PROCESSO_ERRO);
			super.log(Definicoes.ERRO, "atualizaIndProcessamento", "Erro ao atualizar TBL_INT_PPP_IN: " + e);
		}
	}

	/**
	 * Metodo...: gravaXmlSaida
	 * Descricao: Grava o xml de Saída para o Vitria
	 * @param 	conexaoPrep			- Conexao com o banco de dados
	 * @param 	id_processamento	- Identificador do processamento da importacao
	 * @param	dataProcessamento	- Data do processamento da importacao
	 * @param 	idt_evento_negocio	- Identificador do evento de negócio
	 * @param	xmlRetorno			- Xml de retorno
	 */
	private void gravaXmlSaida(PREPConexao conexaoPrep, int id_processamento, String dataProcessamento, String idt_evento_negocio, String xmlRetorno)
	{
		try
		{
			// Grava os campos na tabela de saída
			String sql_grava = "INSERT INTO TBL_INT_PPP_OUT  " +
							  " (ID_PROCESSAMENTO, DAT_CADASTRO, IDT_EVENTO_NEGOCIO, " +
							  " XML_DOCUMENT, IDT_STATUS_PROCESSAMENTO) VALUES " +
							  " ( ?, TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), ?, ?, ? )";

			Object paramInsert[] = {new Integer(id_processamento), dataProcessamento, idt_evento_negocio, xmlRetorno, Definicoes.IDT_PROCESSAMENTO_NOT_OK};
			int ret = conexaoPrep.executaPreparedUpdate(sql_grava, paramInsert, super.logId);
            
            if (ret <= 0)
                super.log(Definicoes.ERRO, "gravaXmlSaida", "Erro na gravacao de TBL_INT_PPP_OUT. Retorno do banco: " + ret);
		}
		catch (Exception e)
		{
            produtor.setStatusProcesso(Definicoes.PROCESSO_ERRO);
			super.log(Definicoes.ERRO, "gravaXmlSaida", "Erro ao gravar na TBL_INT_PPP_OUT: " + e + "; XML: " + xmlRetorno);
		}
	}

	/**
	 * Metodo...: inserirUsuario
	 * Descricao: Insere um usuário da tabela de Usuarios
	 * @param conexaoPrep 		- Conexao com o banco de dados para alteracado de dados do usuario
	 * @param dadosUsuario		- Dados do usuario a alterar
	 * @return					- OK em sucesso, ou o erro funcional / tecnico em caso de falha
	 */
	private int inserirUsuario(PREPConexao conexaoPrep, DadosUsuario dadosUsuario)
	{
        int retorno = Definicoes.RET_OPERACAO_OK;
        
        try
        {
    		String sql_inclusao = "INSERT INTO TBL_PPP_USUARIO " +
    			                  " (ID_USUARIO, NOM_USUARIO, NOM_DEPARTAMENTO, " +
    			                  " DES_CARGO, IDT_EMAIL) VALUES ( ?, ?, ?, ?, ? )";
    
    		Object paramInclusao[] = {dadosUsuario.getidUsuario(), dadosUsuario.getnomeUsuario(), 
                    dadosUsuario.getdeptoUsuario(), dadosUsuario.getcargoUsuario(), dadosUsuario.getemailUsuario()};

			conexaoPrep.executaPreparedUpdate(sql_inclusao, paramInclusao, super.logId);
		}
		catch (GPPInternalErrorException e)
		{
			if (conexaoPrep.getCodigoErro() == 1)
            {
				super.log(Definicoes.WARN, "inserirUsuario", "Usuario '" + dadosUsuario.getidUsuario() + 
                        "' ja existe na tabela TBL_PPP_USUARIO. Excecao: " + e);
            }
			else
			{
                produtor.setStatusProcesso(Definicoes.PROCESSO_ERRO);
				super.log(Definicoes.ERRO, "inserirUsuario", "Erro: " + e);			

                retorno = Definicoes.RET_ERRO_TECNICO;
			}
            
		}

		return retorno;
	}

	/**
	 * Metodo...: atualizarUsuario
	 * Descricao: Atualiza um usuário da tabela de Usuarios
	 * @param conexaoPrep 		- Conexao com o banco de dados para alteracado de dados do usuario
	 * @param dadosUsuario		- Dados do usuario a alterar
	 * @return					- OK em sucesso, ou o erro funcional / tecnico em caso de falha
	 */
	private int atualizarUsuario(PREPConexao conexaoPrep, DadosUsuario dadosUsuario)
	{
        int retorno = Definicoes.RET_OPERACAO_OK;
        
        try
        {
    		String sql_alteracao = "UPDATE TBL_PPP_USUARIO SET " +
    								   " NOM_USUARIO=?, NOM_DEPARTAMENTO=?, DES_CARGO=?, " +
    								   " IDT_EMAIL=? WHERE ID_USUARIO=?";
    
    		Object paramaAlteracao[] = {dadosUsuario.getnomeUsuario(), dadosUsuario.getdeptoUsuario(), 
                    dadosUsuario.getcargoUsuario(), dadosUsuario.getemailUsuario(), dadosUsuario.getidUsuario()};

			int ret = conexaoPrep.executaPreparedUpdate(sql_alteracao,paramaAlteracao, super.logId);
            
            if(ret <= 0)
            {
                // Erro Usuario inexistente
                super.log(Definicoes.WARN, "atualizarUsuario", "Usuario '"+dadosUsuario.getidUsuario()+
                        "' nao existe na tabela TBL_PPP_USUARIO. Retorno do banco: " + ret);
                retorno = Definicoes.RET_ALTERACAO_USUARIO_INEXISTENTE;
            }
		}
		catch (GPPInternalErrorException e)
		{
            produtor.setStatusProcesso(Definicoes.PROCESSO_ERRO);
			super.log(Definicoes.ERRO, "atualizarUsuario", "Erro: " + e);            
            retorno = Definicoes.RET_ERRO_TECNICO;
		}

		return retorno;
	}

	/**
	 * Metodo...: excluirUsuario
	 * Descricao: Exclui um usuário da tabela de Usuarios
	 * @param conexaoPrep 		- Conexao com o banco de dados para alteracado de dados do usuario
	 * @param dadosUsuario		- Dados do usuario a alterar
	 * @return					- OK em sucesso, ou o erro funcional / tecnico em caso de falha
	 */
	private int excluirUsuario(PREPConexao conexaoPrep, DadosUsuario dadosUsuario)
	{
        int retorno = Definicoes.RET_OPERACAO_OK;
        
        try
        {
    		String sql_exclusao = "DELETE FROM TBL_PPP_USUARIO WHERE ID_USUARIO=?";
    		String sql_deleta = "DELETE FROM TBL_PPP_GRUPO_USUARIO WHERE ID_USUARIO=?";
    
    		Object paramExclusao[] = {dadosUsuario.getidUsuario()};
		
			conexaoPrep.executaPreparedUpdate(sql_deleta,paramExclusao, super.logId);
			int ret = conexaoPrep.executaPreparedUpdate(sql_exclusao,paramExclusao, super.logId);
            
            if(ret <= 0)
            {
                //Erro na exclusao do usuario
                super.log(Definicoes.WARN, "excluirUsuario", "Usuario '"+dadosUsuario.getidUsuario()+
                        "' nao existe na tabela TBL_PPP_USUARIO. Retorno do banco: " + ret);
                retorno = Definicoes.RET_EXCLUSAO_USUARIO_INEXISTENTE;
            }
		}
		catch (GPPInternalErrorException e)
		{
            produtor.setStatusProcesso(Definicoes.PROCESSO_ERRO);
			super.log(Definicoes.ERRO, "excluirUsuario", "Erro tecnico:" + e);
            retorno = Definicoes.RET_ERRO_TECNICO;
		}

		return retorno;
	}
}