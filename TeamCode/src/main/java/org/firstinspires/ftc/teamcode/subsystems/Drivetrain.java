package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.RobotHardware;
import org.firstinspires.ftc.teamcode.util.BetterGamepad;
import org.firstinspires.ftc.teamcode.util.values.Globals;
import org.firstinspires.ftc.teamcode.util.wrappers.BetterSubsystem;

public class Drivetrain extends BetterSubsystem {

    boolean redAlliance;

    private BetterGamepad _cGamepad1, _cGamepad2;

    private final RobotHardware robot;

    double frontLeftPower = 0, backLeftPower = 0, frontRightPower = 0, backRightPower = 0;

    //Constructor
    public Drivetrain(Gamepad gamepad1, Gamepad gamepad2, boolean redAlliance)
    {
        this.robot = RobotHardware.getInstance();

        // gamepad helper to see if pressed button once
        this._cGamepad1 = new BetterGamepad(gamepad1);
        this._cGamepad2 = new BetterGamepad(gamepad2);

        this.redAlliance = redAlliance;

        resetAngle();
    }

    @Override
    public void periodic() {
        double heading = robot.getAngle();

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
        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), Globals.MAX_POWER);
        frontLeftPower = (rotY + rotX + rx) / denominator;
        backLeftPower = (rotY - rotX + rx) / denominator;
        frontRightPower = (rotY - rotX - rx) / denominator;
        backRightPower = (rotY + rotX - rx) / denominator;
    }

    @Override
    public void read() {

    }

    @Override
    public void write() {
        robot.dtFrontLeftMotor.setPower(frontLeftPower);
        robot.dtBackLeftMotor.setPower(backLeftPower);
        robot.dtFrontLeftMotor.setPower(frontRightPower);
        robot.dtFrontRightMotor.setPower(backRightPower);
    }

    @Override
    public void reset() {
    }

    void resetAngle()
    {
        // check if we are blue/red alliance and set zero angle - For centric drive
        if(!redAlliance)
        {
            robot.setImuOffset(-(Math.PI + Math.PI/2));
        }
        else if(redAlliance)
        {
            robot.setImuOffset(Math.PI + Math.PI/2);
        }
    }

}
