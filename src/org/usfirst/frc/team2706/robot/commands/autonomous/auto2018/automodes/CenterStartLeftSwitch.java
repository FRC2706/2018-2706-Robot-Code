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

public class CenterStartLeftSwitch extends CommandGroup {
    
    public CenterStartLeftSwitch(boolean isMultiCube) {
        CurveDrive c = new CurveDrive(76 / 12.0, 94 / 12.0, 0, 0.8, true, 0.25, "CurveToSfawfawfwitch");
        this.addSequential(c);
        this.addParallel(new CurveDriveStop(c.endCurve));
        this.addParallel(new MoveLiftUp(),1.0);
        this.addSequential(new StraightDriveWithEncoders(0.5, 1, 1, 3, "lul"));
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