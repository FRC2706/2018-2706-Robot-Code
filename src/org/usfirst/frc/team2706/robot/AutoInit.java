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
import org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.multicube.CenterStartLeftSwitchMultiCube;
import org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.multicube.CenterStartRightSwitchMultiCube;
import org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.multicube.LeftStartLeftScaleLeftSwitchMultiCube;
import org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.multicube.RightStartRightScaleRightSwitchMultiCube;
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
    public AutonomousSelector selectorSwitch;

    // Dashboard selection
    DashboardAutoSelector dashBoardAutoSelector;

    Priority doNothing, driveForward, centerStartExchangeCube, centerStartLeftSwitch,
                    centerStartRightSwitch, centerStartLeftSwitchMultiCube,
                    centerStartRightSwitchMultiCube, leftStartLeftSwitch, rightStartRightSwitch,
                    leftStartLeftScale, rightStartRightScale, leftStartRightScale,
                    rightStartLeftScale, leftStartLeftScaleMultiCube, rightStartRightScaleMultiCube;

    public AutoInit() {

        doNothing = new Priority(new ArcadeDriveWithJoystick());

        driveForward = new Priority("drive_forward", "Drive Forward", new DriveForward());

        centerStartExchangeCube = new Priority("center_exchange", "Exchange Cube",
                        new CenterStartExchangeCube());

        centerStartLeftSwitch = new Priority("center_left_switch", "Center Start Left Switch",
                        Priority.IS_SWITCH, !Priority.IS_SCALE, Priority.LEFT,
                        new CenterStartLeftSwitch());

        centerStartRightSwitch = new Priority("center_right_switch", "Center Start Right Switch",
                        Priority.IS_SWITCH, !Priority.IS_SCALE, Priority.RIGHT,
                        new CenterStartRightSwitch());

        centerStartLeftSwitchMultiCube = new Priority("center_left_switch_multi",
                        "Center Start Left Switch Multi Cube", Priority.IS_SWITCH,
                        !Priority.IS_SCALE, Priority.LEFT, new CenterStartLeftSwitchMultiCube());

        centerStartRightSwitchMultiCube = new Priority("center_right_switch_multi",
                        "Center Start Right Switch Multi Cube", Priority.IS_SWITCH,
                        !Priority.IS_SCALE, Priority.RIGHT, new CenterStartRightSwitchMultiCube());

        leftStartLeftSwitch = new Priority("left_left_switch", "Left Start Left Switch",
                        Priority.IS_SWITCH, !Priority.IS_SCALE, Priority.LEFT,
                        new LeftStartLeftSwitch());

        rightStartRightSwitch = new Priority("right_right_switch", "Right Start Right Switch",
                        Priority.IS_SWITCH, !Priority.IS_SCALE, Priority.RIGHT,
                        new RightStartRightSwitch());

        leftStartLeftScale = new Priority("left_left_scale", "Left Start Left Scale",
                        !Priority.IS_SWITCH, Priority.IS_SCALE, Priority.LEFT,
                        new LeftStartLeftScale());

        rightStartRightScale = new Priority("right_right_scale", "Right Start Right Scale",
                        !Priority.IS_SWITCH, Priority.IS_SCALE, Priority.RIGHT,
                        new RightStartRightScale());

        leftStartRightScale = new Priority("left_right_scale", "Left Start Right Scale",
                        !Priority.IS_SWITCH, Priority.IS_SCALE, Priority.RIGHT,
                        new LeftStartRightScale());

        rightStartLeftScale = new Priority("right_left_scale", "Right Start Left Scale",
                        !Priority.IS_SWITCH, Priority.IS_SCALE, Priority.LEFT,
                        new RightStartLeftScale());
        
        leftStartLeftScaleMultiCube = new Priority("left_left_scale_multi", "Left Start Left Scale to Left Switch",
                        Priority.IS_SWITCH, Priority.IS_SCALE, Priority.LEFT,
                        new LeftStartLeftScaleLeftSwitchMultiCube());
        
        rightStartRightScaleMultiCube = new Priority("right_right_scale_multi", "Right Start Right Scale to Right Switch",
                        Priority.IS_SWITCH, Priority.IS_SCALE, Priority.RIGHT,
                        new RightStartRightScaleRightSwitchMultiCube());

        selectorSwitch = new AutonomousSelector();
        setDashboardPriorities();
        setSelectorPriorities();
    }

    /**
     * Gets things ready to listen for a NetworkTables input from dashboard
     */
    public void setDashboardPriorities() {
        // Dashboard Autonomous Mode List
        Priority[] leftPriorities = {leftStartLeftSwitch, leftStartLeftScale, leftStartRightScale,
                        driveForward, leftStartLeftScaleMultiCube, doNothing};

        Priority[] centerPriorities = {centerStartLeftSwitch, centerStartRightSwitch,
                        centerStartLeftSwitchMultiCube, centerStartRightSwitchMultiCube,
                        centerStartExchangeCube, driveForward, doNothing};

        Priority[] rightPriorities = {rightStartRightSwitch, rightStartRightScale,
                        rightStartLeftScale, rightStartRightScaleMultiCube, driveForward, doNothing};
        dashBoardAutoSelector = new DashboardAutoSelector(leftPriorities, centerPriorities,
                        rightPriorities);
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
                        new Priority[][] {new Priority[] {doNothing}},
                        // position 1: do Nothing
                        new Priority[][] {new Priority[] {doNothing}},
                        // position 2: drive forward
                        new Priority[][] {new Priority[] {driveForward}},
                        // position 3: center position
                        new Priority[][] {
                                        // position 1: exchange cube
                                        new Priority[] {centerStartExchangeCube},
                                        // position 2: do any switch
                                        new Priority[] {centerStartLeftSwitch,
                                                        centerStartRightSwitch},
                                        // position 3: do any switch multi cube
                                        new Priority[] {centerStartLeftSwitchMultiCube,
                                                        centerStartRightSwitchMultiCube}},
                        // position 4: left position
                        new Priority[][] {
                                        // position 1: do left switch
                                        new Priority[] {leftStartLeftSwitch, driveForward},
                                        // position 2: do left scale
                                        new Priority[] {leftStartLeftScale, driveForward},
                                        // position 3: try left switch, otherwise do left scale
                                        new Priority[] {leftStartLeftSwitch, leftStartLeftScale,
                                                        driveForward},
                                        // position 4: try left scale, otherwise do left switch
                                        new Priority[] {leftStartLeftScale, leftStartLeftSwitch,
                                                        driveForward},
                                        // position 5: try left switch, otherwise do scale
                                        new Priority[] {leftStartLeftSwitch, leftStartLeftScale,
                                                        leftStartRightScale},
                                        // position 6: do scale
                                        new Priority[] {leftStartLeftScale, leftStartRightScale},
                                        // position 7: multi cube no fallback
                                        new Priority[] {leftStartLeftScaleMultiCube, leftStartLeftScale, leftStartLeftSwitch, driveForward},
                                        // position 6: multi cube right scale fallback
                                        new Priority[] {leftStartLeftScaleMultiCube, leftStartLeftScale, leftStartLeftSwitch, leftStartRightScale}},
                        // position 5: Right Position
                        new Priority[][] {
                                        // position 1: do right switch
                                        new Priority[] {rightStartRightSwitch, driveForward},
                                        // position 2: do right scale
                                        new Priority[] {rightStartRightScale, driveForward},
                                        // position 3: try right switch, otherwise do right scale
                                        new Priority[] {rightStartRightSwitch, rightStartRightScale,
                                                        driveForward},
                                        // position 4: try right scale, otherwise do right switch
                                        new Priority[] {rightStartRightScale, rightStartRightSwitch,
                                                        driveForward},
                                        // position 5: try right switch, otherwise do scale
                                        new Priority[] {rightStartRightSwitch, rightStartRightScale,
                                                        rightStartLeftScale},
                                        // position 6: do scale
                                        new Priority[] {rightStartRightScale,
                                                        rightStartLeftScale},
                                        // position 7: multi cube no fallback
                                        new Priority[] {rightStartRightScaleMultiCube, rightStartRightScale, rightStartRightSwitch, driveForward},
                                        // position 6: multi cube right scale fallback
                                        new Priority[] {rightStartRightScaleMultiCube, rightStartRightScale, rightStartRightSwitch, rightStartLeftScale}});
    }

    /**
     * Call in Robot.autonomousInit()
     */
    public void initialize() {

        autonomousCommand = new CenterStartRightSwitchMultiCube();//getAutonomousCommand(new ArcadeDriveWithJoystick());

        Command dashboardResponse = Priority
                        .chooseCommandFromPriorityList(dashBoardAutoSelector.getPriorityList());
        Robot.driveTrain.brakeMode(true);

        // If no input falls back on the auto switches if (dashboardResponse == null) { //
        // Schedule the autonomous command that was selected
        // autonomousCommand = new SetLiftHeightBlocking(1, 4, 0.1);

        Log.d("Auto", "uRunning " + dashboardResponse + ", " + "switch running "
                        + autonomousCommand);
        if (dashboardResponse != null)
            dashboardResponse.start();
        else
            autonomousCommand.start();

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

    public void initTestMode() {
        selectorSwitch.selector1.setName("Auto Selector","Selector 1");
        selectorSwitch.selector2.setName("Auto Selector","Selector 2");
    }
    public void setAutonomousCommandList(Priority[][]... commands) {
        selectorSwitch.setCommands(commands);
    }

    public Command getAutonomousCommand(Command fallbackCommand) {
        return Priority.chooseCommandFromPriorityList(selectorSwitch.getSelected());
    }
}
