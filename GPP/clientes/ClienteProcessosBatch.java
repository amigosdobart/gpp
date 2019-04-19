package clientes;

import org.omg.CORBA.OBJECT_NOT_EXIST;
import org.omg.CORBA.TRANSIENT;

import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.componentes.processosBatch.orb.*;

/**
 * Esta classe eh o cliente responsavel pela execucao de processos batch. A execucao
 * de processos batch serah somente atraves desse cliente onde o nome do processo batch
 * eh passado como parametro assim como seus parametros e entao um delegate de execucao
 * batch sera utilizado para executar o processo desejado. Este programa espera que os
 * parametros sejam passados da seguinte forma:
 *   Ordem dos Parametros	Nome do Parametro			Exemplos
 * 			0				Porta CORBA					15001
 * 			1				IP CORBA					10.61.176.117
 * 			2				Nome do processo batch		B001_UsuariosShutdown
 * 			3				Data de execucao(Parametro)	22/08/2005
 * 
 * Este programa irah identificar qual o ID do processo batch deverah ser executado atraves
 * do nome curto passado como parametro (3o parametro).
 * 
 * Os possiveis codigos de erro sao retornados abaixo:
 * 
 * 	Codigo Erro				Significado
 * 		1					Sistema GPP esta fora do ar ou o servidor e IP sao invalidos
 * 		2					Durante a execucao o sistema "derrubou" a conexao CORBA estabelecida
 * 		3					Nome do processo batch passado como parametro estah no formato invalido
 * 		4					Numero de parametros minimos nao foram estabelecidos para a execucao do processo batch
 * 		5					Erro na aplicacao (Ver mensagem correspondente)
 * 		6					Erro desconhecido (Ver mensagem correspondente)
 * 
 * @author Joao Carlos
 * Data..: 22/08/2005
 *
 */
public class ClienteProcessosBatch
{
	public ClienteProcessosBatch( )
	{
	}
	
	/**
	 * Metodo....:parseIdProcessoBatch
	 * Descricao.:Realiza o parse do id do processo batch atraves do nome curto
	 * @param nomeCurto - Nome curto do processo batch
	 * @return int		- Id do processo batch
	 */
	public static int parseIdProcessoBatch(String nomeCurto)
	{
		// O Nome curto de um processo batch eh definido da seguinte forma:
		// BXXX_NOME onde X eh o id do processo batch cadastrado no GPP
		// Portanto para utilizacao na execucao do processo eh feito um parse
		// no nome informado retirando a parte do Id para ser repassado para 
		// o delegate. Ex: B001_UsuarioShutdown entao 001 eh o id do processo batch
		// Considerando BXXX_Nome..
		//              01234567... entao da posicao 1 ateh 3 inclusive eh o id
		return Integer.parseInt(nomeCurto.substring(1,4));
	}

	public static void main(String[] args) 
	{
		java.util.Properties props = System.getProperties();
		props.put("vbroker.agent.port", args[0]);
		props.put("vbroker.agent.addr", args[1]);
		System.setProperties ( props );	
		try
		{
			// Inicializando o ORB
			org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, props);
			byte[] managerId = "ComponenteNegociosProcessosBatch".getBytes();
			processosBatch pPOA = processosBatchHelper.bind(orb, "/AtivaComponentesPOA", managerId);

			// Define o ID do processo batch.
			int idProcessoBatch = parseIdProcessoBatch(args[2]);
			// Define os parametros para o processo batch sendo que serao os parametros apos
			// o nome do processo batch. O tamanho do array de parametros sera do mesmo tamanho
			// dos parametros passados como parametro desconsiderando 3 posicoes correspondentes
			// a Porta, IP e Id do processo batch
			String params[] = new String[args.length-3];
			for (int i=0; i < params.length; i++)
				params[i] = args[i+3];

			// Executa o processo batch
			pPOA.executaProcessoBatch(idProcessoBatch,params);
			// Caso o programa execute com erro entao o erro sera tratado
			// no codigo abaixo pelos catch indentificando cada tipo de erro
			// para retornar o codigo de retorno apropriado. Caso nao retorne
			// erro entao o processo retorna o codigo 0
			System.out.println("Processo batch:"+args[2]+" Id:"+idProcessoBatch+" Executado com sucesso.");
			System.exit(0);
		}
		catch (OBJECT_NOT_EXIST o)
		{
			System.out.println("Servidor nao encontrado ou nao estah ativo. IP:"+args[1]+" Porta:"+args[0]);
			System.exit(1);
		}
		catch (TRANSIENT t)
		{
			System.out.println("Sessao terminada pelo servidor");
			System.exit(2);
		}
		catch (NumberFormatException ne)
		{
			System.out.println("Nome do processo batch nao estah no padrao correto. Nome:"+args[2]);
			System.exit(3);
		}
		catch (ArrayIndexOutOfBoundsException ae)
		{
			System.out.println("Faltam parametros para o programa. Ex: ClienteProcessoBatch [Porta] [IP] [IdProcessoBatch] [Parametros[]]");
			System.exit(4);
		}
		catch (GPPInternalErrorException e) 
		{
			System.out.println("Erro na aplicacao. Erro:"+e);
			System.exit(5);
		}
		catch(Exception e)
		{
			System.out.println("Erro desconhecido. Erro:"+e);
			System.exit(6);
		}
	}
}