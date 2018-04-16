package org.usfirst.frc.team2706.robot.commands.autonomous.core;

import java.util.Timer;
import java.util.TimerTask;

import org.usfirst.frc.team2706.robot.Log;
import org.usfirst.frc.team2706.robot.LoggedCommand;
import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.RobotConfig;

/**
 * Have the robot drive certain amount of time
 */
public class StraightDriveWithTime extends LoggedCommand {

    private final double speed;

    private final long time;

    /**
     * Drive at a specific speed for a certain amount of time
     * 
     * @param speed Speed in range [-1,1]
     * @param time Time in milliseconds to drive
     * @param name The name of the of the configuration properties to look for
     */
    public StraightDriveWithTime(double speed, long time, String name) {
        super(name);
        requires(Robot.driveTrain);

        this.speed = -RobotConfig.get(name + ".speed", speed);

        this.time = RobotConfig.get(name + ".time", time);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Log.i(this, "Driving for " + time / 1000 + " seconds");
        
        // Creates task to stop robot after time
        CommandTimerTask interrupt = new CommandTimerTask();
        new Timer().schedule(interrupt, time);

        done = false;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        // Drive while command is running
        Robot.driveTrain.drive(speed, speed);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return done;
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.driveTrain.drive(0, 0);
        Log.i(this, "Done driving");
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }

    private boolean done;

    private class CommandTimerTask extends TimerTask {

        public void run() {
            done = true;
        }
    }
}
