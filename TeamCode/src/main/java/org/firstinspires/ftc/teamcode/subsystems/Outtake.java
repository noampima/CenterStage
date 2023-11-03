package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

// TOOD: PRETTY MUCH THE HAND
public class Outtake {

    private Servo _pixelPlacerServo;

    //Constructor
    public Outtake(HardwareMap hardwareMap)
    {
        this._pixelPlacerServo = hardwareMap.get(Servo.class, "pixelPlacerServo");
    }
}
