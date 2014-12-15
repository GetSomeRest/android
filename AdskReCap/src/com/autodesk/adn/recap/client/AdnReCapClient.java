package com.autodesk.adn.recap.client;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpGet;
import android.content.Context;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.autodesk.adn.oauth.AdnOAuthConnector;
import com.autodesk.adn.recap.datacontracts.MeshFormatEnum;
import com.autodesk.adn.recap.datacontracts.ReCapError;
import com.autodesk.adn.recap.datacontracts.ReCapPhotosceneListResponse;
import com.autodesk.adn.recap.datacontracts.ReCapPhotoscenePropsResponse;
import com.autodesk.adn.recap.datacontracts.ReCapPhotosceneResponse;
import com.google.gson.Gson;

///////////////////////////////////////////////////////////////////////////////
//
//
///////////////////////////////////////////////////////////////////////////////
public class AdnReCapClient 
{
	private String _baseUrl;
	
	private String _clientId;
	
	private RequestQueue _requestQueue;

	private AdnOAuthConnector _connector;

	///////////////////////////////////////////////////////////////////////////
	//
	//
	///////////////////////////////////////////////////////////////////////////
	public AdnReCapClient(
			Context context,
			AdnOAuthConnector connector,
			String baseUrl,
			String clientId)
	{
		_clientId = clientId;
		
		_connector = connector;
		
		_requestQueue = Volley.newRequestQueue(context);
		
		_baseUrl = baseUrl + (baseUrl.endsWith("/") ? "":"/");
	}
	  
	///////////////////////////////////////////////////////////////////////////
	//
	//
	///////////////////////////////////////////////////////////////////////////
	public void getPhotosceneList(
		ReCapResponseListener<ReCapPhotosceneListResponse> listener)
	{
		try
		{
			String url = _baseUrl + 
				"photoscene/properties" + 
				"?json=1" + 
				"&clientID=" + _clientId;
			
			ReCapRequest<ReCapPhotosceneListResponse> request = 
				new ReCapRequest<ReCapPhotosceneListResponse>(
					Request.Method.GET, 
					url, 
					listener);
			
			_requestQueue.add(request);
		}
		catch(Exception ex)
		{
			listener.onError(new ReCapError(ex));
		}
	}
	
	///////////////////////////////////////////////////////////////////////////
	//
	//
	///////////////////////////////////////////////////////////////////////////
	public void getPhotosceneProperties(
        String photosceneId,
        ReCapResponseListener<ReCapPhotoscenePropsResponse> listener)
	{
		try
		{
			String url = _baseUrl + 
				"photoscene/" + photosceneId + "/properties" +
				"?json=1" +
				"&clientID=" + _clientId;
			
			ReCapRequest<ReCapPhotoscenePropsResponse> request = 
				new ReCapRequest<ReCapPhotoscenePropsResponse>(
					Request.Method.GET, 
					url, 
					listener);
			
			_requestQueue.add(request);
		}
		catch(Exception ex)
		{
			listener.onError(new ReCapError(ex));
		}
	}
	
	///////////////////////////////////////////////////////////////////////////
	//
	//
	///////////////////////////////////////////////////////////////////////////
	public void getPhotosceneLink(
			String photosceneId,
			MeshFormatEnum.Format meshFormat,
			ReCapResponseListener<ReCapPhotosceneResponse> listener)
	{
		try
		{
			String url = _baseUrl + 
				"photoscene/" + photosceneId + 
				"?json=1" +
				"&clientID=" + _clientId + 
				"&format=" + MeshFormatEnum.ToReCapString(meshFormat);
			
			ReCapRequest<ReCapPhotosceneResponse> request = 
				new ReCapRequest<ReCapPhotosceneResponse>(
				Request.Method.GET, 
				url, 
				listener);
			
			_requestQueue.add(request);
		}
		catch(Exception ex)
		{
			listener.onError(new ReCapError(ex));
		}
	}

	
	///////////////////////////////////////////////////////////////////////////
	//
	//
	///////////////////////////////////////////////////////////////////////////
	class ReCapRequest<T> extends StringRequest
	{
		public ReCapRequest(
			int method, 
			String url, 
			ReCapResponseListener<T> listener)
		{
			super(method, url, 
				new VolleyStringListener<T>(listener),
				new VolleyErrorListener<T>(listener));
		}
		
		@Override
		public Map<String, String> getHeaders() throws AuthFailureError 
		{					
			try
			{
				HashMap<String, String> headerMap = 
					new HashMap<String, String>();
				
				HttpGet request = new HttpGet(new URI(getUrl()));

				_connector.signRequest(request); 

				Header[] headers = request.getAllHeaders();
				
				for(Header header:headers)
				{
					headerMap.put(
						header.getName(), 
						header.getValue());
				}
				
				return headerMap;
			}
			catch(Exception ex)
			{
				return null;
			}
		}
	}
	
	class VolleyStringListener<T> implements Listener<String>
	{
		ReCapResponseListener<T> _reCapListener;
		
		public VolleyStringListener(
			ReCapResponseListener<T> reCapListener)
		{
			_reCapListener = reCapListener;
		}
		
		@Override
		public void onResponse(String response) 
		{
			try
			{
			    Gson gson = new Gson();
			
				T result = gson.fromJson(
					response.replace("{}", "\"\""), 
					_reCapListener.getResponseType());
				
				_reCapListener.onSuccess(result);
			}
			catch(Exception ex)
			{
				_reCapListener.onError(new ReCapError(ex));
			}
		}
	}
	
	class VolleyErrorListener<T> implements ErrorListener
	{
		ReCapResponseListener<T> _reCapListener;
		
		public VolleyErrorListener(
			ReCapResponseListener<T> reCapListener)
		{
			_reCapListener = reCapListener;
		}
		
		@Override
		public void onErrorResponse(VolleyError error) 
		{
			_reCapListener.onError(new ReCapError(error));			
		}		
	}
}






