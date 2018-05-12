
package org.usfirst.frc.team2706.robot;

import org.usfirst.frc.team2706.robot.commands.autonomous.experimential.recordreplay.RecordJoystick;
import org.usfirst.frc.team2706.robot.controls.operatorFeedback.Rumbler;
import org.usfirst.frc.team2706.robot.subsystems.Bling;
import org.usfirst.frc.team2706.robot.subsystems.Climber;
import org.usfirst.frc.team2706.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2706.robot.subsystems.Intake;
import org.usfirst.frc.team2706.robot.subsystems.Lift;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class controls all of the robot initialization, every tick of the robot, and should be very
 * bare bones to preserve readability and simplicity. Do not change the class name without updating
 * the manifest file, and references to different subsystems should be static. Refer to your local
 * gatekeeper if you have no idea what you are doing :)
 */
public class Robot extends IterativeRobot {

    // The robot's main drive train
    public static DriveTrain driveTrain;

    // intake subsystem
    public static Intake intake;
    
    // Lift subsystem
    public static Lift lift;
    
    //climber code
    public static Climber climb;

    // Stores all of the joysticks, and returns them as read only.
    public static OI oi;
    
    // Bling Subsystem
    public static Bling blingSystem;

    // Records joystick states to file for later replaying
    RecordJoystick recordAJoystick;
    
    AutoInit autoInit;

    private static boolean enteredTeleop;
    
    /**
     * This function is run when the robot is first started up and should be used for any
     * initialization code.
     */
    public void robotInit() {
        Log.setUpLogging();

        Log.i("Robot", "Starting robot code");

        RobotMap.log();

        // FIXME: Camera causes (at least partially) to memory issues
//        UsbCamera camera = CameraServer.getInstance().startAutomaticCapture(0);
//        Runtime.getRuntime().addShutdownHook(new Thread(camera::free));
        
        // Instantiate the robot subsystems
        driveTrain = new DriveTrain();

        // Make sure to initialize cube intake and eject
        // mechanisms
        intake = new Intake(RobotMap.INTAKE_LEFT_MOTOR_MAX_POWER, 
                            RobotMap.INTAKE_RIGHT_MOTOR_MAX_POWER,
                            RobotMap.EJECT_MAX_POWER);
        
        // Initialize lift
        lift = new Lift();
        
        //Climber initialization 
        climb = new Climber(); 

        
        oi = new OI();
        
        autoInit = new AutoInit();
        recordAJoystick = new RecordJoystick(oi.getDriverJoystick(), oi.getOperatorJoystick(),
                        () -> SmartDashboard.getString("record-joystick-name", "default"),
                        "recordJoystick");
        
        blingSystem = new Bling();
    }

    /**
     * This function is called once each time the robot enters Disabled mode. You can use it to
     * reset any subsystem information you want to clear when the robot is disabled.
     */
    public void disabledInit() {
        Log.i("Robot", "Disabled");
        
        lift.enable();
        Log.updateTableLog();
        if(enteredTeleop) {
            Log.save();
        }
        // Stop timer on the dashboard
        SmartDashboard.putBoolean("time_running", false);
    }

    public void disabledPeriodic() {
        Scheduler.getInstance().run();
        log();
    }

    /**
     * This autonomous (along with the chooser code above) shows how to select between different
     * autonomous modes using the dashboard. The sendable chooser code works with the Java
     * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
     * uncomment the getString code to get the auto name from the text box below the Gyro
     *
     * You can add additional auto modes by adding additional commands to the chooser code above
     * (like the commented example) or additional comparisons to the switch structure below with
     * additional strings & commands.
     */
    public void autonomousInit() {
        // Begin timer.
        SmartDashboard.putBoolean("time_running", true);
        Log.i("Robot", "Entering autonomous mode");
        Log.i("Robot", "Autonomous game specific message: " + DriverStation.getInstance().getGameSpecificMessage());

        driveTrain.reset();
        lift.resetSetpoint();
        
        autoInit.initialize();
        
        // Start timer on the dashboard
        SmartDashboard.putBoolean("time_running", true);
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        log();
    }

    public void teleopInit() {
        Log.i("Robot", "Entering teleop mode");
        
        Log.i("Robot", "Teleop game specific message: " + DriverStation.getInstance().getGameSpecificMessage());
        enteredTeleop = true;
        
        Robot.lift.resetSetpoint();
        
        autoInit.end();
        
        Robot.driveTrain.brakeMode(true);
        
        if (SmartDashboard.getBoolean("record-joystick", false))
            recordAJoystick.start();
        // Tell drive team to drive
        new Rumbler(0.2, 0.1, 3, Rumbler.DRIVER_JOYSTICK);
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
      SmartDashboard.putBoolean("CubeIn", Robot.intake.cubeCaptured());
        
        Scheduler.getInstance().run();
        log();
    }

    @Override
    public void testInit() {
        Log.i("Robot", "Entering test mode");
        initTestMode();
        Robot.lift.resetSetpoint();
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {}

    private void log() {
        // Don't use unnecessary bandwidth at competition
        if (!DriverStation.getInstance().isFMSAttached() || DriverStation.getInstance().isDisabled()) {
            driveTrain.log();
            autoInit.selectorSwitch.log();
            lift.log();
            intake.log();
        }
        
        if(DriverStation.getInstance().isEnabled()) {
            driveTrain.debugLog();
            autoInit.selectorSwitch.debugLog();
            lift.debugLog();
            intake.debugLog();
            climb.debugLog();
        }
    }
    public void initTestMode() {
        driveTrain.initTestMode();
        intake.initTestMode();
        lift.initTestMode();
        climb.initTestMode();
        autoInit.initTestMode();
    }
}
