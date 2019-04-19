<?xml version="1.0" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:template match="FINAJSTJUDICIAL">
	<html>
		<xsl:apply-templates/>
	</html>
	</xsl:template>
	<xsl:template match="LIST_G_FILIAL1">
		<table border="1">
				<tr>
					<th>Filial</th>
					<th>Valor Positivo</th>
					<th>Valor Negativo</th>
					<th>Imposto</th>
					<th>Total Líquido</th>
				</tr>
			<xsl:apply-templates/>
			</table>
	</xsl:template>
	<xsl:template match="G_FILIAL1">
			<tr >
				<xsl:apply-templates/>
			</tr>
	</xsl:template>

	<xsl:template match="FILIAL1">	</xsl:template>
	<xsl:template match="VALOR">	</xsl:template>
	<xsl:template match="VALOR_ALIQUOTA"></xsl:template>
	<xsl:template match="VALOR_VALOR_ALIQUOTA"></xsl:template>
	
	<xsl:template match="SUMVALORPERREPORT"></xsl:template>
	<xsl:template match="SUMVALOR_ALIQUOTAPERREPORT"></xsl:template>
	<xsl:template match="SUMVALOR_VALOR_ALIQUOTAPERREPO"></xsl:template>
	
	
	<xsl:template match="LIST_G_FILIAL"></xsl:template>
	<xsl:template match="G_FILIAL"></xsl:template>

	<xsl:template match="FILIAL1"><td><xsl:apply-templates/></td></xsl:template>
	<xsl:template match="VALORPOSITIVO"><td><xsl:apply-templates/></td></xsl:template>
	<xsl:template match="VALORNEGATIVO"><td><xsl:apply-templates/></td></xsl:template>
	<xsl:template match="COMICMS"><td><xsl:apply-templates/></td></xsl:template>
	<xsl:template match="SEMICMS"><td><xsl:apply-templates/></td></xsl:template>

	<xsl:template match="SUMVALORPOSITIVOPERREPORT"><b>Valor Total Positivo:</b><xsl:apply-templates/> <br></br></xsl:template>
	<xsl:template match="SUMVALORNEGATIVOPERREPORT"><b>Valor Total Negativo:</b><xsl:apply-templates/> <br></br></xsl:template>
	<xsl:template match="SUMCOMICMSPERREPORT"><b>Valor Total:</b><xsl:apply-templates/> <br></br></xsl:template>
	<xsl:template match="SUMSEMICMSPERREPORT"><b>Valor Total Líquido:</b><xsl:apply-templates/> <br></br></xsl:template>
	
</xsl:stylesheet>