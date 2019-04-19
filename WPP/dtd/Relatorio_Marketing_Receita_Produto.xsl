<?xml version="1.0" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:template match="MKTRECEITAPLANOPRODUTO">
	<html>
		<table border="1">
			<xsl:apply-templates/>
		</table>
	</html>
	</xsl:template>
	<xsl:template match="LIST_G_IDT_PLANO">
		
			<tr>
				<th>Produto</th>
				<th>Data</th>
				<th>Quantidade</th>
				<th>Valor(R$)</th>
			</tr>
			<xsl:apply-templates/>
			
	</xsl:template>
	
	<xsl:template match="G_IDT_PLANO">
		<tr>
				<xsl:apply-templates/>
		</tr>
	</xsl:template>

	<xsl:template match="IDT_PRODUTO">
		<td><xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="DAT_SUMARIO">
		<td><xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="VLR_QUANTIDADE">
		<td><xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="VLR_TOTAL_100000">
		<td><xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="SOMA_VLR_PRODUTOS">	</xsl:template>	
	<xsl:template match="SOMA_QTD_PRODUTOS">	</xsl:template>
	
	<xsl:template match="LIST_G_QTD_RESUMO">
		<xsl:apply-templates/>
	</xsl:template>
	<xsl:template match="G_QTD_RESUMO">
		<tr bgcolor="#007700">
			<td colspan="2">Total da Receita de Chamadas de Dados</td>
				<xsl:apply-templates/>
		</tr>
	</xsl:template>
	<xsl:template match="QTD_RESUMO">
		<td ><xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="VLR_RESUMO">
		<td ><xsl:apply-templates/></td>
	</xsl:template>

	<xsl:template match="LIST_G_IDT_PRODUTO1">
		<xsl:apply-templates/>
	</xsl:template>
	<xsl:template match="G_IDT_PRODUTO1">
		<tr bgcolor="#007700">
			<td colspan="2">Estorno</td>
				<xsl:apply-templates/>
		</tr>
	</xsl:template>
	<xsl:template match="SUM_VLR_TOTAL_100000">
		<td >Valor: <xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="SUM_VLR_QUANTIDADE">
		<td >Quantidade: <xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="ESTORNO">	</xsl:template>
	
</xsl:stylesheet>