package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Elevator {

    private DcMotor _mE;

    //Constructor
    public Elevator(HardwareMap hardwareMap)
    {
        this._mE = hardwareMap.get(DcMotor.class, "mE");
        // reset encoder
        this._mE.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // if power zero so brake
        this._mE.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // use  PID
        this._mE.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

}
