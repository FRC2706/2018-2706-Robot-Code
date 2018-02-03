package org.usfirst.frc.team2706.robot.subsystems;

import org.usfirst.frc.team2706.robot.commands.bling.patterns.BlingPattern;
import org.usfirst.frc.team2706.robot.commands.bling.BlingController;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Bling extends Subsystem{
    
    // Used to make sure we don't send the same pattern twice in a row
    String lastPattern = "";

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
    public static final int COLOUR_WIPE = 1;
    public static final int COLOUR_WIPE_WITH_BLANK = 2;
    public static final int THEATRE_CHASE = 3;
    public static final int RAINBOW = 4;
    public static final int THEATRE_CHASE_RAINBOW = 5;
    public static final int COLOUR_BAR = 6;
    public static final int COLOUR_BAR_FLASH = 7;
    public static final int BOUNCE = 8;
    public static final int BOUNCE_WIPE = 9;
    public static final int MULTI_BOUNCE = 10;
    public static final int MULTI_BOUNCE_WIPE = 11;
    public static final int MULTI_COLOUR_WIPE = 12;
    
    //  Too far if we're above this.
    public static final double tooFarDist = 20;
    
    // Too close if we're lower than this
    public static final double tooCloseDist = 11;
    
    // Don't show the pattern at all if the distance is over this.
    public static final double irrelevanceDist = 40;
    
    
    private Command defaultCommand;
   
    /**
     * Class used as the basic part of handling bling commands. 
     */
    public Bling() {
        
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
        String patternString = patternToShow.getPattern();        
        if (patternString == null || patternString.equals(lastPattern)) return;
        
        lastPattern = patternString;
        // TODO  Send the pattern somehow.
        
    }
    
    /**
     * Used to properly format patterns
     * @return The properly formatted string pattern code.
     * @param colour The colour of the pattern in DEC
     * @param patternType The pattern number, one of the following: <p>
     * 1 : Colour wipe <p>
     * 2 : Colour wipe with blank period <p>
     * 3 : Theatre chase<p>
     * 4 : Rainbow<p>
     * 5 : Theatre chase rainbow<p>
     * 6 : Color bar<p>
     * 7 : Color bar flash<p>
     * 8 : Bounce<p>
     * 9 : Bounce wipe<p>
     * 10: Multi bounce<p>
     * 11: Multi bouce wipe<p>
     * 12: Multi colour wipe<p>
     * @param timeDelay The amount of delay between segments
     * @param brightness 1 to 100 percentage brightness
     */
    public static String formatPattern(long colour, int patternType, 
                    double timeDelay, int brightness) {
        return String.format("F%sC%sB%sD%sR1000E%sZ", patternType, colour, brightness, timeDelay, patternType);
    }
    
}
