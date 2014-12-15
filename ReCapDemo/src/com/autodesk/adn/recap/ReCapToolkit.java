package com.autodesk.adn.recap;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.autodesk.adn.recap.datacontracts.ReCapError;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

public class ReCapToolkit 
{
	public static void displayErrorDialog(
		Context ctx, 
		ReCapError error)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		
		builder.setCancelable(false);
	    builder.setTitle("ReCap Error");
	    builder.setIcon(R.drawable.ic_launcher);
	    builder.setMessage(error.getLocalizedMessage());
	   	    
	    builder.setPositiveButton("OK",
    		new DialogInterface.OnClickListener() 
	    {
	        public void onClick(DialogInterface dialog, int id) 
	        {	
	            dialog.cancel();
	        }
	    });
	
	    AlertDialog dlg = builder.create();
	    dlg.show();
	}
	
	public static ProgressDialog displayLoginDialog(Context ctx)
	{
		ProgressDialog dlg = ProgressDialog.show(
			ctx, 
			"Connecting Autodesk",
		    "Please Wait...", 
		    true);
			
		dlg.setIcon(R.drawable.ic_launcher);
		
		return dlg;
	}
	
	public static ProgressDialog displayWaitDialog(
		Context ctx,
		String title,
		String msg)
	{
		ProgressDialog dlg = ProgressDialog.show(
			ctx, 
			title,
		    msg, 
		    true);
			
		dlg.setIcon(R.drawable.ic_launcher);
		
		return dlg;
	}
	
	public static void unzip(
		String zipFile, 
		String location) 
		throws IOException 
	{		
		int BUFFER_SIZE = 2048;
		
	    int size;
	    byte[] buffer = new byte[BUFFER_SIZE];

	    try 
	    {
	       location += (location.endsWith("/") ? "" :"/");
	       
	        File f = new File(location);
	        
	        if(!f.isDirectory()) 
	        {
	            f.mkdirs();
	        }
	        
	        ZipInputStream zin = new ZipInputStream(
        		new BufferedInputStream(
    				new FileInputStream(zipFile), 
    				BUFFER_SIZE));
	        
	        try 
	        {
	            ZipEntry ze = null;
	            
	            while ((ze = zin.getNextEntry()) != null) 
	            {
	                String path = location + ze.getName();
	                
	                File unzipFile = new File(path);

	                if (ze.isDirectory())
	                {
	                    if(!unzipFile.isDirectory())
	                    {
	                        unzipFile.mkdirs();
	                    }
	                }
	                else 
	                {
	                    // check for and create parent directories if they don't exist
	                    File parentDir = unzipFile.getParentFile();
	                    
	                    if (null != parentDir) 
	                    {
	                        if (!parentDir.isDirectory())
	                        {
	                            parentDir.mkdirs();
	                        }
	                    }

	                    // unzip the file                
	                    BufferedOutputStream fout = 
                    		new BufferedOutputStream(
                				new FileOutputStream(
            						unzipFile,
            						false), 
        						BUFFER_SIZE);
	                    
	                    try 
	                    {
	                        while ( (size = zin.read(buffer, 0, BUFFER_SIZE)) != -1 ) 
	                        {
	                            fout.write(buffer, 0, size);
	                        }

	                        zin.closeEntry();
	                    }
	                    finally 
	                    {
	                        fout.flush();
	                        fout.close();
	                    }
	                }
	            }
	        }
	        finally 
	        {
	            zin.close();
	        }
	    }
	    catch (Exception ex) 
	    {
	        Log.e("", "Unzip exception", ex);
	    }
	}
}







