/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.probeTracking.deviceToWorldConversion;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

/**
 * This is the general parent abstract class for 
 *      classes which take the device input 
 *      and then translate that to x,y,z displacement
 * 
 * NOTE: These classes are only meant to track displacement
 *      at an individual point in world coordinates, NOT
 *      the canonical coordinates of the device, which should 
 *      be found in another class using a translation.
 *
 * @author BLI
 */
public abstract class AbstractSerialInputToWorldConverter {

    /*
     * The following three variables are calibration factors
     *      and should only be changed if the calibration changed
     */
    
    //device displacement to world displacement matrix
    //      should only be called after scale factors have been applied
    protected Quaternion rotationCalibration = new Quaternion();
    
    /*
     * The scale factors for x and y coordinates. The absolute value of them
     *      should match.
     * If one of the scale factors is set to be negative, then that coordinate
     *      is getting reflected before the rotation is done. 
     * Reflection is NOT a rotation, thus we need to account for it, and to do
     *      that we are implementing negative scale factors. 
     */      
    protected float scaleFactorX = 1.0f;
    protected float scaleFactorY = 1.0f;
    
    public Vector3f getXYZDisplacement(float deltaX, float deltaY, 
            float yaw, float pitch, float roll){
        
        float xChangeMagnitude = deltaX*scaleFactorX;
        float yChangeMagnitude = deltaY*scaleFactorY;
        
        Vector3f initDisplacementVector = new Vector3f(xChangeMagnitude,yChangeMagnitude,0);
        
        /*rotation matrix for most recent yaw, pitch, roll numbers
         *  or other numbers if a different subclass is being used
         */ 
        Quaternion rotationFromData = getRotationQuat(yaw,pitch,roll);
        
        Quaternion currentRotation = rotationCalibration.mult(rotationFromData);
        
        return currentRotation.mult(initDisplacementVector);
    }
    
    public Vector2f getXYDisplacement(float deltaX, float deltaY){
        return new Vector2f(deltaX*scaleFactorX,deltaY*scaleFactorY);
    }
    
    protected abstract Quaternion getRotationQuat(float yaw, float pitch, float roll);

    public void setRotationCalibration(Quaternion rotationCalibration) {
        this.rotationCalibration = rotationCalibration;
    }

    public Quaternion getRotationCalibration() {
        return rotationCalibration;
    }

    public void setScaleFactorX(float scaleFactorX) {
        this.scaleFactorX = scaleFactorX;
    }

    public void setScaleFactorY(float scaleFactorY) {
        this.scaleFactorY = scaleFactorY;
    }

    public float getScaleFactorX() {
        return scaleFactorX;
    }

    public float getScaleFactorY() {
        return scaleFactorY;
    }
    
    
    
}
