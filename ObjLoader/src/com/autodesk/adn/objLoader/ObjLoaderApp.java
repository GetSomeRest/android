package com.autodesk.adn.objLoader;

import android.app.Activity;
import android.os.Bundle;
import android.os.Debug;

/**
 * The initial Android Activity, setting and initiating
 * the OpenGL ES Renderer Class @see Lesson02.java
 * 
 * @author Savas Ziplies (nea/INsanityDesign)
 */
public class ObjLoaderApp extends Activity 
{
	/** The OpenGL View */


	/**
	 * Initiate the OpenGL View and set our own
	 * Renderer (@see Lesson02.java)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		Debug.startMethodTracing("calc");
		super.onCreate(savedInstanceState);
		
		TDModel model = OBJParser.parseOBJ(			
			"//mnt/sdcard/Download/KwMNxQZ4ePIXip9IJeXT7yejDGA/mesh.obj");
		
		//TDModel model = new Cube();
		
		setContentView(
			new MyRenderer(this,
			model));
	}

	/**
	 * Remember to resume the glSurface
	 */
	@Override
	protected void onResume() {
		super.onResume();
	}

	/**
	 * Also pause the glSurface
	 */
	@Override
	protected void onPause() {
		Debug.stopMethodTracing();
		super.onPause();
	}
	@Override
	protected void onDestroy(){
		Debug.stopMethodTracing();
	}

}