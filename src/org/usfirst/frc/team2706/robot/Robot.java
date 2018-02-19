
package org.usfirst.frc.team2706.robot;

import org.usfirst.frc.team2706.robot.commands.autonomous.DashboardAutoSelector;
import org.usfirst.frc.team2706.robot.commands.autonomous.Priority;
import org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.CenterStartExchangeCube;
import org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.CenterStartLeftSwitch;
import org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.CenterStartRightSwitch;
import org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.DriveForward;
import org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.LeftStartLeftScale;
import org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.LeftStartLeftSwitch;
import org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.LeftStartRightScale;
import org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.RightStartLeftScale;
import org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.RightStartRightScale;
import org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.RightStartRightSwitch;
import org.usfirst.frc.team2706.robot.commands.autonomous.experimential.recordreplay.RecordJoystick;
import org.usfirst.frc.team2706.robot.commands.teleop.ArcadeDriveWithJoystick;
import org.usfirst.frc.team2706.robot.controls.StickRumble;
import org.usfirst.frc.team2706.robot.subsystems.Camera;
import org.usfirst.frc.team2706.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2706.robot.subsystems.Intake;

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

    // intake subsystem
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

    DashboardAutoSelector dashBoardAutoSelector;

    /**
     * This function is run when the robot is first started up and should be used for any
     * initialization code.
     */
    public void robotInit() {
        Log.setUpLogging();

        Log.i("Robot", "Starting robot code");

        RobotMap.log();

        // Instantiate the robot subsystems
        driveTrain = new DriveTrain();

        camera = new Camera();

        // Make sure to initialize cube intake and eject
        // mechanisms
        intake = new Intake();
        exhale = new Intake();

        oi = new OI();

        /*
         * List of all possible auto modes & priority setup
         */
        Priority doNothing = new Priority(new ArcadeDriveWithJoystick());
        
        Priority driveForward = new Priority("drive_forward", "Drive Forward", new DriveForward());
        
        Priority centerStartExchangeCube = new Priority("center_exchange", "Exchange Cube",
                        new CenterStartExchangeCube());
        
        Priority centerStartLeftSwitch = new Priority("center_left_switch",
                        "Center Start Left Switch", Priority.SWITCH, Priority.LEFT,
                        new CenterStartLeftSwitch());
        
        Priority centerStartRightSwitch = new Priority("center_right_switch",
                        "Center Start Right Switch", Priority.SWITCH, Priority.RIGHT,
                        new CenterStartRightSwitch());
        
        Priority leftStartLeftSwitch = new Priority("left_left_switch", "Left Start Left Switch",
                        Priority.SWITCH, Priority.LEFT, new LeftStartLeftSwitch());
        
        Priority rightStartRightSwitch = new Priority("right_right_switch",
                        "Right Start Right Switch", Priority.SWITCH, Priority.RIGHT,
                        new RightStartRightSwitch());
        
        Priority leftStartLeftScale = new Priority("left_left_scale", "Left Start Left Scale",
                        Priority.SCALE, Priority.LEFT, new LeftStartLeftScale());
        
        Priority rightStartRightScale = new Priority("right_right_scale", "Right Start Right Scale",
                        Priority.SCALE, Priority.RIGHT, new RightStartRightScale());
        
        Priority leftStartRightScale = new Priority("left_right_scale", "Left Start Right Scale",
                        Priority.SCALE, Priority.RIGHT, new LeftStartRightScale());
        
        Priority rightStartLeftScale = new Priority("right_left_scale", "Right Start Left Scale",
                        Priority.SCALE, Priority.LEFT, new RightStartLeftScale());
        
        dashBoardAutoSelector = new DashboardAutoSelector();
        dashBoardAutoSelector.getPositionAndRespond();
        // WARNING DO NOT AUTOFORMAT THIS OR BAD THINGS WILL HAPPEN TO YOU
        // Set up our autonomous modes with the hardware selector switch
        driveTrain.setAutonomousCommandList(
            // no switch: do nothing
            new Priority[][] {
                new Priority[] {doNothing}
            },
            // position 1: Do Nothing
            new Priority[][] {
                new Priority[] {doNothing}
            },
            // position 2: Drive Forward
            new Priority[][] {
                new Priority[] {driveForward}
            },
            // position 3: Center Position
            new Priority[][] {
                new Priority[] {centerStartExchangeCube}, 
                new Priority[] {centerStartLeftSwitch, centerStartRightSwitch}
            },
            // position 4: Left Position
            new Priority[][] {
                new Priority[] {leftStartLeftSwitch, driveForward},
                new Priority[] {leftStartLeftScale, driveForward},
                new Priority[] {leftStartLeftSwitch, leftStartLeftScale, driveForward},
                new Priority[] {leftStartLeftScale, leftStartLeftSwitch, driveForward},
                new Priority[] {leftStartLeftSwitch, leftStartLeftScale, leftStartRightScale},
                new Priority[] {leftStartLeftScale, leftStartRightScale}
            },
            // position 5: Right Position
            new Priority[][] {
                new Priority[] {rightStartRightSwitch, driveForward},
                new Priority[] {rightStartRightScale, driveForward},
                new Priority[] {rightStartRightSwitch, rightStartRightScale, driveForward},
                new Priority[] {rightStartRightScale, rightStartRightSwitch, driveForward},
                new Priority[] {rightStartRightSwitch, rightStartRightScale, rightStartLeftScale},
                new Priority[] {rightStartRightScale, rightStartLeftScale}
            }
        );

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
        Log.i("Autonomous Selector", "Running " + driveTrain.getAutonomousCommand(new ArcadeDriveWithJoystick()) + "...");

        autonomousCommand = driveTrain.getAutonomousCommand(new ArcadeDriveWithJoystick());

        Command dashboardResponse = Priority.chooseCommandFromPriorityList(dashBoardAutoSelector.getPriorityList());
        Log.d("Autonomous Dashboard Selector", "Running " + dashboardResponse);
        Robot.driveTrain.brakeMode(true);

        // If no input falls back on the auto switches
        if (dashboardResponse == null) {
            // Schedule the autonomous command that was selected
            if (autonomousCommand != null)
                autonomousCommand.start();
        } else {
            dashboardResponse.start();
        }
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
        if (!DriverStation.getInstance().isFMSAttached()) {
            driveTrain.log();
        }
    }
}
