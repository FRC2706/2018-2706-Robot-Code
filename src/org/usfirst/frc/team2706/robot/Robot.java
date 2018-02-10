
package org.usfirst.frc.team2706.robot;

import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.TalonStraightDriveWithEncoders;
import org.usfirst.frc.team2706.robot.commands.autonomous.experimential.curvedrive.CurveDrive;
import org.usfirst.frc.team2706.robot.commands.autonomous.experimential.recordreplay.RecordJoystick;
import org.usfirst.frc.team2706.robot.commands.autonomous.experimential.recordreplay.ReplayRecordedJoystick;
import org.usfirst.frc.team2706.robot.commands.teleop.ArcadeDriveWithJoystick;
import org.usfirst.frc.team2706.robot.controls.StickRumble;
import org.usfirst.frc.team2706.robot.subsystems.Camera;
import org.usfirst.frc.team2706.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2706.robot.subsystems.Intake;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class controls all of the robot initialization, every tick of the robot, and should be very
 * bare bones to preserve readability and simplicity. Do not change the class name without updating
 * the manifest file, and references to different subsystems should be static. Refer to your local
 * gatekeeper if you have no idea what you are doing :)
 */
public class Robot extends IterativeRobot {

    // Reference for the main vision camera on the robot
    public static Camera camera;

    // The robot's main drive train
    public static DriveTrain driveTrain;
    
    //intake subsystem
    public static Intake intake;
    
    public static Intake exhale;

    // Stores all of the joysticks, and returns them as read only.
    public static OI oi;

    // Which command is going to be ran based on the hardwareChooser
    Command autonomousCommand;

    // Records joystick states to file for later replaying
    RecordJoystick recordAJoystick;

    // Rumbles joystick to tell drive team which mode we're in
    StickRumble rumbler;

    /**
     * This function is run when the robot is first started up and should be used for any
     * initialization code.
     */
    @SuppressWarnings("unused")
    public void robotInit() {
        Log.setUpLogging();

        Log.i("Robot", "Starting robot code");
        
        RobotMap.log();

        // Instantiate the robot subsystems
        driveTrain = new DriveTrain();

        camera = new Camera();
        
        //Make sure to initialize cube intake and eject
        //mechanisms
        intake = new Intake();
        exhale = new Intake();

        oi = new OI();
        // WARNING DO NOT AUTOFORMAT THIS OR BAD THINGS WILL HAPPEN TO YOU
        // Set up our autonomous modes with the hardware selector switch
        driveTrain.setAutonomousCommandList(
                        /* no switch: do nothing */ new ArcadeDriveWithJoystick(),
                       /* position 1: do nothing */ new ArcadeDriveWithJoystick(),
            /* position 2: Move Forward one foot */ new StraightDriveWithEncoders(0.3, 2, 1, 5, "AutoForwardFoot"),
                                                    new RotateDriveWithGyro(0.5, 90, 5, "AutoTurn90"),
                                                    new ReplayRecordedJoystick(oi.getDriverJoystick(), oi.getOperatorJoystick(), false, "2018", "replay"),
                                                    new TalonStraightDriveWithEncoders(0.3, 2, 1, 5, "AutoTalonForwardFoot"), 
                                                    new CurveDrive(6.395, 10.33, 0, 0.65, true, 0.25, "CurveToSwitch")
                                                    
        );

        // Set up the Microsoft LifeCam and start streaming it to the Driver Station
        // TODO Do switching with cam switching or just get rid of the variable because unused
        UsbCamera forwardCamera = CameraServer.getInstance().startAutomaticCapture(0);
        UsbCamera rearCamera = CameraServer.getInstance().startAutomaticCapture(1);

        recordAJoystick = new RecordJoystick(oi.getDriverJoystick(), oi.getOperatorJoystick(),
                        () -> SmartDashboard.getString("record-joystick-name", "default"),
                        "recordJoystick");
    }

    /**
     * This function is called once each time the robot enters Disabled mode. You can use it to
     * reset any subsystem information you want to clear when the robot is disabled.
     */
    public void disabledInit() {
        Log.updateTableLog();
        Log.save();
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
        Log.i("Robot", "Entering autonomous mode");
        
        driveTrain.reset();
        
        // Great for safety just in case you set the wrong one in practice ;)
        Log.i("Autonomous Selector", "Running " + driveTrain.getAutonomousCommand() + "...");

        autonomousCommand = driveTrain.getAutonomousCommand();
        Robot.driveTrain.brakeMode(true);
        // Schedule the autonomous command that was selected
        if (autonomousCommand != null)
            autonomousCommand.start();
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
        
        /*
         * This makes sure that the autonomous stops running when teleop starts running. If you want
         * the autonomous to continue until interrupted by another command, remove this line or
         * comment it out.
         */
        if (autonomousCommand != null)
            autonomousCommand.cancel();
        Robot.driveTrain.brakeMode(false);
        if (SmartDashboard.getBoolean("record-joystick", false))
            recordAJoystick.start();
        // Tell drive team to drive
        rumbler = new StickRumble(0.4, 0.15, 1, 0, 1, 1.0, 1, "controllerStickRumble");
        rumbler.start();

        // Deactivate the camera ring light
        // camera.enableRingLight(false);
    }
    
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        log();
    }

    @Override
    public void testInit() {
        Log.i("Robot", "Entering test mode");
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {}

    private void log() {
        // Don't use unecessary bandwidth at competition
        if(!DriverStation.getInstance().isFMSAttached()) {
            driveTrain.log();
        }
    }
}
