<?xml version="1.0" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:template match="CONTABILRECARGAS">
	<html>
		<xsl:apply-templates/>
	</html>
	</xsl:template>
	<xsl:template match="LIST_G_PERÍODO">
		<table border="1">
		 <th>Período</th>
		 <th>CN</th>
		 <th>Código de Serviço</th>
		 <th>Descrição</th>
		 <th>Categoria</th>
		 <th>Documento</th>
		 <th>Qtd. de Recargas</th>
		 <th>Valor (R$)</th>
		 <th>Valor Líquido (R$)</th>
		 <th>Valor do ICMS(R$)</th>
		 <th>ICMS (%)</th>
			<xsl:apply-templates/>
			</table>
	</xsl:template>
	<xsl:template match="G_PERÍODO">
		<tr>
			<xsl:apply-templates/>
		</tr>
	</xsl:template>
	<xsl:template match="IDT_PERIODO_CONTABIL">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="IDT_CODIGO_NACIONAL">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="IDT_CODIGO_SERVICO_SFA">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="DESCRIÇÃO">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="CATEGORIA">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="DOCUMENTO">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="QTD_REGISTRO">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="VLR_TOTAL">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="VLR_TOTAL_SI">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="REL_VLR_TOTAL_REL_VLR_TOTAL_SI">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="ALIQUOTA_VLR_ALIQUOTA_100">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="LIST_G_NULL">
	</xsl:template>
	<xsl:template match="SOMA_QTDS">
					Total:<br></br>
							<b>Quantidade:</b><xsl:apply-templates/><br></br>
	</xsl:template>
	<xsl:template match="SOMA_VALOR">
			<b>Valor(R$):</b><xsl:apply-templates/><br></br>
	</xsl:template>
	<xsl:template match="SOMA_ICMS">
			<b>Valor do ICMS(R$):</b><xsl:apply-templates/><br></br>
	</xsl:template>
	<xsl:template match="SOMA_LIQUIDO">
				<b>Valor Líquido (R$):</b><xsl:apply-templates/><br></br>
	</xsl:template>
</xsl:stylesheet>