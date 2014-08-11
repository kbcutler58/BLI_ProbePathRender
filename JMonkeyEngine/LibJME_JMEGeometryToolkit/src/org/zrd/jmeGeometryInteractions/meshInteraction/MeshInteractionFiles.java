/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.jmeGeometryInteractions.meshInteraction;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.zrd.jmeGeometryIO.meshIO.MeshDataFiles;

/**
 * 
 * This is a class that stores all the files with loading and interacting
 *      with a mesh. Right now this includes the following:
 * - OBJ and MTL file for the shape
 * - PNG file for the texture
 * - TXT file with camera coordinates for the default place to put the camera
 * - TXT file with fixed points and their triangles for calibration
 * 
 * The formats must be as follows:
 * - OBJ file has format "meshDefinition_{suffix}.obj"
 * - PNG file has format "textureDefinition_{suffix}.png"
 * - TXT file with camera coords has format "cameraCoords_{suffix}.txt"
 * - TXT file with fixed points has format "fixedPoints_{suffix}.txt"
 *
 * @author BLI
 */
public class MeshInteractionFiles {
    
    private MeshDataFiles dataFiles;
    private File cameraCoordFile;
    private File fixedPointsFile;
    private Path locationOfFiles;
    private String suffixOfFiles;
    
    private File cameraCoordFileToCopy;
    private File fixedPointsFileToCopy;

    public MeshDataFiles getDataFiles() {
        return dataFiles;
    }

    public File getCameraCoordFile() {
        return cameraCoordFile;
    }

    public File getFixedPointsFile() {
        return fixedPointsFile;
    }

    public void setCameraCoordFileToCopy(File cameraCoordFileToCopy) {
        this.cameraCoordFileToCopy = cameraCoordFileToCopy;
    }

    public void setFixedPointsFileToCopy(File fixedPointsFileToCopy) {
        this.fixedPointsFileToCopy = fixedPointsFileToCopy;
    }
    
    public void copyFixedPointsFile(){
        try {
            Files.copy(fixedPointsFileToCopy.toPath(), fixedPointsFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            System.out.println("Error copying mesh interaction files: " + ex);
        }
    }
    
    public MeshInteractionFiles(File meshObjFile){
        
        String meshObjFileName = meshObjFile.getName();
        
        //split into file name and extension
        String[] meshObjFileNameParts = meshObjFileName.split("\\x2E");
        
        //split the file name
        String[] meshObjNameParts = meshObjFileNameParts[0].split("_");
        suffixOfFiles = meshObjNameParts[1];
        
        locationOfFiles = meshObjFile.getParentFile().toPath();
        
        dataFiles = new MeshDataFiles(meshObjFile,getMeshInteractionFile("textureDefinition",".png"));
        
        cameraCoordFile = getMeshInteractionFile("cameraCoords",".txt");
        fixedPointsFile = getMeshInteractionFile("fixedPoints",".txt");
    }
    
    private File getMeshInteractionFile(String prefix,String fileExtension){
        return locationOfFiles.resolve(getMeshInteractionFileName(prefix,fileExtension)).toFile();
    }
    
    private String getMeshInteractionFileName(String prefix, String fileExtension){
        return prefix + "_" + suffixOfFiles + fileExtension;
    }
    
}
