package com.autodesk.adn.recap;

import com.autodesk.adn.objLoader.MyRenderer;
import com.autodesk.adn.objLoader.OBJParser;
import com.autodesk.adn.objLoader.TDModel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;

public class ViewerActivity extends Activity 
{
	public static final String ARG_FILENAME = "filename";
	
	MyRenderer _renderer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		Debug.startMethodTracing("calc");
		
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		
		String filename = intent.getStringExtra(
				ViewerActivity.ARG_FILENAME);
		
		ProgressDialog dlg = ReCapToolkit.displayWaitDialog(
			this,
			"Loading model",
			"Please wait ...");
		
		AsyncLoaderTask task = new AsyncLoaderTask(this);
		
		task.setProgressDialog(dlg);
		
		task.execute(filename);
	}	

	@Override
	public void onConfigurationChanged(Configuration newConfig) 
	{
	    super.onConfigurationChanged(newConfig); 
	}
	
	public class AsyncLoaderTask 
		extends AsyncTask<String, Void, Void>
	{
		private Activity _context;
		
		private ProgressDialog _dlg;
		
		private TDModel _model;
		
		public AsyncLoaderTask(Activity context)
		{
			_dlg = null;
			
			_context = context;
		}
		
		public void setProgressDialog(ProgressDialog dlg)
		{
			_dlg = dlg;
		}
				
		@Override
		protected void onPreExecute() 
		{
			if(_dlg != null)
				_dlg.show();
		}
		
		@Override
		protected Void doInBackground(String... params) 
		{
			try 
			{
				String filename = params[0];
				
				_model = OBJParser.parseOBJ(filename);
			} 
			catch (Exception ex) 
			{
				ex.printStackTrace();
			}
			
			return null;
		}
	
		@Override
		protected void onPostExecute(Void result) 
		{
			if(_dlg != null)
				if(_dlg.isShowing())
					_dlg.dismiss();
			
			_renderer = new MyRenderer(
				_context, _model);
			
			_context.setContentView(_renderer);
		}
	}
}
