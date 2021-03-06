/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.probeTracking;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import java.nio.file.Path;
import java.util.ArrayList;
import org.zrd.geometryToolkit.pathDataStructure.SegmentSet;
import org.zrd.geometryToolkit.pathTools.PathHelper;
import org.zrd.util.dataHelp.DataArrayToStringConversion;
import org.zrd.util.dataHelp.OutputHelper;
import org.zrd.util.dataWriting.ProbeDataWriter;
import org.zrd.util.dataWriting.TimeHelper;
import org.zrd.util.fileHelper.GeneralFileHelper;

/**
 *
 * @author BLI
 */
public class PathRecorder {

    private SegmentSet pathInformation;
    private boolean pathSpecified = false;
    private ProbeDataWriter xyzVertexWriter;
    private ProbeDataWriter xyVertexWriter;
    private ProbeDataWriter yawPitchRollWriter;
    private ProbeDataWriter xyzSignalDataWriter;
    private Path pathRecordingFilePath;
    
    private DataArrayToStringConversion convertor;

    private float arcLengthSinceLastRead = 0;
    private Vector3f lastPosition;
    private ArrayList<Vector3f> verticesSinceLastRead;
    private String fileNameSuffix;
    
    private String pathVertexFilePrefix;
    private String compressedPathFilePrefix;
    private String compressedPathInfoFilePrefix;
    
    
    //whether or not we are recording the path that is explictly following the mesh
    private boolean pathIsOnMesh = false;
    
    public PathRecorder(Vector3f startingPosition){
        pathInformation = new SegmentSet(100);
        verticesSinceLastRead = new ArrayList<Vector3f>(100);
        
        pathInformation.addToSet(startingPosition.clone());
        verticesSinceLastRead.add(startingPosition.clone());
        
        lastPosition = startingPosition.clone();
        pathSpecified = false;
    }
    
    public PathRecorder(Vector3f startingPosition,Path pathRecordingFilePath, 
            boolean pathIsOnMesh, boolean offsetPath){
        this(startingPosition);
        String timestampSuffix = TimeHelper.getTimestampSuffix();
        
        this.pathRecordingFilePath = pathRecordingFilePath;
        this.pathIsOnMesh = pathIsOnMesh;
        
        if(offsetPath){
            this.fileNameSuffix = "withOffset_" + timestampSuffix;
        }else{
            this.fileNameSuffix = timestampSuffix;
        }
        
        setFilePrefixes();
        
        xyzVertexWriter = ProbeDataWriter.getNewWriter(
                pathRecordingFilePath, fileNameSuffix,pathVertexFilePrefix);
        xyzSignalDataWriter = ProbeDataWriter.getNewWriter(
                pathRecordingFilePath, 
                fileNameSuffix, "xyzVerticesAndSignalData");
        
        if(!pathIsOnMesh){
            xyVertexWriter = ProbeDataWriter.getNewWriter(
                pathRecordingFilePath, fileNameSuffix,
                "pathXYvertices");
            yawPitchRollWriter = ProbeDataWriter.getNewWriter(
                    pathRecordingFilePath, fileNameSuffix, "yawPitchRollData");
        }
        
        pathSpecified = true;
    }
    
    public PathRecorder(Vector3f startingPosition,Path pathRecordingFilePath, boolean pathIsOnMesh){
        this(startingPosition,pathRecordingFilePath,pathIsOnMesh,false);
    }
    
    public PathRecorder(Vector3f startingPosition,Path pathRecordingFilePath, 
            boolean pathIsOnMesh, String[] startingData, DataArrayToStringConversion convertor, 
            long initTimestamp, boolean offsetPath){
        this(startingPosition,pathRecordingFilePath,pathIsOnMesh,offsetPath);
        this.convertor = convertor;
        pathInformation.addToSet(startingData);
        pathInformation.addToSet(initTimestamp);
    }
    
    public PathRecorder(Vector3f startingPosition,Path pathRecordingFilePath, 
            boolean pathIsOnMesh, String[] startingData, DataArrayToStringConversion convertor, 
            long initTimestamp){
        this(startingPosition,pathRecordingFilePath,pathIsOnMesh,startingData,convertor,initTimestamp,false);
    }
    
    private void setFilePrefixes(){
        pathVertexFilePrefix = pathIsOnMesh ? 
                "pathOnMeshVertices" : "pathVertices";
        compressedPathFilePrefix = pathIsOnMesh ? 
                "compressedPathOnMeshVertices" : "compressedPathVertices";
        compressedPathInfoFilePrefix = pathIsOnMesh ? 
                "compressedPathOnMeshInfo" : "compressedPathInfo";
    }
    
    public PathRecorder(Vector3f startingPosition,Path pathRecordingFilePath){
        this(startingPosition,pathRecordingFilePath,false);
    }

    public static String getPositionOutputText(Vector3f position){
        return OutputHelper.getPositionOutputText(
                position.getX(), 
                position.getY(), 
                position.getZ());
    }
    public static String getPositionOutputText(Vector2f position){
        return OutputHelper.getPositionOutputText(
                position.getX(), 
                position.getY());
    }
    public static String getOrientationOutputString(float yaw, float pitch, float roll){
        return OutputHelper.getOrientationOutputText(
                yaw, pitch, roll);
    }
    public ArrayList<Vector3f> getVertices() {
        return pathInformation.getPathVertices();
    }
    
    public void closeRecording(){
        ProbeDataWriter.closeWriter(xyzVertexWriter);
        ProbeDataWriter.closeWriter(xyzSignalDataWriter);
        if(!pathIsOnMesh){
            ProbeDataWriter.closeWriter(xyVertexWriter);
            ProbeDataWriter.closeWriter(yawPitchRollWriter);
        }
        pathInformation.finalizeSegment();
        
        //gets the post-processing text file paths
        Path recordedPathStats = GeneralFileHelper.getNewDataFilePath(
                pathRecordingFilePath,fileNameSuffix, compressedPathInfoFilePrefix);
        Path compressedPathAndSignalFile = GeneralFileHelper.getNewDataFilePath(
                pathRecordingFilePath,fileNameSuffix, "compressedVerticesAndSignalInfo");
        Path allVerticesAndSignalFile = GeneralFileHelper.getNewDataFilePath(
                pathRecordingFilePath,fileNameSuffix, "xyzVerticesTimestampSignalInfo");
        Path compressedPathFile = GeneralFileHelper.getNewDataFilePath(
                pathRecordingFilePath,fileNameSuffix, compressedPathFilePrefix);
        
        //runs the post-processor
        PathPostProcessing processor = new PathPostProcessing(recordedPathStats,
                compressedPathAndSignalFile,compressedPathFile,allVerticesAndSignalFile,pathInformation,
                PathHelper.MIN_SEGMENT_LENGTH,convertor);
        Thread postProcess = new Thread(processor);
        postProcess.start();
        
    }
    
    /**
     * This gets the most recently read vertices. Before returning the most recent vertices,
     *      it clears that list. It does retain the last vertex though
     *      in that list. 
     * @return 
     */
    public ArrayList<Vector3f> getMostRecentVertices(){
        arcLengthSinceLastRead = 0;
        ArrayList<Vector3f> returnVerts = PathHelper.getCopyOfPath(verticesSinceLastRead);
        Vector3f lastVertex = PathHelper.getLastPoint(returnVerts);
        verticesSinceLastRead.clear();
        verticesSinceLastRead.add(lastVertex);
        return returnVerts;
    }

    public float getArcLengthSinceLastRead() {
        return arcLengthSinceLastRead;
    }
    
    void addToPath(Vector3f currentPosition, long timestamp){
        
        if(pathSpecified){
            
            ProbeDataWriter.writeLineInWriter(
                    xyzVertexWriter, 
                    getPositionOutputText(currentPosition) + "," + timestamp);
            
        }

        float segLength = currentPosition.distance(lastPosition);
        arcLengthSinceLastRead += segLength;
        lastPosition = currentPosition.clone();
        
        pathInformation.addToSet(currentPosition.clone());
        pathInformation.addToSet(timestamp);
        verticesSinceLastRead.add(currentPosition.clone());
    }
    
    void addToPath(String[] signalData, Vector3f currentPosition, 
            Vector2f currentXYPosition, 
            float currentYaw, float currentPitch, float currentRoll, long timestamp){
        addToPath(currentPosition,currentXYPosition,currentYaw,currentPitch,currentRoll,timestamp);
        
        String vertexPart = getPositionOutputText(currentPosition);
        StringBuilder signalPart = new StringBuilder(signalData.length*5);
        for(String entry: signalData){
            signalPart.append(",");
            signalPart.append(entry);
        }
        
        pathInformation.addToSet(signalData);

        ProbeDataWriter.writeLineInWriter(xyzSignalDataWriter, vertexPart + signalPart);
        
    }

    void addToPath(Vector3f currentPosition, 
            Vector2f currentXYPosition, 
            float currentYaw, float currentPitch, float currentRoll, long timestamp) {
        
        addToPath(currentPosition,timestamp);
        
        if(pathSpecified){
        
            if(!pathIsOnMesh){
                
                ProbeDataWriter.writeLineInWriter(xyVertexWriter, 
                    getPositionOutputText(currentXYPosition));

                ProbeDataWriter.writeLineInWriter(yawPitchRollWriter, 
                        getOrientationOutputString(currentYaw,currentPitch,currentRoll));
            }
            
        }
    }
    
    
}
