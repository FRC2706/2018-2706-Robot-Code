package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.commands.autonomous.experimential.curvedrive.CurveDrive;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CenterStartLeftSwitch extends CommandGroup {
    
    public CenterStartLeftSwitch() {
        this.addSequential(new CurveDrive(6.395, 10.33, 0, 0.65, true, 0.25, "CurveToSwitch"));
    }
    
}