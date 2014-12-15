package com.autodesk.adn.recap;

import com.autodesk.adn.oauth.AdnOAuthConnector;
import com.autodesk.adn.oauth.AdnOAuthResponseHandler;
import com.autodesk.adn.recap.R;
import com.autodesk.adn.recap.client.AdnReCapClient;
import com.autodesk.adn.toolkit.networking.AdnNetworkUtils;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.Menu;
import android.webkit.WebView;
import android.widget.Toast;

public class OAuthLoginActivity extends Activity 
{
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onResume() 
	{	
		super.onResume();    
		
		if(!AdnNetworkUtils.isNetworkAvailable(this))
		{
			// no internet connection...
			setContentView(R.layout.login_failed);
			return;
		}

		setContentView(R.layout.oauth_login);

		WebView webView = (WebView) findViewById(R.id.webView);
		
		App.Connector = new AdnOAuthConnector(
			UserSettings.OAUTH_URL,
			UserSettings.CONSUMER_KEY,
			UserSettings.CONSUMER_SECRET);
		
		final ProgressDialog dlg = 
			ReCapToolkit.displayLoginDialog(this);
		
		AdnOAuthResponseHandler responseHandler = new AdnOAuthResponseHandler()
		{
			@Override
			public void onSuccess(
					String accessToken, 
					String accessTokenSecret) 
			{
				try
				{		
					Context ctx = getApplicationContext();
					
					App.ReCapClient = new AdnReCapClient(
						ctx,
						App.Connector,
						UserSettings.RECAP_URL,
						UserSettings.RECAP_CLIENTID);
					
					Intent intent = new Intent(
						ctx,
						PhotosceneItemListActivity.class);
					
					startActivity(intent);
				}
				catch(Exception ex)
				{

				}
			}
			
			@Override
			public void onLoginDisplayed()
			{
				dlg.dismiss();	
			}	
					
			@Override
			public void onError(
				Exception ex) 
			{
				final Exception _ex = ex;
				
				runOnUiThread(new Runnable() 
				{
					@Override
					public void run() 
					{
						Toast.makeText(
							getApplicationContext(), 
							"Failed to connect with Autodesk...", 
							Toast.LENGTH_LONG).show();
						
						dlg.dismiss();	
						
						WebView webView = (WebView) findViewById(R.id.webView);
						
						webView.loadData(_ex.getLocalizedMessage(), "text/html; charset=UTF-8", null);
					}
				});
			}
		};
		
		App.Connector.doLogin(webView, responseHandler);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) 
	{
	    super.onConfigurationChanged(newConfig); 
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}