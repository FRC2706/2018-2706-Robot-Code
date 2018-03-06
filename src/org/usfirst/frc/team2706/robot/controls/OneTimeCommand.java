package org.usfirst.frc.team2706.robot.controls;

import edu.wpi.first.wpilibj.command.Command;

public class OneTimeCommand extends Command {

    private final Runnable command;
    
    public OneTimeCommand(Runnable command) {
        this.command = command;
    }
    
    @Override
    public void initialize() {
        command.run();
    }
    
    @Override
    protected boolean isFinished() {
        return true;
    }
    
    /**
     * Not sure why anyone wants this...
     * 
     * @param command The runnable to run like a command like a runnable
     */
    public static void run(Runnable command) {
        run(new OneTimeCommand(command));
    }
    
    public static void run(OneTimeCommand command) {
        command.start();
    }
}