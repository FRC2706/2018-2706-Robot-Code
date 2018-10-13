package org.usfirst.frc.team2706.robot.controls;

import org.usfirst.frc.team2706.robot.LoggedCommand;

/**
 * A command that runs once
 */
public class OneTimeCommand extends LoggedCommand {

    private final Runnable command;

    /**
     * Creates a one time command
     */
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

    /**
     * Runs a {@code OneTimeCommand}
     * 
     * @param command The {@code OneTimeCommand} to run
     */
    public static void run(OneTimeCommand command) {
        command.start();
    }
}
