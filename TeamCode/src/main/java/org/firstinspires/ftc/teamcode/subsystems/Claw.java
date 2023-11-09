package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.RobotHardware;
import org.firstinspires.ftc.teamcode.commands.ClawCommand;
import org.firstinspires.ftc.teamcode.commands.ElevatorCommand;
import org.firstinspires.ftc.teamcode.util.values.ClawSide;
import org.firstinspires.ftc.teamcode.util.values.Globals;
import org.firstinspires.ftc.teamcode.util.wrappers.BetterSubsystem;
import org.jetbrains.annotations.NotNull;

@Config
public class Claw extends BetterSubsystem
{
    public static boolean auto = true;
    public static double delay = 135;
    private boolean useSensor; // Use this variable to specify whether to use the right sensor

    private final RobotHardware robot;

    public enum ClawState
    {
        CLOSED,
        INTERMEDIATE,
        OPEN
    }

    public ClawState leftClaw = ClawState.CLOSED;
    public ClawState rightClaw = ClawState.CLOSED;

    public static double openLeft = 0.1, closeLeft = 0;
    public static double openRight = 0.1, closeRight = 0;

    public Claw()
    {
        this.robot = RobotHardware.getInstance();

        updateState(ClawState.OPEN, ClawSide.BOTH);
    }

    @Override
    public void periodic() {
        if(auto)
        {
            checkAndClose(robot.breambeamRight, ClawSide.RIGHT);
            checkAndClose(robot.breambeamLeft, ClawSide.LEFT);
        }
    }

    @Override
    public void read() {

    }

    @Override
    public void write() {

    }

    @Override
    public void reset() {

    }

    public void updateState(@NotNull ClawState state, @NotNull ClawSide side) {
        double position = getClawStatePosition(state, side);

        switch(side) {
            case LEFT:
                robot.clawLeftServo.setPosition(position);
                this.leftClaw = state;
                break;
            case RIGHT:
                robot.clawRightServo.setPosition(position);
                this.rightClaw = state;
                break;
            case BOTH:
                position = getClawStatePosition(state, ClawSide.LEFT);
                robot.clawLeftServo.setPosition(position);
                this.leftClaw = state;
                position = getClawStatePosition(state, ClawSide.RIGHT);
                robot.clawRightServo.setPosition(position);
                this.rightClaw = state;
                break;
        }
    }

    public void openClaw()
    {
        robot.clawLeftServo.setPosition(openLeft);
        robot.clawRightServo.setPosition(openRight);
    }

    public void closeClaw()
    {
        robot.clawLeftServo.setPosition(closeLeft);
        robot.clawRightServo.setPosition(closeRight);
    }

    private double getClawStatePosition(ClawState state, ClawSide side)
    {
        switch (side)
        {
            case LEFT:
                switch (state) {
                    case CLOSED:
                        return closeLeft;
                    case OPEN:
                        return openLeft;
                    default:
                        return 0.0;
                }
            case RIGHT:
                switch (state) {
                    case CLOSED:
                        return closeRight;
                    case OPEN:
                        return openRight;
                    default:
                        return 0.0;
                }
            default:
                return 0.0;
        }
    }

    void checkAndClose(DigitalChannel sensor, ClawSide side)
    {
        if(sensor.getState() && useSensor)
        {
            new SequentialCommandGroup(
                    new WaitCommand((long)delay),
                    new ClawCommand(this, Claw.ClawState.OPEN, side)).schedule();
        }
        else if(useSensor)
        {
            new SequentialCommandGroup(
                    new WaitCommand((long) delay),
                    new ClawCommand(this, Claw.ClawState.CLOSED, side)).schedule();
        }
    }

    public boolean isUseSensor() {
        return useSensor;
    }

    public void setUseSensor(boolean useSensor) {
        this.useSensor = useSensor;
    }
}
