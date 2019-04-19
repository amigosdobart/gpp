package br.com.brasiltelecom.awt;

import java.awt.Frame;
import java.awt.Panel;
import java.awt.Label;

public class FrameApresentacao extends Frame {

	private Panel pCentral = null;
	private Label lInfo = null;
	private Label lValor = null;

	/**
	 * This method initializes pCentral	
	 * 	
	 * @return java.awt.Panel	
	 */
	private Panel getPCentral() {
		if (pCentral == null) {
			lValor = new Label();
			lValor.setText("valor");
			lInfo = new Label();
			lInfo.setText("info");
			pCentral = new Panel();
			pCentral.add(lInfo, null);
			pCentral.add(lValor, null);
		}
		return pCentral;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	/**
	 * This is the default constructor
	 */
	public FrameApresentacao() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(169, 65);
		this.setTitle("Frame");
		this.add(getPCentral(), java.awt.BorderLayout.CENTER);
	}

	public void setInfo(String vlr){
		lInfo.setText(vlr);
	}
	public void setValor(String vlr){
		lValor.setText(vlr);
	}
	public void getImage(){
		
		
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
