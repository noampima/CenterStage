package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Claw {

    Servo left, right;

    public static double open = 0.5;
    public static double close = 0;

    public Claw(HardwareMap hardwareMap)
    {
        left = hardwareMap.get(Servo.class, "sCL");
        right = hardwareMap.get(Servo.class, "sCR");
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

}
