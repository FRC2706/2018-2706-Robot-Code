package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.SetLiftHeight;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;
import org.usfirst.frc.team2706.robot.commands.autonomous.experimential.curvedrive.CurveDrive;
import org.usfirst.frc.team2706.robot.commands.autonomous.experimential.curvedrive.CurveDriveStop;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CenterStartLeftSwitch extends CommandGroup {
    
    public CenterStartLeftSwitch() {
        CurveDrive c = new CurveDrive(65 / 12.0, 94 / 12.0, 0, 0.7, true, 0.25, "CurveToSfawfawfwitch");
        this.addSequential(c);
        this.addParallel(new CurveDriveStop(c.endCurve));
        this.addParallel(new SetLiftHeight(2.5), 1.5);
        this.addSequential(new StraightDriveWithEncoders(0.4, 1, 1, 3, "lul"), 1.5);
        this.addSequential(new EjectCube(0.6),0.5);
    }
    
}