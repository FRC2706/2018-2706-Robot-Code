package org.usfirst.frc.team2706.robot.subsystems;

import org.usfirst.frc.team2706.robot.commands.bling.BlingController;
import org.usfirst.frc.team2706.robot.commands.bling.patterns.BlingPattern;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.networktables.*;

public class Bling extends Subsystem{    
    // All of the pattern numbers
    public static final String COLOUR_WIPE = "colorWipe", THEATRE_CHASE = "theaterChase", SOLID = "solid", BLINK = "blink",
                    RAINBOW = "rainbow", THEATRE_CHASE_RAINBOW = "theaterChaseRainbow", RAINBOW_CYCLE = "rainbowCycle",
                    CLEAR = "clear";
    
    /**
     * A nice purple merge RGB colour.
     */
    public static final int[] MERGERGB = {114, 14, 172};
    
    public static final int GOODBRIGHTNESS  = 128;
    
    /**
     * The networktables key for the bling table.
     */
    private static final String NTKEY = "blingTable";
    
    
    // Networktables entries for bling
    NetworkTableEntry waitMSNT, redNT, greenNT, blueNT, repeatNT, brightnessNT, commandNT;
    
    private NetworkTable blingTable;
    
    
    private Command defaultCommand;
    
    // Used to make sure we don't run the same command twice in a row.
    private int[] lastRGBArray = new int[3];
    private int lastRepeat = -1;
    private int lastWaitMs = -1;
    private int lastLEDBrightness = -1;
    private String lastCommand = "";
   
    /**
     * Class used as the basic part of handling bling commands. 
     */
    public Bling() {        
        blingTable = NetworkTableInstance.getDefault().getTable(NTKEY);
        
        // Declare all the necessary variables to work with for networktables setting
        waitMSNT = blingTable.getEntry("wait_ms");
        redNT = blingTable.getEntry("red");
        greenNT = blingTable.getEntry("green");
        blueNT = blingTable.getEntry("blue");
        repeatNT = blingTable.getEntry("repeat");
        brightnessNT = blingTable.getEntry("LED_BRIGHTNESS");
        commandNT = blingTable.getEntry("command");
    }
    
    @Override
    public Command getDefaultCommand(){
        if (defaultCommand == null) {
            defaultCommand = new BlingController();
        }
        return defaultCommand;
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(getDefaultCommand());
    }
    
    /**
     * The method used to display bling patterns.
     * @param patternToShow The bling pattern object whose pattern to show.
     */
    public void Display(BlingPattern patternToShow) {              
        
        boolean isSameCommand = isSameAsLastCommandRun(patternToShow);
        
        // Don't spam the pi with the same command, so determine if this is the same as the last command
        if (isSameCommand) return;
        
        lastRepeat = patternToShow.getRepeatCount();
        lastLEDBrightness = patternToShow.getBrightness();
        lastWaitMs = patternToShow.getWaitMS();
        lastCommand = patternToShow.getCommand();
        lastRGBArray = patternToShow.getRGB();
        
        // Display pattern
        Display(patternToShow.getBrightness(), patternToShow.getWaitMS(), patternToShow.getRGB(), patternToShow.getCommand(), patternToShow.getRepeatCount());
        
        // Run the command
        patternToShow.runCommand();
        
    }
    
    /**
     * Displays the given type of LED pattern on the LED strip.
     * @param LEDbrightness The brightness, an integer between 0 and 255, 255 being full brightness
     * @param waitMS The amount of miliseconds to delay between each pattern 
     * @param rgb The RGB colour code (red, green, blue) to display
     * @param command The type of pattern to display. Use one of the Bling class constants for patterns.
     * @param repeatCount The number of times to repeat the pattern
     */
    public void Display(int LEDbrightness, int waitMS, int[] rgb, String command, int repeatCount) {
        // Send pattern parameters to Networktables. The command one must be sent last because that's the cue for the pi to display the pattern
        redNT.setNumber(rgb[0]);
        greenNT.setNumber(rgb[1]);
        blueNT.setNumber(rgb[2]);
        repeatNT.setNumber(repeatCount);
        waitMSNT.setNumber(waitMS);
        brightnessNT.setNumber(LEDbrightness);
        commandNT.setString(command);
    }
    
    private boolean isSameAsLastCommandRun(BlingPattern patternToShow) {
        return patternToShow.getBrightness() == lastLEDBrightness && 
                        patternToShow.getRepeatCount() == lastRepeat && patternToShow.getCommand().equals(lastCommand)
                        && patternToShow.getRGB().equals(lastRGBArray) && lastWaitMs == patternToShow.getWaitMS();
    }

    /**
     * Clears the LED strip
     */
    public void clearStrip() {
        int[] colour = new int[] {0, 0, 0};
        Display(0, 0, colour, CLEAR, 0);
    }
    
}
