package com.brt.gpp.aplicacoes.importacaoUsuarioPortalNDS;

import java.io.Reader;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;

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
public class UsuarioPortalNDSProdutor extends Aplicacoes implements ProcessoBatchProdutor{
	
	private PREPConexao conexaoPrep = null;
	private ResultSet rs_pesquisa   = null;
	private int numRegistros        = 0;
	private String status           = null;
	
	public UsuarioPortalNDSProdutor(long logId) 
    {
		super(logId, Definicoes.CL_IMPORTACAO_USUARIOS);	
        status = Definicoes.PROCESSO_SUCESSO;
	}

	public void startup(String[] params) throws Exception
	{
		try
		{
			// Busca uma conexao de banco de dados
			conexaoPrep = GerentePoolBancoDados.getInstancia(super.getIdLog()).getConexaoPREP(super.getIdLog());

			// Faz a pesquisa no banco para saber os dados da importacao de usuarios 
			String sql_pesquisa = "SELECT ID_PROCESSAMENTO, XML_DOCUMENT FROM TBL_INT_PPP_IN  " +
						         " WHERE IDT_EVENTO_NEGOCIO = ? AND IDT_STATUS_PROCESSAMENTO = ? " +
						         //Alteracao realizada por Luciano Vilela em 09/08/2006
						         " ORDER BY ID_PROCESSAMENTO" ;
			
			Object paramPesquisa[]= {Definicoes.IDT_EVT_NEGOCIO_IMPORTACAO_USUARIO_IN, Definicoes.IDT_PROCESSAMENTO_NOT_OK};
	
			rs_pesquisa = conexaoPrep.executaPreparedQuery(sql_pesquisa,paramPesquisa, super.logId);
		} 
        catch (Exception e) 
        {
            this.status = Definicoes.PROCESSO_ERRO;
            super.log(Definicoes.ERRO, "startup", "Excecao ao iniciar o processo de importacao de usuarios do NDS. " + e);
            throw e;
		}
	}

	public Object next() throws Exception
	{
        if (rs_pesquisa == null)
            return null;
        
		if (rs_pesquisa.next())
		{	
			char chr_buffer[] = new char[(int)rs_pesquisa.getClob("XML_DOCUMENT").length()];
			Reader chr_instream = rs_pesquisa.getClob("XML_DOCUMENT").getCharacterStream();
			chr_instream.read(chr_buffer);
            
            Map dados = new HashMap();
            dados.put("ID_PROCESSAMENTO", new Integer(rs_pesquisa.getInt("ID_PROCESSAMENTO")));
            dados.put("XML_DOCUMENT", new String(chr_buffer));
            numRegistros++;
            
            return dados;
		}		
        
		return null;
	}

	public void finish() throws Exception 
    {
		if (rs_pesquisa != null)
			rs_pesquisa.close();

		GerentePoolBancoDados.getInstancia(super.getIdLog()).liberaConexaoPREP(conexaoPrep,super.getIdLog());
	}

	public void handleException() {	}

    public int getIdProcessoBatch() 
    {
        return Definicoes.IND_IMPORTACAO_USUARIOS;
    }

    public String getDescricaoProcesso() 
    {
        return "Foram tratados " + numRegistros + " XMLs de importacao de usuarios.";
    }

    public String getStatusProcesso() 
    {
        return status;
    }

    public void setStatusProcesso(String status) 
    {
        this.status = status;       
    }

    public String getDataProcessamento() 
    {
        return new SimpleDateFormat(Definicoes.MASCARA_DATE).format(Calendar.getInstance().getTime());
    }

    public PREPConexao getConexao() 
    {
        return conexaoPrep;
    }
}
