package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.util.wrappers.BetterSubsystem;

public class Intake extends BetterSubsystem
{

    private DcMotor _intakeMotor;

    //Constructor
    public Intake(HardwareMap hardwareMap)
    {
        this._intakeMotor = hardwareMap.get(DcMotor.class, "mI");

        this._intakeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this._intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this._intakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void periodic() {

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
}
