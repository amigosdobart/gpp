package com.daehosting.webservices.dateservice;

public class DatesServiceSoapTypeProxy implements com.daehosting.webservices.dateservice.DatesServiceSoapType {
  private String _endpoint = null;
  private com.daehosting.webservices.dateservice.DatesServiceSoapType datesServiceSoapType = null;
  
  public DatesServiceSoapTypeProxy() {
    _initDatesServiceSoapTypeProxy();
  }
  
  public DatesServiceSoapTypeProxy(String endpoint) {
    _endpoint = endpoint;
    _initDatesServiceSoapTypeProxy();
  }
  
  private void _initDatesServiceSoapTypeProxy() {
    try {
      datesServiceSoapType = (new com.daehosting.webservices.dateservice.DatesServiceLocator()).getDatesServiceSoap();
      if (datesServiceSoapType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)datesServiceSoapType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)datesServiceSoapType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (datesServiceSoapType != null)
      ((javax.xml.rpc.Stub)datesServiceSoapType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.daehosting.webservices.dateservice.DatesServiceSoapType getDatesServiceSoapType() {
    if (datesServiceSoapType == null)
      _initDatesServiceSoapTypeProxy();
    return datesServiceSoapType;
  }
  
  public java.lang.String[] mainLanguages() throws java.rmi.RemoteException{
    if (datesServiceSoapType == null)
      _initDatesServiceSoapTypeProxy();
    return datesServiceSoapType.mainLanguages();
  }
  
  public java.lang.String[] subLanguages() throws java.rmi.RemoteException{
    if (datesServiceSoapType == null)
      _initDatesServiceSoapTypeProxy();
    return datesServiceSoapType.subLanguages();
  }
  
  public int systemDefaultLanguage() throws java.rmi.RemoteException{
    if (datesServiceSoapType == null)
      _initDatesServiceSoapTypeProxy();
    return datesServiceSoapType.systemDefaultLanguage();
  }
  
  public int languageId(java.lang.String sMainLanguage, java.lang.String sSubLanguage) throws java.rmi.RemoteException{
    if (datesServiceSoapType == null)
      _initDatesServiceSoapTypeProxy();
    return datesServiceSoapType.languageId(sMainLanguage, sSubLanguage);
  }
  
  public java.lang.String dayName(int iLanguage, int iDay, boolean bAbbreviated) throws java.rmi.RemoteException{
    if (datesServiceSoapType == null)
      _initDatesServiceSoapTypeProxy();
    return datesServiceSoapType.dayName(iLanguage, iDay, bAbbreviated);
  }
  
  public java.lang.String[] dayNames(int iLanguage, boolean bAbbreviated) throws java.rmi.RemoteException{
    if (datesServiceSoapType == null)
      _initDatesServiceSoapTypeProxy();
    return datesServiceSoapType.dayNames(iLanguage, bAbbreviated);
  }
  
  public java.lang.String monthName(int iLanguage, int iMonth, boolean bAbbreviated) throws java.rmi.RemoteException{
    if (datesServiceSoapType == null)
      _initDatesServiceSoapTypeProxy();
    return datesServiceSoapType.monthName(iLanguage, iMonth, bAbbreviated);
  }
  
  public java.lang.String[] monthNames(int iLanguage, boolean bAbbreviated, boolean bUse13Months) throws java.rmi.RemoteException{
    if (datesServiceSoapType == null)
      _initDatesServiceSoapTypeProxy();
    return datesServiceSoapType.monthNames(iLanguage, bAbbreviated, bUse13Months);
  }
  
  
}