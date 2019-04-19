package com.brt.gpp.componentes.aprovisionamento.orb;

/**
 * <ul>
 * <li> <b>IDL Source</b>    "Aprovisionamento.idl"
 * <li> <b>IDL Name</b>      ::com::brt::gpp::componentes::aprovisionamento::orb::aprovisionamento
 * <li> <b>Repository Id</b> IDL:com/brt/gpp/componentes/aprovisionamento/orb/aprovisionamento:1.0
 * </ul>
 * <b>IDL definition:</b>
 * <pre>
 * interface aprovisionamento {
  ...
};
 * </pre>
 */
public interface aprovisionamentoOperations {
  /**
   * <pre>
   *   short ativaAssinante (in string MSISDN, in string IMSI,
                        in string planoPreco, in double creditoInicial,
                        in short idioma, in string operador)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException,
            com.brt.gpp.comum.gppExceptions.GPPCorbaException,
            com.brt.gpp.comum.gppExceptions.GPPTecnomenException);
   * </pre>
   */
  public short ativaAssinante (java.lang.String MSISDN, 
                               java.lang.String IMSI, 
                               java.lang.String planoPreco, 
                               double creditoInicial, 
                               short idioma, 
                               java.lang.String operador) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                                 com.brt.gpp.comum.gppExceptions.GPPCorbaException, 
                                                                 com.brt.gpp.comum.gppExceptions.GPPTecnomenException;

  /**
   * <pre>
   *   com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoDesativacaoAssinante desativaAssinante (in string MSISDN,
                                                                                                                      in string motivoDesativacao,
                                                                                                                      in string operador)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException,
            com.brt.gpp.comum.gppExceptions.GPPCorbaException,
            com.brt.gpp.comum.gppExceptions.GPPTecnomenException);
   * </pre>
   */
  public com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoDesativacaoAssinante desativaAssinante (java.lang.String MSISDN, 
                                                                                                                             java.lang.String motivoDesativacao, 
                                                                                                                             java.lang.String operador) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                                                                                                                               com.brt.gpp.comum.gppExceptions.GPPCorbaException, 
                                                                                                                                                               com.brt.gpp.comum.gppExceptions.GPPTecnomenException;

  /**
   * <pre>
   *   string desativarAssinanteXML (in string MSISDN, in string motivoDesativacao,
                                in string operador)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException,
            com.brt.gpp.comum.gppExceptions.GPPCorbaException,
            com.brt.gpp.comum.gppExceptions.GPPTecnomenException);
   * </pre>
   */
  public java.lang.String desativarAssinanteXML (java.lang.String MSISDN, 
                                                 java.lang.String motivoDesativacao, 
                                                 java.lang.String operador) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                                                   com.brt.gpp.comum.gppExceptions.GPPCorbaException, 
                                                                                   com.brt.gpp.comum.gppExceptions.GPPTecnomenException;

  /**
   * <pre>
   *   short alterarStatusAssinante (in string msisdn, in short status,
                                in string dataExpiracao, in string operador);
   * </pre>
   */
  public short alterarStatusAssinante (java.lang.String msisdn, 
                                       short status, 
                                       java.lang.String dataExpiracao, 
                                       java.lang.String operador);

  /**
   * <pre>
   *   short alterarStatusPeriodico (in string msisdn, in short status,
                                in string dataExpiracao, in string operador);
   * </pre>
   */
  public short alterarStatusPeriodico (java.lang.String msisdn, 
                                       short status, 
                                       java.lang.String dataExpiracao, 
                                       java.lang.String operador);

  /**
   * <pre>
   *   short bloqueiaAssinante (in string MSISDN, in string idBloqueio,
                           in double tarifa, in string operador)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException,
            com.brt.gpp.comum.gppExceptions.GPPCorbaException,
            com.brt.gpp.comum.gppExceptions.GPPTecnomenException);
   * </pre>
   */
  public short bloqueiaAssinante (java.lang.String MSISDN, 
                                  java.lang.String idBloqueio, 
                                  double tarifa, 
                                  java.lang.String operador) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                                    com.brt.gpp.comum.gppExceptions.GPPCorbaException, 
                                                                    com.brt.gpp.comum.gppExceptions.GPPTecnomenException;

  /**
   * <pre>
   *   short desbloqueiaAssinante (in string MSISDN, in string operador)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException,
            com.brt.gpp.comum.gppExceptions.GPPCorbaException,
            com.brt.gpp.comum.gppExceptions.GPPTecnomenException);
   * </pre>
   */
  public short desbloqueiaAssinante (java.lang.String MSISDN, 
                                     java.lang.String operador) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                                       com.brt.gpp.comum.gppExceptions.GPPCorbaException, 
                                                                       com.brt.gpp.comum.gppExceptions.GPPTecnomenException;

  /**
   * <pre>
   *   short trocaMSISDNAssinante (in string antigoMSISDN, in string novoMSISDN,
                              in string idTarifa, in double valorTarifa,
                              in string operador)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException,
            com.brt.gpp.comum.gppExceptions.GPPCorbaException,
            com.brt.gpp.comum.gppExceptions.GPPTecnomenException);
   * </pre>
   */
  public short trocaMSISDNAssinante (java.lang.String antigoMSISDN, 
                                     java.lang.String novoMSISDN, 
                                     java.lang.String idTarifa, 
                                     double valorTarifa, 
                                     java.lang.String operador) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                                       com.brt.gpp.comum.gppExceptions.GPPCorbaException, 
                                                                       com.brt.gpp.comum.gppExceptions.GPPTecnomenException;

  /**
   * <pre>
   *   short trocaPlanoAssinante (in string MSISDN, in string novoPlano,
                             in double valorMudanca, in string operador,
                             in double valorFranquia)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException,
            com.brt.gpp.comum.gppExceptions.GPPCorbaException,
            com.brt.gpp.comum.gppExceptions.GPPTecnomenException);
   * </pre>
   */
  public short trocaPlanoAssinante (java.lang.String MSISDN, 
                                    java.lang.String novoPlano, 
                                    double valorMudanca, 
                                    java.lang.String operador, 
                                    double valorFranquia) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                                 com.brt.gpp.comum.gppExceptions.GPPCorbaException, 
                                                                 com.brt.gpp.comum.gppExceptions.GPPTecnomenException;

  /**
   * <pre>
   *   short trocaSimCardAssinante (in string MSISDN, in string novoIMSI,
                               in double valorMudanca, in string operador)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException,
            com.brt.gpp.comum.gppExceptions.GPPCorbaException,
            com.brt.gpp.comum.gppExceptions.GPPTecnomenException);
   * </pre>
   */
  public short trocaSimCardAssinante (java.lang.String MSISDN, 
                                      java.lang.String novoIMSI, 
                                      double valorMudanca, 
                                      java.lang.String operador) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                                        com.brt.gpp.comum.gppExceptions.GPPCorbaException, 
                                                                        com.brt.gpp.comum.gppExceptions.GPPTecnomenException;

  /**
   * <pre>
   *   short atualizaFriendsFamilyAssinante (in string MSISDN,
                                        in string listaMSISDN,
                                        in string operador,
                                        in string codigoServico)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short atualizaFriendsFamilyAssinante (java.lang.String MSISDN, 
                                               java.lang.String listaMSISDN, 
                                               java.lang.String operador, 
                                               java.lang.String codigoServico) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   string trocaSenha (in string GPPTrocaSenha)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException,
            com.brt.gpp.comum.gppExceptions.GPPCorbaException,
            com.brt.gpp.comum.gppExceptions.GPPTecnomenException,
            com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatException);
   * </pre>
   */
  public java.lang.String trocaSenha (java.lang.String GPPTrocaSenha) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                                             com.brt.gpp.comum.gppExceptions.GPPCorbaException, 
                                                                             com.brt.gpp.comum.gppExceptions.GPPTecnomenException, 
                                                                             com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatException;

  /**
   * <pre>
   *   short resetSenha (in string MSISDN, in string novaSenha)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException,
            com.brt.gpp.comum.gppExceptions.GPPCorbaException,
            com.brt.gpp.comum.gppExceptions.GPPTecnomenException);
   * </pre>
   */
  public short resetSenha (java.lang.String MSISDN, 
                           java.lang.String novaSenha) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                              com.brt.gpp.comum.gppExceptions.GPPCorbaException, 
                                                              com.brt.gpp.comum.gppExceptions.GPPTecnomenException;

  /**
   * <pre>
   *   string consultaAssinante (in string MSISDN)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException,
            com.brt.gpp.comum.gppExceptions.GPPCorbaException,
            com.brt.gpp.comum.gppExceptions.GPPTecnomenException);
   * </pre>
   */
  public java.lang.String consultaAssinante (java.lang.String MSISDN) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                                             com.brt.gpp.comum.gppExceptions.GPPCorbaException, 
                                                                             com.brt.gpp.comum.gppExceptions.GPPTecnomenException;

  /**
   * <pre>
   *   short ativacaoCancelamentoServico (in string MSISDN, in string idServico,
                                     in short acao)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException,
            com.brt.gpp.comum.gppExceptions.GPPCorbaException,
            com.brt.gpp.comum.gppExceptions.GPPTecnomenException);
   * </pre>
   */
  public short ativacaoCancelamentoServico (java.lang.String MSISDN, 
                                            java.lang.String idServico, 
                                            short acao) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                               com.brt.gpp.comum.gppExceptions.GPPCorbaException, 
                                                               com.brt.gpp.comum.gppExceptions.GPPTecnomenException;

  /**
   * <pre>
   *   long long bloquearServicos (in string msisdn)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public long bloquearServicos (java.lang.String msisdn) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   long long desativarHotLine (in string msisdn, in string categoria)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public long desativarHotLine (java.lang.String msisdn, 
                                java.lang.String categoria) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   long long desativarHotLineURA (in string msisdn, in string categoria)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public long desativarHotLineURA (java.lang.String msisdn, 
                                   java.lang.String categoria) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   void confirmaBloqueioDesbloqueioServicos (in string xmlAprovisionamento)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void confirmaBloqueioDesbloqueioServicos (java.lang.String xmlAprovisionamento) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   void enviarSMS (in string MSISDN, in string mensagem)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void enviarSMS (java.lang.String MSISDN, 
                         java.lang.String mensagem) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   void enviarSMSMulti (in string MSISDN, in string mensagem,
                       in string operador)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void enviarSMSMulti (java.lang.String MSISDN, 
                              java.lang.String mensagem, 
                              java.lang.String operador) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   boolean gravarMensagemSMS (in string msisdnOrigem, in string msisdnDestino,
                             in string mensagem, in long prioridade,
                             in string tipo)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean gravarMensagemSMS (java.lang.String msisdnOrigem, 
                                    java.lang.String msisdnDestino, 
                                    java.lang.String mensagem, 
                                    int prioridade, 
                                    java.lang.String tipo) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   short inserePulaPula (in string msisdn, in string promocao,
                        in string operador);
   * </pre>
   */
  public short inserePulaPula (java.lang.String msisdn, 
                               java.lang.String promocao, 
                               java.lang.String operador);

  /**
   * <pre>
   *   short retiraPulaPula (in string msisdn, in string operador);
   * </pre>
   */
  public short retiraPulaPula (java.lang.String msisdn, 
                               java.lang.String operador);

  /**
   * <pre>
   *   short trocaPulaPula (in string msisdn, in string promocaoNova,
                       in string operador);
   * </pre>
   */
  public short trocaPulaPula (java.lang.String msisdn, 
                              java.lang.String promocaoNova, 
                              java.lang.String operador);

  /**
   * <pre>
   *   short trocaPulaPulaPPP (in string msisdn, in long promocaoNova,
                          in string operador, in long motivo,
                          in long tipoDocumento, in string numDocumento);
   * </pre>
   */
  public short trocaPulaPulaPPP (java.lang.String msisdn, 
                                 int promocaoNova, 
                                 java.lang.String operador, 
                                 int motivo, 
                                 int tipoDocumento, 
                                 java.lang.String numDocumento);

  /**
   * <pre>
   *   short executaPulaPula (in string tipoExecucao, in string msisdn,
                         in string dataReferencia, in string operador,
                         in long motivo);
   * </pre>
   */
  public short executaPulaPula (java.lang.String tipoExecucao, 
                                java.lang.String msisdn, 
                                java.lang.String dataReferencia, 
                                java.lang.String operador, 
                                int motivo);

  /**
   * <pre>
   *   short zerarSaldos (in string msisdn, in string operador,
                     in string tipoTransacao, in long codSaldosZerados);
   * </pre>
   */
  public short zerarSaldos (java.lang.String msisdn, 
                            java.lang.String operador, 
                            java.lang.String tipoTransacao, 
                            int codSaldosZerados);

  /**
   * <pre>
   *   short ativaAssinanteComStatus (in string msisdn, in string imsi,
                                 in string planoPreco,
                                 in double creditoInicial, in short idioma,
                                 in string operador, in short status);
   * </pre>
   */
  public short ativaAssinanteComStatus (java.lang.String msisdn, 
                                        java.lang.String imsi, 
                                        java.lang.String planoPreco, 
                                        double creditoInicial, 
                                        short idioma, 
                                        java.lang.String operador, 
                                        short status);

  /**
   * <pre>
   *   string cobrarServico (in string msisdn, in string codigoServico,
                        in string operacao, in string operador)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public java.lang.String cobrarServico (java.lang.String msisdn, 
                                         java.lang.String codigoServico, 
                                         java.lang.String operacao, 
                                         java.lang.String operador) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   string cadastrarBumerangue (in string msisdn, in string codigoServico,
                              in string operacao, in string operador)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public java.lang.String cadastrarBumerangue (java.lang.String msisdn, 
                                               java.lang.String codigoServico, 
                                               java.lang.String operacao, 
                                               java.lang.String operador) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   string atualizarAmigosTodaHora (in string msisdn, in string listaMsisdn,
                                  in string codigoServico, in string operacao,
                                  in string operador)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public java.lang.String atualizarAmigosTodaHora (java.lang.String msisdn, 
                                                   java.lang.String listaMsisdn, 
                                                   java.lang.String codigoServico, 
                                                   java.lang.String operacao, 
                                                   java.lang.String operador) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   short reativarAssinante (in string msisdnNovo, in string msisdnAntigo,
                           in string operador);
   * </pre>
   */
  public short reativarAssinante (java.lang.String msisdnNovo, 
                                  java.lang.String msisdnAntigo, 
                                  java.lang.String operador);

  /**
   * <pre>
   *   short aprovarLote (in string lote)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short aprovarLote (java.lang.String lote) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   short rejeitarLote (in string lote)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short rejeitarLote (java.lang.String lote) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   short inserirPromocaoFaleGratisANoite (in string msisdn, in string promocao,
                                         in string operador);
   * </pre>
   */
  public short inserirPromocaoFaleGratisANoite (java.lang.String msisdn, 
                                                java.lang.String promocao, 
                                                java.lang.String operador);

  /**
   * <pre>
   *   short retirarPromocaoFaleGratisANoite (in string msisdn, in string operador);
   * </pre>
   */
  public short retirarPromocaoFaleGratisANoite (java.lang.String msisdn, 
                                                java.lang.String operador);

  /**
   * <pre>
   *   short enviarRequisicaoTangram (in string xml)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short enviarRequisicaoTangram (java.lang.String xml) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   short atualizaAmigosDeGraca (in string MSISDN, in string listaMSISDN,
                               in string operador, in string codigoServico);
   * </pre>
   */
  public short atualizaAmigosDeGraca (java.lang.String MSISDN, 
                                      java.lang.String listaMSISDN, 
                                      java.lang.String operador, 
                                      java.lang.String codigoServico);

  /**
   * <pre>
   *   short ativarAssinante (in string xml);
   * </pre>
   */
  public short ativarAssinante (java.lang.String xml);

}
