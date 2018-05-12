package org.usfirst.frc.team2706.robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Command that logs whenever started, interrupted, or ended
 */
public abstract class LoggedCommand extends Command {
    
    /**
     * @see edu.wpi.first.wpilibj.command.Command#Command()
     */
    public LoggedCommand() {
        super();
    }
    
    /**
     * @see edu.wpi.first.wpilibj.command.Command#Command(String)
     * 
     * @param name The name of of the command
     */
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
