/**
 * DatesServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.daehosting.webservices.dateservice;

public class DatesServiceLocator extends org.apache.axis.client.Service implements com.daehosting.webservices.dateservice.DatesService {

/**
 * Visual DataFlex Web Service for All kind of Date Features
 */

    public DatesServiceLocator() {
    }


    public DatesServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public DatesServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for DatesServiceSoap
    private java.lang.String DatesServiceSoap_address = "http://webservices.daehosting.com/services/datesservice.wso";

    public java.lang.String getDatesServiceSoapAddress() {
        return DatesServiceSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String DatesServiceSoapWSDDServiceName = "DatesServiceSoap";

    public java.lang.String getDatesServiceSoapWSDDServiceName() {
        return DatesServiceSoapWSDDServiceName;
    }

    public void setDatesServiceSoapWSDDServiceName(java.lang.String name) {
        DatesServiceSoapWSDDServiceName = name;
    }

    public com.daehosting.webservices.dateservice.DatesServiceSoapType getDatesServiceSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(DatesServiceSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getDatesServiceSoap(endpoint);
    }

    public com.daehosting.webservices.dateservice.DatesServiceSoapType getDatesServiceSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.daehosting.webservices.dateservice.DatesServiceSoapBindingStub _stub = new com.daehosting.webservices.dateservice.DatesServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getDatesServiceSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setDatesServiceSoapEndpointAddress(java.lang.String address) {
        DatesServiceSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.daehosting.webservices.dateservice.DatesServiceSoapType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.daehosting.webservices.dateservice.DatesServiceSoapBindingStub _stub = new com.daehosting.webservices.dateservice.DatesServiceSoapBindingStub(new java.net.URL(DatesServiceSoap_address), this);
                _stub.setPortName(getDatesServiceSoapWSDDServiceName());
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
        if ("DatesServiceSoap".equals(inputPortName)) {
            return getDatesServiceSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://webservices.daehosting.com/dateservice", "DatesService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://webservices.daehosting.com/dateservice", "DatesServiceSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("DatesServiceSoap".equals(portName)) {
            setDatesServiceSoapEndpointAddress(address);
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
