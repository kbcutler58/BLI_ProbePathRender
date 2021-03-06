/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.geometricCalculations;

import com.jme3.math.Matrix3f;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

/**
 *
 * @author Zach
 */
public class TransformHelper {

    /**
     * This finds the coordinate transformation from the standard basis
     *      to these three vectors as the basis.
     * @param vector1
     * @param vector2
     * @param vector3
     * @return
     */
    public static Matrix3f getCoordinateTransformation(Vector3f vector1, Vector3f vector2, Vector3f vector3) {
        Matrix3f coordMatrix = new Matrix3f();
        float[][] coords = new float[3][3];
        for (int j = 0; j < 3; j++) {
            coords[j][0] = vector1.get(j);
            coords[j][1] = vector2.get(j);
            coords[j][2] = vector3.get(j);
        }
        coordMatrix.set(coords);
        coordMatrix.invertLocal();
        return coordMatrix;
    }
    
    /**
     * This takes in a coord transform matrix and the vector to be put
     *      in new coordinates and then finds the vector in the new coordinate
     *      system. This is used for cases where the z coordinate 
     *      should be near zero, so this method only returns the x,y 
     *      coordinates in the new system. 
     * @param coordMatrix       change of coordinate matrix
     * @param originalVector    vector in standard basis
     */
    public static Vector2f getXYVectorInNewCoords(Matrix3f coordMatrix, Vector3f originalVector){
        Vector3f modVector = coordMatrix.mult(originalVector);
        return new Vector2f(modVector.getX(),modVector.getY());
    }
    
}
