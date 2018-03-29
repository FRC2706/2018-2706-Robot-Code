package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.commands.IntakeCubeCustom;

import edu.wpi.first.wpilibj.command.Command;

public class IntakeUntilGrabbedNoDrive extends Command {
public double leftSpeed, rightSpeed;
public double distance;
Command intakeCube;
    public IntakeUntilGrabbedNoDrive(double leftSpeed, double rightSpeed) {
        this.leftSpeed = leftSpeed;
        this.rightSpeed = rightSpeed;
    }
    public void initialize() {
       intakeCube = new IntakeCubeCustom(leftSpeed, rightSpeed);
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
            return Robot.intake.readIRSensor() > 1.35;        // TODO Auto-generated method stub
    }

}
