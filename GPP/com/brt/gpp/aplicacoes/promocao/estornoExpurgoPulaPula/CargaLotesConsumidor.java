package com.brt.gpp.aplicacoes.promocao.estornoExpurgoPulaPula;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.mapeamentos.MapPromocaoOrigemEstorno;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
 *	Classe responsavel pela carga de um registro do arquivo de 
 *  lote do Estorno de Bonus Pula-Pula.
 *  
 *  Formato do registro:
 *  
 *  Sintaxe: <LOTE>;<TIPO>;<TEL_ORIGEM>;<TEL_ASSINANTE>;<DATA_LIGACAO>[;<CAMPO_EXTRA>{;<CAMPO_EXTRA>}]
 *  
 *  Onde:
 *  
 *    LOTE				:= String em maíusculo
 *    TIPO 				:= String em maíusculo
 *    TEL_ORIGEM 		:= <COD_NACIONAL><TELEFONE>
 *    TEL_ASSINANTE		:= 55<COD_NACIONAL><TELEFONE>
 *    DATA_LIGACAO		:= <DIA>/<MES>/<ANO>
 *    CAMPO_EXTRA		:= String em maíusculo
 *      
 *    COD_NACIONAL	:= 2 dígitos numéricos   
 * 	  TELEFONE		:= 8 dígitos numéricos 
 *    DIA, MES, ANO	:= campo numérico
 *    
 *  Observações: 
 *  
 *    1 - os campos extras não são considerados nesse processo.
 *    2 - o usuário pode inserir a quantidade desejada de campos extras.
 *    3 - a data deve ser um valor possível, caso constrário o registro é 
 *        considerado mal formatado.
 *    4 - o campo TIPO é chave estrangeira para a tabela TBL_PRO_ORIGEM_ESTORNO.
 *        Caso o valor não exista no banco o registro é recusado.
 *    5 - os campos do tipo String nao sao validados em termo de espaços,
 *        quantidade de caracteres e caixa (conforme decisão em equipe).
 * 
 *	@author	Bernardo Vergne Dias
 *	@since	02/01/2007
 *	@review_author 
 *	@review_date dd/mm/yyyy
 */
public class CargaLotesConsumidor extends Aplicacoes implements ProcessoBatchConsumidor
{
	private CargaLotesProdutor produtor = null;
	private SimpleDateFormat dateFormat = null;
	
    /**
     *	Construtor da classe.
     */
	public CargaLotesConsumidor()
	{
		super(GerentePoolLog.getInstancia(CargaLotesConsumidor.class).
				getIdProcesso(Definicoes.CL_CARGA_LOTES_PRODUTOR), 
		        Definicoes.CL_CARGA_LOTES_CONSUMIDOR);
	}

    /**
     *	@see com.brt.gpp.comum.produtorConsumidor.Consumidor#startup()
     */
	public void startup() throws Exception
	{
	}

    /**
     *	@see com.brt.gpp.comum.produtorConsumidor.Consumidor#startup(Produtor)
     */
	public void startup(Produtor produtor) throws Exception
	{
	}
	
	 /**
     *	@see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor#startup(ProcessoBatchProdutor)
     */
	public void startup(ProcessoBatchProdutor produtor) throws Exception
	{
		this.produtor = (CargaLotesProdutor)produtor;
		this.dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	}
	
    /**
     *	@see com.brt.gpp.comum.produtorConsumidor.Consumidor#execute(Object)
     */
	public void execute(Object obj) throws Exception
	{
		CargaLotesVO vo = (CargaLotesVO)obj;
		
		try
		{
			RegistroLote registro = parseRegistroLote(vo); // valida os campos
			
			if (registro != null)
			{
				String sql = "INSERT INTO tbl_int_estorno_pula_pula (DAT_REFERENCIA, IDT_MSISDN," +
						" IDT_NUMERO_ORIGEM, IDT_ORIGEM, DAT_CADASTRO, IDT_STATUS_PROCESSAMENTO," +
						" IDT_LOTE) VALUES (?,?,?,?,?,?,?)";

				Timestamp now = new Timestamp(System.currentTimeMillis());
				
				Object[] params = {registro.getDataReferencia(), registro.getMsisdn(), registro.getTelOrigem(),
								   registro.getTipo(), now, Definicoes.IDT_PROCESSAMENTO_VALIDACAO,registro.getLote()}; 
				
				
				int res = produtor.getConexao().executaPreparedUpdate(sql, params, super.logId);
			    if (res  <= 0) throw new Exception("");
			}
		}
		catch(Exception e)
		{
			String erro = (e.getMessage() == null) ? "" : e.getMessage();
		    super.log(Definicoes.ERRO, "execute", "Erro ao inserir o registro " + vo.getNumLinha() 
		    		+ " [" + vo.getRegistro() + "] no banco. " + erro);
		    produtor.addMensagemErro("Erro ao gravar o registro " + vo.getNumLinha() 
		    		+ " no banco. Verifique se o conjunto MSISDN, TEL_ORIGEM e DATA_LIGACAO já foi inserido anteriormente no banco.");
		}
	}

    /**
     *	@see com.brt.gpp.comum.produtorConsumidor.Consumidor#finish()
     */
	public void finish()
	{
	}

	private RegistroLote parseRegistroLote(CargaLotesVO vo)
	{
		RegistroLote registro = new RegistroLote();
		
		String[] campo = vo.getRegistro().split(";");
		
		try
		{
			// valida quantidade minima de campos
			
			if (campo.length < 5)
			{
				throw new Exception("Linha " + vo.getNumLinha() + 
						": Especifique pelo menos 5 campos.");
			} 
			
			// campo LOTE
			
			registro.setLote(campo[0]);
			
			// valida campo TIPO
			
			if (MapPromocaoOrigemEstorno.getInstance().getTipAnalise(campo[1]) != null)
			{
				registro.setTipo(campo[1]);
			}
			else
			{
				throw new Exception("Linha " + vo.getNumLinha() + 
						", campo TIPO: valor informado (" + campo[1] + ") não consta no sistema.");
			}
			
			// valida campo TELEFONE_ORIGEM
			
			if (!campo[2].matches("^[0-9]{10}$"))
			{
				throw new Exception("Linha " + vo.getNumLinha() + 
						", campo TELEFONE_ORIGEM: especifique os 10 dígitos do telefone conforme o " +
						"formato 'aabbbbbbbb' (a = cod. nacional, b = telefone).");
			}
			
			registro.setTelOrigem(campo[2]);
			
			// valida campo TELEFONE_ASSINANTE
			
			if (!campo[3].matches("^55[0-9]{10}$"))
			{
				throw new Exception("Linha " + vo.getNumLinha() + 
						", campo TELEFONE_ASSINANTE: especifique os 12 dígitos do telefone conforme o " +
						"formato '55aabbbbbbbb' (a = cod. nacional, b = telefone).");
			}
			
			registro.setMsisdn(campo[3]);
			
			// valida campo DATA_LIGACAO
			
			try
			{
				registro.setDataReferencia(new Date(dateFormat.parse(campo[4], new ParsePosition(0)).getTime()));
			}
			catch (Exception e)
			{
				throw new Exception("Linha " + vo.getNumLinha() + 
						", campo DATA_LIGACAO: especifique a data conforme o " +
						"formato 'dd/mm/aaaa' (d = dia, m = mes, a = ano).");
			}
		}
		catch (Exception e) 
		{
			produtor.addMensagemErro(e.getMessage());
			return null;
		}

		return registro;
	}
}
