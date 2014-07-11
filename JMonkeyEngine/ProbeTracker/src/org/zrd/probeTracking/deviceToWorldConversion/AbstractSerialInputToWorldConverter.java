/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.probeTracking.deviceToWorldConversion;

import com.jme3.math.Matrix3f;
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
    protected Matrix3f rotationCalibrationMatrix = new Matrix3f();
    
    //scale factor for x coordinate. should be same as one for y coordinate
    protected float scaleFactorX = 1.0f;
    
    //scale factor for y coordinate. should be same as one for x coordinate
    protected float scaleFactorY = 1.0f;
    
    /*
     * These vectors are the initial x and y displacement vectors.
     *      The subclasses of this one are in charge of manipulating
     *      these vectors based on rotation to get the actual
     *      x, y, and z displacment
     */
    
    //vector for x displacement
    public static final Vector3f INIT_X_VECTOR = new Vector3f(1,0,0);
    
    //vector for y displacement
    public static final Vector3f INIT_Y_VECTOR = new Vector3f(0,1,0);
    
    //vector for normal to displacement
    public static final Vector3f INIT_NORMAL_VECTOR = new Vector3f(0,0,-1);
    
    //rotation matrix for most recent yaw, pitch, roll numbers
    private Matrix3f currentRotationMatrix = new Matrix3f();
    
    public Vector3f getXYZDisplacement(float deltaX, float deltaY, 
            float yaw, float pitch, float roll){
        
        float xChangeMagnitude = deltaX*scaleFactorX;
        float yChangeMagnitude = deltaY*scaleFactorY;
        
        currentRotationMatrix = getRotationMatrix(yaw,pitch,roll);
        
        Vector3f xDispVector = currentRotationMatrix.mult(INIT_X_VECTOR);
        Vector3f yDispVector = currentRotationMatrix.mult(INIT_Y_VECTOR);
        
        Vector3f xDisplacement = xDispVector.mult(xChangeMagnitude);
        Vector3f yDisplacement = yDispVector.mult(yChangeMagnitude);
        
        Vector3f initDisplacement = xDisplacement.add(yDisplacement);
        
        return rotationCalibrationMatrix.mult(initDisplacement);
    }
    
    public Vector2f getXYDisplacement(float deltaX, float deltaY){
        return new Vector2f(deltaX*scaleFactorX,deltaY*scaleFactorY);
    }
    
    protected abstract Matrix3f getRotationMatrix(float yaw, float pitch, float roll);

    public void setRotationCalibrationMatrix(Matrix3f rotationCalibrationMatrix) {
        this.rotationCalibrationMatrix = rotationCalibrationMatrix;
    }

    public void setScaleFactorX(float scaleFactorX) {
        this.scaleFactorX = scaleFactorX;
    }

    public void setScaleFactorY(float scaleFactorY) {
        this.scaleFactorY = scaleFactorY;
    }
    
    private Vector3f getManipulatedVector(Vector3f inputVector){
        return rotationCalibrationMatrix.mult(currentRotationMatrix).mult(inputVector);
    }
    
    public Vector3f getCurrentXAxis(){
        return getManipulatedVector(INIT_X_VECTOR);
    }
    
    public Vector3f getCurrentYAxis(){
        return getManipulatedVector(INIT_Y_VECTOR);
    }
    
    public Vector3f getCurrentNormal(){
        return getManipulatedVector(INIT_NORMAL_VECTOR);
    }

    public float getScaleFactorX() {
        return scaleFactorX;
    }

    public float getScaleFactorY() {
        return scaleFactorY;
    }
    
    
    
}