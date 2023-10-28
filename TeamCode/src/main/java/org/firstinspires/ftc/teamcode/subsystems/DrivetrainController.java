package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.util.GamepadHelper;

public class DrivetrainController {

    boolean redAlliance;
    HardwareMap hardwareMap;

    private MecanumDrive _drivetrain;
    private Telemetry _telemetry;

    private GamepadHelper _cGamepad1, _cGamepad2;

    //Constructor
    public DrivetrainController(HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2, Telemetry telemetry, boolean redAlliance)
    {

        // gamepad helper to see if pressed button once
        this._cGamepad1 = new GamepadHelper(gamepad1);
        this._cGamepad2 = new GamepadHelper(gamepad2);

        this._telemetry = telemetry;
        this.redAlliance = redAlliance;
        this.hardwareMap = hardwareMap;

        resetAngle();
    }

    public void update()
    {
        this._drivetrain.updatePoseEstimate();

        double heading = this._drivetrain.pose.heading.log(); // check if radians

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

        this._drivetrain.setDrivePowers(new PoseVelocity2d(new Vector2d(rotX, rotY), rx));
    }

    void resetAngle()
    {
        // check if we are blue/red alliance and set zero angle - For centric drive
        if(!redAlliance)
        {
            this._drivetrain = new MecanumDrive(hardwareMap, new Pose2d(0,0,-(Math.PI + Math.PI/2)));
        }
        else if(redAlliance)
        {
            this._drivetrain = new MecanumDrive(hardwareMap, new Pose2d(0,0,(Math.PI + Math.PI/2)));
        }
    }
}
