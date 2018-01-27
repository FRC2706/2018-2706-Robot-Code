package org.usfirst.frc.team2706.robot.controls;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class TalonPID {

    private final TalonSRX left, right;
    
    private double P, I, D, FF;
    private double error;
    private double setpoint;
    private double minOutput, maxOutput;
    private double leftDPP, rightDPP;
    private double leftStart, rightStart;
    
    public TalonPID(TalonSRX masterLeft, TalonSRX slaveLeft, TalonSRX masterRight, TalonSRX slaveRight) {
        this(masterLeft, masterRight);
        
        slaveLeft.follow(masterLeft);
        slaveRight.follow(masterRight);
    }
    
    public TalonPID(TalonSRX left, TalonSRX right) {
        this.left = left;
        this.right = right;
    }
    
    public void setPID(double P, double I, double D) {
        setPID(P, I, D, 0);
    }
    
    public void setPID(double P, double I, double D, double FF) {
        this.P = P;
        this.I = I;
        this.D = D;
        this.FF = FF;
    }
    
    public double getError() {
        return error;
    }
    
    public void setError(double error) {
        this.error = error;
    }
    
    public double getSetpoint() {
        return setpoint;
    }
    
    public void setSetpoint(double setpoint) {
        this.setpoint = setpoint;;
    }
    
    public void setOutputRange(double min, double max) {
        this.minOutput = min;
        this.maxOutput = max;
    }
    
    public double getMinOutput() {
        return minOutput;
    }
    
    public double getMaxOutput() {
        return maxOutput;
    }
    
    public double getLeftDPP() {
        return leftDPP;
    }
    
    public void setLeftDPP(double dpp) {
        this.leftDPP = dpp;
    }
    
    public double getRightDPP() {
        return rightDPP;
    }
    
    public void setRightDPP(double dpp) {
        this.leftDPP = dpp;
    }
    
    public double getLeftStart() {
        return leftStart;
    }
    
    public void setLeftStart(double start) {
        this.leftStart = start;
    }
    
    public double getRightStart() {
        return leftStart;
    }
    
    public void setRightStart(double start) {
        this.leftStart = start;
    }
    
    public void enable() {
        // Set PID values
        left.config_kP(0, P, 0);
        left.config_kI(0, I, 0);
        left.config_kD(0, D, 0);
        left.config_kF(0, FF, 0);
        
        right.config_kP(0, P, 0);
        right.config_kI(0, I, 0);
        right.config_kD(0, D, 0);
        right.config_kF(0, FF, 0);
        
        // "Quickly" set the min and max of each motor
        left.configNominalOutputForward(0, 0);
        left.configNominalOutputReverse(0, 0);
        left.configPeakOutputForward(maxOutput, 0);
        left.configPeakOutputReverse(minOutput, 0);
        right.configNominalOutputForward(0, 0);
        right.configNominalOutputReverse(0, 0);
        right.configPeakOutputForward(maxOutput, 0);
        right.configPeakOutputReverse(minOutput, 0);
        
        left.configAllowableClosedloopError(0, (int) (error / leftDPP), 0);
        right.configAllowableClosedloopError(0, (int) (error / rightDPP), 0);
        
        left.set(ControlMode.Position, (setpoint + leftStart) / leftDPP);
        right.set(ControlMode.Position, (setpoint + rightStart) / rightStart);
    }
    
    public void disable() {
        // Set PID values
        left.config_kP(0, 0, 0);
        left.config_kI(0, 0, 0);
        left.config_kD(0, 0, 0);
        left.config_kF(0, 0, 0);
        
        right.config_kP(0, 0, 0);
        right.config_kI(0, 0, 0);
        right.config_kD(0, 0, 0);
        right.config_kF(0, 0, 0);
        
        // "Quickly" set the min and max of each motor
        left.configNominalOutputForward(0, 0);
        left.configNominalOutputReverse(0, 0);
        left.configPeakOutputForward(1, 0);
        left.configPeakOutputReverse(-1, 0);
        right.configNominalOutputForward(0, 0);
        right.configNominalOutputReverse(0, 0);
        right.configPeakOutputForward(1, 0);
        right.configPeakOutputReverse(-1, 0);
        
        left.configAllowableClosedloopError(0, 0, 0);
        right.configAllowableClosedloopError(0, 0, 0);
        
        left.set(ControlMode.PercentOutput, 0);
        right.set(ControlMode.PercentOutput, 0);
    }
    
    public boolean isOnTarget() {
        return left.getClosedLoopError(0) < error && right.getClosedLoopError(0) < error;
    }
}
