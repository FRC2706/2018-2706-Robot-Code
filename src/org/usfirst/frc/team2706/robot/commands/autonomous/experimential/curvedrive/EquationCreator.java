package org.usfirst.frc.team2706.robot.commands.autonomous.experimential.curvedrive;

import java.util.LinkedHashMap;

/**
 * Actually does the math to create the equation from two points and a tangent. Warning: Complicated
 */
public class EquationCreator {

    /**
     * Creates the equation
     * 
     * @param x amount right (any unit)
     * @param y amount up(any unit)
     * @param theta ending angle(degrees)
     * @param rightInvert instead of making the values negative and maybe breaking the graph, just
     *        invert the graph
     * @return the cubic equation that it creates
     */
    public static CubicEquation MakeCubicEquation(double x, double y, double theta,
                    boolean rightInvert) {

        // Makes a new coordinate points and rotates it into the bottom right quadrant
        Coordinates c = new Coordinates(x, y);
        c.rotateRight();

        // Calculates the slope of your triangle at the angle you want, in the bottom right quadrant
        double tangent = Math.tan(Math.toRadians(360 - theta));

        // Instead of making another equation class for each I just store each polynomial in its own
        // double
        double eq1a = Math.pow(c.x, 3);
        double eq1b = Math.pow(c.x, 2);
        double eq1y = c.y;

        // The derivative equation that will be compared to the first equation, basically y`(x)
        double eq2a = Math.pow(c.x, 2) * 3;
        double eq2b = c.x * 2;

        // Figures out a similar b value to each equation so they can be subtracted away
        double multiplier = eq1b / eq2b;

        // Multiplies all of the values in equation b cause algebra
        eq2a *= multiplier;
        eq2b *= multiplier;
        double mTan = tangent * multiplier;

        // Figures out your final a and b value that is the coefficient
        double a = (eq1y - mTan) / (eq1a - eq2a);
        double b = ((eq1y - (eq1a * a)) / eq1b);
        if (rightInvert) {
            return new CubicEquation(a, b, 0, 0);
        } else {
            // Creates a new cubicequation with the values to be returned.
            return new CubicEquation(-a, -b, 0, 0);
        }

    }

    /**
     * Creates the tangents for the equation, effectively how the robot interpolates the curve
     * @param distanceOffsetDegrees The distance between each tangent
     * @param topLimit The furthest you want the tangents to generate
     * @param eq The equation of the curve
     * @return The tangents
     */
    public static LinkedHashMap<Double, Double> createTangents(double distanceOffsetDegrees, double topLimit, CubicEquation eq) {
        LinkedHashMap<Double, Double> followTangents;
        followTangents = new LinkedHashMap<Double, Double>();
        followTangents.put(0.0, 0.0);
        Double lastEntryKey = 0.0;
        double currentParse = 0.0;
        for (; currentParse < topLimit; currentParse += 0.01) {
            double tangent = (3 * eq.a * Math.pow(currentParse, 2)) + (2 * eq.b * currentParse);
            tangent = Math.toDegrees(Math.atan(tangent));
            if (Math.abs(Math.sqrt(Math.pow((Math.pow(eq.a * lastEntryKey, 3) + 
                            Math.pow(eq.b * lastEntryKey, 2)) - (Math.pow(eq.a * currentParse, 3) + 
                                    Math.pow(eq.b * currentParse, 2)), 2) + Math.pow(lastEntryKey - currentParse, 2)))
                                        > distanceOffsetDegrees) {
                followTangents.put(currentParse, tangent);
                lastEntryKey = currentParse;
            }
        }
        double tangent = (3 * eq.a * Math.pow(currentParse, 2)) + (2 * eq.b * currentParse);
        tangent = Math.toDegrees(Math.atan(tangent));
        followTangents.put(currentParse, tangent);
        lastEntryKey = currentParse;
        return followTangents;
    }
}
