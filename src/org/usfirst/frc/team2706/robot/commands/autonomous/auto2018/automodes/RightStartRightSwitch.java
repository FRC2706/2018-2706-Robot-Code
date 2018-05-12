package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.InitLift;
import org.usfirst.frc.team2706.robot.commands.SetLiftHeightBlocking;
import org.usfirst.frc.team2706.robot.commands.autonomous.AutoConstants;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Starts in the right position.
 * 
 * Puts the cube in the side of the switch, then backs up and faces forward
 */
public class RightStartRightSwitch extends CommandGroup {
    
    public RightStartRightSwitch() {
        // Put the lift down
        this.addParallel(new InitLift());
        
        // Drive beside switch
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_FAST, 142 / 12.0, AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".forwardToSwitch"), 7);
        
        // Turn towards switch
        this.addSequential(new RotateDriveWithGyro(-90, this + ".rotateToSwitch"),3);
        
        // Raise the lift        
        this.addSequential(new SetLiftHeightBlocking(AutoConstants.SWITCH_HEIGHT,2,0.2),2);
        
        // Drive into switch
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_CONTROLLED,48 / 12.0,AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".driveToSwitch"),1);
     
        // Place the cube in the switch
        this.addSequential(new EjectCube(AutoConstants.EJECT_SPEED),0.8);
    }
    
}