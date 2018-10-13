package org.usfirst.frc.team2706.robot.commands.autonomous.experimential.curvedrive;

/**
 * Holds an X and Y value
 */
public class Coordinates {
    private double x, y;

    /**
     * Create a coordinate set
     * 
     * @param x The X component of the coordinate
     * @param y The Y component of the coordinate
     */
    public Coordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the X component of the coordinates
     * 
     * @return The X component
     */
    public double getX() {
        return x;
    }

    /**
     * Sets the X component of the coordinates
     * 
     * @param x The X component
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Gets the Y component of the coordinates
     * 
     * @return The Y component
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the Y component of the coordinates
     * 
     * @param y The Y component
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Rotates the coordinates 90 degrees around (0, 0)
     */
    public void rotateRight() {
        double tempX = x;
        x = y;
        y = -tempX;
    }
}
