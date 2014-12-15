package com.autodesk.adn.recap.datacontracts;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ReCapPhotoscene implements Parcelable
{
	@SerializedName("name")
	public String Name;
	
	@SerializedName("photosceneid")
	public String PhotosceneId;
	
	@SerializedName("ProgressMsg")
	public String ProgressMsg;
	
	@SerializedName("userID")
	public String UserId;
	
	@SerializedName("convertStatus")
	public String ConvertStatus;
	
	@SerializedName("convertFormat")
	public String ConvertFormat;
	
	
	
	@SerializedName("meshQuality")
	public int MeshQuality;
	
	@SerializedName("nb3Dpoints")
	public int Nb3dPoints;
	
	@SerializedName("nbfaces")
	public int NbFaces;
	
	@SerializedName("nbvertices")
	public int NbVertices;
	
	@SerializedName("nbShots")
	public int NbShots;
	
	@SerializedName("nbStitchedShots")
	public int NbStitchedShots;
	
	
	@SerializedName("scenelink")
	public String SceneLink;
	
	//@SerializedName("deleted")
	//public String Deleted;
	
	public String getDecodedName()
	{
		try 
		{
			return URLDecoder.decode(Name, "UTF-8");
		} 
		catch (UnsupportedEncodingException e) 
		{
			return "";
		}
	}
	
	public ReCapPhotoscene()
	{
		
	}

	private ReCapPhotoscene(Parcel in) 
	{
	    Name = in.readString();
	    PhotosceneId = in.readString();
	    ProgressMsg = in.readString();
	    UserId = in.readString();
	    ConvertStatus = in.readString();
	    ConvertFormat = in.readString();

	    MeshQuality = in.readInt();
	    Nb3dPoints = in.readInt();
	    NbFaces = in.readInt();
	    NbVertices = in.readInt();
	    NbShots = in.readInt();
	    NbStitchedShots = in.readInt();
	} 
	
	@Override
	public int describeContents() 
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) 
	{
		dest.writeString(Name);
	    dest.writeString(PhotosceneId);
	    dest.writeString(ProgressMsg);
	    dest.writeString(UserId);
	    dest.writeString(ConvertStatus);
	    dest.writeString(ConvertFormat);
	    
	    dest.writeInt(MeshQuality);
	    dest.writeInt(Nb3dPoints);
	    dest.writeInt(NbFaces);
	    dest.writeInt(NbVertices);
	    dest.writeInt(NbShots);
	    dest.writeInt(NbStitchedShots);
	}
	
	public static final Parcelable.Creator<ReCapPhotoscene> CREATOR = 
		new Parcelable.Creator<ReCapPhotoscene>() 
	{
		public ReCapPhotoscene createFromParcel(Parcel in) 
		{
			return new ReCapPhotoscene(in);
		}

	    public ReCapPhotoscene[] newArray(int size) 
	    {
	    	return new ReCapPhotoscene[size];
	    }
	};
	
	 /*
     Progress = progress;
     SceneLink = sceneLink;
     FileSize = fileSize;
     MeshQuality = meshQuality;
     ConvertFormat = convertFormat;
     ConvertStatus = convertStatus;
     ProcessingTime = processingTime;
     Deleted = deleted;
     Files = files;   */
}
