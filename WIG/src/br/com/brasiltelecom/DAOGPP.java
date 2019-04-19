package br.com.brasiltelecom;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

public class DAOGPP {
	private InitialContext ictx;

	private Logger log = null;

	public DAOGPP(InitialContext ictx) {
		this.ictx = ictx;
		log = Logger.getLogger(this.getClass());
	}
/**
 * Verifica no GPP se o cliente eh um cliente Pré-pago
 * @param msisdn
 * @return true - cliente Pré-pago
 * 		   false - não é um cliente Pré-pago
 */
	public boolean ehPrepago(String msisdn) {
		boolean result = false;
		Connection conprepr = null;
		PreparedStatement statementprepr = null;
		ResultSet resultsetprepr = null;
		try {
			if (log.isDebugEnabled()) {
				log.debug("ehPrepago: " + msisdn);
			}
			DataSource dsprepr = (DataSource) ictx
					.lookup("java:/comp/env/jdbc/WPP_GPP");
			conprepr = dsprepr.getConnection();

			statementprepr = conprepr
					.prepareStatement(" SELECT count(*) FROM TBL_APR_ASSINANTE WHERE IDT_MSISDN = ? AND IDT_PLANO_PRECO NOT IN (4,5) ");
			statementprepr.setString(1, msisdn);
			resultsetprepr = statementprepr.executeQuery();
			if (resultsetprepr.next()) {
				if (resultsetprepr.getInt(1) > 0) {
					if (log.isDebugEnabled()) {
						log.debug("msisdn :" + msisdn + "e um prepago");
					}
					result = true;
				}
			}

		} catch (Exception e) {
			log.error("ehPrepago " + e);
		} finally {
			try {
				if (statementprepr != null)
					statementprepr.close();
				if (resultsetprepr != null)
					resultsetprepr.close();
				if (conprepr != null)
					conprepr.close();
			} catch (Exception e) {
				log.error("ehPrepago ", e);
			}
		}
		return result;
	}
	/**
	 * Verifica no GPP se o cliente eh um cliente Pré-pago
	 * @param msisdn
	 * @return true - cliente Pré-pago
	 * 		   false - não é um cliente Pré-pago
	 */
		public void salvaDesbloqueio(String xml) {
			Connection conprepr = null;
			PreparedStatement statementprepr = null;
			try {
				
				long id = findIdXML(xml);
				DataSource dsprepr = (DataSource) ictx
						.lookup("java:/comp/env/jdbc/WPP_GPP");
				conprepr = dsprepr.getConnection();
				
				statementprepr = conprepr.prepareStatement("insert into tbl_int_ppp_out(id_processamento, dat_cadastro, idt_evento_negocio," +
                "xml_document, idt_status_processamento) values ( ?, sysdate, 'BLOQUEIO_ASAP', ?, 'N') ");
				if (log.isDebugEnabled()) {
					log.debug("id: " + id);
					log.debug("xml: " + xml);
				}

				statementprepr.setLong(1, id);
				statementprepr.setString(2, xml);
				statementprepr.execute();

			} catch (Exception e) {
				log.error("salvarDesbloqueio" + e);
			} finally {
				try {
					if (statementprepr != null)
						statementprepr.close();
					if (conprepr != null)
						conprepr.close();
				} catch (Exception e) {
					log.error("salvarDesbloqueio", e);
				}
			}
		}	
		
		private long findIdXML(String xml){
				Pattern p = Pattern.compile("<id_os>(\\d.*)</id_os>");
				Matcher m = p.matcher(xml);
				if(m.find()){
					return Long.parseLong(m.group(1).trim());
				}
				return 0;
		}

		
		/**
		 * Verifica no GPP se o cliente eh um cliente Pré-pago
		 * @param msisdn
		 * @return true - cliente Pré-pago
		 * 		   false - não é um cliente Pré-pago
		 */
			public long getInterfaceId() {
				long result = 0;
				Connection conprepr = null;
				PreparedStatement statementprepr = null;
				ResultSet resultsetprepr = null;
				try {
					DataSource dsprepr = (DataSource) ictx
							.lookup("java:/comp/env/jdbc/WPP_GPP");
					conprepr = dsprepr.getConnection();
					statementprepr = conprepr.prepareStatement("select SEQ_ID_PROCESSAMENTO.NEXTVAL from dual");
					resultsetprepr = statementprepr.executeQuery();
					if (resultsetprepr.next()) {
						result = resultsetprepr.getLong(1);
					}

				} catch (Exception e) {
					log.error("getInterfaceId " + e);
				} finally {
					try {
						if (statementprepr != null)
							statementprepr.close();
						if (resultsetprepr != null)
							resultsetprepr.close();
						if (conprepr != null)
							conprepr.close();
					} catch (Exception e) {
						log.error("getInterfaceId ", e);
					}
				}
				return result;
			}
		
	
}
