
package org.usfirst.frc.team2706.robot;

import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;
import org.usfirst.frc.team2706.robot.commands.autonomous.experimential.recordreplay.RecordJoystick;
import org.usfirst.frc.team2706.robot.commands.teleop.ArcadeDriveWithJoystick;
import org.usfirst.frc.team2706.robot.controls.StickRumble;
import org.usfirst.frc.team2706.robot.subsystems.AutonomousSelector;
import org.usfirst.frc.team2706.robot.subsystems.Camera;
import org.usfirst.frc.team2706.robot.subsystems.DriveTrain;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
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

    // The spinny dial on the robot that selects what autonomous mode we are going to do
    public static AutonomousSelector hardwareChooser;

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
        
        RobotMap.log();
        
        // Instantiate the robot subsystems
        driveTrain = new DriveTrain();

        camera = new Camera();
        
        oi = new OI();
        // WARNING DO NOT AUTOFORMAT THIS OR BAD THINGS WILL HAPPEN TO YOU
        // Set up our autonomous modes with the hardware selector switch
        hardwareChooser = new AutonomousSelector(
                         /* no switch: do nothing */ new ArcadeDriveWithJoystick(),
                        /* position 1: do nothing */ new ArcadeDriveWithJoystick(),
             /* position 2: Move Forward one foot */ new StraightDriveWithEncoders(0.4, 1, 1, 1, "AutoForwardFoot")
        );

        // Set up the Microsoft LifeCam and start streaming it to the Driver Station
        // TODO Do switching with cam switching or just get rid of the variable because unused
        UsbCamera forwardCamera = CameraServer.getInstance().startAutomaticCapture(0);
        UsbCamera rearCamera = CameraServer.getInstance().startAutomaticCapture(1);
        
        recordAJoystick = new RecordJoystick(oi.getDriverJoystick(), oi.getOperatorJoystick(),
                        () -> SmartDashboard.getString("record-joystick-name", "default"), "recordJoystick");        
    }

    /**
     * This function is called once each time the robot enters Disabled mode. You can use it to
     * reset any subsystem information you want to clear when the robot is disabled.
     */
    public void disabledInit() {}

    public void disabledPeriodic() {
        Scheduler.getInstance().run();
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
        driveTrain.reset();

        // Great for safety just in case you set the wrong one in practice ;)
        Log.i("Autonomous Selector", "Running " + hardwareChooser.getSelected() + "...");
        
        autonomousCommand = hardwareChooser.getSelected();
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

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }

    private void log() {
        driveTrain.log();
        hardwareChooser.log();

        
        Log.updateTableLog();
    }
}
