package org.usfirst.frc.team2706.robot.commands.autonomous.experimential.curvedrive;

import java.util.LinkedHashMap;

import org.usfirst.frc.team2706.robot.Log;
import org.usfirst.frc.team2706.robot.LoggedCommand;
import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.RobotConfig;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Drives from one position to another and ends at a certain line based on a cubic equation creator.
 */
public class CurveDrive extends LoggedCommand {

    // How many feet you want to go right
    private final double xFeet;

    // How many feet you want to go forward
    private final double yFeet;

    // What angle do you want to end at
    public final double endCurve;

    // TODO remove this for velocity calculator
    private final double speed;

    // The equation that the robot will follow
    private CubicEquation eq;

    // If you're not resetting before driving you need to remember where you started
    private double initHeading;

    private final boolean isRight;

    private final double P = 0.1, I = 0, D = 0, FF = 0.0;

    private final PIDController pid;

    /**
     * Drives to a specified point and ends at a specified angle.
     * 
     * @param xFeet Distance right, in feet
     * @param yFeet Distance forward, in feet (preferably not negative)
     * @param endCurve Ending angle (-90 to 90 degrees, but only useful at -80 to 80)
     * @param speed Base speed the robot drives (-1.0 to 1.0)
     * @param isRight Is the curve taking the robot right?
     * @param tangentOffset offsets of the tangents for interpolation
     * @param name The name of the of the configuration properties to look for
     */
    public CurveDrive(double xFeet, double yFeet, double endCurve, double speed, boolean isRight,
                    String name) {
        super(name);
        requires(Robot.driveTrain);

        this.xFeet = RobotConfig.get(name + ".xFeet", xFeet);
        this.yFeet = RobotConfig.get(name + ".yFeet", yFeet);
        this.endCurve = RobotConfig.get(name + ".endCurve", endCurve);
        this.speed = RobotConfig.get(name + ".speed", speed);
        this.isRight = RobotConfig.get(name + ".isRight", isRight);

        this.pid = new PIDController(P, I, D, FF, new PIDInput(),
                        (turn) -> Robot.driveTrain.arcadeDrive(speed, -turn));
//        SmartDashboard.putNumber("P", SmartDashboard.getNumber("P", P));
//        SmartDashboard.putNumber("I", SmartDashboard.getNumber("I", I));
//        SmartDashboard.putNumber("D", SmartDashboard.getNumber("D", D));
    }

    // Called just before this Command runs the first time
    protected void initialize() {
//        pid.setP(SmartDashboard.getNumber("P", P));
//        pid.setI(SmartDashboard.getNumber("I", I));
//        pid.setD(SmartDashboard.getNumber("D", D));
        // Creates the cubic equation that the robot follows
        eq = EquationCreator.MakeCubicEquation(xFeet, yFeet, endCurve, isRight);
        // Log.d(this, eq);
        // Resets the gyro and encoders
        Robot.driveTrain.reset();
        initHeading = Robot.driveTrain.getHeading();
        Log.i(this, "Current encoder ticks are " + Robot.driveTrain.getDistance());

        pid.enable();
    }

    @Override
    protected boolean isFinished() {
        // System.out.println("isF" + (yPos - yFeet));
        // Checks if the x is within 1.5 feet and the y within 0.2 feet
        return yPos - yFeet > 0.0;
    }

    /**
     * Resets everything in the command so it can be reused
     */
    protected void end() {
        pid.disable();

        xPos = 0;
        yPos = 0;
        Log.i(this, "Finished with encoder ticks at " + Robot.driveTrain.getDistance());
        Robot.driveTrain.brakeMode(true);
        // new CurveDriveStop(endCurve).start();
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

    public void followCurveRelational() {
        // Figures out the angle that you are currently on
        double tangent = (Robot.driveTrain.getHeading() - initHeading);

        double desiredTangent = 0;
        double desiredBelowY = 0;
        double desiredBelowTangent = 0;
        double desiredBelowClosestDistance = Double.POSITIVE_INFINITY;
        double desiredAboveY = 0;
        double desiredAboveTangent = 0;
        double desiredAboveClosestDistance = Double.POSITIVE_INFINITY;
        for (Double key : tangents.keySet()) {
            Double value = tangents.get(key);
            if (key <= yPos) {
                if (Math.abs(key - yPos) < desiredBelowClosestDistance) {
                    desiredBelowY = key;
                    desiredBelowTangent = value;
                    desiredBelowClosestDistance = Math.abs(key - yPos);
                }
            } else {
                if (Math.abs(key - yPos) < desiredAboveClosestDistance) {
                    desiredAboveY = key;
                    desiredAboveTangent = value;
                    desiredAboveClosestDistance = Math.abs(key - yPos);
                    // TODO break right here lol
                }
            }
        }

        double desiredBelowX =
                        (eq.a * Math.pow(desiredBelowY, 3)) + (eq.b * Math.pow(desiredBelowY, 2));
        double desiredAboveX =
                        (eq.a * Math.pow(desiredAboveY, 3)) + (eq.b * Math.pow(desiredAboveY, 2));

        // pythagoreum theorum
        double distanceBelow = Math.sqrt(
                        Math.pow(yPos - desiredBelowY, 2) + Math.pow(xPos - desiredBelowX, 2));
        double distanceAbove = Math.sqrt(
                        Math.pow(yPos - desiredAboveY, 2) + Math.pow(xPos - desiredAboveX, 2));
        double total = distanceBelow + distanceAbove;
        double proportionBelow = distanceAbove / total;
        double proportionAbove = distanceBelow / total;

        desiredTangent = desiredBelowTangent * proportionBelow
                        + desiredAboveTangent * proportionAbove;
        double rotateVal = desiredTangent - tangent;
        rotateVal /= 7;

        Robot.driveTrain.arcadeDrive(-speed, -rotateVal + (rotateVal < 0 ? 0 : -0));
    }

    public void followCurveArcade() {
        // Figures out the angle that you are currently on
        double tangent = (3 * eq.a * Math.pow(yPos, 2)) + (2 * eq.b * yPos);
        tangent = Math.toDegrees(Math.atan(tangent));

        // Finds out what x position you should be at, and compares it with what you are currently at
        double wantedX = (eq.a * Math.pow(yPos, 3)) + (eq.b * Math.pow(yPos, 2));

        double offset = xPos - wantedX;

        // Figures out how far you should rotate based on offset and gyro pos
        double rotateVal = offset * 10;
        // Tank Drives according to the above factors
        Robot.driveTrain.arcadeDrive(-speed, rotateVal + (rotateVal > 0 ? 0.3 : -0.3));
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
        // System.out.println(xPos + " " + yPos);
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
