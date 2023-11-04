package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.RobotHardware;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.util.BetterGamepad;
import org.firstinspires.ftc.teamcode.util.Globals;

public class Drivetrain {

    boolean redAlliance;
    HardwareMap hardwareMap;

    private BetterGamepad _cGamepad1, _cGamepad2;

    RobotHardware _robot;

    //Constructor
    public Drivetrain(RobotHardware robot, Gamepad gamepad1, Gamepad gamepad2, boolean redAlliance)
    {
        this._robot = robot;

        // gamepad helper to see if pressed button once
        this._cGamepad1 = new BetterGamepad(gamepad1);
        this._cGamepad2 = new BetterGamepad(gamepad2);

        this.redAlliance = redAlliance;

        resetAngle();
    }

    public void update()
    {
        double heading = _robot.getAngle();

        double y = -_cGamepad1.left_stick_y; // Remember, Y stick value is reversed
        double x = _cGamepad1.left_stick_x;
        double rx = _cGamepad1.right_stick_x;

        // This button choice was made so that it is hard to hit on accident,
        // it can be freely changed based on preference.
        // The equivalent button is start on Xbox-style controllers.
        if (_cGamepad1.XOnce()) {
            resetAngle();
        }

        // Rotate the movement direction counter to the bot's rotation
        double rotX = x * Math.cos(-heading) - y * Math.sin(-heading);
        double rotY = x * Math.sin(-heading) + y * Math.cos(-heading);

        rotX = rotX * 1.1;  // Counteract imperfect strafing

        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio,
        // but only if at least one is out of the range [-1, 1]
        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), Globals.maxPower);
        double frontLeftPower = (rotY + rotX + rx) / denominator;
        double backLeftPower = (rotY - rotX + rx) / denominator;
        double frontRightPower = (rotY - rotX - rx) / denominator;
        double backRightPower = (rotY + rotX - rx) / denominator;

        _robot.dtFrontLeftMotor.setPower(frontLeftPower);
        _robot.dtBackLeftMotor.setPower(backLeftPower);
        _robot.dtFrontLeftMotor.setPower(frontRightPower);
        _robot.dtFrontRightMotor.setPower(backRightPower);
    }

    void resetAngle()
    {
        // check if we are blue/red alliance and set zero angle - For centric drive
        if(!redAlliance)
        {
            _robot.setImuOffset(-(Math.PI + Math.PI/2));
        }
        else if(redAlliance)
        {
            _robot.setImuOffset(Math.PI + Math.PI/2);
        }
    }
}
