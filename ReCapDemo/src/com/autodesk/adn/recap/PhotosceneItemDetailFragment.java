package com.autodesk.adn.recap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.autodesk.adn.recap.datacontracts.ReCapPhotoscene;


/**
 * A fragment representing a single PhotosceneItem detail screen. This fragment
 * is either contained in a {@link PhotosceneItemListActivity} in two-pane mode
 * (on tablets) or a {@link PhotosceneItemDetailActivity} on handsets.
 */
public class PhotosceneItemDetailFragment extends Fragment 
{
	public static final String ARG_PHOTOSCENE = "photoscene";

	private ReCapPhotoscene _scene;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public PhotosceneItemDetailFragment()
	{
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_PHOTOSCENE)) 
		{
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			
			_scene = (ReCapPhotoscene) getArguments().getParcelable(
				ARG_PHOTOSCENE);
		}
	}
	
	@Override
	public View onCreateView(
		LayoutInflater inflater, 
		ViewGroup container,
		Bundle savedInstanceState) 
	{
		View rootView = inflater.inflate(
			R.layout.fragment_photosceneitem_detail, 
			container, 
			false);

		if (_scene != null) 
		{
			//((TextView) rootView.findViewById(R.id.photosceneitem_detail))
			//		.setText();
			
			LinearLayout layout = (LinearLayout)rootView.findViewById(
				R.id.detail_layout);
			
			layout.addView(
				new ScenePropertyView(
					getActivity(),
					"Scene Name: ", 
					_scene.getDecodedName()));
			
			layout.addView(
				new ScenePropertyView(
					getActivity(),
					"Photoscene Id: ", 
					_scene.PhotosceneId));
			
			layout.addView(
				new ScenePropertyView(
					getActivity(),
					"Convert Format: ", 
					_scene.ConvertFormat));
			
			layout.addView(
				new ScenePropertyView(
					getActivity(),
					"Convert Status: ", 
					_scene.ConvertStatus));
			
			
			layout.addView(
				new ScenePropertyView(
					getActivity(),
					"Nb 3d Points: ", 
					_scene.Nb3dPoints));
			
			layout.addView(
				new ScenePropertyView(
					getActivity(),
					"Nb Faces: ", 
					_scene.NbFaces));
			
			layout.addView(
				new ScenePropertyView(
					getActivity(),
					"Nb Shots: ", 
					_scene.NbShots));
			
			layout.addView(
				new ScenePropertyView(
					getActivity(),
					"Nb Stitched Shots: ", 
					_scene.NbStitchedShots));
		}

		return rootView;
	}
	
	/*void getSceneProperties()
	{
		for(String id : _sceneMap.keySet())
		{
			App.ReCapClient.getPhotosceneProperties(id,
				new ReCapResponseListener<ReCapPhotoscenePropsResponse> ()
				{
					@Override
					public void onSuccess(ReCapPhotoscenePropsResponse response) 
					{
						
					}

					@Override
					public void onError(ReCapError error) 
					{
					
					}

					@Override
					public Type getResponseType() {
						return new TypeToken<ReCapPhotoscenePropsResponse>(){}.getType();
					}
				});
		}
	}*/
}

























