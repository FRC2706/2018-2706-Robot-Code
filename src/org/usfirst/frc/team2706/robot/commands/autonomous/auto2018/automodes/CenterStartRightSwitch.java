package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.MoveLiftDown;
import org.usfirst.frc.team2706.robot.commands.autonomous.experimential.curvedrive.CurveDrive;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CenterStartRightSwitch extends CommandGroup {
    
    public CenterStartRightSwitch() {
        this.addSequential(new MoveLiftDown(),0.25);
        this.addSequential(new CurveDrive(65 / 12.0, 85 / 12.0, 0, 0.5, false, 0.25, "CuawfawfrveToSwitch"), 6);
        this.addSequential(new MoveLiftDown(),3);
        this.addSequential(new EjectCube(0.6),2);
    }
    
}