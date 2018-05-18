package org.usfirst.frc.team2706.robot.commands;

import org.usfirst.frc.team2706.robot.Log;
import org.usfirst.frc.team2706.robot.LoggedCommand;
import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.controls.talon.TalonPID;
import org.usfirst.frc.team2706.robot.subsystems.Lift;

/**
 * Moves the lift to the height that it is set to go to
 */
public class MoveLiftToDestination extends LoggedCommand {

    private final TalonPID liftPID;

    /**
     * Moves the lift to the height that it is set to go to
     */
    public MoveLiftToDestination() {
        //this.requires(Robot.lift);
        liftPID = Robot.lift.getPID();
    }

    @Override
    protected void initialize() {
        Log.i("Lift", Robot.lift.disabled());
        
        working = true;

        // Ensure lift output is within motor speed bounds
        liftPID.setOutputRange(-Lift.SPEED, Lift.SPEED);

        // Make lift go to its current height
        setDestination(Robot.lift.getEncoderHeight());

        // Use the PID for moving up by default
        Robot.lift.useUpPID();
    }

    public void setDestination(double destination) {
        Log.d(this, "Setting destination to " + destination);
        liftPID.setSetpoint(destination);

    }

    @Override
    public void execute() {
        Log.d(this, "Current setpoint: " + liftPID.getSetpoint());
        liftPID.update();

        if (Robot.lift.getEncoderHeight() < 0) {
            Robot.lift.reset();
        }
        
        if(Robot.lift.disabled()) {
            if(this.liftPID.enabled) {
                liftPID.disable();
            }
        }
        else {
            if(!this.liftPID.enabled) {
                liftPID.enable();
            }
        }
    }

    @Override
    public void end() {
        liftPID.disable();
        working = false;
    }

    @Override
    public void interrupted() {
        end();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
    
    private boolean working = false;
    public boolean working() {
        return working;
    }
}
