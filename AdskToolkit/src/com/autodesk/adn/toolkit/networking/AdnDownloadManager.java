package com.autodesk.adn.toolkit.networking;

import java.util.HashMap;

import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

public class AdnDownloadManager 
{
	Context _context;
	
	DownloadManager _manager;
	
	BroadcastReceiver _receiver;
	
	HashMap<Long, AdnDownloadListener> _listeners;
	
	public AdnDownloadManager(Context context)
	{
		_context = context;
		
		_manager = (DownloadManager) 
			context.getSystemService(
					Context.DOWNLOAD_SERVICE);
		
		_listeners = new HashMap<Long, AdnDownloadListener>();
		
		_receiver = new BroadcastReceiver() 
		{
            @Override
            public void onReceive(Context context, Intent intent) 
            {
                String action = intent.getAction();
                
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) 
                {
                    long downloadId = intent.getLongExtra(
                        DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                    
                    Query query = new Query();
                    
                    query.setFilterById(downloadId);
                    
                    Cursor cursor = _manager.query(query);
                    
                    if (cursor.moveToFirst()) 
                    {
                        int columnIndex = cursor.getColumnIndex(
                    		DownloadManager.COLUMN_STATUS);
                        
                        if (DownloadManager.STATUS_SUCCESSFUL == 
                    		cursor.getInt(columnIndex)) 
                        {
                            String localUri = cursor.getString(
                        		cursor.getColumnIndex(
                    				DownloadManager.COLUMN_LOCAL_URI));
                            
                            if(_listeners.containsKey(downloadId))
                            {
                            	localUri = localUri.substring(7);
                            	
                            	_listeners.get(downloadId).onSuccess(localUri);
                            	_listeners.remove(downloadId);
                            }
                        }
                    }
                }
            }
        };
 
        _context.registerReceiver(
    		_receiver, 
    		new IntentFilter(
				DownloadManager.ACTION_DOWNLOAD_COMPLETE));
	}

	public void download(
		String title,
		String desc,
		String url, 
		String filename,
		int visibility,
		AdnDownloadListener listener)
	{
		DownloadManager.Request request = 
			new DownloadManager.Request(Uri.parse(url));
		
		request.setTitle(title);
		request.setDescription(desc);
		request.allowScanningByMediaScanner();
		
		request.setNotificationVisibility(visibility);
		
		request.setDestinationInExternalPublicDir(
			Environment.DIRECTORY_DOWNLOADS, 
			filename);	
		
		long downloadId = _manager.enqueue(request);
		
		_listeners.put(downloadId, listener);
	}
}


