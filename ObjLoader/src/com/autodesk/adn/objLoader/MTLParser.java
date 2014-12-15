package com.autodesk.adn.objLoader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import android.util.Log;

public class MTLParser 
{
	public static Vector<Material> loadMTL(String file)
	{
		String line;
		Material currentMtl = null;
		BufferedReader reader = null;	
		Vector<Material> materials = new Vector<Material>();
		
		try 
		{
			// try to open file
			reader = new BufferedReader(
				new InputStreamReader(
					new FileInputStream(file)));

			if(reader!=null)
			{
				//try to read lines of the file
				while((line = reader.readLine()) != null) 
				{
					Log.i("MLT", line);
					
					if(line.startsWith("newmtl"))
					{
						String name=line.split("[ ]+",2)[1];
						currentMtl=new Material(name);	
						
						materials.add(currentMtl);
					}
					else if(line.startsWith("Ka"))
					{
						String[] str=line.split("[ ]+");
						
						currentMtl.setAmbientColor(
							Float.parseFloat(str[1]), 
							Float.parseFloat(str[2]), 
							Float.parseFloat(str[3]));
					}
					else if(line.startsWith("Kd"))
					{
						String[] str=line.split("[ ]+");
						
						currentMtl.setDiffuseColor(
							Float.parseFloat(str[1]), 
							Float.parseFloat(str[2]), 
							Float.parseFloat(str[3]));
					}
					else if(line.startsWith("Ks"))
					{
						String[] str=line.split("[ ]+");
						
						currentMtl.setSpecularColor(
							Float.parseFloat(str[1]),
							Float.parseFloat(str[2]),
							Float.parseFloat(str[3]));
					}
					else if(line.startsWith("Tr") || line.startsWith("d"))
					{
						String[] str=line.split("[ ]+");
						currentMtl.setAlpha(Float.parseFloat(str[1]));
					}
					else if(line.startsWith("Ns"))
					{
						String[] str=line.split("[ ]+");
						currentMtl.setShine(Float.parseFloat(str[1]));
					}
					else if(line.startsWith("illum"))
					{
						String[] str=line.split("[ ]+");
						currentMtl.setIllum(Integer.parseInt(str[1]));
					}
					else if(line.startsWith("map_Kd"))
					{
						String[] str=line.split("[ ]+");
						currentMtl.setTextureFile(str[1]);
					}
				}
				
				reader.close();
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return materials;
	}
}
