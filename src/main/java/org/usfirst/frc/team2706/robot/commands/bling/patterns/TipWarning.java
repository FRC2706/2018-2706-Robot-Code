package org.usfirst.frc.team2706.robot.commands.bling.patterns;

import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.commands.bling.BlingController;
import org.usfirst.frc.team2706.robot.subsystems.Bling;
import org.usfirst.frc.team2706.robot.subsystems.Lift;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class TipWarning extends BlingPattern {

    Lift liftSystem;

    // Stuff for sending warnings to the dashboard.
    NetworkTableEntry warningAddQueue, warningRemoveQueue;
    private static final String WARNINGMESSAGE = "Beware Tipping";

    static final String NTKEY = "SmartDashboard/Warnings";

    public TipWarning() {
        liftSystem = Robot.lift;

        rgbColourCode = Bling.TURQUOISE;

        // Set the command that the bling strip will perform.
        command = Bling.BLINK;
        // Set the delay
        wait_ms = 100;

        operationPeriod.add(BlingController.TELEOP_WITHOUT_CLIMB);
        operationPeriod.add(BlingController.CLIMBING_PERIOD);

        // Set networktables variables.
        NetworkTable warningTable = NetworkTableInstance.getDefault().getTable(NTKEY);
        warningAddQueue = warningTable.getEntry("AddQueue");
        warningRemoveQueue = warningTable.getEntry("RemoveQueue");

    }

    @Override
    public boolean conditionsMet() {
        // If the arm height is above half the maximum height, begin warning about tipping
        return (liftSystem.getEncoderHeight() > (Lift.MAX_HEIGHT / 2));
    }

    @Override
    public void initialize() {
        super.initialize();

        // Also set a warning in dashboard.
        warningAddQueue.setString(WARNINGMESSAGE);
    }

    public void end() {
        // Remove the warning from the dashboard once we're done.
        warningRemoveQueue.setString(WARNINGMESSAGE);
    }
}
