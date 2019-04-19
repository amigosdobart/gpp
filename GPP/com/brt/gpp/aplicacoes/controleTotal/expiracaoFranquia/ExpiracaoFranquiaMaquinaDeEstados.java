package com.brt.gpp.aplicacoes.controleTotal.expiracaoFranquia;

import java.util.Calendar;
import java.util.Date;

import com.brt.gpp.aplicacoes.aprovisionar.Aprovisionar;
import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.aplicacoes.consultar.ConsultaAssinante;
import com.brt.gpp.aplicacoes.recarregar.Ajustar;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.gppExceptions.GPPTecnomenException;
import com.brt.gpp.gerentesPool.GerentePoolLog;

public class ExpiracaoFranquiaMaquinaDeEstados
{
    private GerentePoolLog log = null; // Gerente de LOG para registrar as ocorrencias
    private long idProcesso;

    public  static final int AINDA_NAO_PENDENTE                 = 0;
    private static final int ESTADO_PENDENTE                    = 1;
    private static final int ESTADO_BLOQUEADO                   = 2;
    private static final int ESTADO_FRANQUIA_NAO_RETIRADA       = 3;
    private static final int ESTADO_FRANQUIA_AINDA_NAO_RETIRADA = 4;
    private static final int ESTADO_DESBLOQUEADO                = 5;
    private static final int ESTADO_AINDA_DESBLOQUEADO          = 6;
    private static final int ESTADO_FRANQUIA_RETIRADA           = 7;
    private static final int ESTADO_PROCESSO_FINALIZADO         = 8;

    private static final int EXPIRACAO_PARALIZADA_COM_FALHA     = 9;
    private static final int EXPIRACAO_FINALIZADA_COM_SUCESSO   = 10;

    private static final int NUMERO_MAXIMO_DE_TENTATIVAS        = 5;
    
    private ExpiracaoFranquiaVO vo;     // VO a ser processado
    private ExpiracaoFranquiaDAO dao;   // DAP para realizar acesso ao banco
    private Aprovisionar aprovisionar;  // Classe para alterar o Status de Servico do assinante
    private Ajustar ajustar;            // Para alteracao do saldo do assinante
    private boolean entradaEmFalha;

    /**
     * Construtor da maquina de estados. A Conexao eh para o DAO
     * @param conexao
     * @param idProcesso
     */
    public ExpiracaoFranquiaMaquinaDeEstados(PREPConexao conexao, long idProcesso)
    {
        this.idProcesso = idProcesso;
        this.dao = new ExpiracaoFranquiaDAO(conexao, idProcesso);
        this.aprovisionar = new Aprovisionar(idProcesso);
        this.ajustar = new Ajustar(idProcesso);

        this.log = GerentePoolLog.getInstancia(this.getClass());
    }
     
    /**
     * Executa a maquina de estados para um dado VO
     * @param vo
     */
    public void run(ExpiracaoFranquiaVO vo)
    {
        this.vo = vo;
        this.entradaEmFalha = false;
        
        // Rodando a maquina de estados ateh o fim.
        int estado = vo.getIdtStatusExpiracao();
        while(estado != EXPIRACAO_FINALIZADA_COM_SUCESSO &&
                estado != EXPIRACAO_PARALIZADA_COM_FALHA )
        {
            estado = this.maquinaDeEstado(estado);
        }
    }
    /**
     * Cerne da maquina de estados
     * @param estado Estado atual
     * @return       Novo estado para o qual a maquina irah
     */
    private int maquinaDeEstado(int estado)
    {
        // Condicao de pausa:
        if(isEstadoDeFalha(estado))
        {
            this.vo.setIdtStatusExpiracao(estado); //Atualiza o estado do assinante.
            // Por economia de acessos ao banco, a operacao de atualizacao de Numero de tentativas tambem atualiza o status
            // Logo a ordem da atualizacao de status e atualizacao de tentativas tem que ser respeitado
            // Atualizar o numero de tentativas na tabela
            int numeroDeTentativas = atualizarNumeroDeTentativas();
            // Conferir se atingiu numero maximo de tentativas
            if(numeroDeTentativas > NUMERO_MAXIMO_DE_TENTATIVAS)
            {
                return EXPIRACAO_PARALIZADA_COM_FALHA;
            }
            this.entradaEmFalha = true;
        }
        // Limpar o registro de falhas no caso de entrar em um estado de sucesso
        // e o estado anterior foi de falha
        if(isEstadoDeSucesso(estado) && this.entradaEmFalha)
        {
            this.vo.setIdtStatusExpiracao(estado); //Atualiza o estado do assinante.
            // Por economia de acessos ao banco, a operacao de reiniciar o de Numero de tentativas tambem atualiza o status
            // Logo a ordem da atualizacao de status e reinicio do numero de tentativas tem que ser respeitado
            this.removerRegistroDeFalha();
            this.entradaEmFalha = false;
        }

        int novoEstado = 0;
        switch (estado)
        {
            case AINDA_NAO_PENDENTE:
                novoEstado = ESTADO_PENDENTE; //Somente coloca o assinante na maquina de estados
            break;
            case ESTADO_AINDA_DESBLOQUEADO:
                novoEstado = this.estadoAindaDesbloqueado();
                break;
            case ESTADO_BLOQUEADO:
                novoEstado = this.estadoBloqueado();
                break;
            case ESTADO_DESBLOQUEADO:
                novoEstado = this.estadoDesbloqueado();
                break;
            case ESTADO_FRANQUIA_AINDA_NAO_RETIRADA:
                novoEstado = this.estadoFranquiaAindaNaoRetirada();
                break;
            case ESTADO_FRANQUIA_NAO_RETIRADA:
                novoEstado = this.estadoFranquiaNaoRetirada();
                break;
            case ESTADO_FRANQUIA_RETIRADA:
                novoEstado = this.estadoFranquiaRetirada();
                break;
            case ESTADO_PENDENTE:
                novoEstado = this.estadoPendente();
                break;
            case ESTADO_PROCESSO_FINALIZADO:
                novoEstado = this.estadoProcessoFinalizado();
                break;
            default:
                novoEstado = EXPIRACAO_PARALIZADA_COM_FALHA; // Caso receba um estado inexperado
                break;
        }
        return novoEstado;
    }
    
    /**
     * Informa se o estado informado eh um estado de falha
     * @param estado Estado a ser testado como sucesso ou falha
     * @return 
     */
    private boolean isEstadoDeFalha(int estado)
    {
        boolean retorno;
        retorno = (estado == ESTADO_BLOQUEADO) ||
        (estado == ESTADO_FRANQUIA_NAO_RETIRADA) ||
        (estado == ESTADO_FRANQUIA_AINDA_NAO_RETIRADA) ||
        (estado == ESTADO_AINDA_DESBLOQUEADO);
        return retorno;
    }

    /**
     * Informa se o estado informado eh um estado de sucesso
     * @param estado Estado a ser testado como sucesso ou falha
     * @return 
     */
    private boolean isEstadoDeSucesso(int estado)
    {
        boolean retorno;
        retorno = (estado == ESTADO_DESBLOQUEADO) ||
        (estado == ESTADO_PROCESSO_FINALIZADO) ||
        (estado == ESTADO_FRANQUIA_RETIRADA);
        return retorno;
    }

    //  Inicio dos estados da maquina de estados.
    /**
     *  Processamento do estado Pendente
     * @return Novo estado para qual a maquina deve ir
     */
    private int estadoPendente()
    {
        // Conferir se o assinante estah bloqueado ou nao
        if(this.isAssinanteDesbloqueado())
        {// Se o assinante nao estiver bloqueado, tentar retirar a franquia
            if(this.retirarFranquia())
            {
                return ESTADO_PROCESSO_FINALIZADO;
            }
            else
            {
                return ESTADO_FRANQUIA_NAO_RETIRADA;
            }
        }
        else
        {// Se o assinante estiver bloqueado, tentar desbloque-a lo
            if(this.desbloquearAssinante())
            {
                return ESTADO_DESBLOQUEADO;
            }
            else
            {
                return ESTADO_BLOQUEADO;
            }
        }
    }

    /**
     *  Processamento do estado Bloqueado
     * @return Novo estado para qual a maquina deve ir
     */
    private int estadoBloqueado()
    {
        if(this.desbloquearAssinante())
        {
            return ESTADO_DESBLOQUEADO;
        }
        else
        {
            return ESTADO_BLOQUEADO;
        }
    }

    /**
     *  Processamento do estado FranquiaNaoRetirada
     * @return Novo estado para qual a maquina deve ir
     */
    private int estadoFranquiaNaoRetirada()
    {
        if(this.retirarFranquia())
        {
            return ESTADO_PROCESSO_FINALIZADO;
        }
        else
        {
            return ESTADO_FRANQUIA_NAO_RETIRADA;
        }
    }

    /**
     *  Processamento do estado FranquiaAindaNaoRetirada
     * @return Novo estado para qual a maquina deve ir
     */
    private int estadoFranquiaAindaNaoRetirada()
    {
        if(this.retirarFranquia())
        {
            return ESTADO_PROCESSO_FINALIZADO;
        }
        else
        {
            return ESTADO_FRANQUIA_NAO_RETIRADA;
        }
    }

    /**
     *  Processamento do estado Desbloqueado
     * @return Novo estado para qual a maquina deve ir
     */
    private int estadoDesbloqueado()
    {
        if(this.retirarFranquia())
        {
            return ESTADO_FRANQUIA_RETIRADA;
        }
        else
        {
            return ESTADO_FRANQUIA_AINDA_NAO_RETIRADA;
        }
    }

    /**
     *  Processamento do estado AindaDesbloqueado
     * @return Novo estado para qual a maquina deve ir
     */
    private int estadoAindaDesbloqueado()
    {
        if(this.bloquearAssinante())
        {
            return ESTADO_PROCESSO_FINALIZADO;
        }
        else
        {
            return ESTADO_AINDA_DESBLOQUEADO;
        }
    }

    /**
     *  Processamento do estado FranquiaRetirada
     * @return Novo estado para qual a maquina deve ir
     */
    private int estadoFranquiaRetirada()
    {
        if(this.bloquearAssinante())
        {
            return ESTADO_PROCESSO_FINALIZADO;
        }
        else
        {
            return ESTADO_AINDA_DESBLOQUEADO;
        }
    }

    /**
     *  Processamento do estado ProcessoFinalizado
     * @return Novo estado para qual a maquina deve ir
     */
    private int estadoProcessoFinalizado()
    {
        return EXPIRACAO_FINALIZADA_COM_SUCESSO;
    }

//  Fim dos estados da maquina de estados.
    /**
     * Testa se o assinante processado estah desbloqueado
     * @return
     */
    private boolean isAssinanteDesbloqueado()
    {
        Assinante assinante= this.vo.getAssinante();
        if(assinante.getStatusServico()== Definicoes.SERVICO_DESBLOQUEADO)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Tenta executar o procedimento de desbloqueio do assinante processado dentro do VO
     * @return
     */
    private boolean desbloquearAssinante()
    {
        boolean retorno = false;
        try
        {
            short statusAprovisionamento = this.aprovisionar.desbloqueiaAssinante(this.vo.getMsisdn());
            if (statusAprovisionamento == Definicoes.RET_OPERACAO_OK)
            { // Unico caso de sucesso
                retorno = true;
            }
        } catch (GPPTecnomenException e)
        {
            this.log(Definicoes.ERRO, "desbloquearAssinante", "Erro na TECNOMEN: " + e);
        } catch (GPPInternalErrorException e)
        {
            this.log(Definicoes.ERRO, "desbloquearAssinante", "Erro interno GPP: " + e);
        }
        return retorno;
    }
    /**
     * Tenta executar o procedimento de bloqueio do assinante processado dentro do VO
     * @return
     */
    private boolean bloquearAssinante()
    {
        boolean retorno = false;
        try
        {
            short statusOperacao = this.aprovisionar.bloqueiaAssinante(this.vo.getMsisdn());
            if (statusOperacao == Definicoes.RET_OPERACAO_OK)
            { // Unico caso de sucesso
                retorno = true;
            }
        } catch (GPPTecnomenException e)
        {
            this.log(Definicoes.ERRO, "bloquearAssinante", "Erro na TECNOMEN: " + e);
        }
        return retorno;
    }
    
    /**
     * Tenta executar o procedimento de retirar o valor de franquia expirada
     * do assinante processado dentro do VO
     * @return
     */
    private boolean retirarFranquia()
    {
        boolean retorno = false;

        Date dataAtual = Calendar.getInstance().getTime();

        String msisdn = this.vo.getMsisdn();
        String tipoTransacao = Definicoes.EXPIRACAO_FRANQUIA_CONTROLE_TOTAL;
        String operador = Definicoes.GPP_OPERADOR;
        boolean isAjusteNormal = Definicoes.AJUSTE_NORMAL;
        Date dataHora = dataAtual;
        String sistemaOrigem = Definicoes.SO_GNV;
        Assinante assinante = this.vo.getAssinante();
        String descricao = "Expiracao de Franquia Controle Total";
        double valor = this.vo.getVlrFranquiaAtual()- this.vo.getVlrFranquiaNaoExpirada();
        String tipoAjuste = Definicoes.TIPO_AJUSTE_DEBITO;
        String tipoCredito = Definicoes.TIPO_CREDITO_FRANQUIA;
        Date dataExpiracao = null;
        short statusOperacao = this.ajustar.executarAjuste(
                msisdn,
                tipoTransacao,
                tipoCredito,
                valor,
                tipoAjuste,
                dataHora,
                sistemaOrigem,
                operador,
                dataExpiracao,
                assinante,
                descricao,
                isAjusteNormal);
        if (statusOperacao == Definicoes.RET_OPERACAO_OK)
        {
            retorno = true;
        }
        
        // Caso ocorra a necessidade de uma nova consulta assiante
        if (statusOperacao == Definicoes.RET_CREDITO_INSUFICIENTE)
        {
            this.log(Definicoes.INFO, "retirarFranquia",
                    "Nova consulta assinante por credito insuficiente");
            try
            {
                // Atualiza as informacoes do assinante
                ConsultaAssinante consultaAssinante = new ConsultaAssinante(this.idProcesso);
                assinante = consultaAssinante.executaConsultaCompletaAssinanteTecnomen(vo.getMsisdn());
                vo.setAssinante(assinante);
            } catch (GPPInternalErrorException e)
            {
                this.log(Definicoes.ERRO, "retirarFranquia",
                        "Falha em nova consulta assinante por credito insuficiente. Erro " + e.toString());
            }
        }
        return retorno;
    }
    
    /**
     * 
     * @return qtdTentativas. Retorna zero se deu algum problema
     */
    private int atualizarNumeroDeTentativas()
    {
        int qtdTentativas = 0;
        //Inserir na tabela e retorna o numero de tentativas jah realizadas
        try
        {
            qtdTentativas = 1 + this.dao.getQtdTentativas(this.vo); // Increventando as tentativas em 1
            this.vo.setQtdTentativas(qtdTentativas);
            this.dao.atualizarNumeroDeTentativas(this.vo);
        } catch (GPPInternalErrorException e)
        {
            this.log(Definicoes.ERRO, "atualizarNumeroDeTentativas", "Erro interno GPP" + e);
        }
        return qtdTentativas ; // Retorna zero se ocorreu algum problema
    }

    private void removerRegistroDeFalha()
    {
        try
        {
            this.dao.removerFalha(this.vo);
        } catch (GPPInternalErrorException e)
        {
            this.log(Definicoes.ERRO, "removerRegistroDeFalha", "Erro interno GPP" + e);
        }
    }
    /**
     * Registra os eventos em log
     * @param tipo     ERRO ou WARN ou DEBUG ou INFO
     * @param metodo   O metodo no qual ocorreu o evento a ser logado
     * @param mensagem A mensagem a ser passada
     */
 private void log (int tipo, String metodo, String mensagem)
 {
     log.log(this.idProcesso, tipo, Definicoes.CL_EXPIRACAO_FRANQUIA_CONTROLE_TOTAL_DAO, metodo, mensagem);
 }

}
