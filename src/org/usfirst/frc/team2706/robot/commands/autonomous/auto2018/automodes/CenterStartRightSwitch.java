package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.MoveLiftDown;
import org.usfirst.frc.team2706.robot.commands.autonomous.experimential.curvedrive.CurveDrive;
import org.usfirst.frc.team2706.robot.commands.autonomous.experimential.curvedrive.CurveDriveStop;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CenterStartRightSwitch extends CommandGroup {
    
    public CenterStartRightSwitch() {
        CurveDrive c = new CurveDrive(35 / 12.0, 85 / 12.0, 0, 0.8, false, 0.25, "CuawfawfrveToSwitch");
        this.addSequential(c);
        this.addParallel(new CurveDriveStop(c.endCurve));
        this.addSequential(new MoveLiftDown(),1.0);
        this.addSequential(new EjectCube(0.6),0.5);
    }
    
}