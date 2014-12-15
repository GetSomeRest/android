package com.autodesk.adn.objLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;
import android.util.Log;

public class OBJParser 
{
	Vector<Short> faces=new Vector<Short>();
	Vector<Short> vtPointer=new Vector<Short>();
	Vector<Short> vnPointer=new Vector<Short>();
	
	Vector<Float> v=new Vector<Float>();
	Vector<Float> vn=new Vector<Float>();
	Vector<Float> vt=new Vector<Float>();
	
	Vector<TDModelPart> parts=new Vector<TDModelPart>();
	
	Vector<Material> materials = null;

	private OBJParser()
	{

	}

	public static TDModel parseOBJ(String fileName) 
	{
		OBJParser parser = new OBJParser();
		
		BufferedReader reader=null;
		String line = null;
		Material m = null;

		try 
		{
			reader = new BufferedReader(
				new InputStreamReader(
					new FileInputStream(fileName)));
			
			//try to read lines of the file
			while((line = reader.readLine()) != null) 
			{
				Log.v("obj",line);
				
				if(line.startsWith("f"))
				{
					//a polygonal face
					parser.processFLine(line);
				}
				else if(line.startsWith("vn"))
				{
					parser.processVNLine(line);
				}
				else if(line.startsWith("vt"))
				{
					parser.processVTLine(line);
				}
				else if(line.startsWith("v"))
				{
					//line having geometric position of single vertex
					parser.processVLine(line);
				}
				else if(line.startsWith("usemtl"))
				{
					try
					{
						//start of new group
						if(parser.faces.size()!=0)
						{
							//if not this is not the start of the first group
							TDModelPart model = new TDModelPart(
								parser.v,
								parser.faces, 
								parser.vtPointer, 
								parser.vnPointer, 
								m, 
								parser.vn);
							
							parser.parts.add(model);
						}
						
						m = null;
						
						//get the name of the material
						String mtlName = line.split("[ ]+",2)[1]; 
						
						for(int i=0; i<parser.materials.size(); i++)
						{
							if(parser.materials.get(i).getName().equals(mtlName))
							{
								//if found, return from loop
								m = parser.materials.get(i);
								break;
							}
						}
						
						parser.faces=new Vector<Short>();
						parser.vtPointer=new Vector<Short>();
						parser.vnPointer=new Vector<Short>();
					}
					catch (Exception e) 
					{
						// TODO: handle exception
						e.printStackTrace();
					}
				}
				else if(line.startsWith("mtllib"))
				{
					File file = new File(fileName);
					
					String matPath = file.getParent() + "/" +
						line.split("[ ]+")[1];
					
					File matFile = new File(matPath);
					
					if(matFile.exists())
					{
						parser.materials = MTLParser.loadMTL(matPath);
						
						for(int i=0; i<parser.materials.size(); i++)
						{
							Material mat=parser.materials.get(i);
							Log.v("materials", mat.toString());
						}
					}
				}
			}
			
			reader.close();			
		} 		
		catch(IOException e)
		{
			System.out.println("wtf...");
		}

		if(parser.faces != null)
		{
			//if not this is not the start of the first group
			TDModelPart model = new TDModelPart(
				parser.v,
				parser.faces, 
				parser.vtPointer, 
				parser.vnPointer, 
				m, 
				parser.vn);
			
			parser.parts.add(model);
		}
		
		TDModel model = new TDModel(parser.v, parser.vn, parser.vt, parser.parts);
		
		Log.v("models", model.toString());
		return model;
	}

	private void processVLine(String line)
	{
		String [] tokens=line.split("[ ]+");

		for(int i=1; i<tokens.length; i++)
		{ 
			v.add(Float.valueOf(tokens[i]));
		}
	}
	
	private void processVNLine(String line){
		
		String [] tokens=line.split("[ ]+"); 
		
		for(int i=1; i<tokens.length; i++)
		{
			vn.add(Float.valueOf(tokens[i]));
		}
	}
	
	private void processVTLine(String line)
	{
		String [] tokens=line.split("[ ]+"); 
		
		for(int i=1; i<tokens.length; i++)
		{ 
			vt.add(Float.valueOf(tokens[i]));
		}
	}
	
	private void processFLine(String line)
	{
		String [] tokens=line.split("[ ]+");
		int c=tokens.length;

		if(tokens[1].matches("[0-9]+"))
		{
			//f: v
			if(c==4)
			{
				//3 faces
				for(int i=1; i<c; i++)
				{
					Short s=Short.valueOf(tokens[i]);
					s--;
					faces.add(s);
				}
			}
			else
			{
				//more faces
				Vector<Short> polygon = new Vector<Short>();
				
				for(int i=1; i<tokens.length; i++)
				{
					Short s=Short.valueOf(tokens[i]);
					s--;
					polygon.add(s);
				}
				
				//triangulate the polygon and add the resulting faces
				faces.addAll(Triangulator.triangulate(polygon));
			}
		}
		
		if(tokens[1].matches("[0-9]+/[0-9]+"))
		{
			//if: v/vt
			if(c==4)
			{
				//3 faces
				for(int i=1; i<c; i++)
				{
					Short s=Short.valueOf(tokens[i].split("/")[0]);
					s--;
					faces.add(s);
					s=Short.valueOf(tokens[i].split("/")[1]);
					s--;
					vtPointer.add(s);
				}
			}
			else
			{
				//triangulate
				Vector<Short> tmpFaces=new Vector<Short>();
				Vector<Short> tmpVt=new Vector<Short>();
				
				for(int i=1; i<tokens.length; i++)
				{
					Short s=Short.valueOf(tokens[i].split("/")[0]);
					s--;
					tmpFaces.add(s);
					s=Short.valueOf(tokens[i].split("/")[1]);
					s--;
					tmpVt.add(s);
				}
				faces.addAll(Triangulator.triangulate(tmpFaces));
				vtPointer.addAll(Triangulator.triangulate(tmpVt));
			}
		}
		if(tokens[1].matches("[0-9]+//[0-9]+"))
		{
			if(c==4)
			{
				//3 faces
				for(int i=1; i<c; i++)
				{
					Short s=Short.valueOf(tokens[i].split("//")[0]);
					s--;
					faces.add(s);
					s=Short.valueOf(tokens[i].split("//")[1]);
					s--;
					vnPointer.add(s);
				}
			}
			else
			{
				//triangulate
				Vector<Short> tmpFaces=new Vector<Short>();
				Vector<Short> tmpVn=new Vector<Short>();
				for(int i=1; i<tokens.length; i++){
					Short s=Short.valueOf(tokens[i].split("//")[0]);
					s--;
					tmpFaces.add(s);
					s=Short.valueOf(tokens[i].split("//")[1]);
					s--;
					tmpVn.add(s);
				}
				faces.addAll(Triangulator.triangulate(tmpFaces));
				vnPointer.addAll(Triangulator.triangulate(tmpVn));
			}
		}
		
		if(tokens[1].matches("[0-9]+/[0-9]+/[0-9]+"))
		{
			//f: v/vt/vn
			if(c==4)
			{
				//3 faces
				for(int i=1; i<c; i++)
				{
					Short s=Short.valueOf(tokens[i].split("/")[0]);
					s--;
					faces.add(s);
					s=Short.valueOf(tokens[i].split("/")[1]);
					s--;
					vtPointer.add(s);
					s=Short.valueOf(tokens[i].split("/")[2]);
					s--;
					vnPointer.add(s);
				}
			}
			else
			{
				//triangulate
				Vector<Short> tmpFaces=new Vector<Short>();
				Vector<Short> tmpVn=new Vector<Short>();
				//Vector<Short> tmpVt=new Vector<Short>();
				
				for(int i=1; i<tokens.length; i++)
				{
					Short s=Short.valueOf(tokens[i].split("/")[0]);
					s--;
					tmpFaces.add(s);
					//s=Short.valueOf(tokens[i].split("/")[1]);
					//s--;
					//tmpVt.add(s);
					//s=Short.valueOf(tokens[i].split("/")[2]);
					//s--;
					//tmpVn.add(s);
				}
				
				faces.addAll(Triangulator.triangulate(tmpFaces));
				vtPointer.addAll(Triangulator.triangulate(tmpVn));
				vnPointer.addAll(Triangulator.triangulate(tmpVn));
			}
		}
	}
}

