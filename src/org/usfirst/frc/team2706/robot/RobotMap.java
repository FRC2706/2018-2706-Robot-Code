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

    private static final boolean[] MOTOR_FRONT_LEFT_INVERTED_VALS = {false, false, false};
    public static final boolean MOTOR_FRONT_LEFT_INVERTED =
                    getConstant("MOTOR_FRONT_LEFT_INVERTED");


    // Right gearbox
    private static final int[] MOTOR_FRONT_RIGHT_VALS = {3, 3, 3};
    public static final int MOTOR_FRONT_RIGHT = getConstant("MOTOR_FRONT_RIGHT");

    private static final boolean[] MOTOR_FRONT_RIGHT_INVERTED_VALS = {false, false, false};
    public static final boolean MOTOR_FRONT_RIGHT_INVERTED =
                    getConstant("MOTOR_FRONT_RIGHT_INVERTED");


    // Left gearbox
    private static final int[] MOTOR_REAR_LEFT_VALS = {2, 2, 2};
    public static final int MOTOR_REAR_LEFT = getConstant("MOTOR_REAR_LEFT");

    private static final boolean[] MOTOR_REAR_LEFT_INVERTED_VALS = {false, false, false};
    public static final boolean MOTOR_REAR_LEFT_INVERTED = getConstant("MOTOR_REAR_LEFT_INVERTED");


    // Right gearbox
    private static final int[] MOTOR_REAR_RIGHT_VALS = {4, 4, 4};
    public static final int MOTOR_REAR_RIGHT = getConstant("MOTOR_REAR_RIGHT");

    private static final boolean[] MOTOR_REAR_RIGHT_INVERTED_VALS = {false, false, false};
    public static final boolean MOTOR_REAR_RIGHT_INVERTED =
                    getConstant("MOTOR_REAR_RIGHT_INVERTED");
    
    
    private static final double[] ENCODER_LEFT_DPP_VALS = {-1.0 / 2517.5, -1.0 / 2555.0, 1.0 / 264};
    public static final double ENCODER_LEFT_DPP = getConstant("ENCODER_LEFT_DPP");
    

    private static final double[] ENCODER_RIGHT_DPP_VALS = {1.0 / 2517.5, 1.0 / 2555, 1.0 / 264};
    public static final double ENCODER_RIGHT_DPP = getConstant("ENCODER_RIGHT_DPP");


    private static final boolean[] INVERT_JOYSTICK_X_VALS = {false, false, true};
    public static final boolean INVERT_JOYSTICK_X = getConstant("INVERT_JOYSTICK_X");

    private static final boolean[] INVERT_JOYSTICK_Y_VALS = {true, true, false};
    public static final boolean INVERT_JOYSTICK_Y = getConstant("INVERT_JOYSTICK_Y");

    private static final int[] SELECTOR1_CHANNEL_VALS = {0, 0, 0};
    public static final int SELECTOR1_CHANNEL = getConstant("SELECTOR1_CHANNEL");
    
    private static final int[] SELECTOR2_CHANNEL_VALS = {3, 3, 3};
    public static final int SELECTOR2_CHANNEL = getConstant("SELECTOR2_CHANNEL");

    
    // Climber
    private static final int[] CLIMBER_MOTOR_VALS = {8, 8, 8};
    public static final int CLIMBER_MOTOR = getConstant("CLIMBER_MOTOR");

    private static final int[] CLIMBER_IR_SENSOR_VALS = {2, 2, 2};
    public static final int CLIMBER_IR_SENSOR = getConstant("CLIMBER_IR_SENSOR");
    
    // Lift
    private static final int[] MOTOR_LIFT_VALS = {5, 5, 5};
    public static final int MOTOR_LIFT = getConstant("MOTOR_LIFT");
    
    private static final boolean[] MOTOR_LIFT_INVERTED_VALS = {true, true, true};
    public static final boolean MOTOR_LIFT_INVERTED = getConstant("MOTOR_LIFT_INVERTED");

    private static final double[] ENCODER_LIFT_DPP_VALS = {169.0 / 1169280.0, -1.0 / 7075.0, 1};
    public static final double ENCODER_LIFT_DPP = getConstant("ENCODER_LIFT_DPP");
    
    private static final int[] LIMIT_DOWN_VALS = {1, 1, 1};
    public static final int LIMIT_DOWN = getConstant("LIMIT_DOWN");
    
    // Intake
    private static final int[] INTAKE_MOTOR_LEFT_VALS = {6, 6, 6};
    public static final int INTAKE_MOTOR_LEFT =
                    getConstant("INTAKE_MOTOR_LEFT");
    
    private static final int[] INTAKE_MOTOR_RIGHT_VALS = {7, 7, 7};
    public static final int INTAKE_MOTOR_RIGHT =
                    getConstant("INTAKE_MOTOR_RIGHT");
    
    private static final int[] INTAKE_IR_SENSOR_VALS = {1, 1, 1};
    public static final int INTAKE_IR_SENSOR = getConstant("INTAKE_IR_SENSOR");
    
    public static final double INTAKE_LEFT_MOTOR_MAX_POWER = 0.25;
    public static final double INTAKE_RIGHT_MOTOR_MAX_POWER = 0.5;
    public static final double EJECT_MAX_POWER = 1;
    
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
