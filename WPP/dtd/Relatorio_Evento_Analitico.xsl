<?xml version="1.0" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:template match="ESTATISTICAEVENTOSREGIAO">
	<html>
		<xsl:apply-templates/>
	</html>
	</xsl:template>
	<xsl:template match="LIST_G_DAT_APROVISIONAMENTO">
		<table border="1">
			<xsl:apply-templates/>
			</table>
	</xsl:template>
	<xsl:template match="G_DAT_APROVISIONAMENTO">
			<xsl:apply-templates/>
	</xsl:template>
	<xsl:template match="DAT_APROVISIONAMENTO">
			<tr bgcolor="#009900">
				<th>DIA</th>
				<td colspan="2">
					<xsl:apply-templates/>
				</td>
			</tr>
	</xsl:template>
	<xsl:template match="LIST_G_CN">
			<xsl:apply-templates/>
	</xsl:template>

	<xsl:template match="G_CN">
					<xsl:apply-templates/>
	</xsl:template>

	<xsl:template match="CN">
					<th colspan="2">CN</th>
					<th><xsl:apply-templates/></th>
	</xsl:template>
	
	<xsl:template match="LIST_G_IDT_MSISDN">
					<tr>
						<th colspan="3">Assinante</th>
					</tr>
					<xsl:apply-templates/>
	</xsl:template>

	<xsl:template match="G_IDT_MSISDN">
		<tr>
			<xsl:apply-templates/>
		</tr>
	</xsl:template>
	
	<xsl:template match="IDT_MSISDN">
		<td colspan="3"><xsl:apply-templates/></td>
	</xsl:template>

	<xsl:template match="TOTAL_REGIAO">
		<tr>
					<th colspan="2">Total Região</th>
					<th><xsl:apply-templates/></th>

		</tr>					
	</xsl:template>
	<xsl:template match="COUNTIDT_MSISDNPERDAT_APROVISI">
		<tr>
					<th colspan="2">Total dia</th>
					<th><xsl:apply-templates/></th>

		</tr>					
	</xsl:template>
	
	<xsl:template match="LIST_G_DES_EVENTO">
		<xsl:apply-templates/>
	</xsl:template>
	<xsl:template match="G_DES_EVENTO">
		<xsl:apply-templates/>
	</xsl:template>
	
	<xsl:template match="DES_EVENTO">
					<b>Evento:</b><xsl:apply-templates/><br></br>
	</xsl:template>
	<xsl:template match="COUNTIDT_MSISDNPERREPORT">
			<b>Qtd. Total:</b><xsl:apply-templates/> <br></br>
	</xsl:template>
</xsl:stylesheet>