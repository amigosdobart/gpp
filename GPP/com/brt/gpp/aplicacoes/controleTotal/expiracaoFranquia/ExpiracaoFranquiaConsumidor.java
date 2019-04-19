package com.brt.gpp.aplicacoes.controleTotal.expiracaoFranquia;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.aplicacoes.consultar.ConsultaAssinante;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
 * Classe responsavel pela execucao do processo de Expiracao de Franquia
 * Controle Total
 * 
 * @author Magno Batista Corrêa
 * @since 2007/05/17 (yyyy/mm/dd)
 */
public final class ExpiracaoFranquiaConsumidor extends Aplicacoes implements
        ProcessoBatchConsumidor
{
    // Atributos.
    private PREPConexao conexaoPrep;                    // Conexao com o banco de dados.

    private ConsultaAssinante consultaAssinante;        // Classe para realizar a consulta de assinante

    private ExpiracaoFranquiaMaquinaDeEstados maquina;  // Maquina de estados do processo
    
    // Construtores.
    /**
     * Construtor da classe.
     */
    public ExpiracaoFranquiaConsumidor()
    {
        super(
                GerentePoolLog
                        .getInstancia(ExpiracaoFranquiaConsumidor.class)
                        .getIdProcesso(
                                Definicoes.CL_EXPIRACAO_FRANQUIA_CONTROLE_TOTAL_CONSUMIDOR),
                                Definicoes.CL_EXPIRACAO_FRANQUIA_CONTROLE_TOTAL_CONSUMIDOR);

        this.conexaoPrep = null;
    }

    // Implementacao de Consumidor.
    /**
     * Executa o processo de Expiracao de Franquia Controle Total para o
     * Assinante Informado. <BR>
     * Para cada cada VO passado, procssa a Expiracao.</BR>
     * 
     * @return obj VO a ser processado. Fornecido pelo produtor.
     * @throws Exception
     * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#execute(java.lang.Object)
     */
    public void execute(Object obj) throws Exception
    {
        super.log(Definicoes.DEBUG, "execute", "Inicio");
        ExpiracaoFranquiaVO vo = (ExpiracaoFranquiaVO) obj;

        // Faz a consulta do assinante na Tecnomen.
        Assinante assinante = this.consultaAssinante.executaConsultaCompletaAssinanteTecnomen(vo.getMsisdn());

        // Acresce a informacao do assinante no vo
        vo.setAssinante(assinante);

        // Confere as informacoes do assinante para ver se precisa de realizar expiracao ou nao
        double valorExpirado = vo.getVlrFranquiaAtual() - vo.getVlrFranquiaNaoExpirada();
        if(valorExpirado > 0.0)
        {
            // Executa a Maquina de estados para o dado objeto de expiracao
            this.maquina.run(vo);
        }
        // Coso o valor a ser expirado seja menor ou igual a zero, nada precisa ser feito
    }

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#finish()
     */
    public void finish()
    {
    }

    //Implementacao de ProcessoBatchConsumidor.

    /**
     *	@param		Produtor				produtor					Produtor de registros para execucao.
     *	@throws		Exception
     * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#startup(com.brt.gpp.comum.produtorConsumidor.Produtor)
     */
    public void startup(Produtor produtor) throws Exception
    {
        this.startup((ProcessoBatchProdutor) produtor);
    }

    /**
     *	Inicializa o objeto.
     *
     *	@param		ProcessoBatchProdutor	produtor					Produtor de registros para execucao.
     *	@throws		Exception
     */
    public void startup(ProcessoBatchProdutor produtor) throws Exception
    {
        this.conexaoPrep = produtor.getConexao();
        this.startup();
    }

    /**
     *	Inicializa o objeto. Uma vez que a unica operacao necessaria para o startup e a atribuicao do produtor, neste
     *	caso o metodo nao faz nada. 
     *
     *	@throws		Exception
     * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#startup()
     */
    public void startup() throws Exception
    {
        this.consultaAssinante = new ConsultaAssinante(super.logId);
        this.maquina = new ExpiracaoFranquiaMaquinaDeEstados(this.conexaoPrep,this.logId);
    }
}