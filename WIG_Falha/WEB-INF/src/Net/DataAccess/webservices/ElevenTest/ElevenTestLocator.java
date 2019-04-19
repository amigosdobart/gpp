/**
 * ElevenTestLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package Net.DataAccess.webservices.ElevenTest;

public class ElevenTestLocator extends org.apache.axis.client.Service implements Net.DataAccess.webservices.ElevenTest.ElevenTest {

/**
 * Visual DataFlex Web Service for Checksums based on the 'ElfProef'.
 * The 'ElfProef' is a CRC technique determine if a number is a valid
 * number where the sum of the passed digits divided by 11 must be a
 * integer. Security Note: No information about the passed numbers will
 * be stored, we only count the number of times anyone used one of the
 * functions of this webservice for statistic reports.
 */

    public ElevenTestLocator() {
    }


    public ElevenTestLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ElevenTestLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ElevenTestSoap
    private java.lang.String ElevenTestSoap_address = "http://webservices.daehosting.com/services/eleventest.wso";

    public java.lang.String getElevenTestSoapAddress() {
        return ElevenTestSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ElevenTestSoapWSDDServiceName = "ElevenTestSoap";

    public java.lang.String getElevenTestSoapWSDDServiceName() {
        return ElevenTestSoapWSDDServiceName;
    }

    public void setElevenTestSoapWSDDServiceName(java.lang.String name) {
        ElevenTestSoapWSDDServiceName = name;
    }

    public Net.DataAccess.webservices.ElevenTest.ElevenTestSoapType getElevenTestSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ElevenTestSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getElevenTestSoap(endpoint);
    }

    public Net.DataAccess.webservices.ElevenTest.ElevenTestSoapType getElevenTestSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            Net.DataAccess.webservices.ElevenTest.ElevenTestSoapBindingStub _stub = new Net.DataAccess.webservices.ElevenTest.ElevenTestSoapBindingStub(portAddress, this);
            _stub.setPortName(getElevenTestSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setElevenTestSoapEndpointAddress(java.lang.String address) {
        ElevenTestSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (Net.DataAccess.webservices.ElevenTest.ElevenTestSoapType.class.isAssignableFrom(serviceEndpointInterface)) {
                Net.DataAccess.webservices.ElevenTest.ElevenTestSoapBindingStub _stub = new Net.DataAccess.webservices.ElevenTest.ElevenTestSoapBindingStub(new java.net.URL(ElevenTestSoap_address), this);
                _stub.setPortName(getElevenTestSoapWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("ElevenTestSoap".equals(inputPortName)) {
            return getElevenTestSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://webservices.DataAccess.Net/ElevenTest", "ElevenTest");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://webservices.DataAccess.Net/ElevenTest", "ElevenTestSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("ElevenTestSoap".equals(portName)) {
            setElevenTestSoapEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
