package com.autodesk.adn.recap.datacontracts;

import java.util.ArrayList;

public class ReCapPhotosceneListResponse 
	extends ReCapResponseBase
{
	public PhotosceneCollection Photoscenes;
	
	public ArrayList<ReCapPhotoscene> getPhotoscenes()
	{
		return Photoscenes.Photoscene;
	}
}

class PhotosceneCollection 
{
    public ArrayList<ReCapPhotoscene> Photoscene;
} 
