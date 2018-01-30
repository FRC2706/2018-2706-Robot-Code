package org.usfirst.frc.team2706.robot.commands.autonomous.experimential.recordreplay;

import java.util.function.Supplier;

import org.usfirst.frc.team2706.robot.Log;
import org.usfirst.frc.team2706.robot.OI;
import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.RobotConfig;
import org.usfirst.frc.team2706.robot.commands.teleop.ArcadeDriveWithJoystick;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Records a {@link RecordableJoystick} for the driver and operator Joystick
 */
public class RecordJoystick extends Command {

    private final Supplier<String> nameSupplier;

    private Joystick driverStick;
    private Joystick operatorStick;

    /**
     * Uses a String as the Supplier for the name
     * 
     * @see #RecordJoystick(Joystick, Joystick, Supplier) The main constructor
     */
    public RecordJoystick(Joystick driverStick, Joystick operatorStick, String joystickName, String name) {
        this(driverStick, operatorStick, () -> RobotConfig.get(name + ".joystickName", joystickName), name);
    }

    /**
     * Records the driver and operator Joysticks to files based on the name from the Supplier given
     * 
     * @param driverStick The driver Joystick to record
     * @param operatorStick The operator Joystick to record
     * @param nameSupplier The Supplier that is used to find file locations when the command is
     *        enabled
     * @param name The name of the of the configuration properties to look for
     */
    public RecordJoystick(Joystick driverStick, Joystick operatorStick,
                    Supplier<String> nameSupplier, String name) {
        super(name);
        
        this.nameSupplier = nameSupplier;

        this.driverStick = driverStick;
        this.operatorStick = operatorStick;

        if (!SmartDashboard.containsKey("record-joystick"))
            SmartDashboard.putBoolean("record-joystick", false);

        if (!SmartDashboard.containsKey("record-joystick-name"))
            SmartDashboard.putString("record-joystick-name", "default");
    }

    @Override
    public void initialize() {
        String name = nameSupplier.get();
        String folder = "/home/lvuser/joystick-recordings/" + name + "/";

        Log.i("Record and Replay", "Recording joystick to folder " + folder);
        
        String driverLoc = folder + name + "-driver";
        String operatorLoc = folder + name + "-operator";

        driverStick = new RecordableJoystick(driverStick, driverLoc, false);
        operatorStick = new RecordableJoystick(operatorStick, operatorLoc, false);

        ((RecordableJoystick) driverStick).init(this::timeSinceInitialized);
        ((RecordableJoystick) operatorStick).init(this::timeSinceInitialized);

        if (Robot.driveTrain.getDefaultCommand() instanceof ArcadeDriveWithJoystick) {
            ((ArcadeDriveWithJoystick) Robot.driveTrain.getDefaultCommand())
                            .setJoystick(driverStick);
        }

        Robot.oi.destroy();
        Robot.oi = new OI(driverStick, operatorStick);
    }

    @Override
    public void execute() {
        ((RecordableJoystick) driverStick).update();
        ((RecordableJoystick) operatorStick).update();
    }


    @Override
    public boolean isFinished() {
        return ((RecordableJoystick) driverStick).done()
                        && ((RecordableJoystick) operatorStick).done();
    }

    @Override
    public void end() {
        super.end();
        
        ((RecordableJoystick) driverStick).end();
        ((RecordableJoystick) operatorStick).end();

        // Make sure that Oi receives a real joystick, not a RecordableJoystick
        while(driverStick instanceof RecordableJoystick) {
            driverStick = ((RecordableJoystick) driverStick).getRealJoystick();
        }
        
        while(operatorStick instanceof RecordableJoystick) {
            operatorStick = ((RecordableJoystick) operatorStick).getRealJoystick();
        }
        
        Robot.oi = new OI(driverStick, operatorStick);
    }

    @Override
    public void interrupted() {
        end();
    }
}
