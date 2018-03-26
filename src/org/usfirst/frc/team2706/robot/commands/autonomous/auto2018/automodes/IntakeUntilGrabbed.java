package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.commands.IntakeCube;

import edu.wpi.first.wpilibj.command.Command;

public class IntakeUntilGrabbed extends Command {
public double driveSpeed, intakeSpeed;
public double distance;
Command intakeCube;
    public IntakeUntilGrabbed(double driveSpeed, double intakeSpeed) {
        this.driveSpeed = driveSpeed;
        this.intakeSpeed = intakeSpeed;
    }
    public void initialize() {
       intakeCube = new IntakeCube(intakeSpeed, false);
       intakeCube.start();
    }
    public void execute() {
        Robot.driveTrain.drive(driveSpeed, driveSpeed);
    }
    public void end() {
        distance = Robot.driveTrain.getDistance();
        Robot.driveTrain.drive(0,0);
        intakeCube.cancel();
    }
    @Override
    protected boolean isFinished() {
        System.out.println(Robot.intake.readIRSensor());
            return Robot.intake.readIRSensor() > 1.2;        // TODO Auto-generated method stub
    }

}
