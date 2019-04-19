LOAD DATA
   INFILE 'csv.dat'
   APPEND
   INTO TABLE TBL_APR_ASSINANTE_TECNOMEN
   FIELDS TERMINATED BY X'9'
   TRAILING NULLCOLS
(
	ID
	,SUB_ID                                 "DECODE(INSTR(:SUB_ID,'55'),1,TRIM(:SUB_ID),'55'||TO_NUMBER(:SUB_ID))"
	,SRR_ID                                 FILLER
	,NATURE_OF_ADDRESS                      FILLER
	,NUMBERING_PLAN                         FILLER
	,IMSI                                   "TRIM(:IMSI)"
	,SM_ID                                  FILLER
	,SUB_CLASS                              FILLER
	,SERVICE_STATUS
	,TOTAL_MINUTES_USED                     FILLER
	,TOTAL_AMOUNT_USED                      FILLER
	,BLOCKING_TABLE_ID
	,RECHARGE_FRAUD_COUNTER
	,TOTAL_RECHARGE_ERROR_COUNTER
	,SUSPENDED_DATE                         Date "MON DD YYYY  HH12:MIPM"
	,ACTIVATION_DATE                        Date "MON DD YYYY  HH12:MIPM"
	,ACCOUNT_BALANCE
	,EXPIRY                                 Date "MON DD YYYY  HH12:MIPM"
	,ACCOUNT_STATUS
	,ACCOUNT_NUMBER                         FILLER
	,BALANCE_NOTIFICATION_STATUS            FILLER
	,SM_BALANCENOTIFICATIONSTATUS           FILLER
	,EXPIRY_NOTIFICATION_STATUS             FILLER
	,SM_EXPIRYNOTIFICATIONSTATUS            FILLER
	,LANGUAGE_ID                            FILLER
	,DIALECT_ID                             FILLER
	,SERVICE_FEE_DATE                       FILLER  --Date "MON DD YYYY  HH12:MIPM"
	,PROFILE_ID
	,SUB_OPTIONS
	,CURRENT_TARIFF_PLAN_ID                 FILLER
	,IVR_QUERY_CHARGING_EXPIRY              Date "MON DD YYYY  HH12:MIPM"
	,IVR_QUERY_COUNTER
	,PIN
	,PIN_STATUS
	,PIN_ERROR_COUNTER
	,CLONING_UPDATE                         FILLER  --TIMESTAMP 'MON DD YYYY  HH12:MI:SS:FF4PM'
	,HOSTNAME_ID                            FILLER
	,FAMILY_AND_FRIENDS                     "TRIM(:FAMILY_AND_FRIENDS)"
	,OUTGOING_COLLECT_CALL_LIST             FILLER
	,INCOMING_COLLECT_CALL_LIST             FILLER
	,USAGE_EXPIRY                           FILLER  --Date "MON DD YYYY  HH12:MIPM"
	,USAGE_ACCUMULATED_SECS                 FILLER
	,USAGE_ACCUMULATED_BALANCE              FILLER
	,USAGE_SM_COUNTER                       FILLER
	,USAGE_ANNOUNCEMENT_COUNTER             FILLER
	,PREFERRED_LDC                          FILLER  --"TRIM(:PREFERRED_LDC)"
	,SM_PK_ID                               FILLER
	,SM_PK_ID_NEXT                          FILLER
	,SM_PK_COUNTER                          FILLER
	,SM_PK_EXPIRY                           FILLER  --Date "MON DD YYYY  HH12:MIPM"
	,SM_PK_NOTIFICATION_STATUS              FILLER
	,FF_DISCOUNT                            FILLER
	,FF_EXPIRY                              FILLER  --Date "MON DD YYYY  HH12:MIPM"
	,RECHARGE_DISCOUNT                      FILLER
	,FROZEN_DATE                            Date "MON DD YYYY  HH12:MIPM"
	,RECHARGE_COUNT                         FILLER
	,RECHARGE_BONUS_EXPIRY                  FILLER  --Date "MON DD YYYY  HH12:MIPM"
	,BONUS_BALANCE
	,BONUS_EXPIRY                           Date "MON DD YYYY  HH12:MIPM"
	,LAST_RECHARGE_DATE                     FILLER  --Date "MON DD YYYY  HH12:MIPM"
	,USAGE_CALL_TYPES_COUNTER               FILLER
	,PERIODIC_BALANCE
	,PERIODIC_EXPIRY                        Date "MON DD YYYY  HH12:MIPM"
	,LIFECYCLE_CALL_CNTR_SECS               FILLER
	,PERIODIC_TARIFF_PLAN_ID                FILLER
	,LAST_SM_ID                             FILLER
	,CURRENT_VOUCHER_BALANCE                FILLER
	,OVERDRAFT_BALANCE                      FILLER
	,BASE_CURRENCY_ID                       FILLER
	,RESERVATION_COUNTER                    FILLER
	,LAST_RESERVATION_ID                    FILLER
	,MAIN_RESERVATION_AMOUNT                FILLER
	,DATA_RESERVATION_AMOUNT                FILLER
	,PERIODIC_RESERVATION_AMOUNT            FILLER
	,SM_RESERVATION_AMOUNT                  FILLER
	,BONUS_RESERVATION_AMOUNT               FILLER
	,BALANCE_UNITS                          FILLER
	,SERVICE_ID                             FILLER
	,PERIODIC_STATUS
	,PERIODIC_NOTIFICATION_STATUS           FILLER
	,DAT_IMPORTACAO                         EXPRESSION "TRUNC(SYSDATE)"
)
