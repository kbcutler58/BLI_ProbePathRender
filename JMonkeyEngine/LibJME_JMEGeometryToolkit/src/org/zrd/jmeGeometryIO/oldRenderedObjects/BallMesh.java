/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.jmeGeometryIO.oldRenderedObjects;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Matrix4f;
import com.jme3.math.Vector3f;
import org.zrd.geometryToolkit.meshDataStructure.ConnectedComponent;
import org.zrd.geometryToolkit.meshDataStructure.TriangleSet;
import org.zrd.geometryToolkit.modelTesting.ModelCorrection;
import org.zrd.geometryToolkit.modelTesting.ModelVerification;
import org.zrd.jmeGeometryIO.meshIO.MeshInputHelper;
import org.zrd.jmeGeometryIO.meshIO.MeshRenderData;
import org.zrd.jmeGeometryIO.meshIO.MeshRenderHelper;

/**
 * UNUSED CODE BUT KEEP FOR POTENTIAL FUTURE USES
 * 
 * 
 * This generates and tansforms the Ball Mesh for use by JME applications
 *
 * @author Zach
 *
public class BallMesh extends MeshRenderData{

    public BallMesh(AssetManager assetManager){
        
        String objFileLocation = "Models/ballMesh/ballMesh_simplified.obj";
        
        Material lolaMaterial = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        lolaMaterial.setTexture("ColorMap",assetManager.loadTexture("Textures/ball_texture.png"));
        //if(wireframeOn) lolaMaterial.getAdditionalRenderState().setWireframe(true);
        
        renderedMesh = MeshInputHelper.generateModel(objFileLocation, lolaMaterial, assetManager);
        
        //Quaternion yaw = new Quaternion();
        //yaw.fromAngleAxis(180*FastMath.DEG_TO_RAD, Vector3f.UNIT_Z);
        //Quaternion pitch = new Quaternion();
        //pitch.fromAngleAxis(-20*FastMath.DEG_TO_RAD, Vector3f.UNIT_X);
        //Quaternion surfaceRotation = yaw.mult(pitch);
        
        * float surfaceScale = 40f;
        //Vector3f surfaceLoc = new Vector3f(0,22,-53);
        
        Matrix4f surfaceRot = new Matrix4f();
        Matrix4f scaleMat = new Matrix4f();
        Matrix4f moveMatrix = new Matrix4f();
        
        scaleMat.scale(new Vector3f(surfaceScale,surfaceScale,surfaceScale));
        //surfaceRotation.toRotationMatrix(surfaceRot);
        //moveMatrix.setTranslation(surfaceLoc);
        Matrix4f surfaceTransform = moveMatrix.mult(scaleMat).mult(surfaceRot);
        
        //surfaceMesh.setLocalRotation(surfaceRotation);
        renderedMesh.scale(surfaceScale);
        //surfaceMesh.move(surfaceLoc);
        
        
        finalMeshInfo = new TriangleSet();
        finalMeshInfo.setTransform(surfaceTransform);
        finalMeshInfo = MeshInputHelper.addToTriangleSet(finalMeshInfo,renderedMesh,surfaceTransform);
        
        ConnectedComponent mainComponent = ModelCorrection.getLargestComponent(finalMeshInfo);
        TriangleSet correctedMesh = mainComponent.getComponentTriangleSet();
        correctedMesh = ModelCorrection.getSmoothedTriangleSet(correctedMesh);
        System.out.println("Corrected Mesh has " + correctedMesh.getTriangleList().size() + " triangles ");
        renderedMesh = MeshRenderHelper.createMeshFromTriangles(correctedMesh, lolaMaterial);
        finalMeshInfo = correctedMesh;
        
        ModelVerification.performModelVerification(correctedMesh);
    }
    
    
    
}*/
