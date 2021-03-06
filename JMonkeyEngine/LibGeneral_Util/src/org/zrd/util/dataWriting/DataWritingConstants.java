/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.util.dataWriting;

/**
 * These are the constants used
 *      during the process to write
 *      data to text files. 
 * 
 * If something about data writing
 *      should be changed, then these
 *      constants should be changed
 *      or the format specified in another
 *      class in this package should be changed
 *
 * @author Zach
 */
public class DataWritingConstants {
    
    /**
     * The suffix of the text files has a timestamp.
     * This is the format of that timestamp
     * 
     * It is specifically designed so that when put into
     *      alphabetical order, the files line up
     *      in descending order by time
     */
    public static final String TIMESTAMP_SUFFIX_FORMAT = "yyyy_MM_dd__HH_mm_ss";
    
    /**
     * Used to seperate the prefix saying what the file is
     *      with the suffix saying the timestampe
     *      in the file name itself
     */
    public static final String PREFIX_TO_SUFFIX_SEPARATOR = "_";
    
    /**
     * Used to say the output file format for all
     *      of the text files with data that are generated
     * 
     */
    public static final String OUTPUT_FILE_FORMAT = ".txt";
    
}
