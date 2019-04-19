<?xml version="1.0" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:template match="MKTCARTAOUNICO">
		<html>
			<table border="1">
				<xsl:apply-templates/>
			</table>
		</html>
	</xsl:template>
	
	<xsl:template match="LIST_G_CABECALHO"/>

	<xsl:template match="G_CABECALHO"/>
	
	<xsl:template match="MES_CABECALHO"/>
	
	<xsl:template match="CN_CABECALHO"/>

	<xsl:template match="LIST_G_BODY">
		<tr>
			<th bgcolor="#009900">MSISDN</th>
			<th bgcolor="#009900">Duracao (segundos)</th>
			<th bgcolor="#009900">Valor (R$)</th>
			<th bgcolor="#009900">Numero de Registros</th>
		</tr>
		<xsl:apply-templates/>
	</xsl:template>
	
	<xsl:template match="G_BODY">
		<tr><xsl:apply-templates/></tr>
	</xsl:template>
	
	<xsl:template match="IDT_MSISDN">
		<td><xsl:apply-templates/></td>
	</xsl:template>
	
	<xsl:template match="VLR_DURACAO">
		<td><xsl:apply-templates/></td>
	</xsl:template>
	
	<xsl:template match="VLR_TOTAL">
		<td><xsl:apply-templates/></td>
	</xsl:template>
	
	<xsl:template match="QTD_REGISTROS">
		<td><xsl:apply-templates/></td>
	</xsl:template>
	
	<xsl:template match="SUMVLR_DURACAOPERREPORT">
		<tr>
			<th bgcolor="#009900">Duracao Total: </th>
			<td bgcolor="#009900" colspan="3"><xsl:apply-templates/></td>
		</tr>
	</xsl:template>

	<xsl:template match="SUMVLR_TOTALPERREPORT">
		<tr>
			<th bgcolor="#009900">Valor Total: </th>
			<td bgcolor="#009900" colspan="3"><xsl:apply-templates/></td>
		</tr>
	</xsl:template>

	<xsl:template match="SUMQTD_REGISTROSPERREPORT">
		<tr>
			<th bgcolor="#009900">Total de Registros: </th>
			<td bgcolor="#009900" colspan="3"><xsl:apply-templates/></td>
		</tr>
	</xsl:template>
	
</xsl:stylesheet>