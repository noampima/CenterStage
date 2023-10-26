package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.util.GamepadHelper;
import org.firstinspires.ftc.teamcode.util.SampleMecanumDrive;

public class DrivetrainController {

    private DcMotor _mfr;
    private DcMotor _mbr;
    private DcMotor _mfl;
    private DcMotor _mbl;

    private MecanumDrive _drivetrain;
    private Telemetry _telemetry;

    private GamepadHelper _cGamepad1, _cGamepad2;

    //Constructor
    public DrivetrainController(HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2, Telemetry telemetry, boolean redAlliance)
    {
        this._mfr = hardwareMap.get(DcMotor.class, "mfr");
        this._mbr = hardwareMap.get(DcMotor.class, "mbr");
        this._mfl = hardwareMap.get(DcMotor.class, "mfl");
        this._mbl = hardwareMap.get(DcMotor.class, "mbl");

        // reverse left motors
        this._mbl.setDirection(DcMotor.Direction.REVERSE);
        this._mfl.setDirection(DcMotor.Direction.REVERSE);

        //brake
        this._mfr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this._mbr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this._mfl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this._mbl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // gamepad helper to see if pressed button once
        this._cGamepad1 = new GamepadHelper(gamepad1);
        this._cGamepad2 = new GamepadHelper(gamepad2);

        this._telemetry = telemetry;

        //getting imu from roadRunner
        this._drivetrain = new MecanumDrive(hardwareMap, new Pose2d(0,0,0));
        this._drivetrain.(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // check if we are blue/red alliance and set zero angle - For centric drive
        if(!redAlliance)
        {
            this._drivetrain.setPoseEstimate(new Pose2d(0,0,-(Math.PI + Math.PI/2)));
        }
        else if(redAlliance)
        {
            this._drivetrain.setPoseEstimate(new Pose2d(0,0,(Math.PI + Math.PI/2)));
        }
    }
}
