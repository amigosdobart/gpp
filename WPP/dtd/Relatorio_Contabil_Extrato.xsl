<?xml version="1.0" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:template match="CONTABILEXTRATO">
	<html>
		<xsl:apply-templates/>
	</html>
	</xsl:template>
	<xsl:template match="LIST_G_MES_ANTERIOR">
		<xsl:apply-templates/>
	</xsl:template>
	<xsl:template match="G_MES_ANTERIOR">
		<xsl:apply-templates/>
	</xsl:template>
	<xsl:template match="PERIODO">
		<b>Período:</b><xsl:apply-templates/>
	</xsl:template>
	<xsl:template match="LIST_G_IDT_CODIGO_NACIONAL3">
			<table border="1">
			<th>CN</th>
			<th>Saldo Assinantes Inicial</th>
			<th>Recargas</th>
			<th>Bônus</th>
			<th>Ajustes</th>
			<th>Consumo de Voz e Dados</th>
			<th>Outros Serviços</th>
			<th>Ativação (First-time-user->Normal)</th>
			<th>Mudança de Status (Recharge Expired -> Disconnected)</th>
			<th>Mudança de Status (Recharge Expired -> Shutdown)</th>
			<th>Mudança de Status (Normal -> Shutdown)</th>
			<th>Saldo Calculado</th>
			<th>Saldo Assinantes Final</th>
			<th>Diferença</th>
				<xsl:apply-templates/>
			</table>
	</xsl:template>
	<xsl:template match="G_IDT_CODIGO_NACIONAL3">
			<tr>
				<xsl:apply-templates/>
			</tr>
	</xsl:template>
	<xsl:template match="IDT_CODIGO_NACIONAL1">
			<td><xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="NVL_SALDO_ANT_TOTAL_0">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>

	<xsl:template match="NVL_RECARGAS_TOTAL_0">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="NVL_BONUS_TOTAL_0">
		<td>	<xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="NVL_AJUSTES_TOTAL_0">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="NVL_VOZDADOS_TOTAL_0">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="NVL_OUTROS_TOTAL_0">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="NVL_ATIVACAO_TOTAL_0">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="NVL_RDISCONNECTED_TOTAL_0">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="NVL_RSHUTDOWN_TOTAL_0">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="NVL_NSHUTDOWN_TOTAL_0">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="CALC">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="NVL_SALDO_POS_TOTAL_0">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="DIF">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
</xsl:stylesheet>