/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.jmeGeometry.renderedObjects;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Matrix4f;
import org.zrd.geometryToolkit.meshDataStructure.TriangleSet;
import org.zrd.graphicsToolsImpl.meshImpl.MeshHelper;

/**
 *
 * @author Zach
 */
public class SphereMesh extends RenderedMesh{

    public SphereMesh(AssetManager assetManager){
        
        String sphereLocation = "Models/sphere2.obj";
        //String sphereLocation = "Models/simpleCube.obj";
        
        Material sphereMaterial = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        sphereMaterial.setTexture("ColorMap",assetManager.loadTexture("Textures/lola_texture.png"));
        //if(wireframeOn) sphereMaterial.getAdditionalRenderState().setWireframe(true);
        
        surfaceMesh = MeshHelper.generateModel(sphereLocation, sphereMaterial, assetManager);

        float surfaceScale = 20f;
        Matrix4f sphereTransform = new Matrix4f();
        sphereTransform.setScale(20f, 20f, 20f);

        surfaceMesh.scale(surfaceScale);
        
        activeMeshInfo = new TriangleSet();
        activeMeshInfo.setTransform(sphereTransform);
        activeMeshInfo = MeshHelper.addToTriangleSet(activeMeshInfo,surfaceMesh,sphereTransform);
    }
    
    
    
}