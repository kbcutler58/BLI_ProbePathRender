/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.qualityTracker;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.zrd.util.dataStreaming.ProbeDataStream;
import org.zrd.util.dataStreaming.StreamQualityTracker;
import org.zrd.util.dataWriting.ProbeDataWriter;
import org.zrd.util.fileHelper.FileDataHelper;
import org.zrd.util.stats.DataSet;

/**
 *
 * @author BLI
 */
public class QualityReader implements ProbeDataStream{
    
    private StreamQualityTracker currentQualityTracker;
    private QualityRecorder currentQualityRecorder;
    private DataSet currentQualitySet;
    private boolean recordingQuality = false;
    private Path recordingFilePath;
    
    
    public QualityReader(StreamQualityTracker currentQualityTracker, Path recordingFilePath){
        this.currentQualityTracker = currentQualityTracker;
        this.recordingFilePath = recordingFilePath;
    }

    @Override
    public void updateData() {
        currentQualityTracker.updateData();
        
        float qual = currentQualityTracker.getCurrentQuality();
        if(recordingQuality){
            currentQualityRecorder.addQualityLine(qual);
            currentQualitySet.addToDataSet(qual);
        }
        
        //output quality to console
        System.out.println(qual);
    }

    @Override
    public void startStopRecording() {
        if(recordingQuality){
            currentQualityRecorder.closeRecording();
            
            currentQualitySet.processData();
            currentQualitySet.displayResults();
            
            try {
                Path qualityStatsFilePath = ProbeDataWriter.getNewDataFilePath(recordingFilePath, "qualityStats");
                FileDataHelper.exportLinesToFile(currentQualitySet.getResultStrings(), qualityStatsFilePath);
            } catch (IOException ex) {
                System.out.println("Error trying to write stats to file: " + ex);
            }
            
            recordingQuality = false;
        }else{
            currentQualityRecorder = new QualityRecorder(recordingFilePath);
            currentQualitySet = new DataSet(50);
            recordingQuality = true;
        }
    }
    
}