<?xml version="1.0" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:template match="MARKETINGRECARGAFACE">
		<html>
			<xsl:apply-templates/>
		</html>
	</xsl:template>
	<xsl:template match="LIST_G_CABECALHO"/>
	<xsl:template match="G_CABECALHO"/>
	<xsl:template match="CABECALHO_TITULO"/>
	<xsl:template match="DATA_FINAL_CABECALHO"/>
	<xsl:template match="DATA_INICIAL_CABECALHO"/>
	
	<xsl:template match="LIST_G_DATA">
		<table border="1">
			<tr>
				<th>Data</th>
		 		<th>UF</th>
		 		<th>CN</th>
				<th>Tipo de Transação</th>
		 		<th>Sistema de Origem</th>
		 		<th>Descrição</th>
		 		<th>Valor (R$)</th>
		 		<th>Quantidade</th>
		 		<th>Valor Total (R$)</th>
		 	</tr>
		 	<xsl:apply-templates/>
		</table>
	</xsl:template>
	<xsl:template match="G_DATA">
		<tr>
			<xsl:apply-templates/>
		</tr>
	</xsl:template>
	<xsl:template match="DATA">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="UF">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="CN">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="TIPO">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="SISTEMA_DE_ORIGEM">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="DESCRICAO">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="VALOR">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="QTD">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="TOTAL">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
</xsl:stylesheet>