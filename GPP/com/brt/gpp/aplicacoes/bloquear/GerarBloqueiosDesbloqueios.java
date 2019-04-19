package com.brt.gpp.aplicacoes.bloquear;

// importa do gpp
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.ManipuladorArquivos;
import com.brt.gpp.aplicacoes.*;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao; 
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

// imports de java
import java.io.File;
import java.sql.ResultSet;
import java.util.*;
import java.io.IOException;
import java.sql.SQLException;

public class GerarBloqueiosDesbloqueios extends Aplicacoes
{
	// Atributos da classe
	String dirFileBloqDesbloq;
	MapConfiguracaoGPP mapConfigGPP;
	
	/**
	 * Metodo...: GerarBloqueiosDesbloqueios
	 * Descricao: Contrutor
	 * @param 	String	_diretorioArquivoBloqueios	Diretorio do Arquivo
	 * @param 	long	idLog						Id do Log
	 */
	public GerarBloqueiosDesbloqueios(String _diretorioArquivoBloqueios, long idLog)
	{
		super(idLog, Definicoes.CL_GERAR_BLOQUEIO_DESBLOQUEIO);
		
		this.dirFileBloqDesbloq = _diretorioArquivoBloqueios;
	}
	
	/**
	 * Metodo...: getSolicitacoes
	 * Descricao: Busca as solicita��es de bloqueio/desbloqueio no arquivo de bloqueios/desbloqueios
	 * @return	HashMap	Hash contendo msisdn e indica��o de bloqueio(B)/desbloqueio(D)
	 */
	public HashMap getSolicitacoes() throws GPPInternalErrorException
	{
		HashMap hashRetorno = new HashMap();
		this.mapConfigGPP = MapConfiguracaoGPP.getInstancia();
		
		super.log(Definicoes.DEBUG,"gerSolicitacaoes","Lendo arquivo disponibilizados para o gpp");
		// Pegando a lista dos arquivos que constam no diret�rio
		File f = new File(this.dirFileBloqDesbloq);
		if (!f.isDirectory())
			throw new GPPInternalErrorException (this.dirFileBloqDesbloq + " nao e um diretorio valido.");
					
		File arquivos[] = f.listFiles();
		
		// Coloda todos os arquivos num TreeMap, ordenando por data de cria��o
		TreeMap arqsBloqueioDesbloqueio = new TreeMap();
		for(int k=0; k<arquivos.length;k++)
		{
			arqsBloqueioDesbloqueio.put(new Long(arquivos[k].lastModified()), arquivos[k]);			
		}
		
		// Verifica se h� necessidade de desprezar alguns arquivos
		int qtdMaxArquivos = Integer.parseInt(mapConfigGPP.getMapValorConfiguracaoGPP("LIMITE_ARQUIVOS_BLOQUEIO"));
		int numeroArquivosDesprezar = 0;
		if(arquivos.length > qtdMaxArquivos)
		{
			numeroArquivosDesprezar = arquivos.length - qtdMaxArquivos;
		}
		
		// Varre o TreeMap de arquivos a partir do que tem menor data de cria��o
		long nroArquivosProcessados = 0;
		for(Iterator it = arqsBloqueioDesbloqueio.values().iterator();it.hasNext();)
		{
			// Pega o primeiro arquivo do diret�rio
			File arqBloqueio = (File) it.next();
			
			// Quando o n�mero de arquivos lidos do treeMap for maior que o n�mereo de arquivos
			// A serem desprezados, come�o a montar o hash de bloqueios/desbloqueios
			if(nroArquivosProcessados >= numeroArquivosDesprezar )
			{
				// Adiciona solicita��es de Bloqueio no Hash de retorno
				hashRetorno = this.montaHashBloqueioDesbloqueio(hashRetorno, arqBloqueio);
			}
			
			// Transfere arquivo para diret�rio de Processados
			super.log(Definicoes.DEBUG,"montaHashBloqueioDesbloqueio","Movendo Arquivo: "+arqBloqueio.getName());
			int retRename = ManipuladorArquivos.moveArquivo(arqBloqueio, 
											mapConfigGPP.getMapValorConfiguracaoGPP("DIR_HISTORICO_BLOQUEIO") +
											System.getProperty("file.separator") +  
											arqBloqueio.getName());
			
			if(retRename != 0)
			{
				super.log(Definicoes.ERRO,"montaHashBloqueioDesbloqueio","Erro ao mover arquivo "+ arqBloqueio + " para historico");
			}
			nroArquivosProcessados++;
		}
		
		// Acrescenta alguns dos assinantes que deveriam estar bloqueados segundo a importa��o
		// de assinantes mas n�o est�o
		hashRetorno = this.acrescentaAssinantesDivergentes(hashRetorno);
		
		return hashRetorno;
	}
	
	/**
	 * Metodo...: montaHashBloqueioDesbloqueio
	 * Descricao: Adiciona a um HashMap as Solicita��es de Bloqueio derivadas da an�lise
	 * 				de um arquivo com usu�rios e seus respectivos saldos
	 * @param 	HashMap	hashSolicitacoes		Hash que carregar� as solicita��es
	 * @param 	File	arqUsuariosParaBloquear	Arquivo texto contendo 
	 */
	private HashMap montaHashBloqueioDesbloqueio(HashMap hashSolicitacoes, File arqUsuariosParaBloquear) throws GPPInternalErrorException
	{
		ManipuladorArquivos manipularArquivo = null;
		HashMap retorno = new HashMap();
		retorno = hashSolicitacoes;
		
		try
		{
			super.log(Definicoes.DEBUG,"montaHashBloqueioDesbloqueio","Abre arquivo: "+arqUsuariosParaBloquear.getName());
			manipularArquivo = new ManipuladorArquivos(	mapConfigGPP.getMapValorConfiguracaoGPP("DIR_ARQUIVOS_BLOQUEIO")+
														System.getProperty("file.separator")+
														arqUsuariosParaBloquear.getName(), false, super.getIdLog());
			
			// Inicializa vari�vel que carregar� os dados dos bloqueios
			SolicitacaoBloqueio sB = null;

			// Verifica se trata-se de um arquvio de bloqueios por RE
			if(arqUsuariosParaBloquear.getName().startsWith("RE"))
			{
				String msisdn = manipularArquivo.leLinha();
				while(msisdn!=null)
				{
					// Insere Registro de bloqueio para esse acesso no hash
					sB = new SolicitacaoBloqueio(msisdn, "Bloquear", Definicoes.SERVICO_FREE_CALL);
					retorno.put(sB.getMsisdn() + sB.getServico() , sB);
					msisdn = manipularArquivo.leLinha();
				}
			}
			else
			{
				// Campos do arquivo
				String msisdn = null;
				String saldoPrincipal = null;
				String saldoBonus = null;
				String saldoSms = null;
				String saldoDados = null;
				String planoPreco = null;
				
				String registroArquivo = manipularArquivo.leLinha();
				int planoLigmix	   = this.consultarPlanosLigMix();
				while(registroArquivo!=null)
				{
					// Extrai cada um dos campos do registro lido do arquivo
					super.log(Definicoes.DEBUG,"montaHashBloqueioDesbloqueio","Quebrando registro: "+registroArquivo);
					String[] st = registroArquivo.split(";");
					
					msisdn 			= st[0]; 			
					saldoPrincipal 	= st[1]; 			
					saldoBonus 		= st[2];			
					saldoSms 		= st[3]; 			
					saldoDados 		= st[4]; 			
					planoPreco		= st[5];
					
					// Verifica se h� algum saldo n�o nulo e se n�o se trata de um ligmix
					if(	(Double.parseDouble(saldoPrincipal)!= 0 ||	Double.parseDouble(saldoBonus)!= 0 ||
						 Double.parseDouble(saldoSms)	   != 0	||	Double.parseDouble(saldoDados)!= 0) &&
						Integer.parseInt(planoPreco)!= planoLigmix)
					{
						// Se, pelo menos um dos saldos n�o for nulo, o CDR � v�lido
						// Verifica se FREE_CALL deve ser bloqueado/desbloqueado
						if(Double.parseDouble(saldoPrincipal) < Double.parseDouble(this.mapConfigGPP.getMapValorConfiguracaoGPP("LIMITE_MINIMO_CREDITO_BLOQUEIO")))
						{
							// FREE_CALL deve ser bloqueado
							sB = new SolicitacaoBloqueio(msisdn, "Bloquear", Definicoes.SERVICO_FREE_CALL);
						}
						
						if(Double.parseDouble(saldoPrincipal) > Double.parseDouble(this.mapConfigGPP.getMapValorConfiguracaoGPP("LIMITE_MAXIMO_CREDITO_DESBLOQUEIO")))
						{
							// FREE_CALL deve ser desbloqueado
							sB = new SolicitacaoBloqueio(msisdn, "Desbloquear",Definicoes.SERVICO_FREE_CALL);
						}
						retorno.put(sB.getMsisdn() + sB.getServico() , sB);
						
	/***************
	 * **BLACK_LIST n�o deve mais ser bloqueado
	 					// Verifica se BLACK_LIST deve ser bloqueado/desbloqueado
						if(Double.parseDouble(saldoPrincipal) + Double.parseDouble(saldoDados) < Double.parseDouble(this.mapConfigGPP.getMapValorConfiguracaoGPP("LIMITE_MINIMO_CREDITO_BLOQUEIO_BLK_LIST")))
						{
							// FREE_CALL deve ser bloqueado
							sB = new SolicitacaoBloqueio(msisdn, "Bloquear", Definicoes.SERVICO_BLACK_LIST);
						}
						else
						{
							// FREE_CALL deve ser desbloqueado
							sB = new SolicitacaoBloqueio(msisdn, "Desbloquear",Definicoes.SERVICO_BLACK_LIST);
						}			
						retorno.put(sB.getMsisdn() + sB.getServico() , sB);
						***********/
					}
					
					// Le pr�ximo registro
					registroArquivo = manipularArquivo.leLinha();
				}
			}
		}
		catch (IOException ioE)
		{
			super.log(Definicoes.ERRO, "montaHashBloqueioDesbloqueio","Erro ao ler arquivo: "+arqUsuariosParaBloquear.getName());
		}
		finally
		{
			// Fecha Arquivo Lido
			manipularArquivo.fechaArquivo();
		}
		
		return retorno;
	}
	
	/**
	 * Descricao: Metodo que retorna os planos associados ao LigMix
	 * Metodo...: REtorna o plano LigMix 
	 * @return	int		C�digo do Plano LigMix
	 * @throws GPPInternalErrorException
	 */
	private int consultarPlanosLigMix() throws GPPInternalErrorException
	{
	    int planoLigMix=0;

		    PREPConexao conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
		    String sql ="SELECT IDT_PLANO_PRECO	" +
		    			"FROM TBL_GER_PLANO_PRECO	" +
		    			"WHERE IDT_CATEGORIA = " +Definicoes.COD_CAT_LIGMIX;
		    try
		    {
		    ResultSet rs = conexaoPrep.executaQuery(sql,super.getIdLog()); 
		    if(rs.next())
		    {
		        planoLigMix = rs.getInt("IDT_PLANO_PRECO");
		    }
		    }catch (SQLException e)
		    {
				super.log(Definicoes.ERRO, "Consultar planos LigMix", "Excecao SQL:"+ e);	
		    }
		    finally
			{
				// Libera conexao do banco de dados
				this.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
			}
		return planoLigMix;
	}
	
	/**
	 * Metodo...: acrescentaAssinantesQuiabo
	 * Descricao: Procura por assinantes que deveriam estar bloqueados e n�o est�o
	 * 				mas n�o manda tudo de uma vez s�, para  poupar o ASAP
	 * @param 	HashMap		hashBloqueios	hash acumulador de solicita��es de bloqueio
	 * @return	HashMap		Hash com mais solicita��es acumuladas
	 */
	private HashMap acrescentaAssinantesDivergentes(HashMap hashBloqueios)
	{
		PREPConexao conexaoPrep = null;
		double limiteMinimo = Double.parseDouble(this.mapConfigGPP.getMapValorConfiguracaoGPP("LIMITE_MINIMO_CREDITO_BLOQUEIO"));
		
		try
		{
			// Busca uma conexao de banco de dados		
			conexaoPrep = this.gerenteBancoDados.getConexaoPREP(super.getIdLog());
			
			//Selecionar os que deveriam ter sido bloqueados
			//pois encontram-se com saldo menor que o limite m�nimo e n�o fizeram recarga hoje
			String sqlBloqueios = 
				"select msisdn from "+
				"( "+
				"	select sub_id as msisdn from tbl_apr_assinante_tecnomen "+
				"	where dat_importacao = trunc(sysdate) "+
				"	and account_status = 2 "+
				"	and account_balance/1000000 < ? "+		// Parametro 0
				") ass "+
				"where not exists "+
				"( "+
				"	select idt_msisdn msisdn from tbl_apr_bloqueio_servico "+
				"	where id_servico = 'ELM_FREE_CALL' "+
				"	and idt_msisdn = ass.msisdn "+
				") "+
				"and not exists "+
				"(	"+
				"	select idt_msisdn msisdn from tbl_Rec_recargas rec "+
				"	where rec.id_tipo_recarga = 'R' "+
				"	and dat_recarga between trunc(sysdate) and sysdate "+
				"	and idt_msisdn = ass.msisdn "+
				")";
				
			Object parametros[] = {	new Double(limiteMinimo) };		// Parametro 0

			
			ResultSet rsBloqueios = conexaoPrep.executaPreparedQuery(sqlBloqueios, parametros, super.getIdLog());
			
			// Insere bloqueios referentes aos registros acima no hash que acumula os bloqueios
			long qtdBloqueiosRebarba = 0;
			SolicitacaoBloqueio sB;
			
			// Pega, no m�ximo, QUANTUM_BLOQUEIOS_ATRASADOS dos registros acima, para que n�o sobrecarregue
			// o Sistema de Aprovisionamento
			while (rsBloqueios.next() && qtdBloqueiosRebarba < Long.parseLong(mapConfigGPP.getMapValorConfiguracaoGPP("QUANTUM_BLOQUEIOS_ATRASADOS")) ) 
			{
				sB = new SolicitacaoBloqueio(rsBloqueios.getString("msisdn"), "Bloquear", Definicoes.SERVICO_FREE_CALL);
				hashBloqueios.put(sB.getMsisdn() + sB.getServico() , sB);
				qtdBloqueiosRebarba ++;
			}
			
			super.log(Definicoes.INFO,"acrescentaAssinantesDivergentes","Assinantes Divergentes: "+qtdBloqueiosRebarba);
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO,"acrescentaAssinantesQuiabo","Erro no Processamento Bloqueios Atrasados: "+e);
		}
		finally
		{
			// Libera conexao do banco de dados
			this.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
		}
		
		return hashBloqueios;
	}
}
