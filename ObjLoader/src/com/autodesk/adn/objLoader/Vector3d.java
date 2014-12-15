package com.autodesk.adn.objLoader;

public class Vector3d 
{
	Float x;
	Float y;
	Float z;
	
	public Vector3d(Float X, Float Y, Float Z)
	{
		x = X;
		y = Y;
		z = Z;
	}
	
	public Vector3d substract(Vector3d v)
	{
		return new Vector3d(
			x - v.x,
			y - v.y,
			z - v.z);
	}
	
	public Vector3d crossP(Vector3d v)
	{
		return new Vector3d(
			y * v.z - z * v.y,
			z * v.x - x * v.z,
			x * v.y - y * v.x);
	}
}
