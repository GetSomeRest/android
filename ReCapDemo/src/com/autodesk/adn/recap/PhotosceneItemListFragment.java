package com.autodesk.adn.recap;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.autodesk.adn.recap.client.ReCapResponseListener;
import com.autodesk.adn.recap.datacontracts.MeshFormatEnum;
import com.autodesk.adn.recap.datacontracts.ReCapError;
import com.autodesk.adn.recap.datacontracts.ReCapPhotoscene;
import com.autodesk.adn.recap.datacontracts.ReCapPhotosceneListResponse;
import com.autodesk.adn.recap.datacontracts.ReCapPhotosceneResponse;
import com.autodesk.adn.toolkit.networking.AdnDownloadListener;
import com.autodesk.adn.toolkit.networking.AdnDownloadManager;
import com.google.gson.reflect.TypeToken;

/**
 * A list fragment representing a list of PhotosceneItems. This fragment also
 * supports tablet devices by allowing list items to be given an 'activated'
 * state upon selection. This helps indicate which item is currently being
 * viewed in a {@link PhotosceneItemDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class PhotosceneItemListFragment extends ListFragment 
{
	SceneItemAdapter _adapter;
	
	AdnDownloadManager _downloadManager;
	
	HashMap <String, ReCapPhotoscene> _sceneMap = null;
	
	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * activated item position. Only used on tablets.
	 */
	private static final String STATE_ACTIVATED_POSITION = "activated_position";

	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private Callbacks _callbacks = _dummyCallbacks;

	/**
	 * The current activated item position. Only used on tablets.
	 */
	private int _activatedPosition = ListView.INVALID_POSITION;

	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of item
	 * selections.
	 */
	public interface Callbacks 
	{
		/**
		 * Callback for when an item has been selected.
		 */
		public void onItemSelected(ReCapPhotoscene scene);
	}

	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks _dummyCallbacks = new Callbacks() 
	{
		@Override
		public void onItemSelected(ReCapPhotoscene scene) 
		{
			
		}
	};

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public PhotosceneItemListFragment() 
	{
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		final Context ctx = getActivity();
		
		_downloadManager = new AdnDownloadManager(ctx);
		
		App.ReCapClient.getPhotosceneList(
			new ReCapResponseListener <ReCapPhotosceneListResponse> ()
			{
				@Override
				public void onSuccess(ReCapPhotosceneListResponse response)
				{
					if(!response.IsOk())
					{
						ReCapToolkit.displayErrorDialog(
							ctx, response.Error);
						
						return;
					}
					
					_sceneMap = new HashMap <String, ReCapPhotoscene>();
					
					ArrayList<SceneItem> items = 
						new ArrayList<SceneItem>();
					
					for(ReCapPhotoscene scene : response.getPhotoscenes())
					{
						if (!scene.Name.isEmpty())
						{
							items.add(new SceneItem(scene));
							
							_sceneMap.put(scene.PhotosceneId, scene);					
						}
					}
					
					_adapter = new SceneItemAdapter(
						getActivity(),
			            android.R.layout.simple_list_item_1, 
			            items);
					
					getActivity().runOnUiThread(new Runnable() 
					{
						@Override
						public void run() 
						{
							
							setListAdapter(_adapter);
						}
					});
				}

				@Override
				public void onError(ReCapError error) 
				{
					ReCapToolkit.displayErrorDialog(
						ctx, error);
				}

				@Override
				public Type getResponseType() 
				{
					return new TypeToken<ReCapPhotosceneListResponse>(){}.getType();
				}
			});
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
	    ListView lv = getListView(); 
	    
	    registerForContextMenu(lv);
	    
	    super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) 
	{
		super.onViewCreated(view, savedInstanceState);

		// Restore the previously serialized activated item position.
		if (savedInstanceState != null &&
			savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) 
		{
			setActivatedPosition(savedInstanceState
					.getInt(STATE_ACTIVATED_POSITION));
		}
	}

	@Override
	public void onAttach(Activity activity) 
	{
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) 
		{
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		_callbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() 
	{
		super.onDetach();

		// Reset the active callbacks interface to the dummy implementation.
		_callbacks = _dummyCallbacks;
	}

	@Override
	public void onListItemClick(
		ListView listView, 
		View view, 
		int position,
		long id) 
	{
		super.onListItemClick(listView, view, position, id);

		SceneItem item = _adapter.getItem(position);
		
		ReCapPhotoscene scene = _sceneMap.get(item.PhotosceneId);
		
		_callbacks.onItemSelected(scene);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) 
	{
		super.onSaveInstanceState(outState);
		
		if (_activatedPosition != ListView.INVALID_POSITION) 
		{
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, _activatedPosition);
		}
	}
	
	@Override
	public void onCreateContextMenu(
			ContextMenu menu, 
			View v, 
			ContextMenu.ContextMenuInfo menuInfo) 
	{
	    super.onCreateContextMenu(menu, v, menuInfo);

	    MenuInflater inflater = this.getActivity().getMenuInflater();
	    
	    inflater.inflate(R.menu.photoscene_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) 
	{
	    AdapterView.AdapterContextMenuInfo info = 
    		(AdapterView.AdapterContextMenuInfo) 
	    		item.getMenuInfo();
	    
	    SceneItem sceneItem = (SceneItem)
	    		getListAdapter().getItem(info.position);
	    
	    switch (item.getItemId()) 
	    {
	        case R.id.mesh: 
	        	
	        	loadMesh(sceneItem.PhotosceneId);
	        	return true;

	        default:
	            return super.onContextItemSelected(item);
	    }
	}
	

	/**
	 * Turns on activate-on-click mode. When this mode is on, list items will be
	 * given the 'activated' state when touched.
	 */
	public void setActivateOnItemClick(boolean activateOnItemClick) 
	{
		// When setting CHOICE_MODE_SINGLE, ListView will automatically
		// give items the 'activated' state when touched.
		getListView().setChoiceMode(
				activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
						: ListView.CHOICE_MODE_NONE);
	}

	private void setActivatedPosition(int position) 
	{
		if (position == ListView.INVALID_POSITION) 
		{
			getListView().setItemChecked(_activatedPosition, false);
		} 
		else 
		{
			getListView().setItemChecked(position, true);
		}

		_activatedPosition = position;
	}
	
	private void loadMesh(String photosceneId)
	{
		final Context ctx = getActivity();
		
		final ProgressDialog dlg = ReCapToolkit.displayWaitDialog(
			ctx,
			"Retrieving data",
			"Please wait ...");
		
		App.ReCapClient.getPhotosceneLink(
			photosceneId,
			MeshFormatEnum.Format.kObj,
			new ReCapResponseListener<ReCapPhotosceneResponse>()
			{
				@Override
				public void onSuccess(ReCapPhotosceneResponse response) 
				{
					if(!response.IsOk())
					{
						dlg.dismiss();
						
						ReCapToolkit.displayErrorDialog(
							ctx, response.Error);
						
						return;
					}
												
					if(response.Photoscene.SceneLink.isEmpty())
					{
						dlg.dismiss();
						
						return;
					}
					
					final String photosceneId = 
						response.Photoscene.PhotosceneId;
					
					_downloadManager.download(
						"ReCap Download",
						"Downloading scene ...",
						response.Photoscene.SceneLink,
						photosceneId + ".zip",
						DownloadManager.Request.VISIBILITY_HIDDEN,
						new AdnDownloadListener()
						{
							@Override
							public void onSuccess(String localUri) 
							{
								try 
								{
									File file = new File(localUri);
									
									String path = file.getParentFile().getAbsolutePath();
									
									// /storage/emulated/0/Download/photosceneId
									path += "/" + photosceneId;
									
									ReCapToolkit.unzip(
										localUri, 
										path) ;	
									
									file.delete();
									
									dlg.dismiss();
									
									Intent intent = new Intent(
										ctx,
										ViewerActivity.class);
									
									intent.putExtra(
										ViewerActivity.ARG_FILENAME, 
										path + "/mesh.obj");
									
									startActivity(intent);
								} 
								catch (IOException e) 
								{
									dlg.dismiss();
									e.printStackTrace();
								}
							}

							@Override
							public void onError() 
							{

							}										
						});									
				}

				@Override
				public void onError(ReCapError error)
				{
					dlg.dismiss();
					
					ReCapToolkit.displayErrorDialog(ctx, error);
				}

				@Override
				public Type getResponseType() 
				{
					return new TypeToken<ReCapPhotosceneResponse>(){}.getType();
				}	
			});
	}
	
	class SceneItem
	{
		public String Name;
		public String PhotosceneId;
		
		public SceneItem(ReCapPhotoscene scene)
		{			
			Name = scene.getDecodedName();
			
			PhotosceneId = scene.PhotosceneId;
		}
	}
	
	class SceneItemAdapter extends ArrayAdapter<SceneItem> 
	{
		public SceneItemAdapter(
			Context context, 
			int id, 
			ArrayList<SceneItem> items) 
		{
	        super(context, id, items);
		}

        @Override
        public View getView(
    		int position, 
    		View convertView,
            ViewGroup parent) 
    	{
        	SceneItem item = super.getItem(position);
        	
        	View view = super.getView(position, convertView, parent);

        	TextView textView = (TextView) view.findViewById(
    			android.R.id.text1);

        	textView.setTextColor(Color.BLACK);
        	
        	textView.setText(item.Name);

        	return view;
    	}
    }
}

















