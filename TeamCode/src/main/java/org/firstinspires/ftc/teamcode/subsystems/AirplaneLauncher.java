package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

//TODO: MECHANICS

public class AirplaneLauncher {
    private Servo _launcherServo;

    //Constructor
    public AirplaneLauncher(HardwareMap hardwareMap)
    {
        this._launcherServo = hardwareMap.get(Servo.class, "launcherServo");

    }
}
