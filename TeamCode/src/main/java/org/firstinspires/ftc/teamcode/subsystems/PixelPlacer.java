package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class PixelPlacer {

    private Servo _pixelPlacerServo;

    //Constructor
    public PixelPlacer(HardwareMap hardwareMap)
    {
        this._pixelPlacerServo = hardwareMap.get(Servo.class, "pixelPlacerServo");
    }
}
