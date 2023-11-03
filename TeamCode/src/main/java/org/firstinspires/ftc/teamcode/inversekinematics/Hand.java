package org.firstinspires.ftc.teamcode.inversekinematics;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import TrcCommonLib.trclib.TrcPurePursuitDrive;

@Config
public class Hand extends LinearOpMode {
    Interpolation interpX, interpZ;
    Servo servoHandRight, serverHandLeft;
    Servo servoClaw;
    public static int xTarget = 50, zTarget = 100;
    public static double upperLength = 50; // hand
    public static double lowerLength = 100; // claw

    double currentMillis, previousMillis;

    float x = xTarget, z = zTarget;

    double shoulderAngle2a, shoulderAngle2aDegrees, shoulderAngle2, shoulderAngle2Degrees, shoulderPos2, z2, shoulderAngle1a;
    double shoulderAngle1, elbowAngle, shoulderAngleDegrees, elbowAngleDegrees, elbowPos, shoulderPos;

    @Override
    public void runOpMode() throws InterruptedException {
        ElapsedTime opmodeRunTime = new ElapsedTime();

        servoHandRight = hardwareMap.get(Servo.class, "sHR");
        serverHandLeft = hardwareMap.get(Servo.class, "sHL");
        servoClaw = hardwareMap.get(Servo.class, "sC");

        waitForStart();

        while (opModeIsActive()) {
            currentMillis = opmodeRunTime.milliseconds();

            if (currentMillis - previousMillis >= 10) {  // start timed loop
                previousMillis = currentMillis;


                // start interpolation - should be like motion profiling
                //z = interpZ.go(zTarget, 1000);
                //x = interpX.go(xTarget, 1000);

                // *** Inverse Kinematics ***
                // calculate modification to shoulder angle and arm length

                shoulderAngle2a = Math.atan(z / x);
                shoulderAngle2aDegrees = Math.toDegrees(shoulderAngle2a);    // degrees
                shoulderAngle2 = shoulderAngle2a - Math.toRadians(45);  // take away the default 45' offset (in radians)
                shoulderAngle2Degrees = Math.toDegrees(shoulderAngle2);    // degrees
                //shoulderPos2 = Range.scale(shoulderAngle2Degrees, -360, 360, -1, 1);

                z2 = x / Math.cos(shoulderAngle2a);     // calc new arm length to feed to the next bit of code below

                // ****************

                // calculate arm length based on upper/lower length and elbow and shoulder angle
                shoulderAngle1a = (Math.pow(upperLength, 2) + Math.pow(z2, 2) - Math.pow(lowerLength, 2)) / (2 * upperLength * z2);
                shoulderAngle1 = Math.acos(shoulderAngle1a);     // radians
                elbowAngle = Math.PI - (shoulderAngle1 * 2);       // radians

                // calc degrees from angles
                shoulderAngleDegrees = Math.toDegrees(shoulderAngle1);    // degrees
                elbowAngleDegrees = Math.toDegrees(elbowAngle);              // degrees

                // calc milliseconds PWM to drive the servo.
                //shoulderPos = Range.scale(shoulderAngleDegrees, -360, 360, -270, 270);
                //elbowPos = Range.scale(elbowAngleDegrees, -360, 360, -270, 270);

                // *** end of Inverse Kinematics ***

                shoulderPos -= shoulderPos2;
                shoulderPos = Math.abs(shoulderPos) / 202.5;

                elbowPos = Math.abs(elbowPos) / 202.5;

                // write to servos, remove 45' and 90' offsets from arm default position
                servoHandRight.setPosition(shoulderPos);    // hand
                serverHandLeft.setPosition(shoulderPos);    // hand

                servoClaw.setPosition(elbowPos);    // claw

                telemetry.addLine("left hand: " + serverHandLeft.getPosition() + " right hand: " + servoHandRight.getPosition());
                telemetry.addLine("claw: " + servoClaw.getPosition());
                telemetry.addLine("x: " + x + " z: " + z);

                telemetry.update();
            }
        }
    }
}
