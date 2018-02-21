package org.usfirst.frc.team2706.robot.commands.autonomous.experimential.curvedrive;

public class Coordinates {
    double x, y;
    public Coordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public void rotateRight() {
        double tempX = x;
        x = y;
        y = -tempX;
    }
}