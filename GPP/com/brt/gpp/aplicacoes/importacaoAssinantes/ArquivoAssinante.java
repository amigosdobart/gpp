package com.brt.gpp.aplicacoes.importacaoAssinantes;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.aplicacoes.importacaoCDR.entidade.ArquivoDados;
import com.brt.gpp.aplicacoes.promocao.entidade.TotalizacaoPulaPula;

import java.util.Date;
import java.util.Locale;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.sql.Timestamp;

public class ArquivoAssinante implements ArquivoDados
{
	private Date		dataImportacao;
	private long        id;
	private String      subId;
	private int         natureOfAddress;
	private int         numberingPlan;
	private String      imsi;
	private String      smId;
	private long        subClass;
	private int         serviceStatus;
	private long        totalMinutesUsed;
	private long        totalAmountUsed;
	private int         blockingTableId;
	private long        rechargeFraudCounter;
	private long        totalRechargeErrorCounter;
	private Date        suspendedDate;
	private Date        activationDate;
	private long        accountBalance;
	private Date        expiry;
	private int         accountStatus;
	private String      accountNumber;
	private int         balanceNotificationStatus;
	private int         smBalancenotificationstatus;
	private int         expiryNotificationStatus;
	private int         smExpirynotificationstatus;
	private int         languageId;
	private int         dialectId;
	private Date        serviceFeeDate;
	private int         profileId;
	private long        subOptions;
	private int         currentTariffPlanId;
	private int         numVouchers;
	private long        voucherTypes;
	private Date        ivrQueryChargingExpiry;
	private long        ivrQueryCounter;
	private String      pin;
	private int         pinStatus;
	private long        pinErrorCounter;
	private Date	    cloningUpdate;
	private long        hostnameId;
	private String      familyAndFriends;
	private Date        usageExpiry;
	private long        usageAccumulatedSecs;
	private long        usageAccumulatedBalance;
	private int         usageSmCounter;
	private int         usageAnnouncementCounter;
	private long        preferredLdc;
	private long        smPkId;
	private long        smPkIdNext;
	private long        smPkCounter;
	private Date        smPkExpiry;
	private int         smPkNotificationStatus;
	private int         ffDiscount;
	private Date        ffExpiry;
	private int         rechargeDiscount;
	private Date        frozenDate;
	private int         rechargeCount;
	private Date        rechargeBonusExpiry;
	private long        rechargeBonusAmount;
	private long		bonusBalance;
	private Date		bonusExpiry;
	private long		dataBalance;
	private Date		dataExpiry;
	private long		smBalance;
	private Date		smExpiry;
	private long		periodicBalance;
	private Date		periodicExpiry;
	private Date		lastRechargeDate;
	private String		usageCallTypesCounter;
	
	/**
	 * Metodo...:parse
	 * Descricao:Realiza o parse da linha passada como parametro
	 *           construindo as informacoes baseadas na ordem neste construtor
	 * @param String linhaArquivo	- String contendo a linha do arquivo de CDR a ser
	 */
	public void parse(String linhaArquivo)
	{
		// Faz a quebra da linha em varios campos do tipo String
		String campos[] = linhaArquivo.split("\t");
			
		// Realiza a definicao das informacoes na ordem abaixo. Cada campo
		// possui em seu metodo o validador da informacao. Em caso de erro
		// entao uma excecao IllegalArgumentException e retornada
		setId(campos[0]);
		setSubId(campos[1]);
		setNatureOfAddress(campos[2]);
		setNumberingPlan(campos[3]);
		setImsi(campos[4]);
		setSmId(campos[5]);
		setSubClass(campos[6]);
		setServiceStatus(campos[7]);
		setTotalMinutesUsed(campos[8]);
		setTotalAmountUsed(campos[9]);
		setBlockingTableId(campos[10]);
		setRechargeFraudCounter(campos[11]);
		setTotalRechargeErrorCounter(campos[12]);
		setSuspendedDate(campos[13]);
		setActivationDate(campos[14]);
		setAccountBalance(campos[15]);
		setExpiry(campos[16]);
		setAccountStatus(campos[17]);
		setAccountNumber(campos[18]);
		setBalanceNotificationStatus(campos[19]);
		setSmBalancenotificationstatus(campos[20]);
		setExpiryNotificationStatus(campos[21]);
		setSmExpirynotificationstatus(campos[22]);
		setLanguageId(campos[23]);
		setDialectId(campos[24]);
		setServiceFeeDate(campos[25]);
		setProfileId(campos[26]);
		setSubOptions(campos[27]);
		setCurrentTariffPlanId(campos[28]);
		setNumVouchers(campos[29]);
		setVoucherTypes(campos[30]);
		setIvrQueryChargingExpiry(campos[31]);
		setIvrQueryCounter(campos[32]);
		setPin(campos[33]);
		setPinStatus(campos[34]);
		setPinErrorCounter(campos[35]);
		setCloningUpdate(campos[36]);
		setHostnameId(campos[37]);
		setFamilyAndFriends(campos[38]);
		setUsageExpiry(campos[39]);
		setUsageAccumulatedSecs(campos[40]);
		setUsageAccumulatedBalance(campos[41]);
		setUsageSmCounter(campos[42]);
		setUsageAnnouncementCounter(campos[43]);
		setPreferredLdc(campos[44]);
		setSmPkId(campos[45]);
		setSmPkIdNext(campos[46]);
		setSmPkCounter(campos[47]);
		setSmPkExpiry(campos[48]);
		setSmPkNotificationStatus(campos[49]);
		setFfDiscount(campos[50]);
		setFfExpiry(campos[51]);
		setRechargeDiscount(campos[52]);
		setFrozenDate(campos[53]);
		setRechargeCount(campos[54]);
		setRechargeBonusExpiry(campos[55]);
		// De acordo com o pessoal da Tecnomen, este campo foi descontinuado
		// para a versao do multiplos saldos
		//setRechargeBonusAmount(campos[56]);
		
		setBonusBalance(campos[56]);
		setBonusExpiry(campos[57]);
		setDataBalance(campos[58]);
		setDataExpiry(campos[59]);
		setLastRechargeDate(campos[60]);
		setUsageCallTypesCounter(campos[61]);
		setPeriodicBalance(campos[62]);
		setPeriodicExpiry(campos[63]);
		setSmBalance(campos[64]);
		setSmExpiry(campos[65]);
	}

	/**
	 * Metodo....:getComandoInsert
	 * Descricao.:Retorna o comando SQL para insert na tabela
	 * @return String - Comando SQL
	 */
	private String getComandoInsert()
	{
		String sql = "INSERT INTO TBL_APR_ASSINANTE_TECNOMEN " +
					"(DAT_IMPORTACAO " +
					",ID " +
					",SUB_ID " +
					",NATURE_OF_ADDRESS " +
					",NUMBERING_PLAN " +
					",IMSI " +
					",SM_ID " +
					",SUB_CLASS " +
					",SERVICE_STATUS " +
					",TOTAL_MINUTES_USED " +
					",TOTAL_AMOUNT_USED " +
					",BLOCKING_TABLE_ID " +
					",RECHARGE_FRAUD_COUNTER " +
					",TOTAL_RECHARGE_ERROR_COUNTER " +
					",SUSPENDED_DATE " +
					",ACTIVATION_DATE " +
					",ACCOUNT_BALANCE " +
					",EXPIRY " +
					",ACCOUNT_STATUS " +
					",ACCOUNT_NUMBER " +
					",BALANCE_NOTIFICATION_STATUS " +
					",SM_BALANCENOTIFICATIONSTATUS " +
					",EXPIRY_NOTIFICATION_STATUS " +
					",SM_EXPIRYNOTIFICATIONSTATUS " +
					",LANGUAGE_ID " +
					",DIALECT_ID " +
					",SERVICE_FEE_DATE " +
					",PROFILE_ID " +
					",SUB_OPTIONS " +
					",CURRENT_TARIFF_PLAN_ID " +
					",NUM_VOUCHERS " +
					",VOUCHER_TYPES " +
					",IVR_QUERY_CHARGING_EXPIRY " +
					",IVR_QUERY_COUNTER " +
					",PIN " +
					",PIN_STATUS " +
					",PIN_ERROR_COUNTER " +
					",CLONING_UPDATE " +
					",HOSTNAME_ID " +
					",FAMILY_AND_FRIENDS " +
					",USAGE_EXPIRY " +
					",USAGE_ACCUMULATED_SECS " +
					",USAGE_ACCUMULATED_BALANCE " +
					",USAGE_SM_COUNTER " +
					",USAGE_ANNOUNCEMENT_COUNTER " +
					",PREFERRED_LDC " +
					",SM_PK_ID " +
					",SM_PK_ID_NEXT " +
					",SM_PK_COUNTER " +
					",SM_PK_EXPIRY " +
					",SM_PK_NOTIFICATION_STATUS " +
					",FF_DISCOUNT " +
					",FF_EXPIRY " +
					",RECHARGE_DISCOUNT " +
					",FROZEN_DATE " +
					",RECHARGE_COUNT " +
					",RECHARGE_BONUS_EXPIRY " +
					",RECHARGE_BONUS_AMOUNT " +
					",BONUS_BALANCE " +
					",BONUS_EXPIRY " +
					",DATA_BALANCE " +
					",DATA_EXPIRY " +
					",LAST_RECHARGE_DATE " +
					",USAGE_CALL_TYPES_COUNTER " +
					",PERIODIC_BALANCE " +
					",PERIODIC_EXPIRY " +
					",SM_BALANCE " +
					",SM_EXPIRY " +
					") " +
					"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, " +
					"        ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, " +
					"        ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, " +
					"        ?,?,?,?,?,?,?,?,?,?,?)";

		return sql;
	}

	/**
	 * Metodo....:getParametrosInsert
	 * Descricao.:Retorna a lista de parametros a serem vinculados ao comando SQL
	 * @return Object[] - Array de valores dos parametros do SQL
	 */
	private Object[] getParametrosInsert()
	{
		Object[] param = {	new java.sql.Date(getDataImportacao() != null ? getDataImportacao().getTime() : Calendar.getInstance().getTimeInMillis()),
				            new Long(getId())								,getSubId()									,
							new Integer(getNatureOfAddress())				,new Integer(getNumberingPlan())			,getImsi(),
							getSmId()										,new Long(getSubClass())					,new Integer(getServiceStatus()),
							new Long(getTotalMinutesUsed())					,new Long(getTotalAmountUsed())				,new Integer(getBlockingTableId()),
							new Long(getRechargeFraudCounter())				,new Long(getTotalRechargeErrorCounter())	,new Timestamp(getSuspendedDate().getTime()),
							new Timestamp(getActivationDate().getTime())	,new Long(getAccountBalance())				,new Timestamp(getExpiry().getTime()),
							new Integer(getAccountStatus())					,getAccountNumber()							,new Integer(getBalanceNotificationStatus()),
							new Integer(getSmBalancenotificationstatus())	,new Integer(getExpiryNotificationStatus())	,new Integer(getSmExpirynotificationstatus()),
							new Integer(getLanguageId())					,new Integer(getDialectId())				,new Timestamp(getServiceFeeDate().getTime()),
							new Integer(getProfileId())						,new Long(getSubOptions())					,new Integer(getCurrentTariffPlanId()),
							new Integer(getNumVouchers())					,new Long(getVoucherTypes())				,new Timestamp(getIvrQueryChargingExpiry().getTime()),
							new Long(getIvrQueryCounter())					,getPin()									,new Integer(getPinStatus()),
							new Long(getPinErrorCounter())					,new Timestamp(getCloningUpdate().getTime()),new Long(getHostnameId()),
							getFamilyAndFriends()							,new Timestamp(getUsageExpiry().getTime())	,new Long(getUsageAccumulatedSecs()),
							new Long(getUsageAccumulatedBalance())			,new Integer(getUsageSmCounter())			,new Integer(getUsageAnnouncementCounter()),
							new Long(getPreferredLdc())						,new Long(getSmPkId())						,new Long(getSmPkIdNext()),
							new Long(getSmPkCounter())						,new Timestamp(getSmPkExpiry().getTime())	,new Integer(getSmPkNotificationStatus()),
							new Integer(getFfDiscount())					,new Timestamp(getFfExpiry().getTime())		,new Integer(getRechargeDiscount()),
							new Timestamp(getFrozenDate().getTime())		,new Integer(getRechargeCount())			,new Timestamp(getRechargeBonusExpiry().getTime()),
							new Long(getRechargeBonusAmount())				,new Long(getBonusBalance())				,new Timestamp(getBonusExpiry().getTime()),
							new Long(getDataBalance())						,new Timestamp(getDataExpiry().getTime())	,new Timestamp(getLastRechargeDate().getTime()),
							getUsageCallTypesCounter()						,new Long(getPeriodicBalance())				,new Timestamp(getPeriodicExpiry().getTime()),
							new Long(getSmBalance())						,new Timestamp(getSmExpiry().getTime())
						};
		return param;
	}

	/**
	 * Metodo....:getComandosSQLInsert
	 * Descricao.:Retorna uma lista de comandos insert a serem executados por este CDR
	 * @return	String - Lista de comandos SQL a serem executados para insercao de dados
	 */
	public String[] getComandosSQLInsert(PREPConexao prepConexao) throws GPPInternalErrorException
	{
		String[] comando = {getComandoInsert()};
		return comando;
	}
		
	/**
	 * Metodo....:getParametrosSQLInsert
	 * Descricao.:Busca os parametros dos comandos insert a serem executados
	 *            para insercao dos dados na tabela correspondente
	 * @return Object[] - Lista de parametros a ser efetivada juntamente com o
	 *                    comando SQL de Insert
	 */
	public Object[][] getParametrosSQLInsert(PREPConexao prepConexao) throws GPPInternalErrorException
	{
		Object[][] parametros = {getParametrosInsert()};
		return parametros;
	}

	/**
	 * Metodo....:getIdProcessoBatch
	 * Descricao.:Retorna o id do processo batch que sera registrado no historico
	 *            quando for importado arquivos deste tipo
	 * @return int - Id do processo batch para este arquivo de dados
	 */
	public int getIdProcessoBatch()
	{
		return Definicoes.IND_IMPORTACAO_ASSINANTES;
	}

	/**
	 * @return
	 */
	public long getAccountBalance() {
		return accountBalance;
	}

	/**
	 * @return
	 */
	public String getAccountNumber() {
		return accountNumber;
	}

	/**
	 * @return
	 */
	public int getAccountStatus() {
		return accountStatus;
	}

	/**
	 * @return
	 */
	public Date getActivationDate() {
		return activationDate;
	}

	/**
	 * @return
	 */
	public int getBalanceNotificationStatus() {
		return balanceNotificationStatus;
	}

	/**
	 * @return
	 */
	public int getBlockingTableId() {
		return blockingTableId;
	}

	/**
	 * @return
	 */
	public Date getCloningUpdate() {
		return cloningUpdate;
	}

	/**
	 * @return
	 */
	public int getCurrentTariffPlanId() {
		return currentTariffPlanId;
	}

	/**
	 * @return
	 */
	public int getDialectId() {
		return dialectId;
	}

	/**
	 * @return
	 */
	public Date getExpiry() {
		return expiry;
	}

	/**
	 * @return
	 */
	public int getExpiryNotificationStatus() {
		return expiryNotificationStatus;
	}

	/**
	 * @return
	 */
	public String getFamilyAndFriends() {
		return familyAndFriends;
	}

	/**
	 * @return
	 */
	public int getFfDiscount() {
		return ffDiscount;
	}

	/**
	 * @return
	 */
	public Date getFfExpiry() {
		return ffExpiry;
	}

	/**
	 * @return
	 */
	public Date getFrozenDate() {
		return frozenDate;
	}

	/**
	 * @return
	 */
	public long getHostnameId() {
		return hostnameId;
	}

	/**
	 * @return
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return
	 */
	public String getImsi() {
		return imsi;
	}

	/**
	 * @return
	 */
	public Date getIvrQueryChargingExpiry() {
		return ivrQueryChargingExpiry;
	}

	/**
	 * @return
	 */
	public long getIvrQueryCounter() {
		return ivrQueryCounter;
	}

	/**
	 * @return
	 */
	public int getLanguageId() {
		return languageId;
	}

	/**
	 * @return
	 */
	public int getNatureOfAddress() {
		return natureOfAddress;
	}

	/**
	 * @return
	 */
	public int getNumberingPlan() {
		return numberingPlan;
	}

	/**
	 * @return
	 */
	public int getNumVouchers() {
		return numVouchers;
	}

	/**
	 * @return
	 */
	public String getPin() {
		return pin;
	}

	/**
	 * @return
	 */
	public long getPinErrorCounter() {
		return pinErrorCounter;
	}

	/**
	 * @return
	 */
	public int getPinStatus() {
		return pinStatus;
	}

	/**
	 * @return
	 */
	public long getPreferredLdc() {
		return preferredLdc;
	}

	/**
	 * @return
	 */
	public int getProfileId() {
		return profileId;
	}

	/**
	 * @return
	 */
	public long getRechargeBonusAmount() {
		return rechargeBonusAmount;
	}

	/**
	 * @return
	 */
	public Date getRechargeBonusExpiry() {
		return rechargeBonusExpiry;
	}

	/**
	 * @return
	 */
	public int getRechargeCount() {
		return rechargeCount;
	}

	/**
	 * @return
	 */
	public int getRechargeDiscount() {
		return rechargeDiscount;
	}

	/**
	 * @return
	 */
	public long getRechargeFraudCounter() {
		return rechargeFraudCounter;
	}

	/**
	 * @return
	 */
	public Date getServiceFeeDate() {
		return serviceFeeDate;
	}

	/**
	 * @return
	 */
	public int getServiceStatus() {
		return serviceStatus;
	}

	/**
	 * @return
	 */
	public int getSmBalancenotificationstatus() {
		return smBalancenotificationstatus;
	}

	/**
	 * @return
	 */
	public int getSmExpirynotificationstatus() {
		return smExpirynotificationstatus;
	}

	/**
	 * @return
	 */
	public String getSmId() {
		return smId;
	}

	/**
	 * @return
	 */
	public long getSmPkCounter() {
		return smPkCounter;
	}

	/**
	 * @return
	 */
	public Date getSmPkExpiry() {
		return smPkExpiry;
	}

	/**
	 * @return
	 */
	public long getSmPkId() {
		return smPkId;
	}

	/**
	 * @return
	 */
	public long getSmPkIdNext() {
		return smPkIdNext;
	}

	/**
	 * @return
	 */
	public int getSmPkNotificationStatus() {
		return smPkNotificationStatus;
	}

	/**
	 * @return
	 */
	public long getSubClass() {
		return subClass;
	}

	/**
	 * @return
	 */
	public String getSubId() {
		return subId;
	}

	/**
	 * @return
	 */
	public long getSubOptions() {
		return subOptions;
	}

	/**
	 * @return
	 */
	public Date getSuspendedDate() {
		return suspendedDate;
	}

	/**
	 * @return
	 */
	public long getTotalAmountUsed() {
		return totalAmountUsed;
	}

	/**
	 * @return
	 */
	public long getTotalMinutesUsed() {
		return totalMinutesUsed;
	}

	/**
	 * @return
	 */
	public long getTotalRechargeErrorCounter() {
		return totalRechargeErrorCounter;
	}

	/**
	 * @return
	 */
	public long getUsageAccumulatedBalance() {
		return usageAccumulatedBalance;
	}

	/**
	 * @return
	 */
	public long getUsageAccumulatedSecs() {
		return usageAccumulatedSecs;
	}

	/**
	 * @return
	 */
	public int getUsageAnnouncementCounter() {
		return usageAnnouncementCounter;
	}

	/**
	 * @return
	 */
	public Date getUsageExpiry() {
		return usageExpiry;
	}

	/**
	 * @return
	 */
	public int getUsageSmCounter() {
		return usageSmCounter;
	}

	/**
	 * @return
	 */
	public long getVoucherTypes() {
		return voucherTypes;
	}

	/**
	 * @return Returns the bonusBalance.
	 */
	public long getBonusBalance() {
		return bonusBalance;
	}

	/**
	 * @return Returns the bonusExpiry.
	 */
	public Date getBonusExpiry() {
		return bonusExpiry;
	}

	/**
	 * @return Returns the dataBalance.
	 */
	public long getDataBalance() {
		return dataBalance;
	}

	/**
	 * @return Returns the dataExpiry.
	 */
	public Date getDataExpiry() {
		return dataExpiry;
	}

	/**
	 * @return Returns the lastRechargeDate.
	 */
	public Date getLastRechargeDate() {
		return lastRechargeDate;
	}

	/**
	 * @return Returns the periodicBalance.
	 */
	public long getPeriodicBalance() {
		return periodicBalance;
	}

	/**
	 * @return Returns the periodicExpiry.
	 */
	public Date getPeriodicExpiry() {
		return periodicExpiry;
	}

	/**
	 * @return Returns the smBalance.
	 */
	public long getSmBalance() {
		return smBalance;
	}

	/**
	 * @return Returns the smExpiry.
	 */
	public Date getSmExpiry() {
		return smExpiry;
	}

	/**
	 * @return Returns the usageCallTypesCounter.
	 */
	public String getUsageCallTypesCounter() {
		return usageCallTypesCounter;
	}

	public Date getDataImportacao()
	{
		return dataImportacao;
	}

	/**
	 * @param l
	 */
	public void setAccountBalance(String string)
	{
		if (string != null && !string.trim().equals(""))
			accountBalance = Long.parseLong(string);
	}

	/**
	 * @param string
	 */
	public void setAccountNumber(String string)
	{
		accountNumber = string;
	}

	/**
	 * @param i
	 */
	public void setAccountStatus(String string)
	{
		if (string != null && !string.trim().equals(""))
			accountStatus = Integer.parseInt(string);
	}

	/**
	 * Metodo....:parseDate
	 * Descricao.:Este metodo realiza o parse de uma data retornando
	 *            null caso o formato da data passada como parametro
	 *            nao corresponda ao formato informado
	 * @param format	- Formato
	 * @param data		- Data a ser convertida
	 * @return Date		- Data convertida
	 */
	private Date parseDate(String format, String data)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(format, new Locale(Locale.ENGLISH.getLanguage(),Locale.US.getCountry()));
		try
		{
			return sdf.parse(data);
		}
		catch(ParseException e)
		{
			return null;
		}
	}

	/**
	 * @param date
	 */
	public void setActivationDate(String string)
	{
		Date data=null;
		if (string != null && !string.trim().equals(""))
			if ( (data = parseDate("MMM d yyyy h:mma",string)) != null )
				activationDate = data;
	}

	/**
	 * @param i
	 */
	public void setBalanceNotificationStatus(String string)
	{
		if (string != null && !string.trim().equals(""))
			balanceNotificationStatus = Integer.parseInt(string);
	}

	/**
	 * @param i
	 */
	public void setBlockingTableId(String string)
	{
		if (string != null && !string.trim().equals(""))
			blockingTableId = Integer.parseInt(string);
	}

	/**
	 * @param date
	 */
	public void setCloningUpdate(String string)
	{
		Date data=null;
		if (string != null && !string.trim().equals(""))
			if ( (data = parseDate("MMM d yyyy h:mm:ss:SSSa",string)) != null )
				cloningUpdate = data;
	}

	/**
	 * @param i
	 */
	public void setCurrentTariffPlanId(String string)
	{
		if (string != null && !string.trim().equals(""))
			currentTariffPlanId = Integer.parseInt(string);
	}

	/**
	 * @param i
	 */
	public void setDialectId(String string)
	{
		if (string != null && !string.trim().equals(""))
			dialectId = Integer.parseInt(string);
	}

	/**
	 * @param date
	 */
	public void setExpiry(String string)
	{
		Date data=null;
		if (string != null && !string.trim().equals(""))
			if ( (data = parseDate("MMM d yyyy h:mma",string)) != null )
				expiry = data;
	}

	/**
	 * @param i
	 */
	public void setExpiryNotificationStatus(String string)
	{
		if (string != null && !string.trim().equals(""))
			expiryNotificationStatus = Integer.parseInt(string);
	}

	/**
	 * @param string
	 */
	public void setFamilyAndFriends(String string)
	{
		if (string != null && !string.trim().equals(""))
		familyAndFriends = string;
	}

	/**
	 * @param i
	 */
	public void setFfDiscount(String string)
	{
		if (string != null && !string.trim().equals(""))
			ffDiscount = Integer.parseInt(string);
	}

	/**
	 * @param date
	 */
	public void setFfExpiry(String string)
	{
		Date data=null;
		if (string != null && !string.trim().equals(""))
			if ( (data = parseDate("MMM d yyyy K:mma",string)) != null )
				ffExpiry = data;
	}

	/**
	 * @param date
	 */
	public void setFrozenDate(String string)
	{
		Date data=null;
		if (string != null && !string.trim().equals(""))
			if ( (data = parseDate("MMM d yyyy h:mma",string)) != null )
				frozenDate = data;
	}

	/**
	 * @param l
	 */
	public void setHostnameId(String string)
	{
		if (string != null && !string.trim().equals(""))
			hostnameId = Long.parseLong(string);
	}

	/**
	 * @param l
	 */
	public void setId(String string)
	{
		if (string != null && !string.trim().equals(""))
			id = Long.parseLong(string);
	}

	/**
	 * @param string
	 */
	public void setImsi(String string)
	{
		if (string != null && !string.trim().equals(""))
		imsi = string;
	}

	/**
	 * @param date
	 */
	public void setIvrQueryChargingExpiry(String string)
	{
		Date data=null;
		if (string != null && !string.trim().equals(""))
			if ( (data = parseDate("MMM d yyyy h:mma",string)) != null )
				ivrQueryChargingExpiry = data;
	}

	/**
	 * @param l
	 */
	public void setIvrQueryCounter(String string)
	{
		if (string != null && !string.trim().equals(""))
			ivrQueryCounter = Long.parseLong(string);
	}

	/**
	 * @param i
	 */
	public void setLanguageId(String string)
	{
		if (string != null && !string.trim().equals(""))
			languageId = Integer.parseInt(string);
	}

	/**
	 * @param i
	 */
	public void setNatureOfAddress(String string)
	{
		if (string != null && !string.trim().equals(""))
			natureOfAddress = Integer.parseInt(string);
	}

	/**
	 * @param i
	 */
	public void setNumberingPlan(String string)
	{
		if (string != null && !string.trim().equals(""))
			numberingPlan = Integer.parseInt(string);
	}

	/**
	 * @param i
	 */
	public void setNumVouchers(String string)
	{
		if (string != null && !string.trim().equals(""))
			numVouchers = Integer.parseInt(string);
	}

	/**
	 * @param string
	 */
	public void setPin(String string)
	{
		if (string != null && !string.trim().equals(""))
		pin = string;
	}

	/**
	 * @param l
	 */
	public void setPinErrorCounter(String string)
	{
		if (string != null && !string.trim().equals(""))
			pinErrorCounter = Long.parseLong(string);
	}

	/**
	 * @param i
	 */
	public void setPinStatus(String string)
	{
		if (string != null && !string.trim().equals(""))
			pinStatus = Integer.parseInt(string);
	}

	/**
	 * @param l
	 */
	public void setPreferredLdc(String string)
	{
		if (string != null && !string.trim().equals(""))
			preferredLdc = Long.parseLong(string);
	}

	/**
	 * @param i
	 */
	public void setProfileId(String string)
	{
		if (string != null && !string.trim().equals(""))
			profileId = Integer.parseInt(string);
	}

	/**
	 * @param l
	 */
	public void setRechargeBonusAmount(String string)
	{
		if (string != null && !string.trim().equals(""))
			rechargeBonusAmount = Long.parseLong(string);
	}

	/**
	 * @param date
	 */
	public void setRechargeBonusExpiry(String string)
	{
		Date data=null;
		if (string != null && !string.trim().equals(""))
			if ( (data = parseDate("MMM d yyyy h:mma",string)) != null )
				rechargeBonusExpiry = data;
	}

	/**
	 * @param i
	 */
	public void setRechargeCount(String string)
	{
		if (string != null && !string.trim().equals(""))
			rechargeCount = Integer.parseInt(string);
	}

	/**
	 * @param i
	 */
	public void setRechargeDiscount(String string)
	{
		if (string != null && !string.trim().equals(""))
			rechargeDiscount = Integer.parseInt(string);
	}

	/**
	 * @param l
	 */
	public void setRechargeFraudCounter(String string)
	{
		if (string != null && !string.trim().equals(""))
			rechargeFraudCounter = Long.parseLong(string);
	}

	/**
	 * @param date
	 */
	public void setServiceFeeDate(String string)
	{
		Date data=null;
		if (string != null && !string.trim().equals(""))
			if ( (data = parseDate("MMM d yyyy h:mma",string)) != null )
				serviceFeeDate = data;
	}

	/**
	 * @param i
	 */
	public void setServiceStatus(String string)
	{
		if (string != null && !string.trim().equals(""))
			serviceStatus = Integer.parseInt(string);
	}

	/**
	 * @param i
	 */
	public void setSmBalancenotificationstatus(String string)
	{
		if (string != null && !string.trim().equals(""))
			smBalancenotificationstatus = Integer.parseInt(string);
	}

	/**
	 * @param i
	 */
	public void setSmExpirynotificationstatus(String string)
	{
		if (string != null && !string.trim().equals(""))
			smExpirynotificationstatus = Integer.parseInt(string);
	}

	/**
	 * @param string
	 */
	public void setSmId(String string)
	{
		if (string != null && !string.trim().equals(""))
		smId = string;
	}

	/**
	 * @param l
	 */
	public void setSmPkCounter(String string)
	{
		if (string != null && !string.trim().equals(""))
			smPkCounter = Long.parseLong(string);
	}

	/**
	 * @param date
	 */
	public void setSmPkExpiry(String string)
	{
		Date data=null;
		if (string != null && !string.trim().equals(""))
			if ( (data = parseDate("MMM d yyyy h:mma",string)) != null )
				smPkExpiry = data;
	}

	/**
	 * @param l
	 */
	public void setSmPkId(String string)
	{
		if (string != null && !string.trim().equals(""))
			smPkId = Long.parseLong(string);
	}

	/**
	 * @param l
	 */
	public void setSmPkIdNext(String string)
	{
		if (string != null && !string.trim().equals(""))
			smPkIdNext = Long.parseLong(string);
	}

	/**
	 * @param i
	 */
	public void setSmPkNotificationStatus(String string)
	{
		if (string != null && !string.trim().equals(""))
			smPkNotificationStatus = Integer.parseInt(string);
	}

	/**
	 * @param l
	 */
	public void setSubClass(String string)
	{
		if (string != null && !string.trim().equals(""))
			subClass = Long.parseLong(string);
	}

	/**
	 * @param string
	 */
	public void setSubId(String string)
	{
		if (string == null || string.trim().equals(""))
			throw new IllegalArgumentException("Campo SubId nao pode ser nulo.");

		subId = string;
	}

	/**
	 * @param l
	 */
	public void setSubOptions(String string)
	{
		if (string != null && !string.trim().equals(""))
			subOptions = Long.parseLong(string);
	}

	/**
	 * @param date
	 */
	public void setSuspendedDate(String string)
	{
		Date data=null;
		if (string != null && !string.trim().equals(""))
			if ( (data = parseDate("MMM d yyyy h:mma",string)) != null )
				suspendedDate = data;
	}

	/**
	 * @param l
	 */
	public void setTotalAmountUsed(String string)
	{
		if (string != null && !string.trim().equals(""))
			totalAmountUsed = Long.parseLong(string);
	}

	/**
	 * @param l
	 */
	public void setTotalMinutesUsed(String string)
	{
		if (string != null && !string.trim().equals(""))
			totalMinutesUsed = Long.parseLong(string);
	}

	/**
	 * @param l
	 */
	public void setTotalRechargeErrorCounter(String string)
	{
		if (string != null && !string.trim().equals(""))
			totalRechargeErrorCounter = Long.parseLong(string);
	}

	/**
	 * @param l
	 */
	public void setUsageAccumulatedBalance(String string)
	{
		if (string != null && !string.trim().equals(""))
			usageAccumulatedBalance = Long.parseLong(string);
	}

	/**
	 * @param l
	 */
	public void setUsageAccumulatedSecs(String string)
	{
		if (string != null && !string.trim().equals(""))
			usageAccumulatedSecs = Long.parseLong(string);
	}

	/**
	 * @param i
	 */
	public void setUsageAnnouncementCounter(String string)
	{
		if (string != null && !string.trim().equals(""))
			usageAnnouncementCounter = Integer.parseInt(string);
	}

	/**
	 * @param date
	 */
	public void setUsageExpiry(String string)
	{
		Date data=null;
		if (string != null && !string.trim().equals(""))
			if ( (data = parseDate("MMM d yyyy h:mma",string)) != null )
				usageExpiry = data;
	}

	/**
	 * @param i
	 */
	public void setUsageSmCounter(String string)
	{
		if (string != null && !string.trim().equals(""))
			usageSmCounter = Integer.parseInt(string);
	}

	/**
	 * @param l
	 */
	public void setVoucherTypes(String string)
	{
		if (string != null && !string.trim().equals(""))
			voucherTypes = Long.parseLong(string);
	}

	/**
	 * @param bonusBalance The bonusBalance to set.
	 */
	public void setBonusBalance(String string) {
		if (string != null && !string.trim().equals(""))
			bonusBalance = Long.parseLong(string);
	}

	/**
	 * @param bonusExpiry The bonusExpiry to set.
	 */
	public void setBonusExpiry(String string) {
		Date data=null;
		if (string != null && !string.trim().equals(""))
			if ( (data = parseDate("MMM d yyyy h:mma",string)) != null )
				bonusExpiry = data;
	}

	/**
	 * @param dataBalance The dataBalance to set.
	 */
	public void setDataBalance(String string) {
		if (string != null && !string.trim().equals(""))
			dataBalance = Long.parseLong(string);
	}

	/**
	 * @param dataExpiry The dataExpiry to set.
	 */
	public void setDataExpiry(String string) {
		Date data=null;
		if (string != null && !string.trim().equals(""))
			if ( (data = parseDate("MMM d yyyy h:mma",string)) != null )
				dataExpiry = data;
	}

	/**
	 * @param lastRechargeDate The lastRechargeDate to set.
	 */
	public void setLastRechargeDate(String string) {
		Date data=null;
		if (string != null && !string.trim().equals(""))
			if ( (data = parseDate("MMM d yyyy h:mma",string)) != null )
				lastRechargeDate = data;
	}

	/**
	 * @param periodicBalance The periodicBalance to set.
	 */
	public void setPeriodicBalance(String string) {
		if (string != null && !string.trim().equals(""))
			periodicBalance = Long.parseLong(string);
	}

	/**
	 * @param periodicExpiry The periodicExpiry to set.
	 */
	public void setPeriodicExpiry(String string) {
		Date data=null;
		if (string != null && !string.trim().equals(""))
			if ( (data = parseDate("MMM d yyyy h:mma",string)) != null )
				periodicExpiry = data;
	}

	/**
	 * @param smBalance The smBalance to set.
	 */
	public void setSmBalance(String string) {
		if (string != null && !string.trim().equals(""))
			smBalance = Long.parseLong(string);
	}

	/**
	 * @param smExpiry The smExpiry to set.
	 */
	public void setSmExpiry(String string) {
		Date data=null;
		if (string != null && !string.trim().equals(""))
			if ( (data = parseDate("MMM d yyyy h:mma",string)) != null )
				smExpiry = data;
	}

	/**
	 * @param usageCallTypesCounter The usageCallTypesCounter to set.
	 */
	public void setUsageCallTypesCounter(String string) {
		if (string != null && !string.trim().equals(""))
			usageCallTypesCounter = string;
	}
	
	/**
	 * @param l
	 */
	public void setDataImportacao(String dtImport, String formato)
	{
		// Tenta definir a data de importacao passada como parametro como String.
		// Caso a data nao seja valida entao a data de hoje e utilizada
		SimpleDateFormat sdf = new SimpleDateFormat(formato);
		try
		{
			dataImportacao = sdf.parse(dtImport);
		}
		catch(ParseException pe)
		{
			dataImportacao = Calendar.getInstance().getTime();
		}
	}
	
	/**
	 * Metodo....:deveTotalizarParaPromocao
	 * Descricao.:Indica se deve totalizar para a promocao as informacoes do CDR. Para isso
	 *            os metodos que indicam a promocao pula pula ou friends and family sao utilizados
	 * @return boolean - Flag indicando se deve retornar registro contendo informacoes da totalizacao
	 */
	public boolean deveTotalizarParaPromocao()
	{
		return false;
	}
	
	/**
	 * Metodo....:getTotalizacaoPulaPula
	 * Descricao.:Retorna o objeto contendo as informacoes relativas a totalizacao da promocao 
	 *            OBS: Nesse metodo o objeto SEMPRE retornar como valor de minutos o minuto somente
	 *                 do CDR sendo processado. A totalizacao de todos os valores eh realizado pela
	 *                 thread de importacao de dados.
	 */
	public TotalizacaoPulaPula getTotalizacaoPulaPula()
	{
		return null;
	}
}
