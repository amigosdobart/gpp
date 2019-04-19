<?xml version="1.0" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:template match="CONTABILCONSUMO">
	<html>
		<xsl:apply-templates/>
	</html>
	</xsl:template>
	<xsl:template match="LIST_G_PERÍODO">
		<table border="1">
		 <th>Período</th>
		 <th>CN</th>
		 <th>CN Origem</th>
		 <th>Código de Serviço</th>
		 <th>Descrição</th>
		 <th>Categoria</th>
		 <th>Documento</th>
		 <th>Quantidade</th>
		 <th>Duração</th>
		 <th>Índice Bonificação</th>
		 <th>Consumo Recargas(R$)</th>
		 <th>ICMS Recargas (R%)</th>
		 <th>Consumo Bônus(R$)</th>
		 <th>ICMS Bônus(R$)</th>
		 <th>ICMS(%)</th>
			<xsl:apply-templates/>
			</table>
	</xsl:template>
	<xsl:template match="G_PERÍODO">
		<tr>
			<xsl:apply-templates/>
		</tr>
	</xsl:template>
	<xsl:template match="PERÍODO">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="CN">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="CN_ORIGEM">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="CÓDIGO_SERVIÇO">
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
	<xsl:template match="QTD_RECARGAS">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="REPLACE_TO_CHAR_TRUNC_QTD_MINU">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="INDICE_BONIFICACAO">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="TOTAL1">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="CONSUMO_RECARGAS_SI">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="CONSUMO_BONUS">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="CONSUMO_BONUS_SI">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="ALIQUOTA">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="LIST_G_NULL">
	</xsl:template>
	<xsl:template match="LIST_G_QTD_MINUTOS_FORMATADO1">
			<xsl:apply-templates/>
	</xsl:template>
	<xsl:template match="G_QTD_MINUTOS_FORMATADO1">
			<xsl:apply-templates/>
	</xsl:template>
	<xsl:template match="QTD_MINUTOS_FORMATADO1">
			Total:<br></br>
			<b>Duração:</b><xsl:apply-templates/><br></br>
	</xsl:template>
	<xsl:template match="SUMQTD_RECARGASPERREPORT">
							<b>Quantidade:</b><xsl:apply-templates/><br></br>
	</xsl:template>
	<xsl:template match="SUMCONSUMO_RECARGASPERREPORT">
			<b>Consumo de Recargas(R$):</b><xsl:apply-templates/><br></br>
	</xsl:template>
	<xsl:template match="SUMCONSUMO_RECARGAS_SIPERREPOR">
			<b>ICMS Recargas(R$):</b><xsl:apply-templates/><br></br>
	</xsl:template>
	<xsl:template match="SUMCONSUMO_BONUSPERREPORT">
			<b>Consumo de Bônus(R$):</b><xsl:apply-templates/><br></br>
	</xsl:template>
	<xsl:template match="SUMCONSUMO_BONUS_SIPERREPORT">
			<b>ICMS Bônus(R$):</b><xsl:apply-templates/><br></br>
	</xsl:template>
</xsl:stylesheet>