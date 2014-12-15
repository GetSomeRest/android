package com.autodesk.adn.toolkit.networking;

public interface AdnDownloadListener
{
	public void onSuccess(String localUri);

	public void onError();
}