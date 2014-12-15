package com.autodesk.adn.recap;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ScenePropertyView extends LinearLayout
{
	public ScenePropertyView(Context context, String name, String value) 
	{
	    super(context);
	    
	    initialize();
	    
	    setName(name);
		setValue(value);
	}
	
	public ScenePropertyView(Context context, String name, int value) 
	{
	    super(context);
	    
	    initialize();
	    
	    setName(name);
		setValue(new Integer(value).toString());
	}
	
	public ScenePropertyView(Context context, AttributeSet attrs) 
	{
	    super(context, attrs);
	    initialize();
	}
	
	public ScenePropertyView(Context context, AttributeSet attrs, int defStyle) 
	{
	    super(context, attrs, defStyle);
	    initialize();
	}
	
	private void initialize()
	{
		LayoutInflater inflater = (LayoutInflater) 
			getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		
	    inflater.inflate(R.layout.scene_property, this, true);
	}	
	
	public void setName(String name)
	{
		TextView tv = (TextView)findViewById(
			R.id.scene_property_name);
		
		tv.setText("   " + name);
	}
	
	public void setValue(String value)
	{
		TextView tv = (TextView)findViewById(
			R.id.scene_property_value);
		
		tv.setText(value);
	}
}








