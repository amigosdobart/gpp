package com.brt.gpp.comum.conexoes.tecnomen.holder;

import java.util.ArrayList;

import org.omg.CORBA.Any;
import org.omg.CORBA.ORB;

import TINC.tincRecord;
import TINC.tincSeqHolder;
import TINC.PPSubscriberAttributesPackage.ePPSubAtt;

import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.tecnomen.entidade.DataTEC;
import com.brt.gpp.comum.conexoes.tecnomen.entidade.MSISDN;
import com.brt.gpp.comum.mapeamentos.MapPlanoPreco;
import com.brt.gpp.comum.mapeamentos.MapTipoSaldo;
import com.brt.gpp.comum.mapeamentos.entidade.Categoria;
import com.brt.gpp.comum.mapeamentos.entidade.PlanoPreco;
import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;

/**
 *	Conversor de entidades Assinante para estrutura utilizada pelos servicos da Tecnomen.
 *
 *	@author		Daniel Ferreira
 *	@since		02/03/2007
 */
public class AssinanteHolder extends EntidadeGPPHolder
{
	/**
	 *	Constante interna da Tecnomen referente ao MSISDN do assinante.
	 */
	public static final int MSISDN = ePPSubAtt._SubId; 
	
	/**
	 *	Constante interna da Tecnomen referente ao Login do assinante.
	 */
	public static final int LOGIN = ePPSubAtt._Login; 
	
	/**
	 *	Constante interna da Tecnomen referente ao IMSI do assinante.
	 */
	public static final int IMSI = ePPSubAtt._Imsi;
	
	/**
	 *	Constante interna da Tecnomen referente ao Status de Servico do assinante.
	 */
	public static final int STATUS_SERVICO = ePPSubAtt._ServiceStatus; 
	
	/**
	 *	Constante interna da Tecnomen referente ao Status do assinante.
	 */
	public static final int STATUS = ePPSubAtt._AccountStatus; 
	
	/**
	 *	Constante interna da Tecnomen referente ao Status do assinante.
	 */
	public static final int STATUS_PERIODICO = ePPSubAtt._PeriodicStatus; 
	
	/**
	 *	Constante interna da Tecnomen referente ao Saldo Principal do assinante.
	 */
	public static final int SALDO_PRINCIPAL = ePPSubAtt._AccountBalance; 
	
	/**
	 *	Constante interna da Tecnomen referente ao Idioma do assinante.
	 */
	public static final int IDIOMA = ePPSubAtt._LanguageId; 
	
	/**
	 *	Constante interna da Tecnomen referente ao dialeto do assinante.
	 */
	public static final int DIALETO = ePPSubAtt._DialectId;
	
	/**
	 *	Constante interna da Tecnomen referente ao Plano de Preco do assinante.
	 */
	public static final int PLANO_PRECO = ePPSubAtt._ProfileId; 
	
	/**
	 *	Constante interna da Tecnomen referente a categoria do assinante. De acordo com a nomenclatura da Tecnomen, a 
	 *	categoria corresponde ao ID de servico. Exemplos sao: GSM, LigMix, Controle Total.
	 */
	public static final int CATEGORIA = ePPSubAtt._ServiceId;
	
	/**
	 *	Constante interna da Tecnomen referente ao byte de opcoes do assinante (Sub Options).
	 */
	public static final int OPCOES_ASSINANTE = ePPSubAtt._SubOptions; 
	
	/**
	 *	Constante interna da Tecnomen referente a Data de Expiracao Principal do assinante.
	 */
	public static final int EXPIRACAO_PRINCIPAL = ePPSubAtt._ExpiryDate; 
	
	/**
	 *	Constante interna da Tecnomen referente ao Data de Ativacao do assinante.
	 */
	public static final int DATA_ATIVACAO = ePPSubAtt._ActivationDate; 
	
	/**
	 *	Constante interna da Tecnomen referente a senha do assinante.
	 */
	public static final int SENHA = ePPSubAtt._PinNumber; 
	
	/**
	 *	Constante interna da Tecnomen referente ao status da senha do assinante.
	 */
	public static final int STATUS_SENHA = ePPSubAtt._PinStatus;
	
	/**
	 *	Constante interna da Tecnomen referente ao CSP default.
	 */
	public static final int CSP_DEFAULT = ePPSubAtt._PrefLongDistanceCarrier;
	
	/**
	 *	Constante interna da Tecnomen referente a Lista de Family & Friends do assinante.
	 */
	public static final int FAMILY_FRIENDS = ePPSubAtt._FamilyAndFriends; 
	
	/**
	 *	Constante interna da Tecnomen referente a ativacao da Lista de Family & Friends do assinante.
	 */
	public static final int ATIVAR_FF = ePPSubAtt._FFOnAtSubLevel; 
	
	/**
	 *	Constante interna da Tecnomen referente a ativacao de chamadas a cobrar.
	 */
	public static final int ATIVAR_CHAMADA_ACOBRAR = ePPSubAtt._LocalCollectCallOnAtSubLevel;
	
	/**
	 *	Constante interna da Tecnomen referente a ativacao do reset de senha no momento da primeira chamada.
	 */
	public static final int RESET_SENHA = ePPSubAtt._PinReset;
	
	/**
	 *	Constante interna da Tecnomen referente a Data de Congelamento do assinante.
	 */
	public static final int DATA_CONGELAMENTO = ePPSubAtt._FrozenDate; 
	
	/**
	 *	Constante interna da Tecnomen referente ao Saldo Periodico do assinante.
	 */
	public static final int SALDO_PERIODICO = ePPSubAtt._PeriodicBalance; 
	
	/**
	 *	Constante interna da Tecnomen referente a Data de Expiracao do Saldo Periodico do assinante.
	 */
	public static final int EXPIRACAO_PERIODICO = ePPSubAtt._PeriodicExpiry; 
	
	/**
	 *	Constante interna da Tecnomen referente ao Saldo de Bonus do assinante.
	 */
	public static final int SALDO_BONUS = ePPSubAtt._BonusBalance; 
	
	/**
	 *	Constante interna da Tecnomen referente a Data de Expiracao do Saldo de Bonus do assinante.
	 */
	public static final int EXPIRACAO_BONUS = ePPSubAtt._BonusExpiry;
	
	/**
	 * Constante interna da Tecnomen referente ao Plano Tarifário Periódico do assinante.
	 */
	public static final int TARIFF_PLAN_PERIODICO = ePPSubAtt._PeriodicTariffPlanId;
	
	/**
	 *	MSISDN do assinante. Este valor precisa ser definido devido ao fato de a Tecnomen nao utilizar as mesmas 
	 *	chaves nos metodos createSubscriber, getSubscriberDetails e updateSubscriberDetails. Portando o MSISDN do 
	 *	objeto Assinante sera ignorado na construcao da entidade da Tecnomen, sendo utilizados os valores de msisdn e 
	 *	login.
	 */
	private MSISDN msisdn;
	
	/**
	 *	Login do assinante. Este valor precisa ser definido devido ao fato de a Tecnomen nao utilizar as mesmas 
	 *	chaves nos metodos createSubscriber, getSubscriberDetails e updateSubscriberDetails. Portando o MSISDN do 
	 *	objeto Assinante sera ignorado na construcao da entidade da Tecnomen, sendo utilizados os valores de msisdn e 
	 *	login.
	 */
	private MSISDN login;
	
	/**
	 *	Entidade do GPP com as informacoes de um assinante.
	 */
	private Assinante assinante;
	
	/**
	 *	Entidade da Tecnomen com as informacoes de um assinante.
	 */
	private tincSeqHolder entidadeTEC;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		orb						Referencia ao servico da Tecnomen para estabelecimento de conexoes 
	 *										(Object Request Broker).
	 */
	public AssinanteHolder(ORB orb)
	{
		super(orb);
		
		this.assinante		= new Assinante();
		this.entidadeTEC	= new tincSeqHolder(new tincRecord[0]);
	}
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		orb						Referencia ao servico da Tecnomen para estabelecimento de conexoes 
	 *										(Object Request Broker).
	 *	@param		assinante				Entidade do GPP com as informacoes de um assinante.
	 */
	public AssinanteHolder(ORB orb, Object assinante)
	{
		super(orb);
		
		this.assinante		= (Assinante)assinante;
		this.entidadeTEC	= new tincSeqHolder(new tincRecord[0]);
	}
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		orb						Referencia ao servico da Tecnomen para estabelecimento de conexoes 
	 *										(Object Request Broker).
	 *	@param		entidadeTEC				Entidade da Tecnomen com as informacoes de um assinante.
	 */
	public AssinanteHolder(ORB orb, tincSeqHolder entidadeTEC)
	{
		super(orb);
		
		this.assinante		= new Assinante();
		this.entidadeTEC	= entidadeTEC;
	}
	
	/**
	 *	Atribui o MSISDN do assinante.
	 *
	 *	@param		msisdn				MSISDN do assinante.
	 */
	public void setMsisdn(String msisdn)
	{
		this.msisdn = new MSISDN(msisdn);
	}
	
	/**
	 *	Atribui o Login do assinante, que corresponde ao seu MSISDN, uma vez que assinantes no GPP e na Tecnomen nao 
	 *	possuem informacoes cadastrais.
	 *
	 *	@param		login				MSISDN do assinante.
	 */
	public void setLogin(String login)
	{
		this.login = new MSISDN(login);
	}
	
	/**
	 *	@see		com.brt.gpp.comum.conexoes.tecnomen.entidade.EntidadeGPPHolder#toEntidadeGPP()
	 */
	public Object toEntidadeGPP()
	{
		tincRecord[] atributo = (this.entidadeTEC != null) ? this.entidadeTEC.value : null;
		for(int i = 0; ((atributo != null) && (i < atributo.length)); i++)
			switch(atributo[i].id)
			{
				case AssinanteHolder.MSISDN:
					this.assinante.setMSISDN(new MSISDN(atributo[i].value.extract_string()).toMsisdnGPP());
					break;
				case AssinanteHolder.IMSI:
					this.assinante.setIMSI(atributo[i].value.extract_string());
					break;
				case AssinanteHolder.STATUS_SERVICO:
					this.assinante.setStatusServico(atributo[i].value.extract_short());
					break;
				case AssinanteHolder.STATUS:
					this.assinante.setStatusAssinante(atributo[i].value.extract_short());
					break;
				case AssinanteHolder.STATUS_PERIODICO:
					this.assinante.setStatusPeriodico(atributo[i].value.extract_short());
					break;
				case AssinanteHolder.SALDO_PRINCIPAL:
					this.assinante.setSaldoCreditosPrincipal((double)atributo[i].value.extract_long()/Definicoes.TECNOMEN_MULTIPLICADOR);
					break;
				case AssinanteHolder.IDIOMA: 
					this.assinante.setIdioma(atributo[i].value.extract_short());
					break;
				case AssinanteHolder.PLANO_PRECO:
					this.assinante.setPlanoPreco(atributo[i].value.extract_short());
					break;
				case AssinanteHolder.EXPIRACAO_PRINCIPAL:
					DataTEC dataExpPrincipal = new DataTEC();
					dataExpPrincipal.extrair(atributo[i].value);
					this.assinante.setDataExpiracaoPrincipal(dataExpPrincipal.toDate());
					break;
				case AssinanteHolder.DATA_ATIVACAO:
					DataTEC dataAtivacao = new DataTEC();
					dataAtivacao.extrair(atributo[i].value);
					this.assinante.setDataAtivacao(dataAtivacao.toDate());
					break;
				case AssinanteHolder.SENHA:
					this.assinante.setSenha(atributo[i].value.extract_string());
					break;
				case AssinanteHolder.FAMILY_FRIENDS:
					String listaFF = atributo[i].value.extract_string();
					this.assinante.setFriendFamily(listaFF != null ? listaFF : ";");
					break;
				case AssinanteHolder.DATA_CONGELAMENTO:
					DataTEC dataCongelamento = new DataTEC();
					dataCongelamento.extrair(atributo[i].value);
					this.assinante.setDataCongelamento(dataCongelamento.toDate());
					break;
				case AssinanteHolder.SALDO_PERIODICO:
					this.assinante.setSaldoCreditosPeriodico((double)atributo[i].value.extract_long()/Definicoes.TECNOMEN_MULTIPLICADOR);
					break;
				case AssinanteHolder.EXPIRACAO_PERIODICO:
					DataTEC dataExpPeriodico = new DataTEC();
					dataExpPeriodico.extrair(atributo[i].value);
					this.assinante.setDataExpiracaoPeriodico(dataExpPeriodico.toDate());
					break;
				case AssinanteHolder.SALDO_BONUS:
					this.assinante.setSaldoCreditosBonus((double)atributo[i].value.extract_long()/Definicoes.TECNOMEN_MULTIPLICADOR);
					break;
				case AssinanteHolder.EXPIRACAO_BONUS:
					DataTEC dataExpBonus = new DataTEC();
					dataExpBonus.extrair(atributo[i].value);
					this.assinante.setDataExpiracaoBonus(dataExpBonus.toDate());
					break;
				default: break;
			}

		//Se nao possui status periodico para configurado (contornando limitacao da Plataforma
		// quanto a inexistencia de status nao aplicavel
		if(!MapPlanoPreco.getInstance().getPlanoPreco(this.assinante.getPlanoPreco()).possuiStatusPeriodico())
			this.assinante.setStatusPeriodico(Definicoes.STATUS_PERIODICO_NAO_APLICAVEL);

		//Atribuindo a na natureza de acesso do assinante para GSM.
		this.assinante.setNaturezaAcesso("GSM");
		
		return this.assinante;
	}
	
	/**
	 *	@see		com.brt.gpp.comum.conexoes.tecnomen.entidade.EntidadeGPPHolder#toEntidadeTEC()
	 */
	public tincSeqHolder toEntidadeTEC()
	{
		ORB orb = super.getOrb();
		ArrayList atributos = new ArrayList();
		
		//Atribuindo o Login do assinante. Utilizado como chave da estrutura referente aos detalhes da conta do 
		//assinante durante a ativacao. OBS: Ao menos um dos valores, MSISDN ou Login, deve ser definido. Isto e 
		//feito atraves da chamada dos metodos setters desta classe feita pelas conexoes.
		if(this.login != null)
		{
			Any anyLogin = orb.create_any();
			anyLogin.insert_string(this.login.toMsisdnTEC());
			tincRecord login = new tincRecord(AssinanteHolder.LOGIN, anyLogin);
			atributos.add(login);
		}
		
		//Atribuindo o MSISDN do assinante, caso seja definido. OBS: Ao menos um dos valores, MSISDN ou Login, deve 
		//ser definido. Isto e feito atraves da chamada dos metodos setters desta classe feita pelas conexoes.
		if(this.msisdn != null)
		{
			Any anyMsisdn = orb.create_any();
			anyMsisdn.insert_string(this.msisdn.toMsisdnTEC());
			tincRecord msisdn = new tincRecord(AssinanteHolder.MSISDN, anyMsisdn);
			atributos.add(msisdn);
		}
		
		//Atribuindo o IMSI do assinante, caso aplicavel.
		if(this.assinante.getIMSI() != null)
		{
			//Aqui e necessario verificar se o plano de preco esta definido. Se estiver, e necessario verificar se 
			//os assinantes do plano possuem IMSI. Se possuirem, e informado o IMSI conforme o atributo do objeto 
			//assinante. Caso contrario, e informado o MSISDN do assinante no formato para a Tecnomen. Se o plano 
			//nao estiver definido, a verificao e ignorada.
			Any anyImsi = orb.create_any();
			if((this.assinante.getPlanoPreco() <= 0) ||
			   (MapPlanoPreco.getInstance().getPlanoPreco(this.assinante.getPlanoPreco()).possuiImsi()))
				anyImsi.insert_string(this.assinante.getIMSI());
			else
				anyImsi.insert_string(this.msisdn.toMsisdnTEC());
			tincRecord imsi = new tincRecord(AssinanteHolder.IMSI, anyImsi);
			atributos.add(imsi);
		}
		
		//Atribuindo o plano de preco do assinante, caso aplicavel. Se o plano de preco estiver definido, tambem devem
		//ser definidas as informacoes proprias de cada categoria. Esta definicao e requisito necessario para 
		//configuracao do plano do assinante na Tecnomen.
		if(this.assinante.getPlanoPreco() > 0)
		{
			Any anyPlano = orb.create_any();
			anyPlano.insert_short(this.assinante.getPlanoPreco());
			tincRecord plano = new tincRecord(AssinanteHolder.PLANO_PRECO, anyPlano);
			atributos.add(plano);
			
			//Obtendo as informacoes do plano do assinante.
			PlanoPreco planoPreco = MapPlanoPreco.getInstancia().getPlanoPreco(this.assinante.getPlanoPreco());
			
			//Periodic Tariff Plan
			Any anyPeriodicTariffPlan = orb.create_any();
			anyPeriodicTariffPlan.insert_short(planoPreco.getIdtPlanoTarifarioPeriodico());
			tincRecord planoPeriodico = new tincRecord(AssinanteHolder.TARIFF_PLAN_PERIODICO, anyPeriodicTariffPlan);
			
			//Obtendo as informacoes especificas da categoria do assinante.
			Categoria categoria = MapPlanoPreco.getInstancia().getPlanoPreco(this.assinante.getPlanoPreco()).getCategoria();
			
			//Service ID.
			Any anyCategoriaTEC = orb.create_any();
			anyCategoriaTEC.insert_short(categoria.getCategoriaTEC().getIdtCategoria());
			tincRecord categoriaTEC = new tincRecord(AssinanteHolder.CATEGORIA, anyCategoriaTEC);
			
			//Dialect ID.
			Any anyDialeto = orb.create_any();
			anyDialeto.insert_short(categoria.getCategoriaTEC().getIdtDialeto());
			tincRecord dialeto = new tincRecord(AssinanteHolder.DIALETO, anyDialeto);

			//PIN Status.
			Any anyStatusSenha = orb.create_any();
			anyStatusSenha.insert_short(categoria.getCategoriaTEC().getIdtStatusSenha());
			tincRecord statusSenha = new tincRecord(AssinanteHolder.STATUS_SENHA, anyStatusSenha);
			
			//Preferred LDC.
			Any anyCspDefault = orb.create_any();
			anyCspDefault.insert_string(String.valueOf(categoria.getCategoriaTEC().getNumCspDefault()));
			tincRecord cspDefault = new tincRecord(AssinanteHolder.CSP_DEFAULT, anyCspDefault);
			
			//Local Collect Call on at Sub Level.
			Any anyChamadaACobrar = orb.create_any();
			anyChamadaACobrar.insert_boolean(categoria.getCategoriaTEC().permiteChamadaACobrar());
			tincRecord chamadaACobrar = new tincRecord(AssinanteHolder.ATIVAR_CHAMADA_ACOBRAR, anyChamadaACobrar);
			
			//PIN Reset.
			Any anyResetSenha = orb.create_any();
			anyResetSenha.insert_boolean(categoria.getCategoriaTEC().resetarSenha());
			tincRecord resetSenha = new tincRecord(AssinanteHolder.RESET_SENHA, anyResetSenha);
			
			atributos.add(planoPeriodico);
			atributos.add(categoriaTEC);
			atributos.add(dialeto);
			atributos.add(statusSenha);
			atributos.add(cspDefault);
			atributos.add(chamadaACobrar);
			atributos.add(resetSenha);
		}
		
		//Atribuindo o status do assinante, caso aplicavel.
		if(this.assinante.getStatusAssinante() > 0)
		{
			Any anyStatus = orb.create_any();
			anyStatus.insert_short(this.assinante.getStatusAssinante());
			tincRecord status = new tincRecord(AssinanteHolder.STATUS, anyStatus);
			atributos.add(status);
		}
		
		//Atribuindo o status periodico do assinante, caso aplicavel.
		if(this.assinante.getStatusPeriodico() > 0)
		{
			Any anyStatus = orb.create_any();
			anyStatus.insert_short(this.assinante.getStatusPeriodico());
			tincRecord status = new tincRecord(AssinanteHolder.STATUS_PERIODICO, anyStatus);
			atributos.add(status);
		}
		
		//Atribuindo o status de servico do assinante, caso aplicavel.
		if(this.assinante.getStatusServico() > 0)
		{
			Any anyStatus = orb.create_any();
			anyStatus.insert_short(this.assinante.getStatusServico());
			tincRecord status = new tincRecord(AssinanteHolder.STATUS_SERVICO, anyStatus);
			atributos.add(status);
		}
		
		//Atribuindo a lista de Amigos Toda Hora (Family and Friends) do assinante, caso aplicavel.
		if(this.assinante.getFriendFamily() != null)
		{
			//Se a lista de ATH estiver vazia, e necessario desligar o bit de ativacao do F&F do assinante. Caso 
			//contrario e necessario liga-lo. Isto e feito para otimizar a performance das ligacoes.
			Any anyAtivarFF = orb.create_any();
			anyAtivarFF.insert_boolean(!this.assinante.getFriendFamily().equals(";"));
			tincRecord ativarFF = new tincRecord(AssinanteHolder.ATIVAR_FF, anyAtivarFF);
			
			Any anyFF = orb.create_any();
			anyFF.insert_string(this.assinante.getFriendFamily());
			tincRecord familyFriends = new tincRecord(AssinanteHolder.FAMILY_FRIENDS, anyFF);
			
			atributos.add(ativarFF);
			atributos.add(familyFriends);
		}
		
		//Atribuindo a senha do assinante, caso aplicavel.
		if(this.assinante.getSenha() != null)
		{
			Any anySenha = orb.create_any();
			anySenha.insert_string(this.assinante.getSenha());
			tincRecord senha = new tincRecord(AssinanteHolder.SENHA, anySenha);
			atributos.add(senha);
		}
		
		//Atribuindo o idioma do assinante, caso aplicavel.
		if(this.assinante.getIdioma() > 0)
		{
			Any anyIdioma = orb.create_any();
			anyIdioma.insert_short(this.assinante.getIdioma());
			tincRecord idioma = new tincRecord(AssinanteHolder.IDIOMA, anyIdioma);
			atributos.add(idioma);
		}
		
		//Atribuindo os creditos no Saldo Principal do assinante, caso aplicavel.
		if(this.assinante.getCreditosPrincipal() > 0.0)
		{
			Any anyCreditos = orb.create_any();
			anyCreditos.insert_long((int)(this.assinante.getCreditosPrincipal()*Definicoes.TECNOMEN_MULTIPLICADOR));
			tincRecord creditos = new tincRecord(AssinanteHolder.SALDO_PRINCIPAL, anyCreditos);
			atributos.add(creditos);
		}
		
		//Atribuindo a data de expiracao do Saldo Principal do assinante, caso aplicavel.
		if(this.assinante.getDataExpPrincipal() != null)
		{
			Any anyDataExp = orb.create_any();
			DataTEC dataExp = new DataTEC(this.assinante.getDataExpPrincipal());
			dataExp.inserir(anyDataExp);
			tincRecord dataExpPrincipal = new tincRecord(AssinanteHolder.EXPIRACAO_PRINCIPAL, anyDataExp);
			atributos.add(dataExpPrincipal);
		}
		
		//Atribuindo os creditos no Saldo Periodico do assinante, caso aplicavel.
		if(this.assinante.getCreditosPeriodico() > 0.0)
		{
			Any anyCreditos = orb.create_any();
			anyCreditos.insert_long((int)(this.assinante.getCreditosPeriodico()*Definicoes.TECNOMEN_MULTIPLICADOR));
			tincRecord creditos = new tincRecord(AssinanteHolder.SALDO_PERIODICO, anyCreditos);
			atributos.add(creditos);
		}
		
		//Atribuindo a data de expiracao do Saldo Periodico do assinante, caso aplicavel.
		if(this.assinante.getDataExpPeriodico() != null)
		{
			Any anyDataExp = orb.create_any();
			DataTEC dataExp = new DataTEC(this.assinante.getDataExpPeriodico());
			dataExp.inserir(anyDataExp);
			tincRecord dataExpPeriodico = new tincRecord(AssinanteHolder.EXPIRACAO_PERIODICO, anyDataExp);
			atributos.add(dataExpPeriodico);
		}
		
		//Atribuindo os creditos no Saldo de Bonus do assinante, caso aplicavel.
		if(this.assinante.getCreditosBonus() > 0.0)
		{
			Any anyCreditos = orb.create_any();
			anyCreditos.insert_long((int)(this.assinante.getCreditosBonus()*Definicoes.TECNOMEN_MULTIPLICADOR));
			tincRecord creditos = new tincRecord(AssinanteHolder.SALDO_BONUS, anyCreditos);
			atributos.add(creditos);
		}
		
		//Atribuindo a data de expiracao do Saldo de Bonus do assinante, caso aplicavel.
		if(this.assinante.getDataExpBonus() != null)
		{
			Any anyDataExp = orb.create_any();
			DataTEC dataExp = new DataTEC(this.assinante.getDataExpBonus());
			dataExp.inserir(anyDataExp);
			tincRecord dataExpBonus = new tincRecord(AssinanteHolder.EXPIRACAO_BONUS, anyDataExp);
			atributos.add(dataExpBonus);
		}
		
		//Preenchendo o objeto a ser enviado a Tecnomen.
		this.entidadeTEC.value = new tincRecord[atributos.size()];
		
		for(int i = 0; i < atributos.size(); i++)
			this.entidadeTEC.value[i] = (tincRecord)atributos.get(i);
		
		return this.entidadeTEC;
	}
	
}
