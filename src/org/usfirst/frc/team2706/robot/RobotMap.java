package org.usfirst.frc.team2706.robot;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.Scanner;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into to a variable name.
 * This provides flexibility changing wiring, makes checking the wiring easier and significantly
 * reduces the number of magic numbers floating around.
 */
@SuppressWarnings("unused")
public class RobotMap {

    private static final int ROBOT_ID = getRobotID();

    // CONSTANT_VALS [ Competition, Practice, Simulation]

    // Left gearbox
    private static final int[] MOTOR_FRONT_LEFT_VALS = {1, 1, 1};
    public static final int MOTOR_FRONT_LEFT = getConstant("MOTOR_FRONT_LEFT");

    private static final boolean[] MOTOR_FRONT_LEFT_INVERTED_VALS = {true, true, false};
    public static final boolean MOTOR_FRONT_LEFT_INVERTED =
                    getConstant("MOTOR_FRONT_LEFT_INVERTED");


    // Right gearbox
    private static final int[] MOTOR_FRONT_RIGHT_VALS = {3, 3, 3};
    public static final int MOTOR_FRONT_RIGHT = getConstant("MOTOR_FRONT_RIGHT");

    private static final boolean[] MOTOR_FRONT_RIGHT_INVERTED_VALS = {true, true, false};
    public static final boolean MOTOR_FRONT_RIGHT_INVERTED =
                    getConstant("MOTOR_FRONT_RIGHT_INVERTED");


    // Left gearbox
    private static final int[] MOTOR_REAR_LEFT_VALS = {2, 2, 2};
    public static final int MOTOR_REAR_LEFT = getConstant("MOTOR_REAR_LEFT");

    private static final boolean[] MOTOR_REAR_LEFT_INVERTED_VALS = {true, true, false};
    public static final boolean MOTOR_REAR_LEFT_INVERTED = getConstant("MOTOR_REAR_LEFT_INVERTED");


    // Right gearbox
    private static final int[] MOTOR_REAR_RIGHT_VALS = {4, 4, 4};
    public static final int MOTOR_REAR_RIGHT = getConstant("MOTOR_REAR_RIGHT");

    private static final boolean[] MOTOR_REAR_RIGHT_INVERTED_VALS = {true, true, false};
    public static final boolean MOTOR_REAR_RIGHT_INVERTED =
                    getConstant("MOTOR_REAR_RIGHT_INVERTED");

    
    private static final double[] ENCODER_LEFT_DPP_VALS = {-79.0 / 108800.0, 6.0 / 2052.25, 1.0 / 264};
    public static final double ENCODER_LEFT_DPP = getConstant("ENCODER_LEFT_DPP");
    

    private static final double[] ENCODER_RIGHT_DPP_VALS = {79.0 / 108800.0, 6.0 / 2052.25, 1.0 / 264};
    public static final double ENCODER_RIGHT_DPP = getConstant("ENCODER_RIGHT_DPP");


    private static final boolean[] INVERT_JOYSTICK_X_VALS = {true, true, true};
    public static final boolean INVERT_JOYSTICK_X = getConstant("INVERT_JOYSTICK_X");

    private static final boolean[] INVERT_JOYSTICK_Y_VALS = {false, false, false};
    public static final boolean INVERT_JOYSTICK_Y = getConstant("INVERT_JOYSTICK_Y");

    private static final int[] SELECTOR_CHANNEL_VALS = {0, 0, 0};
    public static final int SELECTOR_CHANNEL = getConstant("SELECTOR_CHANNEL");

    private static final int[] RING_LIGHT_VALS = {12, 12, 12};
    public static final int RING_LIGHT = getConstant("RING_LIGHT");

    private static final int[] LEFT_ULTRASONIC_PING_CHANNEL_VALS = {4, 4, 4};
    public static final int LEFT_ULTRASONIC_PING_CHANNEL =
                    getConstant("LEFT_ULTRASONIC_PING_CHANNEL");

    private static final int[] LEFT_ULTRASONIC_ECHO_CHANNEL_VALS = {5, 5, 5};
    public static final int LEFT_ULTRASONIC_ECHO_CHANNEL =
                    getConstant("LEFT_ULTRASONIC_ECHO_CHANNEL");

    private static final int[] RIGHT_ULTRASONIC_PING_CHANNEL_VALS = {6, 6, 6};
    public static final int RIGHT_ULTRASONIC_PING_CHANNEL =
                    getConstant("RIGHT_ULTRASONIC_PING_CHANNEL");

    private static final int[] RIGHT_ULTRASONIC_ECHO_CHANNEL_VALS = {7, 7, 7};
    public static final int RIGHT_ULTRASONIC_ECHO_CHANNEL =
                    getConstant("RIGHT_ULTRASONIC_ECHO_CHANNEL");

    private static final double[] DISTANCE_SENSOR_SEPARATION_CM_VALS = {59.69, 59.69, 59.69};
    public static final double DISTANCE_SENSOR_SEPARATION_CM =
                    getConstant("DISTANCE_SENSOR_SEPARATION_CM");
    
    
    // Raspberry Pi IP for vision *NOTE: Mikes laptop is 10.27.6.10, rPI is 10.27.6.240
    private static final String[] RPI_IPS_VALS = {"10.27.6.240", "10.27.6.240", "10.27.6.10"};
    public static final String RPI_IPS = getConstant("RPI_IPS");
    
    /**
     * Prints which RobotMap is being used
     */
    public static void log() {
        Log.d("RobotMap", "RobotMap ID is " + ROBOT_ID);
    }
    
    private static final String ROBOT_ID_LOC = "/home/lvuser/robot-type.conf";
    
    private static int getRobotID() {
        int temp = 0;

        try (Scanner sc = new Scanner(new BufferedReader(new FileReader(ROBOT_ID_LOC)))) {
            temp = sc.nextInt();
        } catch (FileNotFoundException e) {
            temp = 0;
        }

        return temp;
    }

    @SuppressWarnings("unchecked")
    private static <T> T getConstant(String constant) {
        try {
            return RobotConfig.get("RobotMap." + constant,
                (T) getArray(RobotMap.class.getDeclaredField(constant + "_VALS").get(null))[ROBOT_ID]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    float boat; // must be a float or else it sinks

    private static Object[] getArray(Object val) {
        int arrlength = Array.getLength(val);
        Object[] outputArray = new Object[arrlength];

        for (int i = 0; i < arrlength; ++i) {
            outputArray[i] = Array.get(val, i);
        }

        return outputArray;
    }
}
