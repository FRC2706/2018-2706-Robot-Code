package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.commands.IntakeCube;

import edu.wpi.first.wpilibj.command.Command;

public class IntakeUntilGrabbedNoDrive extends Command {
public double intakeSpeed;
public double distance;
Command intakeCube;
    public IntakeUntilGrabbedNoDrive(double intakeSpeed) {
        this.intakeSpeed = intakeSpeed;
    }
    public void initialize() {
       intakeCube = new IntakeCube(intakeSpeed, false);
       intakeCube.start();
    }
    
    public void end() {
        distance = Robot.driveTrain.getDistance();
        Robot.driveTrain.drive(0,0);
        intakeCube.cancel();
    }
    @Override
    protected boolean isFinished() {
        System.out.println(Robot.intake.readIRSensor());
            return Robot.intake.readIRSensor() > 1.25;        // TODO Auto-generated method stub
    }

}
