
package com.GravityWaves.Controller;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TimelineBuilder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableFloatArray;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point3D;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

import com.GravityWaves.Util.ArrowBox;
import com.GravityWaves.Util.ColormapPlane;
import com.GravityWaves.Util.OrbitMesh;
import com.GravityWaves.Util.Xform;


@SuppressWarnings("deprecation")
public class Controller implements Initializable {

	// FXML imports

	// The root pane of the fxml file
	@FXML
	BorderPane rootBorder;

	// The parent pane for the tabs. Unused, but included for completeness.
	@FXML
	TabPane tabPane;
	
	//thing
	@FXML
	BorderPane scenesBorderPane;
	
	//colour scale stuff
	@FXML
	ImageView amplitudeImage;
	@FXML
	StackPane colorScaleStackPane;
	@FXML
	Label colorScaleMaxLabel, colorScaleMinLabel, colorScaleCenterLabel;
	@FXML
	AnchorPane colorScaleAnchor;
	
	//laser interference pattern stuff
	@FXML
	StackPane patternStackPane;
	@FXML
	AnchorPane patternAnchor;

	// Stars tab

	@FXML
	Tab tabStars;

	@FXML
	CheckBox useEqualDensities, showOrbits, showSystem;
	@FXML
	ComboBox<String> star1MassUnits, star2MassUnits, star1RadiusUnits,
			star2RadiusUnits, distanceUnits;
	@FXML
	TextField star1Mass, star2Mass, star1Radius, star2Radius, distanceText, systemScaleText,
			timeStretchText;
	@FXML
	Button deduceScaleButton, lockScaleToCameraButton, deduceTimeStretchButton;

	// Field tab

	@FXML
	Tab tabField;

	@FXML
	CheckBox showField, showColourBar;
	@FXML
	ComboBox<String> xSeparationUnits, ySeparationUnits, zSeparationUnits,
			xAxisRotUnits, yAxisRotUnits, zAxisRotUnits;
	@FXML
	RadioButton arrowsField, colourMapField;
	@FXML
	TextField numXPointsBox, numYPointsBox, numZPointsBox, xSeparation,
			ySeparation, zSeparation, xAxisRot, yAxisRot, zAxisRot;
	@FXML
	Label numXPointsLabel, numYPointsLabel, numZPointsLabel, xFieldLabel, yFieldLabel,
			zFieldLabel;

	// Group for the radio buttons. Unused, but included for completeness.
	@FXML
	ToggleGroup fieldTypeGroup;

	// Camera tab

	@FXML
	Tab tabCamera;
	@FXML
	Button cameraReset, cameraPointAtZero;
	@FXML
	ComboBox<String> cameraDistanceUnits, cameraXAxisRotUnits, cameraYAxisRotUnits,
			cameraXPivotUnits, cameraYPivotUnits, cameraZPivotUnits,
			inputUnits;
	@FXML
	TextField cameraDistance, cameraXAxisRot, cameraYAxisRot, cameraXPivot,
			cameraYPivot, cameraZPivot;
	@FXML
	Accordion cameraControlsAcc;
	@FXML
	TitledPane cameraControlsTitledPane;

	// Detector tab

	@FXML
	Tab tabDetector;

	@FXML
	Button detectorReset;
	@FXML
	CheckBox detectorShow;
	@FXML
	ComboBox<String> detectorXPosUnits, detectorYPosUnits, detectorZPosUnits,
			arm1LengthUnitsBox, arm2LengthUnitsBox, arm1AngleUnitsBox,
			arm2AngleUnitsBox, detectorXAxisRotUnits, detectorYAxisRotUnits,
			detectorZAxisRotUnits;
	@FXML
	TextField scaleOverride, detectorXPos, detectorYPos, detectorZPos,
			arm1LengthBox, arm2LengthBox, arm1AngleBox, arm2AngleBox,
			detectorXAxisRot, detectorYAxisRot, detectorZAxisRot;

	// Laser tab
	
	@FXML
	TextField wavelengthText, arm1MultText, arm2MultText, slitSeparationText,
			screenDistText, screenWidthText, patternResolutionText;
	@FXML
	ComboBox<String> wavelengthUnitsBox, slitSeparationUnitsBox, screenDistUnitsBox,
			screenWidthUnitsBox;
	@FXML
	CheckBox showPatternChk;
	@FXML
	Button centerCentralMaximaButton;
	@FXML
	Label armLengthDifLabel, armPhaseDifLabel, fringeDistLabel, fringeOscAmplLabel;
	@FXML
	AnchorPane patternNumsAnchor;
	
	///
	
	public double M = 1, KM = 1000, AU = 149597870700.0, LY = 9.4605284E+15,
			PC = 3.08567758E+16;

	public double KG = 1, T = 1E+3, SM = 1.9891E+30;

	public double DEG = 1, RAD = 180 / Math.PI;
	
	public double CM = 1E-2, MM = 1E-3, UM = 1E-6, NM = 1E-9, PM = 1E-12;
	
	public double G = 6.67384E-11;
	public double LIGHTSPEED = 299792458;
	
	public double millisTime, millisTimeLastTimeStretchChange;
	public double timeStretchFactor = 1.0;
	
	int numXPoints = 10;
	int numYPoints = 10;
	int numZPoints = 1;

	int cmapNumXPoints = 90;
	int cmapNumYPoints = 90;

	double xDist = 9000;
	double yDist = 9000;
	double zDist = 9000;

	double cmapXDist = 81000;
	double cmapYDist = 81000;
	double cmapZDist = 81000;

	Map<String, Double> massMap = new HashMap<String, Double>();
	Map<String, Double> distMap = new HashMap<String, Double>();
	Map<String, Double> angleMap = new HashMap<String, Double>();

	Map<Double, String> distInvMap = new HashMap<Double, String>();
	Map<Double, String> angleInvMap = new HashMap<Double, String>();
	
	WritableImage patternImage;
	ImageView patternView;
	PixelWriter patternWriter;

	// from Main

	public Sphere testSphere;
	public double mass1 = 1E-4 * SM;
	public Sphere testSphere2;
	public double mass2 = 10E-4 * SM;
	public double distance = 20000;
	public double radius1 = 2500, radius2 = radius1
			* Math.pow(mass2 / mass1, 1.0 / 3);
	public double systemScaleFactor = Math.atan(0.1);
	public double systemScaleFactor2 = 1.0;
	public Scale systemGroupScale;
	public Group systemGroup;
	public Boolean isScaledToCamera = false; 
	public DecimalFormat smallNumberFormat = new DecimalFormat("0.0###");
	public DecimalFormat bigNumberFormat = new DecimalFormat("0.0###E0");

	public double angle = 0;
	
	public MeshView orbitMesh = new MeshView(new OrbitMesh(distance * mass1
			/ (mass1 + mass2)));
	public MeshView orbitMesh2 = new MeshView(new OrbitMesh(distance * mass2
			/ (mass1 + mass2)));
	PerspectiveCamera camera;
	final Xform cameraXform = new Xform();
	final Xform cameraXform2 = new Xform();
	final Xform cameraXform3 = new Xform();
	double mousePosX;
	double mousePosY;
	double mouseOldX;
	double mouseOldY;
	double mouseDeltaX;
	double mouseDeltaY;
	double DELTA_MULTIPLIER = -200.0;
	double CONTROL_MULTIPLIER = -0.1;
	double SHIFT_MULTIPLIER = -0.1;
	double ALT_MULTIPLIER = -0.5;
	public Timeline timer = new Timeline();
	boolean timelinePlaying = true;

	Group stuffContainer;
	Group arrowGroup, arrowGroupRots;
	public Rotate arrowGroupXRot, arrowGroupYRot, arrowGroupZRot;
	public Rotate fieldColormapXRot, fieldColormapYRot, fieldColormapZRot;

	final Xform world = new Xform();
	ArrayList<ArrowBox> arrowBoxes = new ArrayList<ArrowBox>();
	public PhongMaterial orbitMaterial;
	public PhongMaterial cylinderMaterial;
	public PhongMaterial arrowMaterial;
	StackPane stackPane;

	public boolean isFieldVisible = true;
	public boolean isStarDensityEqual = false;

	public double mass1Units = SM;
	public double mass2Units = SM;
	public double radius1Units = KM;
	public double radius2Units = KM;
	public double distUnits = KM;

	public double xDistUnits = KM;
	public double yDistUnits = KM;
	public double zDistUnits = KM;

	public double cmapXDistUnits = KM;
	public double cmapYDistUnits = KM;
	public double cmapZDistUnits = KM;

	public double xAngleUnits = DEG;
	public double yAngleUnits = DEG;
	public double zAngleUnits = DEG;

	public double cmapXAngleUnits = DEG;
	public double cmapYAngleUnits = DEG;
	public double cmapZAngleUnits = DEG;

	public double xFieldRot = 0;
	public double yFieldRot = 0;
	public double zFieldRot = 0;

	public double cmapXFieldRot = 0;
	public double cmapYFieldRot = 0;
	public double cmapZFieldRot = 0;

	public double distCameraUnits = KM;

	public double xPivotUnits = KM;
	public double yPivotUnits = KM;
	public double zPivotUnits = KM;

	public double xRotCameraUnits = DEG;
	public double yRotCameraUnits = DEG;

	public double distCamera;

	public double xRotCameraVal;
	public double yRotCameraVal;

	public double xPivot = 0;
	public double yPivot = 0;
	public double zPivot = 0;

	public double movePivotUnits = KM;

	// detector
	public double xDetector;
	public double yDetector;
	public double zDetector;

	public double xRotDetector;
	public double yRotDetector;
	public double zRotDetector;

	public double arm1Length;
	public double arm1Angle;
	public int arm1Mult = 1;
	public double slitSeparation = 1E-3;
	public double screenDist = 100;
	public double leftScreenLimit = -0.2;
	public double rightScreenLimit = 0.2;
	public int patternResolution = 100;

	public double arm2Length;
	public double arm2Angle;
	public int arm2Mult = 1;
	public double wavelength = 700 * NM;
	public double maxPhaseDif = 0, minPhaseDif = 0;
	
	public double slitSeparationUnits = MM;
	public double screenDistUnits = M;

	public double xDetectorUnits = KM;
	public double yDetectorUnits = KM;
	public double zDetectorUnits = KM;

	public double xRotDetectorUnits = DEG;
	public double yRotDetectorUnits = DEG;
	public double zRotDetectorUnits = DEG;
	
	public double screenWidthUnits = CM;
	
	public double wavelengthUnits = NM;

	public final double DETECTOR_SIZE = 5;

	Cylinder arm1Cylinder, arm2Cylinder;
	Box detectorA, detectorB, detectorC;

	public Rotate rot90;

	public Rotate detectorBRotate;
	public Rotate arm1CylinderRotate;
	public Rotate detectorCRotate;
	public Rotate arm2CylinderRotate;
	
	public Scale armCylinderScale;
	public Scale detectorBoxScale;

	public double arm1LengthUnits = KM;
	public double arm2LengthUnits = KM;
	public double arm1AngleUnits = DEG;
	public double arm2AngleUnits = DEG;

	public Group detectorGroup;
	public Rotate detectorGroupRotX;
	public Rotate detectorGroupRotY;
	public Rotate detectorGroupRotZ;

	public Scale detectorScale;
	public double detectorScaleFactor;

	public Group scene3DRoot;
	public Group colorScaleRoot;
	public SubScene scene3D;
	public SubScene colorScaleScene;
	
	public ImageView colorScaleView;
	public Rotate colorScaleRotate;
	public Scale colorScaleScale; //lol
	public Translate colorScaleTranslate;
	public PixelReader colorScaleReader;
	public double colorScaleImageHeight;

	// colormap

	public MeshView fieldColormap;

	public Scale fieldColormapScale;
	public Group colormapGroup;

	WritableImage colormapTexture;

	public PhongMaterial colormapMaterial;

	public boolean isArrowsVisible = true;

	private PixelWriter cmapWriter;
	
	public double omega = Math.sqrt(G*(mass1+mass2)/Math.pow(distance,3));
	
	public void deduceScale(){
		
		systemScaleFactor2 = Math.atan(0.1) / Math.atan(distance/2/-distCamera);
		systemScaleFactor = Math.atan(0.1);
		
		if(isScaledToCamera)
			systemScaleText.setText(formatDouble(systemScaleFactor
					/ Math.atan(distance/2/-distCamera)));
		else
			systemScaleText.setText(formatDouble(systemScaleFactor2));
		
		setStars();
		
	}
	
	public void lockScaleToCamera(){
		
		isScaledToCamera = !isScaledToCamera;
		
		if(isScaledToCamera){
			lockScaleToCameraButton.setText("Unlock scale");
			systemScaleFactor = systemScaleFactor2 * Math.atan(distance/2/-distCamera);
		}
		else{
			lockScaleToCameraButton.setText("Lock scale to camera");
			systemScaleFactor2 = systemScaleFactor / Math.atan(distance/2/-distCamera);
		}
	}
	
	public void updateArrows(){
		
		Point3D arrowPos;
		Point3D pointForForce;
		
		for (ArrowBox i:arrowBoxes){
			
			arrowPos = i.boxXform.localToScene(0,0,0);
			pointForForce = getTidalForce((millisTime - millisTimeLastTimeStretchChange) / 1000
					* timeStretchFactor, arrowPos.getX(),
					arrowPos.getY(), arrowPos.getZ());
			i.boxXform.setRz(Math.toDegrees(Math.atan2(
					pointForForce.getY(), pointForForce.getX())));
			i.tipXform.setRz(Math.toDegrees(Math.atan2(
					pointForForce.getY(), pointForForce.getX())));
			i.boxXform.setRy(Math.toDegrees(Math.atan2(pointForForce.getZ(),
					Math.sqrt(Math.pow(pointForForce.getX(),2)
					+Math.pow(pointForForce.getY(),2)))));
			i.tipXform.setRy(Math.toDegrees(Math.atan2(pointForForce.getZ(),
					Math.sqrt(Math.pow(pointForForce.getX(),2)
					+Math.pow(pointForForce.getY(),2)))));
			i.scaleX(5*Math.sqrt(Math.pow(pointForForce.getX(),2) +
					Math.pow(pointForForce.getY(), 2)+Math.pow(pointForForce.getZ(),2)));
				}
	}

	public void updateField() {

		arrowGroupXRot.setAngle(xFieldRot);
		arrowGroupYRot.setAngle(yFieldRot);
		arrowGroupZRot.setAngle(zFieldRot);
		
		Point3D arrowGroupPoint;
		Point3D pointForForce;

		ArrowBox currentArrow;
		double arrowScaleFactor = 1;
		int arrowScaleInvPower = 0;
		if(numXPoints>1 && xDist > 0){
			arrowScaleFactor *= xDist;
			arrowScaleInvPower++;}
		if(numYPoints>1 && yDist > 0){
			arrowScaleFactor *= yDist;
			arrowScaleInvPower++;}
		if(numZPoints>1 && zDist > 0){
			arrowScaleFactor *= zDist;
			arrowScaleInvPower++;}
		arrowScaleFactor = Math.pow(arrowScaleFactor, 1.0/arrowScaleInvPower)/7;
		for (int i = 0; i < numZPoints; i++) {
			for (int j = 0; j < numYPoints; j++) {
				for (int k = 0; k < numXPoints; k++) {

					if ((i * numYPoints + j) * numXPoints + k < arrowBoxes
							.size()) {
						currentArrow = arrowBoxes.get((i * numYPoints + j)
								* numXPoints + k);
						arrowGroupPoint = arrowGroupRots.localToScene(new Point3D(xDist
								* (k - ((numXPoints - 1) / 2.0)), yDist
								* (j - ((numYPoints - 1) / 2.0)), zDist
								* (i - ((numZPoints - 1) / 2.0))));
						currentArrow.boxXform.setTranslate(arrowGroupPoint.getX(),
								arrowGroupPoint.getY(), arrowGroupPoint.getZ());
						currentArrow.tipXform.setTranslate(arrowGroupPoint.getX(),
								arrowGroupPoint.getY(), arrowGroupPoint.getZ());
						
						pointForForce = getTidalForce((millisTime - millisTimeLastTimeStretchChange) / 1000
								* timeStretchFactor,
								arrowGroupPoint.getX(), arrowGroupPoint.getY(),
								arrowGroupPoint.getZ());
						
						currentArrow.boxXform.setRz(Math.toDegrees(Math.atan2(
								pointForForce.getY(), pointForForce.getX())));
						currentArrow.tipXform.setRz(Math.toDegrees(Math.atan2(
								pointForForce.getY(), pointForForce.getX())));
						
						currentArrow.boxXform.setRy(0);
						currentArrow.tipXform.setRy(0);
						
						currentArrow.scaleAll(arrowScaleFactor);
						currentArrow.scaleX(5*Math.sqrt(Math.pow(pointForForce.getX(),2) +
								Math.pow(pointForForce.getY(), 2)));
					} else {
						arrowGroupPoint = arrowGroupRots.localToScene(new Point3D(xDist
								* (k - ((numXPoints - 1) / 2.0)), yDist
								* (j - ((numYPoints - 1) / 2.0)), zDist
								* (i - ((numZPoints - 1) / 2.0))));
						currentArrow = new ArrowBox(arrowGroupPoint.getX(),
								arrowGroupPoint.getY(), arrowGroupPoint.getZ());

						pointForForce = getTidalForce((millisTime - millisTimeLastTimeStretchChange) / 1000
								* timeStretchFactor,
								arrowGroupPoint.getX(), arrowGroupPoint.getY(),
								arrowGroupPoint.getZ());
						
						currentArrow.boxXform.setRz(Math.toDegrees(Math.atan2(
								pointForForce.getY(), pointForForce.getX())));
						currentArrow.tipXform.setRz(Math.toDegrees(Math.atan2(
								pointForForce.getY(), pointForForce.getX())));
						
						currentArrow.boxXform.setRy(0);
						currentArrow.tipXform.setRy(0);
						
						currentArrow.scaleAll(arrowScaleFactor);
						currentArrow.scaleX(5*Math.sqrt(Math.pow(pointForForce.getX(),2) +
								Math.pow(pointForForce.getY(), 2)));
						
						currentArrow.box.setMaterial(arrowMaterial);
						currentArrow.tipView.setMaterial(arrowMaterial);
						
						arrowGroup.getChildren().add(currentArrow.boxXform);
						arrowGroup.getChildren().add(currentArrow.tipXform);
						arrowBoxes.add(currentArrow);
					}
				}
			}
		}

		int numPoints = numZPoints * numYPoints * numXPoints;
		for (int i = numPoints; i < arrowBoxes.size();) {

			arrowGroup.getChildren().remove(arrowBoxes.get(numPoints).boxXform);
			arrowGroup.getChildren().remove(arrowBoxes.get(numPoints).tipXform);
			arrowBoxes.remove(numPoints);
		}
	}

	public void setOrbitsVisibility(boolean isVisible) {

		orbitMesh.setVisible(isVisible);
		orbitMesh2.setVisible(isVisible);
	}

	public void setFieldVisibility(boolean isVisible) {

		isFieldVisible = isVisible;
		
		if(isVisible){
			arrowGroup.setVisible(arrowsField.isSelected());
			fieldColormap.setVisible(!arrowsField.isSelected());
		} else {
			arrowGroup.setVisible(false);
			arrowGroup.setVisible(false);
		}

	}

	public void updateFieldTexts() {

		numXPointsBox.setText(Integer.toString(numXPoints));
		numYPointsBox.setText(Integer.toString(numYPoints));
		numZPointsBox.setText(Integer.toString(numZPoints));

		xSeparation.setText(formatDouble(xDist / xDistUnits));
		ySeparation.setText(formatDouble(yDist / yDistUnits));
		zSeparation.setText(formatDouble(zDist / zDistUnits));

		xAxisRot.setText(formatDouble(xFieldRot / xAngleUnits));
		yAxisRot.setText(formatDouble(yFieldRot / yAngleUnits));
		zAxisRot.setText(formatDouble(zFieldRot / zAngleUnits));

		xSeparationUnits.setValue(distInvMap.get(xDistUnits));
		ySeparationUnits.setValue(distInvMap.get(yDistUnits));
		zSeparationUnits.setValue(distInvMap.get(zDistUnits));

		xAxisRotUnits.setValue(angleInvMap.get(xAngleUnits));
		yAxisRotUnits.setValue(angleInvMap.get(yAngleUnits));
		zAxisRotUnits.setValue(angleInvMap.get(yAngleUnits));
	}

	public void updateColormapTexts() {

		numXPointsBox.setText(Integer.toString(cmapNumXPoints));
		numYPointsBox.setText(Integer.toString(cmapNumYPoints));
		numZPointsBox.setText("1");

		xSeparation.setText(formatDouble(cmapXDist / cmapXDistUnits));
		ySeparation.setText(formatDouble(cmapYDist / cmapYDistUnits));
		zSeparation.setText("0.0");

		xAxisRot.setText(formatDouble(cmapXFieldRot / cmapXAngleUnits));
		yAxisRot.setText(formatDouble(cmapYFieldRot / cmapYAngleUnits));
		zAxisRot.setText(formatDouble(cmapZFieldRot / cmapZAngleUnits));

		xSeparationUnits.setValue(distInvMap.get(cmapXDistUnits));
		ySeparationUnits.setValue(distInvMap.get(cmapYDistUnits));
		zSeparationUnits.setValue(distInvMap.get(cmapZDistUnits));

		xAxisRotUnits.setValue(angleInvMap.get(cmapXAngleUnits));
		yAxisRotUnits.setValue(angleInvMap.get(cmapYAngleUnits));
		zAxisRotUnits.setValue(angleInvMap.get(cmapZAngleUnits));
	}

	public void toggleFieldType() {

		boolean isVisible = arrowsField.isSelected();

		if (isVisible == isArrowsVisible)
			return;

		isArrowsVisible = isVisible;
		
		if(showField.isSelected()){
			arrowGroup.setVisible(isVisible);
			fieldColormap.setVisible(!isVisible);
		}

		if (isVisible) {
			updateFieldTexts();
			updateField();
			scenesBorderPane.setRight(null);
			showColourBar.setDisable(true);
		} else {
			updateColormapTexts();
			updateColormap(true);
			if(showField.isSelected()){
				showColourBar.setDisable(false);
				if(showColourBar.isSelected())
					scenesBorderPane.setRight(colorScaleAnchor);
			}
		}
		
		numXPointsLabel.setText(isVisible?"Number of x points":"Number of x pixels");
		numYPointsLabel.setText(isVisible?"Number of y points":"Number of y pixels");
		numZPointsLabel.setVisible(isVisible);
		xFieldLabel.setText(isVisible?"X points separation":"Colormap width");
		yFieldLabel.setText(isVisible?"Y points separation":"Colormap height");
		zFieldLabel.setVisible(isVisible);
		numZPointsBox.setVisible(isVisible);
		zSeparation.setVisible(isVisible);
		zSeparationUnits.setVisible(isVisible);
		arrowGroup.setVisible(isVisible);
		fieldColormap.setVisible(!isVisible);
	}

	public void setStarDensityEquality(boolean isEqual) {

		isStarDensityEqual = isEqual;

		if (isStarDensityEqual) {
			radius2 = radius1 * Math.pow(mass2 / mass1, 1.0 / 3);
			testSphere2.setRadius(radius2);
		}
	}

	public void setStars() {

		testSphere.setRadius(radius1);
		testSphere2.setRadius(radius2);

		systemGroup.getChildren().remove(orbitMesh);
		systemGroup.getChildren().remove(orbitMesh2);

		orbitMesh = new MeshView(new OrbitMesh(distance * mass1
				/ (mass1 + mass2)));
		orbitMesh2 = new MeshView(new OrbitMesh(distance * mass2
				/ (mass1 + mass2)));
		
		orbitMesh.setDepthTest(DepthTest.ENABLE);
		orbitMesh2.setDepthTest(DepthTest.ENABLE);

		orbitMesh.setMaterial(orbitMaterial);
		orbitMesh.setDrawMode(DrawMode.LINE);

		orbitMesh2.setMaterial(orbitMaterial);
		orbitMesh2.setDrawMode(DrawMode.LINE);
		
		toggleShowOrbits();

		systemGroup.getChildren().add(orbitMesh);
		systemGroup.getChildren().add(orbitMesh2);
		
		if(isScaledToCamera){
			systemGroupScale.setX(systemScaleFactor
					/ Math.atan(distance/2/-distCamera));
			systemGroupScale.setY(systemScaleFactor
					/ Math.atan(distance/2/-distCamera));
			systemGroupScale.setZ(systemScaleFactor
					/ Math.atan(distance/2/-distCamera));
		}
		else {
			systemGroupScale.setX(systemScaleFactor2);
			systemGroupScale.setY(systemScaleFactor2);
			systemGroupScale.setZ(systemScaleFactor2);
		}
		
		
		//omega = Math.toRadians(250);
		omega = Math.sqrt(G*(mass1+mass2)/Math.pow(distance,3));
		updateColorScale();
		resetPhaseLimits();

	}
	
	public void setScale(){
		
		if(!isScaledToCamera)
			return;
		
		if(isScaledToCamera){
			systemGroupScale.setX(systemScaleFactor
					/ Math.atan(distance/2/-distCamera));
			systemGroupScale.setY(systemScaleFactor
					/ Math.atan(distance/2/-distCamera));
			systemGroupScale.setZ(systemScaleFactor
					/ Math.atan(distance/2/-distCamera));
			
			systemScaleText.setText(formatDouble(systemScaleFactor
					/ Math.atan(distance/2/-distCamera)));
		}
		else {
			systemGroupScale.setX(systemScaleFactor2);
			systemGroupScale.setY(systemScaleFactor2);
			systemGroupScale.setZ(systemScaleFactor2);
		}
	}
	
	public void setCamera() {

		cameraXform.t.setX(xPivot);
		cameraXform.t.setY(yPivot);
		cameraXform.t.setZ(zPivot);
		camera.setTranslateZ(distCamera);
		
		setScale();

		cameraXform.setRx(xRotCameraVal);
		cameraXform.setRy(yRotCameraVal);
	}

	public void xFormToVar() {

		xPivot = cameraXform.t.getX();
		yPivot = cameraXform.t.getY();
		zPivot = cameraXform.t.getZ();

		distCamera = camera.getTranslateZ();

		xRotCameraVal = cameraXform.rx.getAngle();
		yRotCameraVal = cameraXform.ry.getAngle();
	}

	public void resetCamera() {

		camera.setTranslateZ(-200 * KM);
		cameraXform.ry.setAngle(0);
		cameraXform.rx.setAngle(0);
		cameraXform.rz.setAngle(0);
		cameraXform.setTx(0);
		cameraXform.setTy(0);
		cameraXform.setTz(0);

		xFormToVar();
		setCameraTextFields();
		setScale();
	}

	public void pointCameraAtZero() {
		cameraXform.setTx(0);
		cameraXform.setTy(0);
		cameraXform.setTz(0);

		xPivot = 0;
		yPivot = 0;
		zPivot = 0;

		cameraXPivot.setText("0.0");
		cameraYPivot.setText("0.0");
		cameraZPivot.setText("0.0");
	}
	
	public String formatDouble(double number){
		
		if(Math.abs(number)<10000 && Math.abs(number)>=1 || number == 0)
			return smallNumberFormat.format(number);
		
		return bigNumberFormat.format(number);
	}

	public void setCameraTextFields() {

		cameraDistance.setText(formatDouble(-distCamera / distCameraUnits));

		cameraXPivot.setText(formatDouble(xPivot / xPivotUnits));
		cameraYPivot.setText(formatDouble(yPivot / yPivotUnits));
		cameraZPivot.setText(formatDouble(zPivot / zPivotUnits));

		cameraXAxisRot
				.setText(formatDouble(xRotCameraVal / xRotCameraUnits));
		cameraYAxisRot
				.setText(formatDouble(yRotCameraVal / yRotCameraUnits));
	}

	public void setDetector() {

		detectorScale.setX(detectorScaleFactor);
		detectorScale.setY(detectorScaleFactor);
		detectorScale.setZ(detectorScaleFactor);

		detectorGroup.setTranslateX(xDetector);
		detectorGroup.setTranslateY(yDetector);
		detectorGroup.setTranslateZ(zDetector);

		detectorGroupRotX.setAngle(xRotDetector);
		detectorGroupRotY.setAngle(yRotDetector);
		detectorGroupRotZ.setAngle(zRotDetector);

		detectorB.setTranslateX(arm1Length);
		detectorBRotate.setPivotX(-arm1Length);
		detectorBRotate.setAngle(arm1Angle);

		arm1Cylinder.setTranslateX(arm1Length / 2);
		arm1Cylinder.setHeight(arm1Length);
		arm1CylinderRotate.setPivotX(-arm1Length / 2);
		arm1CylinderRotate.setAngle(arm1Angle);

		detectorC.setTranslateX(arm2Length);
		detectorCRotate.setPivotX(-arm2Length);
		detectorCRotate.setAngle(arm2Angle);

		arm2Cylinder.setTranslateX(arm2Length / 2);
		arm2Cylinder.setHeight(arm2Length);
		arm2CylinderRotate.setPivotX(-arm2Length / 2);
		arm2CylinderRotate.setAngle(arm2Angle);
		
		armCylinderScale.setX((arm1Length + arm2Length) / 200);
		armCylinderScale.setZ((arm1Length + arm2Length) / 200);
		
		detectorBoxScale.setX((arm1Length + arm2Length) / 200);
		detectorBoxScale.setY((arm1Length + arm2Length) / 200);
		detectorBoxScale.setZ((arm1Length + arm2Length) / 200);
		
		resetPhaseLimits();
	}
	
	public void setLaserTextFields(){
		
		wavelengthText.setText(formatDouble(wavelength / wavelengthUnits));
		arm1MultText.setText(Integer.toString(arm1Mult));
		arm2MultText.setText(Integer.toString(arm2Mult));
		slitSeparationText.setText(formatDouble(slitSeparation / slitSeparationUnits));
		screenDistText.setText(formatDouble(screenDist/ screenDistUnits));
		patternResolutionText.setText(Integer.toString(patternResolution));
		screenWidthText.setText(formatDouble((rightScreenLimit-leftScreenLimit)/screenWidthUnits));
		
		fringeDistLabel.setText(formatDouble(wavelength * screenDist / slitSeparation));
	}

	public void setDetectorTextFields() {

		scaleOverride.setText(formatDouble(detectorScaleFactor));

		detectorXPos.setText(formatDouble(xDetector / xDetectorUnits));
		detectorYPos.setText(formatDouble(yDetector / yDetectorUnits));
		detectorZPos.setText(formatDouble(zDetector / zDetectorUnits));

		detectorXAxisRot.setText(formatDouble(xRotDetector
				/ xRotDetectorUnits));
		detectorYAxisRot.setText(formatDouble(yRotDetector
				/ yRotDetectorUnits));
		detectorZAxisRot.setText(formatDouble(zRotDetector
				/ zRotDetectorUnits));

		arm1LengthBox.setText(formatDouble(arm1Length / arm1LengthUnits));
		arm1AngleBox.setText(formatDouble(arm1Angle / arm1AngleUnits));

		arm2LengthBox.setText(formatDouble(arm2Length / arm2LengthUnits));
		arm2AngleBox.setText(formatDouble(arm2Angle / arm2AngleUnits));
	}

	private void handleMouse(SubScene scene, final Node root) {
		scene.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				updateCamera();
				mousePosX = me.getSceneX();
				mousePosY = me.getSceneY();
				mouseOldX = me.getSceneX();
				mouseOldY = me.getSceneY();
				scene3D.requestFocus();
			}
		});
		scene.setOnMouseDragged(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent me) {
				mouseOldX = mousePosX;
				mouseOldY = mousePosY;
				mousePosX = me.getSceneX();
				mousePosY = me.getSceneY();
				mouseDeltaX = -(mousePosX - mouseOldX);
				mouseDeltaY = -(mousePosY - mouseOldY);

				double modifier = 1.0;
				double modifierFactor = 0.1;

				if (me.isControlDown()) {
					modifier = 0.1;
				}
				if (me.isShiftDown()) {
					modifier = 10.0;
				}
				if (me.isPrimaryButtonDown()) {
					cameraXform.ry.setAngle((cameraXform.ry.getAngle()
							- mouseDeltaX * modifierFactor * modifier * 2.0) % 360); // +
					cameraXform.rx.setAngle((cameraXform.rx.getAngle()
							+ mouseDeltaY * modifierFactor * modifier * 2.0) % 360); // -
				} else if (me.isSecondaryButtonDown()) {
					double z = camera.getTranslateZ();
					double newZ = z + mouseDeltaX * modifierFactor
							* movePivotUnits * modifier;
					camera.setTranslateZ((newZ<0)?newZ:0);
				} else if (me.isMiddleButtonDown()) {
					cameraXform.t.setX(cameraXform.t.getX()
							+ cameraXform3.localToScene(
									new Point3D(mouseDeltaX * modifierFactor
											* modifier * movePivotUnits * 0.3,
											0, 0)).getX()
							- cameraXform3.localToScene(new Point3D(0, 0, 0))
									.getX());
					cameraXform.t.setY(cameraXform.t.getY()
							+ cameraXform3.localToScene(
									new Point3D(mouseDeltaX * modifierFactor
											* modifier * movePivotUnits * 0.3,
											0, 0)).getY()
							- cameraXform3.localToScene(new Point3D(0, 0, 0))
									.getY());
					cameraXform.t.setZ(cameraXform.t.getZ()
							+ cameraXform3.localToScene(
									new Point3D(mouseDeltaX * modifierFactor
											* modifier * movePivotUnits * 0.3,
											0, 0)).getZ()
							- cameraXform3.localToScene(new Point3D(0, 0, 0))
									.getZ());

					cameraXform.t.setX(cameraXform.t.getX()
							+ cameraXform3.localToScene(
									new Point3D(0, mouseDeltaY * modifierFactor
											* modifier * movePivotUnits * 0.3,
											0)).getX()
							- cameraXform3.localToScene(new Point3D(0, 0, 0))
									.getX());
					cameraXform.t.setY(cameraXform.t.getY()
							+ cameraXform3.localToScene(
									new Point3D(0, mouseDeltaY * modifierFactor
											* modifier * movePivotUnits * 0.3,
											0)).getY()
							- cameraXform3.localToScene(new Point3D(0, 0, 0))
									.getY());
					cameraXform.t.setZ(cameraXform.t.getZ()
							+ cameraXform3.localToScene(
									new Point3D(0, mouseDeltaY * modifierFactor
											* modifier * movePivotUnits * 0.3,
											0)).getZ()
							- cameraXform3.localToScene(new Point3D(0, 0, 0))
									.getZ());
				}
				xFormToVar();
				setCameraTextFields();
				setScale();
			}
		});
	}

	private void handleKeyboard(SubScene scene, final Node root1) {
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				double ctrlMult1 = CONTROL_MULTIPLIER;
				double shiftMult1 = SHIFT_MULTIPLIER;
				CONTROL_MULTIPLIER *= movePivotUnits;
				SHIFT_MULTIPLIER *= movePivotUnits;
				switch (event.getCode()) {
				case Z:
					if (event.isShiftDown()) {
						cameraXform.ry.setAngle(0.0);
						cameraXform.rx.setAngle(0.0);
						camera.setTranslateZ(-200.0 * KM);
					}
					cameraXform2.t.setX(0.0);
					cameraXform2.t.setY(0.0);
					xFormToVar();
					setCameraTextFields();
					break;
				case SPACE:
					if (timelinePlaying) {
						timer.pause();
						timelinePlaying = false;
					} else {
						timer.play();
						timelinePlaying = true;
					}
					break;
				case UP:
					if (event.isControlDown() && event.isShiftDown()) {
						cameraXform.t.setX(cameraXform.t.getX()
								- cameraXform3.localToScene(
										new Point3D(0,
												10.0 * CONTROL_MULTIPLIER, 0))
										.getX()
								+ cameraXform3.localToScene(
										new Point3D(0, 0, 0)).getX());
						cameraXform.t.setY(cameraXform.t.getY()
								- cameraXform3.localToScene(
										new Point3D(0,
												10.0 * CONTROL_MULTIPLIER, 0))
										.getY()
								+ cameraXform3.localToScene(
										new Point3D(0, 0, 0)).getY());
						cameraXform.t.setZ(cameraXform.t.getZ()
								- cameraXform3.localToScene(
										new Point3D(0,
												10.0 * CONTROL_MULTIPLIER, 0))
										.getZ()
								+ cameraXform3.localToScene(
										new Point3D(0, 0, 0)).getZ());
					} else if (event.isAltDown() && event.isShiftDown()) {
						if(cameraXform.rx.getAngle() + 10.0 * ALT_MULTIPLIER >= 0)
							cameraXform.rx.setAngle((cameraXform.rx.getAngle()
									+ 10.0 * ALT_MULTIPLIER) % 360);
						else
							cameraXform.rx.setAngle((cameraXform.rx.getAngle() + 10.0
									* ALT_MULTIPLIER) + 360);
					} else if (event.isControlDown()) {
						cameraXform.t.setX(cameraXform.t.getX()
								- cameraXform3.localToScene(
										new Point3D(0,
												1.0 * CONTROL_MULTIPLIER, 0))
										.getX()
								+ cameraXform3.localToScene(
										new Point3D(0, 0, 0)).getX());
						cameraXform.t.setY(cameraXform.t.getY()
								- cameraXform3.localToScene(
										new Point3D(0,
												1.0 * CONTROL_MULTIPLIER, 0))
										.getY()
								+ cameraXform3.localToScene(
										new Point3D(0, 0, 0)).getY());
						cameraXform.t.setZ(cameraXform.t.getZ()
								- cameraXform3.localToScene(
										new Point3D(0,
												1.0 * CONTROL_MULTIPLIER, 0))
										.getZ()
								+ cameraXform3.localToScene(
										new Point3D(0, 0, 0)).getZ());
					} else if (event.isAltDown()) {
						if(cameraXform.rx.getAngle() + 2.0 * ALT_MULTIPLIER >= 0)
							cameraXform.rx.setAngle(cameraXform.rx.getAngle() + 2.0
								* ALT_MULTIPLIER);
						else
							cameraXform.rx.setAngle((cameraXform.rx.getAngle() + 2.0
									* ALT_MULTIPLIER) + 360);
					} else if (event.isShiftDown()) {
						double z = camera.getTranslateZ();
						double newZ = z + 5.0 * SHIFT_MULTIPLIER;
						camera.setTranslateZ(newZ);
					}
					xFormToVar();
					setCameraTextFields();
					break;
				case DOWN:
					if (event.isControlDown() && event.isShiftDown()) {
						cameraXform.t.setX(cameraXform.t.getX()
								+ cameraXform3.localToScene(
										new Point3D(0,
												10.0 * CONTROL_MULTIPLIER, 0))
										.getX()
								- cameraXform3.localToScene(
										new Point3D(0, 0, 0)).getX());
						cameraXform.t.setY(cameraXform.t.getY()
								+ cameraXform3.localToScene(
										new Point3D(0,
												10.0 * CONTROL_MULTIPLIER, 0))
										.getY()
								- cameraXform3.localToScene(
										new Point3D(0, 0, 0)).getY());
						cameraXform.t.setZ(cameraXform.t.getZ()
								+ cameraXform3.localToScene(
										new Point3D(0,
												10.0 * CONTROL_MULTIPLIER, 0))
										.getZ()
								- cameraXform3.localToScene(
										new Point3D(0, 0, 0)).getZ());
					} else if (event.isAltDown() && event.isShiftDown()) {
						cameraXform.rx.setAngle((cameraXform.rx.getAngle()
							- 10.0 * ALT_MULTIPLIER) % 360);
					} else if (event.isControlDown()) {
						cameraXform.t.setX(cameraXform.t.getX()
								+ cameraXform3.localToScene(
										new Point3D(0,
												1.0 * CONTROL_MULTIPLIER, 0))
										.getX()
								- cameraXform3.localToScene(
										new Point3D(0, 0, 0)).getX());
						cameraXform.t.setY(cameraXform.t.getY()
								+ cameraXform3.localToScene(
										new Point3D(0,
												1.0 * CONTROL_MULTIPLIER, 0))
										.getY()
								- cameraXform3.localToScene(
										new Point3D(0, 0, 0)).getY());
						cameraXform.t.setZ(cameraXform.t.getZ()
								+ cameraXform3.localToScene(
										new Point3D(0,
												1.0 * CONTROL_MULTIPLIER, 0))
										.getZ()
								- cameraXform3.localToScene(
										new Point3D(0, 0, 0)).getZ());
					} else if (event.isAltDown()) {
						cameraXform.rx.setAngle((cameraXform.rx.getAngle() - 2.0
							* ALT_MULTIPLIER) % 360);
					} else if (event.isShiftDown()) {
						double z = camera.getTranslateZ();
						double newZ = z - 5.0 * SHIFT_MULTIPLIER;
						camera.setTranslateZ((newZ<0)?newZ:0);
					}
					xFormToVar();
					setCameraTextFields();
					break;
				case RIGHT:
					if (event.isControlDown() && event.isShiftDown()) {
						cameraXform.t.setX(cameraXform.t.getX()
								+ cameraXform3.localToScene(
										new Point3D(10.0 * CONTROL_MULTIPLIER,
												0, 0)).getX()
								- cameraXform3.localToScene(
										new Point3D(0, 0, 0)).getX());
						cameraXform.t.setY(cameraXform.t.getY()
								+ cameraXform3.localToScene(
										new Point3D(10.0 * CONTROL_MULTIPLIER,
												0, 0)).getY()
								- cameraXform3.localToScene(
										new Point3D(0, 0, 0)).getY());
						cameraXform.t.setZ(cameraXform.t.getZ()
								+ cameraXform3.localToScene(
										new Point3D(10.0 * CONTROL_MULTIPLIER,
												0, 0)).getZ()
								- cameraXform3.localToScene(
										new Point3D(0, 0, 0)).getZ());
					} else if (event.isAltDown() && event.isShiftDown()) {
						if(cameraXform.ry.getAngle() + 10.0 * ALT_MULTIPLIER >= 0)
							cameraXform.ry.setAngle(cameraXform.ry.getAngle()
								+ 10.0 * ALT_MULTIPLIER);
						else
							cameraXform.ry.setAngle(cameraXform.ry.getAngle()
									+ 10.0 * ALT_MULTIPLIER + 360);
					} else if (event.isControlDown()) {
						cameraXform.t.setX(cameraXform.t.getX()
								+ cameraXform3.localToScene(
										new Point3D(1.0 * CONTROL_MULTIPLIER,
												0, 0)).getX()
								- cameraXform3.localToScene(
										new Point3D(0, 0, 0)).getX());
						cameraXform.t.setY(cameraXform.t.getY()
								+ cameraXform3.localToScene(
										new Point3D(1.0 * CONTROL_MULTIPLIER,
												0, 0)).getY()
								- cameraXform3.localToScene(
										new Point3D(0, 0, 0)).getY());
						cameraXform.t.setZ(cameraXform.t.getZ()
								+ cameraXform3.localToScene(
										new Point3D(1.0 * CONTROL_MULTIPLIER,
												0, 0)).getZ()
								- cameraXform3.localToScene(
										new Point3D(0, 0, 0)).getZ());
					} else if (event.isAltDown()) {
						if(cameraXform.ry.getAngle() + 2.0 * ALT_MULTIPLIER >= 0)
							cameraXform.ry.setAngle(cameraXform.ry.getAngle() + 2.0
								* ALT_MULTIPLIER);
						else
							cameraXform.ry.setAngle((cameraXform.ry.getAngle() + 2.0
									* ALT_MULTIPLIER) + 360);
					}
					xFormToVar();
					setCameraTextFields();
					break;
				case LEFT:
					if (event.isControlDown() && event.isShiftDown()) {
						cameraXform.t.setX(cameraXform.t.getX()
								- cameraXform3.localToScene(
										new Point3D(10.0 * CONTROL_MULTIPLIER,
												0, 0)).getX()
								+ cameraXform3.localToScene(
										new Point3D(0, 0, 0)).getX());
						cameraXform.t.setY(cameraXform.t.getY()
								- cameraXform3.localToScene(
										new Point3D(10.0 * CONTROL_MULTIPLIER,
												0, 0)).getY()
								+ cameraXform3.localToScene(
										new Point3D(0, 0, 0)).getY());
						cameraXform.t.setZ(cameraXform.t.getZ()
								- cameraXform3.localToScene(
										new Point3D(10.0 * CONTROL_MULTIPLIER,
												0, 0)).getZ()
								+ cameraXform3.localToScene(
										new Point3D(0, 0, 0)).getZ());
					} else if (event.isAltDown() && event.isShiftDown()) {
						cameraXform.ry.setAngle((cameraXform.ry.getAngle()
								- 10.0 * ALT_MULTIPLIER) % 360); // -
					} else if (event.isControlDown()) {
						cameraXform.t.setX(cameraXform.t.getX()
								- cameraXform3.localToScene(
										new Point3D(1.0 * CONTROL_MULTIPLIER,
												0, 0)).getX()
								+ cameraXform3.localToScene(
										new Point3D(0, 0, 0)).getX());
						cameraXform.t.setY(cameraXform.t.getY()
								- cameraXform3.localToScene(
										new Point3D(1.0 * CONTROL_MULTIPLIER,
												0, 0)).getY()
								+ cameraXform3.localToScene(
										new Point3D(0, 0, 0)).getY());
						cameraXform.t.setZ(cameraXform.t.getZ()
								- cameraXform3.localToScene(
										new Point3D(1.0 * CONTROL_MULTIPLIER,
												0, 0)).getZ()
								+ cameraXform3.localToScene(
										new Point3D(0, 0, 0)).getZ());
					} else if (event.isAltDown()) {
						cameraXform.ry.setAngle((cameraXform.ry.getAngle() - 2.0
								* ALT_MULTIPLIER) % 360); // -
					}
					xFormToVar();
					setCameraTextFields();
					break;
				default:
					break;
				}
				
				setScale();
				CONTROL_MULTIPLIER = ctrlMult1;
				SHIFT_MULTIPLIER = shiftMult1;
			}
		});
	}

	public Parent createContent() {

		// Create and position camera
		camera = new PerspectiveCamera(false);

		// Build the Scene Graph
		scene3DRoot = new Group();
		stuffContainer = new Group();
		arrowGroup = new Group();
		arrowGroupRots = new Group();

		arrowGroupXRot = new Rotate();
		arrowGroupXRot.setAxis(Rotate.X_AXIS);

		arrowGroupYRot = new Rotate();
		arrowGroupYRot.setAxis(Rotate.Y_AXIS);

		arrowGroupZRot = new Rotate();
		arrowGroupXRot.setAxis(Rotate.Z_AXIS);

		arrowGroupRots.getTransforms().addAll(arrowGroupXRot, arrowGroupYRot,
				arrowGroupZRot);

		buildCamera();
		scene3DRoot.getChildren().add(world);
		world.getChildren().add(stuffContainer);
		
		systemGroupScale = new Scale();
		systemGroup = new Group();
		
		stuffContainer.getChildren().add(systemGroup);
		systemGroup.getTransforms().add(systemGroupScale);
		
		orbitMesh.setDepthTest(DepthTest.ENABLE);
		orbitMesh2.setDepthTest(DepthTest.ENABLE);
		
		systemGroup.getChildren().add(orbitMesh);
		systemGroup.getChildren().add(orbitMesh2);
		
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++) {
				arrowBoxes.add(new ArrowBox(9 * KM * (i - 4), 9 * KM * (j - 4), 0));
				arrowBoxes.get(i * 9 + j).boxXform.setRz(Math.toDegrees(Math
						.atan2(j - 4, i - 4)));
				arrowBoxes.get(i * 9 + j).tipXform.setRz(Math.toDegrees(Math
						.atan2(j - 4, i - 4)));
				arrowBoxes.get(i * 9 + j).scaleAll(9 * KM / 7);
				arrowBoxes.get(i * 9 + j).box.setMaterial(arrowMaterial);
				arrowBoxes.get(i * 9 + j).tipView.setMaterial(arrowMaterial);
				arrowGroup.getChildren()
						.add(arrowBoxes.get(i * 9 + j).boxXform);
				arrowGroup.getChildren().add(arrowBoxes.get(i * 9 + j).tipXform);
			}
		
		arrowGroup.setDepthTest(DepthTest.ENABLE);
		stuffContainer.getChildren().add(arrowGroup);
		orbitMesh.setCullFace(CullFace.NONE);
		systemGroup.getChildren().add(testSphere);
		systemGroup.getChildren().add(testSphere2);

		// detector shape initialisation
		detectorScaleFactor = 1;
		arm1Length = 100 * KM;
		arm2Length = 100 * KM;
		arm1Angle = 0;
		arm2Angle = 90;
		xDetector = 20 * KM;
		yDetector = 15 * KM;
		zDetector = 0;
		xRotDetector = 0;
		yRotDetector = 0;
		zRotDetector = 0;
		
		detectorBoxScale = new Scale();
		detectorBoxScale.setX(KM);
		detectorBoxScale.setY(KM);
		detectorBoxScale.setZ(KM);

		detectorA = new Box(DETECTOR_SIZE, DETECTOR_SIZE, DETECTOR_SIZE);
		detectorA.setMaterial(new PhongMaterial(Color.SILVER));
		detectorA.getTransforms().add(detectorBoxScale);

		detectorB = new Box(DETECTOR_SIZE, DETECTOR_SIZE, DETECTOR_SIZE);
		detectorB.setMaterial(new PhongMaterial(Color.SILVER));
		detectorB.setTranslateX(arm1Length);
		detectorBRotate = new Rotate();
		detectorB.getTransforms().add(detectorBRotate);
		detectorBRotate.setAxis(Rotate.Z_AXIS);
		detectorBRotate.setPivotX(-arm1Length);
		detectorB.getTransforms().add(detectorBoxScale);

		detectorC = new Box(DETECTOR_SIZE, DETECTOR_SIZE, DETECTOR_SIZE);
		detectorC.setMaterial(new PhongMaterial(Color.SILVER));
		detectorC.setTranslateX(arm2Length);
		detectorCRotate = new Rotate();
		detectorC.getTransforms().add(detectorCRotate);
		detectorCRotate.setAxis(Rotate.Z_AXIS);
		detectorCRotate.setPivotX(-arm2Length);
		detectorC.getTransforms().add(detectorBoxScale);
		
		armCylinderScale = new Scale();
		armCylinderScale.setZ(KM);
		armCylinderScale.setX(KM);

		arm1Cylinder = new Cylinder(1, arm1Length, 4);
		cylinderMaterial = new PhongMaterial(Color.RED);
		cylinderMaterial.setSelfIlluminationMap(new Image("/img/red.png"));
		arm1Cylinder.setMaterial(cylinderMaterial);

		arm1Cylinder.setTranslateX(arm1Length / 2);
		
		arm1CylinderRotate = new Rotate();
		rot90 = new Rotate();
		rot90.setAxis(Rotate.Z_AXIS);
		rot90.setAngle(90);

		
		arm1Cylinder.getTransforms().add(arm1CylinderRotate);
		arm1Cylinder.getTransforms().add(rot90);
		arm1Cylinder.getTransforms().add(armCylinderScale);
		
		arm1CylinderRotate.setAxis(Rotate.Z_AXIS);
		arm1CylinderRotate.setPivotX(-arm1Length / 2);

		arm2Cylinder = new Cylinder(1, arm2Length, 4);
		arm2Cylinder.setMaterial(cylinderMaterial);
		arm2Cylinder.setTranslateX(arm2Length / 2);
		
		arm2CylinderRotate = new Rotate();

		
		arm2Cylinder.getTransforms().add(arm2CylinderRotate);
		arm2Cylinder.getTransforms().add(rot90);
		arm2Cylinder.getTransforms().add(armCylinderScale);
		
		arm2CylinderRotate.setAxis(Rotate.Z_AXIS);
		arm2CylinderRotate.setPivotX(-arm2Length / 2);

		detectorGroup = new Group();

		detectorGroup.getChildren().add(arm1Cylinder);
		detectorGroup.getChildren().add(arm2Cylinder);
		detectorGroup.getChildren().add(detectorA);
		detectorGroup.getChildren().add(detectorB);
		detectorGroup.getChildren().add(detectorC);

		detectorGroupRotX = new Rotate();
		detectorGroupRotX.setAxis(Rotate.X_AXIS);

		detectorGroupRotY = new Rotate();
		detectorGroupRotY.setAxis(Rotate.Y_AXIS);

		detectorGroupRotZ = new Rotate();
		detectorGroupRotZ.setAxis(Rotate.Z_AXIS);

		detectorGroup.getTransforms().addAll(detectorGroupRotX,
				detectorGroupRotY, detectorGroupRotZ);

		detectorScale = new Scale();
		detectorGroup.getTransforms().add(detectorScale);

		setDetector();
		setDetectorTextFields();

		stuffContainer.getChildren().add(detectorGroup);

		// colormap

		fieldColormap = new MeshView(new ColormapPlane());

		fieldColormapScale = new Scale();
		fieldColormapScale.setPivotX(0);
		fieldColormapScale.setPivotY(0);
		fieldColormapScale.setX(81);
		fieldColormapScale.setY(81);

		fieldColormapXRot = new Rotate();
		fieldColormapXRot.setAxis(Rotate.X_AXIS);
		fieldColormapYRot = new Rotate();
		fieldColormapYRot.setAxis(Rotate.Y_AXIS);
		fieldColormapZRot = new Rotate();
		fieldColormapZRot.setAxis(Rotate.Z_AXIS);

		colormapTexture = new WritableImage(cmapNumXPoints, cmapNumYPoints);
		cmapWriter = colormapTexture.getPixelWriter();
		
		colormapMaterial = new PhongMaterial(Color.RED, colormapTexture, null,
				null, null);
		colormapMaterial.setSelfIlluminationMap(colormapTexture);
		fieldColormap.setMaterial(colormapMaterial);
		fieldColormap.setCullFace(CullFace.NONE);

		fieldColormap.getTransforms().addAll(fieldColormapXRot,
				fieldColormapYRot, fieldColormapZRot);
		fieldColormap.getTransforms().add(fieldColormapScale);

		fieldColormap.setVisible(false);

		stuffContainer.getChildren().add(fieldColormap);
		for (int i=0;i<cmapNumXPoints;i++)
			for (int j=0;j<cmapNumYPoints;j++){
				
				cmapWriter.setColor(i, j, Color.rgb(100, 100, 100));
			}

		return scene3DRoot;
	}
	
	public Parent createColorScaleScene(){
		
		colorScaleRoot = new Group();
		colorScaleView = new ImageView();
		colorScaleReader = (new Image("/img/colormap.png")).getPixelReader();
		colorScaleImageHeight = (new Image("/img/colormap.png")).getHeight();
		colorScaleView.setImage(new Image("/img/colormap.png"));
		/*colorScaleScale = new Scale();
		colorScaleTranslate = new Translate();
		colorScaleView.getTransforms().add(colorScaleTranslate);
		colorScaleView.getTransforms().add(colorScaleRotate);
		colorScaleView.getTransforms().add(colorScaleScale);*/
		colorScaleRoot.getChildren().add(colorScaleView);
		
		return colorScaleRoot;
	}

	public void updateSphere(double x, double y) {

		testSphere.setTranslateX(x * (distance * mass2 / (mass1 + mass2)));
		testSphere.setTranslateY(y * (distance * mass2 / (mass1 + mass2)));
		testSphere2.setTranslateX(-x * (distance * mass1 / (mass1 + mass2)));
		testSphere2.setTranslateY(-y * (distance * mass1 / (mass1 + mass2)));
	}
	
	public int doCutoff(int amp){
		
		if(amp<0)
			return 0;
		if(amp>colorScaleImageHeight-1)
			return (int)(colorScaleImageHeight-1);
		return amp;
	}
	
	public void updateColorField1(){
		
		double amplitude;
		Point3D p1 = fieldColormap.localToScene(new Point3D(((TriangleMesh)
						(fieldColormap.getMesh())).getPoints().get(0),
						((TriangleMesh) (fieldColormap.getMesh())).getPoints().get(1),
						((TriangleMesh) (fieldColormap.getMesh())).getPoints().get(2)));
		
		Point3D p2 = fieldColormap.localToScene(new Point3D(((TriangleMesh)
				(fieldColormap.getMesh())).getPoints().get(3),
				((TriangleMesh) (fieldColormap.getMesh())).getPoints().get(4),
				((TriangleMesh) (fieldColormap.getMesh())).getPoints().get(5)));
		
		Point3D p3 = fieldColormap.localToScene(new Point3D(((TriangleMesh)
				(fieldColormap.getMesh())).getPoints().get(6),
				((TriangleMesh) (fieldColormap.getMesh())).getPoints().get(7),
				((TriangleMesh) (fieldColormap.getMesh())).getPoints().get(8)));
		
		double x1 = p1.getX(), y1 = p1.getY(), z1 = p1.getZ();
		double x2 = p2.getX(), y2 = p2.getY(), z2 = p2.getZ();
		double x3 = p3.getX(), y3 = p3.getY(), z3 = p3.getZ();
		
		for (int i=0;i<cmapNumYPoints;i++)
			for (int j=0;j<cmapNumXPoints;j++){
				
				amplitude = getAmplitude1((millisTime
						- millisTimeLastTimeStretchChange) / 1000 * timeStretchFactor,
						x1 + ((x2 - x1) / cmapNumXPoints)
						* (j + 0.5) + ((x3 - x1) / cmapNumYPoints) * (i + 0.5),
						y1 + ((y2 - y1) / cmapNumXPoints) * (j + 0.5) + ((y3 - y1)
							/ cmapNumYPoints) * (i + 0.5), z1 + ((z2 - z1) / cmapNumXPoints)
							* (j + 0.5) + ((z3 - z1) / cmapNumYPoints) * (i + 0.5)) / 2 + 0.5;
				
				cmapWriter.setColor(j, i, colorScaleReader.getColor(0,
						doCutoff((int)(colorScaleImageHeight*amplitude))));
				/*Color.rgb(doCutoff((int)(amplitude* 256)),
						doCutoff((int)(amplitude* 256)),
						doCutoff((int)(amplitude* 256))));*/

			}
		
		fieldColormap.setMaterial(colormapMaterial);

	}

	public void updateColorField() {

		ObservableFloatArray texCoords = ((TriangleMesh) (fieldColormap
				.getMesh())).getTexCoords();
		ObservableFloatArray points = ((TriangleMesh) (fieldColormap
				.getMesh())).getPoints();
		Point3D meshPoint;

		for (int i = 0; i < texCoords.size(); i++) {
			
			meshPoint = fieldColormap.localToScene(new Point3D(points.get(i/2*3),
					points.get(i/2*3+1), points.get(i/2*3+2)));
			texCoords.set(i, (float) (getAmplitude1((millisTime
					- millisTimeLastTimeStretchChange) / 1000 * timeStretchFactor,
					meshPoint.getX(),
					meshPoint.getY(), meshPoint.getZ())
					/ 2 + 0.5));
			texCoords.set(i + 1, (float) 0.5);
			i++;
		}

	}

	public void updatePos() {
		
		millisTime = System.currentTimeMillis();
		angle = Math.toDegrees(((millisTime - millisTimeLastTimeStretchChange) / 1000
				* timeStretchFactor)*omega);
		if (isFieldVisible)
			if (arrowsField.isSelected()){
				updateArrows();
			}else{
				updateColorField1();}

		updateSphere((Math.cos(Math.toRadians(angle))),
				((Math.sin(Math.toRadians(angle)))));
		if(showPatternChk.isSelected()){
			drawPattern();
		}
	}

	private void buildCamera() {
		scene3DRoot.getChildren().add(cameraXform);
		cameraXform.getChildren().add(cameraXform2);
		cameraXform2.getChildren().add(cameraXform3);
		cameraXform3.getChildren().add(camera);

		camera.setNearClip(0.1);
		camera.setFarClip(Double.MAX_VALUE);
		camera.setTranslateZ(-200 * KM);
		cameraXform.ry.setAngle(0);
		cameraXform.rx.setAngle(0);

		xFormToVar();
		setCameraTextFields();
	}

	// from Controller

	private final ChangeListener<Boolean> starFocusListener = new ChangeListener<Boolean>() {

		@Override
		public void changed(ObservableValue<? extends Boolean> observable,
				Boolean oldValue, Boolean newValue) {
			if (newValue == false)
				updateStars();
		}
	};
	
	private final ChangeListener<Boolean> timeStretchFocusListener = new ChangeListener<Boolean>() {

		@Override
		public void changed(ObservableValue<? extends Boolean> observable,
				Boolean oldValue, Boolean newValue) {
			if (newValue == false)
				updateTimeStretch();
		}
	};

	private final ChangeListener<Boolean> fieldFocusListener = new ChangeListener<Boolean>() {

		@Override
		public void changed(ObservableValue<? extends Boolean> observable,
				Boolean oldValue, Boolean newValue) {
			if (newValue == false)
				updatePoints();
		}
	};

	private final ChangeListener<String> starUnitListener = new ChangeListener<String>() {

		@Override
		public void changed(ObservableValue<? extends String> observable,
				String oldValue, String newValue) {
			updateStarUnits();
		}
	};

	private final ChangeListener<String> fieldUnitListener = new ChangeListener<String>() {

		@Override
		public void changed(ObservableValue<? extends String> observable,
				String oldValue, String newValue) {
			updateFieldUnits();
		}
	};

	private final ChangeListener<Boolean> cameraFocusListener = new ChangeListener<Boolean>() {

		@Override
		public void changed(ObservableValue<? extends Boolean> observable,
				Boolean oldValue, Boolean newValue) {
			if (newValue == false)
				updateCamera();
		}
	};

	private final ChangeListener<String> cameraUnitListener = new ChangeListener<String>() {

		@Override
		public void changed(ObservableValue<? extends String> observable,
				String oldValue, String newValue) {
			updateCameraUnits();
		}
	};

	private final ChangeListener<Boolean> detectorFocusListener = new ChangeListener<Boolean>() {

		@Override
		public void changed(ObservableValue<? extends Boolean> observable,
				Boolean oldValue, Boolean newValue) {
			if (newValue == false)
				updateDetector();
		}
	};

	private final ChangeListener<String> detectorUnitListener = new ChangeListener<String>() {

		@Override
		public void changed(ObservableValue<? extends String> observable,
				String oldValue, String newValue) {
			updateDetectorUnits();
		}
	};

	private final ChangeListener<String> movePivotUnitListener = new ChangeListener<String>() {

		@Override
		public void changed(ObservableValue<? extends String> observable,
				String oldValue, String newValue) {
			updateMovePivotUnits();
		}
	};
	
	private final ChangeListener<String> laserUnitListener = new ChangeListener<String>() {

		@Override
		public void changed(ObservableValue<? extends String> observable,
				String oldValue, String newValue) {
			updateLaserUnits();
		}
	};
	
	private final ChangeListener<Boolean> laserFocusListener = new ChangeListener<Boolean>() {

		@Override
		public void changed(ObservableValue<? extends Boolean> observable,
				Boolean oldValue, Boolean newValue) {
			if (newValue == false)
				updateLaser();
		}
	};
	
	private final ChangeListener<Number> scenesBorderPaneSizeListener = new ChangeListener<Number>() {
		
		@Override
		public void changed(ObservableValue<? extends Number> observable,
				Number oldValue, Number newValue) {
			
			updateColorScale();
		}
	};

	/**
	 * Initializes the controller class.
	 */

	@Override
	public void initialize(URL url, ResourceBundle rsrcs) {

		assertFXMLTagsInjected();

		star1Mass.setText(formatDouble(mass1/SM));
		star1Mass.focusedProperty().addListener(starFocusListener);

		star2Mass.setText(formatDouble(mass2/SM));
		star2Mass.focusedProperty().addListener(starFocusListener);

		star1Radius.setText(formatDouble(radius1/KM));
		star1Radius.focusedProperty().addListener(starFocusListener);

		star2Radius.setText(formatDouble(radius2/KM));
		star2Radius.focusedProperty().addListener(starFocusListener);
		
		systemScaleText.setText(formatDouble(systemScaleFactor2));
		systemScaleText.focusedProperty().addListener(starFocusListener);
		
		timeStretchText.setText(formatDouble(timeStretchFactor));
		timeStretchText.focusedProperty().addListener(timeStretchFocusListener);

		numXPointsBox.focusedProperty().addListener(fieldFocusListener);
		numYPointsBox.focusedProperty().addListener(fieldFocusListener);
		numZPointsBox.focusedProperty().addListener(fieldFocusListener);

		xSeparation.focusedProperty().addListener(fieldFocusListener);
		ySeparation.focusedProperty().addListener(fieldFocusListener);
		zSeparation.focusedProperty().addListener(fieldFocusListener);

		xAxisRot.setText(formatDouble(xFieldRot));
		yAxisRot.setText(formatDouble(yFieldRot));
		zAxisRot.setText(formatDouble(zFieldRot));

		xAxisRot.focusedProperty().addListener(fieldFocusListener);
		yAxisRot.focusedProperty().addListener(fieldFocusListener);
		zAxisRot.focusedProperty().addListener(fieldFocusListener);

		star1RadiusUnits.getItems().addAll("m", "km", "au", "ly", "pc");
		star1RadiusUnits.setValue("km");
		star1RadiusUnits.valueProperty().addListener(starUnitListener);

		star2RadiusUnits.getItems().addAll("m", "km", "au", "ly", "pc");
		star2RadiusUnits.setValue("km");
		star2RadiusUnits.valueProperty().addListener(starUnitListener);

		star1MassUnits.getItems().addAll("kg", "t", "M☉");
		star1MassUnits.setValue("M☉");
		star1MassUnits.valueProperty().addListener(starUnitListener);

		star2MassUnits.getItems().addAll("kg", "t", "M☉");
		star2MassUnits.setValue("M☉");
		star2MassUnits.valueProperty().addListener(starUnitListener);

		xSeparationUnits.getItems().addAll("m", "km", "au", "ly", "pc");
		xSeparationUnits.setValue("km");
		xSeparationUnits.valueProperty().addListener(fieldUnitListener);

		ySeparationUnits.getItems().addAll("m", "km", "au", "ly", "pc");
		ySeparationUnits.setValue("km");
		ySeparationUnits.valueProperty().addListener(fieldUnitListener);

		zSeparationUnits.getItems().addAll("m", "km", "au", "ly", "pc");
		zSeparationUnits.setValue("km");
		zSeparationUnits.valueProperty().addListener(fieldUnitListener);

		xAxisRotUnits.getItems().addAll("°", "rad");
		xAxisRotUnits.setValue("°");
		xAxisRotUnits.valueProperty().addListener(fieldUnitListener);

		yAxisRotUnits.getItems().addAll("°", "rad");
		yAxisRotUnits.setValue("°");
		yAxisRotUnits.valueProperty().addListener(fieldUnitListener);

		zAxisRotUnits.getItems().addAll("°", "rad");
		zAxisRotUnits.setValue("°");
		zAxisRotUnits.valueProperty().addListener(fieldUnitListener);

		distanceUnits.getItems().addAll("m", "km", "au", "ly", "pc");
		distanceUnits.setValue("km");
		distanceUnits.valueProperty().addListener(starUnitListener);

		distanceText.setText(formatDouble(distance/KM));

		cameraDistance.focusedProperty().addListener(cameraFocusListener);

		cameraXAxisRot.focusedProperty().addListener(cameraFocusListener);
		cameraYAxisRot.focusedProperty().addListener(cameraFocusListener);

		cameraXPivot.focusedProperty().addListener(cameraFocusListener);
		cameraYPivot.focusedProperty().addListener(cameraFocusListener);
		cameraZPivot.focusedProperty().addListener(cameraFocusListener);
		
		distMap.put("cm", CM);
		distMap.put("mm", MM);
		distMap.put("µm", UM);
		distMap.put("nm", NM);
		distMap.put("pm", PM);
		distMap.put("m", M);
		distMap.put("km", KM);
		distMap.put("au", AU);
		distMap.put("pc", PC);
		distMap.put("ly", LY);
		
		distInvMap.put(CM, "cm");
		distInvMap.put(MM, "mm");
		distInvMap.put(UM, "µm");
		distInvMap.put(NM, "nm");
		distInvMap.put(PM, "pm");
		distInvMap.put(M, "m");
		distInvMap.put(KM, "km");
		distInvMap.put(AU, "au");
		distInvMap.put(PC, "pc");
		distInvMap.put(LY, "ly");

		massMap.put("kg", KG);
		massMap.put("t", T);
		massMap.put("M☉", SM);

		angleMap.put("°", DEG);
		angleMap.put("rad", RAD);

		angleInvMap.put(DEG, "°");
		angleInvMap.put(RAD, "rad");

		cameraDistanceUnits.getItems().addAll("m", "km", "au", "ly", "pc");
		cameraDistanceUnits.setValue("km");
		cameraDistanceUnits.valueProperty().addListener(cameraUnitListener);

		cameraXPivotUnits.getItems().addAll("m", "km", "au", "ly", "pc");
		cameraXPivotUnits.setValue("km");
		cameraXPivotUnits.valueProperty().addListener(cameraUnitListener);

		cameraYPivotUnits.getItems().addAll("m", "km", "au", "ly", "pc");
		cameraYPivotUnits.setValue("km");
		cameraYPivotUnits.valueProperty().addListener(cameraUnitListener);

		cameraZPivotUnits.getItems().addAll("m", "km", "au", "ly", "pc");
		cameraZPivotUnits.setValue("km");
		cameraZPivotUnits.valueProperty().addListener(cameraUnitListener);

		cameraXAxisRotUnits.getItems().addAll("°", "rad");
		cameraXAxisRotUnits.setValue("°");
		cameraXAxisRotUnits.valueProperty().addListener(cameraUnitListener);

		cameraYAxisRotUnits.getItems().addAll("°", "rad");
		cameraYAxisRotUnits.setValue("°");
		cameraYAxisRotUnits.valueProperty().addListener(cameraUnitListener);

		inputUnits.getItems().addAll("m", "km", "au", "ly", "pc");
		inputUnits.setValue("km");
		inputUnits.valueProperty().addListener(movePivotUnitListener);
		
		//detector
		
		detectorXPos.setText(formatDouble(xDetector));
		detectorXPos.focusedProperty().addListener(detectorFocusListener);

		detectorYPos.setText(formatDouble(yDetector));
		detectorYPos.focusedProperty().addListener(detectorFocusListener);

		detectorZPos.setText(formatDouble(zDetector));
		detectorZPos.focusedProperty().addListener(detectorFocusListener);

		arm1LengthBox.setText(formatDouble(arm1Length));
		arm1LengthBox.focusedProperty().addListener(detectorFocusListener);

		arm1AngleBox.setText(formatDouble(arm1Angle));
		arm1AngleBox.focusedProperty().addListener(detectorFocusListener);

		arm2LengthBox.setText(formatDouble(arm2Length));
		arm2LengthBox.focusedProperty().addListener(detectorFocusListener);

		arm2AngleBox.setText(formatDouble(arm2Angle));
		arm2AngleBox.focusedProperty().addListener(detectorFocusListener);
		
		scaleOverride.setText(formatDouble(detectorScaleFactor));
		scaleOverride.focusedProperty().addListener(detectorFocusListener);

		detectorXAxisRot.setText(formatDouble(xRotDetector));
		detectorXAxisRot.focusedProperty().addListener(detectorFocusListener);

		detectorYAxisRot.setText(formatDouble(xRotDetector));
		detectorYAxisRot.focusedProperty().addListener(detectorFocusListener);

		detectorZAxisRot.setText(formatDouble(xRotDetector));
		detectorZAxisRot.focusedProperty().addListener(detectorFocusListener);

		detectorXPosUnits.getItems().addAll("m", "km", "au", "ly", "pc");
		detectorXPosUnits.setValue("km");
		detectorXPosUnits.valueProperty().addListener(detectorUnitListener);

		detectorYPosUnits.getItems().addAll("m", "km", "au", "ly", "pc");
		detectorYPosUnits.setValue("km");
		detectorYPosUnits.valueProperty().addListener(detectorUnitListener);

		detectorZPosUnits.getItems().addAll("m", "km", "au", "ly", "pc");
		detectorZPosUnits.setValue("km");
		detectorZPosUnits.valueProperty().addListener(detectorUnitListener);

		arm1LengthUnitsBox.getItems().addAll("m", "km", "au", "ly", "pc");
		arm1LengthUnitsBox.setValue("km");
		arm1LengthUnitsBox.valueProperty().addListener(detectorUnitListener);

		arm1AngleUnitsBox.getItems().addAll("°", "rad");
		arm1AngleUnitsBox.setValue("°");
		arm1AngleUnitsBox.valueProperty().addListener(detectorUnitListener);

		arm2LengthUnitsBox.getItems().addAll("m", "km", "au", "ly", "pc");
		arm2LengthUnitsBox.setValue("km");
		arm2LengthUnitsBox.valueProperty().addListener(detectorUnitListener);

		arm2AngleUnitsBox.getItems().addAll("°", "rad");
		arm2AngleUnitsBox.setValue("°");
		arm2AngleUnitsBox.valueProperty().addListener(detectorUnitListener);

		detectorXAxisRotUnits.getItems().addAll("°", "rad");
		detectorXAxisRotUnits.setValue("°");
		detectorXAxisRotUnits.valueProperty().addListener(detectorUnitListener);

		detectorYAxisRotUnits.getItems().addAll("°", "rad");
		detectorYAxisRotUnits.setValue("°");
		detectorYAxisRotUnits.valueProperty().addListener(detectorUnitListener);

		detectorZAxisRotUnits.getItems().addAll("°", "rad");
		detectorZAxisRotUnits.setValue("°");
		detectorZAxisRotUnits.valueProperty().addListener(detectorUnitListener);

		// laser
		
		arm1MultText.setText(Integer.toString(arm1Mult));
		arm1MultText.focusedProperty().addListener(laserFocusListener);
		
		arm2MultText.setText(Integer.toString(arm2Mult));
		arm2MultText.focusedProperty().addListener(laserFocusListener);
		
		wavelengthText.setText(formatDouble(wavelength/wavelengthUnits));
		wavelengthText.focusedProperty().addListener(laserFocusListener);
		
		slitSeparationText.setText(formatDouble(slitSeparation/slitSeparationUnits));
		slitSeparationText.focusedProperty().addListener(laserFocusListener);
		
		screenDistText.setText(formatDouble(screenDist/screenDistUnits));
		screenDistText.focusedProperty().addListener(laserFocusListener);
		
		screenWidthText.setText(formatDouble((rightScreenLimit-leftScreenLimit)/screenWidthUnits));
		screenWidthText.focusedProperty().addListener(laserFocusListener);
		
		patternResolutionText.setText(Integer.toString(patternResolution));
		patternResolutionText.focusedProperty().addListener(laserFocusListener);
		
		wavelengthUnitsBox.getItems().addAll("mm", "µm", "nm", "pm");
		wavelengthUnitsBox.setValue("nm");
		wavelengthUnitsBox.valueProperty().addListener(laserUnitListener);
		
		slitSeparationUnitsBox.getItems().addAll("cm", "mm", "µm", "nm", "pm");
		slitSeparationUnitsBox.setValue("mm");
		slitSeparationUnitsBox.valueProperty().addListener(laserUnitListener);
		
		screenDistUnitsBox.getItems().addAll("km", "m", "cm", "mm");
		screenDistUnitsBox.setValue("m");
		screenDistUnitsBox.valueProperty().addListener(laserUnitListener);
		
		screenWidthUnitsBox.getItems().addAll("km", "m", "cm", "mm");
		screenWidthUnitsBox.setValue("cm");
		screenWidthUnitsBox.valueProperty().addListener(laserUnitListener);
		
		// stuff originally in Main

		// sphere
		testSphere = new Sphere(radius1);
		testSphere.setMaterial(new PhongMaterial(Color.RED));
		testSphere.setDrawMode(DrawMode.FILL);

		testSphere2 = new Sphere(radius2);
		testSphere2.setMaterial(new PhongMaterial(Color.RED));
		testSphere2.setDrawMode(DrawMode.FILL);

		orbitMaterial = new PhongMaterial(Color.GREEN);
		orbitMaterial.setSelfIlluminationMap(new Image("/img/green.png"));
		orbitMesh.setMaterial(orbitMaterial);
		orbitMesh.setDrawMode(DrawMode.LINE);
		
		arrowMaterial = orbitMaterial;

		orbitMesh2.setMaterial(orbitMaterial);
		orbitMesh2.setDrawMode(DrawMode.LINE);

		scene3D = new SubScene(createContent(), 600, 500, true,
				SceneAntialiasing.BALANCED);
		stackPane = new StackPane();
		stackPane.setPrefSize(600, 500);
		stackPane.getChildren().add(scene3D);
		scene3D.heightProperty().bind(stackPane.heightProperty());
		scene3D.widthProperty().bind(stackPane.widthProperty());
		stackPane.setMinSize(150, 100);
		
		createColorScaleScene();
		colorScaleStackPane.getChildren().add(colorScaleView);
		colorScaleView.setFitWidth(colorScaleStackPane.getPrefWidth());
		scenesBorderPane.heightProperty().addListener(scenesBorderPaneSizeListener);

		scenesBorderPane.setCenter(stackPane);

		scene3D.setFill(Color.BLACK);

		scene3D.setCamera(camera);
		
		deduceTimeStretch();
		scenesBorderPane.setRight(null);
		scenesBorderPane.setBottom(null);
		showColourBar.setDisable(true);
		
		patternNumsAnchor.setVisible(false);
		
		//create laser pattern
		patternImage = new WritableImage(patternResolution,1);
		patternWriter = patternImage.getPixelWriter();
		patternView = new ImageView(patternImage);
		patternView.setFitHeight(60);
		patternView.fitWidthProperty().bind(scenesBorderPane.widthProperty());
		patternStackPane.getChildren().add(patternView);
		
		//create timeline for animation
		final Duration oneFrameAmt = Duration.seconds(0.04);
		final KeyFrame oneFrame = new KeyFrame(oneFrameAmt,
				new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent evt) {
						updatePos();
					}
				});

		timer = TimelineBuilder.create().cycleCount(-1).keyFrames(oneFrame)
				.build();
		
		//start time for equations
		millisTime = System.currentTimeMillis();
		millisTimeLastTimeStretchChange = millisTime;
		
		//play animation
		timer.playFromStart();
		handleKeyboard(scene3D, world);
		handleMouse(scene3D, world);
		
		cameraControlsTitledPane.setExpanded(false);

	}

	private void assertFXMLTagsInjected() {

		// Assert all FXML Objects have been injected.

		assert detectorYAxisRotUnits != null : "fx:id=\"detectorYAxisRotUnits\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert detectorXAxisRotUnits != null : "fx:id=\"detectorXAxisRotUnits\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert detectorXPos != null : "fx:id=\"detectorXPos\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert star1RadiusUnits != null : "fx:id=\"star1RadiusUnits\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert xSeparation != null : "fx:id=\"xSeparation\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert tabDetector != null : "fx:id=\"tabDetector\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert arm2AngleUnitsBox != null : "fx:id=\"arm2AngleUnitsBox\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert tabStars != null : "fx:id=\"tabStars\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert scaleOverride != null : "fx:id=\"scaleOverride\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert arm2LengthUnitsBox != null : "fx:id=\"arm2LengthUnitsBox\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert yAxisRot != null : "fx:id=\"yAxisRot\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert detectorYPosUnits != null : "fx:id=\"detectorYPosUnits\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert xAxisRot != null : "fx:id=\"xAxisRot\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert zAxisRot != null : "fx:id=\"zAxisRot\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert detectorZAxisRotUnits != null : "fx:id=\"detectorZAxisRotUnits\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert cameraXPivotUnits != null : "fx:id=\"cameraXPivotUnits\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert ySeparation != null : "fx:id=\"ySeparation\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert cameraZPivot != null : "fx:id=\"cameraZPivot\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert detectorYPos != null : "fx:id=\"detectorYPos\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert xSeparationUnits != null : "fx:id=\"xSeparationUnits\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert arm2AngleBox != null : "fx:id=\"arm2AngleBox\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert numXPointsBox != null : "fx:id=\"numXPointsBox\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert cameraXAxisRot != null : "fx:id=\"cameraXAxisRot\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert arm1LengthBox != null : "fx:id=\"arm1LengthBox\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert arm2LengthBox != null : "fx:id=\"arm2LengthBox\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert star1Radius != null : "fx:id=\"star1Radius\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert colourMapField != null : "fx:id=\"colourMapField\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert numYPointsBox != null : "fx:id=\"numYPointsBox\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert star2RadiusUnits != null : "fx:id=\"star2RadiusUnits\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert showColourBar != null : "fx:id=\"showColourBar\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert ySeparationUnits != null : "fx:id=\"ySeparationUnits\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert arm1AngleUnitsBox != null : "fx:id=\"arm1AngleUnitsBox\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert cameraPointAtZero != null : "fx:id=\"cameraPointAtZero\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert cameraDistanceUnits != null : "fx:id=\"cameraDistanceUnits\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert useEqualDensities != null : "fx:id=\"useEqualDensities\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert detectorZAxisRot != null : "fx:id=\"detectorZAxisRot\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert detectorYAxisRot != null : "fx:id=\"detectorYAxisRot\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert zSeparation != null : "fx:id=\"zSeparation\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert star2Mass != null : "fx:id=\"star2Mass\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert cameraYPivotUnits != null : "fx:id=\"cameraYPivotUnits\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert detectorXAxisRot != null : "fx:id=\"detectorXAxisRot\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert detectorShow != null : "fx:id=\"detectorShow\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert cameraYAxisRotUnits != null : "fx:id=\"cameraYAxisRotUnits\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert detectorZPos != null : "fx:id=\"detectorZPos\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert detectorReset != null : "fx:id=\"detectorReset\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert cameraXPivot != null : "fx:id=\"cameraXPivot\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert cameraXAxisRotUnits != null : "fx:id=\"cameraXAxisRotUnits\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert cameraDistance != null : "fx:id=\"cameraDistance\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert tabCamera != null : "fx:id=\"tabCamera\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert star1Mass != null : "fx:id=\"star1Mass\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert zSeparationUnits != null : "fx:id=\"zSeparationUnits\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert inputUnits != null : "fx:id=\"inputUnits\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert yAxisRotUnits != null : "fx:id=\"yAxisRotUnits\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert cameraReset != null : "fx:id=\"cameraReset\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert cameraYPivot != null : "fx:id=\"cameraYPivot\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert detectorZPosUnits != null : "fx:id=\"detectorZPosUnits\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert star1MassUnits != null : "fx:id=\"star1MassUnits\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert showField != null : "fx:id=\"showField\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert arrowsField != null : "fx:id=\"arrowsField\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert zAxisRotUnits != null : "fx:id=\"zAxisRotUnits\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert distanceText != null : "fx:id=\"distanceText\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert numZPointsBox != null : "fx:id=\"numZPointsBox\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert detectorXPosUnits != null : "fx:id=\"detectorXPosUnits\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert arm1LengthUnitsBox != null : "fx:id=\"arm1LengthUnitsBox\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert rootBorder != null : "fx:id=\"rootBorder\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert star2MassUnits != null : "fx:id=\"star2MassUnits\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert cameraYAxisRot != null : "fx:id=\"cameraYAxisRot\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert cameraZPivotUnits != null : "fx:id=\"cameraZPivotUnits\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert tabField != null : "fx:id=\"tabField\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert arm1AngleBox != null : "fx:id=\"arm1AngleBox\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert xAxisRotUnits != null : "fx:id=\"xAxisRotUnits\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert star2Radius != null : "fx:id=\"star2Radius\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert showOrbits != null : "fx:id=\"showOrbits\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert distanceUnits != null : "fx:id=\"distanceUnits\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert fieldTypeGroup != null : "fx:id=\"fieldTypeGroup\" was not injected: check your FXML file 'leftpane.fxml'.";
		assert tabPane != null : "fx:id=\"tabPane\" was not injected: check your FXML file 'leftpane.fxml'.";

		System.out.println("All FXML objects injected successfully.");

	}
	
	//update the colour scale thing on the right; called when its size must change
	//(ex. when the window size changes)
	public void updateColorScale(){
		
		colorScaleView.setFitHeight(scenesBorderPane.getHeight()-amplitudeImage.getFitHeight()
				-patternAnchor.getHeight());
		colorScaleMinLabel.setLayoutY(colorScaleMaxLabel.getLayoutY()+
				colorScaleView.getFitHeight()-101);
		colorScaleCenterLabel.setLayoutY((colorScaleMinLabel.getLayoutY()+
				colorScaleMaxLabel.getLayoutY())/2);
		
		colorScaleMinLabel.setText(formatDouble(-getMaxAmplitude()));
		colorScaleMaxLabel.setText(formatDouble(getMaxAmplitude()));
	}
	
	//called when a text field in the Field tab is edited; chooses what to update
	//based on which visualisation method is selected
	public void updatePoints() {

		if (arrowsField.isSelected())
			updateArrowPoints();
		else
			updateColormapField();
	}
	
	//called when a text field in the Field tab is edited, when "Arrows" is the
	//current visualisation method
	public void updateArrowPoints() {

		try {
			int numXPointsNew, numYPointsNew, numZPointsNew;
			numXPointsNew = (int) (Double.parseDouble(numXPointsBox.getText()));
			numYPointsNew = (int) (Double.parseDouble(numYPointsBox.getText()));
			numZPointsNew = (int) (Double.parseDouble(numZPointsBox.getText()));

			double xDistNew = Double.parseDouble(xSeparation.getText())
					* xDistUnits;
			double yDistNew = Double.parseDouble(ySeparation.getText())
					* yDistUnits;
			double zDistNew = Double.parseDouble(zSeparation.getText())
					* zDistUnits;

			double xFieldRotNew = Double.parseDouble(xAxisRot.getText())
					* xAngleUnits;
			double yFieldRotNew = Double.parseDouble(yAxisRot.getText())
					* yAngleUnits;
			double zFieldRotNew = Double.parseDouble(zAxisRot.getText())
					* zAngleUnits;

			if (numXPointsNew < 0 || numYPointsNew < 0 || numZPointsNew < 0
					|| xDistNew < 0 || yDistNew < 0 || zDistNew < 0) {

				numXPointsBox.setText(Integer.toString(numXPoints));
				numYPointsBox.setText(Integer.toString(numYPoints));
				numZPointsBox.setText(Integer.toString(numZPoints));

				xSeparation.setText(formatDouble(xDist / xDistUnits));
				ySeparation.setText(formatDouble(yDist / yDistUnits));
				zSeparation.setText(formatDouble(zDist / zDistUnits));

				return;
			}

			if (numXPoints != numXPointsNew || numYPoints != numYPointsNew
					|| numZPoints != numZPointsNew || xDist != xDistNew
					|| yDist != yDistNew || zDist != zDistNew
					|| xFieldRotNew != xFieldRot || yFieldRotNew != yFieldRot
					|| zFieldRotNew != zFieldRot) {

				numXPoints = numXPointsNew;
				numYPoints = numYPointsNew;
				numZPoints = numZPointsNew;

				xDist = xDistNew;
				yDist = yDistNew;
				zDist = zDistNew;

				xFieldRot = xFieldRotNew;
				yFieldRot = yFieldRotNew;
				zFieldRot = zFieldRotNew;

				updateField();
			}

		} catch (Exception E) {
		}

		numXPointsBox.setText(Integer.toString(numXPoints));
		numYPointsBox.setText(Integer.toString(numYPoints));
		numZPointsBox.setText(Integer.toString(numZPoints));

		xSeparation.setText(formatDouble(xDist / xDistUnits));
		ySeparation.setText(formatDouble(yDist / yDistUnits));
		zSeparation.setText(formatDouble(zDist / zDistUnits));

		xAxisRot.setText(formatDouble(xFieldRot / xAngleUnits));
		yAxisRot.setText(formatDouble(yFieldRot / yAngleUnits));
		zAxisRot.setText(formatDouble(zFieldRot / zAngleUnits));
	}
	
	//show/hide the circles showing the pulsar orbits
	public void toggleShowOrbits() {

		setOrbitsVisibility(showOrbits.isSelected());
	}
	
	//hide/show the current gravitational wave visualization method (arrows/colormap)
	public void toggleShowField() {

		setFieldVisibility(showField.isSelected());
		if(showField.isSelected()&&!arrowsField.isSelected()){
			scenesBorderPane.setRight(colorScaleAnchor);
			showColourBar.setDisable(false);
		}else{
			scenesBorderPane.setRight(null);
			showColourBar.setDisable(true);
		}
		
		if(showColourBar.isSelected())
			scenesBorderPane.setRight(colorScaleAnchor);
		else
			scenesBorderPane.setRight(null);
		
	}

	public void toggleShowColorbar() {
		
		if(showColourBar.isSelected())
			scenesBorderPane.setRight(colorScaleAnchor);
		else
			scenesBorderPane.setRight(null);
	}
	
	//called when a text field in the Stars tab is edited
	public void updateStars() {

		try {
			double newMass1 = Double.parseDouble(star1Mass.getText())
					* mass1Units;
			double newMass2 = Double.parseDouble(star2Mass.getText())
					* mass2Units;
			double newRadius1 = Double.parseDouble(star1Radius.getText())
					* radius1Units;
			double newRadius2 = Double.parseDouble(star2Radius.getText())
					* radius2Units;
			double newDistance = Double.parseDouble(distanceText.getText())
					* distUnits;
			
			double newSystemScaleFactor = Double.parseDouble(systemScaleText.getText());

			if (newMass1 < 0 || newMass2 < 0 || newRadius1 < 0
					|| newRadius2 < 0 || newDistance < 0 || newSystemScaleFactor < 0) {

				star1Mass.setText(formatDouble(mass1 / mass1Units));
				star2Mass.setText(formatDouble(mass2 / mass2Units));
				star1Radius.setText(formatDouble(radius1 / radius1Units));
				star2Radius.setText(formatDouble(radius2 / radius2Units));
				distanceText.setText(formatDouble(distance / distUnits));
				
				if(isScaledToCamera)
					systemScaleText.setText(formatDouble(systemScaleFactor
							/ Math.atan(distance/2/-distCamera)));
				else
					systemScaleText.setText(formatDouble(systemScaleFactor2));

				return;
			}
			
			if (isScaledToCamera){
				if (newSystemScaleFactor != systemScaleFactor
						/ Math.atan(distance/2/-distCamera)){
					
					systemScaleFactor = newSystemScaleFactor *
							Math.atan(distance/2/-distCamera);
					setStars();
				}
			}
			else{
				if(newSystemScaleFactor != systemScaleFactor2){
					systemScaleFactor2 = newSystemScaleFactor;
					setStars();
				}
			}

			if (mass1 != newMass1 || mass2 != newMass2 || radius1 != newRadius1
					|| radius2 != newRadius2 || distance != newDistance) {

				distance = newDistance;

				if (isStarDensityEqual) {
					if (radius1 != newRadius1 || mass1 != newMass1) {
						mass2 = newMass2;
						mass1 = newMass1;
						radius1 = newRadius1;
						radius2 = radius1 * Math.pow(mass2 / mass1, 1.0 / 3);
					} else {
						mass2 = newMass2;
						mass1 = newMass1;
						radius2 = newRadius2;
						radius1 = radius2 * Math.pow(mass1 / mass2, 1.0 / 3);
					}
				} else {
					mass2 = newMass2;
					mass1 = newMass1;
					radius2 = newRadius2;
					radius1 = newRadius1;
				}

				setStars();
			}

			star1Mass.setText(formatDouble(mass1 / mass1Units));
			star2Mass.setText(formatDouble(mass2 / mass2Units));
			star1Radius.setText(formatDouble(radius1 / radius1Units));
			star2Radius.setText(formatDouble(radius2 / radius2Units));
			distanceText.setText(formatDouble(distance / distUnits));
			
			if(isScaledToCamera)
				systemScaleText.setText(formatDouble(systemScaleFactor
						/ Math.atan(distance/2/-distCamera)));
			else
				systemScaleText.setText(formatDouble(systemScaleFactor2));

		} catch (Exception E) {
		}

		star1Mass.setText(formatDouble(mass1 / mass1Units));
		star2Mass.setText(formatDouble(mass2 / mass2Units));
		star1Radius.setText(formatDouble(radius1 / radius1Units));
		star2Radius.setText(formatDouble(radius2 / radius2Units));
		distanceText.setText(formatDouble(distance / distUnits));
		
		if(isScaledToCamera)
			systemScaleText.setText(formatDouble(systemScaleFactor
					/ Math.atan(distance/2/-distCamera)));
		else
			systemScaleText.setText(formatDouble(systemScaleFactor2));

	}

	public void updateStarUnits() {

		if (massMap.get(star1MassUnits.getValue()) != mass1Units) {
			mass1 = mass1 * massMap.get(star1MassUnits.getValue()) / mass1Units;
			mass1Units = massMap.get(star1MassUnits.getValue());
			setStars();
			return;
		}

		if (massMap.get(star2MassUnits.getValue()) != mass2Units) {
			mass2 = mass2 * massMap.get(star2MassUnits.getValue()) / mass2Units;
			mass2Units = massMap.get(star2MassUnits.getValue());
			setStars();
			return;
		}

		if (distMap.get(star1RadiusUnits.getValue()) != radius1Units) {
			radius1 = radius1 * distMap.get(star1RadiusUnits.getValue())
					/ radius1Units;
			radius1Units = distMap.get(star1RadiusUnits.getValue());
			setStars();
			return;
		}

		if (distMap.get(star2RadiusUnits.getValue()) != radius2Units) {
			radius2 = radius2 * distMap.get(star2RadiusUnits.getValue())
					/ radius2Units;
			radius2Units = distMap.get(star2RadiusUnits.getValue());
			setStars();
			return;
		}

		if (distMap.get(distanceUnits.getValue()) != distUnits) {
			distance = distance * distMap.get(distanceUnits.getValue())
					/ distUnits;
			distUnits = distMap.get(distanceUnits.getValue());
			setStars();
			return;
		}
	}

	public void updateFieldUnits() {

		if (arrowsField.isSelected())
			updateArrowsUnits();
		else
			updateColormapUnits();
	}

	public void updateColormapUnits() {

		if (distMap.get(xSeparationUnits.getValue()) != cmapXDistUnits) {
			cmapXDist = cmapXDist * distMap.get(xSeparationUnits.getValue())
					/ cmapXDistUnits;
			cmapXDistUnits = distMap.get(xSeparationUnits.getValue());
			updateColormap(true);
			return;
		}

		if (distMap.get(ySeparationUnits.getValue()) != cmapYDistUnits) {
			cmapYDist = cmapYDist * distMap.get(ySeparationUnits.getValue())
					/ cmapYDistUnits;
			cmapYDistUnits = distMap.get(ySeparationUnits.getValue());
			updateColormap(true);
			return;
		}

		if (distMap.get(zSeparationUnits.getValue()) != cmapZDistUnits) {
			cmapZDist = cmapZDist * distMap.get(zSeparationUnits.getValue())
					/ cmapZDistUnits;
			cmapZDistUnits = distMap.get(zSeparationUnits.getValue());
			updateColormap(true);
			return;
		}

		if (angleMap.get(xAxisRotUnits.getValue()) != cmapXAngleUnits) {
			cmapXFieldRot = cmapXFieldRot
					* angleMap.get(xAxisRotUnits.getValue()) / cmapXAngleUnits;
			cmapXFieldRot %= 360;
			xAxisRot.setText(formatDouble(cmapXFieldRot / cmapXAngleUnits));
			cmapXAngleUnits = angleMap.get(xAxisRotUnits.getValue());
			updateColormap(true);
			return;
		}

		if (angleMap.get(yAxisRotUnits.getValue()) != cmapYAngleUnits) {
			cmapYFieldRot = cmapYFieldRot
					* angleMap.get(yAxisRotUnits.getValue()) / cmapYAngleUnits;
			cmapYFieldRot %= 360;
			yAxisRot.setText(formatDouble(cmapYFieldRot / cmapYAngleUnits));
			cmapYAngleUnits = angleMap.get(yAxisRotUnits.getValue());
			updateColormap(true);
			return;
		}

		if (angleMap.get(zAxisRotUnits.getValue()) != cmapZAngleUnits) {
			cmapZFieldRot = cmapZFieldRot
					* angleMap.get(zAxisRotUnits.getValue()) / cmapZAngleUnits;
			cmapZFieldRot %= 360;
			zAxisRot.setText(formatDouble(cmapZFieldRot / cmapZAngleUnits));
			cmapZAngleUnits = angleMap.get(zAxisRotUnits.getValue());
			updateColormap(true);
			return;
		}
	}

	public void updateArrowsUnits() {

		if (distMap.get(xSeparationUnits.getValue()) != xDistUnits) {
			xDist = xDist * distMap.get(xSeparationUnits.getValue())
					/ xDistUnits;
			xDistUnits = distMap.get(xSeparationUnits.getValue());
			updateField();
			return;
		}

		if (distMap.get(ySeparationUnits.getValue()) != yDistUnits) {
			yDist = yDist * distMap.get(ySeparationUnits.getValue())
					/ yDistUnits;
			yDistUnits = distMap.get(ySeparationUnits.getValue());
			updateField();
			return;
		}

		if (distMap.get(zSeparationUnits.getValue()) != zDistUnits) {
			zDist = zDist * distMap.get(zSeparationUnits.getValue())
					/ zDistUnits;
			zDistUnits = distMap.get(zSeparationUnits.getValue());
			updateField();
			return;
		}

		if (angleMap.get(xAxisRotUnits.getValue()) != xAngleUnits) {
			xFieldRot = xFieldRot * angleMap.get(xAxisRotUnits.getValue())
					/ xAngleUnits;
			xFieldRot %= 360;
			xAxisRot.setText(formatDouble(xFieldRot / xAngleUnits));
			xAngleUnits = angleMap.get(xAxisRotUnits.getValue());
			updateField();
			return;
		}

		if (angleMap.get(yAxisRotUnits.getValue()) != yAngleUnits) {
			yFieldRot = yFieldRot * angleMap.get(yAxisRotUnits.getValue())
					/ yAngleUnits;
			yFieldRot %= 360;
			yAxisRot.setText(formatDouble(yFieldRot / yAngleUnits));
			yAngleUnits = angleMap.get(yAxisRotUnits.getValue());
			updateField();
			return;
		}

		if (angleMap.get(zAxisRotUnits.getValue()) != zAngleUnits) {
			zFieldRot = zFieldRot * angleMap.get(zAxisRotUnits.getValue())
					/ zAngleUnits;
			zFieldRot %= 360;
			zAxisRot.setText(formatDouble(zFieldRot / zAngleUnits));
			zAngleUnits = angleMap.get(zAxisRotUnits.getValue());
			updateField();
			return;
		}
	}

	public void toggleEqualStellarDensity() {

		setStarDensityEquality(useEqualDensities.isSelected());
		star2Radius.setText(formatDouble(radius2 / radius2Units));

	}

	public void updateColormap(boolean chgPoints) {

		if (chgPoints) {
			colormapTexture = new WritableImage(cmapNumXPoints, cmapNumYPoints);
			cmapWriter = colormapTexture.getPixelWriter();
			colormapMaterial = new PhongMaterial(Color.RED, colormapTexture, null,
					null, null);
			colormapMaterial.setSelfIlluminationMap(colormapTexture);
			fieldColormap.setMaterial(colormapMaterial);
		}
		
		updateColorScale();

		fieldColormapScale.setX(cmapXDist);
		fieldColormapScale.setY(cmapYDist);

		fieldColormapXRot.setAngle(cmapXFieldRot);
		fieldColormapYRot.setAngle(cmapYFieldRot);
		fieldColormapZRot.setAngle(cmapZFieldRot);
	}

	public void updateColormapField() {

		try {
			int cmapNumXPointsNew, cmapNumYPointsNew;
			cmapNumXPointsNew = (int) (Double.parseDouble(numXPointsBox
					.getText()));
			cmapNumYPointsNew = (int) (Double.parseDouble(numYPointsBox
					.getText()));

			double cmapXDistNew = Double.parseDouble(xSeparation.getText())
					* cmapXDistUnits;
			double cmapYDistNew = Double.parseDouble(ySeparation.getText())
					* cmapYDistUnits;

			double cmapXFieldRotNew = (Double.parseDouble(xAxisRot.getText())
					* cmapXAngleUnits) % 360;
			double cmapYFieldRotNew = (Double.parseDouble(yAxisRot.getText())
					* cmapYAngleUnits) % 360;
			double cmapZFieldRotNew = (Double.parseDouble(zAxisRot.getText())
					* cmapZAngleUnits) % 360;

			if (cmapNumXPointsNew < 0 || cmapNumYPointsNew < 0
					|| cmapXDistNew < 0 || cmapYDistNew < 0) {

				numXPointsBox.setText(Integer.toString(cmapNumXPoints));
				numYPointsBox.setText(Integer.toString(cmapNumYPoints));

				xSeparation
						.setText(formatDouble(cmapXDist / cmapXDistUnits));
				ySeparation
						.setText(formatDouble(cmapYDist / cmapYDistUnits));

				return;
			}

			if (cmapNumXPoints != cmapNumXPointsNew
					|| cmapNumYPoints != cmapNumYPointsNew) {

				cmapNumXPoints = cmapNumXPointsNew;
				cmapNumYPoints = cmapNumYPointsNew;
				updateColormap(true);
			} else

			if (cmapXDist != cmapXDistNew || cmapYDist != cmapYDistNew
					|| cmapXFieldRotNew != cmapXFieldRot
					|| cmapYFieldRotNew != cmapYFieldRot
					|| cmapZFieldRotNew != cmapZFieldRot) {

				cmapXDist = cmapXDistNew;
				cmapYDist = cmapYDistNew;

				cmapXFieldRot = cmapXFieldRotNew;
				cmapYFieldRot = cmapYFieldRotNew;
				cmapZFieldRot = cmapZFieldRotNew;

				updateColormap(false);
			}

		} catch (Exception E) {
		}

		numXPointsBox.setText(Integer.toString(cmapNumXPoints));
		numYPointsBox.setText(Integer.toString(cmapNumYPoints));

		xSeparation.setText(formatDouble(cmapXDist / cmapXDistUnits));
		ySeparation.setText(formatDouble(cmapYDist / cmapYDistUnits));

		xAxisRot.setText(formatDouble(cmapXFieldRot / cmapXAngleUnits));
		yAxisRot.setText(formatDouble(cmapYFieldRot / cmapYAngleUnits));
		zAxisRot.setText(formatDouble(cmapZFieldRot / cmapZAngleUnits));
	}

	public void updateCamera() {

		try {
			double distCameraNew = -Double
					.parseDouble(cameraDistance.getText()) * distCameraUnits;

			double xPivotNew = Double.parseDouble(cameraXPivot.getText())
					* xPivotUnits;
			double yPivotNew = Double.parseDouble(cameraYPivot.getText())
					* yPivotUnits;
			double zPivotNew = Double.parseDouble(cameraZPivot.getText())
					* zPivotUnits;

			double xRotCameraNew = (Double.parseDouble(cameraXAxisRot.getText())
					* xRotCameraUnits) % 360;
			double yRotCameraNew = (Double.parseDouble(cameraYAxisRot.getText())
					* yRotCameraUnits) % 360;

			if (distCameraNew > 0) {

				setCameraTextFields();
				return;
			}

			if (xPivotNew != xPivot || yPivotNew != yPivot
					|| zPivotNew != zPivot || xRotCameraNew != xRotCameraVal
					|| yRotCameraNew != yRotCameraVal
					|| distCameraNew != distCamera) {

				distCamera = distCameraNew;

				xPivot = xPivotNew;
				yPivot = yPivotNew;
				zPivot = zPivotNew;

				xRotCameraVal = xRotCameraNew;
				yRotCameraVal = yRotCameraNew;

				setCamera();
			}

			cameraDistance.setText(formatDouble(-distCamera
					/ distCameraUnits));

			cameraXPivot.setText(formatDouble(xPivot / xPivotUnits));
			cameraYPivot.setText(formatDouble(yPivot / yPivotUnits));
			cameraZPivot.setText(formatDouble(zPivot / zPivotUnits));

			cameraXAxisRot.setText(formatDouble(xRotCameraVal
					/ xRotCameraUnits));
			cameraYAxisRot.setText(formatDouble(yRotCameraVal
					/ yRotCameraUnits));

		} catch (Exception E) {

			setCameraTextFields();
		}
	}

	public void updateCameraUnits() {

		if (distMap.get(cameraDistanceUnits.getValue()) != distCameraUnits) {
			distCamera = distCamera
					* distMap.get(cameraDistanceUnits.getValue())
					/ distCameraUnits;
			distCameraUnits = distMap.get(cameraDistanceUnits.getValue());
			setCamera();
			return;
		}

		if (distMap.get(cameraXPivotUnits.getValue()) != xPivotUnits) {
			xPivot = xPivot * distMap.get(cameraXPivotUnits.getValue())
					/ xPivotUnits;
			xPivotUnits = distMap.get(cameraXPivotUnits.getValue());
			setCamera();
			return;
		}

		if (distMap.get(cameraYPivotUnits.getValue()) != yPivotUnits) {
			yPivot = yPivot * distMap.get(cameraYPivotUnits.getValue())
					/ yPivotUnits;
			yPivotUnits = distMap.get(cameraYPivotUnits.getValue());
			setCamera();
			return;
		}

		if (distMap.get(cameraZPivotUnits.getValue()) != zPivotUnits) {
			zPivot = zPivot * distMap.get(cameraZPivotUnits.getValue())
					/ zPivotUnits;
			zPivotUnits = distMap.get(cameraZPivotUnits.getValue());
			setCamera();
			return;
		}

		if (angleMap.get(cameraXAxisRotUnits.getValue()) != xRotCameraUnits) {
			xRotCameraVal = xRotCameraVal
					* angleMap.get(cameraXAxisRotUnits.getValue())
					/ xRotCameraUnits;
			xRotCameraVal %= 360;
			cameraXAxisRot.setText(formatDouble(xRotCameraVal
					/ xRotCameraUnits));
			xRotCameraUnits = angleMap.get(cameraXAxisRotUnits.getValue());
			setCamera();
			return;
		}

		if (angleMap.get(cameraYAxisRotUnits.getValue()) != yRotCameraUnits) {
			yRotCameraVal = yRotCameraVal
					* angleMap.get(cameraYAxisRotUnits.getValue())
					/ yRotCameraUnits;
			yRotCameraVal %= 360;
			cameraYAxisRot.setText(formatDouble(yRotCameraVal
					/ yRotCameraUnits));
			yRotCameraUnits = angleMap.get(cameraYAxisRotUnits.getValue());
			setCamera();
			return;
		}
	}
	
	public double getAmplitude1(double t, double x, double y, double z){
		
		return Math.cos(2*omega*(t + Math.atan(x/y)/omega
				- Math.sqrt(x*x+y*y+z*z)/LIGHTSPEED));
	}
	
	public Point3D getTidalForce(double t, double x, double y, double z){
		
		double fx = -Math.sin(2*omega*(t + Math.atan2(x,y)/omega
					- Math.sqrt(x*x+y*y+z*z)/LIGHTSPEED)) * ((2.0 * y / (x*x+y*y))
					* Math.pow(x*x+y*y+z*z,1.0/2)- 2.0 * x * omega
					/ ((x*x+y*y+z*z)*LIGHTSPEED));
		double fy = -Math.sin(2*omega*(t + Math.atan2(x,y)/omega
					- Math.sqrt(x*x+y*y+z*z)/LIGHTSPEED)) * ((-2.0 * x / (x*x+y*y))
					* Math.pow(x*x+y*y+z*z,1.0/2) - 2.0 * y * omega
					/ ((x*x+y*y+z*z)*LIGHTSPEED));
		double fz = Math.sin(2*omega*(t + Math.atan2(x,y)/omega
					- Math.sqrt(x*x+y*y+z*z)/LIGHTSPEED)) * 2.0 * z * omega
					/ ((x*x+y*y+z*z)*LIGHTSPEED);
		
		return new Point3D(fx,fy,fz);
	}
	
	public double getMaxAmplitude(){
		
		return 4 * G/Math.pow(LIGHTSPEED, 4) * mass1*mass2/(mass1+mass2) * distance * distance
				* omega * omega;
	}
	
	public double cosIntegral(double x){
		
		double xpow = x*x;
		double res = 0.5772156649 + Math.log(x) - xpow/4;
		xpow = xpow*x*x;
		res += xpow/96;
		xpow = xpow*x*x;
		res -= xpow/4320;
		xpow = xpow*x*x;
		res += xpow/322560;
		xpow = xpow*x*x;
		res -= xpow/36288000;
		
		return res;
	}
	
	public double sinIntegral(double x){
		
		double xpow = x;
		double res = xpow;
		xpow = xpow*x*x;
		res -= xpow/18;
		xpow = xpow*x*x;
		res += xpow/600;
		xpow = xpow*x*x;
		res -= xpow/35280;
		xpow = xpow*x*x;
		res += xpow/3265920;
		
		return res;
	}
	
	public double getArmLength(double x0, double y0, double z0, double x1, double y1, double z1){
		
		double t = (millisTime - millisTimeLastTimeStretchChange) / 1000.0 * timeStretchFactor;
		
		double a = 4.0*G/Math.pow(LIGHTSPEED,4)*mass1*mass2/(mass1+mass2)*distance*distance*omega*omega;
		double b = 2*omega/LIGHTSPEED;
		double c = 2*omega*t + 2*Math.atan2((x0+x1)/2,(y0+y1)/2)/omega;
		
		double r = Math.sqrt(((x0+x1)*(x0+x1)/4)+((y0+y1)*(y0+y1)/4)+((z0+z1)*(z0+z1)/4));
		
		//double angleMult = Math.sqrt((x1-x0)*(x1-x0)+(y1-y0)*(y1-y0)+(z1-z0)*(z1-z0))
		//		/(Math.sqrt(x1*x1+y1*y1+z1*z1)-Math.sqrt(x0*x0+y0*y0+z0*z0));
		double distMult = Math.sqrt((x1-x0)*(x1-x0)+(y1-y0)*(y1-y0)+(z1-z0)*(z1-z0));
		return (1 - 1.0/2 * (a/r * Math.cos(-b*r+c))) * distMult;
	}
	
	public double getArmPhaseDif(){
		
		Point3D detAPos, detBPos, detCPos;
		detAPos = detectorA.localToScene(0,0,0);
		detBPos = detectorB.localToScene(0,0,0);
		detCPos = detectorC.localToScene(0,0,0);
		
		detBPos = new Point3D((detAPos.getX()*(detectorScaleFactor-1)+detBPos.getX())/detectorScaleFactor,
				(detAPos.getY()*(detectorScaleFactor-1)+detBPos.getY())/detectorScaleFactor,
				(detAPos.getZ()*(detectorScaleFactor-1)+detBPos.getZ())/detectorScaleFactor);
		detCPos = new Point3D((detAPos.getX()*(detectorScaleFactor-1)+detCPos.getX())/detectorScaleFactor,
				(detAPos.getY()*(detectorScaleFactor-1)+detCPos.getY())/detectorScaleFactor,
				(detAPos.getZ()*(detectorScaleFactor-1)+detCPos.getZ())/detectorScaleFactor);
		
		double res = ((2 * getArmLength(detAPos.getX(),detAPos.getY(),detAPos.getZ(),detBPos.getX(),
				detBPos.getY(), detBPos.getZ()) * arm1Mult - 2 * getArmLength(detAPos.getX(),
				detAPos.getY(),detAPos.getZ(),detCPos.getX(), detCPos.getY(), detCPos.getZ())
				* arm2Mult) / wavelength);// % (2*Math.PI);
		
		return res;
	}
	
	public void updateLaser(){
		
		try{
			int arm1MultNew = (int)Double.parseDouble(arm1MultText.getText());
			int arm2MultNew = (int)Double.parseDouble(arm2MultText.getText());
			double wavelengthNew = Double.parseDouble(wavelengthText.getText())
					* wavelengthUnits;
			double slitSeparationNew = Double.parseDouble(slitSeparationText.getText())
					* slitSeparationUnits;
			double screenDistNew = Double.parseDouble(screenDistText.getText())
					* screenDistUnits;
			double leftScreenLimitNew = -Double.parseDouble(screenWidthText.getText())
					* screenWidthUnits / 2;
			double rightScreenLimitNew = Double.parseDouble(screenWidthText.getText())
					* screenWidthUnits / 2;
			int patternResolutionNew = (int)Double.parseDouble(patternResolutionText.getText());
			
			if(leftScreenLimitNew > rightScreenLimitNew){
				double aLimit = rightScreenLimitNew;
				rightScreenLimitNew = leftScreenLimitNew;
				leftScreenLimitNew = aLimit;
			}
			
			if(arm1MultNew < 1 || arm2MultNew < 1 || wavelengthNew <= 0 || slitSeparationNew <= 0
					|| screenDistNew <= 0 || patternResolutionNew <= 0){
				
				setLaserTextFields();
				return;
			}
				
			if(patternResolutionNew != patternResolution){
				
				patternResolution = patternResolutionNew;
				patternImage = new WritableImage(patternResolution,1);
				patternWriter = patternImage.getPixelWriter();
				patternView.setImage(patternImage);
				patternResolutionText.setText(Integer.toString(patternResolution));
				return;
			}
		
			if(arm1MultNew != arm1Mult || arm2MultNew != arm2Mult || wavelengthNew != wavelength
					|| slitSeparationNew != slitSeparation || screenDistNew != screenDist
					|| leftScreenLimitNew != leftScreenLimit
					|| rightScreenLimit != rightScreenLimitNew){
				
				wavelength = wavelengthNew;
				arm1Mult = arm1MultNew;
				arm2Mult = arm2MultNew;
				slitSeparation = slitSeparationNew;
				screenDist = screenDistNew;
				rightScreenLimit = rightScreenLimitNew;
				leftScreenLimit = leftScreenLimitNew;
			}
			
			setLaserTextFields();
		
		} catch(Exception e){
			
			setLaserTextFields();
		}
	}
	
	public void updateLaserUnits(){
		
		if (distMap.get(wavelengthUnitsBox.getValue()) != wavelengthUnits) {
			wavelength = wavelength * distMap.get(wavelengthUnitsBox.getValue())
					/ wavelengthUnits;
			wavelengthUnits = distMap.get(wavelengthUnitsBox.getValue());
			return;
		}
		
		if (distMap.get(slitSeparationUnitsBox.getValue()) != slitSeparationUnits) {
			slitSeparation = slitSeparation * distMap.get(slitSeparationUnitsBox.getValue())
					/ slitSeparationUnits;
			slitSeparationUnits = distMap.get(slitSeparationUnitsBox.getValue());
			return;
		}
		
		if (distMap.get(screenDistUnitsBox.getValue()) != screenDistUnits) {
			screenDist = screenDist * distMap.get(screenDistUnitsBox.getValue())
					/ screenDistUnits;
			screenDistUnits = distMap.get(screenDistUnitsBox.getValue());
			return;
		}
	}

	public void updateDetector() {
		try {
			double detectorScaleFactorNew = Double.parseDouble(scaleOverride
					.getText());
			double xDetectorNew = Double.parseDouble(detectorXPos.getText())
					* xDetectorUnits;
			double yDetectorNew = Double.parseDouble(detectorYPos.getText())
					* yDetectorUnits;
			double zDetectorNew = Double.parseDouble(detectorZPos.getText())
					* zDetectorUnits;
			double arm1LengthNew = Double.parseDouble(arm1LengthBox.getText())
					* arm1LengthUnits;
			double arm2LengthNew = Double.parseDouble(arm2LengthBox.getText())
					* arm2LengthUnits;

			double xRotDetectorNew = (Double.parseDouble(detectorXAxisRot
					.getText()) * xRotDetectorUnits) % 360;
			double yRotDetectorNew = (Double.parseDouble(detectorYAxisRot
					.getText()) * yRotDetectorUnits) % 360;
			double zRotDetectorNew = (Double.parseDouble(detectorZAxisRot
					.getText()) * zRotDetectorUnits) % 360;
			double arm1AngleNew = (Double.parseDouble(arm1AngleBox.getText())
					* arm1AngleUnits) % 360;
			double arm2AngleNew = (Double.parseDouble(arm2AngleBox.getText())
					* arm2AngleUnits) % 360;

			if (xDetectorNew != xDetector || yDetectorNew != yDetector
					|| zDetectorNew != zDetector
					|| xRotDetectorNew != xRotDetector
					|| yRotDetectorNew != yRotDetector
					|| zRotDetectorNew != zRotDetector
					|| arm1LengthNew != arm1Length
					|| arm2LengthNew != arm2Length || arm1AngleNew != arm1Angle
					|| arm2AngleNew != arm2Angle
					|| detectorScaleFactorNew != detectorScaleFactor) {

				detectorScaleFactor = detectorScaleFactorNew;

				xDetector = xDetectorNew;
				yDetector = yDetectorNew;
				zDetector = zDetectorNew;
				arm1Length = arm1LengthNew;
				arm2Length = arm2LengthNew;

				xRotDetector = xRotDetectorNew;
				yRotDetector = yRotDetectorNew;
				zRotDetector = zRotDetectorNew;
				arm1Angle = arm1AngleNew;
				arm2Angle = arm2AngleNew;

				setDetector();
			}

			setDetectorTextFields();

		} catch (Exception E) {

			setDetectorTextFields();
		}
	}

	public void updateDetectorUnits() {

		if (distMap.get(detectorXPosUnits.getValue()) != xDetectorUnits) {
			xDetector = xDetector * distMap.get(detectorXPosUnits.getValue())
					/ xDetectorUnits;
			xDetectorUnits = distMap.get(detectorXPosUnits.getValue());
			setDetector();
			return;
		}

		if (distMap.get(detectorYPosUnits.getValue()) != yDetectorUnits) {
			yDetector = yDetector * distMap.get(detectorYPosUnits.getValue())
					/ yDetectorUnits;
			yDetectorUnits = distMap.get(detectorYPosUnits.getValue());
			setDetector();
			return;
		}

		if (distMap.get(detectorZPosUnits.getValue()) != zDetectorUnits) {
			zDetector = zDetector * distMap.get(detectorZPosUnits.getValue())
					/ zDetectorUnits;
			zDetectorUnits = distMap.get(detectorZPosUnits.getValue());
			setDetector();
			return;
		}

		if (angleMap.get(detectorXAxisRotUnits.getValue()) != xRotDetectorUnits) {
			xRotDetector = xRotDetector
					* angleMap.get(detectorXAxisRotUnits.getValue())
					/ xRotDetectorUnits;
			xRotDetector %= 360;
			detectorXAxisRot.setText(formatDouble(xRotDetector
					/ xRotDetectorUnits));
			xRotDetectorUnits = angleMap.get(detectorXAxisRotUnits.getValue());
			setDetector();
			return;
		}

		if (angleMap.get(detectorYAxisRotUnits.getValue()) != yRotDetectorUnits) {
			yRotDetector = yRotDetector
					* angleMap.get(detectorYAxisRotUnits.getValue())
					/ yRotDetectorUnits;
			yRotDetector %= 360;
			detectorYAxisRot.setText(formatDouble(yRotDetector
					/ yRotDetectorUnits));
			yRotDetectorUnits = angleMap.get(detectorYAxisRotUnits.getValue());
			setDetector();
			return;
		}

		if (angleMap.get(detectorZAxisRotUnits.getValue()) != zRotDetectorUnits) {
			zRotDetector = zRotDetector
					* angleMap.get(detectorZAxisRotUnits.getValue())
					/ zRotDetectorUnits;
			zRotDetector %= 360;
			detectorZAxisRot.setText(formatDouble(zRotDetector
					/ zRotDetectorUnits));
			zRotDetectorUnits = angleMap.get(detectorZAxisRotUnits.getValue());
			setDetector();
			return;
		}

		if (distMap.get(arm1LengthUnitsBox.getValue()) != arm1LengthUnits) {
			arm1Length = arm1Length
					* distMap.get(arm1LengthUnitsBox.getValue())
					/ arm1LengthUnits;
			arm1LengthUnits = distMap.get(arm1LengthUnitsBox.getValue());
			setDetector();
			return;
		}

		if (angleMap.get(arm1AngleUnitsBox.getValue()) != arm1AngleUnits) {
			arm1Angle = arm1Angle * angleMap.get(arm1AngleUnitsBox.getValue())
					/ arm1AngleUnits;
			arm1Angle %= 360;
			arm1AngleBox.setText(formatDouble(arm1Angle / arm1AngleUnits));
			arm1AngleUnits = angleMap.get(arm1AngleUnitsBox.getValue());
			setDetector();
			return;
		}

		if (distMap.get(arm2LengthUnitsBox.getValue()) != arm2LengthUnits) {
			arm2Length = arm2Length
					* distMap.get(arm2LengthUnitsBox.getValue())
					/ arm2LengthUnits;
			arm2LengthUnits = distMap.get(arm2LengthUnitsBox.getValue());
			setDetector();
			return;
		}

		if (angleMap.get(arm2AngleUnitsBox.getValue()) != arm2AngleUnits) {
			arm2Angle = arm2Angle * angleMap.get(arm2AngleUnitsBox.getValue())
					/ arm2AngleUnits;
			arm2Angle %= 360;
			arm2AngleBox.setText(formatDouble(arm2Angle / arm2AngleUnits));
			arm2AngleUnits = angleMap.get(arm2AngleUnitsBox.getValue());
			setDetector();
			return;
		}
	}

	public void updateMovePivotUnits() {

		if (distMap.get(inputUnits.getValue()) != movePivotUnits) {
			movePivotUnits = distMap.get(inputUnits.getValue());
			return;
		}
	}

	public void toggleShowDetector() {

		if (detectorShow.isSelected())
			detectorGroup.setVisible(true);
		else
			detectorGroup.setVisible(false);
	}

	public void resetDetector() {

		arm1Length = 100;
		arm2Length = 100;
		arm1Angle = 0;
		arm2Angle = 90;
		xDetector = 20;
		yDetector = 20;
		zDetector = 0;
		xRotDetector = 0;
		yRotDetector = 0;
		zRotDetector = 0;
		detectorScaleFactor = 1;

		setDetectorTextFields();
		setDetector();
	}
	
	public void deduceTimeStretch(){
		
		timeStretchFactor = 2*Math.PI / omega;
		timeStretchText.setText(formatDouble(timeStretchFactor));
	}
	
	public void updateTimeStretch(){
		
		try{
			if(timeStretchFactor != Double.parseDouble(timeStretchText.getText())){
				millisTimeLastTimeStretchChange = millisTime -
						(Math.toDegrees(((millisTime
						- millisTimeLastTimeStretchChange) / 1000
						* timeStretchFactor)*omega) % 360) / omega * 1000
						/ Double.parseDouble(timeStretchText.getText());
				
				timeStretchFactor = Double.parseDouble(timeStretchText.getText());
				timeStretchText.setText(formatDouble(timeStretchFactor));
			}
		}catch(Exception e){
			timeStretchText.setText(formatDouble(timeStretchFactor));
		}
	}
	
	public void toggleShowSystem(){
		
		systemGroup.setVisible(showSystem.isSelected());
	}
	
	public void toggleShowPattern(){
		
		patternAnchor.setVisible(showPatternChk.isSelected());
		patternNumsAnchor.setVisible(showPatternChk.isSelected());
		
		if(showPatternChk.isSelected()){
			scenesBorderPane.setBottom(patternAnchor);
			fringeDistLabel.setText(formatDouble(wavelength * screenDist
					/ slitSeparation)+"m");}
		else
			scenesBorderPane.setBottom(null);
	}
	
	public void resetPhaseLimits(){
		maxPhaseDif = -Double.MAX_VALUE;
		minPhaseDif = Double.MAX_VALUE;
	}
	
	public void drawPattern(){
		
		double amplColor, phaseDif;
		phaseDif = getArmPhaseDif();
		if(phaseDif>maxPhaseDif)
			maxPhaseDif = Math.toDegrees(phaseDif);
		if(phaseDif<minPhaseDif)
			minPhaseDif = Math.toDegrees(phaseDif);
		armPhaseDifLabel.setText(formatDouble(Math.toDegrees(phaseDif))+"°");
		armLengthDifLabel.setText(formatDouble(phaseDif/(2*Math.PI)*wavelength)+"m");
		phaseDif%=(2*Math.PI);
		for(int i=0;i<patternResolution;i++){
			amplColor = getPatternBrightness(phaseDif, leftScreenLimit
					+(rightScreenLimit-leftScreenLimit)/(patternResolution-1)*i);
			patternWriter.setColor(i, 0, Color.rgb((int)(amplColor*255),
					(int)(amplColor*255),(int)(amplColor*255)));
			}
		
		fringeOscAmplLabel.setText(formatDouble((maxPhaseDif-minPhaseDif)
				/360*wavelength * screenDist / slitSeparation)+"m");
	}
	
	public double getPatternBrightness(double phaseDif, double pos){
		
		return Math.pow(Math.cos(phaseDif/2+Math.PI*slitSeparation/wavelength
				*(-pos/screenDist)),2);
	}

}
