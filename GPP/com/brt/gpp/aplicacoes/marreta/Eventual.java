package com.brt.gpp.aplicacoes.marreta;

import com.brt.gpp.aplicacoes.*;
/*	
import com.brt.gpp.comum.*;
import com.brt.gpp.comum.gppExceptions.*;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.comum.conexoes.bancoDados.*;
import com.brt.gpp.aplicacoes.aprovisionar.*;

import java.io.IOException;
import java.sql.*;
*/

public class Eventual extends Aplicacoes
{
	/*
	protected GerentePoolBancoDados	gerenteBancoDados = null; 	
	*/
	public Eventual(long idProcesso)
	{
		super(idProcesso, "EVENTUAL");
	/*	
		// Obtem referencia do gerente de conexoes do Banco de Dados
		this.gerenteBancoDados = GerentePoolBancoDados.getInstancia(super.getIdLog());
	*/
	}
	/*	
	public void start(String caminhoNomeArquivo)
	{
		ManipuladorArquivos fileHandler = null;
		ManipuladorArquivos fileOut = null;
		String msisdn;
		String linha = null;
		PREPConexao conexaoPrep = null;
		
		try
		{
			// Busca uma conexao de banco de dados		
			conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
			
			fileHandler = new ManipuladorArquivos(caminhoNomeArquivo, false, super.getIdLog());
			fileOut = new ManipuladorArquivos("c:\\Bloquear",true,super.getIdLog());
			
			linha = fileHandler.leLinha();
			while(linha != null)
			{
				msisdn = this.parseLinha(linha)[0];

				String sql = "SELECT IDT_MSISDN FROM TBL_APR_BLOQUEIO_SERVICO WHERE IDT_MSISDN = ?";
				Object[] par = {msisdn};
				
				ResultSet rs = conexaoPrep.executaPreparedQuery(sql, par, super.getIdLog());
				
				if(rs.next())
				{
					fileOut.escreveLinha(msisdn);
				}
				linha = fileHandler.leLinha();			
			}
		}
		catch(GPPInternalErrorException gppE)
		{
			super.log(Definicoes.ERRO,"start","Erro:"+gppE	);
		}
		catch(SQLException sqlE)
		{
			super.log(Definicoes.ERRO,"start","Erro sql: "+sqlE);
		}
		catch(IOException ioE)
		{
			super.log(Definicoes.ERRO,"start","Erro ao Ler Arquivo (ultima linha foi): "+linha+":"+ioE);
		}
		finally
		{
			gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
			fileOut.fechaArquivo();
			fileHandler.fechaArquivo();
		}
	}
	
	private String[] parseLinha(String linhaRecarga)
	{
		String[] retorno = {linhaRecarga};
		return retorno;
	}
	
	public void ativaTeleMag(String caminhoNomeArquivo)
	{
		String dadosAtivacao[] = {"",""};
		try
		{
			ManipuladorArquivos fileHandler = new ManipuladorArquivos(caminhoNomeArquivo, false, super.getIdLog());
			
			Aprovisionar prov = new Aprovisionar(super.getIdLog());
			
			String linha = fileHandler.leLinha();
			while (linha != null)
			{
				dadosAtivacao = linha.split(";");
				
				// Ativa Assinante
				prov.ativaAssinante(dadosAtivacao[0], dadosAtivacao[0], "1", (double)10, (short)4);
				
				// Reset de Senha
				prov.resetSenha(dadosAtivacao[0], dadosAtivacao[1]);
				super.log(Definicoes.INFO, "ativaTeleMag", "Ativado: "+dadosAtivacao[0]);
			}
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO,"ativaTeleMag","Erro na ativacao: "+dadosAtivacao[0]);
		}
	}
	*/
	
}
