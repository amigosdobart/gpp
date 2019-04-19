package com.brt.gpp.aplicacoes.enviarUsuariosStatus;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.interfaceEscrita.ArquivoInterfaceConfig;
import com.brt.gpp.comum.interfaceEscrita.InterfaceConfiguracao;
import com.brt.gpp.comum.interfaceEscrita.InterfaceEscrita;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
/**
 * Processo batch responsavel por Retornar usuarios que mudaram de status de acordo com uma
 * data especifica. Tal processo retorna os valores num modelo Arquivo de Interface criado
 * pelo GeradorArquivoInterface.
 * Os parametros para este Processo sao:
 * <ul>
 * <li><b>INTERFACE</b>
 * <li><b>DATA</b>
 * <li><b>STATUS ANTERIOR</b>
 * <li><b>STATUS ATUAL</b>
 * <li><b>NOME DO ARQUIVO</b>
 * <li><b>PATH DE SAIDA*</b>
 * </ul>
 * Lembrando que os parametros deverao ser passados na ordem especificada e o PATH DE SAIDA eh opcional.
 * <p>
 * @author Leone Parise Vieira da Silva
 * @since  27/09/2007
 */
public class EnvioUsuariosStatusProdutor extends Aplicacoes implements ProcessoBatchProdutor
{
	private String 		dataProcessamento;
	private int			numRegistros;
	private String		statusProcesso = Definicoes.PROCESSO_SUCESSO;
	private PREPConexao conexaoPrep;
	private ResultSet 	listaUsuarios;
	private InterfaceConfiguracao arquivoInterfaceConfig;
	private InterfaceEscrita      arquivoInterfaceEscrita;

	// Parametros Recebidos
	private String interfaceBrt;
	private String data;
	private String statusAnterior;
	private String statusAtual;
	private String nomeArquivo;
	private String pathArquivo;



	public EnvioUsuariosStatusProdutor(long logId)
	{
		super(logId,Definicoes.CL_ENVIO_USUARIO_STATUS_PRODUTOR);
	}

	public PREPConexao getConexao()
	{
		return conexaoPrep;
	}
	/**
	 * Valida os parametros fornecidos.
	 *
	 * @param params Parametros passados no momento da execucao.
	 * @throws GPPInternalErrorException Caso ocorra alguma excessao na validacao dos parametros
	 * @return String contendo a lista dos valores.
	 */
	private String validarParametros(String[] params) throws GPPInternalErrorException
	{
		if(params == null || params.length < 5)
			throw new GPPInternalErrorException("Os cinco primeiros parametros sao necessarios para a " +
					"execucao do processo batch");
		interfaceBrt	= params[0];
		data			= params[1];
		statusAnterior	= params[2];
		statusAtual		= params[3];
		nomeArquivo		= params[4];

		// Verifica se o nome da interface contem menos que 30 caracteres
		if(!(interfaceBrt.length() <= 30))
			throw new GPPInternalErrorException("O primeiro parametro (INTERFACE)  para a" +
					" execucao do processo batch");

		//Verifica se a data pertence ao padrao YYYYMMDD
		if(!data.matches("\\d{8}"))
			throw new GPPInternalErrorException("O segundo parametro (DATA) deve ser valido (AAAAMMDD) " +
					"para a execucao do processo batch");

		//Verifica se o status anterior contem um unico digito
		if(!statusAnterior.matches("\\d{1}"))
			throw new GPPInternalErrorException("O terceiro parametro (STATUS ANTERIOR) deve ser valido " +
					"(Numero) para a execucao do processo batch");

		//Verifica se o status atual contem um unico digito
		if(!statusAtual.matches("\\d{1}"))
			throw new GPPInternalErrorException("O quarto parametro (STATUS ATUAL) deve ser valido " +
					"(Numero) para a execucao do processo batch");

		// Verifica o diretorio de saida (Caso exista)
		if(params.length == 6 && params[5] != null)
		{
			pathArquivo =  params[5];
			// Diretorios absolutos devem comecar com "/" ou "C:" onde 'C' pode ser qualquer letra
			if(!( pathArquivo.matches("^/.+") || pathArquivo.matches("^[A-Za-z]:\\\\.+") ))
				throw new GPPInternalErrorException("O quinto parametro (PATH) deve ser valido (Absoluto)" +
						" para a execucao do processo batch");
		}

		StringBuffer sb = new StringBuffer();
		sb.append("[ ");
		for(int i = 0; i < params.length; i++)
		{
			if(params[i] == null)
				continue;

			sb.append(params[i]);
			if(i < params.length-1)
				sb.append(", ");
		}
		sb.append(" ]");

		return sb.toString();
	}
	/**
	 * Recupera a lista de usuarios para serem gravados no arquivo de saida
	 *
	 * @param params Parametros passados no momento da execucao.
	 */
	public void startup(String[] params) throws Exception
	{
		String resultado = validarParametros(params);

		conexaoPrep = GerentePoolBancoDados.getInstancia(super.getIdLog()).getConexaoPREP(super.getIdLog());

		dataProcessamento = new SimpleDateFormat(Definicoes.MASCARA_TIMESTAMP).format(Calendar.getInstance().getTime());

		super.log(Definicoes.INFO, "EnvioUsuariosStatus",
				"Iniciando processo Batch EnvioUsuarioStatus. Parametros: "+resultado);

		String sql 	=	"SELECT																"+
						"	a.sub_id           AS MSISDN,									"+
						"	a.account_status   AS STATUS									"+
						"FROM               												"+
						"	tbl_apr_assinante_tecnomen a,    								"+
						"	tbl_apr_assinante_tecnomen b,        							"+
						"	tbl_ger_plano_preco c                     						"+
						"WHERE a.sub_id = b.sub_id											"+
						"  AND a.dat_importacao = TO_DATE(?, 'YYYYMMDD')					"+
						"  AND b.dat_importacao = TO_DATE(?, 'YYYYMMDD') - 1	 			"+
						"  AND a.account_status = ? 										"+
						"  AND b.account_status = ?											"+
						"  AND a.profile_id = c.idt_plano_preco								"+
						"  AND c.idt_categoria = ?											";

		Object SQLParams[] = {data,
						  	  data,
						  	  new Integer(statusAtual),
						  	  new Integer(statusAnterior),
						  	  new Integer(Definicoes.CATEGORIA_PREPAGO)};

		listaUsuarios = conexaoPrep.executaPreparedQuery(sql, SQLParams, super.getIdLog());

		arquivoInterfaceConfig = new ArquivoInterfaceConfig(ArquivoInterfaceConfig.REMESSA,
				interfaceBrt, Definicoes.SO_GPP, nomeArquivo);

		((ArquivoInterfaceConfig)arquivoInterfaceConfig).setDelimitado(true);

		arquivoInterfaceEscrita = arquivoInterfaceConfig.getInterfaceEscrita();
		arquivoInterfaceEscrita.abrir();
	}
	/**
	 * Recupera o proximo usuario da lista
	 *
	 * @return Object Usuario a ser escrito no arquivo
	 */
	public Object next() throws Exception
	{
		UsuariosStatusVO usuario = null;
		try
		{
			// Pega o proximo registro no resultSet e cria o VO que ira armazenar seus dados
			if (listaUsuarios.next())
			{
				usuario = new UsuariosStatusVO();
				usuario.setMsisdn(listaUsuarios.getString("MSISDN"));
				usuario.setStatus(parseUsuarioStatus(listaUsuarios.getInt("STATUS")));
				usuario.setData(data);
				numRegistros++;
			}
		}
		catch(SQLException se)
		{
			super.log(Definicoes.ERRO,"next","Erro ao processar proximo registro no produtor. Erro"+se);
			statusProcesso = Definicoes.PROCESSO_ERRO;
		}
		return usuario;
	}
	/**
	 * Obtem a descricao do Status fornecido.
	 *
	 * @param status	<code>int</code> representando o status do usuario
	 * @return			<code>String</code> contendo a descricao do Status
	 */
	private String parseUsuarioStatus(int status)
	{
		if(status == Definicoes.STATUS_FIRST_TIME_USER)
			return Definicoes.S_FIRST_TIME_USER;

		if(status == Definicoes.STATUS_NORMAL_USER)
			return Definicoes.S_NORMAL;

		if(status == Definicoes.STATUS_RECHARGE_EXPIRED)
			return Definicoes.S_RECHARGE_EXPIRED;

		if(status == Definicoes.STATUS_DISCONNECTED)
			return Definicoes.S_DISCONNECTED;

		if(status == Definicoes.STATUS_SHUTDOWN)
			return Definicoes.S_SHUT_DOWN;

		return null;
	}
	/**
	 * Finaliza conexoes com o banco e fecha o arquivo
	 */
	public void finish() throws Exception
	{
		//Fecha Arquivo de Interface
		if(arquivoInterfaceEscrita != null)
		{
			if(statusProcesso.equals(Definicoes.PROCESSO_SUCESSO))
			{
				if(arquivoInterfaceConfig.getAtributo("observacao") != null)
					arquivoInterfaceConfig.setAtributo("observacao", "Processo executado com SUCESSO!");
				arquivoInterfaceEscrita.fechar();
			}
			else
			{
				if(arquivoInterfaceConfig.getAtributo("observacao") != null)
					arquivoInterfaceConfig.setAtributo("observacao", "Processo executado com ERRO!");
				arquivoInterfaceEscrita.fechar();
			}
		}

		//Libera a conexao de banco de dados
		GerentePoolBancoDados.getInstancia(super.getIdLog()).liberaConexaoPREP(conexaoPrep,super.getIdLog());
		super.log(Definicoes.DEBUG, "EnvioUsuariosStatus", "Fim");
	}


	public void handleException()
	{

	}

	public String getDataProcessamento()
	{
		return dataProcessamento;
	}

	public String getDescricaoProcesso()
	{
		if(getStatusProcesso().equals(Definicoes.PROCESSO_SUCESSO))
			return "Foram processados " + numRegistros + " registros de usuarios";
		return "Erro ao processar registros";
	}

	public int getIdProcessoBatch()
	{
		return Definicoes.IND_ENVIO_USUARIO_STATUS;
	}

	public String getStatusProcesso()
	{
		return this.statusProcesso;
	}

	public void setStatusProcesso(String status)
	{
		this.statusProcesso = status;
	}

	public InterfaceConfiguracao getArquivoInterfaceConfig()
	{
		return arquivoInterfaceConfig;
	}

	public InterfaceEscrita getArquivoInterfaceEscrita()
	{
		return arquivoInterfaceEscrita;
	}

}