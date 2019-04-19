/*
 * Created on 10/09/2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.brt.gppMediadorCDR.gui;

import com.brt.gppMediadorCDR.formatoArquivos.FormatoDeArquivoCDR;
import com.brt.gppMediadorCDR.formatoArquivos.ArquivoFormatoA;
import com.brt.gppMediadorCDR.formatoArquivos.ArquivoFormatoB;
import com.brt.gppMediadorCDR.formatoArquivos.ArquivoFormatoC;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import java.util.TreeSet;
import java.util.Iterator;
import java.text.SimpleDateFormat;

import java.awt.BorderLayout;
/**
 * @author EX204006
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AnaliseCDRs extends JFrame
{
	private javax.swing.JPanel jContentPane = null;
	private JPanel jpnCabecalho = null;
	private JLabel jlbTipCdr = null;
	private JComboBox jcbTipCdr = null;
	private JLabel jlbLinhaCdr = null;
	private JTextField jtxLinhaCdr = null;
	private JButton jbtAdd = null;

	private JPanel jpnDados = null;
	private JTable jtbDados = null;  //  @jve:decl-index=0:visual-constraint="151,-32"
	private JScrollPane jscDados = null;

	private TreeSet dadosCdr = null;

	/**
	 * This is the default constructor
	 */
	public AnaliseCDRs()
	{
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize()
	{
		this.setSize(735, 494);
		this.setContentPane(getJContentPane());
		this.setTitle("Analise de CDRs");
		this.pack();
		populaTiposCDRs();
		dadosCdr = new TreeSet();
	}

	private void populaTiposCDRs()
	{
		jcbTipCdr.addItem("PP");
		jcbTipCdr.addItem("MO");
		jcbTipCdr.addItem("MT");
		jcbTipCdr.addItem("MMS");
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getJpnCabecalho()
	{
		if (jpnCabecalho == null)
		{
			jpnCabecalho = new JPanel();
			jlbLinhaCdr = new JLabel();
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			jlbTipCdr = new JLabel();
			jpnCabecalho.setLayout(new GridBagLayout());
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			jpnCabecalho = new JPanel();
			jlbTipCdr.setText("Tipo de CDR:");
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.insets = new java.awt.Insets(0,0,0,0);
			gridBagConstraints2.gridx = 1;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.fill = java.awt.GridBagConstraints.NONE;
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.gridy = 2;
			jlbLinhaCdr.setText("Linha CDR:");
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.EAST;
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints2.insets = new java.awt.Insets(0,5,0,0);
			gridBagConstraints4.gridx = 1;
			gridBagConstraints4.gridy = 2;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints4.insets = new java.awt.Insets(0,5,0,0);
			gridBagConstraints5.gridx = 2;
			gridBagConstraints5.gridy = 2;
			gridBagConstraints5.insets = new java.awt.Insets(0,5,0,0);
			gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
			jpnCabecalho.add(jlbTipCdr, gridBagConstraints1);
			jpnCabecalho.add(getJcbTipCdr(), gridBagConstraints2);
			jpnCabecalho.add(jlbLinhaCdr, gridBagConstraints3);
			jpnCabecalho.add(getJtxLinhaCdr(), gridBagConstraints4);
			jpnCabecalho.add(getJbtAdd(), gridBagConstraints5);
		}
		return jpnCabecalho;
	}

	/**
	 * This method initializes jComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */    
	private JComboBox getJcbTipCdr()
	{
		if (jcbTipCdr == null)
		{
			jcbTipCdr = new JComboBox();
			jcbTipCdr.setPreferredSize(new java.awt.Dimension(80,20));
		}
		return jcbTipCdr;
	}

	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getJtxLinhaCdr()
	{
		if (jtxLinhaCdr == null)
		{
			jtxLinhaCdr = new JTextField();
			jtxLinhaCdr.setPreferredSize(new java.awt.Dimension(500,20));
		}
		return jtxLinhaCdr;
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getJbtAdd()
	{
		if (jbtAdd == null) {
			jbtAdd = new JButton();
			jbtAdd.setText("Add");
			jbtAdd.setPreferredSize(new java.awt.Dimension(56,20));
			jbtAdd.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					adicionaLinhaCDR(jtxLinhaCdr.getText());
				}
			});
		}
		return jbtAdd;
	}

	private void adicionaLinhaCDR(String linha)
	{
		FormatoDeArquivoCDR arquivoCDR = null;
		if (jcbTipCdr.getSelectedItem().equals("MMS"))
		{
			arquivoCDR = new ArquivoFormatoB();
			((ArquivoFormatoB)arquivoCDR).setTipoCDR("MMS");
		}
		else if (jcbTipCdr.getSelectedItem().equals("MT"))
		{
			arquivoCDR = new ArquivoFormatoC();
			((ArquivoFormatoC)arquivoCDR).setTipoCDR("MT");
		}
		else if (jcbTipCdr.getSelectedItem().equals("MO"))
		{
			arquivoCDR = new ArquivoFormatoA();
			((ArquivoFormatoA)arquivoCDR).setTipoCDR("MO");			
		}
		else
		{
			arquivoCDR = new ArquivoFormatoA();
			((ArquivoFormatoA)arquivoCDR).setTipoCDR("PP");
		}
		
		arquivoCDR.parse(linha);
		dadosCdr.add(arquivoCDR);
		refreshTabelaDados();
		jtxLinhaCdr.setText("");
	}
	
	private void refreshTabelaDados()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		while (jtbDados.getRowCount() > 0)
			((DefaultTableModel)jtbDados.getModel()).removeRow(0);
		
		for (Iterator i=dadosCdr.iterator(); i.hasNext();)
		{
			Object data[] = new Object[jtbDados.getColumnCount()];
			if (jcbTipCdr.getSelectedItem().equals("MMS"))
			{
				ArquivoFormatoB arquivoB = (ArquivoFormatoB)i.next();
				data[0] = arquivoB.getSubscriberId();
				data[1] = sdf.format(arquivoB.getTimestamp()); 
				data[2] = arquivoB.getStartTimeFormatado();
				data[3] = new Long(arquivoB.getCost());
				data[4] = new Long(arquivoB.getTransactionType());
				data[5] = new Long(arquivoB.getAccountBalance());
				data[6] = new Long(0);
				data[7] = arquivoB.getTipoCDR();
			}
			else if (jcbTipCdr.getSelectedItem().equals("MT"))
			{
				ArquivoFormatoC arquivoC = (ArquivoFormatoC)i.next();
				data[0] = arquivoC.getSubscriberId();
				data[1] = sdf.format(arquivoC.getTimestamp());
				data[2] = arquivoC.getStartTimeFormatado();
				data[3] = new Long(arquivoC.getCost());
				data[4] = new Long(arquivoC.getTransactionType());
				data[5] = new Long(arquivoC.getAccountBalance());
				data[6] = new Long(0);
				data[7] = arquivoC.getTipoCDR();
			}
			else 
				{
					ArquivoFormatoA arquivoA = (ArquivoFormatoA)i.next();
					data[0] = arquivoA.getSubId();
					data[1] = sdf.format(arquivoA.getTimestamp());
					data[2] = arquivoA.getStartTimeFormatado();
					data[3] = new Long(arquivoA.getAccountBalanceDelta());
					data[4] = new Long(arquivoA.getTransactionType());
					data[5] = new Long(arquivoA.getFinalAccountBalance());
					data[6] = new Long(arquivoA.getExternalTransactionType());
					data[7] = arquivoA.getTipoCDR();
				}
			((DefaultTableModel)jtbDados.getModel()).addRow(data);
		}
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane()
	{
		if(jContentPane == null)
		{
			jContentPane = new javax.swing.JPanel();
			jContentPane.setLayout(new java.awt.BorderLayout());
			jContentPane.add(getJpnCabecalho(), java.awt.BorderLayout.NORTH);
			jContentPane.add(getJpnDados(), java.awt.BorderLayout.CENTER);
		}
		return jContentPane;
	}
	
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getJpnDados()
	{
		if (jpnDados == null)
		{
			jpnDados = new JPanel();
			jpnDados.setLayout(new BorderLayout());
			jpnDados.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dados do CDR", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
			jpnDados.add(getJscDados(), java.awt.BorderLayout.NORTH);
		}
		return jpnDados;
	}

	/**
	 * This method initializes jTable	
	 * 	
	 * @return javax.swing.JTable	
	 */    
	private JTable getJtbDados()
	{
		if (jtbDados == null)
		{
			jtbDados = new JTable();
			jtbDados.setPreferredSize(new java.awt.Dimension(700,500));
	        jtbDados.setModel(new javax.swing.table.DefaultTableModel
	            (
	                new Object [][] { },
	                new String [] {
	                    "MSISDN", "Data", "StartTime", "CallValue","TransType","FinalBallance","ExtTransType","TipoCDR"
	                              }
	            ){
	                Class[] types = new Class [] {
	                    java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class,java.lang.String.class,java.lang.String.class
	                };
	                boolean[] canEdit = new boolean [] {
	                    false, false, false, false, false, false, false, false
	                };

	                public Class getColumnClass(int columnIndex) {
	                    return types [columnIndex];
	                }

	                public boolean isCellEditable(int rowIndex, int columnIndex) {
	                    return canEdit [columnIndex];
	                }
	            }
	          );	        
		}
		return jtbDados;
	}
	
	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getJscDados()
	{
		if (jscDados == null)
		{
			jscDados = new JScrollPane();
			jscDados.setViewportView(getJtbDados());
			jscDados.setPreferredSize(new java.awt.Dimension(650,350));
		}
		return jscDados;
	}
    
	/**
	 * Metodo....:main
	 * Descricao.:Executa o programa
	 * @param args
	 */
	public static void main(String[] args)
    {
		new AnaliseCDRs().show();
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
