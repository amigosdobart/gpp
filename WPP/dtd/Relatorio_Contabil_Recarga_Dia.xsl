<?xml version="1.0" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:template match="CONTABILRECARGADIA">
		<html>
			<xsl:apply-templates/>
		</html>
	</xsl:template>
	<xsl:template match="LIST_G_DATA_INICIAL_CABECALHO"/>
	<xsl:template match="G_DATA_INICIAL_CABECALHO"/>
	<xsl:template match="DATA_INICIAL_CABECALHO"/>
	<xsl:template match="DATA_FINAL_CABECALHO"/>
	
	<xsl:template match="LIST_G_DATA">
		<table border="1">
			<tr>
		 		<th>Data</th>
		 		<th>UF</th>
		 		<th>CN</th>
				<th>Plano</th>
				<th>Sistema de Origem</th>
		 		<th>Tipo de Transação</th>
		 		<th>Descrição</th>
		 		<th>Valor Principal (R$)</th>
		 		<th>Valor Bônus (R$)</th>
		 		<th>Valor Torpedos (R$)</th>
		 		<th>Valor Dados (R$)</th>
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
	<xsl:template match="PRE_HIBRIDO">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="SISTEMA_DE_ORIGEM">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="TIPO">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="DESCRICAO">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="VALOR_PRINCIPAL">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="VALOR_BONUS">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="VALOR_TORPEDOS">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="VALOR_DADOS">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="VALOR_TOTAL">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
</xsl:stylesheet>