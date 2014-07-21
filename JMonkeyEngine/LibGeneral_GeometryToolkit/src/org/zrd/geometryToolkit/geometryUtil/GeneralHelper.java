/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.geometryUtil;

import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import java.util.ArrayList;

/**
 *
 * @author BLI
 */
public class GeneralHelper {

    public static Matrix3f getRotationMatrix(float angle, Vector3f axis) {
        Quaternion rotation = new Quaternion();
        rotation.fromAngleAxis(angle, axis);
        return rotation.toRotationMatrix();
    }
    
    public static ArrayList<Vector3f> getCopyOfPath(ArrayList<Vector3f> path){
        ArrayList<Vector3f> returnPath = new ArrayList<Vector3f>(path.size());
        for(Vector3f vertex : path){
            returnPath.add(vertex.clone());
        }
        return returnPath;
    }
    
    public static String getXYZDisplayString(Vector3f position){
        return "(X,Y,Z) = " + position;
    }
    
    public static String getYawPitchRollDisplayString(float yawInRadians, float pitchInRadians, float rollInRadians){
        return "(Yaw,Pitch,Roll) = (" + 
                yawInRadians*FastMath.RAD_TO_DEG + "," + 
                pitchInRadians*FastMath.RAD_TO_DEG + "," + 
                rollInRadians*FastMath.RAD_TO_DEG + ")";
    }
    
}