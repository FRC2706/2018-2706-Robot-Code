package org.usfirst.frc.team2706.robot.commands.autonomous.experimential.curvedrive;

import java.util.LinkedHashMap;

import org.usfirst.frc.team2706.robot.Log;
import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.RobotConfig;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Drives from one position to another and ends at a certain line based on a cubic equation creator.
 */
public class CurveDriveTwoSpeed extends Command {

    // How many feet you want to go right
    private final double xFeet;

    // How many feet you want to go forward
    private final double yFeet;

    // What angle do you want to end at
    public final double endCurve;

    // TODO remove this for velocity calculator
    private final double firstSpeed;

    private final double secondSpeed, thirdSpeed;

    private final double splitSpeedY, splitSpeedY2;

    // The equation that the robot will follow
    private CubicEquation eq;

    // If you're not resetting before driving you need to remember where you started
    private double initHeading;

    private final boolean isRight;

    private final double P = 0.1, I = 0, D = 0, FF = 0.0;

    private final PIDController PID;

    /**
     * Drives to a specified point and ends at a specified angle.
     * 
     * @param xFeet Distance right, in feet
     * @param yFeet Distance forward, in feet (preferably not negative)
     * @param endCurve Ending angle (-90 to 90 degrees, but only useful at -80 to 80)
     * @param firstSpeed
     * @param secondSpeed
     * @param splitSpeedY
     * @param isRight Is the curve taking the robot right?
     * @param tangentOffset offsets of the tangents for interpolation
     * @param name The name of the of the configuration properties to look for
     */
    public CurveDriveTwoSpeed(double xFeet, double yFeet, double endCurve, double firstSpeed,
                    double secondSpeed, double thirdSpeed, double splitSpeedY, double splitSpeedY2,
                    boolean isRight, String name) {
        super(name);
        requires(Robot.driveTrain);

        this.xFeet = RobotConfig.get(name + ".xFeet", xFeet);
        this.yFeet = RobotConfig.get(name + ".yFeet", yFeet);
        this.endCurve = RobotConfig.get(name + ".endCurve", endCurve);
        this.firstSpeed = RobotConfig.get(name + ".firstSpeed", firstSpeed);
        this.secondSpeed = RobotConfig.get(name + ".secondSpeed", secondSpeed);
        this.thirdSpeed = RobotConfig.get(name + ".secondSpeed", thirdSpeed);
        this.splitSpeedY = RobotConfig.get(name + ".splitSpeedY", splitSpeedY);
        this.splitSpeedY2 = RobotConfig.get(name + ".splitSpeedY2", splitSpeedY2);
        this.isRight = RobotConfig.get(name + ".isRight", isRight);

        this.PID = new PIDController(P, I, D, FF, new PIDInput(),
                        (turn) -> twoSpeedArcadeDrive(-turn));
//         SmartDashboard.putNumber("P", SmartDashboard.getNumber("P", P));
//         SmartDashboard.putNumber("I", SmartDashboard.getNumber("I", I));
//         SmartDashboard.putNumber("D", SmartDashboard.getNumber("D", D));
    }

    // Called just before this Command runs the first time
    protected void initialize() {
//         PID.setP(SmartDashboard.getNumber("P", P));
//         PID.setI(SmartDashboard.getNumber("I", I));
//         PID.setD(SmartDashboard.getNumber("D", D));
        // Creates the cubic equation that the robot follows
        eq = EquationCreator.MakeCubicEquation(xFeet, yFeet, endCurve, isRight);
//         Log.d(this, eq);
        // Resets the gyro and encoders
        Robot.driveTrain.reset();
        initHeading = Robot.driveTrain.getHeading();
        Log.d(this, "Current encoder ticks are " + Robot.driveTrain.getDistance());

        PID.enable();
    }

    @Override
    protected boolean isFinished() {
//         System.out.println("isF" + (yPos - yFeet));
        // Checks if the x is within 1.5 feet and the y within 0.2 feet
        return yPos - yFeet > 0.0;
    }

    /**
     * Resets everything in the command so it can be reused
     */
    protected void end() {
        PID.disable();

        xPos = 0;
        yPos = 0;
        Log.d(this, "Finished with encoder ticks at " + Robot.driveTrain.getDistance());
        Robot.driveTrain.brakeMode(true);
//         new CurveDriveStop(endCurve).start();
        lastEncoderAv = 0;
        lastGyro = 0;
    }

    protected void interrupt() {
        end();
    }

    private double getRotateVal() {
        // Figures out the angle that you are currently on
        double tangent = (3 * eq.a * Math.pow(yPos, 2)) + (2 * eq.b * yPos);
        tangent = Math.toDegrees(Math.atan(tangent));

        return tangent - (Robot.driveTrain.getHeading() - initHeading);
    }

    LinkedHashMap<Double, Double> tangents;

    public void twoSpeedArcadeDrive(double turn) {
        if (yPos < splitSpeedY) {
            Robot.driveTrain.arcadeDrive(firstSpeed, turn);
        } else if (yPos < splitSpeedY2) {
            Robot.driveTrain.arcadeDrive(secondSpeed, turn);
        } else {
            Robot.driveTrain.arcadeDrive(thirdSpeed, turn);
        }
    }

    private double xPos = 0;

    private double yPos = 0;

    private double lastEncoderAv = 0;
    private double lastGyro = 0;

    /**
     * Called every tick to keep position, an x and y position, not always accurate due to a few
     * reasons
     */
    private void findPosition() {
        // Gets gyro angle
        double gyroAngle = Robot.driveTrain.getHeading() - initHeading;
        double changeInGyro = gyroAngle - lastGyro;
        double encoderAv = (Robot.driveTrain.getDistance() - lastEncoderAv);
        // Gets the radius of the arc
        double distance;
        if (Math.abs(changeInGyro) > 0.001) {
            double radius = encoderAv / Math.toRadians(changeInGyro);

            // Calculate distance based on arc lengths, and invert if driving backwards
            distance = (encoderAv > 0 ? 1 : -1) * Math.sqrt((2 * Math.pow(radius, 2))
                            * (1 - Math.cos(Math.toRadians(changeInGyro))));
        } else {
            distance = encoderAv;
        }

        // Uses trigonometry 'n stuff to figure out how far right and forward you traveled
        double changedXPos = Math.sin(Math.toRadians((lastGyro + gyroAngle) / 2.0)) * distance;
        double changedYPos = Math.cos(Math.toRadians((lastGyro + gyroAngle) / 2.0)) * distance;

        // Adjusts your current position accordingly.
        xPos += changedXPos;
        yPos += changedYPos;

        SmartDashboard.putNumber("X Position", xPos);
        SmartDashboard.putNumber("Y Position", yPos);
//         System.out.println(xPos + " " + yPos);
        // Saves your encoder distance so you can calculate how far you've went in the new tick
        lastEncoderAv = Robot.driveTrain.getDistance();
        lastGyro = gyroAngle;
    }

    private class PIDInput implements PIDSource {

        private PIDSourceType pidSource = PIDSourceType.kDisplacement;

        @Override
        public void setPIDSourceType(PIDSourceType pidSource) {
            this.pidSource = pidSource;

        }

        @Override
        public PIDSourceType getPIDSourceType() {
            return pidSource;
        }

        @Override
        public double pidGet() {
            findPosition();
            return getRotateVal();
        }

    }
}
