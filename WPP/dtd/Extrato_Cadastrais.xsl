<?xml version="1.0" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:template match="GPPExtrato">
	<GPPExtrato>
		<xsl:apply-templates/>
	</GPPExtrato>
	</xsl:template>
	<xsl:template match="mensagemComprovante">
	</xsl:template>
	<xsl:template match="dadosCorrespondencia">
	</xsl:template>
	<xsl:template match="dadosCadastrais">
		<dadosCadastrais>
			<xsl:apply-templates/>
		</dadosCadastrais>
	</xsl:template>

	<xsl:template match="nome">
		<nome>
			<xsl:apply-templates/>
		</nome>
	</xsl:template>
	<xsl:template match="endereco">
		<endereco>
			<xsl:apply-templates/>
		</endereco>
	</xsl:template>
	<xsl:template match="complemento">
		<complemento>
			<xsl:apply-templates/>
		</complemento>
	</xsl:template>
	<xsl:template match="bairro">
		<bairro>
			<xsl:apply-templates/>
		</bairro>
	</xsl:template>
	<xsl:template match="cidade">
		<cidade>
			<xsl:apply-templates/>
		</cidade>
	</xsl:template>
	<xsl:template match="uf">
		<uf>
			<xsl:apply-templates/>
		</uf>
	</xsl:template>
	<xsl:template match="cep">
		<cep>
			<xsl:apply-templates/>
		</cep>
	</xsl:template>
	<xsl:template match="msisdn">
		<msisdn>
			<xsl:apply-templates/>
		</msisdn>
	</xsl:template>
	<xsl:template match="dataAtivacao">
		<dataAtivacao>
			<xsl:apply-templates/>
		</dataAtivacao>
	</xsl:template>
	<xsl:template match="plano">
		<plano>
			<xsl:apply-templates/>
		</plano>
	</xsl:template>
	<xsl:template match="periodoInicial">
		<periodoInicial>
			<xsl:apply-templates/>
		</periodoInicial>
	</xsl:template>
	<xsl:template match="periodoFinal">
		<periodoFinal>
			<xsl:apply-templates/>
		</periodoFinal>
	</xsl:template>
	<xsl:template match="dataHoraImpressao">
		<dataHoraImpressao>
			<xsl:apply-templates/>
		</dataHoraImpressao>
	</xsl:template>

	<xsl:template match="mensagemServicos">
	</xsl:template>
	<xsl:template match="detalhe">
	</xsl:template>
	
	<xsl:template match="totais">
	</xsl:template>
	<xsl:template match="mensagemEventos">
	</xsl:template>
	<xsl:template match="evento">
	</xsl:template>
	<xsl:template match="mensagemInformativo">
	</xsl:template>
</xsl:stylesheet>