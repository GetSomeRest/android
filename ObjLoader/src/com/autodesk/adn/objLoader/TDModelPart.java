package com.autodesk.adn.objLoader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Vector;

public class TDModelPart 
{
	Vector<Short> faces;
	Vector<Short> vtPointer;
	Vector<Short> vnPointer;
	
	Material material;
	
	FloatBuffer normalBuffer;
	ShortBuffer faceBuffer;
	
	public TDModelPart(
		Vector<Float> vertices,
		Vector<Short> faces, 
		Vector<Short> vtPointer,
		Vector<Short> vnPointer, 
		Material material, 
		Vector<Float> vn) 
	{
		super();
		
		this.faces = faces;
		this.vtPointer = vtPointer;
		this.vnPointer = vnPointer;
		this.material = material;
		
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(
			faces.size() * Float.SIZE/8 * 3 * 3);
		
		byteBuf.order(ByteOrder.nativeOrder());
		normalBuffer = byteBuf.asFloatBuffer();
		
		if(vnPointer.size() != 0)
		{
			for(int i=0; i<vnPointer.size(); i++)
			{
				float x = vn.get(vnPointer.get(i)*3);
				float y = vn.get(vnPointer.get(i)*3+1);
				float z = vn.get(vnPointer.get(i)*3+2);
				
				normalBuffer.put(x);
				normalBuffer.put(y);
				normalBuffer.put(z);
			}
		}
		else
		{
			computeNormals(vertices);
		}
		
		normalBuffer.position(0);
		
		ByteBuffer fBuf = ByteBuffer.allocateDirect(faces.size() * 2);
		fBuf.order(ByteOrder.nativeOrder());
		
		faceBuffer = fBuf.asShortBuffer();
		faceBuffer.put(toPrimitiveArrayS(faces));
		faceBuffer.position(0);
	}
	
	void computeNormals(Vector<Float> vertices)
	{
		for(int i=0; i<faces.size(); i+=3)
		{
			int idx1 = 3 * faces.get(i);
			int idx2 = 3 * faces.get(i+1);
			int idx3 = 3 * faces.get(i+2);
			
			Vector3d v1 = new Vector3d(
				vertices.get(idx1),
				vertices.get(idx1 + 1),
				vertices.get(idx1 + 2));
					
			Vector3d v2 = new Vector3d(
				vertices.get(idx2),
				vertices.get(idx2 + 1),
				vertices.get(idx2 + 2));
			
			Vector3d v3 = new Vector3d(
				vertices.get(idx3),
				vertices.get(idx3 + 1),
				vertices.get(idx3 + 2));
			
			Vector3d dir1 = v2.substract(v1);
			
			Vector3d dir2 = v3.substract(v1);
			
			Vector3d n = dir1.crossP(dir2);
		
			normalBuffer.put(n.x);
			normalBuffer.put(n.y);
			normalBuffer.put(n.z);
			
			normalBuffer.put(n.x);
			normalBuffer.put(n.y);
			normalBuffer.put(n.z);
			
			normalBuffer.put(n.x);
			normalBuffer.put(n.y);
			normalBuffer.put(n.z);
		}
	}
	
	public String toString()
	{
		String str=new String();
		if(material!=null)
			str+="Material name:"+material.getName();
		else
			str+="Material not defined!";
		str+="\nNumber of faces:"+faces.size();
		str+="\nNumber of vnPointers:"+vnPointer.size();
		str+="\nNumber of vtPointers:"+vtPointer.size();
		return str;
	}
	
	public ShortBuffer getFaceBuffer()
	{
		return faceBuffer;
	}
	
	public FloatBuffer getNormalBuffer()
	{
		return normalBuffer;
	}
	
	private static short[] toPrimitiveArrayS(Vector<Short> vector)
	{
		short[] s = new short[vector.size()];
		
		for (int i=0; i<vector.size(); i++)
		{
			s[i]=vector.get(i);
		}
		
		return s;
	}
	
	public int getFacesCount()
	{
		return faces.size();
	}
	
	public Material getMaterial()
	{
		return material;
	}
}













