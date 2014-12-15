package com.autodesk.adn.recap.datacontracts;

import com.android.volley.VolleyError;
import com.google.gson.annotations.SerializedName;

public class ReCapError
{
	@SerializedName("msg")
	String _msg;
	
	@SerializedName("code")
	int _code;
	
	StackTraceElement[] _stackTrace;
	
    public ReCapError()
    {

    }

    public ReCapError(VolleyError error)
    {
    	_msg = error.getLocalizedMessage();
    	
    	if(_msg == null)
    	{
    		_msg = error.toString();
    	}
    	
    	_stackTrace = error.getStackTrace();
    }

    public ReCapError(Exception ex)
    {
    	_msg = ex.getLocalizedMessage();
    	
    	if(_msg == null)
    	{
    		_msg = ex.toString();
    	}
    	
    	_stackTrace = ex.getStackTrace();
    }
    
    public String getLocalizedMessage()
    {
    	return _msg;
    }
    
    public StackTraceElement[] getStackTrace()
    {
    	return _stackTrace;
    }
}