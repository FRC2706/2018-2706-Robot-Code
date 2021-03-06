package org.usfirst.frc.team2706.robot.commands.bling;

import java.util.ArrayList;
import java.util.HashMap;

import org.usfirst.frc.team2706.robot.LoggedCommand;
import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.commands.bling.patterns.Blank;
import org.usfirst.frc.team2706.robot.commands.bling.patterns.BlingPattern;
import org.usfirst.frc.team2706.robot.commands.bling.patterns.ClimbWarning;
import org.usfirst.frc.team2706.robot.commands.bling.patterns.CubeInSignaller;
import org.usfirst.frc.team2706.robot.commands.bling.patterns.FunDuringAuto;
import org.usfirst.frc.team2706.robot.commands.bling.patterns.IntakeSignaller;
import org.usfirst.frc.team2706.robot.commands.bling.patterns.TipWarning;
import org.usfirst.frc.team2706.robot.subsystems.Bling;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;

public class BlingController extends LoggedCommand {

    private Bling blingSystem;
    private BlingPattern currentPattern = null;
    private double startTime = 0;

    public static final int AUTONOMOUS_PERIOD = 0;
    public static final int TELEOP_WITHOUT_CLIMB = 1;
    public static final int CLIMBING_PERIOD = 2;

    boolean useMatchTime = false;

    HashMap<Integer, ArrayList<BlingPattern>> commands;

    public BlingController() {
        requires(Robot.blingSystem);

        blingSystem = Robot.blingSystem;
        commands = new HashMap<Integer, ArrayList<BlingPattern>>() {
            private static final long serialVersionUID = 1L;
            {
                put(AUTONOMOUS_PERIOD, new ArrayList<BlingPattern>());
                put(CLIMBING_PERIOD, new ArrayList<BlingPattern>());
                put(TELEOP_WITHOUT_CLIMB, new ArrayList<BlingPattern>());
            }
        };

        /*
         * Make and add the bling patterns. They need to be created in order of highest to lowest
         * priority.
         * 
         * Since patterns from different periods won't run at the same time, you only have to make
         * sure you put patterns from the same period in proper order.
         */
        Add(new ClimbWarning());
        Add(new TipWarning());
        Add(new IntakeSignaller());
        Add(new CubeInSignaller());

        // Do fun in auto
        Add(new FunDuringAuto());
        // Do blank as a last priority
        Add(new Blank());
    }

    public void initialize() {
        startTime = Timer.getFPGATimestamp();
        // If it's already teleop when we start, just subtract 15 seconds to make it seem as though
        // we're in teleop.
        if (!DriverStation.getInstance().isOperatorControl())
            startTime -= 15;

        // If the match time provided is not below 0, it's valid and we're in a game and use it.
        useMatchTime = Timer.getMatchTime() >= 0;
    }

    /**
     * Adds the inputed command to the controller's queue or stuff to run. This will be run
     * whenever the execute method of this controller is run, and if the conditions for the command
     * to be run on the robot are met, show this command on the robot.
     * 
     * @param commandToAdd The command to add to the controller's queue.
     */
    public void Add(BlingPattern commandToAdd) {
        // Add it to its proper place.
        // Loop around all of the periods it can be in.
        for (Integer i : commandToAdd.getPeriod()) {
            // Add it to the periods it can be in
            int period = i;
            commands.get(period).add(commandToAdd);
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    public void execute() {
        // Get the current period
        int currentPeriod = getCurrentPeriod();

        // Loop around all the patterns for the current period, and evaluate the conditions
        for (BlingPattern i : commands.get(currentPeriod)) {


            BlingPattern pattern = i;

            // Break at the first positive return.
            if (pattern.conditionsMet()) {

                /*
                 * Detect if the new pattern whose conditions are met was the last pattern to run.
                 * If not, end the last pattern that ran, and start the new one. Reset the pattern
                 * that we're no longer running
                 */
                if (currentPattern != null && !currentPattern.equals(pattern)) {
                    currentPattern.end();
                    pattern.initialize();
                }
                currentPattern = pattern;
                break;
            }
        }

        // Now that we've selected the pattern, run it.
        runCurrentPattern();
    }

    /**
     * Called when the command is ended
     */
    @Override
    public void end() {
        // Just clear the strip at the end.
        blingSystem.clearStrip();
        if (currentPattern != null)
            currentPattern.end();
        currentPattern = null;
    }

    private int getCurrentPeriod() {

        if (DriverStation.getInstance().isAutonomous())
            return AUTONOMOUS_PERIOD;
        // If we're using match time, use match time. Otherwise, use the other time.
        double timeSinceStart = (useMatchTime) ? Timer.getMatchTime()
                        : Timer.getFPGATimestamp() - startTime;

        if (timeSinceStart <= 105)
            return TELEOP_WITHOUT_CLIMB;
        else
            return CLIMBING_PERIOD;

    }

    /**
     * Displays the current command in the bling subsystem
     */
    private void runCurrentPattern() {
        if (currentPattern != null)
            blingSystem.Display(currentPattern);
    }
}
