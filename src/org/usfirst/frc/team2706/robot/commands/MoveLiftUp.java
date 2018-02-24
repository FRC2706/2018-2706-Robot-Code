package org.usfirst.frc.team2706.robot.commands;

import org.usfirst.frc.team2706.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

public class MoveLiftUp extends Command {

    public MoveLiftUp() {}
    
    
  public void initialize() {}
    
 
    public void execute() {
        Robot.lift.moveUp();
    }
    
    public void end() {
        Robot.lift.stop();
    }
    

    @Override
    protected boolean isFinished() {
        // TODO Auto-generated method stub
        return false;
    }

    
    
}
