package GoogleSearch;

public class GoogleSearchPortProxy implements GoogleSearch.GoogleSearchPort {
  private String _endpoint = null;
  private GoogleSearch.GoogleSearchPort googleSearchPort = null;
  
  public GoogleSearchPortProxy() {
    _initGoogleSearchPortProxy();
  }
  
  public GoogleSearchPortProxy(String endpoint) {
    _endpoint = endpoint;
    _initGoogleSearchPortProxy();
  }
  
  private void _initGoogleSearchPortProxy() {
    try {
      googleSearchPort = (new GoogleSearch.GoogleSearchServiceLocator()).getGoogleSearchPort();
      if (googleSearchPort != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)googleSearchPort)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)googleSearchPort)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (googleSearchPort != null)
      ((javax.xml.rpc.Stub)googleSearchPort)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public GoogleSearch.GoogleSearchPort getGoogleSearchPort() {
    if (googleSearchPort == null)
      _initGoogleSearchPortProxy();
    return googleSearchPort;
  }
  
  public byte[] doGetCachedPage(java.lang.String key, java.lang.String url) throws java.rmi.RemoteException{
    if (googleSearchPort == null)
      _initGoogleSearchPortProxy();
    return googleSearchPort.doGetCachedPage(key, url);
  }
  
  public java.lang.String doSpellingSuggestion(java.lang.String key, java.lang.String phrase) throws java.rmi.RemoteException{
    if (googleSearchPort == null)
      _initGoogleSearchPortProxy();
    return googleSearchPort.doSpellingSuggestion(key, phrase);
  }
  
  public GoogleSearch.GoogleSearchResult doGoogleSearch(java.lang.String key, java.lang.String q, int start, int maxResults, boolean filter, java.lang.String restrict, boolean safeSearch, java.lang.String lr, java.lang.String ie, java.lang.String oe) throws java.rmi.RemoteException{
    if (googleSearchPort == null)
      _initGoogleSearchPortProxy();
    return googleSearchPort.doGoogleSearch(key, q, start, maxResults, filter, restrict, safeSearch, lr, ie, oe);
  }
  
  
}