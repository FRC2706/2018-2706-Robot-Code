package org.usfirst.frc.team2706.robot.commands.bling.patterns;

import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.commands.bling.BlingController;
import org.usfirst.frc.team2706.robot.subsystems.Bling;

public class CubeInSignaller extends BlingPattern {

    public CubeInSignaller() {
        // Pattern will function during climb and regular teleop.
        operationPeriod.add(BlingController.TELEOP_WITHOUT_CLIMB);
        operationPeriod.add(BlingController.CLIMBING_PERIOD);

        // Set command to theatre chase
        command = Bling.THEATRE_CHASE;

        // Set other stuff
        wait_ms = 50;

        // Set to nice rgb merge colour
        rgbColourCode = Bling.MERGERGB;
    }

    @Override
    public boolean conditionsMet() {
        // If cube is in, signal.
        return Robot.intake.cubeCaptured();
    }

}
