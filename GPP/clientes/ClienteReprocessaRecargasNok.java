package clientes;

import com.brt.gpp.componentes.recarga.orb.recarga;
import com.brt.gpp.componentes.recarga.orb.recargaHelper;
import com.brt.gpp.comum.Definicoes;

import java.io.DataInput;
import java.io.DataInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

/**
 * 
 * 
 * @author Joao Carlos
 * 
 * 
 */
public class ClienteReprocessaRecargasNok
{
	Connection 	conn 		= null;
	recarga 	recargaPOA 	= null;

	private Date dataInicial;
	private Date dataFinal;
	private String tipTransacao;
	private String idtMsisdn;
	private String sistemaOrigem;
	private int    codigoErro;
	private String tipoRecarga;
	
	public ClienteReprocessaRecargasNok(String porta, String servidor, String tnsNames, String userName, String passWord) throws Exception
	{
		Class.forName("oracle.jdbc.driver.OracleDriver");
		conn = DriverManager.getConnection("jdbc:oracle:oci8:@" + tnsNames, userName, passWord);
		
		java.util.Properties props = System.getProperties();
		props.put("vbroker.agent.port", porta);
		props.put("vbroker.agent.addr", servidor);
		System.setProperties ( props );	
		String args[] = {porta,servidor};
		// Inicializando o ORB
		org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args,props);
		byte[] managerId = "ComponenteNegociosRecarga".getBytes();
		recargaPOA = recargaHelper.bind(orb, "/AtivaComponentesPOA", managerId);
	}

	public static void main(String args[])
	{
		try
		{
			System.out.println ("\n\n");
			System.out.println("Inciando conexao com banco de dados e com o GPP...");
			// Os parametros da chamada do cliente sao as informacoes
			// necessarias para a conexao na base de dados
			ClienteReprocessaRecargasNok reprocessa = new ClienteReprocessaRecargasNok(args[0],args[1],args[2],args[3],args[4]);
			
			System.out.println("Buscando parametros para pesquisa...");
			// Caso a inicializaca da classe e a conexao ao banco de dados tenha sido efetuada
			// entao os parametros da consulta na tabela de recargas nok devem ser definidos
			// Dentro do metodo para buscar os parametros, caso tenha sido recolhido todos os
			// valores entao chama o metodo para executar a consulta na tabela de recargas nok
			reprocessa.buscaParametros();
			System.out.println("Termino do reprocessamento de recargas");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		System.exit(0);
	}

	public void buscaParametros() throws Exception
	{
		System.out.println ("\n");
		System.out.println ("+--------------------------------------------------------+");
		System.out.println ("+  Parametros para Reprocessamento de Recargas com Erro  +");
		System.out.println ("+--------------------------------------------------------+\n");
		System.out.println ("Os parametros a seguir sao obrigatorios");
		System.out.print ("Digite a Data Inicial para a pesquisa: ");
		setDataInicial(read());
		System.out.print ("Digite a Data Final para a pesquisa: ");
		setDataFinal(read());
		System.out.print ("Digite o Tipo de Recarga (R/A): ");
		setTipoRecarga(read());
		System.out.print ("Digite o Codigo de Erro: ");
		setCodigoErro(read());
		System.out.println ("Os parametros a seguir nao sao obrigatorios");
		System.out.print ("Digite o Codigo do Sistema de Origem: ");
		setSistemaOrigem(read());
		System.out.print ("Digite o Tipo de Transacao: ");
		setTipTransacao(read());
		System.out.print ("Digite o MSISDN do assinante: ");
		setIdtMsisdn(read());

		reprocessaRecargas();
	}

	private void reprocessaRecargas() throws Exception
	{
		int numRecargasOk 	 = 0;
		int numRecargasNok 	 = 0;
		int numRecargasTotal = 0;
		
		String sql =    "select id_recarga, idt_msisdn, tip_transacao, id_tipo_credito, id_valor " +
						      ",dat_recarga, nom_operador, id_tipo_recarga, idt_cpf , num_hash_cc " +
						      ",idt_loja, dat_banco, dat_contabil, idt_terminal, tip_terminal " +
						      ",idt_nsu_instituicao, id_origem, id_sistema_origem, id_canal, vlr_pago " +
						      ",vlr_credito_principal ,vlr_credito_bonus ,vlr_credito_sms, vlr_credito_gprs " +
						      ",num_dias_exp_principal, num_dias_exp_bonus, num_dias_exp_sms, num_dias_exp_gprs " +
						  "from tbl_rec_recargas_nok " +
						 "where dat_recarga    >= ? " +
						   "and dat_recarga    <= ? " +
						   "and idt_erro        = ? " +
						   "and id_tipo_recarga = ? " +
						   "and (idt_msisdn        = ? or (? is null and idt_msisdn        is not null)) " +
						   "and (tip_transacao     = ? or (? is null and tip_transacao     is not null)) " +
						   "and (id_sistema_origem = ? or (? is null and id_sistema_origem is not null))";
		
		// Define os valores dos parametros na consulta preparada
		PreparedStatement pstmt = preparaSQL(sql);
		ResultSet rs = pstmt.executeQuery();
		// Faz a navegacao dos registros retornados para entao executar a recarga para cada um deles
		SimpleDateFormat frmDataRec = new SimpleDateFormat("yyyyMMddHHmmss");
		while (rs.next())
		{
			String idRecarga  = null;
			short  codRetorno = Definicoes.RET_ERRO_TECNICO;
			try
			{
				if (Definicoes.TIPO_RECARGA.equals(getTipoRecarga()))
				{
					idRecarga = rs.getString("id_recarga");
					codRetorno = recargaPOA.executaRecarga( rs.getString("idt_msisdn")
														   ,rs.getString("tip_transacao")
														   ,idRecarga
														   ,rs.getString("id_tipo_credito")
														   ,rs.getDouble("id_valor")
														   ,frmDataRec.format(Calendar.getInstance().getTime())
														   ,rs.getString("id_sistema_origem") 	!= null ? rs.getString("id_sistema_origem")  : ""
														   ,rs.getString("nom_operador")		!= null ? rs.getString("nom_operador") 		 : ""
														   ,rs.getString("idt_nsu_instituicao")	!= null ? rs.getString("idt_nsu_instituicao"): ""
														   ,rs.getString("num_hash_cc")			!= null ? rs.getString("num_hash_cc") 		 : ""
														   ,rs.getString("idt_cpf")				!= null ? rs.getString("idt_cpf")	 		 : ""
											              );

					// Altera as variaveis de contadores para resumo no final
					numRecargasOk  = numRecargasOk  + (codRetorno == Definicoes.RET_OPERACAO_OK ? 1 : 0);
					numRecargasNok = numRecargasNok + (codRetorno != Definicoes.RET_OPERACAO_OK ? 1 : 0);
				}
			}
			catch(Exception e)
			{
				System.out.println("Erro ao realizar a recarga de id:"+idRecarga+" Erro:"+e.toString());
				numRecargasNok++;
			}
			finally
			{
				numRecargasTotal++;
			}
		}
		imprimeResumo(numRecargasTotal,numRecargasOk,numRecargasNok);
	}

	private void imprimeResumo(int numRecTot, int numRecOk, int numRecNok)
	{
		System.out.println ("\n");
		System.out.println ("+---------------------------------------------------------+");
		System.out.println ("+  Resumo do Reprocessamento de Recargas com Erro         +");
		System.out.println ("+---------------------------------------------------------+\n");
		System.out.println ("Numero total de recargas a serem reprocessadas    : "+numRecTot);
		System.out.println ("Numero total de recargas reprocessadas com sucesso: "+numRecOk);
		System.out.println ("Numero total de recargas reprocessadas com erro   : "+numRecNok);
	}

	private PreparedStatement preparaSQL(String sql) throws SQLException
	{
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setTimestamp	(1, new Timestamp(getDataInicial().getTime())	);
		pstmt.setTimestamp	(2, new Timestamp(getDataFinal().getTime())		);
		pstmt.setInt		(3, getCodigoErro()								);
		pstmt.setString		(4, getTipoRecarga()							);
		pstmt.setNull		(5, java.sql.Types.VARCHAR						);
		pstmt.setNull		(6, java.sql.Types.VARCHAR						);
		pstmt.setNull		(7, java.sql.Types.VARCHAR						);
		pstmt.setNull		(8, java.sql.Types.VARCHAR						);
		pstmt.setNull		(9, java.sql.Types.VARCHAR						);
		pstmt.setNull		(10,java.sql.Types.VARCHAR						);

		// Para os parametros nao obrigatorios verifica se o mesmo possui valor
		if (getIdtMsisdn() != null)
		{
			pstmt.setString(5, getIdtMsisdn());
			pstmt.setString(6, getIdtMsisdn());
		}
		
		if (getTipTransacao() != null)
		{
			pstmt.setString(7, getTipTransacao());
			pstmt.setString(8, getTipTransacao());
		}
		
		if (getSistemaOrigem() != null)
		{
			pstmt.setString(9,  getSistemaOrigem());
			pstmt.setString(10, getSistemaOrigem());
		}

		return pstmt;
	}

	private String read() throws Exception
	{
		DataInput di = new DataInputStream(System.in);
		return di.readLine();
	}
	
	// Seguem os metodos para definir e buscar os parametros escolhidos pelo usuario
	private Date getDataInicial()
	{
		return dataInicial;
	}

	private Date getDataFinal()
	{
		return dataFinal;
	}
	
	private String getIdtMsisdn()
	{
		return idtMsisdn;
	}

	private String getSistemaOrigem()
	{
		return sistemaOrigem;
	}

	private String getTipoRecarga()
	{
		return tipoRecarga;
	}

	private String getTipTransacao()
	{
		return tipTransacao;
	}

	private int getCodigoErro()
	{
		return codigoErro;
	}
	
	private void setDataInicial(String dataInicial)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		try
		{
			this.dataInicial = sdf.parse(dataInicial+" 00:00:00");
		}
		catch(ParseException pe)
		{
			throw new IllegalArgumentException("Data Inicial: "+dataInicial+" esta no formato invalido. Formato correto dd/mm/yyyy.");
		}
	}
	
	private void setIdtMsisdn(String idtMsisdn)
	{
		if (idtMsisdn.trim().equals(""))
			this.idtMsisdn = null;

		if (!idtMsisdn.equals("") && idtMsisdn.length() != 12)
			throw new IllegalArgumentException("MSISDN: "+idtMsisdn+" deve possui 12 numeros");
		else
			this.idtMsisdn = idtMsisdn;
	}

	private void setSistemaOrigem(String sistemaOrigem)
	{
		if (sistemaOrigem.trim().equals(""))
			this.sistemaOrigem = null;
		else
			this.sistemaOrigem = sistemaOrigem;
	}

	private void setTipoRecarga(String tipoRecarga)
	{
		if (!Definicoes.TIPO_AJUSTE.equals(tipoRecarga.toUpperCase()) && !Definicoes.TIPO_RECARGA.equals(tipoRecarga.toUpperCase()))
			throw new IllegalArgumentException("Tipo de Recarga: "+tipoRecarga+" nao e valido. Valido somente 'A'-Ajuste ou 'R'-Recarga");

		this.tipoRecarga = tipoRecarga.toUpperCase();
	}
	
	private void setTipTransacao(String tipTransacao)
	{
		if (tipTransacao.trim().equals(""))
			this.tipTransacao = null;
		
		if (!tipTransacao.equals("") && tipTransacao.length() != 5 )
			throw new IllegalArgumentException("Tipo de transacao: "+tipTransacao+" deve possuir 5 numeros");
		else
			this.tipTransacao = tipTransacao;
	}

	private void setDataFinal(String dataFinal)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		try
		{
			this.dataFinal = sdf.parse(dataFinal+" 23:59:59");
		}
		catch(ParseException pe)
		{
			throw new IllegalArgumentException("Data Final: "+dataFinal+" esta no formato invalido. Formato correto dd/mm/yyyy.");
		}
	}

	private void setCodigoErro(String codigoErro)
	{
		try
		{
			this.codigoErro = Integer.parseInt(codigoErro);
		}
		catch(Exception e)
		{
			throw new IllegalArgumentException("Codigo de Erro: "+codigoErro+" nao e um numero valido.");
		}
	}
}
