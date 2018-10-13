package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.InitLift;
import org.usfirst.frc.team2706.robot.commands.autonomous.AutoConstants;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Starts in the centre position.
 * 
 * Places a cube in the exchange, then backs past auto line
 * 
 * This mode is often used when the lift does not work. This mode is also extremely consistent and
 * never failed at competition.
 */
public class CenterStartExchangeCube extends CommandGroup {

    public CenterStartExchangeCube() {
        // Put the lift down
        this.addSequential(new InitLift());

        // Drive 3 side of rectangle to face exchange
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_CONTROLLED, 46 / 12.0,
                        AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES,
                        this + ".driveForward"), 2);
        this.addSequential(new RotateDriveWithGyro(-90, this + ".rotateLeft"), 2);
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_CONTROLLED, 62 / 12.0,
                        AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES,
                        this + ".driveLeft"), 2);
        this.addSequential(new RotateDriveWithGyro(-90, this + ".rotateLeftAgain"), 2);
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_SLOW, 46 / 12.0,
                        AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES,
                        this + ".driveToExchange"), 2);

        // Put cube in the exchange
        this.addSequential(new EjectCube(AutoConstants.EJECT_SPEED), 2);

        // Rotate to the side and back up past auto line
        this.addSequential(new RotateDriveWithGyro(-30, this + ".rotateToSide"), 2);
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_CONTROLLED, -12,
                        AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES,
                        this + ".backUp"));
    }
}
