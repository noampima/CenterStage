package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {

    private DcMotor _intakeMotor;

    //Constructor
    public Intake(HardwareMap hardwareMap)
    {
        this._intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        // reset encoder
        this._intakeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // if power zero so brake
        this._intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // use  PID
        this._intakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}
