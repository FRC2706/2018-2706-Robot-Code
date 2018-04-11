package org.usfirst.frc.team2706.robot;

import edu.wpi.first.wpilibj.command.Command;

public abstract class LoggedCommand extends Command {
    
    public LoggedCommand() {
        super();
    }
    
    public LoggedCommand(String name) {
        super(name);
    }
    
    @Override
    protected void initialize() {
        super.initialize();
        
        Log.d(this, "Command Started");
    }
    
    @Override
    protected void interrupted() {
        super.interrupted();
        
        Log.d(this, "Command Interrupted");
    }
    
    @Override
    protected void end() {
        super.end();
        
        Log.d(this, "Command Ended");
    }
}
