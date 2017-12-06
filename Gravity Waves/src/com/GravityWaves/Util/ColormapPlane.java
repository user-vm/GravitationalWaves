package com.GravityWaves.Util;

import javafx.scene.shape.TriangleMesh;

public class ColormapPlane extends TriangleMesh{

	public ColormapPlane(){
		
		float[] points = new float[12];
		int[] faces = new int[12];
		float[] texCoords = new float[8];
		
		points[0] = (float)-0.5;
		points[1] = (float)-0.5;
		points[2] = (float)0.0;
		points[3] = (float)0.5;
		points[4] = (float)-0.5;
		points[5] = (float)0.0;
		points[6] = (float)-0.5;
		points[7] = (float)0.5;
		points[8] = (float)0.0;
		points[9] = (float)0.5;
		points[10] = (float)0.5;
		points[11] = (float)0.0;
		
		faces[0] = 0;
		faces[1] = 0;
		faces[2] = 1;
		faces[3] = 1;
		faces[4] = 2;
		faces[5] = 2;
		faces[6] = 2;
		faces[7] = 2;
		faces[8] = 1;
		faces[9] = 1;
		faces[10] = 3;
		faces[11] = 3;
		
		texCoords[0] = (float)0.0;
		texCoords[1] = (float)0.0;
		texCoords[2] = (float)1.0;
		texCoords[3] = (float)0.0;
		texCoords[4] = (float)0.0;
		texCoords[5] = (float)1.0;
		texCoords[6] = (float)1.0;
		texCoords[7] = (float)1.0;
		
		this.getPoints().setAll(points);
		this.getTexCoords().setAll(texCoords);
		this.getFaces().setAll(faces);
	}
}
