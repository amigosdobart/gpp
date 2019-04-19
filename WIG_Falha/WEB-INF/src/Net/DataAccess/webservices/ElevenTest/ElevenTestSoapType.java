/**
 * ElevenTestSoapType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package Net.DataAccess.webservices.ElevenTest;

public interface ElevenTestSoapType extends java.rmi.Remote {

    /**
     * This function checks if a passed 'number' contains all numbers,
     * so if every character is in the the '0123456789' range.
     */
    public boolean isAllNumbers(java.lang.String sNumber) throws java.rmi.RemoteException;

    /**
     * This function strips out all characters from the input string
     * that are not numeric.
     */
    public java.lang.String stripToNumeric(java.lang.String sNumber) throws java.rmi.RemoteException;

    /**
     * This function assumes you pass a Bank Account Number (strips
     * out all non numeric characters) and checks the number with the 'ElfProef'.
     * If it is succesful the result will be 1 (=True) else it returns 0
     * (=False).
     */
    public boolean bankAccountNumbersTest(java.lang.String sAccountNumber) throws java.rmi.RemoteException;

    /**
     * This function assumes you pass a 'Burger Service Number' (it
     * strips out all non numeric characters) and checks the number with
     * the 'ElfProef'. If it is succesful the result will be 1 (=True) else
     * it returns 0 (=False).
     */
    public boolean BSNTest(java.lang.String sBSN) throws java.rmi.RemoteException;
}
