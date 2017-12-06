
package com.GravityWaves.Util;

import javafx.scene.transform.*;
import javafx.scene.shape.*;

public class ArrowBox {
	
	public static float boxWidth = (float)0.2;
	public Box box;
	private TriangleMesh tip;
	public Xform tipXform;
	public Xform boxXform;
	private Scale boxScaleX;
	private Scale boxScaleAll;
	private Translate tipTranslateX;
	public MeshView tipView;
	private Rotate yRot90;
	
	public ArrowBox(){
		this(0,0,0);
	}
	
	private TriangleMesh createTipMesh(){
		
		float[] points = {-boxWidth,-boxWidth,0,-boxWidth,boxWidth,0,boxWidth,boxWidth,
				0,boxWidth,-boxWidth,0,0,0,4*boxWidth};//new float[5*3];
		int[] faces = {0,0,1,1,2,2,0,0,2,2,3,3,0,0,1,1,4,4,1,1,2,2,4,4,2,2,3,3,4,4,3,3,0,0,4,4};//new int[12*3];
		float[] texCoords = {(float)0.5,(float)0.5,(float)0.5,(float)0.5,(float)0.5,(float)0.5,
				(float)0.5,(float)0.5,(float)0.5,(float)0.5};//new float[5*2];
		
		TriangleMesh aTip = new TriangleMesh();
		aTip.getPoints().setAll(points);
		aTip.getFaces().setAll(faces);
		aTip.getTexCoords().setAll(texCoords);
		
		return aTip;
	}
	
	public ArrowBox(double xPos, double yPos, double zPos){
		tip = createTipMesh();
		tipView = new MeshView(tip);
		box = new Box(boxWidth,boxWidth,boxWidth);
		//box.setVisible(false);
		boxXform = new Xform();
		tipXform = new Xform();
		boxXform.setTranslate(xPos,yPos,zPos);
		tipXform.setTranslate(xPos,yPos,zPos);
		yRot90 = new Rotate();
		yRot90.setAxis(Rotate.Y_AXIS);
		yRot90.setAngle(90);
		tipView.getTransforms().add(yRot90);
		boxXform.getChildren().add(box);
		tipXform.getChildren().add(tipView);
		boxScaleX = new Scale(1,1,1,-boxWidth/2,0,0);
		boxXform.getTransforms().add(boxScaleX);
		boxScaleAll = new Scale(1,1,1,-boxWidth/2,0,0);
		boxXform.getTransforms().add(boxScaleAll);
		tipTranslateX = new Translate(boxWidth*0.5,0,0);
		
		tipXform.getTransforms().add(boxScaleAll);
		tipXform.getTransforms().add(tipTranslateX);
		tipView.setCullFace(CullFace.NONE);
	}
	
	public void scaleX(double xScale){
		boxScaleX.setX(xScale);
		tipTranslateX.setX(boxWidth*(xScale-0.5));
	}
	
	public void scaleAll(double scale){
		boxScaleAll.setX(scale);
		boxScaleAll.setY(scale);
		boxScaleAll.setZ(scale);
	}
	
}
