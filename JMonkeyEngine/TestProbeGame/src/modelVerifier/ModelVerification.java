/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelVerifier;

import com.jme3.math.Vector3f;
import java.util.HashSet;
import java.util.Set;
import meshTraversal.ConnectedComponent;
import meshTraversal.MeshEdge;
import meshTraversal.MeshEdgeTriangles;
import meshTraversal.MeshTriangle;
import meshTraversal.TriangleSet;
import mygame.Constants;

/**
 *
 * @author BLI
 */
public class ModelVerification {
    
    public static void performModelVerification(TriangleSet triangles){
        boolean numEdgesPerTriangle = verifyNumEdgesPerTriangle(triangles);
        boolean numTrianglesPerEdge = verifyNumTrianglesPerEdge(triangles);
        boolean singleComponent = singleConnectedComponent(triangles);
        boolean noDegenTriangles = verifyNoDegenerateTriangles(triangles);
        boolean goodNormals = verifyGoodNormals(triangles);
        
        System.out.println();
        System.out.println("Now Running Model Verification:");
        System.out.println("3 Edges Exist for each triangle: " + numEdgesPerTriangle);
        System.out.println("2 Triangles Per Edge unless boundary: " + numTrianglesPerEdge);
        System.out.println("Mesh is Single Connected Component: " + singleComponent);
        System.out.println("No Degenerate Triangles: " + noDegenTriangles);
        System.out.println("Good Normals: " + goodNormals);
    }
    
    /**
     * This verifies that the triangles are a single connected component. It works
     *      by recursively travelling through all the neighbors of a triangle
     *      and verifying that the number of triangles added is the total number
     *      of triangles that we have
     * @param triangles
     * @return 
     */
    public static boolean singleConnectedComponent(TriangleSet triangles){
        
        MeshTriangle startingTriangle = triangles.getTriangleList().get(0);
        ConnectedComponent currentComp = new ConnectedComponent(triangles,startingTriangle);
        
        int numTrianglesComponent = currentComp.getComponentTriangles().size();
        int numTrianglesMesh = triangles.getTriangleList().size();
        
        System.out.println("Component has " + numTrianglesComponent + " Triangles");
        System.out.println("Mesh has " + numTrianglesMesh + " Triangles");
        
        return (numTrianglesComponent==numTrianglesMesh);
    }
    
    
    /**
     * This verifies that each edge has 2 triangles unless it's a boundary edge
     * @param triangles the triangle set
     * @return 
     */
    public static boolean verifyNumTrianglesPerEdge(TriangleSet triangles){
        MeshEdgeTriangles currentTriangles;
        for(MeshEdge edge: triangles.getTrianglesByEdge().keySet()){
            currentTriangles = triangles.getTrianglesByEdge().get(edge);
            if(currentTriangles.numTriangles() < 1){
                
                //if lone edge, this is not a clean model
                System.out.println("LONE EDGE!!");
                return false;
            }else if(currentTriangles.numTriangles() < 2){
                
                //if edge has 1 triangle, this makes sure it was correctly labelled as such
                if(!currentTriangles.getTriangle1().isBoundaryTriangle()){
                    System.out.println("UNLABELLED BOUNDARY TRIANGLE!!");
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * This runs the verification that every triangle has 3 edges
     * @param triangles
     * @return whether or not each triangle has 3 edges
     */
    public static boolean verifyNumEdgesPerTriangle(TriangleSet triangles){
        for(MeshTriangle triangle:triangles.getTriangleList()){
            if(!triangle.hasGoodEdges()) return false;
        }
        return true;
    }
    
    /**
     * This runs the verification that every triangle is not degenerate:
     *      - Two vertices cannot be the same
     *      - Points cannot be collinear
     * @param triangles
     * @return whether or not each triangle has 3 distinct vertices
     */
    public static boolean verifyNoDegenerateTriangles(TriangleSet triangles){
        Vector3f vector12,vector13;
        float dotProd;
        for(MeshTriangle triangle:triangles.getTriangleList()){
            vector12 = triangle.getSide12().getVector();
            vector13 = triangle.getSide13().getVector();
            
            //degenerate side
            if( (vector12.length() < Constants.EPSILON) 
                    || (vector13.length() < Constants.EPSILON) ){
                System.out.println("Degen Triangle: " + triangle);
                return false;
            }
            
            //collinear
            vector12.normalizeLocal();
            vector13.normalizeLocal();
            dotProd = vector12.dot(vector13);
            if((Math.abs(dotProd-1) < Constants.EPSILON) || 
                    (Math.abs(dotProd+1) < Constants.EPSILON)){
                //if dot prod is near 1 or -1, then unit vectors are identical
                System.out.println("Degen Triangle: " + triangle);
                return false;
            }
            
        }
        return true;
    }

    private static boolean verifyGoodNormals(TriangleSet triangles) {
        System.out.println("Vertex At Min X: " + triangles.getVertexWithMinX());
        System.out.println("Vertex At Max X: " + triangles.getVertexWithMaxX());
        System.out.println("Vertex At Min Y: " + triangles.getVertexWithMinY());
        System.out.println("Vertex At Max Y: " + triangles.getVertexWithMaxY());
        System.out.println("Vertex At Min Z: " + triangles.getVertexWithMinZ());
        System.out.println("Vertex At Max Z: " + triangles.getVertexWithMaxZ());
        return true;
    }
}
