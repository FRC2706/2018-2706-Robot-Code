package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.IntakeCube;
import org.usfirst.frc.team2706.robot.commands.MoveLiftDown;
import org.usfirst.frc.team2706.robot.commands.MoveLiftUp;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;
import org.usfirst.frc.team2706.robot.commands.autonomous.experimential.curvedrive.CurveDrive;
import org.usfirst.frc.team2706.robot.commands.autonomous.experimential.curvedrive.CurveDriveStop;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CenterStartRightSwitch extends CommandGroup {
    
    public CenterStartRightSwitch(boolean isMultiCube) {
        CurveDrive c = new CurveDrive(35 / 12.0, 85 / 12.0, 0, 0.8, false, 0.25, "CuawfawfrveToSwitch");
        this.addSequential(c);
        this.addParallel(new CurveDriveStop(c.endCurve));
        this.addSequential(new MoveLiftDown(),1.0);
        this.addSequential(new EjectCube(0.6),0.5);
        if(isMultiCube) {
            this.addParallel(new MoveLiftUp(), 1.0);
            this.addSequential(new StraightDriveWithEncoders(0.5, -1, 1, 3, "Back"));
            this.addSequential(new RotateDriveWithGyro(0.5, 90, 1, 3, "FFF"));
            this.addParallel(new IntakeCube(0.6), 2);
            this.addSequential(new StraightDriveWithEncoders(0.5, 1, 1, 3, "BBB"));
            this.addSequential(new StraightDriveWithEncoders(0.5, -1, 1, 3, "LLL"));
            this.addSequential(new RotateDriveWithGyro(0.5, -90, 1, 3, "WWW"));
            this.addParallel(new MoveLiftDown(), 1.0);
            this.addSequential(new StraightDriveWithEncoders(0.5, 1, 1, 3, "UHHHHHHHHH"));
            this.addSequential(new EjectCube(0.6),0.5);
        }
    }
    
}