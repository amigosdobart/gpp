package br.com.brasiltelecom;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.wig.entity.cadpre.PessoaClarify;

public class DAOClarify
{
	private InitialContext ictx;
	private Logger log = null;

	public DAOClarify(InitialContext ictx)
	{
		this.ictx = ictx;
		log = Logger.getLogger(this.getClass());
	}

	public PessoaClarify ConsultaMSISDN(String msisdn, Connection conn)
	{
		PessoaClarify Resposta = null;
		//Connection conn = null;
		CallableStatement stmt = null;
		
		try
		{
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date currentTime_1 = new Date();
			String dateString = formatter.format(currentTime_1);

			StringBuffer inXml = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			inXml.append("<mensagem><cabecalho><empresa>BRG</empresa><sistema>SIB</sistema><processo>REL000CONCLIENT</processo>");
			inXml.append("<data>").append(dateString).append("</data><identificador_requisicao>123</identificador_requisicao></cabecalho><conteudo>");
			inXml.append("<![CDATA[<?xml version=\"1.0\" encoding=\"UTF-8\"?><root>");
			inXml.append("<msisdn>").append(msisdn).append("</msisdn>");
			inXml.append("</root>]]></conteudo></mensagem>");
			
			stmt = conn.prepareCall("begin SA.intp_consulta_acesso_movel_xml(?,?); end;");
			stmt.setString(1, inXml.toString());
			stmt.registerOutParameter(2, Types.VARCHAR);
			stmt.execute();
			//stmt.close();

			// Resposta = "1." + stmt.getString(2);
			String xml = stmt.getString(2);
			Resposta = new PessoaClarify();
			Resposta.loadFromXml(xml);
		}
		catch (SQLException e)
		{
			log.error("ConsultaMSISDN", e);
			// Resposta = "2." + e.toString();
		}
		catch (Exception e)
		{
			log.error("ConsultaMSISDN", e);
			// Resposta = "3." + e.toString();
		}
		finally 
		{
			try
			{
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			}
			catch (SQLException e1)
			{
				log.error("ConsultaMSISDN", e1);
				// Resposta = "4." + e1.toString();
			}
			catch (Exception e)
			{
				log.error("ConsultaMSISDN", e);
				// Resposta = "5." + e.toString();
			}
		}

		// if (Resposta.indexOf("<cod_retorno>101</cod_retorno>")>=0)
		// {
		// Resposta = "OK";
		// }

		return Resposta;
	}

	public int criaOS(String msisdn, String cpf, String nome, String endereco, String cep, String pdv, Connection clarifyCadastro)
	{
		CallableStatement callablestatement = null;
		int retorno = 0;
		try
		{
			// Geracao de OS no Clarify COM cadastro do PDV
			callablestatement = clarifyCadastro.prepareCall("begin SA.INTP_SOL_GERACAO_OS_ATIVACAO6(?,?,?,?,?,?,?,?,?,?); end;");
			
			callablestatement.setString(1, msisdn);
			if ("".equals(cpf))
				callablestatement.setString(2, null);
			else
				callablestatement.setString(2, cpf);
			callablestatement.setString(3, nome);
			callablestatement.setString(4, null);
			callablestatement.setString(5, null);
			callablestatement.setString(6, null);
			callablestatement.setString(7, endereco);
			callablestatement.setString(8, cep);
			callablestatement.setString(9, pdv); // Inclusao do PDV na chamada
			callablestatement.registerOutParameter(10, 2);
			callablestatement.execute();
			retorno = callablestatement.getInt(10);
			
			// Chamada da procedure SEM o cadastro do PDV
			/*callablestatement = con.prepareCall("call SA.INTP_SOL_GERACAO_OS_ATIVACAO5(?,?,?,?,?,?,?,?,?)");
			callablestatement.setString(1, msisdn);
			if ("".equals(cpf))
				callablestatement.setString(2, null);
			else
				callablestatement.setString(2, cpf);
			callablestatement.setString(3, nome);
			callablestatement.setString(4, null);
			callablestatement.setString(5, null);
			callablestatement.setString(6, null);
			callablestatement.setString(7, endereco);
			callablestatement.setString(8, cep);
			callablestatement.registerOutParameter(10, 2);
			callablestatement.execute();
			retorno = callablestatement.getInt(9); // Retorno antigo sem o PDV */			
			callablestatement.close();

		}
		catch (SQLException e)
		{
			log.error("criaOS", e);
			retorno = 1;
		}
		catch (Exception e)
		{
			log.error("criaOS", e);
			retorno = 1;
		} finally
		{
			try
			{
				if (callablestatement != null)
					callablestatement.close();
			}
			catch (SQLException e)
			{
				log.error("criaOS", e);
				retorno = 1;
			}
		}

		return retorno;
	}

}
