/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier; 
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent; 
import gnu.io.SerialPortEventListener; 
import java.util.Enumeration;
import java.util.HashMap;

/*
 * 
 * Info on setup was found here:
 * 
 * http://playground.arduino.cc/Interfacing/Java#.UxkWA_mwLYg
 * 
 * The RXTX library was found here
 * http://mfizz.com/oss/rxtx-for-java
 * 
 */


public class SerialReader implements SerialPortEventListener {
	SerialPort serialPort;
        /** The port we're normally going to use. */
	/*private static final String PORT_NAMES[] = { 
                    "/dev/tty.usbserial-A9007UX1", // Mac OS X
                    "/dev/ttyUSB0", // Linux
                    "COM3", // Windows
	};*/
        private static final String PORT_NAME = "COM4";
	/**
	* A BufferedReader which will be fed by a InputStreamReader 
	* converting the bytes into characters 
	* making the displayed results codepage independent
	*/
	private BufferedReader input;
	/** The output stream to the port */
	private OutputStream output;
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 57600;

        private ArduinoDataPoint currentData;

    public ArduinoDataPoint getCurrentData() {
        return currentData;
    }
        
        private static HashMap<String,Integer> dataLocations;

	public void initialize() {
            makeDataLocationsMap();
            CommPortIdentifier portId = null;
            Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

            //First, Find an instance of serial port as set in PORT_NAMES.
            while (portEnum.hasMoreElements()) {
                CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
                if (currPortId.getName().equals(PORT_NAME)) {
                    portId = currPortId;
                    break;
                }
                /*for (String portName : PORT_NAMES) {
                    if (currPortId.getName().equals(portName)) {
                            portId = currPortId;
                            break;
                    }
                }*/
            }
            if (portId == null) {
                System.out.println("Could not find COM port.");
                return;
            }

            try {
                // open serial port, and use class name for the appName.
                serialPort = (SerialPort) portId.open(this.getClass().getName(),
                                TIME_OUT);

                // set port parameters
                serialPort.setSerialPortParams(DATA_RATE,
                                SerialPort.DATABITS_8,
                                SerialPort.STOPBITS_1,
                                SerialPort.PARITY_NONE);

                // open the streams
                input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
                output = serialPort.getOutputStream();

                // add event listeners
                serialPort.addEventListener(this);
                serialPort.notifyOnDataAvailable(true);
            } catch (Exception e) {
                System.err.println(e.toString());
            }
	}

	/**
	 * This should be called when you stop using the port.
	 * This will prevent port locking on platforms like Linux.
	 */
	public synchronized void close() {
            if (serialPort != null) {
                serialPort.removeEventListener();
                serialPort.close();
            }
	}
        
        private void makeDataLocationsMap(){
            dataLocations = new HashMap<String,Integer>(10);
            dataLocations.put("timestamp", 0);
            dataLocations.put("x", 1);
            dataLocations.put("y", 2);
            dataLocations.put("yaw", 4);
            dataLocations.put("pitch", 5);
            dataLocations.put("roll", 6);
        }
        

	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	public synchronized void serialEvent(SerialPortEvent oEvent) {
            if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
                try {
                    String inputLine=input.readLine();
                    currentData = new ArduinoDataPoint(inputLine,dataLocations);
                } catch (Exception e) {
                    System.err.println(e.toString());
                }
            }
            // Ignore all the other eventTypes, but you should consider the other ones.
	}
        
        public String getCurrentOutput(){
            return String.valueOf(currentData);
        }

        public static void executeMain(){
            SerialReader main = new SerialReader();
            main.beginExecution();
        }
        
        public void beginExecution(){
            initialize();
            Thread t=new Thread() {
                public void run() {
                        //the following line will keep this app alive for 1000 seconds,
                        //waiting for events to occur and responding to them (printing incoming messages to console).
                        try {
                            Thread.sleep(1000000);
                        } catch (InterruptedException ie) {
                        }
                }
            };
            t.start();
            System.out.println("Started");
        }
}