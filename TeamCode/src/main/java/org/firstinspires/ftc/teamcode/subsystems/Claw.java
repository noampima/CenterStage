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

    private final RobotHardware robot;

    public enum ClawState
    {
        CLOSED,
        INTERMEDIATE,
        OPEN
    }

    public ClawState leftClaw = ClawState.CLOSED;
    public ClawState rightClaw = ClawState.CLOSED;

    public static double open = 0.1, intermediate = 0.05, close = 0;

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
        robot.clawLeftServo.setPosition(open);
        robot.clawRightServo.setPosition(open);
    }

    public void closeClaw()
    {
        robot.clawLeftServo.setPosition(close);
        robot.clawRightServo.setPosition(close);
    }

    private double getClawStatePosition(ClawState state, ClawSide side)
    {
        switch (side)
        {
            case LEFT:
            case RIGHT:
                switch (state) {
                    case CLOSED:
                        return close;
                    case INTERMEDIATE:
                        return intermediate;
                    case OPEN:
                        return open;
                    default:
                        return 0.0;
                }
//            case RIGHT:
//                switch (state) {
//                    case CLOSED:
//                        return close;
//                    case INTERMEDIATE:
//                        return intermediate;
//                    case OPEN:
//                        return open;
//                    default:
//                        return 0.0;
//                }
            default:
                return 0.0;
        }
    }

    void checkAndClose(DigitalChannel sensor, ClawSide side)
    {
        if(sensor.getState())
        {
            new SequentialCommandGroup(
                    new WaitCommand((long)delay),
                    new ClawCommand(this, Claw.ClawState.OPEN, side)).schedule();
        }
        else
        {
            new SequentialCommandGroup(
                    new WaitCommand((long) delay),
                    new ClawCommand(this, Claw.ClawState.CLOSED, side)).schedule();
        }
    }


}
