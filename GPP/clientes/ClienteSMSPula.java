package clientes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;



public class ClienteSMSPula{
		private ResultSet num = null;
		private Connection con = null;
		private PreparedStatement call_id = null;	
		private SimpleDateFormat dfLog = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		public static void main(String args[])throws Exception{
			ClienteSMSPula pulapula = new ClienteSMSPula();
			pulapula.log( "Inicio do processo ClienteSMSPula");
			pulapula.init(args[0], args[1], args[2], args[3]);
			pulapula.run(args[0], args[1], args[2], args[3], Integer.parseInt(args[4]));	
			pulapula.log( "Fim do processo ClienteSMSPula");
		}

		public void log(String msg){
			System.out.println(dfLog.format(new java.util.Date()) + " - " + msg);
		}
		private void init(String sid, String usuario, String senha, String data) throws Exception{
			 Class.forName("oracle.jdbc.driver.OracleDriver");
			 

//			 ResultSet rs = null;
			 try {
				 con =  DriverManager.getConnection("jdbc:oracle:oci8:@"+sid,
				   usuario, senha);
				 call_id = con.prepareStatement(getSelecaoClientes(data));
				 num = call_id.executeQuery();

			 }
			 catch(Exception e){
				 e.printStackTrace();
			 }			        
		}

		private void run(String sid, String usuario, String senha, String data, int numThreads) throws Exception{
			ThreadGroup tg = new ThreadGroup("Envio SMS");
			for(int i=0; i<numThreads; i++){
				ProcessaSMS threads = new ProcessaSMS(tg, "Processamento[" + i + "]", sid, usuario, senha, data, this);	
				threads.start();
			}
			
			while(tg.activeCount() > 0){
				 try{Thread.sleep(1000);}catch(Exception ignorado){}
			}
			num.close();
			call_id.close();
			con.close();
		}
		
		public synchronized String nextClient() throws SQLException{
			if(num.next()){
				return num.getString(1);
			}
			else{
				return null;
			}
		}

	/**
	 * Retorna a string que sera utilizado pela consulta para selecionar os usuario conforme
	 * regras definidas pelo marketing
	 * 
	 * @param datas 
	 * @return
	 * @throws ParseException
	 */
	private String getSelecaoClientes(String datas) throws ParseException {
		String result = null;
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//		SimpleDateFormat dfMes = new SimpleDateFormat("MM/yyyy");
		java.util.Date data = df.parse(datas);
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		switch (cal.get(Calendar.DAY_OF_WEEK)) {

			case Calendar.MONDAY :
				result =
					"SELECT IDT_MSISDN FROM TBL_GER_PROMOCAO_ASSINANTE A "
						+ " WHERE "
						+ " idt_promocao = 1 AND "
						+ " TO_CHAR(dat_entrada_promocao, 'DD') IN ('01', '02', '03', '04' ) ";
				break;

			case Calendar.TUESDAY :
				result =
					"SELECT IDT_MSISDN FROM TBL_GER_PROMOCAO_ASSINANTE A "
						+ " WHERE "
						+ " idt_promocao = 1 AND "
						+ " TO_CHAR(dat_entrada_promocao, 'DD') IN ( '05', '06', '07', '08', '09' ) ";
				break;

			case Calendar.WEDNESDAY :
				result =
					"SELECT IDT_MSISDN FROM TBL_GER_PROMOCAO_ASSINANTE A "
						+ " WHERE "
						+ " idt_promocao = 1 AND "
						+ " TO_CHAR(dat_entrada_promocao, 'DD') IN ('10', '11', '12', '13', '14') ";
				break;

			case Calendar.THURSDAY :
				result =
					"SELECT IDT_MSISDN FROM TBL_GER_PROMOCAO_ASSINANTE A "
						+ " WHERE "
						+ " idt_promocao = 1 AND "
						+ " TO_CHAR(dat_entrada_promocao, 'DD') IN ('15', '16', '17', '18', '19')";
				break;

			case Calendar.FRIDAY :
				result =
					"SELECT IDT_MSISDN FROM TBL_GER_PROMOCAO_ASSINANTE A "
						+ " WHERE "
						+ " idt_promocao = 1 AND "
						+ " TO_CHAR(dat_entrada_promocao, 'DD') IN ('20', '21', '22', '23', '24') ";
				break;
			case Calendar.SATURDAY :
				result =
					"SELECT IDT_MSISDN FROM TBL_GER_PROMOCAO_ASSINANTE A "
						+ " WHERE "
						+ " idt_promocao = 1 AND "
						+ " TO_CHAR(dat_entrada_promocao, 'DD') IN ('25', '26', '27', '28', '29') ";
				break;

			case Calendar.SUNDAY :
				result =
					"SELECT IDT_MSISDN FROM TBL_GER_PROMOCAO_ASSINANTE A "
						+ " WHERE "
						+ " idt_promocao = 1 AND "
						+ " TO_CHAR(dat_entrada_promocao, 'DD') IN ('30', '31')  ";
				break;

		}

		return result;

	}


        
}

class ProcessaSMS extends Thread{
	private Connection con=null;
	private PreparedStatement dia = null;
	private PreparedStatement insert = null;
	private ClienteSMSPula pulapula = null;
	private String nome;
	private final int LIMITE_CREDITO = 1000;
	SimpleDateFormat dfDia = new SimpleDateFormat("dd");
	java.util.Date data;

	public ProcessaSMS(ThreadGroup tg, String nome, String sid, String usuario, String senha, String datas, ClienteSMSPula pulapula) throws SQLException, ParseException	{
			super(tg, nome);
			this.pulapula = pulapula;
			this.nome=nome;
//			SimpleDateFormat dfPartition = new SimpleDateFormat("yyyyMM");
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		
			data = df.parse(datas);
			
			con =  DriverManager.getConnection("jdbc:oracle:oci8:@"+sid,
			   usuario, senha);
			
			dia = con.prepareStatement( " SELECT SUB_ID /*+ index(a xpktbl_ger_cdr) */ ,  " +  
									 " SUM(  (CALL_DURATION / 60) *  B.VLR_BONUS_MINUTO ) as credito   " +  
									 " FROM tbl_ger_cdr  " +									// " partition(pc"+ dfPartition.format(data) +") " +									 " a,  " +  
									 " TBL_GER_BONUS_PULA_PULA B " +  
									 " where   " +  
									 " a.sub_id = ? and  " +  
									 " a.timestamp  between to_date('"+ df.format(primeiroDiaMes(data))+"', 'dd/mm/yyyy') " +									 " and to_date('"+  df.format(data)+"', 'dd/mm/yyyy') and  " +  
									 " a.transaction_type in (select transaction_type from  " +  
									 "                        tbl_ger_promocao_transaction   " +  
									 "                        where idt_promocao = 1) and   " +  
									 " a.tip_chamada in (select rate_name from   " +  
									 "                   tbl_ger_promocao_rate_name   " +  
									 "                   where idt_promocao = 1) and  " +  
									 " a.num_csp in ('00', '14')  AND   " +  
									 " SUBSTR(A.SUB_ID, 3,2) = B.ID_CODIGO_NACIONAL (+)  " +  
									 " group by sub_id  " ); 
			
			
			insert = con.prepareStatement("insert into tbl_ger_envio_sms(id_registro, idt_msisdn, des_mensagem, num_prioridade, " +
			" dat_envio_sms, idt_status_processamento, dat_processamento,  " +
			" tip_sms)  " +
			"   values(      " +
			
			" SEQ_ENVIO_SMS.NEXTVAL, ?, ? ,   " +
			" 1, sysdate, 1, sysdate, 'PULAPULA')    " );
					
	}
	private java.util.Date primeiroDiaMes(java.util.Date data){
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		cal.set(Calendar.DAY_OF_MONTH,1);
		return cal.getTime();
	}
	
	
	public void run() {
		String msisdn = null;
		DecimalFormat df = new DecimalFormat("#,##0.00");
		try{
			while((msisdn = pulapula.nextClient())!= null ){
					pulapula.log(nome + "- Processando - " + msisdn);
					dia.setString(1, msisdn);
					ResultSet rs = dia.executeQuery();
					if(rs.next()){
						double valor = rs.getDouble(2);
						pulapula.log(nome + "-" + msisdn + "-"+df.format(valor));
						String mensagem = null;
						
						if(valor < LIMITE_CREDITO){
							mensagem = "Voce acumulou R$" + df.format(valor)+ " nas chamadas recebidas entre os dias 1 a " + dfDia.format(data) +
										". Tenha sempre creditos ativos para ter direito ao bonus (credito no proximo dia 5)";
						}
						else{
							mensagem = "Para receber o bonus do seu Pula-Pula faca uma recarga a cada 30 dias.";
							
						}
						
						insert.setString(1, msisdn);
						insert.setString(2, mensagem);
						insert.executeUpdate();
					}
					rs.close();
			}	
			dia.close();
			insert.close();
			con.close();
		}
		catch(SQLException e){
			pulapula.log("Exception : " + msisdn + " : " + e.getMessage());
			e.printStackTrace();
		}
	}

}