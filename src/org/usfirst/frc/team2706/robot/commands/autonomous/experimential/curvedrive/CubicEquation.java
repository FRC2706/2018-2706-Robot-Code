package org.usfirst.frc.team2706.robot.commands.autonomous.experimential.curvedrive;

import java.text.DecimalFormat;

public class CubicEquation {
    public double a,b,c,d;

    public CubicEquation(double a, double b, double c, double d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }
    public String toString() {
        DecimalFormat df = new DecimalFormat("############.#########################################");  
        String aa = df.format(a);
        String bb = df.format(b);
        return "x = " + aa + "y^3 + " + bb + "y^2";
    }
}
