package br.com.brasiltelecom.ppp.portal.entity;

public class UsedVoucher implements org.exolab.castor.jdo.TimeStampable
{
	private String voucherNo;
	private double faceValue;
	private String subId;
	private long jdoTimestamp;
	
	// Metodos necessarios para implementacao 
	public long jdoGetTimeStamp()
	{
		return jdoTimestamp;
	}
	
	public void jdoSetTimeStamp(long jdoTimestamp)
	{
		this.jdoTimestamp = jdoTimestamp;
	}

	/**
	 * @return Returns the faceValue.
	 */
	public double getFaceValue() {
		return faceValue;
	}

	/**
	 * @param faceValue The faceValue to set.
	 */
	public void setFaceValue(double faceValue) {
		this.faceValue = faceValue;
	}

	/**
	 * @return Returns the subId.
	 */
	public String getSubId() {
		return subId;
	}

	/**
	 * @param subId The subId to set.
	 */
	public void setSubId(String subId) {
		this.subId = subId;
	}

	/**
	 * @return Returns the voucherNo.
	 */
	public String getVoucherNo() {
		return voucherNo;
	}

	/**
	 * @param voucherNo The voucherNo to set.
	 */
	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}
}
