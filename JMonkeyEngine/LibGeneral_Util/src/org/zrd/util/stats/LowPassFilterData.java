/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.util.stats;

import java.util.ArrayList;

/**
 * IMPORTANT NOTE:
 * CODE IS CURRENTLY UNUSED BUT SHOULD
 *      BE KEPT FOR POSSIBLE LATER USE
 * 
 * 
 * @author BLI
 */
public class LowPassFilterData {
    
    
    private ArrayList<Float> data;
    private int window;
    private float average;
    
    public LowPassFilterData(int windowSize){
        data = new ArrayList<Float>(windowSize);
        window = windowSize;
    }
    
    public float getAverage(){
        return average;
    }
    
    private void recalculateAverage(){
        float sum = 0;
        for(Float number:data){
            sum = sum + number;
        }
        average = sum/window;
    }
    
    public void addToData(float number){
        data.add(number);
        if(data.size()>window){
            data.remove(0);
            recalculateAverage();
        }
        
    }
    
}
