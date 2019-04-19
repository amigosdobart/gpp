<?xml version="1.0" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:template match="MARKETINGCLIENTESSHUTDOWN">
		<html>
			<xsl:apply-templates/>
		</html>
	</xsl:template>
	<xsl:template match="LIST_G_CABECALHO"/>
	<xsl:template match="G_CABECALHO"/>
	<xsl:template match="CABECALHO"/>
	<xsl:template match="FINAL"/>
	<xsl:template match="INICIAL"/>
	
	<xsl:template match="LIST_G_CONSULTA">
		<table border="1">
			<tr>
				<th>Código Nacional</th>
				<th>MSISDN</th>
		 		<th>Data de Ativação</th>
		 		<th>Quantidade de Recargas</th>
				<th>Data de Shutdown</th>
		 	</tr>
		 	<xsl:apply-templates/>
		</table>
	</xsl:template>
	<xsl:template match="G_CONSULTA">
		<tr>
			<xsl:apply-templates/>
		</tr>
	</xsl:template>
	<xsl:template match="CN">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="MSISDN">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="DATA_ATIVACAO">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="QTD">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="DATA_ENTRADA">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>	
</xsl:stylesheet>