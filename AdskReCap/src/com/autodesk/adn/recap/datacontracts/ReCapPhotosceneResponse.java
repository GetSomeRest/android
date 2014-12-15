package com.autodesk.adn.recap.datacontracts;

import com.google.gson.annotations.SerializedName;

public class ReCapPhotosceneResponse 
	extends ReCapResponseBase
{
	@SerializedName("Photoscene")
    public ReCapPhotoscene Photoscene;
}
