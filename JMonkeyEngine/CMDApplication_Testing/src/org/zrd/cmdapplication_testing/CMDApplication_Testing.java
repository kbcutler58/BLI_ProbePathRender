/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.cmdapplication_testing;

import java.util.Calendar;

/**
 *
 * @author BLI
 */
public class CMDApplication_Testing {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        /*int n = 16;
        FFT.testFFT(n);
        double[] blackmanWindow = WindowFunction.generateBlackmanWindow(n);
        for(double windowVal: blackmanWindow){
            System.out.print(windowVal + " ");
        }*/
        
        //data at index 4 in the CW data
        double[] waveform = {6.84,3.56,525,123,149,1884,1838,1805,1793,
            1803,1833,1879,1935,
            1991,2043,2079,2098,2097,2082,2043,1997,1941,1888,1839,1809,
            1796,1806,1833,1876,1930,1983,2033,2072,2092,2096,2079,2046,
            2008,1948,1895,1851,1818,1803,1808,1832,1873,1924,1973,2026,
            2065,2087,2094,2081,2050,2007,1735,1687,1655,1631,1630,1634,
            1648,1684,1720,1777,1830,1892,1954,2011,2079,2142,2190,2224,
            2258,2275,2269,2262,2240,2203,2150,2094,2029,1961,1894,1833,
            1769,1713,1679,1646,1627,1624,1635,1651,1686,1733,1790,1830,
            1909,1968,2036,2090,2154,2196,2229,2265,0};
        
        long beforeCalc = Calendar.getInstance().getTimeInMillis();
        CWData data = CWFFT.getCWFFTData(waveform, 7);
        long afterCalc = Calendar.getInstance().getTimeInMillis();
        
        System.out.println("Peak Power: " + data.getPower());
        System.out.println("Frequency at Peak: " + data.getFrequency());
        System.out.println("Time For Calculation: " + (afterCalc-beforeCalc) + " ms");
        
    }

}
