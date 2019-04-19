/**
 * ElevenTest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package Net.DataAccess.webservices.ElevenTest;

public interface ElevenTest extends javax.xml.rpc.Service {

/**
 * Visual DataFlex Web Service for Checksums based on the 'ElfProef'.
 * The 'ElfProef' is a CRC technique determine if a number is a valid
 * number where the sum of the passed digits divided by 11 must be a
 * integer. Security Note: No information about the passed numbers will
 * be stored, we only count the number of times anyone used one of the
 * functions of this webservice for statistic reports.
 */
    public java.lang.String getElevenTestSoapAddress();

    public Net.DataAccess.webservices.ElevenTest.ElevenTestSoapType getElevenTestSoap() throws javax.xml.rpc.ServiceException;

    public Net.DataAccess.webservices.ElevenTest.ElevenTestSoapType getElevenTestSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
