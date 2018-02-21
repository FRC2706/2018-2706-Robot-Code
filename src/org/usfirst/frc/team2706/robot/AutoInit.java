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
import org.usfirst.frc.team2706.robot.commands.teleop.ArcadeDriveWithJoystick;
import org.usfirst.frc.team2706.robot.subsystems.AutonomousSelector;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Where autonomous modes are set up, hardcoded, and ran
 */
public class AutoInit {
    
    // The command that will be ran
    Command autonomousCommand;
    
    // The spinny dial on the robot that selects what autonomous mode we are going to do
    private AutonomousSelector selectorSwitch;
    
    // Dashboard selection
    DashboardAutoSelector dashBoardAutoSelector;
    
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
    
    public AutoInit() {
        selectorSwitch = new AutonomousSelector();
        setDashboardPriorities();
        setSelectorPriorities();
    }
    
    /**
     * Gets things ready to listen for a NetworkTables input from dashboard
     */
    public void setDashboardPriorities() {
        // Dashboard Autonomous Mode List
        Priority[] leftPriorities = {
                            leftStartLeftSwitch, leftStartLeftScale, leftStartRightScale, driveForward, doNothing
                        };
        
        Priority[] centerPriorities = {
                            centerStartLeftSwitch, centerStartRightSwitch, centerStartExchangeCube, driveForward, doNothing
                        };
        
        Priority[] rightPriorities = {
                            rightStartRightSwitch, rightStartRightScale, rightStartLeftScale, driveForward, doNothing
                        };
        dashBoardAutoSelector = new DashboardAutoSelector(leftPriorities, centerPriorities, rightPriorities);
        dashBoardAutoSelector.getPositionAndRespond();
    }
    
    /**
     * Sets up the auto selector to turn knobs into results
     */
    public void setSelectorPriorities() {
        // WARNING DO NOT AUTOFORMAT THIS OR BAD THINGS WILL HAPPEN TO YOU
        // Set up our autonomous modes with the two hardware selector switches
        setAutonomousCommandList(
            // no switch: do nothing
            new Priority[][] {
                new Priority[] {doNothing}
            },
            // position 1: do Nothing
            new Priority[][] {
                new Priority[] {doNothing}
            },
            // position 2: drive forward
            new Priority[][] {
                new Priority[] {driveForward}
            },
            // position 3: center position
            new Priority[][] {
                // position 1: exchange cube
                new Priority[] {centerStartExchangeCube}, 
                // position 2: do any switch
                new Priority[] {centerStartLeftSwitch, centerStartRightSwitch}
            },
            // position 4: left position
            new Priority[][] {
                // position 1: do left switch
                new Priority[] {leftStartLeftSwitch, driveForward},
                // position 2: do left scale
                new Priority[] {leftStartLeftScale, driveForward},
                // position 3: try left switch, otherwise do left scale
                new Priority[] {leftStartLeftSwitch, leftStartLeftScale, driveForward},
                // position 4: try left scale, otherwise do left switch
                new Priority[] {leftStartLeftScale, leftStartLeftSwitch, driveForward},
                // position 5: try left switch, otherwise do scale
                new Priority[] {leftStartLeftSwitch, leftStartLeftScale, leftStartRightScale},
                // position 6: do scale
                new Priority[] {leftStartLeftScale, leftStartRightScale}
            },
            // position 5: Right Position
            new Priority[][] {
                // position 1: do right switch
                new Priority[] {rightStartRightSwitch, driveForward},
                // position 2: do right scale
                new Priority[] {rightStartRightScale, driveForward},
                // position 3: try right switch, otherwise do right scale
                new Priority[] {rightStartRightSwitch, rightStartRightScale, driveForward},
                // position 4: try right scale, otherwise do right switch
                new Priority[] {rightStartRightScale, rightStartRightSwitch, driveForward},
                // position 5: try right switch, otherwise do scale
                new Priority[] {rightStartRightSwitch, rightStartRightScale, rightStartLeftScale},
                // position 6: do scale
                new Priority[] {rightStartRightScale, rightStartLeftScale}
            }
        );
    }
    /**
     * Call in Robot.autonomousInit()
     */
    public void initialize() {
        
        // Great for safety just in case you set the wrong one in practice ;)
        Log.i("Autonomous Selector", "Running " + getAutonomousCommand(new ArcadeDriveWithJoystick()) + "...");

        autonomousCommand = getAutonomousCommand(new ArcadeDriveWithJoystick());

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
     * Call in Robot.teleopInit();
     */
    public void end() {
        /*
         * This makes sure that the autonomous stops running when teleop starts running. If you want
         * the autonomous to continue until interrupted by another command, remove this line or
         * comment it out.
         */
        if (autonomousCommand != null)
            autonomousCommand.cancel();
    }
    
    public void setAutonomousCommandList(Priority[][]... commands) {
        selectorSwitch.setCommands(commands);
    }

    public Command getAutonomousCommand(Command fallbackCommand) {
        return Priority.chooseCommandFromPriorityList(selectorSwitch.getSelected());
    }
}
