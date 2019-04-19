<?xml version="1.0" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:template match="GPPExtrato">
		<GPPExtrato>
			<xsl:apply-templates/>
		</GPPExtrato>
	</xsl:template>
	
	<xsl:template match="mensagemComprovante"/>
	<xsl:template match="dadosCorrespondencia"/>
	<xsl:template match="dadosCadastrais"/>
	<xsl:template match="mensagemServicos"/>
	<xsl:template match="detalhe"/>
	
	<xsl:template match="totais">
		<xsl:apply-templates/>
	</xsl:template>
	
	<xsl:template match="saldoInicial">
		<saldo>
			<xsl:apply-templates/>
		</saldo>
	</xsl:template>
	
	<xsl:template match="totalDebitos">
		<saldo>
			<xsl:apply-templates/>
		</saldo>
	</xsl:template>
	
	<xsl:template match="totalCreditos">
		<saldo>
			<xsl:apply-templates/>
		</saldo>
	</xsl:template>
	
	<xsl:template match="saldoFinalPeriodo">
		<saldo>
			<xsl:apply-templates/>
		</saldo>
	</xsl:template>
	
	<xsl:template match="totalSaldoPrincipal">
		<saldoPrincipal>
			<xsl:apply-templates/>
		</saldoPrincipal>
	</xsl:template>
	
	<xsl:template match="totalSaldoBonus">
		<saldoBonus>
			<xsl:apply-templates/>
		</saldoBonus>
		
	</xsl:template>
	<xsl:template match="totalSaldoSMS">
		<saldoSMS>
			<xsl:apply-templates/>
		</saldoSMS>
	</xsl:template>
	
	<xsl:template match="totalSaldoGPRS">
		<saldoGPRS>
			<xsl:apply-templates/>
		</saldoGPRS>
	</xsl:template>
	
	<xsl:template match="totalSaldoPeriodico">
		<saldoPeriodico>
			<xsl:apply-templates/>
		</saldoPeriodico>
	</xsl:template>
	
	<xsl:template match="saldoTotalInicial">
		<saldoTotal>
			<xsl:apply-templates/>
		</saldoTotal>
	</xsl:template>
	
	<xsl:template match="totalDebitosPrincipal">
		<saldoPrincipal>
			<xsl:apply-templates/>
		</saldoPrincipal>
	</xsl:template>
	
	<xsl:template match="totalDebitosBonus">
		<saldoBonus>
			<xsl:apply-templates/>
		</saldoBonus>
		
	</xsl:template>
	<xsl:template match="totalDebitosSMS">
		<saldoSMS>
			<xsl:apply-templates/>
		</saldoSMS>
	</xsl:template>
	
	<xsl:template match="totalDebitosGPRS">
		<saldoGPRS>
			<xsl:apply-templates/>
		</saldoGPRS>
	</xsl:template>
	
	<xsl:template match="totalDebitosPeriodico">
		<saldoPeriodico>
			<xsl:apply-templates/>
		</saldoPeriodico>
	</xsl:template>
	
	<xsl:template match="debitosTotais">
		<saldoTotal>
			<xsl:apply-templates/>
		</saldoTotal>
	</xsl:template>
	
	<xsl:template match="totalCreditosPrincipal">
		<saldoPrincipal>
			<xsl:apply-templates/>
		</saldoPrincipal>
	</xsl:template>
	
	<xsl:template match="totalCreditosBonus">
		<saldoBonus>
			<xsl:apply-templates/>
		</saldoBonus>
		
	</xsl:template>
	<xsl:template match="totalCreditosSMS">
		<saldoSMS>
			<xsl:apply-templates/>
		</saldoSMS>
	</xsl:template>
	
	<xsl:template match="totalCreditosGPRS">
		<saldoGPRS>
			<xsl:apply-templates/>
		</saldoGPRS>
	</xsl:template>
	
	<xsl:template match="totalCreditosPeriodico">
		<saldoPeriodico>
			<xsl:apply-templates/>
		</saldoPeriodico>
	</xsl:template>
	
	<xsl:template match="creditosTotais">
		<saldoTotal>
			<xsl:apply-templates/>
		</saldoTotal>
	</xsl:template>
	
	<xsl:template match="totalSaldoFinalPrincipal">
		<saldoPrincipal>
			<xsl:apply-templates/>
		</saldoPrincipal>
	</xsl:template>
	
	<xsl:template match="totalSaldoFinalBonus">
		<saldoBonus>
			<xsl:apply-templates/>
		</saldoBonus>
		
	</xsl:template>
	<xsl:template match="totalSaldoFinalSMS">
		<saldoSMS>
			<xsl:apply-templates/>
		</saldoSMS>
	</xsl:template>
	
	<xsl:template match="totalSaldoFinalGPRS">
		<saldoGPRS>
			<xsl:apply-templates/>
		</saldoGPRS>
	</xsl:template>

	<xsl:template match="totalSaldoFinalPeriodico">
		<saldoPeriodico>
			<xsl:apply-templates/>
		</saldoPeriodico>
	</xsl:template>
	
	<xsl:template match="saldoTotalFinal">
		<saldoTotal>
			<xsl:apply-templates/>
		</saldoTotal>
	</xsl:template>
	
	<xsl:template match="mensagemEventos"/>
	<xsl:template match="evento"/>
	<xsl:template match="mensagemInformativo"/>
	
</xsl:stylesheet>