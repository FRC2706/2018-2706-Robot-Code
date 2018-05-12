package org.usfirst.frc.team2706.robot.commands.autonomous.experimential.curvedrive;

import java.text.DecimalFormat;

/**
 * Stores and allows you to print the equation if neccesary, more useful for the console version but
 * still valuable
 */
public class CubicEquation {

    // The four coefficients that need to be saved
    public double a, b, c, d;

    /**
     * Creates a cubic equation with four coefficients:
     * {@code x = ay^3 + by^2 + cy + d}
     * 
     * @param a The A term
     * @param b The B term
     * @param c The C term
     * @param d The D term
     */
    public CubicEquation(double a, double b, double c, double d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    /**
     * Prints the equation so it can be copied into desmos easily.
     */
    public String toString() {
        DecimalFormat df =
                        new DecimalFormat("############.#########################################");
        String aa = df.format(a);
        String bb = df.format(b);
        String cc = df.format(c);
        String dd = df.format(d);
        return "x = " + aa + "y^3 + " + bb + "y^2" + cc + "y^2" + dd;
    }
}
