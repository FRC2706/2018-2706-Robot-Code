package org.usfirst.frc.team2706.robot.subsystems;

import org.usfirst.frc.team2706.robot.commands.bling.BlingController;
import org.usfirst.frc.team2706.robot.commands.bling.patterns.BlingPattern;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Bling extends Subsystem{
    // Bunch of colour presets.
    public static final long WHITE = 16777215;
    public static final long YELLOW = 16775680;
    public static final long GREEN = 524288;
    public static final long PURPLE = 8388736;
    public static final long ORANGE = 16753920;
    public static final long BLUE = 255;
    public static final long VIOLET = 15631086;
    public static final long MERGE = 6160762;
    public static final long TAN = 16767411;
    public static final long PINK = 14027935;
    public static final long BLACK = 0;
    public static final long GOLD = 16766720;
    public static final long SILVER = 12632256;
    public static final long RED = 16711680;
    
    // All of the pattern numbers
    public static final String COLOUR_WIPE = "color_Wipe";
    
    //  Too far if we're above this.
    public static final double tooFarDist = 20;
    
    // Too close if we're lower than this
    public static final double tooCloseDist = 11;
    
    // Don't show the pattern at all if the distance is over this.
    public static final double irrelevanceDist = 40;
    
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
        blingTable = NetworkTable.getTable("blingTable");
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
        
        System.out.println("IsSameCommand : " + isSameCommand + " Pattern to show : " + patternToShow);
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
    @SuppressWarnings("deprecation")
    public void Display(int LEDbrightness, int waitMS, int[] rgb, String command, int repeatCount) {
        blingTable.putNumber("red", rgb[0]);
        blingTable.putNumber("green", rgb[1]);
        blingTable.putNumber("blue", rgb[2]);
        blingTable.putNumber("repeat", repeatCount);
        blingTable.putNumber("wait_ms", waitMS);
        blingTable.putNumber("LED_BRIGHTNESS", LEDbrightness);
        blingTable.putString("command", command);
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
        Display(0, 0, colour, COLOUR_WIPE, 0);
    }
    
}
