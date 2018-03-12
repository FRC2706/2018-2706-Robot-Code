package org.usfirst.frc.team2706.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class InitLift extends CommandGroup {

    public InitLift() {
        this.addSequential(new SetLiftHeightBlocking(0.25,2,0.1),0.25);
    }
    
}
