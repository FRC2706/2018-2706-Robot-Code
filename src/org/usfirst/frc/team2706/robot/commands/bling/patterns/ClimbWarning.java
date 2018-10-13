package org.usfirst.frc.team2706.robot.commands.bling.patterns;

import org.usfirst.frc.team2706.robot.commands.bling.BlingController;
import org.usfirst.frc.team2706.robot.controls.operatorFeedback.Rumbler;
import org.usfirst.frc.team2706.robot.subsystems.Bling;

import edu.wpi.first.wpilibj.Timer;

public class ClimbWarning extends BlingPattern {

    public ClimbWarning() {
        operationPeriod.add(BlingController.TELEOP_WITHOUT_CLIMB);
        operationPeriod.add(BlingController.CLIMBING_PERIOD);

        command = Bling.BLINK;

        rgbColourCode = Bling.RED;
    }

    @Override
    public boolean conditionsMet() {
        double timeLeft = Timer.getMatchTime();
        return timeLeft <= 45 && timeLeft > 40;
    }

    @Override
    public void initialize() {
        new Rumbler(2, 0, 1, Rumbler.JoystickSelection.BOTH_JOYSTICKS);
    }
}
