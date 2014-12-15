package com.autodesk.adn.objLoader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Debug;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class MyRenderer extends GLSurfaceView implements Renderer 
{	
	private TDModel _model;
	
	/* Rotation values */
	private float xrot;					//X Rotation
	private float yrot;					//Y Rotation

	/* Rotation speed values */
	
	private float xspeed;				//X Rotation Speed ( NEW )
	private float yspeed;				//Y Rotation Speed ( NEW )
	
	private float z = 50.0f;
	
	private float oldX;
    private float oldY;
	private final float TOUCH_SCALE = 0.4f;		//Proved to be good for normal rotation ( NEW )
	
	public MyRenderer(Context ctx, TDModel model) 
	{
		super(ctx);
		
		_model = model;
		
		Debug.stopMethodTracing();
		this.setRenderer(this);
		this.requestFocus();
		this.setFocusableInTouchMode(true);
	}

	/**
	 * The Surface is created/init()
	 */
	public void onSurfaceCreated(GL10 gl, EGLConfig config) 
	{
		gl.glClearColor(102f/255.0f, 178.0f/255.0f, 255.0f/255.0f, 1.0f);
		
		gl.glEnable(GL10.GL_LIGHTING);	
		gl.glEnable(GL10.GL_LIGHT0);
		gl.glEnable(GL10.GL_COLOR_MATERIAL);	
		
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, 
				new float[] { 1.0f, 1.0f, 1.0f, 1.0f }, 0);
		
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, 
				new float[] { 0.0f, 0.0f, 0.0f, 1.0f }, 0);
		
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, 
				new float[] { 1.0f, 1.0f, 1.0f, 1.0f }, 0);
		
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, 
				new float[] { 0, 0, -5, 1 }, 0);

         gl.glClearStencil(0);
									
		gl.glShadeModel(GL10.GL_SMOOTH); 	
		gl.glEnable(GL10.GL_NORMALIZE);
			
		gl.glClearDepthf(1.0f); 					
		gl.glEnable(GL10.GL_DEPTH_TEST); 			
		gl.glDepthFunc(GL10.GL_LEQUAL); 		
	
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST); 
	}
	
	/**
	 * Here we do our drawing
	 */
	public void onDrawFrame(GL10 gl) 
	{
		gl.glClear(
			GL10.GL_COLOR_BUFFER_BIT | 
			GL10.GL_DEPTH_BUFFER_BIT | 
			GL10.GL_STENCIL_BUFFER_BIT);	
		
		gl.glLoadIdentity();		
		
		gl.glTranslatef(0.0f, -1.2f, -z);	
		gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f);	
		gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f);
		
		_model.draw(gl);					
		
		xrot += xspeed;
		yrot += yspeed;
		
		gl.glFlush();
	}

	/**
	 * If the surface changes, reset the view
	 */
	public void onSurfaceChanged(GL10 gl, int width, int height) 
	{
		if(height == 0) 
		{ 						
			//Prevent A Divide By Zero By
			height = 1; 						
		}

		gl.glViewport(0, 0, width, height); 	//Reset The Current Viewport
		gl.glMatrixMode(GL10.GL_PROJECTION); 	//Select The Projection Matrix
		gl.glLoadIdentity(); 					//Reset The Projection Matrix

		//Calculate The Aspect Ratio Of The Window
		GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 500.0f);

		gl.glMatrixMode(GL10.GL_MODELVIEW); 	//Select The Modelview Matrix
		gl.glLoadIdentity(); 					//Reset The Modelview Matrix
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		float x = event.getX();
        float y = event.getY();
        
        //If a touch is moved on the screen
        if(event.getAction() == MotionEvent.ACTION_MOVE) 
        {
        	//Calculate the change
        	float dx = x - oldX;
	        float dy = y - oldY;
        	//Define an upper area of 10% on the screen
        	int upperArea = this.getHeight() / 10;
        	
        	//Zoom in/out if the touch move has been made in the upper
        	if(y < upperArea) 
        	{
        		z -= dx * TOUCH_SCALE / 2;
        	} 
        	else
        	{        		
    	        xrot += dy * TOUCH_SCALE;
    	        yrot += dx * TOUCH_SCALE;
        	}        
        } 
        
        //Remember the values
        oldX = x;
        oldY = y;
        
        //We handled the event
		return true;
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) 
	{
		if(keyCode == KeyEvent.KEYCODE_DPAD_UP) 
		{
			z -= 3;
			
		} 
		else if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN) 
		{
			z += 3;	
		} 
		
		//We handled the event
		return true;
	}
}