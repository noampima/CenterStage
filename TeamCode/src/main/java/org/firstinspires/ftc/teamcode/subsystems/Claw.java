package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.RobotHardware;
import org.firstinspires.ftc.teamcode.util.ClawSide;
import org.jetbrains.annotations.NotNull;

@Config
public class Claw
{
    private final RobotHardware robot;

    public enum ClawState
    {
        CLOSED,
        INTERMEDIATE,
        OPEN
    }

    public ClawState leftClaw = ClawState.CLOSED;
    public ClawState rightClaw = ClawState.CLOSED;

    Servo left, right;

    public static double open = 0.5, intermediate = 0.5, close = 0;

    public Claw(HardwareMap hardwareMap)
    {
        this.robot = RobotHardware.getInstance();

        left = hardwareMap.get(Servo.class, "sCL");
        right = hardwareMap.get(Servo.class, "sCR");

        updateState(ClawState.CLOSED, ClawSide.BOTH);
    }

    public void updateState(@NotNull ClawState state, @NotNull ClawSide side) {
        double position = getClawStatePosition(state, side);
//        this.clawState = state;
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
        left.setPosition(open);
        right.setPosition(open);
    }

    public void closeClaw()
    {
        left.setPosition(close);
        right.setPosition(close);
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

}
