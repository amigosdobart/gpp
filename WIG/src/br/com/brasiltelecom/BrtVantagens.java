package br.com.brasiltelecom;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.DataSource;

public class BrtVantagens extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private InitialContext ictx = null;
	private String IP;
	private String Porta;
	private String cabecalhowml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?> " +
								  "<!DOCTYPE wml PUBLIC \"-//SmartTrust//DTD WIG-WML 4.0//EN\" " +
								  "\"http://www.smarttrust.com/DTD/WIG-WML4.0.dtd\">";

	public void init(ServletConfig arg0) throws ServletException
	{
		try
		{
			ictx = new InitialContext();
			IP = (String)arg0.getServletContext().getAttribute("ServidorWIG");
			Porta = (String)arg0.getServletContext().getAttribute("PortaServidorWIG");
		}catch (NamingException e)
		{
			e.printStackTrace();
		}
	}
	
	// Metodo para retonar o WML quando surgir uma excecao
	public String getWMLExcecao()
	{
		return cabecalhowml + "<wml>"   +
							  "<card>"  +
							  "	<p>Problema no cadastro. Favor entrar em contato com a Central de Relacionamento com o Cliente.</p>" +
							  "</card>" +
							  "</wml>";
	}
	
	// Metodos para retornar o erro <!--comentado--> apos o WML de excecao
	public String getErro(SQLException e)
	{
		return "<!--Erro: " + e + "-->";
	}
	public String getErro(Exception e)
	{
		return "<!--Erro: " + e + "-->";
	}
	public String getErro(NamingException e)
	{
		return "<!--Erro: " + e + "-->";
	}

	protected void service(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		//String querystring = request.getQueryString();
		String msisdn = request.getParameter("MSISDN");
		/*
		 * Servicos disponiveis:
		 * 
		 *		1  > Menu inicial de "Bonus todo mes:"
		 *		2  > Menu inicial de "Amigos toda hora:"
		 *		w1 > "Cadastro" do Bonus todo mes
		 *		q1 > "Consulta" do Bonus todo mes
		 *		w2 > "Cadastro" do Amigos toda hora
		 *		q2 > "Consulta" do Amigos toda hora
		 * 
		 **/
		String servico = request.getParameter("s");	
		PrintWriter out = response.getWriter();
		Connection con = null;

		if (servico.equals("1"))
			out.print(cabecalhowml + "<wml>" +
									 "<card>" +
									 "	<p>" +
									 "	<select name=\"c\" title=\"Bonus todo mes:\">" +
									 "		<option value=\"w1\">Cadastrar</option>" +
									 "		<option value=\"q1\">Consultar</option>" +
									 "	</select>" +
									 "	<do type=\"accept\">" +
									 "		<go href=\"http://" + IP + ":"+Porta+"/wig/brtvantagens?s=$(c)\"/>" +
									 "	</do>" +
									 "	</p>" +
									 "</card>" +
									 "</wml> ");
        else if(servico.equals("2"))
        	out.print(cabecalhowml + "<wml>" +
        							 "<card>" +
        							 "	<p>" +
        							 "	<select name=\"c\" title=\"Amigos toda hora:\">" +
        							 "		<option value=\"w2\">Cadastrar</option>" +
        							 "		<option value=\"q2\">Consultar</option>" +
        							 "	</select>" +
        							 "	<do type=\"accept\">" +
        							 "		<go href=\"http://" + IP + ":"+Porta+"/wig/brtvantagens?s=$(c)\"/>" +
        							 "	</do>" +
        							 "	</p>" +
        							 "</card>" +
        							 "</wml> ");

        else if(servico.equals("q1"))
        {
        	try
			{
        		DataSource ds = (DataSource) ictx.lookup("java:/comp/env/jdbc/WPP_CLARIFY");
				con = ds.getConnection();

		        Statement statement = con.createStatement();
		        String s3 = "select sa.x_valor_atrib from table_contr_itm ci, table_x_selec_atrib sa, table_mod_level ml, table_part_num pn " +
		        			" where (ci.child2contr_itm = (select objid from table_contr_itm where quote_sn = '" + msisdn.substring(2) + "') " +
		        			"		 or ci.objid = " + "(select objid " +
		        			"							   from table_contr_itm " +
		        			"							  where quote_sn = '" + msisdn.substring(2) + "') " + ") " +
		        			"   and ci.objid = sa.x_selec_atrib2contr_itm " + 
		        			"   and ci.contr_itm2mod_level = ml.objid " +
		        			"   and ml.part_info2part_num = pn.objid " +
		        			"   and pn.part_number = 'ELM_IRMAO14_ID1014' ";

		        ResultSet resultset = statement.executeQuery(s3);
		        int j = 0;
		        String numerofixobonus;
		        for(numerofixobonus = null; resultset.next(); numerofixobonus = resultset.getString(1))
		            j++;

		        s3 = "SELECT nvl(BONUS,0) FROM TABLE_X_REQUISICOES_EXTERNAS WHERE msisdn = '" + msisdn.substring(2) + "' AND STATUS = 'PENDING'  AND REQ_TYPE = 'BRASILV' ";
		        resultset = statement.executeQuery(s3);
		        int j1 = 0;
		        String numerofixobonuspendente;
		        for(numerofixobonuspendente = null; resultset.next(); numerofixobonuspendente = resultset.getString(1))
		            j1++;

		        if(j > 0)
		            out.println(cabecalhowml + "<wml>" +
		            						   "<card>" +
		            						   "	<p>Numero cadastrado para Bonus Todo Mes: " + numerofixobonus + ".</p>" +
		            						   "</card>" +
		            						   "</wml>");
		        else
		        	if(j1 > 0 && !numerofixobonuspendente.equals("0"))
		        		out.println(cabecalhowml + "<wml>" +
		        								   "<card>" +
		        								   "	<p>O cadastro do numero " + numerofixobonuspendente + " ainda nao foi efetivado.Consulte novamente mais tarde.</p>" +
		        								   "</card>" +
		        								   "</wml>");
		        	else
		        		out.println(cabecalhowml + "<wml>" +
		        								   "<card>" +
		        								   "	<p>Nenhum numero cadastrado para Bonus Todo Mes.</p>" +
		        								   "</card>" +
		        								   "</wml>");
		        resultset.close();
		        statement.close();
			}
        	catch(SQLException e)
			{
        		e.printStackTrace();
        		out.print(getWMLExcecao()+getErro(e));
			}
        	catch(Exception e)
			{
        		e.printStackTrace();
				out.print(getWMLExcecao()+getErro(e));
			}
			finally
			{
				try
				{
					con.close();
				}catch (SQLException e)
				{
					e.printStackTrace();
					out.print(getWMLExcecao()+getErro(e));
				}
			}
        }

        else if(servico.equals("q2"))
        {
        	try
			{
        		DataSource ds = (DataSource) ictx.lookup("java:/comp/env/jdbc/WPP_CLARIFY");
        		con = ds.getConnection();
        		Statement statement = con.createStatement();
        		
        		// NOVO COMANDO SQL
        		String s4 = "SELECT sa.x_valor_atrib,														" +
        					"       case WHEN instr(sa.x_cod_atrib,'movel') > 0 THEN 'celular'				" +
        					"            WHEN instr(sa.x_cod_atrib,'terminal') > 0 THEN 'fixo'				" +
        					"            ELSE 'desconhecido'												" +
        					"       END AS x_cod_atrib														" +
        					"  FROM table_contr_itm ci,														" +
        					"       table_x_selec_atrib sa,													" +
        					"       table_mod_level ml,														" +
        					"       table_part_num pn														" +
        					" WHERE (   ci.child2contr_itm = (SELECT objid									" +
        					"                                   FROM table_contr_itm						" +
        					"                                  WHERE quote_sn = '"+msisdn.substring(2)+"')	" +
        					"        OR ci.objid = (SELECT objid											" +
        					"                         FROM table_contr_itm									" +
        					"                        WHERE quote_sn = '"+msisdn.substring(2)+"')			" +
        					"       )																		" +
        					"   AND ci.objid = sa.x_selec_atrib2contr_itm									" +
        					"   AND ci.contr_itm2mod_level = ml.objid										" +
        					"   AND ml.part_info2part_num = pn.objid										" +
        					"   AND pn.part_number = 'ELM_G14_ID0990'										" +
        					"   AND sa.x_valor_atrib IS NOT NULL											";						
        		
        		
        		// COMANDO SQL EXTINTO EM 26/04, APOS CONSTATACAO DE FALHA NO RETORNO DOS RESULTADOS
        		/*String sx4 = "select sa.x_valor_atrib,decode(substr(sa.X_COD_ATRIB,7,length(sa.X_COD_ATRIB)-7),'terminal','fixo','acesso_movel','celular') " +
    			"from table_contr_itm ci, table_x_selec_atrib sa, table_mod_level ml, table_part_num pn " +
    			"where (ci.child2contr_itm = (select objid " +
    										 "from table_contr_itm " +
    										 "where quote_sn = '" + msisdn.substring(2) + "') " +
											 "or ci.objid = " + "(select objid from table_contr_itm " +
											 				     "where quote_sn = '" + msisdn.substring(2) + "') " + ") " +
				 "and ci.objid = sa.x_selec_atrib2contr_itm " +
				 "and ci.contr_itm2mod_level = ml.objid " +
				 "and ml.part_info2part_num = pn.objid " +
				 "and pn.part_number = 'ELM_G14_ID0990' ";*/
        					
		        ResultSet resultset1 = statement.executeQuery(s4);
		        int k = 0;
		        int l = 0;
		        String s11 = null;
		        String s15 = null;
		        while(resultset1.next())
		        	if (resultset1.getString(2) != null)
		        	{
		        		if(resultset1.getString(2).equals("fixo"))
			        	{
			        		if(l == 0)
			        			s15 = resultset1.getString(1);
			                else
			                    s15 = s15 + "," + resultset1.getString(1);
			                l++;
			        	} else
			        		if(resultset1.getString(2).equals("celular"))
				            {
				                if(k == 0)
				                    s11 = resultset1.getString(1);
				                else
				                	s11 = s11 + "," + resultset1.getString(1);
				                k++;
				            }
		        	}
		        s4 = "select nvl(AMIGO1_CEL,0),nvl(AMIGO2_CEL,0),nvl(AMIGO3_CEL,0),nvl(AMIGO4_FIXA,0),nvl(AMIGO5_FIXA,0) " +
		        	 "from TABLE_X_REQUISICOES_EXTERNAS " +
		        	 "WHERE msisdn = '" + msisdn.substring(2) +"' " +
					 "AND STATUS = 'PENDING' " +
					 "AND REQ_TYPE = 'BRASILV' ";
		        resultset1 = statement.executeQuery(s4);
		        int l1 = 0;
		        int j2 = 0;
		        String s19 = null;
		        String s20 = null;
		        while(resultset1.next()) 
		        {
		        	if(!resultset1.getString(1).equals("0"))
		        	{
		        		if(j2 == 0)
		        			s20 = resultset1.getString(1);
		        		else
		        			s20 = s20 + "," + resultset1.getString(1);
		        		j2++;
		            }
		            if(!resultset1.getString(2).equals("0"))
		            {
		                if(j2 == 0)
		                    s20 = resultset1.getString(2);
		                else
		                    s20 = s20 + "," + resultset1.getString(2);
		                j2++;
		            }
		            if(!resultset1.getString(3).equals("0"))
		            {
		                if(j2 == 0)
		                    s20 = resultset1.getString(3);
		                else
		                    s20 = s20 + "," + resultset1.getString(3);
		                j2++;
		            }
		            if(!resultset1.getString(4).equals("0"))
		            {
		                if(l1 == 0)
		                    s19 = resultset1.getString(4);
		                else
		                    s19 = s19 + "," + resultset1.getString(4);
		                l1++;
		            }
		            if(!resultset1.getString(5).equals("0"))
		            {
		                if(l1 == 0)
		                    s19 = resultset1.getString(5);
		                else
		                    s19 = s19 + "," + resultset1.getString(5);
		                l1++;
		            }
		        }
		        resultset1.close();
		        statement.close();
		        out.println(cabecalhowml + "<wml>" +
		        						   "<card>" +
		        						   "	<p>");
		        if(k == 0 && l == 0 && l1 == 0 && j2 == 0)
		        {
		        	out.println("Nenhum numero cadastrado para Amigos Toda Hora.");
		        } else
		        {
		            if(k > 0 || l > 0)
		            {
		                out.println("Cadastrados:<br/>");
		                if(k > 0)
		                    out.println("Cel: " + s11 + " ");
		                if(l > 0)
		                    out.println("Fixo/Cel: " + s15 + " ");
		                    
		            out.println("<br/>");
		            }
		            if(l1 > 0 || j2 > 0)
		            {
		                out.println("Em cadastramento:<br/>");
		                if(j2 > 0)
		                    out.println("Cel: " + s20);
		                if(l1 > 0)
		                	out.println("Fixo/Cel: " + s19);
		                out.println("<br/>");
		            }
		        }

		        out.println("	</p>" +
		        			"</card>" +
		        			"</wml>");
			}
        	catch(SQLException e)
			{
        		e.printStackTrace();
        		out.print(getWMLExcecao()+getErro(e));
			}
			catch(Exception e)
			{
				e.printStackTrace();
				out.print(getWMLExcecao()+getErro(e));
			}
			finally
			{
				try
				{
					con.close();
				} catch (SQLException e)
				{
					e.printStackTrace();
					out.print(getWMLExcecao()+getErro(e));
				}
			}
        }

        else if(servico.equals("w1") || servico.equals("w2"))
        {
        	try
			{
        		int retorno;
        		DataSource ds = (DataSource) ictx.lookup("java:/comp/env/jdbc/WPP_CLARIFY");
        		con = ds.getConnection();
        		Statement statement = con.createStatement();

        		CallableStatement callablestatement = con.prepareCall("call SA.INTP_CONSULTA_ACESSO_MOVEL(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		        callablestatement.setString(1, msisdn.substring(2));
		        callablestatement.registerOutParameter(2, 2);
		        callablestatement.registerOutParameter(3, 12);
		        callablestatement.registerOutParameter(4, 12);
		        callablestatement.registerOutParameter(5, 12);
		        callablestatement.registerOutParameter(6, 12);
		        callablestatement.registerOutParameter(7, 12);
		        callablestatement.registerOutParameter(8, 12);
		        callablestatement.registerOutParameter(9, 2);
		        callablestatement.registerOutParameter(10, 2);
		        callablestatement.registerOutParameter(11, 2);
		        callablestatement.registerOutParameter(12, 2);
		        callablestatement.registerOutParameter(13, 2);
		        callablestatement.registerOutParameter(14, 2);
		        callablestatement.registerOutParameter(15, 12);
		        callablestatement.registerOutParameter(16, 12);
		        callablestatement.execute();
		        retorno = callablestatement.getInt(2);
		        String s12 = callablestatement.getString(5);
		        String s16 = callablestatement.getString(6);

		        int totalprocpossivelcelular = callablestatement.getInt(9);
		        int totalprocpossivelfixo = callablestatement.getInt(10);
		        //int qtdbonus = callablestatement.getInt(11);
		        int qtdamigos = callablestatement.getInt(12);
		        //int ttdiasultimaatualizacao = callablestatement.getInt(13);
		        //int ttdiasatualizacao = callablestatement.getInt(14);
		        
		        callablestatement.close();
		        statement.close();

		        if(retorno == 0)
		        {
		        	if (s12.equals("Ativo") && s16.equals("N"))
		        	{
		        		if (( (servico.equals("w1")) ) || ( (qtdamigos >=1) && (servico.equals("w2")) ) )
		        		{
		        			if (servico.equals("w1"))
		        			{
						        Statement statement2 = con.createStatement();
						        String s5 = "select count(*) " +
						        			"from table_contr_itm ci, table_x_selec_atrib sa, table_mod_level ml, table_part_num pn " +
						        			"where (ci.child2contr_itm = (select objid " +
						        										 "from table_contr_itm " +
						        										 "where quote_sn = '" + msisdn.substring(2) + "') " + "or ci.objid = " +
																		 "(select objid from table_contr_itm " +
																		 "where quote_sn = '" + msisdn.substring(2) + "') " + ") " +
											"and ci.objid = sa.x_selec_atrib2contr_itm " +
											"and ci.contr_itm2mod_level = ml.objid " +
											"and ml.part_info2part_num = pn.objid " +
											"and pn.part_number = 'ELM_IRMAO14_ID1014' ";
						        ResultSet resultset2 = statement2.executeQuery(s5);
						        int l3;
						        for(l3 = 0; resultset2.next();l3 = resultset2.getInt(1));
						        s5 = "SELECT count(*) " +
						        	 "FROM TABLE_X_REQUISICOES_EXTERNAS " +
						        	 "WHERE msisdn = '" + msisdn.substring(2) + "' " +
									 "AND STATUS = 'PENDING' " +
									 "AND REQ_TYPE = 'BRASILV' " +
									 "AND BONUS IS NOT NULL";
						        resultset2 = statement2.executeQuery(s5);
						        int j4;
						        for(j4 = 0; resultset2.next(); j4 = resultset2.getInt(1));
						        if(j4 + l3 > 0)
						        	out.print(cabecalhowml + "<wml>" +
						        							 "<card>" +
						        							 "	<p>Cadastro limitado a um numero.</p>" +
						        							 "</card>" +
						        							 "</wml>");
						        else
						        	out.print(cabecalhowml + "<wml>" +
						        							 "<card id=\"Main\">" +
						        							 "	<p>Cadastre um numero de telefone fixo da Brasil Telecom com DDD para o Bonus Todo Mes. Exemplo: 6134150000." +
						        							 "		<input title=\"No. Fixo:\" type=\"text\" name=\"nf\" format=\"*N\" maxlength=\"10\" emptyok=\"false\"/>" +
						        							 "	</p>" +
						        							 "	<do type=\"accept\">" +
						        							 "		<go href=\"http://" + IP + ":"+Porta+"/wig/brtvantagens?s=c1&amp;nf=$(nf)\"/>" +
						        							 "	</do>" +
						        							 "</card>" +
						        							 "</wml> ");
						        resultset2.close();
						        statement2.close();
		        			}																				
		        			else if(servico.equals("w2"))
		        			{
		        				Statement statement3 = con.createStatement();

						        int ttamigosfixospendentes = 0;
						        int ttamigoscelularpendentes = 0;
						        int totalpossivelfixo = 0;
						        int totalpossivelcelular = 0;
						        String query = "SELECT count(*) " +
						        			   "FROM TABLE_X_REQUISICOES_EXTERNAS " +
						        			   "WHERE msisdn = '" + msisdn.substring(2) + "' " +
											   "AND STATUS = 'PENDING' " +
											   "AND REQ_TYPE = 'BRASILV' " +
											   "AND AMIGO1_CEL IS NOT NULL";
						        ResultSet resultset3 = statement3.executeQuery(query);
						        while (resultset3.next())
						            ttamigoscelularpendentes  += resultset3.getInt(1);
						        resultset3.close();

						        query = "SELECT count(*) " +
						        		"FROM TABLE_X_REQUISICOES_EXTERNAS " +
						        		"WHERE msisdn = '" + msisdn.substring(2) + "' " +
										"AND STATUS = 'PENDING' " +
										"AND REQ_TYPE = 'BRASILV' " +
										"AND AMIGO2_CEL IS NOT NULL";
						        ResultSet resultset4 = statement3.executeQuery(query);
						        
						        while(resultset4.next())
						            ttamigoscelularpendentes  += resultset4.getInt(1);
						        resultset4.close();
						        
						        query = "SELECT count(*) " +
						        		"FROM TABLE_X_REQUISICOES_EXTERNAS " +
						        		"WHERE msisdn = '" + msisdn.substring(2) + "' " +
										"AND STATUS = 'PENDING' " +
										"AND REQ_TYPE = 'BRASILV' " +
										"AND AMIGO3_CEL IS NOT NULL";
						        
						        ResultSet resultset5 = statement3.executeQuery(query);
						        while(resultset5.next())
						            ttamigoscelularpendentes  += resultset5.getInt(1);
						        resultset5.close();

						        query = "SELECT count(*) " +
						        		"FROM TABLE_X_REQUISICOES_EXTERNAS " +
						        		"WHERE msisdn = '" + msisdn.substring(2) + "' " +
										"AND STATUS = 'PENDING' " +
										"AND REQ_TYPE = 'BRASILV' " +
										"AND AMIGO4_FIXA IS NOT NULL";
						        
						        ResultSet resultset6 = statement3.executeQuery(query);
						        while(resultset6.next())
						            ttamigosfixospendentes += resultset6.getInt(1);
						        resultset6.close();
						        
						        query = "SELECT count(*) " +
						        		"FROM TABLE_X_REQUISICOES_EXTERNAS " +
						        		"WHERE msisdn = '" + msisdn.substring(2) + "' " +
										"AND STATUS = 'PENDING' " +
										"AND REQ_TYPE = 'BRASILV' " +
										"AND AMIGO5_FIXA IS NOT NULL";
						        ResultSet resultset7 = statement3.executeQuery(query);
						        while (resultset7.next())
						            ttamigosfixospendentes += resultset7.getInt(1);
						        resultset7.close();
						        statement3.close();

						        if(totalprocpossivelcelular == 14)
						        	totalprocpossivelcelular = 12;
						        else
						        	if(totalprocpossivelcelular == 7)
						        		totalprocpossivelcelular = 5;
						        totalpossivelfixo = totalprocpossivelfixo - ttamigosfixospendentes;
						        totalpossivelcelular = totalprocpossivelcelular - ttamigoscelularpendentes ;

						        if(totalpossivelfixo + totalpossivelcelular == 0)
						        	out.print(cabecalhowml + "<wml>" +
						        							 "<card>" +
						        							 "	<p>Nao e possivel cadastrar mais nenhum numero no Amigos Toda Hora pois ja foi atingido o limite maximo.</p>" +
						        							 "</card>" +
						        							 "</wml>");
						        else
						        {
						        	if (totalpossivelfixo + totalpossivelcelular == 1)
									{
										if (totalpossivelfixo == 1)
											out.print(cabecalhowml + "<wml>" +
																	 "<card id=\"Main\">" +
																	 "	<p>" +
																	 "	<setvar name=\"ddd\" value=\"" + msisdn.substring(2, 4) + "\"/>" +
																	 "	<setvar name=\"f1\" value=\"\"/>" +
																	 "	<setvar name=\"f2\" value=\"\"/>" +
																	 "	<setvar name=\"c1\" value=\"\"/>" +
																	 "	<setvar name=\"c2\" value=\"\"/>" +
																	 "	<setvar name=\"c3\" value=\"\"/>" +
																	 "	<setvar name=\"c4\" value=\"\"/>" +
																	 "	<setvar name=\"c5\" value=\"\"/>" +
																	 "	<setvar name=\"c6\" value=\"\"/>" +
																	 "	<setvar name=\"c7\" value=\"\"/>" +
																	 "	<setvar name=\"c8\" value=\"\"/>" +
																	 "	<setvar name=\"c9\" value=\"\"/>" +
																	 "	<setvar name=\"c10\" value=\"\"/>" +
																	 "	<setvar name=\"c11\" value=\"\"/>" +
																	 "	<setvar name=\"c12\" value=\"\"/>" +
																	 "	Cadastre mais 1 numero local, fixo ou celular, sem o DDD, para o Amigos Toda Hora. Voce pode cadastrar qualquer celular da Brasil Telecom GSM.");
										else
											out.print(cabecalhowml + "<wml>" +
																	 "<card id=\"Main\">" +
																	 "	<p>" +
																	 "	<setvar name=\"ddd\" value=\"" + msisdn.substring(2, 4) + "\"/>" +
																	 "	<setvar name=\"f1\" value=\"\"/>" +
																	 "	<setvar name=\"f2\" value=\"\"/>" +
																	 "	<setvar name=\"c1\" value=\"\"/>" +
																	 "	<setvar name=\"c2\" value=\"\"/>" +
																	 "	<setvar name=\"c3\" value=\"\"/>" +
																	 "	<setvar name=\"c4\" value=\"\"/>" +
																	 "	<setvar name=\"c5\" value=\"\"/>" +
																	 "	<setvar name=\"c6\" value=\"\"/>" +
																	 "	<setvar name=\"c7\" value=\"\"/>" +
																	 "	<setvar name=\"c8\" value=\"\"/>" +
																	 "	<setvar name=\"c9\" value=\"\"/>" +
																	 "	<setvar name=\"c10\" value=\"\"/>" +
																	 "	<setvar name=\"c11\" value=\"\"/>" +
																	 "	<setvar name=\"c12\" value=\"\"/>" +
																	 "	Cadastre mais 1 numero local celular da Brasil Telecom GSM, sem o DDD, para o Amigos Toda Hora.");
									}
						        	else if (totalpossivelfixo == 0)
										out.print(cabecalhowml + "<wml>" +
																 "<card id=\"Main\">" +
																 "	<p>" +
																 "	<setvar name=\"ddd\" value=\"" + msisdn.substring(2, 4) + "\"/>" +
																 "	<setvar name=\"f1\" value=\"\"/>" +
																 "	<setvar name=\"f2\" value=\"\"/>" +
																 "	<setvar name=\"c1\" value=\"\"/>" +
																 "	<setvar name=\"c2\" value=\"\"/>" +
																 "	<setvar name=\"c3\" value=\"\"/>" +
																 "	<setvar name=\"c4\" value=\"\"/>" +
																 "	<setvar name=\"c5\" value=\"\"/>" +
																 "	<setvar name=\"c6\" value=\"\"/>" +
																 "	<setvar name=\"c7\" value=\"\"/>" +
																 "	<setvar name=\"c8\" value=\"\"/>" +
																 "	<setvar name=\"c9\" value=\"\"/>" +
																 "	<setvar name=\"c10\" value=\"\"/>" +
																 "	<setvar name=\"c11\" value=\"\"/>" +
																 "	<setvar name=\"c12\" value=\"\"/>" + 
																 "	Cadastre ate " + (totalpossivelfixo + totalpossivelcelular) + " numeros locais celulares da Brasil Telecom GSM, sem o DDD, para o Amigos Toda Hora. O cadastro podera ser feito em uma unica vez ou em partes.");
									else if (totalpossivelfixo == 1)
										out.print(cabecalhowml + "<wml>" +
																 "<card id=\"Main\">" +
																 "	<p>" +
																 "	<setvar name=\"ddd\" value=\"" + msisdn.substring(2, 4) + "\"/>" +
																 "	<setvar name=\"f1\" value=\"\"/>" +
																 "	<setvar name=\"f2\" value=\"\"/>" +
																 "	<setvar name=\"c1\" value=\"\"/>" +
																 "	<setvar name=\"c2\" value=\"\"/>" +
																 "	<setvar name=\"c3\" value=\"\"/>" +
																 "	<setvar name=\"c4\" value=\"\"/>" +
																 "	<setvar name=\"c5\" value=\"\"/>" +
																 "	<setvar name=\"c6\" value=\"\"/>" +
																 "	<setvar name=\"c7\" value=\"\"/>" +
																 "	<setvar name=\"c8\" value=\"\"/>" +
																 "	<setvar name=\"c9\" value=\"\"/>" +
																 "	<setvar name=\"c10\" value=\"\"/>" +
																 "	<setvar name=\"c11\" value=\"\"/>" +
																 "	<setvar name=\"c12\" value=\"\"/>" +
																 "	Cadastre ate " + (totalpossivelfixo + totalpossivelcelular) + " numeros locais, sem o DDD, para o Amigos Toda Hora, com um limite de 1 telefone fixo. Voce pode cadastrar qualquer celular da Brasil Telecom GSM. O cadastro podera ser feito em uma unica vez ou em partes.");
									else
										out.print(cabecalhowml + "<wml>" +
																 "<card id=\"Main\">" +
																 "	<p>" +
																 "	<setvar name=\"ddd\" value=\"" + msisdn.substring(2, 4) + "\"/>" +
																 "	<setvar name=\"f1\" value=\"\"/>" +
																 "	<setvar name=\"f2\" value=\"\"/>" +
																 "	<setvar name=\"c1\" value=\"\"/>" +
																 "	<setvar name=\"c2\" value=\"\"/>" +
																 "	<setvar name=\"c3\" value=\"\"/>" +
																 "	<setvar name=\"c4\" value=\"\"/>" +
																 "	<setvar name=\"c5\" value=\"\"/>" +
																 "	<setvar name=\"c6\" value=\"\"/>" +
																 "	<setvar name=\"c7\" value=\"\"/>" +
																 "	<setvar name=\"c8\" value=\"\"/>" +
																 "	<setvar name=\"c9\" value=\"\"/>" +
																 "	<setvar name=\"c10\" value=\"\"/>" +
																 "	<setvar name=\"c11\" value=\"\"/>" +
																 "	<setvar name=\"c12\" value=\"\"/>" +
																 "	Cadastre ate " + (totalpossivelfixo + totalpossivelcelular) + " numeros locais, sem o DDD, para o Amigos Toda Hora, com um limite de " + totalpossivelfixo +  " telefones fixos. Voce pode cadastrar qualquer celular da Brasil Telecom GSM. O cadastro podera ser feito em uma unica vez ou em partes.");

						        	for(int i = 1; i <= totalpossivelfixo; i++)
						        		out.print("<input title=\"Fixo/Cel " + i + ":\" type=\"text\" name=\"f" + i + "\" format=\"*N\" maxlength=\"8\"/>");
						        	for(int x = 1; x <= totalpossivelcelular; x++)
						        		out.print("<input title=\"Cel " + x + ":\" type=\"text\" name=\"c" + x + "\" format=\"*N\" maxlength=\"8\"/>");
						        	out.print("	</p>" +
						        			  "	<do type=\"accept\">" +
						        			  "		<go href=\"http://" + IP + ":"+Porta+"/wig/brtvantagens?s=c2&amp;f1=$(f1)&amp;f2=$(f2)&amp;c1=$(c1)&amp;c2=$(c2)&amp;c3=$(c3)&amp;c4=$(c4)&amp;c5=$(c5)&amp;c6=$(c6)&amp;c7=$(c7)&amp;c8=$(c8)&amp;c9=$(c9)&amp;c10=$(c10)&amp;c11=$(c11)&amp;c12=$(c12)\"/>" +
						        			  "	</do>" +
						        			  "</card>" +
						        			  "</wml> ");
						        }
		        			}				
		        		}
		        		else
							out.print(cabecalhowml + "<wml>" +
													 "<card>" +
													 "	<p>O Amigos Toda Hora nao esta disponivel para o seu plano.</p>" +
													 "</card>" +
													 "</wml>");
		        	}
		        	else
						out.print(getWMLExcecao());
		        }
		        else if ((retorno == 1) || (retorno == 2) || (retorno == 3))
		        	out.print(cabecalhowml + "<wml>" +
		        							 "<card>" +
		        							 "	<p>Servico temporariamente indisponivel. Tente mais tarde novamente.</p>" +
		        							 "</card>" +
		        							 "</wml>");
		        else if ((retorno == 100) || (retorno == 101))
		        	out.print(getWMLExcecao());
			}
        	catch (NamingException e)
        	{
				e.printStackTrace();
				out.print(getWMLExcecao()+getErro(e));
			}
        	catch(Exception e){
        	    e.printStackTrace(); 
        	}
        	finally
			{
        		
        		try
				{
					con.close();
				}
				catch (SQLException e1)
				{
					e1.printStackTrace();
					out.print(getWMLExcecao()+getErro(e1));
				}
			}

        }

        else if(servico.equals("c1"))
        {
        	String numerofixo;
	        numerofixo = request.getParameter("nf");

	        if ( ((numerofixo.length() == 10) && (numerofixo.substring(2,3).equals("9"))) || ((numerofixo.length() == 10) && (numerofixo.substring(2,3).equals("8"))  ) )
	        	out.println(cabecalhowml + "<wml>" +
	        							   "<card>" +
	        							   "	<p>Cadastro limitado a telefone fixo da Brasil Telecom GSM.</p>" +
	        							   "</card>" +
	        							   "</wml>");
	        else if ( (!numerofixo.substring(0,1).equals("6")) && (!numerofixo.substring(0,1).equals("5")) && (!numerofixo.substring(0,1).equals("4")))
	        	out.println(cabecalhowml + "<wml>" +
	        							   "<card>" +
	        							   "	<p>Nao foi possivel cadastrar. Verifique se o numero esta correto. Lembre-se de informar um número fixo da Brasil Telecom com o DDD. Exemplo: 6134150000.</p>" +
	        							   "</card>" +
	        							   "</wml>");
	        else if (numerofixo.length() < 9)
	        	out.println(cabecalhowml + "<wml>" +
	        							   "<card>" +
	        							   "	<p>Nao foi possivel cadastrar. Verifique se o numero esta correto. Lembre-se de informar um número fixo da Brasil Telecom com o DDD. Exemplo: 6134150000.</p>" +
	        							   "</card>" +
	        							   "</wml>");
	        else
	        {
	        	try
				{
	        		DataSource ds = (DataSource) ictx.lookup("java:/comp/env/jdbc/WPP_CLARIFY");
	        		con = ds.getConnection();

	        		CallableStatement callablestatement = con.prepareCall("call sa.INTP_SOL_GERACAO_OS_BRASILV(?,?,?,?,?,?,?,?)");
			        callablestatement.setString(1, msisdn.substring(2));
			        callablestatement.setString(2, null);
			        callablestatement.setString(3, null);
			        callablestatement.setString(4, null);
			        callablestatement.setString(5, null);
			        callablestatement.setString(6, null);
			        callablestatement.setString(7, numerofixo);
			        callablestatement.registerOutParameter(8, 2);
			        callablestatement.execute();
			        int retorno = callablestatement.getInt(8);
			        callablestatement.close();
			        
			        if(retorno == 0)
			            out.println(cabecalhowml + "<wml>" +
			            						   "<card>" +
			            						   "	<p>Sua solicitacao foi recebida com sucesso e sera processada em ate 48 horas.</p>" +
			            						   "</card>" +
			            						   "</wml>");
			        else
			            out.println(getWMLExcecao());
				}
	        	catch(SQLException e)
				{
	        		e.printStackTrace();
					out.print(getWMLExcecao()+getErro(e));
				}
				catch(Exception e)
				{
					e.printStackTrace();
					out.print(getWMLExcecao()+getErro(e));
				}
				finally
				{
					try
					{
						con.close();
					} catch (SQLException e)
					{
						e.printStackTrace();
						out.print(getWMLExcecao()+getErro(e));
					}
				}
	        }
        }

        else if(servico.equals("c2"))
        {
        	int retorno=1;
	        String fixo1 = request.getParameter("f1");
	        String fixo2 = request.getParameter("f2");
	        String celular1 = request.getParameter("c1");
	        String celular2 = request.getParameter("c2");
	        String celular3 = request.getParameter("c3");
	        String celular4 = request.getParameter("c4");
	        String celular5 = request.getParameter("c5");
	        String celular6 = request.getParameter("c6");
	        String celular7 = request.getParameter("c7");
	        String celular8 = request.getParameter("c8");
	        String celular9 = request.getParameter("c9");
	        String celular10 = request.getParameter("c10");
	        String celular11 = request.getParameter("c11");
	        String celular12 = request.getParameter("c12");

	        if(
					(!celular1.equals("") && ((celular1.length() != 8) || (!celular1.substring(0,2).equals("84"))))    || 
					(!celular2.equals("") && ((celular2.length() != 8) || (!celular2.substring(0,2).equals("84"))))    ||  
					(!celular3.equals("") && ((celular3.length() != 8) || (!celular3.substring(0,2).equals("84"))))    ||  
					(!celular4.equals("") && ((celular4.length() != 8) || (!celular4.substring(0,2).equals("84"))))    ||  
					(!celular5.equals("") && ((celular5.length() != 8) || (!celular5.substring(0,2).equals("84"))))    ||  
					(!celular6.equals("") && ((celular6.length() != 8) || (!celular6.substring(0,2).equals("84"))))    ||  
					(!celular7.equals("") && ((celular7.length() != 8) || (!celular7.substring(0,2).equals("84"))))    ||  
					(!celular8.equals("") && ((celular8.length() != 8) || (!celular8.substring(0,2).equals("84"))))    ||  
					(!celular9.equals("") && ((celular9.length() != 8) || (!celular9.substring(0,2).equals("84"))))    ||  
					(!celular10.equals("") && ((celular10.length() != 8) || (!celular10.substring(0,2).equals("84")))) ||  
					(!celular11.equals("") && ((celular11.length() != 8) || (!celular11.substring(0,2).equals("84")))) ||  
					(!celular12.equals("") && ((celular12.length() != 8) || (!celular12.substring(0,2).equals("84")))) )  
	        {
	        	out.println(cabecalhowml + "<wml>" +
	        							   "<card>" +
	        							   "	<p>Nao foi possivel cadastrar. Verifique se os numeros cadastrados estao corretos.</p>" +
	        							   "</card>" +
	        							   "</wml>");
	        }
	        else if	(
					(!fixo1.equals("") && (fixo1.length() == 8) && ((fixo1.substring(0,1).equals("9")) || ((fixo1.substring(0,1).equals("8")) && (!fixo1.substring(0,2).equals("84"))))) || 
					(!fixo2.equals("") && (fixo2.length() == 8) && ((fixo2.substring(0,1).equals("9")) || ((fixo2.substring(0,1).equals("8")) && (!fixo2.substring(0,2).equals("84"))))  ) 
	        )  
	        {
	        	out.println(cabecalhowml + "<wml>" +
	        							   "<card>" +
	        							   "	<p>Nao foi possivel cadastrar. Verifique se os numeros cadastrados estao corretos.</p>" +
	        							   "</card>" +
	        							   "</wml>");
	        }
	        else
	        {							
	        	try
				{
	        		DataSource ds = (DataSource) ictx.lookup("java:/comp/env/jdbc/WPP_CLARIFY");
	        		con = ds.getConnection();

	        		if(!fixo1.equals("") || !fixo2.equals("") || !celular1.equals("") || !celular2.equals("") || !celular3.equals(""))
	        		{
				        CallableStatement callablestatement2 = con.prepareCall("call sa.INTP_SOL_GERACAO_OS_BRASILV(?,?,?,?,?,?,?,?)");
			            callablestatement2.setString(1, msisdn.substring(2));
				        if (!celular1.equals("") )
				        	callablestatement2.setString(2, msisdn.substring(2, 4) + celular1);
				        else
				        	callablestatement2.setString(2, null);

				        if (!celular2.equals("") )
				        	callablestatement2.setString(3, msisdn.substring(2, 4) + celular2);
				        else
				        	callablestatement2.setString(3, null);

				        if (!celular3.equals("") )
				        	callablestatement2.setString(4, msisdn.substring(2, 4) + celular3);
				        else 
				        	callablestatement2.setString(4, null);

				        if (!fixo1.equals("") )
				        	callablestatement2.setString(5, msisdn.substring(2, 4) + fixo1);
				        else
				        	callablestatement2.setString(5, null);

				        if (!fixo2.equals("") )
				        	callablestatement2.setString(6, msisdn.substring(2, 4) + fixo2);
				        else
				        	callablestatement2.setString(6, null);

			            callablestatement2.setString(7, null);
			            callablestatement2.registerOutParameter(8, 2);
			            callablestatement2.execute();
			            retorno = callablestatement2.getInt(8);
			            callablestatement2.close();
	        		}
	        		if ((retorno == 0) && (!celular4.equals("") || !celular5.equals("") || !celular6.equals("")))
	        		{
	        			CallableStatement callablestatement3 = con.prepareCall("call sa.INTP_SOL_GERACAO_OS_BRASILV(?,?,?,?,?,?,?,?)");
			            callablestatement3.setString(1, msisdn.substring(2));
				        if (!celular4.equals("") )
				        	callablestatement3.setString(2, msisdn.substring(2, 4) + celular4);
				        else
				        	callablestatement3.setString(2, null);

				        if (!celular5.equals("") )
				        	callablestatement3.setString(3, msisdn.substring(2, 4) + celular5);
				        else
				        	callablestatement3.setString(3, null);

				        if (!celular6.equals("") )
				        	callablestatement3.setString(4, msisdn.substring(2, 4) + celular6);
				        else
				        	callablestatement3.setString(4, null);

				        callablestatement3.setString(5, null);
				        callablestatement3.setString(6, null);
			            callablestatement3.setString(7, null);
			            callablestatement3.registerOutParameter(8, 2);
			            callablestatement3.execute();
			            retorno = callablestatement3.getInt(8);
			            callablestatement3.close();
	        		}
	        		if ((retorno == 0) && (!celular7.equals("") || !celular8.equals("") || !celular9.equals("")))
	        		{
	        			CallableStatement callablestatement4 = con.prepareCall("call sa.INTP_SOL_GERACAO_OS_BRASILV(?,?,?,?,?,?,?,?)");
			            callablestatement4.setString(1, msisdn.substring(2));
				        if (!celular7.equals("") )
				        	callablestatement4.setString(2, msisdn.substring(2, 4) + celular7);
				        else

				        	callablestatement4.setString(2, null);
				        if (!celular8.equals("") )
				        	callablestatement4.setString(3, msisdn.substring(2, 4) + celular8);
				        else
				        	callablestatement4.setString(3, null);

				        if (!celular9.equals("") )
				        	callablestatement4.setString(4, msisdn.substring(2, 4) + celular9);
				        else
				        	callablestatement4.setString(4, null);

				        callablestatement4.setString(5, null);
				        callablestatement4.setString(6, null);
			            callablestatement4.setString(7, null);
			            callablestatement4.registerOutParameter(8, 2);
			            callablestatement4.execute();
			            retorno = callablestatement4.getInt(8);
			            callablestatement4.close();
	        		}
	        		if ((retorno == 0) && (!celular10.equals("") || !celular11.equals("") || !celular12.equals("")))
	        		{
	        			CallableStatement callablestatement5 = con.prepareCall("call sa.INTP_SOL_GERACAO_OS_BRASILV(?,?,?,?,?,?,?,?)");
			            callablestatement5.setString(1, msisdn.substring(2));
				        if (!celular10.equals("") )
				        	callablestatement5.setString(2, msisdn.substring(2, 4) + celular10);
				        else
				        	callablestatement5.setString(2, null);

				        if (!celular11.equals("") )
				        	callablestatement5.setString(3, msisdn.substring(2, 4) + celular11);
				        else
				        	callablestatement5.setString(3, null);

				        if (!celular12.equals("") )
				        	callablestatement5.setString(4, msisdn.substring(2, 4) + celular12);
				        else
				        	callablestatement5.setString(4, null);

				        callablestatement5.setString(5, null);
				        callablestatement5.setString(6, null);
			            callablestatement5.setString(7, null);
			            callablestatement5.registerOutParameter(8, 2);
			            callablestatement5.execute();
			            retorno = callablestatement5.getInt(8);
			            callablestatement5.close();
			        }

	        		if(retorno == 0)
	        			out.println(cabecalhowml + "<wml>" +
	        									   "<card>" +
	        									   "	<p>Sua solicitacao foi recebida com sucesso e sera processada em ate 48 horas.</p>" +
	        									   "</card>" +
	        									   "</wml>");
	        		else
	        			out.println(getWMLExcecao());
				}
	        	catch(SQLException e)
				{
	        		e.printStackTrace();
	        		out.print(getWMLExcecao()+getErro(e));
				}
				catch(Exception e)
				{
					e.printStackTrace();
					out.print(getWMLExcecao()+getErro(e));
				}
				finally
				{
					try
					{
						con.close();
					} catch (SQLException e)
					{
						e.printStackTrace();
						out.print(getWMLExcecao()+getErro(e));
					}
				}
			}
		}
	}
}