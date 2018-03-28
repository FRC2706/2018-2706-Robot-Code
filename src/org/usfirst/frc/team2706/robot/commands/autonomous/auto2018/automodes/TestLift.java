package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.commands.SetLiftHeightBlocking;
import org.usfirst.frc.team2706.robot.commands.SetLiftHeightBlockingAfterTime;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class TestLift extends CommandGroup {

    public TestLift() {
        this.addParallel(new SetLiftHeightBlocking(5,2,0.2));
        this.addSequential(new SetLiftHeightBlockingAfterTime(Double.MIN_VALUE,2,0.2,1000));
    }
}
