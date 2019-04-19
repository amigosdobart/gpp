<?xml version="1.0" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:template match="GPPExtrato">
	<GPPExtrato>
		<xsl:apply-templates/>
	</GPPExtrato>
	</xsl:template>
	<xsl:template match="mensagemComprovante">
	</xsl:template>
	<xsl:template match="dadosCorrespondencia">
	</xsl:template>
	<xsl:template match="dadosCadastrais">
	</xsl:template>
	<xsl:template match="mensagemServicos">
	</xsl:template>
	<xsl:template match="detalhe">
		<detalhe>
			<xsl:apply-templates/>
		</detalhe>
	</xsl:template>
	<xsl:template match="numeroLinha">
		<numeroLinha>
			<xsl:apply-templates/>
		</numeroLinha>
	</xsl:template>
	<xsl:template match="numeroOrigem">
		<numeroOrigem>
			<xsl:apply-templates/>
		</numeroOrigem>
	</xsl:template>
	<xsl:template match="data">
		<data>
			<xsl:apply-templates/>
		</data>
	</xsl:template>
	<xsl:template match="hora">
		<hora>
			<xsl:apply-templates/>
		</hora>
	</xsl:template>
	<xsl:template match="tipo">
		<tipo>
			<xsl:apply-templates/>
		</tipo>
	</xsl:template>
	<xsl:template match="operacao">
		<operacao>
			<xsl:apply-templates/>
		</operacao>
	</xsl:template>
	<xsl:template match="regiaoOrigem">
		<regiaoOrigem>
			<xsl:apply-templates/>
		</regiaoOrigem>
	</xsl:template>
	<xsl:template match="regiaoDestino">
		<regiaoDestino>
			<xsl:apply-templates/>
		</regiaoDestino>
	</xsl:template>
	<xsl:template match="numeroDestino">
		<numeroDestino>
			<xsl:apply-templates/>
		</numeroDestino>
	</xsl:template>
	<xsl:template match="duracao">
		<duracao>
			<xsl:apply-templates/>
		</duracao>
	</xsl:template>

	<xsl:template match="velores">
		<xsl:apply-templates/>
	</xsl:template>
	<xsl:template match="saldos">
		<xsl:apply-templates/>
	</xsl:template>

	<xsl:template match="valorPrincipal">
		<valorPrincipal>
			<xsl:apply-templates/>
		</valorPrincipal>
	</xsl:template>
	<xsl:template match="saldoPrincipal">
		<saldoPrincipal>
			<xsl:apply-templates/>
		</saldoPrincipal>
	</xsl:template>
	<xsl:template match="valorBonus">
		<valorBonus>
			<xsl:apply-templates/>
		</valorBonus>
	</xsl:template>
	<xsl:template match="saldoBonus">
		<saldoBonus>
			<xsl:apply-templates/>
		</saldoBonus>
	</xsl:template>
	<xsl:template match="valorSMS">
		<valorSMS>
			<xsl:apply-templates/>
		</valorSMS>
	</xsl:template>
	<xsl:template match="saldoSMS">
		<saldoSMS>
			<xsl:apply-templates/>
		</saldoSMS>
	</xsl:template>
	<xsl:template match="valorGPRS">
		<valorGPRS>
			<xsl:apply-templates/>
		</valorGPRS>
	</xsl:template>
	<xsl:template match="saldoGPRS">
		<saldoGPRS>
			<xsl:apply-templates/>
		</saldoGPRS>
	</xsl:template>
	<xsl:template match="valorPeriodico">
		<valorPeriodico>
			<xsl:apply-templates/>
		</valorPeriodico>
	</xsl:template>
	<xsl:template match="saldoPeriodico">
		<saldoPeriodico>
			<xsl:apply-templates/>
		</saldoPeriodico>
	</xsl:template>
	<xsl:template match="valorTotal">
		<valorTotal>
			<xsl:apply-templates/>
		</valorTotal>
	</xsl:template>
	<xsl:template match="saldoTotal">
		<saldoTotal>
			<xsl:apply-templates/>
		</saldoTotal>
	</xsl:template>
	
	
	<xsl:template match="totais">
	</xsl:template>
	<xsl:template match="mensagemEventos">
	</xsl:template>
	<xsl:template match="evento">
	</xsl:template>
	<xsl:template match="mensagemInformativo">
	</xsl:template>
</xsl:stylesheet>