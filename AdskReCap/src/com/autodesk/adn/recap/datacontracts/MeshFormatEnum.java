package com.autodesk.adn.recap.datacontracts;

public class MeshFormatEnum 
{
	public enum Format
	{
	    k3dp,
	    kFbx,
	    kIpm,
	    kLas,
	    kObj,
	    kFysc,
	    kRcs,
	    kRcm,
	    kInvalid
	}
	
	public static String ToReCapString(Format value)
    {
        switch (value)
        {
            case k3dp:
                return "3dp";

            case kFbx:
                return "fbx";

            case kFysc:
                return "fysc";

            case kIpm:
                return "ipm";

            case kLas:
                return "las";

            case kObj:
                return "obj";

            case kRcm:
                return "rcm";

            case kRcs:
                return "rcs";

            case kInvalid:
                return "";

            default:
                return "3dp";
        }
    }	

    public static Format FromReCapString(String value)
    {
        String str = value.toLowerCase();
        
        if(str =="3dp")
        	return Format.k3dp;
        
        else if(str =="fbx")
        	return Format.kFbx;
        
        else if(str =="fysc")
        	return Format.kFysc;
        
        else if(str =="ipm")
        	return Format.kIpm;
        
        else if(str =="las")
        	return Format.kLas;
        
        else if(str =="obj")
        	return Format.kObj;
        
        else if(str =="rcm")
        	return Format.kRcm;
        
        else if(str =="rcs")
        	return Format.kRcs;
        
        else
        	return Format.kInvalid;
    }
}