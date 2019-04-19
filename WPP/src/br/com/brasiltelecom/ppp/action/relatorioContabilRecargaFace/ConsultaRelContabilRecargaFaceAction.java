package br.com.brasiltelecom.ppp.action.relatorioContabilRecargaFace;

import java.io.PrintWriter;
import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.util.Uteis;
/**
 * Gera o relatório contabil/recargas
 * 
 * @author Alberto Magno
 * @since 01/07/2004
 */
public class ConsultaRelContabilRecargaFaceAction extends ActionPortal {

	private String codOperacao;
	Logger logger = Logger.getLogger(this.getClass());
	private Connection conexaoJDBC = null;
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(
				ActionMapping actionMapping,
				ActionForm actionForm,
				HttpServletRequest request,
				HttpServletResponse response,
				Database db)
				throws Exception {
					
				db.begin();
	
				ActionForward result = null;
				
				logger.info("Consulta por relatório contabil para recargasFace");
	
				request.setAttribute(Constantes.MENSAGEM, "Consulta ao relatório contabil/recargas/face realizada com sucesso!");
				this.codOperacao = Constantes.COD_CONSULTAR_CONTABIL_RCFACE;
								
				try
				{
					/*DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver ());            
					c =	DriverManager.getConnection("jdbc:oracle:thin:@brtdev2:1521:brtdev2","pppdev","pppdev");
					*/
					
				InitialContext ctx = new InitialContext();
				//DataSource  ds = (DataSource) ctx.lookup( "java:/comp/env/jdbc/WPP_DS" );
				
				DataSource  ds = (DataSource) ctx.lookup( "jdbc/WPP_DS" );
				conexaoJDBC = ds.getConnection();
				
				

				/*DataSource ds = getDataSource(request);
				conexaoJDBC = ds.getConnection();*/ 
				
				if (conexaoJDBC!=null)
				{
					Statement stmt = conexaoJDBC.createStatement ();
					stmt.executeUpdate ("alter session set NLS_territory='BRAZIL'");
					stmt.executeUpdate ("alter session set NLS_CURRENCY='R$'");

					PreparedStatement preparedStatement = conexaoJDBC.prepareStatement("" +
							" SELECT   b.cn, b.vlr_face, SUM (b.qtd_face)," +
							"          TO_CHAR (NVL (SUM (tot_cn), 0), '999G999G990D00') AS total" +
							"     FROM (SELECT   base.idt_codigo_nacional, base.des_codigo_nacional AS cn," +
							"                    TO_CHAR (base.id_valor, '999G999G990D00') AS vlr_face," +
							"                    SUM (NVL (r.qtd_registros, 0)) AS qtd_face" +
							"               FROM (SELECT *" +
							"	                      FROM tbl_rel_recargasface a," +
							"	                           tbl_ger_plano_preco d," +
							"	                           tbl_ger_categoria e    " +     
							"	                     WHERE TO_CHAR (dat_recargasface, 'MM/YYYY') = ?" +
							"	                     AND a.idt_plano_preco = d.idt_plano_preco" +
							"	                     AND d.idt_categoria = e.idt_categoria" +
							"	                     AND INSTR (?, e.idt_categoria) > 0 ) r," +
							"                    (SELECT cn.idt_codigo_nacional," +
							"                            uf.des_estado AS des_codigo_nacional, vlr.id_valor" +
							"                       FROM tbl_ger_codigo_nacional cn," +
							"                            tbl_rec_valores vlr," +
							"                            tbl_ger_estados uf" +
							"                      WHERE ind_vlr_face = 1" +
							"                        AND cn.ind_regiao_brt = 1" +
							"                        AND cn.idt_uf = uf.idt_uf(+)) base" +
							"              WHERE base.idt_codigo_nacional = r.idt_codigo_nacional(+)" +
							"                    AND base.id_valor = r.vlr_face(+)" +
							"           GROUP BY base.idt_codigo_nacional," +
							"                    base.des_codigo_nacional," +
							"                    TO_CHAR (base.id_valor, '999G999G990D00')) b," +
							"                    " +
							"          (SELECT   a.idt_codigo_nacional," +
							"                    SUM (a.vlr_face * a.qtd_registros) AS tot_cn" +
							"               FROM tbl_rel_recargasface a," +
							"                    tbl_rec_origem b," +
							"                    tbl_ger_plano_preco d," +
							"                    tbl_ger_categoria e" +
							"              WHERE a.id_canal = b.id_canal" +
							"                AND a.id_origem = b.id_origem" +
							"                AND a.idt_plano_preco = d.idt_plano_preco" +
							"                AND d.idt_categoria = e.idt_categoria" +
							"                AND TO_CHAR (a.dat_recargasface, 'MM/YYYY') = ?" +
							"                AND INSTR (?, e.idt_categoria) > 0" +
							"                AND b.idt_classificacao_recarga = 'REC'" +
							"                AND b.id_canal <> '06'" +
							"           GROUP BY a.idt_codigo_nacional) t" +
							"           " +
							"    WHERE b.idt_codigo_nacional = t.idt_codigo_nacional(+)" +
							" GROUP BY b.cn, b.vlr_face" +
							" ORDER BY b.cn ASC, b.vlr_face ASC");
				
	
					preparedStatement.setString(1, request.getParameter("MES")+"/"+request.getParameter("ANO"));
					preparedStatement.setString(2, Uteis.arrayParaString(request.getParameterValues("CATEGORIA"),","));
					preparedStatement.setString(3, request.getParameter("MES")+"/"+request.getParameter("ANO"));
					preparedStatement.setString(4, Uteis.arrayParaString(request.getParameterValues("CATEGORIA"),","));
	
					ResultSet resultSet = preparedStatement.executeQuery(); 
					StringBuffer saida = new StringBuffer("<html><body><table border=1><tr><th bgcolor=#cccccc>Estado</th><th bgcolor=#cccccc>Valor de Face</th><th bgcolor=#cccccc >Quantidade</th><th bgcolor=#cccccc >Total</th></tr>");
					boolean diferente = false;
					String estado="",face="",qtd="",total="";
					
					while (resultSet.next())
					{ 
									if (!resultSet.isFirst())
									{ 
										diferente = !estado.equals(resultSet.getString(1));
										saida.append(linha(diferente,estado,face,qtd,total));
									}
									estado = resultSet.getString(1);
									face = resultSet.getString(2);
									qtd = resultSet.getString(3);
									total = resultSet.getString(4);
									
					} 
					saida.append(linha(true,estado,face,qtd,total));
					preparedStatement.close(); 
					resultSet.close();
					conexaoJDBC.close(); 
	
					saida.append("</table></body><html>");
					
					//response.addHeader("Pragma","no-cache");
					response.setContentType("application/vnd.ms-excel");
					PrintWriter out = response.getWriter();
					System.out.println(saida.toString());
					out.println(saida.toString());
	
					//request.setAttribute(Constantes.RESULT,saida.toString());								
				}

				}
				catch (SQLException e)
				{
					logger.error("Erro de SQL na geração do extrato para relatorio Excel Contingencia(" +
									e.getMessage() + ")");
					e.printStackTrace();
					
				}
				finally
				{
					if (conexaoJDBC!=null)
						conexaoJDBC.close();
				}
				
				//result = actionMapping.findForward("HTML");
				return result;
			}
		
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return this.codOperacao;
	}

	private String linha(boolean diferente, String estado, String face, String qtd, String total) throws SQLException
	{
			StringBuffer saida = new StringBuffer();
			String td = "<td "+(diferente?" bgcolor=yellow >":">");
			saida.append("<tr>"+td+(diferente?estado:"")+"</td>"); 
			saida.append(td+face+"</td>"); 
			saida.append(td+qtd+"</td>"); 
			saida.append(td+(diferente?total:"")+"</td></tr>"); 
			return saida.toString();
	}
}
