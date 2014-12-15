package com.autodesk.adn.recap.datacontracts;

import com.google.gson.annotations.SerializedName;

public class ReCapPhotoscenePropsResponse 
	extends ReCapResponseBase
{
	@SerializedName("Photoscenes")
	public ReCapPhotosceneContainer Photoscenes;
	
	public ReCapPhotoscene getPhotoscene()
	{
		return Photoscenes.Photoscene;
	}
}

class ReCapPhotosceneContainer
{
	@SerializedName("Photoscene")
    public ReCapPhotoscene Photoscene;
}