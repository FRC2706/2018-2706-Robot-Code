package org.usfirst.frc.team2706.robot.subsystems;

import java.util.function.Supplier;

import org.usfirst.frc.team2706.robot.JoystickMap;
import org.usfirst.frc.team2706.robot.Log;
import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.RobotMap;
import org.usfirst.frc.team2706.robot.commands.teleop.ArcadeDriveWithJoystick;
import org.usfirst.frc.team2706.robot.controls.talon.EWPI_TalonSRX;
import org.usfirst.frc.team2706.robot.controls.talon.TalonEncoder;
import org.usfirst.frc.team2706.robot.controls.talon.TalonPID;
import org.usfirst.frc.team2706.robot.controls.talon.TalonSensorGroup;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The DriveTrain subsystem incorporates the sensors and actuators attached to the robots chassis.
 * These include four drive motors, a left and right encoder and a gyro.
 */
public class DriveTrain extends Subsystem {
    private EWPI_TalonSRX front_left_motor, back_left_motor, front_right_motor, back_right_motor;
    private DifferentialDrive drive;
    private TalonEncoder left_encoder, right_encoder;
    private AHRS gyro;

    // TODO: maybe we don't need this
    private GyroPIDSource gyroPIDSource;
    private AverageEncoderPIDSource encoderPIDSource;
    private TalonPID talonPID;

    public double initGyro;

    private Command defaultCommand;

    public DriveTrain() {
        super();
        front_left_motor = new EWPI_TalonSRX(RobotMap.MOTOR_FRONT_LEFT);
        back_left_motor = new EWPI_TalonSRX(RobotMap.MOTOR_REAR_LEFT);
        front_right_motor = new EWPI_TalonSRX(RobotMap.MOTOR_FRONT_RIGHT);
        back_right_motor = new EWPI_TalonSRX(RobotMap.MOTOR_REAR_RIGHT);

        front_left_motor.setInverted(RobotMap.MOTOR_FRONT_LEFT_INVERTED);
        back_left_motor.setInverted(RobotMap.MOTOR_REAR_LEFT_INVERTED);
        front_right_motor.setInverted(RobotMap.MOTOR_FRONT_RIGHT_INVERTED);
        back_right_motor.setInverted(RobotMap.MOTOR_REAR_RIGHT_INVERTED);
        
        front_left_motor.configPeakCurrentLimit(2, 0);
        back_left_motor.configPeakCurrentLimit(2, 0);
        front_right_motor.configPeakCurrentLimit(2, 0);
        back_right_motor.configPeakCurrentLimit(2, 0);
        
        setVoltageDrive(true);
        
        drive = new DifferentialDrive(new SpeedControllerGroup(front_left_motor, back_left_motor),
                        new SpeedControllerGroup(front_right_motor, back_right_motor));

        left_encoder = new TalonEncoder(front_left_motor);
        right_encoder = new TalonEncoder(front_right_motor);

        // Encoders may measure differently in the real world and in
        // simulation. In this example the robot move at some random value
        // per tick in the real world, but the simulated encoders
        // simulate 360 tick encoders. This if statement allows for the
        // real robot to handle this difference in devices.
        if (Robot.isReal()) {
            left_encoder.setDistancePerPulse(RobotMap.ENCODER_LEFT_DPP);
            right_encoder.setDistancePerPulse(RobotMap.ENCODER_RIGHT_DPP);
        } else {
            // Circumference in ft = 4in/12(in/ft)*PI
            left_encoder.setDistancePerPulse((4.0 / 12.0 * Math.PI) / 360.0);
            right_encoder.setDistancePerPulse((4.0 / 12.0 * Math.PI) / 360.0);
        }

        encoderPIDSource = new AverageEncoderPIDSource(left_encoder, right_encoder);

        talonPID = new TalonPID(
                        new TalonSensorGroup(front_left_motor, drive::setSafetyEnabled,
                                        left_encoder, back_left_motor),
                        new TalonSensorGroup(front_right_motor, drive::setSafetyEnabled,
                                        right_encoder, back_right_motor));

        // Set up navX gyro
        gyro = new AHRS(SPI.Port.kMXP);
        while (gyro.isCalibrating()) {
            ;
        }

        gyroPIDSource = new GyroPIDSource(this);

        reset();

      
        // selectorSwitch.setName("Drive Train", "Autonomous Selector");
    }

    public void setVoltageDrive(boolean voltage) {
        front_left_motor.setUseVoltage(voltage);
        back_left_motor.setUseVoltage(voltage);
        front_right_motor.setUseVoltage(voltage);
        back_right_motor.setUseVoltage(voltage);
    }
     
    public void initTestMode() {
        // Let's show everything on the LiveWindow
        new WPI_TalonSRX(1).setName("Drive Train","Left Front Motor");
        new WPI_TalonSRX(2).setName("Drive Train","Left Back Motor");
        new WPI_TalonSRX(3).setName("Drive Train","Right Front Motor");
        new WPI_TalonSRX(4).setName("Drive Train","Right Back Motor");
        
     
        
        left_encoder.setName("Drive Train", "Left Encoder");
        right_encoder.setName("Drive Train", "Right Encoder");
        gyro.setName("Drive Train", "Gyro");
    }
    /**
     * When no other command is running let the operator drive around using the Xbox joystick.
     */
    public void initDefaultCommand() {
        if (defaultCommand == null) {
            getDefaultCommand();
        }
        setDefaultCommand(defaultCommand);

        Log.i("Drive Train Command", defaultCommand);
    }

    public Command getDefaultCommand() {
        if (defaultCommand == null) {
            defaultCommand = new ArcadeDriveWithJoystick();
        }
        return defaultCommand;
    }

    /**
     * Get the NavX AHRS
     * 
     * @return the NavX AHRS
     */
    public AHRS getGyro() {
        return gyro;
    }

    /**
     * The log method puts interesting information to the SmartDashboard.
     */
    public void log() {
        SmartDashboard.putNumber("Left Distance", left_encoder.getDistance());
        SmartDashboard.putNumber("Right Distance", right_encoder.getDistance());
        SmartDashboard.putNumber("Left Speed (RPM)", left_encoder.getRate());
        SmartDashboard.putNumber("Right Speed (RPM)", right_encoder.getRate());
        SmartDashboard.putNumber("Gyro", gyro.getAngle());
        // SmartDashboard.putNumber("Autonomous Selector 1",
        // selectorSwitch.getVoltageAsIndex(selectorSwitch.selector1));
        // SmartDashboard.putNumber("Autonomous Selector 2",
        // selectorSwitch.getVoltageAsIndex(selectorSwitch.selector2));
    }

    /**
     * Tank style driving for the DriveTrain.
     * 
     * @param left Speed in range [-1,1]
     * @param right Speed in range [-1,1]
     */
    public void drive(double left, double right) {
        drive.tankDrive(left, right);
    }

    public void curvatureDrive(double speed, double rotation, boolean override) {
        if (Robot.oi.getDriverJoystick().getRawButton(JoystickMap.XBOX_LB_BUTTON)) {
            drive.curvatureDrive(speed,(override ? rotation / 3.5 : rotation), override); 
        }
        else {
            drive.curvatureDrive(speed,(override ? rotation / 2 : rotation), override); 
        }
          
    }
    

    /**
     * @param joy The Xbox style joystick to use to drive arcade style.
     */
    public void arcadeDrive(double forward, double turn) {
        drive.arcadeDrive(forward, turn, true);
    }

    /**
     * @param joy The Xbox style joystick to use to drive arcade style.
     */
    public void drive(GenericHID joy) {
        double YAxis = RobotMap.INVERT_JOYSTICK_Y ? -joy.getRawAxis(5) : joy.getRawAxis(5);
        double XAxis = RobotMap.INVERT_JOYSTICK_X ? -joy.getRawAxis(4) : joy.getRawAxis(4);
        if (Robot.oi.getDriverJoystick().getRawButton(JoystickMap.XBOX_LB_BUTTON)) {
            YAxis /= 1.85;
            XAxis /= 1.5;
        }
        
        drive.arcadeDrive(YAxis, XAxis, true);
    }

    /**
     * Untested code that should allow you to drive relative to the field instead of the robot TODO
     * add a rotation joystick function for robot relative control when needed to align
     * 
     * @param joy The main drive joystick
     * @param rotate Joystick to rotate the robot with
     */
    public void headlessDrive(GenericHID joy) {
        Log.d("HeadlessDrive", "X: " + joy.getRawAxis(5) + ", Y: " + joy.getRawAxis(4));
        double raw5 = joy.getRawAxis(5);
        double raw4 = joy.getRawAxis(4);
        double angle = normalize(Math.toDegrees(Math.atan(raw5 / raw4)));

        double speed = (raw5 + raw4) / 2; // hyp
        if (Math.abs(speed) < 0.1) {
            speed = 0;
            angle = Robot.driveTrain.getHeading();
        }
        Log.d("Headless Drive", "Angle: " + angle + ", Speed: " + speed);
        double gyroAngle;
        gyroAngle = normalize(Robot.driveTrain.getHeading());
        Log.d("Headless Drive", "Output: " + (angle - gyroAngle * 0.1) * speed);
        drive.arcadeDrive(-speed, -(angle - gyroAngle * 0.1) * speed, true);
    }

    /**
     * Reset the robots sensors to the zero states.
     */
    public void reset() {
        resetEncoders();
        resetGyro();
        resetDisplacement();

        Log.i("Drive Train", "Resetting sensors");
    }

    /**
     * Reset the robot gyro to the zero state.
     */
    public void resetGyro() {
        gyro.reset();
    }

    /**
     * Reset the robot encoders to zero states
     */
    public void resetEncoders() {
        left_encoder.reset();
        right_encoder.reset();
    }

    /**
     * @return The robots heading in degrees.
     */
    public double getHeading() {
        return gyro.getAngle();
    }

    /**
     * Resets the displacement of the robot
     */
    public void resetDisplacement() {
        gyro.resetDisplacement();
    }

    /**
     * Gets the x distance of the robot with a direction
     */
    public double getDisplacementX() {
        return gyro.getDisplacementX();
    }

    /**
     * Gets the y distance of the robot with a direction
     */

    public double getDisplacementY() {
        return gyro.getDisplacementY();
    }

    /**
     * Gets the z distance of the robot with a direction
     */
    public double getDisplacementZ() {
        return gyro.getDisplacementZ();
    }

    /**
     * Sets the CANTalon motors to go into brake mode or coast mode
     * 
     * @param on Set to brake when true and coast when false
     */
    public void brakeMode(boolean on) {
        NeutralMode mode = on ? NeutralMode.Brake : NeutralMode.Coast;

        Log.i("Brake Mode", mode);

        front_left_motor.setNeutralMode(mode);
        back_left_motor.setNeutralMode(mode);
        front_right_motor.setNeutralMode(mode);
        back_right_motor.setNeutralMode(mode);
    }

    /**
     * @param useGyroStraightening True to invert second motor direction for rotating
     * 
     * @return The robot's drive PIDOutput
     */
    public PIDOutput getDrivePIDOutput(boolean useGyroStraightening, boolean useCamera,
                    boolean invert) {
        return new DrivePIDOutput(drive, useGyroStraightening, useCamera,
                        () -> -Robot.oi.getDriverJoystick().getRawAxis(JoystickMap.XBOX_LEFT_AXIS_Y), invert);
    }
    
    /**
     * @param useGyroStraightening True to invert second motor direction for rotating
     * 
     * @return The robot's drive PIDOutput
     */
    public PIDOutput getDrivePIDOutput(boolean useGyroStraightening, boolean useCamera,
                    Supplier<Double> forward, boolean invert) {
        return new DrivePIDOutput(drive, useGyroStraightening, useCamera, forward, invert);
    }

    /**
     * @param useGyroStraightening True to invert second motor direction for rotating
     * 
     * @return The robot's drive PIDOutput
     */
    public TalonPID getTalonPID() {
        return talonPID;
    }

    /**
     * @return The robot's gyro PIDSource
     */
    public PIDSource getGyroPIDSource(boolean invert) {
        gyroPIDSource.invert(invert);
        return gyroPIDSource;
    }

    public void invertGyroPIDSource(boolean invert) {
        gyroPIDSource.invert(invert);
    }

    /**
     * @return The distance driven (average of left and right encoders).
     */
    public double getDistance() {
        return (right_encoder.getDistance() + left_encoder.getDistance()) / 2;
    }
    
    /**
     * @return The robot's encoder PIDSource
     */
    public PIDSource getEncoderPIDSource(boolean left) {
        if (left) {
            return left_encoder;
        } else {
            return right_encoder;
        }

    }

    public PIDSource getAverageEncoderPIDSource() {
        return encoderPIDSource;
    }

    class UltrasonicPIDSource implements PIDSource {

        private final Ultrasonic left, right;

        public UltrasonicPIDSource(Ultrasonic left, Ultrasonic right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public void setPIDSourceType(PIDSourceType pidSource) {
            left.setPIDSourceType(pidSource);
            right.setPIDSourceType(pidSource);
        }

        @Override
        public PIDSourceType getPIDSourceType() {
            return left.getPIDSourceType();
        }

        @Override
        public double pidGet() {
            return (left.getRangeInches() + right.getRangeInches()) / 2;
        }

    }

    class AverageEncoderPIDSource implements PIDSource {

        private final TalonEncoder left, right;

        public AverageEncoderPIDSource(TalonEncoder left, TalonEncoder right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public void setPIDSourceType(PIDSourceType pidSource) {
            left.setPIDSourceType(pidSource);
            right.setPIDSourceType(pidSource);
        }

        @Override
        public PIDSourceType getPIDSourceType() {
            return left.getPIDSourceType();
        }

        @Override
        public double pidGet() {
           // Log.d("DriveTrain", "Got encoder input of " + right.getDistance());

            return (right.getDistance() + left.getDistance()) / 2;
        }

    }


    class GyroPIDSource implements PIDSource {

        private final DriveTrain driveTrain;
        private boolean invert;

        public GyroPIDSource(DriveTrain driveTrain) {
            this.driveTrain = driveTrain;
        }

        @Override
        public void setPIDSourceType(PIDSourceType pidSource) {
            driveTrain.gyro.setPIDSourceType(pidSource);
        };

        @Override
        public PIDSourceType getPIDSourceType() {
            return driveTrain.gyro.getPIDSourceType();
        }

        @Override
        public double pidGet() {
            double heading = driveTrain.getHeading();
            if (heading > 358.0)
                heading = 0;

         //   Log.d("Gyro PID", (invert ? -heading : heading));

            return invert ? -heading : heading;
        }


        public void invert(boolean invert) {
            this.invert = invert;
        }
    }

    public class DrivePIDOutput implements PIDOutput {

        private final DifferentialDrive drive;

        private boolean invert;

        private final boolean useGyroStraightening;

        private final boolean useCamera;
        private final Supplier<Double> forward;

        public DrivePIDOutput(DifferentialDrive drive, boolean useGyroStraightening,
                        boolean useCamera, Supplier<Double> forward, boolean invert) {
            this.drive = drive;
            this.useGyroStraightening = useGyroStraightening;
            this.useCamera = useCamera;
            this.forward = forward;
            this.invert = invert;
        }

        @Override
        public void pidWrite(double output) {


           // Log.d("Drive PID", output);

            double rotateVal;
            if (useCamera) {
                if (invert) {
                    drive.arcadeDrive(forward.get(), -output,
                                    false);
                } else {
                    drive.arcadeDrive(forward.get(), output,
                                    false);
                }
            } else {
                rotateVal = normalize(getHeading() - initGyro) * 0.1;



                if (useGyroStraightening) {
                    if (invert) {
                        drive.arcadeDrive(-output, rotateVal);
                    } else {
                        drive.arcadeDrive(output, -rotateVal);
                    }
                } else if (invert) {
                    drive.tankDrive(output, -output, false);
                } else {
                    drive.tankDrive(output, output, false);
                }
            }
        }

        public void setInvert(boolean invert) {
            this.invert = invert;
        }
    }

    public double normalize(double input) {
        double normalizedValue = input;
        while (normalizedValue > 180)
            normalizedValue -= 360;
        while (normalizedValue < -180)
            normalizedValue += 360;

        return normalizedValue;
    }
}
