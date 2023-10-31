package org.firstinspires.ftc.teamcode.inversekinematics;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@Config
public class Hand extends LinearOpMode {
    Interpolation interpX, interpZ;
    Servo servoHandRight, serverHandLeft;
    Servo servoClaw;
    public static int xDefault = 50, zDefault = 100;
    public static double upperLength = 50; // hand
    public static double lowerLength = 100; // claw

    int xTarget = xDefault, zTarget = zDefault;
    double currentMillis, previousMillis;

    float x,z;

    double shoulderAngle2a, shoulderAngle2aDegrees, shoulderAngle2, shoulderAngle2Degrees, shoulderPos2, z2, shoulderAngle1a;
    double shoulderAngle1, elbowAngle, shoulderAngleDegrees, elbowAngleDegrees, elbowPos, shoulderPos;

    @Override
    public void runOpMode() throws InterruptedException {
        ElapsedTime opmodeRunTime = new ElapsedTime();

        waitForStart();

        while (opModeIsActive()) {
            currentMillis = opmodeRunTime.milliseconds();

            if (currentMillis - previousMillis >= 10) {  // start timed loop
                previousMillis = currentMillis;


                // start interpolation

                z = interpZ.go(zTarget, 1000);
                x = interpX.go(xTarget, 1000);

                // *** Inverse Kinematics ***
                // calculate modification to shoulder angle and arm length

                shoulderAngle2a = Math.atan(z / x);
                shoulderAngle2aDegrees = shoulderAngle2a * (180 / Math.PI);    // degrees
                shoulderAngle2 = shoulderAngle2a - 0.7853908;  // take away the default 45' offset (in radians)
                shoulderAngle2Degrees = shoulderAngle2 * (180 / Math.PI);    // degrees
                shoulderPos2 = Range.scale(shoulderAngle2Degrees, -360, 360, -1, 1);

                z2 = x / Math.cos(shoulderAngle2a);     // calc new arm length to feed to the next bit of code below

                // ****************

                // calculate arm length based on upper/lower length and elbow and shoulder angle
                shoulderAngle1a = (Math.pow(upperLength, 2) + Math.pow(z2, 2) - Math.pow(lowerLength, 2)) / (2 * upperLength * z2);
                shoulderAngle1 = Math.acos(shoulderAngle1a);     // radians
                elbowAngle = Math.PI - (shoulderAngle1 * 2);       // radians

                // calc degrees from angles
                shoulderAngleDegrees = shoulderAngle1 * (180 / Math.PI);    // degrees
                elbowAngleDegrees = elbowAngle * (180 / Math.PI);              // degrees

                // calc milliseconds PWM to drive the servo.
                shoulderPos = Range.scale(shoulderAngleDegrees, -360, 360, -1, 1);
                elbowPos = Range.scale(elbowAngleDegrees, -360, 360, -1, 1);

                // *** end of Inverse Kinematics ***


                // write to servos, remove 45' and 90' offsets from arm default position
                servoHandRight.setPosition(shoulderPos - shoulderPos2);    // hand
                serverHandLeft.setPosition(shoulderPos - shoulderPos2);    // hand

                servoClaw.setPosition(Math.abs(elbowPos));    // claw


            }
        }
    }
}
