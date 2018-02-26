package org.usfirst.frc.team2706.robot.commands;

import org.usfirst.frc.team2706.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

public class CheckLiftHeight extends Command {

    public CheckLiftHeight() {

    }
    
    
  public void initialize() {}
    
 
    public void execute() {
        Robot.lift.checkHeight();
    }
    
    public void end() {}
    

    @Override
    protected boolean isFinished() {
        // TODO Auto-generated method stub
        return false;
    }

    
    
}
