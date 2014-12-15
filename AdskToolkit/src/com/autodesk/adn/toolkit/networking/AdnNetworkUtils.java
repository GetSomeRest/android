package com.autodesk.adn.toolkit.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class AdnNetworkUtils 
{
	public static boolean isNetworkAvailable(Context ctx) 
	{
		ConnectivityManager cm = 
			(ConnectivityManager) ctx.getSystemService(
					Context.CONNECTIVITY_SERVICE);

		NetworkInfo[] netInfo = cm.getAllNetworkInfo();

		for (NetworkInfo ni : netInfo) 
		{
			String name = ni.getTypeName();

			if (name.equalsIgnoreCase("ETH"))
				if (ni.isConnected())
					return true;

			if (name.equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
					return true;

			if (name.equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					return true;
		}

		return false;
	}
	
	public static String getJsonString(org.apache.http.HttpResponse response) 
	{ 
		InputStream is = null;
		StringBuilder sb = null;
		
		try 
		{ 
			is = response.getEntity().getContent(); 
		       
			BufferedReader reader = new BufferedReader(
				new InputStreamReader(is)); 
			
			sb = new StringBuilder(); 

			String line = null; 
			
			while ((line = reader.readLine()) != null) 
			{ 
				sb.append(line + "\n"); 
			} 
		} 
		catch (IOException e) 
		{ 
			e.printStackTrace(); 
		} 
		finally 
		{ 
			try 
			{ 
				is.close(); 
			} 
			catch (IOException e) 
			{ 
				e.printStackTrace(); 
			} 
		} 

		return sb.toString(); 
	}
}
