package com.GravityWaves.Util;

import javafx.scene.shape.TriangleMesh;

public class OrbitMesh extends TriangleMesh{
	
	public OrbitMesh(int sides, double radius){
		
		if (sides<3)
			sides = 3;
		
		float[] points = new float[6*sides];
		int[] faces = new int[12*sides];
		float[] texCoords = new float[4*sides];
		int i = 0;
		
		points[6*i] = (float)(radius*Math.cos(i*2*Math.PI/sides));
		points[6*i+1] = (float)(radius*Math.sin(i*2*Math.PI/sides));
		points[6*i+2] = (float)(0);
		points[6*i+3] = (float)(radius*Math.cos(i*2*Math.PI/sides));
		points[6*i+4] = (float)(radius*Math.sin(i*2*Math.PI/sides));
		points[6*i+5] = (float)(0);
		
		texCoords[4*i] = (float)(i*1.0/sides);
		texCoords[4*i+1] = (float)(0.0);
		texCoords[4*i+2] = (float)(i*1.0/sides);
		texCoords[4*i+3] = (float)(1.0);
		
		for (i=1;i<sides;i++){
			points[6*i] = (float)(radius*Math.cos(i*2*Math.PI/sides));
			points[6*i+1] = (float)(radius*Math.sin(i*2*Math.PI/sides));
			points[6*i+2] = (float)(0);
			points[6*i+3] = (float)(radius*Math.cos(i*2*Math.PI/sides));
			points[6*i+4] = (float)(radius*Math.sin(i*2*Math.PI/sides));
			points[6*i+5] = (float)(0);
			
			faces[12*i-12]=2*i-2;
			faces[12*i-11]=2*i-2;
			faces[12*i-10]=2*i-1;
			faces[12*i-9]=2*i-1;
			faces[12*i-8]=2*i;
			faces[12*i-7]=2*i;
			faces[12*i-6]=2*i;
			faces[12*i-5]=2*i;
			faces[12*i-4]=2*i-1;
			faces[12*i-3]=2*i-1;
			faces[12*i-2]=2*i+1;
			faces[12*i-1]=2*i+1;
			
			texCoords[4*i] = (float)(i*1.0/sides);
			texCoords[4*i+1] = (float)(0.0);
			texCoords[4*i+2] = (float)(i*1.0/sides);
			texCoords[4*i+3] = (float)(1.0);
		}
		
		faces[12*i-12]=2*i-2;
		faces[12*i-11]=2*i-2;
		faces[12*i-10]=2*i-1;
		faces[12*i-9]=2*i-1;
		faces[12*i-8]=0;
		faces[12*i-7]=0;
		faces[12*i-6]=0;
		faces[12*i-5]=0;
		faces[12*i-4]=2*i-1;
		faces[12*i-3]=2*i-1;
		faces[12*i-2]=1;
		faces[12*i-1]=1;
		
		//for(i=0;i<texCoords.length;i++)
			//texCoords[i]=1;
		
		this.getPoints().setAll(points);
		this.getTexCoords().setAll(texCoords);
		this.getFaces().setAll(faces);
	}
	
	public OrbitMesh(double radius){
		this(100,radius);
	}
}
