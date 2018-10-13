package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.InitLift;
import org.usfirst.frc.team2706.robot.commands.SetLiftHeightBlocking;
import org.usfirst.frc.team2706.robot.commands.autonomous.AutoConstants;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Starts in the centre position.
 * 
 * Places the cube in the left switch
 */
public class CenterStartLeftSwitch extends CommandGroup {

    public CenterStartLeftSwitch() {
        // Put the lift down
        this.addParallel(new InitLift());

        // Drive forward
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_FAST, 0.8,
                        AutoConstants.LENIENT_ERROR, AutoConstants.LENIENT_CYCLES,
                        this + ".awayFromWall"), 1);

        // Turn and drive towards switch
        this.addSequential(new RotateDriveWithGyro(-50, this + ".faceSwitch"), 2);
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_FAST, 8.1,
                        AutoConstants.LENIENT_ERROR, AutoConstants.LENIENT_CYCLES,
                        this + ".forwardToSwitch"), 5);

        // Raise lift and face the switch
        this.addParallel(new SetLiftHeightBlocking(AutoConstants.SWITCH_HEIGHT, 2, 0.2), 2);
        this.addSequential(new RotateDriveWithGyro(50, this + ".rotateToSwitch"), 2);

        // Drive into switch
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_CONTROLLED, 3.5,
                        AutoConstants.LENIENT_ERROR, AutoConstants.LENIENT_CYCLES,
                        this + ".hitSwitch"), 2);

        // Place the cube in the switch
        this.addSequential(new EjectCube(AutoConstants.EJECT_SPEED), 0.8);
    }

}
