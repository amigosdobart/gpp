// Cliente de Aprovisionamento.java
package clientes;

import com.brt.gpp.componentes.aprovisionamento.orb.*;
import com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoDesativacaoAssinante;
import com.brt.gpp.comum.mapeamentos.entidade.ValorAjuste;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class ClienteAprovisionamentoGPP
{
    public ClienteAprovisionamentoGPP ( )
    {
        System.out.println ("Ativando cliente de aprovisionamento...");
    }
    
    public static void main(String[] args)
    {

        java.util.Properties props = System.getProperties();
        props.put("vbroker.agent.port", args[0]);
        props.put("vbroker.agent.addr", args[1]);
        System.setProperties ( props );
        
        // Inicializando o ORB
        org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, props);
        
        byte[] managerId = "ComponenteNegociosAprovisionamento".getBytes();
        
        aprovisionamento pPOA = aprovisionamentoHelper.bind(orb, "/AtivaComponentesPOA", managerId);

        int userOption = 0;
        boolean exit = true;
        
        while ( exit )
        {
            userOption = menuOpcoes ();
            
            switch ( userOption )
            {
                case 0:
                {
                    exit = false;
                    break;
                }       
                case 1:
                {
                    System.out.print ("Digite o codigo do Assinante: ");
                    String tmpMSISDN = read();

                    System.out.print ("Digite numero do Simcard/IMSI: ");
                    String tmpIMSI = read();
                    
                    System.out.print ("Digite o plano de preco: ");
                    String tmpPlanoPreco = read();
                    
                    System.out.print ("Digite credito inicial: ");
                    String tmp_CreditoInicial = read();
                    double tmpCreditoInicial = (new Double (tmp_CreditoInicial)).doubleValue();
                    
                    System.out.print ("Digite o idioma: ");
                    String tmp_Idioma = read();
                    short tmpIdioma = (short) (new Integer (tmp_Idioma)).intValue();
                    
                    String tmpOperador = "ClienteAprovisionamentoGPP";

                    short ret = -1;
                    
                    try
                    {
                        ret = pPOA.ativaAssinante (tmpMSISDN, tmpIMSI, tmpPlanoPreco, tmpCreditoInicial, tmpIdioma, tmpOperador );
                    }
                    catch (Exception e) 
                    {
                        System.out.println("Erro:" + e);
                    }
                    if (  ret == 0 )
                    {
                        System.out.println ("Metodo remoto ativaMSISDN executado com sucesso...");
                    }
                    else
                    {
                        System.out.println ("Metodo remoto ativaMSISDN retornou o erro: " + ret );
                    }
                    
                    break;
                }
                case 2:
                {
                    System.out.print ("Digite o codigo do Assinante a ser desativado: ");
                    String tmpMSISDN = read();

                    System.out.print ("Digite o motivo da desativacao: ");
                    String tmpMotivo = read();

                    String tmpOperador = "ClienteAprocisionamentoGPP";

                    retornoDesativacaoAssinante ret = null;
                    
                    try
                    {
                        ret = pPOA.desativaAssinante (tmpMSISDN, tmpMotivo, tmpOperador );
                    }
                    catch (Exception e) 
                    {
                        System.out.println("Erro:" + e);
                    }
                    if (  ret != null )
                    {
                        System.out.println ("Metodo remoto desativaMSISDN executado com sucesso...");
                        if ( ret.codigoRetorno == 0)
                        {
                            System.out.println ("Assinante desativado com saldo residual principal:" + ret.somaSaldos);
                        }
                        else
                        {
                            System.out.println ("Assinante nao ativo na plataforma tecnomen.");
                        }
                    }
                    else
                    {
                        System.out.println ("Metodo remoto desativaMSISDN retornou o erro: " + ret );
                    }
                    
                    break;
                }
                case 3:
                {
                    System.out.print ("Digite o codigo do Assinante a ser bloqueado / suspenso: ");
                    String tmpMSISDN = read();

                    System.out.print ("Digite o motivo (id) do bloqueio (1-6): ");
                    String tmpMotivo = read();

                    System.out.print ("Digite a tarifa a ser cobrada: ");
                    String tmpTMPTarifa = read();
                    double tmpTarifa = (new Double (tmpTMPTarifa)).doubleValue();
                    
                    String tmpOperador = "ClienteAprocisionamentoGPP";

                    short ret = 0;
                    
                    try
                    {
                        ret = pPOA.bloqueiaAssinante (tmpMSISDN, tmpMotivo, tmpTarifa, tmpOperador );
                    }
                    catch (Exception e) 
                    {
                        System.out.println("Erro:" + e);
                    }
                    if (  ret == 0 )
                    {
                        System.out.println ("Metodo remoto bloqueiaAssinante executado com sucesso...");
                    }
                    else
                    {
                        System.out.println ("Metodo remoto bloqueiaAssinante retornou o erro: " + ret );
                    }
                    
                    break;
                }
                case 4:
                {
                    System.out.print ("Digite o codigo do Assinante a ser desbloqueado: ");
                    String tmpMSISDN = read();

                    String tmpOperador = "ClienteAprocisionamentoGPP";

                    short ret = 0;
                    
                    try
                    {
                        ret = pPOA.desbloqueiaAssinante (tmpMSISDN, tmpOperador );
                    }
                    catch (Exception e) 
                    {
                        System.out.println("Erro:" + e);
                    }
                    if (  ret == 0 )
                    {
                        System.out.println ("Metodo remoto desbloqueiaAssinante executado com sucesso...");
                    }
                    else
                    {
                        System.out.println ("Metodo remoto desbloqueiaAssinante retornou o erro: " + ret );
                    }
                    
                    break;
                }
                case 5:
                {
                    System.out.print ("Digite o codigo antigo do Assinante: ");
                    String tmpAntigoMSISDN = read();

                    System.out.print ("Digite o codigo novo do Assinante: ");
                    String tmpNovoMSISDN = read();

                    System.out.print ("Digite o ID da troca (0-3): ");
                    String tmpID = read();

                    System.out.print ("Digite a tarifa a ser cobrada: ");
                    String tmpTMPTarifa = read();
                    double tmpTarifa = (new Double (tmpTMPTarifa)).doubleValue();
                    
                    String tmpOperador = "ClienteAprocisionamentoGPP";

                    short ret = 0;
                    
                    try
                    {
                        ret = pPOA.trocaMSISDNAssinante (tmpAntigoMSISDN, tmpNovoMSISDN, tmpID, tmpTarifa, tmpOperador );
                    }
                    catch (Exception e) 
                    {
                        System.out.println("Erro:" + e);
                    }
                    if (  ret == 0 )
                    {
                        System.out.println ("Metodo remoto trocaMSISDNAssinante executado com sucesso...");
                    }
                    else
                    {
                        System.out.println ("Metodo remoto trocaMSISDNAssinante retornou o erro: " + ret );
                    }
                    
                    break;
                }
                case 6:
                {
                    System.out.print ("Digite o codigo do Assinante: ");
                    String tmpMSISDN = read();

                    System.out.print ("Digite o codigo novo do plano: ");
                    String tmpNovoPlano = read();

                    System.out.print ("Digite a tarifa a ser cobrada: ");
                    String tmpTMPTarifa = read();
                    double tmpTarifa = (new Double (tmpTMPTarifa)).doubleValue();
                    
                    System.out.print ("Digite a franquia a ser cobrada: ");
                    String tmpTMPFranquia = read();
                    double tmpFranquia = (new Double (tmpTMPFranquia)).doubleValue();
                    
                    String tmpOperador = "ClienteAprocisionamentoGPP";

                    short ret = 0;
                    
                    try
                    {
                        ret = pPOA.trocaPlanoAssinante (tmpMSISDN, tmpNovoPlano, tmpTarifa, tmpOperador, tmpFranquia );
                    }
                    catch (Exception e) 
                    {
                        System.out.println("Erro:" + e);
                    }
                    if (  ret == 0 )
                    {
                        System.out.println ("Metodo remoto trocaPlanoAssinante executado com sucesso...");
                    }
                    else
                    {
                        System.out.println ("Metodo remoto trocaPlanoAssinantes retornou o erro: " + ret );
                    }
                    
                    break;
                }
                case 7:
                {
                    System.out.print ("Digite o codigo do Assinante: ");
                    String tmpMSISDN = read();

                    System.out.print ("Digite o codigo novo SimCard: ");
                    String tmpNovoSimCard = read();

                    System.out.print ("Digite a tarifa a ser cobrada: ");
                    String tmpTMPTarifa = read();
                    double tmpTarifa = (new Double (tmpTMPTarifa)).doubleValue();
                    
                    String tmpOperador = "ClienteAprocisionamentoGPP";

                    short ret = 0;
                    
                    try
                    {
                        ret = pPOA.trocaSimCardAssinante (tmpMSISDN, tmpNovoSimCard, tmpTarifa, tmpOperador );
                    }
                    catch (Exception e) 
                    {
                        System.out.println("Erro:" + e);
                    }
                    if (  ret == 0 )
                    {
                        System.out.println ("Metodo remoto trocaSimCardAssinante executado com sucesso...");
                    }
                    else
                    {
                        System.out.println ("Metodo remoto trocaSimCardAssinante retornou o erro: " + ret );
                    }
                    
                    break;
                }
                case 8:
                {
                    System.out.print ("Digite o codigo do Assinante: ");
                    String tmpMSISDN = read();

                    System.out.print ("Digite a lista de FF (separados por ';') : ");
                    String tmpListaFF = read();

                    String tmpOperador = "ClienteAprocisionamentoGPP";
                    
                    System.out.print ("Codigo de Servico : ");
                    String codigoServico = read();
                    
                    short ret = 0;
                    
                    try
                    {
                        ret = pPOA.atualizaFriendsFamilyAssinante(tmpMSISDN, tmpListaFF, tmpOperador,codigoServico);
                    }
                    catch (Exception e) 
                    {
                        System.out.println("Erro:" + e);
                    }
                    if (  ret == 0 )
                    {
                        System.out.println ("Metodo remoto atualizaFriendsFamilyAssinante executado com sucesso...");
                    }
                    else
                    {
                        System.out.println ("Metodo remoto atualizaFriendsFamilyAssinante retornou o erro: " + ret );
                    }
                    
                    break;
                }
                case 9:
                {
                    System.out.print ("Digite o codigo do Assinante: ");
                    String tmpMSISDN = read();

                    System.out.print ("Digite a nova senha (codificada): ");
                    String tmpNovaSenha = read();

                    String aXML = "<?xml version=\"1.0\"?>";
                    aXML = aXML + "<GPPTrocaSenha>";
                    aXML = aXML + "<msisdn>" + tmpMSISDN + "</msisdn>";
                    aXML = aXML + "<novaSenha>" + tmpNovaSenha + "</novaSenha>";
                    aXML = aXML + "</GPPTrocaSenha>";   

                    String ret = null;

                    try
                    {
                        ret = pPOA.trocaSenha(aXML);
                    }
                    catch (Exception e) 
                    {
                        System.out.println("Erro:" + e);
                    }
                    if (  ret != null )
                    {
                        System.out.println ("Metodo remoto trocaSenha executado com sucesso...");
                        System.out.println (ret);
                    }
                    else
                    {
                        System.out.println ("Metodo remoto trocaSenha retornou o erro: " + ret );
                    }
                    
                    break;
                }
                case 10:
                {
                    System.out.print ("Digite o codigo do Assinante: ");
                    String tmpMSISDN = read();

                    String ret = null;
                    
                    try
                    {
                        ret = pPOA.consultaAssinante (tmpMSISDN );
                    }
                    catch (Exception e) 
                    {
                        System.out.println("Erro:" + e);
                    }
                    if (  ret != null )
                    {
                        System.out.println ("Metodo remoto consultaMSISDN executado com sucesso...");
                        System.out.println (ret.toString());
                    }
                    else
                    {
                        System.out.println ("Metodo remoto consultaMSISDN retornou o erro: " + ret );
                    }
                    
                    break;
                }
                case 11:
                {
                    int retorno = menuOpcoesNotificacaoServicos ();
                    String aMSISDN = null;
                    String aServico = null;
                    short aAcao = 0;
                    int ret = 0;
                    
                    System.out.print ("Digite o numero do assinante: ");
                    aMSISDN = read();

                    System.out.print ("Digite o codigo do servico: ");
                    aServico = read();

                    if (retorno == 1)
                        aAcao = 0;
                    else
                        aAcao = 1;
                        
                    try
                    {
                        ret = pPOA.ativacaoCancelamentoServico(aMSISDN, aServico, aAcao);
                        
                        if (  ret == 0 )
                        {
                            System.out.println ("Metodo remoto ativacaoCancelamentoServico executado com sucesso...");
                        }
                        else
                        {
                            System.out.println ("Metodo remoto ativacaoCancelamentoServico retornou o erro: " + ret );
                        }
                    }
                    catch (Exception e) 
                    {
                        System.out.println("Erro:" + e);
                    }
                    
                    break;                  
                }
                case 12:
                {
                    System.out.print ("Digite o numero do assinante: ");
                    String aMSISDN = read();
                    try
                    {
                        pPOA.bloquearServicos(aMSISDN);
                    }
                    catch(Exception e)
                    {
                        System.out.println("Erro ao bloquear servicos do assinante. Erro:"+e);
                    }
                    break;
                }
                case 13:
                {
                    System.out.print ("Digite o numero do assinante: ");
                    String aMSISDN = read();
                    System.out.print ("Digite a categoria do assinante (F1-PosPago, F2-PrePago, F3-Controle): ");
                    String aCategoria = read();
                    try
                    {
                        pPOA.desativarHotLine(aMSISDN,aCategoria);
                    }
                    catch(Exception e)
                    {
                        System.out.println("Erro ao desativar hotline do assinante. Erro:"+e);
                    }
                    break;
                }
                case 14:
                {
                    System.out.print ("Digite o codigo do Assinante: ");
                    String tmpMSISDN = read();

                    System.out.print ("Digite a nova senha: ");
                    String tmpNovaSenha = read();

                    short ret = 0;

                    try
                    {
                        ret = pPOA.resetSenha(tmpMSISDN, tmpNovaSenha );
                    }
                    catch (Exception e) 
                    {
                        System.out.println("Erro:" + e);
                    }
                    if (  ret == 0 )
                    {
                        System.out.println ("Metodo remoto resetSenha executado com sucesso...");
                    }
                    else
                    {
                        System.out.println ("Metodo remoto resetSenha retornou o erro: " + ret );
                    }
                    
                    break;
                }
                case 15:
                {
                    System.out.print ("Digite o codigo do Assinante: ");
                    String msisdn = read();
                    
                    System.out.print ("Digite o identificador da promocao do Assinante: ");
                    String promocao = read();

                    
                    short result = -1;
                    
                    try
                    {
                        result = pPOA.inserePulaPula(msisdn, promocao, "ClienteAprovisionamentoGPP");
                        if((result == 0) || (result == 69)) //Codigo de retorno 69: Pendente de primeira recarga
                        {
                            System.out.println(result);
                            System.out.println("\nMetodo remoto inserePulaPula executado com sucesso...");
                        }
                        else
                        {
                            System.out.println("\nMetodo remoto inserePulaPula retornou o erro: " + result);
                        }
                    }
                    catch(Exception e)
                    {
                        System.out.println("Excecao: " + e);
                    }
                    
                    break;
                }
                case 16:
                {
                    System.out.print ("Digite o codigo do Assinante: ");
                    String msisdn = read();
                    
                    
                    short result = -1;
                    
                    try
                    {
                        result = pPOA.retiraPulaPula(msisdn, "ClienteAprovisionamentoGPP");
                        if(result == 0)
                        {
                            System.out.println(result);
                            System.out.println("\nMetodo remoto retiraPulaPula executado com sucesso...");
                        }
                        else
                        {
                            System.out.println("\nMetodo remoto retiraPulaPula retornou o erro: " + result);
                        }
                    }
                    catch(Exception e)
                    {
                        System.out.println("Excecao: " + e);
                    }
                    
                    break;
                }
                case 17:
                {
                    System.out.print ("Digite o codigo do Assinante: ");
                    String msisdn = read();
                    
                    System.out.print ("Digite o identificador da nova promocao do Assinante: ");
                    String promocaoNova = read();

                    
                    short result = -1;
                    
                    try
                    {
                        result = pPOA.trocaPulaPula(msisdn, promocaoNova, "ClienteAprovisionamentoGPP");
                        if(result == 0)
                        {
                            System.out.println(result);
                            System.out.println("\nMetodo remoto trocaPulaPula executado com sucesso...");
                        }
                        else
                        {
                            System.out.println("\nMetodo remoto trocaPulaPula retornou o erro: " + result);
                        }
                    }
                    catch(Exception e)
                    {
                        System.out.println("Excecao: " + e);
                    }
                    
                    break;
                }
                case 18:
                {
                    System.out.print ("Digite o tipo de execucao da concessao: ");
                    String tipoExecucao = read();
                    
                    System.out.print ("Digite o codigo do Assinante: ");
                    String msisdn = read();
                    
                    System.out.print ("Digite a data de referencia (dd/mm/yyyy): ");
                    String dataReferencia = read();
                    
                    System.out.print ("Digite o motivo da operacao: ");
                    int motivo = Integer.parseInt(read());
                    
                    short result = -1;
                    
                    try
                    {
                        result = pPOA.executaPulaPula(tipoExecucao, msisdn, dataReferencia, "ClienteAprovisionamentoGPP", motivo);
                        if(result == 0)
                        {
                            System.out.println(result);
                            System.out.println("\nMetodo remoto executaPulaPula executado com sucesso...");
                        }
                        else
                        {
                            System.out.println("\nMetodo remoto executaPulaPula retornou o erro: " + result);
                        }
                    }
                    catch(Exception e)
                    {
                        System.out.println("Excecao: " + e);
                    }
                    
                    break;
                }
                case 19:
                {
                    System.out.print ("Digite o codigo do Assinante: ");
                    String tmpMSISDN = read();

                    System.out.print ("Digite numero do Simcard/IMSI: ");
                    String tmpIMSI = read();
                    
                    System.out.print ("Digite o plano de preco: ");
                    String tmpPlanoPreco = read();
                    
                    System.out.print ("Digite credito inicial: ");
                    String tmp_CreditoInicial = read();
                    double tmpCreditoInicial = (new Double(tmp_CreditoInicial)).doubleValue();
                    
                    System.out.print ("Digite o idioma: ");
                    String tmp_Idioma = read();
                    short tmpIdioma = (new Short(tmp_Idioma)).shortValue();
                    
                    System.out.print ("Digite o status: ");
                    String tmp_Status = read();
                    short tmpStatus = (new Short(tmp_Status)).shortValue();
                    
                    String tmpOperador = "ClienteAprovisionamentoGPP";

                    short ret = -1;
                    
                    try
                    {
                        ret = pPOA.ativaAssinanteComStatus(tmpMSISDN, tmpIMSI, tmpPlanoPreco, tmpCreditoInicial, tmpIdioma, tmpOperador, tmpStatus);
                    }
                    catch (Exception e) 
                    {
                        System.out.println("Erro:" + e);
                    }
                    if(ret == 0)
                        System.out.println ("Metodo remoto ativaAssinanteComStatus executado com sucesso...");
                    else
                        System.out.println ("Metodo remoto ativaAssinanteComStatus retornou o erro: " + ret );
                    break;
                }
                case 20:
                {
                    System.out.print ("Digite o codigo do Assinante: ");
                    String tmpMSISDN = read();
                    
                    System.out.print ("Digite o tipo de transacao: ");
                    String tmpTipoTrans = read();
                    
                    System.out.print ("Digite o codigo zerar saldos (1,2,3,4, 8 ou 9): ");
                    String tmpCodZera = read();
                    
                    String tmpOperador = "ClienteAprovisionamentoGPP";
                    short ret = -1;         
                    try
                    {
                        ret = pPOA.zerarSaldos(tmpMSISDN, tmpOperador, tmpTipoTrans, Integer.parseInt(tmpCodZera));
                    }
                    catch (Exception e) 
                    {
                        System.out.println("Erro:" + e);
                    }
                    if(ret == 0)
                        System.out.println ("Metodo remoto zerar saldos executado com sucesso...");
                    else
                        System.out.println ("Metodo remoto zerar saldos retornou o erro: " + ret );
                    break;
                }
                case 21:
                {
                    System.out.print ("Digite o codigo do Assinante: ");
                    String tmpMSISDN = read();

                    System.out.print ("Digite numero do Simcard/IMSI: ");
                    String tmpIMSI = read();
                    
                    System.out.print ("Digite o plano de preco: ");
                    String tmpPlanoPreco = read();
                    
                    System.out.print ("Digite credito inicial: ");
                    String tmp_CreditoInicial = read();
                    double tmpCreditoInicial = (new Double (tmp_CreditoInicial)).doubleValue();
                    
                    System.out.print ("Digite o idioma: ");
                    String tmp_Idioma = read();
                    short tmpIdioma = (short) (new Integer (tmp_Idioma)).intValue();
                    
                    String tmpOperador = "ClienteAprovisionamentoGPP";
                    short tmpStatus=4;

                    short ret = -1;
                    
                    try
                    {
                        ret = pPOA.ativaAssinanteComStatus(tmpMSISDN, tmpIMSI, tmpPlanoPreco, tmpCreditoInicial, tmpIdioma, tmpOperador,tmpStatus );
                    }
                    catch (Exception e) 
                    {
                        System.out.println("Erro:" + e);
                    }
                    if (  ret == 0 )
                    {
                        System.out.println ("Metodo remoto ativa assinante com status executado com sucesso...");
                    }
                    else
                    {
                        System.out.println ("Metodo remoto ativa assinante com status retornou o erro: " + ret );
                    }
                    
                    break;
                }
                case 22:
                {
                    System.out.print ("Digite o codigo do Assinante: ");
                    String msisdn = read();

                    System.out.print ("Digite codigo do servico: ");
                    String codigoServico = read();
                    
                    System.out.print ("Digite a operacao (C: Consulta, D: Debito, E: Estorno): ");
                    String operacao = read();
                    
                    String operador = "ClienteAprovisionamentoGPP";
                    
                    String xml = null;
                    
                    try
                    {
                        xml = pPOA.cobrarServico(msisdn, codigoServico, operacao, operador);
                    }
                    catch (Exception e) 
                    {
                        System.out.println("Erro:" + e);
                    }
                    
                    System.out.println (xml);
                                        
                    break;
                }
                case 23:
                {
                    System.out.print ("Digite o codigo do Assinante: ");
                    String msisdn = read();

                    System.out.print ("Digite codigo do servico: ");
                    String codigoServico = read();
                    
                    System.out.print ("Digite a operacao (C: Consulta, D: Debito, E: Estorno): ");
                    String operacao = read();
                    
                    String operador = "ClienteAprovisionamentoGPP";
                    
                    String xml = null;
                    
                    try
                    {
                        xml = pPOA.cadastrarBumerangue(msisdn, codigoServico, operacao, operador);
                    }
                    catch (Exception e) 
                    {
                        System.out.println("Erro:" + e);
                    }
                    
                    System.out.println (xml);
                                        
                    break;
                }
                case 24:
                {
                    System.out.print ("Digite o codigo do Assinante: ");
                    String msisdn = read();
                    
                    System.out.print ("Digite a lista de FF (separados por ';') : ");
                    String listaFF = read();

                    System.out.print ("Digite codigo do servico: ");
                    String codigoServico = read();
                    
                    System.out.print ("Digite a operacao (C: Consulta, D: Debito, E: Estorno): ");
                    String operacao = read();
                    
                    String operador = "ClienteAprovisionamentoGPP";
                    
                    String xml = null;
                    
                    try
                    {
                        xml = pPOA.atualizarAmigosTodaHora(msisdn, listaFF, codigoServico, operacao, operador);
                    }
                    catch (Exception e) 
                    {
                        System.out.println("Erro:" + e);
                    }
                    
                    System.out.println (xml);
                                        
                    break;
                }
                case 25:
                {
                    System.out.print ("Digite o novo codigo do Assinante: ");
                    String msisdnNovo = read();
                    
                    System.out.print ("Digite o antigo codigo do Assinante: ");
                    String msisdnAntigo = read();                   
                    
                    String operador = "ClienteAprovisionamentoGPP";
                    
                    try
                    {
                        pPOA.reativarAssinante(msisdnNovo, msisdnAntigo, operador);
                    }
                    catch (Exception e) 
                    {
                        System.out.println("Erro:" + e);
                    }
                    
                    break;
                }
                case 26:
                {
                    System.out.print ("Digite o codigo do Assinante a ser desativado: ");
                    String tmpMSISDN = read();

                    System.out.print ("Digite o motivo da desativacao: ");
                    String tmpMotivo = read();

                    String tmpOperador = "ClienteAprovisionamentoGPP";

                    try
                    {
                        String xmlRetorno = pPOA.desativarAssinanteXML( tmpMSISDN, tmpMotivo, tmpOperador );
                        System.out.println(xmlRetorno);
                    }
                    catch (Exception e) 
                    {
                        System.out.println("Erro ao remover assiannte "+tmpMSISDN);
                        System.out.println("Erro:" + e);
                    }

                    break;
                }
                case 27:
                {
                    System.out.print("Digite o MSISDN do Assinante: ");
                    String msisdn = read();

                    System.out.print("Digite o novo status: ");
                    short status = Short.parseShort(read());

                    System.out.print("Digite a nova data de expiracao dos saldos (dd/mm/yyyy): ");
                    String dataExpiracao = read();

                    String operador = "ClienteAprovisionamentoGPP";

                    short retorno = pPOA.alterarStatusAssinante(msisdn, status, dataExpiracao, operador);
                    
                    if(retorno == 0)
                        System.out.println("Metodo remoto alterarStatusAssinante executado com sucesso.");
                    else
                        System.out.println("Metodo remoto alterarStatusAssinante executado com erro: " + retorno);
                    
                    break;
                }
                case 28:
                {
                    System.out.print("Digite o MSISDN do Assinante: ");
                    String msisdn = read();

                    System.out.print("Digite o novo status periodico: ");
                    short status = Short.parseShort(read());

                    System.out.print("Digite a nova data de expiracao do Saldo Periodico (dd/mm/yyyy): ");
                    String dataExpiracao = read();

                    String operador = "ClienteAprovisionamentoGPP";

                    short retorno = pPOA.alterarStatusPeriodico(msisdn, status, dataExpiracao, operador);
                    
                    if(retorno == 0)
                        System.out.println("Metodo remoto alterarStatusPeriodico executado com sucesso.");
                    else
                        System.out.println("Metodo remoto alterarStatusPeriodico executado com erro: " + retorno);
                    
                    break;
                }
                case 29:
                {
                    short result = -1;
                    
                    System.out.print ("Digite o codigo do Assinante: ");
                    String msisdn = read();
                    
                    System.out.print ("Digite o plano FGN do Assinante: ");
                    String plano = read();
                    
                    try
                    {
                        result = pPOA.inserirPromocaoFaleGratisANoite(msisdn, plano, "ClienteAprovisionamentoGPP");
                        if(result == 0)
                        {
                            System.out.println(result);
                            System.out.println("\nMetodo remoto inserirPromocaoFaleGratisANoite executado com sucesso...");
                        }
                        else
                        {
                            System.out.println("\nMetodo remoto inserirPromocaoFaleGratisANoite retornou o erro: " + result);
                        }
                    }
                    catch(Exception e)
                    {
                        System.out.println("Excecao: " + e);
                    }
                    
                    break;
                }
                case 30:
                {
                    short result = -1;
                    
                    System.out.print ("Digite o codigo do Assinante: ");
                    String msisdn = read();
                    
                    try
                    {
                        result = pPOA.retirarPromocaoFaleGratisANoite(msisdn, "ClienteAprovisionamentoGPP");
                        if(result == 0)
                        {
                            System.out.println(result);
                            System.out.println("\nMetodo remoto retirarPromocaoFaleGratisANoite executado com sucesso...");
                        }
                        else
                        {
                            System.out.println("\nMetodo remoto retirarPromocaoFaleGratisANoite retornou o erro: " + result);
                        }
                    }
                    catch(Exception e)
                    {
                        System.out.println("Excecao: " + e);
                    }
                    
                    break;
                }
                case 31:
                {
                    short result = -1;
                    
                    System.out.print ("Digite o codigo do Assinante: ");
                    String msisdn = read();
                    
                    System.out.print ("Digite a lista de MSISDN dos amigos separados por ';': ");
                    String listaAmigos = read();
                    
                    System.out.print ("Digite o nome do operador: ");
                    String operador = read();
                    
                    System.out.print ("Digite o codigo do servico: ");
                    String codigoServico = read();
                                        
                    try
                    {
                        result = pPOA.atualizaAmigosDeGraca(msisdn, listaAmigos, operador, codigoServico);
                        if(result == 0)
                        {
                            System.out.println("\nMetodo remoto atualizaAmigosDeGraca executado com sucesso para o assinante "+msisdn+". Codigo de Retorno: "+result);
                        }
                        else
                        {
                            System.out.println("\nMetodo remoto atualizar amigos gratis retornou o codigo de erro: " + result);
                        }
                    }
                    catch(Exception e)
                    {
                        System.out.println("Excecao: " + e);
                    }
                    
                    break;
                }
                case 32:
                {
                    System.out.print ("Digite o codigo do Assinante: ");
                    String msisdn = read();

                    System.out.print ("Digite numero do Simcard/IMSI: ");
                    String imsi = read();
                    
                    System.out.print ("Digite o plano de preco: ");
                    String planoPreco = read();
                    
                    System.out.print ("Digite o idioma: ");
                    String idioma = read();

                    System.out.print ("Digite o status: ");
                    String status = read();

                    System.out.println ("Digite os valores para cada saldo identificado. Qualquer texto para sair: ");
                    int i = 0;
                    Double valor;
                    ArrayList creditoInicial = new ArrayList();
                    while(true)
                    {
                        System.out.print ("Digite o valor do credito inicial no saldo ["+i+"]: ");
                        try
                        {
                            valor = new Double(read());
                            if((i == 0 && valor.doubleValue()==0))//POG da Tecnomen
                                creditoInicial.add(new Double(0.001));//POG da Tecnomen
                            else
                            	creditoInicial.add(i,valor);
                            i++;
                        }
                        catch (Exception e)
                        {
                        	break;
                        }
                    }
                                        
            		String xml = ""+
            	    "<mensagem>                                                                \n" +
            	    "    <cabecalho>                                                           \n" +
            	    "        <empresa>BRG</empresa>                                            \n" +
            	    "        <sistema>GPP</sistema>                                            \n" +
            	    "        <processo>GPPAtivacaoAssinante</processo>                         \n" +
            	    "    </cabecalho>                                                          \n" +
            	    "    <conteudo>                                                            \n" +
            	    "        <![CDATA[                                                         \n" +
            	    "             <root>                                                       \n" +
            	    "              <GPPAtivacaoAssinante>                                      \n"+
            	    "                <assinante>                                               \n" +
            	    "                    <msisdn>"+msisdn+"</msisdn>                           \n" +
            	    "                    <imsi>"+imsi+"</imsi>                                 \n" +
            	    "                    <planoPreco>"+planoPreco+"</planoPreco>               \n" +
            	    "                    <idioma>"+idioma+"</idioma>                           \n" +
            	    "                    <status>"+status+"</status>                           \n" +
            	    "                    <creditoInicial>                                      \n" +
            	    "";
            		i = 0;
        			for(Iterator iterator = creditoInicial.iterator(); iterator.hasNext();)
        			{
        				Double saldo = (Double)iterator.next();
                	    xml+="                        <saldo id=\""+i+"\">"+saldo+"</saldo>    \n" ;
                	    i++;
        			}
        			xml+=
            	    "                    </creditoInicial>                                     \n" +
            	    "                </assinante>                                              \n" +
            	    "                <operador>ClienteAprovisionamentoGPP</operador>           \n" +
            	    "              </GPPAtivacaoAssinante>                                     \n"+
            	    "            </root>                                                       \n" +
            	    "        ]]>                                                               \n" +
            	    "    </conteudo>                                                           \n" +
            	    "</mensagem>                                                               " ;

            		
            		short ret = -1;
                    
                    ret = pPOA.ativarAssinante(xml);
                    if (  ret == 0 )
                        System.out.println ("Metodo remoto ativaMSISDN executado com sucesso...");
                    else
                        System.out.println ("Metodo remoto ativaMSISDN retornou o erro: " + ret );                    
                    break;
                }

                default:
                    break;
            }
        }
    }
    public static int menuOpcoes ( )
    {
        int userOption;
        System.out.println ("\n\n");
        System.out.println ("+----------------------------------------+");
        System.out.println ("+  Sistema de Teste de Aprovisionamento  +");
        System.out.println ("+----------------------------------------+\n");
        System.out.println ("01 - Ativa Assinante");
        System.out.println ("02 - Desativa / Remove Assinante");
        System.out.println ("03 - Bloqueia Assinante");
        System.out.println ("04 - Desbloqueia Assinante");
        System.out.println ("05 - Troca MSISDN");
        System.out.println ("06 - Troca Plano de Precos");
        System.out.println ("07 - Troca de SimCard");
        System.out.println ("08 - Atualiza Friends and Family");
        System.out.println ("09 - Troca de Senha");
        System.out.println ("10 - Consulta Assiannte");
        System.out.println ("11 - Notificacao de Servico");
        System.out.println ("12 - Bloquear Servicos do Assinante");
        System.out.println ("13 - Desativar HotLine para Assinante");
        System.out.println ("14 - Reseta Senha do Assinante");
        System.out.println ("15 - Insere Assinante em Promocao Pula-Pula");
        System.out.println ("16 - Retira Assinante de Promocao Pula-Pula");
        System.out.println ("17 - Troca Promocao Pula-Pula do Assinante");
        System.out.println ("18 - Executa a Promocao Pula-Pula para o Assinante");
        System.out.println ("19 - Ativa Assinante com Status Definido"); 
        System.out.println ("20 - Zerar Saldos");
        System.out.println ("21 - Ativa Assinante com Status");
        System.out.println ("22 - Cobrar Servico");
        System.out.println ("23 - Cadastrar Bumerangue 14");
        System.out.println ("24 - Atualizar Amigos Toda Hora");
        System.out.println ("25 - Reativar Assinante");
        System.out.println ("26 - Desativar / Remover Assinante com retorno XML");
        System.out.println ("27 - Atualizar status do Assinante");
        System.out.println ("28 - Atualizar status periodico do Assinante");
        System.out.println ("29 - Insere Assinante em Promocao Fale Gratis a Noite");
        System.out.println ("30 - Retira Assinante de Promocao Fale Gratis a Noite");
        System.out.println ("31 - Atualiza Amigos Gratis");
        System.out.println ("32 - Ativar assinantes multiplos saldos");
        System.out.println ("0 - Saida");
        System.out.print ("Opcao:");
        userOption = Integer.parseInt ( read() );
        
        return userOption;
    }
    public static int menuOpcoesNotificacaoServicos ( )
    {
        int userOption;
        System.out.println ("\n\n");
        System.out.println ("+----------------------------------------+");
        System.out.println ("+  Notificacao de Servicos               +");
        System.out.println ("+----------------------------------------+\n");
        System.out.println ("01 - Ativacao de servico");
        System.out.println ("02 - Cancelamento de servico");
        System.out.println (" 0 - Saida");
        System.out.print ("Opcao:");
        userOption = Integer.parseInt ( read() );
        
        return userOption;
    }
    
    public static String read ( )
    {
        String msg = "";
        DataInput di = new DataInputStream(System.in);
        
        try 
        {
          msg = di.readLine();
        } 
        catch (Exception e) 
        {
          System.out.println("Erro lendo linha: " + e.getMessage());
        }
        return msg;
    }
}
