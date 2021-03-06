/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.signalProcessRecorder;

import com.jme3.math.Vector3f;
import java.util.Properties;
import org.zrd.probeTracking.ProbeTracker;
import org.zrd.serialDataInterpreter.dataInterpretation.SerialDataInterpreter;
import org.zrd.signalProcessingTools.fftTools.SignalProcessingOutput_Threaded;
import org.zrd.util.fileHelper.FilePathHelper;
import org.zrd.util.properties.PropertiesHelper;
import org.zrd.util.trackingInterface.AbstractInputSourceTracker;

/**
 *
 * @author BLI
 */
public class ProbeTracker_SignalProcessRecorder {
    
    public static final Vector3f STARTING_POSITION = new Vector3f(0,0,0);
    
    public static final float INIT_SCALE_X = 0.00001f,INIT_SCALE_Y = 0.00001f;
    
    public static ProbeTracker createNewProbeTracker(){
        Properties trackerProps = PropertiesHelper.getDefaultProperties();
        AbstractInputSourceTracker currentSourceTracker;
        short displacementMode;
        
        currentSourceTracker = new SerialDataInterpreter(trackerProps);

        displacementMode = Short.parseShort(
                trackerProps.getProperty("trackDisplacementMode"));

        ProbeTracker activeTracker = ProbeTracker.initializeProbeTracker(
                currentSourceTracker, displacementMode, FilePathHelper.getDefaultOutputFolder(), 
                INIT_SCALE_X, INIT_SCALE_Y, STARTING_POSITION);

        //makes the signal tracker
        SignalProcessingOutput_Threaded signalTracker = 
                new SignalProcessingOutput_Threaded(100,14);
        activeTracker.setDataArrayToStringConvertor(
                signalTracker.getDataTracker());
        activeTracker.setOutputStreaming(signalTracker);
        signalTracker.setDisplayOutput(true);
        
        return activeTracker;
    }
    
}
