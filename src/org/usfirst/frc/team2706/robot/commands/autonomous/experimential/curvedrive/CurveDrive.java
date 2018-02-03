package org.usfirst.frc.team2706.robot.commands.autonomous.experimential.curvedrive;
import java.util.LinkedHashMap;

import org.usfirst.frc.team2706.robot.Log;
import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.RobotConfig;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Drives from one position to another and ends at a certain line based on a cubic equation creator.
 */
public class CurveDrive extends Command {

    // How many feet you want to go right
    private final double xFeet;

    // How many feet you want to go forward
    private final double yFeet;

    // What angle do you want to end at
    private final double endCurve;

    // TODO remove this for velocity calculator
    private final double speed;

    // The equation that the robot will follow
    private CubicEquation eq;

    // If you're not resetting before driving you need to remember where you started
    private double initHeading;

    private final boolean isRight;

    /**
     * Drives to a specified point and ends at a specified angle.
     * 
     * @param xFeet Distance right, in feet
     * @param yFeet Distance forward, in feet (preferably not negative)
     * @param endCurve Ending angle (-90 to 90 degrees, but only useful at -80 to 80)
     * @param speed Base speed the robot drives (-1.0 to 1.0)
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
    }

    protected void initialize() {
        // Creates the cubic equation that the robot follows
        eq = EquationCreator.MakeCubicEquation(xFeet, yFeet, endCurve, isRight);
        tangents = EquationCreator.createTangents(0.5, yFeet, eq);
        Log.d(this, eq);
        // Resets the gyro and encoders
        Robot.driveTrain.reset();
        initHeading = Robot.driveTrain.getHeading();
    }

    protected void execute() {
        findPosition();
        followCurveRelational();
    }

    @Override
    protected boolean isFinished() {
        System.out.println(Math.abs(xPos - xFeet) + " , " +  Math.abs(yPos - yFeet));
        // Checks if the x is within 1.5 feet and the y within 0.2 feet
        if (Math.abs(xPos - xFeet) < 10 && Math.abs(yPos - yFeet) < 1)
            return true;
        return false;
    }

    /**
     * Resets everything in the command so it can be reused
     */
    protected void end() {
        xPos = 0;
        yPos = 0;
        Robot.driveTrain.reset();
        Robot.driveTrain.brakeMode(true);
        //new StraightDriveWithEncoders(0.5, 0.0, 0.1, 10).start();
        lastEncoderAv = 0;
    }

    protected void interrupt() {
        end();
    }

    /**
     * Uses gyro and encoders along with the equation of the line to actually follow the curve with
     * the robot. Uses tank drive.
     */
    @SuppressWarnings("unused")
    private void followCurve() {
        // Figures out the angle that you are currently on
        double tangent = (3 * eq.a * Math.pow(yPos, 2)) + (2 * eq.b * yPos);
        tangent = Math.toDegrees(Math.atan(tangent));

        // Finds out what x position you should be at, and compares it with what you are currently at
        double wantedX = (eq.a * Math.pow(yPos, 3)) + (eq.b * Math.pow(yPos, 2));

        double offset = xPos - wantedX;

        // Figures out how far you should rotate based on offset and gyro pos
        double rotateVal = tangent - (Robot.driveTrain.getHeading() - initHeading);
        rotateVal /= 9;

        // Calculates your tank drive speeds based on the base speed and the rotation
        double leftSpeed = (speed + rotateVal);
        double rightSpeed = (speed - rotateVal);

        if (Math.abs(leftSpeed - rightSpeed) > 0.4) {
            leftSpeed /= 2;
            rightSpeed /= 2;
        }
        // Tank Drives according to the above factors
        Robot.driveTrain.drive(-leftSpeed, -rightSpeed);
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
        for(Double key : tangents.keySet()) {
            Double value = tangents.get(key);
            if(key <= yPos) {
                if(Math.abs(key - yPos) < desiredBelowClosestDistance) {
                    desiredBelowY = key;
                    desiredBelowTangent = value;
                    desiredBelowClosestDistance = Math.abs(key - yPos);
                }
            }
            else {
                if(Math.abs(key - yPos) < desiredAboveClosestDistance) {
                    desiredAboveY = key;
                    desiredAboveTangent = value;
                    desiredAboveClosestDistance = Math.abs(key - yPos);
                    //TODO break right here lol
                }
            }
        }
       
       double desiredBelowX = (eq.a * Math.pow(desiredBelowY, 3)) + (eq.b * Math.pow(desiredBelowY, 2));
       double desiredAboveX = (eq.a * Math.pow(desiredAboveY, 3)) + (eq.b * Math.pow(desiredAboveY, 2));
       
       // pythagoreum theorum
       double distanceBelow = Math.sqrt(Math.pow(yPos - desiredBelowY, 2) + Math.pow(xPos - desiredBelowX, 2));
       double distanceAbove = Math.sqrt(Math.pow(yPos - desiredAboveY, 2) + Math.pow(xPos - desiredAboveX, 2));
       double total = distanceBelow + distanceAbove;
       double proportionBelow = distanceAbove / total;
       double proportionAbove = distanceBelow / total;
       
       desiredTangent = desiredBelowTangent * proportionBelow + desiredAboveTangent * proportionAbove;
       System.out.println(desiredTangent + "," + desiredBelowTangent + "," + desiredAboveTangent);
      // System.out.println(desiredTangent + " : " + desiredBelowX + "," + desiredBelowTangent + "," + distanceBelow + " : " + desiredAboveY + "," + desiredAboveTangent + "," + distanceAbove);
       double rotateVal = desiredTangent - tangent;
       rotateVal /= 10;

       Robot.driveTrain.arcadeDrive(-speed, -rotateVal);
    }
    public void followCurveArcade() {
        // Figures out the angle that you are currently on
        double tangent = (3 * eq.a * Math.pow(yPos, 2)) + (2 * eq.b * yPos);
        tangent = Math.toDegrees(Math.atan(tangent));

        // Finds out what x position you should be at, and compares it with what you are currently
        // at
        double wantedX = (eq.a * Math.pow(yPos, 3)) + (eq.b * Math.pow(xPos, 2));

        @SuppressWarnings("unused")
        double offset = xPos - wantedX;

        // Figures out how far you should rotate based on offset and gyro pos
        double rotateVal = tangent - (Robot.driveTrain.getHeading() - initHeading);
        rotateVal /= 5;
        // Tank Drives according to the above factors
        Robot.driveTrain.arcadeDrive(speed, rotateVal);
    }

    private double xPos = 0;

    private double yPos = 0;

    private double lastEncoderAv = 0;

    /**
     * Called every tick to keep position, an x and y position, not always accurate due to a few
     * reasons
     */
    private void findPosition() {
        // Gets gyro angle
        double gyroAngle = Robot.driveTrain.getHeading() - initHeading;

        // Gets encoder average distance
        double encoderAv = Robot.driveTrain.getDistance() - lastEncoderAv;

        // Uses trigonometry 'n stuff to figure out how far right and forward you travelled
        double changedXPos = Math.sin(Math.toRadians(gyroAngle)) * encoderAv;
        double changedYPos = Math.cos(Math.toRadians(gyroAngle)) * encoderAv;

        // Adjusts your current position accordingly.
        xPos += changedXPos;
        yPos += changedYPos;

        // Saves your encoder distance so you can calculate how far youve went in the new tick
        lastEncoderAv = Robot.driveTrain.getDistance();
    }
}
