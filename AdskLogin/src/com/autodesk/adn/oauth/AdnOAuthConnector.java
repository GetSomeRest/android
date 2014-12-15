////////////////////////////////////////////////////////////////////////////////
//Copyright (c) Autodesk, Inc. All rights reserved 
//Written by Daniel Du 2013 - ADN/Developer Technical Services
//
//Permission to use, copy, modify, and distribute this software in
//object code form for any purpose and without fee is hereby granted, 
//provided that the above copyright notice appears in all copies and 
//that both that copyright notice and the limited warranty and
//restricted rights notice below appear in all supporting 
//documentation.
//
//AUTODESK PROVIDES THIS PROGRAM "AS IS" AND WITH ALL FAULTS. 
//AUTODESK SPECIFICALLY DISCLAIMS ANY IMPLIED WARRANTY OF
//MERCHANTABILITY OR FITNESS FOR A PARTICULAR USE.  AUTODESK, INC. 
//DOES NOT WARRANT THAT THE OPERATION OF THE PROGRAM WILL BE
//UNINTERRUPTED OR ERROR FREE.
/////////////////////////////////////////////////////////////////////////////////

package com.autodesk.adn.oauth;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.http.Header;
import org.apache.http.client.methods.HttpGet;

import android.net.Uri;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

public class AdnOAuthConnector 
{
	private OAuthConsumer _consumer;
	private OAuthProvider _provider;
	private AdnOAuthResponseHandler _responseHandler;
	
	public AdnOAuthConnector(
		String baseUrl,
		String consumerKey,
		String consumerSecret)
	{    
		String slash = (baseUrl.endsWith("/") ? "":"/");
		
	    String requestUrl = baseUrl + slash + "OAuth/RequestToken";
	    String accessUrl = baseUrl + slash + "OAuth/AccessToken";
	    String authorizeUrl = baseUrl + slash + "OAuth/Authorize?viewmode=mobile";
	    
		_consumer = new CommonsHttpOAuthConsumer(
				consumerKey, 
				consumerSecret);
        	    
	    _provider = new CommonsHttpOAuthProvider(
	    		requestUrl,
	    		accessUrl,
	    		authorizeUrl);
	
	    _provider.setOAuth10a(true);
	}
	
	public String getConsumerKey()
	{
		return _consumer.getConsumerKey();
	}
	
	public String getConsumerSecret()
	{
		return _consumer.getConsumerSecret();
	}
	
	public String getAccessToken()
	{
		return _consumer.getToken();
	}
	
	public String getAccessTokenSecret()
	{
		return _consumer.getTokenSecret();
	}
	
	public void signRequest(Object request) 
			throws 
			OAuthMessageSignerException, 
			OAuthExpectationFailedException, 
			OAuthCommunicationException
	{
		_consumer.sign(request);
	}
	
	public String getOauthUrlComponent(HttpGet request) 
			throws 
			OAuthMessageSignerException, 
			OAuthExpectationFailedException, 
			OAuthCommunicationException, 
			UnsupportedEncodingException
	{
		String urlComponent = "";

		signRequest(request);

		Header[] headers = request.getAllHeaders();

		String value = headers[0].getValue();

		String[] elements = value.substring(6).split(",");

		for(String element : elements)
		{
			urlComponent += "&" + URLDecoder.decode(element, "UTF-8");
		}

		return urlComponent.replace(" ", "").replace("\"", "");
	}
	
	public void doLogin(
		WebView webview, 
		AdnOAuthResponseHandler responseHandler) 
	{
		final WebView view = webview;
		
		_responseHandler = responseHandler;
		
	    initializeWebView(webview);
	    
		new Thread()
		{ 
			public void run() 
			{			
				try
				{
					String authorizeUrl = 
						_provider.getAuthorizationWebsiteUrl();
					
					String url = _provider.retrieveRequestToken(
						_consumer, 
						authorizeUrl);
					
					if(url!=null && !url.isEmpty())
					{  
						view.loadUrl(url);  
					}  
				}
				catch (Exception ex) 
				{ 
					Throwable cause = ex.getCause();
					_responseHandler.onError(ex);
				} 
			} 	  
		}.start();
	}

	void initializeWebView(final WebView webview)
	{
		webview.clearCache(true);

		webview.setWebViewClient(new WebViewClient()
		{
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) 
			{    
				if(url.contains("LogOn?ReturnUrl"))
    			{
            		_responseHandler.onLoginDisplayed();
    			}
				
				else if(url.contains("oauth_verifier"))
				{
					//authorization complete, hide the webview for now.
					webview.setVisibility(View.GONE);
					
					Uri uri = Uri.parse(url);
			        final String verifier = uri.getQueryParameter("oauth_verifier");
	
			        new Thread()
	 				{ 
			        	public void run() 
			 			{			
			 				try
			 				{	
								_provider.retrieveAccessToken(_consumer, verifier);
								
								_responseHandler.onSuccess(
									_consumer.getToken(),
									_consumer.getTokenSecret());
		 					} 
							catch (Exception ex) 
							{
								_responseHandler.onError(ex);
							} 
			 			}
	 				}.start();
				}
				
				return super.shouldOverrideUrlLoading(view, url);
			}
		});
	}
}
