package Net.DataAccess.webservices.ElevenTest;

public class ElevenTestSoapTypeProxy implements Net.DataAccess.webservices.ElevenTest.ElevenTestSoapType {
  private String _endpoint = null;
  private Net.DataAccess.webservices.ElevenTest.ElevenTestSoapType elevenTestSoapType = null;
  
  public ElevenTestSoapTypeProxy() {
    _initElevenTestSoapTypeProxy();
  }
  
  public ElevenTestSoapTypeProxy(String endpoint) {
    _endpoint = endpoint;
    _initElevenTestSoapTypeProxy();
  }
  
  private void _initElevenTestSoapTypeProxy() {
    try {
      elevenTestSoapType = (new Net.DataAccess.webservices.ElevenTest.ElevenTestLocator()).getElevenTestSoap();
      if (elevenTestSoapType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)elevenTestSoapType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)elevenTestSoapType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (elevenTestSoapType != null)
      ((javax.xml.rpc.Stub)elevenTestSoapType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public Net.DataAccess.webservices.ElevenTest.ElevenTestSoapType getElevenTestSoapType() {
    if (elevenTestSoapType == null)
      _initElevenTestSoapTypeProxy();
    return elevenTestSoapType;
  }
  
  public boolean isAllNumbers(java.lang.String sNumber) throws java.rmi.RemoteException{
    if (elevenTestSoapType == null)
      _initElevenTestSoapTypeProxy();
    return elevenTestSoapType.isAllNumbers(sNumber);
  }
  
  public java.lang.String stripToNumeric(java.lang.String sNumber) throws java.rmi.RemoteException{
    if (elevenTestSoapType == null)
      _initElevenTestSoapTypeProxy();
    return elevenTestSoapType.stripToNumeric(sNumber);
  }
  
  public boolean bankAccountNumbersTest(java.lang.String sAccountNumber) throws java.rmi.RemoteException{
    if (elevenTestSoapType == null)
      _initElevenTestSoapTypeProxy();
    return elevenTestSoapType.bankAccountNumbersTest(sAccountNumber);
  }
  
  public boolean BSNTest(java.lang.String sBSN) throws java.rmi.RemoteException{
    if (elevenTestSoapType == null)
      _initElevenTestSoapTypeProxy();
    return elevenTestSoapType.BSNTest(sBSN);
  }
  
  
}