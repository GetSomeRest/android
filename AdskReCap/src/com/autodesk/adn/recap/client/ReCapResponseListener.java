package com.autodesk.adn.recap.client;

import java.lang.reflect.Type;

import com.autodesk.adn.recap.datacontracts.ReCapError;

public interface ReCapResponseListener <T>
{
	public void onSuccess(T response);

	public void onError(ReCapError error);
	
	public Type getResponseType();
}
