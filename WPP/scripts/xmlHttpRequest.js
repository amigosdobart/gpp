/*
 * Returns a new XMLHttpRequest object, or false if this browser
 * doesn't support it
 */
function newXMLHttpRequest()
{
  var xmlreq = false;
  if (window.XMLHttpRequest)
  {
  	// Create XMLHttpRequest object in non-Microsoft browsers
    xmlreq = new XMLHttpRequest();
  }
  else if (window.ActiveXObject)
  		{
			// Create XMLHttpRequest via MS ActiveX
    		try
    		{
		      // Try to create XMLHttpRequest in later versions
		      // of Internet Explorer
		      xmlreq = new ActiveXObject("Msxml2.XMLHTTP");
    		}
    		catch (e1)
    		{
				// Failed to create required ActiveXObject
	      		try
	      		{
	        		// Try version supported by older versions
	        		// of Internet Explorer
					xmlreq = new ActiveXObject("Microsoft.XMLHTTP");
				}
				catch (e2)
				{
	        		// Unable to create an XMLHttpRequest with ActiveX
	      		}
    		}
  		}

  return xmlreq;
}
